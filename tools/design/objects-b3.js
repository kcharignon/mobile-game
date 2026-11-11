// Lot 3 des objets de Cherche et Trouve (20 objets) : robot, prune, bonbon, pieuvre, chauve-souris, lavande, pensée, diamant, baguette magique,
// chapeau de magicien, beignet, macaron, barbe à papa, sucette, flamant rose, crevette, fleur, coquillage, cadeau, robe.
// Même style que les 40 premiers (voir objects.js) : plat, contour encre, ombre de bord en bas à droite, petit reflet, via la boîte à outils objkit.js.
'use strict';

const { solid, flat, tubes, eye, dotEye, smile, blush, spot, el, ci, rr, path, ln, clip, ink, line, tone, pb, ellD, rrD, capD, polyD, starD, starPts, lensD, splineD, rot, mirror, RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET, PINK, BROWN, WHITEISH, GREY, LEAF, STEM, INK, WHITE, LW } = require('./objkit.js');

const draw = {};

// ---------------------------------------------------------------- bleu
draw.robot = () => {
  const head = rrD(-0.27, -0.37, 0.54, 0.35, 0.09);
  const body = rrD(-0.22, 0.03, 0.44, 0.33, 0.06);
  const arm = (s) => [
    ...tubes([`M ${s * 0.2},0.1 L ${s * 0.37},0.24`], GREY, 0.07),
    flat(ellD(s * 0.38, 0.26, 0.055, 0.055), YELLOW),
  ];
  return [
    ...arm(-1), ...arm(1),
    ...[-1, 1].map((s) => flat(rrD(s * 0.115 - 0.05, 0.33, 0.1, 0.11, 0.03), GREY)),
    ...[-1, 1].map((s) => flat(rrD(s * 0.135 - 0.085 + (s < 0 ? -0.02 : 0.02), 0.4, 0.17, 0.08, 0.04), '#8E9AB4')),
    ...tubes(['M 0,-0.36 L 0,-0.45'], GREY, 0.04),
    ...[-1, 1].map((s) => flat(rrD(s * 0.285 - 0.03, -0.27, 0.06, 0.15, 0.02), GREY)),
    ...solid(body, BLUE, { off: [-0.02, -0.03], hl: [[-0.15, 0.1, 0.02, 0.05, 0, 0.5]] }),
    flat(rrD(-0.14, 0.09, 0.28, 0.17, 0.04), '#CFE6FF', ink(0.026)),
    ci(-0.07, 0.175, 0.035, RED), ci(0.01, 0.175, 0.035, YELLOW), ci(0.09, 0.175, 0.035, GREEN),
    ...solid(head, BLUE, { off: [-0.02, -0.04], hl: [[-0.19, -0.3, 0.05, 0.025, -20]] }),
    ...eye(-0.1, -0.22, 0.085, [0.1, 0.25]),
    ...eye(0.1, -0.22, 0.085, [-0.1, 0.25]),
    flat(rrD(-0.1, -0.1, 0.2, 0.05, 0.025), WHITE, ink(0.022)),
    flat(ellD(0, -0.47, 0.05, 0.05), RED),
  ];
};

// ---------------------------------------------------------------- violet
draw.prune = () => {
  const body = 'M 0,-0.33 C 0.15,-0.42 0.36,-0.28 0.39,-0.02 C 0.42,0.24 0.26,0.46 0,0.46 C -0.26,0.46 -0.42,0.24 -0.39,-0.02 C -0.36,-0.28 -0.15,-0.42 0,-0.33 Z';
  return [
    ...tubes(['M 0,-0.31 Q 0,-0.4 0.05,-0.47'], STEM, 0.05),
    flat(lensD(0.04, -0.4, 0.31, -0.48, 0.07), LEAF),
    ...solid(body, '#7A47C8', {
      off: [-0.025, -0.05],
      hl: [[-0.22, -0.1, 0.07, 0.16, -10, 0.4], [-0.25, 0.14, 0.024, 0.024]],
      extra: [path(pb().M(0.03, -0.31).C(0.2, -0.12, 0.2, 0.22, 0.05, 0.44).d, 'none', line(tone('#4B2A8A', { a: 0.55 }), 0.03))],
    }),
  ];
};

draw.bonbon = () => {
  const wrap = (s) => {
    const d = pb().M(s * 0.17, -0.06).L(s * 0.42, -0.27).Q(s * 0.52, -0.2, s * 0.46, 0).Q(s * 0.52, 0.2, s * 0.42, 0.27).L(s * 0.17, 0.06).Z().d;
    return [
      ...solid(d, '#B697F2', { off: [s * -0.015, -0.03], dk: 0.14 }),
      ln(s * 0.2, -0.04, s * 0.4, -0.16, tone(WHITE, { a: 0.6 }), 0.025, { nosil: true }),
      ln(s * 0.2, 0.04, s * 0.4, 0.16, tone(WHITE, { a: 0.6 }), 0.025, { nosil: true }),
    ];
  };
  const body = ellD(0, 0, 0.27, 0.22);
  return [
    ...wrap(-1), ...wrap(1),
    ...solid(body, VIOLET, {
      off: [-0.02, -0.04],
      extra: [clip(body, [ln(-0.2, 0.3, 0.05, -0.3, tone(WHITE, { a: 0.55 }), 0.07, { nosil: true }), ln(0.0, 0.3, 0.25, -0.3, tone(WHITE, { a: 0.55 }), 0.07, { nosil: true })])],
      hl: [[-0.15, -0.12, 0.04, 0.025, -30, 0.7]],
    }),
  ];
};

draw.poulpe = () => {
  const head = ellD(0, -0.14, 0.34, 0.34);
  const tent = [
    'M -0.22,0.12 C -0.3,0.3 -0.46,0.34 -0.48,0.2',
    'M -0.08,0.14 C -0.1,0.3 -0.2,0.46 -0.3,0.43',
    'M 0.08,0.14 C 0.1,0.3 0.2,0.46 0.3,0.43',
    'M 0.22,0.12 C 0.3,0.3 0.46,0.34 0.48,0.2',
  ];
  return [
    ...tubes(tent, VIOLET, 0.1),
    ...solid(head, VIOLET, { off: [-0.025, -0.05], hl: [[-0.2, -0.3, 0.06, 0.1, -35], [-0.27, -0.14, 0.024, 0.024]] }),
    ...eye(-0.13, -0.14, 0.09, [0.1, 0.25]),
    ...eye(0.13, -0.14, 0.09, [-0.1, 0.25]),
    smile(0, 0.0, 0.14, 0.04, 0.026),
    blush(-0.26, -0.03, 0.045, 0.03), blush(0.26, -0.03, 0.045, 0.03),
  ];
};

draw.chauvesouris = () => {
  const wingD = pb().M(0.1, -0.1).C(0.2, -0.38, 0.45, -0.42, 0.54, -0.24).Q(0.43, -0.18, 0.46, 0.04).Q(0.35, -0.04, 0.31, 0.22).Q(0.2, 0.02, 0.12, 0.1).Z().d;
  const wing = solid(wingD, '#6E40BC', { off: [-0.015, -0.035], dk: 0.2 });
  const ear = (s) => [
    flat(polyD([[s * 0.2, -0.16], [s * 0.2, -0.46], [s * 0.03, -0.27]], 0.025), VIOLET),
    path(polyD([[s * 0.165, -0.24], [s * 0.17, -0.38], [s * 0.08, -0.28]], 0.015), '#FFB3D1'),
  ];
  return [
    ...wing, ...mirror(wing),
    ...solid(ellD(0, 0.16, 0.13, 0.17), VIOLET, { off: [-0.015, -0.03] }),
    ...ear(-1), ...ear(1),
    ...solid(ellD(0, -0.06, 0.21, 0.19), VIOLET, { off: [-0.02, -0.04], hl: [[-0.1, -0.16, 0.04, 0.025, -30]] }),
    ...eye(-0.08, -0.08, 0.06, [0.1, 0.25]),
    ...eye(0.08, -0.08, 0.06, [-0.1, 0.25]),
    smile(0, 0.02, 0.1, 0.025, 0.022),
  ];
};

draw.lavande = () => {
  // un brin vertical : une tige et un épi festonné (une seule forme), puis on en fait un petit bouquet de trois
  const rows = 6;
  const yy = (i) => 0.06 - 0.085 * i;
  const hwid = (i) => 0.105 - 0.011 * i;
  const spikeD = (() => {
    const b = pb().M(0, 0.1);
    b.L(hwid(0), yy(0));
    for (let i = 0; i < rows - 1; i++) b.Q(hwid(i) + 0.05, (yy(i) + yy(i + 1)) / 2 - 0.01, hwid(i + 1), yy(i + 1));
    b.Q(hwid(rows - 1) + 0.03, yy(rows - 1) - 0.06, 0, yy(rows - 1) - 0.1);
    b.Q(-hwid(rows - 1) - 0.03, yy(rows - 1) - 0.06, -hwid(rows - 1), yy(rows - 1));
    for (let i = rows - 2; i >= 0; i--) b.Q(-hwid(i) - 0.05, (yy(i) + yy(i + 1)) / 2 - 0.01, -hwid(i), yy(i));
    return b.L(0, 0.1).Z().d;
  })();
  const chevrons = pb();
  for (let i = 0; i < rows; i++) chevrons.M(-hwid(i) - 0.02, yy(i) + 0.01).L(0, yy(i) - 0.045).L(hwid(i) + 0.02, yy(i) + 0.01);
  const sprig = () => [
    ...tubes(['M 0,0.5 L 0,0.0'], '#4C9A3A', 0.05),
    ...solid(spikeD, VIOLET, { off: [-0.015, -0.03], extra: [clip(spikeD, [path(chevrons.d, 'none', line(tone('#4B2A8A', { a: 0.7 }), 0.022))])], hl: [[-0.045, -0.12, 0.014, 0.05, 0, 0.5]] }),
  ];
  return [
    ...rot(sprig(), -30, 0, 0.5),
    ...rot(sprig(), 30, 0, 0.5),
    ...sprig(),
    flat(rrD(-0.09, 0.3, 0.18, 0.07, 0.025), '#E3A85A', ink(0.026)),
  ];
};

draw.pensee = () => {
  const up = (s) => solid(ellD(s * 0.17, -0.17, 0.2, 0.23, s * 18), '#6A3DB8', { off: [-0.015, -0.035], hl: [[s * 0.2 - 0.04, -0.27, 0.035, 0.07, s * 18]] });
  const side = (s) => solid(ellD(s * 0.29, 0.08, 0.21, 0.18, s * -15), VIOLET, { off: [-0.015, -0.035], hl: [[s * 0.36, 0.0, 0.03, 0.05, s * -30]] });
  const whisk = (x1, y1, x2, y2) => ln(x1, y1, x2, y2, tone('#3E2275'), 0.024, { nosil: true });
  return [
    ...up(-1), ...up(1),
    ...side(-1), ...side(1),
    ...solid(ellD(0, 0.2, 0.27, 0.22), '#B08DF0', { off: [-0.02, -0.04], hl: [[-0.15, 0.22, 0.04, 0.06, 20]] }),
    whisk(-0.04, 0.12, -0.1, 0.3), whisk(0.0, 0.14, 0.0, 0.33), whisk(0.04, 0.12, 0.1, 0.3),
    flat(ellD(0, 0.0, 0.11, 0.11), YELLOW, ink(0.028)),
    ci(0, 0.0, 0.035, ORANGE),
  ];
};

draw.diamant = () => {
  const T1 = [-0.23, -0.34]; const T2 = [0.23, -0.34]; const R = [0.5, -0.08]; const L = [-0.5, -0.08];
  const B = [0, 0.44]; const GL = [-0.19, -0.08]; const GR = [0.19, -0.08];
  const facet = (pts, c) => path(polyD(pts, 0.012), c, ink(0.024));
  const outer = polyD([T1, T2, R, B, L], 0.04);
  return [
    path(outer, '#6A3DB3', { sil: true }),
    facet([L, T1, GL], '#A27BEA'),
    facet([T1, T2, GR, GL], '#CDB1FA'),
    facet([T2, R, GR], '#A27BEA'),
    facet([L, GL, B], '#7B4DC7'),
    facet([GL, GR, B], '#9467DD'),
    facet([GR, R, B], '#6A3DB3'),
    path(outer, 'none', ink()),
    path(polyD([[-0.12, -0.28], [-0.04, -0.28], [-0.1, -0.16], [-0.17, -0.16]], 0.01), tone(WHITE, { a: 0.55 })),
  ];
};

draw.baguette = () => {
  const shaft = capD(0, -0.1, 0, 0.46, 0.06);
  const ob = [
    ...solid(shaft, '#7447C4', {
      off: [-0.015, -0.02],
      extra: [clip(shaft, [rr(-0.1, 0.33, 0.2, 0.2, 0, WHITE), rr(-0.1, -0.05, 0.2, 0.07, 0, '#C9A9F7')])],
    }),
    ...solid(starD(0, -0.3, 5, 0.24, 0.115, -90, 0.04), '#C9A3FF', { off: [-0.02, -0.035], hl: [[-0.08, -0.36, 0.03, 0.05, 30, 0.7]] }),
    flat(starD(0.3, -0.47, 4, 0.07, 0.025, -90, 0.0), YELLOW, ink(0.02)),
    flat(starD(-0.3, -0.12, 4, 0.06, 0.02, -90, 0.0), YELLOW, ink(0.02)),
  ];
  return rot(ob, 35);
};

draw.chapeaumagique = () => {
  const cone = pb().M(-0.31, 0.3).C(-0.26, 0.0, -0.1, -0.3, 0.12, -0.48).C(0.2, -0.2, 0.28, 0.05, 0.31, 0.3).Q(0, 0.4, -0.31, 0.3).Z().d;
  const band = 'M -0.4,0.17 Q 0,0.28 0.4,0.17 L 0.4,0.5 L -0.4,0.5 Z';
  const star = (x, y, r) => flat(starD(x, y, 5, r, r * 0.48, -90, r * 0.12), YELLOW, ink(0.022));
  return [
    ...solid(ellD(0, 0.3, 0.5, 0.14), '#6F40BD', { off: [-0.02, -0.04], dk: 0.2 }),
    ...solid(cone, VIOLET, {
      off: [-0.02, -0.03],
      extra: [clip(cone, [path(band, YELLOW), path('M -0.4,0.17 Q 0,0.28 0.4,0.17', 'none', line(INK, 0.022))])],
    }),
    clip(cone, [star(-0.03, -0.02, 0.11), star(0.07, -0.29, 0.065)]),
  ];
};

// ---------------------------------------------------------------- rose
/** Anneau (disque percé) : le trou est un sous-chemin dessiné à l'envers. */
const ringD = (cx, cy, R, r) => ellD(cx, cy, R, R) + ' ' + pb().arc(cx, cy, r, r, 360, 0).Z().d;

draw.donut = () => {
  const dough = ringD(0, 0, 0.47, 0.15);
  const wavy = [];
  for (let k = 0; k < 36; k++) {
    const a = (k * 10 * Math.PI) / 180;
    const r = 0.4 + 0.02 * Math.cos(7 * a);
    wavy.push([r * Math.cos(a), r * Math.sin(a)]);
  }
  const icing = splineD(wavy) + ' ' + pb().arc(0, 0, 0.2, 0.2, 360, 0).Z().d;
  const sprinkle = (r, a, t, c) => {
    const x = r * Math.cos((a * Math.PI) / 180);
    const y = r * Math.sin((a * Math.PI) / 180);
    const u = (t * Math.PI) / 180;
    return path(capD(x - 0.045 * Math.cos(u), y - 0.045 * Math.sin(u), x + 0.045 * Math.cos(u), y + 0.045 * Math.sin(u), 0.02), c);
  };
  return [
    ...solid(dough, '#E9B666', { off: [-0.02, -0.04], dk: 0.2 }),
    ...solid(icing, PINK, {
      off: [-0.015, -0.03],
      hl: [[-0.24, -0.22, 0.05, 0.09, -45]],
      extra: [
        sprinkle(0.3, -20, 30, WHITE), sprinkle(0.32, 30, -50, YELLOW), sprinkle(0.28, 80, 20, '#4FC3F7'), sprinkle(0.31, 125, -30, WHITE),
        sprinkle(0.3, 175, 60, '#7B4FD0'), sprinkle(0.3, 225, 10, YELLOW), sprinkle(0.3, 270, -40, '#4FC3F7'), sprinkle(0.3, 320, 70, '#7B4FD0'),
        sprinkle(0.25, 0, 90, YELLOW),
      ],
    }),
  ];
};

draw.macaron = () => {
  const bump = (x0, x1, y, n, h) => {
    const b = pb();
    const w = (x1 - x0) / n;
    for (let i = 0; i < n; i++) b.Q(x0 + w * (i + 0.5), y + h, x0 + w * (i + 1), y);
    return b;
  };
  // coque du haut : un dôme dont le bord du bas est froncé (les « pieds »)
  const top = (() => {
    const b = pb().M(-0.44, -0.02).C(-0.44, -0.26, -0.26, -0.38, 0, -0.38).C(0.26, -0.38, 0.44, -0.26, 0.44, -0.02);
    const n = 7;
    const w = 0.88 / n;
    for (let i = 0; i < n; i++) b.Q(0.44 - w * (i + 0.5), 0.06, 0.44 - w * (i + 1), -0.02);
    return b.Z().d;
  })();
  const bottom = (() => {
    const b = pb().M(-0.44, 0.2);
    const n = 7;
    const w = 0.88 / n;
    for (let i = 0; i < n; i++) b.Q(-0.44 + w * (i + 0.5), 0.12, -0.44 + w * (i + 1), 0.2);
    return b.C(0.44, 0.38, 0.28, 0.45, 0, 0.45).C(-0.28, 0.45, -0.44, 0.38, -0.44, 0.2).Z().d;
  })();
  return [
    flat(rrD(-0.41, -0.01, 0.82, 0.21, 0.1), '#FFF1DC'),
    ...solid(bottom, PINK, { off: [-0.02, -0.03], extra: [clip(bottom, [rr(-0.5, 0.0, 1, 0.3, 0, '#FF6FA3')])] }),
    ...solid(top, PINK, { off: [-0.02, -0.04], extra: [clip(top, [rr(-0.5, -0.1, 1, 0.2, 0, '#FF6FA3')])], hl: [[-0.22, -0.24, 0.07, 0.04, -30, 0.55], [-0.32, -0.12, 0.02, 0.02]] }),
  ];
};

draw.barbapapa = () => {
  // un nuage de N bosses rondes : des cercles de rayon rb centrés sur un cercle de rayon Rc, reliés par leurs points d'intersection extérieurs
  const N = 9; const Rc = 0.285; const rb = 0.155; const cy = -0.1;
  const C = (k) => [Rc * Math.cos(((k * 360) / N - 90) * (Math.PI / 180)), cy + Rc * Math.sin(((k * 360) / N - 90) * (Math.PI / 180))];
  const X = (k) => { // intersection extérieure des bosses k et k+1
    const a = C(k); const b = C(k + 1);
    const mx = (a[0] + b[0]) / 2; const my = (a[1] + b[1]) / 2;
    const dx = b[0] - a[0]; const dy = b[1] - a[1];
    const d = Math.hypot(dx, dy);
    const h = Math.sqrt(rb * rb - (d / 2) ** 2);
    const n1 = [-dy / d, dx / d];
    const cand = [[mx + n1[0] * h, my + n1[1] * h], [mx - n1[0] * h, my - n1[1] * h]];
    return Math.hypot(cand[0][0], cand[0][1] - cy) > Math.hypot(cand[1][0], cand[1][1] - cy) ? cand[0] : cand[1];
  };
  const bb = pb();
  for (let k = 0; k < N; k++) {
    const c = C(k);
    const s0 = X((k + N - 1) % N); const e0 = X(k);
    const a0 = (Math.atan2(s0[1] - c[1], s0[0] - c[0]) * 180) / Math.PI;
    let a1 = (Math.atan2(e0[1] - c[1], e0[0] - c[0]) * 180) / Math.PI;
    while (a1 < a0) a1 += 360;
    bb.arc(c[0], c[1], rb, rb, a0, a1);
  }
  const cloud = bb.Z().d;
  const swirl = (d) => path(d, 'none', line(tone(WHITE, { a: 0.55 }), 0.03));
  return [
    ...tubes(['M 0,0.2 L 0,0.46'], '#F3DFB6', 0.075),
    ...solid(cloud, PINK, {
      off: [-0.02, -0.04],
      extra: [
        clip(cloud, [swirl(pb().M(-0.3, -0.1).Q(-0.18, -0.3, 0.02, -0.2).Q(0.12, -0.14, 0.04, -0.06).d), swirl(pb().M(-0.2, 0.08).Q(0.0, 0.18, 0.2, 0.04).Q(0.28, -0.04, 0.22, -0.15).d)]),
      ],
      hl: [[-0.26, -0.28, 0.06, 0.04, -35, 0.6]],
    }),
  ];
};

draw.sucette = () => {
  const disc = ellD(0, -0.12, 0.35, 0.35);
  const sp = pb();
  const turns = 2.6;
  const steps = Math.round(turns * 24);
  for (let i = 0; i <= steps; i++) {
    const t = (i / steps) * turns * 2 * Math.PI;
    const r = 0.025 + (t / (2 * Math.PI)) * 0.105;
    const x = r * Math.cos(t);
    const y = -0.12 + r * Math.sin(t);
    if (i === 0) sp.M(x, y);
    else sp.L(x, y);
  }
  const ob = [
    ...tubes(['M 0,0.2 L 0,0.47'], '#F3DFB6', 0.07),
    ...solid(disc, PINK, { off: [-0.02, -0.04], extra: [clip(disc, [path(sp.d, 'none', line(WHITE, 0.055))])], hl: [[-0.22, -0.28, 0.04, 0.025, -40, 0.7]] }),
  ];
  return rot(ob, 25, 0, 0.1);
};

draw.flamant = () => {
  const DK = '#F2639B';
  const body = 'M -0.47,-0.13 C -0.35,-0.09 -0.27,-0.21 -0.06,-0.21 C 0.18,-0.21 0.3,-0.08 0.28,0.06 C 0.26,0.2 0.1,0.25 -0.08,0.24 C -0.26,0.23 -0.38,0.07 -0.47,-0.13 Z';
  const beak = 'M 0.24,-0.47 C 0.36,-0.48 0.44,-0.41 0.43,-0.32 C 0.37,-0.37 0.3,-0.37 0.25,-0.36 Z';
  return [
    ...tubes(['M -0.02,0.2 L -0.02,0.5'], DK, 0.055),
    flat(capD(-0.06, 0.52, 0.08, 0.52, 0.03), DK),
    ...tubes(['M 0.13,-0.04 C 0.36,-0.1 0.32,-0.28 0.12,-0.28 C -0.04,-0.28 0.0,-0.42 0.18,-0.42'], PINK, 0.075),
    ...solid(body, PINK, { off: [-0.02, -0.04], hl: [[-0.1, -0.13, 0.08, 0.025, -10]] }),
    path(lensD(-0.3, -0.02, 0.12, 0.12, 0.065, 0.03), DK),
    flat(ellD(0.2, -0.42, 0.078, 0.078), PINK),
    flat(beak, '#FFE3EE'),
    clip(beak, [rr(0.36, -0.5, 0.2, 0.2, 0, INK)]),
    ...dotEye(0.2, -0.44, 0.03),
  ];
};

draw.crevette = () => {
  // une crevette recroquevillée en C : le corps suit un arc (de la tête, en haut à droite, vers la queue, en bas à gauche), plus épais côté tête
  const DK = '#F2639B';
  const R = 0.3; const cx = 0.0; const cy = -0.02;
  const ang = (t) => ((-20 - 230 * t) * Math.PI) / 180;
  const hw = (t) => 0.04 + 0.12 * Math.pow(1 - t, 0.8);
  const at = (t, off) => [cx + (R + off) * Math.cos(ang(t)), cy + (R + off) * Math.sin(ang(t))];
  const N = 10;
  const pts = [];
  for (let i = 0; i <= N; i++) pts.push(at(i / N, hw(i / N)));
  for (let i = N; i >= 0; i--) pts.push(at(i / N, -hw(i / N)));
  const body = splineD(pts);
  const T = at(1, 0);
  const fwd = Math.atan2(-Math.cos(ang(1)), Math.sin(ang(1)));
  const flap = (dth, L, w) => flat(lensD(T[0], T[1], T[0] + L * Math.cos(fwd + (dth * Math.PI) / 180), T[1] + L * Math.sin(fwd + (dth * Math.PI) / 180), w), '#FF9EC5');
  const seg = [0.2, 0.36, 0.52, 0.68, 0.84].map((t) => {
    const a = at(t, -hw(t) * 1.3); const b = at(t, hw(t) * 1.3);
    return ln(a[0], a[1], b[0], b[1], tone('#D9487F', { a: 0.75 }), 0.024, { nosil: true });
  });
  const leg = (t) => {
    const a = at(t, -hw(t) + 0.03); const b = at(t, -hw(t) - 0.1);
    return `M ${a[0]},${a[1]} L ${b[0]},${b[1]}`;
  };
  const E = at(0.1, 0.06);
  const A1 = at(0.02, 0.08);
  return [
    ...tubes([leg(0.22), leg(0.36), leg(0.5)], DK, 0.035),
    ...tubes([pb().M(A1[0], A1[1]).Q(0.5, -0.3, 0.33, -0.5).d, pb().M(A1[0], A1[1]).Q(0.72, -0.08, 0.64, -0.3).d], DK, 0.03),
    flap(-40, 0.2, 0.06), flap(40, 0.2, 0.06), flap(0, 0.26, 0.06),
    ...solid(body, PINK, { off: [-0.02, -0.04], extra: [clip(body, seg)], hl: [[-0.1, -0.3, 0.1, 0.025, 10, 0.6]] }),
    ...eye(E[0], E[1], 0.05, [0.3, 0.2]),
  ];
};

draw.fleur = () => {
  const cy = -0.17;
  const petal = (k) => {
    const a = ((-90 + 72 * k) * Math.PI) / 180;
    return solid(ellD(0.2 * Math.cos(a), cy + 0.2 * Math.sin(a), 0.14, 0.19, 72 * k), PINK, { off: [-0.015, -0.03], hl: k === 0 ? [[-0.05, cy - 0.28, 0.02, 0.05, 0, 0.6]] : [] });
  };
  return [
    ...tubes(['M 0,-0.1 Q 0.02,0.2 0,0.48'], '#4C9A3A', 0.055),
    flat(lensD(0.02, 0.3, 0.34, 0.12, 0.08), LEAF),
    flat(lensD(0.0, 0.38, -0.28, 0.24, 0.07), tone(LEAF, { l: 0.1 })),
    ...[0, 1, 2, 3, 4].flatMap(petal),
    ...solid(ellD(0, cy, 0.125, 0.125), YELLOW, { off: [-0.015, -0.03], hl: [[-0.045, cy - 0.04, 0.025, 0.025]] }),
  ];
};

draw.coquillage = () => {
  const r = 0.43; const cy = -0.03; const hinge = [0, 0.43];
  const n = 6;
  const a0 = -170; const a1 = -10;
  const da = (a1 - a0) / n;
  const P = (a, rr_ = r) => [rr_ * Math.cos((a * Math.PI) / 180), cy + rr_ * Math.sin((a * Math.PI) / 180)];
  const valleys = [];
  for (let i = 0; i <= n; i++) valleys.push(P(a0 + da * i));
  const b = pb().M(hinge[0], hinge[1]).L(valleys[0][0], valleys[0][1]);
  for (let i = 0; i < n; i++) {
    const m = P(a0 + da * (i + 0.5), (r + 0.045) / Math.cos((da / 2 * Math.PI) / 180));
    b.Q(m[0], m[1], valleys[i + 1][0], valleys[i + 1][1]);
  }
  const shell = b.Z().d;
  const wedge = (i) => path(polyD([hinge, valleys[i], P(a0 + da * (i + 0.5), r + 0.12), valleys[i + 1]], 0), '#FFAFCF');
  const rib = (i) => ln(hinge[0], hinge[1], valleys[i][0], valleys[i][1], tone('#D9487F', { a: 0.8 }), 0.025, { nosil: true });
  return [
    ...solid(shell, PINK, {
      off: [-0.02, -0.03],
      extra: [clip(shell, [wedge(1), wedge(3), wedge(5), ...[0, 1, 2, 3, 4, 5, 6].map(rib)])],
      hl: [[-0.2, -0.2, 0.05, 0.08, -30, 0.5]],
    }),
    flat(polyD([[-0.19, 0.33], [0.19, 0.33], [0.15, 0.47], [-0.15, 0.47]], 0.035), '#F2639B'),
  ];
};

draw.cadeau = () => {
  const lid = rrD(-0.47, -0.2, 0.94, 0.24, 0.04);
  const box = rrD(-0.4, 0.03, 0.8, 0.44, 0.04);
  const loop = (s) => path(pb().M(0, -0.2).C(s * 0.1, -0.44, s * 0.38, -0.52, s * 0.37, -0.34).C(s * 0.36, -0.2, s * 0.14, -0.2, 0, -0.2).Z().d, YELLOW, ink(0.03));
  return [
    ...solid(box, PINK, { off: [-0.02, -0.03], hl: [[-0.3, 0.18, 0.025, 0.1, 0, 0.45]] }),
    flat(rrD(-0.08, 0.03, 0.16, 0.44, 0.0), YELLOW, ink(0.03)),
    ...solid(lid, '#FF6FA3', { off: [-0.02, -0.035], hl: [[-0.34, -0.12, 0.08, 0.025, 0, 0.5]] }),
    flat(rrD(-0.08, -0.2, 0.16, 0.24, 0.0), YELLOW, ink(0.03)),
    loop(-1), loop(1),
    flat(ellD(0, -0.2, 0.075, 0.065), YELLOW, ink(0.03)),
  ];
};

draw.robe = () => {
  const n = 5;
  const b = pb().M(-0.13, -0.48).Q(0, -0.36, 0.13, -0.48).L(0.32, -0.4).L(0.35, -0.25).L(0.17, -0.21).L(0.15, -0.04).L(0.47, 0.38);
  const w = 0.94 / n;
  for (let i = 0; i < n; i++) b.Q(0.47 - w * (i + 0.5), 0.5, 0.47 - w * (i + 1), 0.38);
  b.L(-0.15, -0.04).L(-0.17, -0.21).L(-0.35, -0.25).L(-0.32, -0.4).Z();
  const dress = b.d;
  const dot = (x, y) => ci(x, y, 0.04, WHITE);
  return [
    ...solid(dress, PINK, {
      off: [-0.02, -0.03],
      extra: [clip(dress, [dot(-0.22, 0.2), dot(0.0, 0.14), dot(0.22, 0.22), dot(-0.1, 0.34), dot(0.14, 0.38), dot(-0.34, 0.36)])],
      hl: [[-0.08, -0.34, 0.025, 0.04, 20, 0.6]],
    }),
    flat(rrD(-0.18, -0.09, 0.36, 0.1, 0.03), '#E8508E', ink(0.028)),
    flat(ellD(0, -0.04, 0.05, 0.05), '#FFD3E4', ink(0.024)),
  ];
};

module.exports = draw;
