// Maquettes de Poisson Perdu (labyrinthe à tracer, issue #22), dans docs/design : `node tools/design/make-recif.js`, puis `node tools/design/render.js` pour les PNG.
// Le jeu n'est pas encore codé : ces images sont la référence visuelle à valider AVANT d'écrire le Kotlin.
'use strict';
const fs = require('fs');
const path = require('path');
const D = require('./ds.js');
const R = require('./recif.js');
const { T, W, H, fx, text, star, button, roundButton, panel, icons, avatar, screen, withBg, dim, darken, car, cone } = D;
const { WORLDS, wallEdge, contrast, parseGrid, solve, mazeBoard, sea, fish, anemone, sparkle, motif, walls, ribbon, hash } = R;

const OUT = path.join(__dirname, '..', '..', 'docs', 'design');
fs.mkdirSync(OUT, { recursive: true });
const write = (name, svg) => fs.writeFileSync(path.join(OUT, name), svg);

// ------------------------------------------------------------------ garde-fous : le contraste des dix mondes et les grilles dessinées
const ratios = R.checkContrast();
const worst = ratios.reduce((a, b) => (a.ratio < b.ratio ? a : b));
console.log(`contraste mur/sol : ${ratios.length} couleurs de murs, le plus faible ${worst.ratio.toFixed(2)}:1 (${worst.world}, ${worst.wall})`);

// Niveau 14 du prototype de l'issue : 5 x 7, départ en bas, sortie en haut à droite.
const L14 = {
  n: 14, start: [1, 6], exit: [4, 0],
  grid: [
    '+--+--+--+--+  +',
    '|              |',
    '+  +--+  +  +  +',
    '|  |  |  |  |  |',
    '+  +  +--+  +  +',
    '|  |     |  |  |',
    '+  +--+  +  +--+',
    '|  |        |  |',
    '+  +--+  +  +  +',
    '|  |     |  |  |',
    '+  +--+--+  +  +',
    '|        |  |  |',
    '+  +--+--+  +  +',
    '|     |        |',
    '+--+  +--+--+--+',
  ],
};
L14.g = parseGrid(L14.grid);
L14.path = solve(L14.g, L14.start, L14.exit);

// Un petit morceau de labyrinthe (7 x 3) pour la carte du hub.
const CARD = {
  start: [1, 2], exit: [5, 0],
  grid: [
    '+--+--+--+--+--+  +--+',
    '|        |           |',
    '+--+  +--+  +--+  +--+',
    '|              |     |',
    '+  +--+  +--+--+--+  +',
    '|  |        |        |',
    '+--+  +--+--+--+--+--+',
  ],
};
CARD.g = parseGrid(CARD.grid);
CARD.path = solve(CARD.g, CARD.start, CARD.exit);

// Un morceau de mur pour la planche de style.
const SAMPLE = parseGrid([
  '+--+--+--+--+',
  '|     |     |',
  '+  +--+  +  +',
  '|        |  |',
  '+--+--+--+--+',
]);

const P2 = WORLDS[1]; // le récif de corail : le monde des maquettes de jeu
const tall = (body, h) =>
  `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${W} ${h}" width="${W}" height="${h}">` +
  `<style>@font-face{font-family:'Fredoka';src:url('../../android/app/src/main/res/font/fredoka.ttf');font-weight:300 700}</style>` +
  body + `</svg>`;
const title = (x, y, s, size = 34) => text(x, y, s, { size, weight: 700, fill: '#fff', stroke: T.ink, strokeWidth: size * 0.22 });

// ------------------------------------------------------------------ 10. planche de style
function planche() {
  const pal = WORLDS[0];
  let b = '';
  b += title(195, 82, 'Planche de style');
  b += text(195, 108, 'Petits Jeux · Poisson Perdu (projet)', { size: 16, weight: 500, fill: T.ink, opacity: 0.8 });

  b += panel({ x: 20, y: 124, w: 350, h: 176 });
  b += text(40, 152, 'Le poisson-clown et sa maison', { size: 14, weight: 600, anchor: 'start', fill: T.inkSoft });
  [0, 90, 180, 270].forEach((a, i) => { b += `<rect x="${28 + i * 62}" y="166" width="58" height="84" rx="16" fill="${pal.floor}"/>` + fish(57 + i * 62, 208, 44, a); });
  ['haut', 'droite', 'bas', 'gauche'].forEach((l, i) => { b += text(57 + i * 62, 268, l, { size: 11, weight: 500, fill: T.inkSoft }); });
  b += `<rect x="278" y="166" width="84" height="84" rx="20" fill="${pal.frame[0]}"/>` + anemone(320, 208, 62);
  b += text(320, 268, 'la sortie', { size: 11, weight: 500, fill: T.inkSoft });
  b += text(40, 290, 'Il regarde où il va ; à la victoire il suit la trace jusqu’à l’anémone.', { size: 11, weight: 500, anchor: 'start', fill: T.inkSoft });

  b += panel({ x: 20, y: 316, w: 350, h: 196 });
  b += text(40, 344, 'Les murs : des coraux, avec un contour foncé', { size: 14, weight: 600, anchor: 'start', fill: T.inkSoft });
  b += `<rect x="30" y="358" width="236" height="140" rx="14" fill="${pal.frame[0]}"/>`;
  b += floorAndWalls(SAMPLE, 50, 48, 378, pal);
  pal.walls.forEach((c, i) => {
    const y = 382 + i * 34;
    b += `<line x1="288" y1="${y}" x2="310" y2="${y}" stroke="${wallEdge(pal, c)}" stroke-width="20" stroke-linecap="round"/><line x1="288" y1="${y}" x2="310" y2="${y}" stroke="${c}" stroke-width="13" stroke-linecap="round"/>`;
    b += text(326, y + 5, `${contrast(pal.floor, wallEdge(pal, c)).toFixed(1)}:1`, { size: 13, weight: 700, anchor: 'start', fill: T.ink });
  });
  b += text(318, 486, 'contraste avec le sol', { size: 10, weight: 500, fill: T.inkSoft });

  b += panel({ x: 20, y: 528, w: 350, h: 112 });
  b += text(40, 556, 'La trace : un fil de bulles', { size: 14, weight: 600, anchor: 'start', fill: T.inkSoft });
  b += `<rect x="30" y="566" width="330" height="64" rx="14" fill="${pal.floor}"/>`;
  b += ribbon([[52, 604], [128, 604], [128, 584], [190, 584]], 44, pal);
  b += ribbon([[212, 604], [262, 604]], 44, pal, 'gold');
  b += [0, 1, 2].map((i) => `<circle cx="${296 + i * 24}" cy="${599 - i * 3}" r="${8 - i}" fill="#FFC83D" stroke="#fff" stroke-width="2.5"/>`).join('');
  b += text(120, 624, 'ta trace', { size: 11, weight: 500, fill: T.inkSoft });
  b += text(238, 624, 'parfaite', { size: 11, weight: 500, fill: T.inkSoft });
  b += text(320, 624, 'indice', { size: 11, weight: 500, fill: T.inkSoft });

  b += panel({ x: 20, y: 656, w: 350, h: 124 });
  b += text(40, 684, 'Posés au fond des impasses seulement', { size: 14, weight: 600, anchor: 'start', fill: T.inkSoft });
  WORLDS.forEach((w, i) => {
    const x = 30 + (i % 5) * 66;
    const y = 696 + Math.floor(i / 5) * 40;
    b += `<rect x="${x}" y="${y}" width="62" height="36" rx="10" fill="${w.floor}"/><g opacity="0.85">` + motif(w.motif, x + 31, y + 18, 46, 0.4) + '</g>';
  });
  b += text(195, 808, 'Les dix palettes : 11-recif-dix-mondes', { size: 12, weight: 500, fill: T.ink, opacity: 0.7 });
  return screen(withBg(b));
}
// le sol, les murs, et rien d'autre (planche de style)
function floorAndWalls(g, s, ox, oy, pal) {
  return R.floor(g, s, ox, oy, pal) + walls(g, s, ox, oy, pal);
}
write('10-recif-planche-style.svg', planche());

// ------------------------------------------------------------------ 11. les dix mondes (le même niveau, dix palettes)
function dixMondes() {
  const TH = 1380;
  let b = `<defs><linearGradient id="bg" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="${T.sunLight}"/><stop offset="0.3" stop-color="${T.sun}"/><stop offset="1" stop-color="${T.sunDark}"/></linearGradient></defs><rect width="${W}" height="${TH}" fill="url(#bg)"/>`;
  b += title(195, 62, 'Les dix mondes');
  b += text(195, 90, 'Le même niveau, dix palettes. Contour des murs ≥ 3:1 avec le sol.', { size: 13, weight: 500, fill: T.ink, opacity: 0.8 });
  WORLDS.forEach((w, i) => {
    const x = 20 + (i % 2) * 183;
    const y = 108 + Math.floor(i / 2) * 250;
    b += panel({ x, y, w: 167, h: 236, r: 24, ledge: 5 });
    b += text(x + 12, y + 22, `${i + 1} · ${w.name}`, { size: 13, weight: 700, anchor: 'start' });
    const gid = `tile${i}`;
    b += `<defs><linearGradient id="${gid}" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="${w.frame[0]}"/><stop offset="1" stop-color="${w.frame[1]}"/></linearGradient></defs>` +
      `<rect x="${x + 10}" y="${y + 32}" width="147" height="170" rx="16" fill="url(#${gid})"/>`;
    const s = 22;
    const ox = x + (167 - 5 * s) / 2;
    const oy = y + 50;
    b += mazeBoard({ g: L14.g, start: L14.start, exit: L14.exit, s, ox, oy, pal: w, trail: L14.path.slice(0, 6), fish: 'start' });
    w.walls.forEach((c, k) => { b += `<circle cx="${x + 20 + k * 18}" cy="${y + 220}" r="7" fill="${wallEdge(w, c)}"/><circle cx="${x + 20 + k * 18}" cy="${y + 220}" r="5" fill="${c}"/>`; });
    const worst = Math.min(...w.walls.map((c) => contrast(w.floor, wallEdge(w, c))));
    b += text(x + 156, y + 224, `${worst.toFixed(1)}:1`, { size: 12, weight: 700, anchor: 'end', fill: T.inkSoft });
  });
  return tall(b, TH);
}
write('11-recif-dix-mondes.svg', dixMondes());

// ------------------------------------------------------------------ 12. accueil : la carte du jeu parmi les autres
function accueil() {
  let b = '';
  b += panel({ x: 20, y: 56, w: 350, h: 92, r: 30 });
  b += avatar(68, 102, 32, 'chat');
  b += text(112, 94, 'Léa', { size: 26, weight: 700, anchor: 'start' });
  b += text(112, 118, 'As du créneau', { size: 15, weight: 500, anchor: 'start', fill: T.inkSoft });
  b += star(296, 86, 14) + text(316, 94, '74', { size: 22, weight: 700, fill: T.starDark, anchor: 'start' });
  b += `<rect x="112" y="128" width="190" height="8" rx="4" fill="#E9DDB8"/><rect x="112" y="128" width="${fx(190 * 0.47)}" height="8" rx="4" fill="${T.green}"/>`;
  b += text(312, 137, '120', { size: 10, weight: 500, fill: T.inkSoft, anchor: 'start' });
  b += text(30, 194, 'Choisis ton jeu', { size: 22, weight: 700, anchor: 'start' });

  // la carte du récif
  const cy = 212;
  const pal = P2;
  b += `<rect x="20" y="${cy + 10}" width="350" height="290" rx="34" fill="#E58A00"/><rect x="20" y="${cy}" width="350" height="290" rx="34" fill="#FF9F1A"/>`;
  b += `<clipPath id="reefcard"><rect x="30" y="${cy + 10}" width="330" height="170" rx="26"/></clipPath>`;
  b += `<g clip-path="url(#reefcard)">` + sea(pal, 390, 844).replace(/<rect width="390" height="844"/, `<rect x="0" y="${cy - 300}" width="390" height="844"`);
  b += mazeBoard({ g: CARD.g, start: CARD.start, exit: CARD.exit, s: 40, ox: 55, oy: cy + 32, pal, trail: CARD.path.slice(0, 4), fish: 'start', buoyOn: false }) + `</g>`;
  b += `<rect x="42" y="${cy + 18}" width="120" height="34" rx="17" fill="#fff" opacity="0.92"/>` + star(64, cy + 35, 10) + text(80, cy + 41, 'Nouveau !', { size: 15, weight: 700, anchor: 'start' });
  b += text(195, cy + 224, 'Poisson Perdu', { size: 32, weight: 700, fill: '#fff', stroke: '#B86A00', strokeWidth: 8 });
  b += text(195, cy + 256, 'Aide le poisson à retrouver sa maison !', { size: 14, weight: 500, fill: '#fff' });

  // la carte de Parking Malin juste dessous (pour juger le contraste avec les autres illustrations)
  const cy2 = 530;
  // la liste défile sous les boutons du bas : la carte s'efface en douceur au lieu d'être coupée net
  b += `<defs><linearGradient id="fadeList" gradientUnits="userSpaceOnUse" x1="0" y1="660" x2="0" y2="712"><stop offset="0" stop-color="#fff"/><stop offset="1" stop-color="#000"/></linearGradient>` +
    `<mask id="listMask" maskUnits="userSpaceOnUse" x="0" y="0" width="${W}" height="${H}"><rect width="${W}" height="660" fill="#fff"/><rect y="660" width="${W}" height="52" fill="url(#fadeList)"/></mask></defs><g mask="url(#listMask)">`;
  b += `<rect x="20" y="${cy2 + 10}" width="350" height="290" rx="34" fill="#E58A00"/><rect x="20" y="${cy2}" width="350" height="290" rx="34" fill="#FF9F1A"/>`;
  b += `<rect x="30" y="${cy2 + 10}" width="330" height="170" rx="26" fill="${T.asphalt}"/>`;
  const o = { s: 40, ox: 50, oy: cy2 + 24 };
  b += car({ ...o, c: 0, r: 0, len: 2, dir: 'R', color: D.CAR_COLORS.rose });
  b += car({ ...o, c: 3, r: 0, len: 2, dir: 'D', color: D.CAR_COLORS.cyan });
  b += car({ ...o, c: 5, r: 0, len: 3, dir: 'D', color: D.CAR_COLORS.jaune });
  b += car({ ...o, c: 1, r: 2, len: 2, dir: 'L', color: D.CAR_COLORS.violet });
  b += cone(48, cy2 + 164, 26) + cone(342, cy2 + 164, 26);
  b += text(195, cy2 + 224, 'Parking Malin', { size: 32, weight: 700, fill: '#fff', stroke: '#B86A00', strokeWidth: 8 }) + `</g>`;

  b += button({ x: 20, y: 722, w: 240, h: 60, label: 'Changer de joueur', color: T.blue, dark: T.blueDark, size: 21 });
  b += roundButton({ cx: 322, cy: 750, r: 30, icon: icons.gear(322, 750, 1.15) });
  return screen(withBg(b));
}
write('12-recif-accueil.svg', accueil());

// ------------------------------------------------------------------ 13. choix du niveau : dix mondes de dix niveaux
function niveaux() {
  let b = '';
  b += roundButton({ cx: 48, cy: 84, r: 26, icon: icons.back(48, 84) });
  b += text(188, 94, 'Poisson Perdu', { size: 30, weight: 700, fill: '#fff', stroke: T.ink, strokeWidth: 8 });
  b += `<rect x="290" y="64" width="80" height="40" rx="20" fill="#fff" opacity="0.92"/>` + star(314, 84, 12) + text(332, 91, '37', { size: 20, weight: 700, anchor: 'start', fill: T.starDark });
  b += panel({ x: 20, y: 130, w: 350, h: 660, r: 34, ledge: 8 });
  const stars = [[3, 3, 3, 3, 2, 3, 3, 3, 3, 3], [3, 2, 3, 0, 0, 0, 0, 0, 0, 0]];
  const cur = 14;
  [0, 1, 2].forEach((w, wi) => {
    const pal = WORLDS[w];
    const y0 = 166 + wi * 204;
    const locked = w === 2;
    const done = stars[w] ? stars[w].reduce((a, c) => a + c, 0) : 0;
    b += text(40, y0 + 12, `Monde ${w + 1} · ${pal.name}`, { size: 16, weight: 600, anchor: 'start', fill: T.inkSoft });
    // les trois couleurs de corail du monde, à droite du titre (un monde se montre toujours en couleurs : on peut y sauter)
    pal.walls.forEach((c, k) => { b += `<circle cx="${262 + k * 15}" cy="${y0 + 7}" r="5.5" fill="${c}"/>`; });
    if (!locked) b += star(324, y0 + 6, 9) + text(338, y0 + 12, `${done}`, { size: 15, weight: 700, anchor: 'start', fill: T.starDark });
    for (let i = 0; i < 10; i++) {
      const n = w * 10 + i + 1;
      const col = i % 5;
      const row = Math.floor(i / 5);
      const x = 40 + col * 64;
      const y = y0 + 34 + row * 76;
      const isCur = n === cur;
      const isLocked = n > cur;
      const fill = isLocked ? '#D5D9E6' : isCur ? '#FFB020' : T.green;
      const dark = isLocked ? '#B9BECE' : isCur ? '#D68A00' : T.greenDark;
      b += `<rect x="${x}" y="${y + 5}" width="54" height="44" rx="16" fill="${dark}"/><rect x="${x}" y="${y}" width="54" height="44" rx="16" fill="${fill}"/>`;
      b += `<rect x="${x + 10}" y="${y + 4}" width="34" height="9" rx="4.5" fill="#fff" opacity="0.28"/>`;
      // tous les niveaux sont jouables : un niveau pas encore réussi est gris, avec son numéro (pas de cadenas)
      b += text(x + 27, y + 31, String(n), { size: 24, weight: 700, fill: isLocked ? '#7B8196' : '#fff' });
      if (!isLocked) {
        const k = isCur ? 0 : stars[w][i];
        for (let j = 0; j < 3; j++) b += star(x + 11 + j * 16, y + 61 - (j === 1 ? 3 : 0), 6.5, j < k);
      }
      if (isCur) b += `<rect x="${x - 4}" y="${y - 4}" width="62" height="52" rx="19" fill="none" stroke="#fff" stroke-width="4" stroke-dasharray="8 6"/>`;
    }
  });
  return screen(withBg(b));
}
write('13-recif-niveaux.svg', niveaux());

// ------------------------------------------------------------------ 14 à 16. le jeu
const BOARD = { s: 66 };
BOARD.ox = (W - L14.g.cols * BOARD.s) / 2;
BOARD.oy = 178;
function jeuFragment({ trailLen = 0, hintLen = 0, kind = 'water', fishAt = 'start', finger = false, lostStar = false, bottomBar = true } = {}) {
  let b = sea(P2, W, H);
  b += roundButton({ cx: 44, cy: 84, r: 24, icon: icons.back(44, 84, 0.9) });
  b += `<rect x="86" y="58" width="218" height="54" rx="27" fill="#fff" opacity="0.94"/>`;
  b += text(195, 83, 'Niveau 14', { size: 22, weight: 700 });
  b += text(195, 101, P2.name, { size: 13, weight: 500, fill: T.inkSoft });
  b += roundButton({ cx: 346, cy: 84, r: 24, icon: icons.restart(346, 84, 0.9), color: T.blue, dark: T.blueDark });
  b += star(150, 142, 16) + star(195, 138, 20) + star(240, 142, 16, !lostStar);
  const trail = L14.path.slice(0, trailLen);
  const hint = hintLen ? L14.path.slice(trailLen, trailLen + hintLen) : [];
  b += mazeBoard({ g: L14.g, start: L14.start, exit: L14.exit, ...BOARD, pal: P2, trail, trailKind: kind, hint, fish: fishAt, finger, angle: 0 });
  if (!bottomBar) return b;
  b += `<rect x="20" y="722" width="160" height="58" rx="29" fill="#fff" opacity="0.94"/>`;
  b += text(44, 757, 'Impasses', { size: 16, weight: 600, anchor: 'start', fill: T.inkSoft });
  b += text(152, 760, lostStar ? '1' : '0', { size: 26, weight: 700, fill: T.ink });
  b += button({ x: 196, y: 722, w: 174, h: 58, label: 'Indice', color: '#FFB020', dark: '#D68A00', size: 24, icon: icons.bulb(236, 750, 0.9) });
  return b;
}
write('14-recif-jeu.svg', screen(jeuFragment({ trailLen: Math.ceil(L14.path.length * 0.55), finger: true })));
write('15-recif-jeu-indice.svg', screen(jeuFragment({ trailLen: Math.ceil(L14.path.length * 0.35), hintLen: 5, finger: true })));

function victoire() {
  // Le panneau est en bas : on voit la trace dorée et le poisson arrivé chez lui (c'est la récompense du jeu).
  let o = jeuFragment({ trailLen: L14.path.length, kind: 'gold', fishAt: 'exit', bottomBar: false }) + dim(0.28);
  const cols = ['#FFFFFF', '#FFD54A', '#FF8FB5', '#7FE8FF', '#B58CFF'];
  for (let i = 0; i < 30; i++) {
    const x = 14 + ((i * 97) % (W - 28));
    const y = 120 + ((i * 53) % 330);
    o += i % 3 === 0 ? sparkle(x, y, 8 + (i % 4) * 2, cols[i % cols.length]) : `<circle cx="${x}" cy="${y}" r="${3 + (i % 4) * 1.5}" fill="#fff" fill-opacity="0.35" stroke="#fff" stroke-width="2"/>`;
  }
  o += panel({ x: 24, y: 478, w: 342, h: 318, r: 36, ledge: 8 });
  o += text(195, 534, 'Bravo !', { size: 40, weight: 700, fill: '#FF7A1A' });
  o += star(131, 590, 26) + star(195, 582, 34) + star(259, 590, 26);
  o += text(195, 638, 'Niveau 14 réussi · chemin parfait', { size: 17, weight: 600 });
  o += text(195, 660, '0 impasse', { size: 14, weight: 500, fill: T.inkSoft });
  o += button({ x: 50, y: 676, w: 290, h: 54, label: 'Niveau suivant', size: 24 });
  o += button({ x: 50, y: 740, w: 138, h: 40, label: 'Rejouer', color: T.blue, dark: T.blueDark, size: 17 });
  o += button({ x: 202, y: 740, w: 138, h: 40, label: 'Niveaux', color: '#8E96AB', dark: '#6B7388', size: 17 });
  return screen(o);
}
write('16-recif-victoire.svg', victoire());

console.log(`maquettes du récif écrites dans ${path.relative(process.cwd(), OUT)} (niveau 14 : ${L14.path.length} cases de solution)`);
