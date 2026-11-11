// Fabrique les niveaux de Cherche et Trouve (issue #29) : `node tools/find/make-levels.js`
//
//   android/app/src/main/res/raw/find_levels.json   les 100 niveaux : le modèle, les faux, une graine de mise en page
//
//   node tools/find/make-levels.js --check          régénère en mémoire et compare au fichier du dépôt (code de sortie 1 si différent)
//
// L'outil est DÉTERMINISTE : son propre générateur pseudo-aléatoire, une seule graine ; mêmes entrées, mêmes niveaux, sur toutes les machines.
// Les niveaux 1 à 100 sont publiés dès la livraison : ils ne changent plus (un test Kotlin garde l'empreinte de chaque monde). Pour ajouter des niveaux,
// on ajoute des mondes à la fin des tables ci-dessous sans toucher aux premières lignes.
//
// Un niveau = { n, target, others, seed } : le modèle, les faux (identifiants du catalogue `tools/find/catalog.js`) et la graine de la mise en page
// (l'appli en tire les places, voir FindLayout.kt). Le monde `w` (1 à 10) montre 4 + w objets, le bon compris.
//
// La difficulté monte de deux façons (issue #29, §2) : le NOMBRE d'objets (demandé) et la RESSEMBLANCE des faux (ajoutée) :
//   mondes 1-2   faux très différents : autre couleur ET autre sorte, jamais deux faux de la même couleur ;
//   mondes 3-4   autre couleur que le modèle, deux faux n'ont jamais la même couleur ; ils peuvent être de la même sorte ;
//   mondes 5-7   à partir du 4e niveau du monde, UN faux a la même couleur que le modèle (il faut regarder la forme) ;
//   monde 8      un faux de la même sorte, et un faux de la même couleur à partir du 5e niveau ;
//   mondes 9-10  un faux de la même sorte et deux de la même couleur (un seul de la même couleur avant le 4e niveau du monde 9).
'use strict';
const fs = require('fs');
const path = require('path');
const { OBJECTS } = require('./catalog.js');

const ROOT = path.join(__dirname, '..', '..');
const OUT_JSON = path.join(ROOT, 'android', 'app', 'src', 'main', 'res', 'raw', 'find_levels.json');

const LEVELS_PER_WORLD = 10;
const WORLD_COUNT = 10;
const objectCount = (world) => 4 + world;
const SEED = 0x5eed2910;

/** Les règles de ressemblance du niveau d'indice `idx` (0 à 9) du monde `world` (1 à 10). */
function rules(world, idx) {
  if (world <= 2) return { sameHue: 0, minSameKind: 0, distinctHues: true, differentKind: true };
  if (world <= 4) return { sameHue: 0, minSameKind: 0, distinctHues: true, differentKind: false };
  if (world <= 7) return { sameHue: idx >= 3 ? 1 : 0, minSameKind: 0, distinctHues: false, differentKind: false };
  if (world === 8) return { sameHue: idx >= 4 ? 1 : 0, minSameKind: 1, distinctHues: false, differentKind: false };
  if (world === 9) return { sameHue: idx >= 3 ? 2 : 1, minSameKind: 1, distinctHues: false, differentKind: false };
  return { sameHue: 2, minSameKind: 1, distinctHues: false, differentKind: false };
}

// ------------------------------------------------------------------ générateur pseudo-aléatoire (mulberry32)
function makeRng(seed) {
  let a = seed >>> 0;
  const next = () => {
    a = (a + 0x6d2b79f5) >>> 0;
    let t = a;
    t = Math.imul(t ^ (t >>> 15), t | 1);
    t ^= t + Math.imul(t ^ (t >>> 7), t | 61);
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
  };
  return next;
}
const shuffled = (list, rnd) => {
  const a = list.slice();
  for (let i = a.length - 1; i > 0; i--) {
    const j = Math.floor(rnd() * (i + 1));
    [a[i], a[j]] = [a[j], a[i]];
  }
  return a;
};

// ------------------------------------------------------------------ les modèles : chaque objet sert 2 ou 3 fois, jamais deux fois de suite ni trop près
const SPACING = 8;
function targetsFor(total, rnd) {
  const out = [];
  while (out.length < total) {
    const need = Math.min(OBJECTS.length, total - out.length);
    let deck;
    for (let tries = 0; ; tries++) {
      deck = shuffled(OBJECTS, rnd).slice(0, need);
      // le i-ème modèle du paquet ne doit pas être l'un des (SPACING - 1 - i) derniers déjà posés
      const recent = (k) => (k > 0 ? out.slice(-k) : []);
      const clash = deck.some((o, i) => recent(SPACING - 1 - i).some((p) => p.id === o.id));
      if (!clash) break;
      if (tries > 500) throw new Error('impossible de répartir les modèles');
    }
    out.push(...deck);
  }
  return out;
}

// ------------------------------------------------------------------ les faux d'un niveau
function pickFakes(target, count, r, rnd, usage) {
  const pool = OBJECTS.filter((o) => o.id !== target.id);
  // les moins employés d'abord (équilibre sur 100 niveaux), au hasard à égalité
  const prefer = (list) => shuffled(list, rnd).sort((a, b) => usage[a.id] - usage[b.id]);
  for (let attempt = 0; attempt < 400; attempt++) {
    const chosen = [];
    const take = (candidates, n) => {
      for (const c of prefer(candidates)) {
        if (n === 0) break;
        if (chosen.some((x) => x.id === c.id)) continue;
        chosen.push(c);
        n--;
      }
      return n === 0;
    };
    if (!take(pool.filter((o) => o.hue === target.hue), r.sameHue)) continue;
    if (!take(pool.filter((o) => o.kind === target.kind && o.hue !== target.hue), r.minSameKind)) continue;
    // le reste : une autre couleur que le modèle (et, selon le monde, autre sorte, couleurs deux à deux différentes ; quand on réclame des faux de la même sorte, on n'en ajoute pas d'autres)
    while (chosen.length < count) {
      const usedHues = new Set(chosen.map((x) => x.hue));
      const candidates = pool.filter(
        (o) => o.hue !== target.hue && !chosen.some((x) => x.id === o.id) && (!(r.differentKind || r.minSameKind > 0) || o.kind !== target.kind) && (!r.distinctHues || !usedHues.has(o.hue)),
      );
      if (candidates.length === 0) break;
      chosen.push(prefer(candidates)[0]);
    }
    if (chosen.length !== count) continue;
    return shuffled(chosen, rnd);
  }
  throw new Error(`pas de faux possibles pour ${target.id} (${JSON.stringify(r)})`);
}

// ------------------------------------------------------------------ vérification indépendante des règles (sert aussi de garde-fou après génération)
function check(level) {
  const errors = [];
  const byId = new Map(OBJECTS.map((o) => [o.id, o]));
  const world = Math.ceil(level.n / LEVELS_PER_WORLD);
  const idx = (level.n - 1) % LEVELS_PER_WORLD;
  const r = rules(world, idx);
  const t = byId.get(level.target);
  if (!t) return [`niveau ${level.n} : modèle inconnu ${level.target}`];
  const others = level.others.map((id) => byId.get(id));
  if (others.some((o) => !o)) return [`niveau ${level.n} : faux inconnu`];
  const all = [level.target, ...level.others];
  if (new Set(all).size !== all.length) errors.push(`niveau ${level.n} : objet en double`);
  if (all.length !== objectCount(world)) errors.push(`niveau ${level.n} : ${all.length} objets au lieu de ${objectCount(world)}`);
  const sameHue = others.filter((o) => o.hue === t.hue).length;
  if (sameHue !== r.sameHue) errors.push(`niveau ${level.n} : ${sameHue} faux de la couleur du modèle au lieu de ${r.sameHue}`);
  const sameKind = others.filter((o) => o.kind === t.kind && o.hue !== t.hue).length;
  if (r.minSameKind > 0 ? sameKind !== r.minSameKind : false) errors.push(`niveau ${level.n} : ${sameKind} faux de la même sorte au lieu de ${r.minSameKind}`);
  if (r.differentKind && others.some((o) => o.kind === t.kind)) errors.push(`niveau ${level.n} : un faux est de la même sorte que le modèle`);
  if (r.distinctHues && new Set(others.map((o) => o.hue)).size !== others.length) errors.push(`niveau ${level.n} : deux faux de la même couleur`);
  return errors;
}

// ------------------------------------------------------------------ fabrication
function build() {
  const rnd = makeRng(SEED);
  const total = LEVELS_PER_WORLD * WORLD_COUNT;
  const targets = targetsFor(total, rnd);
  const usage = Object.fromEntries(OBJECTS.map((o) => [o.id, 0]));
  const levels = [];
  for (let n = 1; n <= total; n++) {
    const world = Math.ceil(n / LEVELS_PER_WORLD);
    const idx = (n - 1) % LEVELS_PER_WORLD;
    const target = targets[n - 1];
    const fakes = pickFakes(target, objectCount(world) - 1, rules(world, idx), rnd, usage);
    for (const f of fakes) usage[f.id]++;
    levels.push({ n, target: target.id, others: fakes.map((f) => f.id), seed: Math.floor(rnd() * 2147483647) });
  }
  return levels;
}

function stats(levels) {
  const asTarget = Object.fromEntries(OBJECTS.map((o) => [o.id, 0]));
  const asAny = Object.fromEntries(OBJECTS.map((o) => [o.id, 0]));
  for (const l of levels) {
    asTarget[l.target]++;
    for (const id of [l.target, ...l.others]) asAny[id]++;
  }
  return { asTarget, asAny };
}

function serialize(levels) {
  const lines = levels.map((l) => `    ${JSON.stringify(l)}`);
  return `{\n  "version": 1,\n  "levels": [\n${lines.join(',\n')}\n  ]\n}\n`;
}

const levels = build();
const problems = levels.flatMap(check);
const { asTarget, asAny } = stats(levels);
for (const o of OBJECTS) {
  if (asTarget[o.id] < 2 || asTarget[o.id] > 4) problems.push(`${o.id} sert de modèle ${asTarget[o.id]} fois (2 à 4 attendues)`);
}
for (let i = 1; i < levels.length; i++) {
  if (levels[i].target === levels[i - 1].target) problems.push(`niveau ${levels[i].n} : même modèle que le précédent`);
}
if (problems.length) {
  console.error(problems.join('\n'));
  process.exit(1);
}

const json = serialize(levels);
if (process.argv.includes('--check')) {
  const current = fs.existsSync(OUT_JSON) ? fs.readFileSync(OUT_JSON, 'utf8') : '';
  if (current !== json) {
    console.error('find_levels.json n’est pas à jour : lancer `make find-levels` (`node tools/find/make-levels.js`) et le committer.');
    process.exit(1);
  }
  console.log('find_levels.json est à jour (', levels.length, 'niveaux )');
} else {
  fs.writeFileSync(OUT_JSON, json);
  const shown = [...OBJECTS].sort((a, b) => asAny[b.id] - asAny[a.id]);
  console.log('niveaux écrits :', path.relative(ROOT, OUT_JSON), '(', levels.length, 'niveaux )');
  console.log('objets les plus montrés :', shown.slice(0, 3).map((o) => `${o.id} ${asAny[o.id]}`).join(', '), '| les moins montrés :', shown.slice(-3).map((o) => `${o.id} ${asAny[o.id]}`).join(', '));
}
