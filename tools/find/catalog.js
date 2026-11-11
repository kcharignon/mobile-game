// Le catalogue de « Cherche et Trouve » : LA liste des 120 objets (identifiant, nom, famille de couleur, sorte).
// Source unique : `tools/design/objects.js` la dessine (et génère FindArt.kt), `FindRound` (Kotlin) en tire les manches au hasard.
// Ne jamais changer ni retirer un identifiant ; on peut en ajouter à la fin.
'use strict';

const HUES = ['rouge', 'orange', 'jaune', 'vert', 'bleu', 'violet', 'rose', 'marron', 'neutre'];
const KINDS = ['nourriture', 'animal', 'transport', 'nature', 'objet', 'vetement', 'instrument'];

const o = (id, label, hue, kind) => ({ id, label, hue, kind });

const OBJECTS = [
  // rouge
  o('pomme', 'une pomme', 'rouge', 'nourriture'),
  o('fraise', 'une fraise', 'rouge', 'nourriture'),
  o('coeur', 'un cœur', 'rouge', 'objet'),
  o('voiture', 'une voiture', 'rouge', 'transport'),
  o('champignon', 'un champignon', 'rouge', 'nature'),
  o('cerises', 'des cerises', 'rouge', 'nourriture'),
  // orange
  o('carotte', 'une carotte', 'orange', 'nourriture'),
  o('orange', 'une orange', 'orange', 'nourriture'),
  o('citrouille', 'une citrouille', 'orange', 'nourriture'),
  o('poisson', 'un poisson', 'orange', 'animal'),
  o('renard', 'un renard', 'orange', 'animal'),
  // jaune
  o('banane', 'une banane', 'jaune', 'nourriture'),
  o('etoile', 'une étoile', 'jaune', 'nature'),
  o('soleil', 'un soleil', 'jaune', 'nature'),
  o('poussin', 'un poussin', 'jaune', 'animal'),
  o('fromage', 'un fromage', 'jaune', 'nourriture'),
  // vert
  o('grenouille', 'une grenouille', 'vert', 'animal'),
  o('sapin', 'un sapin', 'vert', 'nature'),
  o('poire', 'une poire', 'vert', 'nourriture'),
  o('tortue', 'une tortue', 'vert', 'animal'),
  o('cactus', 'un cactus', 'vert', 'nature'),
  // bleu
  o('baleine', 'une baleine', 'bleu', 'animal'),
  o('parapluie', 'un parapluie', 'bleu', 'objet'),
  o('goutte', 'une goutte d’eau', 'bleu', 'nature'),
  o('bateau', 'un bateau', 'bleu', 'transport'),
  o('ballon', 'un ballon', 'bleu', 'objet'),
  // violet
  o('raisin', 'du raisin', 'violet', 'nourriture'),
  o('aubergine', 'une aubergine', 'violet', 'nourriture'),
  o('papillon', 'un papillon', 'violet', 'animal'),
  // rose
  o('cochon', 'un cochon', 'rose', 'animal'),
  o('glace', 'une glace', 'rose', 'nourriture'),
  o('cupcake', 'un petit gâteau', 'rose', 'nourriture'),
  // marron
  o('ours', 'un ours', 'marron', 'animal'),
  o('cookie', 'un biscuit', 'marron', 'nourriture'),
  o('maison', 'une maison', 'marron', 'objet'),
  o('chocolat', 'du chocolat', 'marron', 'nourriture'),
  // neutre
  o('nuage', 'un nuage', 'neutre', 'nature'),
  o('fantome', 'un fantôme', 'neutre', 'objet'),
  o('bonhomme', 'un bonhomme de neige', 'neutre', 'objet'),
  o('elephant', 'un éléphant', 'neutre', 'animal'),
  // ---- lot 2 : 80 objets de plus (refonte en manches aléatoires, 120 au total)
  // rouge
  o('tomate', 'une tomate', 'rouge', 'nourriture'),
  o('piment', 'un piment', 'rouge', 'nourriture'),
  o('coccinelle', 'une coccinelle', 'rouge', 'animal'),
  o('pompier', 'un camion de pompiers', 'rouge', 'transport'),
  o('tulipe', 'une tulipe', 'rouge', 'nature'),
  o('cerfvolant', 'un cerf-volant', 'rouge', 'objet'),
  o('bonnet', 'un bonnet', 'rouge', 'vetement'),
  // orange
  o('croissant', 'un croissant', 'orange', 'nourriture'),
  o('pizza', 'une part de pizza', 'orange', 'nourriture'),
  o('hamburger', 'un hamburger', 'orange', 'nourriture'),
  o('crabe', 'un crabe', 'orange', 'animal'),
  o('lion', 'un lion', 'orange', 'animal'),
  o('feuille', 'une feuille d’automne', 'orange', 'nature'),
  o('montgolfiere', 'une montgolfière', 'orange', 'transport'),
  o('cone', 'un cône de chantier', 'orange', 'objet'),
  // jaune
  o('citron', 'un citron', 'jaune', 'nourriture'),
  o('mais', 'un épi de maïs', 'jaune', 'nourriture'),
  o('ananas', 'un ananas', 'jaune', 'nourriture'),
  o('canard', 'un canard', 'jaune', 'animal'),
  o('abeille', 'une abeille', 'jaune', 'animal'),
  o('tournesol', 'un tournesol', 'jaune', 'nature'),
  o('lune', 'la lune', 'jaune', 'nature'),
  o('bus', 'un bus d’école', 'jaune', 'transport'),
  o('couronne', 'une couronne', 'jaune', 'objet'),
  o('trompette', 'une trompette', 'jaune', 'instrument'),
  // vert
  o('brocoli', 'un brocoli', 'vert', 'nourriture'),
  o('pasteque', 'une pastèque', 'vert', 'nourriture'),
  o('crocodile', 'un crocodile', 'vert', 'animal'),
  o('dinosaure', 'un dinosaure', 'vert', 'animal'),
  o('serpent', 'un serpent', 'vert', 'animal'),
  o('trefle', 'un trèfle', 'vert', 'nature'),
  o('palmier', 'un palmier', 'vert', 'nature'),
  o('tracteur', 'un tracteur', 'vert', 'transport'),
  // bleu
  o('dauphin', 'un dauphin', 'bleu', 'animal'),
  o('oiseau', 'un oiseau', 'bleu', 'animal'),
  o('hippocampe', 'un hippocampe', 'bleu', 'animal'),
  o('flocon', 'un flocon de neige', 'bleu', 'nature'),
  o('avion', 'un avion', 'bleu', 'transport'),
  o('velo', 'un vélo', 'bleu', 'transport'),
  o('seau', 'un seau de plage', 'bleu', 'objet'),
  o('robot', 'un robot', 'bleu', 'objet'),
  o('casquette', 'une casquette', 'bleu', 'vetement'),
  // violet
  o('prune', 'une prune', 'violet', 'nourriture'),
  o('bonbon', 'un bonbon', 'violet', 'nourriture'),
  o('poulpe', 'une pieuvre', 'violet', 'animal'),
  o('chauvesouris', 'une chauve-souris', 'violet', 'animal'),
  o('lavande', 'de la lavande', 'violet', 'nature'),
  o('pensee', 'une pensée', 'violet', 'nature'),
  o('diamant', 'un diamant', 'violet', 'objet'),
  o('baguette', 'une baguette magique', 'violet', 'objet'),
  o('chapeaumagique', 'un chapeau de magicien', 'violet', 'vetement'),
  // rose
  o('donut', 'un beignet', 'rose', 'nourriture'),
  o('macaron', 'un macaron', 'rose', 'nourriture'),
  o('barbapapa', 'une barbe à papa', 'rose', 'nourriture'),
  o('sucette', 'une sucette', 'rose', 'nourriture'),
  o('flamant', 'un flamant rose', 'rose', 'animal'),
  o('crevette', 'une crevette', 'rose', 'animal'),
  o('fleur', 'une fleur', 'rose', 'nature'),
  o('coquillage', 'un coquillage', 'rose', 'nature'),
  o('cadeau', 'un cadeau', 'rose', 'objet'),
  o('robe', 'une robe', 'rose', 'vetement'),
  // marron
  o('pain', 'un pain', 'marron', 'nourriture'),
  o('chien', 'un chien', 'marron', 'animal'),
  o('singe', 'un singe', 'marron', 'animal'),
  o('hibou', 'un hibou', 'marron', 'animal'),
  o('escargot', 'un escargot', 'marron', 'animal'),
  o('gland', 'un gland', 'marron', 'nature'),
  o('pommedepin', 'une pomme de pin', 'marron', 'nature'),
  o('valise', 'une valise', 'marron', 'objet'),
  o('guitare', 'une guitare', 'marron', 'instrument'),
  // neutre
  o('oeuf', 'un œuf', 'neutre', 'nourriture'),
  o('lapin', 'un lapin', 'neutre', 'animal'),
  o('mouton', 'un mouton', 'neutre', 'animal'),
  o('panda', 'un panda', 'neutre', 'animal'),
  o('pingouin', 'un pingouin', 'neutre', 'animal'),
  o('fusee', 'une fusée', 'neutre', 'transport'),
  o('ampoule', 'une ampoule', 'neutre', 'objet'),
  o('horloge', 'une horloge', 'neutre', 'objet'),
  o('cle', 'une clé', 'neutre', 'objet'),
  o('piano', 'un piano', 'neutre', 'instrument'),
];

// Contrôles de cohérence : si on casse le catalogue, tout s'arrête tout de suite.
const ids = new Set();
for (const x of OBJECTS) {
  if (ids.has(x.id)) throw new Error(`identifiant en double : ${x.id}`);
  ids.add(x.id);
  if (!HUES.includes(x.hue)) throw new Error(`couleur inconnue : ${x.id} ${x.hue}`);
  if (!KINDS.includes(x.kind)) throw new Error(`sorte inconnue : ${x.id} ${x.kind}`);
}
if (OBJECTS.length !== 120) throw new Error(`120 objets attendus, ${OBJECTS.length} trouvés`);

module.exports = { HUES, KINDS, OBJECTS };
