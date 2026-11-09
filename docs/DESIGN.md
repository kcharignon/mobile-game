# Petits Jeux — document de design

Appli Android **sans pub et sans suivi**, pour les enfants : un menu principal où l'on choisit **qui joue** (5 sauvegardes
locales distinctes), puis **le jeu**. Trois jeux : **Parking Malin** (des voitures garées, à faire sortir une par une dans le bon ordre), **Champis Magiques** (des champignons s'allument, à toucher vite) et **Poussin Traverse** (traverser des routes de plus en plus larges, en paysage).
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
- **Portrait** partout, une main, Android 8+ (API 26) ; **sauf Poussin Traverse, qui force le paysage** (voir plus bas).
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
  `android-latest`), sans identifiant ni envoi de données. Pas de réseau ? Elle ne dit rien, **mais elle recommence chaque fois qu'on revient dans l'appli** (si la dernière recherche a échoué, ou date de plus de 10 minutes) : un téléphone sans réseau au lancement n'empêche plus d'être prévenu.
- S'il y en a une, un **gros bouton vert « Nouvelle version ! Mettre à jour »** apparaît **dès le premier écran (« Qui joue ? »)**, et aussi sur l'accueil du joueur. Le numéro de la version installée est écrit en bas du premier écran. Un seul geste : l'appli télécharge, **vérifie l'empreinte SHA-256**
  du fichier (celle de `version.json`), puis ouvre la fenêtre « Installer » d'Android.
- **La première fois seulement**, Android demande d'autoriser Petits Jeux à installer des mises à jour : un bouton orange « Autoriser » explique quoi faire, et l'installation
  continue toute seule au retour dans l'appli.
- Réglages > « Chercher une mise à jour » fait la même chose à la demande, et affiche le numéro de la version installée.
- **Sécurité** : seules les adresses `https://github.com/kcharignon/mobile-game/releases/download/…` sont acceptées, et Android **refuse** toute mise à jour qui ne serait pas
  signée de la même clé : personne d'autre ne peut pousser une mise à jour. Les joueurs et leurs étoiles sont conservés.
- Code : `core/update/` (machine d'états testée sans téléphone, réseau testé contre un petit serveur local), interface dans `core/screens/UpdateUi.kt`.

## Parking Malin : règles

- Un parking = une grille (de 5×6 à 11×13 cases) ; des **voitures** (2 cases : berline, citadine, sportive), des **camions** (3 cases : pick-up, camion de livraison, fourgon)
  et, à partir du monde 3 (niveau 41), des **bus et poids lourds** (4 cases : autobus, camion-citerne), larges d'une case, garés dans les 4 directions.
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

### Collisions : seulement si les véhicules se touchent vraiment à l'écran

Deux véhicules ne se percutent **que s'ils se toucheraient visuellement**, avec une marge (**0,25 case** de chaque côté) : s'ils se frôlent, ça passe.

- Un véhicule **à l'arrêt** occupe ses cases : si on envoie un autre véhicule dessus, c'est une faute (« Pouet ! », rebond).
- Un véhicule qui **sort** n'occupe que les cases où il est (ou sera) **au moment où l'autre y passe**. Tous les véhicules qui sortent ont la **même accélération** (la distance parcourue ne dépend que du temps écoulé :
  14 × t² cases) : le moteur sait donc, case par case, à quel instant chacun l'occupe, et ne signale une collision que si les deux instants se chevauchent.
  Conséquences : deux véhicules **l'un derrière l'autre** sur la même file partent à la suite sans se heurter (le deuxième arrive toujours après) ; un véhicule peut **traverser devant** un autre qui arrive encore de loin ;
  il n'y a **pas de couloir fantôme** devant un véhicule qui s'éloigne, et un véhicule sorti du plateau ne gêne plus rien.
- Un véhicule qui **rebondit** (il a foncé sur un obstacle) occupe tout ce qu'il balaie tant qu'il fonce, puis seulement l'endroit où il est quand il revient.
- Un véhicule qui rebondit encore ne peut pas être touché une deuxième fois (un enfant impatient ne perd pas deux étoiles d'un coup).

Le moteur lit la position **dessinée** du véhicule (c'est l'animation qui fait foi) : ce qu'on voit est ce qui compte.

### Comment les niveaux évoluent

**Tous les 20 niveaux, le parking s'agrandit ET une nouvelle mécanique arrive.** Les mécaniques **se cumulent** : on garde les anciennes. Chaque tranche de 20 niveaux est un **monde** :

| Niveaux | Monde | Parking | Mécanique ajoutée | Ce qui change |
|---|---|---|---|---|
| 1 à 20 | Le petit parking | 5×6 | **Marche avant et arrière** | Le sens du glissement choisit le sens du véhicule. Les premiers niveaux obligent à reculer (le niveau 2 le montre). |
| 21 à 40 | Les travaux | 6×7 | **Plots de chantier** | Un plot occupe une case pour de bon : on ne passe pas dessus. **1 plot au début**, puis de plus en plus. |
| 41 à 60 | Le château fort | 7×8 | **Murs sur le bord du parking** | Un mur de briques ferme **la sortie d'une file** : le véhicule arrive jusqu'au bord, mais ne passe pas ; il doit sortir par l'autre bout ou attendre. **1 mur au début**, puis de plus en plus. Les bus (4 cases) arrivent aussi. |
| 61 à 80 | Le port | 8×9 | **Le pick-up laisse tomber une caisse** | En partant, un pick-up (celui qui a une caisse dans sa benne) laisse tomber sa caisse **derrière lui**, toujours sur la **même case : celle que sa queue occupait**. Si un véhicule roule ensuite sur cette caisse, il la **détruit et continue sa route**, mais c'est **une faute** (jamais de partie perdue). **1 pick-up au début**, jusqu'à 3. |
| 81 à 100 | La station-service | 9×10 | **Taches d'huile** | Un véhicule qui roule sur une tache fait un **virage à 90°** : **à droite s'il avance, à gauche s'il recule**. Puis il repart tout droit dans ce nouveau sens jusqu'au bord : c'est là qu'il sort (et les murs comptent à cet endroit). **1 tache au début**. |
| 101 à 120 | Le parking à étages | 10×11 | **Sens uniques** | Un panneau bleu à flèche blanche : on ne franchit la case que **dans le sens de la flèche**. Dans l'autre sens, ou en travers, le véhicule avance jusqu'au panneau, klaxonne et revient. **1 panneau au début**. |
| 121 à 140 | L'usine | 10×12 | **Tapis roulants** | Un véhicule qui **traverse** un tapis (en travers) est **poussé d'une case de côté**, sans tourner, puis continue tout droit dans sa nouvelle file. Dans le sens du tapis ou à contresens, il n'a aucun effet. **1 tapis au début**. |
| 141 à 160 | Le parc magique | 11×12 | **Téléporteurs** | Deux cases de la même couleur : un véhicule qui entre par l'une **ressort par l'autre**, dans le même sens, puis continue tout droit. Il s'efface d'un côté et réapparaît de l'autre. **1 paire au début**, jusqu'à 3. |
| 161 à 180 | Le péage | 11×13 | **Barrières** | Une barrière est **fermée ou ouverte**, et **elle change à chaque véhicule qui sort du parking** (qu'il passe par elle ou non). Fermée, elle bloque comme un plot. Le **bon ordre** compte : l'indice ne conseille jamais un coup qui fermerait une barrière pour toujours. **1 barrière au début**, jusqu'à 4. |

- **Les premiers niveaux d'un monde sont plus simples** : à chaque début de monde (niveaux 1, 21, 41, 61, 81, 101, 121, 141, 161), les 3 premiers niveaux ont 55 % des véhicules habituels, les 3 suivants 75 %, les 3 suivants 90 %,
  avec un seul élément de la nouveauté au départ. La difficulté remonte ensuite. Le premier niveau de chaque mécanique (et les niveaux 1 à 3) affiche un petit mot qui explique la nouveauté ;
  ce même niveau est aussi celui où le parking s'agrandit.
- Dans les niveaux avec une mécanique, celle-ci **sert vraiment** : le générateur choisit des plans où chaque plot, mur, huile, sens unique, tapis, téléporteur ou barrière touche le trajet d'au moins un véhicule.
- **Les 100 premiers niveaux ne changent plus** : leurs étoiles sont rangées par numéro de niveau (un test d'empreinte le vérifie). Pour ajouter une mécanique : ajouter un monde dans `Worlds`, sa taille dans `LevelGenerator.sizes` et ses réglages dans `LevelGenerator.paramsFor`.
- Les grands parkings (jusqu'à 11×13) rendent les cases plus petites : environ 5 mm de large sur un téléphone ordinaire.

**La caisse** : le pick-up peut partir dans les deux sens, mais la caisse ne tombe pas au même endroit : s'il part vers l'avant, elle tombe sur la case de sa queue ;
s'il recule, elle tombe sur la case de son nez (le bout qui reste « derrière » dans ce sens). Choisir le bon sens, et le bon moment, est le casse-tête. **Une caisse ne bloque jamais** : un véhicule qui passe dessus la casse (elle éclate en morceaux quand il arrive sur sa case) et sort quand même, mais chaque caisse cassée est **une faute** (comme un coup de klaxon), donc des étoiles en moins. Une caisse mal placée ne fait plus perdre la partie. L'indice ne conseille jamais un coup qui laisserait une caisse à un mauvais endroit tant qu'il en existe un autre ; si c'est déjà arrivé, il montre le coup qui en casse le moins.

**Le sol, en détail** : tout ce qui est au sol (huile, sens unique, tapis, téléporteur, barrière) est sur une case où **aucun véhicule n'est garé**. Le trajet d'un véhicule est calculé case par case, dans l'ordre :

- **huile** : virage dans le sens de la marche (à droite en avançant, à gauche en reculant). À l'écran, les deux bouts d'une même file tournent du **même côté** ;
- **tapis** : poussée d'une case de côté en travers ; si elle sort du plateau, le véhicule glisse hors du plateau par le côté (un mur sur ce bord le bloque) ;
- **téléporteur** : saut vers l'autre case de la paire, sans effet à l'arrivée (jamais d'aller-retour), puis tout droit ;
- **sens unique** : franchissable seulement dans le sens de sa flèche, sinon le trajet s'arrête devant ;
- un trajet qui **tourne en rond**, ou qui **reviendrait sur la place du véhicule**, n'est pas un trajet : il ne peut pas sortir par là ;
- le chemin doit être libre (véhicules, plots, caisses, barrières fermées) et la sortie finale ne doit pas être murée. L'**indice** et le **solveur** suivent exactement ces trajets.

### Niveaux

**180 niveaux** en 9 mondes de 20, **générés de façon déterministe** (le niveau *n* est toujours le même) et **toujours solubles**.

| Monde | Niveaux | Parking | Véhicules | Plots | Murs | Pick-up | Taches | Sens uniques | Tapis | Téléporteurs | Barrières |
|---|---|---|---|---|---|---|---|---|---|---|---|
| Le petit parking | 1-20 | 5×6 | 3 → 6 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Les travaux | 21-40 | 6×7 | 4 → 10 | 1 → 2 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Le château fort | 41-60 | 7×8 | 6 → 14 | 2 → 3 | 1 → 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| Le port | 61-80 | 8×9 | 9 → 20 | 3 → 4 | 3 → 4 | 1 → 2 | 0 | 0 | 0 | 0 | 0 |
| La station-service | 81-100 | 9×10 | 12 → 25 | 4 → 5 | 4 → 6 | 2 → 3 | 1 → 5 | 0 | 0 | 0 | 0 |
| Le parking à étages | 101-120 | 10×11 | 15 → 31 | 5 → 6 | 6 → 7 | 3 | 5 → 6 | 1 → 2 | 0 | 0 | 0 |
| L'usine | 121-140 | 10×12 | 16 → 33 | 6 → 7 | 7 → 8 | 3 → 4 | 6 → 7 | 2 → 3 | 1 → 2 | 0 | 0 |
| Le parc magique | 141-160 | 11×12 | 17 → 34 | 7 → 8 | 8 → 9 | 4 | 7 → 8 | 4 → 5 | 2 → 4 | 1 → 2 | 0 |
| Le péage | 161-180 | 11×13 | 18 → 34 | 8 → 9 | 9 → 10 | 4 → 5 | 8 → 9 | 5 → 6 | 4 → 5 | 2 → 3 | 1 → 4 |

**Pourquoi c'est toujours soluble** : faire sortir un véhicule libère toujours de la place. Sans caisse ni barrière, le niveau est donc soluble si et seulement si on peut tout vider en retirant à chaque fois un véhicule qui a **une voie libre dans un sens ou dans l'autre**
(en suivant les virages, les poussées et les sauts du sol, sans véhicule ni plot ni caisse, et **sans mur à la sortie**). Les pick-up qui laissent tomber une caisse **ajoutent** un obstacle pour le **solveur** (qui cherche le parcours sans aucune faute) : seuls eux demandent de chercher le bon ordre, et ils sont peu nombreux,
donc la recherche du solveur (`Solver`) est minuscule. Dans le jeu, une caisse n'est qu'un obstacle « mou » : la casser est une faute, jamais une impasse. Les **barrières** changent à chaque sortie, donc l'ordre compte : l'état est alors (véhicules restants, caisses, parité du nombre de sorties), et le solveur fait une recherche en profondeur mémorisée,
bornée (en essayant d'abord les véhicules que les barrières concernent). Dans le jeu, si la recherche est trop longue pour conclure, on suppose que c'est faisable (jamais d'écran « bloqué » à tort) ; le générateur, lui, n'accepte que les niveaux dont il est sûr.
Un test vérifie les 180 niveaux en suivant les indices sans faute, et un autre que les niveaux ne changent jamais (empreinte).
La **difficulté** vient du nombre de « vagues » de sorties (la longueur de la plus longue suite de dépendances) et du peu de véhicules libres au départ ; dans les derniers mondes, on cherche aussi des **pièges**
(un coup qui paraît bon mais mène à l'impasse).

Le niveau suivant se débloque quand le précédent est réussi.

## Champis Magiques : règles

Un jeu d'adresse et de réflexes, **où l'on ne perd jamais** : le but est de s'amuser et d'entraîner sa dextérité.

- **La forêt** : une forêt du soir (bleu-vert profond, sapins, lucioles) remplie de **champignons blancs de tailles différentes**, de 12 à 20 selon la difficulté. Tous les champignons sont assez grands pour un doigt d'enfant (chapeau d'au moins 52 dp).
- **Le jeu** : des champignons **s'allument** (jaune, orange, rose, cyan ou vert, avec un halo qui pulse). L'enfant doit **les toucher**. On réagit **dès que le doigt se pose** (pas besoin de le relever), et à plusieurs doigts si on veut. Toucher un champignon éteint ne fait rien et ne coûte rien.
- **Le score** : plus on touche vite un champignon allumé, plus il rapporte : **100 points tout de suite, environ 60 après 1 seconde, 38 après 2, 19 après 4, jamais moins de 10**. Des points « +85 » montent au-dessus du champignon touché, des étincelles jaillissent.
- **Le son : une petite mélodie.** Pas de musique de fond : **chaque champignon touché joue la note suivante d'une mélodie** de 32 notes (boîte à musique), qui recommence au début de chaque partie et quand elle est finie. Le rythme est celui des doigts de l'enfant ; l'ensemble des touchers chante. La mélodie est en do majeur pentatonique (comme la fanfare et les « ding » des étoiles : rien ne sonne faux, même très vite ou en se chevauchant), faite de 4 phrases autour d'un motif (sol la sol mi), avec un point culminant à la 14e note pour qu'une partie courte l'entende aussi. Le **dernier** champignon d'une partie joue un accord de do plein au lieu de la note suivante (on s'arrête presque toujours au milieu de la mélodie). Un champignon interdit, ou raté, ne fait pas avancer la mélodie. Elle a été composée par un agent « compositeur » puis vérifiée par des tests (gamme, sauts, répétitions, boucle) ; elle n'a pas été écoutée sur un téléphone.
- **Une partie** : un nombre fixe de champignons à toucher (15 à 30 selon la difficulté), puis la partie finit toujours, avec **au moins 1 étoile** (2 et 3 étoiles selon le score). On ne perd jamais : un champignon qui s'éteint tout seul n'est qu'un champignon raté, sans punition, et la partie continue.
- **Au début d'une partie** : une fenêtre explique le jeu (« Appuie vite sur les champignons qui s'allument ! ») et le chronomètre ne démarre que quand l'enfant appuie sur **« C'est parti ! »**. « Rejouer » relance tout de suite.

### Les difficultés

On **choisit sa difficulté** au début ; on **débloque la suivante en faisant 2 étoiles sur la précédente**. La première est toujours ouverte.

| Difficulté | Champignons | À toucher | Allumés en même temps | Ce qui change | 2 étoiles | 3 étoiles |
|---|---|---|---|---|---|---|
| Petit champi | 12 | 15 | **1** | Ils **restent allumés** jusqu'à ce qu'on les touche | 700 | 1000 |
| Champi rapide | 16 | 20 | **2** | Plus de champignons, **deux allumés en même temps** | 800 | 1300 |
| Champi filant | 18 | 25 | **3** | Trois allumés en même temps, et ils **s'éteignent au bout de 4 s** si on est trop lent | 700 | 1300 |
| Champi malin | 20 | 30 | **4** | Quatre allumés en même temps, ils s'éteignent au bout de 3,6 s, et des champignons **violets (interdits)** s'allument aussi : il ne faut pas les toucher | 650 | 1300 |

- **Plusieurs allumés en même temps** : le nombre de champignons allumés **monte de 1 à chaque difficulté (1, 2, 3, puis 4)**. Ils ne s'allument pas tous d'un coup : ils s'allument **l'un après l'autre** (quelques dixièmes de seconde d'écart), et **dès qu'il en manque un** (touché ou éteint), un nouveau s'allume vite, **sans attendre que les autres soient touchés ou éteints**. Un joueur rapide voit donc presque toujours le maximum allumé. Le petit champi reste comme avant : un seul champignon à la fois.
- **Les étoiles suivent le rythme** : un champignon allumé perd des points tant qu'on ne l'a pas touché, donc quand 4 attendent en même temps, chacun rapporte moins. Les seuils d'étoiles sont réglés pour un joueur qui touche un champignon à la fois : environ 0,5 s par champignon pour 3 étoiles (la 1re difficulté est plus tolérante), environ 1 s pour 2 étoiles. Les seuils ont été calibrés par simulation, pas avec de vrais enfants.
- **La couleur interdite** (violet, avec une barre rouge) ne vient qu'à la dernière difficulté, et on la rappelle partout : dans la fenêtre du début et dans une pastille « Pas les violets ! » en haut de l'écran. Un champignon interdit touché ne donne **aucun point** et remet la série à zéro (aucune punition, la partie continue) ; s'il n'est pas touché, il s'éteint tout seul au bout de 2,5 s. Les interdits ne comptent pas parmi les champignons à toucher.
- **Les étoiles comptent pour le rang** du joueur, comme celles de Parking Malin (jusqu'à 12 étoiles : 4 difficultés × 3).
- **Sauvegarde** (`games.mushrooms`) : le meilleur score de chaque difficulté, le nombre de champignons touchés, de parties jouées et la plus longue série. Les étoiles se déduisent du meilleur score.

### Comment c'est fait

Le moteur (`games/mushrooms/engine`) est du Kotlin pur piloté par une horloge simulée : `MushroomRound` allume et éteint les champignons, `Scoring` donne les points, `ForestLayout` place des champignons de tailles variées sans qu'ils se touchent (graine fixe = même forêt), `Difficulty` décrit les quatre difficultés. Des tests jouent des parties complètes avec des « enfants » qui touchent chaque champignon un certain temps après son allumage, et avec un enfant plus vrai qui touche **un champignon à la fois** (0,45 s, 0,9 s ou 2,2 s par champignon) : ils vérifient le score, les étoiles, qu'on ne perd jamais, et que le nombre d'allumés en même temps monte bien à 1, 2, 3 puis 4.
L'interface (`games/mushrooms/ui`) dessine tout au pinceau : `ForestArt` (forêt, champignons, étincelles), le menu des difficultés, l'écran de partie. L'horloge de la partie n'avance que pendant qu'on joue et jamais de plus de 100 ms par image : si l'appli est mise de côté, rien n'expire d'un coup.

## Poussin Traverse : règles

Un jeu d'attention et d'adresse, **où l'on ne perd jamais** : un poussin traverse des routes de plus en plus larges. Se faire écraser fait perdre des points, jamais la partie. L'analyse complète (alternatives écartées, risques) est dans l'[issue #14](https://github.com/kcharignon/mobile-game/issues/14).

- **Toujours en paysage.** Dès qu'on ouvre le jeu (menu des niveaux compris) l'appli se met en paysage, dans les deux sens, même si la rotation automatique est coupée ; en sortant elle retrouve son portrait. Le manifeste déclare `configChanges="orientation|screenSize|…"` : tourner le téléphone ne recrée pas l'activité, donc une traversée en cours n'est pas perdue. (`core/Orientation.kt`, `LockLandscape`.)
- **La scène** : la route est horizontale (14 cases de large), le poussin part du trottoir du bas et saute **vers le haut** ; la caméra le suit. Les véhicules se voient arriver de loin. Les voies du bas vont vers la droite, celles du haut vers la gauche : une **route à double sens**. Des terre-pleins (herbe, buissons) servent de refuges.
- **Les contrôles** (sur tout l'écran, sans bouton) : **toucher** fait sauter le poussin d'une ligne vers l'avant ; **glisser le doigt dans le sens où l'on veut aller** le fait sauter de ce côté (devant, derrière, gauche, droite) dès que le doigt a bougé de 20 dp, un seul saut par glissé. Un doigt qui tremble un peu reste un toucher. Une première version avait une croix de boutons en bas à gauche : sur un vrai téléphone elle ne répondait pas, et la route ne touchait pas le bord de l'écran à gauche ; remplacée par les glissés (0.8.1). La route et les gestes occupent **tout l'écran, bord à bord** (barres système et encoche comprises) ; seuls le bouton retour et les pastilles restent dans les zones sûres. Les glissés qui partent du bord ne déclenchent pas le geste « retour » d'Android (`systemGestureExclusion`). Un geste pendant un saut est gardé et joué à l'atterrissage.
- **Le score** : +10 la première fois qu'on atteint une ligne, +10 par graine ramassée, +50 à l'arrivée, **−15 quand on est écrasé** (le score ne descend jamais sous 0). Pas de chronomètre.
- **Écrasé** : le poussin est aplati en dessin animé (yeux en croix, étoiles, plumes), le véhicule klaxonne, puis il revient **au dernier refuge où il s'est posé** (ou au départ), clignote 1,2 s sans pouvoir être retouché, et la partie continue.
- **Collisions indulgentes** (comme dans Parking Malin) : le poussin se dessine sur 0,32 case de rayon mais n'est touché que sur 0,22, et chaque véhicule est raccourci de 0,08 case à chaque bout : s'ils se frôlent, ça passe. Pendant un saut le poussin compte dans sa ligne de départ pour la première moitié, puis dans la suivante.
- **Les étoiles** : 1 dès l'arrivée (on ne perd jamais), 2 à 50 % du score maximum, 3 à 80 %. Le maximum d'un niveau = toutes les lignes + toutes les graines + l'arrivée. Elles comptent pour le rang, comme celles des autres jeux (jusqu'à 144 : 48 niveaux × 3).
- **Les graines** (dorées, trois grains) donnent une raison de se décaler et de prendre un petit risque. Elles sont posées par le robot (voir plus bas) **le long d'un chemin sans aucune touche** : on peut toujours toutes les ramasser, donc les 3 étoiles sont toujours possibles. Chaque graine ramassée joue la note suivante de la petite mélodie de Champis Magiques.
- Le niveau suivant se débloque quand le précédent est réussi.

### Les 48 niveaux

8 mondes de 6 niveaux, **déterministes** (le niveau n est le même sur tous les téléphones). Voies : `2 + monde + (indice dans le monde) / 3`, donc de **2 voies** (une dans chaque sens) au niveau 1 jusqu'à **10 voies** au niveau 48. Un terre-plein au milieu dès 4 voies, et **jamais plus de 3 voies d'affilée** sans refuge. Chaque monde apporte de nouveaux véhicules (les anciens restent) ; dans une même voie ils vont tous à la même vitesse (voies lentes, normales, lourdes ou rapides).

| Monde | Niveaux | Nom | Voies | Nouveautés | Ambiance |
|---|---|---|---|---|---|
| 1 | 1-6 | La petite rue | 2-3 | voitures, scooters | jour |
| 2 | 7-12 | Le quartier | 3-4 | camionnettes, tracteurs (très lents) | jour |
| 3 | 13-18 | La nationale | 4-5 | camions, bus (très longs) | campagne |
| 4 | 19-24 | La grande ville | 5-6 | taxis, ambulances (rapides, gyrophare) | ville |
| 5 | 25-30 | L'autoroute | 6-7 | voitures de sport (très rapides) | soir, phares allumés |
| 6 | 31-36 | Le passage à niveau | 7-8 | **voie ferrée** : feux, cloche, puis un train de 14 cases | nuit |
| 7 | 37-42 | L'heure de pointe | 8-9 | tous les véhicules, un train | soir |
| 8 | 43-48 | La mégapole | 9-10 | **deux voies ferrées**, tous les véhicules | nuit |

- **Difficulté** : de niveau 1 à 48, la vitesse de base monte de 1,6 à 2,9 cases/s (× le facteur de la voie : 0,7 lente, 1 normale, 0,85 lourde, 1,45 rapide) et la **fenêtre de passage** entre deux véhicules descend de 3 s à 1,1 s.
- **La voie ferrée** : toutes les 10 s (8 s au monde 8), les feux rouges clignotent et la cloche sonne environ 2,5 s, puis le train traverse toute la route à 12 cases/s (en 2,3 s).

### Garantie « toujours faisable » : le robot

Chaque niveau n'est accepté que si le `Planner` trouve une traversée **sans aucune touche, avec de la marge** (50 essais au plus, sinon le test échoue). C'est une recherche en espace-temps : un état = (ligne, colonne, tick), un tick = 0,1 s, horizon 60 s ; actions : attendre ou sauter (2 ticks). Un état est sûr si aucun véhicule n'est dans une zone **élargie** autour du poussin : la marge de réaction (0,3 case) plus ce que le véhicule parcourt en un tick. Pendant un saut il faut être sûr dans la ligne de départ **et** celle d'arrivée (plus prudent que le vrai moteur). On peut aussi se décaler pour « surfer » sur un trou du trafic. Le robot pose ensuite les graines de graine en graine, dans l'ordre des lignes, en gardant toujours un chemin pour finir. Le test **rejoue le chemin du robot dans le vrai moteur** (horloge injectée) : 0 touche, toutes les graines, l'arrivée, le score maximum, pour les 48 niveaux ; un autre le joue **en touchant l'écran** (Robolectric).

### Dessin et sons

Tout est dessiné par le code (`games/road/ui/RoadArt.kt`). Vue du dessus : bitume avec pointillés entre voies de même sens et double ligne jaune entre sens opposés, trottoirs dallés avec fleurs et passage piéton au départ, damier à l'arrivée, terre-pleins herbeux avec buissons, voie ferrée en gravier avec traverses et feux. 5 ambiances (jour, campagne, ville, soir, nuit : en soirée et la nuit les véhicules allument leurs phares). 9 véhicules (voiture, taxi, voiture de sport, scooter avec son conducteur, camionnette, tracteur, ambulance à gyrophare, camion à remorque, bus) et le train.
Le poussin est jaune, vu du dessus, bec en avant ; il bat des ailes et grossit au sommet du saut, son ombre rétrécit. Sons (synthétisés, pas de musique de fond) : un petit « pip » à chaque saut, la mélodie pour les graines, le vrai klaxon du véhicule qui écrase (voiture, camion ou bus selon sa longueur), une cloche à deux tons quand les feux de la voie ferrée se mettent à clignoter, et les sons habituels de fin de niveau (étoiles, fanfare). **Jamais écoutés sur un téléphone.**

### Comment c'est fait

Le moteur (`games/road/engine`) est du Kotlin pur piloté par une horloge en millisecondes : tout ce qui roule (`Stream`, `TrainLine`) est une **fonction pure du temps** (flux périodique de véhicules : aucune simulation, aucun hasard pendant la partie), `Crossing` est la traversée (sauts, points, écrasements, retour au refuge), `Planner` est le robot, `RoadLevels` génère les 48 niveaux. Le moteur avance par pas de 20 ms au plus pour qu'aucun véhicule ne « traverse » le poussin sans le toucher. La sauvegarde (`games.road`) garde le meilleur score et les étoiles de chaque niveau, le nombre d'écrasements et de traversées.

## Progression du joueur (« l'évolution »)

- **Étoiles** cumulées (somme des meilleures notes) → **rang**, affiché partout où le joueur apparaît :

| Étoiles | Rang |
|---|---|
| 0 | Petite voiture |
| 10 | Premier tour de roue |
| 30 | Moteur chaud |
| 60 | As du créneau |
| 100 | Super turbo |
| 160 | Génie du parking |
| 230 | Légende du parking |
| 300 | Maître du bitume |
| 400 | Roi de la route |
| 540 | Légende absolue (toutes les étoiles de Parking Malin : 180 niveaux × 3) |
| 696 | Super champion (toutes les étoiles de tous les jeux : 540 + 12 + 144) |

- Statistiques gardées : voitures sorties, fautes, niveaux réussis, plus haut niveau atteint.
- Le choix du **joueur** : un prénom (12 caractères) et un **animal** parmi 12 (chat, chien, ours, panda, lapin, grenouille, renard, lion, licorne, tigre, hibou, pingouin), dessinés par le code.

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
games/mushrooms/engine règles pures de Champis Magiques : difficultés, score, partie, forêt
games/mushrooms/ui     forêt, champignons, menu des difficultés, écran de partie
games/road/engine      règles pures de Poussin Traverse : véhicules (fonctions du temps), traversée, robot, 48 niveaux
games/road/ui          dessin de la route, des véhicules et du poussin, menu des niveaux, écran de traversée (paysage)
```

Le moteur de Parking Malin est du Kotlin pur, testé sur la JVM : règles de sortie et de faute, solveur, **les 180 niveaux solubles et stables**.
L'écriture de la sauvegarde est testée sur un dossier temporaire. L'interface est jouée sans téléphone (Robolectric) : `make android-screenshots` crée un joueur, finit un niveau, vérifie la sauvegarde et rend les captures de `docs/screenshots/`.

## Hors périmètre (pour l'instant)

Publicités, achats, compte en ligne, classement, publication sur le Play Store (l'appli se distribue en APK signé sur GitHub : voir
[`android/PUBLICATION.md`](../android/PUBLICATION.md)), tablette (l'appli est conçue pour un téléphone ; seul Poussin Traverse passe en paysage).
