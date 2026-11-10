// Poisson Perdu (labyrinthe à tracer, issue #22) : le dessin du récif.
// Le poisson-clown, l'anémone, les coraux qui font les murs, le fil de bulles de la trace, les dix décors.
// Tout est du SVG généré : ce sont les maquettes de référence (docs/design). Le Kotlin reprendra les mêmes proportions
// (games/maze/ui/MazeArt.kt), et le même test de contraste que `checkContrast` ci-dessous.
'use strict';

const D = require('./ds.js');
const { T, fx, mix, lighten, darken, text } = D;

let uid = 0;
const nid = (p) => `${p}${++uid}`;
const INK = '#1B1D2E';

// ---------------------------------------------------------------- couleurs et contraste (WCAG)
const lin = (v) => {
  const c = v / 255;
  return c <= 0.03928 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4);
};
function luminance(hex) {
  const h = hex.replace('#', '');
  const [r, g, b] = [0, 2, 4].map((i) => parseInt(h.slice(i, i + 2), 16));
  return 0.2126 * lin(r) + 0.7152 * lin(g) + 0.0722 * lin(b);
}
function contrast(a, b) {
  const [hi, lo] = [luminance(a), luminance(b)].sort((p, q) => q - p);
  return (hi + 0.05) / (lo + 0.05);
}

// ---------------------------------------------------------------- les dix mondes (palette + un petit objet posé au fond des impasses)
// `edge: 'dark'` = contour foncé (sol clair) ; `'light'` = liseré clair (sol sombre : les abysses).
const WORLDS = [
  { name: 'Le lagon', floor: '#C8F3F1', sand: '#F4E3B1', walls: ['#FF8FA3', '#FFB27A', '#B58CFF'], edge: 'dark', frame: ['#35B3CB', '#176F8C'], trail: ['#1F6FB5', '#5DB8F2'], motif: 'shell' },
  { name: 'Le récif de corail', floor: '#BFEAF2', sand: '#F4E3B1', walls: ['#FF6F91', '#FF9671', '#D65DB1'], edge: 'dark', frame: ['#2E8FC0', '#17537F'], trail: ['#1F6FB5', '#5DB8F2'], motif: 'starfish' },
  { name: "La forêt d'algues", floor: '#D2F1D9', sand: '#EBE3B4', walls: ['#2F9E5B', '#52B96E', '#1F8A55'], edge: 'dark', frame: ['#27A08A', '#0F5F55'], trail: ['#1F6FB5', '#5DB8F2'], motif: 'seaweed' },
  { name: "L'épave", floor: '#EBDCBF', sand: '#D9C597', walls: ['#B07A4B', '#8C5E3A', '#C99A67'], edge: 'dark', frame: ['#3C85A3', '#1B4B63'], trail: ['#1F6FB5', '#5DB8F2'], motif: 'plank' },
  { name: 'La grotte lumineuse', floor: '#DDD6F7', sand: '#C9BDF0', walls: ['#8E6CF2', '#4FB6E8', '#C77DFF'], edge: 'dark', frame: ['#5A47B0', '#241A55'], trail: ['#1F6FB5', '#5DB8F2'], motif: 'crystal' },
  { name: 'La banquise', floor: '#E8F7FF', sand: '#CBE9F8', walls: ['#9BD5F5', '#BEE6FA', '#7FC0E8'], edge: 'dark', frame: ['#4B8FC4', '#285C8F'], trail: ['#1F6FB5', '#5DB8F2'], motif: 'iceblock' },
  { name: 'Le volcan', floor: '#FFE6D5', sand: '#F3C9A8', walls: ['#E8503A', '#FF7A3D', '#5A4552'], edge: 'dark', frame: ['#7A3447', '#34182A'], trail: ['#1F6FB5', '#5DB8F2'], motif: 'rock' },
  { name: "Le jardin d'anémones", floor: '#FFE4F1', sand: '#F6CDE0', walls: ['#FF7BAC', '#C77DFF', '#FFB347'], edge: 'dark', frame: ['#3B8FBF', '#1A5A8A'], trail: ['#1F6FB5', '#5DB8F2'], motif: 'anemone' },
  { name: 'Les abysses', floor: '#1C2B5E', sand: '#2E4688', walls: ['#4FD1E6', '#9B8CFF', '#FF8AD8'], edge: 'light', frame: ['#0F1A3D', '#070C22'], trail: ['#FFF2A8', '#FFFFFF'], motif: 'jelly' },
  { name: 'Le palais du roi', floor: '#FFF3D6', sand: '#F0DDAA', walls: ['#F2C14E', '#E8A33D', '#F7D98B'], edge: 'dark', frame: ['#7B5BC4', '#3F2F80'], trail: ['#1F6FB5', '#5DB8F2'], motif: 'pearl' },
];

const wallEdge = (pal, c) => (pal.edge === 'light' ? mix(c, '#FFFFFF', 0.55) : mix(c, INK, 0.5));

/** Le contour d'un mur doit contraster au moins 3:1 avec le sol (consigne de l'issue #22) : on le vérifie sur chaque couleur de chaque monde. */
function checkContrast() {
  const out = [];
  for (const w of WORLDS) {
    for (const c of w.walls) {
      const ratio = contrast(w.floor, wallEdge(w, c));
      if (ratio < 3) throw new Error(`contraste mur/sol trop faible (${ratio.toFixed(2)}:1) : ${w.name}, mur ${c}, sol ${w.floor}`);
      out.push({ world: w.name, wall: c, ratio });
    }
    const tr = contrast(w.floor, w.trail[0]);
    if (tr < 3) throw new Error(`contraste trace/sol trop faible (${tr.toFixed(2)}:1) : ${w.name}`);
  }
  return out;
}

// ---------------------------------------------------------------- une grille de labyrinthe (même format que le futur fichier de niveaux : art ASCII)
function parseGrid(lines) {
  const rows = (lines.length - 1) / 2;
  const cols = (lines[0].length - 1) / 3;
  if (!Number.isInteger(rows) || !Number.isInteger(cols)) throw new Error('grille mal formée');
  for (const l of lines) if (l.length !== 3 * cols + 1) throw new Error(`ligne de longueur ${l.length}, attendu ${3 * cols + 1} : « ${l} »`);
  const h = Array.from({ length: rows + 1 }, (_, r) => Array.from({ length: cols }, (_, x) => lines[2 * r].slice(1 + 3 * x, 3 + 3 * x) === '--'));
  const v = Array.from({ length: rows }, (_, r) => Array.from({ length: cols + 1 }, (_, x) => lines[2 * r + 1][3 * x] === '|'));
  return { cols, rows, h, v };
}
function neighbors(g, x, y) {
  const out = [];
  if (x + 1 < g.cols && !g.v[y][x + 1]) out.push([x + 1, y]);
  if (x > 0 && !g.v[y][x]) out.push([x - 1, y]);
  if (y > 0 && !g.h[y][x]) out.push([x, y - 1]);
  if (y + 1 < g.rows && !g.h[y + 1][x]) out.push([x, y + 1]);
  return out;
}
function solve(g, start, exit) {
  const key = (c) => c[1] * g.cols + c[0];
  const prev = new Map([[key(start), null]]);
  const q = [start];
  for (let i = 0; i < q.length; i++) {
    const c = q[i];
    for (const n of neighbors(g, c[0], c[1])) {
      if (prev.has(key(n))) continue;
      prev.set(key(n), c);
      q.push(n);
    }
  }
  if (!prev.has(key(exit))) throw new Error('sortie inatteignable');
  if (prev.size !== g.cols * g.rows) throw new Error(`labyrinthe non relié : ${prev.size} cases sur ${g.cols * g.rows}`);
  const path = [];
  for (let c = exit; c; c = prev.get(key(c))) path.push(c);
  return path.reverse();
}
function deadEnds(g, start, exit) {
  const out = [];
  for (let y = 0; y < g.rows; y++) {
    for (let x = 0; x < g.cols; x++) {
      const isEnd = (x === start[0] && y === start[1]) || (x === exit[0] && y === exit[1]);
      if (!isEnd && neighbors(g, x, y).length === 1) out.push([x, y]);
    }
  }
  return out;
}

function hash(a, b) {
  let x = (Math.imul(a, 374761393) + Math.imul(b, 668265263)) | 0;
  x = Math.imul(x ^ (x >>> 13), 1274126177);
  return ((x ^ (x >>> 16)) >>> 0) / 4294967296;
}

// ---------------------------------------------------------------- le poisson-clown (vu du dessus, le nez vers le haut)
function fish(cx, cy, s, angle = 0, { scale = 1, shadow = true } = {}) {
  const u = s * scale;
  const f = (n) => fx(n * u);
  const id = nid('fb');
  const O = '#FF8A1F';
  const line = `stroke="${T.ink}" stroke-width="${fx(Math.max(1.2, 0.04 * u))}" stroke-linejoin="round"`;
  const tail = `<path d="M 0 ${f(0.3)} C ${f(-0.06)} ${f(0.4)} ${f(-0.25)} ${f(0.5)} ${f(-0.23)} ${f(0.68)} Q 0 ${f(0.6)} ${f(0.23)} ${f(0.68)} C ${f(0.25)} ${f(0.5)} ${f(0.06)} ${f(0.4)} 0 ${f(0.3)} Z" fill="${lighten(O, 0.1)}" ${line}/>`;
  const fin = (sx) => `<ellipse cx="${f(0.3 * sx)}" cy="${f(-0.07)}" rx="${f(0.1)}" ry="${f(0.18)}" transform="rotate(${32 * sx} ${f(0.3 * sx)} ${f(-0.07)})" fill="${lighten(O, 0.12)}" ${line}/>`;
  const bands = [[-0.17, 0.1], [0.1, 0.13], [0.33, 0.07]]
    .map(([c, th]) => `<rect x="${f(-0.3)}" y="${f(c - th / 2)}" width="${f(0.6)}" height="${f(th)}" fill="#fff"/>` +
      `<line x1="${f(-0.3)}" x2="${f(0.3)}" y1="${f(c - th / 2)}" y2="${f(c - th / 2)}" stroke="${T.ink}" stroke-width="${fx(Math.max(1, 0.03 * u))}"/>` +
      `<line x1="${f(-0.3)}" x2="${f(0.3)}" y1="${f(c + th / 2)}" y2="${f(c + th / 2)}" stroke="${T.ink}" stroke-width="${fx(Math.max(1, 0.03 * u))}"/>`)
    .join('');
  const eye = (sx) => `<circle cx="${f(0.135 * sx)}" cy="${f(-0.31)}" r="${f(0.064)}" fill="#fff" stroke="${T.ink}" stroke-width="${fx(Math.max(1, 0.025 * u))}"/>` +
    `<circle cx="${f(0.14 * sx)}" cy="${f(-0.325)}" r="${f(0.034)}" fill="${T.ink}"/><circle cx="${f(0.128 * sx)}" cy="${f(-0.337)}" r="${f(0.011)}" fill="#fff"/>`;
  const body = `<ellipse cx="0" cy="${f(-0.02)}" rx="${f(0.25)}" ry="${f(0.43)}"`;
  return (
    `<g transform="translate(${fx(cx)} ${fx(cy)}) rotate(${angle})">` +
    (shadow ? `<ellipse cx="0" cy="${f(0.08)}" rx="${f(0.29)}" ry="${f(0.5)}" fill="#000" opacity="0.16"/>` : '') +
    `<defs><radialGradient id="${id}" cx="0.38" cy="0.3" r="0.85"><stop offset="0" stop-color="${lighten(O, 0.38)}"/><stop offset="0.55" stop-color="${O}"/><stop offset="1" stop-color="${darken(O, 0.12)}"/></radialGradient>` +
    `<clipPath id="${id}c">${body}/></clipPath></defs>` +
    tail + fin(-1) + fin(1) +
    `${body} fill="url(#${id})"/>` +
    `<g clip-path="url(#${id}c)">${bands}</g>` +
    `${body} fill="none" ${line}/>` +
    `<rect x="${f(-0.032)}" y="${f(-0.14)}" width="${f(0.064)}" height="${f(0.42)}" rx="${f(0.032)}" fill="${darken(O, 0.1)}" opacity="0.55"/>` +
    eye(-1) + eye(1) +
    `</g>`
  );
}

// ---------------------------------------------------------------- l'anémone : la maison du poisson, et la sortie
function anemone(cx, cy, s, { glow = true, scale = 1, sparkles = true } = {}) {
  const u = s * scale;
  const f = (n) => fx(n * u);
  let out = '';
  if (glow) {
    const gid = nid('ag');
    out += `<defs><radialGradient id="${gid}"><stop offset="0" stop-color="#FFF2A8" stop-opacity="0.95"/><stop offset="0.5" stop-color="#FFE07A" stop-opacity="0.55"/><stop offset="1" stop-color="#FFE07A" stop-opacity="0"/></radialGradient></defs>` +
      `<circle cx="${fx(cx)}" cy="${fx(cy)}" r="${f(1.0)}" fill="url(#${gid})"/>`;
  }
  const ring = (n, r1, r2, w, off, cols, tip) => {
    let e = '';
    let fl = '';
    let tp = '';
    for (let i = 0; i < n; i++) {
      const a = off + (i * 2 * Math.PI) / n;
      const len = r2 * (0.9 + 0.12 * hash(i, n));
      const x1 = cx + Math.cos(a) * r1 * u;
      const y1 = cy + Math.sin(a) * r1 * u;
      const x2 = cx + Math.cos(a) * len * u;
      const y2 = cy + Math.sin(a) * len * u;
      e += `<line x1="${fx(x1)}" y1="${fx(y1)}" x2="${fx(x2)}" y2="${fx(y2)}" stroke="#7A2450" stroke-opacity="0.55" stroke-width="${f(w + 0.05)}" stroke-linecap="round"/>`;
      fl += `<line x1="${fx(x1)}" y1="${fx(y1)}" x2="${fx(x2)}" y2="${fx(y2)}" stroke="${cols[i % cols.length]}" stroke-width="${f(w)}" stroke-linecap="round"/>`;
      tp += `<circle cx="${fx(x2)}" cy="${fx(y2)}" r="${f(w * 0.5)}" fill="${tip}"/>`;
    }
    return e + fl + tp;
  };
  out += ring(14, 0.1, 0.42, 0.11, 0, ['#FF6FA0', '#FF9B6B'], '#FFF2A8');
  out += ring(10, 0.06, 0.3, 0.1, Math.PI / 10, ['#FF8FB5', '#FFB383'], '#FFFFFF');
  out += `<circle cx="${fx(cx)}" cy="${fx(cy)}" r="${f(0.15)}" fill="#C23A78" stroke="#7A2450" stroke-opacity="0.6" stroke-width="${f(0.03)}"/>` +
    `<ellipse cx="${fx(cx)}" cy="${fx(cy + 0.01 * u)}" rx="${f(0.07)}" ry="${f(0.05)}" fill="#8E1F57"/>` +
    `<ellipse cx="${fx(cx - 0.05 * u)}" cy="${fx(cy - 0.07 * u)}" rx="${f(0.05)}" ry="${f(0.025)}" fill="#fff" opacity="0.5"/>`;
  if (sparkles) {
    for (const [a, r, k] of [[-0.9, 0.62, 0.1], [0.5, 0.66, 0.08], [2.4, 0.6, 0.09], [3.7, 0.64, 0.07]]) {
      out += sparkle(cx + Math.cos(a) * r * u, cy + Math.sin(a) * r * u, k * u, '#FFFFFF');
    }
  }
  return out;
}

function sparkle(cx, cy, r, fill = '#fff', opacity = 0.95) {
  const k = r * 0.28;
  return `<path d="M ${fx(cx)} ${fx(cy - r)} Q ${fx(cx + k)} ${fx(cy - k)} ${fx(cx + r)} ${fx(cy)} Q ${fx(cx + k)} ${fx(cy + k)} ${fx(cx)} ${fx(cy + r)} Q ${fx(cx - k)} ${fx(cy + k)} ${fx(cx - r)} ${fx(cy)} Q ${fx(cx - k)} ${fx(cy - k)} ${fx(cx)} ${fx(cy - r)} Z" fill="${fill}" opacity="${opacity}"/>`;
}

// ---------------------------------------------------------------- la bouée du départ
function buoy(cx, cy, s) {
  const r = 0.17 * s;
  const w = 0.1 * s;
  const circ = 2 * Math.PI * r;
  return (
    `<ellipse cx="${fx(cx)}" cy="${fx(cy + r + w * 0.9)}" rx="${fx(r * 1.2)}" ry="${fx(r * 0.38)}" fill="#000" opacity="0.18"/>` +
    `<circle cx="${fx(cx)}" cy="${fx(cy)}" r="${fx(r)}" fill="none" stroke="${INK}" stroke-opacity="0.45" stroke-width="${fx(w + 3)}"/>` +
    `<circle cx="${fx(cx)}" cy="${fx(cy)}" r="${fx(r)}" fill="none" stroke="#fff" stroke-width="${fx(w)}"/>` +
    `<circle cx="${fx(cx)}" cy="${fx(cy)}" r="${fx(r)}" fill="none" stroke="#E8362F" stroke-width="${fx(w)}" stroke-dasharray="${fx(circ / 8)} ${fx(circ / 8)}"/>`
  );
}

// ---------------------------------------------------------------- les petits objets posés au fond des impasses (jamais sur la solution : ils ne donneraient pas la réponse)
function motif(kind, cx, cy, s, k = 0) {
  const f = (n) => fx(n * s);
  switch (kind) {
    case 'shell':
      return `<path d="M ${fx(cx - 0.2 * s)} ${fx(cy + 0.16 * s)} Q ${fx(cx - 0.27 * s)} ${fx(cy - 0.12 * s)} ${fx(cx - 0.12 * s)} ${fx(cy - 0.2 * s)} Q ${fx(cx)} ${fx(cy - 0.28 * s)} ${fx(cx + 0.12 * s)} ${fx(cy - 0.2 * s)} Q ${fx(cx + 0.27 * s)} ${fx(cy - 0.12 * s)} ${fx(cx + 0.2 * s)} ${fx(cy + 0.16 * s)} Z" fill="#FFD6E0" stroke="#D9577F" stroke-width="${fx(Math.max(1, 0.035 * s))}" stroke-linejoin="round"/>` +
        [-0.12, 0, 0.12].map((dx) => `<line x1="${fx(cx + dx * 0.4 * s)}" y1="${fx(cy + 0.13 * s)}" x2="${fx(cx + dx * 1.5 * s)}" y2="${fx(cy - 0.16 * s)}" stroke="#D9577F" stroke-width="${fx(Math.max(0.8, 0.025 * s))}" stroke-linecap="round" opacity="0.7"/>`).join('') +
        `<rect x="${fx(cx - 0.07 * s)}" y="${fx(cy + 0.13 * s)}" width="${f(0.14)}" height="${f(0.07)}" rx="${f(0.03)}" fill="#D9577F"/>`;
    case 'starfish': {
      const pts = [];
      for (let i = 0; i < 10; i++) {
        const a = -Math.PI / 2 + (i * Math.PI) / 5 + 0.2 * (k - 0.5);
        const r = (i % 2 ? 0.09 : 0.23) * s;
        pts.push(`${fx(cx + Math.cos(a) * r)},${fx(cy + Math.sin(a) * r)}`);
      }
      return `<polygon points="${pts.join(' ')}" fill="#FF9A4D" stroke="#C9591A" stroke-width="${fx(Math.max(1, 0.04 * s))}" stroke-linejoin="round"/>` +
        [0, 1, 2].map((i) => `<circle cx="${fx(cx + (i - 1) * 0.06 * s)}" cy="${fx(cy + (i % 2 ? 0.03 : -0.02) * s)}" r="${f(0.018)}" fill="#FFE0B8"/>`).join('');
    }
    case 'seaweed':
      return [-0.1, 0, 0.1].map((dx, i) => `<path d="M ${fx(cx + dx * s)} ${fx(cy + 0.22 * s)} Q ${fx(cx + (dx - 0.12 + i * 0.05) * s)} ${fx(cy)} ${fx(cx + (dx + 0.04) * s)} ${fx(cy - (0.2 + 0.05 * i) * s)}" fill="none" stroke="${['#2F9E5B', '#52B96E', '#1F8A55'][i]}" stroke-width="${f(0.075)}" stroke-linecap="round"/>`).join('');
    case 'plank':
      return `<g transform="rotate(${-24 + 40 * k} ${fx(cx)} ${fx(cy)})"><rect x="${fx(cx - 0.24 * s)}" y="${fx(cy - 0.08 * s)}" width="${f(0.48)}" height="${f(0.16)}" rx="${f(0.04)}" fill="#B07A4B" stroke="#5E3B1F" stroke-width="${fx(Math.max(1, 0.035 * s))}"/>` +
        `<line x1="${fx(cx - 0.2 * s)}" y1="${fx(cy)}" x2="${fx(cx + 0.2 * s)}" y2="${fx(cy)}" stroke="#5E3B1F" stroke-width="${fx(Math.max(0.8, 0.02 * s))}" opacity="0.5"/>` +
        `<circle cx="${fx(cx - 0.18 * s)}" cy="${fx(cy)}" r="${f(0.02)}" fill="#5E3B1F"/><circle cx="${fx(cx + 0.18 * s)}" cy="${fx(cy)}" r="${f(0.02)}" fill="#5E3B1F"/></g>`;
    case 'crystal':
      return [[-0.1, 0.02, 0.12, '#8E6CF2'], [0.08, -0.02, 0.15, '#4FB6E8'], [0, 0.08, 0.09, '#C77DFF']]
        .map(([dx, dy, h, c]) => `<polygon points="${fx(cx + dx * s)},${fx(cy + (dy - h * 1.3) * s)} ${fx(cx + (dx + h * 0.55) * s)},${fx(cy + dy * s)} ${fx(cx + dx * s)},${fx(cy + (dy + h * 0.8) * s)} ${fx(cx + (dx - h * 0.55) * s)},${fx(cy + dy * s)}" fill="${c}" stroke="${darken(c, 0.35)}" stroke-width="${fx(Math.max(1, 0.03 * s))}" stroke-linejoin="round"/>` +
          `<line x1="${fx(cx + dx * s)}" y1="${fx(cy + (dy - h * 1.3) * s)}" x2="${fx(cx + (dx - h * 0.2) * s)}" y2="${fx(cy + dy * s)}" stroke="#fff" stroke-opacity="0.6" stroke-width="${fx(Math.max(0.8, 0.02 * s))}"/>`).join('');
    case 'iceblock':
      return `<g transform="rotate(${-10 + 20 * k} ${fx(cx)} ${fx(cy)})"><rect x="${fx(cx - 0.17 * s)}" y="${fx(cy - 0.17 * s)}" width="${f(0.34)}" height="${f(0.34)}" rx="${f(0.07)}" fill="#EAF8FF" stroke="#6FA9CF" stroke-width="${fx(Math.max(1, 0.04 * s))}"/>` +
        `<line x1="${fx(cx - 0.09 * s)}" y1="${fx(cy - 0.08 * s)}" x2="${fx(cx - 0.02 * s)}" y2="${fx(cy - 0.12 * s)}" stroke="#fff" stroke-width="${f(0.04)}" stroke-linecap="round"/></g>`;
    case 'rock':
      return `<ellipse cx="${fx(cx)}" cy="${fx(cy)}" rx="${f(0.2)}" ry="${f(0.16)}" fill="#4A3B47" stroke="${INK}" stroke-width="${fx(Math.max(1, 0.035 * s))}"/>` +
        `<path d="M ${fx(cx - 0.1 * s)} ${fx(cy - 0.02 * s)} L ${fx(cx - 0.02 * s)} ${fx(cy + 0.03 * s)} L ${fx(cx + 0.05 * s)} ${fx(cy - 0.05 * s)} L ${fx(cx + 0.12 * s)} ${fx(cy + 0.02 * s)}" fill="none" stroke="#FF7A3D" stroke-width="${f(0.04)}" stroke-linecap="round" stroke-linejoin="round"/>`;
    case 'anemone': {
      let e = '';
      for (let i = 0; i < 9; i++) {
        const a = (i * 2 * Math.PI) / 9;
        e += `<line x1="${fx(cx)}" y1="${fx(cy)}" x2="${fx(cx + Math.cos(a) * 0.2 * s)}" y2="${fx(cy + Math.sin(a) * 0.2 * s)}" stroke="${i % 2 ? '#FF9B6B' : '#FF6FA0'}" stroke-width="${f(0.07)}" stroke-linecap="round"/>`;
      }
      return e + `<circle cx="${fx(cx)}" cy="${fx(cy)}" r="${f(0.08)}" fill="#C23A78"/>`;
    }
    case 'jelly': {
      const gid = nid('jg');
      let e = `<defs><radialGradient id="${gid}"><stop offset="0" stop-color="#9BF0FF" stop-opacity="0.8"/><stop offset="1" stop-color="#9BF0FF" stop-opacity="0"/></radialGradient></defs><circle cx="${fx(cx)}" cy="${fx(cy)}" r="${f(0.34)}" fill="url(#${gid})"/>`;
      for (const dx of [-0.1, -0.035, 0.035, 0.1]) e += `<path d="M ${fx(cx + dx * s)} ${fx(cy + 0.02 * s)} q ${fx(0.04 * s)} ${fx(0.08 * s)} 0 ${fx(0.14 * s)} t 0 ${fx(0.1 * s)}" fill="none" stroke="#C9F6FF" stroke-width="${f(0.03)}" stroke-linecap="round" opacity="0.9"/>`;
      return e + `<path d="M ${fx(cx - 0.15 * s)} ${fx(cy + 0.03 * s)} A ${f(0.15)} ${f(0.15)} 0 0 1 ${fx(cx + 0.15 * s)} ${fx(cy + 0.03 * s)} Z" fill="#7FE8FF" stroke="#E8FFFF" stroke-width="${fx(Math.max(1, 0.03 * s))}" stroke-linejoin="round"/>`;
    }
    case 'pearl':
      return motif('shell', cx, cy + 0.03 * s, s, k) + `<circle cx="${fx(cx)}" cy="${fx(cy - 0.05 * s)}" r="${f(0.075)}" fill="#fff" stroke="#BFA5D9" stroke-width="${fx(Math.max(0.8, 0.025 * s))}"/><circle cx="${fx(cx - 0.025 * s)}" cy="${fx(cy - 0.075 * s)}" r="${f(0.02)}" fill="#fff"/>`;
    default:
      return '';
  }
}

// ---------------------------------------------------------------- les murs : coraux épais à bouts ronds, contour obligatoire
function walls(g, s, ox, oy, pal) {
  const t = s * 0.26;
  const segs = [];
  const verts = new Map();
  const addV = (x, y, o) => {
    const k = `${x},${y}`;
    const e = verts.get(k) || { n: 0, h: 0, v: 0, x, y };
    e.n++;
    e[o] = 1;
    verts.set(k, e);
  };
  for (let r = 0; r <= g.rows; r++) for (let x = 0; x < g.cols; x++) if (g.h[r][x]) { segs.push({ x1: x, y1: r, x2: x + 1, y2: r, o: 'h' }); addV(x, r, 'h'); addV(x + 1, r, 'h'); }
  for (let r = 0; r < g.rows; r++) for (let x = 0; x <= g.cols; x++) if (g.v[r][x]) { segs.push({ x1: x, y1: r, x2: x, y2: r + 1, o: 'v' }); addV(x, r, 'v'); addV(x, r + 1, 'v'); }
  const colorAt = (mx, my) => pal.walls[(Math.floor(mx / 2) + Math.floor(my / 2) + 30) % 3];
  const P = (vx, vy) => [ox + vx * s, oy + vy * s];
  let ledge = '';
  let edge = '';
  let fill = '';
  let detail = '';
  for (const sg of segs) {
    const [x1, y1] = P(sg.x1, sg.y1);
    const [x2, y2] = P(sg.x2, sg.y2);
    const c = colorAt((sg.x1 + sg.x2) / 2, (sg.y1 + sg.y2) / 2);
    const e = wallEdge(pal, c);
    const ln = (w, col, dy = 0) => `<line x1="${fx(x1)}" y1="${fx(y1 + dy)}" x2="${fx(x2)}" y2="${fx(y2 + dy)}" stroke="${col}" stroke-width="${fx(w)}" stroke-linecap="round"/>`;
    ledge += ln(t + 3.6, darken(e, 0.3), Math.max(3, s * 0.07));
    edge += ln(t + 3.6, e);
    fill += ln(t, c);
    const off = t * 0.16;
    const ins = t * 0.3;
    const hl = lighten(c, 0.5);
    detail += sg.o === 'h'
      ? `<line x1="${fx(x1 + ins)}" y1="${fx(y1 - off)}" x2="${fx(x2 - ins)}" y2="${fx(y2 - off)}" stroke="${hl}" stroke-opacity="0.6" stroke-width="${fx(t * 0.26)}" stroke-linecap="round"/>`
      : `<line x1="${fx(x1 - off)}" y1="${fx(y1 + ins)}" x2="${fx(x2 - off)}" y2="${fx(y2 - ins)}" stroke="${hl}" stroke-opacity="0.6" stroke-width="${fx(t * 0.26)}" stroke-linecap="round"/>`;
    const hv = hash(Math.round(sg.x1 * 10 + sg.y1), sg.o === 'h' ? 1 : 2);
    if (hv > 0.62 && s >= 30) {
      const mx = (x1 + x2) / 2;
      const my = (y1 + y2) / 2;
      detail += `<circle cx="${fx(mx + (sg.o === 'h' ? 0 : -off))}" cy="${fx(my + (sg.o === 'h' ? t * 0.02 : 0))}" r="${fx(t * 0.1)}" fill="${lighten(c, 0.75)}" opacity="0.85"/>`;
    }
  }
  // têtes de corail aux jonctions (et à quelques coudes)
  for (const e of verts.values()) {
    const corner = e.n === 2 && e.h && e.v;
    if (!(e.n >= 3 || (corner && hash(e.x, e.y) > 0.55))) continue;
    const [cx, cy] = P(e.x, e.y);
    const c = colorAt(e.x, e.y);
    const ed = wallEdge(pal, c);
    const r = t * (e.n >= 3 ? 0.74 : 0.68);
    ledge += `<circle cx="${fx(cx)}" cy="${fx(cy + Math.max(3, s * 0.07))}" r="${fx(r + 1.8)}" fill="${darken(ed, 0.3)}"/>`;
    edge += `<circle cx="${fx(cx)}" cy="${fx(cy)}" r="${fx(r + 1.8)}" fill="${ed}"/>`;
    fill += `<circle cx="${fx(cx)}" cy="${fx(cy)}" r="${fx(r)}" fill="${c}"/>`;
    detail += `<circle cx="${fx(cx - r * 0.28)}" cy="${fx(cy - r * 0.3)}" r="${fx(r * 0.3)}" fill="${lighten(c, 0.55)}" opacity="0.7"/>`;
  }
  return ledge + edge + fill + detail;
}

// ---------------------------------------------------------------- le sol
function floor(g, s, ox, oy, pal) {
  let out = `<rect x="${fx(ox)}" y="${fx(oy)}" width="${fx(g.cols * s)}" height="${fx(g.rows * s)}" fill="${pal.floor}"/>`;
  const alt = mix(pal.floor, '#FFFFFF', 0.35);
  const altOpacity = luminance(pal.floor) < 0.2 ? 0.14 : 0.55; // sol sombre (abysses) : damier à peine visible
  for (let y = 0; y < g.rows; y++) {
    for (let x = 0; x < g.cols; x++) {
      if ((x + y) % 2) out += `<rect x="${fx(ox + x * s + 1)}" y="${fx(oy + y * s + 1)}" width="${fx(s - 2)}" height="${fx(s - 2)}" rx="${fx(s * 0.12)}" fill="${alt}" opacity="${altOpacity}"/>`;
      if (s >= 22) for (let i = 0; i < 2; i++) {
        const hx = hash(x * 7 + i, y * 13 + 5);
        const hy = hash(x * 11 + i, y * 17 + 9);
        if (hx > 0.45) out += `<circle cx="${fx(ox + (x + 0.15 + 0.7 * hx) * s)}" cy="${fx(oy + (y + 0.15 + 0.7 * hy) * s)}" r="${fx(Math.max(0.9, s * 0.03))}" fill="${pal.sand}" opacity="0.8"/>`;
      }
    }
  }
  return out;
}

// ---------------------------------------------------------------- la trace : un ruban d'eau jalonné de bulles
function roundedPath(pts, r) {
  let d = `M ${fx(pts[0][0])} ${fx(pts[0][1])}`;
  for (let i = 1; i < pts.length - 1; i++) {
    const [px, py] = pts[i - 1];
    const [cx, cy] = pts[i];
    const [nx, ny] = pts[i + 1];
    const d1 = [cx - px, cy - py];
    const d2 = [nx - cx, ny - cy];
    const l1 = Math.hypot(d1[0], d1[1]);
    const l2 = Math.hypot(d2[0], d2[1]);
    if (Math.abs(d1[0] * d2[1] - d1[1] * d2[0]) < 1e-6) continue;
    const rr = Math.min(r, l1 / 2, l2 / 2);
    d += ` L ${fx(cx - (d1[0] / l1) * rr)} ${fx(cy - (d1[1] / l1) * rr)} Q ${fx(cx)} ${fx(cy)} ${fx(cx + (d2[0] / l2) * rr)} ${fx(cy + (d2[1] / l2) * rr)}`;
  }
  const last = pts[pts.length - 1];
  return d + ` L ${fx(last[0])} ${fx(last[1])}`;
}
function along(pts, step) {
  const out = [];
  let carry = step * 0.5;
  for (let i = 1; i < pts.length; i++) {
    const [x1, y1] = pts[i - 1];
    const [x2, y2] = pts[i];
    const L = Math.hypot(x2 - x1, y2 - y1);
    let d = carry;
    while (d <= L) {
      out.push([x1 + ((x2 - x1) * d) / L, y1 + ((y2 - y1) * d) / L, (x2 - x1) / L, (y2 - y1) / L]);
      d += step;
    }
    carry = d - L;
  }
  return out;
}
/** `kind` : 'water' (le ruban bleu du jeu), 'gold' (trace parfaite : aucune impasse). `pal.trail` donne le contour et le cœur du ruban. */
function ribbon(pts, s, pal, kind = 'water') {
  if (pts.length < 2) return '';
  const tw = s * 0.34;
  const [edgeC, coreC] = kind === 'gold' ? ['#C98A00', '#FFD54A'] : pal.trail;
  const d = roundedPath(pts, s * 0.3);
  let out = `<path d="${d}" fill="none" stroke="${edgeC}" stroke-width="${fx(tw + 4)}" stroke-linecap="round" stroke-linejoin="round" opacity="0.95"/>` +
    `<path d="${d}" fill="none" stroke="${coreC}" stroke-width="${fx(tw)}" stroke-linecap="round" stroke-linejoin="round"/>` +
    `<path d="${d}" fill="none" stroke="#fff" stroke-opacity="0.45" stroke-width="${fx(tw * 0.3)}" stroke-linecap="round" stroke-linejoin="round" transform="translate(${fx(-tw * 0.1)} ${fx(-tw * 0.1)})"/>`;
  along(pts, s * 0.4).forEach(([x, y, dx, dy], i) => {
    const off = (hash(i, 3) - 0.5) * tw * 0.55;
    const r = Math.max(1.4, tw * (0.1 + 0.13 * hash(i, 5)));
    const bx = x - dy * off;
    const by = y + dx * off;
    out += `<circle cx="${fx(bx)}" cy="${fx(by)}" r="${fx(r)}" fill="#fff" fill-opacity="0.55" stroke="#fff" stroke-width="${fx(Math.max(0.8, r * 0.22))}"/>` +
      `<circle cx="${fx(bx - r * 0.3)}" cy="${fx(by - r * 0.3)}" r="${fx(r * 0.28)}" fill="#fff"/>`;
  });
  if (kind === 'gold') along(pts, s * 1.1).forEach(([x, y], i) => { out += sparkle(x + (hash(i, 8) - 0.5) * tw, y + (hash(i, 9) - 0.5) * tw, tw * 0.42, '#FFFFFF'); });
  return out;
}

// ---------------------------------------------------------------- le plateau complet
/**
 * g : grille (parseGrid) ; start, exit : [x, y] ; pal : un monde de WORLDS.
 * trail : cases (liste de [x, y]) déjà tracées depuis le départ ; hint : cases à montrer comme indice ;
 * fish : 'start' | 'exit' | null ; finger : montre le doigt au bout de la trace.
 */
function mazeBoard({ g, start, exit, s, ox, oy, pal, trail = [], trailKind = 'water', hint = [], fish: fishAt = 'start', finger = false, motifs = true, angle = null, buoyOn = true }) {
  const C = ([x, y]) => [ox + (x + 0.5) * s, oy + (y + 0.5) * s];
  let out = '';
  const w = g.cols * s;
  const h = g.rows * s;
  // la lumière qui sort par l'ouverture de la sortie, et l'ombre portée du bassin
  const [ex, ey] = C(exit);
  const lid = nid('lg');
  out += `<defs><linearGradient id="${lid}" x1="0" y1="1" x2="0" y2="0"><stop offset="0" stop-color="#FFF2A8" stop-opacity="0.65"/><stop offset="1" stop-color="#FFF2A8" stop-opacity="0"/></linearGradient></defs>` +
    `<polygon points="${fx(ex - s * 0.32)},${fx(oy)} ${fx(ex + s * 0.32)},${fx(oy)} ${fx(ex + s * 0.7)},${fx(oy - s * 1.0)} ${fx(ex - s * 0.7)},${fx(oy - s * 1.0)}" fill="url(#${lid})"/>`;
  out += floor(g, s, ox, oy, pal);
  // décor discret (opacité réduite, plus petit) : il ne doit jamais ressembler à quelque chose à ramasser
  if (motifs) {
    out += '<g opacity="0.7">';
    deadEnds(g, start, exit).forEach(([x, y], i) => {
      if (hash(x * 31 + 7, y * 17 + 3) < 0.28) return;
      const [cx, cy] = C([x, y]);
      out += motif(pal.motif, cx + (hash(x, y) - 0.5) * s * 0.12, cy + (hash(y, x) - 0.5) * s * 0.12, s * 0.82, hash(x + i, y));
    });
    out += '</g>';
  }
  if (buoyOn) out += buoy(C(start)[0], oy + h + s * 0.6, s);
  if (trail.length > 1) out += ribbon(trail.map(C), s, pal, trailKind);
  if (hint.length) {
    hint.forEach((cell, i) => {
      const [x, y] = C(cell);
      const r = s * (0.17 - i * 0.012);
      out += `<circle cx="${fx(x)}" cy="${fx(y)}" r="${fx(r * 1.9)}" fill="#FFC83D" opacity="0.25"/><circle cx="${fx(x)}" cy="${fx(y)}" r="${fx(r)}" fill="#FFC83D" stroke="#fff" stroke-width="${fx(Math.max(1.5, s * 0.04))}"/>`;
    });
  }
  out += walls(g, s, ox, oy, pal);
  out += anemone(ex, ey, s * 1.05, { glow: true });
  if (fishAt === 'start') {
    const [fx0, fy0] = C(start);
    out += fish(fx0, fy0 - s * 0.04, s, angle === null ? 0 : angle, { scale: 0.82 });
  } else if (fishAt === 'exit') {
    out += fish(ex, ey + s * 0.02, s, 0, { scale: 0.8 });
  }
  if (finger && trail.length) {
    const [hx, hy] = C(trail[trail.length - 1]);
    out += `<circle cx="${fx(hx)}" cy="${fx(hy)}" r="${fx(s * 0.36)}" fill="#fff" fill-opacity="0.9" stroke="${pal.trail[0]}" stroke-width="3"/>` +
      `<circle cx="${fx(hx + s * 0.18)}" cy="${fx(hy + s * 0.34)}" r="${fx(s * 0.46)}" fill="#fff" fill-opacity="0.28" stroke="#fff" stroke-opacity="0.85" stroke-width="3"/>`;
  }
  return out;
}

// ---------------------------------------------------------------- le fond marin d'un écran de jeu
function sea(pal, W, H) {
  const gid = nid('sg');
  const rid = nid('sr');
  let out = `<defs><linearGradient id="${gid}" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="${pal.frame[0]}"/><stop offset="1" stop-color="${pal.frame[1]}"/></linearGradient>` +
    `<linearGradient id="${rid}" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="#fff" stop-opacity="0.14"/><stop offset="1" stop-color="#fff" stop-opacity="0"/></linearGradient></defs><rect width="${W}" height="${H}" fill="url(#${gid})"/>`;
  // rayons de lumière qui s'effacent en descendant (pas de bord net)
  for (const [x, w] of [[40, 90], [170, 60], [280, 110]]) {
    out += `<polygon points="${x},0 ${x + w},0 ${x + w * 2.2},${H} ${x - w * 0.8},${H}" fill="url(#${rid})"/>`;
  }
  for (let i = 0; i < 22; i++) {
    const x = hash(i, 1) * W;
    const y = hash(i, 2) * H;
    const r = 2 + hash(i, 3) * 7;
    out += `<circle cx="${fx(x)}" cy="${fx(y)}" r="${fx(r)}" fill="#fff" fill-opacity="0.1" stroke="#fff" stroke-opacity="0.35" stroke-width="1.4"/><circle cx="${fx(x - r * 0.3)}" cy="${fx(y - r * 0.3)}" r="${fx(r * 0.25)}" fill="#fff" opacity="0.5"/>`;
  }
  return out;
}

module.exports = {
  WORLDS, INK, wallEdge, contrast, checkContrast,
  parseGrid, neighbors, solve, deadEnds, hash,
  fish, anemone, sparkle, buoy, motif, walls, floor, ribbon, mazeBoard, sea, roundedPath,
  text,
};
