# Petits Jeux

**Des petits jeux pour les enfants, sur Android. Sans pub, sans compte, sans suivi.**

Petits Jeux réunit plusieurs jeux simples dans une seule appli. Au lancement, chacun choisit **qui joue** (jusqu'à 5 joueurs sur le même téléphone,
chacun avec son prénom, son animal, ses étoiles et sa progression), puis **le jeu**. Les gros boutons et le peu de texte sont faits pour les petites mains,
et on ne « perd » jamais : une faute coûte une étoile, jamais la partie.

| Qui joue ? | Les jeux du joueur | Parking Malin | Champis Magiques |
|---|---|---|---|
| ![Qui joue ?](docs/screenshots/01-choix-joueur.png) | ![Accueil](docs/screenshots/03-accueil.png) | ![Parking Malin](docs/screenshots/07-jeu.png) | ![Champis Magiques](docs/screenshots/44-champis-jeu.png) |

| Poussin Traverse : la petite rue | Poussin Traverse : une grande route, de nuit |
|---|---|
| ![Petite rue](docs/screenshots/54-route-jeu.png) | ![Grande route](docs/screenshots/55-route-grande-route.png) |

## Les jeux

### Parking Malin

Des voitures, des camions et des bus sont garés serrés. On les **glisse du doigt** pour les faire sortir un par un, dans le bon ordre : si la voie n'est pas libre,
le véhicule klaxonne (« Pouet ! ») et revient. 180 niveaux, tous faisables, où le parking s'agrandit et où une nouvelle idée arrive tous les 20 niveaux :
marche arrière, plots de chantier, murs, caisses qui tombent des camions, taches d'huile, sens uniques, tapis roulants, téléporteurs, barrières de péage.
Pas de chronomètre, et un bouton **Indice** gratuit.

### Champis Magiques

Dans une forêt, des champignons s'allument : il faut les **toucher le plus vite possible**. Plus on est rapide, plus on marque de points, et on finit toujours
la partie avec au moins une étoile. Quatre difficultés à débloquer, jusqu'à quatre champignons allumés en même temps et même une couleur interdite.
Chaque champignon touché joue la note suivante d'une petite mélodie.

### Poussin Traverse

En paysage. Un poussin doit **traverser la route** : un toucher le fait sauter vers l'avant, un geste du doigt le fait sauter de côté ou en arrière.
Il faut regarder passer les voitures, les scooters, les tracteurs, les bus, les ambulances et même les trains, et choisir son moment.
Des routes de plus en plus larges sur 48 niveaux. Écrasé, le poussin perd quelques points et repart du dernier refuge.

### Et ensuite

Les étoiles gagnées dans tous les jeux font monter le **rang** du joueur, de « Petite voiture » à « Super champion ». D'autres jeux viendront.

## Installer

Android 8 ou plus. L'appli se distribue en **APK signé**, sans passer par Google Play.

1. Sur le téléphone, télécharger la **[dernière version](https://github.com/kcharignon/mobile-game/releases/download/android-latest/petitsjeux.apk)**.
2. Ouvrir le fichier téléchargé. Si Android le demande, autoriser l'installation depuis le navigateur, puis toucher **Installer**
   (si Play Protect hésite : *Installer quand même*).

**Les mises à jour se font dans l'appli** : quand une nouvelle version existe, un gros bouton vert « Mettre à jour » apparaît dès le premier écran. Les joueurs et leurs étoiles sont conservés.
Toutes les versions sont sur la page des [releases](https://github.com/kcharignon/mobile-game/releases).

## Vie privée

Petits Jeux est fait pour des enfants : **il ne collecte rien**.

- **Aucune donnée collectée.** Pas de compte, pas de publicité, pas de statistiques d'usage, aucun outil de suivi ni service tiers dans l'appli.
- **Tout reste sur le téléphone.** Les prénoms, les animaux, les étoiles et la progression sont dans des fichiers privés de l'appli. Ils ne sont envoyés nulle part et
  disparaissent si l'on désinstalle l'appli. Si la sauvegarde automatique d'Android est activée sur le téléphone, c'est elle (et non l'appli) qui peut en garder une copie
  dans le compte Google de son propriétaire.
- **Internet sert uniquement à la mise à jour.** Au lancement, l'appli demande à `github.com` s'il existe une nouvelle version (un petit fichier public), sans envoyer
  d'identifiant ni d'information sur le joueur. Comme pour tout téléchargement, GitHub voit l'adresse IP de la connexion.
- **Permissions demandées** : la vibration (désactivable dans les réglages), Internet et l'installation d'applications, uniquement pour le bouton « Mettre à jour ».

Le code est public : tout cela peut se vérifier.

## Pour aller plus loin

Règles détaillées des jeux, sauvegarde et architecture : [`docs/DESIGN.md`](docs/DESIGN.md). Compiler, tester et publier : [`docs/DEVELOPPEMENT.md`](docs/DEVELOPPEMENT.md).

## Mentions

Projet personnel. Les véhicules, avatars, décors, la mélodie et presque tous les sons sont des créations originales faites par le code ; seuls les quatre klaxons
viennent de [Mixkit](third_party/mixkit/README.md) (licence Mixkit Sound Effects). Police : [Fredoka](third_party/fredoka/OFL.txt) (licence SIL OFL).
