// GÉNÉRÉ par tools/design/make-objects.js : ne pas modifier à la main (la CI vérifie que ce fichier est à jour).
// Les 40 objets de Cherche et Trouve, dessinés de face ou de côté, centrés en (0, 0), dans le carré de côté `s` (en pixels). Style plat, contour encre.
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

/** Les familles de couleurs des objets (dans l'ordre du catalogue `tools/find/catalog.js`). */
enum class FindHue { ROUGE, ORANGE, JAUNE, VERT, BLEU, VIOLET, ROSE, MARRON, NEUTRE }

/** Les sortes d'objets (dans l'ordre du catalogue). */
enum class FindKind { NOURRITURE, ANIMAL, TRANSPORT, NATURE, OBJET }

/** Les 40 objets : identifiant (écrit dans find_levels.json), nom (« une pomme »), famille de couleur, sorte. */
enum class FindObject(val id: String, val label: String, val hue: FindHue, val kind: FindKind) {
    POMME("pomme", "une pomme", FindHue.ROUGE, FindKind.NOURRITURE),
    FRAISE("fraise", "une fraise", FindHue.ROUGE, FindKind.NOURRITURE),
    COEUR("coeur", "un cœur", FindHue.ROUGE, FindKind.OBJET),
    VOITURE("voiture", "une voiture", FindHue.ROUGE, FindKind.TRANSPORT),
    CHAMPIGNON("champignon", "un champignon", FindHue.ROUGE, FindKind.NATURE),
    CERISES("cerises", "des cerises", FindHue.ROUGE, FindKind.NOURRITURE),
    CAROTTE("carotte", "une carotte", FindHue.ORANGE, FindKind.NOURRITURE),
    ORANGE("orange", "une orange", FindHue.ORANGE, FindKind.NOURRITURE),
    CITROUILLE("citrouille", "une citrouille", FindHue.ORANGE, FindKind.NOURRITURE),
    POISSON("poisson", "un poisson", FindHue.ORANGE, FindKind.ANIMAL),
    RENARD("renard", "un renard", FindHue.ORANGE, FindKind.ANIMAL),
    BANANE("banane", "une banane", FindHue.JAUNE, FindKind.NOURRITURE),
    ETOILE("etoile", "une étoile", FindHue.JAUNE, FindKind.NATURE),
    SOLEIL("soleil", "un soleil", FindHue.JAUNE, FindKind.NATURE),
    POUSSIN("poussin", "un poussin", FindHue.JAUNE, FindKind.ANIMAL),
    FROMAGE("fromage", "un fromage", FindHue.JAUNE, FindKind.NOURRITURE),
    GRENOUILLE("grenouille", "une grenouille", FindHue.VERT, FindKind.ANIMAL),
    SAPIN("sapin", "un sapin", FindHue.VERT, FindKind.NATURE),
    POIRE("poire", "une poire", FindHue.VERT, FindKind.NOURRITURE),
    TORTUE("tortue", "une tortue", FindHue.VERT, FindKind.ANIMAL),
    CACTUS("cactus", "un cactus", FindHue.VERT, FindKind.NATURE),
    BALEINE("baleine", "une baleine", FindHue.BLEU, FindKind.ANIMAL),
    PARAPLUIE("parapluie", "un parapluie", FindHue.BLEU, FindKind.OBJET),
    GOUTTE("goutte", "une goutte d’eau", FindHue.BLEU, FindKind.NATURE),
    BATEAU("bateau", "un bateau", FindHue.BLEU, FindKind.TRANSPORT),
    BALLON("ballon", "un ballon", FindHue.BLEU, FindKind.OBJET),
    RAISIN("raisin", "du raisin", FindHue.VIOLET, FindKind.NOURRITURE),
    AUBERGINE("aubergine", "une aubergine", FindHue.VIOLET, FindKind.NOURRITURE),
    PAPILLON("papillon", "un papillon", FindHue.VIOLET, FindKind.ANIMAL),
    COCHON("cochon", "un cochon", FindHue.ROSE, FindKind.ANIMAL),
    GLACE("glace", "une glace", FindHue.ROSE, FindKind.NOURRITURE),
    CUPCAKE("cupcake", "un petit gâteau", FindHue.ROSE, FindKind.NOURRITURE),
    OURS("ours", "un ours", FindHue.MARRON, FindKind.ANIMAL),
    COOKIE("cookie", "un biscuit", FindHue.MARRON, FindKind.NOURRITURE),
    MAISON("maison", "une maison", FindHue.MARRON, FindKind.OBJET),
    CHOCOLAT("chocolat", "du chocolat", FindHue.MARRON, FindKind.NOURRITURE),
    NUAGE("nuage", "un nuage", FindHue.NEUTRE, FindKind.NATURE),
    FANTOME("fantome", "un fantôme", FindHue.NEUTRE, FindKind.OBJET),
    BONHOMME("bonhomme", "un bonhomme de neige", FindHue.NEUTRE, FindKind.OBJET),
    ELEPHANT("elephant", "un éléphant", FindHue.NEUTRE, FindKind.ANIMAL);

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
        FindObject.POMME -> artPomme(s, sil)
        FindObject.FRAISE -> artFraise(s, sil)
        FindObject.COEUR -> artCoeur(s, sil)
        FindObject.VOITURE -> artVoiture(s, sil)
        FindObject.CHAMPIGNON -> artChampignon(s, sil)
        FindObject.CERISES -> artCerises(s, sil)
        FindObject.CAROTTE -> artCarotte(s, sil)
        FindObject.ORANGE -> artOrange(s, sil)
        FindObject.CITROUILLE -> artCitrouille(s, sil)
        FindObject.POISSON -> artPoisson(s, sil)
        FindObject.RENARD -> artRenard(s, sil)
        FindObject.BANANE -> artBanane(s, sil)
        FindObject.ETOILE -> artEtoile(s, sil)
        FindObject.SOLEIL -> artSoleil(s, sil)
        FindObject.POUSSIN -> artPoussin(s, sil)
        FindObject.FROMAGE -> artFromage(s, sil)
        FindObject.GRENOUILLE -> artGrenouille(s, sil)
        FindObject.SAPIN -> artSapin(s, sil)
        FindObject.POIRE -> artPoire(s, sil)
        FindObject.TORTUE -> artTortue(s, sil)
        FindObject.CACTUS -> artCactus(s, sil)
        FindObject.BALEINE -> artBaleine(s, sil)
        FindObject.PARAPLUIE -> artParapluie(s, sil)
        FindObject.GOUTTE -> artGoutte(s, sil)
        FindObject.BATEAU -> artBateau(s, sil)
        FindObject.BALLON -> artBallon(s, sil)
        FindObject.RAISIN -> artRaisin(s, sil)
        FindObject.AUBERGINE -> artAubergine(s, sil)
        FindObject.PAPILLON -> artPapillon(s, sil)
        FindObject.COCHON -> artCochon(s, sil)
        FindObject.GLACE -> artGlace(s, sil)
        FindObject.CUPCAKE -> artCupcake(s, sil)
        FindObject.OURS -> artOurs(s, sil)
        FindObject.COOKIE -> artCookie(s, sil)
        FindObject.MAISON -> artMaison(s, sil)
        FindObject.CHOCOLAT -> artChocolat(s, sil)
        FindObject.NUAGE -> artNuage(s, sil)
        FindObject.FANTOME -> artFantome(s, sil)
        FindObject.BONHOMME -> artBonhomme(s, sil)
        FindObject.ELEPHANT -> artElephant(s, sil)
    }
}

private fun DrawScope.artPomme(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.205f * s); cubicTo(0.065f * s, -0.288f * s, 0.185f * s, -0.307f * s, 0.287f * s, -0.26f * s); cubicTo(0.435f * s, -0.196f * s, 0.462f * s, 0.008f * s, 0.407f * s, 0.211f * s); cubicTo(0.351f * s, 0.378f * s, 0.203f * s, 0.452f * s, 0.093f * s, 0.424f * s); quadraticTo(0.0f * s, 0.396f * s, -0.092f * s, 0.424f * s); cubicTo(-0.203f * s, 0.452f * s, -0.351f * s, 0.378f * s, -0.407f * s, 0.211f * s); cubicTo(-0.462f * s, 0.008f * s, -0.435f * s, -0.196f * s, -0.287f * s, -0.26f * s); cubicTo(-0.185f * s, -0.307f * s, -0.065f * s, -0.288f * s, 0.0f * s, -0.205f * s); close() }
    val p1 = Path().apply { moveTo(-0.023f * s, -0.251f * s); cubicTo(0.042f * s, -0.334f * s, 0.162f * s, -0.353f * s, 0.264f * s, -0.307f * s); cubicTo(0.412f * s, -0.242f * s, 0.439f * s, -0.038f * s, 0.384f * s, 0.165f * s); cubicTo(0.328f * s, 0.331f * s, 0.18f * s, 0.405f * s, 0.069f * s, 0.378f * s); quadraticTo(-0.023f * s, 0.35f * s, -0.116f * s, 0.378f * s); cubicTo(-0.226f * s, 0.405f * s, -0.374f * s, 0.331f * s, -0.43f * s, 0.165f * s); cubicTo(-0.485f * s, -0.038f * s, -0.458f * s, -0.242f * s, -0.31f * s, -0.307f * s); cubicTo(-0.208f * s, -0.353f * s, -0.088f * s, -0.334f * s, -0.023f * s, -0.251f * s); close() }
    val p2 = Path().apply { moveTo(-0.205f * s, -0.059f * s); cubicTo(-0.192f * s, 0.001f * s, -0.206f * s, 0.055f * s, -0.236f * s, 0.061f * s); cubicTo(-0.266f * s, 0.067f * s, -0.3f * s, 0.024f * s, -0.313f * s, -0.036f * s); cubicTo(-0.326f * s, -0.096f * s, -0.312f * s, -0.15f * s, -0.282f * s, -0.156f * s); cubicTo(-0.252f * s, -0.163f * s, -0.217f * s, -0.119f * s, -0.205f * s, -0.059f * s); close() }
    val p3 = Path().apply { moveTo(0.0f * s, -0.196f * s); quadraticTo(0.0f * s, -0.307f * s, 0.056f * s, -0.39f * s) }
    val p4 = Path().apply { moveTo(0.046f * s, -0.307f * s); quadraticTo(0.219f * s, -0.223f * s, 0.296f * s, -0.399f * s); quadraticTo(0.123f * s, -0.483f * s, 0.046f * s, -0.307f * s); close() }
    drawPath(p0, sil ?: Color(0xFFBA2C25))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFE8372E))
        }
        drawPath(p2, Color(0x8CFFFFFF))
        drawOval(Color(0x8CFFFFFF), Offset(-0.253f * s, 0.124f * s), Size(0.044f * s, 0.044f * s))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF7A4A2A), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF3FAE49))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artFraise(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.25f * s); cubicTo(0.181f * s, -0.304f * s, 0.38f * s, -0.25f * s, 0.407f * s, -0.087f * s); cubicTo(0.426f * s, 0.094f * s, 0.272f * s, 0.293f * s, 0.091f * s, 0.411f * s); quadraticTo(0.0f * s, 0.456f * s, -0.09f * s, 0.411f * s); cubicTo(-0.272f * s, 0.293f * s, -0.426f * s, 0.094f * s, -0.407f * s, -0.087f * s); cubicTo(-0.38f * s, -0.25f * s, -0.181f * s, -0.304f * s, 0.0f * s, -0.25f * s); close() }
    val p1 = Path().apply { moveTo(-0.023f * s, -0.295f * s); cubicTo(0.159f * s, -0.349f * s, 0.358f * s, -0.295f * s, 0.385f * s, -0.132f * s); cubicTo(0.403f * s, 0.049f * s, 0.249f * s, 0.248f * s, 0.068f * s, 0.366f * s); quadraticTo(-0.023f * s, 0.411f * s, -0.113f * s, 0.366f * s); cubicTo(-0.294f * s, 0.248f * s, -0.448f * s, 0.049f * s, -0.43f * s, -0.132f * s); cubicTo(-0.403f * s, -0.295f * s, -0.204f * s, -0.349f * s, -0.023f * s, -0.295f * s); close() }
    val p2 = Path().apply { moveTo(-0.229f * s, -0.039f * s); cubicTo(-0.214f * s, 0.003f * s, -0.22f * s, 0.045f * s, -0.244f * s, 0.053f * s); cubicTo(-0.267f * s, 0.062f * s, -0.299f * s, 0.034f * s, -0.314f * s, -0.008f * s); cubicTo(-0.33f * s, -0.05f * s, -0.323f * s, -0.091f * s, -0.299f * s, -0.1f * s); cubicTo(-0.276f * s, -0.109f * s, -0.244f * s, -0.081f * s, -0.229f * s, -0.039f * s); close() }
    val p3 = Path().apply { moveTo(-0.011f * s, -0.399f * s); quadraticTo(0.0f * s, -0.418f * s, 0.011f * s, -0.399f * s); lineTo(0.048f * s, -0.333f * s); quadraticTo(0.059f * s, -0.313f * s, 0.081f * s, -0.316f * s); lineTo(0.213f * s, -0.331f * s); quadraticTo(0.235f * s, -0.334f * s, 0.217f * s, -0.321f * s); lineTo(0.136f * s, -0.263f * s); quadraticTo(0.118f * s, -0.25f * s, 0.136f * s, -0.237f * s); lineTo(0.217f * s, -0.179f * s); quadraticTo(0.235f * s, -0.166f * s, 0.213f * s, -0.168f * s); lineTo(0.081f * s, -0.184f * s); quadraticTo(0.059f * s, -0.187f * s, 0.048f * s, -0.167f * s); lineTo(0.011f * s, -0.101f * s); quadraticTo(0.0f * s, -0.081f * s, -0.011f * s, -0.101f * s); lineTo(-0.048f * s, -0.167f * s); quadraticTo(-0.059f * s, -0.187f * s, -0.081f * s, -0.184f * s); lineTo(-0.213f * s, -0.168f * s); quadraticTo(-0.235f * s, -0.166f * s, -0.217f * s, -0.179f * s); lineTo(-0.136f * s, -0.237f * s); quadraticTo(-0.118f * s, -0.25f * s, -0.136f * s, -0.263f * s); lineTo(-0.217f * s, -0.321f * s); quadraticTo(-0.235f * s, -0.334f * s, -0.213f * s, -0.331f * s); lineTo(-0.081f * s, -0.316f * s); quadraticTo(-0.059f * s, -0.313f * s, -0.048f * s, -0.333f * s); close() }
    val p4 = Path().apply { moveTo(0.0f * s, -0.259f * s); quadraticTo(0.0f * s, -0.331f * s, 0.027f * s, -0.395f * s) }
    drawPath(p0, sil ?: Color(0xFFBA2C25))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFE8372E))
        }
        drawOval(Color(0xFFFFE9A8), Offset(-0.243f * s, -0.125f * s), Size(0.051f * s, 0.076f * s))
        drawOval(Color(0xFFFFE9A8), Offset(-0.025f * s, -0.152f * s), Size(0.051f * s, 0.076f * s))
        drawOval(Color(0xFFFFE9A8), Offset(0.192f * s, -0.125f * s), Size(0.051f * s, 0.076f * s))
        drawOval(Color(0xFFFFE9A8), Offset(-0.143f * s, 0.047f * s), Size(0.051f * s, 0.076f * s))
        drawOval(Color(0xFFFFE9A8), Offset(0.092f * s, 0.047f * s), Size(0.051f * s, 0.076f * s))
        drawOval(Color(0xFFFFE9A8), Offset(-0.089f * s, 0.201f * s), Size(0.051f * s, 0.076f * s))
        drawOval(Color(0xFFFFE9A8), Offset(0.038f * s, 0.201f * s), Size(0.051f * s, 0.076f * s))
        drawPath(p2, Color(0x8CFFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF3FAE49))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF3FAE49), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCoeur(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, 0.386f * s); cubicTo(-0.169f * s, 0.254f * s, -0.433f * s, 0.085f * s, -0.433f * s, -0.141f * s); cubicTo(-0.433f * s, -0.311f * s, -0.32f * s, -0.386f * s, -0.207f * s, -0.386f * s); cubicTo(-0.094f * s, -0.386f * s, -0.028f * s, -0.32f * s, 0.0f * s, -0.235f * s); cubicTo(0.028f * s, -0.32f * s, 0.094f * s, -0.386f * s, 0.207f * s, -0.386f * s); cubicTo(0.32f * s, -0.386f * s, 0.433f * s, -0.311f * s, 0.433f * s, -0.141f * s); cubicTo(0.433f * s, 0.085f * s, 0.17f * s, 0.254f * s, 0.0f * s, 0.386f * s); close() }
    val p1 = Path().apply { moveTo(-0.023f * s, 0.339f * s); cubicTo(-0.193f * s, 0.207f * s, -0.457f * s, 0.038f * s, -0.457f * s, -0.188f * s); cubicTo(-0.457f * s, -0.358f * s, -0.344f * s, -0.433f * s, -0.231f * s, -0.433f * s); cubicTo(-0.118f * s, -0.433f * s, -0.052f * s, -0.367f * s, -0.023f * s, -0.282f * s); cubicTo(0.005f * s, -0.367f * s, 0.071f * s, -0.433f * s, 0.184f * s, -0.433f * s); cubicTo(0.297f * s, -0.433f * s, 0.41f * s, -0.358f * s, 0.41f * s, -0.188f * s); cubicTo(0.41f * s, 0.038f * s, 0.146f * s, 0.207f * s, -0.023f * s, 0.339f * s); close() }
    val p2 = Path().apply { moveTo(-0.21f * s, -0.235f * s); cubicTo(-0.18f * s, -0.193f * s, -0.18f * s, -0.141f * s, -0.21f * s, -0.121f * s); cubicTo(-0.239f * s, -0.1f * s, -0.288f * s, -0.117f * s, -0.317f * s, -0.16f * s); cubicTo(-0.347f * s, -0.202f * s, -0.347f * s, -0.254f * s, -0.318f * s, -0.275f * s); cubicTo(-0.288f * s, -0.296f * s, -0.239f * s, -0.278f * s, -0.21f * s, -0.235f * s); close() }
    drawPath(p0, sil ?: Color(0xFFBE2E3B))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFEE3A4A))
        }
        drawPath(p2, Color(0x8CFFFFFF))
        drawOval(Color(0x8CFFFFFF), Offset(-0.372f * s, -0.089f * s), Size(0.047f * s, 0.047f * s))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artVoiture(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.277f * s, -0.053f * s); lineTo(-0.184f * s, -0.274f * s); quadraticTo(-0.166f * s, -0.32f * s, -0.111f * s, -0.32f * s); lineTo(0.111f * s, -0.32f * s); quadraticTo(0.166f * s, -0.32f * s, 0.203f * s, -0.274f * s); lineTo(0.304f * s, -0.053f * s); close() }
    val p1 = Path().apply { moveTo(-0.3f * s, -0.099f * s); lineTo(-0.207f * s, -0.32f * s); quadraticTo(-0.189f * s, -0.366f * s, -0.134f * s, -0.366f * s); lineTo(0.088f * s, -0.366f * s); quadraticTo(0.143f * s, -0.366f * s, 0.18f * s, -0.32f * s); lineTo(0.281f * s, -0.099f * s); close() }
    val p2 = Path().apply { moveTo(-0.235f * s, -0.081f * s); lineTo(-0.166f * s, -0.247f * s); quadraticTo(-0.157f * s, -0.265f * s, -0.138f * s, -0.265f * s); lineTo(-0.028f * s, -0.265f * s); lineTo(-0.028f * s, -0.081f * s); close() }
    val p3 = Path().apply { moveTo(0.028f * s, -0.081f * s); lineTo(0.028f * s, -0.265f * s); lineTo(0.101f * s, -0.265f * s); quadraticTo(0.129f * s, -0.265f * s, 0.148f * s, -0.237f * s); lineTo(0.24f * s, -0.081f * s); close() }
    val p4 = Path().apply { moveTo(-0.332f * s, -0.081f * s); lineTo(0.332f * s, -0.081f * s); cubicTo(0.388f * s, -0.081f * s, 0.433f * s, -0.035f * s, 0.433f * s, 0.021f * s); lineTo(0.433f * s, 0.104f * s); cubicTo(0.433f * s, 0.16f * s, 0.388f * s, 0.205f * s, 0.332f * s, 0.205f * s); lineTo(-0.332f * s, 0.205f * s); cubicTo(-0.388f * s, 0.205f * s, -0.433f * s, 0.16f * s, -0.433f * s, 0.104f * s); lineTo(-0.433f * s, 0.021f * s); cubicTo(-0.433f * s, -0.035f * s, -0.388f * s, -0.081f * s, -0.332f * s, -0.081f * s); close() }
    val p5 = Path().apply { moveTo(-0.355f * s, -0.127f * s); lineTo(0.309f * s, -0.127f * s); cubicTo(0.365f * s, -0.127f * s, 0.41f * s, -0.081f * s, 0.41f * s, -0.025f * s); lineTo(0.41f * s, 0.058f * s); cubicTo(0.41f * s, 0.114f * s, 0.365f * s, 0.159f * s, 0.309f * s, 0.159f * s); lineTo(-0.355f * s, 0.159f * s); cubicTo(-0.411f * s, 0.159f * s, -0.456f * s, 0.114f * s, -0.456f * s, 0.058f * s); lineTo(-0.456f * s, -0.025f * s); cubicTo(-0.456f * s, -0.081f * s, -0.411f * s, -0.127f * s, -0.355f * s, -0.127f * s); close() }
    val p6 = Path().apply { moveTo(0.429f * s, 0.021f * s); cubicTo(0.429f * s, 0.046f * s, 0.412f * s, 0.067f * s, 0.392f * s, 0.067f * s); cubicTo(0.372f * s, 0.067f * s, 0.355f * s, 0.046f * s, 0.355f * s, 0.021f * s); cubicTo(0.355f * s, -0.005f * s, 0.372f * s, -0.025f * s, 0.392f * s, -0.025f * s); cubicTo(0.412f * s, -0.025f * s, 0.429f * s, -0.005f * s, 0.429f * s, 0.021f * s); close() }
    val p7 = Path().apply { moveTo(-0.364f * s, 0.021f * s); cubicTo(-0.364f * s, 0.044f * s, -0.377f * s, 0.062f * s, -0.392f * s, 0.062f * s); cubicTo(-0.407f * s, 0.062f * s, -0.42f * s, 0.044f * s, -0.42f * s, 0.021f * s); cubicTo(-0.42f * s, -0.002f * s, -0.407f * s, -0.021f * s, -0.392f * s, -0.021f * s); cubicTo(-0.377f * s, -0.021f * s, -0.364f * s, -0.002f * s, -0.364f * s, 0.021f * s); close() }
    drawPath(p0, sil ?: Color(0xFFB52B24))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFE8372E))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFBFE3FF))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFBFE3FF))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFB52B24))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFE8372E))
        }
        drawOval(Color(0x80FFFFFF), Offset(-0.369f * s, -0.062f * s), Size(0.184f * s, 0.055f * s))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFFFE066))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFFFA42B))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF41465F), 0.115f * s, Offset(-0.249f * s, 0.205f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.115f * s, Offset(-0.249f * s, 0.205f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFD9DEE9), 0.048f * s, Offset(-0.249f * s, 0.205f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.048f * s, Offset(-0.249f * s, 0.205f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF41465F), 0.115f * s, Offset(0.249f * s, 0.205f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.115f * s, Offset(0.249f * s, 0.205f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFD9DEE9), 0.048f * s, Offset(0.249f * s, 0.205f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.048f * s, Offset(0.249f * s, 0.205f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artChampignon(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.141f * s, 0.066f * s); cubicTo(-0.16f * s, 0.188f * s, -0.207f * s, 0.282f * s, -0.188f * s, 0.358f * s); quadraticTo(-0.179f * s, 0.414f * s, -0.094f * s, 0.414f * s); lineTo(0.094f * s, 0.414f * s); quadraticTo(0.179f * s, 0.414f * s, 0.188f * s, 0.358f * s); cubicTo(0.207f * s, 0.282f * s, 0.16f * s, 0.188f * s, 0.141f * s, 0.066f * s); close() }
    val p1 = Path().apply { moveTo(-0.165f * s, 0.019f * s); cubicTo(-0.184f * s, 0.141f * s, -0.231f * s, 0.235f * s, -0.212f * s, 0.311f * s); quadraticTo(-0.202f * s, 0.367f * s, -0.118f * s, 0.367f * s); lineTo(0.071f * s, 0.367f * s); quadraticTo(0.155f * s, 0.367f * s, 0.165f * s, 0.311f * s); cubicTo(0.184f * s, 0.235f * s, 0.137f * s, 0.141f * s, 0.118f * s, 0.019f * s); close() }
    val p2 = Path().apply { moveTo(-0.433f * s, 0.057f * s); cubicTo(-0.433f * s, -0.282f * s, -0.226f * s, -0.414f * s, 0.0f * s, -0.414f * s); cubicTo(0.226f * s, -0.414f * s, 0.433f * s, -0.282f * s, 0.433f * s, 0.057f * s); quadraticTo(0.433f * s, 0.122f * s, 0.367f * s, 0.122f * s); lineTo(-0.367f * s, 0.122f * s); quadraticTo(-0.433f * s, 0.122f * s, -0.433f * s, 0.057f * s); close() }
    val p3 = Path().apply { moveTo(-0.452f * s, 0.019f * s); cubicTo(-0.452f * s, -0.32f * s, -0.245f * s, -0.452f * s, -0.019f * s, -0.452f * s); cubicTo(0.207f * s, -0.452f * s, 0.414f * s, -0.32f * s, 0.414f * s, 0.019f * s); quadraticTo(0.414f * s, 0.085f * s, 0.348f * s, 0.085f * s); lineTo(-0.386f * s, 0.085f * s); quadraticTo(-0.452f * s, 0.085f * s, -0.452f * s, 0.019f * s); close() }
    val p4 = Path().apply { moveTo(-0.211f * s, -0.262f * s); cubicTo(-0.177f * s, -0.222f * s, -0.17f * s, -0.174f * s, -0.194f * s, -0.154f * s); cubicTo(-0.218f * s, -0.134f * s, -0.264f * s, -0.15f * s, -0.297f * s, -0.19f * s); cubicTo(-0.331f * s, -0.229f * s, -0.339f * s, -0.278f * s, -0.315f * s, -0.298f * s); cubicTo(-0.291f * s, -0.318f * s, -0.244f * s, -0.302f * s, -0.211f * s, -0.262f * s); close() }
    drawPath(p0, sil ?: Color(0xFFE0D3B9))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFFF0D2))
        }
        drawOval(Color(0x8CFFFFFF), Offset(-0.089f * s, 0.188f * s), Size(0.047f * s, 0.151f * s))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFBA2C25))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFE8372E))
        }
        clipPath(p2) {
            drawCircle(Color(0xFFFFFFFF), 0.08f * s, Offset(-0.16f * s, -0.113f * s))
            drawCircle(Color(0xFFFFFFFF), 0.071f * s, Offset(0.094f * s, -0.254f * s))
            drawCircle(Color(0xFFFFFFFF), 0.066f * s, Offset(0.254f * s, -0.056f * s))
            drawCircle(Color(0xFFFFFFFF), 0.047f * s, Offset(-0.348f * s, 0.019f * s))
            drawCircle(Color(0xFFFFFFFF), 0.047f * s, Offset(0.0f * s, 0.0f * s))
        }
        drawPath(p4, Color(0x8CFFFFFF))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCerises(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.198f * s, 0.036f * s); quadraticTo(-0.149f * s, -0.302f * s, 0.05f * s, -0.381f * s) }
    val p1 = Path().apply { moveTo(0.198f * s, 0.055f * s); quadraticTo(0.149f * s, -0.282f * s, 0.05f * s, -0.381f * s) }
    val p2 = Path().apply { moveTo(-0.01f * s, 0.214f * s); cubicTo(-0.01f * s, 0.318f * s, -0.099f * s, 0.402f * s, -0.208f * s, 0.402f * s); cubicTo(-0.318f * s, 0.402f * s, -0.407f * s, 0.318f * s, -0.407f * s, 0.214f * s); cubicTo(-0.407f * s, 0.11f * s, -0.318f * s, 0.026f * s, -0.208f * s, 0.026f * s); cubicTo(-0.099f * s, 0.026f * s, -0.01f * s, 0.11f * s, -0.01f * s, 0.214f * s); close() }
    val p3 = Path().apply { moveTo(-0.03f * s, 0.169f * s); cubicTo(-0.03f * s, 0.273f * s, -0.118f * s, 0.358f * s, -0.228f * s, 0.358f * s); cubicTo(-0.338f * s, 0.358f * s, -0.426f * s, 0.273f * s, -0.426f * s, 0.169f * s); cubicTo(-0.426f * s, 0.065f * s, -0.338f * s, -0.019f * s, -0.228f * s, -0.019f * s); cubicTo(-0.118f * s, -0.019f * s, -0.03f * s, 0.065f * s, -0.03f * s, 0.169f * s); close() }
    val p4 = Path().apply { moveTo(-0.257f * s, 0.163f * s); cubicTo(-0.273f * s, 0.198f * s, -0.305f * s, 0.218f * s, -0.327f * s, 0.207f * s); cubicTo(-0.349f * s, 0.197f * s, -0.354f * s, 0.16f * s, -0.338f * s, 0.126f * s); cubicTo(-0.322f * s, 0.091f * s, -0.29f * s, 0.071f * s, -0.268f * s, 0.082f * s); cubicTo(-0.246f * s, 0.092f * s, -0.241f * s, 0.129f * s, -0.257f * s, 0.163f * s); close() }
    val p5 = Path().apply { moveTo(0.407f * s, 0.244f * s); cubicTo(0.407f * s, 0.348f * s, 0.318f * s, 0.432f * s, 0.208f * s, 0.432f * s); cubicTo(0.099f * s, 0.432f * s, 0.01f * s, 0.348f * s, 0.01f * s, 0.244f * s); cubicTo(0.01f * s, 0.14f * s, 0.099f * s, 0.055f * s, 0.208f * s, 0.055f * s); cubicTo(0.318f * s, 0.055f * s, 0.407f * s, 0.14f * s, 0.407f * s, 0.244f * s); close() }
    val p6 = Path().apply { moveTo(0.387f * s, 0.199f * s); cubicTo(0.387f * s, 0.303f * s, 0.298f * s, 0.388f * s, 0.188f * s, 0.388f * s); cubicTo(0.079f * s, 0.388f * s, -0.01f * s, 0.303f * s, -0.01f * s, 0.199f * s); cubicTo(-0.01f * s, 0.095f * s, 0.079f * s, 0.011f * s, 0.188f * s, 0.011f * s); cubicTo(0.298f * s, 0.011f * s, 0.387f * s, 0.095f * s, 0.387f * s, 0.199f * s); close() }
    val p7 = Path().apply { moveTo(0.16f * s, 0.193f * s); cubicTo(0.143f * s, 0.228f * s, 0.112f * s, 0.248f * s, 0.09f * s, 0.237f * s); cubicTo(0.067f * s, 0.227f * s, 0.062f * s, 0.19f * s, 0.079f * s, 0.156f * s); cubicTo(0.095f * s, 0.121f * s, 0.126f * s, 0.101f * s, 0.148f * s, 0.111f * s); cubicTo(0.171f * s, 0.122f * s, 0.176f * s, 0.158f * s, 0.16f * s, 0.193f * s); close() }
    val p8 = Path().apply { moveTo(0.06f * s, -0.381f * s); quadraticTo(0.195f * s, -0.195f * s, 0.387f * s, -0.322f * s); quadraticTo(0.252f * s, -0.507f * s, 0.06f * s, -0.381f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF4F9A3A), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF4F9A3A), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFB6222F))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFE32A3B))
        }
        drawPath(p4, Color(0x8CFFFFFF))
        drawOval(Color(0x8CFFFFFF), Offset(-0.307f * s, 0.273f * s), Size(0.04f * s, 0.04f * s))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFB6222F))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFFE32A3B))
        }
        drawPath(p7, Color(0x8CFFFFFF))
        drawOval(Color(0x8CFFFFFF), Offset(0.109f * s, 0.303f * s), Size(0.04f * s, 0.04f * s))
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF3FAE49))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCarotte(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.036f * s, -0.066f * s); quadraticTo(0.081f * s, -0.264f * s, -0.074f * s, -0.432f * s); quadraticTo(-0.191f * s, -0.235f * s, -0.036f * s, -0.066f * s); close() }
    val p1 = Path().apply { moveTo(-0.036f * s, -0.066f * s); quadraticTo(-0.158f * s, -0.245f * s, -0.358f * s, -0.161f * s); quadraticTo(-0.235f * s, 0.017f * s, -0.036f * s, -0.066f * s); close() }
    val p2 = Path().apply { moveTo(-0.036f * s, -0.066f * s); quadraticTo(-0.046f * s, -0.269f * s, -0.24f * s, -0.328f * s); quadraticTo(-0.23f * s, -0.125f * s, -0.036f * s, -0.066f * s); close() }
    val p3 = Path().apply { moveTo(-0.161f * s, 0.106f * s); quadraticTo(-0.215f * s, 0.037f * s, -0.137f * s, -0.037f * s); lineTo(0.017f * s, -0.157f * s); quadraticTo(0.108f * s, -0.215f * s, 0.162f * s, -0.146f * s); cubicTo(0.271f * s, 0.041f * s, 0.322f * s, 0.249f * s, 0.356f * s, 0.395f * s); quadraticTo(0.367f * s, 0.449f * s, 0.317f * s, 0.425f * s); cubicTo(0.183f * s, 0.357f * s, -0.006f * s, 0.257f * s, -0.161f * s, 0.106f * s); close() }
    val p4 = Path().apply { moveTo(-0.202f * s, 0.101f * s); quadraticTo(-0.256f * s, 0.032f * s, -0.178f * s, -0.042f * s); lineTo(-0.024f * s, -0.162f * s); quadraticTo(0.067f * s, -0.22f * s, 0.121f * s, -0.151f * s); cubicTo(0.23f * s, 0.036f * s, 0.281f * s, 0.244f * s, 0.315f * s, 0.39f * s); quadraticTo(0.326f * s, 0.444f * s, 0.276f * s, 0.42f * s); cubicTo(0.142f * s, 0.352f * s, -0.047f * s, 0.252f * s, -0.202f * s, 0.101f * s); close() }
    val p5 = Path().apply { moveTo(0.012f * s, 0.075f * s); cubicTo(0.036f * s, 0.117f * s, 0.045f * s, 0.157f * s, 0.031f * s, 0.165f * s); cubicTo(0.017f * s, 0.174f * s, -0.014f * s, 0.146f * s, -0.039f * s, 0.104f * s); cubicTo(-0.063f * s, 0.062f * s, -0.071f * s, 0.021f * s, -0.057f * s, 0.013f * s); cubicTo(-0.043f * s, 0.005f * s, -0.012f * s, 0.033f * s, 0.012f * s, 0.075f * s); close() }
    drawPath(p0, sil ?: Color(0xFF3FAE49))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF3FAE49))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF56B85F))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFCC6E19))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFFF8A1F))
        }
    }
    drawLine(sil ?: Color(0xFFC25A0A), Offset(-0.039f * s, 0.184f * s), Offset(0.066f * s, 0.127f * s), 0.026f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFFC25A0A), Offset(0.082f * s, 0.275f * s), Offset(0.163f * s, 0.236f * s), 0.026f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFFC25A0A), Offset(0.188f * s, -0.018f * s), Offset(0.099f * s, 0.027f * s), 0.026f * s, StrokeCap.Round)
    if (sil == null) {
        drawPath(p5, Color(0x8CFFFFFF))
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artOrange(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.389f * s, 0.053f * s); cubicTo(0.389f * s, 0.263f * s, 0.215f * s, 0.433f * s, 0.0f * s, 0.433f * s); cubicTo(-0.215f * s, 0.433f * s, -0.389f * s, 0.263f * s, -0.389f * s, 0.053f * s); cubicTo(-0.389f * s, -0.156f * s, -0.215f * s, -0.327f * s, 0.0f * s, -0.327f * s); cubicTo(0.215f * s, -0.327f * s, 0.389f * s, -0.156f * s, 0.389f * s, 0.053f * s); close() }
    val p1 = Path().apply { moveTo(0.366f * s, 0.007f * s); cubicTo(0.366f * s, 0.217f * s, 0.192f * s, 0.387f * s, -0.023f * s, 0.387f * s); cubicTo(-0.238f * s, 0.387f * s, -0.412f * s, 0.217f * s, -0.412f * s, 0.007f * s); cubicTo(-0.412f * s, -0.203f * s, -0.238f * s, -0.373f * s, -0.023f * s, -0.373f * s); cubicTo(0.192f * s, -0.373f * s, 0.366f * s, -0.203f * s, 0.366f * s, 0.007f * s); close() }
    val p2 = Path().apply { moveTo(-0.132f * s, -0.132f * s); cubicTo(-0.1f * s, -0.086f * s, -0.097f * s, -0.032f * s, -0.127f * s, -0.011f * s); cubicTo(-0.156f * s, 0.009f * s, -0.206f * s, -0.012f * s, -0.238f * s, -0.058f * s); cubicTo(-0.271f * s, -0.104f * s, -0.273f * s, -0.158f * s, -0.244f * s, -0.178f * s); cubicTo(-0.214f * s, -0.199f * s, -0.164f * s, -0.178f * s, -0.132f * s, -0.132f * s); close() }
    val p3 = Path().apply { moveTo(0.0f * s, -0.299f * s); lineTo(0.009f * s, -0.354f * s) }
    val p4 = Path().apply { moveTo(0.019f * s, -0.327f * s); quadraticTo(0.185f * s, -0.254f * s, 0.278f * s, -0.41f * s); quadraticTo(0.111f * s, -0.483f * s, 0.019f * s, -0.327f * s); close() }
    drawPath(p0, sil ?: Color(0xFFCC7B19))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFF9A1F))
        }
        drawCircle(Color(0xB3D96A00), 0.014f * s, Offset(0.167f * s, 0.165f * s))
        drawCircle(Color(0xB3D96A00), 0.014f * s, Offset(-0.056f * s, 0.239f * s))
        drawCircle(Color(0xB3D96A00), 0.014f * s, Offset(0.074f * s, -0.002f * s))
        drawCircle(Color(0xB3D96A00), 0.014f * s, Offset(0.25f * s, -0.03f * s))
        drawPath(p2, Color(0x8CFFFFFF))
        drawOval(Color(0x8CFFFFFF), Offset(-0.271f * s, 0.051f * s), Size(0.041f * s, 0.041f * s))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.117f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF5E8F3A), style = Stroke(0.045f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF3FAE49))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCitrouille(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.018f * s, 0.085f * s); cubicTo(-0.018f * s, 0.24f * s, -0.111f * s, 0.365f * s, -0.226f * s, 0.365f * s); cubicTo(-0.341f * s, 0.365f * s, -0.434f * s, 0.24f * s, -0.434f * s, 0.085f * s); cubicTo(-0.434f * s, -0.07f * s, -0.341f * s, -0.195f * s, -0.226f * s, -0.195f * s); cubicTo(-0.111f * s, -0.195f * s, -0.018f * s, -0.07f * s, -0.018f * s, 0.085f * s); close() }
    val p1 = Path().apply { moveTo(-0.036f * s, 0.04f * s); cubicTo(-0.036f * s, 0.195f * s, -0.129f * s, 0.32f * s, -0.244f * s, 0.32f * s); cubicTo(-0.359f * s, 0.32f * s, -0.452f * s, 0.195f * s, -0.452f * s, 0.04f * s); cubicTo(-0.452f * s, -0.115f * s, -0.359f * s, -0.24f * s, -0.244f * s, -0.24f * s); cubicTo(-0.129f * s, -0.24f * s, -0.036f * s, -0.115f * s, -0.036f * s, 0.04f * s); close() }
    val p2 = Path().apply { moveTo(0.434f * s, 0.085f * s); cubicTo(0.434f * s, 0.24f * s, 0.341f * s, 0.365f * s, 0.226f * s, 0.365f * s); cubicTo(0.111f * s, 0.365f * s, 0.018f * s, 0.24f * s, 0.018f * s, 0.085f * s); cubicTo(0.018f * s, -0.07f * s, 0.111f * s, -0.195f * s, 0.226f * s, -0.195f * s); cubicTo(0.341f * s, -0.195f * s, 0.434f * s, -0.07f * s, 0.434f * s, 0.085f * s); close() }
    val p3 = Path().apply { moveTo(0.416f * s, 0.04f * s); cubicTo(0.416f * s, 0.195f * s, 0.323f * s, 0.32f * s, 0.208f * s, 0.32f * s); cubicTo(0.093f * s, 0.32f * s, 0.0f * s, 0.195f * s, 0.0f * s, 0.04f * s); cubicTo(0.0f * s, -0.115f * s, 0.093f * s, -0.24f * s, 0.208f * s, -0.24f * s); cubicTo(0.323f * s, -0.24f * s, 0.416f * s, -0.115f * s, 0.416f * s, 0.04f * s); close() }
    val p4 = Path().apply { moveTo(0.199f * s, 0.085f * s); cubicTo(0.199f * s, 0.26f * s, 0.11f * s, 0.401f * s, 0.0f * s, 0.401f * s); cubicTo(-0.11f * s, 0.401f * s, -0.199f * s, 0.26f * s, -0.199f * s, 0.085f * s); cubicTo(-0.199f * s, -0.09f * s, -0.11f * s, -0.231f * s, 0.0f * s, -0.231f * s); cubicTo(0.11f * s, -0.231f * s, 0.199f * s, -0.09f * s, 0.199f * s, 0.085f * s); close() }
    val p5 = Path().apply { moveTo(0.181f * s, 0.04f * s); cubicTo(0.181f * s, 0.214f * s, 0.092f * s, 0.356f * s, -0.018f * s, 0.356f * s); cubicTo(-0.128f * s, 0.356f * s, -0.217f * s, 0.214f * s, -0.217f * s, 0.04f * s); cubicTo(-0.217f * s, -0.135f * s, -0.128f * s, -0.276f * s, -0.018f * s, -0.276f * s); cubicTo(0.092f * s, -0.276f * s, 0.181f * s, -0.135f * s, 0.181f * s, 0.04f * s); close() }
    val p6 = Path().apply { moveTo(-0.045f * s, -0.057f * s); cubicTo(-0.05f * s, -0.008f * s, -0.065f * s, 0.032f * s, -0.08f * s, 0.03f * s); cubicTo(-0.095f * s, 0.029f * s, -0.104f * s, -0.012f * s, -0.099f * s, -0.062f * s); cubicTo(-0.095f * s, -0.112f * s, -0.079f * s, -0.151f * s, -0.064f * s, -0.15f * s); cubicTo(-0.049f * s, -0.148f * s, -0.041f * s, -0.107f * s, -0.045f * s, -0.057f * s); close() }
    val p7 = Path().apply { moveTo(0.0f * s, -0.231f * s); lineTo(0.027f * s, -0.349f * s) }
    val p8 = Path().apply { moveTo(0.045f * s, -0.313f * s); quadraticTo(0.175f * s, -0.224f * s, 0.271f * s, -0.349f * s); quadraticTo(0.141f * s, -0.438f * s, 0.045f * s, -0.313f * s); close() }
    drawPath(p0, sil ?: Color(0xFFC25A18))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFF2701E))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFC25A18))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFF2701E))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFC25A18))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFF2701E))
        }
        drawPath(p6, Color(0x8CFFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.152f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF5E8F3A), style = Stroke(0.08f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF3FAE49))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artPoisson(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.183f * s, 0.001f * s); cubicTo(-0.252f * s, -0.048f * s, -0.33f * s, -0.194f * s, -0.388f * s, -0.243f * s); cubicTo(-0.447f * s, -0.116f * s, -0.447f * s, 0.158f * s, -0.388f * s, 0.265f * s); cubicTo(-0.33f * s, 0.206f * s, -0.252f * s, 0.06f * s, -0.183f * s, 0.001f * s); close() }
    val p1 = Path().apply { moveTo(-0.037f * s, -0.233f * s); cubicTo(0.002f * s, -0.409f * s, 0.198f * s, -0.429f * s, 0.276f * s, -0.233f * s); close() }
    val p2 = Path().apply { moveTo(0.432f * s, 0.021f * s); cubicTo(0.432f * s, 0.172f * s, 0.284f * s, 0.294f * s, 0.1f * s, 0.294f * s); cubicTo(-0.083f * s, 0.294f * s, -0.232f * s, 0.172f * s, -0.232f * s, 0.021f * s); cubicTo(-0.232f * s, -0.13f * s, -0.083f * s, -0.253f * s, 0.1f * s, -0.253f * s); cubicTo(0.284f * s, -0.253f * s, 0.432f * s, -0.13f * s, 0.432f * s, 0.021f * s); close() }
    val p3 = Path().apply { moveTo(0.413f * s, -0.028f * s); cubicTo(0.413f * s, 0.123f * s, 0.264f * s, 0.246f * s, 0.081f * s, 0.246f * s); cubicTo(-0.103f * s, 0.246f * s, -0.252f * s, 0.123f * s, -0.252f * s, -0.028f * s); cubicTo(-0.252f * s, -0.179f * s, -0.103f * s, -0.302f * s, 0.081f * s, -0.302f * s); cubicTo(0.264f * s, -0.302f * s, 0.413f * s, -0.179f * s, 0.413f * s, -0.028f * s); close() }
    val p4 = Path().apply { moveTo(0.23f * s, -0.39f * s); quadraticTo(0.195f * s, 0.001f * s, 0.23f * s, 0.392f * s); lineTo(0.303f * s, 0.392f * s); quadraticTo(0.269f * s, 0.001f * s, 0.303f * s, -0.39f * s); close() }
    val p5 = Path().apply { moveTo(-0.037f * s, -0.39f * s); quadraticTo(-0.071f * s, 0.001f * s, -0.037f * s, 0.392f * s); lineTo(0.061f * s, 0.392f * s); quadraticTo(0.027f * s, 0.001f * s, 0.061f * s, -0.39f * s); close() }
    val p6 = Path().apply { moveTo(-0.188f * s, -0.39f * s); quadraticTo(-0.222f * s, 0.001f * s, -0.188f * s, 0.392f * s); lineTo(-0.139f * s, 0.392f * s); quadraticTo(-0.173f * s, 0.001f * s, -0.139f * s, -0.39f * s); close() }
    val p7 = Path().apply { moveTo(0.158f * s, -0.175f * s); cubicTo(0.16f * s, -0.159f * s, 0.109f * s, -0.141f * s, 0.044f * s, -0.136f * s); cubicTo(-0.02f * s, -0.13f * s, -0.074f * s, -0.138f * s, -0.075f * s, -0.155f * s); cubicTo(-0.077f * s, -0.171f * s, -0.025f * s, -0.188f * s, 0.039f * s, -0.194f * s); cubicTo(0.104f * s, -0.2f * s, 0.157f * s, -0.191f * s, 0.158f * s, -0.175f * s); close() }
    val p8 = Path().apply { moveTo(0.042f * s, 0.216f * s); quadraticTo(-0.111f * s, 0.224f * s, -0.076f * s, 0.373f * s); quadraticTo(0.077f * s, 0.365f * s, 0.042f * s, 0.216f * s); close() }
    val p9 = Path().apply { moveTo(0.335f * s, 0.099f * s); quadraticTo(0.384f * s, 0.158f * s, 0.432f * s, 0.099f * s) }
    drawPath(p0, sil ?: Color(0xFFFF9635))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFFF9635))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFCC6E19))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFFF8A1F))
        }
    }
    clipPath(p2) {
        drawPath(p4, sil ?: Color(0xFFFFFFFF))
        drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p5, sil ?: Color(0xFFFFFFFF))
        drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p6, sil ?: Color(0xFFFFFFFF))
        drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    if (sil == null) {
        drawPath(p7, Color(0x8CFFFFFF))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFFF9635))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.073f * s, Offset(0.305f * s, -0.067f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.073f * s, Offset(0.305f * s, -0.067f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.043f * s, Offset(0.327f * s, -0.06f * s))
        drawCircle(Color(0xFFFFFFFF), 0.015f * s, Offset(0.311f * s, -0.079f * s))
    }
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artRenard(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.406f * s, -0.057f * s); quadraticTo(-0.438f * s, -0.039f * s, -0.432f * s, -0.076f * s); lineTo(-0.379f * s, -0.385f * s); quadraticTo(-0.373f * s, -0.422f * s, -0.343f * s, -0.401f * s); lineTo(-0.124f * s, -0.247f * s); quadraticTo(-0.093f * s, -0.226f * s, -0.126f * s, -0.208f * s); close() }
    val p1 = Path().apply { moveTo(-0.357f * s, -0.105f * s); quadraticTo(-0.373f * s, -0.095f * s, -0.371f * s, -0.114f * s); lineTo(-0.347f * s, -0.301f * s); quadraticTo(-0.345f * s, -0.319f * s, -0.33f * s, -0.308f * s); lineTo(-0.211f * s, -0.219f * s); quadraticTo(-0.196f * s, -0.207f * s, -0.212f * s, -0.197f * s); close() }
    val p2 = Path().apply { moveTo(0.406f * s, -0.057f * s); quadraticTo(0.439f * s, -0.039f * s, 0.432f * s, -0.076f * s); lineTo(0.379f * s, -0.385f * s); quadraticTo(0.373f * s, -0.422f * s, 0.343f * s, -0.401f * s); lineTo(0.124f * s, -0.247f * s); quadraticTo(0.093f * s, -0.226f * s, 0.126f * s, -0.208f * s); close() }
    val p3 = Path().apply { moveTo(0.357f * s, -0.105f * s); quadraticTo(0.373f * s, -0.095f * s, 0.371f * s, -0.114f * s); lineTo(0.348f * s, -0.301f * s); quadraticTo(0.345f * s, -0.319f * s, 0.33f * s, -0.308f * s); lineTo(0.211f * s, -0.219f * s); quadraticTo(0.196f * s, -0.207f * s, 0.212f * s, -0.197f * s); close() }
    val p4 = Path().apply { moveTo(-0.429f * s, -0.021f * s); cubicTo(-0.429f * s, -0.189f * s, -0.28f * s, -0.263f * s, 0.0f * s, -0.263f * s); cubicTo(0.28f * s, -0.263f * s, 0.429f * s, -0.189f * s, 0.429f * s, -0.021f * s); cubicTo(0.429f * s, 0.129f * s, 0.205f * s, 0.278f * s, 0.0f * s, 0.408f * s); cubicTo(-0.205f * s, 0.278f * s, -0.429f * s, 0.129f * s, -0.429f * s, -0.021f * s); close() }
    val p5 = Path().apply { moveTo(-0.448f * s, -0.049f * s); cubicTo(-0.448f * s, -0.217f * s, -0.298f * s, -0.291f * s, -0.019f * s, -0.291f * s); cubicTo(0.261f * s, -0.291f * s, 0.411f * s, -0.217f * s, 0.411f * s, -0.049f * s); cubicTo(0.411f * s, 0.101f * s, 0.187f * s, 0.25f * s, -0.019f * s, 0.38f * s); cubicTo(-0.224f * s, 0.25f * s, -0.448f * s, 0.101f * s, -0.448f * s, -0.049f * s); close() }
    val p6 = Path().apply { moveTo(-0.429f * s, -0.002f * s); cubicTo(-0.317f * s, 0.129f * s, -0.149f * s, 0.054f * s, 0.0f * s, 0.147f * s); cubicTo(0.149f * s, 0.054f * s, 0.317f * s, 0.129f * s, 0.429f * s, -0.002f * s); cubicTo(0.429f * s, 0.129f * s, 0.205f * s, 0.278f * s, 0.0f * s, 0.408f * s); cubicTo(-0.205f * s, 0.278f * s, -0.429f * s, 0.129f * s, -0.429f * s, -0.002f * s); close() }
    val p7 = Path().apply { moveTo(-0.15f * s, -0.202f * s); cubicTo(-0.148f * s, -0.189f * s, -0.179f * s, -0.173f * s, -0.22f * s, -0.166f * s); cubicTo(-0.26f * s, -0.159f * s, -0.295f * s, -0.163f * s, -0.297f * s, -0.176f * s); cubicTo(-0.3f * s, -0.188f * s, -0.269f * s, -0.204f * s, -0.228f * s, -0.212f * s); cubicTo(-0.187f * s, -0.219f * s, -0.153f * s, -0.214f * s, -0.15f * s, -0.202f * s); close() }
    val p8 = Path().apply { moveTo(-0.047f * s, 0.178f * s); quadraticTo(-0.065f * s, 0.157f * s, -0.037f * s, 0.157f * s); lineTo(0.037f * s, 0.157f * s); quadraticTo(0.065f * s, 0.157f * s, 0.047f * s, 0.178f * s); lineTo(0.019f * s, 0.21f * s); quadraticTo(0.0f * s, 0.231f * s, -0.018f * s, 0.21f * s); close() }
    val p9 = Path().apply { moveTo(0.0f * s, 0.231f * s); lineTo(0.0f * s, 0.268f * s); quadraticTo(-0.047f * s, 0.334f * s, -0.103f * s, 0.287f * s) }
    val p10 = Path().apply { moveTo(0.0f * s, 0.268f * s); quadraticTo(0.047f * s, 0.334f * s, 0.103f * s, 0.287f * s) }
    drawPath(p0, sil ?: Color(0xFFFF8A1F))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p1, Color(0xFF7A3B1C))
    }
    drawPath(p2, sil ?: Color(0xFFFF8A1F))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p3, Color(0xFF7A3B1C))
    }
    drawPath(p4, sil ?: Color(0xFFCC6E19))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFFF8A1F))
        }
        clipPath(p4) {
            drawPath(p6, Color(0xFFFFFFFF))
        }
        drawPath(p7, Color(0x8CFFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.061f * s, Offset(-0.187f * s, -0.03f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.061f * s, Offset(-0.187f * s, -0.03f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.035f * s, Offset(-0.181f * s, -0.012f * s))
        drawCircle(Color(0xFFFFFFFF), 0.012f * s, Offset(-0.194f * s, -0.028f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.061f * s, Offset(0.187f * s, -0.03f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.061f * s, Offset(0.187f * s, -0.03f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.035f * s, Offset(0.181f * s, -0.012f * s))
        drawCircle(Color(0xFFFFFFFF), 0.012f * s, Offset(0.167f * s, -0.028f * s))
        drawPath(p8, Color(0xFF2B2D42))
    }
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artBanane(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.379f * s, -0.187f * s); lineTo(0.383f * s, -0.295f * s) }
    val p1 = Path().apply { moveTo(-0.406f * s, 0.119f * s); cubicTo(-0.415f * s, 0.142f * s, -0.361f * s, 0.218f * s, -0.325f * s, 0.252f * s); cubicTo(-0.288f * s, 0.286f * s, -0.237f * s, 0.308f * s, -0.187f * s, 0.323f * s); cubicTo(-0.138f * s, 0.338f * s, -0.082f * s, 0.346f * s, -0.03f * s, 0.344f * s); cubicTo(0.023f * s, 0.341f * s, 0.079f * s, 0.329f * s, 0.128f * s, 0.31f * s); cubicTo(0.177f * s, 0.29f * s, 0.226f * s, 0.259f * s, 0.265f * s, 0.225f * s); cubicTo(0.305f * s, 0.19f * s, 0.34f * s, 0.145f * s, 0.365f * s, 0.1f * s); cubicTo(0.389f * s, 0.055f * s, 0.412f * s, 0.003f * s, 0.414f * s, -0.046f * s); cubicTo(0.417f * s, -0.096f * s, 0.403f * s, -0.189f * s, 0.38f * s, -0.198f * s); cubicTo(0.357f * s, -0.208f * s, 0.308f * s, -0.132f * s, 0.277f * s, -0.104f * s); cubicTo(0.247f * s, -0.076f * s, 0.224f * s, -0.052f * s, 0.197f * s, -0.031f * s); cubicTo(0.17f * s, -0.009f * s, 0.142f * s, 0.008f * s, 0.114f * s, 0.024f * s); cubicTo(0.086f * s, 0.04f * s, 0.058f * s, 0.053f * s, 0.029f * s, 0.065f * s); cubicTo(0.0f * s, 0.076f * s, -0.029f * s, 0.086f * s, -0.06f * s, 0.094f * s); cubicTo(-0.092f * s, 0.102f * s, -0.124f * s, 0.109f * s, -0.158f * s, 0.113f * s); cubicTo(-0.192f * s, 0.116f * s, -0.225f * s, 0.114f * s, -0.267f * s, 0.115f * s); cubicTo(-0.308f * s, 0.116f * s, -0.396f * s, 0.096f * s, -0.406f * s, 0.119f * s); close() }
    val p2 = Path().apply { moveTo(-0.428f * s, 0.091f * s); cubicTo(-0.437f * s, 0.114f * s, -0.383f * s, 0.19f * s, -0.347f * s, 0.224f * s); cubicTo(-0.31f * s, 0.258f * s, -0.259f * s, 0.28f * s, -0.209f * s, 0.295f * s); cubicTo(-0.16f * s, 0.31f * s, -0.104f * s, 0.318f * s, -0.052f * s, 0.316f * s); cubicTo(0.001f * s, 0.313f * s, 0.057f * s, 0.301f * s, 0.106f * s, 0.281f * s); cubicTo(0.155f * s, 0.262f * s, 0.204f * s, 0.231f * s, 0.243f * s, 0.196f * s); cubicTo(0.283f * s, 0.162f * s, 0.318f * s, 0.117f * s, 0.343f * s, 0.072f * s); cubicTo(0.367f * s, 0.027f * s, 0.39f * s, -0.025f * s, 0.392f * s, -0.074f * s); cubicTo(0.395f * s, -0.124f * s, 0.381f * s, -0.217f * s, 0.358f * s, -0.226f * s); cubicTo(0.335f * s, -0.236f * s, 0.286f * s, -0.16f * s, 0.255f * s, -0.132f * s); cubicTo(0.225f * s, -0.104f * s, 0.202f * s, -0.08f * s, 0.175f * s, -0.059f * s); cubicTo(0.148f * s, -0.037f * s, 0.12f * s, -0.02f * s, 0.092f * s, -0.004f * s); cubicTo(0.064f * s, 0.012f * s, 0.036f * s, 0.025f * s, 0.007f * s, 0.037f * s); cubicTo(-0.022f * s, 0.048f * s, -0.051f * s, 0.058f * s, -0.082f * s, 0.066f * s); cubicTo(-0.113f * s, 0.074f * s, -0.145f * s, 0.081f * s, -0.18f * s, 0.085f * s); cubicTo(-0.214f * s, 0.088f * s, -0.247f * s, 0.086f * s, -0.289f * s, 0.087f * s); cubicTo(-0.33f * s, 0.088f * s, -0.418f * s, 0.068f * s, -0.428f * s, 0.091f * s); close() }
    val p3 = Path().apply { moveTo(0.219f * s, 0.248f * s); cubicTo(0.222f * s, 0.258f * s, 0.158f * s, 0.288f * s, 0.076f * s, 0.315f * s); cubicTo(-0.006f * s, 0.341f * s, -0.076f * s, 0.355f * s, -0.079f * s, 0.344f * s); cubicTo(-0.082f * s, 0.334f * s, -0.018f * s, 0.304f * s, 0.064f * s, 0.277f * s); cubicTo(0.146f * s, 0.251f * s, 0.215f * s, 0.237f * s, 0.219f * s, 0.248f * s); close() }
    val p4 = Path().apply { moveTo(-0.384f * s, 0.106f * s); cubicTo(-0.378f * s, 0.121f * s, -0.385f * s, 0.138f * s, -0.4f * s, 0.144f * s); cubicTo(-0.415f * s, 0.15f * s, -0.432f * s, 0.143f * s, -0.438f * s, 0.128f * s); cubicTo(-0.444f * s, 0.113f * s, -0.437f * s, 0.096f * s, -0.422f * s, 0.09f * s); cubicTo(-0.407f * s, 0.084f * s, -0.39f * s, 0.091f * s, -0.384f * s, 0.106f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF7A4A2A), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFCCA832))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFFFFD23F))
        }
        drawPath(p3, Color(0x8CFFFFFF))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF7A4A2A))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artEtoile(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.028f * s, -0.381f * s); quadraticTo(0.0f * s, -0.443f * s, 0.028f * s, -0.381f * s); lineTo(0.108f * s, -0.208f * s); quadraticTo(0.137f * s, -0.146f * s, 0.204f * s, -0.138f * s); lineTo(0.394f * s, -0.116f * s); quadraticTo(0.461f * s, -0.108f * s, 0.411f * s, -0.062f * s); lineTo(0.271f * s, 0.068f * s); quadraticTo(0.221f * s, 0.114f * s, 0.235f * s, 0.181f * s); lineTo(0.272f * s, 0.368f * s); quadraticTo(0.285f * s, 0.434f * s, 0.226f * s, 0.401f * s); lineTo(0.059f * s, 0.308f * s); quadraticTo(0.0f * s, 0.275f * s, -0.059f * s, 0.308f * s); lineTo(-0.226f * s, 0.401f * s); quadraticTo(-0.285f * s, 0.434f * s, -0.272f * s, 0.368f * s); lineTo(-0.235f * s, 0.181f * s); quadraticTo(-0.221f * s, 0.114f * s, -0.271f * s, 0.068f * s); lineTo(-0.411f * s, -0.062f * s); quadraticTo(-0.461f * s, -0.108f * s, -0.394f * s, -0.116f * s); lineTo(-0.204f * s, -0.138f * s); quadraticTo(-0.137f * s, -0.146f * s, -0.108f * s, -0.208f * s); close() }
    val p1 = Path().apply { moveTo(-0.048f * s, -0.42f * s); quadraticTo(-0.019f * s, -0.482f * s, 0.009f * s, -0.42f * s); lineTo(0.089f * s, -0.247f * s); quadraticTo(0.118f * s, -0.185f * s, 0.185f * s, -0.177f * s); lineTo(0.374f * s, -0.155f * s); quadraticTo(0.442f * s, -0.147f * s, 0.392f * s, -0.101f * s); lineTo(0.252f * s, 0.029f * s); quadraticTo(0.202f * s, 0.075f * s, 0.215f * s, 0.142f * s); lineTo(0.253f * s, 0.329f * s); quadraticTo(0.266f * s, 0.396f * s, 0.206f * s, 0.362f * s); lineTo(0.04f * s, 0.269f * s); quadraticTo(-0.019f * s, 0.236f * s, -0.079f * s, 0.269f * s); lineTo(-0.245f * s, 0.362f * s); quadraticTo(-0.304f * s, 0.396f * s, -0.291f * s, 0.329f * s); lineTo(-0.254f * s, 0.142f * s); quadraticTo(-0.241f * s, 0.075f * s, -0.291f * s, 0.029f * s); lineTo(-0.431f * s, -0.101f * s); quadraticTo(-0.481f * s, -0.147f * s, -0.413f * s, -0.155f * s); lineTo(-0.224f * s, -0.177f * s); quadraticTo(-0.156f * s, -0.185f * s, -0.128f * s, -0.247f * s); close() }
    val p2 = Path().apply { moveTo(-0.154f * s, -0.076f * s); cubicTo(-0.182f * s, -0.036f * s, -0.222f * s, -0.017f * s, -0.244f * s, -0.032f * s); cubicTo(-0.266f * s, -0.047f * s, -0.261f * s, -0.092f * s, -0.234f * s, -0.131f * s); cubicTo(-0.206f * s, -0.171f * s, -0.166f * s, -0.19f * s, -0.144f * s, -0.175f * s); cubicTo(-0.122f * s, -0.16f * s, -0.127f * s, -0.115f * s, -0.154f * s, -0.076f * s); close() }
    drawPath(p0, sil ?: Color(0xFFCCA832))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFFD23F))
        }
        drawPath(p2, Color(0x8CFFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artSoleil(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.023f * s, -0.411f * s); quadraticTo(0.0f * s, -0.456f * s, 0.023f * s, -0.411f * s); lineTo(0.057f * s, -0.344f * s); quadraticTo(0.08f * s, -0.299f * s, 0.122f * s, -0.327f * s); lineTo(0.186f * s, -0.368f * s); quadraticTo(0.228f * s, -0.395f * s, 0.226f * s, -0.345f * s); lineTo(0.222f * s, -0.269f * s); quadraticTo(0.219f * s, -0.219f * s, 0.269f * s, -0.222f * s); lineTo(0.345f * s, -0.225f * s); quadraticTo(0.395f * s, -0.228f * s, 0.368f * s, -0.186f * s); lineTo(0.327f * s, -0.122f * s); quadraticTo(0.299f * s, -0.08f * s, 0.344f * s, -0.057f * s); lineTo(0.411f * s, -0.023f * s); quadraticTo(0.456f * s, 0.0f * s, 0.411f * s, 0.023f * s); lineTo(0.344f * s, 0.057f * s); quadraticTo(0.299f * s, 0.08f * s, 0.327f * s, 0.122f * s); lineTo(0.368f * s, 0.186f * s); quadraticTo(0.395f * s, 0.228f * s, 0.345f * s, 0.226f * s); lineTo(0.269f * s, 0.222f * s); quadraticTo(0.219f * s, 0.219f * s, 0.222f * s, 0.269f * s); lineTo(0.226f * s, 0.345f * s); quadraticTo(0.228f * s, 0.395f * s, 0.186f * s, 0.368f * s); lineTo(0.122f * s, 0.327f * s); quadraticTo(0.08f * s, 0.299f * s, 0.057f * s, 0.344f * s); lineTo(0.023f * s, 0.411f * s); quadraticTo(0.0f * s, 0.456f * s, -0.023f * s, 0.411f * s); lineTo(-0.057f * s, 0.344f * s); quadraticTo(-0.08f * s, 0.299f * s, -0.122f * s, 0.327f * s); lineTo(-0.186f * s, 0.368f * s); quadraticTo(-0.228f * s, 0.395f * s, -0.225f * s, 0.345f * s); lineTo(-0.222f * s, 0.269f * s); quadraticTo(-0.219f * s, 0.219f * s, -0.269f * s, 0.222f * s); lineTo(-0.345f * s, 0.226f * s); quadraticTo(-0.395f * s, 0.228f * s, -0.368f * s, 0.186f * s); lineTo(-0.327f * s, 0.122f * s); quadraticTo(-0.299f * s, 0.08f * s, -0.344f * s, 0.057f * s); lineTo(-0.411f * s, 0.023f * s); quadraticTo(-0.456f * s, 0.0f * s, -0.411f * s, -0.023f * s); lineTo(-0.344f * s, -0.057f * s); quadraticTo(-0.299f * s, -0.08f * s, -0.327f * s, -0.122f * s); lineTo(-0.368f * s, -0.186f * s); quadraticTo(-0.395f * s, -0.228f * s, -0.345f * s, -0.225f * s); lineTo(-0.269f * s, -0.222f * s); quadraticTo(-0.219f * s, -0.219f * s, -0.222f * s, -0.269f * s); lineTo(-0.225f * s, -0.345f * s); quadraticTo(-0.228f * s, -0.395f * s, -0.186f * s, -0.368f * s); lineTo(-0.122f * s, -0.327f * s); quadraticTo(-0.08f * s, -0.299f * s, -0.057f * s, -0.344f * s); close() }
    val p1 = Path().apply { moveTo(-0.037f * s, -0.434f * s); quadraticTo(-0.014f * s, -0.479f * s, 0.009f * s, -0.434f * s); lineTo(0.044f * s, -0.367f * s); quadraticTo(0.067f * s, -0.322f * s, 0.109f * s, -0.349f * s); lineTo(0.172f * s, -0.39f * s); quadraticTo(0.214f * s, -0.418f * s, 0.212f * s, -0.367f * s); lineTo(0.208f * s, -0.292f * s); quadraticTo(0.206f * s, -0.242f * s, 0.256f * s, -0.244f * s); lineTo(0.331f * s, -0.248f * s); quadraticTo(0.381f * s, -0.251f * s, 0.354f * s, -0.209f * s); lineTo(0.313f * s, -0.145f * s); quadraticTo(0.286f * s, -0.103f * s, 0.33f * s, -0.08f * s); lineTo(0.398f * s, -0.046f * s); quadraticTo(0.442f * s, -0.023f * s, 0.398f * s, 0.0f * s); lineTo(0.33f * s, 0.035f * s); quadraticTo(0.286f * s, 0.057f * s, 0.313f * s, 0.1f * s); lineTo(0.354f * s, 0.163f * s); quadraticTo(0.381f * s, 0.205f * s, 0.331f * s, 0.203f * s); lineTo(0.256f * s, 0.199f * s); quadraticTo(0.206f * s, 0.196f * s, 0.208f * s, 0.246f * s); lineTo(0.212f * s, 0.322f * s); quadraticTo(0.214f * s, 0.372f * s, 0.172f * s, 0.345f * s); lineTo(0.109f * s, 0.304f * s); quadraticTo(0.067f * s, 0.277f * s, 0.044f * s, 0.321f * s); lineTo(0.009f * s, 0.389f * s); quadraticTo(-0.014f * s, 0.433f * s, -0.037f * s, 0.389f * s); lineTo(-0.071f * s, 0.321f * s); quadraticTo(-0.094f * s, 0.277f * s, -0.136f * s, 0.304f * s); lineTo(-0.199f * s, 0.345f * s); quadraticTo(-0.242f * s, 0.372f * s, -0.239f * s, 0.322f * s); lineTo(-0.235f * s, 0.246f * s); quadraticTo(-0.233f * s, 0.196f * s, -0.283f * s, 0.199f * s); lineTo(-0.358f * s, 0.203f * s); quadraticTo(-0.408f * s, 0.205f * s, -0.381f * s, 0.163f * s); lineTo(-0.34f * s, 0.1f * s); quadraticTo(-0.313f * s, 0.057f * s, -0.358f * s, 0.035f * s); lineTo(-0.425f * s, 0.0f * s); quadraticTo(-0.47f * s, -0.023f * s, -0.425f * s, -0.046f * s); lineTo(-0.358f * s, -0.08f * s); quadraticTo(-0.313f * s, -0.103f * s, -0.34f * s, -0.145f * s); lineTo(-0.381f * s, -0.209f * s); quadraticTo(-0.408f * s, -0.251f * s, -0.358f * s, -0.248f * s); lineTo(-0.283f * s, -0.244f * s); quadraticTo(-0.233f * s, -0.242f * s, -0.235f * s, -0.292f * s); lineTo(-0.239f * s, -0.367f * s); quadraticTo(-0.242f * s, -0.418f * s, -0.199f * s, -0.39f * s); lineTo(-0.136f * s, -0.349f * s); quadraticTo(-0.094f * s, -0.322f * s, -0.071f * s, -0.367f * s); close() }
    val p2 = Path().apply { moveTo(0.274f * s, 0.0f * s); cubicTo(0.274f * s, 0.151f * s, 0.151f * s, 0.274f * s, 0.0f * s, 0.274f * s); cubicTo(-0.151f * s, 0.274f * s, -0.273f * s, 0.151f * s, -0.273f * s, 0.0f * s); cubicTo(-0.273f * s, -0.151f * s, -0.151f * s, -0.273f * s, 0.0f * s, -0.273f * s); cubicTo(0.151f * s, -0.273f * s, 0.274f * s, -0.151f * s, 0.274f * s, 0.0f * s); close() }
    val p3 = Path().apply { moveTo(0.255f * s, -0.036f * s); cubicTo(0.255f * s, 0.115f * s, 0.133f * s, 0.237f * s, -0.018f * s, 0.237f * s); cubicTo(-0.169f * s, 0.237f * s, -0.292f * s, 0.115f * s, -0.292f * s, -0.036f * s); cubicTo(-0.292f * s, -0.187f * s, -0.169f * s, -0.31f * s, -0.018f * s, -0.31f * s); cubicTo(0.133f * s, -0.31f * s, 0.255f * s, -0.187f * s, 0.255f * s, -0.036f * s); close() }
    val p4 = Path().apply { moveTo(-0.099f * s, -0.154f * s); cubicTo(-0.076f * s, -0.121f * s, -0.074f * s, -0.082f * s, -0.095f * s, -0.068f * s); cubicTo(-0.115f * s, -0.053f * s, -0.151f * s, -0.068f * s, -0.174f * s, -0.101f * s); cubicTo(-0.197f * s, -0.134f * s, -0.199f * s, -0.173f * s, -0.179f * s, -0.187f * s); cubicTo(-0.158f * s, -0.202f * s, -0.122f * s, -0.187f * s, -0.099f * s, -0.154f * s); close() }
    val p5 = Path().apply { moveTo(-0.077f * s, 0.064f * s); quadraticTo(0.0f * s, 0.155f * s, 0.078f * s, 0.064f * s) }
    drawPath(p0, sil ?: Color(0xFFD1921E))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFFB224))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFCCA832))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFFFD23F))
        }
        drawPath(p4, Color(0x8CFFFFFF))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.033f * s, Offset(-0.091f * s, -0.036f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(-0.101f * s, -0.048f * s))
        drawCircle(Color(0xFF2B2D42), 0.033f * s, Offset(0.091f * s, -0.036f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(0.081f * s, -0.048f * s))
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(-0.219f * s, 0.024f * s), Size(0.091f * s, 0.062f * s))
        drawOval(Color(0x66FF4F86), Offset(0.128f * s, 0.024f * s), Size(0.091f * s, 0.062f * s))
    }
}

private fun DrawScope.artPoussin(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.036f * s, 0.384f * s); cubicTo(-0.036f * s, 0.411f * s, -0.077f * s, 0.434f * s, -0.127f * s, 0.434f * s); cubicTo(-0.177f * s, 0.434f * s, -0.218f * s, 0.411f * s, -0.218f * s, 0.384f * s); cubicTo(-0.218f * s, 0.356f * s, -0.177f * s, 0.334f * s, -0.127f * s, 0.334f * s); cubicTo(-0.077f * s, 0.334f * s, -0.036f * s, 0.356f * s, -0.036f * s, 0.384f * s); close() }
    val p1 = Path().apply { moveTo(0.218f * s, 0.384f * s); cubicTo(0.218f * s, 0.411f * s, 0.177f * s, 0.434f * s, 0.127f * s, 0.434f * s); cubicTo(0.077f * s, 0.434f * s, 0.036f * s, 0.411f * s, 0.036f * s, 0.384f * s); cubicTo(0.036f * s, 0.356f * s, 0.077f * s, 0.334f * s, 0.127f * s, 0.334f * s); cubicTo(0.177f * s, 0.334f * s, 0.218f * s, 0.356f * s, 0.218f * s, 0.384f * s); close() }
    val p2 = Path().apply { moveTo(0.0f * s, -0.288f * s); quadraticTo(0.082f * s, -0.361f * s, 0.0f * s, -0.434f * s); quadraticTo(-0.082f * s, -0.361f * s, 0.0f * s, -0.288f * s); close() }
    val p3 = Path().apply { moveTo(-0.073f * s, -0.288f * s); quadraticTo(-0.054f * s, -0.389f * s, -0.154f * s, -0.406f * s); quadraticTo(-0.173f * s, -0.306f * s, -0.073f * s, -0.288f * s); close() }
    val p4 = Path().apply { moveTo(0.073f * s, -0.288f * s); quadraticTo(0.173f * s, -0.306f * s, 0.154f * s, -0.406f * s); quadraticTo(0.054f * s, -0.389f * s, 0.073f * s, -0.288f * s); close() }
    val p5 = Path().apply { moveTo(0.372f * s, 0.02f * s); cubicTo(0.372f * s, 0.211f * s, 0.206f * s, 0.366f * s, 0.0f * s, 0.366f * s); cubicTo(-0.206f * s, 0.366f * s, -0.372f * s, 0.211f * s, -0.372f * s, 0.02f * s); cubicTo(-0.372f * s, -0.17f * s, -0.206f * s, -0.325f * s, 0.0f * s, -0.325f * s); cubicTo(0.206f * s, -0.325f * s, 0.372f * s, -0.17f * s, 0.372f * s, 0.02f * s); close() }
    val p6 = Path().apply { moveTo(0.354f * s, -0.025f * s); cubicTo(0.354f * s, 0.166f * s, 0.187f * s, 0.32f * s, -0.018f * s, 0.32f * s); cubicTo(-0.224f * s, 0.32f * s, -0.39f * s, 0.166f * s, -0.39f * s, -0.025f * s); cubicTo(-0.39f * s, -0.216f * s, -0.224f * s, -0.37f * s, -0.018f * s, -0.37f * s); cubicTo(0.187f * s, -0.37f * s, 0.354f * s, -0.216f * s, 0.354f * s, -0.025f * s); close() }
    val p7 = Path().apply { moveTo(-0.173f * s, -0.192f * s); cubicTo(-0.147f * s, -0.155f * s, -0.146f * s, -0.111f * s, -0.171f * s, -0.094f * s); cubicTo(-0.196f * s, -0.077f * s, -0.237f * s, -0.093f * s, -0.263f * s, -0.13f * s); cubicTo(-0.288f * s, -0.167f * s, -0.289f * s, -0.211f * s, -0.265f * s, -0.228f * s); cubicTo(-0.24f * s, -0.245f * s, -0.199f * s, -0.229f * s, -0.173f * s, -0.192f * s); close() }
    val p8 = Path().apply { moveTo(-0.206f * s, 0.127f * s); cubicTo(-0.223f * s, 0.206f * s, -0.27f * s, 0.262f * s, -0.312f * s, 0.253f * s); cubicTo(-0.353f * s, 0.245f * s, -0.374f * s, 0.174f * s, -0.357f * s, 0.095f * s); cubicTo(-0.34f * s, 0.017f * s, -0.293f * s, -0.04f * s, -0.251f * s, -0.031f * s); cubicTo(-0.21f * s, -0.022f * s, -0.189f * s, 0.049f * s, -0.206f * s, 0.127f * s); close() }
    val p9 = Path().apply { moveTo(0.357f * s, 0.095f * s); cubicTo(0.374f * s, 0.174f * s, 0.354f * s, 0.245f * s, 0.312f * s, 0.253f * s); cubicTo(0.27f * s, 0.262f * s, 0.223f * s, 0.206f * s, 0.206f * s, 0.127f * s); cubicTo(0.189f * s, 0.049f * s, 0.21f * s, -0.022f * s, 0.251f * s, -0.031f * s); cubicTo(0.293f * s, -0.04f * s, 0.34f * s, 0.017f * s, 0.357f * s, 0.095f * s); close() }
    val p10 = Path().apply { moveTo(-0.059f * s, 0.039f * s); quadraticTo(-0.073f * s, 0.02f * s, -0.05f * s, 0.02f * s); lineTo(0.05f * s, 0.02f * s); quadraticTo(0.073f * s, 0.02f * s, 0.059f * s, 0.039f * s); lineTo(0.013f * s, 0.102f * s); quadraticTo(0.0f * s, 0.12f * s, -0.013f * s, 0.102f * s); close() }
    drawPath(p0, sil ?: Color(0xFFFF8A1F))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFFF8A1F))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFF2C83C))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFF2C83C))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFF2C83C))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFCCA832))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFFFFD23F))
        }
        drawPath(p7, Color(0x8CFFFFFF))
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFE6BD39))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFE6BD39))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.056f * s, Offset(-0.136f * s, -0.07f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.056f * s, Offset(-0.136f * s, -0.07f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.033f * s, Offset(-0.133f * s, -0.059f * s))
        drawCircle(Color(0xFFFFFFFF), 0.011f * s, Offset(-0.146f * s, -0.074f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.056f * s, Offset(0.136f * s, -0.07f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.056f * s, Offset(0.136f * s, -0.07f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.033f * s, Offset(0.133f * s, -0.059f * s))
        drawCircle(Color(0xFFFFFFFF), 0.011f * s, Offset(0.121f * s, -0.074f * s))
    }
    drawPath(p10, sil ?: Color(0xFFFF8A1F))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(-0.254f * s, 0.049f * s), Size(0.073f * s, 0.051f * s))
        drawOval(Color(0x66FF4F86), Offset(0.182f * s, 0.049f * s), Size(0.073f * s, 0.051f * s))
    }
}

private fun DrawScope.artFromage(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.281f * s, 0.324f * s); quadraticTo(0.261f * s, 0.347f * s, 0.261f * s, 0.317f * s); lineTo(0.261f * s, -0.126f * s); quadraticTo(0.261f * s, -0.156f * s, 0.281f * s, -0.179f * s); lineTo(0.412f * s, -0.334f * s); quadraticTo(0.432f * s, -0.357f * s, 0.432f * s, -0.327f * s); lineTo(0.432f * s, 0.116f * s); quadraticTo(0.432f * s, 0.146f * s, 0.412f * s, 0.169f * s); close() }
    val p1 = Path().apply { moveTo(-0.423f * s, 0.324f * s); quadraticTo(-0.443f * s, 0.347f * s, -0.418f * s, 0.329f * s); lineTo(0.237f * s, -0.138f * s); quadraticTo(0.261f * s, -0.156f * s, 0.281f * s, -0.179f * s); lineTo(0.412f * s, -0.334f * s); quadraticTo(0.432f * s, -0.357f * s, 0.407f * s, -0.339f * s); lineTo(-0.247f * s, 0.128f * s); quadraticTo(-0.272f * s, 0.146f * s, -0.291f * s, 0.169f * s); close() }
    val p2 = Path().apply { moveTo(-0.41f * s, 0.324f * s); quadraticTo(-0.443f * s, 0.347f * s, -0.403f * s, 0.347f * s); lineTo(0.221f * s, 0.347f * s); quadraticTo(0.261f * s, 0.347f * s, 0.261f * s, 0.307f * s); lineTo(0.261f * s, -0.116f * s); quadraticTo(0.261f * s, -0.156f * s, 0.228f * s, -0.132f * s); close() }
    val p3 = Path().apply { moveTo(-0.43f * s, 0.293f * s); quadraticTo(-0.463f * s, 0.317f * s, -0.423f * s, 0.317f * s); lineTo(0.201f * s, 0.317f * s); quadraticTo(0.241f * s, 0.317f * s, 0.241f * s, 0.277f * s); lineTo(0.241f * s, -0.146f * s); quadraticTo(0.241f * s, -0.186f * s, 0.208f * s, -0.163f * s); close() }
    val p4 = Path().apply { moveTo(0.155f * s, 0.146f * s); cubicTo(0.155f * s, 0.183f * s, 0.122f * s, 0.214f * s, 0.08f * s, 0.214f * s); cubicTo(0.038f * s, 0.214f * s, 0.005f * s, 0.183f * s, 0.005f * s, 0.146f * s); cubicTo(0.005f * s, 0.108f * s, 0.038f * s, 0.078f * s, 0.08f * s, 0.078f * s); cubicTo(0.122f * s, 0.078f * s, 0.155f * s, 0.108f * s, 0.155f * s, 0.146f * s); close() }
    val p5 = Path().apply { moveTo(-0.071f * s, 0.267f * s); cubicTo(-0.071f * s, 0.292f * s, -0.093f * s, 0.312f * s, -0.121f * s, 0.312f * s); cubicTo(-0.149f * s, 0.312f * s, -0.171f * s, 0.292f * s, -0.171f * s, 0.267f * s); cubicTo(-0.171f * s, 0.241f * s, -0.149f * s, 0.221f * s, -0.121f * s, 0.221f * s); cubicTo(-0.093f * s, 0.221f * s, -0.071f * s, 0.241f * s, -0.071f * s, 0.267f * s); close() }
    val p6 = Path().apply { moveTo(0.191f * s, 0.287f * s); cubicTo(0.191f * s, 0.307f * s, 0.173f * s, 0.323f * s, 0.15f * s, 0.323f * s); cubicTo(0.128f * s, 0.323f * s, 0.11f * s, 0.307f * s, 0.11f * s, 0.287f * s); cubicTo(0.11f * s, 0.267f * s, 0.128f * s, 0.25f * s, 0.15f * s, 0.25f * s); cubicTo(0.173f * s, 0.25f * s, 0.191f * s, 0.267f * s, 0.191f * s, 0.287f * s); close() }
    val p7 = Path().apply { moveTo(0.211f * s, -0.015f * s); cubicTo(0.211f * s, 0.005f * s, 0.193f * s, 0.021f * s, 0.171f * s, 0.021f * s); cubicTo(0.148f * s, 0.021f * s, 0.13f * s, 0.005f * s, 0.13f * s, -0.015f * s); cubicTo(0.13f * s, -0.035f * s, 0.148f * s, -0.051f * s, 0.171f * s, -0.051f * s); cubicTo(0.193f * s, -0.051f * s, 0.211f * s, -0.035f * s, 0.211f * s, -0.015f * s); close() }
    drawPath(p0, sil ?: Color(0xFFE4A31A))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFFFE680))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFDBB536))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFFFD23F))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFF0B01C))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFF0B01C))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFF0B01C))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFF0B01C))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artGrenouille(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.151f * s, 0.357f * s); cubicTo(-0.151f * s, 0.39f * s, -0.209f * s, 0.417f * s, -0.281f * s, 0.417f * s); cubicTo(-0.353f * s, 0.417f * s, -0.412f * s, 0.39f * s, -0.412f * s, 0.357f * s); cubicTo(-0.412f * s, 0.323f * s, -0.353f * s, 0.296f * s, -0.281f * s, 0.296f * s); cubicTo(-0.209f * s, 0.296f * s, -0.151f * s, 0.323f * s, -0.151f * s, 0.357f * s); close() }
    val p1 = Path().apply { moveTo(0.412f * s, 0.357f * s); cubicTo(0.412f * s, 0.39f * s, 0.353f * s, 0.417f * s, 0.281f * s, 0.417f * s); cubicTo(0.209f * s, 0.417f * s, 0.151f * s, 0.39f * s, 0.151f * s, 0.357f * s); cubicTo(0.151f * s, 0.323f * s, 0.209f * s, 0.296f * s, 0.281f * s, 0.296f * s); cubicTo(0.353f * s, 0.296f * s, 0.412f * s, 0.323f * s, 0.412f * s, 0.357f * s); close() }
    val p2 = Path().apply { moveTo(0.432f * s, 0.055f * s); cubicTo(0.432f * s, 0.244f * s, 0.239f * s, 0.397f * s, 0.0f * s, 0.397f * s); cubicTo(-0.239f * s, 0.397f * s, -0.432f * s, 0.244f * s, -0.432f * s, 0.055f * s); cubicTo(-0.432f * s, -0.133f * s, -0.239f * s, -0.286f * s, 0.0f * s, -0.286f * s); cubicTo(0.239f * s, -0.286f * s, 0.432f * s, -0.133f * s, 0.432f * s, 0.055f * s); close() }
    val p3 = Path().apply { moveTo(0.412f * s, 0.005f * s); cubicTo(0.412f * s, 0.194f * s, 0.219f * s, 0.347f * s, -0.02f * s, 0.347f * s); cubicTo(-0.259f * s, 0.347f * s, -0.452f * s, 0.194f * s, -0.452f * s, 0.005f * s); cubicTo(-0.452f * s, -0.184f * s, -0.259f * s, -0.336f * s, -0.02f * s, -0.336f * s); cubicTo(0.219f * s, -0.336f * s, 0.412f * s, -0.184f * s, 0.412f * s, 0.005f * s); close() }
    val p4 = Path().apply { moveTo(-0.03f * s, -0.246f * s); cubicTo(-0.03f * s, -0.152f * s, -0.107f * s, -0.075f * s, -0.201f * s, -0.075f * s); cubicTo(-0.295f * s, -0.075f * s, -0.372f * s, -0.152f * s, -0.372f * s, -0.246f * s); cubicTo(-0.372f * s, -0.34f * s, -0.295f * s, -0.417f * s, -0.201f * s, -0.417f * s); cubicTo(-0.107f * s, -0.417f * s, -0.03f * s, -0.34f * s, -0.03f * s, -0.246f * s); close() }
    val p5 = Path().apply { moveTo(-0.045f * s, -0.281f * s); cubicTo(-0.045f * s, -0.187f * s, -0.122f * s, -0.11f * s, -0.216f * s, -0.11f * s); cubicTo(-0.31f * s, -0.11f * s, -0.387f * s, -0.187f * s, -0.387f * s, -0.281f * s); cubicTo(-0.387f * s, -0.376f * s, -0.31f * s, -0.452f * s, -0.216f * s, -0.452f * s); cubicTo(-0.122f * s, -0.452f * s, -0.045f * s, -0.376f * s, -0.045f * s, -0.281f * s); close() }
    val p6 = Path().apply { moveTo(0.372f * s, -0.246f * s); cubicTo(0.372f * s, -0.152f * s, 0.295f * s, -0.075f * s, 0.201f * s, -0.075f * s); cubicTo(0.107f * s, -0.075f * s, 0.03f * s, -0.152f * s, 0.03f * s, -0.246f * s); cubicTo(0.03f * s, -0.34f * s, 0.107f * s, -0.417f * s, 0.201f * s, -0.417f * s); cubicTo(0.295f * s, -0.417f * s, 0.372f * s, -0.34f * s, 0.372f * s, -0.246f * s); close() }
    val p7 = Path().apply { moveTo(0.357f * s, -0.281f * s); cubicTo(0.357f * s, -0.187f * s, 0.28f * s, -0.11f * s, 0.186f * s, -0.11f * s); cubicTo(0.092f * s, -0.11f * s, 0.015f * s, -0.187f * s, 0.015f * s, -0.281f * s); cubicTo(0.015f * s, -0.376f * s, 0.092f * s, -0.452f * s, 0.186f * s, -0.452f * s); cubicTo(0.28f * s, -0.452f * s, 0.357f * s, -0.376f * s, 0.357f * s, -0.281f * s); close() }
    val p8 = Path().apply { moveTo(-0.241f * s, 0.025f * s); quadraticTo(0.0f * s, 0.226f * s, 0.241f * s, 0.025f * s) }
    drawPath(p0, sil ?: Color(0xFF42AD45))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF42AD45))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF37923A))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF45B649))
        }
        clipPath(p2) {
            drawOval(Color(0xFFD4F5A8), Offset(-0.301f * s, 0.055f * s), Size(0.603f * s, 0.402f * s))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF37923A))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFF45B649))
        }
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.105f * s, Offset(-0.201f * s, -0.256f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.105f * s, Offset(-0.201f * s, -0.256f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.061f * s, Offset(-0.19f * s, -0.23f * s))
        drawCircle(Color(0xFFFFFFFF), 0.021f * s, Offset(-0.214f * s, -0.257f * s))
    }
    drawPath(p6, sil ?: Color(0xFF37923A))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFF45B649))
        }
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.105f * s, Offset(0.201f * s, -0.256f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.105f * s, Offset(0.201f * s, -0.256f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.061f * s, Offset(0.19f * s, -0.23f * s))
        drawCircle(Color(0xFFFFFFFF), 0.021f * s, Offset(0.167f * s, -0.257f * s))
    }
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.028f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.014f * s, Offset(-0.04f * s, -0.065f * s))
        drawCircle(Color(0xFF2B2D42), 0.014f * s, Offset(0.04f * s, -0.065f * s))
        drawOval(Color(0x66FF4F86), Offset(-0.352f * s, 0.041f * s), Size(0.1f * s, 0.068f * s))
        drawOval(Color(0x66FF4F86), Offset(0.251f * s, 0.041f * s), Size(0.1f * s, 0.068f * s))
    }
}

private fun DrawScope.artSapin(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.036f * s, -0.031f * s); quadraticTo(0.0f * s, -0.062f * s, 0.036f * s, -0.031f * s); lineTo(0.373f * s, 0.25f * s); quadraticTo(0.409f * s, 0.281f * s, 0.362f * s, 0.281f * s); lineTo(-0.362f * s, 0.281f * s); quadraticTo(-0.409f * s, 0.281f * s, -0.373f * s, 0.25f * s); close() }
    val p1 = Path().apply { moveTo(-0.032f * s, -0.227f * s); quadraticTo(0.0f * s, -0.262f * s, 0.032f * s, -0.227f * s); lineTo(0.291f * s, 0.055f * s); quadraticTo(0.324f * s, 0.09f * s, 0.276f * s, 0.09f * s); lineTo(-0.276f * s, 0.09f * s); quadraticTo(-0.323f * s, 0.09f * s, -0.291f * s, 0.055f * s); close() }
    val p2 = Path().apply { moveTo(-0.028f * s, -0.414f * s); quadraticTo(0.0f * s, -0.452f * s, 0.028f * s, -0.414f * s); lineTo(0.2f * s, -0.176f * s); quadraticTo(0.228f * s, -0.138f * s, 0.181f * s, -0.138f * s); lineTo(-0.181f * s, -0.138f * s); quadraticTo(-0.228f * s, -0.138f * s, -0.2f * s, -0.176f * s); close() }
    val p3 = Path().apply { moveTo(-0.047f * s, -0.442f * s); quadraticTo(-0.019f * s, -0.481f * s, 0.009f * s, -0.442f * s); lineTo(0.181f * s, -0.205f * s); quadraticTo(0.209f * s, -0.167f * s, 0.162f * s, -0.167f * s); lineTo(-0.2f * s, -0.167f * s); quadraticTo(-0.247f * s, -0.167f * s, -0.219f * s, -0.205f * s); close() }
    val p4 = Path().apply { moveTo(-0.044f * s, -0.234f * s); cubicTo(-0.058f * s, -0.2f * s, -0.079f * s, -0.176f * s, -0.091f * s, -0.181f * s); cubicTo(-0.104f * s, -0.186f * s, -0.102f * s, -0.217f * s, -0.089f * s, -0.252f * s); cubicTo(-0.075f * s, -0.286f * s, -0.054f * s, -0.309f * s, -0.042f * s, -0.304f * s); cubicTo(-0.029f * s, -0.3f * s, -0.031f * s, -0.268f * s, -0.044f * s, -0.234f * s); close() }
    drawRoundRect(sil ?: Color(0xFF7A4A2A), Offset(-0.067f * s, 0.262f * s), Size(0.133f * s, 0.171f * s), CornerRadius(0.029f * s))
    drawRoundRect(sil ?: Color(0xFF2B2D42), Offset(-0.067f * s, 0.262f * s), Size(0.133f * s, 0.171f * s), CornerRadius(0.029f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF2F9E47))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF3DB554))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF44A34F))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF55CC63))
        }
        drawPath(p4, Color(0x8CFFFFFF))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artPoire(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.272f * s); cubicTo(0.106f * s, -0.29f * s, 0.15f * s, -0.166f * s, 0.168f * s, -0.078f * s); cubicTo(0.194f * s, 0.028f * s, 0.406f * s, 0.081f * s, 0.406f * s, 0.24f * s); cubicTo(0.406f * s, 0.381f * s, 0.247f * s, 0.434f * s, 0.0f * s, 0.434f * s); cubicTo(-0.247f * s, 0.434f * s, -0.406f * s, 0.381f * s, -0.406f * s, 0.24f * s); cubicTo(-0.406f * s, 0.081f * s, -0.194f * s, 0.028f * s, -0.168f * s, -0.078f * s); cubicTo(-0.15f * s, -0.166f * s, -0.106f * s, -0.29f * s, 0.0f * s, -0.272f * s); close() }
    val p1 = Path().apply { moveTo(-0.022f * s, -0.317f * s); cubicTo(0.084f * s, -0.334f * s, 0.128f * s, -0.211f * s, 0.146f * s, -0.122f * s); cubicTo(0.172f * s, -0.016f * s, 0.384f * s, 0.037f * s, 0.384f * s, 0.196f * s); cubicTo(0.384f * s, 0.337f * s, 0.225f * s, 0.39f * s, -0.022f * s, 0.39f * s); cubicTo(-0.269f * s, 0.39f * s, -0.428f * s, 0.337f * s, -0.428f * s, 0.196f * s); cubicTo(-0.428f * s, 0.037f * s, -0.216f * s, -0.016f * s, -0.19f * s, -0.122f * s); cubicTo(-0.172f * s, -0.211f * s, -0.128f * s, -0.334f * s, -0.022f * s, -0.317f * s); close() }
    val p2 = Path().apply { moveTo(-0.187f * s, 0.191f * s); cubicTo(-0.175f * s, 0.238f * s, -0.187f * s, 0.282f * s, -0.216f * s, 0.29f * s); cubicTo(-0.244f * s, 0.297f * s, -0.277f * s, 0.265f * s, -0.29f * s, 0.218f * s); cubicTo(-0.302f * s, 0.171f * s, -0.29f * s, 0.127f * s, -0.261f * s, 0.119f * s); cubicTo(-0.233f * s, 0.112f * s, -0.2f * s, 0.144f * s, -0.187f * s, 0.191f * s); close() }
    val p3 = Path().apply { moveTo(-0.04f * s, -0.146f * s); cubicTo(-0.043f * s, -0.112f * s, -0.056f * s, -0.086f * s, -0.068f * s, -0.087f * s); cubicTo(-0.08f * s, -0.089f * s, -0.087f * s, -0.117f * s, -0.084f * s, -0.151f * s); cubicTo(-0.08f * s, -0.185f * s, -0.067f * s, -0.212f * s, -0.055f * s, -0.21f * s); cubicTo(-0.043f * s, -0.209f * s, -0.036f * s, -0.18f * s, -0.04f * s, -0.146f * s); close() }
    val p4 = Path().apply { moveTo(0.0f * s, -0.264f * s); quadraticTo(0.0f * s, -0.343f * s, 0.044f * s, -0.396f * s) }
    val p5 = Path().apply { moveTo(0.027f * s, -0.325f * s); quadraticTo(0.173f * s, -0.231f * s, 0.265f * s, -0.378f * s); quadraticTo(0.119f * s, -0.473f * s, 0.027f * s, -0.325f * s); close() }
    drawPath(p0, sil ?: Color(0xFF639E32))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF7CC63F))
        }
        drawPath(p2, Color(0x8CFFFFFF))
        drawPath(p3, Color(0x8CFFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF7A4A2A), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF3FAE49))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artTortue(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.34f * s, 0.105f * s); quadraticTo(-0.347f * s, 0.082f * s, -0.362f * s, 0.101f * s); lineTo(-0.429f * s, 0.185f * s); quadraticTo(-0.445f * s, 0.204f * s, -0.42f * s, 0.201f * s); lineTo(-0.339f * s, 0.19f * s); quadraticTo(-0.314f * s, 0.187f * s, -0.321f * s, 0.164f * s); close() }
    val p1 = Path().apply { moveTo(-0.131f * s, 0.261f * s); cubicTo(-0.131f * s, 0.306f * s, -0.169f * s, 0.342f * s, -0.217f * s, 0.342f * s); cubicTo(-0.264f * s, 0.342f * s, -0.302f * s, 0.306f * s, -0.302f * s, 0.261f * s); cubicTo(-0.302f * s, 0.216f * s, -0.264f * s, 0.179f * s, -0.217f * s, 0.179f * s); cubicTo(-0.169f * s, 0.179f * s, -0.131f * s, 0.216f * s, -0.131f * s, 0.261f * s); close() }
    val p2 = Path().apply { moveTo(0.228f * s, 0.261f * s); cubicTo(0.228f * s, 0.306f * s, 0.189f * s, 0.342f * s, 0.142f * s, 0.342f * s); cubicTo(0.095f * s, 0.342f * s, 0.056f * s, 0.306f * s, 0.056f * s, 0.261f * s); cubicTo(0.056f * s, 0.216f * s, 0.095f * s, 0.179f * s, 0.142f * s, 0.179f * s); cubicTo(0.189f * s, 0.179f * s, 0.228f * s, 0.216f * s, 0.228f * s, 0.261f * s); close() }
    val p3 = Path().apply { moveTo(0.435f * s, -0.024f * s); cubicTo(0.435f * s, 0.045f * s, 0.373f * s, 0.102f * s, 0.297f * s, 0.102f * s); cubicTo(0.22f * s, 0.102f * s, 0.158f * s, 0.045f * s, 0.158f * s, -0.024f * s); cubicTo(0.158f * s, -0.094f * s, 0.22f * s, -0.151f * s, 0.297f * s, -0.151f * s); cubicTo(0.373f * s, -0.151f * s, 0.435f * s, -0.094f * s, 0.435f * s, -0.024f * s); close() }
    val p4 = Path().apply { moveTo(-0.363f * s, 0.147f * s); cubicTo(-0.363f * s, -0.179f * s, -0.184f * s, -0.342f * s, -0.021f * s, -0.342f * s); cubicTo(0.142f * s, -0.342f * s, 0.256f * s, -0.179f * s, 0.256f * s, 0.147f * s); close() }
    val p5 = Path().apply { moveTo(-0.379f * s, 0.114f * s); cubicTo(-0.379f * s, -0.212f * s, -0.2f * s, -0.375f * s, -0.037f * s, -0.375f * s); cubicTo(0.126f * s, -0.375f * s, 0.24f * s, -0.212f * s, 0.24f * s, 0.114f * s); close() }
    val p6 = Path().apply { moveTo(-0.159f * s, -0.084f * s); quadraticTo(-0.168f * s, -0.098f * s, -0.159f * s, -0.112f * s); lineTo(-0.119f * s, -0.181f * s); quadraticTo(-0.111f * s, -0.196f * s, -0.094f * s, -0.196f * s); lineTo(-0.013f * s, -0.196f * s); quadraticTo(0.003f * s, -0.196f * s, 0.012f * s, -0.181f * s); lineTo(0.052f * s, -0.112f * s); quadraticTo(0.061f * s, -0.098f * s, 0.052f * s, -0.084f * s); lineTo(0.012f * s, -0.014f * s); quadraticTo(0.003f * s, 0.0f * s, -0.013f * s, 0.0f * s); lineTo(-0.094f * s, 0.0f * s); quadraticTo(-0.111f * s, 0.0f * s, -0.119f * s, -0.014f * s); close() }
    val p7 = Path().apply { moveTo(-0.054f * s, -0.342f * s); lineTo(-0.054f * s, -0.196f * s); moveTo(-0.168f * s, -0.098f * s); lineTo(-0.363f * s, -0.049f * s); moveTo(0.061f * s, -0.098f * s); lineTo(0.256f * s, -0.065f * s); moveTo(-0.111f * s, 0.0f * s); lineTo(-0.143f * s, 0.147f * s); moveTo(0.003f * s, 0.0f * s); lineTo(0.036f * s, 0.147f * s) }
    val p8 = Path().apply { moveTo(-0.347f * s, 0.122f * s); lineTo(0.24f * s, 0.122f * s); cubicTo(0.262f * s, 0.122f * s, 0.281f * s, 0.141f * s, 0.281f * s, 0.163f * s); lineTo(0.281f * s, 0.163f * s); cubicTo(0.281f * s, 0.186f * s, 0.262f * s, 0.204f * s, 0.24f * s, 0.204f * s); lineTo(-0.347f * s, 0.204f * s); cubicTo(-0.369f * s, 0.204f * s, -0.388f * s, 0.186f * s, -0.388f * s, 0.163f * s); lineTo(-0.388f * s, 0.163f * s); cubicTo(-0.388f * s, 0.141f * s, -0.369f * s, 0.122f * s, -0.347f * s, 0.122f * s); close() }
    val p9 = Path().apply { moveTo(0.309f * s, 0.041f * s); quadraticTo(0.346f * s, 0.082f * s, 0.382f * s, 0.041f * s) }
    drawPath(p0, sil ?: Color(0xFF7BCB4E))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF7BCB4E))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF7BCB4E))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF7BCB4E))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF267E36))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFF2F9E44))
        }
    }
    clipPath(p4) {
        drawPath(p6, sil ?: Color(0xFF58BD62))
        drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFE9D99B))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.049f * s, Offset(0.329f * s, -0.057f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.049f * s, Offset(0.329f * s, -0.057f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.028f * s, Offset(0.344f * s, -0.052f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(0.333f * s, -0.065f * s))
    }
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(0.248f * s, -0.002f * s), Size(0.049f * s, 0.036f * s))
    }
}

private fun DrawScope.artCactus(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.278f * s); lineTo(0.0f * s, 0.209f * s) }
    val p1 = Path().apply { moveTo(0.0f * s, 0.046f * s); lineTo(-0.189f * s, 0.046f * s); lineTo(-0.189f * s, -0.152f * s) }
    val p2 = Path().apply { moveTo(0.0f * s, 0.137f * s); lineTo(0.189f * s, 0.137f * s); lineTo(0.189f * s, -0.044f * s) }
    val p3 = Path().apply { moveTo(-0.167f * s, 0.226f * s); quadraticTo(-0.171f * s, 0.2f * s, -0.144f * s, 0.2f * s); lineTo(0.144f * s, 0.2f * s); quadraticTo(0.171f * s, 0.2f * s, 0.167f * s, 0.226f * s); lineTo(0.139f * s, 0.407f * s); quadraticTo(0.135f * s, 0.434f * s, 0.108f * s, 0.434f * s); lineTo(-0.108f * s, 0.434f * s); quadraticTo(-0.135f * s, 0.434f * s, -0.139f * s, 0.407f * s); close() }
    val p4 = Path().apply { moveTo(-0.189f * s, 0.155f * s); lineTo(0.189f * s, 0.155f * s); cubicTo(0.209f * s, 0.155f * s, 0.225f * s, 0.171f * s, 0.225f * s, 0.191f * s); lineTo(0.225f * s, 0.209f * s); cubicTo(0.225f * s, 0.229f * s, 0.209f * s, 0.245f * s, 0.189f * s, 0.245f * s); lineTo(-0.189f * s, 0.245f * s); cubicTo(-0.209f * s, 0.245f * s, -0.225f * s, 0.229f * s, -0.225f * s, 0.209f * s); lineTo(-0.225f * s, 0.191f * s); cubicTo(-0.225f * s, 0.171f * s, -0.209f * s, 0.155f * s, -0.189f * s, 0.155f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.342f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.222f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.222f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF3FB24A), style = Stroke(0.27f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF3FB24A), style = Stroke(0.15f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF3FB24A), style = Stroke(0.15f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawLine(Color(0x59FFFFFF), Offset(-0.063f * s, -0.278f * s), Offset(-0.063f * s, 0.118f * s), 0.04f * s, StrokeCap.Round)
        drawLine(Color(0xCC2A8A34), Offset(0.054f * s, -0.224f * s), Offset(0.054f * s, -0.026f * s), 0.022f * s, StrokeCap.Round)
    }
    drawPath(p3, sil ?: Color(0xFFD07A3E))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFE58A47))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFF7FB0), 0.045f * s, Offset(0.0f * s, -0.395f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.045f * s, Offset(0.0f * s, -0.395f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artBaleine(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.213f * s, -0.142f * s); quadraticTo(0.213f * s, -0.285f * s, 0.095f * s, -0.31f * s) }
    val p1 = Path().apply { moveTo(0.213f * s, -0.142f * s); quadraticTo(0.213f * s, -0.285f * s, 0.331f * s, -0.31f * s) }
    val p2 = Path().apply { moveTo(-0.14f * s, 0.22f * s); cubicTo(-0.258f * s, 0.203f * s, -0.325f * s, 0.102f * s, -0.317f * s, -0.016f * s); cubicTo(-0.426f * s, -0.032f * s, -0.46f * s, -0.15f * s, -0.417f * s, -0.251f * s); cubicTo(-0.359f * s, -0.234f * s, -0.325f * s, -0.184f * s, -0.3f * s, -0.117f * s); cubicTo(-0.275f * s, -0.184f * s, -0.232f * s, -0.251f * s, -0.165f * s, -0.251f * s); cubicTo(-0.14f * s, -0.15f * s, -0.207f * s, -0.066f * s, -0.207f * s, 0.035f * s); lineTo(-0.123f * s, 0.085f * s); close() }
    val p3 = Path().apply { moveTo(-0.207f * s, 0.085f * s); cubicTo(-0.106f * s, -0.1f * s, 0.095f * s, -0.167f * s, 0.247f * s, -0.1f * s); cubicTo(0.398f * s, -0.032f * s, 0.449f * s, 0.085f * s, 0.432f * s, 0.169f * s); cubicTo(0.381f * s, 0.338f * s, 0.129f * s, 0.371f * s, -0.039f * s, 0.321f * s); cubicTo(-0.14f * s, 0.287f * s, -0.207f * s, 0.186f * s, -0.207f * s, 0.085f * s); close() }
    val p4 = Path().apply { moveTo(-0.224f * s, 0.043f * s); cubicTo(-0.123f * s, -0.142f * s, 0.079f * s, -0.209f * s, 0.23f * s, -0.142f * s); cubicTo(0.381f * s, -0.074f * s, 0.432f * s, 0.043f * s, 0.415f * s, 0.127f * s); cubicTo(0.365f * s, 0.295f * s, 0.112f * s, 0.329f * s, -0.056f * s, 0.279f * s); cubicTo(-0.157f * s, 0.245f * s, -0.224f * s, 0.144f * s, -0.224f * s, 0.043f * s); close() }
    val p5 = Path().apply { moveTo(0.12f * s, -0.116f * s); cubicTo(0.121f * s, -0.102f * s, 0.071f * s, -0.084f * s, 0.007f * s, -0.075f * s); cubicTo(-0.058f * s, -0.066f * s, -0.112f * s, -0.07f * s, -0.114f * s, -0.083f * s); cubicTo(-0.116f * s, -0.097f * s, -0.065f * s, -0.116f * s, -0.001f * s, -0.125f * s); cubicTo(0.064f * s, -0.134f * s, 0.118f * s, -0.13f * s, 0.12f * s, -0.116f * s); close() }
    val p6 = Path().apply { moveTo(0.045f * s, 0.22f * s); quadraticTo(-0.082f * s, 0.213f * s, -0.056f * s, 0.338f * s); quadraticTo(0.071f * s, 0.344f * s, 0.045f * s, 0.22f * s); close() }
    val p7 = Path().apply { moveTo(0.297f * s, 0.144f * s); quadraticTo(0.348f * s, 0.203f * s, 0.398f * s, 0.144f * s) }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF8CCBFF), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF8CCBFF), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2A7BD6))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2671BC))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFF2F8DEB))
        }
        clipPath(p3) {
            drawOval(Color(0xFFDCEEFF), Offset(-0.333f * s, 0.178f * s), Size(0.841f * s, 0.252f * s))
        }
        drawPath(p5, Color(0x8CFFFFFF))
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF2A7BD6))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.046f * s, Offset(0.28f * s, 0.043f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.046f * s, Offset(0.28f * s, 0.043f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.027f * s, Offset(0.294f * s, 0.048f * s))
        drawCircle(Color(0xFFFFFFFF), 0.009f * s, Offset(0.284f * s, 0.036f * s))
    }
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artParapluie(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.046f * s); lineTo(0.0f * s, 0.266f * s); quadraticTo(0.0f * s, 0.394f * s, -0.11f * s, 0.394f * s); quadraticTo(-0.202f * s, 0.394f * s, -0.202f * s, 0.302f * s) }
    val p1 = Path().apply { moveTo(-0.422f * s, -0.028f * s); cubicTo(-0.422f * s, -0.285f * s, -0.22f * s, -0.413f * s, 0.0f * s, -0.413f * s); cubicTo(0.22f * s, -0.413f * s, 0.422f * s, -0.285f * s, 0.422f * s, -0.028f * s); quadraticTo(0.338f * s, -0.111f * s, 0.253f * s, -0.028f * s); quadraticTo(0.169f * s, -0.111f * s, 0.084f * s, -0.028f * s); quadraticTo(0.0f * s, -0.111f * s, -0.084f * s, -0.028f * s); quadraticTo(-0.169f * s, -0.111f * s, -0.253f * s, -0.028f * s); quadraticTo(-0.338f * s, -0.111f * s, -0.422f * s, -0.028f * s); close() }
    val p2 = Path().apply { moveTo(-0.44f * s, -0.065f * s); cubicTo(-0.44f * s, -0.322f * s, -0.238f * s, -0.45f * s, -0.018f * s, -0.45f * s); cubicTo(0.202f * s, -0.45f * s, 0.404f * s, -0.322f * s, 0.404f * s, -0.065f * s); quadraticTo(0.319f * s, -0.147f * s, 0.235f * s, -0.065f * s); quadraticTo(0.151f * s, -0.147f * s, 0.066f * s, -0.065f * s); quadraticTo(-0.018f * s, -0.147f * s, -0.103f * s, -0.065f * s); quadraticTo(-0.187f * s, -0.147f * s, -0.272f * s, -0.065f * s); quadraticTo(-0.356f * s, -0.147f * s, -0.44f * s, -0.065f * s); close() }
    val p3 = Path().apply { moveTo(0.0f * s, -0.413f * s); quadraticTo(0.241f * s, -0.322f * s, 0.253f * s, -0.028f * s); quadraticTo(0.169f * s, -0.111f * s, 0.084f * s, -0.028f * s); quadraticTo(0.08f * s, -0.322f * s, 0.0f * s, -0.413f * s); close() }
    val p4 = Path().apply { moveTo(0.0f * s, -0.413f * s); quadraticTo(-0.08f * s, -0.322f * s, -0.084f * s, -0.028f * s); quadraticTo(-0.169f * s, -0.111f * s, -0.253f * s, -0.028f * s); quadraticTo(-0.241f * s, -0.322f * s, 0.0f * s, -0.413f * s); close() }
    val p5 = Path().apply { moveTo(0.0f * s, -0.413f * s); quadraticTo(0.184f * s, -0.322f * s, 0.253f * s, -0.028f * s); moveTo(0.0f * s, -0.413f * s); quadraticTo(-0.183f * s, -0.322f * s, -0.253f * s, -0.028f * s); moveTo(0.0f * s, -0.413f * s); quadraticTo(0.092f * s, -0.322f * s, 0.084f * s, -0.028f * s); moveTo(0.0f * s, -0.413f * s); quadraticTo(-0.092f * s, -0.322f * s, -0.084f * s, -0.028f * s) }
    val p6 = Path().apply { moveTo(-0.246f * s, -0.274f * s); cubicTo(-0.215f * s, -0.248f * s, -0.203f * s, -0.211f * s, -0.219f * s, -0.192f * s); cubicTo(-0.235f * s, -0.172f * s, -0.274f * s, -0.178f * s, -0.305f * s, -0.204f * s); cubicTo(-0.336f * s, -0.23f * s, -0.348f * s, -0.267f * s, -0.331f * s, -0.286f * s); cubicTo(-0.315f * s, -0.306f * s, -0.277f * s, -0.3f * s, -0.246f * s, -0.274f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF3C4F86), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2671BC))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFF2F8DEB))
        }
        clipPath(p1) {
            drawPath(p3, Color(0xFF7DBBFF))
            drawPath(p4, Color(0xFF7DBBFF))
        }
    }
    clipPath(p1) {
        drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    if (sil == null) {
        drawPath(p6, Color(0x8CFFFFFF))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.028f * s, Offset(0.0f * s, -0.422f * s))
    }
}

private fun DrawScope.artGoutte(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.433f * s); cubicTo(0.094f * s, -0.264f * s, 0.377f * s, 0.019f * s, 0.377f * s, 0.188f * s); cubicTo(0.377f * s, 0.339f * s, 0.207f * s, 0.433f * s, 0.0f * s, 0.433f * s); cubicTo(-0.207f * s, 0.433f * s, -0.377f * s, 0.339f * s, -0.377f * s, 0.188f * s); cubicTo(-0.377f * s, 0.019f * s, -0.094f * s, -0.264f * s, 0.0f * s, -0.433f * s); close() }
    val p1 = Path().apply { moveTo(-0.023f * s, -0.485f * s); cubicTo(0.071f * s, -0.315f * s, 0.353f * s, -0.033f * s, 0.353f * s, 0.137f * s); cubicTo(0.353f * s, 0.287f * s, 0.184f * s, 0.381f * s, -0.023f * s, 0.381f * s); cubicTo(-0.231f * s, 0.381f * s, -0.4f * s, 0.287f * s, -0.4f * s, 0.137f * s); cubicTo(-0.4f * s, -0.033f * s, -0.118f * s, -0.315f * s, -0.023f * s, -0.485f * s); close() }
    val p2 = Path().apply { moveTo(-0.111f * s, 0.168f * s); cubicTo(-0.131f * s, 0.222f * s, -0.169f * s, 0.258f * s, -0.195f * s, 0.248f * s); cubicTo(-0.222f * s, 0.238f * s, -0.228f * s, 0.187f * s, -0.209f * s, 0.133f * s); cubicTo(-0.189f * s, 0.079f * s, -0.151f * s, 0.044f * s, -0.125f * s, 0.053f * s); cubicTo(-0.098f * s, 0.063f * s, -0.092f * s, 0.115f * s, -0.111f * s, 0.168f * s); close() }
    val p3 = Path().apply { moveTo(-0.077f * s, -0.02f * s); cubicTo(-0.088f * s, 0.003f * s, -0.105f * s, 0.019f * s, -0.114f * s, 0.014f * s); cubicTo(-0.123f * s, 0.01f * s, -0.122f * s, -0.013f * s, -0.111f * s, -0.036f * s); cubicTo(-0.1f * s, -0.06f * s, -0.084f * s, -0.075f * s, -0.074f * s, -0.071f * s); cubicTo(-0.065f * s, -0.066f * s, -0.066f * s, -0.044f * s, -0.077f * s, -0.02f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2E84C4))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF3AA5F5))
        }
        drawPath(p2, Color(0x8CFFFFFF))
        drawPath(p3, Color(0x8CFFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artBateau(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, 0.112f * s); lineTo(0.0f * s, -0.394f * s) }
    val p1 = Path().apply { moveTo(0.065f * s, -0.333f * s); quadraticTo(0.045f * s, -0.358f * s, 0.045f * s, -0.326f * s); lineTo(0.045f * s, 0.026f * s); quadraticTo(0.045f * s, 0.058f * s, 0.077f * s, 0.058f * s); lineTo(0.348f * s, 0.058f * s); quadraticTo(0.38f * s, 0.058f * s, 0.36f * s, 0.033f * s); close() }
    val p2 = Path().apply { moveTo(-0.066f * s, -0.225f * s); quadraticTo(-0.045f * s, -0.249f * s, -0.045f * s, -0.218f * s); lineTo(-0.045f * s, 0.026f * s); quadraticTo(-0.045f * s, 0.058f * s, -0.077f * s, 0.058f * s); lineTo(-0.276f * s, 0.058f * s); quadraticTo(-0.307f * s, 0.058f * s, -0.287f * s, 0.034f * s); close() }
    val p3 = Path().apply { moveTo(0.0f * s, -0.376f * s); quadraticTo(0.0f * s, -0.394f * s, 0.017f * s, -0.388f * s); lineTo(0.137f * s, -0.346f * s); quadraticTo(0.154f * s, -0.34f * s, 0.137f * s, -0.334f * s); lineTo(0.017f * s, -0.292f * s); quadraticTo(0.0f * s, -0.285f * s, 0.0f * s, -0.304f * s); close() }
    val p4 = Path().apply { moveTo(-0.385f * s, 0.136f * s); quadraticTo(-0.398f * s, 0.112f * s, -0.37f * s, 0.112f * s); lineTo(0.371f * s, 0.112f * s); quadraticTo(0.398f * s, 0.112f * s, 0.385f * s, 0.136f * s); lineTo(0.31f * s, 0.273f * s); quadraticTo(0.28f * s, 0.329f * s, 0.217f * s, 0.329f * s); lineTo(-0.217f * s, 0.329f * s); quadraticTo(-0.28f * s, 0.329f * s, -0.31f * s, 0.273f * s); close() }
    val p5 = Path().apply { moveTo(-0.403f * s, 0.104f * s); quadraticTo(-0.416f * s, 0.08f * s, -0.389f * s, 0.08f * s); lineTo(0.352f * s, 0.08f * s); quadraticTo(0.38f * s, 0.08f * s, 0.367f * s, 0.104f * s); lineTo(0.292f * s, 0.242f * s); quadraticTo(0.262f * s, 0.297f * s, 0.199f * s, 0.297f * s); lineTo(-0.235f * s, 0.297f * s); quadraticTo(-0.298f * s, 0.297f * s, -0.328f * s, 0.242f * s); close() }
    val p6 = Path().apply { moveTo(-0.434f * s, 0.311f * s); quadraticTo(-0.361f * s, 0.239f * s, -0.271f * s, 0.311f * s); quadraticTo(-0.181f * s, 0.239f * s, -0.09f * s, 0.311f * s); quadraticTo(0.0f * s, 0.239f * s, 0.09f * s, 0.311f * s); quadraticTo(0.181f * s, 0.239f * s, 0.271f * s, 0.311f * s); quadraticTo(0.361f * s, 0.239f * s, 0.434f * s, 0.311f * s); lineTo(0.434f * s, 0.401f * s); quadraticTo(0.434f * s, 0.428f * s, 0.398f * s, 0.428f * s); lineTo(-0.398f * s, 0.428f * s); quadraticTo(-0.434f * s, 0.428f * s, -0.434f * s, 0.401f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF7A4A2A), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFF4F9FF))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF9CCBFF))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFE8372E))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2671BC))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFF2F8DEB))
        }
        drawOval(Color(0x80FFFFFF), Offset(-0.325f * s, 0.139f * s), Size(0.145f * s, 0.036f * s))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFEAF5FF), 0.036f * s, Offset(-0.09f * s, 0.202f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.036f * s, Offset(-0.09f * s, 0.202f * s), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFEAF5FF), 0.036f * s, Offset(0.09f * s, 0.202f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.036f * s, Offset(0.09f * s, 0.202f * s), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF5BB0FF))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artBallon(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, 0.298f * s); quadraticTo(0.071f * s, 0.342f * s, 0.009f * s, 0.378f * s); quadraticTo(-0.053f * s, 0.413f * s, 0.018f * s, 0.44f * s) }
    val p1 = Path().apply { moveTo(0.0f * s, 0.244f * s); cubicTo(-0.107f * s, 0.208f * s, -0.321f * s, 0.101f * s, -0.321f * s, -0.131f * s); cubicTo(-0.321f * s, -0.309f * s, -0.178f * s, -0.434f * s, 0.0f * s, -0.434f * s); cubicTo(0.178f * s, -0.434f * s, 0.321f * s, -0.309f * s, 0.321f * s, -0.131f * s); cubicTo(0.321f * s, 0.101f * s, 0.107f * s, 0.208f * s, 0.0f * s, 0.244f * s); close() }
    val p2 = Path().apply { moveTo(-0.022f * s, 0.199f * s); cubicTo(-0.129f * s, 0.164f * s, -0.343f * s, 0.057f * s, -0.343f * s, -0.175f * s); cubicTo(-0.343f * s, -0.354f * s, -0.201f * s, -0.478f * s, -0.022f * s, -0.478f * s); cubicTo(0.156f * s, -0.478f * s, 0.299f * s, -0.354f * s, 0.299f * s, -0.175f * s); cubicTo(0.299f * s, 0.057f * s, 0.085f * s, 0.164f * s, -0.022f * s, 0.199f * s); close() }
    val p3 = Path().apply { moveTo(-0.103f * s, -0.179f * s); cubicTo(-0.128f * s, -0.126f * s, -0.17f * s, -0.092f * s, -0.197f * s, -0.105f * s); cubicTo(-0.224f * s, -0.117f * s, -0.225f * s, -0.171f * s, -0.2f * s, -0.225f * s); cubicTo(-0.175f * s, -0.278f * s, -0.133f * s, -0.312f * s, -0.106f * s, -0.299f * s); cubicTo(-0.08f * s, -0.287f * s, -0.078f * s, -0.233f * s, -0.103f * s, -0.179f * s); close() }
    val p4 = Path().apply { moveTo(0.007f * s, 0.237f * s); quadraticTo(0.0f * s, 0.226f * s, -0.007f * s, 0.237f * s); lineTo(-0.046f * s, 0.295f * s); quadraticTo(-0.053f * s, 0.306f * s, -0.04f * s, 0.306f * s); lineTo(0.04f * s, 0.306f * s); quadraticTo(0.054f * s, 0.306f * s, 0.046f * s, 0.295f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2F68C0))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFF3B82F0))
        }
        drawPath(p3, Color(0x8CFFFFFF))
        drawOval(Color(0x8CFFFFFF), Offset(-0.076f * s, -0.358f * s), Size(0.045f * s, 0.045f * s))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2A66CC))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artRaisin(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.154f * s); quadraticTo(0.0f * s, -0.297f * s, 0.036f * s, -0.386f * s) }
    val p1 = Path().apply { moveTo(0.027f * s, -0.333f * s); quadraticTo(0.193f * s, -0.247f * s, 0.295f * s, -0.404f * s); quadraticTo(0.129f * s, -0.489f * s, 0.027f * s, -0.333f * s); close() }
    val p2 = Path().apply { moveTo(0.105f * s, 0.329f * s); cubicTo(0.105f * s, 0.387f * s, 0.058f * s, 0.434f * s, 0.0f * s, 0.434f * s); cubicTo(-0.058f * s, 0.434f * s, -0.105f * s, 0.387f * s, -0.105f * s, 0.329f * s); cubicTo(-0.105f * s, 0.27f * s, -0.058f * s, 0.223f * s, 0.0f * s, 0.223f * s); cubicTo(0.058f * s, 0.223f * s, 0.105f * s, 0.27f * s, 0.105f * s, 0.329f * s); close() }
    val p3 = Path().apply { moveTo(0.092f * s, 0.302f * s); cubicTo(0.092f * s, 0.36f * s, 0.045f * s, 0.407f * s, -0.013f * s, 0.407f * s); cubicTo(-0.072f * s, 0.407f * s, -0.119f * s, 0.36f * s, -0.119f * s, 0.302f * s); cubicTo(-0.119f * s, 0.244f * s, -0.072f * s, 0.196f * s, -0.013f * s, 0.196f * s); cubicTo(0.045f * s, 0.196f * s, 0.092f * s, 0.244f * s, 0.092f * s, 0.302f * s); close() }
    val p4 = Path().apply { moveTo(-0.025f * s, 0.296f * s); cubicTo(-0.034f * s, 0.313f * s, -0.051f * s, 0.321f * s, -0.062f * s, 0.315f * s); cubicTo(-0.074f * s, 0.308f * s, -0.075f * s, 0.289f * s, -0.065f * s, 0.272f * s); cubicTo(-0.055f * s, 0.255f * s, -0.038f * s, 0.247f * s, -0.027f * s, 0.253f * s); cubicTo(-0.016f * s, 0.259f * s, -0.015f * s, 0.278f * s, -0.025f * s, 0.296f * s); close() }
    val p5 = Path().apply { moveTo(0.0f * s, 0.159f * s); cubicTo(0.0f * s, 0.217f * s, -0.047f * s, 0.264f * s, -0.105f * s, 0.264f * s); cubicTo(-0.163f * s, 0.264f * s, -0.21f * s, 0.217f * s, -0.21f * s, 0.159f * s); cubicTo(-0.21f * s, 0.101f * s, -0.163f * s, 0.053f * s, -0.105f * s, 0.053f * s); cubicTo(-0.047f * s, 0.053f * s, 0.0f * s, 0.101f * s, 0.0f * s, 0.159f * s); close() }
    val p6 = Path().apply { moveTo(-0.013f * s, 0.132f * s); cubicTo(-0.013f * s, 0.19f * s, -0.06f * s, 0.237f * s, -0.118f * s, 0.237f * s); cubicTo(-0.177f * s, 0.237f * s, -0.224f * s, 0.19f * s, -0.224f * s, 0.132f * s); cubicTo(-0.224f * s, 0.074f * s, -0.177f * s, 0.027f * s, -0.118f * s, 0.027f * s); cubicTo(-0.06f * s, 0.027f * s, -0.013f * s, 0.074f * s, -0.013f * s, 0.132f * s); close() }
    val p7 = Path().apply { moveTo(-0.129f * s, 0.126f * s); cubicTo(-0.139f * s, 0.143f * s, -0.156f * s, 0.151f * s, -0.167f * s, 0.145f * s); cubicTo(-0.179f * s, 0.139f * s, -0.18f * s, 0.12f * s, -0.17f * s, 0.103f * s); cubicTo(-0.16f * s, 0.085f * s, -0.143f * s, 0.077f * s, -0.132f * s, 0.083f * s); cubicTo(-0.121f * s, 0.09f * s, -0.12f * s, 0.109f * s, -0.129f * s, 0.126f * s); close() }
    val p8 = Path().apply { moveTo(0.21f * s, 0.159f * s); cubicTo(0.21f * s, 0.217f * s, 0.163f * s, 0.264f * s, 0.105f * s, 0.264f * s); cubicTo(0.047f * s, 0.264f * s, 0.0f * s, 0.217f * s, 0.0f * s, 0.159f * s); cubicTo(0.0f * s, 0.101f * s, 0.047f * s, 0.053f * s, 0.105f * s, 0.053f * s); cubicTo(0.163f * s, 0.053f * s, 0.21f * s, 0.101f * s, 0.21f * s, 0.159f * s); close() }
    val p9 = Path().apply { moveTo(0.197f * s, 0.132f * s); cubicTo(0.197f * s, 0.19f * s, 0.15f * s, 0.237f * s, 0.092f * s, 0.237f * s); cubicTo(0.033f * s, 0.237f * s, -0.014f * s, 0.19f * s, -0.014f * s, 0.132f * s); cubicTo(-0.014f * s, 0.074f * s, 0.033f * s, 0.027f * s, 0.092f * s, 0.027f * s); cubicTo(0.15f * s, 0.027f * s, 0.197f * s, 0.074f * s, 0.197f * s, 0.132f * s); close() }
    val p10 = Path().apply { moveTo(0.08f * s, 0.126f * s); cubicTo(0.071f * s, 0.143f * s, 0.054f * s, 0.151f * s, 0.042f * s, 0.145f * s); cubicTo(0.031f * s, 0.139f * s, 0.03f * s, 0.12f * s, 0.04f * s, 0.103f * s); cubicTo(0.05f * s, 0.085f * s, 0.067f * s, 0.077f * s, 0.078f * s, 0.083f * s); cubicTo(0.089f * s, 0.09f * s, 0.09f * s, 0.109f * s, 0.08f * s, 0.126f * s); close() }
    val p11 = Path().apply { moveTo(-0.104f * s, -0.011f * s); cubicTo(-0.104f * s, 0.047f * s, -0.152f * s, 0.095f * s, -0.21f * s, 0.095f * s); cubicTo(-0.268f * s, 0.095f * s, -0.315f * s, 0.047f * s, -0.315f * s, -0.011f * s); cubicTo(-0.315f * s, -0.069f * s, -0.268f * s, -0.116f * s, -0.21f * s, -0.116f * s); cubicTo(-0.152f * s, -0.116f * s, -0.104f * s, -0.069f * s, -0.104f * s, -0.011f * s); close() }
    val p12 = Path().apply { moveTo(-0.118f * s, -0.038f * s); cubicTo(-0.118f * s, 0.021f * s, -0.165f * s, 0.068f * s, -0.223f * s, 0.068f * s); cubicTo(-0.282f * s, 0.068f * s, -0.329f * s, 0.021f * s, -0.329f * s, -0.038f * s); cubicTo(-0.329f * s, -0.096f * s, -0.282f * s, -0.143f * s, -0.223f * s, -0.143f * s); cubicTo(-0.165f * s, -0.143f * s, -0.118f * s, -0.096f * s, -0.118f * s, -0.038f * s); close() }
    val p13 = Path().apply { moveTo(-0.234f * s, -0.044f * s); cubicTo(-0.244f * s, -0.027f * s, -0.261f * s, -0.018f * s, -0.272f * s, -0.025f * s); cubicTo(-0.283f * s, -0.031f * s, -0.285f * s, -0.05f * s, -0.275f * s, -0.067f * s); cubicTo(-0.265f * s, -0.084f * s, -0.248f * s, -0.093f * s, -0.237f * s, -0.086f * s); cubicTo(-0.226f * s, -0.08f * s, -0.225f * s, -0.061f * s, -0.234f * s, -0.044f * s); close() }
    val p14 = Path().apply { moveTo(0.105f * s, -0.011f * s); cubicTo(0.105f * s, 0.047f * s, 0.058f * s, 0.095f * s, 0.0f * s, 0.095f * s); cubicTo(-0.058f * s, 0.095f * s, -0.105f * s, 0.047f * s, -0.105f * s, -0.011f * s); cubicTo(-0.105f * s, -0.069f * s, -0.058f * s, -0.116f * s, 0.0f * s, -0.116f * s); cubicTo(0.058f * s, -0.116f * s, 0.105f * s, -0.069f * s, 0.105f * s, -0.011f * s); close() }
    val p15 = Path().apply { moveTo(0.092f * s, -0.038f * s); cubicTo(0.092f * s, 0.021f * s, 0.045f * s, 0.068f * s, -0.013f * s, 0.068f * s); cubicTo(-0.072f * s, 0.068f * s, -0.119f * s, 0.021f * s, -0.119f * s, -0.038f * s); cubicTo(-0.119f * s, -0.096f * s, -0.072f * s, -0.143f * s, -0.013f * s, -0.143f * s); cubicTo(0.045f * s, -0.143f * s, 0.092f * s, -0.096f * s, 0.092f * s, -0.038f * s); close() }
    val p16 = Path().apply { moveTo(-0.025f * s, -0.044f * s); cubicTo(-0.034f * s, -0.027f * s, -0.051f * s, -0.018f * s, -0.062f * s, -0.025f * s); cubicTo(-0.074f * s, -0.031f * s, -0.075f * s, -0.05f * s, -0.065f * s, -0.067f * s); cubicTo(-0.055f * s, -0.084f * s, -0.038f * s, -0.093f * s, -0.027f * s, -0.086f * s); cubicTo(-0.016f * s, -0.08f * s, -0.015f * s, -0.061f * s, -0.025f * s, -0.044f * s); close() }
    val p17 = Path().apply { moveTo(0.315f * s, -0.011f * s); cubicTo(0.315f * s, 0.047f * s, 0.268f * s, 0.095f * s, 0.21f * s, 0.095f * s); cubicTo(0.152f * s, 0.095f * s, 0.105f * s, 0.047f * s, 0.105f * s, -0.011f * s); cubicTo(0.105f * s, -0.069f * s, 0.152f * s, -0.116f * s, 0.21f * s, -0.116f * s); cubicTo(0.268f * s, -0.116f * s, 0.315f * s, -0.069f * s, 0.315f * s, -0.011f * s); close() }
    val p18 = Path().apply { moveTo(0.302f * s, -0.038f * s); cubicTo(0.302f * s, 0.021f * s, 0.255f * s, 0.068f * s, 0.197f * s, 0.068f * s); cubicTo(0.138f * s, 0.068f * s, 0.091f * s, 0.021f * s, 0.091f * s, -0.038f * s); cubicTo(0.091f * s, -0.096f * s, 0.138f * s, -0.143f * s, 0.197f * s, -0.143f * s); cubicTo(0.255f * s, -0.143f * s, 0.302f * s, -0.096f * s, 0.302f * s, -0.038f * s); close() }
    val p19 = Path().apply { moveTo(0.185f * s, -0.044f * s); cubicTo(0.176f * s, -0.027f * s, 0.159f * s, -0.018f * s, 0.147f * s, -0.025f * s); cubicTo(0.136f * s, -0.031f * s, 0.135f * s, -0.05f * s, 0.145f * s, -0.067f * s); cubicTo(0.155f * s, -0.084f * s, 0.172f * s, -0.093f * s, 0.183f * s, -0.086f * s); cubicTo(0.194f * s, -0.08f * s, 0.195f * s, -0.061f * s, 0.185f * s, -0.044f * s); close() }
    val p20 = Path().apply { moveTo(-0.209f * s, -0.181f * s); cubicTo(-0.209f * s, -0.122f * s, -0.257f * s, -0.075f * s, -0.315f * s, -0.075f * s); cubicTo(-0.373f * s, -0.075f * s, -0.42f * s, -0.122f * s, -0.42f * s, -0.181f * s); cubicTo(-0.42f * s, -0.239f * s, -0.373f * s, -0.286f * s, -0.315f * s, -0.286f * s); cubicTo(-0.257f * s, -0.286f * s, -0.209f * s, -0.239f * s, -0.209f * s, -0.181f * s); close() }
    val p21 = Path().apply { moveTo(-0.223f * s, -0.207f * s); cubicTo(-0.223f * s, -0.149f * s, -0.27f * s, -0.102f * s, -0.328f * s, -0.102f * s); cubicTo(-0.386f * s, -0.102f * s, -0.434f * s, -0.149f * s, -0.434f * s, -0.207f * s); cubicTo(-0.434f * s, -0.266f * s, -0.386f * s, -0.313f * s, -0.328f * s, -0.313f * s); cubicTo(-0.27f * s, -0.313f * s, -0.223f * s, -0.266f * s, -0.223f * s, -0.207f * s); close() }
    val p22 = Path().apply { moveTo(-0.339f * s, -0.214f * s); cubicTo(-0.349f * s, -0.197f * s, -0.366f * s, -0.188f * s, -0.377f * s, -0.194f * s); cubicTo(-0.388f * s, -0.201f * s, -0.39f * s, -0.22f * s, -0.38f * s, -0.237f * s); cubicTo(-0.37f * s, -0.254f * s, -0.353f * s, -0.263f * s, -0.342f * s, -0.256f * s); cubicTo(-0.331f * s, -0.25f * s, -0.329f * s, -0.231f * s, -0.339f * s, -0.214f * s); close() }
    val p23 = Path().apply { moveTo(0.0f * s, -0.181f * s); cubicTo(0.0f * s, -0.122f * s, -0.047f * s, -0.075f * s, -0.105f * s, -0.075f * s); cubicTo(-0.163f * s, -0.075f * s, -0.21f * s, -0.122f * s, -0.21f * s, -0.181f * s); cubicTo(-0.21f * s, -0.239f * s, -0.163f * s, -0.286f * s, -0.105f * s, -0.286f * s); cubicTo(-0.047f * s, -0.286f * s, 0.0f * s, -0.239f * s, 0.0f * s, -0.181f * s); close() }
    val p24 = Path().apply { moveTo(-0.013f * s, -0.207f * s); cubicTo(-0.013f * s, -0.149f * s, -0.06f * s, -0.102f * s, -0.118f * s, -0.102f * s); cubicTo(-0.177f * s, -0.102f * s, -0.224f * s, -0.149f * s, -0.224f * s, -0.207f * s); cubicTo(-0.224f * s, -0.266f * s, -0.177f * s, -0.313f * s, -0.118f * s, -0.313f * s); cubicTo(-0.06f * s, -0.313f * s, -0.013f * s, -0.266f * s, -0.013f * s, -0.207f * s); close() }
    val p25 = Path().apply { moveTo(-0.129f * s, -0.214f * s); cubicTo(-0.139f * s, -0.197f * s, -0.156f * s, -0.188f * s, -0.167f * s, -0.194f * s); cubicTo(-0.179f * s, -0.201f * s, -0.18f * s, -0.22f * s, -0.17f * s, -0.237f * s); cubicTo(-0.16f * s, -0.254f * s, -0.143f * s, -0.263f * s, -0.132f * s, -0.256f * s); cubicTo(-0.121f * s, -0.25f * s, -0.12f * s, -0.231f * s, -0.129f * s, -0.214f * s); close() }
    val p26 = Path().apply { moveTo(0.21f * s, -0.181f * s); cubicTo(0.21f * s, -0.122f * s, 0.163f * s, -0.075f * s, 0.105f * s, -0.075f * s); cubicTo(0.047f * s, -0.075f * s, 0.0f * s, -0.122f * s, 0.0f * s, -0.181f * s); cubicTo(0.0f * s, -0.239f * s, 0.047f * s, -0.286f * s, 0.105f * s, -0.286f * s); cubicTo(0.163f * s, -0.286f * s, 0.21f * s, -0.239f * s, 0.21f * s, -0.181f * s); close() }
    val p27 = Path().apply { moveTo(0.197f * s, -0.207f * s); cubicTo(0.197f * s, -0.149f * s, 0.15f * s, -0.102f * s, 0.092f * s, -0.102f * s); cubicTo(0.033f * s, -0.102f * s, -0.014f * s, -0.149f * s, -0.014f * s, -0.207f * s); cubicTo(-0.014f * s, -0.266f * s, 0.033f * s, -0.313f * s, 0.092f * s, -0.313f * s); cubicTo(0.15f * s, -0.313f * s, 0.197f * s, -0.266f * s, 0.197f * s, -0.207f * s); close() }
    val p28 = Path().apply { moveTo(0.08f * s, -0.214f * s); cubicTo(0.071f * s, -0.197f * s, 0.054f * s, -0.188f * s, 0.042f * s, -0.194f * s); cubicTo(0.031f * s, -0.201f * s, 0.03f * s, -0.22f * s, 0.04f * s, -0.237f * s); cubicTo(0.05f * s, -0.254f * s, 0.067f * s, -0.263f * s, 0.078f * s, -0.256f * s); cubicTo(0.089f * s, -0.25f * s, 0.09f * s, -0.231f * s, 0.08f * s, -0.214f * s); close() }
    val p29 = Path().apply { moveTo(0.42f * s, -0.181f * s); cubicTo(0.42f * s, -0.122f * s, 0.373f * s, -0.075f * s, 0.315f * s, -0.075f * s); cubicTo(0.257f * s, -0.075f * s, 0.21f * s, -0.122f * s, 0.21f * s, -0.181f * s); cubicTo(0.21f * s, -0.239f * s, 0.257f * s, -0.286f * s, 0.315f * s, -0.286f * s); cubicTo(0.373f * s, -0.286f * s, 0.42f * s, -0.239f * s, 0.42f * s, -0.181f * s); close() }
    val p30 = Path().apply { moveTo(0.407f * s, -0.207f * s); cubicTo(0.407f * s, -0.149f * s, 0.36f * s, -0.102f * s, 0.302f * s, -0.102f * s); cubicTo(0.243f * s, -0.102f * s, 0.196f * s, -0.149f * s, 0.196f * s, -0.207f * s); cubicTo(0.196f * s, -0.266f * s, 0.243f * s, -0.313f * s, 0.302f * s, -0.313f * s); cubicTo(0.36f * s, -0.313f * s, 0.407f * s, -0.266f * s, 0.407f * s, -0.207f * s); close() }
    val p31 = Path().apply { moveTo(0.29f * s, -0.214f * s); cubicTo(0.281f * s, -0.197f * s, 0.263f * s, -0.188f * s, 0.252f * s, -0.194f * s); cubicTo(0.241f * s, -0.201f * s, 0.24f * s, -0.22f * s, 0.25f * s, -0.237f * s); cubicTo(0.26f * s, -0.254f * s, 0.277f * s, -0.263f * s, 0.288f * s, -0.256f * s); cubicTo(0.299f * s, -0.25f * s, 0.3f * s, -0.231f * s, 0.29f * s, -0.214f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF7A4A2A), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF3FAE49))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF8B5CD6))
        }
        drawPath(p4, Color(0x8CFFFFFF))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFF8B5CD6))
        }
        drawPath(p7, Color(0x8CFFFFFF))
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p8) {
            drawPath(p9, Color(0xFF8B5CD6))
        }
        drawPath(p10, Color(0x8CFFFFFF))
    }
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p11) {
            drawPath(p12, Color(0xFF8B5CD6))
        }
        drawPath(p13, Color(0x8CFFFFFF))
    }
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p14, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p14) {
            drawPath(p15, Color(0xFF8B5CD6))
        }
        drawPath(p16, Color(0x8CFFFFFF))
    }
    drawPath(p14, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p17, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p17) {
            drawPath(p18, Color(0xFF8B5CD6))
        }
        drawPath(p19, Color(0x8CFFFFFF))
    }
    drawPath(p17, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p20, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p20) {
            drawPath(p21, Color(0xFF8B5CD6))
        }
        drawPath(p22, Color(0x8CFFFFFF))
    }
    drawPath(p20, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p23, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p23) {
            drawPath(p24, Color(0xFF8B5CD6))
        }
        drawPath(p25, Color(0x8CFFFFFF))
    }
    drawPath(p23, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p26, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p26) {
            drawPath(p27, Color(0xFF8B5CD6))
        }
        drawPath(p28, Color(0x8CFFFFFF))
    }
    drawPath(p26, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p29, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p29) {
            drawPath(p30, Color(0xFF8B5CD6))
        }
        drawPath(p31, Color(0x8CFFFFFF))
    }
    drawPath(p29, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artAubergine(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.128f * s, -0.262f * s); quadraticTo(0.169f * s, -0.34f * s, 0.225f * s, -0.393f * s) }
    val p1 = Path().apply { moveTo(0.037f * s, -0.236f * s); cubicTo(-0.045f * s, -0.062f * s, -0.226f * s, 0.043f * s, -0.273f * s, 0.217f * s); cubicTo(-0.296f * s, 0.356f * s, -0.185f * s, 0.434f * s, -0.046f * s, 0.434f * s); cubicTo(0.093f * s, 0.434f * s, 0.209f * s, 0.356f * s, 0.215f * s, 0.217f * s); cubicTo(0.244f * s, 0.043f * s, 0.129f * s, -0.062f * s, 0.194f * s, -0.236f * s); close() }
    val p2 = Path().apply { moveTo(0.015f * s, -0.271f * s); cubicTo(-0.067f * s, -0.097f * s, -0.248f * s, 0.008f * s, -0.295f * s, 0.182f * s); cubicTo(-0.318f * s, 0.321f * s, -0.207f * s, 0.4f * s, -0.068f * s, 0.4f * s); cubicTo(0.071f * s, 0.4f * s, 0.187f * s, 0.321f * s, 0.193f * s, 0.182f * s); cubicTo(0.222f * s, 0.008f * s, 0.108f * s, -0.097f * s, 0.172f * s, -0.271f * s); close() }
    val p3 = Path().apply { moveTo(-0.09f * s, -0.041f * s); cubicTo(-0.095f * s, -0.013f * s, -0.107f * s, 0.009f * s, -0.116f * s, 0.007f * s); cubicTo(-0.126f * s, 0.005f * s, -0.129f * s, -0.019f * s, -0.124f * s, -0.047f * s); cubicTo(-0.119f * s, -0.076f * s, -0.107f * s, -0.097f * s, -0.098f * s, -0.096f * s); cubicTo(-0.088f * s, -0.094f * s, -0.085f * s, -0.07f * s, -0.09f * s, -0.041f * s); close() }
    val p4 = Path().apply { moveTo(-0.031f * s, -0.249f * s); quadraticTo(-0.016f * s, -0.271f * s, 0.006f * s, -0.271f * s); lineTo(0.259f * s, -0.271f * s); quadraticTo(0.28f * s, -0.271f * s, 0.275f * s, -0.249f * s); lineTo(0.258f * s, -0.179f * s); quadraticTo(0.254f * s, -0.157f * s, 0.236f * s, -0.165f * s); lineTo(0.19f * s, -0.185f * s); quadraticTo(0.173f * s, -0.192f * s, 0.151f * s, -0.176f * s); lineTo(0.079f * s, -0.121f * s); quadraticTo(0.058f * s, -0.105f * s, 0.05f * s, -0.121f * s); lineTo(0.024f * s, -0.176f * s); quadraticTo(0.017f * s, -0.192f * s, -0.007f * s, -0.185f * s); lineTo(-0.071f * s, -0.165f * s); quadraticTo(-0.094f * s, -0.157f * s, -0.08f * s, -0.179f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.132f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF4C9A3A), style = Stroke(0.06f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFF8B5CD6))
        }
        drawOval(Color(0x8CFFFFFF), Offset(-0.212f * s, 0.121f * s), Size(0.07f * s, 0.191f * s))
        drawPath(p3, Color(0x8CFFFFFF))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF4C9A3A))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artPapillon(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.035f * s, 0.045f * s); cubicTo(-0.14f * s, 0.027f * s, -0.351f * s, 0.08f * s, -0.377f * s, 0.238f * s); cubicTo(-0.377f * s, 0.361f * s, -0.237f * s, 0.414f * s, -0.149f * s, 0.343f * s); cubicTo(-0.07f * s, 0.273f * s, -0.035f * s, 0.15f * s, -0.035f * s, 0.045f * s); close() }
    val p1 = Path().apply { moveTo(-0.048f * s, 0.01f * s); cubicTo(-0.154f * s, -0.008f * s, -0.364f * s, 0.045f * s, -0.391f * s, 0.203f * s); cubicTo(-0.391f * s, 0.326f * s, -0.25f * s, 0.379f * s, -0.162f * s, 0.308f * s); cubicTo(-0.083f * s, 0.238f * s, -0.048f * s, 0.115f * s, -0.048f * s, 0.01f * s); close() }
    val p2 = Path().apply { moveTo(-0.035f * s, 0.001f * s); cubicTo(-0.088f * s, -0.236f * s, -0.246f * s, -0.385f * s, -0.369f * s, -0.341f * s); cubicTo(-0.483f * s, -0.289f * s, -0.439f * s, -0.078f * s, -0.307f * s, 0.01f * s); cubicTo(-0.193f * s, 0.08f * s, -0.088f * s, 0.062f * s, -0.035f * s, 0.001f * s); close() }
    val p3 = Path().apply { moveTo(-0.048f * s, -0.034f * s); cubicTo(-0.101f * s, -0.271f * s, -0.259f * s, -0.42f * s, -0.382f * s, -0.377f * s); cubicTo(-0.496f * s, -0.324f * s, -0.452f * s, -0.113f * s, -0.32f * s, -0.025f * s); cubicTo(-0.206f * s, 0.045f * s, -0.101f * s, 0.027f * s, -0.048f * s, -0.034f * s); close() }
    val p4 = Path().apply { moveTo(-0.303f * s, -0.289f * s); cubicTo(-0.297f * s, -0.278f * s, -0.306f * s, -0.262f * s, -0.323f * s, -0.252f * s); cubicTo(-0.339f * s, -0.242f * s, -0.358f * s, -0.243f * s, -0.364f * s, -0.254f * s); cubicTo(-0.37f * s, -0.264f * s, -0.361f * s, -0.28f * s, -0.345f * s, -0.29f * s); cubicTo(-0.328f * s, -0.3f * s, -0.309f * s, -0.299f * s, -0.303f * s, -0.289f * s); close() }
    val p5 = Path().apply { moveTo(0.035f * s, 0.045f * s); cubicTo(0.141f * s, 0.027f * s, 0.351f * s, 0.08f * s, 0.378f * s, 0.238f * s); cubicTo(0.378f * s, 0.361f * s, 0.237f * s, 0.414f * s, 0.149f * s, 0.343f * s); cubicTo(0.07f * s, 0.273f * s, 0.035f * s, 0.15f * s, 0.035f * s, 0.045f * s); close() }
    val p6 = Path().apply { moveTo(0.048f * s, 0.01f * s); cubicTo(0.154f * s, -0.008f * s, 0.364f * s, 0.045f * s, 0.391f * s, 0.203f * s); cubicTo(0.391f * s, 0.326f * s, 0.25f * s, 0.379f * s, 0.162f * s, 0.308f * s); cubicTo(0.083f * s, 0.238f * s, 0.048f * s, 0.115f * s, 0.048f * s, 0.01f * s); close() }
    val p7 = Path().apply { moveTo(0.035f * s, 0.001f * s); cubicTo(0.088f * s, -0.236f * s, 0.246f * s, -0.385f * s, 0.369f * s, -0.341f * s); cubicTo(0.483f * s, -0.289f * s, 0.439f * s, -0.078f * s, 0.307f * s, 0.01f * s); cubicTo(0.193f * s, 0.08f * s, 0.088f * s, 0.062f * s, 0.035f * s, 0.001f * s); close() }
    val p8 = Path().apply { moveTo(0.048f * s, -0.034f * s); cubicTo(0.101f * s, -0.271f * s, 0.259f * s, -0.42f * s, 0.382f * s, -0.377f * s); cubicTo(0.496f * s, -0.324f * s, 0.452f * s, -0.113f * s, 0.321f * s, -0.025f * s); cubicTo(0.206f * s, 0.045f * s, 0.101f * s, 0.027f * s, 0.048f * s, -0.034f * s); close() }
    val p9 = Path().apply { moveTo(0.303f * s, -0.289f * s); cubicTo(0.297f * s, -0.278f * s, 0.306f * s, -0.262f * s, 0.323f * s, -0.252f * s); cubicTo(0.339f * s, -0.242f * s, 0.358f * s, -0.243f * s, 0.364f * s, -0.254f * s); cubicTo(0.37f * s, -0.264f * s, 0.361f * s, -0.28f * s, 0.345f * s, -0.29f * s); cubicTo(0.328f * s, -0.3f * s, 0.309f * s, -0.299f * s, 0.303f * s, -0.289f * s); close() }
    val p10 = Path().apply { moveTo(-0.018f * s, -0.148f * s); quadraticTo(-0.061f * s, -0.289f * s, -0.149f * s, -0.359f * s) }
    val p11 = Path().apply { moveTo(0.018f * s, -0.148f * s); quadraticTo(0.062f * s, -0.289f * s, 0.149f * s, -0.359f * s) }
    val p12 = Path().apply { moveTo(0.044f * s, 0.291f * s); cubicTo(0.044f * s, 0.315f * s, 0.024f * s, 0.335f * s, 0.0f * s, 0.335f * s); cubicTo(-0.024f * s, 0.335f * s, -0.044f * s, 0.315f * s, -0.044f * s, 0.291f * s); lineTo(-0.044f * s, -0.131f * s); cubicTo(-0.044f * s, -0.155f * s, -0.024f * s, -0.175f * s, 0.0f * s, -0.175f * s); cubicTo(0.024f * s, -0.175f * s, 0.044f * s, -0.155f * s, 0.044f * s, -0.131f * s); close() }
    val p13 = Path().apply { moveTo(0.035f * s, 0.273f * s); cubicTo(0.035f * s, 0.297f * s, 0.016f * s, 0.317f * s, -0.009f * s, 0.317f * s); cubicTo(-0.033f * s, 0.317f * s, -0.053f * s, 0.297f * s, -0.053f * s, 0.273f * s); lineTo(-0.053f * s, -0.148f * s); cubicTo(-0.053f * s, -0.172f * s, -0.033f * s, -0.192f * s, -0.009f * s, -0.192f * s); cubicTo(0.016f * s, -0.192f * s, 0.035f * s, -0.172f * s, 0.035f * s, -0.148f * s); close() }
    drawPath(p0, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF8B5CD6))
        }
        clipPath(p0) {
            drawCircle(Color(0xFFEBDDFF), 0.044f * s, Offset(-0.219f * s, 0.273f * s))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF8B5CD6))
        }
        clipPath(p2) {
            drawCircle(Color(0xFFEBDDFF), 0.066f * s, Offset(-0.263f * s, -0.183f * s))
            drawCircle(Color(0xFFEBDDFF), 0.026f * s, Offset(-0.123f * s, -0.017f * s))
        }
        drawPath(p4, Color(0x8CFFFFFF))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFF8B5CD6))
        }
        clipPath(p5) {
            drawCircle(Color(0xFFEBDDFF), 0.044f * s, Offset(0.219f * s, 0.273f * s))
        }
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p7) {
            drawPath(p8, Color(0xFF8B5CD6))
        }
        clipPath(p7) {
            drawCircle(Color(0xFFEBDDFF), 0.066f * s, Offset(0.263f * s, -0.183f * s))
            drawCircle(Color(0xFFEBDDFF), 0.026f * s, Offset(0.123f * s, -0.017f * s))
        }
        drawPath(p9, Color(0x8CFFFFFF))
    }
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.01f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.01f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFF3C2569))
    if (sil == null) {
        clipPath(p12) {
            drawPath(p13, Color(0xFF4B2E83))
        }
    }
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.022f * s, Offset(-0.149f * s, -0.368f * s))
        drawCircle(Color(0xFF2B2D42), 0.022f * s, Offset(0.149f * s, -0.368f * s))
    }
    drawCircle(sil ?: Color(0xFF4B2E83), 0.057f * s, Offset(0.0f * s, -0.148f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.057f * s, Offset(0.0f * s, -0.148f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCochon(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.163f * s, -0.284f * s); quadraticTo(-0.112f * s, -0.324f * s, -0.174f * s, -0.344f * s); lineTo(-0.376f * s, -0.407f * s); quadraticTo(-0.438f * s, -0.427f * s, -0.433f * s, -0.362f * s); lineTo(-0.416f * s, -0.156f * s); quadraticTo(-0.41f * s, -0.091f * s, -0.359f * s, -0.132f * s); close() }
    val p1 = Path().apply { moveTo(0.163f * s, -0.284f * s); quadraticTo(0.112f * s, -0.324f * s, 0.174f * s, -0.344f * s); lineTo(0.376f * s, -0.407f * s); quadraticTo(0.438f * s, -0.427f * s, 0.433f * s, -0.362f * s); lineTo(0.416f * s, -0.156f * s); quadraticTo(0.41f * s, -0.091f * s, 0.359f * s, -0.132f * s); close() }
    val p2 = Path().apply { moveTo(0.42f * s, 0.048f * s); cubicTo(0.42f * s, 0.249f * s, 0.232f * s, 0.412f * s, 0.0f * s, 0.412f * s); cubicTo(-0.232f * s, 0.412f * s, -0.419f * s, 0.249f * s, -0.419f * s, 0.048f * s); cubicTo(-0.419f * s, -0.152f * s, -0.232f * s, -0.315f * s, 0.0f * s, -0.315f * s); cubicTo(0.232f * s, -0.315f * s, 0.42f * s, -0.152f * s, 0.42f * s, 0.048f * s); close() }
    val p3 = Path().apply { moveTo(0.401f * s, 0.002f * s); cubicTo(0.401f * s, 0.203f * s, 0.213f * s, 0.365f * s, -0.019f * s, 0.365f * s); cubicTo(-0.25f * s, 0.365f * s, -0.438f * s, 0.203f * s, -0.438f * s, 0.002f * s); cubicTo(-0.438f * s, -0.199f * s, -0.25f * s, -0.362f * s, -0.019f * s, -0.362f * s); cubicTo(0.213f * s, -0.362f * s, 0.401f * s, -0.199f * s, 0.401f * s, 0.002f * s); close() }
    val p4 = Path().apply { moveTo(-0.177f * s, -0.171f * s); cubicTo(-0.166f * s, -0.153f * s, -0.183f * s, -0.124f * s, -0.214f * s, -0.106f * s); cubicTo(-0.246f * s, -0.088f * s, -0.279f * s, -0.088f * s, -0.29f * s, -0.105f * s); cubicTo(-0.3f * s, -0.123f * s, -0.283f * s, -0.152f * s, -0.252f * s, -0.17f * s); cubicTo(-0.22f * s, -0.188f * s, -0.187f * s, -0.188f * s, -0.177f * s, -0.171f * s); close() }
    val p5 = Path().apply { moveTo(0.177f * s, 0.132f * s); cubicTo(0.177f * s, 0.204f * s, 0.098f * s, 0.263f * s, 0.0f * s, 0.263f * s); cubicTo(-0.098f * s, 0.263f * s, -0.177f * s, 0.204f * s, -0.177f * s, 0.132f * s); cubicTo(-0.177f * s, 0.06f * s, -0.098f * s, 0.002f * s, 0.0f * s, 0.002f * s); cubicTo(0.098f * s, 0.002f * s, 0.177f * s, 0.06f * s, 0.177f * s, 0.132f * s); close() }
    drawPath(p0, sil ?: Color(0xFFFF6FA3))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFFF6FA3))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFFF86B3))
        }
        drawPath(p4, Color(0x8CFFFFFF))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.056f * s, Offset(-0.186f * s, -0.073f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.056f * s, Offset(-0.186f * s, -0.073f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.032f * s, Offset(-0.181f * s, -0.062f * s))
        drawCircle(Color(0xFFFFFFFF), 0.011f * s, Offset(-0.193f * s, -0.076f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.056f * s, Offset(0.186f * s, -0.073f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.056f * s, Offset(0.186f * s, -0.073f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.032f * s, Offset(0.181f * s, -0.062f * s))
        drawCircle(Color(0xFFFFFFFF), 0.011f * s, Offset(0.169f * s, -0.076f * s))
    }
    drawPath(p5, sil ?: Color(0xFFFFB3CE))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0xFF2B2D42), Offset(-0.089f * s, 0.09f * s), Size(0.056f * s, 0.084f * s))
        drawOval(Color(0xFF2B2D42), Offset(0.033f * s, 0.09f * s), Size(0.056f * s, 0.084f * s))
        drawOval(Color(0x66FF4F86), Offset(-0.364f * s, 0.081f * s), Size(0.093f * s, 0.065f * s))
        drawOval(Color(0x66FF4F86), Offset(0.27f * s, 0.081f * s), Size(0.093f * s, 0.065f * s))
    }
}

private fun DrawScope.artGlace(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.197f * s, 0.035f * s); quadraticTo(-0.209f * s, 0.011f * s, -0.181f * s, 0.011f * s); lineTo(0.181f * s, 0.011f * s); quadraticTo(0.209f * s, 0.011f * s, 0.197f * s, 0.035f * s); lineTo(0.012f * s, 0.421f * s); quadraticTo(0.0f * s, 0.446f * s, -0.012f * s, 0.421f * s); close() }
    val p1 = Path().apply { moveTo(-0.215f * s, 0.008f * s); quadraticTo(-0.227f * s, -0.016f * s, -0.199f * s, -0.016f * s); lineTo(0.163f * s, -0.016f * s); quadraticTo(0.19f * s, -0.016f * s, 0.179f * s, 0.008f * s); lineTo(-0.006f * s, 0.394f * s); quadraticTo(-0.018f * s, 0.419f * s, -0.03f * s, 0.394f * s); close() }
    val p2 = Path().apply { moveTo(-0.209f * s, 0.083f * s); lineTo(0.064f * s, 0.392f * s); moveTo(-0.109f * s, 0.011f * s); lineTo(0.145f * s, 0.301f * s); moveTo(0.209f * s, 0.083f * s); lineTo(-0.063f * s, 0.392f * s); moveTo(0.109f * s, 0.011f * s); lineTo(-0.145f * s, 0.301f * s) }
    val p3 = Path().apply { moveTo(-0.299f * s, -0.044f * s); cubicTo(-0.317f * s, -0.261f * s, 0.317f * s, -0.261f * s, 0.299f * s, -0.044f * s); lineTo(0.308f * s, 0.011f * s); quadraticTo(0.245f * s, 0.11f * s, 0.154f * s, 0.038f * s); quadraticTo(0.073f * s, 0.12f * s, 0.0f * s, 0.038f * s); quadraticTo(-0.072f * s, 0.12f * s, -0.154f * s, 0.038f * s); quadraticTo(-0.245f * s, 0.11f * s, -0.308f * s, 0.011f * s); close() }
    val p4 = Path().apply { moveTo(-0.317f * s, -0.08f * s); cubicTo(-0.335f * s, -0.298f * s, 0.299f * s, -0.298f * s, 0.281f * s, -0.08f * s); lineTo(0.29f * s, -0.026f * s); quadraticTo(0.227f * s, 0.074f * s, 0.136f * s, 0.002f * s); quadraticTo(0.054f * s, 0.083f * s, -0.018f * s, 0.002f * s); quadraticTo(-0.091f * s, 0.083f * s, -0.172f * s, 0.002f * s); quadraticTo(-0.263f * s, 0.074f * s, -0.326f * s, -0.026f * s); close() }
    val p5 = Path().apply { moveTo(-0.143f * s, -0.094f * s); cubicTo(-0.134f * s, -0.07f * s, -0.145f * s, -0.045f * s, -0.166f * s, -0.037f * s); cubicTo(-0.187f * s, -0.03f * s, -0.211f * s, -0.043f * s, -0.22f * s, -0.066f * s); cubicTo(-0.228f * s, -0.09f * s, -0.218f * s, -0.115f * s, -0.197f * s, -0.123f * s); cubicTo(-0.176f * s, -0.13f * s, -0.152f * s, -0.117f * s, -0.143f * s, -0.094f * s); close() }
    val p6 = Path().apply { moveTo(0.209f * s, -0.243f * s); cubicTo(0.209f * s, -0.138f * s, 0.115f * s, -0.053f * s, 0.0f * s, -0.053f * s); cubicTo(-0.115f * s, -0.053f * s, -0.209f * s, -0.138f * s, -0.209f * s, -0.243f * s); cubicTo(-0.209f * s, -0.348f * s, -0.115f * s, -0.434f * s, 0.0f * s, -0.434f * s); cubicTo(0.115f * s, -0.434f * s, 0.209f * s, -0.348f * s, 0.209f * s, -0.243f * s); close() }
    val p7 = Path().apply { moveTo(0.19f * s, -0.279f * s); cubicTo(0.19f * s, -0.174f * s, 0.097f * s, -0.089f * s, -0.018f * s, -0.089f * s); cubicTo(-0.133f * s, -0.089f * s, -0.227f * s, -0.174f * s, -0.227f * s, -0.279f * s); cubicTo(-0.227f * s, -0.385f * s, -0.133f * s, -0.47f * s, -0.018f * s, -0.47f * s); cubicTo(0.097f * s, -0.47f * s, 0.19f * s, -0.385f * s, 0.19f * s, -0.279f * s); close() }
    val p8 = Path().apply { moveTo(-0.053f * s, -0.351f * s); cubicTo(-0.035f * s, -0.324f * s, -0.036f * s, -0.291f * s, -0.057f * s, -0.277f * s); cubicTo(-0.077f * s, -0.262f * s, -0.109f * s, -0.272f * s, -0.128f * s, -0.299f * s); cubicTo(-0.146f * s, -0.325f * s, -0.145f * s, -0.359f * s, -0.124f * s, -0.373f * s); cubicTo(-0.104f * s, -0.387f * s, -0.072f * s, -0.377f * s, -0.053f * s, -0.351f * s); close() }
    drawPath(p0, sil ?: Color(0xFFBA8E4A))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFE8B25C))
        }
    }
    clipPath(p0) {
        drawPath(p2, sil ?: Color(0xFFC98A3A), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFCC92A8))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFFFB6D2))
        }
        drawPath(p5, Color(0x8CFFFFFF))
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFFFF86B3))
        }
        drawPath(p8, Color(0x8CFFFFFF))
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCupcake(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.312f * s, 0.067f * s); quadraticTo(-0.318f * s, 0.03f * s, -0.281f * s, 0.03f * s); lineTo(0.281f * s, 0.03f * s); quadraticTo(0.318f * s, 0.03f * s, 0.312f * s, 0.067f * s); lineTo(0.252f * s, 0.387f * s); quadraticTo(0.244f * s, 0.433f * s, 0.197f * s, 0.433f * s); lineTo(-0.197f * s, 0.433f * s); quadraticTo(-0.243f * s, 0.433f * s, -0.252f * s, 0.387f * s); close() }
    val p1 = Path().apply { moveTo(-0.33f * s, 0.039f * s); quadraticTo(-0.337f * s, 0.002f * s, -0.3f * s, 0.002f * s); lineTo(0.262f * s, 0.002f * s); quadraticTo(0.3f * s, 0.002f * s, 0.293f * s, 0.039f * s); lineTo(0.233f * s, 0.359f * s); quadraticTo(0.225f * s, 0.405f * s, 0.178f * s, 0.405f * s); lineTo(-0.215f * s, 0.405f * s); quadraticTo(-0.262f * s, 0.405f * s, -0.271f * s, 0.359f * s); close() }
    val p2 = Path().apply { moveTo(0.272f * s, -0.082f * s); cubicTo(0.334f * s, -0.082f * s, 0.384f * s, -0.032f * s, 0.384f * s, 0.03f * s); cubicTo(0.384f * s, 0.093f * s, 0.334f * s, 0.143f * s, 0.272f * s, 0.143f * s); lineTo(-0.272f * s, 0.143f * s); cubicTo(-0.334f * s, 0.143f * s, -0.384f * s, 0.093f * s, -0.384f * s, 0.03f * s); cubicTo(-0.384f * s, -0.032f * s, -0.334f * s, -0.082f * s, -0.272f * s, -0.082f * s); close() }
    val p3 = Path().apply { moveTo(0.258f * s, -0.119f * s); cubicTo(0.32f * s, -0.119f * s, 0.37f * s, -0.069f * s, 0.37f * s, -0.007f * s); cubicTo(0.37f * s, 0.055f * s, 0.32f * s, 0.105f * s, 0.258f * s, 0.105f * s); lineTo(-0.286f * s, 0.105f * s); cubicTo(-0.348f * s, 0.105f * s, -0.398f * s, 0.055f * s, -0.398f * s, -0.007f * s); cubicTo(-0.398f * s, -0.069f * s, -0.348f * s, -0.119f * s, -0.286f * s, -0.119f * s); close() }
    val p4 = Path().apply { moveTo(0.187f * s, -0.232f * s); cubicTo(0.244f * s, -0.232f * s, 0.29f * s, -0.186f * s, 0.29f * s, -0.129f * s); cubicTo(0.29f * s, -0.072f * s, 0.244f * s, -0.026f * s, 0.187f * s, -0.026f * s); lineTo(-0.187f * s, -0.026f * s); cubicTo(-0.244f * s, -0.026f * s, -0.29f * s, -0.072f * s, -0.29f * s, -0.129f * s); cubicTo(-0.29f * s, -0.186f * s, -0.244f * s, -0.232f * s, -0.187f * s, -0.232f * s); close() }
    val p5 = Path().apply { moveTo(0.173f * s, -0.269f * s); cubicTo(0.23f * s, -0.269f * s, 0.276f * s, -0.223f * s, 0.276f * s, -0.166f * s); cubicTo(0.276f * s, -0.109f * s, 0.23f * s, -0.063f * s, 0.173f * s, -0.063f * s); lineTo(-0.201f * s, -0.063f * s); cubicTo(-0.258f * s, -0.063f * s, -0.304f * s, -0.109f * s, -0.304f * s, -0.166f * s); cubicTo(-0.304f * s, -0.223f * s, -0.258f * s, -0.269f * s, -0.201f * s, -0.269f * s); close() }
    val p6 = Path().apply { moveTo(0.112f * s, -0.269f * s); cubicTo(0.112f * s, -0.212f * s, 0.062f * s, -0.166f * s, 0.0f * s, -0.166f * s); cubicTo(-0.062f * s, -0.166f * s, -0.112f * s, -0.212f * s, -0.112f * s, -0.269f * s); cubicTo(-0.112f * s, -0.326f * s, -0.062f * s, -0.372f * s, 0.0f * s, -0.372f * s); cubicTo(0.062f * s, -0.372f * s, 0.112f * s, -0.326f * s, 0.112f * s, -0.269f * s); close() }
    val p7 = Path().apply { moveTo(0.098f * s, -0.302f * s); cubicTo(0.098f * s, -0.245f * s, 0.048f * s, -0.199f * s, -0.014f * s, -0.199f * s); cubicTo(-0.076f * s, -0.199f * s, -0.126f * s, -0.245f * s, -0.126f * s, -0.302f * s); cubicTo(-0.126f * s, -0.359f * s, -0.076f * s, -0.405f * s, -0.014f * s, -0.405f * s); cubicTo(0.048f * s, -0.405f * s, 0.098f * s, -0.359f * s, 0.098f * s, -0.302f * s); close() }
    val p8 = Path().apply { moveTo(-0.022f * s, -0.321f * s); cubicTo(-0.012f * s, -0.303f * s, -0.015f * s, -0.282f * s, -0.028f * s, -0.274f * s); cubicTo(-0.041f * s, -0.266f * s, -0.061f * s, -0.275f * s, -0.071f * s, -0.293f * s); cubicTo(-0.081f * s, -0.311f * s, -0.079f * s, -0.331f * s, -0.066f * s, -0.339f * s); cubicTo(-0.052f * s, -0.347f * s, -0.033f * s, -0.339f * s, -0.022f * s, -0.321f * s); close() }
    val p9 = Path().apply { moveTo(0.056f * s, -0.382f * s); cubicTo(0.056f * s, -0.353f * s, 0.031f * s, -0.33f * s, 0.0f * s, -0.33f * s); cubicTo(-0.031f * s, -0.33f * s, -0.056f * s, -0.353f * s, -0.056f * s, -0.382f * s); cubicTo(-0.056f * s, -0.41f * s, -0.031f * s, -0.433f * s, 0.0f * s, -0.433f * s); cubicTo(0.031f * s, -0.433f * s, 0.056f * s, -0.41f * s, 0.056f * s, -0.382f * s); close() }
    val p10 = Path().apply { moveTo(0.047f * s, -0.4f * s); cubicTo(0.047f * s, -0.372f * s, 0.022f * s, -0.349f * s, -0.009f * s, -0.349f * s); cubicTo(-0.04f * s, -0.349f * s, -0.066f * s, -0.372f * s, -0.066f * s, -0.4f * s); cubicTo(-0.066f * s, -0.429f * s, -0.04f * s, -0.452f * s, -0.009f * s, -0.452f * s); cubicTo(0.022f * s, -0.452f * s, 0.047f * s, -0.429f * s, 0.047f * s, -0.4f * s); close() }
    drawPath(p0, sil ?: Color(0xFFC04E7D))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFF0629C))
        }
    }
    drawLine(sil ?: Color(0x59FFFFFF), Offset(-0.159f * s, 0.077f * s), Offset(-0.122f * s, 0.405f * s), 0.04f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0x59FFFFFF), Offset(0.0f * s, 0.077f * s), Offset(0.0f * s, 0.405f * s), 0.04f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0x59FFFFFF), Offset(0.159f * s, 0.077f * s), Offset(0.122f * s, 0.405f * s), 0.04f * s, StrokeCap.Round)
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFCC7D9B))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFFF9CC2))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFCC87A2))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFFFA9CB))
        }
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFCC92A9))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFFFFB7D3))
        }
        drawPath(p8, Color(0x8CFFFFFF))
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFB72D2F))
    if (sil == null) {
        clipPath(p9) {
            drawPath(p10, Color(0xFFE5383B))
        }
    }
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artOurs(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.157f * s, -0.272f * s); cubicTo(-0.157f * s, -0.196f * s, -0.219f * s, -0.134f * s, -0.295f * s, -0.134f * s); cubicTo(-0.371f * s, -0.134f * s, -0.433f * s, -0.196f * s, -0.433f * s, -0.272f * s); cubicTo(-0.433f * s, -0.348f * s, -0.371f * s, -0.41f * s, -0.295f * s, -0.41f * s); cubicTo(-0.219f * s, -0.41f * s, -0.157f * s, -0.348f * s, -0.157f * s, -0.272f * s); close() }
    val p1 = Path().apply { moveTo(-0.166f * s, -0.3f * s); cubicTo(-0.166f * s, -0.223f * s, -0.228f * s, -0.161f * s, -0.304f * s, -0.161f * s); cubicTo(-0.381f * s, -0.161f * s, -0.443f * s, -0.223f * s, -0.443f * s, -0.3f * s); cubicTo(-0.443f * s, -0.376f * s, -0.381f * s, -0.438f * s, -0.304f * s, -0.438f * s); cubicTo(-0.228f * s, -0.438f * s, -0.166f * s, -0.376f * s, -0.166f * s, -0.3f * s); close() }
    val p2 = Path().apply { moveTo(-0.221f * s, -0.263f * s); cubicTo(-0.221f * s, -0.222f * s, -0.254f * s, -0.189f * s, -0.295f * s, -0.189f * s); cubicTo(-0.336f * s, -0.189f * s, -0.369f * s, -0.222f * s, -0.369f * s, -0.263f * s); cubicTo(-0.369f * s, -0.304f * s, -0.336f * s, -0.337f * s, -0.295f * s, -0.337f * s); cubicTo(-0.254f * s, -0.337f * s, -0.221f * s, -0.304f * s, -0.221f * s, -0.263f * s); close() }
    val p3 = Path().apply { moveTo(0.433f * s, -0.272f * s); cubicTo(0.433f * s, -0.196f * s, 0.371f * s, -0.134f * s, 0.295f * s, -0.134f * s); cubicTo(0.219f * s, -0.134f * s, 0.157f * s, -0.196f * s, 0.157f * s, -0.272f * s); cubicTo(0.157f * s, -0.348f * s, 0.219f * s, -0.41f * s, 0.295f * s, -0.41f * s); cubicTo(0.371f * s, -0.41f * s, 0.433f * s, -0.348f * s, 0.433f * s, -0.272f * s); close() }
    val p4 = Path().apply { moveTo(0.424f * s, -0.3f * s); cubicTo(0.424f * s, -0.223f * s, 0.362f * s, -0.161f * s, 0.286f * s, -0.161f * s); cubicTo(0.21f * s, -0.161f * s, 0.148f * s, -0.223f * s, 0.148f * s, -0.3f * s); cubicTo(0.148f * s, -0.376f * s, 0.21f * s, -0.438f * s, 0.286f * s, -0.438f * s); cubicTo(0.362f * s, -0.438f * s, 0.424f * s, -0.376f * s, 0.424f * s, -0.3f * s); close() }
    val p5 = Path().apply { moveTo(0.369f * s, -0.263f * s); cubicTo(0.369f * s, -0.222f * s, 0.336f * s, -0.189f * s, 0.295f * s, -0.189f * s); cubicTo(0.254f * s, -0.189f * s, 0.221f * s, -0.222f * s, 0.221f * s, -0.263f * s); cubicTo(0.221f * s, -0.304f * s, 0.254f * s, -0.337f * s, 0.295f * s, -0.337f * s); cubicTo(0.336f * s, -0.337f * s, 0.369f * s, -0.304f * s, 0.369f * s, -0.263f * s); close() }
    val p6 = Path().apply { moveTo(0.406f * s, 0.051f * s); cubicTo(0.406f * s, 0.249f * s, 0.224f * s, 0.41f * s, 0.0f * s, 0.41f * s); cubicTo(-0.224f * s, 0.41f * s, -0.406f * s, 0.249f * s, -0.406f * s, 0.051f * s); cubicTo(-0.406f * s, -0.148f * s, -0.224f * s, -0.309f * s, 0.0f * s, -0.309f * s); cubicTo(0.224f * s, -0.309f * s, 0.406f * s, -0.148f * s, 0.406f * s, 0.051f * s); close() }
    val p7 = Path().apply { moveTo(0.387f * s, 0.005f * s); cubicTo(0.387f * s, 0.203f * s, 0.206f * s, 0.364f * s, -0.018f * s, 0.364f * s); cubicTo(-0.242f * s, 0.364f * s, -0.424f * s, 0.203f * s, -0.424f * s, 0.005f * s); cubicTo(-0.424f * s, -0.194f * s, -0.242f * s, -0.355f * s, -0.018f * s, -0.355f * s); cubicTo(0.206f * s, -0.355f * s, 0.387f * s, -0.194f * s, 0.387f * s, 0.005f * s); close() }
    val p8 = Path().apply { moveTo(-0.165f * s, -0.175f * s); cubicTo(-0.154f * s, -0.155f * s, -0.17f * s, -0.125f * s, -0.201f * s, -0.107f * s); cubicTo(-0.231f * s, -0.089f * s, -0.266f * s, -0.091f * s, -0.277f * s, -0.111f * s); cubicTo(-0.289f * s, -0.13f * s, -0.273f * s, -0.161f * s, -0.242f * s, -0.179f * s); cubicTo(-0.211f * s, -0.197f * s, -0.177f * s, -0.195f * s, -0.165f * s, -0.175f * s); close() }
    val p9 = Path().apply { moveTo(0.175f * s, 0.152f * s); cubicTo(0.175f * s, 0.229f * s, 0.097f * s, 0.291f * s, 0.0f * s, 0.291f * s); cubicTo(-0.097f * s, 0.291f * s, -0.175f * s, 0.229f * s, -0.175f * s, 0.152f * s); cubicTo(-0.175f * s, 0.076f * s, -0.097f * s, 0.014f * s, 0.0f * s, 0.014f * s); cubicTo(0.097f * s, 0.014f * s, 0.175f * s, 0.076f * s, 0.175f * s, 0.152f * s); close() }
    val p10 = Path().apply { moveTo(-0.037f * s, 0.101f * s); quadraticTo(-0.06f * s, 0.078f * s, -0.028f * s, 0.078f * s); lineTo(0.028f * s, 0.078f * s); quadraticTo(0.06f * s, 0.078f * s, 0.037f * s, 0.101f * s); lineTo(0.023f * s, 0.116f * s); quadraticTo(0.0f * s, 0.138f * s, -0.023f * s, 0.116f * s); close() }
    val p11 = Path().apply { moveTo(0.0f * s, 0.134f * s); lineTo(0.0f * s, 0.189f * s); quadraticTo(-0.046f * s, 0.254f * s, -0.101f * s, 0.208f * s) }
    val p12 = Path().apply { moveTo(0.0f * s, 0.189f * s); quadraticTo(0.046f * s, 0.254f * s, 0.101f * s, 0.208f * s) }
    drawPath(p0, sil ?: Color(0xFF87542F))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFA9693B))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p2, Color(0xFFE7B98A))
    }
    drawPath(p3, sil ?: Color(0xFF87542F))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFA9693B))
        }
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p5, Color(0xFFE7B98A))
    }
    drawPath(p6, sil ?: Color(0xFF87542F))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFFA9693B))
        }
        drawPath(p8, Color(0x8CFFFFFF))
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.055f * s, Offset(-0.175f * s, -0.06f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.055f * s, Offset(-0.175f * s, -0.06f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.032f * s, Offset(-0.17f * s, -0.049f * s))
        drawCircle(Color(0xFFFFFFFF), 0.011f * s, Offset(-0.182f * s, -0.063f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.055f * s, Offset(0.175f * s, -0.06f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.055f * s, Offset(0.175f * s, -0.06f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.032f * s, Offset(0.17f * s, -0.049f * s))
        drawCircle(Color(0xFFFFFFFF), 0.011f * s, Offset(0.158f * s, -0.063f * s))
    }
    drawPath(p9, sil ?: Color(0xFFEBC9A0))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p10, Color(0xFF2B2D42))
    }
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(-0.35f * s, 0.084f * s), Size(0.092f * s, 0.063f * s))
        drawOval(Color(0x66FF4F86), Offset(0.258f * s, 0.084f * s), Size(0.092f * s, 0.063f * s))
    }
}

private fun DrawScope.artCookie(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.409f * s, -0.141f * s); cubicTo(0.476f * s, 0.054f * s, 0.396f * s, 0.268f * s, 0.219f * s, 0.373f * s); cubicTo(0.042f * s, 0.477f * s, -0.184f * s, 0.442f * s, -0.321f * s, 0.289f * s); cubicTo(-0.459f * s, 0.137f * s, -0.47f * s, -0.092f * s, -0.348f * s, -0.257f * s); cubicTo(-0.225f * s, -0.422f * s, -0.004f * s, -0.479f * s, 0.183f * s, -0.392f * s); cubicTo(0.113f * s, -0.322f * s, 0.181f * s, -0.246f * s, 0.258f * s, -0.308f * s); cubicTo(0.189f * s, -0.238f * s, 0.257f * s, -0.163f * s, 0.333f * s, -0.224f * s); cubicTo(0.264f * s, -0.154f * s, 0.332f * s, -0.079f * s, 0.409f * s, -0.141f * s); close() }
    val p1 = Path().apply { moveTo(0.389f * s, -0.19f * s); cubicTo(0.456f * s, 0.005f * s, 0.377f * s, 0.219f * s, 0.2f * s, 0.323f * s); cubicTo(0.023f * s, 0.428f * s, -0.203f * s, 0.393f * s, -0.341f * s, 0.24f * s); cubicTo(-0.479f * s, 0.088f * s, -0.489f * s, -0.141f * s, -0.367f * s, -0.306f * s); cubicTo(-0.245f * s, -0.471f * s, -0.023f * s, -0.528f * s, 0.163f * s, -0.441f * s); cubicTo(0.094f * s, -0.371f * s, 0.162f * s, -0.296f * s, 0.238f * s, -0.357f * s); cubicTo(0.169f * s, -0.287f * s, 0.237f * s, -0.212f * s, 0.314f * s, -0.273f * s); cubicTo(0.244f * s, -0.204f * s, 0.312f * s, -0.128f * s, 0.389f * s, -0.19f * s); close() }
    val p2 = Path().apply { moveTo(-0.137f * s, -0.175f * s); cubicTo(-0.146f * s, -0.149f * s, -0.18f * s, -0.138f * s, -0.213f * s, -0.15f * s); cubicTo(-0.246f * s, -0.162f * s, -0.266f * s, -0.193f * s, -0.257f * s, -0.218f * s); cubicTo(-0.247f * s, -0.244f * s, -0.213f * s, -0.255f * s, -0.18f * s, -0.243f * s); cubicTo(-0.147f * s, -0.231f * s, -0.127f * s, -0.2f * s, -0.137f * s, -0.175f * s); close() }
    val p3 = Path().apply { moveTo(0.112f * s, -0.074f * s); cubicTo(0.123f * s, -0.052f * s, 0.107f * s, -0.023f * s, 0.078f * s, -0.009f * s); cubicTo(0.048f * s, 0.005f * s, 0.016f * s, -0.002f * s, 0.005f * s, -0.024f * s); cubicTo(-0.005f * s, -0.046f * s, 0.011f * s, -0.075f * s, 0.04f * s, -0.089f * s); cubicTo(0.07f * s, -0.103f * s, 0.102f * s, -0.096f * s, 0.112f * s, -0.074f * s); close() }
    val p4 = Path().apply { moveTo(-0.178f * s, 0.088f * s); cubicTo(-0.174f * s, 0.112f * s, -0.196f * s, 0.136f * s, -0.228f * s, 0.142f * s); cubicTo(-0.26f * s, 0.148f * s, -0.29f * s, 0.133f * s, -0.294f * s, 0.109f * s); cubicTo(-0.298f * s, 0.084f * s, -0.276f * s, 0.06f * s, -0.244f * s, 0.055f * s); cubicTo(-0.212f * s, 0.049f * s, -0.182f * s, 0.064f * s, -0.178f * s, 0.088f * s); close() }
    val p5 = Path().apply { moveTo(0.222f * s, 0.229f * s); cubicTo(0.209f * s, 0.252f * s, 0.173f * s, 0.257f * s, 0.142f * s, 0.239f * s); cubicTo(0.112f * s, 0.222f * s, 0.098f * s, 0.188f * s, 0.112f * s, 0.165f * s); cubicTo(0.125f * s, 0.141f * s, 0.161f * s, 0.136f * s, 0.192f * s, 0.154f * s); cubicTo(0.222f * s, 0.172f * s, 0.236f * s, 0.205f * s, 0.222f * s, 0.229f * s); close() }
    val p6 = Path().apply { moveTo(0.01f * s, 0.265f * s); cubicTo(0.01f * s, 0.287f * s, -0.012f * s, 0.305f * s, -0.039f * s, 0.305f * s); cubicTo(-0.066f * s, 0.305f * s, -0.089f * s, 0.287f * s, -0.089f * s, 0.265f * s); cubicTo(-0.089f * s, 0.244f * s, -0.066f * s, 0.226f * s, -0.039f * s, 0.226f * s); cubicTo(-0.012f * s, 0.226f * s, 0.01f * s, 0.244f * s, 0.01f * s, 0.265f * s); close() }
    val p7 = Path().apply { moveTo(0.367f * s, 0.061f * s); cubicTo(0.362f * s, 0.079f * s, 0.339f * s, 0.089f * s, 0.315f * s, 0.082f * s); cubicTo(0.292f * s, 0.076f * s, 0.277f * s, 0.056f * s, 0.281f * s, 0.038f * s); cubicTo(0.286f * s, 0.019f * s, 0.31f * s, 0.01f * s, 0.333f * s, 0.016f * s); cubicTo(0.357f * s, 0.022f * s, 0.372f * s, 0.042f * s, 0.367f * s, 0.061f * s); close() }
    val p8 = Path().apply { moveTo(0.02f * s, -0.295f * s); cubicTo(0.02f * s, -0.277f * s, 0.002f * s, -0.263f * s, -0.02f * s, -0.263f * s); cubicTo(-0.041f * s, -0.263f * s, -0.059f * s, -0.277f * s, -0.059f * s, -0.295f * s); cubicTo(-0.059f * s, -0.312f * s, -0.041f * s, -0.326f * s, -0.02f * s, -0.326f * s); cubicTo(0.002f * s, -0.326f * s, 0.02f * s, -0.312f * s, 0.02f * s, -0.295f * s); close() }
    drawPath(p0, sil ?: Color(0xFFA16F3C))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFC98B4B))
        }
        drawPath(p2, Color(0xFF5A3320))
        drawPath(p3, Color(0xFF5A3320))
        drawPath(p4, Color(0xFF5A3320))
        drawPath(p5, Color(0xFF5A3320))
        drawPath(p6, Color(0xFF5A3320))
        drawPath(p7, Color(0xFF5A3320))
        drawPath(p8, Color(0xFF5A3320))
        drawOval(Color(0x59FFFFFF), Offset(-0.285f * s, -0.138f * s), Size(0.059f * s, 0.177f * s))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artMaison(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.289f * s, -0.061f * s); lineTo(0.289f * s, -0.061f * s); cubicTo(0.311f * s, -0.061f * s, 0.328f * s, -0.044f * s, 0.328f * s, -0.023f * s); lineTo(0.328f * s, 0.382f * s); cubicTo(0.328f * s, 0.404f * s, 0.311f * s, 0.421f * s, 0.289f * s, 0.421f * s); lineTo(-0.289f * s, 0.421f * s); cubicTo(-0.31f * s, 0.421f * s, -0.328f * s, 0.404f * s, -0.328f * s, 0.382f * s); lineTo(-0.328f * s, -0.023f * s); cubicTo(-0.328f * s, -0.044f * s, -0.31f * s, -0.061f * s, -0.289f * s, -0.061f * s); close() }
    val p1 = Path().apply { moveTo(-0.308f * s, -0.09f * s); lineTo(0.27f * s, -0.09f * s); cubicTo(0.291f * s, -0.09f * s, 0.309f * s, -0.073f * s, 0.309f * s, -0.051f * s); lineTo(0.309f * s, 0.353f * s); cubicTo(0.309f * s, 0.375f * s, 0.291f * s, 0.392f * s, 0.27f * s, 0.392f * s); lineTo(-0.308f * s, 0.392f * s); cubicTo(-0.33f * s, 0.392f * s, -0.347f * s, 0.375f * s, -0.347f * s, 0.353f * s); lineTo(-0.347f * s, -0.051f * s); cubicTo(-0.347f * s, -0.073f * s, -0.33f * s, -0.09f * s, -0.308f * s, -0.09f * s); close() }
    val p2 = Path().apply { moveTo(-0.405f * s, -0.023f * s); quadraticTo(-0.453f * s, -0.023f * s, -0.417f * s, -0.055f * s); lineTo(-0.036f * s, -0.405f * s); quadraticTo(0.0f * s, -0.437f * s, 0.036f * s, -0.405f * s); lineTo(0.418f * s, -0.055f * s); quadraticTo(0.453f * s, -0.023f * s, 0.405f * s, -0.023f * s); close() }
    val p3 = Path().apply { moveTo(-0.424f * s, -0.056f * s); quadraticTo(-0.472f * s, -0.056f * s, -0.437f * s, -0.089f * s); lineTo(-0.055f * s, -0.438f * s); quadraticTo(-0.019f * s, -0.471f * s, 0.016f * s, -0.438f * s); lineTo(0.398f * s, -0.089f * s); quadraticTo(0.434f * s, -0.056f * s, 0.386f * s, -0.056f * s); close() }
    val p4 = Path().apply { moveTo(-0.01f * s, 0.141f * s); lineTo(0.01f * s, 0.141f * s); cubicTo(0.052f * s, 0.141f * s, 0.087f * s, 0.176f * s, 0.087f * s, 0.218f * s); lineTo(0.087f * s, 0.344f * s); cubicTo(0.087f * s, 0.386f * s, 0.052f * s, 0.421f * s, 0.01f * s, 0.421f * s); lineTo(-0.01f * s, 0.421f * s); cubicTo(-0.052f * s, 0.421f * s, -0.087f * s, 0.386f * s, -0.087f * s, 0.344f * s); lineTo(-0.087f * s, 0.218f * s); cubicTo(-0.087f * s, 0.176f * s, -0.052f * s, 0.141f * s, -0.01f * s, 0.141f * s); close() }
    drawRoundRect(sil ?: Color(0xFF6B3F25), Offset(0.154f * s, -0.389f * s), Size(0.116f * s, 0.231f * s), CornerRadius(0.019f * s))
    drawRoundRect(sil ?: Color(0xFF2B2D42), Offset(0.154f * s, -0.389f * s), Size(0.116f * s, 0.231f * s), CornerRadius(0.019f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF9D6E44))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFC48A55))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF5C3821))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF7B4B2C))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFFFE7B8))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFFB4783C), 0.014f * s, Offset(0.043f * s, 0.286f * s))
    }
    drawRoundRect(sil ?: Color(0xFFFFE7B8), Offset(-0.275f * s, 0.055f * s), Size(0.125f * s, 0.125f * s), CornerRadius(0.019f * s))
    drawRoundRect(sil ?: Color(0xFF2B2D42), Offset(-0.275f * s, 0.055f * s), Size(0.125f * s, 0.125f * s), CornerRadius(0.019f * s), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.212f * s, 0.055f * s), Offset(-0.212f * s, 0.18f * s), 0.018f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.275f * s, 0.117f * s), Offset(-0.149f * s, 0.117f * s), 0.018f * s, StrokeCap.Round)
    drawRoundRect(sil ?: Color(0xFFFFE7B8), Offset(0.149f * s, 0.055f * s), Size(0.125f * s, 0.125f * s), CornerRadius(0.019f * s))
    drawRoundRect(sil ?: Color(0xFF2B2D42), Offset(0.149f * s, 0.055f * s), Size(0.125f * s, 0.125f * s), CornerRadius(0.019f * s), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.212f * s, 0.055f * s), Offset(0.212f * s, 0.18f * s), 0.018f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.149f * s, 0.117f * s), Offset(0.275f * s, 0.117f * s), 0.018f * s, StrokeCap.Round)
}

private fun DrawScope.artChocolat(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.294f * s, -0.348f * s); lineTo(0.108f * s, -0.433f * s); cubicTo(0.141f * s, -0.44f * s, 0.173f * s, -0.419f * s, 0.18f * s, -0.387f * s); lineTo(0.315f * s, 0.251f * s); cubicTo(0.322f * s, 0.283f * s, 0.301f * s, 0.315f * s, 0.269f * s, 0.322f * s); lineTo(-0.134f * s, 0.408f * s); cubicTo(-0.166f * s, 0.415f * s, -0.198f * s, 0.394f * s, -0.205f * s, 0.361f * s); lineTo(-0.341f * s, -0.276f * s); cubicTo(-0.348f * s, -0.309f * s, -0.327f * s, -0.341f * s, -0.294f * s, -0.348f * s); close() }
    val p1 = Path().apply { moveTo(-0.317f * s, -0.369f * s); lineTo(0.086f * s, -0.455f * s); cubicTo(0.119f * s, -0.462f * s, 0.151f * s, -0.441f * s, 0.157f * s, -0.408f * s); lineTo(0.293f * s, 0.229f * s); cubicTo(0.3f * s, 0.262f * s, 0.279f * s, 0.294f * s, 0.247f * s, 0.3f * s); lineTo(-0.156f * s, 0.386f * s); cubicTo(-0.188f * s, 0.393f * s, -0.22f * s, 0.372f * s, -0.227f * s, 0.34f * s); lineTo(-0.363f * s, -0.298f * s); cubicTo(-0.37f * s, -0.33f * s, -0.349f * s, -0.362f * s, -0.317f * s, -0.369f * s); close() }
    val p2 = Path().apply { moveTo(-0.318f * s, -0.334f * s); lineTo(-0.2f * s, -0.359f * s); cubicTo(-0.186f * s, -0.362f * s, -0.173f * s, -0.353f * s, -0.17f * s, -0.339f * s); lineTo(-0.145f * s, -0.221f * s); cubicTo(-0.142f * s, -0.208f * s, -0.151f * s, -0.194f * s, -0.165f * s, -0.191f * s); lineTo(-0.282f * s, -0.166f * s); cubicTo(-0.296f * s, -0.163f * s, -0.31f * s, -0.172f * s, -0.313f * s, -0.186f * s); lineTo(-0.338f * s, -0.303f * s); cubicTo(-0.341f * s, -0.317f * s, -0.332f * s, -0.331f * s, -0.318f * s, -0.334f * s); close() }
    val p3 = Path().apply { moveTo(-0.15f * s, -0.369f * s); lineTo(-0.033f * s, -0.394f * s); cubicTo(-0.019f * s, -0.397f * s, -0.005f * s, -0.389f * s, -0.002f * s, -0.375f * s); lineTo(0.023f * s, -0.257f * s); cubicTo(0.026f * s, -0.243f * s, 0.017f * s, -0.23f * s, 0.003f * s, -0.227f * s); lineTo(-0.114f * s, -0.202f * s); cubicTo(-0.128f * s, -0.199f * s, -0.142f * s, -0.208f * s, -0.145f * s, -0.221f * s); lineTo(-0.17f * s, -0.339f * s); cubicTo(-0.173f * s, -0.353f * s, -0.164f * s, -0.367f * s, -0.15f * s, -0.369f * s); close() }
    val p4 = Path().apply { moveTo(0.018f * s, -0.405f * s); lineTo(0.135f * s, -0.43f * s); cubicTo(0.149f * s, -0.433f * s, 0.163f * s, -0.424f * s, 0.166f * s, -0.41f * s); lineTo(0.191f * s, -0.293f * s); cubicTo(0.194f * s, -0.279f * s, 0.185f * s, -0.265f * s, 0.171f * s, -0.262f * s); lineTo(0.054f * s, -0.237f * s); cubicTo(0.04f * s, -0.234f * s, 0.026f * s, -0.243f * s, 0.023f * s, -0.257f * s); lineTo(-0.002f * s, -0.375f * s); cubicTo(-0.005f * s, -0.389f * s, 0.004f * s, -0.402f * s, 0.018f * s, -0.405f * s); close() }
    val p5 = Path().apply { moveTo(-0.282f * s, -0.166f * s); lineTo(-0.165f * s, -0.191f * s); cubicTo(-0.151f * s, -0.194f * s, -0.137f * s, -0.185f * s, -0.134f * s, -0.171f * s); lineTo(-0.109f * s, -0.054f * s); cubicTo(-0.106f * s, -0.04f * s, -0.115f * s, -0.026f * s, -0.129f * s, -0.023f * s); lineTo(-0.246f * s, 0.002f * s); cubicTo(-0.26f * s, 0.005f * s, -0.274f * s, -0.004f * s, -0.277f * s, -0.018f * s); lineTo(-0.302f * s, -0.135f * s); cubicTo(-0.305f * s, -0.149f * s, -0.296f * s, -0.163f * s, -0.282f * s, -0.166f * s); close() }
    val p6 = Path().apply { moveTo(-0.114f * s, -0.202f * s); lineTo(0.003f * s, -0.227f * s); cubicTo(0.017f * s, -0.23f * s, 0.031f * s, -0.221f * s, 0.034f * s, -0.207f * s); lineTo(0.059f * s, -0.089f * s); cubicTo(0.062f * s, -0.075f * s, 0.053f * s, -0.062f * s, 0.039f * s, -0.059f * s); lineTo(-0.079f * s, -0.034f * s); cubicTo(-0.093f * s, -0.031f * s, -0.106f * s, -0.04f * s, -0.109f * s, -0.054f * s); lineTo(-0.134f * s, -0.171f * s); cubicTo(-0.137f * s, -0.185f * s, -0.128f * s, -0.199f * s, -0.114f * s, -0.202f * s); close() }
    val p7 = Path().apply { moveTo(0.054f * s, -0.237f * s); lineTo(0.171f * s, -0.262f * s); cubicTo(0.185f * s, -0.265f * s, 0.199f * s, -0.256f * s, 0.202f * s, -0.242f * s); lineTo(0.226f * s, -0.125f * s); cubicTo(0.229f * s, -0.111f * s, 0.221f * s, -0.097f * s, 0.207f * s, -0.094f * s); lineTo(0.089f * s, -0.07f * s); cubicTo(0.075f * s, -0.067f * s, 0.062f * s, -0.075f * s, 0.059f * s, -0.089f * s); lineTo(0.034f * s, -0.207f * s); cubicTo(0.031f * s, -0.221f * s, 0.04f * s, -0.234f * s, 0.054f * s, -0.237f * s); close() }
    val p8 = Path().apply { moveTo(-0.246f * s, 0.002f * s); lineTo(-0.129f * s, -0.023f * s); cubicTo(-0.115f * s, -0.026f * s, -0.101f * s, -0.017f * s, -0.098f * s, -0.003f * s); lineTo(-0.074f * s, 0.114f * s); cubicTo(-0.071f * s, 0.128f * s, -0.079f * s, 0.142f * s, -0.093f * s, 0.145f * s); lineTo(-0.211f * s, 0.17f * s); cubicTo(-0.225f * s, 0.173f * s, -0.238f * s, 0.164f * s, -0.241f * s, 0.15f * s); lineTo(-0.266f * s, 0.032f * s); cubicTo(-0.269f * s, 0.018f * s, -0.26f * s, 0.005f * s, -0.246f * s, 0.002f * s); close() }
    val p9 = Path().apply { moveTo(-0.079f * s, -0.034f * s); lineTo(0.039f * s, -0.059f * s); cubicTo(0.053f * s, -0.062f * s, 0.066f * s, -0.053f * s, 0.069f * s, -0.039f * s); lineTo(0.094f * s, 0.079f * s); cubicTo(0.097f * s, 0.092f * s, 0.088f * s, 0.106f * s, 0.074f * s, 0.109f * s); lineTo(-0.043f * s, 0.134f * s); cubicTo(-0.057f * s, 0.137f * s, -0.071f * s, 0.128f * s, -0.074f * s, 0.114f * s); lineTo(-0.098f * s, -0.003f * s); cubicTo(-0.101f * s, -0.017f * s, -0.093f * s, -0.031f * s, -0.079f * s, -0.034f * s); close() }
    val p10 = Path().apply { moveTo(0.089f * s, -0.07f * s); lineTo(0.207f * s, -0.094f * s); cubicTo(0.221f * s, -0.097f * s, 0.234f * s, -0.089f * s, 0.237f * s, -0.075f * s); lineTo(0.262f * s, 0.043f * s); cubicTo(0.265f * s, 0.057f * s, 0.256f * s, 0.07f * s, 0.242f * s, 0.073f * s); lineTo(0.125f * s, 0.098f * s); cubicTo(0.111f * s, 0.101f * s, 0.097f * s, 0.092f * s, 0.094f * s, 0.079f * s); lineTo(0.069f * s, -0.039f * s); cubicTo(0.066f * s, -0.053f * s, 0.075f * s, -0.067f * s, 0.089f * s, -0.07f * s); close() }
    val p11 = Path().apply { moveTo(-0.211f * s, 0.17f * s); lineTo(-0.093f * s, 0.145f * s); cubicTo(-0.079f * s, 0.142f * s, -0.066f * s, 0.151f * s, -0.063f * s, 0.165f * s); lineTo(-0.038f * s, 0.282f * s); cubicTo(-0.035f * s, 0.296f * s, -0.044f * s, 0.31f * s, -0.058f * s, 0.313f * s); lineTo(-0.175f * s, 0.338f * s); cubicTo(-0.189f * s, 0.34f * s, -0.203f * s, 0.332f * s, -0.206f * s, 0.318f * s); lineTo(-0.231f * s, 0.2f * s); cubicTo(-0.234f * s, 0.186f * s, -0.225f * s, 0.173f * s, -0.211f * s, 0.17f * s); close() }
    val p12 = Path().apply { moveTo(-0.043f * s, 0.134f * s); lineTo(0.074f * s, 0.109f * s); cubicTo(0.088f * s, 0.106f * s, 0.102f * s, 0.115f * s, 0.105f * s, 0.129f * s); lineTo(0.13f * s, 0.246f * s); cubicTo(0.133f * s, 0.26f * s, 0.124f * s, 0.274f * s, 0.11f * s, 0.277f * s); lineTo(-0.007f * s, 0.302f * s); cubicTo(-0.021f * s, 0.305f * s, -0.035f * s, 0.296f * s, -0.038f * s, 0.282f * s); lineTo(-0.063f * s, 0.165f * s); cubicTo(-0.066f * s, 0.151f * s, -0.057f * s, 0.137f * s, -0.043f * s, 0.134f * s); close() }
    val p13 = Path().apply { moveTo(0.125f * s, 0.098f * s); lineTo(0.242f * s, 0.073f * s); cubicTo(0.256f * s, 0.07f * s, 0.27f * s, 0.079f * s, 0.273f * s, 0.093f * s); lineTo(0.298f * s, 0.211f * s); cubicTo(0.301f * s, 0.225f * s, 0.292f * s, 0.238f * s, 0.278f * s, 0.241f * s); lineTo(0.161f * s, 0.266f * s); cubicTo(0.147f * s, 0.269f * s, 0.133f * s, 0.26f * s, 0.13f * s, 0.246f * s); lineTo(0.105f * s, 0.129f * s); cubicTo(0.102f * s, 0.115f * s, 0.111f * s, 0.101f * s, 0.125f * s, 0.098f * s); close() }
    val p14 = Path().apply { moveTo(-0.283f * s, 0.08f * s); lineTo(-0.199f * s, 0.018f * s); lineTo(-0.096f * s, 0.049f * s); lineTo(-0.015f * s, -0.021f * s); lineTo(0.088f * s, 0.01f * s); lineTo(0.17f * s, -0.06f * s); lineTo(0.271f * s, -0.038f * s); lineTo(0.341f * s, 0.289f * s); quadraticTo(0.348f * s, 0.323f * s, 0.314f * s, 0.33f * s); lineTo(-0.172f * s, 0.433f * s); quadraticTo(-0.206f * s, 0.44f * s, -0.213f * s, 0.407f * s); close() }
    val p15 = Path().apply { moveTo(-0.307f * s, 0.05f * s); lineTo(-0.223f * s, -0.012f * s); lineTo(-0.12f * s, 0.019f * s); lineTo(-0.039f * s, -0.051f * s); lineTo(0.065f * s, -0.02f * s); lineTo(0.146f * s, -0.09f * s); lineTo(0.247f * s, -0.068f * s); lineTo(0.317f * s, 0.259f * s); quadraticTo(0.324f * s, 0.293f * s, 0.29f * s, 0.3f * s); lineTo(-0.196f * s, 0.403f * s); quadraticTo(-0.23f * s, 0.411f * s, -0.237f * s, 0.377f * s); close() }
    val p16 = Path().apply { moveTo(-0.12f * s, 0.185f * s); cubicTo(-0.108f * s, 0.241f * s, -0.11f * s, 0.288f * s, -0.124f * s, 0.291f * s); cubicTo(-0.137f * s, 0.294f * s, -0.158f * s, 0.252f * s, -0.17f * s, 0.196f * s); cubicTo(-0.182f * s, 0.14f * s, -0.18f * s, 0.093f * s, -0.166f * s, 0.09f * s); cubicTo(-0.152f * s, 0.087f * s, -0.132f * s, 0.13f * s, -0.12f * s, 0.185f * s); close() }
    drawPath(p0, sil ?: Color(0xFF623B22))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF7A4A2A))
        }
    }
    clipPath(p0) {
        drawPath(p2, sil ?: Color(0xFF8E5A36))
        drawPath(p2, sil ?: Color(0xFF4B2A18), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p3, sil ?: Color(0xFF8E5A36))
        drawPath(p3, sil ?: Color(0xFF4B2A18), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p4, sil ?: Color(0xFF8E5A36))
        drawPath(p4, sil ?: Color(0xFF4B2A18), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p5, sil ?: Color(0xFF8E5A36))
        drawPath(p5, sil ?: Color(0xFF4B2A18), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p6, sil ?: Color(0xFF8E5A36))
        drawPath(p6, sil ?: Color(0xFF4B2A18), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p7, sil ?: Color(0xFF8E5A36))
        drawPath(p7, sil ?: Color(0xFF4B2A18), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p8, sil ?: Color(0xFF8E5A36))
        drawPath(p8, sil ?: Color(0xFF4B2A18), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p9, sil ?: Color(0xFF8E5A36))
        drawPath(p9, sil ?: Color(0xFF4B2A18), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p10, sil ?: Color(0xFF8E5A36))
        drawPath(p10, sil ?: Color(0xFF4B2A18), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p11, sil ?: Color(0xFF8E5A36))
        drawPath(p11, sil ?: Color(0xFF4B2A18), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p12, sil ?: Color(0xFF8E5A36))
        drawPath(p12, sil ?: Color(0xFF4B2A18), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p13, sil ?: Color(0xFF8E5A36))
        drawPath(p13, sil ?: Color(0xFF4B2A18), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p14, sil ?: Color(0xFFB4B9C4))
    if (sil == null) {
        clipPath(p14) {
            drawPath(p15, Color(0xFFD4DAE6))
        }
        drawPath(p16, Color(0x99FFFFFF))
    }
    drawPath(p14, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artNuage(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.277f * s, 0.276f * s); cubicTo(-0.479f * s, 0.276f * s, -0.496f * s, -0.01f * s, -0.277f * s, -0.035f * s); cubicTo(-0.319f * s, -0.312f * s, 0.008f * s, -0.363f * s, 0.059f * s, -0.119f * s); cubicTo(0.126f * s, -0.346f * s, 0.378f * s, -0.262f * s, 0.327f * s, -0.018f * s); cubicTo(0.495f * s, -0.001f * s, 0.462f * s, 0.276f * s, 0.26f * s, 0.276f * s); close() }
    val p1 = Path().apply { moveTo(-0.294f * s, 0.225f * s); cubicTo(-0.496f * s, 0.225f * s, -0.513f * s, -0.06f * s, -0.294f * s, -0.085f * s); cubicTo(-0.336f * s, -0.363f * s, -0.009f * s, -0.413f * s, 0.042f * s, -0.169f * s); cubicTo(0.109f * s, -0.396f * s, 0.361f * s, -0.312f * s, 0.311f * s, -0.069f * s); cubicTo(0.479f * s, -0.052f * s, 0.445f * s, 0.225f * s, 0.243f * s, 0.225f * s); close() }
    val p2 = Path().apply { moveTo(-0.11f * s, -0.173f * s); cubicTo(-0.097f * s, -0.154f * s, -0.104f * s, -0.126f * s, -0.127f * s, -0.11f * s); cubicTo(-0.15f * s, -0.094f * s, -0.179f * s, -0.096f * s, -0.193f * s, -0.115f * s); cubicTo(-0.206f * s, -0.134f * s, -0.198f * s, -0.163f * s, -0.175f * s, -0.179f * s); cubicTo(-0.153f * s, -0.195f * s, -0.123f * s, -0.192f * s, -0.11f * s, -0.173f * s); close() }
    drawPath(p0, sil ?: Color(0xFFDDDCD6))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFF6F4EE))
        }
        drawPath(p2, Color(0x8CFFFFFF))
        drawOval(Color(0x8CFFFFFF), Offset(-0.37f * s, 0.04f * s), Size(0.05f * s, 0.05f * s))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artFantome(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.262f * s, -0.01f * s); cubicTo(-0.248f * s, 0.017f * s, -0.273f * s, 0.06f * s, -0.32f * s, 0.084f * s); cubicTo(-0.366f * s, 0.109f * s, -0.416f * s, 0.107f * s, -0.43f * s, 0.079f * s); cubicTo(-0.445f * s, 0.052f * s, -0.419f * s, 0.01f * s, -0.373f * s, -0.015f * s); cubicTo(-0.326f * s, -0.04f * s, -0.277f * s, -0.037f * s, -0.262f * s, -0.01f * s); close() }
    val p1 = Path().apply { moveTo(0.43f * s, 0.079f * s); cubicTo(0.416f * s, 0.107f * s, 0.366f * s, 0.109f * s, 0.32f * s, 0.084f * s); cubicTo(0.273f * s, 0.06f * s, 0.248f * s, 0.017f * s, 0.262f * s, -0.01f * s); cubicTo(0.277f * s, -0.037f * s, 0.326f * s, -0.04f * s, 0.373f * s, -0.015f * s); cubicTo(0.419f * s, 0.01f * s, 0.445f * s, 0.052f * s, 0.43f * s, 0.079f * s); close() }
    val p2 = Path().apply { moveTo(-0.312f * s, 0.286f * s); lineTo(-0.312f * s, -0.061f * s); cubicTo(-0.312f * s, -0.286f * s, -0.173f * s, -0.389f * s, 0.0f * s, -0.389f * s); cubicTo(0.173f * s, -0.389f * s, 0.312f * s, -0.286f * s, 0.312f * s, -0.061f * s); lineTo(0.312f * s, 0.286f * s); quadraticTo(0.208f * s, 0.493f * s, 0.104f * s, 0.286f * s); quadraticTo(0.0f * s, 0.493f * s, -0.104f * s, 0.286f * s); quadraticTo(-0.208f * s, 0.493f * s, -0.312f * s, 0.286f * s); close() }
    val p3 = Path().apply { moveTo(-0.333f * s, 0.251f * s); lineTo(-0.333f * s, -0.095f * s); cubicTo(-0.333f * s, -0.32f * s, -0.195f * s, -0.424f * s, -0.022f * s, -0.424f * s); cubicTo(0.152f * s, -0.424f * s, 0.29f * s, -0.32f * s, 0.29f * s, -0.095f * s); lineTo(0.29f * s, 0.251f * s); quadraticTo(0.186f * s, 0.459f * s, 0.082f * s, 0.251f * s); quadraticTo(-0.022f * s, 0.459f * s, -0.125f * s, 0.251f * s); quadraticTo(-0.229f * s, 0.459f * s, -0.333f * s, 0.251f * s); close() }
    val p4 = Path().apply { moveTo(-0.138f * s, -0.276f * s); cubicTo(-0.118f * s, -0.248f * s, -0.119f * s, -0.215f * s, -0.138f * s, -0.201f * s); cubicTo(-0.158f * s, -0.188f * s, -0.189f * s, -0.199f * s, -0.209f * s, -0.226f * s); cubicTo(-0.228f * s, -0.254f * s, -0.227f * s, -0.287f * s, -0.208f * s, -0.301f * s); cubicTo(-0.188f * s, -0.314f * s, -0.157f * s, -0.303f * s, -0.138f * s, -0.276f * s); close() }
    val p5 = Path().apply { moveTo(-0.069f * s, 0.009f * s); quadraticTo(0.0f * s, 0.113f * s, 0.069f * s, 0.009f * s); close() }
    drawPath(p0, sil ?: Color(0xFFF6F4EE))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFF6F4EE))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFDDDCD6))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFF6F4EE))
        }
        drawPath(p4, Color(0x8CFFFFFF))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0xFF2B2D42), Offset(-0.164f * s, -0.156f * s), Size(0.087f * s, 0.121f * s))
        drawOval(Color(0xFF2B2D42), Offset(0.078f * s, -0.156f * s), Size(0.087f * s, 0.121f * s))
        drawCircle(Color(0xFFFFFFFF), 0.014f * s, Offset(-0.108f * s, -0.121f * s))
        drawCircle(Color(0xFFFFFFFF), 0.014f * s, Offset(0.134f * s, -0.121f * s))
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(-0.26f * s, -0.029f * s), Size(0.087f * s, 0.059f * s))
        drawOval(Color(0x66FF4F86), Offset(0.173f * s, -0.029f * s), Size(0.087f * s, 0.059f * s))
    }
}

private fun DrawScope.artBonhomme(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.182f * s, 0.073f * s); lineTo(-0.383f * s, -0.055f * s); moveTo(-0.328f * s, -0.027f * s); lineTo(-0.401f * s, -0.137f * s) }
    val p1 = Path().apply { moveTo(0.182f * s, 0.073f * s); lineTo(0.383f * s, -0.055f * s); moveTo(0.328f * s, -0.027f * s); lineTo(0.401f * s, -0.137f * s) }
    val p2 = Path().apply { moveTo(0.246f * s, 0.164f * s); cubicTo(0.246f * s, 0.295f * s, 0.136f * s, 0.401f * s, 0.0f * s, 0.401f * s); cubicTo(-0.136f * s, 0.401f * s, -0.246f * s, 0.295f * s, -0.246f * s, 0.164f * s); cubicTo(-0.246f * s, 0.033f * s, -0.136f * s, -0.073f * s, 0.0f * s, -0.073f * s); cubicTo(0.136f * s, -0.073f * s, 0.246f * s, 0.033f * s, 0.246f * s, 0.164f * s); close() }
    val p3 = Path().apply { moveTo(0.223f * s, 0.119f * s); cubicTo(0.223f * s, 0.25f * s, 0.113f * s, 0.356f * s, -0.023f * s, 0.356f * s); cubicTo(-0.159f * s, 0.356f * s, -0.269f * s, 0.25f * s, -0.269f * s, 0.119f * s); cubicTo(-0.269f * s, -0.012f * s, -0.159f * s, -0.118f * s, -0.023f * s, -0.118f * s); cubicTo(0.113f * s, -0.118f * s, 0.223f * s, -0.012f * s, 0.223f * s, 0.119f * s); close() }
    val p4 = Path().apply { moveTo(0.182f * s, -0.219f * s); cubicTo(0.182f * s, -0.118f * s, 0.101f * s, -0.036f * s, 0.0f * s, -0.036f * s); cubicTo(-0.101f * s, -0.036f * s, -0.182f * s, -0.118f * s, -0.182f * s, -0.219f * s); cubicTo(-0.182f * s, -0.32f * s, -0.101f * s, -0.401f * s, 0.0f * s, -0.401f * s); cubicTo(0.101f * s, -0.401f * s, 0.182f * s, -0.32f * s, 0.182f * s, -0.219f * s); close() }
    val p5 = Path().apply { moveTo(0.164f * s, -0.255f * s); cubicTo(0.164f * s, -0.155f * s, 0.083f * s, -0.073f * s, -0.018f * s, -0.073f * s); cubicTo(-0.119f * s, -0.073f * s, -0.201f * s, -0.155f * s, -0.201f * s, -0.255f * s); cubicTo(-0.201f * s, -0.356f * s, -0.119f * s, -0.438f * s, -0.018f * s, -0.438f * s); cubicTo(0.083f * s, -0.438f * s, 0.164f * s, -0.356f * s, 0.164f * s, -0.255f * s); close() }
    val p6 = Path().apply { moveTo(-0.061f * s, -0.322f * s); cubicTo(-0.047f * s, -0.301f * s, -0.048f * s, -0.275f * s, -0.065f * s, -0.263f * s); cubicTo(-0.081f * s, -0.252f * s, -0.107f * s, -0.259f * s, -0.121f * s, -0.28f * s); cubicTo(-0.135f * s, -0.301f * s, -0.134f * s, -0.327f * s, -0.117f * s, -0.338f * s); cubicTo(-0.101f * s, -0.35f * s, -0.076f * s, -0.342f * s, -0.061f * s, -0.322f * s); close() }
    val p7 = Path().apply { moveTo(-0.018f * s, -0.191f * s); quadraticTo(-0.018f * s, -0.21f * s, 0.0f * s, -0.206f * s); lineTo(0.146f * s, -0.177f * s); quadraticTo(0.164f * s, -0.173f * s, 0.146f * s, -0.17f * s); lineTo(0.0f * s, -0.14f * s); quadraticTo(-0.018f * s, -0.137f * s, -0.018f * s, -0.155f * s); close() }
    val p8 = Path().apply { moveTo(0.155f * s, -0.082f * s); cubicTo(0.18f * s, -0.082f * s, 0.201f * s, -0.062f * s, 0.201f * s, -0.036f * s); cubicTo(0.201f * s, -0.011f * s, 0.18f * s, 0.009f * s, 0.155f * s, 0.009f * s); lineTo(-0.155f * s, 0.009f * s); cubicTo(-0.18f * s, 0.009f * s, -0.201f * s, -0.011f * s, -0.201f * s, -0.036f * s); cubicTo(-0.201f * s, -0.062f * s, -0.18f * s, -0.082f * s, -0.155f * s, -0.082f * s); close() }
    val p9 = Path().apply { moveTo(0.082f * s, -0.018f * s); lineTo(0.119f * s, -0.018f * s); cubicTo(0.134f * s, -0.018f * s, 0.146f * s, -0.006f * s, 0.146f * s, 0.009f * s); lineTo(0.146f * s, 0.109f * s); cubicTo(0.146f * s, 0.125f * s, 0.134f * s, 0.137f * s, 0.119f * s, 0.137f * s); lineTo(0.082f * s, 0.137f * s); cubicTo(0.067f * s, 0.137f * s, 0.055f * s, 0.125f * s, 0.055f * s, 0.109f * s); lineTo(0.055f * s, 0.009f * s); cubicTo(0.055f * s, -0.006f * s, 0.067f * s, -0.018f * s, 0.082f * s, -0.018f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.107f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.107f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF7A4A2A), style = Stroke(0.035f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF7A4A2A), style = Stroke(0.035f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFDDDCD6))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFF6F4EE))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFDDDCD6))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFF6F4EE))
        }
        drawPath(p6, Color(0x8CFFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.027f * s, Offset(-0.073f * s, -0.274f * s))
        drawCircle(Color(0xFFFFFFFF), 0.008f * s, Offset(-0.081f * s, -0.283f * s))
        drawCircle(Color(0xFF2B2D42), 0.027f * s, Offset(0.073f * s, -0.274f * s))
        drawCircle(Color(0xFFFFFFFF), 0.008f * s, Offset(0.065f * s, -0.283f * s))
    }
    drawPath(p7, sil ?: Color(0xFFFF8A1F))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFE8372E))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFE8372E))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.027f * s, Offset(0.0f * s, 0.082f * s))
        drawCircle(Color(0xFF2B2D42), 0.027f * s, Offset(0.0f * s, 0.182f * s))
        drawCircle(Color(0xFF2B2D42), 0.027f * s, Offset(0.0f * s, 0.283f * s))
    }
}

private fun DrawScope.artElephant(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.13f * s, -0.168f * s); cubicTo(-0.13f * s, -0.05f * s, -0.199f * s, 0.046f * s, -0.283f * s, 0.046f * s); cubicTo(-0.368f * s, 0.046f * s, -0.436f * s, -0.05f * s, -0.436f * s, -0.168f * s); cubicTo(-0.436f * s, -0.287f * s, -0.368f * s, -0.383f * s, -0.283f * s, -0.383f * s); cubicTo(-0.199f * s, -0.383f * s, -0.13f * s, -0.287f * s, -0.13f * s, -0.168f * s); close() }
    val p1 = Path().apply { moveTo(-0.142f * s, -0.199f * s); cubicTo(-0.142f * s, -0.081f * s, -0.21f * s, 0.015f * s, -0.295f * s, 0.015f * s); cubicTo(-0.379f * s, 0.015f * s, -0.448f * s, -0.081f * s, -0.448f * s, -0.199f * s); cubicTo(-0.448f * s, -0.317f * s, -0.379f * s, -0.413f * s, -0.295f * s, -0.413f * s); cubicTo(-0.21f * s, -0.413f * s, -0.142f * s, -0.317f * s, -0.142f * s, -0.199f * s); close() }
    val p2 = Path().apply { moveTo(-0.191f * s, -0.153f * s); cubicTo(-0.191f * s, -0.068f * s, -0.232f * s, 0.0f * s, -0.283f * s, 0.0f * s); cubicTo(-0.334f * s, 0.0f * s, -0.375f * s, -0.068f * s, -0.375f * s, -0.153f * s); cubicTo(-0.375f * s, -0.238f * s, -0.334f * s, -0.306f * s, -0.283f * s, -0.306f * s); cubicTo(-0.232f * s, -0.306f * s, -0.191f * s, -0.238f * s, -0.191f * s, -0.153f * s); close() }
    val p3 = Path().apply { moveTo(0.436f * s, -0.168f * s); cubicTo(0.436f * s, -0.05f * s, 0.368f * s, 0.046f * s, 0.283f * s, 0.046f * s); cubicTo(0.199f * s, 0.046f * s, 0.13f * s, -0.05f * s, 0.13f * s, -0.168f * s); cubicTo(0.13f * s, -0.287f * s, 0.199f * s, -0.383f * s, 0.283f * s, -0.383f * s); cubicTo(0.368f * s, -0.383f * s, 0.436f * s, -0.287f * s, 0.436f * s, -0.168f * s); close() }
    val p4 = Path().apply { moveTo(0.425f * s, -0.199f * s); cubicTo(0.425f * s, -0.081f * s, 0.356f * s, 0.015f * s, 0.272f * s, 0.015f * s); cubicTo(0.187f * s, 0.015f * s, 0.119f * s, -0.081f * s, 0.119f * s, -0.199f * s); cubicTo(0.119f * s, -0.317f * s, 0.187f * s, -0.413f * s, 0.272f * s, -0.413f * s); cubicTo(0.356f * s, -0.413f * s, 0.425f * s, -0.317f * s, 0.425f * s, -0.199f * s); close() }
    val p5 = Path().apply { moveTo(0.375f * s, -0.153f * s); cubicTo(0.375f * s, -0.068f * s, 0.334f * s, 0.0f * s, 0.283f * s, 0.0f * s); cubicTo(0.232f * s, 0.0f * s, 0.191f * s, -0.068f * s, 0.191f * s, -0.153f * s); cubicTo(0.191f * s, -0.238f * s, 0.232f * s, -0.306f * s, 0.283f * s, -0.306f * s); cubicTo(0.334f * s, -0.306f * s, 0.375f * s, -0.238f * s, 0.375f * s, -0.153f * s); close() }
    val p6 = Path().apply { moveTo(-0.096f * s, -0.031f * s); lineTo(-0.096f * s, 0.214f * s); cubicTo(-0.096f * s, 0.329f * s, -0.023f * s, 0.383f * s, 0.077f * s, 0.383f * s); cubicTo(0.184f * s, 0.383f * s, 0.26f * s, 0.321f * s, 0.26f * s, 0.214f * s); quadraticTo(0.26f * s, 0.168f * s, 0.214f * s, 0.168f * s); quadraticTo(0.168f * s, 0.168f * s, 0.168f * s, 0.214f * s); cubicTo(0.168f * s, 0.26f * s, 0.13f * s, 0.283f * s, 0.092f * s, 0.276f * s); cubicTo(0.096f * s, 0.268f * s, 0.096f * s, 0.245f * s, 0.096f * s, 0.214f * s); lineTo(0.096f * s, -0.031f * s); close() }
    val p7 = Path().apply { moveTo(-0.111f * s, -0.054f * s); lineTo(-0.111f * s, 0.191f * s); cubicTo(-0.111f * s, 0.306f * s, -0.038f * s, 0.36f * s, 0.061f * s, 0.36f * s); cubicTo(0.168f * s, 0.36f * s, 0.245f * s, 0.299f * s, 0.245f * s, 0.191f * s); quadraticTo(0.245f * s, 0.145f * s, 0.199f * s, 0.145f * s); quadraticTo(0.153f * s, 0.145f * s, 0.153f * s, 0.191f * s); cubicTo(0.153f * s, 0.237f * s, 0.115f * s, 0.26f * s, 0.077f * s, 0.253f * s); cubicTo(0.08f * s, 0.245f * s, 0.08f * s, 0.222f * s, 0.08f * s, 0.191f * s); lineTo(0.08f * s, -0.054f * s); close() }
    val p8 = Path().apply { moveTo(0.237f * s, -0.122f * s); cubicTo(0.237f * s, 0.004f * s, 0.131f * s, 0.107f * s, 0.0f * s, 0.107f * s); cubicTo(-0.131f * s, 0.107f * s, -0.237f * s, 0.004f * s, -0.237f * s, -0.122f * s); cubicTo(-0.237f * s, -0.249f * s, -0.131f * s, -0.352f * s, 0.0f * s, -0.352f * s); cubicTo(0.131f * s, -0.352f * s, 0.237f * s, -0.249f * s, 0.237f * s, -0.122f * s); close() }
    val p9 = Path().apply { moveTo(0.222f * s, -0.161f * s); cubicTo(0.222f * s, -0.034f * s, 0.116f * s, 0.069f * s, -0.015f * s, 0.069f * s); cubicTo(-0.146f * s, 0.069f * s, -0.253f * s, -0.034f * s, -0.253f * s, -0.161f * s); cubicTo(-0.253f * s, -0.287f * s, -0.146f * s, -0.39f * s, -0.015f * s, -0.39f * s); cubicTo(0.116f * s, -0.39f * s, 0.222f * s, -0.287f * s, 0.222f * s, -0.161f * s); close() }
    val p10 = Path().apply { moveTo(-0.076f * s, -0.264f * s); cubicTo(-0.067f * s, -0.248f * s, -0.08f * s, -0.222f * s, -0.105f * s, -0.207f * s); cubicTo(-0.131f * s, -0.193f * s, -0.159f * s, -0.194f * s, -0.169f * s, -0.21f * s); cubicTo(-0.178f * s, -0.227f * s, -0.165f * s, -0.252f * s, -0.14f * s, -0.267f * s); cubicTo(-0.114f * s, -0.282f * s, -0.086f * s, -0.28f * s, -0.076f * s, -0.264f * s); close() }
    drawPath(p0, sil ?: Color(0xFFABB0BA))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFC7CDD8))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p2, Color(0xFFEBD0D8))
    }
    drawPath(p3, sil ?: Color(0xFFABB0BA))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFC7CDD8))
        }
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p5, Color(0xFFEBD0D8))
    }
    drawPath(p6, sil ?: Color(0xFFABB0BA))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFFC7CDD8))
        }
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFABB0BA))
    if (sil == null) {
        clipPath(p8) {
            drawPath(p9, Color(0xFFC7CDD8))
        }
        drawPath(p10, Color(0x8CFFFFFF))
    }
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.044f * s, Offset(-0.099f * s, -0.145f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.044f * s, Offset(-0.099f * s, -0.145f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.026f * s, Offset(-0.095f * s, -0.134f * s))
        drawCircle(Color(0xFFFFFFFF), 0.009f * s, Offset(-0.105f * s, -0.146f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.044f * s, Offset(0.099f * s, -0.145f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.044f * s, Offset(0.099f * s, -0.145f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.026f * s, Offset(0.095f * s, -0.134f * s))
        drawCircle(Color(0xFFFFFFFF), 0.009f * s, Offset(0.085f * s, -0.146f * s))
        drawOval(Color(0x66FF4F86), Offset(-0.207f * s, -0.061f * s), Size(0.061f * s, 0.046f * s))
        drawOval(Color(0x66FF4F86), Offset(0.145f * s, -0.061f * s), Size(0.061f * s, 0.046f * s))
        drawLine(Color(0x4D2B2D42), Offset(-0.046f * s, 0.168f * s), Offset(-0.046f * s, 0.214f * s), 0.016f * s, StrokeCap.Round)
        drawLine(Color(0x4D2B2D42), Offset(-0.046f * s, 0.245f * s), Offset(-0.046f * s, 0.291f * s), 0.016f * s, StrokeCap.Round)
    }
}
