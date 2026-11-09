// Génère le code Kotlin des véhicules et la planche d'aperçu : `node tools/design/make-vehicles.js`
// (PNG de la planche : `node tools/design/render.js`, demande Playwright).
'use strict';
const fs = require('fs');
const path = require('path');
const V = require('./vehicles.js');
const { CAR_COLORS } = require('./ds.js');

const root = path.join(__dirname, '..', '..');
const kt = path.join(root, 'android', 'app', 'src', 'main', 'java', 'com', 'kcharignon', 'petitsjeux', 'games', 'parking', 'ui', 'VehicleArt.kt');
fs.writeFileSync(kt, V.kotlin());

// ---- planche : tous les modèles, de dessus, sur le fond du jeu
const colors = Object.values(CAR_COLORS);
const S = 118;
const W = 780;
const rows = [
  { title: 'Voitures (2 cases)', models: V.byLen[2] },
  { title: 'Camions (3 cases)', models: V.byLen[3] },
  { title: 'Bus et poids lourds (4 cases)', models: V.byLen[4] },
];
let y = 70;
let body = '';
let n = 0;
for (const r of rows) {
  body += `<text x="30" y="${y}" font-family="Fredoka, sans-serif" font-size="26" font-weight="700" fill="#2B2D42">${r.title}</text>`;
  y += 24;
  const tallest = Math.max(...r.models.map((m) => V.models[m].len)) * S;
  r.models.forEach((m, i) => {
    const cx = 120 + i * 200;
    const cy = y + tallest / 2 + 10;
    const color = colors[(n++ * 4 + 1) % colors.length];
    const wide = V.models[m].len >= 3 ? V.WIDE : 1;
    const { lower, upper, depth } = V.layers(m, color, S);
    body += `<ellipse cx="${cx}" cy="${cy + S * 0.2}" rx="${S * 0.4 * wide}" ry="${(V.models[m].len * S - S * 0.2) / 2}" fill="#000" opacity="0"/>`;
    body += `<rect x="${cx - S * 0.4 * wide}" y="${cy - (V.models[m].len * S - S * 0.2) / 2 + (V.DEPTH + 0.06) * S}" width="${S * 0.8 * wide}" height="${V.models[m].len * S - S * 0.2}" rx="${S * 0.3}" fill="#000" opacity="0.22"/>`;
    body += `<g transform="translate(${cx} ${cy})"><g transform="translate(0 ${depth})">${lower}</g>${upper}</g>`;
    body += `<text x="${cx}" y="${y + tallest + 52}" text-anchor="middle" font-family="Fredoka, sans-serif" font-size="20" font-weight="600" fill="#5B6079">${m}</text>`;
  });
  y += tallest + 90;
}
const H = y + 20;
const svg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${W} ${H}" width="${W}" height="${H}">
<style>@font-face{font-family:'Fredoka';src:url('../../android/app/src/main/res/font/fredoka.ttf');font-weight:300 700}</style>
<defs><linearGradient id="bg" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="#FFE07A"/><stop offset="0.35" stop-color="#FFC83D"/><stop offset="1" stop-color="#F2A91B"/></linearGradient></defs>
<rect width="${W}" height="${H}" fill="url(#bg)"/>${body}</svg>
`;
fs.writeFileSync(path.join(root, 'docs', 'design', 'vehicules.svg'), svg);
console.log('véhicules écrits :', path.relative(root, kt), 'et docs/design/vehicules.svg (', W, 'x', H, ')');
