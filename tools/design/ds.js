// Design system « Petits Jeux » : jetons (couleurs), dessin des voitures, du plateau, des boutons, des étoiles et des avatars.
// Tout est du SVG généré : ce sont les maquettes de référence (docs/design). Les mêmes proportions sont reprises en Kotlin
// (games/parking/ui/CarDrawing.kt, core/ui/*) : si on change un dessin ici, on le change là-bas.
'use strict';

const T = {
  sun: '#FFC83D',
  sunLight: '#FFE07A',
  sunDark: '#F2A91B',
  ink: '#2B2D42',
  inkSoft: '#5B6079',
  white: '#FFFFFF',
  panel: '#FFF8E6',
  panelEdge: '#E8D5A6',
  asphalt: '#7C879B',
  asphaltDark: '#5B667B',
  asphaltLight: '#8E99AD',
  line: '#F4F6FA',
  glass: '#33425F',
  green: '#4CD964',
  greenDark: '#2FA845',
  blue: '#3E8BFF',
  blueDark: '#2B66D1',
  red: '#FF5A4A',
  redDark: '#CF3A2B',
  gray: '#C9CDD8',
  grayDark: '#9AA0B2',
  star: '#FFC107',
  starDark: '#E59A00',
};

const CAR_COLORS = {
  vert: '#6BD23C',
  orange: '#FF8A1F',
  cyan: '#2BC4EE',
  bleu: '#2D5FE0',
  rose: '#F0468A',
  jaune: '#FFD41F',
  violet: '#9A4DE3',
  turquoise: '#14B8A6',
  rouge: '#E8362F',
};

const fx = (n) => Math.round(n * 100) / 100;

function rgb(hex) {
  const h = hex.replace('#', '');
  return [0, 2, 4].map((i) => parseInt(h.slice(i, i + 2), 16));
}
function mix(hex, other, t) {
  const a = rgb(hex);
  const b = rgb(other);
  const c = a.map((v, i) => Math.round(v + (b[i] - v) * t));
  return '#' + c.map((v) => v.toString(16).padStart(2, '0')).join('');
}
const lighten = (c, t) => mix(c, '#FFFFFF', t);
const darken = (c, t) => mix(c, '#000000', t);

let uid = 0;
const nextId = (p) => `${p}${++uid}`;

const esc = (s) => String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');

// ---------------------------------------------------------------- texte
function text(x, y, s, { size = 24, weight = 600, fill = T.ink, anchor = 'middle', stroke = null, strokeWidth = 0, opacity = 1, spacing = 0 } = {}) {
  const st = stroke ? ` stroke="${stroke}" stroke-width="${strokeWidth}" stroke-linejoin="round" paint-order="stroke"` : '';
  return `<text x="${fx(x)}" y="${fx(y)}" font-family="Fredoka, sans-serif" font-size="${size}" font-weight="${weight}" fill="${fill}" text-anchor="${anchor}" opacity="${opacity}" letter-spacing="${spacing}"${st}>${esc(s)}</text>`;
}

// ---------------------------------------------------------------- étoiles
function starPath(cx, cy, r) {
  const pts = [];
  for (let i = 0; i < 10; i++) {
    const a = -Math.PI / 2 + (i * Math.PI) / 5;
    const rr = i % 2 === 0 ? r : r * 0.48;
    pts.push(`${fx(cx + rr * Math.cos(a))},${fx(cy + rr * Math.sin(a))}`);
  }
  return pts.join(' ');
}
function star(cx, cy, r, on = true) {
  if (!on) {
    return `<polygon points="${starPath(cx, cy, r)}" fill="#D9DCE6" stroke="#B8BDCC" stroke-width="${fx(r * 0.14)}" stroke-linejoin="round"/>`;
  }
  return (
    `<polygon points="${starPath(cx, cy + r * 0.1, r)}" fill="${T.starDark}" stroke="${T.starDark}" stroke-width="${fx(r * 0.2)}" stroke-linejoin="round"/>` +
    `<polygon points="${starPath(cx, cy, r)}" fill="${T.star}" stroke="#FFE066" stroke-width="${fx(r * 0.12)}" stroke-linejoin="round"/>`
  );
}

// ---------------------------------------------------------------- boutons et panneaux
function button({ x, y, w, h, label, color = T.green, dark = T.greenDark, size = 26, fill = T.white, icon = '' }) {
  const ledge = Math.max(5, h * 0.09);
  const r = h / 2;
  return (
    `<g>` +
    `<rect x="${x}" y="${fx(y + ledge)}" width="${w}" height="${h}" rx="${r}" fill="${dark}"/>` +
    `<rect x="${x}" y="${y}" width="${w}" height="${h}" rx="${r}" fill="${color}"/>` +
    `<rect x="${fx(x + h * 0.25)}" y="${fx(y + h * 0.1)}" width="${fx(w - h * 0.5)}" height="${fx(h * 0.22)}" rx="${fx(h * 0.11)}" fill="#fff" opacity="0.28"/>` +
    icon +
    (label ? text(x + w / 2, y + h / 2 + size * 0.34, label, { size, weight: 700, fill, stroke: darken(color, 0.35), strokeWidth: 0 }) : '') +
    `</g>`
  );
}
function roundButton({ cx, cy, r = 24, color = T.blue, dark = T.blueDark, icon = '' }) {
  return (
    `<circle cx="${cx}" cy="${cy + 4}" r="${r}" fill="${dark}"/>` +
    `<circle cx="${cx}" cy="${cy}" r="${r}" fill="${color}"/>` +
    `<ellipse cx="${fx(cx - r * 0.2)}" cy="${fx(cy - r * 0.5)}" rx="${fx(r * 0.5)}" ry="${fx(r * 0.2)}" fill="#fff" opacity="0.3"/>` +
    icon
  );
}
function panel({ x, y, w, h, r = 28, fill = T.panel, edge = T.panelEdge, ledge = 6 }) {
  return (
    `<rect x="${x}" y="${y + ledge}" width="${w}" height="${h}" rx="${r}" fill="${edge}"/>` +
    `<rect x="${x}" y="${y}" width="${w}" height="${h}" rx="${r}" fill="${fill}"/>`
  );
}

// Icônes simples (centrées sur 0,0, taille ~ 1)
const icons = {
  back: (cx, cy, s = 1) => `<path d="M ${fx(cx + 5 * s)} ${fx(cy - 11 * s)} L ${fx(cx - 6 * s)} ${cy} L ${fx(cx + 5 * s)} ${fx(cy + 11 * s)}" fill="none" stroke="#fff" stroke-width="${fx(5 * s)}" stroke-linecap="round" stroke-linejoin="round"/>`,
  restart: (cx, cy, s = 1) =>
    `<path d="M ${fx(cx + 9 * s)} ${fx(cy - 4 * s)} A ${fx(10 * s)} ${fx(10 * s)} 0 1 0 ${fx(cx + 8 * s)} ${fx(cy + 6 * s)}" fill="none" stroke="#fff" stroke-width="${fx(4 * s)}" stroke-linecap="round"/>` +
    `<polygon points="${fx(cx + 13 * s)},${fx(cy - 11 * s)} ${fx(cx + 13 * s)},${fx(cy + 1 * s)} ${fx(cx + 3 * s)},${fx(cy - 4 * s)}" fill="#fff"/>`,
  bulb: (cx, cy, s = 1) =>
    `<circle cx="${cx}" cy="${fx(cy - 3 * s)}" r="${fx(9 * s)}" fill="#fff"/>` +
    `<rect x="${fx(cx - 5 * s)}" y="${fx(cy + 5 * s)}" width="${fx(10 * s)}" height="${fx(6 * s)}" rx="${fx(2 * s)}" fill="#fff"/>`,
  gear: (cx, cy, s = 1) => {
    let g = '';
    for (let i = 0; i < 8; i++) {
      g += `<rect x="${fx(cx - 2.6 * s)}" y="${fx(cy - 12 * s)}" width="${fx(5.2 * s)}" height="${fx(7 * s)}" rx="${fx(1.6 * s)}" fill="#fff" transform="rotate(${i * 45} ${cx} ${cy})"/>`;
    }
    return g + `<circle cx="${cx}" cy="${cy}" r="${fx(8 * s)}" fill="#fff"/><circle cx="${cx}" cy="${cy}" r="${fx(3.6 * s)}" fill="${T.blue}"/>`;
  },
  lock: (cx, cy, s = 1) =>
    `<path d="M ${fx(cx - 6 * s)} ${fx(cy - 2 * s)} V ${fx(cy - 7 * s)} a ${fx(6 * s)} ${fx(6 * s)} 0 0 1 ${fx(12 * s)} 0 V ${fx(cy - 2 * s)}" fill="none" stroke="#7B8196" stroke-width="${fx(3.2 * s)}" stroke-linecap="round"/>` +
    `<rect x="${fx(cx - 9 * s)}" y="${fx(cy - 3 * s)}" width="${fx(18 * s)}" height="${fx(14 * s)}" rx="${fx(3.5 * s)}" fill="#7B8196"/>`,
  plus: (cx, cy, s = 1, fill = T.inkSoft) =>
    `<rect x="${fx(cx - 12 * s)}" y="${fx(cy - 2.5 * s)}" width="${fx(24 * s)}" height="${fx(5 * s)}" rx="${fx(2.5 * s)}" fill="${fill}"/>` +
    `<rect x="${fx(cx - 2.5 * s)}" y="${fx(cy - 12 * s)}" width="${fx(5 * s)}" height="${fx(24 * s)}" rx="${fx(2.5 * s)}" fill="${fill}"/>`,
  people: (cx, cy, s = 1) =>
    `<circle cx="${fx(cx - 6 * s)}" cy="${fx(cy - 4 * s)}" r="${fx(5 * s)}" fill="#fff"/><path d="M ${fx(cx - 15 * s)} ${fx(cy + 11 * s)} a ${fx(9 * s)} ${fx(8 * s)} 0 0 1 ${fx(18 * s)} 0 Z" fill="#fff"/>` +
    `<circle cx="${fx(cx + 8 * s)} " cy="${fx(cy - 2 * s)}" r="${fx(4 * s)}" fill="#fff" opacity="0.85"/><path d="M ${fx(cx + 1 * s)} ${fx(cy + 11 * s)} a ${fx(7 * s)} ${fx(6 * s)} 0 0 1 ${fx(14 * s)} 0 Z" fill="#fff" opacity="0.85"/>`,
};

// ---------------------------------------------------------------- voiture
// Repère local : centre de la voiture en (0,0), vue de dessus, nez vers le haut (y négatif). Taille d'une case = s.
// `len` = nombre de cases (2 = voiture, 3 = minibus). L'épaisseur (la « tranche » sombre) est TOUJOURS dessinée vers le bas de l'écran.
function carLayers(len, color, s, { flash = false } = {}) {
  const W = s * 0.8;
  const L = s * len - s * 0.2;
  const r = s * 0.27;
  const depth = s * 0.11;
  const body = flash ? mix(color, '#FF2D1F', 0.55) : color;
  const gid = nextId('cg');
  const wheel = (x, y) => `<rect x="${fx(x - s * 0.07)}" y="${fx(y - s * 0.15)}" width="${fx(s * 0.14)}" height="${fx(s * 0.3)}" rx="${fx(s * 0.06)}" fill="#262938"/>`;
  const wy = L / 2 - s * 0.34;
  const wheelsAt = (dy) => wheel(-W / 2, -wy + dy) + wheel(W / 2, -wy + dy) + wheel(-W / 2, wy + dy) + wheel(W / 2, wy + dy);
  const cabinLen = len === 2 ? L * 0.46 : L * 0.66;
  const cabinW = W * 0.74;
  const cabinY = len === 2 ? L * 0.06 : L * 0.04;
  const glassFront = `<path d="M ${fx(-cabinW / 2 + s * 0.06)} ${fx(cabinY - cabinLen / 2 + s * 0.2)} Q 0 ${fx(cabinY - cabinLen / 2 - s * 0.02)} ${fx(cabinW / 2 - s * 0.06)} ${fx(cabinY - cabinLen / 2 + s * 0.2)} L ${fx(cabinW / 2 - s * 0.1)} ${fx(cabinY - cabinLen / 2 + s * 0.42)} L ${fx(-cabinW / 2 + s * 0.1)} ${fx(cabinY - cabinLen / 2 + s * 0.42)} Z" fill="${T.glass}"/>`;
  const glassRear = `<rect x="${fx(-cabinW / 2 + s * 0.09)}" y="${fx(cabinY + cabinLen / 2 - s * 0.26)}" width="${fx(cabinW - s * 0.18)}" height="${fx(s * 0.14)}" rx="${fx(s * 0.05)}" fill="${T.glass}"/>`;
  let roofLines = '';
  if (len === 3) {
    const y0 = cabinY - cabinLen / 2 + s * 0.42;
    const y1 = cabinY + cabinLen / 2 - s * 0.26;
    roofLines = `<rect x="${fx(-cabinW / 2 + s * 0.11)}" y="${fx(y0 + (y1 - y0) * 0.12)}" width="${fx(cabinW - s * 0.22)}" height="${fx((y1 - y0) * 0.34)}" rx="${fx(s * 0.05)}" fill="${lighten(color, 0.3)}" opacity="0.55"/>` +
      `<rect x="${fx(-cabinW / 2 + s * 0.11)}" y="${fx(y0 + (y1 - y0) * 0.54)}" width="${fx(cabinW - s * 0.22)}" height="${fx((y1 - y0) * 0.34)}" rx="${fx(s * 0.05)}" fill="${lighten(color, 0.3)}" opacity="0.55"/>`;
  } else {
    roofLines = `<rect x="${fx(-cabinW / 2 + s * 0.14)}" y="${fx(cabinY - cabinLen / 2 + s * 0.52)}" width="${fx(cabinW - s * 0.28)}" height="${fx(cabinLen - s * 0.52 - s * 0.32)}" rx="${fx(s * 0.06)}" fill="${lighten(color, 0.3)}" opacity="0.5"/>`;
  }
  const lower = wheelsAt(0) + `<rect x="${fx(-W / 2)}" y="${fx(-L / 2)}" width="${fx(W)}" height="${fx(L)}" rx="${fx(r)}" fill="${darken(body, 0.38)}"/>`;
  const upper =
    `<defs><linearGradient id="${gid}" x1="0" y1="0" x2="0.35" y2="1"><stop offset="0" stop-color="${lighten(body, 0.28)}"/><stop offset="0.55" stop-color="${body}"/><stop offset="1" stop-color="${darken(body, 0.1)}"/></linearGradient></defs>` +
    wheelsAt(-depth * 0.2) +
    `<rect x="${fx(-W / 2)}" y="${fx(-L / 2)}" width="${fx(W)}" height="${fx(L)}" rx="${fx(r)}" fill="url(#${gid})"/>` +
    `<ellipse cx="${fx(-W * 0.18)}" cy="${fx(-L / 2 + s * 0.2)}" rx="${fx(W * 0.2)}" ry="${fx(s * 0.07)}" fill="#fff" opacity="0.4"/>` +
    `<rect x="${fx(-cabinW / 2)}" y="${fx(cabinY - cabinLen / 2)}" width="${fx(cabinW)}" height="${fx(cabinLen)}" rx="${fx(s * 0.17)}" fill="${lighten(body, 0.12)}" stroke="${darken(body, 0.14)}" stroke-width="${fx(s * 0.03)}"/>` +
    glassFront + roofLines + glassRear +
    `<circle cx="${fx(-W * 0.3)}" cy="${fx(-L / 2 + s * 0.1)}" r="${fx(s * 0.075)}" fill="#FFF6B8"/><circle cx="${fx(W * 0.3)}" cy="${fx(-L / 2 + s * 0.1)}" r="${fx(s * 0.075)}" fill="#FFF6B8"/>` +
    `<rect x="${fx(-W * 0.4)}" y="${fx(L / 2 - s * 0.13)}" width="${fx(s * 0.17)}" height="${fx(s * 0.07)}" rx="${fx(s * 0.03)}" fill="#E8352B"/><rect x="${fx(W * 0.4 - s * 0.17)}" y="${fx(L / 2 - s * 0.13)}" width="${fx(s * 0.17)}" height="${fx(s * 0.07)}" rx="${fx(s * 0.03)}" fill="#E8352B"/>`;
  return { lower, upper, depth };
}

const DIR_ANGLE = { U: 0, R: 90, D: 180, L: 270 };

// Pose un véhicule sur la grille (modèles : tools/design/vehicles.js). (c, r) = case en haut à gauche de son emprise ;
// ox, oy = coin haut gauche du plateau. Le modèle est choisi parmi ceux de sa longueur, comme dans l'appli.
function car({ c, r, len, dir, color, s, ox, oy, flash = false, shadow = true }) {
  const V = require('./vehicles.js');
  const names = V.byLen[len];
  const model = names[(c + 3 * r) % names.length];
  const horizontal = dir === 'L' || dir === 'R';
  const w = (horizontal ? len : 1) * s;
  const h = (horizontal ? 1 : len) * s;
  const cx = ox + c * s + w / 2;
  const cy = oy + r * s + h / 2;
  const { lower, upper, depth } = V.layers(model, flash ? mix(color, '#FF3B30', 0.5) : color, s);
  const a = DIR_ANGLE[dir];
  const wide = len >= 3 ? V.WIDE : 1;
  const L = (len - 0.2) * s;
  // ombre plate (la silhouette, plus bas que la tranche), puis la tranche (décalée vers le bas de l'ÉCRAN, hors rotation), puis le dessus
  const sh = shadow ? `<g transform="translate(${fx(cx)} ${fx(cy + (V.DEPTH + 0.07) * s)}) rotate(${a})"><rect x="${fx(-0.4 * s * wide)}" y="${fx(-L / 2)}" width="${fx(0.8 * s * wide)}" height="${fx(L)}" rx="${fx(0.3 * s)}" fill="#000" opacity="0.22"/></g>` : '';
  return `${sh}<g transform="translate(${fx(cx)} ${fx(cy)})"><g transform="translate(0 ${fx(depth)})"><g transform="rotate(${a})">${lower}</g></g><g transform="rotate(${a})">${upper}</g></g>`;
}

// ---------------------------------------------------------------- décor du parking
function cone(cx, cy, s) {
  const b = s * 0.34;
  return (
    `<ellipse cx="${fx(cx)}" cy="${fx(cy + b * 0.65)}" rx="${fx(b * 0.95)}" ry="${fx(b * 0.4)}" fill="#000" opacity="0.2"/>` +
    `<rect x="${fx(cx - b)}" y="${fx(cy + b * 0.35)}" width="${fx(b * 2)}" height="${fx(b * 0.5)}" rx="${fx(b * 0.2)}" fill="#E8641E"/>` +
    `<path d="M ${fx(cx - b * 0.7)} ${fx(cy + b * 0.4)} L ${fx(cx - b * 0.18)} ${fx(cy - b * 1.15)} Q ${fx(cx)} ${fx(cy - b * 1.3)} ${fx(cx + b * 0.18)} ${fx(cy - b * 1.15)} L ${fx(cx + b * 0.7)} ${fx(cy + b * 0.4)} Z" fill="#FF8A3D"/>` +
    `<path d="M ${fx(cx - b * 0.46)} ${fx(cy - b * 0.35)} L ${fx(cx + b * 0.46)} ${fx(cy - b * 0.35)} L ${fx(cx + b * 0.56)} ${fx(cy + b * 0.0)} L ${fx(cx - b * 0.56)} ${fx(cy + b * 0.0)} Z" fill="#fff"/>`
  );
}
function psign(cx, cy, s) {
  return (
    `<rect x="${fx(cx - s * 0.06)}" y="${fx(cy)}" width="${fx(s * 0.12)}" height="${fx(s * 0.85)}" rx="${fx(s * 0.04)}" fill="#8E96AB"/>` +
    `<rect x="${fx(cx - s * 0.4)}" y="${fx(cy - s * 0.6)}" width="${fx(s * 0.8)}" height="${fx(s * 0.8)}" rx="${fx(s * 0.2)}" fill="#2D6BD8"/>` +
    `<rect x="${fx(cx - s * 0.34)}" y="${fx(cy - s * 0.54)}" width="${fx(s * 0.68)}" height="${fx(s * 0.68)}" rx="${fx(s * 0.15)}" fill="none" stroke="#fff" stroke-width="${fx(s * 0.04)}"/>` +
    text(cx, cy - s * 0.04, 'P', { size: s * 0.56, weight: 700, fill: '#fff' })
  );
}

// Plateau d'asphalte : cols x rows cases de taille s, coin haut gauche en (ox, oy).
function board({ cols, rows, s, ox, oy, pad = 10 }) {
  const w = cols * s + pad * 2;
  const h = rows * s + pad * 2;
  const x = ox - pad;
  const y = oy - pad;
  let marks = '';
  // petites barres blanches de places de parking le long du bord haut et bas, et séparateurs discrets entre les cases
  for (let i = 1; i < cols; i++) {
    marks += `<rect x="${fx(ox + i * s - 1.5)}" y="${fx(oy - pad + 2)}" width="3" height="${fx(pad * 0.9)}" rx="1.5" fill="${T.line}" opacity="0.85"/>`;
    marks += `<rect x="${fx(ox + i * s - 1.5)}" y="${fx(oy + rows * s + 1)}" width="3" height="${fx(pad * 0.9)}" rx="1.5" fill="${T.line}" opacity="0.85"/>`;
  }
  let tiles = '';
  for (let r = 0; r < rows; r++) {
    for (let c = 0; c < cols; c++) {
      tiles += `<rect x="${fx(ox + c * s + 1)}" y="${fx(oy + r * s + 1)}" width="${fx(s - 2)}" height="${fx(s - 2)}" rx="${fx(s * 0.14)}" fill="${(r + c) % 2 ? T.asphaltLight : '#8590A5'}" opacity="0.55"/>`;
    }
  }
  return (
    `<rect x="${x}" y="${fx(y + 14)}" width="${w}" height="${h}" rx="30" fill="${T.asphaltDark}"/>` +
    `<rect x="${x}" y="${y}" width="${w}" height="${h}" rx="30" fill="${T.asphalt}"/>` +
    tiles + marks
  );
}

// ---------------------------------------------------------------- avatars (animaux dessinés)
const ANIMALS = ['chat', 'chien', 'ours', 'panda', 'lapin', 'grenouille', 'renard', 'lion'];
const ANIMAL_BG = {
  chat: '#9ED8FF', chien: '#FFD1A1', ours: '#C8F0A2', panda: '#FFB8D9', lapin: '#D8C4FF', grenouille: '#FFF0A0', renard: '#B8F0E4', lion: '#FFC4B8',
};
function avatar(cx, cy, r, kind) {
  const bg = ANIMAL_BG[kind];
  const u = r / 50;
  const P = (x, y) => `${fx(cx + x * u)} ${fx(cy + y * u)}`;
  let back = '';
  let face = '';
  let faceFill = '#FFB066';
  let snout = '';
  switch (kind) {
    case 'chat':
      faceFill = '#FFB066';
      back = `<path d="M ${P(-34, -10)} L ${P(-30, -42)} L ${P(-6, -26)} Z" fill="${faceFill}"/><path d="M ${P(34, -10)} L ${P(30, -42)} L ${P(6, -26)} Z" fill="${faceFill}"/>` +
        `<path d="M ${P(-29, -18)} L ${P(-27, -34)} L ${P(-15, -26)} Z" fill="#FF8FA8"/><path d="M ${P(29, -18)} L ${P(27, -34)} L ${P(15, -26)} Z" fill="#FF8FA8"/>`;
      snout = `<ellipse cx="${cx}" cy="${fx(cy + 12 * u)}" rx="${fx(14 * u)}" ry="${fx(10 * u)}" fill="#FFE3C4"/>`;
      break;
    case 'chien':
      faceFill = '#E0B07A';
      back = `<ellipse cx="${fx(cx - 36 * u)}" cy="${fx(cy - 6 * u)}" rx="${fx(12 * u)}" ry="${fx(22 * u)}" fill="#8A5A34" transform="rotate(12 ${fx(cx - 36 * u)} ${fx(cy - 6 * u)})"/><ellipse cx="${fx(cx + 36 * u)}" cy="${fx(cy - 6 * u)}" rx="${fx(12 * u)}" ry="${fx(22 * u)}" fill="#8A5A34" transform="rotate(-12 ${fx(cx + 36 * u)} ${fx(cy - 6 * u)})"/>`;
      snout = `<ellipse cx="${cx}" cy="${fx(cy + 12 * u)}" rx="${fx(17 * u)}" ry="${fx(13 * u)}" fill="#FBE6C8"/>`;
      break;
    case 'ours':
      faceFill = '#B5835A';
      back = `<circle cx="${fx(cx - 28 * u)}" cy="${fx(cy - 30 * u)}" r="${fx(12 * u)}" fill="${faceFill}"/><circle cx="${fx(cx + 28 * u)}" cy="${fx(cy - 30 * u)}" r="${fx(12 * u)}" fill="${faceFill}"/><circle cx="${fx(cx - 28 * u)}" cy="${fx(cy - 30 * u)}" r="${fx(6 * u)}" fill="#E5B98F"/><circle cx="${fx(cx + 28 * u)}" cy="${fx(cy - 30 * u)}" r="${fx(6 * u)}" fill="#E5B98F"/>`;
      snout = `<ellipse cx="${cx}" cy="${fx(cy + 12 * u)}" rx="${fx(16 * u)}" ry="${fx(12 * u)}" fill="#EBCBA4"/>`;
      break;
    case 'panda':
      faceFill = '#FFFFFF';
      back = `<circle cx="${fx(cx - 29 * u)}" cy="${fx(cy - 30 * u)}" r="${fx(12 * u)}" fill="#2B2D42"/><circle cx="${fx(cx + 29 * u)}" cy="${fx(cy - 30 * u)}" r="${fx(12 * u)}" fill="#2B2D42"/>`;
      break;
    case 'lapin':
      faceFill = '#FFFFFF';
      back = `<ellipse cx="${fx(cx - 17 * u)}" cy="${fx(cy - 38 * u)}" rx="${fx(10 * u)}" ry="${fx(26 * u)}" fill="#fff"/><ellipse cx="${fx(cx + 17 * u)}" cy="${fx(cy - 38 * u)}" rx="${fx(10 * u)}" ry="${fx(26 * u)}" fill="#fff"/><ellipse cx="${fx(cx - 17 * u)}" cy="${fx(cy - 38 * u)}" rx="${fx(5 * u)}" ry="${fx(18 * u)}" fill="#FFB3C7"/><ellipse cx="${fx(cx + 17 * u)}" cy="${fx(cy - 38 * u)}" rx="${fx(5 * u)}" ry="${fx(18 * u)}" fill="#FFB3C7"/>`;
      break;
    case 'grenouille':
      faceFill = '#6FD36B';
      back = `<circle cx="${fx(cx - 22 * u)}" cy="${fx(cy - 26 * u)}" r="${fx(15 * u)}" fill="${faceFill}"/><circle cx="${fx(cx + 22 * u)}" cy="${fx(cy - 26 * u)}" r="${fx(15 * u)}" fill="${faceFill}"/>`;
      break;
    case 'renard':
      faceFill = '#FF8A3D';
      back = `<path d="M ${P(-36, -8)} L ${P(-32, -44)} L ${P(-8, -24)} Z" fill="${faceFill}"/><path d="M ${P(36, -8)} L ${P(32, -44)} L ${P(8, -24)} Z" fill="${faceFill}"/><path d="M ${P(-31, -18)} L ${P(-29, -35)} L ${P(-16, -26)} Z" fill="#2B2D42"/><path d="M ${P(31, -18)} L ${P(29, -35)} L ${P(16, -26)} Z" fill="#2B2D42"/>`;
      snout = `<path d="M ${P(-34, 6)} Q ${P(-14, 2)} ${P(0, 20)} Q ${P(14, 2)} ${P(34, 6)} Q ${P(24, 34)} ${P(0, 38)} Q ${P(-24, 34)} ${P(-34, 6)} Z" fill="#FFF3E0"/>`;
      break;
    case 'lion':
      faceFill = '#FFD27A';
      back = `<circle cx="${cx}" cy="${fx(cy + 2 * u)}" r="${fx(46 * u)}" fill="#E8902B"/>` +
        `<circle cx="${fx(cx - 26 * u)}" cy="${fx(cy - 30 * u)}" r="${fx(9 * u)}" fill="#FFD27A"/><circle cx="${fx(cx + 26 * u)}" cy="${fx(cy - 30 * u)}" r="${fx(9 * u)}" fill="#FFD27A"/>`;
      snout = `<ellipse cx="${cx}" cy="${fx(cy + 13 * u)}" rx="${fx(14 * u)}" ry="${fx(10 * u)}" fill="#FFF0CC"/>`;
      break;
    default:
      break;
  }
  const fr = kind === 'lion' ? 33 : 38;
  const panda = kind === 'panda' ? `<ellipse cx="${fx(cx - 15 * u)}" cy="${fx(cy - 4 * u)}" rx="${fx(9 * u)}" ry="${fx(11 * u)}" fill="#2B2D42" transform="rotate(20 ${fx(cx - 15 * u)} ${fx(cy - 4 * u)})"/><ellipse cx="${fx(cx + 15 * u)}" cy="${fx(cy - 4 * u)}" rx="${fx(9 * u)}" ry="${fx(11 * u)}" fill="#2B2D42" transform="rotate(-20 ${fx(cx + 15 * u)} ${fx(cy - 4 * u)})"/>` : '';
  const frog = kind === 'grenouille' ? `<circle cx="${fx(cx - 22 * u)}" cy="${fx(cy - 26 * u)}" r="${fx(9 * u)}" fill="#fff"/><circle cx="${fx(cx + 22 * u)}" cy="${fx(cy - 26 * u)}" r="${fx(9 * u)}" fill="#fff"/>` : '';
  const eyes = kind === 'grenouille'
    ? `<circle cx="${fx(cx - 22 * u)}" cy="${fx(cy - 26 * u)}" r="${fx(5 * u)}" fill="#2B2D42"/><circle cx="${fx(cx + 22 * u)}" cy="${fx(cy - 26 * u)}" r="${fx(5 * u)}" fill="#2B2D42"/>`
    : `<circle cx="${fx(cx - 15 * u)}" cy="${fx(cy - 4 * u)}" r="${fx(5.5 * u)}" fill="#2B2D42"/><circle cx="${fx(cx + 15 * u)}" cy="${fx(cy - 4 * u)}" r="${fx(5.5 * u)}" fill="#2B2D42"/>` +
      `<circle cx="${fx(cx - 13 * u)}" cy="${fx(cy - 6 * u)}" r="${fx(1.8 * u)}" fill="#fff"/><circle cx="${fx(cx + 17 * u)}" cy="${fx(cy - 6 * u)}" r="${fx(1.8 * u)}" fill="#fff"/>`;
  const noseY = kind === 'grenouille' ? 8 : 9;
  const nose = kind === 'grenouille' ? '' : `<ellipse cx="${cx}" cy="${fx(cy + noseY * u)}" rx="${fx(6 * u)}" ry="${fx(4.4 * u)}" fill="${kind === 'chat' || kind === 'lapin' ? '#FF7FA0' : '#2B2D42'}"/>`;
  return (
    `<circle cx="${cx}" cy="${fx(cy + 3)}" r="${r}" fill="${darken(bg, 0.18)}"/>` +
    `<circle cx="${cx}" cy="${cy}" r="${r}" fill="${bg}"/>` +
    `<clipPath id="av${uid}"><circle cx="${cx}" cy="${cy}" r="${r}"/></clipPath>` +
    `<g>${back}<ellipse cx="${cx}" cy="${fx(cy + 2 * u)}" rx="${fx(fr * u)}" ry="${fx(fr * 0.9 * u)}" fill="${faceFill}"/>${snout}${panda}${frog}${eyes}${nose}${mouthPath(cx, cy, u, kind)}</g>`
  );
}
function mouthPath(cx, cy, u, kind) {
  const dy = kind === 'grenouille' ? 14 : 19;
  const w = kind === 'grenouille' ? 17 : 8;
  return `<path d="M ${fx(cx - w * u)} ${fx(cy + dy * u)} Q ${cx} ${fx(cy + (dy + 9) * u)} ${fx(cx + w * u)} ${fx(cy + dy * u)}" fill="none" stroke="#2B2D42" stroke-width="${fx(2.8 * u)}" stroke-linecap="round"/>`;
}

// ---------------------------------------------------------------- cadre téléphone et fond
const W = 390;
const H = 844;
function backdrop() {
  let dots = '';
  const seeds = [[40, 120, 70], [350, 90, 46], [320, 300, 90], [30, 520, 60], [360, 640, 80], [80, 790, 50]];
  for (const [x, y, r] of seeds) dots += `<circle cx="${x}" cy="${y}" r="${r}" fill="#fff" opacity="0.1"/>`;
  return (
    `<defs><linearGradient id="bg" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="${T.sunLight}"/><stop offset="0.35" stop-color="${T.sun}"/><stop offset="1" stop-color="${T.sunDark}"/></linearGradient></defs>` +
    `<rect width="${W}" height="${H}" fill="url(#bg)"/>${dots}`
  );
}
function statusBar() {
  return text(28, 30, '9:41', { size: 15, weight: 600, fill: T.ink, anchor: 'start', opacity: 0.8 }) +
    `<rect x="338" y="19" width="22" height="11" rx="3" fill="none" stroke="${T.ink}" stroke-width="1.6" opacity="0.7"/><rect x="340" y="21" width="14" height="7" rx="1.5" fill="${T.ink}" opacity="0.7"/>`;
}
// `screen` enveloppe un fragment SVG complet (fond compris) : police, contenu, barre d'état.
function screen(body) {
  return (
    `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${W} ${H}" width="${W}" height="${H}">` +
    `<style>@font-face{font-family:'Fredoka';src:url('../../android/app/src/main/res/font/fredoka.ttf');font-weight:300 700}</style>` +
    body + statusBar() + `</svg>`
  );
}
const withBg = (body) => backdrop() + body;
const dim = (a = 0.5) => `<rect width="${W}" height="${H}" fill="#1B1D2E" opacity="${a}"/>`;

module.exports = {
  T, CAR_COLORS, ANIMALS, DIR_ANGLE, W, H,
  fx, mix, lighten, darken, text, star, button, roundButton, panel, icons,
  car, carLayers, cone, psign, board, avatar, screen, withBg, dim, backdrop,
};
