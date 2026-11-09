# Petits Jeux — document de design

Appli Android **sans pub, sans réseau**, pour les enfants : un menu principal où l'on choisit **qui joue** (5 sauvegardes
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
- **Zéro pub, zéro suivi, zéro permission réseau.** Seule permission : vibration (retour tactile, désactivable).
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

## Parking Malin : règles

- Un parking = une grille (de 4×5 à 7×9 cases) ; des **voitures** (2 cases : berline, citadine, sportive), des **camions** (3 cases : pick-up, camion de livraison, fourgon)
  et, à partir du monde 3, des **bus et poids lourds** (4 cases : autobus, camion-citerne), larges d'une case, garés dans les 4 directions.
  Le **nez** (phares blancs, grand pare-brise ; l'arrière a une petite lunette teintée) montre où ils roulent.
- **Toucher une voiture la fait rouler tout droit, dans la direction de son nez.**
  - Si la voie est libre jusqu'au bord du plateau, elle **sort** et disparaît : bravo.
  - Sinon elle avance jusqu'à toucher l'autre voiture, **klaxonne (« Pouet ! »)**, devient rouge un instant et recule : c'est une **faute**.
- On gagne quand **toutes** les voitures sont sorties. Pas de chronomètre, pas de limite de coups.
- Les **plots** et le panneau **P** ne sont que du décor (jamais dans la voie d'une voiture).
- **Étoiles** d'un niveau : 0 faute = 3 ★ ; 1 ou 2 fautes = 2 ★ ; 3 fautes ou plus = 1 ★. L'indice ne coûte rien.
  Un niveau déjà réussi garde sa meilleure note.
- **Indice** : entoure une voiture qui peut sortir tout de suite (la première de l'ordre qui mène à la solution).
- **Recommencer** : remet le niveau à zéro (fautes comprises).

### Niveaux

100 niveaux en 5 mondes de 20, **générés de façon déterministe** (le niveau *n* est toujours le même) et **toujours solubles**.

| Monde | Nom | Grille | Voitures | Décor |
|---|---|---|---|---|
| 1 | Le petit parking | 4×5 → 5×6 | 3 → 8 (voitures, puis camions dès le niveau 4) | asphalte bleu-gris |
| 2 | Le centre-ville | 5×6 → 6×7 | 8 → 12 | asphalte chaud |
| 3 | Le centre commercial | 6×7 → 6×8 | 12 → 16 (+ bus et poids lourds) | asphalte vert |
| 4 | L'aéroport | 6×8 → 7×9 | 15 → 20 | asphalte violet |
| 5 | Le grand garage | 7×9 | 18 → 24 | asphalte sombre |

**Pourquoi c'est toujours soluble** : une voiture ne fait que rouler droit vers le bord et, une fois sortie, ne revient plus. Elle peut donc sortir
si et seulement si **aucune voiture restante** n'est dans sa voie. Le graphe « A attend que B sorte » doit être **sans cycle** : le générateur
n'ajoute une voiture que si le graphe reste sans cycle, et un test vérifie les 100 niveaux (et que le niveau *n* ne change jamais).
La **difficulté** vient de la longueur de la plus longue chaîne d'attente et du peu de voitures libres au départ.

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
- **Sons** (synthétisés, aucun fichier ; coupables) : un « vroum » de moteur qui monte en régime avec le souffle de passage (3 variantes à tour de rôle), un choc puis un klaxon « pouet » à la faute,
  une fanfare en cloches avec réverbération à la victoire, un « ding » plus aigu à chaque étoile (do, mi, sol), un « tin-tin » pour l'indice, un petit « pop » sur les boutons.
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

Le moteur de Parking Malin est du Kotlin pur, testé sur la JVM : règles de sortie et de faute, solveur, **les 100 niveaux solubles et stables**.
L'écriture de la sauvegarde est testée sur un dossier temporaire. L'interface est jouée sans téléphone (Robolectric) : `make android-screenshots` crée un joueur, finit un niveau, vérifie la sauvegarde et rend les captures de `docs/screenshots/`.

## Hors périmètre (pour l'instant)

Publicités, achats, compte en ligne, classement, publication sur le Play Store (l'appli se distribue en APK signé sur GitHub : voir
[`android/PUBLICATION.md`](../android/PUBLICATION.md)), tablette en paysage.
