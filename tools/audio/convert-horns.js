// Convertit les klaxons Mixkit (WAV 24 bits stéréo) en WAV 16 bits mono, rognés, normalisés, avec un petit fondu :
// `node tools/audio/convert-horns.js <dossier des .wav d'origine>` écrit dans android/app/src/main/res/raw/.
// Les fichiers d'origine ne sont pas dans le dépôt (licence Mixkit : third_party/mixkit/README.md).
'use strict';
const fs = require('fs');
const path = require('path');

const src = process.argv[2];
if (!src) {
  console.error('usage : node tools/audio/convert-horns.js <dossier des .wav Mixkit>');
  process.exit(1);
}
const out = path.join(__dirname, '..', '..', 'android', 'app', 'src', 'main', 'res', 'raw');

// nom d'origine -> nom dans l'appli (res/raw : minuscules et _)
const FILES = {
  'mixkit-small-car-horn-717.wav': 'horn_small',
  'mixkit-car-horn-718.wav': 'horn_car',
  'mixkit-classic-car-horn-1565.wav': 'horn_classic',
  'mixkit-car-double-horn-719.wav': 'horn_double',
};

function readWav(file) {
  const b = fs.readFileSync(file);
  let o = 12;
  let fmt = null;
  let data = null;
  while (o + 8 <= b.length) {
    const id = b.toString('ascii', o, o + 4);
    const size = b.readUInt32LE(o + 4);
    if (id === 'fmt ') fmt = { channels: b.readUInt16LE(o + 10), rate: b.readUInt32LE(o + 12), bits: b.readUInt16LE(o + 22) };
    if (id === 'data') data = b.subarray(o + 8, o + 8 + size);
    o += 8 + size + (size % 2);
  }
  if (!fmt || !data) throw new Error(`WAV illisible : ${file}`);
  const bytes = fmt.bits / 8;
  const frames = Math.floor(data.length / (bytes * fmt.channels));
  const mono = new Float64Array(frames);
  for (let i = 0; i < frames; i++) {
    let sum = 0;
    for (let c = 0; c < fmt.channels; c++) {
      const p = (i * fmt.channels + c) * bytes;
      const v = bytes === 3 ? data.readIntLE(p, 3) / 8388608 : bytes === 2 ? data.readInt16LE(p) / 32768 : data.readInt32LE(p) / 2147483648;
      sum += v;
    }
    mono[i] = sum / fmt.channels;
  }
  return { mono, rate: fmt.rate };
}

for (const [name, target] of Object.entries(FILES)) {
  const { mono, rate } = readWav(path.join(src, name));
  // rogne le silence au début et à la fin (en dessous de -50 dB)
  const gate = 0.003;
  let a = 0;
  let z = mono.length - 1;
  while (a < z && Math.abs(mono[a]) < gate) a++;
  while (z > a && Math.abs(mono[z]) < gate) z--;
  const s = mono.subarray(Math.max(0, a - Math.floor(rate * 0.004)), Math.min(mono.length, z + Math.floor(rate * 0.02)));
  let peak = 0;
  for (const v of s) peak = Math.max(peak, Math.abs(v));
  const gain = 0.9 / peak;
  const fadeIn = Math.floor(rate * 0.006);
  const fadeOut = Math.floor(rate * 0.03);
  const pcm = Buffer.alloc(s.length * 2);
  for (let i = 0; i < s.length; i++) {
    const g = Math.min(1, i / fadeIn) * Math.min(1, (s.length - i) / fadeOut);
    pcm.writeInt16LE(Math.max(-32768, Math.min(32767, Math.round(s[i] * gain * g * 32767))), i * 2);
  }
  const head = Buffer.alloc(44);
  head.write('RIFF', 0); head.writeUInt32LE(36 + pcm.length, 4); head.write('WAVE', 8); head.write('fmt ', 12);
  head.writeUInt32LE(16, 16); head.writeUInt16LE(1, 20); head.writeUInt16LE(1, 22); head.writeUInt32LE(rate, 24);
  head.writeUInt32LE(rate * 2, 28); head.writeUInt16LE(2, 32); head.writeUInt16LE(16, 34); head.write('data', 36); head.writeUInt32LE(pcm.length, 40);
  fs.writeFileSync(path.join(out, target + '.wav'), Buffer.concat([head, pcm]));
  console.log(target, (s.length / rate).toFixed(2) + ' s', rate + ' Hz', 'mono 16 bits', (pcm.length / 1024).toFixed(0) + ' Ko');
}
