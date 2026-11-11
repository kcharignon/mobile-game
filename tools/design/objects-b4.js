// Lot 4 des objets de Cherche et Trouve : 20 objets (casquette, pain, chien, singe, hibou, escargot, gland, pomme de pin, valise, guitare,
// œuf, lapin, mouton, panda, pingouin, fusée, ampoule, horloge, clé, piano). Même langage que objects.js : à plat, contour encre, ombre de bord.
'use strict';
const { solid, flat, tubes, eye, smile, blush, spot, el, ci, rr, path, ln, clip, ink, line, tone, pb, ellD, rrD, capD, polyD, lensD, splineD, rot, ORANGE, YELLOW, BLUE, BROWN, WHITEISH, INK, WHITE } = require('./objkit.js');

const draw = {};

// ---- un peu d'outillage local
/** Courbe ouverte lisse passant près des points (quadratiques entre les milieux). */
const smoothD = (pts) => {
  const b = pb().M(pts[0][0], pts[0][1]);
  for (let i = 1; i < pts.length - 1; i++) {
    const mx = (pts[i][0] + pts[i + 1][0]) / 2;
    const my = (pts[i][1] + pts[i + 1][1]) / 2;
    b.Q(pts[i][0], pts[i][1], i === pts.length - 2 ? pts[i + 1][0] : mx, i === pts.length - 2 ? pts[i + 1][1] : my);
  }
  return b.d;
};

/** Contour bouclé : arcs extérieurs de cercles [x, y, r] rangés dans le sens horaire (chaque cercle coupe le suivant). */
const puffD = (circles) => {
  const n = circles.length;
  const gx = circles.reduce((s, c) => s + c[0], 0) / n;
  const gy = circles.reduce((s, c) => s + c[1], 0) / n;
  const meet = (a, b) => {
    const d = Math.hypot(b[0] - a[0], b[1] - a[1]);
    const l = (a[2] * a[2] - b[2] * b[2] + d * d) / (2 * d);
    const h = Math.sqrt(Math.max(0, a[2] * a[2] - l * l));
    const ux = (b[0] - a[0]) / d;
    const uy = (b[1] - a[1]) / d;
    const mx = a[0] + ux * l;
    const my = a[1] + uy * l;
    const p1 = [mx - uy * h, my + ux * h];
    const p2 = [mx + uy * h, my - ux * h];
    return Math.hypot(p1[0] - gx, p1[1] - gy) > Math.hypot(p2[0] - gx, p2[1] - gy) ? p1 : p2;
  };
  const ang = (c, p) => (Math.atan2(p[1] - c[1], p[0] - c[0]) * 180) / Math.PI;
  const pts = circles.map((c, i) => meet(c, circles[(i + 1) % n]));
  const b = pb();
  circles.forEach((c, i) => {
    const from = pts[(i + n - 1) % n];
    const to = pts[i];
    const a0 = ang(c, from);
    let a1 = ang(c, to);
    while (a1 <= a0) a1 += 360;
    b.arc(c[0], c[1], c[2], c[2], a0, a1);
  });
  return b.Z().d;
};

// ================================================================ bleu
draw.casquette = () => {
  // la calotte (bord du bas un peu incurvé), un bandeau foncé, deux coutures ; la visière est un croissant posé par-dessus, vers l'avant (à droite)
  const dome = 'M -0.44,0.1 C -0.47,-0.16 -0.26,-0.38 0,-0.38 C 0.27,-0.38 0.42,-0.2 0.45,0.16 Q 0,0.2 -0.44,0.1 Z';
  const brim = 'M 0.05,0.16 C 0.22,0.0 0.58,0.0 0.74,0.2 C 0.79,0.27 0.73,0.33 0.66,0.32 C 0.46,0.28 0.24,0.27 0.05,0.16 Z';
  const front = 'M 0.0,-0.4 Q 0.18,-0.12 0.2,0.22 L 0.5,0.22 L 0.5,-0.5 Z';
  const seam = (d) => path(d, 'none', line('#1B63B8', 0.024));
  return [
    ...solid(dome, BLUE, {
      off: [-0.025, -0.05],
      extra: [
        clip(dome, [path(front, '#5BAEFF'), path('M -0.5,-0.02 Q 0,0.1 0.5,0.06 L 0.5,0.3 L -0.5,0.3 Z', '#1F6FC9')]),
        clip(dome, [seam('M 0,-0.4 Q -0.14,-0.12 -0.16,0.22'), seam('M 0,-0.4 Q 0.18,-0.12 0.2,0.22')]),
      ],
      hl: [[-0.22, -0.16, 0.05, 0.1, 35]],
    }),
    ...solid(brim, '#1F6FC9', { off: [-0.02, -0.03], dk: 0.2, hl: [[0.4, 0.07, 0.11, 0.022, 14, 0.4]] }),
    ...solid(ellD(0.0, -0.385, 0.05, 0.04), '#1F6FC9', { off: [-0.01, -0.02], sw: 0.026 }),
  ];
};

// ================================================================ marron
draw.pain = () => {
  // une baguette entaillée, penchée
  const body = capD(-0.34, 0, 0.34, 0, 0.19);
  const cut = (x) => flat(lensD(x - 0.08, 0.08, x + 0.08, -0.08, 0.042), '#F6DDA4', ink(0.022));
  const ob = [
    ...solid(body, '#BA7230', { off: [-0.02, -0.05], hl: [[-0.3, -0.07, 0.1, 0.025, 0, 0.5]], extra: [clip(body, [cut(-0.3), cut(-0.1), cut(0.1), cut(0.3)])] }),
  ];
  return rot(ob, -35);
};

draw.chien = () => {
  const DARK = '#7A4A2A';
  const head = ellD(0, 0.04, 0.37, 0.34);
  const ear = (s) => solid(ellD(s * 0.39, 0.02, 0.14, 0.28, s * -14), DARK, { off: [-0.015, -0.03] });
  return [
    ...ear(-1), ...ear(1),
    ...solid(head, BROWN, { off: [-0.02, -0.05], extra: [clip(head, [path(ellD(-0.17, -0.06, 0.14, 0.13), DARK)])], hl: [[-0.2, -0.22, 0.07, 0.04, -25]] }),
    ...eye(-0.17, -0.07, 0.065, [0.15, 0.25]),
    ...eye(0.17, -0.07, 0.065, [-0.15, 0.25]),
    flat(ellD(0, 0.17, 0.2, 0.16), '#F0D2A8', ink(0.026)),
    flat(rrD(-0.065, 0.2, 0.13, 0.15, 0.065), '#FF7A9A', ink(0.024)),
    path(pb().M(0, 0.2).L(0, 0.33).d, 'none', line(tone(INK, { a: 0.35 }), 0.016)),
    path(pb().M(0, 0.12).L(0, 0.2).Q(-0.05, 0.27, -0.12, 0.22).d, 'none', line(INK, 0.026)),
    path(pb().M(0, 0.2).Q(0.05, 0.27, 0.12, 0.22).d, 'none', line(INK, 0.026)),
    el(0, 0.1, 0.085, 0.055, INK),
    ci(-0.03, 0.082, 0.016, WHITE),
  ];
};

draw.singe = () => {
  const FUR = '#8A5A32';
  const TAN = '#F2D3A6';
  const head = ellD(0, -0.02, 0.38, 0.36);
  const ear = (s) => [
    ...solid(ellD(s * 0.4, 0.02, 0.14, 0.14), FUR, { off: [-0.015, -0.03] }),
    path(ellD(s * 0.4, 0.02, 0.075, 0.075), '#E7B98A'),
  ];
  const mask = splineD([[0, -0.1], [-0.1, -0.19], [-0.24, -0.18], [-0.31, -0.06], [-0.28, 0.08], [-0.2, 0.2], [-0.1, 0.3], [0, 0.32], [0.1, 0.3], [0.2, 0.2], [0.28, 0.08], [0.31, -0.06], [0.24, -0.18], [0.1, -0.19]]);
  return [
    ...ear(-1), ...ear(1),
    ...solid(head, FUR, { off: [-0.02, -0.05], hl: [[-0.22, -0.25, 0.07, 0.045, -30]] }),
    flat(mask, TAN, ink(0.026)),
    ...eye(-0.13, -0.07, 0.065, [0.15, 0.25]),
    ...eye(0.13, -0.07, 0.065, [-0.15, 0.25]),
    el(-0.04, 0.13, 0.017, 0.022, '#9B6B45'),
    el(0.04, 0.13, 0.017, 0.022, '#9B6B45'),
    smile(0, 0.2, 0.2, 0.045),
    blush(-0.23, 0.14, 0.04, 0.028),
    blush(0.23, 0.14, 0.04, 0.028),
  ];
};

draw.hibou = () => {
  const DARK = '#7A4A2A';
  const CREAM = '#F2D8B0';
  const body = ellD(0, 0.06, 0.38, 0.42);
  const tuft = (s) => flat(polyD([[s * 0.4, -0.16], [s * 0.37, -0.5], [s * 0.1, -0.36]], 0.04), DARK);
  const wing = (s) => flat(ellD(s * 0.37, 0.14, 0.1, 0.26, s * -9), DARK);
  const disc = (s) => flat(ellD(s * 0.16, -0.14, 0.165, 0.155), CREAM, ink(0.026));
  const v = (x, y) => path(pb().M(x - 0.06, y).Q(x, y + 0.075, x + 0.06, y).d, 'none', line('#B98A5A', 0.024));
  return [
    ...[-1, 1].map((s) => flat(ellD(s * 0.14, 0.47, 0.09, 0.04), ORANGE, ink(0.026))),
    tuft(-1), tuft(1),
    ...solid(body, BROWN, { off: [-0.02, -0.05], hl: [[-0.24, -0.26, 0.05, 0.035, -30]] }),
    path(ellD(0, 0.26, 0.22, 0.21), CREAM),
    v(-0.09, 0.15), v(0.09, 0.15), v(-0.18, 0.27), v(0, 0.27), v(0.18, 0.27), v(-0.09, 0.39), v(0.09, 0.39),
    wing(-1), wing(1),
    disc(-1), disc(1),
    ...eye(-0.16, -0.14, 0.105, [0.2, 0.25]),
    ...eye(0.16, -0.14, 0.105, [-0.2, 0.25]),
    flat(polyD([[-0.06, -0.06], [0.06, -0.06], [0, 0.06]], 0.02), ORANGE, ink(0.026)),
  ];
};

draw.escargot = () => {
  const SKIN = '#DDA56B';
  const foot = 'M -0.52,0.36 C -0.42,0.31 -0.3,0.22 -0.2,0.2 L 0.2,0.14 C 0.28,0.12 0.3,0.0 0.34,-0.08 C 0.37,-0.17 0.5,-0.17 0.53,-0.06 C 0.57,0.06 0.55,0.2 0.51,0.28 C 0.49,0.35 0.44,0.39 0.36,0.39 L -0.4,0.39 Q -0.5,0.39 -0.52,0.36 Z';
  const shell = ellD(-0.1, -0.06, 0.35, 0.35);
  // spirale de la coquille : deux tours, du centre vers l'extérieur
  const sp = [];
  for (let i = 0; i <= 26; i++) {
    const t = (i * Math.PI) / 6;
    const r = 0.012 + 0.0203 * t;
    sp.push([-0.08 + r * Math.cos(t + 2.6), -0.06 + r * Math.sin(t + 2.6)]);
  }
  return [
    ...tubes(['M 0.38,-0.08 Q 0.36,-0.2 0.33,-0.32', 'M 0.48,-0.08 Q 0.52,-0.2 0.56,-0.3'], SKIN, 0.045),
    ...eye(0.33, -0.34, 0.06, [0.3, 0.2]),
    ...eye(0.56, -0.32, 0.06, [0.3, 0.2]),
    ...solid(foot, SKIN, { off: [-0.02, -0.04], dk: 0.16, hl: [[0.45, -0.07, 0.025, 0.035]] }),
    smile(0.5, 0.17, 0.09, 0.03, 0.024),
    blush(0.45, 0.22, 0.035, 0.025),
    ...solid(shell, BROWN, { off: [-0.025, -0.05], hl: [[-0.3, -0.2, 0.05, 0.09, 40]] }),
    path(smoothD(sp), 'none', line('#6B3E20', 0.034)),
  ];
};

draw.gland = () => {
  const NUT = '#C98B4B';
  const CAP = '#7A4A2A';
  const nut = 'M -0.27,0.0 C -0.27,0.22 -0.12,0.4 0,0.5 C 0.12,0.4 0.27,0.22 0.27,0.0 Z';
  const cap = 'M -0.35,0.08 C -0.38,-0.2 -0.2,-0.3 0,-0.3 C 0.2,-0.3 0.38,-0.2 0.35,0.08 Q 0,0.17 -0.35,0.08 Z';
  const scale = (x, y) => path(pb().M(x - 0.09, y).Q(x, y + 0.1, x + 0.09, y).d, 'none', line('#4E2D17', 0.026));
  const ob = [
    ...solid(nut, NUT, { off: [-0.025, -0.04], hl: [[-0.13, 0.22, 0.035, 0.1, -12]] }),
    ...tubes(['M 0,-0.3 Q 0.0,-0.4 0.06,-0.46'], '#5E3A20', 0.06),
    ...solid(cap, CAP, {
      off: [-0.02, -0.04],
      extra: [clip(cap, [scale(-0.18, -0.12), scale(0.0, -0.12), scale(0.18, -0.12), scale(-0.09, -0.01), scale(0.09, -0.01), scale(-0.27, -0.01), scale(0.27, -0.01)])],
      hl: [[-0.2, -0.17, 0.05, 0.045, -25, 0.4]],
    }),
  ];
  return rot(ob, 18);
};

draw.pommedepin = () => {
  // des écailles en pointe vers le bas, rangée par rangée ; les rangées du haut recouvrent celles du bas
  const rows = [
    [-0.31, [-0.095, 0.095]],
    [-0.17, [-0.19, 0, 0.19]],
    [-0.03, [-0.285, -0.095, 0.095, 0.285]],
    [0.11, [-0.19, 0, 0.19]],
    [0.25, [-0.095, 0.095]],
    [0.39, [0]],
  ];
  const w = 0.095;
  const sc = (x, y) => pb().M(x - w, y - 0.1).L(x + w, y - 0.1).L(x + w, y + 0.01).Q(x + w, y + 0.11, x, y + 0.14).Q(x - w, y + 0.11, x - w, y + 0.01).Z().d;
  const tiles = [];
  rows.slice().reverse().forEach(([y, xs], k) => {
    const ri = rows.length - 1 - k;
    xs.forEach((x, i) => {
      tiles.push(flat(sc(x, y), (ri + i) % 2 === 0 ? '#BE7F43' : '#A06232', ink(0.026)));
    });
  });
  const ob = [
    ...tubes(['M 0,-0.4 Q 0.0,-0.47 0.05,-0.53'], '#5E3A20', 0.06),
    path(ellD(0, 0.0, 0.28, 0.36), '#6B3E20'),
    ...tiles,
  ];
  return rot(ob, 14);
};

draw.valise = () => {
  const DARK = '#6B3E20';
  const body = rrD(-0.46, -0.2, 0.92, 0.6, 0.09);
  const latch = (x) => flat(rrD(x - 0.07, -0.08, 0.14, 0.12, 0.025), YELLOW, ink(0.024));
  return [
    ...tubes(['M -0.17,-0.2 L -0.17,-0.35 Q -0.17,-0.42 -0.1,-0.42 L 0.1,-0.42 Q 0.17,-0.42 0.17,-0.35 L 0.17,-0.2'], DARK, 0.055),
    ...[-1, 1].map((s) => flat(rrD(s * 0.32 - 0.07, 0.38, 0.14, 0.08, 0.03), DARK)),
    ...solid(body, BROWN, {
      off: [-0.025, -0.045],
      hl: [[-0.32, -0.1, 0.05, 0.035, -20]],
      extra: [clip(body, [rr(-0.34, -0.2, 0.1, 0.6, 0, DARK), rr(0.24, -0.2, 0.1, 0.6, 0, DARK), rr(-0.5, -0.02, 1, 0.045, 0, tone(DARK, { a: 0.6 }))])],
    }),
    latch(-0.13), latch(0.13),
  ];
};

draw.guitare = () => {
  const WOOD = '#C0773C';
  const half = [[0, -0.15], [0.12, -0.14], [0.185, -0.07], [0.185, 0.03], [0.115, 0.13], [0.2, 0.2], [0.27, 0.3], [0.255, 0.42], [0.14, 0.5], [0, 0.52]];
  const pts = [...half, ...half.slice(1, -1).reverse().map(([x, y]) => [-x, y])];
  const body = splineD(pts);
  const ob = [
    flat(rrD(-0.085, -0.68, 0.17, 0.21, 0.045), '#4E2D17'),
    flat(rrD(-0.05, -0.54, 0.1, 0.46, 0.012), '#6B3E20'),
    ...solid(body, WOOD, { off: [-0.025, -0.045], hl: [[-0.15, 0.36, 0.04, 0.11, -12]] }),
    ci(0, 0.24, 0.085, '#8A5230', ink(0.024)),
    ci(0, 0.24, 0.058, INK),
    flat(rrD(-0.11, 0.4, 0.22, 0.055, 0.02), '#4E2D17', ink(0.024)),
  ];
  return rot(ob, 40);
};

// ================================================================ neutre
draw.oeuf = () => {
  // un œuf au plat : un blanc en lobes irrondis, un jaune doré au milieu
  const lobes = [[-100, 0.23, 0.2], [-48, 0.26, 0.17], [8, 0.25, 0.22], [58, 0.23, 0.19], [112, 0.27, 0.21], [168, 0.22, 0.18], [222, 0.27, 0.2]];
  const white = puffD(lobes.map(([a, d, r]) => [d * Math.cos((a * Math.PI) / 180) * 1.1, 0.02 + d * Math.sin((a * Math.PI) / 180), r]));
  return [
    ...solid(white, WHITEISH, { off: [-0.025, -0.05], dk: 0.12, hl: [[-0.27, -0.2, 0.06, 0.03, -35]] }),
    ...solid(ellD(-0.03, 0.02, 0.2, 0.2), '#FFC21F', { off: [-0.02, -0.04], dk: 0.16, hl: [[-0.1, -0.06, 0.05, 0.07, -35]] }),
  ];
};

draw.lapin = () => {
  const head = ellD(0, 0.17, 0.31, 0.27);
  const ear = (s) => [
    ...solid(ellD(s * 0.14, -0.22, 0.105, 0.31, s * 9), WHITEISH, { off: [-0.015, -0.04], dk: 0.1 }),
    path(ellD(s * 0.145, -0.2, 0.05, 0.2, s * 9), '#FFB6C9'),
  ];
  return [
    ...ear(-1), ...ear(1),
    ...solid(head, WHITEISH, { off: [-0.02, -0.045], dk: 0.1, hl: [[-0.2, 0.0, 0.06, 0.035, -30]] }),
    ...eye(-0.13, 0.11, 0.06, [0.15, 0.25]),
    ...eye(0.13, 0.11, 0.06, [-0.15, 0.25]),
    blush(-0.23, 0.22, 0.045, 0.03),
    blush(0.23, 0.22, 0.045, 0.03),
    flat(rrD(-0.052, 0.285, 0.104, 0.1, 0.022), WHITE, ink(0.022)),
    ln(0, 0.285, 0, 0.385, tone(INK, { a: 0.4 }), 0.014, { nosil: true }),
    path(pb().M(0, 0.2).L(0, 0.25).d, 'none', line(INK, 0.022)),
    path(pb().M(0, 0.25).Q(-0.05, 0.31, -0.11, 0.27).d, 'none', line(INK, 0.024)),
    path(pb().M(0, 0.25).Q(0.05, 0.31, 0.11, 0.27).d, 'none', line(INK, 0.024)),
    flat(polyD([[-0.055, 0.15], [0.055, 0.15], [0, 0.215]], 0.03), '#FF7A9A', ink(0.02)),
  ];
};

draw.mouton = () => {
  const WOOL = WHITEISH;
  const FACE = '#3F4258';
  const ring = [];
  for (let i = 0; i < 8; i++) {
    const t = ((-100 + i * 45) * Math.PI) / 180;
    ring.push([-0.15 + 0.28 * Math.cos(t), -0.04 + 0.19 * Math.sin(t), 0.15]);
  }
  const body = puffD(ring);
  const leg = (x) => flat(capD(x, 0.1, x, 0.39, 0.04), FACE);
  const curl = (x, y, a) => path(pb().arc(x, y, 0.045, 0.045, a, a + 230).d, 'none', line(tone(INK, { a: 0.28 }), 0.022));
  return [
    leg(-0.36), leg(-0.21), leg(-0.01), leg(0.15),
    ...solid(body, WOOL, { off: [-0.02, -0.05], dk: 0.12, extra: [curl(-0.28, -0.06, 20), curl(-0.08, 0.06, 160), curl(-0.02, -0.14, 60)], hl: [[-0.27, -0.2, 0.05, 0.04, -30]] }),
    ...solid(ellD(0.36, 0.08, 0.15, 0.19, 8), FACE, { off: [-0.015, -0.035], dk: 0.2, hl: [[0.3, -0.03, 0.03, 0.045, 20, 0.3]] }),
    flat(ellD(0.2, 0.0, 0.1, 0.05, -28), FACE),
    ...solid(ellD(0.34, -0.1, 0.085, 0.065), WOOL, { off: [-0.01, -0.02], dk: 0.1, sw: 0.03 }),
    ...eye(0.42, 0.05, 0.052, [0.3, 0.2]),
    smile(0.43, 0.17, 0.08, 0.03, 0.022),
  ];
};

draw.panda = () => {
  const BLK = '#34374D';
  const head = ellD(0, 0.05, 0.45, 0.38);
  const ear = (s) => solid(ellD(s * 0.33, -0.27, 0.14, 0.14), BLK, { off: [-0.015, -0.03], dk: 0.3 });
  const patch = (s) => path(ellD(s * 0.19, 0.0, 0.1, 0.14, s * 25), BLK);
  return [
    ...ear(-1), ...ear(1),
    ...solid(head, WHITEISH, { off: [-0.02, -0.05], dk: 0.1, extra: [patch(-1), patch(1)], hl: [[-0.27, -0.18, 0.06, 0.035, -30]] }),
    ...eye(-0.19, -0.01, 0.052, [0.15, 0.2]),
    ...eye(0.19, -0.01, 0.052, [-0.15, 0.2]),
    flat(polyD([[-0.075, 0.1], [0.075, 0.1], [0, 0.17]], 0.04), BLK, ink(0.02)),
    path(pb().M(0, 0.17).L(0, 0.21).d, 'none', line(INK, 0.024)),
    path(pb().M(0, 0.21).Q(-0.05, 0.29, -0.12, 0.24).d, 'none', line(INK, 0.026)),
    path(pb().M(0, 0.21).Q(0.05, 0.29, 0.12, 0.24).d, 'none', line(INK, 0.026)),
    blush(-0.33, 0.17, 0.045, 0.03),
    blush(0.33, 0.17, 0.045, 0.03),
  ];
};

draw.pingouin = () => {
  const BLK = '#343852';
  const body = 'M 0,-0.45 C 0.2,-0.45 0.34,-0.32 0.36,-0.1 C 0.4,0.12 0.39,0.3 0.29,0.4 C 0.2,0.47 -0.2,0.47 -0.29,0.4 C -0.39,0.3 -0.4,0.12 -0.36,-0.1 C -0.34,-0.32 -0.2,-0.45 0,-0.45 Z';
  const belly = splineD([[0, -0.07], [-0.08, -0.16], [-0.18, -0.17], [-0.26, -0.09], [-0.27, 0.05], [-0.25, 0.22], [-0.15, 0.36], [0, 0.4], [0.15, 0.36], [0.25, 0.22], [0.27, 0.05], [0.26, -0.09], [0.18, -0.17], [0.08, -0.16]]);
  const flipper = (s) => solid(ellD(s * 0.41, 0.07, 0.075, 0.22, s * -16), BLK, { off: [-0.015, -0.03], dk: 0.3 });
  return [
    ...[-1, 1].map((s) => flat(ellD(s * 0.15, 0.46, 0.125, 0.05), ORANGE)),
    ...flipper(-1), ...flipper(1),
    ...solid(body, BLK, { off: [-0.025, -0.05], dk: 0.3, hl: [[-0.2, -0.33, 0.05, 0.04, -30, 0.35]] }),
    flat(belly, WHITEISH, ink(0.026)),
    ...eye(-0.13, -0.09, 0.06, [0.15, 0.2]),
    ...eye(0.13, -0.09, 0.06, [-0.15, 0.2]),
    flat(polyD([[-0.085, -0.02], [0.085, -0.02], [0, 0.09]], 0.03), ORANGE, ink(0.026)),
    blush(-0.21, 0.04, 0.04, 0.028),
    blush(0.21, 0.04, 0.04, 0.028),
  ];
};

draw.fusee = () => {
  const SLATE = '#7A8299';
  const body = 'M 0,-0.48 C 0.15,-0.4 0.23,-0.2 0.23,0.02 L 0.23,0.22 L -0.23,0.22 L -0.23,0.02 C -0.23,-0.2 -0.15,-0.4 0,-0.48 Z';
  const nose = 'M -0.3,-0.55 L 0.3,-0.55 L 0.3,-0.21 Q 0,-0.15 -0.3,-0.21 Z';
  const fin = (s) => flat(polyD([[s * 0.2, -0.04], [s * 0.45, 0.33], [s * 0.45, 0.42], [s * 0.2, 0.22]], 0.035), SLATE);
  return [
    flat('M -0.12,0.3 C -0.19,0.42 -0.08,0.52 0,0.66 C 0.08,0.52 0.19,0.42 0.12,0.3 Z', ORANGE),
    path('M -0.06,0.3 C -0.1,0.4 -0.03,0.46 0,0.54 C 0.03,0.46 0.1,0.4 0.06,0.3 Z', YELLOW),
    fin(-1), fin(1),
    flat(polyD([[-0.13, 0.2], [0.13, 0.2], [0.1, 0.32], [-0.1, 0.32]], 0.025), '#59627C'),
    ...solid(body, WHITEISH, { off: [-0.025, -0.045], dk: 0.14, extra: [clip(body, [path(nose, SLATE), rr(-0.3, 0.1, 0.6, 0.05, 0, SLATE)])], hl: [[-0.14, 0.0, 0.025, 0.1, 0, 0.55]] }),
    flat(ellD(0, -0.04, 0.115, 0.115), '#8A94AD'),
    flat(ellD(0, -0.04, 0.082, 0.082), '#BFE3FF', ink(0.022)),
    spot(-0.03, -0.07, 0.022, 0.03, -30, 0.7),
  ];
};

draw.ampoule = () => {
  const glass = 'M -0.13,0.24 C -0.13,0.12 -0.35,0.06 -0.35,-0.16 C -0.35,-0.35 -0.19,-0.48 0,-0.48 C 0.19,-0.48 0.35,-0.35 0.35,-0.16 C 0.35,0.06 0.13,0.12 0.13,0.24 Z';
  const GR = '#9AA3B7';
  return [
    ...solid(glass, '#FFF6CF', { off: [-0.025, -0.05], dk: 0.1, hl: [[-0.2, -0.28, 0.05, 0.1, 30], [-0.26, -0.1, 0.02, 0.02]] }),
    path(pb().M(-0.07, 0.22).L(-0.07, 0.04).Q(-0.11, -0.12, 0, -0.12).Q(0.11, -0.12, 0.07, 0.04).L(0.07, 0.22).d, 'none', line('#FF9A1F', 0.03)),
    ...solid(rrD(-0.15, 0.22, 0.3, 0.09, 0.03), GR, { off: [-0.015, -0.025], dk: 0.2 }),
    ...solid(rrD(-0.15, 0.3, 0.3, 0.09, 0.03), '#B6BECE', { off: [-0.015, -0.025], dk: 0.2 }),
    ...solid(pb().M(-0.13, 0.38).L(0.13, 0.38).Q(0.11, 0.5, 0, 0.5).Q(-0.11, 0.5, -0.13, 0.38).Z().d, '#59627C', { off: [-0.01, -0.02], dk: 0.2 }),
  ];
};

draw.horloge = () => {
  // un réveil : deux cloches, deux pieds, un cadran blanc aux aiguilles sur dix heures dix
  const RING = '#A0A7B8';
  const BELL = '#808799';
  const cx = 0;
  const cy = 0.08;
  const bell = (s) => solid(ellD(s * 0.27, cy - 0.34, 0.16, 0.11, s * 38), BELL, { off: [-0.015, -0.03], dk: 0.2 });
  const foot = (s) => flat(capD(s * 0.24, cy + 0.38, s * 0.33, cy + 0.48, 0.055), BELL);
  const hand = (deg, len, w) => {
    const a = (deg * Math.PI) / 180;
    return ln(cx, cy, cx + len * Math.sin(a), cy - len * Math.cos(a), INK, w);
  };
  const tick = (deg) => {
    const a = (deg * Math.PI) / 180;
    return ln(cx + 0.2 * Math.sin(a), cy - 0.2 * Math.cos(a), cx + 0.25 * Math.sin(a), cy - 0.25 * Math.cos(a), INK, 0.035);
  };
  return [
    ...bell(-1), ...bell(1),
    foot(-1), foot(1),
    ...solid(ellD(cx, cy, 0.41, 0.41), RING, { off: [-0.025, -0.05], dk: 0.2, hl: [[-0.28, -0.2, 0.05, 0.09, 40]] }),
    flat(ellD(cx, cy, 0.3, 0.3), WHITE, ink(0.026)),
    tick(0), tick(90), tick(180), tick(270),
    hand(-60, 0.15, 0.045),
    hand(60, 0.22, 0.036),
    ci(cx, cy, 0.036, INK),
  ];
};

draw.cle = () => {
  const METAL = '#AEB7C9';
  const ob = [
    ...tubes([[ellD(-0.3, 0, 0.135, 0.135), 0.11], ['M -0.12,0 L 0.4,0', 0.11], ['M 0.19,0.02 L 0.19,0.17', 0.11], ['M 0.34,0.02 L 0.34,0.13', 0.11]], METAL),
    ln(-0.1, -0.03, 0.37, -0.03, tone(WHITE, { a: 0.55 }), 0.024, { nosil: true }),
    spot(-0.395, -0.095, 0.018, 0.04, 45, 0.6),
  ];
  return rot(ob, 45);
};

draw.piano = () => {
  const BLK = '#3A3D55';
  const keysX = -0.42;
  const kw = 0.12;
  const gaps = [1, 2, 4, 5, 6];
  const seps = [1, 2, 3, 4, 5, 6].map((k) => ln(keysX + k * kw, -0.08, keysX + k * kw, 0.34, INK, 0.022));
  return [
    ...solid(rrD(-0.46, -0.44, 0.92, 0.36, 0.07), BLK, { off: [-0.02, -0.04], dk: 0.3, hl: [[-0.3, -0.36, 0.1, 0.022, 0, 0.35]] }),
    flat(rrD(keysX, -0.1, 0.84, 0.46, 0.03), WHITEISH),
    ...seps,
    ...gaps.map((k) => flat(rrD(keysX + k * kw - 0.04, -0.1, 0.08, 0.26, 0.02), BLK, ink(0.02))),
    ...[-1, 1].map((s) => flat(rrD(s * 0.33 - 0.06, 0.42, 0.12, 0.1, 0.03), BLK)),
    ...solid(rrD(-0.46, 0.34, 0.92, 0.1, 0.04), BLK, { off: [-0.02, -0.03], dk: 0.3 }),
  ];
};

module.exports = draw;
