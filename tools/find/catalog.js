// Le catalogue de « Cherche et Trouve » : LA liste des 40 objets (identifiant, nom, famille de couleur, sorte).
// Source unique : `tools/design/objects.js` la dessine (et génère FindArt.kt), `tools/find/make-levels.js` en tire les niveaux.
// Ne jamais changer ni retirer un identifiant (il est écrit dans find_levels.json) ; on peut en ajouter à la fin.
'use strict';

const HUES = ['rouge', 'orange', 'jaune', 'vert', 'bleu', 'violet', 'rose', 'marron', 'neutre'];
const KINDS = ['nourriture', 'animal', 'transport', 'nature', 'objet'];

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
];

// Contrôles de cohérence : si on casse le catalogue, tout s'arrête tout de suite.
const ids = new Set();
for (const x of OBJECTS) {
  if (ids.has(x.id)) throw new Error(`identifiant en double : ${x.id}`);
  ids.add(x.id);
  if (!HUES.includes(x.hue)) throw new Error(`couleur inconnue : ${x.id} ${x.hue}`);
  if (!KINDS.includes(x.kind)) throw new Error(`sorte inconnue : ${x.id} ${x.kind}`);
}
if (OBJECTS.length !== 40) throw new Error(`40 objets attendus, ${OBJECTS.length} trouvés`);

module.exports = { HUES, KINDS, OBJECTS };
