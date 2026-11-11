// Lot 1 de « Cherche et Trouve » : 20 objets (rouge, orange, jaune). Chaque dessin est une liste d'opérations dans [-0.5, 0.5]² (y vers le bas),
// recadrée automatiquement par `fit`. Style plat, contour encre, ombre de bord et reflet via `solid`.
'use strict';
const { solid, flat, tubes, eye, smile, blush, spot, el, ci, rr, path, ln, clip, ink, line, tone, pb, ellD, rrD, capD, polyD, starD, starPts, lensD, splineD, rot, RED, ORANGE, YELLOW, BLUE, BROWN, WHITEISH, LEAF, STEM, INK, WHITE } = require('./objkit.js');

const draw = {};

/** Courbe ouverte lisse (Catmull-Rom) passant par les points. */
const openD = (pts) => {
  const n = pts.length;
  const b = pb().M(pts[0][0], pts[0][1]);
  for (let i = 0; i < n - 1; i++) {
    const p0 = pts[Math.max(0, i - 1)];
    const p1 = pts[i];
    const p2 = pts[i + 1];
    const p3 = pts[Math.min(n - 1, i + 2)];
    b.C(p1[0] + (p2[0] - p0[0]) / 6, p1[1] + (p2[1] - p0[1]) / 6, p2[0] - (p3[0] - p1[0]) / 6, p2[1] - (p3[1] - p1[1]) / 6, p2[0], p2[1]);
  }
  return b.d;
};

// ================================================================ rouge
draw.tomate = () => {
  const body = 'M 0,-0.25 C 0.1,-0.36 0.32,-0.39 0.43,-0.27 C 0.56,-0.12 0.54,0.18 0.38,0.33 C 0.26,0.45 0.1,0.45 0,0.44 C -0.1,0.45 -0.26,0.45 -0.38,0.33 C -0.54,0.18 -0.56,-0.12 -0.43,-0.27 C -0.32,-0.39 -0.1,-0.36 0,-0.25 Z';
  // le chapeau : une étoile à cinq pointes aplatie, posée dans le creux du haut
  const calyx = polyD(starPts(0, -0.28, 5, 0.34, 0.09, -90).map(([x, y]) => [x, -0.28 + (y + 0.28) * 0.5]), 0.03);
  return [
    ...tubes(['M 0,-0.3 Q 0,-0.42 0.05,-0.5'], '#5E8F3A', 0.055),
    ...solid(body, '#EC3B2F', { off: [-0.025, -0.05], hl: [[-0.31, -0.02, 0.06, 0.12, -15], [-0.29, 0.2, 0.025, 0.025]] }),
    flat(calyx, LEAF),
  ];
};

draw.piment = () => {
  // une courbe centrale (Bézier cubique) et une demi-largeur qui s'effile jusqu'à la pointe
  const P = [[-0.2, -0.3], [-0.32, 0.15], [0.0, 0.42], [0.44, 0.2]];
  const at = (t) => {
    const u = 1 - t;
    const x = u * u * u * P[0][0] + 3 * u * u * t * P[1][0] + 3 * u * t * t * P[2][0] + t * t * t * P[3][0];
    const y = u * u * u * P[0][1] + 3 * u * u * t * P[1][1] + 3 * u * t * t * P[2][1] + t * t * t * P[3][1];
    const dx = 3 * u * u * (P[1][0] - P[0][0]) + 6 * u * t * (P[2][0] - P[1][0]) + 3 * t * t * (P[3][0] - P[2][0]);
    const dy = 3 * u * u * (P[1][1] - P[0][1]) + 6 * u * t * (P[2][1] - P[1][1]) + 3 * t * t * (P[3][1] - P[2][1]);
    const L = Math.hypot(dx, dy);
    return { x, y, nx: -dy / L, ny: dx / L, ang: (Math.atan2(dy, dx) * 180) / Math.PI };
  };
  const hw = (t) => 0.18 * Math.pow(1 - Math.pow(t, 1.5), 0.75);
  const N = 9;
  const left = [];
  const right = [];
  for (let i = 0; i < N; i++) {
    const a = at(i / N);
    left.push([a.x + a.nx * hw(i / N), a.y + a.ny * hw(i / N)]);
    right.push([a.x - a.nx * hw(i / N), a.y - a.ny * hw(i / N)]);
  }
  const a0 = at(0);
  const tip = at(1);
  const d = splineD([[a0.x, a0.y - 0.04], ...left, [tip.x, tip.y], ...right.reverse()]);
  const h = at(0.4);
  const { x, y } = a0;
  const cap = `M ${x - 0.19},${y + 0.03} C ${x - 0.2},${y - 0.08} ${x - 0.09},${y - 0.12} ${x},${y - 0.12} C ${x + 0.09},${y - 0.12} ${x + 0.2},${y - 0.08} ${x + 0.19},${y + 0.03} Q ${x},${y + 0.07} ${x - 0.19},${y + 0.03} Z`;
  return [
    ...tubes([`M ${x},${y - 0.1} Q ${x + 0.01},${y - 0.2} ${x + 0.1},${y - 0.24}`], '#4C9A3A', 0.07),
    ...solid(d, RED, { off: [-0.02, -0.04], hl: [[h.x + h.nx * 0.08, h.y + h.ny * 0.08, 0.022, 0.09, h.ang - 90]] }),
    flat(cap, LEAF),
  ];
};

draw.coccinelle = () => {
  const shell = ellD(0, 0.1, 0.42, 0.4);
  const leg = (s, y) => ln(s * 0.34, y, s * 0.5, y + 0.05, INK, 0.05);
  return [
    ...[-1, 1].map((s) => path(pb().M(s * 0.09, -0.36).Q(s * 0.14, -0.46, s * 0.24, -0.5).d, 'none', line(INK, 0.035))),
    ...[-1, 1].flatMap((s) => [leg(s, -0.02), leg(s, 0.16), leg(s, 0.33)]),
    ...solid(ellD(0, -0.3, 0.23, 0.17), '#3A3D58', { off: [-0.01, -0.02], dk: 0.3 }),
    ci(-0.09, -0.37, 0.05, WHITE), ci(0.09, -0.37, 0.05, WHITE),
    ci(-0.085, -0.365, 0.022, INK), ci(0.085, -0.365, 0.022, INK),
    ...solid(shell, RED, {
      off: [-0.025, -0.05],
      hl: [[-0.25, -0.1, 0.05, 0.1, 20]],
      extra: [clip(shell, [ci(-0.2, 0.0, 0.085, INK), ci(0.2, 0.0, 0.085, INK), ci(-0.17, 0.27, 0.075, INK), ci(0.17, 0.27, 0.075, INK), ci(0, 0.4, 0.06, INK)])],
    }),
    ln(0, -0.3, 0, 0.5, INK, 0.032),
  ];
};

draw.pompier = () => {
  const box = rrD(-0.5, -0.1, 0.64, 0.34, 0.05);
  const cab = 'M 0.08,-0.28 Q 0.08,-0.32 0.13,-0.32 L 0.3,-0.32 Q 0.35,-0.32 0.38,-0.27 L 0.48,-0.06 Q 0.5,-0.03 0.5,0.01 L 0.5,0.2 Q 0.5,0.24 0.46,0.24 L 0.08,0.24 Z';
  const wheel = (x) => [ci(x, 0.26, 0.125, '#41465F', ink()), ci(x, 0.26, 0.052, '#D9DEE9', ink(0.022))];
  return [
    ...solid(box, RED, { dk: 0.22, off: [-0.02, -0.04], extra: [clip(box, [rr(-0.52, 0.05, 0.7, 0.06, 0, '#FFD23F')])] }),
    flat(rrD(-0.47, -0.27, 0.54, 0.17, 0.03), '#F3F5FA'),
    ...[-0.34, -0.2, -0.06].map((x) => ln(x, -0.27, x, -0.1, INK, 0.026)),
    ...solid(cab, RED, { dk: 0.22, off: [-0.02, -0.04], extra: [clip(cab, [rr(0.0, 0.05, 0.6, 0.06, 0, '#FFD23F')])] }),
    flat('M 0.15,-0.25 L 0.3,-0.25 Q 0.33,-0.25 0.35,-0.22 L 0.41,-0.08 L 0.15,-0.08 Z', '#BFE3FF', ink(0.026)),
    flat(rrD(0.15, -0.41, 0.17, 0.1, 0.04), BLUE, ink(0.026)),
    flat(ellD(0.485, 0.1, 0.03, 0.05), '#FFE066', ink(0.022)),
    ...wheel(-0.3), ...wheel(0.28),
  ];
};

draw.tulipe = () => {
  const head = 'M -0.3,-0.42 L -0.14,-0.22 L 0,-0.46 L 0.14,-0.22 L 0.3,-0.42 C 0.42,-0.12 0.3,0.12 0,0.16 C -0.3,0.12 -0.42,-0.12 -0.3,-0.42 Z';
  const front = pb().M(0, -0.46).C(0.23, -0.28, 0.2, 0.04, 0, 0.16).C(-0.2, 0.04, -0.23, -0.28, 0, -0.46).Z().d;
  return [
    flat(lensD(0, 0.5, -0.36, 0.1, 0.03, 0.11), LEAF),
    flat(lensD(0, 0.5, 0.36, 0.1, 0.11, 0.03), LEAF),
    ...tubes(['M 0,0.1 L 0,0.5'], '#3FAE49', 0.07),
    ...solid(head, '#CC2B27', { off: [-0.02, -0.04], dk: 0.1 }),
    ...solid(front, RED, { off: [-0.02, -0.04], hl: [[-0.08, -0.15, 0.025, 0.1, 8]] }),
  ];
};

draw.cerfvolant = () => {
  const T = [0, -0.5]; const R = [0.3, -0.18]; const B = [0, 0.08]; const L = [-0.3, -0.18]; const C = [0, -0.18];
  const kite = polyD([T, R, B, L], 0.03);
  const LIGHT = '#FF6F5C';
  const tail = [[0, 0.08], [0.07, 0.19], [-0.06, 0.31], [0.03, 0.43]];
  const bow = (x, y, c) => [
    flat(polyD([[x, y], [x - 0.085, y - 0.06], [x - 0.085, y + 0.06]], 0.015), c, ink(0.026)),
    flat(polyD([[x, y], [x + 0.085, y - 0.06], [x + 0.085, y + 0.06]], 0.015), c, ink(0.026)),
  ];
  const ob = [
    path(openD(tail), 'none', line(INK, 0.032)),
    ...bow(tail[1][0], tail[1][1], YELLOW), ...bow(tail[2][0], tail[2][1], BLUE),
    ...solid(kite, RED, { off: [-0.025, -0.04], extra: [clip(kite, [path(polyD([T, R, C], 0), LIGHT), path(polyD([B, L, C], 0), LIGHT)])] }),
    path(pb().M(T[0], T[1]).L(B[0], B[1]).M(L[0], L[1]).L(R[0], R[1]).d, 'none', line(INK, 0.026)),
  ];
  return rot(ob, 16);
};

draw.bonnet = () => {
  const dome = 'M -0.38,0.12 C -0.42,-0.26 -0.22,-0.4 0,-0.4 C 0.22,-0.4 0.42,-0.26 0.38,0.12 Z';
  const cuff = rrD(-0.46, 0.06, 0.92, 0.3, 0.1);
  return [
    ...solid(dome, RED, { off: [-0.025, -0.05], hl: [[-0.22, -0.18, 0.05, 0.1, 25]], extra: [clip(dome, [rr(-0.5, -0.16, 1, 0.1, 0, WHITEISH)])] }),
    ...solid(cuff, '#D92D2A', { off: [-0.02, -0.04], dk: 0.2, extra: [-0.3, -0.15, 0, 0.15, 0.3].map((x) => ln(x, 0.09, x, 0.33, tone('#8E1B1B', { a: 0.55 }), 0.03)) }),
    ...solid(ellD(0, -0.42, 0.12, 0.11), WHITEISH, { off: [-0.02, -0.03], dk: 0.1 }),
  ];
};

// ================================================================ orange
draw.croissant = () => {
  const C = '#F4A23A';
  // un croissant en arc, cornes vers le bas : des bandes (traits épais à bouts ronds, posés en travers de l'arc) fondues en une seule silhouette,
  // puis les plis entre les bandes
  const R = 0.3;
  const P = (r, deg) => [r * Math.sin((deg * Math.PI) / 180), 0.2 - r * Math.cos((deg * Math.PI) / 180)];
  const band = (deg, hw, half) => {
    const [x1, y1] = P(R - half, deg);
    const [x2, y2] = P(R + half, deg);
    return [`M ${x1},${y1} L ${x2},${y2}`, 2 * hw];
  };
  const crease = (deg, h1, h2) => {
    const [x1, y1] = P(R - h1, deg);
    const [x2, y2] = P(R + h2, deg);
    return ln(x1, y1, x2, y2, tone(C, { d: 0.22 }), 0.03);
  };
  // les bandes rapetissent vers les cornes, qui se recourbent vers l'intérieur
  const sides = [-1, 1].flatMap((s) => [band(s * 38, 0.115, 0.06), band(s * 66, 0.095, 0.04), band(s * 90, 0.075, 0.02), band(s * 112, 0.055, 0)]);
  return [
    ...tubes([...sides, band(0, 0.14, 0.08)], C),
    ...[-1, 1].flatMap((s) => [crease(s * 19, 0.13, 0.13), crease(s * 52, 0.07, 0.1), crease(s * 78, 0.02, 0.06)]),
    spot(-0.07, -0.2, 0.05, 0.03, -20),
  ];
};

draw.pizza = () => {
  const cheese = polyD([[-0.4, -0.24], [0.4, -0.24], [0, 0.48]], 0.04);
  const pep = (x, y, r) => solid(ellD(x, y, r, r), '#D8342C', { off: [-0.012, -0.025], dk: 0.2, sw: 0.026 });
  const ob = [
    ...solid(cheese, '#FFAE3B', { off: [-0.025, -0.04], dk: 0.2 }),
    ...pep(-0.15, -0.08, 0.08), ...pep(0.16, -0.02, 0.075), ...pep(-0.01, 0.22, 0.065),
    ...solid(capD(-0.4, -0.3, 0.4, -0.3, 0.1), '#E0852A', { off: [-0.02, -0.04], dk: 0.18, hl: [[-0.25, -0.34, 0.1, 0.02, 0, 0.5]] }),
  ];
  return rot(ob, 14);
};

draw.hamburger = () => {
  const BUN = '#F2A03C';
  const topBun = 'M -0.46,-0.02 C -0.46,-0.36 -0.24,-0.46 0,-0.46 C 0.24,-0.46 0.46,-0.36 0.46,-0.02 Q 0.46,0.03 0.41,0.03 L -0.41,0.03 Q -0.46,0.03 -0.46,-0.02 Z';
  const botBun = 'M -0.44,0.22 L 0.44,0.22 Q 0.44,0.46 0.28,0.46 L -0.28,0.46 Q -0.44,0.46 -0.44,0.22 Z';
  const lettuce = pb().M(-0.49, -0.02).Q(-0.44, 0.1, -0.36, 0.06).Q(-0.28, 0.14, -0.18, 0.07).Q(-0.09, 0.14, 0, 0.07).Q(0.09, 0.14, 0.18, 0.07).Q(0.28, 0.14, 0.36, 0.06).Q(0.44, 0.1, 0.49, -0.02).Z().d;
  const seed = (x, y, a) => path(ellD(x, y, 0.05, 0.026, a), '#FFF1CC');
  return [
    ...solid(botBun, BUN, { off: [-0.02, -0.04], dk: 0.2 }),
    flat(rrD(-0.46, 0.08, 0.92, 0.17, 0.08), '#7A4A2A'),
    flat(pb().M(-0.4, 0.04).L(0.4, 0.04).L(0.4, 0.1).L(0.12, 0.1).L(0.05, 0.2).L(-0.02, 0.1).L(-0.4, 0.1).Z().d, '#FFD23F'),
    flat(lettuce, '#55C85A'),
    ...solid(topBun, BUN, {
      off: [-0.025, -0.05],
      dk: 0.2,
      hl: [[-0.28, -0.26, 0.06, 0.1, 35]],
      extra: [seed(-0.08, -0.31, -10), seed(0.13, -0.22, 15), seed(-0.2, -0.12, 20), seed(0.0, -0.13, -5), seed(0.28, -0.09, 25)],
    }),
  ];
};

draw.crabe = () => {
  const claw = (s) => {
    const cx = s * 0.37;
    const cy = -0.17;
    const R = 0.18;
    const d = pb().M(cx, cy).arc(cx, cy, R, R, -60, 240).Z().d;
    return rot([
      ...tubes([`M ${s * 0.27},0.04 L ${s * 0.36},-0.06`], ORANGE, 0.06),
      ...solid(d, ORANGE, { off: [-0.015, -0.03] }),
    ], s * 14, cx, cy);
  };
  const leg = (s, x1, y1, x2, y2) => `M ${s * x1},${y1} L ${s * x2},${y2}`;
  return [
    ...tubes([-1, 1].flatMap((s) => [leg(s, 0.28, 0.24, 0.45, 0.36), leg(s, 0.2, 0.32, 0.34, 0.48), leg(s, 0.33, 0.14, 0.5, 0.22)]), ORANGE, 0.04),
    ...tubes(['M -0.13,-0.06 L -0.13,-0.24', 'M 0.13,-0.06 L 0.13,-0.24'], ORANGE, 0.045),
    ...claw(-1), ...claw(1),
    ...solid(ellD(0, 0.12, 0.4, 0.29), ORANGE, { off: [-0.02, -0.05], hl: [[-0.25, -0.01, 0.07, 0.03, -20]] }),
    ...eye(-0.13, -0.3, 0.08, [0.1, 0.25]), ...eye(0.13, -0.3, 0.08, [-0.1, 0.25]),
    smile(0, 0.1, 0.2, 0.05, 0.026),
    blush(-0.25, 0.1), blush(0.25, 0.1),
  ];
};

draw.lion = () => {
  const mane = starD(0, 0.02, 12, 0.5, 0.4, -90, 0.075);
  const ear = (s) => solid(ellD(s * 0.27, -0.3, 0.1, 0.1), '#FFC470', { off: [-0.01, -0.02], sw: 0.03 });
  return [
    ...solid(mane, '#F07F1F', { off: [-0.02, -0.04], dk: 0.22 }),
    ...ear(-1), ...ear(1),
    ...solid(ellD(0, 0.06, 0.32, 0.3), '#FFC470', { off: [-0.02, -0.04], dk: 0.14, hl: [[-0.17, -0.12, 0.06, 0.04, -30]] }),
    ...eye(-0.13, -0.04, 0.06, [0.1, 0.2]), ...eye(0.13, -0.04, 0.06, [-0.1, 0.2]),
    flat(ellD(0, 0.16, 0.16, 0.12), '#FFEBC8', ink(0.022)),
    path(polyD([[-0.06, 0.09], [0.06, 0.09], [0, 0.16]], 0.03), '#7A3B1C'),
    path(pb().M(0, 0.15).L(0, 0.2).Q(-0.05, 0.27, -0.1, 0.22).d, 'none', line(INK, 0.022)),
    path(pb().M(0, 0.2).Q(0.05, 0.27, 0.1, 0.22).d, 'none', line(INK, 0.022)),
    blush(-0.24, 0.12, 0.045, 0.032), blush(0.24, 0.12, 0.045, 0.032),
  ];
};

draw.feuille = () => {
  // une feuille d'érable : trois pointes de chaque côté, la grande pointe du haut, une courte queue
  const half = [[0, -0.5], [0.08, -0.33], [0.19, -0.38], [0.15, -0.2], [0.42, -0.3], [0.34, -0.12], [0.5, -0.02], [0.28, 0.08], [0.38, 0.22], [0.1, 0.18]];
  const pts = [...half, [0.03, 0.3], [-0.03, 0.3], ...half.slice(1).reverse().map(([x, y]) => [-x, y])];
  const d = polyD(pts, 0.02);
  const vein = tone('#F27A1A', { d: 0.3 });
  const ob = [
    ...tubes(['M 0,0.1 L 0,0.5'], STEM, 0.05),
    ...solid(d, '#F27A1A', {
      off: [-0.025, -0.04],
      dk: 0.2,
      hl: [[-0.1, -0.28, 0.025, 0.08, 5]],
      extra: [clip(d, [ln(0, 0.3, 0, -0.36, vein, 0.028), ln(0, 0.12, 0.3, -0.18, vein, 0.026), ln(0, 0.12, -0.3, -0.18, vein, 0.026), ln(0, 0.18, 0.26, 0.17, vein, 0.024), ln(0, 0.18, -0.26, 0.17, vein, 0.024)])],
    }),
  ];
  return rot(ob, 14);
};

draw.montgolfiere = () => {
  const env = 'M 0.09,0.18 C 0.14,0.1 0.44,-0.04 0.44,-0.22 C 0.44,-0.4 0.24,-0.5 0,-0.5 C -0.24,-0.5 -0.44,-0.4 -0.44,-0.22 C -0.44,-0.04 -0.14,0.1 -0.09,0.18 Z';
  const mid = 'M 0,-0.5 C 0.38,-0.42 0.34,0.0 0.09,0.18 L -0.09,0.18 C -0.34,0.0 -0.38,-0.42 0,-0.5 Z';
  const mid2 = 'M 0,-0.5 C 0.25,-0.42 0.2,0.0 0.05,0.18 L -0.05,0.18 C -0.2,0.0 -0.25,-0.42 0,-0.5 Z';
  return [
    ln(-0.07, 0.16, -0.1, 0.32, INK, 0.028), ln(0.07, 0.16, 0.1, 0.32, INK, 0.028),
    ...solid(rrD(-0.14, 0.3, 0.28, 0.18, 0.04), BROWN, { off: [-0.015, -0.03], dk: 0.2 }),
    ...solid(env, '#E8601C', {
      off: [-0.025, -0.05],
      dk: 0.2,
      extra: [clip(env, [path(mid, ORANGE), path(mid2, '#FFE066')]), path(mid, 'none', line(INK, 0.02)), path(mid2, 'none', line(INK, 0.02))],
      hl: [[-0.3, -0.27, 0.045, 0.09, 25]],
    }),
  ];
};

draw.cone = () => {
  const body = polyD([[-0.12, -0.46], [0.12, -0.46], [0.34, 0.34], [-0.34, 0.34]], [0.05, 0.05, 0.03, 0.03]);
  return [
    ...solid(body, ORANGE, {
      off: [-0.025, -0.03],
      dk: 0.2,
      extra: [clip(body, [rr(-0.5, -0.28, 1, 0.15, 0, WHITE), rr(-0.5, 0.0, 1, 0.16, 0, WHITE)])],
    }),
    ...solid(rrD(-0.46, 0.3, 0.92, 0.17, 0.05), '#E8641E', { off: [-0.02, -0.03], dk: 0.22 }),
  ];
};

// ================================================================ jaune
draw.citron = () => {
  const body = 'M -0.52,0.01 C -0.47,-0.01 -0.44,-0.05 -0.4,-0.1 C -0.3,-0.3 -0.12,-0.34 0,-0.34 C 0.12,-0.34 0.3,-0.3 0.4,-0.1 C 0.44,-0.05 0.47,-0.01 0.52,0.01 C 0.47,0.03 0.44,0.06 0.4,0.1 C 0.3,0.3 0.12,0.34 0,0.34 C -0.12,0.34 -0.3,0.3 -0.4,0.1 C -0.44,0.06 -0.47,0.03 -0.52,0.01 Z';
  const ob = [
    ...tubes(['M 0.08,-0.33 Q 0.1,-0.42 0.17,-0.45'], STEM, 0.045),
    flat(lensD(0.07, -0.34, 0.36, -0.48, 0.085), LEAF),
    ...solid(body, '#FFE03A', { off: [-0.02, -0.05], hl: [[-0.2, -0.13, 0.1, 0.04, -20], [-0.33, 0.0, 0.025, 0.025]] }),
  ];
  return rot(ob, -22);
};

draw.mais = () => {
  const cob = 'M 0,-0.5 C 0.15,-0.5 0.23,-0.3 0.23,-0.1 L 0.23,0.2 L -0.23,0.2 L -0.23,-0.1 C -0.23,-0.3 -0.15,-0.5 0,-0.5 Z';
  const kernels = [];
  for (let r = 0; r < 6; r++) for (let c = 0; c < 3; c++) kernels.push(el(-0.15 + c * 0.15, -0.41 + r * 0.1, 0.055, 0.041, '#FFF08A'));
  const ob = [
    ...solid(cob, '#FFC928', { off: [-0.02, -0.04], extra: [clip(cob, kernels)] }),
    flat(lensD(0, 0.5, -0.3, -0.02, 0.03, 0.13), '#45B649'),
    flat(lensD(0, 0.5, 0.3, -0.02, 0.13, 0.03), '#45B649'),
    flat(lensD(0, 0.5, 0, 0.02, 0.11), '#5FCB5F'),
  ];
  return rot(ob, 22);
};

draw.ananas = () => {
  const body = ellD(0, 0.2, 0.3, 0.36);
  const hatch = [];
  for (let i = -3; i <= 3; i++) {
    hatch.push(ln(i * 0.16 - 0.4, -0.2, i * 0.16 + 0.4, 0.6, tone('#D98A00', { a: 0.75 }), 0.024));
    hatch.push(ln(i * 0.16 + 0.4, -0.2, i * 0.16 - 0.4, 0.6, tone('#D98A00', { a: 0.75 }), 0.024));
  }
  const leaf = (x, y, c) => flat(lensD(0, -0.1, x, y, 0.08), c);
  return [
    leaf(-0.34, -0.32, '#2F9E47'), leaf(0.34, -0.32, '#2F9E47'),
    leaf(-0.2, -0.49, '#3DB554'), leaf(0.2, -0.49, '#3DB554'),
    leaf(0, -0.54, '#55CC63'),
    ...solid(body, '#FFC92E', { off: [-0.025, -0.05], dk: 0.2, extra: [clip(body, hatch)], hl: [[-0.19, 0.12, 0.035, 0.1, 10, 0.5]] }),
  ];
};

draw.canard = () => {
  const bodyD = ellD(-0.03, 0.2, 0.45, 0.27);
  const wing = 'M 0.1,0.16 C 0.0,0.08 -0.2,0.08 -0.34,0.14 C -0.26,0.3 -0.1,0.37 0.02,0.35 C 0.12,0.33 0.14,0.22 0.1,0.16 Z';
  return [
    flat(polyD([[-0.36, 0.2], [-0.5, -0.07], [-0.16, 0.05]], 0.03), YELLOW),
    ...solid(bodyD, YELLOW, { off: [-0.02, -0.05], hl: [[-0.3, 0.06, 0.08, 0.025, -10]] }),
    flat(wing, tone(YELLOW, { d: 0.1 }), ink(0.028)),
    ...solid(ellD(0.17, -0.2, 0.23, 0.22), YELLOW, { off: [-0.02, -0.04], hl: [[0.04, -0.3, 0.05, 0.03, -35]] }),
    flat(ellD(0.43, -0.14, 0.13, 0.065, 8), ORANGE, ink(0.03)),
    ...eye(0.2, -0.25, 0.055, [0.3, 0.1]),
    blush(0.14, -0.1, 0.04, 0.028),
  ];
};

draw.abeille = () => {
  const bodyD = ellD(0, 0.1, 0.38, 0.37);
  const wing = (s) => flat(ellD(s * 0.27, -0.33, 0.13, 0.21, s * 28), tone('#CFEAFF', { a: 0.95 }), ink(0.03));
  return [
    ...[-1, 1].map((s) => path(pb().M(s * 0.1, -0.2).Q(s * 0.12, -0.38, s * 0.22, -0.46).d, 'none', line(INK, 0.034))),
    ...[-1, 1].map((s) => ci(s * 0.22, -0.46, 0.04, INK)),
    wing(-1), wing(1),
    flat(polyD([[-0.07, 0.42], [0.07, 0.42], [0, 0.54]], 0.015), INK),
    ...solid(bodyD, YELLOW, {
      off: [-0.02, -0.05],
      hl: [[-0.24, -0.05, 0.04, 0.08, 25]],
      extra: [clip(bodyD, [rr(-0.5, 0.17, 1, 0.1, 0, INK), rr(-0.5, 0.36, 1, 0.1, 0, INK)])],
    }),
    ...eye(-0.14, -0.06, 0.06, [0.1, 0.2]), ...eye(0.14, -0.06, 0.06, [-0.1, 0.2]),
    smile(0, 0.05, 0.14, 0.04, 0.024),
    blush(-0.26, 0.04, 0.04, 0.028), blush(0.26, 0.04, 0.04, 0.028),
  ];
};

module.exports = draw;
