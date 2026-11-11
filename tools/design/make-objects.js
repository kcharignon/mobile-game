// Génère le code Kotlin des objets de Cherche et Trouve et leur planche d'aperçu : `node tools/design/make-objects.js`
// (PNG de la planche : `node tools/design/render.js`, demande Playwright). La sortie est déterministe : la CI relance ce script
// et vérifie qu'il ne change rien (git diff --exit-code).
'use strict';
const fs = require('fs');
const path = require('path');
const O = require('./objects.js');
const { HUES } = require('../find/catalog.js');

const root = path.join(__dirname, '..', '..');
const kt = path.join(root, 'android', 'app', 'src', 'main', 'java', 'com', 'kcharignon', 'petitsjeux', 'games', 'find', 'ui', 'FindArt.kt');
fs.mkdirSync(path.dirname(kt), { recursive: true });
fs.writeFileSync(kt, O.kotlin());

// ---- planche : 8 colonnes x 5 lignes, les objets dans l'ordre du catalogue (donc regroupés par couleur), sur un fond pastel clair
const COLS = 8;
const CELL_W = 150;
const CELL_H = 184;
const S = 124; // côté du carré de chaque objet
const M = 30;
const TOP = 78;
const W = M * 2 + COLS * CELL_W;
const rows = Math.ceil(O.objects.length / COLS);
const H = TOP + rows * CELL_H + M - 8;

let body = `<text x="${M}" y="50" font-family="Fredoka, sans-serif" font-size="30" font-weight="700" fill="#2B2D42">Cherche et Trouve : les ${O.objects.length} objets</text>`;
O.objects.forEach((o, i) => {
  const col = i % COLS;
  const row = Math.floor(i / COLS);
  const x = M + col * CELL_W;
  const y = TOP + row * CELL_H;
  const cx = x + CELL_W / 2;
  const cy = y + 8 + S / 2 + 4;
  body += `<rect x="${x + 4}" y="${y}" width="${CELL_W - 8}" height="${CELL_H - 12}" rx="26" fill="#FFFFFF" fill-opacity="0.55"/>`;
  body += `<g transform="translate(${cx} ${cy})">${O.svgObject(o, S)}</g>`;
  body += `<text x="${cx}" y="${y + S + 40}" text-anchor="middle" font-family="Fredoka, sans-serif" font-size="${o.label.length > 20 ? 12 : o.label.length > 18 ? 14 : 16}" font-weight="600" fill="#5B6079">${o.label}</text>`;
});

const svg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${W} ${H}" width="${W}" height="${H}">
<style>@font-face{font-family:'Fredoka';src:url('../../android/app/src/main/res/font/fredoka.ttf');font-weight:300 700}</style>
<defs><linearGradient id="bg" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="#F1ECFF"/><stop offset="1" stop-color="#E2EEFF"/></linearGradient></defs>
<rect width="${W}" height="${H}" fill="url(#bg)"/>${body}</svg>
`;
fs.writeFileSync(path.join(root, 'docs', 'design', 'objets.svg'), svg);
console.log('objets écrits :', path.relative(root, kt), 'et docs/design/objets.svg (', W, 'x', H, ') ;', HUES.length, 'familles de couleurs');
