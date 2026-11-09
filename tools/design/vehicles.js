undefined// Les véhicules de Parking Malin : UNE description (ce fichier) qui produit l'aperçu SVG (docs/design) ET le code Kotlin
// (android/.../games/parking/ui/VehicleArt.kt, généré). Tout est dessiné « de dessus », nez vers le haut (y négatif), l'unité est la case.
//
// Une voiture de n cases : largeur 0,8 (x de -0,4 à 0,4), longueur n - 0,2 (y de -L/2 à L/2).
// Chaque modèle donne : `len`, `silhouette` (pour la tranche sombre), `wheels` (centres des roues) et `parts` (le dessus).
// Couleurs : 'body' = la couleur de la voiture ; `{c:'body', l:0.2}` éclaircie, `{d:0.3}` assombrie, `{a:0.5}` transparence ;
// une chaîne '#RRGGBB' est une couleur fixe. Dégradé : `{grad:{from:[x,y], to:[x,y], stops:[[0,F],[1,F]]}}`.
'use strict';

const BODY = 'body';
const WHITE = '#FFFFFF';
const BLACK = '#000000';

const tone = (c, o = {}) => ({ c, ...o });
const body = (o = {}) => tone(BODY, o);
const lat = (l = 0.3, d = 0.22, x0 = -0.4, x1 = 0.4) => ({
  grad: { from: [x0, 0], to: [x1, 0], stops: [[0, body({ l })], [0.3, body({ l: 0.08 })], [0.75, body()], [1, body({ d })]] },
});
const vert = (y0, y1, top, bottom) => ({ grad: { from: [0, y0], to: [0, y1], stops: [[0, top], [1, bottom]] } });
const glass = (y0, y1) => vert(y0, y1, '#7FA3D6', '#1E3050');
const shine = (a = 0.35) => tone(WHITE, { a });
const shade = (a = 0.25) => tone(BLACK, { a });

// ---- primitives
const rr = (x, y, w, h, r, fill, o = {}) => ({ t: 'rr', x, y, w, h, r, fill, ...o });
const el = (cx, cy, rx, ry, fill, o = {}) => ({ t: 'el', cx, cy, rx, ry, fill, ...o });
const ci = (cx, cy, r, fill, o = {}) => ({ t: 'ci', cx, cy, r, fill, ...o });
const path = (d, fill, o = {}) => ({ t: 'path', d, fill, ...o });
const ln = (x1, y1, x2, y2, stroke, w) => ({ t: 'ln', x1, y1, x2, y2, stroke, w });
const line = (c, w) => ({ stroke: c, sw: w });

// ---- morceaux communs
const TIRE = '#1E2433';
const WIDE = 1.075; // les camions et les bus sont un peu plus larges que les voitures
// Grandes roues trapues : à moitié cachées sous la carrosserie, le moyeu clair dépasse sur le côté.
const wheelAt = (x, y) => [
  rr(x - 0.085, y - 0.19, 0.17, 0.38, 0.08, TIRE),
  ci(x + Math.sign(x) * 0.04, y, 0.026, '#4A5470'),
];
const headlights = (y, spread = 0.27) => [-1, 1].map((k) => rr(k * spread - 0.085, y - 0.045, 0.17, 0.09, 0.045, '#FFFFFF', line(body({ d: 0.4 }), 0.016)));
const taillights = (y, spread = 0.29) => [-1, 1].map((k) => rr(k * spread - 0.07, y - 0.03, 0.14, 0.06, 0.025, '#E5352B'));
const bumperFront = (y, w = 0.5) => rr(-w / 2, y, w, 0.05, 0.025, tone('#F4F6FA', { a: 0.95 }));
const bumperRear = (y, w = 0.5) => rr(-w / 2, y, w, 0.045, 0.02, '#2A2D3D');
// Un pare-brise est plus large côté capot (yTop, bombé vers l'avant) que côté toit (yBot) : les montants se rapprochent.
const windshield = (yTop, yBot, wRoof = 0.27, wHood = 0.32, bulge = 0.07) => [
  path('M ' + (-wHood) + ',' + yTop + ' Q 0,' + (yTop - bulge) + ' ' + wHood + ',' + yTop + ' L ' + wRoof + ',' + yBot + ' L ' + (-wRoof) + ',' + yBot + ' Z', glass(yTop - bulge, yBot), line(body({ d: 0.45 }), 0.016)),
  path('M ' + (-wHood + 0.06) + ',' + (yTop + 0.01) + ' L ' + (-wHood + 0.17) + ',' + (yTop - 0.01) + ' L ' + (-wRoof + 0.13) + ',' + (yBot - 0.015) + ' L ' + (-wRoof + 0.03) + ',' + (yBot - 0.015) + ' Z', shine(0.34)),
];
const rearWindow = (yTop, yBot, wTop = 0.25, wBot = 0.21) => [
  path('M ' + (-wTop) + ',' + yTop + ' L ' + wTop + ',' + yTop + ' L ' + wBot + ',' + yBot + ' Q 0,' + (yBot + 0.05) + ' ' + (-wBot) + ',' + yBot + ' Z', vert(yTop, yBot + 0.05, body({ d: 0.42 }), body({ d: 0.6 })), line(body({ d: 0.55 }), 0.016)),
];
// L'habitacle : un toit plus clair que le capot, avec un liseré plus sombre.
const roof = (x, y, w, h, r) => [
  rr(x, y, w, h, r, vert(y, y + h, body({ l: 0.22 }), body({ l: 0.08 })), line(body({ d: 0.3 }), 0.022)),
  rr(x + 0.05, y + 0.05, w * 0.3, h - 0.1, r * 0.6, shine(0.18)),
];
// Liseré lumineux en haut à gauche, trait sombre en bas à droite : l'effet « plastique brillant » autour de toute la silhouette.
const rim = (d, y0, y1) => [path(d, 'none', line(body({ d: 0.5 }), 0.022)), path(d, 'none', {
  stroke: { grad: { from: [-0.4, y0], to: [0.32, y1], stops: [[0, tone(WHITE, { a: 0.7 })], [0.4, tone(WHITE, { a: 0 })], [0.62, tone(BLACK, { a: 0 })], [1, tone(BLACK, { a: 0.4 })]] } },
  sw: 0.03,
})];

// ---- les modèles
const models = {};

const SEDAN = 'M -0.30,-0.90 C -0.18,-0.925 0.18,-0.925 0.30,-0.90 C 0.38,-0.86 0.40,-0.70 0.40,-0.52 L 0.40,0.55 C 0.40,0.78 0.36,0.88 0.28,0.90 C 0.15,0.93 -0.15,0.93 -0.28,0.90 C -0.36,0.88 -0.40,0.78 -0.40,0.55 L -0.40,-0.52 C -0.40,-0.70 -0.38,-0.86 -0.30,-0.90 Z';
// Berline (2 cases) : capot long, grand pare-brise, petit coffre
models.sedan = {
  len: 2,
  silhouette: SEDAN,
  wheels: [[-0.36, -0.5], [0.36, -0.5], [-0.36, 0.52], [0.36, 0.52]],
  parts: () => [
    path(SEDAN, lat()),
    el(-0.13, -0.7, 0.15, 0.09, shine(0.3)),
    ...windshield(-0.40, -0.08, 0.27, 0.32, 0.07),
    ...roof(-0.29, -0.10, 0.58, 0.46, 0.15),
    ...rearWindow(0.41, 0.53, 0.25, 0.21),
    bumperFront(-0.915),
    ...headlights(-0.83),
    ...taillights(0.84),
    ...rim(SEDAN, -0.9, 0.9),
  ],
};

const HATCH = 'M -0.31,-0.90 C -0.18,-0.925 0.18,-0.925 0.31,-0.90 C 0.38,-0.86 0.40,-0.70 0.40,-0.54 L 0.40,0.62 C 0.40,0.80 0.37,0.88 0.30,0.90 C 0.15,0.915 -0.15,0.915 -0.30,0.90 C -0.37,0.88 -0.40,0.80 -0.40,0.62 L -0.40,-0.54 C -0.40,-0.70 -0.38,-0.86 -0.31,-0.90 Z';
// Citadine (2 cases) : museau court, grand toit, hayon avec petit aileron
models.hatch = {
  len: 2,
  silhouette: HATCH,
  wheels: [[-0.36, -0.5], [0.36, -0.5], [-0.36, 0.5], [0.36, 0.5]],
  parts: () => [
    path(HATCH, lat()),
    el(-0.12, -0.7, 0.14, 0.085, shine(0.3)),
    ...windshield(-0.46, -0.14, 0.3, 0.34, 0.08),
    ...roof(-0.31, -0.16, 0.62, 0.66, 0.16),
    ...rearWindow(0.56, 0.68, 0.27, 0.23),
    rr(-0.3, 0.76, 0.6, 0.07, 0.035, body({ d: 0.35 })),
    bumperFront(-0.915),
    ...headlights(-0.83, 0.28),
    ...taillights(0.86, 0.3),
    ...rim(HATCH, -0.9, 0.9),
  ],
};

const SPORT = 'M -0.24,-0.90 C -0.12,-0.93 0.12,-0.93 0.24,-0.90 C 0.34,-0.84 0.40,-0.66 0.40,-0.44 L 0.40,0.48 C 0.40,0.74 0.37,0.88 0.30,0.90 C 0.15,0.93 -0.15,0.93 -0.30,0.90 C -0.37,0.88 -0.40,0.74 -0.40,0.48 L -0.40,-0.44 C -0.40,-0.66 -0.34,-0.84 -0.24,-0.90 Z';
// Voiture de sport (2 cases) : bandes blanches, petit habitacle, grand aileron
models.sport = {
  len: 2,
  silhouette: SPORT,
  wheels: [[-0.36, -0.52], [0.36, -0.52], [-0.36, 0.52], [0.36, 0.52]],
  parts: () => [
    path(SPORT, lat(0.34, 0.26)),
    rr(-0.17, -0.92, 0.08, 1.8, 0.02, tone(WHITE, { a: 0.9 })),
    rr(0.09, -0.92, 0.08, 1.8, 0.02, tone(WHITE, { a: 0.9 })),
    ...windshield(-0.26, -0.03, 0.27, 0.31, 0.07),
    rr(-0.26, -0.04, 0.52, 0.4, 0.14, vert(-0.04, 0.36, body({ l: 0.22 }), body({ l: 0.04 })), line(body({ d: 0.35 }), 0.022)),
    rr(-0.2, 0.0, 0.13, 0.3, 0.06, shine(0.2)),
    ...rearWindow(0.37, 0.49, 0.23, 0.2),
    rr(-0.38, 0.7, 0.76, 0.11, 0.045, body({ d: 0.5 })),
    ...headlights(-0.82, 0.25),
    ...taillights(0.88, 0.3),
    ...rim(SPORT, -0.9, 0.9),
  ],
};

const PICKUP_CAB = 'M -0.30,-1.40 C -0.18,-1.425 0.18,-1.425 0.30,-1.40 C 0.38,-1.36 0.40,-1.2 0.40,-1.0 L 0.40,-0.18 L -0.40,-0.18 L -0.40,-1.0 C -0.40,-1.2 -0.38,-1.36 -0.30,-1.40 Z';
// Pick-up (3 cases) : cabine devant, benne ouverte avec une caisse
models.pickup = {
  len: 3,
  silhouette: 'M -0.30,-1.40 C -0.18,-1.425 0.18,-1.425 0.30,-1.40 C 0.38,-1.36 0.40,-1.2 0.40,-1.0 L 0.40,1.3 C 0.40,1.38 0.37,1.4 0.30,1.4 L -0.30,1.4 C -0.37,1.4 -0.40,1.38 -0.40,1.3 L -0.40,-1.0 C -0.40,-1.2 -0.38,-1.36 -0.30,-1.40 Z',
  wheels: [[-0.36, -0.95], [0.36, -0.95], [-0.36, 0.9], [0.36, 0.9]],
  parts: () => [
    path(PICKUP_CAB, lat()),
    el(-0.13, -1.2, 0.15, 0.1, shine(0.3)),
    ...windshield(-0.84, -0.52, 0.29, 0.32, 0.08),
    ...roof(-0.3, -0.54, 0.6, 0.34, 0.12),
    rr(-0.4, -0.2, 0.8, 1.6, 0.09, lat(0.22, 0.24), line(body({ d: 0.3 }), 0.018)),
    rr(-0.3, -0.09, 0.6, 1.38, 0.05, vert(-0.09, 1.29, '#3A405A', '#262B40')),
    rr(-0.2, 0.12, 0.34, 0.36, 0.05, '#E2B878', line('#8C6535', 0.02)),
    rr(-0.05, 0.62, 0.26, 0.26, 0.045, '#C99A5C', line('#8C6535', 0.02)),
    rr(-0.4, 1.33, 0.8, 0.07, 0.035, body({ d: 0.35 })),
    bumperFront(-1.415),
    ...headlights(-1.33, 0.28),
    ...taillights(1.36, 0.3),
    ...rim('M -0.30,-1.40 C -0.18,-1.425 0.18,-1.425 0.30,-1.40 C 0.38,-1.36 0.40,-1.2 0.40,-1.0 L 0.40,1.3 C 0.40,1.38 0.37,1.4 0.30,1.4 L -0.30,1.4 C -0.37,1.4 -0.40,1.38 -0.40,1.3 L -0.40,-1.0 C -0.40,-1.2 -0.38,-1.36 -0.30,-1.40 Z', -1.4, 1.4),
  ],
};

const BOX = 'M -0.30,-1.40 C -0.18,-1.425 0.18,-1.425 0.30,-1.40 C 0.38,-1.36 0.40,-1.2 0.40,-1.0 L 0.40,1.3 C 0.40,1.38 0.37,1.4 0.30,1.4 L -0.30,1.4 C -0.37,1.4 -0.40,1.38 -0.40,1.3 L -0.40,-1.0 C -0.40,-1.2 -0.38,-1.36 -0.30,-1.40 Z';
// Camion de livraison (3 cases) : cabine courte, grande caisse claire avec une bande de couleur
models.box = {
  len: 3,
  silhouette: BOX,
  wheels: [[-0.36, -1.0], [0.36, -1.0], [-0.36, 0.95], [0.36, 0.95]],
  parts: () => [
    path('M -0.30,-1.40 C -0.18,-1.425 0.18,-1.425 0.30,-1.40 C 0.38,-1.36 0.40,-1.2 0.40,-1.0 L 0.40,-0.5 L -0.40,-0.5 L -0.40,-1.0 C -0.40,-1.2 -0.38,-1.36 -0.30,-1.40 Z', lat()),
    el(-0.13, -1.28, 0.15, 0.08, shine(0.3)),
    ...windshield(-1.22, -0.9, 0.3, 0.34, 0.08),
    ...roof(-0.31, -0.91, 0.62, 0.38, 0.1),
    rr(-0.4, -0.48, 0.8, 1.9, 0.09, { grad: { from: [-0.4, 0], to: [0.4, 0], stops: [[0, body({ l: 0.78 })], [0.35, body({ l: 0.68 })], [0.8, body({ l: 0.56 })], [1, body({ l: 0.4 })]] } }, line(body({ d: 0.3 }), 0.018)),
    rr(-0.15, -0.38, 0.3, 1.72, 0.06, body(), line(body({ d: 0.3 }), 0.016)),
    rr(-0.08, -0.3, 0.16, 1.56, 0.04, shine(0.3)),
    rr(-0.4, -0.5, 0.8, 0.05, 0.025, shade(0.5)),
    bumperFront(-1.415),
    ...headlights(-1.33, 0.28),
    ...taillights(1.37, 0.3),
    ...rim(BOX, -1.4, 1.4),
  ],
};

const VAN = 'M -0.30,-1.40 C -0.18,-1.425 0.18,-1.425 0.30,-1.40 C 0.38,-1.36 0.40,-1.2 0.40,-1.0 L 0.40,1.15 C 0.40,1.32 0.36,1.4 0.28,1.4 C 0.15,1.42 -0.15,1.42 -0.28,1.4 C -0.36,1.4 -0.40,1.32 -0.40,1.15 L -0.40,-1.0 C -0.40,-1.2 -0.38,-1.36 -0.30,-1.40 Z';
// Fourgon de livraison (3 cases) : museau court, grand toit avec une grande trappe
models.van = {
  len: 3,
  silhouette: VAN,
  wheels: [[-0.36, -0.92], [0.36, -0.92], [-0.36, 0.9], [0.36, 0.9]],
  parts: () => [
    path(VAN, lat()),
    el(-0.13, -1.22, 0.15, 0.09, shine(0.3)),
    ...windshield(-1.02, -0.66, 0.3, 0.34, 0.09),
    rr(-0.34, -0.68, 0.68, 1.9, 0.15, vert(-0.68, 1.22, body({ l: 0.2 }), body({ l: 0.06 })), line(body({ d: 0.3 }), 0.022)),
    rr(-0.27, -0.6, 0.12, 1.74, 0.05, shine(0.2)),
    rr(-0.22, -0.35, 0.44, 0.58, 0.09, body({ l: 0.3 }), line(body({ d: 0.3 }), 0.02)),
    rr(-0.14, -0.27, 0.2, 0.42, 0.05, shine(0.22)),
    rr(-0.27, 1.02, 0.54, 0.14, 0.05, glass(1.02, 1.16), line(body({ d: 0.45 }), 0.016)),
    bumperFront(-1.415),
    ...headlights(-1.33, 0.27),
    ...taillights(1.36, 0.29),
    ...rim(VAN, -1.4, 1.4),
  ],
};

const BUS = 'M -0.32,-1.90 C -0.2,-1.925 0.2,-1.925 0.32,-1.90 C 0.38,-1.87 0.40,-1.8 0.40,-1.7 L 0.40,1.7 C 0.40,1.82 0.36,1.9 0.30,1.9 L -0.30,1.9 C -0.36,1.9 -0.40,1.82 -0.40,1.7 L -0.40,-1.7 C -0.40,-1.8 -0.38,-1.87 -0.32,-1.90 Z';
// Autobus (4 cases) : toit de couleur avec deux blocs de climatisation, larges bandes de fenêtres sur les côtés
models.bus = {
  len: 4,
  silhouette: BUS,
  wheels: [[-0.36, -1.25], [0.36, -1.25], [-0.36, 1.2], [0.36, 1.2]],
  parts: () => [
    rr(-0.4, -1.9, 0.8, 3.8, 0.2, lat(0.3, 0.26)),
    // une bande de fenêtres continue sur chaque côté
    rr(-0.4, -1.42, 0.1, 2.92, 0.05, vert(-1.42, 1.5, '#3E5C93', '#27406B'), line(body({ d: 0.5 }), 0.014)),
    rr(0.3, -1.42, 0.1, 2.92, 0.05, vert(-1.42, 1.5, '#3E5C93', '#27406B'), line(body({ d: 0.5 }), 0.014)),
    rr(-0.31, -1.52, 0.62, 3.34, 0.12, vert(-1.52, 1.82, body({ l: 0.2 }), body({ l: 0.04 })), line(body({ d: 0.3 }), 0.022)),
    rr(-0.25, -1.45, 0.14, 3.2, 0.06, shine(0.2)),
    rr(-0.15, -0.9, 0.3, 0.46, 0.07, body({ l: 0.42 }), line(body({ d: 0.3 }), 0.02)),
    rr(-0.15, 0.25, 0.3, 0.46, 0.07, body({ l: 0.42 }), line(body({ d: 0.3 }), 0.02)),
    path('M -0.33,-1.5 Q 0,-1.64 0.33,-1.5 L 0.37,-1.8 Q 0,-1.9 -0.37,-1.8 Z', glass(-1.88, -1.5), line(body({ d: 0.45 }), 0.016)),
    path('M -0.3,-1.58 L -0.15,-1.6 L -0.21,-1.8 L -0.34,-1.78 Z', shine(0.34)),
    rr(-0.28, 1.74, 0.56, 0.1, 0.04, glass(1.74, 1.84)),
    ...headlights(-1.84, 0.27),
    ...taillights(1.87, 0.29),
    ...rim(BUS, -1.9, 1.9),
  ],
};

const TANKER = 'M -0.30,-1.90 C -0.18,-1.925 0.18,-1.925 0.30,-1.90 C 0.38,-1.86 0.40,-1.7 0.40,-1.5 L 0.40,1.85 C 0.40,1.88 0.38,1.9 0.34,1.9 L -0.34,1.9 C -0.38,1.9 -0.40,1.88 -0.40,1.85 L -0.40,-1.5 C -0.40,-1.7 -0.38,-1.86 -0.30,-1.90 Z';
// Camion-citerne (4 cases) : cabine colorée, longue citerne brillante avec une bande de couleur
models.tanker = {
  len: 4,
  silhouette: TANKER,
  wheels: [[-0.36, -1.45], [0.36, -1.45], [-0.36, 1.0], [0.36, 1.0], [-0.36, 1.5], [0.36, 1.5]],
  parts: () => [
    path('M -0.30,-1.90 C -0.18,-1.925 0.18,-1.925 0.30,-1.90 C 0.38,-1.86 0.40,-1.7 0.40,-1.5 L 0.40,-0.98 L -0.40,-0.98 L -0.40,-1.5 C -0.40,-1.7 -0.38,-1.86 -0.30,-1.90 Z', lat()),
    el(-0.13, -1.74, 0.15, 0.08, shine(0.3)),
    ...windshield(-1.66, -1.36, 0.3, 0.34, 0.08),
    ...roof(-0.31, -1.37, 0.62, 0.36, 0.1),
    rr(-0.39, -0.92, 0.78, 2.82, 0.36, { grad: { from: [-0.39, 0], to: [0.39, 0], stops: [[0, '#8C97AC'], [0.2, '#E6EBF4'], [0.4, '#FFFFFF'], [0.7, '#C5CDDC'], [1, '#76819A']] } }, line('#5E6880', 0.02)),
    ...[-0.4, 0.5, 1.4].map((y) => ln(-0.38, y, 0.38, y, tone('#5E6880', { a: 0.45 }), 0.022)),
    rr(-0.39, 0.0, 0.78, 0.32, 0, body()),
    rr(-0.39, 0.0, 0.78, 0.05, 0, body({ d: 0.3 })),
    ci(0, -0.55, 0.14, '#5E6880'),
    ci(0, -0.55, 0.085, '#B4BDCE'),
    ci(0, 1.15, 0.14, '#5E6880'),
    ci(0, 1.15, 0.085, '#B4BDCE'),
    rr(-0.2, -0.4, 0.07, 1.4, 0.035, tone(WHITE, { a: 0.55 })),
    bumperFront(-1.915),
    ...headlights(-1.83, 0.28),
    ...taillights(1.87, 0.28),
    ...rim(TANKER, -1.9, 1.9),
  ],
};

const WIDE_SCALE = WIDE;
const byLen = { 2: ['sedan', 'hatch', 'sport'], 3: ['pickup', 'box', 'van'], 4: ['bus', 'tanker'] };
const order = Object.keys(models);

// ================================================================ rendu SVG
const f2 = (n) => Math.round(n * 1000) / 1000;
const hex = (c) => { const h = c.replace('#', ''); return [0, 2, 4].map((i) => parseInt(h.slice(i, i + 2), 16)); };
const mix = (a, b, t) => { const x = hex(a), y = hex(b); return '#' + x.map((v, i) => Math.round(v + (y[i] - v) * t).toString(16).padStart(2, '0')).join(''); };

function resolve(F, bodyHex) {
  if (typeof F === 'string') return { hex: F === BODY ? bodyHex : F, a: 1 };
  let h = F.c === BODY ? bodyHex : F.c;
  if (F.l) h = mix(h, '#FFFFFF', F.l);
  if (F.d) h = mix(h, '#000000', F.d);
  return { hex: h, a: F.a === undefined ? 1 : F.a };
}

let gradId = 0;
function svgPaint(F, bodyHex, s, defs) {
  if (F === 'none') return 'fill="none"';
  if (F && F.grad) {
    const id = `g${++gradId}`;
    const g = F.grad;
    const stops = g.stops.map(([o, c]) => { const r = resolve(c, bodyHex); return `<stop offset="${o}" stop-color="${r.hex}" stop-opacity="${r.a}"/>`; }).join('');
    defs.push(`<linearGradient id="${id}" gradientUnits="userSpaceOnUse" x1="${f2(g.from[0] * s)}" y1="${f2(g.from[1] * s)}" x2="${f2(g.to[0] * s)}" y2="${f2(g.to[1] * s)}">${stops}</linearGradient>`);
    return `fill="url(#${id})"`;
  }
  const r = resolve(F, bodyHex);
  return `fill="${r.hex}"${r.a < 1 ? ` fill-opacity="${r.a}"` : ''}`;
}
function svgStroke(o, bodyHex, s, defs) {
  if (!o.stroke) return '';
  const sw = f2((o.sw || 0.01) * s);
  if (o.stroke.grad) return ` ${svgPaint(o.stroke, bodyHex, s, defs).replace('fill=', 'stroke=')} stroke-width="${sw}" stroke-linejoin="round" stroke-linecap="round"`;
  const r = resolve(o.stroke, bodyHex);
  return ` stroke="${r.hex}"${r.a < 1 ? ` stroke-opacity="${r.a}"` : ''} stroke-width="${sw}" stroke-linejoin="round" stroke-linecap="round"`;
}
function scalePath(d, s) {
  return d.replace(/-?\d*\.?\d+/g, (n) => f2(parseFloat(n) * s));
}

function opSvg(op, bodyHex, s, defs) {
  switch (op.t) {
    case 'rr':
      return `<rect x="${f2(op.x * s)}" y="${f2(op.y * s)}" width="${f2(op.w * s)}" height="${f2(op.h * s)}" rx="${f2(op.r * s)}" ${svgPaint(op.fill, bodyHex, s, defs)}${svgStroke(op, bodyHex, s, defs)}/>`;
    case 'el':
      return `<ellipse cx="${f2(op.cx * s)}" cy="${f2(op.cy * s)}" rx="${f2(op.rx * s)}" ry="${f2(op.ry * s)}" ${svgPaint(op.fill, bodyHex, s, defs)}${svgStroke(op, bodyHex, s, defs)}/>`;
    case 'ci':
      return `<circle cx="${f2(op.cx * s)}" cy="${f2(op.cy * s)}" r="${f2(op.r * s)}" ${svgPaint(op.fill, bodyHex, s, defs)}${svgStroke(op, bodyHex, s, defs)}/>`;
    case 'path':
      return `<path d="${scalePath(op.d, s)}" ${svgPaint(op.fill, bodyHex, s, defs)}${svgStroke(op, bodyHex, s, defs)}/>`;
    case 'ln': {
      const r = resolve(op.stroke, bodyHex);
      return `<path d="M${f2(op.x1 * s)},${f2(op.y1 * s)}L${f2(op.x2 * s)},${f2(op.y2 * s)}" fill="none" stroke="${r.hex}"${r.a < 1 ? ` stroke-opacity="${r.a}"` : ''} stroke-width="${f2(op.w * s)}" stroke-linecap="round"/>`;
    }
    default:
      throw new Error(`primitive inconnue ${op.t}`);
  }
}

const DEPTH = 0.15;

/** Les deux couches SVG d'un modèle : `lower` (tranche sombre + roues, décalée vers le bas de l'écran) et `upper`. */
function layers(modelName, bodyHex, s) {
  const m = models[modelName];
  const defs = [];
  const wheels = m.wheels.flatMap(([x, y]) => wheelAt(x, y));
  const dark = mix(bodyHex, '#000000', 0.45);
  const lower = wheels.map((o) => opSvg(o, bodyHex, s, defs)).join('') + `<path d="${scalePath(m.silhouette, s)}" fill="${dark}"/>`;
  const upperOps = [...m.wheels.flatMap(([x, y]) => wheelAt(x, y - DEPTH * 0.2)), ...m.parts()];
  const upper = upperOps.map((o) => opSvg(o, bodyHex, s, defs)).join('');
  const wide = m.len >= 3 ? ` transform="scale(${WIDE} 1)"` : '';
  return { lower: `<g${wide}>${lower}</g>`, upper: `<defs>${defs.join('')}</defs><g${wide}>${upper}</g>`, depth: DEPTH * s };
}

// ================================================================ génération Kotlin
const kf = (n) => {
  const v = Math.round(n * 1000) / 1000;
  return Number.isInteger(v) ? `${v}.0f` : `${v}f`;
};
const khex = (h) => `Color(0xFF${h.replace('#', '').toUpperCase()})`;

function kColor(F) {
  if (typeof F === 'string') return F === BODY ? 'body' : khex(F);
  const base = F.c === BODY ? 'body' : khex(F.c);
  const args = [];
  if (F.l) args.push(`l = ${kf(F.l)}`);
  if (F.d) args.push(`d = ${kf(F.d)}`);
  if (F.a !== undefined && F.a !== 1) args.push(`a = ${kf(F.a)}`);
  return args.length ? `tone(${base}, ${args.join(', ')})` : base;
}
function kPaint(F) {
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
function opKotlin(op) {
  const strokeLine = (draw) => (op.stroke ? `\n    ${draw(kPaint(op.stroke), `, style = Stroke(${kf(op.sw || 0.01)} * s, cap = StrokeCap.Round, join = StrokeJoin.Round)`)}` : '');
  switch (op.t) {
    case 'rr': {
      const draw = (paint, extra = '') => `drawRoundRect(${paint}, Offset(${kf(op.x)} * s, ${kf(op.y)} * s), Size(${kf(op.w)} * s, ${kf(op.h)} * s), CornerRadius(${kf(op.r)} * s)${extra})`;
      return `    ${draw(kPaint(op.fill))}${strokeLine(draw)}`;
    }
    case 'el': {
      const draw = (paint, extra = '') => `drawOval(${paint}, Offset(${kf(op.cx - op.rx)} * s, ${kf(op.cy - op.ry)} * s), Size(${kf(op.rx * 2)} * s, ${kf(op.ry * 2)} * s)${extra})`;
      return `    ${draw(kPaint(op.fill))}${strokeLine(draw)}`;
    }
    case 'ci': {
      const draw = (paint, extra = '') => `drawCircle(${paint}, ${kf(op.r)} * s, Offset(${kf(op.cx)} * s, ${kf(op.cy)} * s)${extra})`;
      return `    ${draw(kPaint(op.fill))}${strokeLine(draw)}`;
    }
    case 'path': {
      const p = kPath(op.d);
      const fill = op.fill === 'none' ? '' : `    drawPath(${p}, ${kPaint(op.fill)})`;
      const stroke = op.stroke ? `    drawPath(${p}, ${kPaint(op.stroke)}, style = Stroke(${kf(op.sw || 0.01)} * s, cap = StrokeCap.Round, join = StrokeJoin.Round))` : '';
      return [fill, stroke].filter(Boolean).join('\n');
    }
    case 'ln':
      return `    drawLine(${kColor(op.stroke)}, Offset(${kf(op.x1)} * s, ${kf(op.y1)} * s), Offset(${kf(op.x2)} * s, ${kf(op.y2)} * s), ${kf(op.w)} * s, StrokeCap.Round)`;
    default:
      throw new Error(`primitive inconnue ${op.t}`);
  }
}

const camel = (n) => n.charAt(0).toUpperCase() + n.slice(1);

function kotlin() {
  const names = order;
  const enumEntries = names.map((n) => `    ${n.toUpperCase()}(${models[n].len})`).join(',\n');
  let out = `// GÉNÉRÉ par tools/design/make-vehicles.js : ne pas modifier à la main (la CI vérifie que ce fichier est à jour).
// Les véhicules de Parking Malin, dessinés de dessus, nez vers le haut, unité = \`s\` (côté d'une case en pixels), centrés en (0, 0).
@file:Suppress("ktlint", "UNUSED_PARAMETER", "NAME_SHADOWING")

package com.kcharignon.petitsjeux.games.parking.ui

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

/** Les modèles de véhicules ; [len] = nombre de cases. */
enum class VehicleModel(val len: Int) {
${enumEntries};

    /** Les camions et les bus sont un peu plus larges que les voitures. */
    val widthScale: Float get() = if (len >= 3) ${WIDE}f else 1f

    companion object {
        /** Les modèles de [len] cases. */
        fun ofLength(len: Int): List<VehicleModel> = entries.filter { it.len == len }
    }
}

/** Épaisseur de la « tranche » sombre, en cases. */
const val VEHICLE_DEPTH = ${DEPTH}f

/** Dessous : roues et silhouette sombre (à décaler vers le bas de l'écran, hors rotation). */
fun DrawScope.drawVehicleLower(model: VehicleModel, body: Color, s: Float) {
    when (model) {
${names.map((n) => `        VehicleModel.${n.toUpperCase()} -> lower${camel(n)}(body, s)`).join('\n')}
    }
}

/** Silhouette pleine, d'une seule couleur : sert à rougir tout le véhicule (flash de faute) sans en dénaturer la couleur. */
fun DrawScope.drawVehicleSilhouette(model: VehicleModel, color: Color, s: Float) {
    when (model) {
${names.map((n) => `        VehicleModel.${n.toUpperCase()} -> drawPath(${kPath(models[n].silhouette)}, color)`).join('\n')}
    }
}

/** Dessus : la carrosserie et tous ses détails. */
fun DrawScope.drawVehicleUpper(model: VehicleModel, body: Color, s: Float) {
    when (model) {
${names.map((n) => `        VehicleModel.${n.toUpperCase()} -> upper${camel(n)}(body, s)`).join('\n')}
    }
}
`;
  for (const n of names) {
    const m = models[n];
    const wheelsLower = m.wheels.flatMap(([x, y]) => wheelAt(x, y));
    const wheelsUpper = m.wheels.flatMap(([x, y]) => wheelAt(x, y - DEPTH * 0.2));
    out += `\nprivate fun DrawScope.lower${camel(n)}(body: Color, s: Float) {\n${wheelsLower.map(opKotlin).join('\n')}\n    drawPath(${kPath(m.silhouette)}, tone(body, d = 0.45f))\n}\n`;
    out += `\nprivate fun DrawScope.upper${camel(n)}(body: Color, s: Float) {\n${[...wheelsUpper, ...m.parts()].map(opKotlin).join('\n')}\n}\n`;
  }
  return out;
}

module.exports = { models, byLen, order, layers, kotlin, DEPTH, WIDE, mix };
