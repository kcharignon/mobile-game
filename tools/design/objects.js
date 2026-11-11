// Les 40 objets de Cherche et Trouve : UNE description (ce fichier) qui produit l'aperçu SVG (docs/design/objets.svg) ET le code Kotlin
// (android/.../games/find/ui/FindArt.kt, généré), exactement comme vehicles.js le fait pour les véhicules.
// La liste des objets (identifiant, nom, couleur, sorte) vient de tools/find/catalog.js : on ne la recopie jamais ici.
//
// Chaque objet est dessiné de face ou de côté, dans le carré [-0.5, 0.5] x [-0.5, 0.5] (l'unité = le côté du carré, y vers le bas),
// puis mis à l'échelle et recentré automatiquement (`fit`) pour occuper ~90 % du carré, contour compris.
// Une description = une liste d'opérations dessinées dans l'ordre : `rr`, `el`, `ci`, `path` (M/L/Q/C/Z), `ln`, `clip`.
// Couleurs : une chaîne '#RRGGBB', ou une teinte `{c:'#RRGGBB', l:0.2}` éclaircie, `{d:0.3}` assombrie, `{a:0.5}` transparence.
// Dégradé : `{grad:{from:[x,y], to:[x,y], stops:[[0,F],[1,F]]}}` (rien n'en utilise pour l'instant : le style est plat).
// Contour : `{stroke:'#RRGGBB', sw:0.036}` (épaisseur en unités). Silhouette (aplat d'une seule couleur) : toute opération qui a un contour,
// ou l'option `sil: true` ; les autres (reflets, ombres, détails sans contour) n'en font pas partie.
'use strict';

const { OBJECTS } = require('../find/catalog.js');

const INK = '#2B2D42'; // = Pj.Ink
const LW = 0.036; // épaisseur du contour : 3,6 % de la taille
const WHITE = '#FFFFFF';
const BLACK = '#000000';
const FIT = 0.9; // part du carré occupée par l'objet (contour compris)

const tone = (c, o = {}) => ({ c, ...o });
const shine = (a = 0.55) => tone(WHITE, { a });

// ---- primitives
const rr = (x, y, w, h, r, fill, o = {}) => ({ t: 'rr', x, y, w, h, r, fill, ...o });
const el = (cx, cy, rx, ry, fill, o = {}) => ({ t: 'el', cx, cy, rx, ry, fill, ...o });
const ci = (cx, cy, r, fill, o = {}) => ({ t: 'ci', cx, cy, r, fill, ...o });
const path = (d, fill, o = {}) => ({ t: 'path', d, fill, ...o });
const ln = (x1, y1, x2, y2, stroke, w, o = {}) => ({ t: 'ln', x1, y1, x2, y2, stroke, w, ...o });
const clip = (d, ops) => ({ t: 'clip', d, ops });
const line = (c, w) => ({ stroke: c, sw: w });
const ink = (w = LW) => line(INK, w);

// ================================================================ géométrie : fabrication de chemins (M/L/Q/C/Z uniquement)
const r4 = (n) => {
  const v = Math.round(n * 10000) / 10000;
  return v === 0 ? 0 : v;
};

/** Un constructeur de chemin ; `arc` ajoute des arcs d'ellipse en courbes de Bézier cubiques (angles en degrés, sens horaire à l'écran). */
function pb() {
  const parts = [];
  let cx = null;
  let cy = null;
  const api = {
    M(x, y) { parts.push(`M ${r4(x)},${r4(y)}`); cx = x; cy = y; return api; },
    L(x, y) { parts.push(`L ${r4(x)},${r4(y)}`); cx = x; cy = y; return api; },
    Q(x1, y1, x, y) { parts.push(`Q ${r4(x1)},${r4(y1)} ${r4(x)},${r4(y)}`); cx = x; cy = y; return api; },
    C(x1, y1, x2, y2, x, y) { parts.push(`C ${r4(x1)},${r4(y1)} ${r4(x2)},${r4(y2)} ${r4(x)},${r4(y)}`); cx = x; cy = y; return api; },
    Z() { parts.push('Z'); return api; },
    arc(ax, ay, rx, ry, a0, a1, rot = 0) {
      const rad = Math.PI / 180;
      const n = Math.max(1, Math.ceil(Math.abs(a1 - a0) / 90 - 1e-9));
      const da = ((a1 - a0) / n) * rad;
      const k = (4 / 3) * Math.tan(da / 4);
      const cr = Math.cos(rot * rad);
      const sr = Math.sin(rot * rad);
      const P = (ux, uy) => {
        const X = ux * rx;
        const Y = uy * ry;
        return [ax + X * cr - Y * sr, ay + X * sr + Y * cr];
      };
      let t0 = a0 * rad;
      const s = P(Math.cos(t0), Math.sin(t0));
      if (cx === null) api.M(s[0], s[1]);
      else if (Math.hypot(cx - s[0], cy - s[1]) > 1e-6) api.L(s[0], s[1]);
      for (let i = 0; i < n; i++) {
        const t1 = t0 + da;
        const p1 = P(Math.cos(t0) - k * Math.sin(t0), Math.sin(t0) + k * Math.cos(t0));
        const p2 = P(Math.cos(t1) + k * Math.sin(t1), Math.sin(t1) - k * Math.cos(t1));
        const p3 = P(Math.cos(t1), Math.sin(t1));
        api.C(p1[0], p1[1], p2[0], p2[1], p3[0], p3[1]);
        t0 = t1;
      }
      return api;
    },
    get d() { return parts.join(' '); },
  };
  return api;
}

/** Ellipse (éventuellement penchée de `rot` degrés) en chemin fermé. */
const ellD = (cx, cy, rx, ry, rot = 0) => pb().arc(cx, cy, rx, ry, 0, 360, rot).Z().d;
/** Rectangle à coins arrondis en chemin fermé. */
const rrD = (x, y, w, h, r) => {
  r = Math.min(r, w / 2, h / 2);
  return pb().M(x + r, y).L(x + w - r, y).arc(x + w - r, y + r, r, r, -90, 0).L(x + w, y + h - r).arc(x + w - r, y + h - r, r, r, 0, 90)
    .L(x + r, y + h).arc(x + r, y + h - r, r, r, 90, 180).L(x, y + r).arc(x + r, y + r, r, r, 180, 270).Z().d;
};
/** Gélule (stade) entre deux points, de rayon r. */
const capD = (x1, y1, x2, y2, r) => {
  const a = (Math.atan2(y2 - y1, x2 - x1) * 180) / Math.PI;
  return pb().arc(x2, y2, r, r, a - 90, a + 90).arc(x1, y1, r, r, a + 90, a + 270).Z().d;
};
/** Polygone dont les sommets sont arrondis (rayon r, ou un rayon par sommet). */
const polyD = (pts, r = 0) => {
  const b = pb();
  const n = pts.length;
  for (let i = 0; i < n; i++) {
    const [px, py] = pts[(i + n - 1) % n];
    const [x, y] = pts[i];
    const [nx, ny] = pts[(i + 1) % n];
    const ri = Array.isArray(r) ? r[i] : r;
    const l1 = Math.hypot(px - x, py - y);
    const l2 = Math.hypot(nx - x, ny - y);
    const d = Math.min(ri, l1 / 2, l2 / 2);
    const a = [x + ((px - x) / l1) * d, y + ((py - y) / l1) * d];
    const c = [x + ((nx - x) / l2) * d, y + ((ny - y) / l2) * d];
    if (i === 0) b.M(a[0], a[1]);
    else b.L(a[0], a[1]);
    b.Q(x, y, c[0], c[1]);
  }
  return b.Z().d;
};
/** Étoile régulière à n branches (rayons ro / ri), sommets arrondis. */
const starPts = (cx, cy, n, ro, ri, rot = -90) => {
  const pts = [];
  for (let i = 0; i < n * 2; i++) {
    const a = ((rot + (i * 180) / n) * Math.PI) / 180;
    const r = i % 2 === 0 ? ro : ri;
    pts.push([cx + r * Math.cos(a), cy + r * Math.sin(a)]);
  }
  return pts;
};
const starD = (cx, cy, n, ro, ri, rot = -90, rc = 0) => polyD(starPts(cx, cy, n, ro, ri, rot), rc);
/** Feuille : lentille pointue entre deux points, demi-largeur w1 d'un côté, w2 de l'autre. */
const lensD = (x1, y1, x2, y2, w1, w2 = w1) => {
  const mx = (x1 + x2) / 2;
  const my = (y1 + y2) / 2;
  const L = Math.hypot(x2 - x1, y2 - y1);
  const nx = -(y2 - y1) / L;
  const ny = (x2 - x1) / L;
  return pb().M(x1, y1).Q(mx + nx * 2 * w1, my + ny * 2 * w1, x2, y2).Q(mx - nx * 2 * w2, my - ny * 2 * w2, x1, y1).Z().d;
};
/** Courbe fermée lisse (Catmull-Rom) passant par les points. */
const splineD = (pts) => {
  const n = pts.length;
  const b = pb().M(pts[0][0], pts[0][1]);
  for (let i = 0; i < n; i++) {
    const p0 = pts[(i + n - 1) % n];
    const p1 = pts[i];
    const p2 = pts[(i + 1) % n];
    const p3 = pts[(i + 2) % n];
    b.C(p1[0] + (p2[0] - p0[0]) / 6, p1[1] + (p2[1] - p0[1]) / 6, p2[0] - (p3[0] - p1[0]) / 6, p2[1] - (p3[1] - p1[1]) / 6, p2[0], p2[1]);
  }
  return b.Z().d;
};

/** Applique une fonction de point (x, y) -> [x, y] à tous les points d'un chemin. */
function mapD(d, f) {
  const t = d.trim().split(/[\s,]+/);
  const out = [];
  const counts = { M: 1, L: 1, Q: 2, C: 3 };
  let i = 0;
  while (i < t.length) {
    const c = t[i++];
    if (c === 'Z') { out.push('Z'); continue; }
    if (!counts[c]) throw new Error(`commande de chemin non gérée : ${c}`);
    const pts = [];
    for (let k = 0; k < counts[c]; k++) {
      const [x, y] = f(parseFloat(t[i]), parseFloat(t[i + 1]));
      i += 2;
      pts.push(`${r4(x)},${r4(y)}`);
    }
    out.push(`${c} ${pts.join(' ')}`);
  }
  return out.join(' ');
}
const shiftD = (d, dx, dy) => mapD(d, (x, y) => [x + dx, y + dy]);

// ================================================================ transformations (rotation, déplacement, mise à l'échelle) et cadrage
const opToD = (op) => {
  if (op.t === 'rr') return rrD(op.x, op.y, op.w, op.h, op.r);
  if (op.t === 'el') return ellD(op.cx, op.cy, op.rx, op.ry);
  if (op.t === 'ci') return ellD(op.cx, op.cy, op.r, op.r);
  throw new Error(`pas de chemin pour ${op.t}`);
};

/** p' = R(rot) (p - o) * s + o + d ; les épaisseurs de trait ne changent pas. Les formes restent natives si rien ne tourne. */
function xf(ops, { rot = 0, dx = 0, dy = 0, s = 1, ox = 0, oy = 0 } = {}) {
  const cr = Math.cos((rot * Math.PI) / 180);
  const sr = Math.sin((rot * Math.PI) / 180);
  const f = (x, y) => {
    const X = x - ox;
    const Y = y - oy;
    return [(X * cr - Y * sr) * s + ox + dx, (X * sr + Y * cr) * s + oy + dy];
  };
  const fill = (F) => {
    if (F && F.grad) {
      const g = F.grad;
      return { grad: { from: f(...g.from), to: f(...g.to), stops: g.stops } };
    }
    return F;
  };
  const one = (op) => {
    switch (op.t) {
      case 'clip': return { ...op, d: mapD(op.d, f), ops: op.ops.map(one) };
      case 'ln': { const [x1, y1] = f(op.x1, op.y1); const [x2, y2] = f(op.x2, op.y2); return { ...op, x1, y1, x2, y2, stroke: fill(op.stroke) }; }
      case 'path': return { ...op, d: mapD(op.d, f), fill: fill(op.fill), stroke: fill(op.stroke) };
      default: break;
    }
    if (rot !== 0) return { ...op, t: 'path', d: mapD(opToD(op), f), fill: fill(op.fill), stroke: fill(op.stroke) };
    if (op.t === 'rr') { const [x, y] = f(op.x, op.y); return { ...op, x, y, w: op.w * s, h: op.h * s, r: op.r * s, fill: fill(op.fill) }; }
    if (op.t === 'el') { const [cx, cy] = f(op.cx, op.cy); return { ...op, cx, cy, rx: op.rx * s, ry: op.ry * s, fill: fill(op.fill) }; }
    const [cx, cy] = f(op.cx, op.cy);
    return { ...op, cx, cy, r: op.r * s, fill: fill(op.fill) };
  };
  return ops.map(one);
}
const rot = (ops, deg, ox = 0, oy = 0) => xf(ops, { rot: deg, ox, oy });
const mirror = (ops) => {
  // symétrie gauche-droite : on passe par des chemins (un rr ou une ellipse symétriques restent identiques à eux-mêmes)
  const f = (x, y) => [-x, y];
  const fill = (F) => (F && F.grad ? { grad: { from: f(...F.grad.from), to: f(...F.grad.to), stops: F.grad.stops } } : F);
  const one = (op) => {
    switch (op.t) {
      case 'clip': return { ...op, d: mapD(op.d, f), ops: op.ops.map(one) };
      case 'ln': return { ...op, x1: -op.x1, x2: -op.x2, stroke: fill(op.stroke) };
      case 'path': return { ...op, d: mapD(op.d, f), fill: fill(op.fill), stroke: fill(op.stroke) };
      case 'rr': return { ...op, x: -op.x - op.w, fill: fill(op.fill) };
      default: return { ...op, cx: -op.cx, fill: fill(op.fill) };
    }
  };
  return ops.map(one);
};

/** Rectangle englobant (contour compris) : les courbes sont échantillonnées. */
function bbox(ops) {
  let x0 = Infinity; let y0 = Infinity; let x1 = -Infinity; let y1 = -Infinity;
  const add = (x, y, h = 0) => { x0 = Math.min(x0, x - h); y0 = Math.min(y0, y - h); x1 = Math.max(x1, x + h); y1 = Math.max(y1, y + h); };
  const pathPts = (d, h) => {
    const t = d.trim().split(/[\s,]+/);
    let i = 0; let px = 0; let py = 0;
    const num = () => parseFloat(t[i++]);
    while (i < t.length) {
      const c = t[i++];
      if (c === 'M' || c === 'L') { px = num(); py = num(); add(px, py, h); } else if (c === 'Q') {
        const ax = num(); const ay = num(); const bx = num(); const by = num();
        for (let k = 1; k <= 12; k++) { const u = k / 12; add((1 - u) * (1 - u) * px + 2 * (1 - u) * u * ax + u * u * bx, (1 - u) * (1 - u) * py + 2 * (1 - u) * u * ay + u * u * by, h); }
        px = bx; py = by;
      } else if (c === 'C') {
        const ax = num(); const ay = num(); const bx = num(); const by = num(); const cx = num(); const cy = num();
        for (let k = 1; k <= 16; k++) {
          const u = k / 16; const v = 1 - u;
          add(v * v * v * px + 3 * v * v * u * ax + 3 * v * u * u * bx + u * u * u * cx, v * v * v * py + 3 * v * v * u * ay + 3 * v * u * u * by + u * u * u * cy, h);
        }
        px = cx; py = cy;
      }
    }
  };
  const one = (op) => {
    const h = op.stroke && op.sw ? op.sw / 2 : 0;
    switch (op.t) {
      case 'rr': add(op.x, op.y, h); add(op.x + op.w, op.y + op.h, h); break;
      case 'el': add(op.cx - op.rx, op.cy - op.ry, h); add(op.cx + op.rx, op.cy + op.ry, h); break;
      case 'ci': add(op.cx - op.r, op.cy - op.r, h); add(op.cx + op.r, op.cy + op.r, h); break;
      case 'path': pathPts(op.d, h); break;
      case 'ln': add(op.x1, op.y1, op.w / 2); add(op.x2, op.y2, op.w / 2); break;
      case 'clip': pathPts(op.d, 0); break;
      default: throw new Error(`primitive inconnue ${op.t}`);
    }
  };
  // les ombres, reflets et détails clippés restent dans la forme : on ne compte que le chemin du clip
  ops.forEach(one);
  return { x0, y0, x1, y1, w: x1 - x0, h: y1 - y0, cx: (x0 + x1) / 2, cy: (y0 + y1) / 2 };
}

/** Met l'objet à l'échelle pour que son plus grand côté fasse `target`, centré en (0, 0). */
function fit(ops, target = FIT) {
  const b = bbox(ops);
  const s = target / Math.max(b.w, b.h);
  return xf(ops, { s, ox: b.cx, oy: b.cy, dx: -b.cx, dy: -b.cy });
}

// ================================================================ morceaux de dessin communs
/** Reflet : tache claire (ellipse penchée de `rot` degrés). */
const spot = (cx, cy, rx, ry, rotation = 0, a = 0.55) => (rotation ? path(ellD(cx, cy, rx, ry, rotation), shine(a)) : el(cx, cy, rx, ry, shine(a)));
/** Forme pleine : aplat, bord plus foncé en bas à droite (la forme décalée recouvre l'aplat foncé), reflets, puis contour encre. */
function solid(d, base, { off = [-0.025, -0.05], dk = 0.2, hl = [], sw = LW, extra = [] } = {}) {
  return [
    path(d, tone(base, { d: dk }), { sil: true }),
    clip(d, [path(shiftD(d, off[0], off[1]), base)]),
    ...extra,
    ...hl.map((h) => spot(...h)),
    path(d, 'none', ink(sw)),
  ];
}
/** Pièce à plat (aplat + contour). */
const flat = (d, fill, o = {}) => path(d, fill, { ...ink(), ...o });
/** Tubes (tiges, bras…) : tous les contours d'abord, puis toutes les couleurs, pour que les tubes se fondent entre eux. */
const tubes = (list, color, w) => {
  const items = list.map((e) => (Array.isArray(e) ? e : [e, w]));
  return [
    ...items.map(([d, wi]) => path(d, 'none', line(INK, wi + 2 * LW))),
    ...items.map(([d, wi]) => path(d, 'none', line(color, wi))),
  ];
};
/** Œil rond : blanc, pupille encre, petit reflet. `look` = direction du regard. */
const eye = (x, y, r = 0.06, look = [0.2, 0.15]) => [
  ci(x, y, r, WHITE, ink(0.022)),
  ci(x + look[0] * r, y + look[1] * r, r * 0.58, INK),
  ci(x + look[0] * r - r * 0.22, y + look[1] * r - r * 0.26, r * 0.2, WHITE),
];
const dotEye = (x, y, r = 0.034) => [ci(x, y, r, INK), ci(x - r * 0.3, y - r * 0.35, r * 0.3, WHITE)];
/** Sourire : un trait encre qui s'affaisse de `h` au milieu. */
const smile = (cx, cy, w, h, sw = 0.026) => path(pb().M(cx - w / 2, cy).Q(cx, cy + 2 * h, cx + w / 2, cy).d, 'none', line(INK, sw));
const blush = (x, y, rx = 0.05, ry = 0.034) => el(x, y, rx, ry, tone('#FF4F86', { a: 0.4 }));

// ================================================================ les couleurs
const RED = '#E8372E';
const ORANGE = '#FF8A1F';
const YELLOW = '#FFD23F';
const GREEN = '#45B649';
const BLUE = '#2F8DEB';
const VIOLET = '#8B5CD6';
const PINK = '#FF86B3';
const BROWN = '#A9693B';
const WHITEISH = '#F6F4EE';
const GREY = '#C7CDD8';
const LEAF = '#3FAE49';
const STEM = '#7A4A2A';

// ================================================================ les 40 objets
const draw = {};

// ---- rouge
draw.pomme = () => {
  const body = 'M 0,-0.25 C 0.07,-0.34 0.2,-0.36 0.31,-0.31 C 0.47,-0.24 0.5,-0.02 0.44,0.2 C 0.38,0.38 0.22,0.46 0.1,0.43 Q 0,0.4 -0.1,0.43 C -0.22,0.46 -0.38,0.38 -0.44,0.2 C -0.5,-0.02 -0.47,-0.24 -0.31,-0.31 C -0.2,-0.36 -0.07,-0.34 0,-0.25 Z';
  return [
    ...solid(body, RED, { hl: [[-0.28, -0.08, 0.06, 0.12, -12], [-0.25, 0.13, 0.024, 0.024]] }),
    ...tubes(['M 0,-0.24 Q 0,-0.36 0.06,-0.45'], STEM, 0.05),
    flat(lensD(0.05, -0.36, 0.32, -0.46, 0.075), LEAF),
  ];
};

draw.fraise = () => {
  const body = 'M 0,-0.3 C 0.2,-0.36 0.42,-0.3 0.45,-0.12 C 0.47,0.08 0.3,0.3 0.1,0.43 Q 0,0.48 -0.1,0.43 C -0.3,0.3 -0.47,0.08 -0.45,-0.12 C -0.42,-0.3 -0.2,-0.36 0,-0.3 Z';
  const seeds = [[-0.24, -0.12], [0, -0.15], [0.24, -0.12], [-0.13, 0.07], [0.13, 0.07], [-0.07, 0.24], [0.07, 0.24]];
  return [
    ...solid(body, RED, { hl: [[-0.3, -0.05, 0.05, 0.09, -20]], extra: seeds.map(([x, y]) => el(x, y, 0.028, 0.042, '#FFE9A8')) }),
    // le chapeau de feuilles : une étoile à six pointes aplatie, posée sur le haut de la fraise
    flat(polyD(starPts(0, -0.3, 6, 0.3, 0.13, -90).map(([x, y]) => [x, -0.3 + (y + 0.3) * 0.62]), 0.025), LEAF),
    ...tubes(['M 0,-0.31 Q 0,-0.39 0.03,-0.46'], LEAF, 0.05),
  ];
};

draw.coeur = () => {
  const body = 'M 0,0.42 C -0.18,0.28 -0.46,0.1 -0.46,-0.14 C -0.46,-0.32 -0.34,-0.4 -0.22,-0.4 C -0.1,-0.4 -0.03,-0.33 0,-0.24 C 0.03,-0.33 0.1,-0.4 0.22,-0.4 C 0.34,-0.4 0.46,-0.32 0.46,-0.14 C 0.46,0.1 0.18,0.28 0,0.42 Z';
  return solid(body, '#EE3A4A', { hl: [[-0.28, -0.2, 0.07, 0.1, -35], [-0.37, -0.06, 0.025, 0.025]] });
};

draw.voiture = () => {
  const cabin = 'M -0.3,-0.02 L -0.2,-0.26 Q -0.18,-0.31 -0.12,-0.31 L 0.12,-0.31 Q 0.18,-0.31 0.22,-0.26 L 0.33,-0.02 Z';
  const body = rrD(-0.47, -0.05, 0.94, 0.31, 0.11);
  const win = '#BFE3FF';
  return [
    ...solid(cabin, RED, { dk: 0.22 }),
    flat('M -0.255,-0.05 L -0.18,-0.23 Q -0.17,-0.25 -0.15,-0.25 L -0.03,-0.25 L -0.03,-0.05 Z', win, ink(0.026)),
    flat('M 0.03,-0.05 L 0.03,-0.25 L 0.11,-0.25 Q 0.14,-0.25 0.16,-0.22 L 0.26,-0.05 Z', win, ink(0.026)),
    ...solid(body, RED, { dk: 0.22, hl: [[-0.3, 0.0, 0.1, 0.03, 0, 0.5]] }),
    flat(ellD(0.425, 0.06, 0.04, 0.05), '#FFE066', ink(0.022)),
    flat(ellD(-0.425, 0.06, 0.03, 0.045), '#FFA42B', ink(0.022)),
    ...[-0.27, 0.27].flatMap((x) => [ci(x, 0.26, 0.125, '#41465F', ink()), ci(x, 0.26, 0.052, '#D9DEE9', ink(0.022))]),
  ];
};

draw.champignon = () => {
  const cap = 'M -0.46,0.06 C -0.46,-0.3 -0.24,-0.44 0,-0.44 C 0.24,-0.44 0.46,-0.3 0.46,0.06 Q 0.46,0.13 0.39,0.13 L -0.39,0.13 Q -0.46,0.13 -0.46,0.06 Z';
  const stem = 'M -0.15,0.07 C -0.17,0.2 -0.22,0.3 -0.2,0.38 Q -0.19,0.44 -0.1,0.44 L 0.1,0.44 Q 0.19,0.44 0.2,0.38 C 0.22,0.3 0.17,0.2 0.15,0.07 Z';
  return [
    ...solid(stem, '#FFF0D2', { dk: 0.12, hl: [[-0.07, 0.28, 0.025, 0.08]] }),
    ...solid(cap, RED, {
      off: [-0.02, -0.04],
      hl: [[-0.27, -0.24, 0.06, 0.1, -40]],
      extra: [clip(cap, [ci(-0.17, -0.12, 0.085, WHITE), ci(0.1, -0.27, 0.075, WHITE), ci(0.27, -0.06, 0.07, WHITE), ci(-0.37, 0.02, 0.05, WHITE), ci(0.0, 0.0, 0.05, WHITE)])],
    }),
  ];
};

draw.cerises = () => {
  const cherry = (x, y) => [
    ...solid(ellD(x, y, 0.2, 0.19), '#E32A3B', { off: [-0.02, -0.045], hl: [[x - 0.09, y - 0.07, 0.045, 0.07, 25], [x - 0.08, y + 0.08, 0.02, 0.02]] }),
  ];
  return [
    ...tubes(['M -0.19,0.04 Q -0.14,-0.3 0.06,-0.38', 'M 0.21,0.06 Q 0.16,-0.28 0.06,-0.38'], '#4F9A3A', 0.04),
    ...cherry(-0.2, 0.22),
    ...cherry(0.22, 0.25),
    flat(lensD(0.07, -0.38, 0.4, -0.32, 0.08), LEAF),
  ];
};

// ---- orange
draw.carotte = () => {
  const body = 'M -0.21,-0.12 Q -0.21,-0.21 -0.1,-0.22 L 0.1,-0.22 Q 0.21,-0.21 0.21,-0.12 C 0.18,0.1 0.09,0.3 0.025,0.44 Q 0,0.49 -0.025,0.44 C -0.09,0.3 -0.18,0.1 -0.21,-0.12 Z';
  const ridge = (x1, y1, x2, y2) => ln(x1, y1, x2, y2, tone('#C25A0A'), 0.026);
  const ob = [
    flat(lensD(0.0, -0.18, 0.2, -0.5, 0.07), LEAF),
    flat(lensD(0.0, -0.18, -0.2, -0.46, 0.07), LEAF),
    flat(lensD(0.0, -0.18, 0.0, -0.52, 0.06), tone(LEAF, { l: 0.12 })),
    ...solid(body, ORANGE, { off: [-0.03, -0.03], hl: [[-0.08, -0.04, 0.03, 0.09, 8]], extra: [ridge(-0.16, 0.02, -0.04, 0.04), ridge(-0.12, 0.17, -0.03, 0.19), ridge(0.15, 0.0, 0.05, -0.02)] }),
  ];
  return rot(ob, -38);
};

draw.orange = () => {
  const dot = (x, y) => ci(x, y, 0.015, tone('#D96A00', { a: 0.7 }));
  return [
    ...solid(ellD(0, 0.04, 0.42, 0.41), '#FF9A1F', { off: [-0.025, -0.05], hl: [[-0.2, -0.12, 0.07, 0.11, -35], [-0.27, 0.06, 0.022, 0.022]], extra: [dot(0.18, 0.16), dot(-0.06, 0.24), dot(0.08, -0.02), dot(0.27, -0.05)] }),
    ...tubes(['M 0,-0.34 L 0.01,-0.4'], '#5E8F3A', 0.045),
    flat(lensD(0.02, -0.37, 0.3, -0.46, 0.065), LEAF),
  ];
};

draw.citrouille = () => {
  const lobe = (cx, w, ry, hl) => solid(ellD(cx, 0.1, w, ry), '#F2701E', { off: [-0.02, -0.05], dk: 0.2, hl });
  return [
    ...lobe(-0.25, 0.23, 0.31, []),
    ...lobe(0.25, 0.23, 0.31, []),
    ...lobe(0, 0.22, 0.35, [[-0.08, -0.06, 0.03, 0.1, 5]]),
    ...tubes(['M 0,-0.25 L 0.03,-0.38'], '#5E8F3A', 0.08),
    flat(lensD(0.05, -0.34, 0.3, -0.38, 0.06), LEAF),
  ];
};

draw.poisson = () => {
  const bodyD = ellD(0.04, 0.02, 0.34, 0.28);
  const tail = 'M -0.25,0 C -0.32,-0.05 -0.4,-0.2 -0.46,-0.25 C -0.52,-0.12 -0.52,0.16 -0.46,0.27 C -0.4,0.21 -0.32,0.06 -0.25,0 Z';
  const dorsal = 'M -0.1,-0.24 C -0.06,-0.42 0.14,-0.44 0.22,-0.24 Z';
  const band = (x, w) => path(pb().M(x - w / 2, -0.4).Q(x - w / 2 - 0.035, 0, x - w / 2, 0.4).L(x + w / 2, 0.4).Q(x + w / 2 - 0.035, 0, x + w / 2, -0.4).Z().d, WHITE, ink(0.024));
  return [
    flat(tail, tone(ORANGE, { l: 0.1 })),
    flat(dorsal, tone(ORANGE, { l: 0.1 })),
    ...solid(bodyD, ORANGE, {
      off: [-0.02, -0.05],
      hl: [[-0.02, -0.17, 0.12, 0.03, -5]],
      extra: [clip(bodyD, [band(0.21, 0.075), band(-0.05, 0.1), band(-0.23, 0.05)])],
    }),
    flat(lensD(-0.02, 0.22, -0.14, 0.38, 0.06), tone(ORANGE, { l: 0.1 })),
    ...eye(0.25, -0.07, 0.075, [0.3, 0.1]),
    smile(0.33, 0.1, 0.1, 0.03, 0.022),
  ];
};

draw.renard = () => {
  const head = 'M -0.46,-0.02 C -0.46,-0.2 -0.3,-0.28 0,-0.28 C 0.3,-0.28 0.46,-0.2 0.46,-0.02 C 0.46,0.14 0.22,0.3 0,0.44 C -0.22,0.3 -0.46,0.14 -0.46,-0.02 Z';
  const cheeks = 'M -0.46,0.0 C -0.34,0.14 -0.16,0.06 0,0.16 C 0.16,0.06 0.34,0.14 0.46,0.0 C 0.46,0.14 0.22,0.3 0,0.44 C -0.22,0.3 -0.46,0.14 -0.46,0.0 Z';
  const ear = (s) => [
    flat(polyD([[s * 0.47, -0.04], [s * 0.4, -0.45], [s * 0.1, -0.24]], 0.04), ORANGE),
    path(polyD([[s * 0.4, -0.1], [s * 0.37, -0.34], [s * 0.21, -0.22]], 0.02), '#7A3B1C'),
  ];
  return [
    ...ear(-1), ...ear(1),
    ...solid(head, ORANGE, { off: [-0.02, -0.03], hl: [[-0.24, -0.2, 0.08, 0.025, -10]], extra: [clip(head, [path(cheeks, WHITE)])] }),
    ...eye(-0.2, -0.03, 0.065, [0.1, 0.3]),
    ...eye(0.2, -0.03, 0.065, [-0.1, 0.3]),
    path(polyD([[-0.07, 0.17], [0.07, 0.17], [0, 0.25]], 0.03), INK),
    path(pb().M(0, 0.25).L(0, 0.29).Q(-0.05, 0.36, -0.11, 0.31).d, 'none', line(INK, 0.022)),
    path(pb().M(0, 0.29).Q(0.05, 0.36, 0.11, 0.31).d, 'none', line(INK, 0.022)),
  ];
};

// ---- jaune
draw.banane = () => {
  // un croissant : une courbe en arc de cercle, plus épaisse au milieu qu'aux bouts ; le gros bout a une queue brune
  const R = 0.5;
  const A = 60; // demi-ouverture de l'arc, en degrés
  const N = 8;
  const half = (t) => 0.02 + 0.115 * Math.sin(Math.PI * t) ** 0.75;
  const at = (t, r) => {
    const a = ((-A + 2 * A * t) * Math.PI) / 180;
    return [r * Math.sin(a), -0.3 + r * Math.cos(a)];
  };
  const pts = [at(0, R)];
  for (let i = 1; i < N; i++) pts.push(at(i / N, R + half(i / N)));
  pts.push(at(1, R));
  for (let i = N - 1; i >= 1; i--) pts.push(at(i / N, R - half(i / N)));
  const d = splineD(pts);
  const [rx, ry] = at(1, R);
  const [lx, ly] = at(0, R);
  const ob = [
    ...tubes([pb().M(rx - 0.005, ry + 0.01).L(rx + 0.04, ry - 0.09).d], STEM, 0.065),
    ...solid(d, YELLOW, { off: [-0.01, -0.035], hl: [[-0.05, 0.3, 0.16, 0.02, 4]] }),
    ci(lx - 0.004, ly - 0.004, 0.03, STEM, ink(0.02)),
  ];
  return rot(ob, -22);
};

draw.etoile = () => solid(starD(0, 0.03, 5, 0.5, 0.24, -90, 0.07), YELLOW, { off: [-0.02, -0.04], hl: [[-0.2, -0.12, 0.05, 0.09, 35]] });

draw.soleil = () => [
  ...solid(starD(0, 0, 12, 0.5, 0.34, -90, 0.055), '#FFB224', { off: [-0.015, -0.025], dk: 0.18 }),
  ...solid(ellD(0, 0, 0.3, 0.3), YELLOW, { off: [-0.02, -0.04], hl: [[-0.15, -0.14, 0.05, 0.08, -35]] }),
  ...dotEye(-0.1, -0.04, 0.036),
  ...dotEye(0.1, -0.04, 0.036),
  smile(0, 0.07, 0.17, 0.05, 0.026),
  blush(-0.19, 0.06),
  blush(0.19, 0.06),
];

draw.poussin = () => {
  const bodyD = ellD(0, 0.0, 0.41, 0.38);
  return [
    ...[-1, 1].map((s) => flat(ellD(s * 0.14, 0.4, 0.1, 0.055), ORANGE)),
    ...[[0, -0.34, 0, -0.5, 0.045], [-0.08, -0.34, -0.17, -0.47, 0.04], [0.08, -0.34, 0.17, -0.47, 0.04]].map(([x1, y1, x2, y2, w]) => flat(lensD(x1, y1, x2, y2, w), tone(YELLOW, { d: 0.05 }))),
    ...solid(bodyD, YELLOW, { off: [-0.02, -0.05], hl: [[-0.24, -0.2, 0.06, 0.09, -35]] }),
    ...[-1, 1].map((s) => flat(ellD(s * 0.31, 0.1, 0.085, 0.16, s * -12), tone(YELLOW, { d: 0.1 }))),
    ...eye(-0.15, -0.1, 0.062, [0.05, 0.2]),
    ...eye(0.15, -0.1, 0.062, [-0.05, 0.2]),
    flat(polyD([[-0.08, 0.0], [0.08, 0.0], [0, 0.11]], 0.025), ORANGE, ink(0.026)),
    blush(-0.24, 0.06, 0.04, 0.028),
    blush(0.24, 0.06, 0.04, 0.028),
  ];
};

draw.fromage = () => {
  // une part de gruyère en perspective : face avant (triangle à trous), dessus clair, côté foncé
  const A = [-0.44, 0.3]; const B = [0.26, 0.3]; const C = [0.26, -0.2];
  const dx = 0.17; const dy = -0.2;
  const sh = (p) => [p[0] + dx, p[1] + dy];
  const front = polyD([A, B, C], 0.04);
  const top = polyD([A, C, sh(C), sh(A)], 0.03);
  const side = polyD([B, C, sh(C), sh(B)], 0.03);
  const hole = (x, y, r) => flat(ellD(x, y, r, r * 0.9), '#F0B01C', ink(0.022));
  return [
    flat(side, '#E4A31A'),
    flat(top, '#FFE680'),
    ...solid(front, YELLOW, { off: [-0.02, -0.03], dk: 0.14 }),
    hole(0.08, 0.1, 0.075), hole(-0.12, 0.22, 0.05), hole(0.15, 0.24, 0.04), hole(0.17, -0.06, 0.04),
  ];
};

// ---- vert
draw.grenouille = () => {
  const bodyD = ellD(0, 0.1, 0.43, 0.34);
  const eyeBump = (s) => [
    ...solid(ellD(s * 0.2, -0.2, 0.17, 0.17), GREEN, { off: [-0.015, -0.035] }),
    ...eye(s * 0.2, -0.21, 0.105, [s * -0.1, 0.25]),
  ];
  return [
    ...[-1, 1].map((s) => flat(ellD(s * 0.28, 0.4, 0.13, 0.06), tone(GREEN, { d: 0.05 }))),
    ...solid(bodyD, GREEN, { off: [-0.02, -0.05], extra: [clip(bodyD, [el(0, 0.3, 0.3, 0.2, '#D4F5A8')])] }),
    ...eyeBump(-1), ...eyeBump(1),
    path(pb().M(-0.24, 0.07).Q(0, 0.27, 0.24, 0.07).d, 'none', line(INK, 0.028)),
    ci(-0.04, -0.02, 0.014, INK), ci(0.04, -0.02, 0.014, INK),
    blush(-0.3, 0.12, 0.05, 0.034), blush(0.3, 0.12, 0.05, 0.034),
  ];
};

draw.sapin = () => [
  rr(-0.07, 0.28, 0.14, 0.18, 0.03, STEM, ink()),
  flat(polyD([[0, -0.06], [0.43, 0.3], [-0.43, 0.3]], 0.05), '#2F9E47'),
  flat(polyD([[0, -0.27], [0.34, 0.1], [-0.34, 0.1]], 0.05), '#3DB554'),
  ...solid(polyD([[0, -0.47], [0.24, -0.14], [-0.24, -0.14]], 0.05), '#55CC63', { off: [-0.02, -0.03], hl: [[-0.07, -0.25, 0.025, 0.07, 22]] }),
];

draw.poire = () => {
  const body = 'M 0,-0.34 C 0.12,-0.36 0.17,-0.22 0.19,-0.12 C 0.22,0.0 0.46,0.06 0.46,0.24 C 0.46,0.4 0.28,0.46 0,0.46 C -0.28,0.46 -0.46,0.4 -0.46,0.24 C -0.46,0.06 -0.22,0.0 -0.19,-0.12 C -0.17,-0.22 -0.12,-0.36 0,-0.34 Z';
  return [
    ...solid(body, '#7CC63F', { off: [-0.025, -0.05], hl: [[-0.27, 0.2, 0.06, 0.1, -15], [-0.07, -0.2, 0.025, 0.07, 6]] }),
    ...tubes(['M 0,-0.33 Q 0,-0.42 0.05,-0.48'], STEM, 0.05),
    flat(lensD(0.03, -0.4, 0.3, -0.46, 0.07), LEAF),
  ];
};

draw.tortue = () => {
  const shell = 'M -0.42,0.2 C -0.42,-0.2 -0.2,-0.4 0,-0.4 C 0.2,-0.4 0.34,-0.2 0.34,0.2 Z';
  const hex = polyD([[-0.04 - 0.14, -0.1], [-0.04 - 0.07, -0.22], [-0.04 + 0.07, -0.22], [-0.04 + 0.14, -0.1], [-0.04 + 0.07, 0.02], [-0.04 - 0.07, 0.02]], 0.02);
  return [
    flat(polyD([[-0.4, 0.12], [-0.52, 0.27], [-0.36, 0.25]], 0.03), '#7BCB4E'),
    ...[-0.24, 0.2].map((x) => flat(ellD(x, 0.34, 0.105, 0.1), '#7BCB4E')),
    flat(ellD(0.39, -0.01, 0.17, 0.155), '#7BCB4E'),
    ...solid(shell, '#2F9E44', {
      off: [-0.02, -0.04],
      extra: [clip(shell, [path(hex, '#58BD62', ink(0.022)), path(pb().M(-0.04, -0.4).L(-0.04, -0.22).M(-0.18, -0.1).L(-0.42, -0.04).M(0.1, -0.1).L(0.34, -0.06).M(-0.11, 0.02).L(-0.15, 0.2).M(0.03, 0.02).L(0.07, 0.2).d, 'none', line(INK, 0.022))])],
    }),
    flat(rrD(-0.45, 0.17, 0.82, 0.1, 0.05), '#E9D99B'),
    ...eye(0.43, -0.05, 0.06, [0.3, 0.1]),
    smile(0.45, 0.07, 0.09, 0.025, 0.022),
    blush(0.36, 0.04, 0.03, 0.022),
  ];
};

draw.cactus = () => [
  ...tubes([['M 0,-0.34 L 0,0.2', 0.27], ['M 0,0.02 L -0.21,0.02 L -0.21,-0.2', 0.15], ['M 0,0.12 L 0.21,0.12 L 0.21,-0.08', 0.15]], '#3FB24A'),
  ln(-0.07, -0.34, -0.07, 0.1, shine(0.35), 0.04, { nosil: true }),
  ln(0.06, -0.28, 0.06, -0.06, tone('#2A8A34', { a: 0.8 }), 0.022, { nosil: true }),
  flat(polyD([[-0.19, 0.19], [0.19, 0.19], [0.15, 0.45], [-0.15, 0.45]], 0.03), '#D07A3E'),
  flat(rrD(-0.25, 0.14, 0.5, 0.1, 0.04), '#E58A47'),
  ci(0, -0.47, 0.05, '#FF7FB0', ink(0.022)),
];

// ---- bleu
draw.baleine = () => {
  const body = 'M -0.3,0.1 C -0.18,-0.12 0.06,-0.2 0.24,-0.12 C 0.42,-0.04 0.48,0.1 0.46,0.2 C 0.4,0.4 0.1,0.44 -0.1,0.38 C -0.22,0.34 -0.3,0.22 -0.3,0.1 Z';
  const tail = 'M -0.22,0.26 C -0.36,0.24 -0.44,0.12 -0.43,-0.02 C -0.56,-0.04 -0.6,-0.18 -0.55,-0.3 C -0.48,-0.28 -0.44,-0.22 -0.41,-0.14 C -0.38,-0.22 -0.33,-0.3 -0.25,-0.3 C -0.22,-0.18 -0.3,-0.08 -0.3,0.04 L -0.2,0.1 Z';
  return [
    ...tubes(['M 0.2,-0.17 Q 0.2,-0.34 0.06,-0.37', 'M 0.2,-0.17 Q 0.2,-0.34 0.34,-0.37'], '#8CCBFF', 0.04),
    flat(tail, '#2A7BD6'),
    ...solid(body, BLUE, { off: [-0.02, -0.05], hl: [[-0.05, -0.12, 0.14, 0.03, -8]], extra: [clip(body, [el(0.05, 0.36, 0.5, 0.15, '#DCEEFF')])] }),
    flat(lensD(0.0, 0.26, -0.12, 0.4, 0.06), '#2A7BD6'),
    ...eye(0.28, 0.05, 0.055, [0.3, 0.1]),
    smile(0.36, 0.17, 0.12, 0.035, 0.022),
  ];
};

draw.parapluie = () => {
  const xs = [0.46, 0.276, 0.092, -0.092, -0.276, -0.46];
  const scallop = pb().M(0.46, -0.02);
  for (let i = 0; i < 5; i++) scallop.Q((xs[i] + xs[i + 1]) / 2, -0.11, xs[i + 1], -0.02);
  const dome = pb().M(-0.46, -0.02).C(-0.46, -0.3, -0.24, -0.44, 0, -0.44).C(0.24, -0.44, 0.46, -0.3, 0.46, -0.02).d + ' ' + scallop.d.replace(/^M [^ ]+ /, '') + ' Z';
  const panel = (i) => pb().M(0, -0.44).Q(xs[i] * 0.95, -0.34, xs[i], -0.02).Q((xs[i] + xs[i + 1]) / 2, -0.11, xs[i + 1], -0.02).Q(xs[i + 1] * 0.95, -0.34, 0, -0.44).Z().d;
  return [
    ...tubes(['M 0,-0.04 L 0,0.3 Q 0,0.44 -0.12,0.44 Q -0.22,0.44 -0.22,0.34'], '#3C4F86', 0.05),
    ...solid(dome, BLUE, {
      off: [-0.02, -0.04],
      extra: [clip(dome, [path(panel(1), '#7DBBFF'), path(panel(3), '#7DBBFF')]), clip(dome, [path(pb().M(0, -0.44).Q(0.2, -0.34, 0.276, -0.02).M(0, -0.44).Q(-0.2, -0.34, -0.276, -0.02).M(0, -0.44).Q(0.1, -0.34, 0.092, -0.02).M(0, -0.44).Q(-0.1, -0.34, -0.092, -0.02).d, 'none', line(INK, 0.022))])],
      hl: [[-0.3, -0.25, 0.05, 0.08, -50]],
    }),
    ci(0, -0.45, 0.03, INK),
  ];
};

draw.goutte = () => solid('M 0,-0.46 C 0.1,-0.28 0.4,0.02 0.4,0.2 C 0.4,0.36 0.22,0.46 0,0.46 C -0.22,0.46 -0.4,0.36 -0.4,0.2 C -0.4,0.02 -0.1,-0.28 0,-0.46 Z', '#3AA5F5', {
  off: [-0.025, -0.055],
  hl: [[-0.17, 0.16, 0.055, 0.11, 20], [-0.1, -0.03, 0.02, 0.05, 25]],
});

draw.bateau = () => [
  ...tubes(['M 0,0.12 L 0,-0.44'], STEM, 0.04),
  flat(polyD([[0.05, -0.4], [0.05, 0.06], [0.42, 0.06]], 0.035), '#F4F9FF'),
  flat(polyD([[-0.05, -0.28], [-0.05, 0.06], [-0.34, 0.06]], 0.035), '#9CCBFF'),
  flat(polyD([[0, -0.44], [0.17, -0.38], [0, -0.32]], 0.02), '#E8372E'),
  ...solid(polyD([[-0.44, 0.12], [0.44, 0.12], [0.31, 0.36], [-0.31, 0.36]], [0.03, 0.03, 0.07, 0.07]), BLUE, { off: [-0.02, -0.035], hl: [[-0.28, 0.17, 0.08, 0.02, 0, 0.5]] }),
  ci(-0.1, 0.22, 0.04, '#EAF5FF', ink(0.024)),
  ci(0.1, 0.22, 0.04, '#EAF5FF', ink(0.024)),
  flat(pb().M(-0.48, 0.34).Q(-0.4, 0.26, -0.3, 0.34).Q(-0.2, 0.26, -0.1, 0.34).Q(0, 0.26, 0.1, 0.34).Q(0.2, 0.26, 0.3, 0.34).Q(0.4, 0.26, 0.48, 0.34).L(0.48, 0.44).Q(0.48, 0.47, 0.44, 0.47).L(-0.44, 0.47).Q(-0.48, 0.47, -0.48, 0.44).Z().d, '#5BB0FF'),
];

draw.ballon = () => [
  path('M 0,0.36 Q 0.08,0.41 0.01,0.45 Q -0.06,0.49 0.02,0.52', 'none', line(INK, 0.022)),
  ...solid('M 0,0.3 C -0.12,0.26 -0.36,0.14 -0.36,-0.12 C -0.36,-0.32 -0.2,-0.46 0,-0.46 C 0.2,-0.46 0.36,-0.32 0.36,-0.12 C 0.36,0.14 0.12,0.26 0,0.3 Z', '#3B82F0', {
    off: [-0.025, -0.05],
    hl: [[-0.17, -0.2, 0.06, 0.12, 25], [-0.06, -0.35, 0.025, 0.025]],
  }),
  flat(polyD([[0, 0.28], [-0.06, 0.37], [0.06, 0.37]], 0.015), '#2A66CC', ink(0.026)),
];

// ---- violet
draw.raisin = () => {
  // une grappe en triangle : 4, 3, 2 puis 1 grains, du haut vers le bas ; les grains du bas sont dessinés d'abord
  const R = 0.118;
  const grapes = [];
  [[0.36, 1], [0.17, 2], [-0.02, 3], [-0.21, 4]].forEach(([y, n]) => {
    for (let i = 0; i < n; i++) grapes.push([(i - (n - 1) / 2) * 0.235, y]);
  });
  return [
    ...tubes(['M 0,-0.18 Q 0,-0.34 0.04,-0.44'], STEM, 0.05),
    flat(lensD(0.03, -0.38, 0.33, -0.46, 0.07), LEAF),
    ...grapes.flatMap(([x, y]) => solid(ellD(x, y, R, R), VIOLET, { off: [-0.015, -0.03], hl: [[x - 0.05, y - 0.05, 0.026, 0.04, 30]] })),
  ];
};

draw.aubergine = () => {
  // longue, le cou courbé, le gros bout en bas, un chapeau de sépales verts
  const bend = (x, y) => [x + 0.2 * ((0.47 - y) / 0.8) ** 2, y];
  const body = mapD('M -0.09,-0.3 C -0.1,-0.1 -0.27,0.02 -0.28,0.22 C -0.29,0.38 -0.16,0.47 0,0.47 C 0.16,0.47 0.29,0.38 0.28,0.22 C 0.27,0.02 0.1,-0.1 0.09,-0.3 Z', bend);
  const calyx = mapD(polyD([[-0.17, -0.34], [0.17, -0.34], [0.2, -0.21], [0.09, -0.25], [0, -0.15], [-0.09, -0.25], [-0.2, -0.21]], 0.025), bend);
  const stem = mapD('M 0,-0.33 Q 0,-0.42 0.03,-0.48', bend);
  return [
    ...tubes([stem], '#4C9A3A', 0.06),
    ...solid(body, VIOLET, { off: [-0.025, -0.04], hl: [[-0.15, 0.22, 0.04, 0.11, 0], [-0.07, -0.08, 0.02, 0.06, 10]] }),
    flat(calyx, '#4C9A3A'),
  ];
};

draw.papillon = () => {
  const upper = 'M -0.04,-0.03 C -0.1,-0.3 -0.28,-0.47 -0.42,-0.42 C -0.55,-0.36 -0.5,-0.12 -0.35,-0.02 C -0.22,0.06 -0.1,0.04 -0.04,-0.03 Z';
  const lower = 'M -0.04,0.02 C -0.16,0.0 -0.4,0.06 -0.43,0.24 C -0.43,0.38 -0.27,0.44 -0.17,0.36 C -0.08,0.28 -0.04,0.14 -0.04,0.02 Z';
  const spots = [ci(-0.3, -0.24, 0.075, '#EBDDFF'), ci(-0.25, 0.28, 0.05, '#EBDDFF'), ci(-0.14, -0.05, 0.03, '#EBDDFF')];
  const wing = [
    ...solid(lower, VIOLET, { off: [-0.015, -0.04], extra: [clip(lower, [spots[1]])] }),
    ...solid(upper, VIOLET, { off: [-0.015, -0.04], extra: [clip(upper, [spots[0], spots[2]])], hl: [[-0.38, -0.34, 0.04, 0.025, -30]] }),
  ];
  return [
    ...wing,
    ...mirror(wing),
    ...tubes(['M -0.02,-0.2 Q -0.07,-0.36 -0.17,-0.44', 'M 0.02,-0.2 Q 0.07,-0.36 0.17,-0.44'], INK, 0.0),
    ...solid(capD(0, -0.18, 0, 0.3, 0.05), '#4B2E83', { off: [-0.01, -0.02], dk: 0.2 }),
    ci(-0.17, -0.45, 0.025, INK), ci(0.17, -0.45, 0.025, INK),
    ci(0, -0.2, 0.065, '#4B2E83', ink()),
  ];
};

// ---- rose
draw.cochon = () => {
  const ear = (s) => flat(polyD([[s * 0.12, -0.35], [s * 0.47, -0.46], [s * 0.44, -0.1]], 0.07), '#FF6FA3');
  return [
    ear(-1), ear(1),
    ...solid(ellD(0, 0.05, 0.45, 0.39), PINK, { off: [-0.02, -0.05], hl: [[-0.25, -0.15, 0.07, 0.04, -30]] }),
    ...eye(-0.2, -0.08, 0.06, [0.1, 0.2]),
    ...eye(0.2, -0.08, 0.06, [-0.1, 0.2]),
    flat(ellD(0, 0.14, 0.19, 0.14), '#FFB3CE'),
    el(-0.065, 0.14, 0.03, 0.045, INK),
    el(0.065, 0.14, 0.03, 0.045, INK),
    blush(-0.34, 0.12, 0.05, 0.035),
    blush(0.34, 0.12, 0.05, 0.035),
  ];
};

draw.glace = () => {
  const cone = polyD([[-0.23, 0.0], [0.23, 0.0], [0, 0.48]], 0.03);
  const skirt = pb().M(-0.33, -0.06).C(-0.35, -0.3, 0.35, -0.3, 0.33, -0.06).L(0.34, 0.0).Q(0.27, 0.11, 0.17, 0.03).Q(0.08, 0.12, 0, 0.03).Q(-0.08, 0.12, -0.17, 0.03).Q(-0.27, 0.11, -0.34, 0.0).Z().d;
  return [
    ...solid(cone, '#E8B25C', {
      off: [-0.02, -0.03],
      extra: [clip(cone, [path(pb().M(-0.23, 0.08).L(0.07, 0.42).M(-0.12, 0.0).L(0.16, 0.32).M(0.23, 0.08).L(-0.07, 0.42).M(0.12, 0.0).L(-0.16, 0.32).d, 'none', line('#C98A3A', 0.022))])],
    }),
    ...solid(skirt, '#FFB6D2', { off: [-0.02, -0.04], hl: [[-0.2, -0.1, 0.045, 0.05, -20]] }),
    ...solid(ellD(0, -0.28, 0.23, 0.21), PINK, { off: [-0.02, -0.04], hl: [[-0.1, -0.37, 0.05, 0.065, -35]] }),
  ];
};

draw.cupcake = () => {
  const cup = polyD([[-0.34, 0.0], [0.34, 0.0], [0.26, 0.43], [-0.26, 0.43]], [0.04, 0.04, 0.05, 0.05]);
  return [
    ...solid(cup, '#F0629C', {
      off: [-0.02, -0.03],
      extra: [ln(-0.17, 0.05, -0.13, 0.4, tone(WHITE, { a: 0.35 }), 0.04), ln(0.0, 0.05, 0.0, 0.4, tone(WHITE, { a: 0.35 }), 0.04), ln(0.17, 0.05, 0.13, 0.4, tone(WHITE, { a: 0.35 }), 0.04)],
    }),
    ...solid(capD(-0.29, -0.0, 0.29, -0.0, 0.12), '#FF9CC2', { off: [-0.015, -0.04] }),
    ...solid(capD(-0.2, -0.17, 0.2, -0.17, 0.11), '#FFA9CB', { off: [-0.015, -0.04] }),
    ...solid(ellD(0, -0.32, 0.12, 0.11), '#FFB7D3', { off: [-0.015, -0.035], hl: [[-0.05, -0.36, 0.03, 0.04, -30]] }),
    ...solid(ellD(0, -0.44, 0.06, 0.055), '#E5383B', { off: [-0.01, -0.02], dk: 0.2 }),
  ];
};

// ---- marron
draw.ours = () => {
  const ear = (s) => [...solid(ellD(s * 0.32, -0.3, 0.15, 0.15), BROWN, { off: [-0.01, -0.03] }), path(ellD(s * 0.32, -0.29, 0.08, 0.08), '#E7B98A')];
  return [
    ...ear(-1), ...ear(1),
    ...solid(ellD(0, 0.05, 0.44, 0.39), BROWN, { off: [-0.02, -0.05], hl: [[-0.24, -0.16, 0.07, 0.045, -30]] }),
    ...eye(-0.19, -0.07, 0.06, [0.1, 0.2]),
    ...eye(0.19, -0.07, 0.06, [-0.1, 0.2]),
    flat(ellD(0, 0.16, 0.19, 0.15), '#EBC9A0'),
    path(polyD([[-0.065, 0.08], [0.065, 0.08], [0, 0.145]], 0.035), INK),
    path(pb().M(0, 0.14).L(0, 0.2).Q(-0.05, 0.27, -0.11, 0.22).d, 'none', line(INK, 0.022)),
    path(pb().M(0, 0.2).Q(0.05, 0.27, 0.11, 0.22).d, 'none', line(INK, 0.022)),
    blush(-0.33, 0.12), blush(0.33, 0.12),
  ];
};

draw.cookie = () => {
  const R = 0.44;
  const th = -42;
  const a1 = th - 23;
  const a2 = th + 23;
  const P = (a) => [R * Math.cos((a * Math.PI) / 180), R * Math.sin((a * Math.PI) / 180)];
  const p1 = P(a1);
  const p2 = P(a2);
  const b = pb().arc(0, 0, R, R, a2, a1 + 360);
  // trois croissants de morsure, du bout p1 au bout p2, creusés vers le centre
  const mx = (p1[0] + p2[0]) / 2;
  const my = (p1[1] + p2[1]) / 2;
  const L = Math.hypot(p2[0] - p1[0], p2[1] - p1[1]);
  const nx = -(mx) / Math.hypot(mx, my);
  const ny = -(my) / Math.hypot(mx, my);
  const ux = (p2[0] - p1[0]) / L;
  const uy = (p2[1] - p1[1]) / L;
  const seg = L / 3;
  for (let i = 0; i < 3; i++) {
    const sx = p1[0] + ux * seg * i;
    const sy = p1[1] + uy * seg * i;
    const ex = sx + ux * seg;
    const ey = sy + uy * seg;
    b.C(sx + nx * 0.1 + ux * seg * 0.05, sy + ny * 0.1 + uy * seg * 0.05, ex + nx * 0.1 - ux * seg * 0.05, ey + ny * 0.1 - uy * seg * 0.05, ex, ey);
  }
  const d = b.Z().d;
  const chips = [[-0.2, -0.2, 0.065, 0.05, 20], [0.06, -0.05, 0.06, 0.045, -25], [-0.24, 0.1, 0.06, 0.045, -10], [0.17, 0.2, 0.065, 0.05, 30], [-0.04, 0.27, 0.05, 0.04, 0], [0.33, 0.05, 0.045, 0.035, 15], [-0.02, -0.3, 0.04, 0.032, 0]];
  return solid(d, '#C98B4B', {
    off: [-0.02, -0.05],
    extra: chips.map(([x, y, rx, ry, a]) => path(ellD(x, y, rx, ry, a), '#5A3320')),
    hl: [[-0.26, -0.05, 0.03, 0.09, 0, 0.35]],
  });
};

draw.maison = () => [
  rr(0.16, -0.4, 0.12, 0.24, 0.02, '#6B3F25', ink()),
  ...solid(rrD(-0.34, -0.06, 0.68, 0.5, 0.04), '#C48A55', { off: [-0.02, -0.03] }),
  ...solid(polyD([[-0.47, -0.02], [0, -0.45], [0.47, -0.02]], 0.05), '#7B4B2C', { off: [-0.02, -0.035], dk: 0.25 }),
  flat(rrD(-0.09, 0.15, 0.18, 0.29, 0.08), '#FFE7B8'),
  ci(0.045, 0.3, 0.015, '#B4783C'),
  ...[-0.22, 0.22].flatMap((x) => [rr(x - 0.065, 0.06, 0.13, 0.13, 0.02, '#FFE7B8', ink(0.026)), ln(x, 0.06, x, 0.19, INK, 0.018), ln(x - 0.065, 0.125, x + 0.065, 0.125, INK, 0.018)]),
];

draw.chocolat = () => {
  const bar = rrD(-0.31, -0.45, 0.62, 0.9, 0.07);
  const cells = [];
  for (let r = 0; r < 4; r++) for (let c = 0; c < 3; c++) cells.push(rr(-0.3 + c * 0.2, -0.44 + r * 0.2, 0.2, 0.2, 0.03, '#8E5A36', line('#4B2A18', 0.02)));
  const foil = pb().M(-0.33, 0.04).L(-0.22, -0.01).L(-0.11, 0.05).L(0.0, -0.01).L(0.11, 0.05).L(0.22, -0.01).L(0.33, 0.04).L(0.33, 0.43).Q(0.33, 0.47, 0.29, 0.47).L(-0.29, 0.47).Q(-0.33, 0.47, -0.33, 0.43).Z().d;
  const ob = [
    ...solid(bar, '#7A4A2A', { off: [-0.02, -0.03], extra: [clip(bar, cells)] }),
    ...solid(foil, '#D4DAE6', { off: [-0.02, -0.04], dk: 0.15, hl: [[-0.2, 0.2, 0.03, 0.12, 0, 0.6]] }),
  ];
  return rot(ob, -12);
};

// ---- neutre
draw.nuage = () => {
  const body = 'M -0.3,0.3 C -0.54,0.3 -0.56,-0.04 -0.3,-0.07 C -0.35,-0.4 0.04,-0.46 0.1,-0.17 C 0.18,-0.44 0.48,-0.34 0.42,-0.05 C 0.62,-0.03 0.58,0.3 0.34,0.3 Z';
  return solid(body, WHITEISH, { off: [-0.02, -0.06], dk: 0.1, hl: [[-0.15, -0.2, 0.06, 0.05, -35], [-0.38, 0.05, 0.03, 0.03]] });
};

draw.fantome = () => {
  const body = pb().M(-0.36, 0.34).L(-0.36, -0.06).C(-0.36, -0.32, -0.2, -0.44, 0, -0.44).C(0.2, -0.44, 0.36, -0.32, 0.36, -0.06).L(0.36, 0.34).Q(0.24, 0.58, 0.12, 0.34).Q(0, 0.58, -0.12, 0.34).Q(-0.24, 0.58, -0.36, 0.34).Z().d;
  return [
    ...[-1, 1].map((s) => flat(ellD(s * 0.4, 0.05, 0.11, 0.065, s * 28), WHITEISH)),
    ...solid(body, WHITEISH, { off: [-0.025, -0.04], dk: 0.1, hl: [[-0.2, -0.28, 0.05, 0.07, -35]] }),
    el(-0.14, -0.1, 0.05, 0.07, INK), el(0.14, -0.1, 0.05, 0.07, INK),
    ci(-0.125, -0.13, 0.016, WHITE), ci(0.155, -0.13, 0.016, WHITE),
    path(pb().M(-0.08, 0.02).Q(0, 0.14, 0.08, 0.02).Z().d, INK, line(INK, 0.02)),
    blush(-0.25, 0.01), blush(0.25, 0.01),
  ];
};

draw.bonhomme = () => [
  ...tubes(['M -0.2,0.1 L -0.42,-0.04 M -0.36,-0.01 L -0.44,-0.13', 'M 0.2,0.1 L 0.42,-0.04 M 0.36,-0.01 L 0.44,-0.13'], STEM, 0.035),
  ...solid(ellD(0, 0.2, 0.27, 0.26), WHITEISH, { off: [-0.025, -0.05], dk: 0.1 }),
  ...solid(ellD(0, -0.22, 0.2, 0.2), WHITEISH, { off: [-0.02, -0.04], dk: 0.1, hl: [[-0.1, -0.31, 0.04, 0.05, -35]] }),
  ...dotEye(-0.08, -0.28, 0.03), ...dotEye(0.08, -0.28, 0.03),
  flat(polyD([[-0.02, -0.21], [0.18, -0.17], [-0.02, -0.13]], 0.02), ORANGE, ink(0.026)),
  flat(capD(-0.17, -0.02, 0.17, -0.02, 0.05), '#E8372E', ink(0.026)),
  flat(rrD(0.06, -0.0, 0.1, 0.17, 0.03), '#E8372E', ink(0.026)),
  ci(0, 0.11, 0.03, INK), ci(0, 0.22, 0.03, INK), ci(0, 0.33, 0.03, INK),
];

draw.elephant = () => {
  const ear = (s) => [
    ...solid(ellD(s * 0.37, -0.2, 0.2, 0.28), GREY, { off: [-0.015, -0.04], dk: 0.14 }),
    path(ellD(s * 0.37, -0.18, 0.12, 0.2), '#EBD0D8'),
  ];
  return [
    ...ear(-1), ...ear(1),
    ...solid('M -0.125,-0.02 L -0.125,0.3 C -0.125,0.45 -0.03,0.52 0.1,0.52 C 0.24,0.52 0.34,0.44 0.34,0.3 Q 0.34,0.24 0.28,0.24 Q 0.22,0.24 0.22,0.3 C 0.22,0.36 0.17,0.39 0.12,0.38 C 0.125,0.37 0.125,0.34 0.125,0.3 L 0.125,-0.02 Z', GREY, { off: [-0.02, -0.03], dk: 0.14 }),
    ...solid(ellD(0, -0.14, 0.31, 0.3), GREY, { off: [-0.02, -0.05], dk: 0.14, hl: [[-0.16, -0.29, 0.07, 0.045, -30]] }),
    ...eye(-0.13, -0.17, 0.058, [0.1, 0.25]),
    ...eye(0.13, -0.17, 0.058, [-0.1, 0.25]),
    blush(-0.23, -0.03, 0.04, 0.03), blush(0.23, -0.03, 0.04, 0.03),
    ln(-0.06, 0.24, -0.06, 0.3, tone(INK, { a: 0.3 }), 0.016, { nosil: true }),
    ln(-0.06, 0.34, -0.06, 0.4, tone(INK, { a: 0.3 }), 0.016, { nosil: true }),
  ];
};

// ================================================================ assemblage
const objects = OBJECTS.map((o) => {
  if (!draw[o.id]) throw new Error(`objet du catalogue sans dessin : ${o.id}`);
  const ops = fit(draw[o.id]());
  return { ...o, ops };
});
for (const id of Object.keys(draw)) if (!OBJECTS.some((o) => o.id === id)) throw new Error(`dessin sans objet dans le catalogue : ${id}`);

// ================================================================ rendu SVG
const f2 = (n) => Math.round(n * 1000) / 1000;
const hex = (c) => { const h = c.replace('#', ''); return [0, 2, 4].map((i) => parseInt(h.slice(i, i + 2), 16)); };
const mix = (a, b, t) => { const x = hex(a); const y = hex(b); return '#' + x.map((v, i) => Math.round(v + (y[i] - v) * t).toString(16).padStart(2, '0')).join(''); };

function resolve(F) {
  if (typeof F === 'string') return { hex: F, a: 1 };
  let h = F.c;
  if (F.l) h = mix(h, WHITE, F.l);
  if (F.d) h = mix(h, BLACK, F.d);
  return { hex: h, a: F.a === undefined ? 1 : F.a };
}

let uid = 0;
function svgPaint(F, s, defs, attr = 'fill') {
  if (F === 'none') return `${attr}="none"`;
  if (F && F.grad) {
    const id = `g${++uid}`;
    const g = F.grad;
    const stops = g.stops.map(([o, c]) => { const r = resolve(c); return `<stop offset="${o}" stop-color="${r.hex}" stop-opacity="${r.a}"/>`; }).join('');
    defs.push(`<linearGradient id="${id}" gradientUnits="userSpaceOnUse" x1="${f2(g.from[0] * s)}" y1="${f2(g.from[1] * s)}" x2="${f2(g.to[0] * s)}" y2="${f2(g.to[1] * s)}">${stops}</linearGradient>`);
    return `${attr}="url(#${id})"`;
  }
  const r = resolve(F);
  return `${attr}="${r.hex}"${r.a < 1 ? ` ${attr}-opacity="${r.a}"` : ''}`;
}
function svgStroke(o, s, defs) {
  if (!o.stroke) return '';
  return ` ${svgPaint(o.stroke, s, defs, 'stroke')} stroke-width="${f2((o.sw || 0.01) * s)}" stroke-linejoin="round" stroke-linecap="round"`;
}
const scalePath = (d, s) => d.replace(/-?\d*\.?\d+/g, (n) => f2(parseFloat(n) * s));

/** Opérations en SVG ; en mode `silhouette`, tout est peint de la couleur `silColor` et seules les opérations contournées (ou `sil`) comptent. */
function svgOps(ops, s, defs, sil = null) {
  const out = [];
  for (const op of ops) {
    if (op.t === 'clip') {
      const inner = svgOps(op.ops, s, defs, sil);
      if (!inner) continue;
      const id = `c${++uid}`;
      defs.push(`<clipPath id="${id}"><path d="${scalePath(op.d, s)}"/></clipPath>`);
      out.push(`<g clip-path="url(#${id})">${inner}</g>`);
      continue;
    }
    let fillF = op.fill;
    let strokeF = op.stroke;
    if (sil) {
      if (!inSilhouette(op)) continue;
      fillF = op.fill === 'none' ? 'none' : sil;
      strokeF = op.stroke ? sil : undefined;
    }
    const o = { ...op, stroke: strokeF };
    switch (op.t) {
      case 'rr': out.push(`<rect x="${f2(op.x * s)}" y="${f2(op.y * s)}" width="${f2(op.w * s)}" height="${f2(op.h * s)}" rx="${f2(op.r * s)}" ${svgPaint(fillF, s, defs)}${svgStroke(o, s, defs)}/>`); break;
      case 'el': out.push(`<ellipse cx="${f2(op.cx * s)}" cy="${f2(op.cy * s)}" rx="${f2(op.rx * s)}" ry="${f2(op.ry * s)}" ${svgPaint(fillF, s, defs)}${svgStroke(o, s, defs)}/>`); break;
      case 'ci': out.push(`<circle cx="${f2(op.cx * s)}" cy="${f2(op.cy * s)}" r="${f2(op.r * s)}" ${svgPaint(fillF, s, defs)}${svgStroke(o, s, defs)}/>`); break;
      case 'path': out.push(`<path d="${scalePath(op.d, s)}" ${svgPaint(fillF, s, defs)}${svgStroke(o, s, defs)}/>`); break;
      case 'ln': {
        const c = sil || op.stroke;
        out.push(`<path d="M${f2(op.x1 * s)},${f2(op.y1 * s)}L${f2(op.x2 * s)},${f2(op.y2 * s)}" fill="none" ${svgPaint(c, s, defs, 'stroke')} stroke-width="${f2(op.w * s)}" stroke-linecap="round"/>`);
        break;
      }
      default: throw new Error(`primitive inconnue ${op.t}`);
    }
  }
  return out.join('');
}

/** Le dessin d'un objet en SVG, centré en (0, 0), pour un carré de côté `s` pixels. `silhouette` = une couleur : aplat d'une seule couleur. */
function svgObject(obj, s, silhouette = null) {
  const defs = [];
  const body = svgOps(obj.ops, s, defs, silhouette);
  return `<defs>${defs.join('')}</defs>${body}`;
}

// ================================================================ génération Kotlin
// Un objet = UNE fonction `art<Nom>(s, sil)` : `sil == null` dessine l'objet en couleurs, `sil != null` dessine sa silhouette (tout de la couleur `sil`).
// Les chemins sont construits une fois par fonction (`val pN`) et réutilisés pour l'aplat, le clip et le contour.
const kf = (n) => {
  const v = Math.round(n * 1000) / 1000;
  return Number.isInteger(v) ? `${v}.0f` : `${v}f`;
};
function kColor(F) {
  const r = resolve(F);
  const a = Math.round(r.a * 255).toString(16).padStart(2, '0');
  return `Color(0x${(a + r.hex.slice(1)).toUpperCase()})`;
}
function kBrush(F) {
  if (F && F.grad) {
    const g = F.grad;
    const stops = g.stops.map(([o, c]) => `${kf(o)} to ${kColor(c)}`).join(', ');
    return `Brush.linearGradient(${stops}, start = Offset(${kf(g.from[0])} * s, ${kf(g.from[1])} * s), end = Offset(${kf(g.to[0])} * s, ${kf(g.to[1])} * s))`;
  }
  return kColor(F);
}
function kPath(d) {
  const t = d.trim().split(/[\s,]+/);
  const out = [];
  let i = 0;
  const num = () => `${kf(parseFloat(t[i++]))} * s`;
  while (i < t.length) {
    const cmd = t[i++];
    if (cmd === 'M') out.push(`moveTo(${num()}, ${num()})`);
    else if (cmd === 'L') out.push(`lineTo(${num()}, ${num()})`);
    else if (cmd === 'Q') out.push(`quadraticTo(${num()}, ${num()}, ${num()}, ${num()})`);
    else if (cmd === 'C') out.push(`cubicTo(${num()}, ${num()}, ${num()}, ${num()}, ${num()}, ${num()})`);
    else if (cmd === 'Z') out.push('close()');
    else throw new Error(`commande de chemin non gérée : ${cmd}`);
  }
  return `Path().apply { ${out.join('; ')} }`;
}

/** Une opération fait partie de la silhouette si elle a un contour, ou l'option `sil` (un `ln` en fait toujours partie sauf `nosil`). */
const inSilhouette = (op) => {
  if (op.t === 'clip') return op.ops.some(inSilhouette);
  if (op.nosil) return false;
  return op.t === 'ln' || Boolean(op.stroke || op.sil);
};

/** Les lignes Kotlin d'une liste d'opérations (`pv` donne le nom du chemin `val` d'un chemin). */
function kOps(ops, ind, pv, guarded = false) {
  const pad = ' '.repeat(ind);
  const items = []; // { lines, sil }
  for (const op of ops) {
    if (op.t === 'clip') {
      const silClip = inSilhouette(op);
      // un clip hors silhouette est déjà dans un `if (sil == null)` : ses enfants n'ont pas besoin d'y être
      const inner = kOps(op.ops, ind + 4, pv, !silClip);
      items.push({ lines: [`${pad}clipPath(${pv(op.d)}) {`, ...inner, `${pad}}`], sil: silClip });
      continue;
    }
    const sil = guarded ? false : inSilhouette(op);
    // couleur : celle de l'opération, ou `sil` quand on dessine la silhouette (seulement pour les opérations qui en font partie)
    const paint = (F) => {
      if (sil && F && F.grad) throw new Error('un dégradé ne peut pas faire partie d\'une silhouette');
      return sil ? `sil ?: ${kBrush(F)}` : kBrush(F);
    };
    const stroke = op.stroke ? `, style = Stroke(${kf(op.sw || 0.01)} * s, cap = StrokeCap.Round, join = StrokeJoin.Round)` : '';
    const lines = [];
    switch (op.t) {
      case 'rr': {
        const draw = (p, extra = '') => `drawRoundRect(${p}, Offset(${kf(op.x)} * s, ${kf(op.y)} * s), Size(${kf(op.w)} * s, ${kf(op.h)} * s), CornerRadius(${kf(op.r)} * s)${extra})`;
        if (op.fill !== 'none') lines.push(pad + draw(paint(op.fill)));
        if (op.stroke) lines.push(pad + draw(paint(op.stroke), stroke));
        break;
      }
      case 'el': {
        const draw = (p, extra = '') => `drawOval(${p}, Offset(${kf(op.cx - op.rx)} * s, ${kf(op.cy - op.ry)} * s), Size(${kf(op.rx * 2)} * s, ${kf(op.ry * 2)} * s)${extra})`;
        if (op.fill !== 'none') lines.push(pad + draw(paint(op.fill)));
        if (op.stroke) lines.push(pad + draw(paint(op.stroke), stroke));
        break;
      }
      case 'ci': {
        const draw = (p, extra = '') => `drawCircle(${p}, ${kf(op.r)} * s, Offset(${kf(op.cx)} * s, ${kf(op.cy)} * s)${extra})`;
        if (op.fill !== 'none') lines.push(pad + draw(paint(op.fill)));
        if (op.stroke) lines.push(pad + draw(paint(op.stroke), stroke));
        break;
      }
      case 'path': {
        const p = pv(op.d);
        if (op.fill !== 'none') lines.push(`${pad}drawPath(${p}, ${paint(op.fill)})`);
        if (op.stroke) lines.push(`${pad}drawPath(${p}, ${paint(op.stroke)}${stroke})`);
        break;
      }
      case 'ln':
        lines.push(`${pad}drawLine(${paint(op.stroke)}, Offset(${kf(op.x1)} * s, ${kf(op.y1)} * s), Offset(${kf(op.x2)} * s, ${kf(op.y2)} * s), ${kf(op.w)} * s, StrokeCap.Round)`);
        break;
      default:
        throw new Error(`primitive inconnue ${op.t}`);
    }
    items.push({ lines, sil });
  }
  // les opérations hors silhouette sont regroupées dans un `if (sil == null) { ... }`
  if (guarded) return items.flatMap((it) => it.lines);
  const out = [];
  for (let i = 0; i < items.length;) {
    if (items[i].sil) { out.push(...items[i].lines); i++; continue; }
    let j = i;
    while (j < items.length && !items[j].sil) j++;
    out.push(`${pad}if (sil == null) {`, ...items.slice(i, j).flatMap((it) => it.lines.map((l) => `    ${l}`)), `${pad}}`);
    i = j;
  }
  return out;
}

const camel = (id) => id.charAt(0).toUpperCase() + id.slice(1);
const ktString = (s) => `"${s.replace(/\\/g, '\\\\').replace(/"/g, '\\"').replace(/\$/g, '\\$')}"`;

function kObject(o) {
  const names = new Map();
  const vals = [];
  const pv = (d) => {
    if (!names.has(d)) {
      names.set(d, `p${names.size}`);
      vals.push(`    val ${names.get(d)} = ${kPath(d)}`);
    }
    return names.get(d);
  };
  const body = kOps(o.ops, 4, pv);
  return `\nprivate fun DrawScope.art${camel(o.id)}(s: Float, sil: Color?) {\n${vals.join('\n')}\n${body.join('\n')}\n}\n`;
}

function kotlin() {
  const { HUES, KINDS } = require('../find/catalog.js');
  const enumEntries = objects.map((o) => `    ${o.id.toUpperCase()}(${ktString(o.id)}, ${ktString(o.label)}, FindHue.${o.hue.toUpperCase()}, FindKind.${o.kind.toUpperCase()})`).join(',\n');
  let out = `// GÉNÉRÉ par tools/design/make-objects.js : ne pas modifier à la main (la CI vérifie que ce fichier est à jour).
// Les 40 objets de Cherche et Trouve, dessinés de face ou de côté, centrés en (0, 0), dans le carré de côté \`s\` (en pixels). Style plat, contour encre.
@file:Suppress("ktlint", "UNUSED_PARAMETER", "NAME_SHADOWING")

package com.kcharignon.petitsjeux.games.find.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath

/** Les familles de couleurs des objets (dans l'ordre du catalogue \`tools/find/catalog.js\`). */
enum class FindHue { ${HUES.map((h) => h.toUpperCase()).join(', ')} }

/** Les sortes d'objets (dans l'ordre du catalogue). */
enum class FindKind { ${KINDS.map((k) => k.toUpperCase()).join(', ')} }

/** Les ${objects.length} objets : identifiant (écrit dans find_levels.json), nom (« une pomme »), famille de couleur, sorte. */
enum class FindObject(val id: String, val label: String, val hue: FindHue, val kind: FindKind) {
${enumEntries};

    companion object {
        fun byId(id: String): FindObject? = entries.firstOrNull { it.id == id }
    }
}

/** Dessine l'objet centré en (0, 0), contenu dans le carré [-s/2, s/2] x [-s/2, s/2] (s en pixels). */
fun DrawScope.drawFindObject(o: FindObject, s: Float) = drawArt(o, s, null)

/** Même silhouette en aplat d'une seule couleur (pour pâlir un objet ou le surligner), même cadre. */
fun DrawScope.drawFindSilhouette(o: FindObject, color: Color, s: Float) = drawArt(o, s, color)

private fun DrawScope.drawArt(o: FindObject, s: Float, sil: Color?) {
    when (o) {
${objects.map((x) => `        FindObject.${x.id.toUpperCase()} -> art${camel(x.id)}(s, sil)`).join('\n')}
    }
}
`;
  for (const o of objects) out += kObject(o);
  return out;
}

module.exports = { objects, kotlin, svgObject, bbox, INK, FIT };
