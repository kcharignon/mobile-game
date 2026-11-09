// Rend les maquettes SVG de docs/design en PNG (2x) avec Chromium sans interface : `node tools/design/render.js [dossier]`.
// Demande Playwright (non installé dans ce dépôt : outil de développement local). Chemin du module : PLAYWRIGHT_MODULE, sinon `playwright` du PATH de Node.
'use strict';
const fs = require('fs');
const path = require('path');

const dir = path.resolve(process.argv[2] || path.join(__dirname, '..', '..', 'docs', 'design'));
const { chromium } = require(process.env.PLAYWRIGHT_MODULE || 'playwright');

(async () => {
  const browser = await chromium.launch();
  const page = await browser.newPage({ viewport: { width: 390, height: 844 }, deviceScaleFactor: 2 });
  for (const f of fs.readdirSync(dir).filter((n) => n.endsWith('.svg')).sort()) {
    await page.goto('file://' + path.join(dir, f));
    await page.evaluate(() => document.fonts.ready);
    await page.waitForTimeout(150);
    const out = path.join(dir, f.replace(/\.svg$/, '.png'));
    await page.screenshot({ path: out });
    console.log('→', path.basename(out));
  }
  await browser.close();
})();
