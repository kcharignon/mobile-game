// Génère les maquettes (SVG) de docs/design : `node tools/design/make-design.js`, puis `node tools/design/render.js` pour les PNG.
'use strict';
const fs = require('fs');
const path = require('path');
const D = require('./ds.js');
const { T, CAR_COLORS, ANIMALS, W, H, fx, text, star, button, roundButton, panel, icons, car, cone, psign, board, avatar, screen, withBg, dim, darken } = D;

const OUT = path.join(__dirname, '..', '..', 'docs', 'design');
fs.mkdirSync(OUT, { recursive: true });
const write = (name, svg) => fs.writeFileSync(path.join(OUT, name), svg);

// ------------------------------------------------------------------ niveau d'exemple (6 x 8), vérifié soluble
const COLS = 6;
const ROWS = 8;
const LEVEL = [
  { id: 'A', c: 0, r: 0, len: 2, dir: 'R', color: CAR_COLORS.cyan },
  { id: 'B', c: 3, r: 0, len: 3, dir: 'U', color: CAR_COLORS.jaune },
  { id: 'C', c: 4, r: 1, len: 2, dir: 'R', color: CAR_COLORS.vert },
  { id: 'D', c: 2, r: 1, len: 2, dir: 'D', color: CAR_COLORS.rose },
  { id: 'E', c: 0, r: 3, len: 3, dir: 'L', color: CAR_COLORS.violet },
  { id: 'F', c: 5, r: 2, len: 2, dir: 'D', color: CAR_COLORS.orange },
  { id: 'G', c: 3, r: 3, len: 2, dir: 'R', color: CAR_COLORS.bleu },
  { id: 'H', c: 1, r: 4, len: 2, dir: 'U', color: CAR_COLORS.rouge },
  { id: 'I', c: 2, r: 5, len: 2, dir: 'L', color: CAR_COLORS.beige },
  { id: 'J', c: 4, r: 4, len: 3, dir: 'D', color: CAR_COLORS.vert },
  { id: 'K', c: 0, r: 7, len: 2, dir: 'R', color: CAR_COLORS.orange },
  { id: 'L', c: 2, r: 6, len: 2, dir: 'R', color: CAR_COLORS.cyan },
  { id: 'M', c: 0, r: 5, len: 2, dir: 'U', color: CAR_COLORS.rose },
];

function cells(v) {
  const h = v.dir === 'L' || v.dir === 'R';
  return Array.from({ length: v.len }, (_, i) => (h ? [v.c + i, v.r] : [v.c, v.r + i]));
}
function exitPath(v) {
  const out = [];
  if (v.dir === 'U') for (let r = v.r - 1; r >= 0; r--) out.push([v.c, r]);
  if (v.dir === 'D') for (let r = v.r + v.len; r < ROWS; r++) out.push([v.c, r]);
  if (v.dir === 'L') for (let c = v.c - 1; c >= 0; c--) out.push([c, v.r]);
  if (v.dir === 'R') for (let c = v.c + v.len; c < COLS; c++) out.push([c, v.r]);
  return out;
}
(function checkLevel() {
  const occ = new Map();
  for (const v of LEVEL) {
    for (const [c, r] of cells(v)) {
      const k = `${c},${r}`;
      if (occ.has(k)) throw new Error(`chevauchement en ${k} : ${occ.get(k)} / ${v.id}`);
      occ.set(k, v.id);
    }
  }
  const deps = new Map(LEVEL.map((v) => [v.id, new Set(exitPath(v).map(([c, r]) => occ.get(`${c},${r}`)).filter(Boolean))]));
  const left = new Set(LEVEL.map((v) => v.id));
  let progress = true;
  while (left.size && progress) {
    progress = false;
    for (const id of [...left]) {
      if ([...deps.get(id)].every((d) => !left.has(d))) {
        left.delete(id);
        progress = true;
      }
    }
  }
  if (left.size) throw new Error(`niveau d'exemple insoluble : ${[...left]}`);
})();

// ------------------------------------------------------------------ 0. planche : voitures, couleurs, composants
function sheet() {
  let b = '';
  b += text(195, 82, 'Planche de style', { size: 34, weight: 700, fill: '#fff', stroke: T.ink, strokeWidth: 7 });
  b += text(195, 108, 'Petits Jeux · Parking Malin', { size: 16, weight: 500, fill: T.ink, opacity: 0.8 });
  const s = 48;
  const col = Object.values(CAR_COLORS);
  b += panel({ x: 20, y: 124, w: 350, h: 300 });
  b += text(40, 152, 'Voitures (2 cases) et minibus (3 cases)', { size: 14, weight: 600, anchor: 'start', fill: T.inkSoft });
  // 4 directions : le nez (pare-brise et phares) montre où elle roule ; la dernière est rouge (faute)
  const o = { s, ox: 51, oy: 166 };
  b += car({ ...o, c: 0, r: 0, len: 2, dir: 'U', color: col[0] });
  b += car({ ...o, c: 1, r: 0, len: 2, dir: 'D', color: col[1] });
  b += car({ ...o, c: 2, r: 0, len: 3, dir: 'U', color: col[2] });
  b += car({ ...o, c: 3, r: 0, len: 3, dir: 'D', color: col[3] });
  b += car({ ...o, c: 4, r: 0, len: 2, dir: 'U', color: col[4] });
  b += car({ ...o, c: 5, r: 0, len: 2, dir: 'D', color: col[7] });
  b += car({ ...o, c: 0, r: 3, len: 2, dir: 'R', color: col[5] });
  b += car({ ...o, c: 2, r: 3, len: 2, dir: 'L', color: col[6] });
  b += car({ ...o, c: 4, r: 3, len: 2, dir: 'L', color: col[8], flash: true });
  b += car({ ...o, c: 0, r: 4, len: 3, dir: 'R', color: col[4] });
  b += car({ ...o, c: 3, r: 4, len: 3, dir: 'L', color: col[1] });
  b += text(310, 404, 'rouge vif = faute', { size: 11, weight: 500, fill: T.inkSoft });
  b += panel({ x: 20, y: 442, w: 350, h: 92 });
  b += text(40, 470, 'Couleurs des voitures', { size: 14, weight: 600, anchor: 'start', fill: T.inkSoft });
  Object.entries(CAR_COLORS).forEach(([n, c], i) => {
    const x = 52 + i * 36;
    b += `<circle cx="${x}" cy="500" r="15" fill="${darken(c, 0.3)}"/><circle cx="${x}" cy="497" r="15" fill="${c}"/>`;
    b += text(x, 526, n.slice(0, 4), { size: 9, weight: 500, fill: T.inkSoft });
  });
  b += panel({ x: 20, y: 552, w: 350, h: 270 });
  b += text(40, 578, 'Composants', { size: 14, weight: 600, anchor: 'start', fill: T.inkSoft });
  b += button({ x: 40, y: 592, w: 150, h: 52, label: 'Jouer', size: 24 });
  b += button({ x: 206, y: 592, w: 130, h: 52, label: 'Rejouer', color: T.blue, dark: T.blueDark, size: 22 });
  b += roundButton({ cx: 62, cy: 694, icon: icons.back(62, 694) });
  b += roundButton({ cx: 122, cy: 694, icon: icons.restart(122, 694), color: T.blue, dark: T.blueDark });
  b += roundButton({ cx: 182, cy: 694, color: '#FFB020', dark: '#D68A00', icon: icons.bulb(182, 694) });
  b += roundButton({ cx: 242, cy: 694, icon: icons.gear(242, 694) });
  b += star(298, 694, 20) + star(338, 694, 20, false);
  b += cone(60, 770, 36) + cone(112, 770, 36) + psign(176, 766, 36);
  b += avatar(250, 776, 26, 'chat') + avatar(310, 776, 26, 'lion');
  return screen(withBg(b));
}
write('00-planche-style.svg', sheet());

// ------------------------------------------------------------------ 1. choix du joueur (5 emplacements)
function profilsFragment() {
  let b = '';
  b += text(195, 98, 'Petits Jeux', { size: 40, weight: 700, fill: '#fff', stroke: T.ink, strokeWidth: 9 });
  b += text(195, 134, 'Qui joue ?', { size: 24, weight: 600, fill: T.ink });
  b += car({ c: 0, r: 0, len: 2, dir: 'R', color: CAR_COLORS.rose, s: 30, ox: 20, oy: 66 });
  b += car({ c: 0, r: 0, len: 2, dir: 'L', color: CAR_COLORS.cyan, s: 30, ox: 310, oy: 66 });
  const slots = [
    { name: 'Léa', rank: 'As du créneau', stars: 74, av: 'chat' },
    { name: 'Hugo', rank: 'Premier tour de roue', stars: 12, av: 'renard' },
    { name: 'Emma', rank: 'Petite voiture', stars: 0, av: 'lapin' },
    null,
    null,
  ];
  slots.forEach((s, i) => {
    const y = 160 + i * 110;
    if (s) {
      b += panel({ x: 24, y, w: 342, h: 94, r: 30 });
      b += avatar(76, y + 47, 34, s.av);
      b += text(124, y + 44, s.name, { size: 28, weight: 700, anchor: 'start' });
      b += text(124, y + 70, s.rank, { size: 15, weight: 500, anchor: 'start', fill: T.inkSoft });
      b += star(334, y + 34, 14) + text(334, y + 70, String(s.stars), { size: 20, weight: 700, fill: T.starDark });
    } else {
      b += `<rect x="24" y="${y}" width="342" height="94" rx="30" fill="#fff" opacity="0.35" stroke="#fff" stroke-width="3" stroke-dasharray="12 10"/>`;
      b += `<circle cx="76" cy="${y + 47}" r="30" fill="#fff" opacity="0.55"/>` + icons.plus(76, y + 47, 1.1);
      b += text(124, y + 56, 'Nouveau joueur', { size: 24, weight: 600, anchor: 'start', fill: T.ink, opacity: 0.75 });
    }
  });
  b += text(195, 764, 'Chaque joueur garde sa progression', { size: 14, weight: 500, fill: T.ink, opacity: 0.6 });
  b += text(195, 786, 'sur ce téléphone. Aucune pub, aucun réseau.', { size: 14, weight: 500, fill: T.ink, opacity: 0.6 });
  return withBg(b);
}
write('01-choix-joueur.svg', screen(profilsFragment()));

// ------------------------------------------------------------------ 2. nouveau joueur
function nouveau() {
  let b = profilsFragment() + dim(0.45);
  b += panel({ x: 22, y: 150, w: 346, h: 540, r: 36, ledge: 8 });
  b += text(195, 208, 'Nouveau joueur', { size: 30, weight: 700 });
  b += text(48, 252, 'Ton prénom', { size: 15, weight: 600, anchor: 'start', fill: T.inkSoft });
  b += `<rect x="46" y="262" width="298" height="58" rx="18" fill="#fff" stroke="${T.panelEdge}" stroke-width="3"/>`;
  b += text(66, 301, 'Léa', { size: 28, weight: 600, anchor: 'start' }) + `<rect x="122" y="276" width="3" height="30" rx="1.5" fill="${T.blue}"/>`;
  b += text(48, 360, 'Ton animal', { size: 15, weight: 600, anchor: 'start', fill: T.inkSoft });
  ANIMALS.forEach((a, i) => {
    const cx = 85 + (i % 4) * 74;
    const cy = 424 + Math.floor(i / 4) * 92;
    if (a === 'chat') b += `<circle cx="${cx}" cy="${cy}" r="39" fill="none" stroke="${T.blue}" stroke-width="5"/>`;
    b += avatar(cx, cy, 32, a);
  });
  b += button({ x: 62, y: 590, w: 266, h: 64, label: 'C’est parti !', size: 28 });
  return screen(b);
}
write('02-nouveau-joueur.svg', nouveau());

// ------------------------------------------------------------------ 3. accueil : les jeux du joueur
function accueil() {
  let b = '';
  b += panel({ x: 20, y: 56, w: 350, h: 92, r: 30 });
  b += avatar(68, 102, 32, 'chat');
  b += text(112, 94, 'Léa', { size: 26, weight: 700, anchor: 'start' });
  b += text(112, 118, 'As du créneau', { size: 15, weight: 500, anchor: 'start', fill: T.inkSoft });
  b += star(296, 86, 14) + text(316, 94, '74', { size: 22, weight: 700, fill: T.starDark, anchor: 'start' });
  b += `<rect x="112" y="128" width="190" height="8" rx="4" fill="#E9DDB8"/><rect x="112" y="128" width="${fx(190 * 0.47)}" height="8" rx="4" fill="${T.green}"/>`;
  b += text(312, 137, 'rang suivant : 120', { size: 10, weight: 500, fill: T.inkSoft, anchor: 'start' }).replace('rang suivant : 120', '120');
  b += text(30, 194, 'Choisis ton jeu', { size: 22, weight: 700, anchor: 'start' });
  const cy = 212;
  b += `<rect x="20" y="${cy + 10}" width="350" height="290" rx="34" fill="#E58A00"/>`;
  b += `<rect x="20" y="${cy}" width="350" height="290" rx="34" fill="#FF9F1A"/>`;
  b += `<rect x="30" y="${cy + 10}" width="330" height="170" rx="26" fill="${T.asphalt}"/>`;
  const o = { s: 40, ox: 50, oy: cy + 24 };
  b += car({ ...o, c: 0, r: 0, len: 2, dir: 'R', color: CAR_COLORS.rose });
  b += car({ ...o, c: 3, r: 0, len: 2, dir: 'D', color: CAR_COLORS.cyan });
  b += car({ ...o, c: 5, r: 0, len: 3, dir: 'D', color: CAR_COLORS.jaune });
  b += car({ ...o, c: 6, r: 1, len: 2, dir: 'U', color: CAR_COLORS.vert });
  b += car({ ...o, c: 1, r: 2, len: 2, dir: 'L', color: CAR_COLORS.violet });
  b += car({ ...o, c: 3, r: 3, len: 2, dir: 'R', color: CAR_COLORS.bleu });
  b += cone(48, cy + 164, 26) + cone(342, cy + 164, 26);
  b += text(195, cy + 224, 'Parking Malin', { size: 32, weight: 700, fill: '#fff', stroke: '#B86A00', strokeWidth: 8 });
  b += text(195, cy + 256, 'Fais sortir les voitures dans le bon ordre !', { size: 14, weight: 500, fill: '#fff' });
  b += `<rect x="252" y="${cy + 18}" width="100" height="34" rx="17" fill="#fff" opacity="0.92"/>` + star(272, cy + 35, 10) + text(286, cy + 42, 'Niv. 27', { size: 16, weight: 700, anchor: 'start' });
  const cy2 = 530;
  b += `<rect x="20" y="${cy2 + 8}" width="350" height="116" rx="30" fill="#B9BECE"/><rect x="20" y="${cy2}" width="350" height="116" rx="30" fill="#D5D9E6"/>`;
  b += `<circle cx="80" cy="${cy2 + 58}" r="34" fill="#fff" opacity="0.7"/>` + text(80, cy2 + 72, '?', { size: 44, weight: 700, fill: '#A2A8BC' });
  b += text(132, cy2 + 56, 'Bientôt…', { size: 26, weight: 700, anchor: 'start', fill: '#7B8196' });
  b += text(132, cy2 + 84, 'Un nouveau jeu arrive', { size: 15, weight: 500, anchor: 'start', fill: '#8D93A8' });
  b += icons.lock(335, cy2 + 40, 1.1);
  b += button({ x: 20, y: 722, w: 240, h: 60, label: 'Changer de joueur', color: T.blue, dark: T.blueDark, size: 21 });
  b += roundButton({ cx: 322, cy: 750, r: 30, icon: icons.gear(322, 750, 1.15) });
  return screen(withBg(b));
}
write('03-accueil-jeux.svg', accueil());

// ------------------------------------------------------------------ 4. choix du niveau
function niveaux() {
  let b = '';
  b += roundButton({ cx: 48, cy: 84, r: 26, icon: icons.back(48, 84) });
  b += text(188, 94, 'Parking Malin', { size: 30, weight: 700, fill: '#fff', stroke: T.ink, strokeWidth: 8 });
  b += `<rect x="290" y="64" width="80" height="40" rx="20" fill="#fff" opacity="0.92"/>` + star(314, 84, 12) + text(332, 91, '74', { size: 20, weight: 700, anchor: 'start', fill: T.starDark });
  b += panel({ x: 20, y: 130, w: 350, h: 660, r: 34, ledge: 8 });
  b += text(40, 170, 'Monde 1 · Le petit parking', { size: 17, weight: 600, anchor: 'start', fill: T.inkSoft });
  const stars = [3, 3, 2, 3, 3, 1, 2, 3, 3, 2, 3, 3, 3, 2, 1, 3, 2, 3, 3, 3, 2, 3, 3, 2, 1, 2, 0, 0];
  const cur = 27;
  for (let i = 0; i < 28; i++) {
    const col = i % 4;
    const row = Math.floor(i / 4);
    const x = 40 + col * 80;
    const y = 192 + row * 84;
    const n = i + 1;
    const isCur = n === cur;
    const locked = n > cur;
    const fill = locked ? '#D5D9E6' : isCur ? '#FFB020' : T.green;
    const dark = locked ? '#B9BECE' : isCur ? '#D68A00' : T.greenDark;
    b += `<rect x="${x}" y="${y + 5}" width="68" height="54" rx="19" fill="${dark}"/><rect x="${x}" y="${y}" width="68" height="54" rx="19" fill="${fill}"/>`;
    b += `<rect x="${x + 12}" y="${y + 5}" width="44" height="11" rx="5.5" fill="#fff" opacity="0.28"/>`;
    if (locked) b += icons.lock(x + 34, y + 24, 0.9);
    else b += text(x + 34, y + 37, String(n), { size: 28, weight: 700, fill: '#fff' });
    if (!locked) {
      const k = isCur ? 0 : stars[i];
      for (let j = 0; j < 3; j++) b += star(x + 14 + j * 20, y + 72 - (j === 1 ? 3 : 0), 8, j < k);
    }
    if (isCur) b += `<rect x="${x - 4}" y="${y - 4}" width="76" height="62" rx="22" fill="none" stroke="#fff" stroke-width="4" stroke-dasharray="8 6"/>`;
  }
  return screen(withBg(b));
}
write('04-niveaux.svg', niveaux());

// ------------------------------------------------------------------ 5. jeu
function jeuFragment({ bump = false, hint = false } = {}) {
  let b = '';
  b += roundButton({ cx: 44, cy: 84, r: 24, icon: icons.back(44, 84, 0.9) });
  b += `<rect x="86" y="62" width="218" height="46" rx="23" fill="#fff" opacity="0.92"/>`;
  b += text(195, 94, 'Niveau 27', { size: 24, weight: 700 });
  b += roundButton({ cx: 346, cy: 84, r: 24, icon: icons.restart(346, 84, 0.9), color: T.blue, dark: T.blueDark });
  b += star(150, 144, 16) + star(195, 140, 20) + star(240, 144, 16, !bump);
  const s = 52;
  const ox = (W - COLS * s) / 2;
  const oy = 190;
  b += psign(40, 156, 30) + cone(352, 160, 24);
  b += board({ cols: COLS, rows: ROWS, s, ox, oy });
  b += cone(30, 636, 30) + cone(360, 636, 30);
  for (const v of LEVEL) {
    b += car({ c: v.c, r: v.r, len: v.len, dir: v.dir, color: v.color, s, ox, oy, flash: bump && v.id === 'G' });
    if (hint && v.id === 'K') {
      const w = v.len * s;
      b += `<rect x="${fx(ox + v.c * s - 3)}" y="${fx(oy + v.r * s - 3)}" width="${fx(w + 6)}" height="${fx(s + 6)}" rx="24" fill="none" stroke="#fff" stroke-width="5" stroke-dasharray="10 7"/>`;
    }
  }
  if (bump) {
    const v = LEVEL.find((x) => x.id === 'G');
    const cx = ox + (v.c + 2) * s;
    const cy = oy + v.r * s + s / 2;
    b += `<g transform="translate(${fx(cx + 6)} ${fx(cy - 36)}) rotate(-8)"><rect x="-42" y="-20" width="84" height="38" rx="19" fill="#fff" stroke="${T.red}" stroke-width="4"/><polygon points="-8,17 8,17 0,29" fill="#fff"/>` +
      text(0, 8, 'Pouet !', { size: 20, weight: 700, fill: T.red }) + `</g>`;
  }
  b += `<rect x="20" y="722" width="128" height="58" rx="29" fill="#fff" opacity="0.92"/>`;
  b += car({ c: 0, r: 0, len: 2, dir: 'R', color: CAR_COLORS.rose, s: 22, ox: 32, oy: 740, shadow: false });
  b += text(116, 761, '13', { size: 26, weight: 700, fill: T.ink });
  b += button({ x: 160, y: 722, w: 210, h: 58, label: 'Indice', color: '#FFB020', dark: '#D68A00', size: 24, icon: icons.bulb(196, 750, 0.9) });
  return withBg(b);
}
write('05-jeu.svg', screen(jeuFragment()));
write('06-jeu-pouet.svg', screen(jeuFragment({ bump: true })));
write('08-jeu-indice.svg', screen(jeuFragment({ hint: true })));

// ------------------------------------------------------------------ 7. victoire
function victoire() {
  let o = jeuFragment() + dim(0.5);
  const cols = [T.red, T.blue, T.green, '#FFD93B', '#F0409A', '#A55BE8'];
  for (let i = 0; i < 44; i++) {
    const x = (i * 97) % W;
    const y = 40 + ((i * 53) % 330);
    o += `<rect x="${x}" y="${y}" width="9" height="15" rx="3" fill="${cols[i % cols.length]}" transform="rotate(${(i * 37) % 180} ${x} ${y})"/>`;
  }
  o += panel({ x: 36, y: 236, w: 318, h: 410, r: 38, ledge: 8 });
  o += text(195, 300, 'Bravo !', { size: 46, weight: 700, fill: '#FF7A1A' });
  o += star(120, 376, 34) + star(195, 364, 44) + star(270, 376, 34);
  o += text(195, 444, 'Niveau 27 réussi', { size: 22, weight: 600 });
  o += text(195, 472, '13 voitures · 0 faute', { size: 16, weight: 500, fill: T.inkSoft });
  o += button({ x: 64, y: 498, w: 262, h: 62, label: 'Niveau suivant', size: 26 });
  o += button({ x: 64, y: 578, w: 124, h: 44, label: 'Rejouer', color: T.blue, dark: T.blueDark, size: 18 });
  o += button({ x: 202, y: 578, w: 124, h: 44, label: 'Niveaux', color: '#8E96AB', dark: '#6B7388', size: 18 });
  return screen(o);
}
write('07-victoire.svg', victoire());

fs.writeFileSync(
  path.join(OUT, 'niveau-exemple.json'),
  JSON.stringify({ cols: COLS, rows: ROWS, cars: LEVEL.map(({ id, c, r, len, dir }) => ({ id, c, r, len, dir })) }, null, 2) + '\n',
);
console.log('maquettes écrites dans', path.relative(process.cwd(), OUT));
