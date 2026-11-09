# Publier Petits Jeux : l'appli Android dans une release GitHub

Procédure **pour toi**, une étape à la fois. Les étapes 1 à 3 ne servent **qu'une fois** ; ensuite, publier une version, c'est l'étape 4.

## Ce que GitHub fournit

| Quoi | Où | Comment |
|---|---|---|
| L'**appli Android** signée | GitHub **Releases** : `https://github.com/kcharignon/mobile-game/releases` | [`android-release.yml`](../.github/workflows/android-release.yml), quand tu pousses une étiquette `android-vX.Y.Z` |
| Une adresse **fixe** vers la dernière version | release « `android-latest` » : `…/releases/download/android-latest/petitsjeux.apk` | mise à jour par le même workflow ; c'est le lien « dernière version » du README |
| Des versions d'**essai** (débogage) | pré-versions `android-dev-<date>` | [`android.yml`](../.github/workflows/android.yml), lancé à la main |

> **Dépôt public ou privé ?** Le téléchargement de l'APK depuis le téléphone est gratuit et sans connexion sur un dépôt **public**. Sur un dépôt privé,
> l'APK ne se télécharge qu'une fois connecté à GitHub.

## Ce qu'il faut savoir avant de commencer

- La version de production est un **fichier `.apk` signé avec ta clé**, installé à la main, sans Google Play. Les mises à jour se font en installant le nouveau fichier **par-dessus**.
- **Les sauvegardes des joueurs survivent aux mises à jour** (même clé de signature, même identifiant `com.kcharignon.petitsjeux`). Elles sont perdues si on désinstalle l'appli ou si on change de clé.
- **Ta clé de signature est la chose la plus importante** : si tu la perds, plus aucune version ne peut s'installer par-dessus l'ancienne (il faudrait désinstaller, donc perdre les sauvegardes). Fais **deux copies** (étape 1).
- **Jamais de clé ni de mot de passe dans un commit** (`*.p12`, `*.jks` et `*.keystore` sont ignorés par Git, sauf la clé de débogage publique `android/debug.keystore`).
- Les pré-versions `android-dev-…` sont signées d'une **clé de débogage publique** : ne les installe pas à la place d'une version `android-vX.Y.Z`.
- Seule l'étiquette `android-vX.Y.Z` déclenche la publication ; le workflow refuse de publier sans clé, hors de `main`, ou avec un numéro plus ancien que le dernier publié.

## Étape 1. La clé de signature (une fois)

Elle a été créée avec le JDK de Docker, **hors du dépôt**, dans `~/petitsjeux-cle/` :

- `petitsjeux-publication.p12` : la clé (alias `petitsjeux`, RSA 4096, valable 10 000 jours) ;
- `mot-de-passe.txt` : son mot de passe (le même pour le fichier et pour la clé).

**À faire maintenant** : copier ces deux fichiers à **deux endroits qui ne sont pas ce PC**, et mettre le mot de passe dans ton gestionnaire de mots de passe.
Pour la refaire (par exemple pour un autre projet) :

```bash
mkdir -p ~/petitsjeux-cle && chmod 700 ~/petitsjeux-cle
docker run --rm -it --user "$(id -u):$(id -g)" -v "$HOME/petitsjeux-cle:/cle" eclipse-temurin:17-jdk-jammy \
  keytool -genkeypair -keystore /cle/petitsjeux-publication.p12 -storetype PKCS12 \
  -alias petitsjeux -keyalg RSA -keysize 4096 -validity 10000 -dname "CN=PetitsJeux"
chmod 600 ~/petitsjeux-cle/petitsjeux-publication.p12
```

Ne mets ni ton nom ni ton e-mail dans `-dname` : ce texte est public (il est dans l'application).

## Étape 2. L'empreinte publique du certificat (fait)

L'empreinte n'a rien de secret : le workflow **refuse de publier** si l'APK n'est pas signé par la clé qui y correspond. Elle est dans
[`release-cert.sha256`](release-cert.sha256) (une empreinte par ligne ; plusieurs lignes permettent de changer de clé plus tard). Pour la recalculer :

```bash
docker run --rm -it -v "$HOME/petitsjeux-cle:/cle:ro" eclipse-temurin:17-jdk-jammy \
  keytool -list -v -keystore /cle/petitsjeux-publication.p12 -alias petitsjeux | awk '/SHA256:/ {print $2; exit}'
```

## Étape 3. Poser les quatre secrets dans GitHub (une fois)

GitHub chiffre les secrets : une fois posés, personne ne peut les relire, seul le workflow les utilise. Avec `gh` connecté (`gh auth status`), le script
[`tools/set-release-secrets.sh`](../tools/set-release-secrets.sh) lit `~/petitsjeux-cle/` et pose les quatre secrets sans jamais afficher une valeur :

```bash
tools/set-release-secrets.sh          # ou : tools/set-release-secrets.sh /autre/dossier/de/cle
gh secret list                        # les quatre noms, jamais les valeurs
```

À la main : `Settings > Secrets and variables > Actions > New repository secret` : `ANDROID_KEYSTORE_BASE64` (le `.p12` en base64 : `base64 -w0 petitsjeux-publication.p12`),
`ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_PASSWORD` (le mot de passe) et `ANDROID_KEY_ALIAS` (`petitsjeux`).

Facultatif : `Settings > Environments > android-release` (créé au premier passage du workflow) permet d'exiger une approbation avant chaque publication.

## Étape 4. Publier une version (à chaque version)

Le numéro vient de l'étiquette : `android-v1.2.3` donne `versionName 1.2.3` et le code de version `1002003`. Il ne doit jamais baisser.

```bash
git switch main && git pull
git tag android-v0.1.0
git push origin android-v0.1.0
```

Le workflow construit, teste, signe, **contrôle** (certificat attendu, version, permissions, APK non débogable), puis publie la release `android-v0.1.0` et met à jour `android-latest`. Suis-le avec `gh run watch`.

**Vérifier** : la release `android-v0.1.0` contient `petitsjeux-0.1.0.apk`, `petitsjeux.apk`, leurs `.sha256` et `version.json` ; elle n'est ni brouillon ni pré-version. `sha256sum -c petitsjeux-0.1.0.apk.sha256` doit répondre `OK`.

## Étape 5. Installer, puis mettre à jour

1. Sur le téléphone, ouvre le lien « dernière version » du README (<https://github.com/kcharignon/mobile-game/releases/download/android-latest/petitsjeux.apk>) ou la page des [releases](https://github.com/kcharignon/mobile-game/releases) pour choisir une version (`petitsjeux-X.Y.Z.apk`).
2. Ouvre le fichier téléchargé, autorise l'installation depuis Chrome si Android le demande, touche **Installer** (si Play Protect hésite : **Installer quand même**).
3. **Mettre à jour** : même geste avec la version suivante, par-dessus. Les joueurs, leurs étoiles et leurs niveaux sont conservés.

## Ce dont la mise à jour dans l'appli dépend (à ne pas casser)

- La release **`android-latest`** et son fichier **`version.json`** : le workflow les publie à chaque version (versionName, versionCode, apkUrl, sha256). L'appli lit `https://github.com/kcharignon/mobile-game/releases/download/android-latest/version.json`.
- L'adresse du dépôt : l'appli n'accepte que des APK sous `https://github.com/kcharignon/mobile-game/releases/download/` (`ReleaseInfo.OFFICIAL_PREFIX`). Si le dépôt change de nom ou de propriétaire, changer cette constante **et** publier une version avec l'ancienne adresse pour que les téléphones déjà installés trouvent la suite.
- Le **numéro de version ne doit jamais baisser** (l'appli propose une mise à jour seulement si `versionCode` est plus grand) et la **clé de signature ne doit jamais changer** (Android refuserait l'installation).
- Permissions déclarées (et seules autorisées par le contrôle de l'APK) : vibration, `INTERNET` et `REQUEST_INSTALL_PACKAGES`, uniquement pour cette mise à jour.

## Versions d'essai (débogage)

Pour essayer une version avant de la publier : `gh workflow run android.yml --ref main` (éventuellement `-f tag=android-dev-2026-11-09b`). Elle publie une **pré-version** `android-dev-<date>` avec `petitsjeux-debug.apk`. Ne l'installe pas sur le téléphone que tu utilises pour jouer vraiment : elle est signée d'une clé publique, et **une version signée d'une autre clé ne s'installe pas par-dessus** (il faudrait désinstaller, donc perdre les sauvegardes).

## Essayer la chaîne en local, sans GitHub

```bash
make android-release-selftest   # refus sans clé, réussite avec une clé jetable créée à l'instant, refus du contrôle : rien n'est conservé
```

Une vraie construction locale : `ANDROID_KEYSTORE_BASE64=… ANDROID_KEYSTORE_PASSWORD=… ANDROID_KEY_ALIAS=… ANDROID_KEY_PASSWORD=… ANDROID_VERSION_TAG=android-v0.1.0 make android-release`, puis `ANDROID_VERSION_TAG=android-v0.1.0 ANDROID_EXPECTED_CERT_SHA256="$(paste -sd, android/release-cert.sha256)" make android-release-verify`.

## Si ça ne marche pas

- **« Pas de clé de signature »** : un des quatre secrets manque (`gh secret list`). Rien n'a été construit.
- **« release-cert.sha256 manque »** ou **« mal formé »** : étape 2.
- **« une étiquette plus récente existe déjà »** : un numéro ne se republie pas ; choisis-en un plus grand.
- **« le commit de l'étiquette n'est pas dans main »** : fusionne dans `main` avant d'étiqueter.
- **« certificat inattendu »** : l'APK n'est pas signé de la clé de `release-cert.sha256` ; vérifie le secret `ANDROID_KEYSTORE_BASE64`.
- **L'installation est refusée** : l'appli installée est signée d'une autre clé (version d'essai `android-dev-…`) ; la désinstaller (les sauvegardes sont perdues), puis recommencer.
