package com.kcharignon.petitsjeux.core

/** Un son prêt à jouer : des échantillons 16 bits mono, à [rate] Hz. */
class Sound(val pcm: ShortArray, val rate: Int) {
    val seconds: Double get() = pcm.size.toDouble() / rate
}

/** Lecture d'un fichier WAV très simple : PCM 16 bits, mono (c'est ce que produit tools/audio/convert-horns.js). */
object Wav {
    fun parse(bytes: ByteArray): Sound {
        fun u16(o: Int) = (bytes[o].toInt() and 0xFF) or ((bytes[o + 1].toInt() and 0xFF) shl 8)
        fun u32(o: Int) = u16(o).toLong() or (u16(o + 2).toLong() shl 16)
        require(bytes.size > 44 && String(bytes, 0, 4) == "RIFF" && String(bytes, 8, 4) == "WAVE") { "pas un fichier WAV" }
        var offset = 12
        var rate = 0
        while (offset + 8 <= bytes.size) {
            val id = String(bytes, offset, 4)
            val size = u32(offset + 4).toInt()
            when (id) {
                "fmt " -> {
                    require(u16(offset + 8) == 1) { "WAV non PCM" }
                    require(u16(offset + 10) == 1) { "WAV non mono" }
                    require(u16(offset + 22) == 16) { "WAV non 16 bits" }
                    rate = u32(offset + 12).toInt()
                }
                "data" -> {
                    require(rate > 0) { "WAV sans en-tête fmt avant les données" }
                    val n = minOf(size, bytes.size - offset - 8) / 2
                    return Sound(ShortArray(n) { i -> u16(offset + 8 + 2 * i).toShort() }, rate)
                }
            }
            offset += 8 + size + (size % 2)
        }
        error("WAV sans données")
    }
}
