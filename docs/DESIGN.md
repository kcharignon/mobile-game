# Petits Jeux — document de design

Appli Android **sans pub et sans suivi**, pour les enfants : un menu principal où l'on choisit **qui joue** (5 sauvegardes
locales distinctes), puis **le jeu**. Premier jeu : **Parking Malin** (des voitures garées, à faire sortir une par une dans le bon ordre).
D'autres jeux viendront : le menu et la sauvegarde sont faits pour ça.

Maquettes : [`docs/design/`](design/) (SVG générés par `tools/design/`, PNG rendus à côté). Elles sont la référence visuelle ;
le Kotlin reprend les mêmes proportions.

| Écran | Maquette |
|---|---|
| Planche de style (voitures, couleurs, composants) | [`00-planche-style.png`](design/00-planche-style.png) |
| Choix du joueur (5 emplacements) | [`01-choix-joueur.png`](design/01-choix-joueur.png) |
| Nouveau joueur (prénom + animal) | [`02-nouveau-joueur.png`](design/02-nouveau-joueur.png) |
| Accueil : les jeux du joueur | [`03-accueil-jeux.png`](design/03-accueil-jeux.png) |
| Choix du niveau | [`04-niveaux.png`](design/04-niveaux.png) |
| Jeu, faute (« Pouet ! »), indice | [`05-jeu.png`](design/05-jeu.png), [`06-jeu-pouet.png`](design/06-jeu-pouet.png), [`08-jeu-indice.png`](design/08-jeu-indice.png) |
| Victoire | [`07-victoire.png`](design/07-victoire.png) |

## Principes

- **Pensé pour un enfant** : gros boutons, peu de texte, jamais de « perdu » : une faute coûte une étoile, pas la partie. Un bouton **Indice** gratuit.
- **Zéro pub, zéro suivi.** Permissions : la vibration (retour tactile, désactivable), et Internet + installation **uniquement pour la mise à jour** de l'appli (voir plus bas).
- **Portrait uniquement**, Android 8+ (API 26), une main.
- **Rapide** : tout est dessiné par le code (Canvas), pas d'image, pas de son à charger. Les sons sont synthétisés.
- **Extensible** : un jeu = un module qui reçoit le joueur courant et sait lire/écrire **sa part** de la sauvegarde.

## Parcours

```
Lancement ─► Choix du joueur ─► Accueil (jeux) ─► Choix du niveau ─► Jeu ─► Victoire ─► niveau suivant
                 │  ▲                  │
                 │  └──── « Changer de joueur »
                 └─► Nouveau joueur (prénom + animal)      engrenage : son, vibration, effacer ce joueur
```

- Au lancement on arrive **toujours** sur le choix du joueur (appareil partagé entre frères et sœurs).
- Un emplacement vide propose « Nouveau joueur » ; un emplacement plein se touche pour jouer. Modifier / effacer : engrenage de l'accueil
  (effacer demande une confirmation à deux temps).
- Le bouton retour du téléphone remonte d'un écran ; sur le choix du joueur il quitte l'appli.

## Mise à jour de l'appli (sans rien connaître à un « APK »)

- Au lancement, l'appli regarde discrètement s'il existe une version plus récente : une seule petite requête HTTPS vers `github.com` (le fichier `version.json` de la release
  `android-latest`), sans identifiant ni envoi de données. Pas de réseau ? Elle ne dit rien.
- S'il y en a une, un **gros bouton vert « Nouvelle version ! Mettre à jour »** apparaît sur l'accueil. Un seul geste : l'appli télécharge, **vérifie l'empreinte SHA-256**
  du fichier (celle de `version.json`), puis ouvre la fenêtre « Installer » d'Android.
- **La première fois seulement**, Android demande d'autoriser Petits Jeux à installer des mises à jour : un bouton orange « Autoriser » explique quoi faire, et l'installation
  continue toute seule au retour dans l'appli.
- Réglages > « Chercher une mise à jour » fait la même chose à la demande, et affiche le numéro de la version installée.
- **Sécurité** : seules les adresses `https://github.com/kcharignon/mobile-game/releases/download/…` sont acceptées, et Android **refuse** toute mise à jour qui ne serait pas
  signée de la même clé : personne d'autre ne peut pousser une mise à jour. Les joueurs et leurs étoiles sont conservés.
- Code : `core/update/` (machine d'états testée sans téléphone, réseau testé contre un petit serveur local), interface dans `core/screens/UpdateUi.kt`.

## Parking Malin : règles

- Un parking = une grille (de 4×5 à 8×10 cases) ; des **voitures** (2 cases : berline, citadine, sportive), des **camions** (3 cases : pick-up, camion de livraison, fourgon)
  et, à partir du monde 3, des **bus et poids lourds** (4 cases : autobus, camion-citerne), larges d'une case, garés dans les 4 directions.
  Le **nez** (phares blancs, grand pare-brise ; l'arrière a une petite lunette teintée) montre le sens de la **marche avant**.
- **Chaque véhicule peut rouler en marche avant ou en marche arrière, selon le sens où on le glisse.**
  - **Glisser** un véhicule le long de sa longueur, dans un sens ou dans l'autre : il roule tout droit dans ce sens.
  - Un simple **toucher** le fait avancer (marche avant) ; un glissement en travers ne fait rien.
  - Si la voie est libre jusqu'au bord du plateau **dans ce sens**, il **sort** et disparaît : bravo.
  - Sinon il avance jusqu'à toucher l'obstacle, **klaxonne** (« Pouet ! » : un vrai klaxon, de voiture, de camion ou de bus selon le véhicule), devient rouge un instant et revient : c'est une **faute**.
- On gagne quand **tous** les véhicules sont sortis. Pas de chronomètre, pas de limite de coups.
- **Étoiles** d'un niveau : 0 faute = 3 ★ ; 1 ou 2 fautes = 2 ★ ; 3 fautes ou plus = 1 ★. L'indice ne coûte rien.
  Un niveau déjà réussi garde sa meilleure note.
- **Indice** : entoure un véhicule qui peut sortir et fait filer deux **chevrons** dans le sens où il faut le glisser. Il ne conseille jamais un coup qui mène à une impasse.
- **Recommencer** : remet le niveau à zéro (fautes comprises).

### Une mécanique de plus tous les 60 niveaux

| Niveaux | Mécanique | Ce qui change |
|---|---|---|
| 1 à 60 | **Marche avant et arrière** | Le sens du glissement choisit le sens du véhicule. Les premiers niveaux obligent à reculer (le niveau 2 le montre). |
| 61 à 120 | **Plots de chantier** | Un plot occupe une case pour de bon : on ne passe pas dessus, ni devant, ni derrière. De 1 à 5 plots. |
| 121 à 180 | **Parking agrandi** | La grille grandit jusqu'à 8×10 cases, avec plus de véhicules (et encore des plots). |
| 181 à 240 | **Le pick-up laisse tomber une caisse** | En partant, un pick-up (celui qui a une caisse dans sa benne) laisse tomber sa caisse **derrière lui**, toujours sur la **même case : celle que sa queue occupait**. Elle bloque cette case pour de bon. De 1 à 3 pick-up. |

Le premier niveau de chaque groupe (1 à 3, 61, 121, 181) affiche un petit mot qui explique la nouveauté.

**La caisse** : le pick-up peut partir dans les deux sens, mais la caisse ne tombe pas au même endroit : si le pick-up part vers l'avant, elle tombe sur la case de sa queue ;
s'il recule, elle tombe sur la case de son nez (le bout qui reste « derrière » dans ce sens). Choisir le bon sens, et le bon moment, est le casse-tête. Si une caisse ferme le dernier
passage, l'écran **« Oups, bloqué ! »** propose de **recommencer** (sans punition).

### Niveaux

**240 niveaux** en 12 mondes de 20 (4 groupes de 3 mondes), **générés de façon déterministe** (le niveau *n* est toujours le même) et **toujours solubles**.

| Groupe | Mondes | Grille | Véhicules | Plots | Pick-up à caisse |
|---|---|---|---|---|---|
| 1 (1-60) | Le petit parking, Le centre-ville, Le centre commercial | 4×5 → 6×8 | 3 → 16 (camions dès le niveau 4, bus dès le niveau 41) | 0 | 0 |
| 2 (61-120) | Les travaux, Le chantier, Le tunnel | 6×7 → 7×9 | 12 → 20 | 1 → 5 | 0 |
| 3 (121-180) | Le grand parking, L'aéroport, La gare | 7×9 → 8×10 | 18 → 24 | 3 → 7 | 0 |
| 4 (181-240) | Le port, L'entrepôt, Le grand garage | 8×10 | 20 → 25 | 4 → 8 | 1 → 3 |

**Pourquoi c'est toujours soluble** : faire sortir un véhicule libère toujours de la place. Sans caisse, le niveau est donc soluble si et seulement si on peut tout vider en retirant à chaque fois un véhicule qui a **une voie libre dans un sens ou dans l'autre**
(vers le bord, sans véhicule ni plot ni caisse). Les pick-up qui laissent tomber une caisse **ajoutent** un obstacle : seuls eux demandent de chercher le bon ordre, et ils sont peu nombreux, donc la recherche du solveur
(`Solver`) est minuscule. Le générateur ne garde qu'un niveau que le solveur résout, un test vérifie les 240 niveaux en suivant les indices, et un autre que le niveau *n* ne change jamais (empreinte).
La **difficulté** vient du nombre de « vagues » de sorties (la longueur de la plus longue suite de dépendances) et du peu de véhicules libres au départ ; dans le dernier groupe, on cherche aussi des **pièges**
(un coup qui paraît bon mais mène à l'impasse).

Le niveau suivant se débloque quand le précédent est réussi.

## Progression du joueur (« l'évolution »)

- **Étoiles** cumulées (somme des meilleures notes) → **rang**, affiché partout où le joueur apparaît :

| Étoiles | Rang |
|---|---|
| 0 | Petite voiture |
| 10 | Premier tour de roue |
| 30 | Moteur chaud |
| 60 | As du créneau |
| 120 | Super turbo |
| 200 | Génie du parking |
| 300 | Légende du parking |
| 450 | Étoile du bitume |
| 620 | Légende absolue |

- Statistiques gardées : voitures sorties, fautes, niveaux réussis, plus haut niveau atteint.
- Le choix du **joueur** : un prénom (12 caractères) et un **animal** parmi 8 (chat, chien, ours, panda, lapin, grenouille, renard, lion), dessinés par le code.

## Sauvegarde locale

- **5 emplacements** (0 à 4), un fichier JSON par emplacement dans le stockage **privé** de l'appli : `files/saves/slot-<n>.json`.
- Écriture **atomique** (fichier temporaire puis renommage) : une coupure en pleine écriture ne corrompt jamais la sauvegarde.
- Un fichier illisible est mis de côté (`slot-<n>.json.corrupt`) et l'emplacement apparaît vide : l'appli ne plante jamais.
- Chaque jeu possède **sa part** sous `games.<id>` ; le reste du fichier (joueur, réglages) est commun. Un jeu n'écrit jamais dans la part d'un autre.
- `"v"` = version du format, pour pouvoir migrer plus tard sans perdre les sauvegardes des joueurs.
- La sauvegarde survit aux mises à jour de l'APK (même signature, même identifiant d'appli). Elle est supprimée si on désinstalle l'appli.

```json
{
  "v": 1,
  "slot": 0,
  "name": "Léa",
  "avatar": "chat",
  "created": 1762650000000,
  "lastPlayed": 1762690000000,
  "settings": { "sound": true, "vibration": true },
  "games": {
    "parking": {
      "stars": { "1": 3, "2": 2, "3": 3 },
      "carsFreed": 128,
      "bumps": 9,
      "played": 31
    }
  }
}
```

## Système visuel

- **Fond** jaune soleil dégradé (comme l'icône de référence), grands cercles clairs en filigrane.
- **Typographie** : Fredoka (graisse 500-700), arrondie et lisible ; titres blancs cernés d'encre.
- **Panneaux** crème à « marche » (ombre pleine en dessous), **boutons** épais à reflet : vert = action principale, bleu = secondaire,
  orange = indice, gris = verrouillé.
- **Voitures** vues de dessus, **toit, pare-brise, phares, feux, roues** ; une **tranche sombre** est toujours dessinée vers le bas de l'écran
  (même quand la voiture tourne) pour donner du volume sans 3D. 9 couleurs (vert, orange, cyan, bleu, rose, jaune, violet, beige, rouge).
- **Plateau** d'asphalte à damier discret, petits traits de parking, **plots** orange et panneau **P** autour.
- **Animations** : sortie en accélérant puis fondu ; faute = avance, rebond, rougeur + bulle « Pouet ! » ; victoire : une pause de 800 ms sur le plateau vide, puis confettis et étoiles qui s'allument une à une.
- **Sons** (coupables) : de **vrais klaxons** enregistrés à la faute (petit klaxon ou klaxon de voiture pour une voiture, klaxon classique pour un camion, double klaxon pour un bus ; `res/raw`, voir `third_party/mixkit`),
  et le reste synthétisé, sans fichier : un « vroum » de moteur qui monte en régime avec le souffle de passage (3 variantes à tour de rôle), une fanfare en cloches avec réverbération à la victoire, un « ding » plus aigu à chaque étoile (do, mi, sol), un « tin-tin » pour l'indice, un petit « pop » sur les boutons.
  Toutes les notes sont en do majeur : elles s'accordent entre elles. **Vibration** légère sur faute et victoire.

## Architecture de l'appli

```
core/save      Profile, ProfileStore (JSON, 5 emplacements, écriture atomique), rangs
core/ui        thème (Fredoka), boutons, étoiles, avatars, fond, fenêtres : le « kit » commun à tous les jeux
core/screens   « Qui joue ? », accueil des jeux, réglages
core/          MiniGame (contrat d'un jeu), registre des jeux, navigation (PetitsJeuxApp), sons et vibrations (Feedback)
games/parking/engine   règles pures (aucune dépendance Android) : grille, voitures, tap, attentes, générateur, étoiles
games/parking/ui       plateau, voitures, animations, écrans de niveaux et de jeu
```

Le moteur de Parking Malin est du Kotlin pur, testé sur la JVM : règles de sortie et de faute, solveur, **les 240 niveaux solubles et stables**.
L'écriture de la sauvegarde est testée sur un dossier temporaire. L'interface est jouée sans téléphone (Robolectric) : `make android-screenshots` crée un joueur, finit un niveau, vérifie la sauvegarde et rend les captures de `docs/screenshots/`.

## Hors périmètre (pour l'instant)

Publicités, achats, compte en ligne, classement, publication sur le Play Store (l'appli se distribue en APK signé sur GitHub : voir
[`android/PUBLICATION.md`](../android/PUBLICATION.md)), tablette en paysage.
