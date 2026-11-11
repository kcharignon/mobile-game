// Lot b2 des objets de Cherche et Trouve : tournesol, lune, bus, couronne, trompette (jaune) ; brocoli, pastèque, crocodile, dinosaure, serpent,
// trèfle, palmier, tracteur (vert) ; dauphin, oiseau, hippocampe, flocon, avion, vélo, seau (bleu).
// Même style que objects.js : plat, contour encre épais, ombre de bord en bas à droite, petit reflet (`solid`).
'use strict';
const { solid, flat, tubes, eye, dotEye, smile, blush, spot, el, ci, rr, path, ln, clip, ink, line, tone, pb, ellD, rrD, capD, polyD, starD, starPts, lensD, splineD, mapD, shiftD, xf, rot, mirror, RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET, PINK, BROWN, WHITEISH, GREY, LEAF, STEM, INK, WHITE, LW } = require('./objkit.js');

// ================================================================ aides locales
const rad = (deg) => (deg * Math.PI) / 180;

/** Union de formes : un seul contour encre autour de l'ensemble, une seule ombre de bord, aucun trait entre les pièces. */
function blob(pieces, base, { off = [-0.025, -0.05], dk = 0.2, hl = [], extra = [] } = {}) {
  return [
    ...pieces.map((d) => path(d, 'none', line(INK, 2 * LW))),
    ...pieces.map((d) => path(d, tone(base, { d: dk }), { sil: true })),
    ...pieces.map((d) => clip(d, [path(shiftD(d, off[0], off[1]), base)])),
    ...(extra.length ? pieces.map((d) => clip(d, extra)) : []),
    ...hl.map((h) => spot(...h)),
  ];
}

/** Points [x, y, demi-largeur] lissés (Catmull-Rom) le long d'une ligne centrale. */
function ribbonPts(P, n) {
  const m = P.length;
  const out = [];
  for (let k = 0; k <= n; k++) {
    const t = (k / n) * (m - 1);
    const i = Math.min(m - 2, Math.floor(t));
    const u = t - i;
    const p0 = P[Math.max(0, i - 1)];
    const p1 = P[i];
    const p2 = P[i + 1];
    const p3 = P[Math.min(m - 1, i + 2)];
    const cr = (j) => 0.5 * (2 * p1[j] + (p2[j] - p0[j]) * u + (2 * p0[j] - 5 * p1[j] + 4 * p2[j] - p3[j]) * u * u + (3 * p1[j] - p0[j] - 3 * p2[j] + p3[j]) * u * u * u);
    out.push([cr(0), cr(1), cr(2)]);
  }
  return out;
}

/** Ruban de largeur variable le long d'une ligne centrale : chemin fermé, bouts arrondis. */
function ribbonD(P, n = 18, cap = 0.9) {
  const S = ribbonPts(P, n);
  const L = [];
  const R = [];
  S.forEach((s, k) => {
    const a = S[Math.max(0, k - 1)];
    const b = S[Math.min(n, k + 1)];
    const len = Math.hypot(b[0] - a[0], b[1] - a[1]) || 1;
    const tx = (b[0] - a[0]) / len;
    const ty = (b[1] - a[1]) / len;
    L.push([s[0] - ty * s[2], s[1] + tx * s[2]]);
    R.push([s[0] + ty * s[2], s[1] - tx * s[2]]);
  });
  const dir = (a, b) => {
    const len = Math.hypot(b[0] - a[0], b[1] - a[1]) || 1;
    return [(b[0] - a[0]) / len, (b[1] - a[1]) / len];
  };
  const t0 = dir(S[1], S[0]);
  const t1 = dir(S[n - 1], S[n]);
  const tip0 = [S[0][0] + t0[0] * S[0][2] * cap, S[0][1] + t0[1] * S[0][2] * cap];
  const tip1 = [S[n][0] + t1[0] * S[n][2] * cap, S[n][1] + t1[1] * S[n][2] * cap];
  return splineD([tip0, ...L, tip1, ...R.reverse()]);
}

/** Joue rose pâle (sur le bleu et le vert, le rose translucide de `blush` tournerait au violet ou au brun). */
const cheek = (x, y, rx, ry) => el(x, y, rx, ry, tone('#FFB0C8', { a: 0.8 }));

/** Un petit demi-disque (tranche) : bord plat en haut à la hauteur y0, arrondi vers le bas. */
const halfDiscD = (r, y0) => pb().M(-r, y0).arc(0, y0, r, r, 180, 0).Z().d;

const draw = {};

// ================================================================ jaune
draw.tournesol = () => {
  const cx = 0;
  const cy = -0.14;
  const ring = (n, ph, r0, r1, w, fill) => Array.from({ length: n }, (_, i) => {
    const a = rad(ph + (360 / n) * i);
    return flat(lensD(cx + r0 * Math.cos(a), cy + r0 * Math.sin(a), cx + r1 * Math.cos(a), cy + r1 * Math.sin(a), w), fill);
  });
  const seeds = Array.from({ length: 5 }, (_, i) => ci(cx + 0.125 * Math.cos(rad(i * 72 + 18)), cy + 0.125 * Math.sin(rad(i * 72 + 18)), 0.03, '#C98B4B'));
  return [
    ...tubes(['M 0,0.1 Q 0.04,0.3 0,0.5'], '#3FAE49', 0.065),
    flat(lensD(0.0, 0.38, -0.3, 0.24, 0.075), LEAF),
    flat(lensD(0.0, 0.42, 0.3, 0.29, 0.075), LEAF),
    ...ring(10, 0, 0.08, 0.42, 0.1, '#FFB224'),
    ...ring(10, 18, 0.08, 0.4, 0.1, YELLOW),
    ...solid(ellD(cx, cy, 0.2, 0.2), '#8A5A2B', { off: [-0.02, -0.04], dk: 0.25, extra: [path(ellD(cx, cy, 0.09, 0.09), '#5A3320'), ...seeds] }),
  ];
};

draw.lune = () => {
  const R = 0.47;
  const c2 = [0.2, -0.05];
  const r2 = 0.4;
  const d = Math.hypot(c2[0], c2[1]);
  const a = (R * R - r2 * r2 + d * d) / (2 * d);
  const h = Math.sqrt(R * R - a * a);
  const ux = c2[0] / d;
  const uy = c2[1] / d;
  const pts = [[a * ux - h * uy, a * uy + h * ux], [a * ux + h * uy, a * uy - h * ux]].sort((p, q) => p[1] - q[1]);
  const [pt, pbm] = pts;
  const deg = (p, c) => (Math.atan2(p[1] - c[1], p[0] - c[0]) * 180) / Math.PI;
  const body = pb().arc(0, 0, R, R, deg(pt, [0, 0]), deg(pbm, [0, 0]) - 360).arc(c2[0], c2[1], r2, r2, deg(pbm, c2), deg(pt, c2) + 360).Z().d;
  return [
    ...solid(body, YELLOW, { off: [-0.02, -0.04], hl: [[-0.37, -0.22, 0.04, 0.09, 30]] }),
    smile(-0.3, -0.05, 0.1, 0.035, 0.026),
    smile(-0.3, 0.13, 0.07, 0.02, 0.022),
    blush(-0.37, 0.06, 0.045, 0.03),
  ];
};

draw.bus = () => {
  const bodyD = polyD([[-0.48, -0.36], [0.22, -0.36], [0.3, -0.1], [0.5, -0.06], [0.5, 0.2], [-0.48, 0.2]], [0.07, 0.07, 0.03, 0.04, 0.04, 0.04]);
  const win = '#BFE3FF';
  const wins = [-0.42, -0.27, -0.12, 0.03].map((x) => rr(x, -0.29, 0.12, 0.16, 0.02, win, ink(0.024)));
  return [
    ...solid(bodyD, YELLOW, { dk: 0.18, extra: [clip(bodyD, [rr(-0.5, 0.04, 1.0, 0.045, 0, INK)])], hl: [[-0.36, -0.335, 0.06, 0.015, 0, 0.5]] }),
    ...wins,
    flat(polyD([[0.17, -0.29], [0.22, -0.29], [0.27, -0.1], [0.17, -0.1]], 0.01), win, ink(0.024)),
    flat(ellD(0.465, 0.0, 0.03, 0.045), '#FFF2B0', ink(0.02)),
    ...[-0.28, 0.3].flatMap((x) => [ci(x, 0.22, 0.115, '#41465F', ink()), ci(x, 0.22, 0.045, '#D9DEE9', ink(0.02))]),
  ];
};

draw.couronne = () => {
  const body = polyD([[-0.44, 0.3], [-0.44, -0.26], [-0.22, -0.03], [0, -0.36], [0.22, -0.03], [0.44, -0.26], [0.44, 0.3]], [0.05, 0.05, 0.03, 0.05, 0.03, 0.05, 0.05]);
  return [
    ...[[-0.44, -0.3, 0.065], [0, -0.41, 0.07], [0.44, -0.3, 0.065]].map(([x, y, r]) => flat(ellD(x, y, r, r), RED, ink(0.026))),
    ...solid(body, YELLOW, { off: [-0.025, -0.045], extra: [clip(body, [rr(-0.5, 0.12, 1.0, 0.2, 0, '#FFB224')])], hl: [[-0.3, -0.06, 0.03, 0.09, 20]] }),
    flat(ellD(0, 0.215, 0.07, 0.065), RED, ink(0.024)),
    flat(ellD(-0.27, 0.215, 0.05, 0.05), BLUE, ink(0.024)),
    flat(ellD(0.27, 0.215, 0.05, 0.05), BLUE, ink(0.024)),
  ];
};

draw.trompette = () => {
  const GOLD = '#F5B800';
  const ob = [
    // la boucle du tuyau, sous le tube principal
    ...tubes([rrD(-0.3, -0.02, 0.4, 0.22, 0.1)], YELLOW, 0.06),
    // le tube principal et l'embouchure
    ...tubes(['M -0.36,-0.02 L 0.14,-0.02'], YELLOW, 0.07),
    flat(capD(-0.46, -0.02, -0.38, -0.02, 0.05), GOLD, ink(0.026)),
    // trois pistons
    ...[-0.17, -0.04, 0.09].flatMap((x) => [
      flat(rrD(x - 0.03, -0.24, 0.06, 0.2, 0.025), GOLD, ink(0.024)),
      flat(ellD(x, -0.25, 0.045, 0.03), ORANGE, ink(0.024)),
    ]),
    // le pavillon
    ...solid('M 0.1,-0.07 C 0.22,-0.08 0.3,-0.2 0.4,-0.3 Q 0.5,-0.3 0.5,-0.2 L 0.5,0.17 Q 0.5,0.26 0.4,0.26 C 0.3,0.14 0.22,0.04 0.1,0.03 Z', YELLOW, { off: [-0.02, -0.04], hl: [[0.3, -0.05, 0.02, 0.1, 25]] }),
    flat(ellD(0.465, -0.04, 0.035, 0.22), '#FFF0A8', ink(0.024)),
  ];
  return rot(ob, -22);
};

// ================================================================ vert
draw.brocoli = () => {
  const D = '#2FA54A';
  const stalk = polyD([[-0.11, -0.05], [0.11, -0.05], [0.18, 0.46], [-0.18, 0.46]], [0.01, 0.01, 0.07, 0.07]);
  const lobe = (x, y, r, hl = []) => solid(ellD(x, y, r, r), D, { off: [-0.02, -0.045], hl });
  return [
    ...tubes(['M -0.02,0.1 L -0.2,-0.08', 'M 0.02,0.1 L 0.2,-0.08'], '#A8D86B', 0.08),
    ...solid(stalk, '#A8D86B', { off: [-0.025, -0.03], dk: 0.15, hl: [[-0.07, 0.22, 0.02, 0.1]] }),
    ...lobe(0, -0.2, 0.25, [[-0.1, -0.3, 0.05, 0.07, -35]]),
    ...lobe(-0.27, -0.06, 0.2),
    ...lobe(0.27, -0.06, 0.2),
    ...lobe(-0.1, 0.0, 0.17),
    ...lobe(0.12, 0.0, 0.17, [[0.05, -0.05, 0.03, 0.04, -30]]),
  ];
};

draw.pasteque = () => {
  const y0 = -0.2;
  const ob = [
    ...solid(halfDiscD(0.48, y0), '#2F9E44', { off: [-0.02, -0.04], dk: 0.2 }),
    path(halfDiscD(0.4, y0), '#DDF3B8'),
    path(halfDiscD(0.35, y0), '#F0434C'),
    ...[[-0.18, 0.0, -20], [0, 0.07, 0], [0.18, 0.0, 20], [-0.09, 0.19, -10], [0.09, 0.19, 10]].map(([x, y, a]) => path(ellD(x, y, 0.028, 0.048, a), INK)),
    spot(-0.27, 0.0, 0.025, 0.07, 20, 0.35),
  ];
  return rot(ob, -14);
};

draw.crocodile = () => {
  const GD = '#2E9E44';
  const spikes = [-0.34, -0.21, -0.08].map((x) => flat(polyD([[x - 0.06, -0.03], [x, -0.25], [x + 0.06, -0.03]], 0.015), GD, ink(0.026)));
  const legs = [[-0.27, 0.14, -0.29, 0.3], [0.05, 0.14, 0.07, 0.3]].map(([x1, y1, x2, y2]) => flat(capD(x1, y1, x2, y2, 0.08), GD, ink(0.03)));
  const pieces = [
    ellD(-0.1, 0.06, 0.3, 0.2),
    ribbonD([[-0.25, 0.07, 0.16], [-0.45, 0.08, 0.1], [-0.62, 0.0, 0.03]]),
    ribbonD([[0.06, 0.0, 0.18], [0.3, 0.03, 0.14], [0.47, 0.04, 0.11]], 12, 0.7),
    ellD(0.1, -0.15, 0.11, 0.105),
    ellD(0.47, -0.08, 0.045, 0.04),
  ];
  const tooth = (x) => flat(polyD([[x - 0.034, 0.07], [x + 0.034, 0.07], [x, 0.15]], 0.008), WHITE, ink(0.018));
  return [
    ...spikes,
    ...legs,
    ...blob(pieces, '#4FBF54', { extra: [el(-0.05, 0.3, 0.6, 0.12, '#C4EE96')], hl: [[-0.2, -0.04, 0.1, 0.025, -5, 0.45]] }),
    ...eye(0.11, -0.16, 0.08, [0.2, 0.3]),
    path(pb().M(0.51, 0.06).L(0.24, 0.075).Q(0.16, 0.08, 0.13, 0.02).d, 'none', line(INK, 0.026)),
    tooth(0.42), tooth(0.32), tooth(0.22),
  ];
};

draw.dinosaure = () => {
  const GD = '#2F9E44';
  const pieces = [
    ellD(-0.04, 0.1, 0.33, 0.23),
    ribbonD([[-0.22, 0.14, 0.16], [-0.4, 0.22, 0.09], [-0.52, 0.14, 0.03]]),
    ribbonD([[0.12, 0.05, 0.14], [0.26, -0.15, 0.09], [0.3, -0.34, 0.08]]),
    ellD(0.36, -0.4, 0.14, 0.1),
  ];
  const spots = [ci(-0.16, 0.0, 0.06, GD), ci(0.04, -0.06, 0.05, GD), ci(-0.3, 0.1, 0.045, GD)];
  return [
    ...[-0.24, -0.09, 0.09, 0.21].map((x) => flat(capD(x, 0.24, x, 0.36, 0.065), '#4AAE4F', ink(0.03))),
    ...blob(pieces, '#5BC45C', { extra: [el(0.0, 0.34, 0.5, 0.12, '#D2F2A0'), ...spots], hl: [[-0.2, -0.08, 0.1, 0.025, -10, 0.45]] }),
    ...eye(0.4, -0.43, 0.052, [0.3, 0.2]),
    smile(0.43, -0.34, 0.1, 0.02, 0.02),
    cheek(0.34, -0.35, 0.035, 0.024),
  ];
};

draw.serpent = () => {
  const GD = '#2E9E44';
  const centre = [[-0.42, 0.2, 0.02], [-0.3, 0.38, 0.06], [-0.05, 0.43, 0.09], [0.25, 0.33, 0.1], [0.3, 0.1, 0.1], [0.1, -0.02, 0.1], [-0.18, -0.08, 0.1], [-0.3, -0.25, 0.1], [-0.15, -0.4, 0.1], [0.08, -0.42, 0.11], [0.22, -0.37, 0.12]];
  const S = ribbonPts(centre, 40);
  const marks = [6, 10, 14, 18, 22, 26, 30, 34].map((k) => ci(S[k][0], S[k][1], S[k][2] * 0.6, GD));
  const pieces = [ribbonD(centre, 40), ellD(0.31, -0.35, 0.15, 0.115)];
  return [
    path('M 0.43,-0.33 L 0.53,-0.33 M 0.53,-0.33 L 0.58,-0.37 M 0.53,-0.33 L 0.58,-0.29', 'none', line(RED, 0.03)),
    ...blob(pieces, '#5BC45C', { extra: marks, hl: [[0.22, -0.43, 0.06, 0.02, -10, 0.5]] }),
    ...eye(0.31, -0.4, 0.055, [0.3, 0.2]),
    smile(0.38, -0.3, 0.09, 0.02, 0.02),
  ];
};

draw.trefle = () => {
  const heart = 'M 0,0.42 C -0.18,0.28 -0.46,0.1 -0.46,-0.14 C -0.46,-0.32 -0.34,-0.4 -0.22,-0.4 C -0.1,-0.4 -0.03,-0.33 0,-0.24 C 0.03,-0.33 0.1,-0.4 0.22,-0.4 C 0.34,-0.4 0.46,-0.32 0.46,-0.14 C 0.46,0.1 0.18,0.28 0,0.42 Z';
  const k = 0.54;
  const leafD = mapD(heart, (x, y) => [x * k, (y - 0.42) * k]);
  const leaf = (angle) => rot([...solid(leafD, '#3FB24A', { off: [-0.02, -0.04], hl: [[-0.1, -0.33, 0.03, 0.05, 20]] }), ln(0, -0.1, 0, -0.27, tone(WHITE, { a: 0.45 }), 0.03, { nosil: true })], angle);
  return [
    ...tubes(['M 0,0.02 Q 0.03,0.36 0.2,0.54'], '#4F9A3A', 0.07),
    ...leaf(-135), ...leaf(135), ...leaf(-45), ...leaf(45),
  ];
};

draw.palmier = () => {
  const cx = 0.03;
  const cy = -0.2;
  const frond = (ang, len, droop, fill) => xf(rot([flat(pb().M(0, 0).Q(len * 0.45, -0.2, len, droop).Q(len * 0.55, 0.1, 0, 0).Z().d, fill)], ang), { dx: cx, dy: cy });
  const right = [[-68, 0.34, 0.1, LEAF], [-30, 0.44, 0.17, '#52C25A'], [8, 0.4, 0.2, LEAF], [42, 0.26, 0.12, '#52C25A']];
  const trunkAt = (t) => [(1 - t) * (1 - t) * -0.07 + 2 * (1 - t) * t * 0.07 + t * t * 0.03, (1 - t) * (1 - t) * 0.46 + 2 * (1 - t) * t * 0.15 + t * t * -0.18];
  return [
    flat(ellD(0, 0.47, 0.3, 0.06), '#F2D48B'),
    ...tubes(['M -0.07,0.46 Q 0.07,0.15 0.03,-0.18'], '#B07A45', 0.11),
    ...[0.2, 0.4, 0.6, 0.8].map((t) => { const [x, y] = trunkAt(t); return ln(x - 0.045, y + 0.012, x + 0.045, y - 0.012, tone('#7A4A2A'), 0.024, { nosil: true }); }),
    ...right.flatMap(([a, l, d, f]) => frond(a, l, d, f)),
    ...right.flatMap(([a, l, d, f]) => mirror(frond(a, l, d, f)).map((o) => o)),
    ci(cx - 0.05, cy + 0.06, 0.055, '#7A4A2A', ink(0.026)),
    ci(cx + 0.06, cy + 0.07, 0.055, '#7A4A2A', ink(0.026)),
  ];
};

draw.tracteur = () => {
  const G = '#3FB24A';
  const GD = '#2A8A34';
  const tire = '#41465F';
  return [
    ...tubes(['M 0.3,-0.06 L 0.3,-0.3'], tire, 0.06),
    ...solid(polyD([[-0.02, -0.08], [0.36, -0.08], [0.48, 0.02], [0.48, 0.22], [-0.02, 0.22]], [0.02, 0.06, 0.05, 0.03, 0.02]), G, { off: [-0.02, -0.03], hl: [[0.1, -0.03, 0.08, 0.015, 0, 0.5]] }),
    flat(rrD(-0.42, -0.42, 0.5, 0.075, 0.035), GD),
    ...solid(polyD([[-0.38, -0.35], [0.02, -0.35], [0.02, 0.22], [-0.38, 0.22]], 0.02), G, { off: [-0.02, -0.03] }),
    flat(rrD(-0.31, -0.29, 0.28, 0.24, 0.025), '#BFE3FF', ink(0.026)),
    flat(ellD(0.465, 0.06, 0.03, 0.045), '#FFF2B0', ink(0.02)),
    ci(-0.2, 0.2, 0.27, tire, ink()),
    ci(-0.2, 0.2, 0.12, YELLOW, ink(0.026)),
    ci(-0.2, 0.2, 0.04, tire),
    ci(0.33, 0.31, 0.14, tire, ink()),
    ci(0.33, 0.31, 0.065, YELLOW, ink(0.024)),
  ];
};

// ================================================================ bleu
draw.dauphin = () => {
  const centre = [[-0.42, 0.14, 0.02], [-0.28, 0.06, 0.06], [-0.08, -0.02, 0.13], [0.14, -0.04, 0.15], [0.3, 0.0, 0.12], [0.4, 0.07, 0.06], [0.5, 0.1, 0.03]];
  const pieces = [
    ribbonD(centre, 20),
    'M 0.12,-0.1 C 0.04,-0.16 -0.02,-0.26 -0.1,-0.34 C -0.16,-0.24 -0.16,-0.14 -0.15,-0.06 Z',
    lensD(-0.4, 0.14, -0.56, 0.0, 0.06),
    lensD(-0.4, 0.14, -0.54, 0.3, 0.06),
    lensD(0.1, 0.08, -0.02, 0.3, 0.05),
  ];
  const ob = [
    ...blob(pieces, BLUE, { extra: [el(0.05, 0.24, 0.55, 0.13, '#CFE8FF')], hl: [[-0.1, -0.06, 0.12, 0.025, -10, 0.45]] }),
    ...eye(0.27, -0.04, 0.048, [0.3, 0.2]),
    path(pb().M(0.31, 0.06).Q(0.38, 0.12, 0.47, 0.1).d, 'none', line(INK, 0.022)),
    cheek(0.2, 0.05, 0.035, 0.024),
  ];
  return rot(ob, -14);
};

draw.oiseau = () => {
  const D = '#1F6FC8';
  const pieces = [ellD(-0.03, 0.1, 0.35, 0.3), ellD(0.16, -0.19, 0.22, 0.2)];
  return [
    flat(lensD(-0.28, 0.2, -0.52, 0.32, 0.06), D),
    flat(lensD(-0.26, 0.24, -0.46, 0.46, 0.06), D),
    ...tubes(['M -0.04,0.36 L -0.04,0.47', 'M 0.12,0.36 L 0.12,0.47'], ORANGE, 0.04),
    ...blob(pieces, BLUE, { extra: [el(0.12, 0.34, 0.4, 0.14, '#CFE8FF')], hl: [[-0.14, -0.1, 0.05, 0.03, -30], [0.08, -0.34, 0.06, 0.025, -20, 0.5]] }),
    flat(ellD(-0.13, 0.12, 0.2, 0.12, -28), D),
    flat(polyD([[0.34, -0.25], [0.34, -0.12], [0.5, -0.17]], 0.02), ORANGE, ink(0.026)),
    ...eye(0.2, -0.22, 0.07, [0.3, 0.1]),
    cheek(0.12, -0.08, 0.045, 0.03),
  ];
};

draw.hippocampe = () => {
  const centre = [[0.42, -0.3, 0.035], [0.28, -0.3, 0.05], [0.1, -0.3, 0.11], [0.0, -0.18, 0.09], [0.04, -0.02, 0.15], [0.05, 0.14, 0.12], [-0.02, 0.28, 0.08], [0.02, 0.4, 0.06], [0.14, 0.46, 0.045], [0.25, 0.4, 0.035], [0.25, 0.31, 0.03]];
  const S = ribbonPts(centre, 40);
  const bands = [16, 20, 24].map((k) => ln(S[k][0] - 0.18, S[k][1], S[k][0] + 0.18, S[k][1] + 0.02, tone(WHITE, { a: 0.5 }), 0.03));
  const pieces = [ribbonD(centre, 40), lensD(-0.06, -0.02, -0.26, -0.1, 0.06), ellD(0.0, -0.4, 0.04, 0.04), ellD(0.08, -0.43, 0.04, 0.04)];
  return [
    ...blob(pieces, BLUE, { extra: bands, hl: [[0.09, -0.36, 0.04, 0.02, -20, 0.5]] }),
    ...eye(0.12, -0.31, 0.05, [0.3, 0.1]),
    cheek(0.1, -0.22, 0.035, 0.024),
  ];
};

draw.flocon = () => {
  const R = 0.46;
  const arms = [];
  for (let i = 0; i < 3; i++) {
    const a = rad(i * 60 - 90);
    arms.push(`M ${R * Math.cos(a)},${R * Math.sin(a)} L ${-R * Math.cos(a)},${-R * Math.sin(a)}`);
  }
  const branches = [];
  for (let i = 0; i < 6; i++) {
    const a = rad(i * 60 - 90);
    for (const [at, len] of [[0.52, 0.17]]) {
      const bx = at * R * Math.cos(a);
      const by = at * R * Math.sin(a);
      for (const s of [-1, 1]) {
        const b = a + s * rad(55);
        branches.push(`M ${bx},${by} L ${bx + len * Math.cos(b)},${by + len * Math.sin(b)}`);
      }
    }
  }
  const tips = Array.from({ length: 6 }, (_, i) => ci(R * Math.cos(rad(i * 60 - 90)), R * Math.sin(rad(i * 60 - 90)), 0.055, '#5FB0FF', ink(0.026)));
  return [
    ...tubes([...arms, ...branches], BLUE, 0.065),
    ...tips,
    flat(starD(0, 0, 6, 0.12, 0.085, -90, 0.02), '#CFE8FF', ink(0.026)),
  ];
};

draw.avion = () => {
  const D = '#1F6FC8';
  const fus = 'M 0.37,-0.13 Q 0.5,-0.13 0.5,0.0 Q 0.5,0.13 0.37,0.13 L -0.3,0.13 Q -0.5,0.1 -0.5,-0.06 Q -0.5,-0.13 -0.42,-0.13 Z';
  return [
    flat(polyD([[-0.48, -0.04], [-0.48, -0.38], [-0.37, -0.38], [-0.2, -0.04]], 0.02), D),
    ...solid(fus, BLUE, { off: [-0.02, -0.04], extra: [clip(fus, [rr(-0.52, 0.04, 1.04, 0.2, 0, '#EAF5FF')])], hl: [[-0.1, -0.1, 0.18, 0.015, 0, 0.5]] }),
    flat(polyD([[0.3, -0.1], [0.42, -0.1], [0.46, -0.02], [0.3, -0.02]], 0.015), '#BFE3FF', ink(0.022)),
    ...[-0.26, -0.14, -0.02, 0.1].map((x) => ci(x, -0.03, 0.032, '#BFE3FF', ink(0.02))),
    flat(polyD([[-0.44, 0.0], [-0.28, 0.02], [-0.4, 0.16], [-0.5, 0.16]], 0.03), D),
    flat(polyD([[0.22, 0.06], [-0.14, 0.06], [-0.36, 0.4], [-0.14, 0.4]], 0.03), D),
    flat(capD(0.0, 0.26, 0.14, 0.26, 0.05), GREY, ink(0.026)),
  ];
};

draw.velo = () => {
  const rear = [-0.28, 0.17];
  const front = [0.28, 0.17];
  const wheel = ([x, y]) => [
    ci(x, y, 0.2, '#BFE0FF', ink()),
    ...tubes([ellD(x, y, 0.2, 0.2)], '#41465F', 0.05).slice(1),
    ci(x, y, 0.045, '#41465F'),
  ];
  const bb = [-0.04, 0.17];
  const seat = [-0.13, -0.14];
  const head = [0.2, -0.14];
  return [
    ...wheel(rear), ...wheel(front),
    ...tubes([
      `M ${bb[0]},${bb[1]} L ${seat[0]},${seat[1]}`,
      `M ${seat[0]},${seat[1]} L ${head[0]},${head[1]}`,
      `M ${bb[0]},${bb[1]} L ${head[0]},${head[1]}`,
      `M ${bb[0]},${bb[1]} L ${rear[0]},${rear[1]}`,
      `M ${seat[0]},${seat[1]} L ${rear[0]},${rear[1]}`,
      `M ${head[0]},${head[1]} L ${front[0]},${front[1]}`,
    ], BLUE, 0.05),
    ...tubes([`M ${seat[0]},${seat[1]} L ${seat[0] - 0.01},-0.21`, 'M 0.2,-0.14 L 0.18,-0.27', 'M 0.16,-0.28 L 0.3,-0.29'], '#41465F', 0.035),
    flat(capD(-0.22, -0.235, -0.08, -0.235, 0.032), '#41465F', ink(0.022)),
    ci(bb[0], bb[1], 0.065, '#EAF5FF', ink(0.026)),
    ci(bb[0], bb[1], 0.02, '#41465F'),
  ];
};

draw.seau = () => {
  const D = '#1F6FC8';
  const body = polyD([[-0.34, -0.15], [0.34, -0.15], [0.26, 0.42], [-0.26, 0.42]], [0.03, 0.03, 0.07, 0.07]);
  return [
    ...tubes(['M -0.31,-0.16 C -0.3,-0.5 0.3,-0.5 0.31,-0.16'], '#41465F', 0.035),
    ...tubes(['M 0.12,-0.1 L 0.34,-0.36'], ORANGE, 0.05),
    ...rot([flat(rrD(0.32, -0.62, 0.17, 0.24, 0.075), RED)], 41, 0.4, -0.5),
    ...solid(body, BLUE, { off: [-0.025, -0.04], hl: [[-0.19, 0.08, 0.025, 0.12, 5]] }),
    ...solid(capD(-0.34, -0.16, 0.34, -0.16, 0.055), '#5BB0FF', { off: [-0.02, -0.03] }),
    flat(starD(0, 0.15, 5, 0.12, 0.055, -90, 0.015), YELLOW, ink(0.026)),
  ];
};

module.exports = draw;
