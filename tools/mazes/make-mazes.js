// Fabrique les niveaux de Poisson Perdu (labyrinthe à tracer, issue #22) : `node tools/mazes/make-mazes.js`
//
//   android/app/src/main/res/raw/maze_levels.json   les niveaux (grilles en art ASCII, lisibles dans un diff)
//   docs/design/20-labyrinthes-planche.svg          une planche-contact des 100 niveaux, pour les relire d'un coup d'œil
//
//   node tools/mazes/make-mazes.js --check          régénère en mémoire et compare au fichier du dépôt (code de sortie 1 si différent)
//
// L'outil est DÉTERMINISTE : son propre générateur pseudo-aléatoire, une graine par niveau (dérivée de son numéro). Les mêmes graines donnent les mêmes
// labyrinthes sur toutes les machines. Les niveaux 1 à 100 sont publiés : ils ne changent plus (un test Kotlin garde l'empreinte de chacun). Pour
// ajouter des niveaux, on étend les tables ci-dessous à la fin, sans toucher aux premières lignes ; pour remplacer UN mauvais niveau avant publication,
// on met son numéro dans SALT (une autre graine, les 99 autres ne bougent pas).
//
// Format d'une grille : (3 x colonnes + 1) caractères sur (2 x lignes + 1) lignes. Les lignes paires portent les murs horizontaux (`+--+` mur, `+  +` passage),
// les lignes impaires les murs verticaux (`|` mur, espace passage) ; chaque case occupe deux espaces. Le bord est dessiné aussi : l'entrée (en bas, sous le
// départ) et la sortie (en haut, au-dessus) sont des ouvertures dans le bord. Une sortie au centre n'a pas d'ouverture.
'use strict';
const fs = require('fs');
const path = require('path');

const ROOT = path.join(__dirname, '..', '..');
const OUT_JSON = path.join(ROOT, 'android', 'app', 'src', 'main', 'res', 'raw', 'maze_levels.json');
const OUT_SHEET = path.join(ROOT, 'docs', 'design', '20-labyrinthes-planche.svg');

// ------------------------------------------------------------------ les tables : 10 mondes de 10 niveaux (ne jamais réordonner les premières lignes)
const LEVELS_PER_WORLD = 10;
/**
 * cols x rows, longueur de la solution (cases) et embranchements sur la solution visés du premier au dernier niveau du monde,
 * `braid` : part des culs-de-sac percés (des boucles : plusieurs chemins), `pLow`/`pHigh` : bornes de `p` (voir makeMaze).
 */
const WORLDS = [
  { name: 'Le lagon', cols: 4, rows: 5, sol: [8, 10], dec: [1, 2], braid: 0 },
  { name: 'Le récif de corail', cols: 5, rows: 7, sol: [12, 14], dec: [2, 3], braid: 0 },
  { name: "La forêt d'algues", cols: 6, rows: 8, sol: [15, 18], dec: [3, 5], braid: 0 },
  { name: "L'épave", cols: 7, rows: 9, sol: [19, 24], dec: [5, 7], braid: 0 },
  { name: 'La grotte lumineuse', cols: 8, rows: 11, sol: [27, 33], dec: [7, 10], braid: 0 },
  { name: 'La banquise', cols: 9, rows: 12, sol: [34, 42], dec: [10, 13], braid: 0.12 },
  { name: 'Le volcan', cols: 10, rows: 13, sol: [44, 52], dec: [13, 16], braid: 0.18 },
  { name: "Le jardin d'anémones", cols: 10, rows: 14, sol: [49, 58], dec: [15, 18], braid: 0.24 },
  { name: 'Les abysses', cols: 11, rows: 15, sol: [55, 66], dec: [18, 22], braid: 0.3 },
  { name: 'Le palais du roi', cols: 11, rows: 16, sol: [64, 78], dec: [21, 25], braid: 0.3 },
];
const LEVEL_COUNT = WORLDS.length * LEVELS_PER_WORLD;
/** Niveaux dont la sortie est au centre du labyrinthe (monde 8, quelques niveaux) : pas d'ouverture dans le bord. */
const CENTER_EXIT = new Set([73, 76, 79]);
/** Niveaux refaits avec une autre graine (avant publication seulement). */
const SALT = {};
const CANDIDATES = 500;
const MAX_STRAIGHT = 0.6;
/** Écart maximal d'embranchements avec la cible du niveau ; la solution reste à 15 % près de la bande de son monde. */
const MAX_DEC_GAP = 2;

// ------------------------------------------------------------------ hasard déterministe
function rng(seed) { // mulberry32
  let a = seed >>> 0;
  return () => {
    a = (a + 0x6D2B79F5) >>> 0;
    let t = a;
    t = Math.imul(t ^ (t >>> 15), t | 1);
    t ^= t + Math.imul(t ^ (t >>> 7), t | 61);
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
  };
}

const DIRS = [[0, -1], [1, 0], [0, 1], [-1, 0]]; // haut, droite, bas, gauche

// ------------------------------------------------------------------ le labyrinthe
/**
 * Labyrinthe parfait (un seul chemin entre deux cases) par « growing tree » : à chaque pas on creuse depuis la case la plus récente avec la probabilité `p`,
 * sinon depuis une case au hasard. `p` haut : longs couloirs sinueux, peu d'embranchements (facile à lire) ; `p` bas : beaucoup de petites impasses (difficile).
 */
function makeMaze(cols, rows, p, rand) {
  const open = Array.from({ length: cols * rows }, () => [false, false, false, false]);
  const seen = new Uint8Array(cols * rows);
  const first = Math.floor(rand() * cols * rows);
  const active = [first];
  seen[first] = 1;
  while (active.length) {
    const idx = rand() < p ? active.length - 1 : Math.floor(rand() * active.length);
    const c = active[idx];
    const x = c % cols;
    const y = (c / cols) | 0;
    const nbrs = [];
    for (let d = 0; d < 4; d++) {
      const nx = x + DIRS[d][0];
      const ny = y + DIRS[d][1];
      if (nx >= 0 && ny >= 0 && nx < cols && ny < rows && !seen[ny * cols + nx]) nbrs.push(d);
    }
    if (!nbrs.length) { active.splice(idx, 1); continue; }
    const d = nbrs[Math.floor(rand() * nbrs.length)];
    const n = (y + DIRS[d][1]) * cols + x + DIRS[d][0];
    open[c][d] = true;
    open[n][(d + 2) % 4] = true;
    seen[n] = 1;
    active.push(n);
  }
  return { cols, rows, open };
}

/** Nombre de passages entre cases (cols x rows - 1 pour un labyrinthe parfait, plus s'il a des boucles). */
const passages = (m) => m.open.reduce((a, o) => a + o.filter(Boolean).length, 0) / 2;
const degree = (m, c) => m.open[c].filter(Boolean).length;
const cellAt = (m, c, d) => (Math.floor(c / m.cols) + DIRS[d][1]) * m.cols + (c % m.cols) + DIRS[d][0];

/** Perce une part des culs-de-sac : des boucles, donc plusieurs chemins (monde 6 et suivants). */
function braid(m, share, rand) {
  const ends = [];
  for (let c = 0; c < m.cols * m.rows; c++) if (degree(m, c) === 1) ends.push(c);
  // mélange déterministe
  for (let i = ends.length - 1; i > 0; i--) {
    const j = Math.floor(rand() * (i + 1));
    [ends[i], ends[j]] = [ends[j], ends[i]];
  }
  for (const c of ends) {
    if (degree(m, c) !== 1 || rand() >= share) continue;
    const x = c % m.cols;
    const y = (c / m.cols) | 0;
    const walls = [];
    for (let d = 0; d < 4; d++) {
      const nx = x + DIRS[d][0];
      const ny = y + DIRS[d][1];
      if (nx < 0 || ny < 0 || nx >= m.cols || ny >= m.rows || m.open[c][d]) continue;
      walls.push([d, ny * m.cols + nx]);
    }
    if (!walls.length) continue;
    // on préfère relier à un autre cul-de-sac (la boucle est alors plus longue et plus belle)
    const deadEnd = walls.filter(([, n]) => degree(m, n) === 1);
    const pick = (deadEnd.length ? deadEnd : walls)[Math.floor(rand() * (deadEnd.length ? deadEnd.length : walls.length))];
    m.open[c][pick[0]] = true;
    m.open[pick[1]][(pick[0] + 2) % 4] = true;
  }
}

function bfs(m, from) {
  const dist = new Int32Array(m.cols * m.rows).fill(-1);
  const q = [from];
  dist[from] = 0;
  for (let i = 0; i < q.length; i++) {
    const c = q[i];
    for (let d = 0; d < 4; d++) {
      if (!m.open[c][d]) continue;
      const n = cellAt(m, c, d);
      if (dist[n] < 0) { dist[n] = dist[c] + 1; q.push(n); }
    }
  }
  return dist;
}

/** Le chemin le plus court de `from` à `to` (le premier dans l'ordre des directions : déterministe), d'après les distances depuis `from`. */
function shortestPath(m, dist, to) {
  const path = [to];
  while (dist[path[path.length - 1]] > 0) {
    const c = path[path.length - 1];
    for (let d = 0; d < 4; d++) {
      if (!m.open[c][d]) continue;
      const n = cellAt(m, c, d);
      if (dist[n] === dist[c] - 1) { path.push(n); break; }
    }
  }
  return path.reverse();
}

/** Longueur de la solution, embranchements (vraies décisions de l'enfant), virages, plus longue ligne droite en part de la solution. */
function pathMetrics(m, path) {
  let decisions = 0;
  let turns = 0;
  let run = 1;
  let longest = 1;
  for (let i = 1; i < path.length - 1; i++) {
    if (degree(m, path[i]) >= 3) decisions++;
    if (path[i] - path[i - 1] !== path[i + 1] - path[i]) { turns++; run = 1; } else { run++; longest = Math.max(longest, run); }
  }
  return { sol: path.length, decisions, turns, straight: longest / path.length };
}

// ------------------------------------------------------------------ la cible d'un niveau et le choix du meilleur candidat
function target(world, idx) {
  const f = idx / (LEVELS_PER_WORLD - 1);
  const w = WORLDS[world];
  return { sol: w.sol[0] + (w.sol[1] - w.sol[0]) * f, dec: w.dec[0] + (w.dec[1] - w.dec[0]) * f };
}

function makeLevel(n, used) {
  const world = Math.floor((n - 1) / LEVELS_PER_WORLD);
  const idx = (n - 1) % LEVELS_PER_WORLD;
  const w = WORLDS[world];
  const { cols, rows } = w;
  const t = target(world, idx);
  const solSpan = Math.max(2, w.sol[1] - w.sol[0]);
  const decSpan = Math.max(1, w.dec[1] - w.dec[0]);
  const center = CENTER_EXIT.has(n);
  let best = null;
  // des paquets de CANDIDATES candidats, tant qu'aucun n'est dans les bornes (jusqu'à 8 paquets) : toujours déterministe
  for (let k = 0; k < CANDIDATES * 8; k++) {
    if (k > 0 && k % CANDIDATES === 0 && best) break;
    const rand = rng(n * 100003 + k * 7919 + (SALT[n] || 0) * 1000003);
    const p = 0.3 + rand() * 0.65;
    const m = makeMaze(cols, rows, p, rand);
    if (w.braid > 0) {
      braid(m, w.braid, rand);
      if (passages(m) === cols * rows - 1) continue; // un monde à boucles : chaque niveau en a au moins une
    }
    const exits = [];
    if (center) {
      for (let y = Math.floor(rows / 4); y < rows - Math.floor(rows / 4); y++) for (let x = Math.floor(cols / 4); x < cols - Math.floor(cols / 4); x++) exits.push(y * cols + x);
    } else {
      for (let x = 0; x < cols; x++) exits.push(x);
    }
    for (let sx = 0; sx < cols; sx++) {
      const start = (rows - 1) * cols + sx;
      const dist = bfs(m, start);
      for (const exit of exits) {
        if (exit === start || dist[exit] < 0) continue;
        const path = shortestPath(m, dist, exit);
        const mt = pathMetrics(m, path);
        if (mt.straight > MAX_STRAIGHT) continue;
        // bornes dures : jamais un niveau trop loin de la bande de son monde, quel que soit son score
        if (Math.abs(mt.decisions - t.dec) > MAX_DEC_GAP || mt.sol < w.sol[0] * 0.85 || mt.sol > w.sol[1] * 1.15) continue;
        const score = Math.abs(mt.sol - t.sol) / solSpan + Math.abs(mt.decisions - t.dec) / decSpan;
        if (best && score >= best.score) continue;
        const key = signature(m, start, exit);
        if (used.has(key.a) || used.has(key.b)) continue;
        best = { score, m, start, exit, path, mt, key };
      }
    }
  }
  if (!best) throw new Error(`niveau ${n} : aucun candidat dans les bornes (cible : ${t.sol.toFixed(0)} cases, ${t.dec.toFixed(1)} embranchements) : élargir CANDIDATES ou la plage de p`);
  used.add(best.key.a);
  used.add(best.key.b);
  return { n, world: world + 1, cols, rows, start: [best.start % cols, rows - 1], exit: [best.exit % cols, (best.exit / cols) | 0], center, m: best.m, path: best.path, mt: best.mt, score: best.score };
}

/** Empreinte du labyrinthe et de son miroir gauche-droite : deux niveaux ne sont jamais le même, ni le reflet l'un de l'autre. */
function signature(m, start, exit) {
  const a = [];
  const b = [];
  for (let c = 0; c < m.cols * m.rows; c++) {
    a.push(m.open[c].map((v) => (v ? 1 : 0)).join(''));
    const x = c % m.cols;
    const y = (c / m.cols) | 0;
    const mc = y * m.cols + (m.cols - 1 - x);
    b[mc] = [m.open[c][0], m.open[c][3], m.open[c][2], m.open[c][1]].map((v) => (v ? 1 : 0)).join('');
  }
  const mirror = (cell) => ((cell / m.cols) | 0) * m.cols + (m.cols - 1 - (cell % m.cols));
  return { a: `${m.cols}x${m.rows}|${start}|${exit}|${a.join('')}`, b: `${m.cols}x${m.rows}|${mirror(start)}|${mirror(exit)}|${b.join('')}` };
}

// ------------------------------------------------------------------ la grille en art ASCII
function toGrid(level) {
  const { m, cols, rows } = level;
  const startCell = (rows - 1) * cols + level.start[0];
  const lines = [];
  let top = '+';
  for (let x = 0; x < cols; x++) top += (!level.center && x === level.exit[0] ? '  ' : '--') + '+';
  lines.push(top);
  for (let y = 0; y < rows; y++) {
    let row = '|';
    let under = '+';
    for (let x = 0; x < cols; x++) {
      const c = y * cols + x;
      row += '  ' + (x === cols - 1 ? '|' : m.open[c][1] ? ' ' : '|');
      const bottomOpen = y === rows - 1 ? c === startCell : m.open[c][2];
      under += (bottomOpen ? '  ' : '--') + '+';
    }
    lines.push(row, under);
  }
  return lines;
}

// ------------------------------------------------------------------ le fichier
function build() {
  const used = new Set();
  const levels = [];
  for (let n = 1; n <= LEVEL_COUNT; n++) levels.push(makeLevel(n, used));
  // la difficulté moyenne des mondes ne baisse jamais
  let prev = -1;
  for (let w = 0; w < WORLDS.length; w++) {
    const ls = levels.filter((l) => l.world === w + 1);
    const mean = ls.reduce((a, l) => a + l.mt.decisions, 0) / ls.length;
    if (mean < prev) throw new Error(`le monde ${w + 1} est plus simple que le précédent (${mean.toFixed(1)} embranchements en moyenne contre ${prev.toFixed(1)})`);
    prev = mean;
  }
  return levels;
}

function format(levels) {
  const body = levels.map((l) => {
    const grid = toGrid(l);
    return `    {"n": ${l.n}, "world": ${l.world}, "cols": ${l.cols}, "rows": ${l.rows}, "start": [${l.start}], "exit": [${l.exit}], "grid": [\n` +
      grid.map((g) => `      "${g}"`).join(',\n') + '\n    ]}';
  });
  return `{\n  "format": 1,\n  "levels": [\n${body.join(',\n')}\n  ]\n}\n`;
}

// ------------------------------------------------------------------ la planche-contact
function sheet(levels) {
  const D = require('../design/ds.js');
  const R = require('../design/recif.js');
  const { T, fx, text } = D;
  const TILE_W = 80;
  const TILE_H = 112;
  const LEFT = 86;
  const W = LEFT + 10 * TILE_W + 16;
  const H = 76 + WORLDS.length * (TILE_H + 26) + 20;
  let b = `<defs><linearGradient id="bg" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="${T.sunLight}"/><stop offset="0.3" stop-color="${T.sun}"/><stop offset="1" stop-color="${T.sunDark}"/></linearGradient></defs><rect width="${W}" height="${H}" fill="url(#bg)"/>`;
  b += text(W / 2, 40, 'Les 100 niveaux de Poisson Perdu', { size: 28, weight: 700, fill: '#fff', stroke: T.ink, strokeWidth: 6 });
  b += text(W / 2, 62, 'Chemin le plus court en bleu · départ en bas (orange) · sortie en rose · généré par tools/mazes/make-mazes.js', { size: 12, weight: 500, fill: T.ink, opacity: 0.8 });
  for (const l of levels) {
    const pal = R.WORLDS[l.world - 1];
    const idx = (l.n - 1) % LEVELS_PER_WORLD;
    const x0 = LEFT + idx * TILE_W;
    const y0 = 76 + (l.world - 1) * (TILE_H + 26) + 18;
    if (idx === 0) {
      b += text(14, y0 + 22, `${l.world}`, { size: 30, weight: 700, anchor: 'start', fill: '#fff', stroke: T.ink, strokeWidth: 6 });
      b += text(14, y0 + 40, `${l.cols}×${l.rows}`, { size: 12, weight: 600, anchor: 'start', fill: T.ink });
      b += text(14, y0 + 54, pal.name.length > 14 ? pal.name.slice(0, 13) + '…' : pal.name, { size: 9, weight: 500, anchor: 'start', fill: T.ink, opacity: 0.8 });
    }
    const s = Math.min((TILE_W - 12) / l.cols, (TILE_H - 14) / l.rows);
    const ox = x0 + (TILE_W - l.cols * s) / 2;
    const oy = y0 + 8 + (TILE_H - 14 - l.rows * s) / 2;
    b += `<rect x="${x0 + 2}" y="${y0 - 2}" width="${TILE_W - 4}" height="${TILE_H}" rx="10" fill="${pal.frame[0]}"/>`;
    b += `<rect x="${fx(ox)}" y="${fx(oy)}" width="${fx(l.cols * s)}" height="${fx(l.rows * s)}" fill="${pal.floor}"/>`;
    const C = (c) => [ox + ((c % l.cols) + 0.5) * s, oy + (((c / l.cols) | 0) + 0.5) * s];
    b += `<polyline points="${l.path.map((c) => C(c).map(fx).join(',')).join(' ')}" fill="none" stroke="${pal.trail[0]}" stroke-width="${fx(Math.max(1.6, s * 0.34))}" stroke-linecap="round" stroke-linejoin="round" opacity="0.85"/>`;
    const g = R.parseGrid(toGrid(l));
    const edge = R.wallEdge(pal, pal.walls[1]);
    let walls = '';
    for (let r = 0; r <= g.rows; r++) for (let x = 0; x < g.cols; x++) if (g.h[r][x]) walls += `M${fx(ox + x * s)} ${fx(oy + r * s)}h${fx(s)}`;
    for (let r = 0; r < g.rows; r++) for (let x = 0; x <= g.cols; x++) if (g.v[r][x]) walls += `M${fx(ox + x * s)} ${fx(oy + r * s)}v${fx(s)}`;
    b += `<path d="${walls}" fill="none" stroke="${edge}" stroke-width="${fx(Math.max(1.2, s * 0.16))}" stroke-linecap="round"/>`;
    const [sx, sy] = C(l.start[1] * l.cols + l.start[0]);
    const [ex, ey] = C(l.exit[1] * l.cols + l.exit[0]);
    b += `<circle cx="${fx(sx)}" cy="${fx(sy)}" r="${fx(Math.max(2, s * 0.3))}" fill="#FF8A1F" stroke="${T.ink}" stroke-width="0.8"/>`;
    b += `<circle cx="${fx(ex)}" cy="${fx(ey)}" r="${fx(Math.max(2, s * 0.3))}" fill="#FF6FA0" stroke="${T.ink}" stroke-width="0.8"/>`;
    b += text(x0 + 8, y0 + 10, String(l.n), { size: 9, weight: 700, anchor: 'start', fill: '#fff', stroke: T.ink, strokeWidth: 2 });
  }
  return `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${W} ${H}" width="${W}" height="${H}">` +
    `<style>@font-face{font-family:'Fredoka';src:url('../../android/app/src/main/res/font/fredoka.ttf');font-weight:300 700}</style>` + b + `</svg>`;
}

// ------------------------------------------------------------------ lancement
const levels = build();
const json = format(levels);
if (process.argv.includes('--check')) {
  const current = fs.existsSync(OUT_JSON) ? fs.readFileSync(OUT_JSON, 'utf8') : '';
  if (current !== json) {
    console.error('❌ maze_levels.json n\'est pas ce que produit tools/mazes/make-mazes.js : relancer `make mazes` (ou revenir sur l\'édition à la main).');
    process.exit(1);
  }
  console.log(`✅ maze_levels.json est à jour (${levels.length} niveaux)`);
  process.exit(0);
}
fs.mkdirSync(path.dirname(OUT_JSON), { recursive: true });
fs.writeFileSync(OUT_JSON, json);
fs.writeFileSync(OUT_SHEET, sheet(levels));
console.log(`${levels.length} niveaux écrits : ${path.relative(process.cwd(), OUT_JSON)} (${(json.length / 1024).toFixed(0)} Ko), planche ${path.relative(process.cwd(), OUT_SHEET)}`);
console.log('monde  grille   solution (cases)  embranchements   culs-de-sac');
for (let w = 0; w < WORLDS.length; w++) {
  const ls = levels.filter((l) => l.world === w + 1);
  const fmt = (f) => { const v = ls.map(f); return `${Math.min(...v)}-${Math.max(...v)}`; };
  const ends = (l) => { let k = 0; for (let c = 0; c < l.cols * l.rows; c++) if (degree(l.m, c) === 1) k++; return k; };
  console.log(`${String(w + 1).padStart(3)}    ${`${WORLDS[w].cols}x${WORLDS[w].rows}`.padEnd(7)}  ${fmt((l) => l.mt.sol).padEnd(16)}  ${fmt((l) => l.mt.decisions).padEnd(14)}  ${fmt(ends)}`);
}
