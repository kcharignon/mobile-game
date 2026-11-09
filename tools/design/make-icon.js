// Génère l'icône de l'appli : un VectorDrawable Android (premier plan de l'icône adaptative) et son aperçu SVG.
// `node tools/design/make-icon.js` écrit android/app/src/main/res/drawable/ic_launcher_foreground.xml et docs/design/icon.svg.
// La voiture reprend la géométrie de tools/design/ds.js (carLayers) en aplats : un VectorDrawable n'a pas besoin de dégradés.
'use strict';
const fs = require('fs');
const path = require('path');
const { T, CAR_COLORS, lighten, darken } = require('./ds.js');

const f = (n) => Math.round(n * 100) / 100;

// ---- formes en « pathData » (valable en SVG et en VectorDrawable)
const rrect = (x, y, w, h, r) =>
  `M${f(x + r)},${f(y)}H${f(x + w - r)}A${f(r)},${f(r)} 0 0 1 ${f(x + w)},${f(y + r)}V${f(y + h - r)}A${f(r)},${f(r)} 0 0 1 ${f(x + w - r)},${f(y + h)}H${f(x + r)}A${f(r)},${f(r)} 0 0 1 ${f(x)},${f(y + h - r)}V${f(y + r)}A${f(r)},${f(r)} 0 0 1 ${f(x + r)},${f(y)}Z`;
const circle = (cx, cy, r) => `M${f(cx - r)},${f(cy)}A${f(r)},${f(r)} 0 1 1 ${f(cx + r)},${f(cy)}A${f(r)},${f(r)} 0 1 1 ${f(cx - r)},${f(cy)}Z`;

// ---- la voiture, centrée en (0,0), nez vers le haut, `s` = côté d'une case
function carShapes(color, s, len = 2) {
  const W = s * 0.8;
  const L = s * len - s * 0.2;
  const r = s * 0.27;
  const wy = L / 2 - s * 0.34;
  const wheels = [];
  for (const x of [-W / 2, W / 2]) for (const y of [-wy, wy]) wheels.push({ d: rrect(x - s * 0.07, y - s * 0.15, s * 0.14, s * 0.3, s * 0.06), fill: '#262938' });
  const cabinLen = L * 0.46;
  const cabinW = W * 0.74;
  const cabinY = L * 0.06;
  const top = cabinY - cabinLen / 2;
  const lower = [...wheels, { d: rrect(-W / 2, -L / 2, W, L, r), fill: darken(color, 0.38) }];
  const upper = [
    ...wheels.map((w) => ({ ...w })),
    { d: rrect(-W / 2, -L / 2, W, L, r), fill: color },
    { d: rrect(-W * 0.4, -L / 2 + s * 0.06, W * 0.4, s * 0.14, s * 0.07), fill: '#66FFFFFF' },
    { d: rrect(-cabinW / 2, top, cabinW, cabinLen, s * 0.17), fill: lighten(color, 0.14) },
    {
      d: `M${f(-cabinW / 2 + s * 0.06)},${f(top + s * 0.2)}Q0,${f(top - s * 0.02)} ${f(cabinW / 2 - s * 0.06)},${f(top + s * 0.2)}L${f(cabinW / 2 - s * 0.1)},${f(top + s * 0.42)}L${f(-cabinW / 2 + s * 0.1)},${f(top + s * 0.42)}Z`,
      fill: T.glass,
    },
    { d: rrect(-cabinW / 2 + s * 0.14, top + s * 0.52, cabinW - s * 0.28, cabinLen - s * 0.84, s * 0.06), fill: '#80FFFFFF' },
    { d: rrect(-cabinW / 2 + s * 0.09, cabinY + cabinLen / 2 - s * 0.26, cabinW - s * 0.18, s * 0.14, s * 0.05), fill: T.glass },
    { d: circle(-W * 0.3, -L / 2 + s * 0.1, s * 0.075), fill: '#FFF6B8' },
    { d: circle(W * 0.3, -L / 2 + s * 0.1, s * 0.075), fill: '#FFF6B8' },
    { d: rrect(-W * 0.4, L / 2 - s * 0.13, s * 0.17, s * 0.07, s * 0.03), fill: '#E8352B' },
    { d: rrect(W * 0.4 - s * 0.17, L / 2 - s * 0.13, s * 0.17, s * 0.07, s * 0.03), fill: '#E8352B' },
  ];
  return { lower, upper, depth: s * 0.11 };
}

// Dans l'icône adaptative (108 x 108), tout doit tenir dans le disque central de 66 : la voiture est inclinée comme sur l'icône de référence.
const S = 27;
const ANGLE = 35;
const main = carShapes(CAR_COLORS.rose, S);
const back = carShapes(CAR_COLORS.cyan, S * 0.8);

function xmlPath({ d, fill }) {
  const color = fill.length === 9 ? fill : '#FF' + fill.slice(1).toUpperCase();
  const argb = fill.length === 9 ? '#' + fill.slice(1, 3).toUpperCase() + fill.slice(3).toUpperCase() : color;
  return `        <path android:fillColor="${argb}" android:pathData="${d}" />`;
}
function xmlCar(shapes, cx, cy, angle) {
  return [
    `    <group android:translateX="${cx}" android:translateY="${cy}">`,
    `      <group android:translateY="${f(shapes.depth)}">`,
    `        <group android:rotation="${angle}">`,
    ...shapes.lower.map((s) => '  ' + xmlPath(s)),
    '        </group>',
    '      </group>',
    `      <group android:rotation="${angle}">`,
    ...shapes.upper.map(xmlPath),
    '      </group>',
    '    </group>',
  ].join('\n');
}

const vd = `<?xml version="1.0" encoding="utf-8"?>
<!-- Généré par tools/design/make-icon.js : ne pas modifier à la main. -->
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
${xmlCar(back, 42, 43, ANGLE)}
${xmlCar(main, 62, 61, ANGLE)}
</vector>
`;
fs.writeFileSync(path.join(__dirname, '..', '..', 'android', 'app', 'src', 'main', 'res', 'drawable', 'ic_launcher_foreground.xml'), vd);

// ---- aperçu SVG (même dessin, fond soleil, masque rond)
function svgCar(shapes, cx, cy, angle) {
  const p = (s) => `<path d="${s.d}" fill="${s.fill.length === 9 ? '#' + s.fill.slice(3) : s.fill}" ${s.fill.length === 9 ? `fill-opacity="${f(parseInt(s.fill.slice(1, 3), 16) / 255)}"` : ''}/>`;
  return `<g transform="translate(${cx} ${cy})"><g transform="translate(0 ${f(shapes.depth)})"><g transform="rotate(${angle})">${shapes.lower.map(p).join('')}</g></g><g transform="rotate(${angle})">${shapes.upper.map(p).join('')}</g></g>`;
}
const svg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 108 108" width="432" height="432">
<defs><clipPath id="m"><circle cx="54" cy="54" r="54"/></clipPath></defs>
<g clip-path="url(#m)"><rect width="108" height="108" fill="${T.sun}"/>
<circle cx="86" cy="22" r="26" fill="#fff" opacity="0.12"/><circle cx="18" cy="92" r="30" fill="#fff" opacity="0.1"/>
${svgCar(back, 42, 43, ANGLE)}${svgCar(main, 62, 61, ANGLE)}</g></svg>
`;
fs.writeFileSync(path.join(__dirname, '..', '..', 'docs', 'design', 'icon.svg'), svg);
console.log('icône écrite');
