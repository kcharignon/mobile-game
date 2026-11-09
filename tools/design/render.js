// Rend les maquettes SVG de docs/design en PNG (2x) avec Chromium sans interface : `node tools/design/render.js [dossier]`.
// Demande Playwright (non installé dans ce dépôt : outil de développement local). Chemin du module : PLAYWRIGHT_MODULE, sinon `playwright` du PATH de Node.
'use strict';
const fs = require('fs');
const path = require('path');

const dir = path.resolve(process.argv[2] || path.join(__dirname, '..', '..', 'docs', 'design'));
const { chromium } = require(process.env.PLAYWRIGHT_MODULE || 'playwright');

(async () => {
  const browser = await chromium.launch();
  for (const f of fs.readdirSync(dir).filter((n) => n.endsWith('.svg')).sort()) {
    // chaque maquette est rendue à sa taille : écrans de téléphone (390 de large) en 2x, planches plus larges en 1x
    const head = fs.readFileSync(path.join(dir, f), 'utf8').slice(0, 400);
    const width = Number((head.match(/width="(\d+)"/) || [])[1]) || 390;
    const height = Number((head.match(/height="(\d+)"/) || [])[1]) || 844;
    const page = await browser.newPage({ viewport: { width, height }, deviceScaleFactor: width <= 432 ? 2 : 1 });
    await page.goto('file://' + path.join(dir, f));
    await page.evaluate(() => document.fonts.ready);
    await page.waitForTimeout(150);
    const out = path.join(dir, f.replace(/\.svg$/, '.png'));
    await page.screenshot({ path: out });
    console.log('→', path.basename(out));
    await page.close();
  }
  await browser.close();
})();
