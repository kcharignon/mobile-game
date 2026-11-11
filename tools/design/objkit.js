// La boîte à outils de dessin des objets de Cherche et Trouve : primitives, constructeurs de chemins, formes communes, couleurs.
// `objects.js` l'assemble avec les dessins ; les lots `objects-*.js` y piochent via `require('./objkit.js')`. Rien ici ne dessine un objet.
//
// Chaque objet est dessiné de face ou de côté, dans le carré [-0.5, 0.5] x [-0.5, 0.5] (l'unité = le côté du carré, y vers le bas),
// puis mis à l'échelle et recentré automatiquement (`fit`) pour occuper ~90 % du carré, contour compris.
// Une description = une liste d'opérations dessinées dans l'ordre : `rr`, `el`, `ci`, `path` (M/L/Q/C/Z), `ln`, `clip`.
// Couleurs : une chaîne '#RRGGBB', ou une teinte `{c:'#RRGGBB', l:0.2}` éclaircie, `{d:0.3}` assombrie, `{a:0.5}` transparence.
// Contour : `{stroke:'#RRGGBB', sw:0.036}` (épaisseur en unités). Silhouette (aplat d'une seule couleur) : toute opération qui a un contour,
// ou l'option `sil: true` ; les autres (reflets, ombres, détails sans contour) n'en font pas partie.
'use strict';


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

module.exports = { INK, LW, WHITE, BLACK, FIT, tone, shine, rr, el, ci, path, ln, clip, line, ink, r4, pb, ellD, rrD, capD, polyD, starPts, starD, lensD, splineD, mapD, shiftD, opToD, xf, rot, mirror, bbox, fit, spot, solid, flat, tubes, eye, dotEye, smile, blush, RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET, PINK, BROWN, WHITEISH, GREY, LEAF, STEM };
