# Petits Jeux — développer

Tout tourne dans Docker : aucun JDK ni SDK Android à installer, seulement Docker.

```bash
make android               # APK de débogage : dist/petitsjeux-debug.apk (le premier lancement télécharge le SDK et Gradle : plusieurs minutes)
make android-test          # tests Kotlin : règles, solveurs, solubilité des 180 niveaux de parking et des 48 traversées, sauvegardes, mise à jour
make android-screenshots   # rend les écrans en PNG (Robolectric, sans téléphone) dans docs/screenshots/ et joue de vrais parcours
make android-release-selftest  # prouve la chaîne de publication signée avec une clé jetable
make design                # régénère les maquettes SVG (PNG : node tools/design/render.js, demande Playwright)
make mazes                 # régénère les 100 niveaux de Poisson Perdu (res/raw/maze_levels.json) et leur planche-contact ; `node tools/mazes/make-mazes.js --check` vérifie le fichier
make help                  # toutes les commandes
```

Chaîne : JDK 17 en conteneur, Gradle 8.14, plugin Android 8.13, Kotlin 2.2, Jetpack Compose (BOM 2025.03).
Dependabot ne propose aucune montée majeure (`.github/dependabot.yml`) : monter la chaîne est un chantier à faire d'un bloc, à la main.
Sans Docker : ouvrir `android/` dans Android Studio (JDK 17).

## Organisation du dépôt

```
android/app/src/main/java/com/kcharignon/petitsjeux/
  core/save      joueurs, sauvegarde JSON atomique (5 emplacements), rangs
  core/ui        thème, boutons, étoiles, avatars : le kit commun à tous les jeux
  core/screens   « Qui joue ? », accueil des jeux, réglages
  core/          MiniGame (contrat d'un jeu), registre des jeux, navigation, sons et vibrations
  games/parking  Parking Malin : engine/ (règles pures), ui/ (plateau, voitures, animations), progression
  games/mushrooms  Champis Magiques : engine/ (difficultés, score, partie, forêt), ui/ (forêt, champignons, écrans), progression
  games/maze       Poisson Perdu : engine/ (niveaux lus du fichier, partie, indice), ui/ (récif, tracé au doigt, écrans), progression
  games/road       Poussin Traverse : engine/ (véhicules, traversée, robot de preuve, 48 niveaux), ui/ (route, véhicules, poussin, écrans en paysage), progression
android/app/src/test/    tests Kotlin ; screenshots/ = interface rendue par Robolectric (sans téléphone)
docs/                    DESIGN.md, maquettes (design/), captures du README (screenshots/)
tools/mazes/             l'outil qui fabrique à l'avance les 100 niveaux de Poisson Perdu (make-mazes.js, déterministe)
tools/design/            générateurs (Node, sans dépendance) : maquettes, icône, les véhicules (vehicles.js -> VehicleArt.kt généré + aperçu), et le récif de Poisson Perdu (recif.js, make-recif.js)
.github/                 workflows : tests et APK de débogage, publication signée
```

**Ajouter un jeu** : implémenter `MiniGame` (`core/MiniGame.kt`) dans `games/<nom>/` et l'ajouter à `Games.all`. Le menu, la sauvegarde par joueur et le rang suivent tout seuls.

Règles complètes des jeux, sauvegarde et architecture : [`DESIGN.md`](DESIGN.md) ; maquettes : [`design/`](design/).

## Publier une version

Procédure complète (clé de signature, secrets GitHub, étiquette) : [`../android/PUBLICATION.md`](../android/PUBLICATION.md). En bref, une fois la clé et ses secrets en place :

```bash
git tag android-v0.2.0 && git push origin android-v0.2.0
```

Le workflow [`android-release.yml`](../.github/workflows/android-release.yml) construit, teste, signe, **contrôle** (certificat attendu, version, permissions, APK non débogable),
publie la release et met à jour `android-latest`. Les pré-versions `android-dev-…` (APK de débogage) sont signées d'une clé publique : ne pas les installer à la place d'une vraie version.
