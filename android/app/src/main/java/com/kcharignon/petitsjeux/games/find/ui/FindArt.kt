// GÉNÉRÉ par tools/design/make-objects.js : ne pas modifier à la main (la CI vérifie que ce fichier est à jour).
// Les objets de Cherche et Trouve, dessinés de face ou de côté, centrés en (0, 0), dans le carré de côté `s` (en pixels). Style plat, contour encre.
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
enum class FindKind { NOURRITURE, ANIMAL, TRANSPORT, NATURE, OBJET, VETEMENT, INSTRUMENT }

/** Les 120 objets : identifiant (écrit dans la sauvegarde ou les tests, ne change jamais), nom (« une pomme »), famille de couleur, sorte. */
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
    ELEPHANT("elephant", "un éléphant", FindHue.NEUTRE, FindKind.ANIMAL),
    TOMATE("tomate", "une tomate", FindHue.ROUGE, FindKind.NOURRITURE),
    PIMENT("piment", "un piment", FindHue.ROUGE, FindKind.NOURRITURE),
    COCCINELLE("coccinelle", "une coccinelle", FindHue.ROUGE, FindKind.ANIMAL),
    POMPIER("pompier", "un camion de pompiers", FindHue.ROUGE, FindKind.TRANSPORT),
    TULIPE("tulipe", "une tulipe", FindHue.ROUGE, FindKind.NATURE),
    CERFVOLANT("cerfvolant", "un cerf-volant", FindHue.ROUGE, FindKind.OBJET),
    BONNET("bonnet", "un bonnet", FindHue.ROUGE, FindKind.VETEMENT),
    CROISSANT("croissant", "un croissant", FindHue.ORANGE, FindKind.NOURRITURE),
    PIZZA("pizza", "une part de pizza", FindHue.ORANGE, FindKind.NOURRITURE),
    HAMBURGER("hamburger", "un hamburger", FindHue.ORANGE, FindKind.NOURRITURE),
    CRABE("crabe", "un crabe", FindHue.ORANGE, FindKind.ANIMAL),
    LION("lion", "un lion", FindHue.ORANGE, FindKind.ANIMAL),
    FEUILLE("feuille", "une feuille d’automne", FindHue.ORANGE, FindKind.NATURE),
    MONTGOLFIERE("montgolfiere", "une montgolfière", FindHue.ORANGE, FindKind.TRANSPORT),
    CONE("cone", "un cône de chantier", FindHue.ORANGE, FindKind.OBJET),
    CITRON("citron", "un citron", FindHue.JAUNE, FindKind.NOURRITURE),
    MAIS("mais", "un épi de maïs", FindHue.JAUNE, FindKind.NOURRITURE),
    ANANAS("ananas", "un ananas", FindHue.JAUNE, FindKind.NOURRITURE),
    CANARD("canard", "un canard", FindHue.JAUNE, FindKind.ANIMAL),
    ABEILLE("abeille", "une abeille", FindHue.JAUNE, FindKind.ANIMAL),
    TOURNESOL("tournesol", "un tournesol", FindHue.JAUNE, FindKind.NATURE),
    LUNE("lune", "la lune", FindHue.JAUNE, FindKind.NATURE),
    BUS("bus", "un bus d’école", FindHue.JAUNE, FindKind.TRANSPORT),
    COURONNE("couronne", "une couronne", FindHue.JAUNE, FindKind.OBJET),
    TROMPETTE("trompette", "une trompette", FindHue.JAUNE, FindKind.INSTRUMENT),
    BROCOLI("brocoli", "un brocoli", FindHue.VERT, FindKind.NOURRITURE),
    PASTEQUE("pasteque", "une pastèque", FindHue.VERT, FindKind.NOURRITURE),
    CROCODILE("crocodile", "un crocodile", FindHue.VERT, FindKind.ANIMAL),
    DINOSAURE("dinosaure", "un dinosaure", FindHue.VERT, FindKind.ANIMAL),
    SERPENT("serpent", "un serpent", FindHue.VERT, FindKind.ANIMAL),
    TREFLE("trefle", "un trèfle", FindHue.VERT, FindKind.NATURE),
    PALMIER("palmier", "un palmier", FindHue.VERT, FindKind.NATURE),
    TRACTEUR("tracteur", "un tracteur", FindHue.VERT, FindKind.TRANSPORT),
    DAUPHIN("dauphin", "un dauphin", FindHue.BLEU, FindKind.ANIMAL),
    OISEAU("oiseau", "un oiseau", FindHue.BLEU, FindKind.ANIMAL),
    HIPPOCAMPE("hippocampe", "un hippocampe", FindHue.BLEU, FindKind.ANIMAL),
    FLOCON("flocon", "un flocon de neige", FindHue.BLEU, FindKind.NATURE),
    AVION("avion", "un avion", FindHue.BLEU, FindKind.TRANSPORT),
    VELO("velo", "un vélo", FindHue.BLEU, FindKind.TRANSPORT),
    SEAU("seau", "un seau de plage", FindHue.BLEU, FindKind.OBJET),
    ROBOT("robot", "un robot", FindHue.BLEU, FindKind.OBJET),
    CASQUETTE("casquette", "une casquette", FindHue.BLEU, FindKind.VETEMENT),
    PRUNE("prune", "une prune", FindHue.VIOLET, FindKind.NOURRITURE),
    BONBON("bonbon", "un bonbon", FindHue.VIOLET, FindKind.NOURRITURE),
    POULPE("poulpe", "une pieuvre", FindHue.VIOLET, FindKind.ANIMAL),
    CHAUVESOURIS("chauvesouris", "une chauve-souris", FindHue.VIOLET, FindKind.ANIMAL),
    LAVANDE("lavande", "de la lavande", FindHue.VIOLET, FindKind.NATURE),
    PENSEE("pensee", "une pensée", FindHue.VIOLET, FindKind.NATURE),
    DIAMANT("diamant", "un diamant", FindHue.VIOLET, FindKind.OBJET),
    BAGUETTE("baguette", "une baguette magique", FindHue.VIOLET, FindKind.OBJET),
    CHAPEAUMAGIQUE("chapeaumagique", "un chapeau de magicien", FindHue.VIOLET, FindKind.VETEMENT),
    DONUT("donut", "un beignet", FindHue.ROSE, FindKind.NOURRITURE),
    MACARON("macaron", "un macaron", FindHue.ROSE, FindKind.NOURRITURE),
    BARBAPAPA("barbapapa", "une barbe à papa", FindHue.ROSE, FindKind.NOURRITURE),
    SUCETTE("sucette", "une sucette", FindHue.ROSE, FindKind.NOURRITURE),
    FLAMANT("flamant", "un flamant rose", FindHue.ROSE, FindKind.ANIMAL),
    CREVETTE("crevette", "une crevette", FindHue.ROSE, FindKind.ANIMAL),
    FLEUR("fleur", "une fleur", FindHue.ROSE, FindKind.NATURE),
    COQUILLAGE("coquillage", "un coquillage", FindHue.ROSE, FindKind.NATURE),
    CADEAU("cadeau", "un cadeau", FindHue.ROSE, FindKind.OBJET),
    ROBE("robe", "une robe", FindHue.ROSE, FindKind.VETEMENT),
    PAIN("pain", "un pain", FindHue.MARRON, FindKind.NOURRITURE),
    CHIEN("chien", "un chien", FindHue.MARRON, FindKind.ANIMAL),
    SINGE("singe", "un singe", FindHue.MARRON, FindKind.ANIMAL),
    HIBOU("hibou", "un hibou", FindHue.MARRON, FindKind.ANIMAL),
    ESCARGOT("escargot", "un escargot", FindHue.MARRON, FindKind.ANIMAL),
    GLAND("gland", "un gland", FindHue.MARRON, FindKind.NATURE),
    POMMEDEPIN("pommedepin", "une pomme de pin", FindHue.MARRON, FindKind.NATURE),
    VALISE("valise", "une valise", FindHue.MARRON, FindKind.OBJET),
    GUITARE("guitare", "une guitare", FindHue.MARRON, FindKind.INSTRUMENT),
    OEUF("oeuf", "un œuf", FindHue.NEUTRE, FindKind.NOURRITURE),
    LAPIN("lapin", "un lapin", FindHue.NEUTRE, FindKind.ANIMAL),
    MOUTON("mouton", "un mouton", FindHue.NEUTRE, FindKind.ANIMAL),
    PANDA("panda", "un panda", FindHue.NEUTRE, FindKind.ANIMAL),
    PINGOUIN("pingouin", "un pingouin", FindHue.NEUTRE, FindKind.ANIMAL),
    FUSEE("fusee", "une fusée", FindHue.NEUTRE, FindKind.TRANSPORT),
    AMPOULE("ampoule", "une ampoule", FindHue.NEUTRE, FindKind.OBJET),
    HORLOGE("horloge", "une horloge", FindHue.NEUTRE, FindKind.OBJET),
    CLE("cle", "une clé", FindHue.NEUTRE, FindKind.OBJET),
    PIANO("piano", "un piano", FindHue.NEUTRE, FindKind.INSTRUMENT);

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
        FindObject.TOMATE -> artTomate(s, sil)
        FindObject.PIMENT -> artPiment(s, sil)
        FindObject.COCCINELLE -> artCoccinelle(s, sil)
        FindObject.POMPIER -> artPompier(s, sil)
        FindObject.TULIPE -> artTulipe(s, sil)
        FindObject.CERFVOLANT -> artCerfvolant(s, sil)
        FindObject.BONNET -> artBonnet(s, sil)
        FindObject.CROISSANT -> artCroissant(s, sil)
        FindObject.PIZZA -> artPizza(s, sil)
        FindObject.HAMBURGER -> artHamburger(s, sil)
        FindObject.CRABE -> artCrabe(s, sil)
        FindObject.LION -> artLion(s, sil)
        FindObject.FEUILLE -> artFeuille(s, sil)
        FindObject.MONTGOLFIERE -> artMontgolfiere(s, sil)
        FindObject.CONE -> artCone(s, sil)
        FindObject.CITRON -> artCitron(s, sil)
        FindObject.MAIS -> artMais(s, sil)
        FindObject.ANANAS -> artAnanas(s, sil)
        FindObject.CANARD -> artCanard(s, sil)
        FindObject.ABEILLE -> artAbeille(s, sil)
        FindObject.TOURNESOL -> artTournesol(s, sil)
        FindObject.LUNE -> artLune(s, sil)
        FindObject.BUS -> artBus(s, sil)
        FindObject.COURONNE -> artCouronne(s, sil)
        FindObject.TROMPETTE -> artTrompette(s, sil)
        FindObject.BROCOLI -> artBrocoli(s, sil)
        FindObject.PASTEQUE -> artPasteque(s, sil)
        FindObject.CROCODILE -> artCrocodile(s, sil)
        FindObject.DINOSAURE -> artDinosaure(s, sil)
        FindObject.SERPENT -> artSerpent(s, sil)
        FindObject.TREFLE -> artTrefle(s, sil)
        FindObject.PALMIER -> artPalmier(s, sil)
        FindObject.TRACTEUR -> artTracteur(s, sil)
        FindObject.DAUPHIN -> artDauphin(s, sil)
        FindObject.OISEAU -> artOiseau(s, sil)
        FindObject.HIPPOCAMPE -> artHippocampe(s, sil)
        FindObject.FLOCON -> artFlocon(s, sil)
        FindObject.AVION -> artAvion(s, sil)
        FindObject.VELO -> artVelo(s, sil)
        FindObject.SEAU -> artSeau(s, sil)
        FindObject.ROBOT -> artRobot(s, sil)
        FindObject.CASQUETTE -> artCasquette(s, sil)
        FindObject.PRUNE -> artPrune(s, sil)
        FindObject.BONBON -> artBonbon(s, sil)
        FindObject.POULPE -> artPoulpe(s, sil)
        FindObject.CHAUVESOURIS -> artChauvesouris(s, sil)
        FindObject.LAVANDE -> artLavande(s, sil)
        FindObject.PENSEE -> artPensee(s, sil)
        FindObject.DIAMANT -> artDiamant(s, sil)
        FindObject.BAGUETTE -> artBaguette(s, sil)
        FindObject.CHAPEAUMAGIQUE -> artChapeaumagique(s, sil)
        FindObject.DONUT -> artDonut(s, sil)
        FindObject.MACARON -> artMacaron(s, sil)
        FindObject.BARBAPAPA -> artBarbapapa(s, sil)
        FindObject.SUCETTE -> artSucette(s, sil)
        FindObject.FLAMANT -> artFlamant(s, sil)
        FindObject.CREVETTE -> artCrevette(s, sil)
        FindObject.FLEUR -> artFleur(s, sil)
        FindObject.COQUILLAGE -> artCoquillage(s, sil)
        FindObject.CADEAU -> artCadeau(s, sil)
        FindObject.ROBE -> artRobe(s, sil)
        FindObject.PAIN -> artPain(s, sil)
        FindObject.CHIEN -> artChien(s, sil)
        FindObject.SINGE -> artSinge(s, sil)
        FindObject.HIBOU -> artHibou(s, sil)
        FindObject.ESCARGOT -> artEscargot(s, sil)
        FindObject.GLAND -> artGland(s, sil)
        FindObject.POMMEDEPIN -> artPommedepin(s, sil)
        FindObject.VALISE -> artValise(s, sil)
        FindObject.GUITARE -> artGuitare(s, sil)
        FindObject.OEUF -> artOeuf(s, sil)
        FindObject.LAPIN -> artLapin(s, sil)
        FindObject.MOUTON -> artMouton(s, sil)
        FindObject.PANDA -> artPanda(s, sil)
        FindObject.PINGOUIN -> artPingouin(s, sil)
        FindObject.FUSEE -> artFusee(s, sil)
        FindObject.AMPOULE -> artAmpoule(s, sil)
        FindObject.HORLOGE -> artHorloge(s, sil)
        FindObject.CLE -> artCle(s, sil)
        FindObject.PIANO -> artPiano(s, sil)
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

private fun DrawScope.artTomate(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.21f * s); quadraticTo(0.0f * s, -0.311f * s, 0.042f * s, -0.379f * s) }
    val p1 = Path().apply { moveTo(0.0f * s, -0.168f * s); cubicTo(0.084f * s, -0.261f * s, 0.27f * s, -0.286f * s, 0.363f * s, -0.185f * s); cubicTo(0.473f * s, -0.058f * s, 0.456f * s, 0.195f * s, 0.321f * s, 0.321f * s); cubicTo(0.219f * s, 0.423f * s, 0.084f * s, 0.423f * s, 0.0f * s, 0.414f * s); cubicTo(-0.084f * s, 0.423f * s, -0.219f * s, 0.423f * s, -0.321f * s, 0.321f * s); cubicTo(-0.456f * s, 0.195f * s, -0.472f * s, -0.058f * s, -0.363f * s, -0.185f * s); cubicTo(-0.27f * s, -0.286f * s, -0.084f * s, -0.261f * s, 0.0f * s, -0.168f * s); close() }
    val p2 = Path().apply { moveTo(-0.021f * s, -0.21f * s); cubicTo(0.063f * s, -0.303f * s, 0.249f * s, -0.328f * s, 0.342f * s, -0.227f * s); cubicTo(0.451f * s, -0.101f * s, 0.435f * s, 0.153f * s, 0.3f * s, 0.279f * s); cubicTo(0.198f * s, 0.38f * s, 0.063f * s, 0.38f * s, -0.021f * s, 0.372f * s); cubicTo(-0.105f * s, 0.38f * s, -0.24f * s, 0.38f * s, -0.342f * s, 0.279f * s); cubicTo(-0.477f * s, 0.153f * s, -0.494f * s, -0.101f * s, -0.384f * s, -0.227f * s); cubicTo(-0.291f * s, -0.328f * s, -0.105f * s, -0.303f * s, -0.021f * s, -0.21f * s); close() }
    val p3 = Path().apply { moveTo(-0.213f * s, 0.013f * s); cubicTo(-0.198f * s, 0.067f * s, -0.208f * s, 0.117f * s, -0.235f * s, 0.124f * s); cubicTo(-0.262f * s, 0.131f * s, -0.296f * s, 0.093f * s, -0.31f * s, 0.039f * s); cubicTo(-0.325f * s, -0.015f * s, -0.315f * s, -0.064f * s, -0.288f * s, -0.072f * s); cubicTo(-0.261f * s, -0.079f * s, -0.227f * s, -0.041f * s, -0.213f * s, 0.013f * s); close() }
    val p4 = Path().apply { moveTo(-0.009f * s, -0.313f * s); quadraticTo(0.0f * s, -0.337f * s, 0.009f * s, -0.313f * s); lineTo(0.035f * s, -0.248f * s); quadraticTo(0.045f * s, -0.224f * s, 0.07f * s, -0.226f * s); lineTo(0.248f * s, -0.236f * s); quadraticTo(0.273f * s, -0.238f * s, 0.249f * s, -0.231f * s); lineTo(0.097f * s, -0.188f * s); quadraticTo(0.072f * s, -0.182f * s, 0.089f * s, -0.163f * s); lineTo(0.152f * s, -0.096f * s); quadraticTo(0.169f * s, -0.077f * s, 0.146f * s, -0.088f * s); lineTo(0.023f * s, -0.145f * s); quadraticTo(0.0f * s, -0.155f * s, -0.023f * s, -0.145f * s); lineTo(-0.146f * s, -0.088f * s); quadraticTo(-0.169f * s, -0.077f * s, -0.151f * s, -0.096f * s); lineTo(-0.089f * s, -0.163f * s); quadraticTo(-0.072f * s, -0.182f * s, -0.097f * s, -0.188f * s); lineTo(-0.248f * s, -0.231f * s); quadraticTo(-0.273f * s, -0.238f * s, -0.248f * s, -0.236f * s); lineTo(-0.07f * s, -0.226f * s); quadraticTo(-0.045f * s, -0.224f * s, -0.035f * s, -0.248f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.127f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF5E8F3A), style = Stroke(0.055f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFBD2F26))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFFEC3B2F))
        }
        drawPath(p3, Color(0x8CFFFFFF))
        drawOval(Color(0x8CFFFFFF), Offset(-0.266f * s, 0.191f * s), Size(0.042f * s, 0.042f * s))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF3FAE49))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artPiment(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.199f * s, -0.259f * s); quadraticTo(-0.19f * s, -0.35f * s, -0.109f * s, -0.386f * s) }
    val p1 = Path().apply { moveTo(-0.199f * s, -0.205f * s); cubicTo(-0.252f * s, -0.219f * s, -0.327f * s, -0.237f * s, -0.356f * s, -0.211f * s); cubicTo(-0.386f * s, -0.185f * s, -0.378f * s, -0.102f * s, -0.379f * s, -0.05f * s); cubicTo(-0.38f * s, 0.002f * s, -0.375f * s, 0.053f * s, -0.364f * s, 0.1f * s); cubicTo(-0.352f * s, 0.147f * s, -0.334f * s, 0.193f * s, -0.312f * s, 0.232f * s); cubicTo(-0.289f * s, 0.271f * s, -0.259f * s, 0.308f * s, -0.226f * s, 0.337f * s); cubicTo(-0.193f * s, 0.366f * s, -0.155f * s, 0.389f * s, -0.115f * s, 0.405f * s); cubicTo(-0.076f * s, 0.421f * s, -0.033f * s, 0.43f * s, 0.009f * s, 0.433f * s); cubicTo(0.051f * s, 0.436f * s, 0.094f * s, 0.431f * s, 0.137f * s, 0.421f * s); cubicTo(0.179f * s, 0.412f * s, 0.222f * s, 0.397f * s, 0.262f * s, 0.374f * s); cubicTo(0.303f * s, 0.351f * s, 0.383f * s, 0.297f * s, 0.379f * s, 0.283f * s); cubicTo(0.376f * s, 0.27f * s, 0.281f * s, 0.293f * s, 0.24f * s, 0.294f * s); cubicTo(0.198f * s, 0.294f * s, 0.162f * s, 0.291f * s, 0.13f * s, 0.285f * s); cubicTo(0.098f * s, 0.279f * s, 0.071f * s, 0.268f * s, 0.048f * s, 0.257f * s); cubicTo(0.025f * s, 0.245f * s, 0.008f * s, 0.231f * s, -0.007f * s, 0.216f * s); cubicTo(-0.022f * s, 0.201f * s, -0.032f * s, 0.185f * s, -0.041f * s, 0.169f * s); cubicTo(-0.05f * s, 0.152f * s, -0.056f * s, 0.135f * s, -0.06f * s, 0.115f * s); cubicTo(-0.065f * s, 0.095f * s, -0.068f * s, 0.074f * s, -0.068f * s, 0.05f * s); cubicTo(-0.069f * s, 0.026f * s, -0.068f * s, 0.0f * s, -0.063f * s, -0.029f * s); cubicTo(-0.059f * s, -0.059f * s, -0.019f * s, -0.098f * s, -0.042f * s, -0.127f * s); cubicTo(-0.065f * s, -0.156f * s, -0.147f * s, -0.191f * s, -0.199f * s, -0.205f * s); close() }
    val p2 = Path().apply { moveTo(-0.217f * s, -0.241f * s); cubicTo(-0.27f * s, -0.255f * s, -0.345f * s, -0.273f * s, -0.375f * s, -0.247f * s); cubicTo(-0.404f * s, -0.221f * s, -0.396f * s, -0.138f * s, -0.397f * s, -0.086f * s); cubicTo(-0.398f * s, -0.034f * s, -0.393f * s, 0.017f * s, -0.382f * s, 0.064f * s); cubicTo(-0.37f * s, 0.111f * s, -0.352f * s, 0.156f * s, -0.33f * s, 0.196f * s); cubicTo(-0.307f * s, 0.235f * s, -0.277f * s, 0.272f * s, -0.244f * s, 0.301f * s); cubicTo(-0.211f * s, 0.329f * s, -0.173f * s, 0.353f * s, -0.134f * s, 0.369f * s); cubicTo(-0.094f * s, 0.385f * s, -0.051f * s, 0.394f * s, -0.009f * s, 0.397f * s); cubicTo(0.033f * s, 0.4f * s, 0.076f * s, 0.395f * s, 0.119f * s, 0.385f * s); cubicTo(0.161f * s, 0.375f * s, 0.204f * s, 0.36f * s, 0.244f * s, 0.337f * s); cubicTo(0.285f * s, 0.314f * s, 0.365f * s, 0.26f * s, 0.361f * s, 0.247f * s); cubicTo(0.358f * s, 0.234f * s, 0.263f * s, 0.257f * s, 0.222f * s, 0.257f * s); cubicTo(0.18f * s, 0.258f * s, 0.144f * s, 0.255f * s, 0.112f * s, 0.249f * s); cubicTo(0.08f * s, 0.243f * s, 0.053f * s, 0.232f * s, 0.03f * s, 0.221f * s); cubicTo(0.007f * s, 0.209f * s, -0.01f * s, 0.194f * s, -0.025f * s, 0.18f * s); cubicTo(-0.04f * s, 0.165f * s, -0.05f * s, 0.149f * s, -0.059f * s, 0.132f * s); cubicTo(-0.068f * s, 0.116f * s, -0.074f * s, 0.098f * s, -0.078f * s, 0.079f * s); cubicTo(-0.083f * s, 0.059f * s, -0.086f * s, 0.038f * s, -0.086f * s, 0.014f * s); cubicTo(-0.087f * s, -0.01f * s, -0.086f * s, -0.036f * s, -0.081f * s, -0.066f * s); cubicTo(-0.077f * s, -0.095f * s, -0.037f * s, -0.134f * s, -0.06f * s, -0.163f * s); cubicTo(-0.083f * s, -0.192f * s, -0.165f * s, -0.227f * s, -0.217f * s, -0.241f * s); close() }
    val p3 = Path().apply { moveTo(-0.2f * s, 0.254f * s); cubicTo(-0.174f * s, 0.29f * s, -0.16f * s, 0.325f * s, -0.169f * s, 0.332f * s); cubicTo(-0.178f * s, 0.338f * s, -0.206f * s, 0.313f * s, -0.232f * s, 0.276f * s); cubicTo(-0.258f * s, 0.24f * s, -0.272f * s, 0.205f * s, -0.263f * s, 0.199f * s); cubicTo(-0.254f * s, 0.192f * s, -0.226f * s, 0.217f * s, -0.2f * s, 0.254f * s); close() }
    val p4 = Path().apply { moveTo(-0.371f * s, -0.142f * s); cubicTo(-0.38f * s, -0.241f * s, -0.281f * s, -0.277f * s, -0.199f * s, -0.277f * s); cubicTo(-0.118f * s, -0.277f * s, -0.018f * s, -0.241f * s, -0.027f * s, -0.142f * s); quadraticTo(-0.199f * s, -0.105f * s, -0.371f * s, -0.142f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.142f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF4C9A3A), style = Stroke(0.07f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFBA2C25))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFFE8372E))
        }
        drawPath(p3, Color(0x8CFFFFFF))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF3FAE49))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCoccinelle(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.077f * s, -0.309f * s); quadraticTo(-0.12f * s, -0.394f * s, -0.206f * s, -0.429f * s) }
    val p1 = Path().apply { moveTo(0.077f * s, -0.309f * s); quadraticTo(0.12f * s, -0.394f * s, 0.206f * s, -0.429f * s) }
    val p2 = Path().apply { moveTo(0.197f * s, -0.257f * s); cubicTo(0.197f * s, -0.177f * s, 0.109f * s, -0.112f * s, 0.0f * s, -0.112f * s); cubicTo(-0.109f * s, -0.112f * s, -0.197f * s, -0.177f * s, -0.197f * s, -0.257f * s); cubicTo(-0.197f * s, -0.338f * s, -0.109f * s, -0.403f * s, 0.0f * s, -0.403f * s); cubicTo(0.109f * s, -0.403f * s, 0.197f * s, -0.338f * s, 0.197f * s, -0.257f * s); close() }
    val p3 = Path().apply { moveTo(0.189f * s, -0.274f * s); cubicTo(0.189f * s, -0.194f * s, 0.1f * s, -0.129f * s, -0.009f * s, -0.129f * s); cubicTo(-0.117f * s, -0.129f * s, -0.206f * s, -0.194f * s, -0.206f * s, -0.274f * s); cubicTo(-0.206f * s, -0.355f * s, -0.117f * s, -0.42f * s, -0.009f * s, -0.42f * s); cubicTo(0.1f * s, -0.42f * s, 0.189f * s, -0.355f * s, 0.189f * s, -0.274f * s); close() }
    val p4 = Path().apply { moveTo(0.36f * s, 0.086f * s); cubicTo(0.36f * s, 0.275f * s, 0.199f * s, 0.428f * s, 0.0f * s, 0.428f * s); cubicTo(-0.199f * s, 0.428f * s, -0.36f * s, 0.275f * s, -0.36f * s, 0.086f * s); cubicTo(-0.36f * s, -0.104f * s, -0.199f * s, -0.257f * s, 0.0f * s, -0.257f * s); cubicTo(0.199f * s, -0.257f * s, 0.36f * s, -0.104f * s, 0.36f * s, 0.086f * s); close() }
    val p5 = Path().apply { moveTo(0.339f * s, 0.043f * s); cubicTo(0.339f * s, 0.232f * s, 0.177f * s, 0.386f * s, -0.021f * s, 0.386f * s); cubicTo(-0.22f * s, 0.386f * s, -0.381f * s, 0.232f * s, -0.381f * s, 0.043f * s); cubicTo(-0.381f * s, -0.147f * s, -0.22f * s, -0.3f * s, -0.021f * s, -0.3f * s); cubicTo(0.177f * s, -0.3f * s, 0.339f * s, -0.147f * s, 0.339f * s, 0.043f * s); close() }
    val p6 = Path().apply { moveTo(-0.174f * s, -0.071f * s); cubicTo(-0.19f * s, -0.027f * s, -0.221f * s, 0.003f * s, -0.244f * s, -0.005f * s); cubicTo(-0.266f * s, -0.013f * s, -0.271f * s, -0.056f * s, -0.255f * s, -0.101f * s); cubicTo(-0.238f * s, -0.145f * s, -0.207f * s, -0.175f * s, -0.185f * s, -0.166f * s); cubicTo(-0.163f * s, -0.158f * s, -0.158f * s, -0.116f * s, -0.174f * s, -0.071f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.035f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.035f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.291f * s, -0.017f * s), Offset(-0.429f * s, 0.025f * s), 0.05f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.291f * s, 0.137f * s), Offset(-0.429f * s, 0.18f * s), 0.05f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.291f * s, 0.283f * s), Offset(-0.429f * s, 0.325f * s), 0.05f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.291f * s, -0.017f * s), Offset(0.429f * s, 0.025f * s), 0.05f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.291f * s, 0.137f * s), Offset(0.429f * s, 0.18f * s), 0.05f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.291f * s, 0.283f * s), Offset(0.429f * s, 0.325f * s), 0.05f * s, StrokeCap.Round)
    drawPath(p2, sil ?: Color(0xFF292B3E))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF3A3D58))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFFFFFFFF), 0.043f * s, Offset(-0.077f * s, -0.317f * s))
        drawCircle(Color(0xFFFFFFFF), 0.043f * s, Offset(0.077f * s, -0.317f * s))
        drawCircle(Color(0xFF2B2D42), 0.019f * s, Offset(-0.073f * s, -0.313f * s))
        drawCircle(Color(0xFF2B2D42), 0.019f * s, Offset(0.073f * s, -0.313f * s))
    }
    drawPath(p4, sil ?: Color(0xFFBA2C25))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFE8372E))
        }
        clipPath(p4) {
            drawCircle(Color(0xFF2B2D42), 0.073f * s, Offset(-0.171f * s, 0.0f * s))
            drawCircle(Color(0xFF2B2D42), 0.073f * s, Offset(0.171f * s, 0.0f * s))
            drawCircle(Color(0xFF2B2D42), 0.064f * s, Offset(-0.146f * s, 0.231f * s))
            drawCircle(Color(0xFF2B2D42), 0.064f * s, Offset(0.146f * s, 0.231f * s))
            drawCircle(Color(0xFF2B2D42), 0.051f * s, Offset(0.0f * s, 0.343f * s))
        }
        drawPath(p6, Color(0x8CFFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.0f * s, -0.257f * s), Offset(0.0f * s, 0.428f * s), 0.032f * s, StrokeCap.Round)
}

private fun DrawScope.artPompier(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.391f * s, -0.078f * s); lineTo(0.074f * s, -0.078f * s); cubicTo(0.098f * s, -0.078f * s, 0.117f * s, -0.058f * s, 0.117f * s, -0.034f * s); lineTo(0.117f * s, 0.172f * s); cubicTo(0.117f * s, 0.196f * s, 0.098f * s, 0.216f * s, 0.074f * s, 0.216f * s); lineTo(-0.391f * s, 0.216f * s); cubicTo(-0.415f * s, 0.216f * s, -0.434f * s, 0.196f * s, -0.434f * s, 0.172f * s); lineTo(-0.434f * s, -0.034f * s); cubicTo(-0.434f * s, -0.058f * s, -0.415f * s, -0.078f * s, -0.391f * s, -0.078f * s); close() }
    val p1 = Path().apply { moveTo(-0.409f * s, -0.112f * s); lineTo(0.057f * s, -0.112f * s); cubicTo(0.081f * s, -0.112f * s, 0.1f * s, -0.093f * s, 0.1f * s, -0.069f * s); lineTo(0.1f * s, 0.138f * s); cubicTo(0.1f * s, 0.162f * s, 0.081f * s, 0.181f * s, 0.057f * s, 0.181f * s); lineTo(-0.409f * s, 0.181f * s); cubicTo(-0.432f * s, 0.181f * s, -0.452f * s, 0.162f * s, -0.452f * s, 0.138f * s); lineTo(-0.452f * s, -0.069f * s); cubicTo(-0.452f * s, -0.093f * s, -0.432f * s, -0.112f * s, -0.409f * s, -0.112f * s); close() }
    val p2 = Path().apply { moveTo(-0.383f * s, -0.224f * s); lineTo(0.031f * s, -0.224f * s); cubicTo(0.045f * s, -0.224f * s, 0.057f * s, -0.213f * s, 0.057f * s, -0.198f * s); lineTo(0.057f * s, -0.103f * s); cubicTo(0.057f * s, -0.089f * s, 0.045f * s, -0.078f * s, 0.031f * s, -0.078f * s); lineTo(-0.383f * s, -0.078f * s); cubicTo(-0.397f * s, -0.078f * s, -0.409f * s, -0.089f * s, -0.409f * s, -0.103f * s); lineTo(-0.409f * s, -0.198f * s); cubicTo(-0.409f * s, -0.213f * s, -0.397f * s, -0.224f * s, -0.383f * s, -0.224f * s); close() }
    val p3 = Path().apply { moveTo(0.066f * s, -0.233f * s); quadraticTo(0.066f * s, -0.267f * s, 0.109f * s, -0.267f * s); lineTo(0.255f * s, -0.267f * s); quadraticTo(0.298f * s, -0.267f * s, 0.324f * s, -0.224f * s); lineTo(0.41f * s, -0.043f * s); quadraticTo(0.428f * s, -0.017f * s, 0.428f * s, 0.017f * s); lineTo(0.428f * s, 0.181f * s); quadraticTo(0.428f * s, 0.216f * s, 0.393f * s, 0.216f * s); lineTo(0.066f * s, 0.216f * s); close() }
    val p4 = Path().apply { moveTo(0.048f * s, -0.267f * s); quadraticTo(0.048f * s, -0.302f * s, 0.091f * s, -0.302f * s); lineTo(0.238f * s, -0.302f * s); quadraticTo(0.281f * s, -0.302f * s, 0.307f * s, -0.259f * s); lineTo(0.393f * s, -0.078f * s); quadraticTo(0.41f * s, -0.052f * s, 0.41f * s, -0.017f * s); lineTo(0.41f * s, 0.147f * s); quadraticTo(0.41f * s, 0.181f * s, 0.376f * s, 0.181f * s); lineTo(0.048f * s, 0.181f * s); close() }
    val p5 = Path().apply { moveTo(0.126f * s, -0.207f * s); lineTo(0.255f * s, -0.207f * s); quadraticTo(0.281f * s, -0.207f * s, 0.298f * s, -0.181f * s); lineTo(0.35f * s, -0.06f * s); lineTo(0.126f * s, -0.06f * s); close() }
    val p6 = Path().apply { moveTo(0.16f * s, -0.345f * s); lineTo(0.238f * s, -0.345f * s); cubicTo(0.257f * s, -0.345f * s, 0.272f * s, -0.329f * s, 0.272f * s, -0.31f * s); lineTo(0.272f * s, -0.293f * s); cubicTo(0.272f * s, -0.274f * s, 0.257f * s, -0.259f * s, 0.238f * s, -0.259f * s); lineTo(0.16f * s, -0.259f * s); cubicTo(0.141f * s, -0.259f * s, 0.126f * s, -0.274f * s, 0.126f * s, -0.293f * s); lineTo(0.126f * s, -0.31f * s); cubicTo(0.126f * s, -0.329f * s, 0.141f * s, -0.345f * s, 0.16f * s, -0.345f * s); close() }
    val p7 = Path().apply { moveTo(0.441f * s, 0.095f * s); cubicTo(0.441f * s, 0.119f * s, 0.429f * s, 0.138f * s, 0.415f * s, 0.138f * s); cubicTo(0.4f * s, 0.138f * s, 0.389f * s, 0.119f * s, 0.389f * s, 0.095f * s); cubicTo(0.389f * s, 0.071f * s, 0.4f * s, 0.052f * s, 0.415f * s, 0.052f * s); cubicTo(0.429f * s, 0.052f * s, 0.441f * s, 0.071f * s, 0.441f * s, 0.095f * s); close() }
    drawPath(p0, sil ?: Color(0xFFB52B24))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFE8372E))
        }
        clipPath(p0) {
            drawRoundRect(Color(0xFFFFD23F), Offset(-0.452f * s, 0.052f * s), Size(0.603f * s, 0.052f * s), CornerRadius(0.0f * s))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFF3F5FA))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.297f * s, -0.224f * s), Offset(-0.297f * s, -0.078f * s), 0.026f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.176f * s, -0.224f * s), Offset(-0.176f * s, -0.078f * s), 0.026f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.055f * s, -0.224f * s), Offset(-0.055f * s, -0.078f * s), 0.026f * s, StrokeCap.Round)
    drawPath(p3, sil ?: Color(0xFFB52B24))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFE8372E))
        }
        clipPath(p3) {
            drawRoundRect(Color(0xFFFFD23F), Offset(-0.003f * s, 0.052f * s), Size(0.517f * s, 0.052f * s), CornerRadius(0.0f * s))
        }
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFBFE3FF))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF2F8DEB))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFFFE066))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF41465F), 0.108f * s, Offset(-0.262f * s, 0.233f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.108f * s, Offset(-0.262f * s, 0.233f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFD9DEE9), 0.045f * s, Offset(-0.262f * s, 0.233f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.045f * s, Offset(-0.262f * s, 0.233f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF41465F), 0.108f * s, Offset(0.238f * s, 0.233f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.108f * s, Offset(0.238f * s, 0.233f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFD9DEE9), 0.045f * s, Offset(0.238f * s, 0.233f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.045f * s, Offset(0.238f * s, 0.233f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artTulipe(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, 0.389f * s); quadraticTo(-0.116f * s, 0.183f * s, -0.309f * s, 0.046f * s); quadraticTo(-0.295f * s, 0.344f * s, 0.0f * s, 0.389f * s); close() }
    val p1 = Path().apply { moveTo(0.0f * s, 0.389f * s); quadraticTo(0.295f * s, 0.344f * s, 0.309f * s, 0.046f * s); quadraticTo(0.116f * s, 0.183f * s, 0.0f * s, 0.389f * s); close() }
    val p2 = Path().apply { moveTo(0.0f * s, 0.046f * s); lineTo(0.0f * s, 0.389f * s) }
    val p3 = Path().apply { moveTo(-0.257f * s, -0.4f * s); lineTo(-0.12f * s, -0.229f * s); lineTo(0.0f * s, -0.435f * s); lineTo(0.12f * s, -0.229f * s); lineTo(0.257f * s, -0.4f * s); cubicTo(0.36f * s, -0.143f * s, 0.257f * s, 0.063f * s, 0.0f * s, 0.097f * s); cubicTo(-0.257f * s, 0.063f * s, -0.36f * s, -0.143f * s, -0.257f * s, -0.4f * s); close() }
    val p4 = Path().apply { moveTo(-0.274f * s, -0.435f * s); lineTo(-0.137f * s, -0.263f * s); lineTo(-0.017f * s, -0.469f * s); lineTo(0.103f * s, -0.263f * s); lineTo(0.24f * s, -0.435f * s); cubicTo(0.343f * s, -0.177f * s, 0.24f * s, 0.029f * s, -0.017f * s, 0.063f * s); cubicTo(-0.274f * s, 0.029f * s, -0.377f * s, -0.177f * s, -0.274f * s, -0.435f * s); close() }
    val p5 = Path().apply { moveTo(0.0f * s, -0.435f * s); cubicTo(0.197f * s, -0.28f * s, 0.172f * s, -0.006f * s, 0.0f * s, 0.097f * s); cubicTo(-0.172f * s, -0.006f * s, -0.197f * s, -0.28f * s, 0.0f * s, -0.435f * s); close() }
    val p6 = Path().apply { moveTo(-0.017f * s, -0.469f * s); cubicTo(0.18f * s, -0.314f * s, 0.154f * s, -0.04f * s, -0.017f * s, 0.063f * s); cubicTo(-0.189f * s, -0.04f * s, -0.214f * s, -0.314f * s, -0.017f * s, -0.469f * s); close() }
    val p7 = Path().apply { moveTo(-0.047f * s, -0.166f * s); cubicTo(-0.054f * s, -0.119f * s, -0.069f * s, -0.082f * s, -0.081f * s, -0.084f * s); cubicTo(-0.092f * s, -0.085f * s, -0.096f * s, -0.125f * s, -0.09f * s, -0.172f * s); cubicTo(-0.083f * s, -0.218f * s, -0.068f * s, -0.255f * s, -0.057f * s, -0.253f * s); cubicTo(-0.045f * s, -0.252f * s, -0.041f * s, -0.212f * s, -0.047f * s, -0.166f * s); close() }
    drawPath(p0, sil ?: Color(0xFF3FAE49))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF3FAE49))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.142f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF3FAE49), style = Stroke(0.07f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFB82723))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFCC2B27))
        }
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFBA2C25))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFFE8372E))
        }
        drawPath(p7, Color(0x8CFFFFFF))
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCerfvolant(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.069f * s, 0.101f * s); cubicTo(-0.063f * s, 0.122f * s, -0.014f * s, 0.189f * s, -0.033f * s, 0.222f * s); cubicTo(-0.053f * s, 0.255f * s, -0.169f * s, 0.264f * s, -0.186f * s, 0.299f * s); cubicTo(-0.203f * s, 0.335f * s, -0.143f * s, 0.412f * s, -0.135f * s, 0.435f * s) }
    val p1 = Path().apply { moveTo(-0.047f * s, 0.227f * s); quadraticTo(-0.033f * s, 0.222f * s, -0.043f * s, 0.211f * s); lineTo(-0.087f * s, 0.155f * s); quadraticTo(-0.097f * s, 0.144f * s, -0.1f * s, 0.158f * s); lineTo(-0.124f * s, 0.241f * s); quadraticTo(-0.128f * s, 0.255f * s, -0.115f * s, 0.251f * s); close() }
    val p2 = Path().apply { moveTo(-0.024f * s, 0.234f * s); quadraticTo(-0.033f * s, 0.222f * s, -0.02f * s, 0.218f * s); lineTo(0.048f * s, 0.194f * s); quadraticTo(0.062f * s, 0.189f * s, 0.057f * s, 0.203f * s); lineTo(0.033f * s, 0.287f * s); quadraticTo(0.03f * s, 0.301f * s, 0.02f * s, 0.289f * s); close() }
    val p3 = Path().apply { moveTo(-0.2f * s, 0.304f * s); quadraticTo(-0.186f * s, 0.299f * s, -0.195f * s, 0.288f * s); lineTo(-0.24f * s, 0.232f * s); quadraticTo(-0.249f * s, 0.221f * s, -0.253f * s, 0.235f * s); lineTo(-0.277f * s, 0.318f * s); quadraticTo(-0.281f * s, 0.332f * s, -0.267f * s, 0.327f * s); close() }
    val p4 = Path().apply { moveTo(-0.177f * s, 0.31f * s); quadraticTo(-0.186f * s, 0.299f * s, -0.172f * s, 0.294f * s); lineTo(-0.105f * s, 0.271f * s); quadraticTo(-0.091f * s, 0.266f * s, -0.095f * s, 0.28f * s); lineTo(-0.119f * s, 0.364f * s); quadraticTo(-0.123f * s, 0.378f * s, -0.132f * s, 0.366f * s); close() }
    val p5 = Path().apply { moveTo(0.06f * s, -0.423f * s); quadraticTo(0.085f * s, -0.437f * s, 0.099f * s, -0.412f * s); lineTo(0.266f * s, -0.086f * s); quadraticTo(0.279f * s, -0.06f * s, 0.252f * s, -0.048f * s); lineTo(-0.043f * s, 0.089f * s); quadraticTo(-0.069f * s, 0.101f * s, -0.085f * s, 0.077f * s); lineTo(-0.263f * s, -0.196f * s); quadraticTo(-0.279f * s, -0.22f * s, -0.254f * s, -0.235f * s); close() }
    val p6 = Path().apply { moveTo(0.048f * s, -0.466f * s); quadraticTo(0.073f * s, -0.481f * s, 0.086f * s, -0.455f * s); lineTo(0.253f * s, -0.13f * s); quadraticTo(0.266f * s, -0.104f * s, 0.24f * s, -0.092f * s); lineTo(-0.055f * s, 0.045f * s); quadraticTo(-0.082f * s, 0.058f * s, -0.098f * s, 0.033f * s); lineTo(-0.275f * s, -0.24f * s); quadraticTo(-0.291f * s, -0.264f * s, -0.266f * s, -0.279f * s); close() }
    val p7 = Path().apply { moveTo(0.085f * s, -0.437f * s); quadraticTo(0.085f * s, -0.437f * s, 0.085f * s, -0.437f * s); lineTo(0.279f * s, -0.06f * s); quadraticTo(0.279f * s, -0.06f * s, 0.279f * s, -0.06f * s); lineTo(0.0f * s, -0.14f * s); quadraticTo(0.0f * s, -0.14f * s, 0.0f * s, -0.14f * s); close() }
    val p8 = Path().apply { moveTo(-0.069f * s, 0.101f * s); quadraticTo(-0.069f * s, 0.101f * s, -0.069f * s, 0.101f * s); lineTo(-0.279f * s, -0.22f * s); quadraticTo(-0.279f * s, -0.22f * s, -0.279f * s, -0.22f * s); lineTo(0.0f * s, -0.14f * s); quadraticTo(0.0f * s, -0.14f * s, 0.0f * s, -0.14f * s); close() }
    val p9 = Path().apply { moveTo(0.085f * s, -0.437f * s); lineTo(-0.069f * s, 0.101f * s); moveTo(-0.279f * s, -0.22f * s); lineTo(0.279f * s, -0.06f * s) }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.032f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFFFD23F))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFFFD23F))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2F8DEB))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2F8DEB))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFBA2C25))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFFE8372E))
        }
        clipPath(p5) {
            drawPath(p7, Color(0xFFFF6F5C))
            drawPath(p8, Color(0xFFFF6F5C))
        }
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artBonnet(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.358f * s, 0.193f * s); cubicTo(-0.395f * s, -0.165f * s, -0.207f * s, -0.296f * s, 0.0f * s, -0.296f * s); cubicTo(0.207f * s, -0.296f * s, 0.395f * s, -0.165f * s, 0.358f * s, 0.193f * s); close() }
    val p1 = Path().apply { moveTo(-0.381f * s, 0.146f * s); cubicTo(-0.419f * s, -0.212f * s, -0.231f * s, -0.344f * s, -0.023f * s, -0.344f * s); cubicTo(0.184f * s, -0.344f * s, 0.372f * s, -0.212f * s, 0.334f * s, 0.146f * s); close() }
    val p2 = Path().apply { moveTo(-0.164f * s, -0.07f * s); cubicTo(-0.186f * s, -0.022f * s, -0.223f * s, 0.007f * s, -0.247f * s, -0.004f * s); cubicTo(-0.27f * s, -0.015f * s, -0.272f * s, -0.062f * s, -0.25f * s, -0.109f * s); cubicTo(-0.228f * s, -0.156f * s, -0.191f * s, -0.186f * s, -0.167f * s, -0.175f * s); cubicTo(-0.144f * s, -0.164f * s, -0.142f * s, -0.117f * s, -0.164f * s, -0.07f * s); close() }
    val p3 = Path().apply { moveTo(-0.339f * s, 0.137f * s); lineTo(0.339f * s, 0.137f * s); cubicTo(0.391f * s, 0.137f * s, 0.433f * s, 0.179f * s, 0.433f * s, 0.231f * s); lineTo(0.433f * s, 0.325f * s); cubicTo(0.433f * s, 0.377f * s, 0.391f * s, 0.419f * s, 0.339f * s, 0.419f * s); lineTo(-0.339f * s, 0.419f * s); cubicTo(-0.391f * s, 0.419f * s, -0.433f * s, 0.377f * s, -0.433f * s, 0.325f * s); lineTo(-0.433f * s, 0.231f * s); cubicTo(-0.433f * s, 0.179f * s, -0.391f * s, 0.137f * s, -0.339f * s, 0.137f * s); close() }
    val p4 = Path().apply { moveTo(-0.358f * s, 0.099f * s); lineTo(0.32f * s, 0.099f * s); cubicTo(0.372f * s, 0.099f * s, 0.414f * s, 0.141f * s, 0.414f * s, 0.193f * s); lineTo(0.414f * s, 0.287f * s); cubicTo(0.414f * s, 0.339f * s, 0.372f * s, 0.381f * s, 0.32f * s, 0.381f * s); lineTo(-0.358f * s, 0.381f * s); cubicTo(-0.41f * s, 0.381f * s, -0.452f * s, 0.339f * s, -0.452f * s, 0.287f * s); lineTo(-0.452f * s, 0.193f * s); cubicTo(-0.452f * s, 0.141f * s, -0.41f * s, 0.099f * s, -0.358f * s, 0.099f * s); close() }
    val p5 = Path().apply { moveTo(0.113f * s, -0.315f * s); cubicTo(0.113f * s, -0.258f * s, 0.062f * s, -0.212f * s, 0.0f * s, -0.212f * s); cubicTo(-0.062f * s, -0.212f * s, -0.113f * s, -0.258f * s, -0.113f * s, -0.315f * s); cubicTo(-0.113f * s, -0.373f * s, -0.062f * s, -0.419f * s, 0.0f * s, -0.419f * s); cubicTo(0.062f * s, -0.419f * s, 0.113f * s, -0.373f * s, 0.113f * s, -0.315f * s); close() }
    val p6 = Path().apply { moveTo(0.094f * s, -0.344f * s); cubicTo(0.094f * s, -0.286f * s, 0.044f * s, -0.24f * s, -0.019f * s, -0.24f * s); cubicTo(-0.081f * s, -0.24f * s, -0.132f * s, -0.286f * s, -0.132f * s, -0.344f * s); cubicTo(-0.132f * s, -0.401f * s, -0.081f * s, -0.447f * s, -0.019f * s, -0.447f * s); cubicTo(0.044f * s, -0.447f * s, 0.094f * s, -0.401f * s, 0.094f * s, -0.344f * s); close() }
    drawPath(p0, sil ?: Color(0xFFBA2C25))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFE8372E))
        }
        clipPath(p0) {
            drawRoundRect(Color(0xFFF6F4EE), Offset(-0.471f * s, -0.071f * s), Size(0.941f * s, 0.094f * s), CornerRadius(0.0f * s))
        }
        drawPath(p2, Color(0x8CFFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFAE2422))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFD92D2A))
        }
    }
    drawLine(sil ?: Color(0x8C8E1B1B), Offset(-0.282f * s, 0.165f * s), Offset(-0.282f * s, 0.391f * s), 0.03f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0x8C8E1B1B), Offset(-0.141f * s, 0.165f * s), Offset(-0.141f * s, 0.391f * s), 0.03f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0x8C8E1B1B), Offset(0.0f * s, 0.165f * s), Offset(0.0f * s, 0.391f * s), 0.03f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0x8C8E1B1B), Offset(0.141f * s, 0.165f * s), Offset(0.141f * s, 0.391f * s), 0.03f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0x8C8E1B1B), Offset(0.282f * s, 0.165f * s), Offset(0.282f * s, 0.391f * s), 0.03f * s, StrokeCap.Round)
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFDDDCD6))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFFF6F4EE))
        }
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCroissant(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.151f * s, -0.013f * s); lineTo(-0.226f * s, -0.109f * s) }
    val p1 = Path().apply { moveTo(-0.242f * s, 0.072f * s); lineTo(-0.316f * s, 0.039f * s) }
    val p2 = Path().apply { moveTo(-0.285f * s, 0.18f * s); lineTo(-0.326f * s, 0.18f * s) }
    val p3 = Path().apply { moveTo(-0.283f * s, 0.294f * s); lineTo(-0.283f * s, 0.294f * s) }
    val p4 = Path().apply { moveTo(0.151f * s, -0.013f * s); lineTo(0.226f * s, -0.109f * s) }
    val p5 = Path().apply { moveTo(0.242f * s, 0.072f * s); lineTo(0.317f * s, 0.039f * s) }
    val p6 = Path().apply { moveTo(0.285f * s, 0.18f * s); lineTo(0.326f * s, 0.18f * s) }
    val p7 = Path().apply { moveTo(0.283f * s, 0.294f * s); lineTo(0.283f * s, 0.294f * s) }
    val p8 = Path().apply { moveTo(0.0f * s, -0.044f * s); lineTo(0.0f * s, -0.208f * s) }
    val p9 = Path().apply { moveTo(-0.023f * s, -0.245f * s); cubicTo(-0.018f * s, -0.229f * s, -0.034f * s, -0.209f * s, -0.061f * s, -0.199f * s); cubicTo(-0.087f * s, -0.19f * s, -0.113f * s, -0.195f * s, -0.119f * s, -0.21f * s); cubicTo(-0.125f * s, -0.226f * s, -0.108f * s, -0.247f * s, -0.082f * s, -0.257f * s); cubicTo(-0.055f * s, -0.266f * s, -0.029f * s, -0.261f * s, -0.023f * s, -0.245f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.302f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.262f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.222f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.182f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.302f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.262f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.222f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.182f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.352f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFFF4A23A), style = Stroke(0.23f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFF4A23A), style = Stroke(0.19f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFF4A23A), style = Stroke(0.15f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFF4A23A), style = Stroke(0.11f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFF4A23A), style = Stroke(0.23f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFF4A23A), style = Stroke(0.19f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFF4A23A), style = Stroke(0.15f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFF4A23A), style = Stroke(0.11f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFF4A23A), style = Stroke(0.28f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawLine(sil ?: Color(0xFFBE7E2D), Offset(-0.056f * s, 0.016f * s), Offset(-0.143f * s, -0.235f * s), 0.03f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFFBE7E2D), Offset(-0.185f * s, 0.035f * s), Offset(-0.321f * s, -0.071f * s), 0.03f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFFBE7E2D), Offset(-0.279f * s, 0.12f * s), Offset(-0.359f * s, 0.103f * s), 0.03f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFFBE7E2D), Offset(0.056f * s, 0.016f * s), Offset(0.143f * s, -0.235f * s), 0.03f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFFBE7E2D), Offset(0.185f * s, 0.035f * s), Offset(0.321f * s, -0.071f * s), 0.03f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFFBE7E2D), Offset(0.279f * s, 0.12f * s), Offset(0.359f * s, 0.103f * s), 0.03f * s, StrokeCap.Round)
    if (sil == null) {
        drawPath(p9, Color(0x8CFFFFFF))
    }
}

private fun DrawScope.artPizza(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.349f * s, -0.242f * s); quadraticTo(-0.358f * s, -0.276f * s, -0.323f * s, -0.267f * s); lineTo(0.298f * s, -0.112f * s); quadraticTo(0.332f * s, -0.104f * s, 0.308f * s, -0.078f * s); lineTo(-0.143f * s, 0.405f * s); quadraticTo(-0.168f * s, 0.431f * s, -0.177f * s, 0.397f * s); close() }
    val p1 = Path().apply { moveTo(-0.362f * s, -0.281f * s); quadraticTo(-0.371f * s, -0.316f * s, -0.336f * s, -0.307f * s); lineTo(0.285f * s, -0.152f * s); quadraticTo(0.319f * s, -0.144f * s, 0.295f * s, -0.118f * s); lineTo(-0.156f * s, 0.365f * s); quadraticTo(-0.181f * s, 0.392f * s, -0.19f * s, 0.357f * s); close() }
    val p2 = Path().apply { moveTo(-0.108f * s, -0.067f * s); cubicTo(-0.117f * s, -0.029f * s, -0.156f * s, -0.005f * s, -0.194f * s, -0.015f * s); cubicTo(-0.232f * s, -0.025f * s, -0.255f * s, -0.063f * s, -0.246f * s, -0.101f * s); cubicTo(-0.236f * s, -0.139f * s, -0.198f * s, -0.163f * s, -0.159f * s, -0.153f * s); cubicTo(-0.121f * s, -0.144f * s, -0.098f * s, -0.105f * s, -0.108f * s, -0.067f * s); close() }
    val p3 = Path().apply { moveTo(-0.113f * s, -0.091f * s); cubicTo(-0.122f * s, -0.053f * s, -0.161f * s, -0.03f * s, -0.199f * s, -0.039f * s); cubicTo(-0.237f * s, -0.049f * s, -0.26f * s, -0.087f * s, -0.251f * s, -0.125f * s); cubicTo(-0.241f * s, -0.164f * s, -0.203f * s, -0.187f * s, -0.164f * s, -0.177f * s); cubicTo(-0.126f * s, -0.168f * s, -0.103f * s, -0.129f * s, -0.113f * s, -0.091f * s); close() }
    val p4 = Path().apply { moveTo(0.143f * s, 0.051f * s); cubicTo(0.134f * s, 0.086f * s, 0.097f * s, 0.108f * s, 0.062f * s, 0.099f * s); cubicTo(0.026f * s, 0.09f * s, 0.004f * s, 0.054f * s, 0.013f * s, 0.018f * s); cubicTo(0.022f * s, -0.017f * s, 0.058f * s, -0.039f * s, 0.094f * s, -0.03f * s); cubicTo(0.13f * s, -0.021f * s, 0.152f * s, 0.015f * s, 0.143f * s, 0.051f * s); close() }
    val p5 = Path().apply { moveTo(0.138f * s, 0.026f * s); cubicTo(0.129f * s, 0.062f * s, 0.092f * s, 0.084f * s, 0.057f * s, 0.075f * s); cubicTo(0.021f * s, 0.066f * s, -0.001f * s, 0.03f * s, 0.008f * s, -0.006f * s); cubicTo(0.017f * s, -0.042f * s, 0.053f * s, -0.063f * s, 0.089f * s, -0.054f * s); cubicTo(0.125f * s, -0.046f * s, 0.147f * s, -0.009f * s, 0.138f * s, 0.026f * s); close() }
    val p6 = Path().apply { moveTo(-0.064f * s, 0.219f * s); cubicTo(-0.072f * s, 0.25f * s, -0.103f * s, 0.269f * s, -0.134f * s, 0.261f * s); cubicTo(-0.165f * s, 0.253f * s, -0.184f * s, 0.222f * s, -0.177f * s, 0.191f * s); cubicTo(-0.169f * s, 0.16f * s, -0.137f * s, 0.141f * s, -0.106f * s, 0.149f * s); cubicTo(-0.076f * s, 0.157f * s, -0.057f * s, 0.188f * s, -0.064f * s, 0.219f * s); close() }
    val p7 = Path().apply { moveTo(-0.069f * s, 0.195f * s); cubicTo(-0.077f * s, 0.226f * s, -0.108f * s, 0.245f * s, -0.139f * s, 0.237f * s); cubicTo(-0.17f * s, 0.229f * s, -0.189f * s, 0.198f * s, -0.182f * s, 0.167f * s); cubicTo(-0.174f * s, 0.136f * s, -0.142f * s, 0.117f * s, -0.111f * s, 0.125f * s); cubicTo(-0.08f * s, 0.132f * s, -0.062f * s, 0.164f * s, -0.069f * s, 0.195f * s); close() }
    val p8 = Path().apply { moveTo(0.367f * s, -0.242f * s); cubicTo(0.414f * s, -0.23f * s, 0.443f * s, -0.182f * s, 0.431f * s, -0.134f * s); cubicTo(0.42f * s, -0.086f * s, 0.371f * s, -0.057f * s, 0.324f * s, -0.069f * s); lineTo(-0.367f * s, -0.241f * s); cubicTo(-0.414f * s, -0.253f * s, -0.443f * s, -0.302f * s, -0.431f * s, -0.349f * s); cubicTo(-0.419f * s, -0.397f * s, -0.371f * s, -0.426f * s, -0.324f * s, -0.414f * s); close() }
    val p9 = Path().apply { moveTo(0.358f * s, -0.281f * s); cubicTo(0.406f * s, -0.269f * s, 0.435f * s, -0.22f * s, 0.423f * s, -0.173f * s); cubicTo(0.411f * s, -0.125f * s, 0.363f * s, -0.096f * s, 0.315f * s, -0.108f * s); lineTo(-0.375f * s, -0.28f * s); cubicTo(-0.423f * s, -0.292f * s, -0.452f * s, -0.34f * s, -0.44f * s, -0.388f * s); cubicTo(-0.428f * s, -0.436f * s, -0.38f * s, -0.465f * s, -0.332f * s, -0.453f * s); close() }
    val p10 = Path().apply { moveTo(-0.121f * s, -0.308f * s); cubicTo(-0.123f * s, -0.299f * s, -0.164f * s, -0.301f * s, -0.211f * s, -0.313f * s); cubicTo(-0.259f * s, -0.324f * s, -0.296f * s, -0.342f * s, -0.293f * s, -0.351f * s); cubicTo(-0.291f * s, -0.361f * s, -0.25f * s, -0.359f * s, -0.203f * s, -0.347f * s); cubicTo(-0.155f * s, -0.335f * s, -0.118f * s, -0.318f * s, -0.121f * s, -0.308f * s); close() }
    drawPath(p0, sil ?: Color(0xFFCC8B2F))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFFAE3B))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFAD2A23))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFD8342C))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFAD2A23))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFD8342C))
        }
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFAD2A23))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFFD8342C))
        }
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFB86D22))
    if (sil == null) {
        clipPath(p8) {
            drawPath(p9, Color(0xFFE0852A))
        }
        drawPath(p10, Color(0x80FFFFFF))
    }
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artHamburger(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.39f * s, 0.195f * s); lineTo(0.39f * s, 0.195f * s); quadraticTo(0.39f * s, 0.408f * s, 0.248f * s, 0.408f * s); lineTo(-0.248f * s, 0.408f * s); quadraticTo(-0.39f * s, 0.408f * s, -0.39f * s, 0.195f * s); close() }
    val p1 = Path().apply { moveTo(-0.407f * s, 0.159f * s); lineTo(0.372f * s, 0.159f * s); quadraticTo(0.372f * s, 0.372f * s, 0.23f * s, 0.372f * s); lineTo(-0.266f * s, 0.372f * s); quadraticTo(-0.407f * s, 0.372f * s, -0.407f * s, 0.159f * s); close() }
    val p2 = Path().apply { moveTo(-0.337f * s, 0.071f * s); lineTo(0.337f * s, 0.071f * s); cubicTo(0.376f * s, 0.071f * s, 0.408f * s, 0.103f * s, 0.408f * s, 0.142f * s); lineTo(0.408f * s, 0.151f * s); cubicTo(0.408f * s, 0.19f * s, 0.376f * s, 0.222f * s, 0.337f * s, 0.222f * s); lineTo(-0.337f * s, 0.222f * s); cubicTo(-0.376f * s, 0.222f * s, -0.407f * s, 0.19f * s, -0.407f * s, 0.151f * s); lineTo(-0.407f * s, 0.142f * s); cubicTo(-0.407f * s, 0.103f * s, -0.376f * s, 0.071f * s, -0.337f * s, 0.071f * s); close() }
    val p3 = Path().apply { moveTo(-0.354f * s, 0.035f * s); lineTo(0.354f * s, 0.035f * s); lineTo(0.354f * s, 0.089f * s); lineTo(0.106f * s, 0.089f * s); lineTo(0.044f * s, 0.177f * s); lineTo(-0.018f * s, 0.089f * s); lineTo(-0.354f * s, 0.089f * s); close() }
    val p4 = Path().apply { moveTo(-0.434f * s, -0.018f * s); quadraticTo(-0.39f * s, 0.089f * s, -0.319f * s, 0.053f * s); quadraticTo(-0.248f * s, 0.124f * s, -0.159f * s, 0.062f * s); quadraticTo(-0.08f * s, 0.124f * s, 0.0f * s, 0.062f * s); quadraticTo(0.08f * s, 0.124f * s, 0.159f * s, 0.062f * s); quadraticTo(0.248f * s, 0.124f * s, 0.319f * s, 0.053f * s); quadraticTo(0.39f * s, 0.089f * s, 0.434f * s, -0.018f * s); close() }
    val p5 = Path().apply { moveTo(-0.407f * s, -0.018f * s); cubicTo(-0.407f * s, -0.319f * s, -0.213f * s, -0.407f * s, 0.0f * s, -0.407f * s); cubicTo(0.213f * s, -0.407f * s, 0.408f * s, -0.319f * s, 0.408f * s, -0.018f * s); quadraticTo(0.408f * s, 0.027f * s, 0.363f * s, 0.027f * s); lineTo(-0.363f * s, 0.027f * s); quadraticTo(-0.407f * s, 0.027f * s, -0.407f * s, -0.018f * s); close() }
    val p6 = Path().apply { moveTo(-0.43f * s, -0.062f * s); cubicTo(-0.43f * s, -0.363f * s, -0.235f * s, -0.452f * s, -0.022f * s, -0.452f * s); cubicTo(0.191f * s, -0.452f * s, 0.385f * s, -0.363f * s, 0.385f * s, -0.062f * s); quadraticTo(0.385f * s, -0.018f * s, 0.341f * s, -0.018f * s); lineTo(-0.385f * s, -0.018f * s); quadraticTo(-0.43f * s, -0.018f * s, -0.43f * s, -0.062f * s); close() }
    val p7 = Path().apply { moveTo(-0.027f * s, -0.282f * s); cubicTo(-0.025f * s, -0.27f * s, -0.043f * s, -0.256f * s, -0.067f * s, -0.252f * s); cubicTo(-0.091f * s, -0.248f * s, -0.112f * s, -0.254f * s, -0.114f * s, -0.267f * s); cubicTo(-0.117f * s, -0.279f * s, -0.099f * s, -0.293f * s, -0.075f * s, -0.297f * s); cubicTo(-0.051f * s, -0.301f * s, -0.029f * s, -0.295f * s, -0.027f * s, -0.282f * s); close() }
    val p8 = Path().apply { moveTo(0.158f * s, -0.183f * s); cubicTo(0.155f * s, -0.171f * s, 0.133f * s, -0.166f * s, 0.109f * s, -0.173f * s); cubicTo(0.086f * s, -0.179f * s, 0.069f * s, -0.194f * s, 0.072f * s, -0.206f * s); cubicTo(0.076f * s, -0.219f * s, 0.098f * s, -0.223f * s, 0.121f * s, -0.217f * s); cubicTo(0.145f * s, -0.211f * s, 0.161f * s, -0.196f * s, 0.158f * s, -0.183f * s); close() }
    val p9 = Path().apply { moveTo(-0.135f * s, -0.091f * s); cubicTo(-0.14f * s, -0.079f * s, -0.162f * s, -0.076f * s, -0.185f * s, -0.085f * s); cubicTo(-0.208f * s, -0.093f * s, -0.223f * s, -0.109f * s, -0.219f * s, -0.121f * s); cubicTo(-0.214f * s, -0.133f * s, -0.192f * s, -0.136f * s, -0.169f * s, -0.128f * s); cubicTo(-0.146f * s, -0.12f * s, -0.131f * s, -0.103f * s, -0.135f * s, -0.091f * s); close() }
    val p10 = Path().apply { moveTo(0.044f * s, -0.119f * s); cubicTo(0.045f * s, -0.106f * s, 0.026f * s, -0.094f * s, 0.002f * s, -0.092f * s); cubicTo(-0.022f * s, -0.09f * s, -0.043f * s, -0.099f * s, -0.044f * s, -0.111f * s); cubicTo(-0.045f * s, -0.124f * s, -0.026f * s, -0.136f * s, -0.002f * s, -0.138f * s); cubicTo(0.022f * s, -0.14f * s, 0.043f * s, -0.132f * s, 0.044f * s, -0.119f * s); close() }
    val p11 = Path().apply { moveTo(0.288f * s, -0.061f * s); cubicTo(0.283f * s, -0.049f * s, 0.26f * s, -0.048f * s, 0.238f * s, -0.059f * s); cubicTo(0.216f * s, -0.069f * s, 0.203f * s, -0.087f * s, 0.208f * s, -0.098f * s); cubicTo(0.213f * s, -0.11f * s, 0.236f * s, -0.111f * s, 0.258f * s, -0.101f * s); cubicTo(0.28f * s, -0.09f * s, 0.294f * s, -0.072f * s, 0.288f * s, -0.061f * s); close() }
    val p12 = Path().apply { moveTo(-0.204f * s, -0.2f * s); cubicTo(-0.232f * s, -0.16f * s, -0.275f * s, -0.141f * s, -0.299f * s, -0.158f * s); cubicTo(-0.323f * s, -0.175f * s, -0.32f * s, -0.221f * s, -0.291f * s, -0.261f * s); cubicTo(-0.263f * s, -0.301f * s, -0.221f * s, -0.32f * s, -0.197f * s, -0.303f * s); cubicTo(-0.173f * s, -0.286f * s, -0.176f * s, -0.24f * s, -0.204f * s, -0.2f * s); close() }
    drawPath(p0, sil ?: Color(0xFFC28030))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFF2A03C))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF7A4A2A))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFFFD23F))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF55C85A))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFC28030))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFFF2A03C))
        }
        drawPath(p7, Color(0xFFFFF1CC))
        drawPath(p8, Color(0xFFFFF1CC))
        drawPath(p9, Color(0xFFFFF1CC))
        drawPath(p10, Color(0xFFFFF1CC))
        drawPath(p11, Color(0xFFFFF1CC))
        drawPath(p12, Color(0x8CFFFFFF))
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCrabe(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.222f * s, 0.133f * s); lineTo(-0.356f * s, 0.228f * s) }
    val p1 = Path().apply { moveTo(-0.158f * s, 0.196f * s); lineTo(-0.269f * s, 0.323f * s) }
    val p2 = Path().apply { moveTo(-0.261f * s, 0.054f * s); lineTo(-0.396f * s, 0.117f * s) }
    val p3 = Path().apply { moveTo(0.222f * s, 0.133f * s); lineTo(0.357f * s, 0.228f * s) }
    val p4 = Path().apply { moveTo(0.158f * s, 0.196f * s); lineTo(0.269f * s, 0.323f * s) }
    val p5 = Path().apply { moveTo(0.261f * s, 0.054f * s); lineTo(0.396f * s, 0.117f * s) }
    val p6 = Path().apply { moveTo(-0.103f * s, -0.105f * s); lineTo(-0.103f * s, -0.248f * s) }
    val p7 = Path().apply { moveTo(0.103f * s, -0.105f * s); lineTo(0.103f * s, -0.248f * s) }
    val p8 = Path().apply { moveTo(-0.176f * s, -0.05f * s); lineTo(-0.264f * s, -0.109f * s) }
    val p9 = Path().apply { moveTo(-0.293f * s, -0.192f * s); lineTo(-0.254f * s, -0.329f * s); cubicTo(-0.192f * s, -0.311f * s, -0.149f * s, -0.254f * s, -0.15f * s, -0.19f * s); cubicTo(-0.152f * s, -0.125f * s, -0.196f * s, -0.069f * s, -0.259f * s, -0.054f * s); cubicTo(-0.321f * s, -0.038f * s, -0.386f * s, -0.066f * s, -0.418f * s, -0.123f * s); cubicTo(-0.449f * s, -0.179f * s, -0.439f * s, -0.25f * s, -0.392f * s, -0.295f * s); close() }
    val p10 = Path().apply { moveTo(-0.31f * s, -0.212f * s); lineTo(-0.271f * s, -0.349f * s); cubicTo(-0.209f * s, -0.332f * s, -0.167f * s, -0.274f * s, -0.168f * s, -0.21f * s); cubicTo(-0.169f * s, -0.145f * s, -0.213f * s, -0.089f * s, -0.276f * s, -0.074f * s); cubicTo(-0.338f * s, -0.058f * s, -0.404f * s, -0.087f * s, -0.435f * s, -0.143f * s); cubicTo(-0.466f * s, -0.2f * s, -0.456f * s, -0.27f * s, -0.409f * s, -0.315f * s); close() }
    val p11 = Path().apply { moveTo(0.176f * s, -0.05f * s); lineTo(0.264f * s, -0.109f * s) }
    val p12 = Path().apply { moveTo(0.293f * s, -0.192f * s); lineTo(0.392f * s, -0.295f * s); cubicTo(0.439f * s, -0.25f * s, 0.449f * s, -0.179f * s, 0.418f * s, -0.123f * s); cubicTo(0.387f * s, -0.066f * s, 0.321f * s, -0.038f * s, 0.259f * s, -0.054f * s); cubicTo(0.196f * s, -0.069f * s, 0.152f * s, -0.125f * s, 0.151f * s, -0.19f * s); cubicTo(0.149f * s, -0.254f * s, 0.192f * s, -0.311f * s, 0.254f * s, -0.329f * s); close() }
    val p13 = Path().apply { moveTo(0.287f * s, -0.218f * s); lineTo(0.386f * s, -0.321f * s); cubicTo(0.433f * s, -0.276f * s, 0.443f * s, -0.205f * s, 0.412f * s, -0.149f * s); cubicTo(0.381f * s, -0.092f * s, 0.315f * s, -0.064f * s, 0.253f * s, -0.08f * s); cubicTo(0.19f * s, -0.095f * s, 0.146f * s, -0.151f * s, 0.145f * s, -0.216f * s); cubicTo(0.144f * s, -0.28f * s, 0.186f * s, -0.337f * s, 0.248f * s, -0.355f * s); close() }
    val p14 = Path().apply { moveTo(0.317f * s, 0.038f * s); cubicTo(0.317f * s, 0.165f * s, 0.175f * s, 0.267f * s, 0.0f * s, 0.267f * s); cubicTo(-0.175f * s, 0.267f * s, -0.317f * s, 0.165f * s, -0.317f * s, 0.038f * s); cubicTo(-0.317f * s, -0.089f * s, -0.175f * s, -0.192f * s, 0.0f * s, -0.192f * s); cubicTo(0.175f * s, -0.192f * s, 0.317f * s, -0.089f * s, 0.317f * s, 0.038f * s); close() }
    val p15 = Path().apply { moveTo(0.301f * s, -0.002f * s); cubicTo(0.301f * s, 0.125f * s, 0.159f * s, 0.228f * s, -0.016f * s, 0.228f * s); cubicTo(-0.191f * s, 0.228f * s, -0.333f * s, 0.125f * s, -0.333f * s, -0.002f * s); cubicTo(-0.333f * s, -0.129f * s, -0.191f * s, -0.232f * s, -0.016f * s, -0.232f * s); cubicTo(0.159f * s, -0.232f * s, 0.301f * s, -0.129f * s, 0.301f * s, -0.002f * s); close() }
    val p16 = Path().apply { moveTo(-0.146f * s, -0.084f * s); cubicTo(-0.141f * s, -0.072f * s, -0.161f * s, -0.053f * s, -0.19f * s, -0.043f * s); cubicTo(-0.219f * s, -0.033f * s, -0.246f * s, -0.034f * s, -0.25f * s, -0.046f * s); cubicTo(-0.255f * s, -0.059f * s, -0.235f * s, -0.077f * s, -0.206f * s, -0.088f * s); cubicTo(-0.177f * s, -0.098f * s, -0.15f * s, -0.097f * s, -0.146f * s, -0.084f * s); close() }
    val p17 = Path().apply { moveTo(-0.079f * s, 0.022f * s); quadraticTo(0.0f * s, 0.101f * s, 0.079f * s, 0.022f * s) }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFFFF8A1F), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFFF8A1F), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFFF8A1F), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFFF8A1F), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFFF8A1F), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFFF8A1F), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.117f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.117f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFFF8A1F), style = Stroke(0.045f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFFF8A1F), style = Stroke(0.045f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.132f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFFF8A1F), style = Stroke(0.06f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFCC6E19))
    if (sil == null) {
        clipPath(p9) {
            drawPath(p10, Color(0xFFFF8A1F))
        }
    }
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.132f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFFFF8A1F), style = Stroke(0.06f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFFCC6E19))
    if (sil == null) {
        clipPath(p12) {
            drawPath(p13, Color(0xFFFF8A1F))
        }
    }
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p14, sil ?: Color(0xFFCC6E19))
    if (sil == null) {
        clipPath(p14) {
            drawPath(p15, Color(0xFFFF8A1F))
        }
        drawPath(p16, Color(0x8CFFFFFF))
    }
    drawPath(p14, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.063f * s, Offset(-0.103f * s, -0.295f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.063f * s, Offset(-0.103f * s, -0.295f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.037f * s, Offset(-0.097f * s, -0.279f * s))
        drawCircle(Color(0xFFFFFFFF), 0.013f * s, Offset(-0.111f * s, -0.296f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.063f * s, Offset(0.103f * s, -0.295f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.063f * s, Offset(0.103f * s, -0.295f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.037f * s, Offset(0.097f * s, -0.279f * s))
        drawCircle(Color(0xFFFFFFFF), 0.013f * s, Offset(0.083f * s, -0.296f * s))
    }
    drawPath(p17, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(-0.238f * s, -0.005f * s), Size(0.079f * s, 0.054f * s))
        drawOval(Color(0x66FF4F86), Offset(0.158f * s, -0.005f * s), Size(0.079f * s, 0.054f * s))
    }
}

private fun DrawScope.artLion(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.046f * s, -0.408f * s); quadraticTo(0.0f * s, -0.459f * s, 0.046f * s, -0.408f * s); lineTo(0.049f * s, -0.405f * s); quadraticTo(0.095f * s, -0.355f * s, 0.161f * s, -0.375f * s); lineTo(0.164f * s, -0.377f * s); quadraticTo(0.23f * s, -0.397f * s, 0.244f * s, -0.33f * s); lineTo(0.245f * s, -0.327f * s); quadraticTo(0.26f * s, -0.26f * s, 0.327f * s, -0.245f * s); lineTo(0.33f * s, -0.244f * s); quadraticTo(0.397f * s, -0.229f * s, 0.377f * s, -0.164f * s); lineTo(0.376f * s, -0.161f * s); quadraticTo(0.355f * s, -0.095f * s, 0.406f * s, -0.049f * s); lineTo(0.408f * s, -0.046f * s); quadraticTo(0.459f * s, 0.0f * s, 0.408f * s, 0.046f * s); lineTo(0.406f * s, 0.049f * s); quadraticTo(0.355f * s, 0.095f * s, 0.376f * s, 0.161f * s); lineTo(0.377f * s, 0.164f * s); quadraticTo(0.397f * s, 0.23f * s, 0.33f * s, 0.244f * s); lineTo(0.327f * s, 0.245f * s); quadraticTo(0.26f * s, 0.26f * s, 0.245f * s, 0.327f * s); lineTo(0.244f * s, 0.33f * s); quadraticTo(0.23f * s, 0.397f * s, 0.164f * s, 0.377f * s); lineTo(0.161f * s, 0.376f * s); quadraticTo(0.095f * s, 0.355f * s, 0.049f * s, 0.406f * s); lineTo(0.046f * s, 0.408f * s); quadraticTo(0.0f * s, 0.459f * s, -0.046f * s, 0.408f * s); lineTo(-0.049f * s, 0.406f * s); quadraticTo(-0.095f * s, 0.355f * s, -0.161f * s, 0.376f * s); lineTo(-0.164f * s, 0.377f * s); quadraticTo(-0.229f * s, 0.397f * s, -0.244f * s, 0.33f * s); lineTo(-0.245f * s, 0.327f * s); quadraticTo(-0.26f * s, 0.26f * s, -0.327f * s, 0.245f * s); lineTo(-0.33f * s, 0.244f * s); quadraticTo(-0.397f * s, 0.23f * s, -0.377f * s, 0.164f * s); lineTo(-0.375f * s, 0.161f * s); quadraticTo(-0.355f * s, 0.095f * s, -0.405f * s, 0.049f * s); lineTo(-0.408f * s, 0.046f * s); quadraticTo(-0.459f * s, 0.0f * s, -0.408f * s, -0.046f * s); lineTo(-0.405f * s, -0.049f * s); quadraticTo(-0.355f * s, -0.095f * s, -0.375f * s, -0.161f * s); lineTo(-0.377f * s, -0.164f * s); quadraticTo(-0.397f * s, -0.229f * s, -0.33f * s, -0.244f * s); lineTo(-0.327f * s, -0.245f * s); quadraticTo(-0.26f * s, -0.26f * s, -0.245f * s, -0.327f * s); lineTo(-0.244f * s, -0.33f * s); quadraticTo(-0.229f * s, -0.397f * s, -0.164f * s, -0.377f * s); lineTo(-0.161f * s, -0.375f * s); quadraticTo(-0.095f * s, -0.355f * s, -0.049f * s, -0.405f * s); close() }
    val p1 = Path().apply { moveTo(-0.065f * s, -0.445f * s); quadraticTo(-0.018f * s, -0.496f * s, 0.028f * s, -0.445f * s); lineTo(0.03f * s, -0.442f * s); quadraticTo(0.077f * s, -0.391f * s, 0.142f * s, -0.412f * s); lineTo(0.146f * s, -0.413f * s); quadraticTo(0.211f * s, -0.434f * s, 0.226f * s, -0.367f * s); lineTo(0.227f * s, -0.363f * s); quadraticTo(0.241f * s, -0.296f * s, 0.309f * s, -0.282f * s); lineTo(0.312f * s, -0.281f * s); quadraticTo(0.379f * s, -0.266f * s, 0.358f * s, -0.2f * s); lineTo(0.357f * s, -0.197f * s); quadraticTo(0.336f * s, -0.132f * s, 0.387f * s, -0.085f * s); lineTo(0.39f * s, -0.083f * s); quadraticTo(0.441f * s, -0.037f * s, 0.39f * s, 0.01f * s); lineTo(0.387f * s, 0.012f * s); quadraticTo(0.336f * s, 0.058f * s, 0.357f * s, 0.124f * s); lineTo(0.358f * s, 0.127f * s); quadraticTo(0.379f * s, 0.193f * s, 0.312f * s, 0.207f * s); lineTo(0.309f * s, 0.208f * s); quadraticTo(0.241f * s, 0.223f * s, 0.227f * s, 0.29f * s); lineTo(0.226f * s, 0.293f * s); quadraticTo(0.211f * s, 0.361f * s, 0.146f * s, 0.34f * s); lineTo(0.142f * s, 0.339f * s); quadraticTo(0.077f * s, 0.318f * s, 0.03f * s, 0.369f * s); lineTo(0.028f * s, 0.371f * s); quadraticTo(-0.018f * s, 0.422f * s, -0.065f * s, 0.371f * s); lineTo(-0.067f * s, 0.369f * s); quadraticTo(-0.113f * s, 0.318f * s, -0.179f * s, 0.339f * s); lineTo(-0.182f * s, 0.34f * s); quadraticTo(-0.248f * s, 0.361f * s, -0.262f * s, 0.293f * s); lineTo(-0.263f * s, 0.29f * s); quadraticTo(-0.278f * s, 0.223f * s, -0.345f * s, 0.208f * s); lineTo(-0.348f * s, 0.207f * s); quadraticTo(-0.416f * s, 0.193f * s, -0.395f * s, 0.127f * s); lineTo(-0.394f * s, 0.124f * s); quadraticTo(-0.373f * s, 0.058f * s, -0.424f * s, 0.012f * s); lineTo(-0.426f * s, 0.01f * s); quadraticTo(-0.477f * s, -0.037f * s, -0.426f * s, -0.083f * s); lineTo(-0.424f * s, -0.085f * s); quadraticTo(-0.373f * s, -0.132f * s, -0.394f * s, -0.197f * s); lineTo(-0.395f * s, -0.2f * s); quadraticTo(-0.416f * s, -0.266f * s, -0.348f * s, -0.281f * s); lineTo(-0.345f * s, -0.282f * s); quadraticTo(-0.278f * s, -0.296f * s, -0.263f * s, -0.363f * s); lineTo(-0.262f * s, -0.367f * s); quadraticTo(-0.248f * s, -0.434f * s, -0.182f * s, -0.413f * s); lineTo(-0.179f * s, -0.412f * s); quadraticTo(-0.113f * s, -0.391f * s, -0.067f * s, -0.442f * s); close() }
    val p2 = Path().apply { moveTo(-0.156f * s, -0.294f * s); cubicTo(-0.156f * s, -0.243f * s, -0.197f * s, -0.202f * s, -0.248f * s, -0.202f * s); cubicTo(-0.298f * s, -0.202f * s, -0.34f * s, -0.243f * s, -0.34f * s, -0.294f * s); cubicTo(-0.34f * s, -0.344f * s, -0.298f * s, -0.385f * s, -0.248f * s, -0.385f * s); cubicTo(-0.197f * s, -0.385f * s, -0.156f * s, -0.344f * s, -0.156f * s, -0.294f * s); close() }
    val p3 = Path().apply { moveTo(-0.165f * s, -0.312f * s); cubicTo(-0.165f * s, -0.261f * s, -0.206f * s, -0.22f * s, -0.257f * s, -0.22f * s); cubicTo(-0.308f * s, -0.22f * s, -0.349f * s, -0.261f * s, -0.349f * s, -0.312f * s); cubicTo(-0.349f * s, -0.363f * s, -0.308f * s, -0.404f * s, -0.257f * s, -0.404f * s); cubicTo(-0.206f * s, -0.404f * s, -0.165f * s, -0.363f * s, -0.165f * s, -0.312f * s); close() }
    val p4 = Path().apply { moveTo(0.34f * s, -0.294f * s); cubicTo(0.34f * s, -0.243f * s, 0.299f * s, -0.202f * s, 0.248f * s, -0.202f * s); cubicTo(0.197f * s, -0.202f * s, 0.156f * s, -0.243f * s, 0.156f * s, -0.294f * s); cubicTo(0.156f * s, -0.344f * s, 0.197f * s, -0.385f * s, 0.248f * s, -0.385f * s); cubicTo(0.299f * s, -0.385f * s, 0.34f * s, -0.344f * s, 0.34f * s, -0.294f * s); close() }
    val p5 = Path().apply { moveTo(0.33f * s, -0.312f * s); cubicTo(0.33f * s, -0.261f * s, 0.289f * s, -0.22f * s, 0.239f * s, -0.22f * s); cubicTo(0.188f * s, -0.22f * s, 0.147f * s, -0.261f * s, 0.147f * s, -0.312f * s); cubicTo(0.147f * s, -0.363f * s, 0.188f * s, -0.404f * s, 0.239f * s, -0.404f * s); cubicTo(0.289f * s, -0.404f * s, 0.33f * s, -0.363f * s, 0.33f * s, -0.312f * s); close() }
    val p6 = Path().apply { moveTo(0.294f * s, 0.037f * s); cubicTo(0.294f * s, 0.189f * s, 0.162f * s, 0.312f * s, 0.0f * s, 0.312f * s); cubicTo(-0.162f * s, 0.312f * s, -0.294f * s, 0.189f * s, -0.294f * s, 0.037f * s); cubicTo(-0.294f * s, -0.115f * s, -0.162f * s, -0.239f * s, 0.0f * s, -0.239f * s); cubicTo(0.162f * s, -0.239f * s, 0.294f * s, -0.115f * s, 0.294f * s, 0.037f * s); close() }
    val p7 = Path().apply { moveTo(0.275f * s, 0.0f * s); cubicTo(0.275f * s, 0.152f * s, 0.144f * s, 0.275f * s, -0.018f * s, 0.275f * s); cubicTo(-0.18f * s, 0.275f * s, -0.312f * s, 0.152f * s, -0.312f * s, 0.0f * s); cubicTo(-0.312f * s, -0.152f * s, -0.18f * s, -0.275f * s, -0.018f * s, -0.275f * s); cubicTo(0.144f * s, -0.275f * s, 0.275f * s, -0.152f * s, 0.275f * s, 0.0f * s); close() }
    val p8 = Path().apply { moveTo(-0.108f * s, -0.156f * s); cubicTo(-0.098f * s, -0.138f * s, -0.111f * s, -0.112f * s, -0.138f * s, -0.097f * s); cubicTo(-0.164f * s, -0.081f * s, -0.194f * s, -0.083f * s, -0.204f * s, -0.101f * s); cubicTo(-0.214f * s, -0.118f * s, -0.201f * s, -0.145f * s, -0.174f * s, -0.16f * s); cubicTo(-0.148f * s, -0.175f * s, -0.118f * s, -0.174f * s, -0.108f * s, -0.156f * s); close() }
    val p9 = Path().apply { moveTo(0.147f * s, 0.129f * s); cubicTo(0.147f * s, 0.189f * s, 0.081f * s, 0.239f * s, 0.0f * s, 0.239f * s); cubicTo(-0.081f * s, 0.239f * s, -0.147f * s, 0.189f * s, -0.147f * s, 0.129f * s); cubicTo(-0.147f * s, 0.068f * s, -0.081f * s, 0.018f * s, 0.0f * s, 0.018f * s); cubicTo(0.081f * s, 0.018f * s, 0.147f * s, 0.068f * s, 0.147f * s, 0.129f * s); close() }
    val p10 = Path().apply { moveTo(-0.037f * s, 0.085f * s); quadraticTo(-0.055f * s, 0.064f * s, -0.027f * s, 0.064f * s); lineTo(0.028f * s, 0.064f * s); quadraticTo(0.055f * s, 0.064f * s, 0.037f * s, 0.085f * s); lineTo(0.018f * s, 0.108f * s); quadraticTo(0.0f * s, 0.129f * s, -0.018f * s, 0.108f * s); close() }
    val p11 = Path().apply { moveTo(0.0f * s, 0.119f * s); lineTo(0.0f * s, 0.165f * s); quadraticTo(-0.046f * s, 0.23f * s, -0.092f * s, 0.184f * s) }
    val p12 = Path().apply { moveTo(0.0f * s, 0.165f * s); quadraticTo(0.046f * s, 0.23f * s, 0.092f * s, 0.184f * s) }
    drawPath(p0, sil ?: Color(0xFFBB6318))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFF07F1F))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFCC9D5A))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFFFC470))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFCC9D5A))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFFFC470))
        }
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFDBA960))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFFFFC470))
        }
        drawPath(p8, Color(0x8CFFFFFF))
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.055f * s, Offset(-0.119f * s, -0.055f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.055f * s, Offset(-0.119f * s, -0.055f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.032f * s, Offset(-0.114f * s, -0.044f * s))
        drawCircle(Color(0xFFFFFFFF), 0.011f * s, Offset(-0.126f * s, -0.058f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.055f * s, Offset(0.119f * s, -0.055f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.055f * s, Offset(0.119f * s, -0.055f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.032f * s, Offset(0.114f * s, -0.044f * s))
        drawCircle(Color(0xFFFFFFFF), 0.011f * s, Offset(0.102f * s, -0.058f * s))
    }
    drawPath(p9, sil ?: Color(0xFFFFEBC8))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p10, Color(0xFF7A3B1C))
    }
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(-0.262f * s, 0.062f * s), Size(0.083f * s, 0.059f * s))
        drawOval(Color(0x66FF4F86), Offset(0.179f * s, 0.062f * s), Size(0.083f * s, 0.059f * s))
    }
}

private fun DrawScope.artFeuille(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.025f * s, 0.062f * s); lineTo(-0.109f * s, 0.397f * s) }
    val p1 = Path().apply { moveTo(0.089f * s, -0.428f * s); quadraticTo(0.1f * s, -0.442f * s, 0.104f * s, -0.425f * s); lineTo(0.129f * s, -0.3f * s); quadraticTo(0.132f * s, -0.282f * s, 0.149f * s, -0.286f * s); lineTo(0.218f * s, -0.298f * s); quadraticTo(0.235f * s, -0.301f * s, 0.227f * s, -0.286f * s); lineTo(0.171f * s, -0.174f * s); quadraticTo(0.163f * s, -0.159f * s, 0.181f * s, -0.161f * s); lineTo(0.394f * s, -0.184f * s); quadraticTo(0.411f * s, -0.186f * s, 0.4f * s, -0.173f * s); lineTo(0.317f * s, -0.066f * s); quadraticTo(0.306f * s, -0.052f * s, 0.318f * s, -0.039f * s); lineTo(0.407f * s, 0.053f * s); quadraticTo(0.419f * s, 0.066f * s, 0.402f * s, 0.069f * s); lineTo(0.231f * s, 0.1f * s); quadraticTo(0.214f * s, 0.103f * s, 0.22f * s, 0.12f * s); lineTo(0.262f * s, 0.226f * s); quadraticTo(0.269f * s, 0.242f * s, 0.253f * s, 0.235f * s); lineTo(0.058f * s, 0.156f * s); quadraticTo(0.042f * s, 0.15f * s, 0.03f * s, 0.162f * s); lineTo(-0.03f * s, 0.223f * s); quadraticTo(-0.042f * s, 0.236f * s, -0.059f * s, 0.232f * s); lineTo(-0.076f * s, 0.227f * s); quadraticTo(-0.092f * s, 0.223f * s, -0.097f * s, 0.207f * s); lineTo(-0.121f * s, 0.124f * s); quadraticTo(-0.126f * s, 0.108f * s, -0.143f * s, 0.106f * s); lineTo(-0.352f * s, 0.085f * s); quadraticTo(-0.369f * s, 0.083f * s, -0.356f * s, 0.072f * s); lineTo(-0.269f * s, -0.002f * s); quadraticTo(-0.256f * s, -0.014f * s, -0.27f * s, -0.025f * s); lineTo(-0.406f * s, -0.133f * s); quadraticTo(-0.42f * s, -0.144f * s, -0.403f * s, -0.149f * s); lineTo(-0.281f * s, -0.189f * s); quadraticTo(-0.265f * s, -0.194f * s, -0.268f * s, -0.211f * s); lineTo(-0.291f * s, -0.345f * s); quadraticTo(-0.294f * s, -0.362f * s, -0.28f * s, -0.352f * s); lineTo(-0.103f * s, -0.231f * s); quadraticTo(-0.088f * s, -0.221f * s, -0.088f * s, -0.239f * s); lineTo(-0.085f * s, -0.364f * s); quadraticTo(-0.084f * s, -0.381f * s, -0.071f * s, -0.37f * s); lineTo(-0.016f * s, -0.327f * s); quadraticTo(-0.002f * s, -0.316f * s, 0.008f * s, -0.329f * s); close() }
    val p2 = Path().apply { moveTo(0.077f * s, -0.467f * s); quadraticTo(0.088f * s, -0.481f * s, 0.091f * s, -0.464f * s); lineTo(0.116f * s, -0.338f * s); quadraticTo(0.119f * s, -0.321f * s, 0.136f * s, -0.325f * s); lineTo(0.205f * s, -0.337f * s); quadraticTo(0.222f * s, -0.34f * s, 0.214f * s, -0.325f * s); lineTo(0.158f * s, -0.213f * s); quadraticTo(0.151f * s, -0.198f * s, 0.168f * s, -0.199f * s); lineTo(0.381f * s, -0.223f * s); quadraticTo(0.398f * s, -0.225f * s, 0.388f * s, -0.211f * s); lineTo(0.304f * s, -0.104f * s); quadraticTo(0.294f * s, -0.091f * s, 0.306f * s, -0.078f * s); lineTo(0.395f * s, 0.014f * s); quadraticTo(0.407f * s, 0.027f * s, 0.39f * s, 0.03f * s); lineTo(0.218f * s, 0.062f * s); quadraticTo(0.201f * s, 0.065f * s, 0.208f * s, 0.081f * s); lineTo(0.25f * s, 0.187f * s); quadraticTo(0.256f * s, 0.203f * s, 0.24f * s, 0.197f * s); lineTo(0.045f * s, 0.117f * s); quadraticTo(0.029f * s, 0.111f * s, 0.017f * s, 0.123f * s); lineTo(-0.042f * s, 0.185f * s); quadraticTo(-0.055f * s, 0.197f * s, -0.071f * s, 0.193f * s); lineTo(-0.088f * s, 0.189f * s); quadraticTo(-0.105f * s, 0.184f * s, -0.11f * s, 0.168f * s); lineTo(-0.134f * s, 0.086f * s); quadraticTo(-0.139f * s, 0.069f * s, -0.156f * s, 0.067f * s); lineTo(-0.365f * s, 0.046f * s); quadraticTo(-0.382f * s, 0.044f * s, -0.369f * s, 0.033f * s); lineTo(-0.282f * s, -0.041f * s); quadraticTo(-0.269f * s, -0.053f * s, -0.282f * s, -0.063f * s); lineTo(-0.419f * s, -0.172f * s); quadraticTo(-0.432f * s, -0.182f * s, -0.416f * s, -0.188f * s); lineTo(-0.294f * s, -0.228f * s); quadraticTo(-0.277f * s, -0.233f * s, -0.28f * s, -0.25f * s); lineTo(-0.304f * s, -0.384f * s); quadraticTo(-0.307f * s, -0.401f * s, -0.292f * s, -0.391f * s); lineTo(-0.115f * s, -0.27f * s); quadraticTo(-0.101f * s, -0.26f * s, -0.101f * s, -0.278f * s); lineTo(-0.097f * s, -0.403f * s); quadraticTo(-0.097f * s, -0.42f * s, -0.083f * s, -0.409f * s); lineTo(-0.029f * s, -0.366f * s); quadraticTo(-0.015f * s, -0.355f * s, -0.004f * s, -0.368f * s); close() }
    val p3 = Path().apply { moveTo(-0.009f * s, -0.271f * s); cubicTo(-0.022f * s, -0.235f * s, -0.041f * s, -0.209f * s, -0.052f * s, -0.213f * s); cubicTo(-0.064f * s, -0.217f * s, -0.063f * s, -0.249f * s, -0.05f * s, -0.285f * s); cubicTo(-0.038f * s, -0.321f * s, -0.018f * s, -0.348f * s, -0.007f * s, -0.344f * s); cubicTo(0.004f * s, -0.34f * s, 0.003f * s, -0.307f * s, -0.009f * s, -0.271f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF7A4A2A), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFC26215))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFFF27A1A))
        }
    }
    clipPath(p1) {
        drawLine(sil ?: Color(0xFFA95512), Offset(-0.067f * s, 0.229f * s), Offset(0.071f * s, -0.324f * s), 0.028f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xFFA95512), Offset(-0.03f * s, 0.078f * s), Offset(0.285f * s, -0.111f * s), 0.026f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xFFA95512), Offset(-0.03f * s, 0.078f * s), Offset(-0.219f * s, -0.236f * s), 0.026f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xFFA95512), Offset(-0.042f * s, 0.129f * s), Offset(0.178f * s, 0.175f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xFFA95512), Offset(-0.042f * s, 0.129f * s), Offset(-0.258f * s, 0.066f * s), 0.024f * s, StrokeCap.Round)
    }
    if (sil == null) {
        drawPath(p3, Color(0x8CFFFFFF))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artMontgolfiere(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.089f * s, 0.275f * s); lineTo(0.089f * s, 0.275f * s); cubicTo(0.108f * s, 0.275f * s, 0.124f * s, 0.291f * s, 0.124f * s, 0.31f * s); lineTo(0.124f * s, 0.399f * s); cubicTo(0.124f * s, 0.418f * s, 0.108f * s, 0.434f * s, 0.089f * s, 0.434f * s); lineTo(-0.089f * s, 0.434f * s); cubicTo(-0.108f * s, 0.434f * s, -0.124f * s, 0.418f * s, -0.124f * s, 0.399f * s); lineTo(-0.124f * s, 0.31f * s); cubicTo(-0.124f * s, 0.291f * s, -0.108f * s, 0.275f * s, -0.089f * s, 0.275f * s); close() }
    val p1 = Path().apply { moveTo(-0.102f * s, 0.248f * s); lineTo(0.075f * s, 0.248f * s); cubicTo(0.095f * s, 0.248f * s, 0.111f * s, 0.264f * s, 0.111f * s, 0.284f * s); lineTo(0.111f * s, 0.372f * s); cubicTo(0.111f * s, 0.392f * s, 0.095f * s, 0.408f * s, 0.075f * s, 0.408f * s); lineTo(-0.102f * s, 0.408f * s); cubicTo(-0.121f * s, 0.408f * s, -0.137f * s, 0.392f * s, -0.137f * s, 0.372f * s); lineTo(-0.137f * s, 0.284f * s); cubicTo(-0.137f * s, 0.264f * s, -0.121f * s, 0.248f * s, -0.102f * s, 0.248f * s); close() }
    val p2 = Path().apply { moveTo(0.08f * s, 0.168f * s); cubicTo(0.124f * s, 0.097f * s, 0.39f * s, -0.027f * s, 0.39f * s, -0.186f * s); cubicTo(0.39f * s, -0.345f * s, 0.213f * s, -0.434f * s, 0.0f * s, -0.434f * s); cubicTo(-0.213f * s, -0.434f * s, -0.39f * s, -0.345f * s, -0.39f * s, -0.186f * s); cubicTo(-0.39f * s, -0.027f * s, -0.124f * s, 0.097f * s, -0.08f * s, 0.168f * s); close() }
    val p3 = Path().apply { moveTo(0.058f * s, 0.124f * s); cubicTo(0.102f * s, 0.053f * s, 0.368f * s, -0.071f * s, 0.368f * s, -0.23f * s); cubicTo(0.368f * s, -0.39f * s, 0.191f * s, -0.478f * s, -0.022f * s, -0.478f * s); cubicTo(-0.235f * s, -0.478f * s, -0.412f * s, -0.39f * s, -0.412f * s, -0.23f * s); cubicTo(-0.412f * s, -0.071f * s, -0.146f * s, 0.053f * s, -0.102f * s, 0.124f * s); close() }
    val p4 = Path().apply { moveTo(0.0f * s, -0.434f * s); cubicTo(0.337f * s, -0.363f * s, 0.301f * s, 0.009f * s, 0.08f * s, 0.168f * s); lineTo(-0.08f * s, 0.168f * s); cubicTo(-0.301f * s, 0.009f * s, -0.337f * s, -0.363f * s, 0.0f * s, -0.434f * s); close() }
    val p5 = Path().apply { moveTo(0.0f * s, -0.434f * s); cubicTo(0.222f * s, -0.363f * s, 0.177f * s, 0.009f * s, 0.044f * s, 0.168f * s); lineTo(-0.044f * s, 0.168f * s); cubicTo(-0.177f * s, 0.009f * s, -0.221f * s, -0.363f * s, 0.0f * s, -0.434f * s); close() }
    val p6 = Path().apply { moveTo(-0.23f * s, -0.213f * s); cubicTo(-0.248f * s, -0.173f * s, -0.279f * s, -0.149f * s, -0.299f * s, -0.158f * s); cubicTo(-0.319f * s, -0.167f * s, -0.32f * s, -0.207f * s, -0.302f * s, -0.247f * s); cubicTo(-0.283f * s, -0.287f * s, -0.252f * s, -0.312f * s, -0.232f * s, -0.303f * s); cubicTo(-0.212f * s, -0.293f * s, -0.211f * s, -0.253f * s, -0.23f * s, -0.213f * s); close() }
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.062f * s, 0.151f * s), Offset(-0.089f * s, 0.292f * s), 0.028f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.062f * s, 0.151f * s), Offset(0.089f * s, 0.292f * s), 0.028f * s, StrokeCap.Round)
    drawPath(p0, sil ?: Color(0xFF87542F))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFA9693B))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFBA4D16))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFE8601C))
        }
        clipPath(p2) {
            drawPath(p4, Color(0xFFFF8A1F))
            drawPath(p5, Color(0xFFFFE066))
        }
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p6, Color(0x8CFFFFFF))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCone(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.124f * s, -0.388f * s); quadraticTo(-0.112f * s, -0.433f * s, -0.065f * s, -0.433f * s); lineTo(0.065f * s, -0.433f * s); quadraticTo(0.112f * s, -0.433f * s, 0.124f * s, -0.388f * s); lineTo(0.309f * s, 0.285f * s); quadraticTo(0.317f * s, 0.312f * s, 0.289f * s, 0.312f * s); lineTo(-0.289f * s, 0.312f * s); quadraticTo(-0.317f * s, 0.312f * s, -0.309f * s, 0.285f * s); close() }
    val p1 = Path().apply { moveTo(-0.147f * s, -0.416f * s); quadraticTo(-0.135f * s, -0.461f * s, -0.088f * s, -0.461f * s); lineTo(0.042f * s, -0.461f * s); quadraticTo(0.089f * s, -0.461f * s, 0.101f * s, -0.416f * s); lineTo(0.286f * s, 0.257f * s); quadraticTo(0.294f * s, 0.284f * s, 0.266f * s, 0.284f * s); lineTo(-0.312f * s, 0.284f * s); quadraticTo(-0.34f * s, 0.284f * s, -0.333f * s, 0.257f * s); close() }
    val p2 = Path().apply { moveTo(-0.382f * s, 0.275f * s); lineTo(0.382f * s, 0.275f * s); cubicTo(0.408f * s, 0.275f * s, 0.429f * s, 0.296f * s, 0.429f * s, 0.321f * s); lineTo(0.429f * s, 0.387f * s); cubicTo(0.429f * s, 0.412f * s, 0.408f * s, 0.433f * s, 0.382f * s, 0.433f * s); lineTo(-0.382f * s, 0.433f * s); cubicTo(-0.408f * s, 0.433f * s, -0.429f * s, 0.412f * s, -0.429f * s, 0.387f * s); lineTo(-0.429f * s, 0.321f * s); cubicTo(-0.429f * s, 0.296f * s, -0.408f * s, 0.275f * s, -0.382f * s, 0.275f * s); close() }
    val p3 = Path().apply { moveTo(-0.401f * s, 0.247f * s); lineTo(0.363f * s, 0.247f * s); cubicTo(0.389f * s, 0.247f * s, 0.41f * s, 0.268f * s, 0.41f * s, 0.294f * s); lineTo(0.41f * s, 0.359f * s); cubicTo(0.41f * s, 0.384f * s, 0.389f * s, 0.405f * s, 0.363f * s, 0.405f * s); lineTo(-0.401f * s, 0.405f * s); cubicTo(-0.426f * s, 0.405f * s, -0.447f * s, 0.384f * s, -0.447f * s, 0.359f * s); lineTo(-0.447f * s, 0.294f * s); cubicTo(-0.447f * s, 0.268f * s, -0.426f * s, 0.247f * s, -0.401f * s, 0.247f * s); close() }
    drawPath(p0, sil ?: Color(0xFFCC6E19))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFF8A1F))
        }
        clipPath(p0) {
            drawRoundRect(Color(0xFFFFFFFF), Offset(-0.466f * s, -0.266f * s), Size(0.932f * s, 0.14f * s), CornerRadius(0.0f * s))
            drawRoundRect(Color(0xFFFFFFFF), Offset(-0.466f * s, -0.005f * s), Size(0.932f * s, 0.149f * s), CornerRadius(0.0f * s))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFB54E17))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFE8641E))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCitron(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.048f * s, -0.2f * s); quadraticTo(-0.061f * s, -0.282f * s, -0.013f * s, -0.33f * s) }
    val p1 = Path().apply { moveTo(-0.06f * s, -0.205f * s); quadraticTo(0.151f * s, -0.209f * s, 0.135f * s, -0.419f * s); quadraticTo(-0.075f * s, -0.415f * s, -0.06f * s, -0.205f * s); close() }
    val p2 = Path().apply { moveTo(-0.434f * s, 0.286f * s); cubicTo(-0.399f * s, 0.253f * s, -0.387f * s, 0.209f * s, -0.371f * s, 0.154f * s); cubicTo(-0.355f * s, -0.047f * s, -0.218f * s, -0.141f * s, -0.118f * s, -0.181f * s); cubicTo(-0.018f * s, -0.222f * s, 0.146f * s, -0.249f * s, 0.297f * s, -0.116f * s); cubicTo(0.347f * s, -0.088f * s, 0.385f * s, -0.064f * s, 0.434f * s, -0.064f * s); cubicTo(0.399f * s, -0.031f * s, 0.384f * s, 0.004f * s, 0.364f * s, 0.051f * s); cubicTo(0.348f * s, 0.252f * s, 0.211f * s, 0.346f * s, 0.111f * s, 0.386f * s); cubicTo(0.011f * s, 0.427f * s, -0.152f * s, 0.454f * s, -0.303f * s, 0.321f * s); cubicTo(-0.35f * s, 0.301f * s, -0.385f * s, 0.286f * s, -0.434f * s, 0.286f * s); close() }
    val p3 = Path().apply { moveTo(-0.467f * s, 0.251f * s); cubicTo(-0.432f * s, 0.218f * s, -0.421f * s, 0.174f * s, -0.404f * s, 0.119f * s); cubicTo(-0.388f * s, -0.082f * s, -0.252f * s, -0.176f * s, -0.151f * s, -0.216f * s); cubicTo(-0.051f * s, -0.257f * s, 0.112f * s, -0.284f * s, 0.263f * s, -0.151f * s); cubicTo(0.313f * s, -0.123f * s, 0.352f * s, -0.099f * s, 0.4f * s, -0.099f * s); cubicTo(0.365f * s, -0.066f * s, 0.35f * s, -0.031f * s, 0.331f * s, 0.016f * s); cubicTo(0.315f * s, 0.217f * s, 0.178f * s, 0.311f * s, 0.078f * s, 0.351f * s); cubicTo(-0.022f * s, 0.392f * s, -0.186f * s, 0.419f * s, -0.337f * s, 0.286f * s); cubicTo(-0.384f * s, 0.266f * s, -0.419f * s, 0.251f * s, -0.467f * s, 0.251f * s); close() }
    val p4 = Path().apply { moveTo(-0.147f * s, 0.001f * s); cubicTo(-0.134f * s, 0.016f * s, -0.153f * s, 0.055f * s, -0.19f * s, 0.088f * s); cubicTo(-0.227f * s, 0.122f * s, -0.267f * s, 0.136f * s, -0.281f * s, 0.122f * s); cubicTo(-0.294f * s, 0.107f * s, -0.275f * s, 0.068f * s, -0.238f * s, 0.035f * s); cubicTo(-0.201f * s, 0.001f * s, -0.16f * s, -0.014f * s, -0.147f * s, 0.001f * s); close() }
    val p5 = Path().apply { moveTo(-0.258f * s, 0.205f * s); cubicTo(-0.253f * s, 0.217f * s, -0.259f * s, 0.23f * s, -0.27f * s, 0.235f * s); cubicTo(-0.282f * s, 0.239f * s, -0.295f * s, 0.234f * s, -0.3f * s, 0.222f * s); cubicTo(-0.304f * s, 0.211f * s, -0.299f * s, 0.198f * s, -0.287f * s, 0.193f * s); cubicTo(-0.275f * s, 0.188f * s, -0.262f * s, 0.194f * s, -0.258f * s, 0.205f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.117f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF7A4A2A), style = Stroke(0.045f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF3FAE49))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFCCB32E))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFFFE03A))
        }
        drawPath(p4, Color(0x8CFFFFFF))
        drawPath(p5, Color(0x8CFFFFFF))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artMais(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.201f * s, -0.423f * s); cubicTo(0.329f * s, -0.371f * s, 0.328f * s, -0.172f * s, 0.259f * s, -0.001f * s); lineTo(0.155f * s, 0.256f * s); lineTo(-0.238f * s, 0.097f * s); lineTo(-0.134f * s, -0.16f * s); cubicTo(-0.065f * s, -0.331f * s, 0.072f * s, -0.475f * s, 0.201f * s, -0.423f * s); close() }
    val p1 = Path().apply { moveTo(0.197f * s, -0.464f * s); cubicTo(0.326f * s, -0.412f * s, 0.325f * s, -0.213f * s, 0.256f * s, -0.042f * s); lineTo(0.152f * s, 0.215f * s); lineTo(-0.241f * s, 0.056f * s); lineTo(-0.138f * s, -0.201f * s); cubicTo(-0.069f * s, -0.372f * s, 0.069f * s, -0.516f * s, 0.197f * s, -0.464f * s); close() }
    val p2 = Path().apply { moveTo(0.088f * s, -0.379f * s); cubicTo(0.08f * s, -0.359f * s, 0.053f * s, -0.352f * s, 0.027f * s, -0.362f * s); cubicTo(0.001f * s, -0.373f * s, -0.014f * s, -0.397f * s, -0.006f * s, -0.417f * s); cubicTo(0.002f * s, -0.436f * s, 0.029f * s, -0.443f * s, 0.055f * s, -0.433f * s); cubicTo(0.081f * s, -0.422f * s, 0.096f * s, -0.398f * s, 0.088f * s, -0.379f * s); close() }
    val p3 = Path().apply { moveTo(0.217f * s, -0.327f * s); cubicTo(0.209f * s, -0.307f * s, 0.181f * s, -0.3f * s, 0.155f * s, -0.311f * s); cubicTo(0.129f * s, -0.321f * s, 0.115f * s, -0.345f * s, 0.122f * s, -0.365f * s); cubicTo(0.13f * s, -0.384f * s, 0.158f * s, -0.391f * s, 0.184f * s, -0.381f * s); cubicTo(0.21f * s, -0.37f * s, 0.224f * s, -0.346f * s, 0.217f * s, -0.327f * s); close() }
    val p4 = Path().apply { moveTo(0.345f * s, -0.275f * s); cubicTo(0.337f * s, -0.255f * s, 0.31f * s, -0.248f * s, 0.284f * s, -0.259f * s); cubicTo(0.258f * s, -0.269f * s, 0.243f * s, -0.294f * s, 0.251f * s, -0.313f * s); cubicTo(0.259f * s, -0.332f * s, 0.286f * s, -0.339f * s, 0.312f * s, -0.329f * s); cubicTo(0.338f * s, -0.318f * s, 0.353f * s, -0.294f * s, 0.345f * s, -0.275f * s); close() }
    val p5 = Path().apply { moveTo(0.054f * s, -0.293f * s); cubicTo(0.046f * s, -0.274f * s, 0.018f * s, -0.266f * s, -0.008f * s, -0.277f * s); cubicTo(-0.034f * s, -0.287f * s, -0.048f * s, -0.312f * s, -0.04f * s, -0.331f * s); cubicTo(-0.033f * s, -0.35f * s, -0.005f * s, -0.358f * s, 0.021f * s, -0.347f * s); cubicTo(0.047f * s, -0.337f * s, 0.061f * s, -0.312f * s, 0.054f * s, -0.293f * s); close() }
    val p6 = Path().apply { moveTo(0.182f * s, -0.241f * s); cubicTo(0.174f * s, -0.222f * s, 0.147f * s, -0.215f * s, 0.121f * s, -0.225f * s); cubicTo(0.095f * s, -0.236f * s, 0.08f * s, -0.26f * s, 0.088f * s, -0.279f * s); cubicTo(0.096f * s, -0.298f * s, 0.123f * s, -0.306f * s, 0.149f * s, -0.295f * s); cubicTo(0.175f * s, -0.285f * s, 0.19f * s, -0.26f * s, 0.182f * s, -0.241f * s); close() }
    val p7 = Path().apply { moveTo(0.31f * s, -0.189f * s); cubicTo(0.303f * s, -0.17f * s, 0.275f * s, -0.163f * s, 0.249f * s, -0.173f * s); cubicTo(0.223f * s, -0.184f * s, 0.208f * s, -0.208f * s, 0.216f * s, -0.227f * s); cubicTo(0.224f * s, -0.247f * s, 0.252f * s, -0.254f * s, 0.278f * s, -0.243f * s); cubicTo(0.304f * s, -0.233f * s, 0.318f * s, -0.209f * s, 0.31f * s, -0.189f * s); close() }
    val p8 = Path().apply { moveTo(0.019f * s, -0.207f * s); cubicTo(0.011f * s, -0.188f * s, -0.016f * s, -0.181f * s, -0.042f * s, -0.191f * s); cubicTo(-0.068f * s, -0.202f * s, -0.083f * s, -0.226f * s, -0.075f * s, -0.245f * s); cubicTo(-0.067f * s, -0.265f * s, -0.04f * s, -0.272f * s, -0.014f * s, -0.261f * s); cubicTo(0.012f * s, -0.251f * s, 0.027f * s, -0.227f * s, 0.019f * s, -0.207f * s); close() }
    val p9 = Path().apply { moveTo(0.147f * s, -0.156f * s); cubicTo(0.14f * s, -0.136f * s, 0.112f * s, -0.129f * s, 0.086f * s, -0.139f * s); cubicTo(0.06f * s, -0.15f * s, 0.046f * s, -0.174f * s, 0.053f * s, -0.194f * s); cubicTo(0.061f * s, -0.213f * s, 0.089f * s, -0.22f * s, 0.115f * s, -0.21f * s); cubicTo(0.141f * s, -0.199f * s, 0.155f * s, -0.175f * s, 0.147f * s, -0.156f * s); close() }
    val p10 = Path().apply { moveTo(0.276f * s, -0.104f * s); cubicTo(0.268f * s, -0.084f * s, 0.241f * s, -0.077f * s, 0.215f * s, -0.088f * s); cubicTo(0.189f * s, -0.098f * s, 0.174f * s, -0.122f * s, 0.182f * s, -0.142f * s); cubicTo(0.189f * s, -0.161f * s, 0.217f * s, -0.168f * s, 0.243f * s, -0.158f * s); cubicTo(0.269f * s, -0.147f * s, 0.284f * s, -0.123f * s, 0.276f * s, -0.104f * s); close() }
    val p11 = Path().apply { moveTo(-0.015f * s, -0.122f * s); cubicTo(-0.023f * s, -0.102f * s, -0.051f * s, -0.095f * s, -0.077f * s, -0.106f * s); cubicTo(-0.103f * s, -0.116f * s, -0.117f * s, -0.14f * s, -0.11f * s, -0.16f * s); cubicTo(-0.102f * s, -0.179f * s, -0.074f * s, -0.186f * s, -0.048f * s, -0.176f * s); cubicTo(-0.022f * s, -0.165f * s, -0.008f * s, -0.141f * s, -0.015f * s, -0.122f * s); close() }
    val p12 = Path().apply { moveTo(0.113f * s, -0.07f * s); cubicTo(0.105f * s, -0.051f * s, 0.078f * s, -0.043f * s, 0.052f * s, -0.054f * s); cubicTo(0.026f * s, -0.064f * s, 0.011f * s, -0.089f * s, 0.019f * s, -0.108f * s); cubicTo(0.027f * s, -0.127f * s, 0.054f * s, -0.135f * s, 0.08f * s, -0.124f * s); cubicTo(0.106f * s, -0.114f * s, 0.121f * s, -0.089f * s, 0.113f * s, -0.07f * s); close() }
    val p13 = Path().apply { moveTo(0.241f * s, -0.018f * s); cubicTo(0.233f * s, 0.001f * s, 0.206f * s, 0.008f * s, 0.18f * s, -0.002f * s); cubicTo(0.154f * s, -0.013f * s, 0.139f * s, -0.037f * s, 0.147f * s, -0.056f * s); cubicTo(0.155f * s, -0.076f * s, 0.182f * s, -0.083f * s, 0.208f * s, -0.072f * s); cubicTo(0.234f * s, -0.062f * s, 0.249f * s, -0.037f * s, 0.241f * s, -0.018f * s); close() }
    val p14 = Path().apply { moveTo(-0.05f * s, -0.036f * s); cubicTo(-0.058f * s, -0.017f * s, -0.085f * s, -0.01f * s, -0.111f * s, -0.02f * s); cubicTo(-0.137f * s, -0.031f * s, -0.152f * s, -0.055f * s, -0.144f * s, -0.074f * s); cubicTo(-0.136f * s, -0.094f * s, -0.109f * s, -0.101f * s, -0.083f * s, -0.09f * s); cubicTo(-0.057f * s, -0.08f * s, -0.042f * s, -0.056f * s, -0.05f * s, -0.036f * s); close() }
    val p15 = Path().apply { moveTo(0.078f * s, 0.016f * s); cubicTo(0.071f * s, 0.035f * s, 0.043f * s, 0.042f * s, 0.017f * s, 0.032f * s); cubicTo(-0.009f * s, 0.021f * s, -0.024f * s, -0.003f * s, -0.016f * s, -0.022f * s); cubicTo(-0.008f * s, -0.042f * s, 0.019f * s, -0.049f * s, 0.045f * s, -0.039f * s); cubicTo(0.071f * s, -0.028f * s, 0.086f * s, -0.004f * s, 0.078f * s, 0.016f * s); close() }
    val p16 = Path().apply { moveTo(0.207f * s, 0.067f * s); cubicTo(0.199f * s, 0.087f * s, 0.171f * s, 0.094f * s, 0.145f * s, 0.083f * s); cubicTo(0.119f * s, 0.073f * s, 0.105f * s, 0.049f * s, 0.113f * s, 0.029f * s); cubicTo(0.12f * s, 0.01f * s, 0.148f * s, 0.003f * s, 0.174f * s, 0.013f * s); cubicTo(0.2f * s, 0.024f * s, 0.215f * s, 0.048f * s, 0.207f * s, 0.067f * s); close() }
    val p17 = Path().apply { moveTo(-0.085f * s, 0.049f * s); cubicTo(-0.092f * s, 0.069f * s, -0.12f * s, 0.076f * s, -0.146f * s, 0.065f * s); cubicTo(-0.172f * s, 0.055f * s, -0.187f * s, 0.031f * s, -0.179f * s, 0.011f * s); cubicTo(-0.171f * s, -0.008f * s, -0.143f * s, -0.015f * s, -0.117f * s, -0.005f * s); cubicTo(-0.091f * s, 0.006f * s, -0.077f * s, 0.03f * s, -0.085f * s, 0.049f * s); close() }
    val p18 = Path().apply { moveTo(0.044f * s, 0.101f * s); cubicTo(0.036f * s, 0.121f * s, 0.009f * s, 0.128f * s, -0.018f * s, 0.117f * s); cubicTo(-0.044f * s, 0.107f * s, -0.058f * s, 0.082f * s, -0.05f * s, 0.063f * s); cubicTo(-0.043f * s, 0.044f * s, -0.015f * s, 0.037f * s, 0.011f * s, 0.047f * s); cubicTo(0.037f * s, 0.058f * s, 0.052f * s, 0.082f * s, 0.044f * s, 0.101f * s); close() }
    val p19 = Path().apply { moveTo(0.172f * s, 0.153f * s); cubicTo(0.164f * s, 0.172f * s, 0.137f * s, 0.18f * s, 0.111f * s, 0.169f * s); cubicTo(0.085f * s, 0.159f * s, 0.07f * s, 0.134f * s, 0.078f * s, 0.115f * s); cubicTo(0.086f * s, 0.096f * s, 0.113f * s, 0.088f * s, 0.139f * s, 0.099f * s); cubicTo(0.165f * s, 0.109f * s, 0.18f * s, 0.134f * s, 0.172f * s, 0.153f * s); close() }
    val p20 = Path().apply { moveTo(-0.145f * s, 0.433f * s); quadraticTo(-0.129f * s, 0.151f * s, -0.222f * s, -0.116f * s); quadraticTo(-0.421f * s, 0.192f * s, -0.145f * s, 0.433f * s); close() }
    val p21 = Path().apply { moveTo(-0.145f * s, 0.433f * s); quadraticTo(0.221f * s, 0.451f * s, 0.291f * s, 0.092f * s); quadraticTo(0.039f * s, 0.219f * s, -0.145f * s, 0.433f * s); close() }
    val p22 = Path().apply { moveTo(-0.145f * s, 0.433f * s); quadraticTo(0.126f * s, 0.304f * s, 0.021f * s, 0.022f * s); quadraticTo(-0.25f * s, 0.152f * s, -0.145f * s, 0.433f * s); close() }
    drawPath(p0, sil ?: Color(0xFFCCA120))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFFC928))
        }
        clipPath(p0) {
            drawPath(p2, Color(0xFFFFF08A))
            drawPath(p3, Color(0xFFFFF08A))
            drawPath(p4, Color(0xFFFFF08A))
            drawPath(p5, Color(0xFFFFF08A))
            drawPath(p6, Color(0xFFFFF08A))
            drawPath(p7, Color(0xFFFFF08A))
            drawPath(p8, Color(0xFFFFF08A))
            drawPath(p9, Color(0xFFFFF08A))
            drawPath(p10, Color(0xFFFFF08A))
            drawPath(p11, Color(0xFFFFF08A))
            drawPath(p12, Color(0xFFFFF08A))
            drawPath(p13, Color(0xFFFFF08A))
            drawPath(p14, Color(0xFFFFF08A))
            drawPath(p15, Color(0xFFFFF08A))
            drawPath(p16, Color(0xFFFFF08A))
            drawPath(p17, Color(0xFFFFF08A))
            drawPath(p18, Color(0xFFFFF08A))
            drawPath(p19, Color(0xFFFFF08A))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p20, sil ?: Color(0xFF45B649))
    drawPath(p20, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p21, sil ?: Color(0xFF45B649))
    drawPath(p21, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p22, sil ?: Color(0xFF5FCB5F))
    drawPath(p22, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artAnanas(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.087f * s); quadraticTo(-0.066f * s, -0.281f * s, -0.269f * s, -0.261f * s); quadraticTo(-0.203f * s, -0.068f * s, 0.0f * s, -0.087f * s); close() }
    val p1 = Path().apply { moveTo(0.0f * s, -0.087f * s); quadraticTo(0.204f * s, -0.068f * s, 0.269f * s, -0.261f * s); quadraticTo(0.066f * s, -0.281f * s, 0.0f * s, -0.087f * s); close() }
    val p2 = Path().apply { moveTo(0.0f * s, -0.087f * s); quadraticTo(0.034f * s, -0.299f * s, -0.158f * s, -0.396f * s); quadraticTo(-0.192f * s, -0.184f * s, 0.0f * s, -0.087f * s); close() }
    val p3 = Path().apply { moveTo(0.0f * s, -0.087f * s); quadraticTo(0.192f * s, -0.184f * s, 0.159f * s, -0.396f * s); quadraticTo(-0.034f * s, -0.299f * s, 0.0f * s, -0.087f * s); close() }
    val p4 = Path().apply { moveTo(0.0f * s, -0.087f * s); quadraticTo(0.127f * s, -0.261f * s, 0.0f * s, -0.436f * s); quadraticTo(-0.127f * s, -0.261f * s, 0.0f * s, -0.087f * s); close() }
    val p5 = Path().apply { moveTo(0.238f * s, 0.151f * s); cubicTo(0.238f * s, 0.308f * s, 0.131f * s, 0.436f * s, 0.0f * s, 0.436f * s); cubicTo(-0.131f * s, 0.436f * s, -0.238f * s, 0.308f * s, -0.238f * s, 0.151f * s); cubicTo(-0.238f * s, -0.007f * s, -0.131f * s, -0.135f * s, 0.0f * s, -0.135f * s); cubicTo(0.131f * s, -0.135f * s, 0.238f * s, -0.007f * s, 0.238f * s, 0.151f * s); close() }
    val p6 = Path().apply { moveTo(0.218f * s, 0.111f * s); cubicTo(0.218f * s, 0.268f * s, 0.112f * s, 0.396f * s, -0.02f * s, 0.396f * s); cubicTo(-0.151f * s, 0.396f * s, -0.257f * s, 0.268f * s, -0.257f * s, 0.111f * s); cubicTo(-0.257f * s, -0.047f * s, -0.151f * s, -0.174f * s, -0.02f * s, -0.174f * s); cubicTo(0.112f * s, -0.174f * s, 0.218f * s, -0.047f * s, 0.218f * s, 0.111f * s); close() }
    val p7 = Path().apply { moveTo(-0.123f * s, 0.092f * s); cubicTo(-0.131f * s, 0.135f * s, -0.149f * s, 0.168f * s, -0.164f * s, 0.165f * s); cubicTo(-0.179f * s, 0.163f * s, -0.185f * s, 0.125f * s, -0.178f * s, 0.082f * s); cubicTo(-0.17f * s, 0.039f * s, -0.152f * s, 0.007f * s, -0.137f * s, 0.009f * s); cubicTo(-0.122f * s, 0.012f * s, -0.116f * s, 0.049f * s, -0.123f * s, 0.092f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2F9E47))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2F9E47))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF3DB554))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF3DB554))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF55CC63))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFCCA125))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFFFFC92E))
        }
    }
    clipPath(p5) {
        drawLine(sil ?: Color(0xBFD98A00), Offset(-0.697f * s, -0.166f * s), Offset(-0.063f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(-0.063f * s, -0.166f * s), Offset(-0.697f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(-0.57f * s, -0.166f * s), Offset(0.063f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(0.063f * s, -0.166f * s), Offset(-0.57f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(-0.444f * s, -0.166f * s), Offset(0.19f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(0.19f * s, -0.166f * s), Offset(-0.444f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(-0.317f * s, -0.166f * s), Offset(0.317f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(0.317f * s, -0.166f * s), Offset(-0.317f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(-0.19f * s, -0.166f * s), Offset(0.444f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(0.444f * s, -0.166f * s), Offset(-0.19f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(-0.063f * s, -0.166f * s), Offset(0.57f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(0.57f * s, -0.166f * s), Offset(-0.063f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(0.063f * s, -0.166f * s), Offset(0.697f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0xBFD98A00), Offset(0.697f * s, -0.166f * s), Offset(0.063f * s, 0.467f * s), 0.024f * s, StrokeCap.Round)
    }
    if (sil == null) {
        drawPath(p7, Color(0x80FFFFFF))
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCanard(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.306f * s, 0.131f * s); quadraticTo(-0.326f * s, 0.146f * s, -0.338f * s, 0.123f * s); lineTo(-0.431f * s, -0.057f * s); quadraticTo(-0.443f * s, -0.079f * s, -0.419f * s, -0.071f * s); lineTo(-0.184f * s, 0.013f * s); quadraticTo(-0.16f * s, 0.021f * s, -0.18f * s, 0.036f * s); close() }
    val p1 = Path().apply { moveTo(0.322f * s, 0.146f * s); cubicTo(0.322f * s, 0.269f * s, 0.155f * s, 0.37f * s, -0.052f * s, 0.37f * s); cubicTo(-0.259f * s, 0.37f * s, -0.426f * s, 0.269f * s, -0.426f * s, 0.146f * s); cubicTo(-0.426f * s, 0.022f * s, -0.259f * s, -0.079f * s, -0.052f * s, -0.079f * s); cubicTo(0.155f * s, -0.079f * s, 0.322f * s, 0.022f * s, 0.322f * s, 0.146f * s); close() }
    val p2 = Path().apply { moveTo(0.305f * s, 0.104f * s); cubicTo(0.305f * s, 0.228f * s, 0.138f * s, 0.328f * s, -0.069f * s, 0.328f * s); cubicTo(-0.275f * s, 0.328f * s, -0.443f * s, 0.228f * s, -0.443f * s, 0.104f * s); cubicTo(-0.443f * s, -0.02f * s, -0.275f * s, -0.12f * s, -0.069f * s, -0.12f * s); cubicTo(0.138f * s, -0.12f * s, 0.305f * s, -0.02f * s, 0.305f * s, 0.104f * s); close() }
    val p3 = Path().apply { moveTo(-0.211f * s, 0.018f * s); cubicTo(-0.209f * s, 0.029f * s, -0.237f * s, 0.043f * s, -0.273f * s, 0.05f * s); cubicTo(-0.309f * s, 0.056f * s, -0.34f * s, 0.052f * s, -0.342f * s, 0.041f * s); cubicTo(-0.344f * s, 0.029f * s, -0.316f * s, 0.015f * s, -0.28f * s, 0.009f * s); cubicTo(-0.244f * s, 0.002f * s, -0.213f * s, 0.006f * s, -0.211f * s, 0.018f * s); close() }
    val p4 = Path().apply { moveTo(0.056f * s, 0.112f * s); cubicTo(-0.027f * s, 0.046f * s, -0.193f * s, 0.046f * s, -0.31f * s, 0.096f * s); cubicTo(-0.243f * s, 0.229f * s, -0.11f * s, 0.287f * s, -0.01f * s, 0.27f * s); cubicTo(0.073f * s, 0.254f * s, 0.089f * s, 0.162f * s, 0.056f * s, 0.112f * s); close() }
    val p5 = Path().apply { moveTo(0.305f * s, -0.187f * s); cubicTo(0.305f * s, -0.086f * s, 0.22f * s, -0.004f * s, 0.114f * s, -0.004f * s); cubicTo(0.009f * s, -0.004f * s, -0.077f * s, -0.086f * s, -0.077f * s, -0.187f * s); cubicTo(-0.077f * s, -0.288f * s, 0.009f * s, -0.37f * s, 0.114f * s, -0.37f * s); cubicTo(0.22f * s, -0.37f * s, 0.305f * s, -0.288f * s, 0.305f * s, -0.187f * s); close() }
    val p6 = Path().apply { moveTo(0.289f * s, -0.22f * s); cubicTo(0.289f * s, -0.119f * s, 0.203f * s, -0.037f * s, 0.098f * s, -0.037f * s); cubicTo(-0.008f * s, -0.037f * s, -0.094f * s, -0.119f * s, -0.094f * s, -0.22f * s); cubicTo(-0.094f * s, -0.321f * s, -0.008f * s, -0.403f * s, 0.098f * s, -0.403f * s); cubicTo(0.203f * s, -0.403f * s, 0.289f * s, -0.321f * s, 0.289f * s, -0.22f * s); close() }
    val p7 = Path().apply { moveTo(0.04f * s, -0.294f * s); cubicTo(0.048f * s, -0.283f * s, 0.039f * s, -0.263f * s, 0.02f * s, -0.25f * s); cubicTo(0.002f * s, -0.237f * s, -0.02f * s, -0.235f * s, -0.028f * s, -0.246f * s); cubicTo(-0.036f * s, -0.258f * s, -0.027f * s, -0.277f * s, -0.008f * s, -0.291f * s); cubicTo(0.011f * s, -0.304f * s, 0.032f * s, -0.305f * s, 0.04f * s, -0.294f * s); close() }
    val p8 = Path().apply { moveTo(0.437f * s, -0.122f * s); cubicTo(0.433f * s, -0.093f * s, 0.382f * s, -0.075f * s, 0.323f * s, -0.084f * s); cubicTo(0.264f * s, -0.092f * s, 0.219f * s, -0.123f * s, 0.223f * s, -0.152f * s); cubicTo(0.228f * s, -0.182f * s, 0.279f * s, -0.199f * s, 0.338f * s, -0.191f * s); cubicTo(0.397f * s, -0.182f * s, 0.442f * s, -0.152f * s, 0.437f * s, -0.122f * s); close() }
    drawPath(p0, sil ?: Color(0xFFFFD23F))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFCCA832))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFFFFD23F))
        }
        drawPath(p3, Color(0x8CFFFFFF))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFE6BD39))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.028f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFCCA832))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFFFFD23F))
        }
        drawPath(p7, Color(0x8CFFFFFF))
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFFF8A1F))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.046f * s, Offset(0.139f * s, -0.229f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.046f * s, Offset(0.139f * s, -0.229f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.027f * s, Offset(0.153f * s, -0.224f * s))
        drawCircle(Color(0xFFFFFFFF), 0.009f * s, Offset(0.143f * s, -0.236f * s))
        drawOval(Color(0x66FF4F86), Offset(0.056f * s, -0.127f * s), Size(0.067f * s, 0.047f * s))
    }
}

private fun DrawScope.artAbeille(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.082f * s, -0.169f * s); quadraticTo(-0.099f * s, -0.318f * s, -0.181f * s, -0.384f * s) }
    val p1 = Path().apply { moveTo(0.082f * s, -0.169f * s); quadraticTo(0.099f * s, -0.318f * s, 0.181f * s, -0.384f * s) }
    val p2 = Path().apply { moveTo(-0.128f * s, -0.327f * s); cubicTo(-0.083f * s, -0.243f * s, -0.089f * s, -0.152f * s, -0.141f * s, -0.124f * s); cubicTo(-0.194f * s, -0.096f * s, -0.272f * s, -0.142f * s, -0.317f * s, -0.226f * s); cubicTo(-0.362f * s, -0.311f * s, -0.356f * s, -0.402f * s, -0.304f * s, -0.429f * s); cubicTo(-0.252f * s, -0.457f * s, -0.173f * s, -0.411f * s, -0.128f * s, -0.327f * s); close() }
    val p3 = Path().apply { moveTo(0.317f * s, -0.226f * s); cubicTo(0.272f * s, -0.142f * s, 0.194f * s, -0.096f * s, 0.141f * s, -0.124f * s); cubicTo(0.089f * s, -0.152f * s, 0.083f * s, -0.243f * s, 0.128f * s, -0.327f * s); cubicTo(0.173f * s, -0.411f * s, 0.252f * s, -0.457f * s, 0.304f * s, -0.429f * s); cubicTo(0.356f * s, -0.402f * s, 0.362f * s, -0.311f * s, 0.317f * s, -0.226f * s); close() }
    val p4 = Path().apply { moveTo(-0.051f * s, 0.352f * s); quadraticTo(-0.058f * s, 0.342f * s, -0.045f * s, 0.342f * s); lineTo(0.045f * s, 0.342f * s); quadraticTo(0.058f * s, 0.342f * s, 0.051f * s, 0.352f * s); lineTo(0.006f * s, 0.43f * s); quadraticTo(0.0f * s, 0.441f * s, -0.006f * s, 0.43f * s); close() }
    val p5 = Path().apply { moveTo(0.313f * s, 0.078f * s); cubicTo(0.313f * s, 0.246f * s, 0.173f * s, 0.383f * s, 0.0f * s, 0.383f * s); cubicTo(-0.173f * s, 0.383f * s, -0.313f * s, 0.246f * s, -0.313f * s, 0.078f * s); cubicTo(-0.313f * s, -0.091f * s, -0.173f * s, -0.227f * s, 0.0f * s, -0.227f * s); cubicTo(0.173f * s, -0.227f * s, 0.313f * s, -0.091f * s, 0.313f * s, 0.078f * s); close() }
    val p6 = Path().apply { moveTo(0.297f * s, 0.037f * s); cubicTo(0.297f * s, 0.205f * s, 0.157f * s, 0.342f * s, -0.016f * s, 0.342f * s); cubicTo(-0.189f * s, 0.342f * s, -0.33f * s, 0.205f * s, -0.33f * s, 0.037f * s); cubicTo(-0.33f * s, -0.132f * s, -0.189f * s, -0.268f * s, -0.016f * s, -0.268f * s); cubicTo(0.157f * s, -0.268f * s, 0.297f * s, -0.132f * s, 0.297f * s, 0.037f * s); close() }
    val p7 = Path().apply { moveTo(-0.168f * s, -0.032f * s); cubicTo(-0.183f * s, 0.001f * s, -0.209f * s, 0.022f * s, -0.226f * s, 0.014f * s); cubicTo(-0.242f * s, 0.006f * s, -0.243f * s, -0.027f * s, -0.228f * s, -0.06f * s); cubicTo(-0.212f * s, -0.093f * s, -0.186f * s, -0.113f * s, -0.17f * s, -0.106f * s); cubicTo(-0.153f * s, -0.098f * s, -0.153f * s, -0.065f * s, -0.168f * s, -0.032f * s); close() }
    val p8 = Path().apply { moveTo(-0.058f * s, 0.037f * s); quadraticTo(0.0f * s, 0.103f * s, 0.058f * s, 0.037f * s) }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.034f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.034f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.033f * s, Offset(-0.181f * s, -0.384f * s))
        drawCircle(Color(0xFF2B2D42), 0.033f * s, Offset(0.181f * s, -0.384f * s))
    }
    drawPath(p2, sil ?: Color(0xF2CFEAFF))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xF2CFEAFF))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFCCA832))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFFFFD23F))
        }
        clipPath(p5) {
            drawRoundRect(Color(0xFF2B2D42), Offset(-0.412f * s, 0.135f * s), Size(0.824f * s, 0.082f * s), CornerRadius(0.0f * s))
            drawRoundRect(Color(0xFF2B2D42), Offset(-0.412f * s, 0.292f * s), Size(0.824f * s, 0.082f * s), CornerRadius(0.0f * s))
        }
        drawPath(p7, Color(0x8CFFFFFF))
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.049f * s, Offset(-0.115f * s, -0.054f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.049f * s, Offset(-0.115f * s, -0.054f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.029f * s, Offset(-0.11f * s, -0.044f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(-0.121f * s, -0.057f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.049f * s, Offset(0.115f * s, -0.054f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.049f * s, Offset(0.115f * s, -0.054f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.029f * s, Offset(0.11f * s, -0.044f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(0.1f * s, -0.057f * s))
    }
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(-0.247f * s, 0.005f * s), Size(0.066f * s, 0.046f * s))
        drawOval(Color(0x66FF4F86), Offset(0.181f * s, 0.005f * s), Size(0.066f * s, 0.046f * s))
    }
}

private fun DrawScope.artTournesol(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, 0.076f * s); quadraticTo(0.032f * s, 0.236f * s, 0.0f * s, 0.395f * s) }
    val p1 = Path().apply { moveTo(0.0f * s, 0.299f * s); quadraticTo(-0.069f * s, 0.135f * s, -0.24f * s, 0.188f * s); quadraticTo(-0.17f * s, 0.352f * s, 0.0f * s, 0.299f * s); close() }
    val p2 = Path().apply { moveTo(0.0f * s, 0.331f * s); quadraticTo(0.168f * s, 0.389f * s, 0.24f * s, 0.228f * s); quadraticTo(0.072f * s, 0.17f * s, 0.0f * s, 0.331f * s); close() }
    val p3 = Path().apply { moveTo(0.064f * s, -0.116f * s); quadraticTo(0.2f * s, 0.044f * s, 0.336f * s, -0.116f * s); quadraticTo(0.2f * s, -0.276f * s, 0.064f * s, -0.116f * s); close() }
    val p4 = Path().apply { moveTo(0.052f * s, -0.078f * s); quadraticTo(0.068f * s, 0.131f * s, 0.272f * s, 0.081f * s); quadraticTo(0.256f * s, -0.128f * s, 0.052f * s, -0.078f * s); close() }
    val p5 = Path().apply { moveTo(0.02f * s, -0.055f * s); quadraticTo(-0.09f * s, 0.123f * s, 0.104f * s, 0.203f * s); quadraticTo(0.214f * s, 0.025f * s, 0.02f * s, -0.055f * s); close() }
    val p6 = Path().apply { moveTo(-0.02f * s, -0.055f * s); quadraticTo(-0.214f * s, 0.025f * s, -0.104f * s, 0.203f * s); quadraticTo(0.09f * s, 0.123f * s, -0.02f * s, -0.055f * s); close() }
    val p7 = Path().apply { moveTo(-0.052f * s, -0.078f * s); quadraticTo(-0.255f * s, -0.128f * s, -0.271f * s, 0.081f * s); quadraticTo(-0.068f * s, 0.131f * s, -0.052f * s, -0.078f * s); close() }
    val p8 = Path().apply { moveTo(-0.064f * s, -0.116f * s); quadraticTo(-0.2f * s, -0.276f * s, -0.336f * s, -0.116f * s); quadraticTo(-0.2f * s, 0.044f * s, -0.064f * s, -0.116f * s); close() }
    val p9 = Path().apply { moveTo(-0.052f * s, -0.154f * s); quadraticTo(-0.068f * s, -0.363f * s, -0.271f * s, -0.313f * s); quadraticTo(-0.255f * s, -0.104f * s, -0.052f * s, -0.154f * s); close() }
    val p10 = Path().apply { moveTo(-0.02f * s, -0.177f * s); quadraticTo(0.09f * s, -0.355f * s, -0.104f * s, -0.435f * s); quadraticTo(-0.214f * s, -0.257f * s, -0.02f * s, -0.177f * s); close() }
    val p11 = Path().apply { moveTo(0.02f * s, -0.177f * s); quadraticTo(0.214f * s, -0.257f * s, 0.104f * s, -0.435f * s); quadraticTo(-0.09f * s, -0.355f * s, 0.02f * s, -0.177f * s); close() }
    val p12 = Path().apply { moveTo(0.052f * s, -0.154f * s); quadraticTo(0.256f * s, -0.104f * s, 0.272f * s, -0.313f * s); quadraticTo(0.068f * s, -0.363f * s, 0.052f * s, -0.154f * s); close() }
    val p13 = Path().apply { moveTo(0.061f * s, -0.096f * s); quadraticTo(0.133f * s, 0.095f * s, 0.304f * s, -0.017f * s); quadraticTo(0.232f * s, -0.209f * s, 0.061f * s, -0.096f * s); close() }
    val p14 = Path().apply { moveTo(0.038f * s, -0.064f * s); quadraticTo(-0.016f * s, 0.133f * s, 0.188f * s, 0.143f * s); quadraticTo(0.242f * s, -0.055f * s, 0.038f * s, -0.064f * s); close() }
    val p15 = Path().apply { moveTo(0.0f * s, -0.052f * s); quadraticTo(-0.16f * s, 0.076f * s, 0.0f * s, 0.204f * s); quadraticTo(0.16f * s, 0.076f * s, 0.0f * s, -0.052f * s); close() }
    val p16 = Path().apply { moveTo(-0.037f * s, -0.064f * s); quadraticTo(-0.242f * s, -0.055f * s, -0.188f * s, 0.143f * s); quadraticTo(0.017f * s, 0.133f * s, -0.037f * s, -0.064f * s); close() }
    val p17 = Path().apply { moveTo(-0.061f * s, -0.096f * s); quadraticTo(-0.232f * s, -0.209f * s, -0.304f * s, -0.017f * s); quadraticTo(-0.133f * s, 0.095f * s, -0.061f * s, -0.096f * s); close() }
    val p18 = Path().apply { moveTo(-0.061f * s, -0.136f * s); quadraticTo(-0.133f * s, -0.327f * s, -0.304f * s, -0.215f * s); quadraticTo(-0.232f * s, -0.023f * s, -0.061f * s, -0.136f * s); close() }
    val p19 = Path().apply { moveTo(-0.037f * s, -0.168f * s); quadraticTo(0.017f * s, -0.365f * s, -0.188f * s, -0.375f * s); quadraticTo(-0.242f * s, -0.177f * s, -0.037f * s, -0.168f * s); close() }
    val p20 = Path().apply { moveTo(0.0f * s, -0.18f * s); quadraticTo(0.16f * s, -0.308f * s, 0.0f * s, -0.436f * s); quadraticTo(-0.16f * s, -0.308f * s, 0.0f * s, -0.18f * s); close() }
    val p21 = Path().apply { moveTo(0.038f * s, -0.168f * s); quadraticTo(0.242f * s, -0.177f * s, 0.188f * s, -0.375f * s); quadraticTo(-0.016f * s, -0.365f * s, 0.038f * s, -0.168f * s); close() }
    val p22 = Path().apply { moveTo(0.061f * s, -0.136f * s); quadraticTo(0.232f * s, -0.023f * s, 0.304f * s, -0.215f * s); quadraticTo(0.133f * s, -0.327f * s, 0.061f * s, -0.136f * s); close() }
    val p23 = Path().apply { moveTo(0.16f * s, -0.116f * s); cubicTo(0.16f * s, -0.028f * s, 0.088f * s, 0.044f * s, 0.0f * s, 0.044f * s); cubicTo(-0.088f * s, 0.044f * s, -0.16f * s, -0.028f * s, -0.16f * s, -0.116f * s); cubicTo(-0.16f * s, -0.204f * s, -0.088f * s, -0.276f * s, 0.0f * s, -0.276f * s); cubicTo(0.088f * s, -0.276f * s, 0.16f * s, -0.204f * s, 0.16f * s, -0.116f * s); close() }
    val p24 = Path().apply { moveTo(0.144f * s, -0.148f * s); cubicTo(0.144f * s, -0.06f * s, 0.072f * s, 0.012f * s, -0.016f * s, 0.012f * s); cubicTo(-0.104f * s, 0.012f * s, -0.176f * s, -0.06f * s, -0.176f * s, -0.148f * s); cubicTo(-0.176f * s, -0.236f * s, -0.104f * s, -0.308f * s, -0.016f * s, -0.308f * s); cubicTo(0.072f * s, -0.308f * s, 0.144f * s, -0.236f * s, 0.144f * s, -0.148f * s); close() }
    val p25 = Path().apply { moveTo(0.072f * s, -0.116f * s); cubicTo(0.072f * s, -0.076f * s, 0.04f * s, -0.044f * s, 0.0f * s, -0.044f * s); cubicTo(-0.04f * s, -0.044f * s, -0.072f * s, -0.076f * s, -0.072f * s, -0.116f * s); cubicTo(-0.072f * s, -0.156f * s, -0.04f * s, -0.188f * s, 0.0f * s, -0.188f * s); cubicTo(0.04f * s, -0.188f * s, 0.072f * s, -0.156f * s, 0.072f * s, -0.116f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF3FAE49), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF3FAE49))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF3FAE49))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFFFB224))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFFFB224))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFFFB224))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFFFB224))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFFFB224))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFFFB224))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFFFB224))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFFFFB224))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFFFFB224))
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFFFFB224))
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p13, sil ?: Color(0xFFFFD23F))
    drawPath(p13, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p14, sil ?: Color(0xFFFFD23F))
    drawPath(p14, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p15, sil ?: Color(0xFFFFD23F))
    drawPath(p15, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p16, sil ?: Color(0xFFFFD23F))
    drawPath(p16, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p17, sil ?: Color(0xFFFFD23F))
    drawPath(p17, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p18, sil ?: Color(0xFFFFD23F))
    drawPath(p18, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p19, sil ?: Color(0xFFFFD23F))
    drawPath(p19, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p20, sil ?: Color(0xFFFFD23F))
    drawPath(p20, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p21, sil ?: Color(0xFFFFD23F))
    drawPath(p21, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p22, sil ?: Color(0xFFFFD23F))
    drawPath(p22, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p23, sil ?: Color(0xFF684420))
    if (sil == null) {
        clipPath(p23) {
            drawPath(p24, Color(0xFF8A5A2B))
        }
        drawPath(p25, Color(0xFF5A3320))
        drawCircle(Color(0xFFC98B4B), 0.024f * s, Offset(0.095f * s, -0.085f * s))
        drawCircle(Color(0xFFC98B4B), 0.024f * s, Offset(0.0f * s, -0.016f * s))
        drawCircle(Color(0xFFC98B4B), 0.024f * s, Offset(-0.095f * s, -0.085f * s))
        drawCircle(Color(0xFFC98B4B), 0.024f * s, Offset(-0.059f * s, -0.197f * s))
        drawCircle(Color(0xFFC98B4B), 0.024f * s, Offset(0.059f * s, -0.197f * s))
    }
    drawPath(p23, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artLune(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.196f * s, -0.412f * s); cubicTo(-0.008f * s, -0.479f * s, -0.231f * s, -0.387f * s, -0.327f * s, -0.195f * s); cubicTo(-0.424f * s, -0.003f * s, -0.365f * s, 0.23f * s, -0.19f * s, 0.354f * s); cubicTo(-0.014f * s, 0.478f * s, 0.225f * s, 0.455f * s, 0.374f * s, 0.3f * s); cubicTo(0.241f * s, 0.349f * s, 0.091f * s, 0.318f * s, -0.011f * s, 0.22f * s); cubicTo(-0.113f * s, 0.122f * s, -0.15f * s, -0.026f * s, -0.106f * s, -0.161f * s); cubicTo(-0.062f * s, -0.296f * s, 0.055f * s, -0.393f * s, 0.196f * s, -0.412f * s); close() }
    val p1 = Path().apply { moveTo(0.177f * s, -0.449f * s); cubicTo(-0.027f * s, -0.516f * s, -0.249f * s, -0.424f * s, -0.346f * s, -0.232f * s); cubicTo(-0.442f * s, -0.04f * s, -0.384f * s, 0.193f * s, -0.208f * s, 0.317f * s); cubicTo(-0.033f * s, 0.441f * s, 0.207f * s, 0.418f * s, 0.355f * s, 0.263f * s); cubicTo(0.222f * s, 0.312f * s, 0.073f * s, 0.281f * s, -0.029f * s, 0.183f * s); cubicTo(-0.132f * s, 0.085f * s, -0.169f * s, -0.063f * s, -0.125f * s, -0.198f * s); cubicTo(-0.081f * s, -0.332f * s, 0.037f * s, -0.43f * s, 0.177f * s, -0.449f * s); close() }
    val p2 = Path().apply { moveTo(-0.249f * s, -0.185f * s); cubicTo(-0.272f * s, -0.145f * s, -0.305f * s, -0.121f * s, -0.323f * s, -0.131f * s); cubicTo(-0.34f * s, -0.141f * s, -0.336f * s, -0.182f * s, -0.313f * s, -0.221f * s); cubicTo(-0.29f * s, -0.261f * s, -0.257f * s, -0.285f * s, -0.24f * s, -0.275f * s); cubicTo(-0.222f * s, -0.265f * s, -0.226f * s, -0.224f * s, -0.249f * s, -0.185f * s); close() }
    val p3 = Path().apply { moveTo(-0.263f * s, -0.046f * s); quadraticTo(-0.217f * s, 0.018f * s, -0.17f * s, -0.046f * s) }
    val p4 = Path().apply { moveTo(-0.249f * s, 0.12f * s); quadraticTo(-0.217f * s, 0.157f * s, -0.184f * s, 0.12f * s) }
    drawPath(p0, sil ?: Color(0xFFCCA832))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFFD23F))
        }
        drawPath(p2, Color(0x8CFFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(-0.323f * s, 0.028f * s), Size(0.083f * s, 0.055f * s))
    }
}

private fun DrawScope.artBus(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.434f * s, -0.246f * s); quadraticTo(-0.434f * s, -0.308f * s, -0.372f * s, -0.308f * s); lineTo(0.124f * s, -0.308f * s); quadraticTo(0.186f * s, -0.308f * s, 0.204f * s, -0.249f * s); lineTo(0.249f * s, -0.103f * s); quadraticTo(0.257f * s, -0.077f * s, 0.283f * s, -0.072f * s); lineTo(0.399f * s, -0.049f * s); quadraticTo(0.434f * s, -0.042f * s, 0.434f * s, -0.007f * s); lineTo(0.434f * s, 0.153f * s); quadraticTo(0.434f * s, 0.188f * s, 0.399f * s, 0.188f * s); lineTo(-0.399f * s, 0.188f * s); quadraticTo(-0.434f * s, 0.188f * s, -0.434f * s, 0.153f * s); close() }
    val p1 = Path().apply { moveTo(-0.456f * s, -0.29f * s); quadraticTo(-0.456f * s, -0.352f * s, -0.394f * s, -0.352f * s); lineTo(0.102f * s, -0.352f * s); quadraticTo(0.164f * s, -0.352f * s, 0.182f * s, -0.293f * s); lineTo(0.227f * s, -0.147f * s); quadraticTo(0.235f * s, -0.122f * s, 0.261f * s, -0.117f * s); lineTo(0.377f * s, -0.093f * s); quadraticTo(0.412f * s, -0.086f * s, 0.412f * s, -0.051f * s); lineTo(0.412f * s, 0.109f * s); quadraticTo(0.412f * s, 0.144f * s, 0.377f * s, 0.144f * s); lineTo(-0.421f * s, 0.144f * s); quadraticTo(-0.456f * s, 0.144f * s, -0.456f * s, 0.109f * s); close() }
    val p2 = Path().apply { moveTo(0.142f * s, -0.237f * s); quadraticTo(0.142f * s, -0.246f * s, 0.151f * s, -0.246f * s); lineTo(0.177f * s, -0.246f * s); quadraticTo(0.186f * s, -0.246f * s, 0.188f * s, -0.237f * s); lineTo(0.228f * s, -0.086f * s); quadraticTo(0.23f * s, -0.077f * s, 0.222f * s, -0.077f * s); lineTo(0.151f * s, -0.077f * s); quadraticTo(0.142f * s, -0.077f * s, 0.142f * s, -0.086f * s); close() }
    val p3 = Path().apply { moveTo(0.43f * s, 0.011f * s); cubicTo(0.43f * s, 0.033f * s, 0.418f * s, 0.051f * s, 0.403f * s, 0.051f * s); cubicTo(0.388f * s, 0.051f * s, 0.377f * s, 0.033f * s, 0.377f * s, 0.011f * s); cubicTo(0.377f * s, -0.011f * s, 0.388f * s, -0.029f * s, 0.403f * s, -0.029f * s); cubicTo(0.418f * s, -0.029f * s, 0.43f * s, -0.011f * s, 0.43f * s, 0.011f * s); close() }
    drawPath(p0, sil ?: Color(0xFFD1AC34))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFFD23F))
        }
        clipPath(p0) {
            drawRoundRect(Color(0xFF2B2D42), Offset(-0.452f * s, 0.047f * s), Size(0.886f * s, 0.04f * s), CornerRadius(0.0f * s))
        }
        drawOval(Color(0x80FFFFFF), Offset(-0.381f * s, -0.299f * s), Size(0.106f * s, 0.027f * s))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(sil ?: Color(0xFFBFE3FF), Offset(-0.381f * s, -0.246f * s), Size(0.106f * s, 0.142f * s), CornerRadius(0.018f * s))
    drawRoundRect(sil ?: Color(0xFF2B2D42), Offset(-0.381f * s, -0.246f * s), Size(0.106f * s, 0.142f * s), CornerRadius(0.018f * s), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(sil ?: Color(0xFFBFE3FF), Offset(-0.248f * s, -0.246f * s), Size(0.106f * s, 0.142f * s), CornerRadius(0.018f * s))
    drawRoundRect(sil ?: Color(0xFF2B2D42), Offset(-0.248f * s, -0.246f * s), Size(0.106f * s, 0.142f * s), CornerRadius(0.018f * s), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(sil ?: Color(0xFFBFE3FF), Offset(-0.115f * s, -0.246f * s), Size(0.106f * s, 0.142f * s), CornerRadius(0.018f * s))
    drawRoundRect(sil ?: Color(0xFF2B2D42), Offset(-0.115f * s, -0.246f * s), Size(0.106f * s, 0.142f * s), CornerRadius(0.018f * s), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(sil ?: Color(0xFFBFE3FF), Offset(0.018f * s, -0.246f * s), Size(0.106f * s, 0.142f * s), CornerRadius(0.018f * s))
    drawRoundRect(sil ?: Color(0xFF2B2D42), Offset(0.018f * s, -0.246f * s), Size(0.106f * s, 0.142f * s), CornerRadius(0.018f * s), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFBFE3FF))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFFFF2B0))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF41465F), 0.102f * s, Offset(-0.257f * s, 0.206f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.102f * s, Offset(-0.257f * s, 0.206f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFD9DEE9), 0.04f * s, Offset(-0.257f * s, 0.206f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.04f * s, Offset(-0.257f * s, 0.206f * s), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF41465F), 0.102f * s, Offset(0.257f * s, 0.206f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.102f * s, Offset(0.257f * s, 0.206f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFD9DEE9), 0.04f * s, Offset(0.257f * s, 0.206f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.04f * s, Offset(0.257f * s, 0.206f * s), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCouronne(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.326f * s, -0.185f * s); cubicTo(-0.326f * s, -0.153f * s, -0.351f * s, -0.128f * s, -0.382f * s, -0.128f * s); cubicTo(-0.413f * s, -0.128f * s, -0.439f * s, -0.153f * s, -0.439f * s, -0.185f * s); cubicTo(-0.439f * s, -0.216f * s, -0.413f * s, -0.241f * s, -0.382f * s, -0.241f * s); cubicTo(-0.351f * s, -0.241f * s, -0.326f * s, -0.216f * s, -0.326f * s, -0.185f * s); close() }
    val p1 = Path().apply { moveTo(0.061f * s, -0.28f * s); cubicTo(0.061f * s, -0.246f * s, 0.034f * s, -0.219f * s, 0.0f * s, -0.219f * s); cubicTo(-0.034f * s, -0.219f * s, -0.061f * s, -0.246f * s, -0.061f * s, -0.28f * s); cubicTo(-0.061f * s, -0.314f * s, -0.034f * s, -0.341f * s, 0.0f * s, -0.341f * s); cubicTo(0.034f * s, -0.341f * s, 0.061f * s, -0.314f * s, 0.061f * s, -0.28f * s); close() }
    val p2 = Path().apply { moveTo(0.439f * s, -0.185f * s); cubicTo(0.439f * s, -0.153f * s, 0.413f * s, -0.128f * s, 0.382f * s, -0.128f * s); cubicTo(0.351f * s, -0.128f * s, 0.326f * s, -0.153f * s, 0.326f * s, -0.185f * s); cubicTo(0.326f * s, -0.216f * s, 0.351f * s, -0.241f * s, 0.382f * s, -0.241f * s); cubicTo(0.413f * s, -0.241f * s, 0.439f * s, -0.216f * s, 0.439f * s, -0.185f * s); close() }
    val p3 = Path().apply { moveTo(-0.339f * s, 0.337f * s); quadraticTo(-0.382f * s, 0.337f * s, -0.382f * s, 0.293f * s); lineTo(-0.382f * s, -0.106f * s); quadraticTo(-0.382f * s, -0.15f * s, -0.352f * s, -0.118f * s); lineTo(-0.209f * s, 0.031f * s); quadraticTo(-0.191f * s, 0.05f * s, -0.177f * s, 0.028f * s); lineTo(-0.024f * s, -0.201f * s); quadraticTo(0.0f * s, -0.237f * s, 0.024f * s, -0.201f * s); lineTo(0.177f * s, 0.028f * s); quadraticTo(0.191f * s, 0.05f * s, 0.209f * s, 0.031f * s); lineTo(0.352f * s, -0.118f * s); quadraticTo(0.382f * s, -0.15f * s, 0.382f * s, -0.106f * s); lineTo(0.382f * s, 0.293f * s); quadraticTo(0.382f * s, 0.337f * s, 0.339f * s, 0.337f * s); close() }
    val p4 = Path().apply { moveTo(-0.36f * s, 0.298f * s); quadraticTo(-0.404f * s, 0.298f * s, -0.404f * s, 0.254f * s); lineTo(-0.404f * s, -0.145f * s); quadraticTo(-0.404f * s, -0.189f * s, -0.374f * s, -0.158f * s); lineTo(-0.231f * s, -0.008f * s); quadraticTo(-0.213f * s, 0.011f * s, -0.198f * s, -0.011f * s); lineTo(-0.046f * s, -0.24f * s); quadraticTo(-0.022f * s, -0.276f * s, 0.002f * s, -0.24f * s); lineTo(0.155f * s, -0.011f * s); quadraticTo(0.169f * s, 0.011f * s, 0.187f * s, -0.008f * s); lineTo(0.331f * s, -0.158f * s); quadraticTo(0.361f * s, -0.189f * s, 0.361f * s, -0.145f * s); lineTo(0.361f * s, 0.254f * s); quadraticTo(0.361f * s, 0.298f * s, 0.317f * s, 0.298f * s); close() }
    val p5 = Path().apply { moveTo(-0.236f * s, 0.033f * s); cubicTo(-0.251f * s, 0.073f * s, -0.274f * s, 0.102f * s, -0.287f * s, 0.097f * s); cubicTo(-0.301f * s, 0.092f * s, -0.3f * s, 0.056f * s, -0.285f * s, 0.015f * s); cubicTo(-0.27f * s, -0.026f * s, -0.247f * s, -0.054f * s, -0.234f * s, -0.05f * s); cubicTo(-0.22f * s, -0.045f * s, -0.221f * s, -0.008f * s, -0.236f * s, 0.033f * s); close() }
    val p6 = Path().apply { moveTo(0.061f * s, 0.263f * s); cubicTo(0.061f * s, 0.294f * s, 0.034f * s, 0.319f * s, 0.0f * s, 0.319f * s); cubicTo(-0.034f * s, 0.319f * s, -0.061f * s, 0.294f * s, -0.061f * s, 0.263f * s); cubicTo(-0.061f * s, 0.232f * s, -0.034f * s, 0.206f * s, 0.0f * s, 0.206f * s); cubicTo(0.034f * s, 0.206f * s, 0.061f * s, 0.232f * s, 0.061f * s, 0.263f * s); close() }
    val p7 = Path().apply { moveTo(-0.191f * s, 0.263f * s); cubicTo(-0.191f * s, 0.287f * s, -0.211f * s, 0.306f * s, -0.235f * s, 0.306f * s); cubicTo(-0.258f * s, 0.306f * s, -0.278f * s, 0.287f * s, -0.278f * s, 0.263f * s); cubicTo(-0.278f * s, 0.239f * s, -0.258f * s, 0.219f * s, -0.235f * s, 0.219f * s); cubicTo(-0.211f * s, 0.219f * s, -0.191f * s, 0.239f * s, -0.191f * s, 0.263f * s); close() }
    val p8 = Path().apply { moveTo(0.278f * s, 0.263f * s); cubicTo(0.278f * s, 0.287f * s, 0.259f * s, 0.306f * s, 0.235f * s, 0.306f * s); cubicTo(0.211f * s, 0.306f * s, 0.191f * s, 0.287f * s, 0.191f * s, 0.263f * s); cubicTo(0.191f * s, 0.239f * s, 0.211f * s, 0.219f * s, 0.235f * s, 0.219f * s); cubicTo(0.259f * s, 0.219f * s, 0.278f * s, 0.239f * s, 0.278f * s, 0.263f * s); close() }
    drawPath(p0, sil ?: Color(0xFFE8372E))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFE8372E))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFE8372E))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFCCA832))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFFFD23F))
        }
        clipPath(p3) {
            drawRoundRect(Color(0xFFFFB224), Offset(-0.434f * s, 0.18f * s), Size(0.869f * s, 0.174f * s), CornerRadius(0.0f * s))
        }
        drawPath(p5, Color(0x8CFFFFFF))
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFE8372E))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF2F8DEB))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF2F8DEB))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artTrompette(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.19f * s, 0.101f * s); lineTo(-0.031f * s, 0.037f * s); cubicTo(0.013f * s, 0.019f * s, 0.063f * s, 0.04f * s, 0.081f * s, 0.084f * s); lineTo(0.087f * s, 0.1f * s); cubicTo(0.105f * s, 0.144f * s, 0.084f * s, 0.194f * s, 0.04f * s, 0.211f * s); lineTo(-0.119f * s, 0.276f * s); cubicTo(-0.163f * s, 0.293f * s, -0.213f * s, 0.272f * s, -0.231f * s, 0.228f * s); lineTo(-0.237f * s, 0.212f * s); cubicTo(-0.255f * s, 0.169f * s, -0.233f * s, 0.119f * s, -0.19f * s, 0.101f * s); close() }
    val p1 = Path().apply { moveTo(-0.317f * s, 0.152f * s); lineTo(0.08f * s, -0.008f * s) }
    val p2 = Path().apply { moveTo(-0.349f * s, 0.119f * s); cubicTo(-0.327f * s, 0.11f * s, -0.302f * s, 0.121f * s, -0.293f * s, 0.143f * s); cubicTo(-0.284f * s, 0.165f * s, -0.295f * s, 0.189f * s, -0.316f * s, 0.198f * s); lineTo(-0.38f * s, 0.224f * s); cubicTo(-0.402f * s, 0.233f * s, -0.427f * s, 0.222f * s, -0.436f * s, 0.2f * s); cubicTo(-0.445f * s, 0.178f * s, -0.434f * s, 0.154f * s, -0.412f * s, 0.145f * s); close() }
    val p3 = Path().apply { moveTo(-0.24f * s, -0.082f * s); lineTo(-0.232f * s, -0.085f * s); cubicTo(-0.221f * s, -0.089f * s, -0.209f * s, -0.084f * s, -0.204f * s, -0.073f * s); lineTo(-0.156f * s, 0.046f * s); cubicTo(-0.152f * s, 0.057f * s, -0.157f * s, 0.069f * s, -0.168f * s, 0.074f * s); lineTo(-0.176f * s, 0.077f * s); cubicTo(-0.187f * s, 0.081f * s, -0.2f * s, 0.076f * s, -0.204f * s, 0.065f * s); lineTo(-0.252f * s, -0.054f * s); cubicTo(-0.257f * s, -0.065f * s, -0.251f * s, -0.077f * s, -0.24f * s, -0.082f * s); close() }
    val p4 = Path().apply { moveTo(-0.204f * s, -0.106f * s); cubicTo(-0.198f * s, -0.093f * s, -0.21f * s, -0.075f * s, -0.23f * s, -0.067f * s); cubicTo(-0.25f * s, -0.059f * s, -0.27f * s, -0.064f * s, -0.275f * s, -0.077f * s); cubicTo(-0.281f * s, -0.09f * s, -0.269f * s, -0.107f * s, -0.249f * s, -0.115f * s); cubicTo(-0.229f * s, -0.123f * s, -0.209f * s, -0.119f * s, -0.204f * s, -0.106f * s); close() }
    val p5 = Path().apply { moveTo(-0.137f * s, -0.123f * s); lineTo(-0.129f * s, -0.127f * s); cubicTo(-0.118f * s, -0.131f * s, -0.106f * s, -0.126f * s, -0.101f * s, -0.115f * s); lineTo(-0.053f * s, 0.004f * s); cubicTo(-0.049f * s, 0.015f * s, -0.054f * s, 0.028f * s, -0.065f * s, 0.032f * s); lineTo(-0.073f * s, 0.035f * s); cubicTo(-0.084f * s, 0.04f * s, -0.096f * s, 0.035f * s, -0.101f * s, 0.024f * s); lineTo(-0.149f * s, -0.096f * s); cubicTo(-0.153f * s, -0.106f * s, -0.148f * s, -0.119f * s, -0.137f * s, -0.123f * s); close() }
    val p6 = Path().apply { moveTo(-0.101f * s, -0.147f * s); cubicTo(-0.095f * s, -0.134f * s, -0.107f * s, -0.117f * s, -0.127f * s, -0.109f * s); cubicTo(-0.147f * s, -0.101f * s, -0.167f * s, -0.105f * s, -0.172f * s, -0.119f * s); cubicTo(-0.177f * s, -0.132f * s, -0.166f * s, -0.149f * s, -0.146f * s, -0.157f * s); cubicTo(-0.126f * s, -0.165f * s, -0.106f * s, -0.161f * s, -0.101f * s, -0.147f * s); close() }
    val p7 = Path().apply { moveTo(-0.034f * s, -0.165f * s); lineTo(-0.026f * s, -0.168f * s); cubicTo(-0.015f * s, -0.173f * s, -0.003f * s, -0.167f * s, 0.002f * s, -0.156f * s); lineTo(0.05f * s, -0.037f * s); cubicTo(0.054f * s, -0.026f * s, 0.049f * s, -0.014f * s, 0.038f * s, -0.01f * s); lineTo(0.03f * s, -0.006f * s); cubicTo(0.019f * s, -0.002f * s, 0.007f * s, -0.007f * s, 0.002f * s, -0.018f * s); lineTo(-0.046f * s, -0.137f * s); cubicTo(-0.05f * s, -0.148f * s, -0.045f * s, -0.161f * s, -0.034f * s, -0.165f * s); close() }
    val p8 = Path().apply { moveTo(0.003f * s, -0.189f * s); cubicTo(0.008f * s, -0.176f * s, -0.004f * s, -0.159f * s, -0.024f * s, -0.151f * s); cubicTo(-0.043f * s, -0.143f * s, -0.064f * s, -0.147f * s, -0.069f * s, -0.16f * s); cubicTo(-0.074f * s, -0.173f * s, -0.063f * s, -0.19f * s, -0.043f * s, -0.198f * s); cubicTo(-0.023f * s, -0.206f * s, -0.003f * s, -0.202f * s, 0.003f * s, -0.189f * s); close() }
    val p9 = Path().apply { moveTo(0.032f * s, -0.035f * s); cubicTo(0.125f * s, -0.081f * s, 0.15f * s, -0.202f * s, 0.197f * s, -0.314f * s); quadraticTo(0.276f * s, -0.346f * s, 0.308f * s, -0.266f * s); lineTo(0.427f * s, 0.027f * s); quadraticTo(0.456f * s, 0.099f * s, 0.376f * s, 0.131f * s); cubicTo(0.259f * s, 0.068f * s, 0.163f * s, 0.014f * s, 0.065f * s, 0.044f * s); close() }
    val p10 = Path().apply { moveTo(0.004f * s, -0.06f * s); cubicTo(0.096f * s, -0.107f * s, 0.121f * s, -0.228f * s, 0.168f * s, -0.339f * s); quadraticTo(0.248f * s, -0.371f * s, 0.28f * s, -0.292f * s); lineTo(0.398f * s, 0.002f * s); quadraticTo(0.427f * s, 0.073f * s, 0.348f * s, 0.105f * s); cubicTo(0.23f * s, 0.042f * s, 0.134f * s, -0.011f * s, 0.036f * s, 0.019f * s); close() }
    val p11 = Path().apply { moveTo(0.215f * s, -0.082f * s); cubicTo(0.212f * s, -0.035f * s, 0.203f * s, 0.003f * s, 0.193f * s, 0.002f * s); cubicTo(0.184f * s, 0.002f * s, 0.178f * s, -0.037f * s, 0.181f * s, -0.084f * s); cubicTo(0.183f * s, -0.131f * s, 0.193f * s, -0.169f * s, 0.202f * s, -0.169f * s); cubicTo(0.212f * s, -0.168f * s, 0.217f * s, -0.13f * s, 0.215f * s, -0.082f * s); close() }
    val p12 = Path().apply { moveTo(0.36f * s, -0.139f * s); cubicTo(0.399f * s, -0.043f * s, 0.418f * s, 0.04f * s, 0.402f * s, 0.046f * s); cubicTo(0.387f * s, 0.053f * s, 0.343f * s, -0.021f * s, 0.304f * s, -0.117f * s); cubicTo(0.265f * s, -0.213f * s, 0.246f * s, -0.297f * s, 0.261f * s, -0.303f * s); cubicTo(0.277f * s, -0.309f * s, 0.321f * s, -0.236f * s, 0.36f * s, -0.139f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.132f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFFFFD23F), style = Stroke(0.06f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.142f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFFFD23F), style = Stroke(0.07f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFF5B800))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFF5B800))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFFF8A1F))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFF5B800))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFFF8A1F))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFF5B800))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFFF8A1F))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFCCA832))
    if (sil == null) {
        clipPath(p9) {
            drawPath(p10, Color(0xFFFFD23F))
        }
        drawPath(p11, Color(0x8CFFFFFF))
    }
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFFFFF0A8))
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artBrocoli(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.018f * s, 0.088f * s); lineTo(-0.184f * s, -0.078f * s) }
    val p1 = Path().apply { moveTo(0.018f * s, 0.088f * s); lineTo(0.184f * s, -0.078f * s) }
    val p2 = Path().apply { moveTo(-0.103f * s, -0.042f * s); quadraticTo(-0.101f * s, -0.051f * s, -0.092f * s, -0.051f * s); lineTo(0.092f * s, -0.051f * s); quadraticTo(0.101f * s, -0.051f * s, 0.103f * s, -0.042f * s); lineTo(0.157f * s, 0.356f * s); quadraticTo(0.166f * s, 0.42f * s, 0.101f * s, 0.42f * s); lineTo(-0.101f * s, 0.42f * s); quadraticTo(-0.166f * s, 0.42f * s, -0.157f * s, 0.356f * s); close() }
    val p3 = Path().apply { moveTo(-0.126f * s, -0.069f * s); quadraticTo(-0.124f * s, -0.078f * s, -0.115f * s, -0.078f * s); lineTo(0.069f * s, -0.078f * s); quadraticTo(0.078f * s, -0.078f * s, 0.08f * s, -0.069f * s); lineTo(0.134f * s, 0.328f * s); quadraticTo(0.143f * s, 0.392f * s, 0.078f * s, 0.392f * s); lineTo(-0.124f * s, 0.392f * s); quadraticTo(-0.189f * s, 0.392f * s, -0.18f * s, 0.328f * s); close() }
    val p4 = Path().apply { moveTo(0.231f * s, -0.189f * s); cubicTo(0.231f * s, -0.062f * s, 0.127f * s, 0.042f * s, 0.0f * s, 0.042f * s); cubicTo(-0.127f * s, 0.042f * s, -0.23f * s, -0.062f * s, -0.23f * s, -0.189f * s); cubicTo(-0.23f * s, -0.316f * s, -0.127f * s, -0.42f * s, 0.0f * s, -0.42f * s); cubicTo(0.127f * s, -0.42f * s, 0.231f * s, -0.316f * s, 0.231f * s, -0.189f * s); close() }
    val p5 = Path().apply { moveTo(0.212f * s, -0.23f * s); cubicTo(0.212f * s, -0.103f * s, 0.109f * s, 0.0f * s, -0.018f * s, 0.0f * s); cubicTo(-0.146f * s, 0.0f * s, -0.249f * s, -0.103f * s, -0.249f * s, -0.23f * s); cubicTo(-0.249f * s, -0.358f * s, -0.146f * s, -0.461f * s, -0.018f * s, -0.461f * s); cubicTo(0.109f * s, -0.461f * s, 0.212f * s, -0.358f * s, 0.212f * s, -0.23f * s); close() }
    val p6 = Path().apply { moveTo(-0.054f * s, -0.308f * s); cubicTo(-0.034f * s, -0.278f * s, -0.034f * s, -0.243f * s, -0.055f * s, -0.228f * s); cubicTo(-0.076f * s, -0.214f * s, -0.109f * s, -0.226f * s, -0.13f * s, -0.255f * s); cubicTo(-0.15f * s, -0.284f * s, -0.15f * s, -0.319f * s, -0.129f * s, -0.334f * s); cubicTo(-0.108f * s, -0.349f * s, -0.075f * s, -0.337f * s, -0.054f * s, -0.308f * s); close() }
    val p7 = Path().apply { moveTo(-0.064f * s, -0.06f * s); cubicTo(-0.064f * s, 0.042f * s, -0.147f * s, 0.125f * s, -0.249f * s, 0.125f * s); cubicTo(-0.351f * s, 0.125f * s, -0.433f * s, 0.042f * s, -0.433f * s, -0.06f * s); cubicTo(-0.433f * s, -0.162f * s, -0.351f * s, -0.244f * s, -0.249f * s, -0.244f * s); cubicTo(-0.147f * s, -0.244f * s, -0.064f * s, -0.162f * s, -0.064f * s, -0.06f * s); close() }
    val p8 = Path().apply { moveTo(-0.083f * s, -0.101f * s); cubicTo(-0.083f * s, 0.001f * s, -0.165f * s, 0.083f * s, -0.267f * s, 0.083f * s); cubicTo(-0.369f * s, 0.083f * s, -0.452f * s, 0.001f * s, -0.452f * s, -0.101f * s); cubicTo(-0.452f * s, -0.203f * s, -0.369f * s, -0.286f * s, -0.267f * s, -0.286f * s); cubicTo(-0.165f * s, -0.286f * s, -0.083f * s, -0.203f * s, -0.083f * s, -0.101f * s); close() }
    val p9 = Path().apply { moveTo(0.433f * s, -0.06f * s); cubicTo(0.433f * s, 0.042f * s, 0.351f * s, 0.125f * s, 0.249f * s, 0.125f * s); cubicTo(0.147f * s, 0.125f * s, 0.065f * s, 0.042f * s, 0.065f * s, -0.06f * s); cubicTo(0.065f * s, -0.162f * s, 0.147f * s, -0.244f * s, 0.249f * s, -0.244f * s); cubicTo(0.351f * s, -0.244f * s, 0.433f * s, -0.162f * s, 0.433f * s, -0.06f * s); close() }
    val p10 = Path().apply { moveTo(0.415f * s, -0.101f * s); cubicTo(0.415f * s, 0.001f * s, 0.332f * s, 0.083f * s, 0.231f * s, 0.083f * s); cubicTo(0.129f * s, 0.083f * s, 0.046f * s, 0.001f * s, 0.046f * s, -0.101f * s); cubicTo(0.046f * s, -0.203f * s, 0.129f * s, -0.286f * s, 0.231f * s, -0.286f * s); cubicTo(0.332f * s, -0.286f * s, 0.415f * s, -0.203f * s, 0.415f * s, -0.101f * s); close() }
    val p11 = Path().apply { moveTo(0.065f * s, -0.005f * s); cubicTo(0.065f * s, 0.082f * s, -0.006f * s, 0.152f * s, -0.092f * s, 0.152f * s); cubicTo(-0.179f * s, 0.152f * s, -0.249f * s, 0.082f * s, -0.249f * s, -0.005f * s); cubicTo(-0.249f * s, -0.091f * s, -0.179f * s, -0.161f * s, -0.092f * s, -0.161f * s); cubicTo(-0.006f * s, -0.161f * s, 0.065f * s, -0.091f * s, 0.065f * s, -0.005f * s); close() }
    val p12 = Path().apply { moveTo(0.046f * s, -0.046f * s); cubicTo(0.046f * s, 0.041f * s, -0.024f * s, 0.111f * s, -0.111f * s, 0.111f * s); cubicTo(-0.197f * s, 0.111f * s, -0.267f * s, 0.041f * s, -0.267f * s, -0.046f * s); cubicTo(-0.267f * s, -0.133f * s, -0.197f * s, -0.203f * s, -0.111f * s, -0.203f * s); cubicTo(-0.024f * s, -0.203f * s, 0.046f * s, -0.133f * s, 0.046f * s, -0.046f * s); close() }
    val p13 = Path().apply { moveTo(0.267f * s, -0.005f * s); cubicTo(0.267f * s, 0.082f * s, 0.197f * s, 0.152f * s, 0.111f * s, 0.152f * s); cubicTo(0.024f * s, 0.152f * s, -0.046f * s, 0.082f * s, -0.046f * s, -0.005f * s); cubicTo(-0.046f * s, -0.091f * s, 0.024f * s, -0.161f * s, 0.111f * s, -0.161f * s); cubicTo(0.197f * s, -0.161f * s, 0.267f * s, -0.091f * s, 0.267f * s, -0.005f * s); close() }
    val p14 = Path().apply { moveTo(0.249f * s, -0.046f * s); cubicTo(0.249f * s, 0.041f * s, 0.179f * s, 0.111f * s, 0.092f * s, 0.111f * s); cubicTo(0.006f * s, 0.111f * s, -0.064f * s, 0.041f * s, -0.064f * s, -0.046f * s); cubicTo(-0.064f * s, -0.133f * s, 0.006f * s, -0.203f * s, 0.092f * s, -0.203f * s); cubicTo(0.179f * s, -0.203f * s, 0.249f * s, -0.133f * s, 0.249f * s, -0.046f * s); close() }
    val p15 = Path().apply { moveTo(0.07f * s, -0.064f * s); cubicTo(0.08f * s, -0.047f * s, 0.078f * s, -0.026f * s, 0.065f * s, -0.019f * s); cubicTo(0.051f * s, -0.011f * s, 0.032f * s, -0.019f * s, 0.022f * s, -0.037f * s); cubicTo(0.012f * s, -0.054f * s, 0.015f * s, -0.075f * s, 0.028f * s, -0.083f * s); cubicTo(0.041f * s, -0.09f * s, 0.06f * s, -0.082f * s, 0.07f * s, -0.064f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.152f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.152f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFFA8D86B), style = Stroke(0.08f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFA8D86B), style = Stroke(0.08f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF8FB85B))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFA8D86B))
        }
        drawOval(Color(0x8CFFFFFF), Offset(-0.083f * s, 0.106f * s), Size(0.037f * s, 0.184f * s))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF26843B))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFF2FA54A))
        }
        drawPath(p6, Color(0x8CFFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF26843B))
    if (sil == null) {
        clipPath(p7) {
            drawPath(p8, Color(0xFF2FA54A))
        }
    }
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFF26843B))
    if (sil == null) {
        clipPath(p9) {
            drawPath(p10, Color(0xFF2FA54A))
        }
    }
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF26843B))
    if (sil == null) {
        clipPath(p11) {
            drawPath(p12, Color(0xFF2FA54A))
        }
    }
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p13, sil ?: Color(0xFF26843B))
    if (sil == null) {
        clipPath(p13) {
            drawPath(p14, Color(0xFF2FA54A))
        }
        drawPath(p15, Color(0x8CFFFFFF))
    }
    drawPath(p13, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artPasteque(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.433f * s, -0.06f * s); cubicTo(-0.375f * s, 0.176f * s, -0.136f * s, 0.319f * s, 0.1f * s, 0.26f * s); cubicTo(0.336f * s, 0.202f * s, 0.48f * s, -0.037f * s, 0.421f * s, -0.273f * s); close() }
    val p1 = Path().apply { moveTo(-0.46f * s, -0.091f * s); cubicTo(-0.401f * s, 0.145f * s, -0.162f * s, 0.288f * s, 0.073f * s, 0.229f * s); cubicTo(0.309f * s, 0.171f * s, 0.453f * s, -0.068f * s, 0.394f * s, -0.304f * s); close() }
    val p2 = Path().apply { moveTo(-0.362f * s, -0.078f * s); cubicTo(-0.313f * s, 0.119f * s, -0.114f * s, 0.238f * s, 0.082f * s, 0.189f * s); cubicTo(0.279f * s, 0.14f * s, 0.399f * s, -0.059f * s, 0.35f * s, -0.255f * s); close() }
    val p3 = Path().apply { moveTo(-0.318f * s, -0.089f * s); cubicTo(-0.275f * s, 0.083f * s, -0.101f * s, 0.188f * s, 0.071f * s, 0.145f * s); cubicTo(0.243f * s, 0.102f * s, 0.348f * s, -0.072f * s, 0.305f * s, -0.244f * s); close() }
    val p4 = Path().apply { moveTo(-0.101f * s, 0.037f * s); cubicTo(-0.087f * s, 0.057f * s, -0.086f * s, 0.08f * s, -0.098f * s, 0.088f * s); cubicTo(-0.109f * s, 0.096f * s, -0.13f * s, 0.086f * s, -0.143f * s, 0.066f * s); cubicTo(-0.157f * s, 0.046f * s, -0.159f * s, 0.023f * s, -0.147f * s, 0.015f * s); cubicTo(-0.135f * s, 0.007f * s, -0.115f * s, 0.017f * s, -0.101f * s, 0.037f * s); close() }
    val p5 = Path().apply { moveTo(0.078f * s, 0.067f * s); cubicTo(0.084f * s, 0.091f * s, 0.078f * s, 0.113f * s, 0.064f * s, 0.116f * s); cubicTo(0.05f * s, 0.12f * s, 0.035f * s, 0.103f * s, 0.029f * s, 0.08f * s); cubicTo(0.023f * s, 0.056f * s, 0.029f * s, 0.034f * s, 0.043f * s, 0.031f * s); cubicTo(0.057f * s, 0.027f * s, 0.073f * s, 0.044f * s, 0.078f * s, 0.067f * s); close() }
    val p6 = Path().apply { moveTo(0.224f * s, -0.026f * s); cubicTo(0.221f * s, -0.002f * s, 0.208f * s, 0.017f * s, 0.194f * s, 0.015f * s); cubicTo(0.179f * s, 0.014f * s, 0.17f * s, -0.007f * s, 0.173f * s, -0.031f * s); cubicTo(0.175f * s, -0.056f * s, 0.189f * s, -0.074f * s, 0.203f * s, -0.072f * s); cubicTo(0.217f * s, -0.071f * s, 0.226f * s, -0.05f * s, 0.224f * s, -0.026f * s); close() }
    val p7 = Path().apply { moveTo(0.024f * s, 0.19f * s); cubicTo(0.033f * s, 0.212f * s, 0.031f * s, 0.235f * s, 0.018f * s, 0.241f * s); cubicTo(0.005f * s, 0.246f * s, -0.014f * s, 0.233f * s, -0.023f * s, 0.211f * s); cubicTo(-0.033f * s, 0.189f * s, -0.031f * s, 0.166f * s, -0.018f * s, 0.16f * s); cubicTo(-0.005f * s, 0.154f * s, 0.014f * s, 0.168f * s, 0.024f * s, 0.19f * s); close() }
    val p8 = Path().apply { moveTo(0.186f * s, 0.159f * s); cubicTo(0.188f * s, 0.183f * s, 0.177f * s, 0.203f * s, 0.163f * s, 0.204f * s); cubicTo(0.149f * s, 0.205f * s, 0.136f * s, 0.186f * s, 0.135f * s, 0.162f * s); cubicTo(0.133f * s, 0.138f * s, 0.143f * s, 0.117f * s, 0.157f * s, 0.116f * s); cubicTo(0.171f * s, 0.115f * s, 0.184f * s, 0.134f * s, 0.186f * s, 0.159f * s); close() }
    val p9 = Path().apply { moveTo(-0.179f * s, 0.074f * s); cubicTo(-0.183f * s, 0.109f * s, -0.196f * s, 0.136f * s, -0.209f * s, 0.135f * s); cubicTo(-0.222f * s, 0.134f * s, -0.229f * s, 0.104f * s, -0.225f * s, 0.069f * s); cubicTo(-0.221f * s, 0.034f * s, -0.208f * s, 0.006f * s, -0.196f * s, 0.007f * s); cubicTo(-0.183f * s, 0.009f * s, -0.176f * s, 0.038f * s, -0.179f * s, 0.074f * s); close() }
    drawPath(p0, sil ?: Color(0xFF267E36))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF2F9E44))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p2, Color(0xFFDDF3B8))
        drawPath(p3, Color(0xFFF0434C))
        drawPath(p4, Color(0xFF2B2D42))
        drawPath(p5, Color(0xFF2B2D42))
        drawPath(p6, Color(0xFF2B2D42))
        drawPath(p7, Color(0xFF2B2D42))
        drawPath(p8, Color(0xFF2B2D42))
        drawPath(p9, Color(0x59FFFFFF))
    }
}

private fun DrawScope.artCrocodile(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.239f * s, -0.058f * s); quadraticTo(-0.25f * s, -0.058f * s, -0.247f * s, -0.069f * s); lineTo(-0.21f * s, -0.205f * s); quadraticTo(-0.207f * s, -0.215f * s, -0.204f * s, -0.205f * s); lineTo(-0.167f * s, -0.069f * s); quadraticTo(-0.164f * s, -0.058f * s, -0.175f * s, -0.058f * s); close() }
    val p1 = Path().apply { moveTo(-0.147f * s, -0.058f * s); quadraticTo(-0.157f * s, -0.058f * s, -0.155f * s, -0.069f * s); lineTo(-0.117f * s, -0.205f * s); quadraticTo(-0.115f * s, -0.215f * s, -0.112f * s, -0.205f * s); lineTo(-0.075f * s, -0.069f * s); quadraticTo(-0.072f * s, -0.058f * s, -0.083f * s, -0.058f * s); close() }
    val p2 = Path().apply { moveTo(-0.054f * s, -0.058f * s); quadraticTo(-0.065f * s, -0.058f * s, -0.062f * s, -0.069f * s); lineTo(-0.025f * s, -0.205f * s); quadraticTo(-0.022f * s, -0.215f * s, -0.019f * s, -0.205f * s); lineTo(0.018f * s, -0.069f * s); quadraticTo(0.021f * s, -0.058f * s, 0.01f * s, -0.058f * s); close() }
    val p3 = Path().apply { moveTo(-0.115f * s, 0.184f * s); cubicTo(-0.119f * s, 0.215f * s, -0.147f * s, 0.237f * s, -0.179f * s, 0.233f * s); cubicTo(-0.21f * s, 0.229f * s, -0.232f * s, 0.201f * s, -0.228f * s, 0.17f * s); lineTo(-0.214f * s, 0.056f * s); cubicTo(-0.21f * s, 0.024f * s, -0.181f * s, 0.002f * s, -0.15f * s, 0.006f * s); cubicTo(-0.119f * s, 0.01f * s, -0.097f * s, 0.039f * s, -0.101f * s, 0.07f * s); close() }
    val p4 = Path().apply { moveTo(0.141f * s, 0.17f * s); cubicTo(0.145f * s, 0.201f * s, 0.123f * s, 0.229f * s, 0.092f * s, 0.233f * s); cubicTo(0.061f * s, 0.237f * s, 0.032f * s, 0.215f * s, 0.028f * s, 0.184f * s); lineTo(0.014f * s, 0.07f * s); cubicTo(0.01f * s, 0.039f * s, 0.032f * s, 0.01f * s, 0.064f * s, 0.006f * s); cubicTo(0.095f * s, 0.002f * s, 0.123f * s, 0.024f * s, 0.127f * s, 0.056f * s); close() }
    val p5 = Path().apply { moveTo(0.177f * s, 0.006f * s); cubicTo(0.177f * s, 0.084f * s, 0.082f * s, 0.148f * s, -0.036f * s, 0.148f * s); cubicTo(-0.154f * s, 0.148f * s, -0.25f * s, 0.084f * s, -0.25f * s, 0.006f * s); cubicTo(-0.25f * s, -0.073f * s, -0.154f * s, -0.137f * s, -0.036f * s, -0.137f * s); cubicTo(0.082f * s, -0.137f * s, 0.177f * s, -0.073f * s, 0.177f * s, 0.006f * s); close() }
    val p6 = Path().apply { moveTo(-0.041f * s, 0.004f * s); cubicTo(-0.044f * s, -0.034f * s, -0.132f * s, -0.084f * s, -0.153f * s, -0.101f * s); cubicTo(-0.173f * s, -0.117f * s, -0.16f * s, -0.098f * s, -0.164f * s, -0.097f * s); cubicTo(-0.168f * s, -0.095f * s, -0.174f * s, -0.093f * s, -0.178f * s, -0.091f * s); cubicTo(-0.183f * s, -0.09f * s, -0.188f * s, -0.087f * s, -0.193f * s, -0.085f * s); cubicTo(-0.198f * s, -0.083f * s, -0.203f * s, -0.081f * s, -0.208f * s, -0.079f * s); cubicTo(-0.213f * s, -0.076f * s, -0.218f * s, -0.074f * s, -0.224f * s, -0.072f * s); cubicTo(-0.229f * s, -0.07f * s, -0.234f * s, -0.067f * s, -0.238f * s, -0.065f * s); cubicTo(-0.243f * s, -0.063f * s, -0.248f * s, -0.061f * s, -0.252f * s, -0.059f * s); cubicTo(-0.256f * s, -0.058f * s, -0.26f * s, -0.056f * s, -0.264f * s, -0.054f * s); cubicTo(-0.267f * s, -0.053f * s, -0.269f * s, -0.051f * s, -0.272f * s, -0.05f * s); cubicTo(-0.275f * s, -0.049f * s, -0.278f * s, -0.048f * s, -0.281f * s, -0.047f * s); cubicTo(-0.285f * s, -0.046f * s, -0.289f * s, -0.045f * s, -0.294f * s, -0.045f * s); cubicTo(-0.299f * s, -0.045f * s, -0.304f * s, -0.045f * s, -0.309f * s, -0.045f * s); cubicTo(-0.315f * s, -0.045f * s, -0.32f * s, -0.045f * s, -0.326f * s, -0.046f * s); cubicTo(-0.331f * s, -0.046f * s, -0.337f * s, -0.047f * s, -0.343f * s, -0.048f * s); cubicTo(-0.348f * s, -0.048f * s, -0.354f * s, -0.049f * s, -0.359f * s, -0.05f * s); cubicTo(-0.364f * s, -0.051f * s, -0.369f * s, -0.052f * s, -0.374f * s, -0.052f * s); cubicTo(-0.378f * s, -0.053f * s, -0.383f * s, -0.054f * s, -0.387f * s, -0.054f * s); cubicTo(-0.39f * s, -0.055f * s, -0.39f * s, -0.057f * s, -0.397f * s, -0.056f * s); cubicTo(-0.403f * s, -0.054f * s, -0.42f * s, -0.052f * s, -0.424f * s, -0.046f * s); cubicTo(-0.427f * s, -0.04f * s, -0.419f * s, -0.024f * s, -0.416f * s, -0.018f * s); cubicTo(-0.414f * s, -0.012f * s, -0.413f * s, -0.014f * s, -0.41f * s, -0.011f * s); cubicTo(-0.408f * s, -0.008f * s, -0.406f * s, -0.005f * s, -0.403f * s, -0.002f * s); cubicTo(-0.4f * s, 0.002f * s, -0.397f * s, 0.006f * s, -0.393f * s, 0.01f * s); cubicTo(-0.39f * s, 0.014f * s, -0.386f * s, 0.019f * s, -0.382f * s, 0.023f * s); cubicTo(-0.378f * s, 0.028f * s, -0.373f * s, 0.033f * s, -0.369f * s, 0.038f * s); cubicTo(-0.364f * s, 0.042f * s, -0.36f * s, 0.047f * s, -0.354f * s, 0.052f * s); cubicTo(-0.349f * s, 0.057f * s, -0.344f * s, 0.061f * s, -0.338f * s, 0.066f * s); cubicTo(-0.332f * s, 0.07f * s, -0.326f * s, 0.075f * s, -0.32f * s, 0.079f * s); cubicTo(-0.313f * s, 0.083f * s, -0.306f * s, 0.087f * s, -0.299f * s, 0.09f * s); cubicTo(-0.291f * s, 0.093f * s, -0.283f * s, 0.096f * s, -0.276f * s, 0.098f * s); cubicTo(-0.268f * s, 0.101f * s, -0.26f * s, 0.103f * s, -0.253f * s, 0.105f * s); cubicTo(-0.245f * s, 0.106f * s, -0.238f * s, 0.108f * s, -0.23f * s, 0.11f * s); cubicTo(-0.223f * s, 0.111f * s, -0.215f * s, 0.112f * s, -0.208f * s, 0.114f * s); cubicTo(-0.201f * s, 0.115f * s, -0.194f * s, 0.116f * s, -0.187f * s, 0.117f * s); cubicTo(-0.181f * s, 0.118f * s, -0.174f * s, 0.119f * s, -0.168f * s, 0.119f * s); cubicTo(-0.163f * s, 0.12f * s, -0.157f * s, 0.121f * s, -0.153f * s, 0.122f * s); cubicTo(-0.148f * s, 0.123f * s, -0.144f * s, 0.123f * s, -0.141f * s, 0.124f * s); cubicTo(-0.138f * s, 0.125f * s, -0.15f * s, 0.146f * s, -0.133f * s, 0.126f * s); cubicTo(-0.117f * s, 0.106f * s, -0.038f * s, 0.042f * s, -0.041f * s, 0.004f * s); close() }
    val p7 = Path().apply { moveTo(-0.011f * s, -0.049f * s); cubicTo(-0.017f * s, -0.006f * s, 0.046f * s, 0.067f * s, 0.061f * s, 0.09f * s); cubicTo(0.076f * s, 0.113f * s, 0.073f * s, 0.09f * s, 0.081f * s, 0.089f * s); cubicTo(0.088f * s, 0.089f * s, 0.098f * s, 0.089f * s, 0.108f * s, 0.089f * s); cubicTo(0.118f * s, 0.088f * s, 0.13f * s, 0.088f * s, 0.141f * s, 0.088f * s); cubicTo(0.152f * s, 0.087f * s, 0.164f * s, 0.087f * s, 0.175f * s, 0.086f * s); cubicTo(0.187f * s, 0.086f * s, 0.199f * s, 0.086f * s, 0.209f * s, 0.085f * s); cubicTo(0.22f * s, 0.085f * s, 0.229f * s, 0.084f * s, 0.239f * s, 0.084f * s); cubicTo(0.248f * s, 0.083f * s, 0.257f * s, 0.082f * s, 0.266f * s, 0.082f * s); cubicTo(0.275f * s, 0.081f * s, 0.283f * s, 0.08f * s, 0.292f * s, 0.079f * s); cubicTo(0.3f * s, 0.078f * s, 0.308f * s, 0.077f * s, 0.316f * s, 0.076f * s); cubicTo(0.323f * s, 0.076f * s, 0.33f * s, 0.075f * s, 0.336f * s, 0.074f * s); cubicTo(0.343f * s, 0.073f * s, 0.349f * s, 0.072f * s, 0.353f * s, 0.071f * s); cubicTo(0.358f * s, 0.071f * s, 0.354f * s, 0.083f * s, 0.366f * s, 0.07f * s); cubicTo(0.378f * s, 0.057f * s, 0.423f * s, 0.02f * s, 0.424f * s, -0.006f * s); cubicTo(0.426f * s, -0.032f * s, 0.384f * s, -0.073f * s, 0.373f * s, -0.087f * s); cubicTo(0.363f * s, -0.101f * s, 0.366f * s, -0.088f * s, 0.361f * s, -0.09f * s); cubicTo(0.356f * s, -0.091f * s, 0.35f * s, -0.092f * s, 0.344f * s, -0.093f * s); cubicTo(0.338f * s, -0.095f * s, 0.331f * s, -0.096f * s, 0.324f * s, -0.098f * s); cubicTo(0.317f * s, -0.099f * s, 0.31f * s, -0.101f * s, 0.303f * s, -0.103f * s); cubicTo(0.295f * s, -0.105f * s, 0.288f * s, -0.107f * s, 0.28f * s, -0.109f * s); cubicTo(0.273f * s, -0.111f * s, 0.266f * s, -0.113f * s, 0.258f * s, -0.115f * s); cubicTo(0.25f * s, -0.117f * s, 0.242f * s, -0.12f * s, 0.233f * s, -0.122f * s); cubicTo(0.224f * s, -0.125f * s, 0.213f * s, -0.128f * s, 0.203f * s, -0.131f * s); cubicTo(0.192f * s, -0.134f * s, 0.181f * s, -0.138f * s, 0.17f * s, -0.141f * s); cubicTo(0.16f * s, -0.144f * s, 0.149f * s, -0.147f * s, 0.14f * s, -0.15f * s); cubicTo(0.13f * s, -0.153f * s, 0.121f * s, -0.156f * s, 0.113f * s, -0.158f * s); cubicTo(0.106f * s, -0.161f * s, 0.115f * s, -0.182f * s, 0.094f * s, -0.164f * s); cubicTo(0.073f * s, -0.146f * s, -0.006f * s, -0.091f * s, -0.011f * s, -0.049f * s); close() }
    val p8 = Path().apply { moveTo(0.185f * s, -0.144f * s); cubicTo(0.185f * s, -0.102f * s, 0.149f * s, -0.069f * s, 0.106f * s, -0.069f * s); cubicTo(0.063f * s, -0.069f * s, 0.028f * s, -0.102f * s, 0.028f * s, -0.144f * s); cubicTo(0.028f * s, -0.185f * s, 0.063f * s, -0.219f * s, 0.106f * s, -0.219f * s); cubicTo(0.149f * s, -0.219f * s, 0.185f * s, -0.185f * s, 0.185f * s, -0.144f * s); close() }
    val p9 = Path().apply { moveTo(0.402f * s, -0.094f * s); cubicTo(0.402f * s, -0.078f * s, 0.387f * s, -0.065f * s, 0.37f * s, -0.065f * s); cubicTo(0.352f * s, -0.065f * s, 0.338f * s, -0.078f * s, 0.338f * s, -0.094f * s); cubicTo(0.338f * s, -0.11f * s, 0.352f * s, -0.122f * s, 0.37f * s, -0.122f * s); cubicTo(0.387f * s, -0.122f * s, 0.402f * s, -0.11f * s, 0.402f * s, -0.094f * s); close() }
    val p10 = Path().apply { moveTo(0.16f * s, -0.03f * s); cubicTo(0.16f * s, 0.049f * s, 0.064f * s, 0.113f * s, -0.054f * s, 0.113f * s); cubicTo(-0.172f * s, 0.113f * s, -0.268f * s, 0.049f * s, -0.268f * s, -0.03f * s); cubicTo(-0.268f * s, -0.109f * s, -0.172f * s, -0.172f * s, -0.054f * s, -0.172f * s); cubicTo(0.064f * s, -0.172f * s, 0.16f * s, -0.109f * s, 0.16f * s, -0.03f * s); close() }
    val p11 = Path().apply { moveTo(-0.059f * s, -0.031f * s); cubicTo(-0.062f * s, -0.069f * s, -0.15f * s, -0.119f * s, -0.171f * s, -0.136f * s); cubicTo(-0.191f * s, -0.153f * s, -0.178f * s, -0.134f * s, -0.182f * s, -0.132f * s); cubicTo(-0.186f * s, -0.131f * s, -0.191f * s, -0.129f * s, -0.196f * s, -0.127f * s); cubicTo(-0.201f * s, -0.125f * s, -0.206f * s, -0.123f * s, -0.211f * s, -0.121f * s); cubicTo(-0.216f * s, -0.119f * s, -0.221f * s, -0.117f * s, -0.226f * s, -0.114f * s); cubicTo(-0.231f * s, -0.112f * s, -0.236f * s, -0.11f * s, -0.241f * s, -0.107f * s); cubicTo(-0.246f * s, -0.105f * s, -0.251f * s, -0.103f * s, -0.256f * s, -0.101f * s); cubicTo(-0.261f * s, -0.099f * s, -0.266f * s, -0.097f * s, -0.27f * s, -0.095f * s); cubicTo(-0.274f * s, -0.093f * s, -0.278f * s, -0.091f * s, -0.282f * s, -0.09f * s); cubicTo(-0.285f * s, -0.088f * s, -0.287f * s, -0.087f * s, -0.29f * s, -0.086f * s); cubicTo(-0.293f * s, -0.084f * s, -0.295f * s, -0.083f * s, -0.299f * s, -0.082f * s); cubicTo(-0.303f * s, -0.081f * s, -0.307f * s, -0.081f * s, -0.312f * s, -0.08f * s); cubicTo(-0.317f * s, -0.08f * s, -0.322f * s, -0.08f * s, -0.327f * s, -0.08f * s); cubicTo(-0.332f * s, -0.08f * s, -0.338f * s, -0.081f * s, -0.344f * s, -0.081f * s); cubicTo(-0.349f * s, -0.082f * s, -0.355f * s, -0.082f * s, -0.36f * s, -0.083f * s); cubicTo(-0.366f * s, -0.084f * s, -0.371f * s, -0.085f * s, -0.377f * s, -0.085f * s); cubicTo(-0.382f * s, -0.086f * s, -0.387f * s, -0.087f * s, -0.391f * s, -0.088f * s); cubicTo(-0.396f * s, -0.089f * s, -0.401f * s, -0.09f * s, -0.404f * s, -0.09f * s); cubicTo(-0.408f * s, -0.091f * s, -0.408f * s, -0.093f * s, -0.414f * s, -0.092f * s); cubicTo(-0.421f * s, -0.09f * s, -0.438f * s, -0.088f * s, -0.441f * s, -0.081f * s); cubicTo(-0.445f * s, -0.075f * s, -0.436f * s, -0.059f * s, -0.434f * s, -0.054f * s); cubicTo(-0.432f * s, -0.048f * s, -0.43f * s, -0.049f * s, -0.428f * s, -0.047f * s); cubicTo(-0.426f * s, -0.044f * s, -0.423f * s, -0.041f * s, -0.421f * s, -0.037f * s); cubicTo(-0.418f * s, -0.034f * s, -0.414f * s, -0.03f * s, -0.411f * s, -0.025f * s); cubicTo(-0.407f * s, -0.021f * s, -0.404f * s, -0.017f * s, -0.4f * s, -0.012f * s); cubicTo(-0.396f * s, -0.008f * s, -0.391f * s, -0.003f * s, -0.387f * s, 0.002f * s); cubicTo(-0.382f * s, 0.007f * s, -0.377f * s, 0.012f * s, -0.372f * s, 0.016f * s); cubicTo(-0.367f * s, 0.021f * s, -0.362f * s, 0.026f * s, -0.356f * s, 0.03f * s); cubicTo(-0.35f * s, 0.035f * s, -0.344f * s, 0.039f * s, -0.338f * s, 0.043f * s); cubicTo(-0.331f * s, 0.047f * s, -0.324f * s, 0.051f * s, -0.317f * s, 0.054f * s); cubicTo(-0.309f * s, 0.058f * s, -0.301f * s, 0.06f * s, -0.294f * s, 0.063f * s); cubicTo(-0.286f * s, 0.065f * s, -0.278f * s, 0.067f * s, -0.271f * s, 0.069f * s); cubicTo(-0.263f * s, 0.071f * s, -0.255f * s, 0.072f * s, -0.248f * s, 0.074f * s); cubicTo(-0.24f * s, 0.075f * s, -0.233f * s, 0.077f * s, -0.226f * s, 0.078f * s); cubicTo(-0.219f * s, 0.079f * s, -0.212f * s, 0.08f * s, -0.205f * s, 0.081f * s); cubicTo(-0.199f * s, 0.082f * s, -0.192f * s, 0.083f * s, -0.186f * s, 0.084f * s); cubicTo(-0.181f * s, 0.085f * s, -0.175f * s, 0.085f * s, -0.17f * s, 0.086f * s); cubicTo(-0.166f * s, 0.087f * s, -0.162f * s, 0.088f * s, -0.159f * s, 0.089f * s); cubicTo(-0.156f * s, 0.089f * s, -0.168f * s, 0.111f * s, -0.151f * s, 0.091f * s); cubicTo(-0.134f * s, 0.071f * s, -0.055f * s, 0.006f * s, -0.059f * s, -0.031f * s); close() }
    val p12 = Path().apply { moveTo(-0.029f * s, -0.084f * s); cubicTo(-0.035f * s, -0.042f * s, 0.028f * s, 0.032f * s, 0.043f * s, 0.055f * s); cubicTo(0.059f * s, 0.078f * s, 0.055f * s, 0.054f * s, 0.063f * s, 0.054f * s); cubicTo(0.071f * s, 0.054f * s, 0.08f * s, 0.053f * s, 0.09f * s, 0.053f * s); cubicTo(0.1f * s, 0.053f * s, 0.112f * s, 0.052f * s, 0.123f * s, 0.052f * s); cubicTo(0.134f * s, 0.052f * s, 0.146f * s, 0.051f * s, 0.158f * s, 0.051f * s); cubicTo(0.169f * s, 0.05f * s, 0.181f * s, 0.05f * s, 0.191f * s, 0.049f * s); cubicTo(0.202f * s, 0.049f * s, 0.212f * s, 0.049f * s, 0.221f * s, 0.048f * s); cubicTo(0.231f * s, 0.047f * s, 0.239f * s, 0.047f * s, 0.248f * s, 0.046f * s); cubicTo(0.257f * s, 0.045f * s, 0.266f * s, 0.044f * s, 0.274f * s, 0.044f * s); cubicTo(0.282f * s, 0.043f * s, 0.29f * s, 0.042f * s, 0.298f * s, 0.041f * s); cubicTo(0.305f * s, 0.04f * s, 0.312f * s, 0.039f * s, 0.319f * s, 0.038f * s); cubicTo(0.325f * s, 0.037f * s, 0.331f * s, 0.036f * s, 0.336f * s, 0.036f * s); cubicTo(0.341f * s, 0.035f * s, 0.336f * s, 0.047f * s, 0.348f * s, 0.034f * s); cubicTo(0.36f * s, 0.021f * s, 0.405f * s, -0.015f * s, 0.407f * s, -0.041f * s); cubicTo(0.408f * s, -0.068f * s, 0.366f * s, -0.108f * s, 0.356f * s, -0.122f * s); cubicTo(0.345f * s, -0.136f * s, 0.348f * s, -0.124f * s, 0.343f * s, -0.125f * s); cubicTo(0.338f * s, -0.126f * s, 0.332f * s, -0.128f * s, 0.326f * s, -0.129f * s); cubicTo(0.32f * s, -0.13f * s, 0.313f * s, -0.132f * s, 0.306f * s, -0.133f * s); cubicTo(0.299f * s, -0.135f * s, 0.292f * s, -0.137f * s, 0.285f * s, -0.139f * s); cubicTo(0.278f * s, -0.14f * s, 0.27f * s, -0.142f * s, 0.262f * s, -0.144f * s); cubicTo(0.255f * s, -0.146f * s, 0.248f * s, -0.148f * s, 0.24f * s, -0.15f * s); cubicTo(0.233f * s, -0.153f * s, 0.224f * s, -0.155f * s, 0.215f * s, -0.158f * s); cubicTo(0.206f * s, -0.161f * s, 0.195f * s, -0.164f * s, 0.185f * s, -0.167f * s); cubicTo(0.174f * s, -0.17f * s, 0.163f * s, -0.173f * s, 0.153f * s, -0.177f * s); cubicTo(0.142f * s, -0.18f * s, 0.131f * s, -0.183f * s, 0.122f * s, -0.186f * s); cubicTo(0.112f * s, -0.189f * s, 0.103f * s, -0.192f * s, 0.095f * s, -0.194f * s); cubicTo(0.088f * s, -0.196f * s, 0.097f * s, -0.218f * s, 0.076f * s, -0.2f * s); cubicTo(0.056f * s, -0.181f * s, -0.024f * s, -0.126f * s, -0.029f * s, -0.084f * s); close() }
    val p13 = Path().apply { moveTo(0.167f * s, -0.179f * s); cubicTo(0.167f * s, -0.138f * s, 0.132f * s, -0.105f * s, 0.088f * s, -0.105f * s); cubicTo(0.045f * s, -0.105f * s, 0.01f * s, -0.138f * s, 0.01f * s, -0.179f * s); cubicTo(0.01f * s, -0.221f * s, 0.045f * s, -0.254f * s, 0.088f * s, -0.254f * s); cubicTo(0.132f * s, -0.254f * s, 0.167f * s, -0.221f * s, 0.167f * s, -0.179f * s); close() }
    val p14 = Path().apply { moveTo(0.384f * s, -0.13f * s); cubicTo(0.384f * s, -0.114f * s, 0.37f * s, -0.101f * s, 0.352f * s, -0.101f * s); cubicTo(0.334f * s, -0.101f * s, 0.32f * s, -0.114f * s, 0.32f * s, -0.13f * s); cubicTo(0.32f * s, -0.145f * s, 0.334f * s, -0.158f * s, 0.352f * s, -0.158f * s); cubicTo(0.37f * s, -0.158f * s, 0.384f * s, -0.145f * s, 0.384f * s, -0.13f * s); close() }
    val p15 = Path().apply { moveTo(-0.037f * s, -0.072f * s); cubicTo(-0.036f * s, -0.062f * s, -0.067f * s, -0.051f * s, -0.106f * s, -0.048f * s); cubicTo(-0.145f * s, -0.044f * s, -0.178f * s, -0.049f * s, -0.178f * s, -0.059f * s); cubicTo(-0.179f * s, -0.069f * s, -0.148f * s, -0.08f * s, -0.109f * s, -0.083f * s); cubicTo(-0.07f * s, -0.087f * s, -0.037f * s, -0.081f * s, -0.037f * s, -0.072f * s); close() }
    val p16 = Path().apply { moveTo(0.398f * s, 0.006f * s); lineTo(0.206f * s, 0.016f * s); quadraticTo(0.149f * s, 0.02f * s, 0.128f * s, -0.023f * s) }
    val p17 = Path().apply { moveTo(0.312f * s, 0.018f * s); quadraticTo(0.31f * s, 0.013f * s, 0.316f * s, 0.013f * s); lineTo(0.353f * s, 0.013f * s); quadraticTo(0.358f * s, 0.013f * s, 0.356f * s, 0.018f * s); lineTo(0.336f * s, 0.065f * s); quadraticTo(0.334f * s, 0.07f * s, 0.332f * s, 0.065f * s); close() }
    val p18 = Path().apply { moveTo(0.241f * s, 0.018f * s); quadraticTo(0.239f * s, 0.013f * s, 0.244f * s, 0.013f * s); lineTo(0.281f * s, 0.013f * s); quadraticTo(0.287f * s, 0.013f * s, 0.285f * s, 0.018f * s); lineTo(0.265f * s, 0.065f * s); quadraticTo(0.263f * s, 0.07f * s, 0.261f * s, 0.065f * s); close() }
    val p19 = Path().apply { moveTo(0.17f * s, 0.018f * s); quadraticTo(0.167f * s, 0.013f * s, 0.173f * s, 0.013f * s); lineTo(0.21f * s, 0.013f * s); quadraticTo(0.216f * s, 0.013f * s, 0.214f * s, 0.018f * s); lineTo(0.194f * s, 0.065f * s); quadraticTo(0.192f * s, 0.07f * s, 0.189f * s, 0.065f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2E9E44))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2E9E44))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2E9E44))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2E9E44))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2E9E44))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF3F9943))
    drawPath(p6, sil ?: Color(0xFF3F9943))
    drawPath(p7, sil ?: Color(0xFF3F9943))
    drawPath(p8, sil ?: Color(0xFF3F9943))
    drawPath(p9, sil ?: Color(0xFF3F9943))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p10, Color(0xFF4FBF54))
        }
        clipPath(p6) {
            drawPath(p11, Color(0xFF4FBF54))
        }
        clipPath(p7) {
            drawPath(p12, Color(0xFF4FBF54))
        }
        clipPath(p8) {
            drawPath(p13, Color(0xFF4FBF54))
        }
        clipPath(p9) {
            drawPath(p14, Color(0xFF4FBF54))
        }
        clipPath(p5) {
            drawOval(Color(0xFFC4EE96), Offset(-0.428f * s, 0.091f * s), Size(0.855f * s, 0.171f * s))
        }
        clipPath(p6) {
            drawOval(Color(0xFFC4EE96), Offset(-0.428f * s, 0.091f * s), Size(0.855f * s, 0.171f * s))
        }
        clipPath(p7) {
            drawOval(Color(0xFFC4EE96), Offset(-0.428f * s, 0.091f * s), Size(0.855f * s, 0.171f * s))
        }
        clipPath(p8) {
            drawOval(Color(0xFFC4EE96), Offset(-0.428f * s, 0.091f * s), Size(0.855f * s, 0.171f * s))
        }
        clipPath(p9) {
            drawOval(Color(0xFFC4EE96), Offset(-0.428f * s, 0.091f * s), Size(0.855f * s, 0.171f * s))
        }
        drawPath(p15, Color(0x73FFFFFF))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.057f * s, Offset(0.113f * s, -0.151f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.057f * s, Offset(0.113f * s, -0.151f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.033f * s, Offset(0.125f * s, -0.134f * s))
        drawCircle(Color(0xFFFFFFFF), 0.011f * s, Offset(0.112f * s, -0.149f * s))
    }
    drawPath(p16, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p17, sil ?: Color(0xFFFFFFFF))
    drawPath(p17, sil ?: Color(0xFF2B2D42), style = Stroke(0.018f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p18, sil ?: Color(0xFFFFFFFF))
    drawPath(p18, sil ?: Color(0xFF2B2D42), style = Stroke(0.018f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p19, sil ?: Color(0xFFFFFFFF))
    drawPath(p19, sil ?: Color(0xFF2B2D42), style = Stroke(0.018f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artDinosaure(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.124f * s, 0.329f * s); cubicTo(-0.124f * s, 0.358f * s, -0.147f * s, 0.382f * s, -0.176f * s, 0.382f * s); cubicTo(-0.205f * s, 0.382f * s, -0.228f * s, 0.358f * s, -0.228f * s, 0.329f * s); lineTo(-0.228f * s, 0.232f * s); cubicTo(-0.228f * s, 0.203f * s, -0.205f * s, 0.18f * s, -0.176f * s, 0.18f * s); cubicTo(-0.147f * s, 0.18f * s, -0.124f * s, 0.203f * s, -0.124f * s, 0.232f * s); close() }
    val p1 = Path().apply { moveTo(-0.003f * s, 0.329f * s); cubicTo(-0.003f * s, 0.358f * s, -0.026f * s, 0.382f * s, -0.055f * s, 0.382f * s); cubicTo(-0.084f * s, 0.382f * s, -0.107f * s, 0.358f * s, -0.107f * s, 0.329f * s); lineTo(-0.107f * s, 0.232f * s); cubicTo(-0.107f * s, 0.203f * s, -0.084f * s, 0.18f * s, -0.055f * s, 0.18f * s); cubicTo(-0.026f * s, 0.18f * s, -0.003f * s, 0.203f * s, -0.003f * s, 0.232f * s); close() }
    val p2 = Path().apply { moveTo(0.143f * s, 0.329f * s); cubicTo(0.143f * s, 0.358f * s, 0.119f * s, 0.382f * s, 0.09f * s, 0.382f * s); cubicTo(0.061f * s, 0.382f * s, 0.038f * s, 0.358f * s, 0.038f * s, 0.329f * s); lineTo(0.038f * s, 0.232f * s); cubicTo(0.038f * s, 0.203f * s, 0.061f * s, 0.18f * s, 0.09f * s, 0.18f * s); cubicTo(0.119f * s, 0.18f * s, 0.143f * s, 0.203f * s, 0.143f * s, 0.232f * s); close() }
    val p3 = Path().apply { moveTo(0.239f * s, 0.329f * s); cubicTo(0.239f * s, 0.358f * s, 0.216f * s, 0.382f * s, 0.187f * s, 0.382f * s); cubicTo(0.158f * s, 0.382f * s, 0.135f * s, 0.358f * s, 0.135f * s, 0.329f * s); lineTo(0.135f * s, 0.232f * s); cubicTo(0.135f * s, 0.203f * s, 0.158f * s, 0.18f * s, 0.187f * s, 0.18f * s); cubicTo(0.216f * s, 0.18f * s, 0.239f * s, 0.203f * s, 0.239f * s, 0.232f * s); close() }
    val p4 = Path().apply { moveTo(0.252f * s, 0.119f * s); cubicTo(0.252f * s, 0.222f * s, 0.132f * s, 0.305f * s, -0.015f * s, 0.305f * s); cubicTo(-0.162f * s, 0.305f * s, -0.281f * s, 0.222f * s, -0.281f * s, 0.119f * s); cubicTo(-0.281f * s, 0.017f * s, -0.162f * s, -0.066f * s, -0.015f * s, -0.066f * s); cubicTo(0.132f * s, -0.066f * s, 0.252f * s, 0.017f * s, 0.252f * s, 0.119f * s); close() }
    val p5 = Path().apply { moveTo(-0.056f * s, 0.1f * s); cubicTo(-0.076f * s, 0.061f * s, -0.189f * s, 0.045f * s, -0.218f * s, 0.036f * s); cubicTo(-0.247f * s, 0.028f * s, -0.225f * s, 0.042f * s, -0.229f * s, 0.046f * s); cubicTo(-0.233f * s, 0.05f * s, -0.238f * s, 0.055f * s, -0.242f * s, 0.059f * s); cubicTo(-0.246f * s, 0.064f * s, -0.251f * s, 0.069f * s, -0.255f * s, 0.074f * s); cubicTo(-0.259f * s, 0.078f * s, -0.264f * s, 0.083f * s, -0.268f * s, 0.088f * s); cubicTo(-0.272f * s, 0.093f * s, -0.277f * s, 0.098f * s, -0.281f * s, 0.103f * s); cubicTo(-0.285f * s, 0.108f * s, -0.289f * s, 0.113f * s, -0.292f * s, 0.117f * s); cubicTo(-0.296f * s, 0.121f * s, -0.299f * s, 0.125f * s, -0.302f * s, 0.129f * s); cubicTo(-0.304f * s, 0.132f * s, -0.306f * s, 0.135f * s, -0.307f * s, 0.137f * s); cubicTo(-0.308f * s, 0.14f * s, -0.306f * s, 0.142f * s, -0.305f * s, 0.144f * s); cubicTo(-0.304f * s, 0.146f * s, -0.302f * s, 0.148f * s, -0.302f * s, 0.15f * s); cubicTo(-0.302f * s, 0.152f * s, -0.303f * s, 0.154f * s, -0.306f * s, 0.155f * s); cubicTo(-0.308f * s, 0.156f * s, -0.311f * s, 0.157f * s, -0.315f * s, 0.157f * s); cubicTo(-0.318f * s, 0.157f * s, -0.323f * s, 0.156f * s, -0.327f * s, 0.155f * s); cubicTo(-0.331f * s, 0.155f * s, -0.336f * s, 0.153f * s, -0.34f * s, 0.152f * s); cubicTo(-0.345f * s, 0.15f * s, -0.349f * s, 0.149f * s, -0.353f * s, 0.147f * s); cubicTo(-0.358f * s, 0.145f * s, -0.362f * s, 0.143f * s, -0.366f * s, 0.141f * s); cubicTo(-0.37f * s, 0.14f * s, -0.374f * s, 0.138f * s, -0.377f * s, 0.136f * s); cubicTo(-0.381f * s, 0.135f * s, -0.38f * s, 0.133f * s, -0.387f * s, 0.133f * s); cubicTo(-0.394f * s, 0.133f * s, -0.414f * s, 0.132f * s, -0.419f * s, 0.138f * s); cubicTo(-0.424f * s, 0.144f * s, -0.418f * s, 0.164f * s, -0.417f * s, 0.171f * s); cubicTo(-0.416f * s, 0.177f * s, -0.415f * s, 0.175f * s, -0.414f * s, 0.177f * s); cubicTo(-0.413f * s, 0.18f * s, -0.411f * s, 0.183f * s, -0.41f * s, 0.187f * s); cubicTo(-0.408f * s, 0.191f * s, -0.406f * s, 0.195f * s, -0.404f * s, 0.2f * s); cubicTo(-0.402f * s, 0.204f * s, -0.399f * s, 0.209f * s, -0.396f * s, 0.214f * s); cubicTo(-0.393f * s, 0.219f * s, -0.39f * s, 0.225f * s, -0.387f * s, 0.23f * s); cubicTo(-0.383f * s, 0.236f * s, -0.379f * s, 0.242f * s, -0.374f * s, 0.247f * s); cubicTo(-0.37f * s, 0.253f * s, -0.364f * s, 0.259f * s, -0.358f * s, 0.264f * s); cubicTo(-0.351f * s, 0.269f * s, -0.344f * s, 0.275f * s, -0.335f * s, 0.279f * s); cubicTo(-0.327f * s, 0.283f * s, -0.315f * s, 0.287f * s, -0.305f * s, 0.289f * s); cubicTo(-0.295f * s, 0.291f * s, -0.284f * s, 0.291f * s, -0.274f * s, 0.291f * s); cubicTo(-0.264f * s, 0.292f * s, -0.254f * s, 0.291f * s, -0.245f * s, 0.29f * s); cubicTo(-0.235f * s, 0.289f * s, -0.226f * s, 0.288f * s, -0.217f * s, 0.287f * s); cubicTo(-0.209f * s, 0.286f * s, -0.2f * s, 0.284f * s, -0.191f * s, 0.283f * s); cubicTo(-0.183f * s, 0.281f * s, -0.174f * s, 0.279f * s, -0.166f * s, 0.278f * s); cubicTo(-0.158f * s, 0.276f * s, -0.151f * s, 0.274f * s, -0.144f * s, 0.273f * s); cubicTo(-0.137f * s, 0.271f * s, -0.13f * s, 0.27f * s, -0.125f * s, 0.269f * s); cubicTo(-0.119f * s, 0.268f * s, -0.115f * s, 0.268f * s, -0.111f * s, 0.267f * s); cubicTo(-0.107f * s, 0.267f * s, -0.111f * s, 0.295f * s, -0.102f * s, 0.267f * s); cubicTo(-0.093f * s, 0.239f * s, -0.037f * s, 0.138f * s, -0.056f * s, 0.1f * s); close() }
    val p6 = Path().apply { moveTo(0.054f * s, 0.161f * s); cubicTo(0.084f * s, 0.183f * s, 0.179f * s, 0.151f * s, 0.205f * s, 0.146f * s); cubicTo(0.231f * s, 0.142f * s, 0.208f * s, 0.139f * s, 0.21f * s, 0.135f * s); cubicTo(0.212f * s, 0.13f * s, 0.215f * s, 0.125f * s, 0.217f * s, 0.119f * s); cubicTo(0.22f * s, 0.113f * s, 0.223f * s, 0.106f * s, 0.227f * s, 0.099f * s); cubicTo(0.23f * s, 0.092f * s, 0.234f * s, 0.084f * s, 0.238f * s, 0.076f * s); cubicTo(0.241f * s, 0.068f * s, 0.246f * s, 0.06f * s, 0.249f * s, 0.051f * s); cubicTo(0.253f * s, 0.043f * s, 0.257f * s, 0.034f * s, 0.261f * s, 0.026f * s); cubicTo(0.265f * s, 0.017f * s, 0.269f * s, 0.008f * s, 0.273f * s, 0.0f * s); cubicTo(0.277f * s, -0.009f * s, 0.28f * s, -0.018f * s, 0.284f * s, -0.026f * s); cubicTo(0.287f * s, -0.035f * s, 0.29f * s, -0.043f * s, 0.293f * s, -0.052f * s); cubicTo(0.296f * s, -0.06f * s, 0.299f * s, -0.069f * s, 0.301f * s, -0.077f * s); cubicTo(0.303f * s, -0.085f * s, 0.305f * s, -0.094f * s, 0.307f * s, -0.102f * s); cubicTo(0.308f * s, -0.11f * s, 0.31f * s, -0.118f * s, 0.311f * s, -0.126f * s); cubicTo(0.312f * s, -0.133f * s, 0.313f * s, -0.141f * s, 0.314f * s, -0.148f * s); cubicTo(0.315f * s, -0.155f * s, 0.316f * s, -0.162f * s, 0.317f * s, -0.169f * s); cubicTo(0.318f * s, -0.176f * s, 0.319f * s, -0.182f * s, 0.319f * s, -0.188f * s); cubicTo(0.32f * s, -0.194f * s, 0.32f * s, -0.199f * s, 0.321f * s, -0.204f * s); cubicTo(0.321f * s, -0.209f * s, 0.322f * s, -0.212f * s, 0.322f * s, -0.216f * s); cubicTo(0.323f * s, -0.219f * s, 0.332f * s, -0.212f * s, 0.323f * s, -0.225f * s); cubicTo(0.315f * s, -0.238f * s, 0.29f * s, -0.289f * s, 0.269f * s, -0.293f * s); cubicTo(0.248f * s, -0.296f * s, 0.209f * s, -0.256f * s, 0.196f * s, -0.246f * s); cubicTo(0.183f * s, -0.237f * s, 0.194f * s, -0.239f * s, 0.194f * s, -0.235f * s); cubicTo(0.193f * s, -0.23f * s, 0.192f * s, -0.225f * s, 0.192f * s, -0.22f * s); cubicTo(0.191f * s, -0.215f * s, 0.19f * s, -0.21f * s, 0.189f * s, -0.204f * s); cubicTo(0.189f * s, -0.199f * s, 0.188f * s, -0.193f * s, 0.187f * s, -0.188f * s); cubicTo(0.186f * s, -0.182f * s, 0.185f * s, -0.176f * s, 0.184f * s, -0.171f * s); cubicTo(0.182f * s, -0.165f * s, 0.181f * s, -0.159f * s, 0.18f * s, -0.154f * s); cubicTo(0.178f * s, -0.148f * s, 0.176f * s, -0.143f * s, 0.174f * s, -0.138f * s); cubicTo(0.173f * s, -0.133f * s, 0.171f * s, -0.128f * s, 0.168f * s, -0.124f * s); cubicTo(0.166f * s, -0.12f * s, 0.164f * s, -0.116f * s, 0.161f * s, -0.113f * s); cubicTo(0.159f * s, -0.109f * s, 0.156f * s, -0.106f * s, 0.152f * s, -0.101f * s); cubicTo(0.148f * s, -0.097f * s, 0.143f * s, -0.092f * s, 0.138f * s, -0.087f * s); cubicTo(0.133f * s, -0.082f * s, 0.127f * s, -0.077f * s, 0.122f * s, -0.072f * s); cubicTo(0.116f * s, -0.067f * s, 0.11f * s, -0.061f * s, 0.104f * s, -0.056f * s); cubicTo(0.097f * s, -0.051f * s, 0.091f * s, -0.045f * s, 0.085f * s, -0.04f * s); cubicTo(0.079f * s, -0.035f * s, 0.072f * s, -0.03f * s, 0.067f * s, -0.025f * s); cubicTo(0.061f * s, -0.02f * s, 0.055f * s, -0.016f * s, 0.05f * s, -0.011f * s); cubicTo(0.044f * s, -0.007f * s, 0.039f * s, -0.002f * s, 0.035f * s, 0.002f * s); cubicTo(0.03f * s, 0.006f * s, 0.02f * s, -0.014f * s, 0.023f * s, 0.012f * s); cubicTo(0.027f * s, 0.039f * s, 0.024f * s, 0.139f * s, 0.054f * s, 0.161f * s); close() }
    val p7 = Path().apply { moveTo(0.421f * s, -0.284f * s); cubicTo(0.421f * s, -0.239f * s, 0.37f * s, -0.203f * s, 0.308f * s, -0.203f * s); cubicTo(0.246f * s, -0.203f * s, 0.195f * s, -0.239f * s, 0.195f * s, -0.284f * s); cubicTo(0.195f * s, -0.328f * s, 0.246f * s, -0.365f * s, 0.308f * s, -0.365f * s); cubicTo(0.37f * s, -0.365f * s, 0.421f * s, -0.328f * s, 0.421f * s, -0.284f * s); close() }
    val p8 = Path().apply { moveTo(0.231f * s, 0.079f * s); cubicTo(0.231f * s, 0.182f * s, 0.112f * s, 0.265f * s, -0.035f * s, 0.265f * s); cubicTo(-0.182f * s, 0.265f * s, -0.301f * s, 0.182f * s, -0.301f * s, 0.079f * s); cubicTo(-0.301f * s, -0.023f * s, -0.182f * s, -0.106f * s, -0.035f * s, -0.106f * s); cubicTo(0.112f * s, -0.106f * s, 0.231f * s, -0.023f * s, 0.231f * s, 0.079f * s); close() }
    val p9 = Path().apply { moveTo(-0.076f * s, 0.059f * s); cubicTo(-0.096f * s, 0.021f * s, -0.209f * s, 0.005f * s, -0.238f * s, -0.004f * s); cubicTo(-0.267f * s, -0.013f * s, -0.245f * s, 0.002f * s, -0.249f * s, 0.006f * s); cubicTo(-0.253f * s, 0.01f * s, -0.258f * s, 0.014f * s, -0.262f * s, 0.019f * s); cubicTo(-0.267f * s, 0.024f * s, -0.271f * s, 0.028f * s, -0.275f * s, 0.033f * s); cubicTo(-0.279f * s, 0.038f * s, -0.284f * s, 0.043f * s, -0.288f * s, 0.048f * s); cubicTo(-0.292f * s, 0.053f * s, -0.297f * s, 0.058f * s, -0.301f * s, 0.063f * s); cubicTo(-0.305f * s, 0.068f * s, -0.309f * s, 0.072f * s, -0.313f * s, 0.077f * s); cubicTo(-0.316f * s, 0.081f * s, -0.32f * s, 0.085f * s, -0.322f * s, 0.088f * s); cubicTo(-0.324f * s, 0.092f * s, -0.327f * s, 0.095f * s, -0.327f * s, 0.097f * s); cubicTo(-0.328f * s, 0.1f * s, -0.326f * s, 0.101f * s, -0.325f * s, 0.103f * s); cubicTo(-0.324f * s, 0.105f * s, -0.322f * s, 0.108f * s, -0.322f * s, 0.11f * s); cubicTo(-0.322f * s, 0.111f * s, -0.324f * s, 0.113f * s, -0.326f * s, 0.115f * s); cubicTo(-0.328f * s, 0.116f * s, -0.332f * s, 0.116f * s, -0.335f * s, 0.116f * s); cubicTo(-0.339f * s, 0.116f * s, -0.343f * s, 0.116f * s, -0.347f * s, 0.115f * s); cubicTo(-0.351f * s, 0.114f * s, -0.356f * s, 0.113f * s, -0.36f * s, 0.112f * s); cubicTo(-0.365f * s, 0.11f * s, -0.369f * s, 0.108f * s, -0.374f * s, 0.107f * s); cubicTo(-0.378f * s, 0.105f * s, -0.382f * s, 0.103f * s, -0.386f * s, 0.101f * s); cubicTo(-0.39f * s, 0.099f * s, -0.394f * s, 0.097f * s, -0.398f * s, 0.096f * s); cubicTo(-0.401f * s, 0.095f * s, -0.4f * s, 0.092f * s, -0.407f * s, 0.093f * s); cubicTo(-0.414f * s, 0.093f * s, -0.434f * s, 0.091f * s, -0.439f * s, 0.098f * s); cubicTo(-0.444f * s, 0.104f * s, -0.438f * s, 0.124f * s, -0.437f * s, 0.13f * s); cubicTo(-0.436f * s, 0.137f * s, -0.435f * s, 0.134f * s, -0.434f * s, 0.137f * s); cubicTo(-0.433f * s, 0.14f * s, -0.432f * s, 0.143f * s, -0.43f * s, 0.147f * s); cubicTo(-0.428f * s, 0.15f * s, -0.426f * s, 0.155f * s, -0.424f * s, 0.159f * s); cubicTo(-0.422f * s, 0.164f * s, -0.419f * s, 0.169f * s, -0.416f * s, 0.174f * s); cubicTo(-0.414f * s, 0.179f * s, -0.41f * s, 0.184f * s, -0.407f * s, 0.19f * s); cubicTo(-0.403f * s, 0.195f * s, -0.399f * s, 0.201f * s, -0.394f * s, 0.207f * s); cubicTo(-0.39f * s, 0.212f * s, -0.385f * s, 0.218f * s, -0.378f * s, 0.223f * s); cubicTo(-0.372f * s, 0.229f * s, -0.364f * s, 0.234f * s, -0.355f * s, 0.239f * s); cubicTo(-0.347f * s, 0.243f * s, -0.336f * s, 0.246f * s, -0.325f * s, 0.249f * s); cubicTo(-0.315f * s, 0.251f * s, -0.304f * s, 0.251f * s, -0.294f * s, 0.251f * s); cubicTo(-0.284f * s, 0.251f * s, -0.274f * s, 0.251f * s, -0.265f * s, 0.25f * s); cubicTo(-0.256f * s, 0.249f * s, -0.247f * s, 0.248f * s, -0.238f * s, 0.247f * s); cubicTo(-0.229f * s, 0.245f * s, -0.22f * s, 0.244f * s, -0.211f * s, 0.242f * s); cubicTo(-0.203f * s, 0.241f * s, -0.194f * s, 0.239f * s, -0.186f * s, 0.237f * s); cubicTo(-0.179f * s, 0.236f * s, -0.171f * s, 0.234f * s, -0.164f * s, 0.233f * s); cubicTo(-0.157f * s, 0.231f * s, -0.15f * s, 0.23f * s, -0.145f * s, 0.229f * s); cubicTo(-0.139f * s, 0.228f * s, -0.135f * s, 0.227f * s, -0.131f * s, 0.227f * s); cubicTo(-0.127f * s, 0.227f * s, -0.131f * s, 0.255f * s, -0.122f * s, 0.227f * s); cubicTo(-0.113f * s, 0.199f * s, -0.057f * s, 0.098f * s, -0.076f * s, 0.059f * s); close() }
    val p10 = Path().apply { moveTo(0.034f * s, 0.121f * s); cubicTo(0.064f * s, 0.143f * s, 0.159f * s, 0.11f * s, 0.185f * s, 0.106f * s); cubicTo(0.211f * s, 0.101f * s, 0.188f * s, 0.099f * s, 0.19f * s, 0.094f * s); cubicTo(0.192f * s, 0.09f * s, 0.194f * s, 0.085f * s, 0.197f * s, 0.079f * s); cubicTo(0.2f * s, 0.073f * s, 0.203f * s, 0.066f * s, 0.207f * s, 0.059f * s); cubicTo(0.21f * s, 0.052f * s, 0.214f * s, 0.044f * s, 0.218f * s, 0.036f * s); cubicTo(0.221f * s, 0.028f * s, 0.225f * s, 0.019f * s, 0.229f * s, 0.011f * s); cubicTo(0.233f * s, 0.003f * s, 0.237f * s, -0.006f * s, 0.241f * s, -0.015f * s); cubicTo(0.245f * s, -0.023f * s, 0.249f * s, -0.032f * s, 0.253f * s, -0.041f * s); cubicTo(0.256f * s, -0.049f * s, 0.26f * s, -0.058f * s, 0.264f * s, -0.067f * s); cubicTo(0.267f * s, -0.075f * s, 0.27f * s, -0.084f * s, 0.273f * s, -0.092f * s); cubicTo(0.276f * s, -0.101f * s, 0.279f * s, -0.109f * s, 0.281f * s, -0.118f * s); cubicTo(0.283f * s, -0.126f * s, 0.285f * s, -0.134f * s, 0.287f * s, -0.142f * s); cubicTo(0.288f * s, -0.15f * s, 0.29f * s, -0.158f * s, 0.291f * s, -0.166f * s); cubicTo(0.292f * s, -0.174f * s, 0.293f * s, -0.181f * s, 0.294f * s, -0.189f * s); cubicTo(0.295f * s, -0.196f * s, 0.296f * s, -0.203f * s, 0.297f * s, -0.209f * s); cubicTo(0.298f * s, -0.216f * s, 0.298f * s, -0.222f * s, 0.299f * s, -0.228f * s); cubicTo(0.3f * s, -0.234f * s, 0.3f * s, -0.239f * s, 0.301f * s, -0.244f * s); cubicTo(0.301f * s, -0.249f * s, 0.302f * s, -0.253f * s, 0.302f * s, -0.256f * s); cubicTo(0.302f * s, -0.26f * s, 0.312f * s, -0.252f * s, 0.303f * s, -0.265f * s); cubicTo(0.294f * s, -0.278f * s, 0.27f * s, -0.33f * s, 0.249f * s, -0.333f * s); cubicTo(0.228f * s, -0.337f * s, 0.188f * s, -0.296f * s, 0.176f * s, -0.287f * s); cubicTo(0.163f * s, -0.277f * s, 0.174f * s, -0.279f * s, 0.174f * s, -0.275f * s); cubicTo(0.173f * s, -0.271f * s, 0.172f * s, -0.265f * s, 0.171f * s, -0.26f * s); cubicTo(0.171f * s, -0.255f * s, 0.17f * s, -0.25f * s, 0.169f * s, -0.245f * s); cubicTo(0.168f * s, -0.239f * s, 0.168f * s, -0.234f * s, 0.167f * s, -0.228f * s); cubicTo(0.166f * s, -0.222f * s, 0.165f * s, -0.217f * s, 0.163f * s, -0.211f * s); cubicTo(0.162f * s, -0.205f * s, 0.161f * s, -0.199f * s, 0.159f * s, -0.194f * s); cubicTo(0.158f * s, -0.189f * s, 0.156f * s, -0.183f * s, 0.154f * s, -0.178f * s); cubicTo(0.152f * s, -0.173f * s, 0.15f * s, -0.168f * s, 0.148f * s, -0.164f * s); cubicTo(0.146f * s, -0.16f * s, 0.144f * s, -0.157f * s, 0.141f * s, -0.153f * s); cubicTo(0.138f * s, -0.149f * s, 0.135f * s, -0.146f * s, 0.132f * s, -0.142f * s); cubicTo(0.128f * s, -0.137f * s, 0.123f * s, -0.133f * s, 0.118f * s, -0.128f * s); cubicTo(0.113f * s, -0.123f * s, 0.107f * s, -0.117f * s, 0.102f * s, -0.112f * s); cubicTo(0.096f * s, -0.107f * s, 0.089f * s, -0.101f * s, 0.083f * s, -0.096f * s); cubicTo(0.077f * s, -0.091f * s, 0.071f * s, -0.086f * s, 0.065f * s, -0.08f * s); cubicTo(0.059f * s, -0.075f * s, 0.052f * s, -0.07f * s, 0.046f * s, -0.065f * s); cubicTo(0.041f * s, -0.061f * s, 0.035f * s, -0.056f * s, 0.029f * s, -0.051f * s); cubicTo(0.024f * s, -0.047f * s, 0.019f * s, -0.042f * s, 0.015f * s, -0.039f * s); cubicTo(0.01f * s, -0.035f * s, 0.0f * s, -0.055f * s, 0.003f * s, -0.028f * s); cubicTo(0.007f * s, -0.002f * s, 0.004f * s, 0.098f * s, 0.034f * s, 0.121f * s); close() }
    val p11 = Path().apply { moveTo(0.401f * s, -0.324f * s); cubicTo(0.401f * s, -0.28f * s, 0.35f * s, -0.244f * s, 0.288f * s, -0.244f * s); cubicTo(0.226f * s, -0.244f * s, 0.175f * s, -0.28f * s, 0.175f * s, -0.324f * s); cubicTo(0.175f * s, -0.369f * s, 0.226f * s, -0.405f * s, 0.288f * s, -0.405f * s); cubicTo(0.35f * s, -0.405f * s, 0.401f * s, -0.369f * s, 0.401f * s, -0.324f * s); close() }
    val p12 = Path().apply { moveTo(-0.064f * s, -0.04f * s); cubicTo(-0.062f * s, -0.029f * s, -0.096f * s, -0.014f * s, -0.14f * s, -0.006f * s); cubicTo(-0.184f * s, 0.002f * s, -0.221f * s, -0.001f * s, -0.223f * s, -0.012f * s); cubicTo(-0.225f * s, -0.023f * s, -0.191f * s, -0.038f * s, -0.147f * s, -0.046f * s); cubicTo(-0.103f * s, -0.053f * s, -0.066f * s, -0.051f * s, -0.064f * s, -0.04f * s); close() }
    val p13 = Path().apply { moveTo(0.324f * s, -0.236f * s); quadraticTo(0.365f * s, -0.203f * s, 0.405f * s, -0.236f * s) }
    drawPath(p0, sil ?: Color(0xFF4AAE4F))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF4AAE4F))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF4AAE4F))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF4AAE4F))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF499D4A))
    drawPath(p5, sil ?: Color(0xFF499D4A))
    drawPath(p6, sil ?: Color(0xFF499D4A))
    drawPath(p7, sil ?: Color(0xFF499D4A))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p8, Color(0xFF5BC45C))
        }
        clipPath(p5) {
            drawPath(p9, Color(0xFF5BC45C))
        }
        clipPath(p6) {
            drawPath(p10, Color(0xFF5BC45C))
        }
        clipPath(p7) {
            drawPath(p11, Color(0xFF5BC45C))
        }
        clipPath(p4) {
            drawOval(Color(0xFFD2F2A0), Offset(-0.386f * s, 0.216f * s), Size(0.807f * s, 0.194f * s))
            drawCircle(Color(0xFF2F9E44), 0.048f * s, Offset(-0.112f * s, 0.039f * s))
            drawCircle(Color(0xFF2F9E44), 0.04f * s, Offset(0.05f * s, -0.01f * s))
            drawCircle(Color(0xFF2F9E44), 0.036f * s, Offset(-0.224f * s, 0.119f * s))
        }
        clipPath(p5) {
            drawOval(Color(0xFFD2F2A0), Offset(-0.386f * s, 0.216f * s), Size(0.807f * s, 0.194f * s))
            drawCircle(Color(0xFF2F9E44), 0.048f * s, Offset(-0.112f * s, 0.039f * s))
            drawCircle(Color(0xFF2F9E44), 0.04f * s, Offset(0.05f * s, -0.01f * s))
            drawCircle(Color(0xFF2F9E44), 0.036f * s, Offset(-0.224f * s, 0.119f * s))
        }
        clipPath(p6) {
            drawOval(Color(0xFFD2F2A0), Offset(-0.386f * s, 0.216f * s), Size(0.807f * s, 0.194f * s))
            drawCircle(Color(0xFF2F9E44), 0.048f * s, Offset(-0.112f * s, 0.039f * s))
            drawCircle(Color(0xFF2F9E44), 0.04f * s, Offset(0.05f * s, -0.01f * s))
            drawCircle(Color(0xFF2F9E44), 0.036f * s, Offset(-0.224f * s, 0.119f * s))
        }
        clipPath(p7) {
            drawOval(Color(0xFFD2F2A0), Offset(-0.386f * s, 0.216f * s), Size(0.807f * s, 0.194f * s))
            drawCircle(Color(0xFF2F9E44), 0.048f * s, Offset(-0.112f * s, 0.039f * s))
            drawCircle(Color(0xFF2F9E44), 0.04f * s, Offset(0.05f * s, -0.01f * s))
            drawCircle(Color(0xFF2F9E44), 0.036f * s, Offset(-0.224f * s, 0.119f * s))
        }
        drawPath(p12, Color(0x73FFFFFF))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.042f * s, Offset(0.34f * s, -0.308f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.042f * s, Offset(0.34f * s, -0.308f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.024f * s, Offset(0.353f * s, -0.3f * s))
        drawCircle(Color(0xFFFFFFFF), 0.008f * s, Offset(0.344f * s, -0.311f * s))
    }
    drawPath(p13, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0xCCFFB0C8), Offset(0.264f * s, -0.263f * s), Size(0.056f * s, 0.039f * s))
    }
}

private fun DrawScope.artSerpent(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.296f * s, -0.26f * s); lineTo(0.376f * s, -0.26f * s); moveTo(0.376f * s, -0.26f * s); lineTo(0.417f * s, -0.292f * s); moveTo(0.376f * s, -0.26f * s); lineTo(0.417f * s, -0.228f * s) }
    val p1 = Path().apply { moveTo(-0.392f * s, 0.152f * s); cubicTo(-0.397f * s, 0.154f * s, -0.4f * s, 0.164f * s, -0.4f * s, 0.172f * s); cubicTo(-0.399f * s, 0.181f * s, -0.394f * s, 0.19f * s, -0.39f * s, 0.203f * s); cubicTo(-0.386f * s, 0.216f * s, -0.382f * s, 0.233f * s, -0.376f * s, 0.249f * s); cubicTo(-0.37f * s, 0.266f * s, -0.364f * s, 0.286f * s, -0.354f * s, 0.302f * s); cubicTo(-0.344f * s, 0.319f * s, -0.331f * s, 0.336f * s, -0.317f * s, 0.349f * s); cubicTo(-0.302f * s, 0.362f * s, -0.284f * s, 0.372f * s, -0.267f * s, 0.381f * s); cubicTo(-0.249f * s, 0.39f * s, -0.23f * s, 0.397f * s, -0.21f * s, 0.404f * s); cubicTo(-0.19f * s, 0.41f * s, -0.169f * s, 0.414f * s, -0.148f * s, 0.417f * s); cubicTo(-0.127f * s, 0.42f * s, -0.105f * s, 0.422f * s, -0.082f * s, 0.421f * s); cubicTo(-0.06f * s, 0.421f * s, -0.036f * s, 0.418f * s, -0.012f * s, 0.414f * s); cubicTo(0.013f * s, 0.41f * s, 0.039f * s, 0.405f * s, 0.065f * s, 0.397f * s); cubicTo(0.09f * s, 0.39f * s, 0.116f * s, 0.382f * s, 0.14f * s, 0.37f * s); cubicTo(0.163f * s, 0.359f * s, 0.188f * s, 0.345f * s, 0.207f * s, 0.327f * s); cubicTo(0.226f * s, 0.31f * s, 0.242f * s, 0.286f * s, 0.253f * s, 0.264f * s); cubicTo(0.265f * s, 0.242f * s, 0.272f * s, 0.217f * s, 0.276f * s, 0.193f * s); cubicTo(0.281f * s, 0.17f * s, 0.283f * s, 0.146f * s, 0.281f * s, 0.123f * s); cubicTo(0.279f * s, 0.099f * s, 0.275f * s, 0.073f * s, 0.265f * s, 0.052f * s); cubicTo(0.255f * s, 0.03f * s, 0.237f * s, 0.009f * s, 0.221f * s, -0.007f * s); cubicTo(0.204f * s, -0.023f * s, 0.184f * s, -0.033f * s, 0.166f * s, -0.043f * s); cubicTo(0.148f * s, -0.053f * s, 0.13f * s, -0.06f * s, 0.111f * s, -0.067f * s); cubicTo(0.093f * s, -0.075f * s, 0.075f * s, -0.082f * s, 0.055f * s, -0.088f * s); cubicTo(0.035f * s, -0.094f * s, 0.013f * s, -0.1f * s, -0.008f * s, -0.104f * s); cubicTo(-0.03f * s, -0.108f * s, -0.054f * s, -0.11f * s, -0.073f * s, -0.113f * s); cubicTo(-0.093f * s, -0.115f * s, -0.111f * s, -0.118f * s, -0.125f * s, -0.121f * s); cubicTo(-0.139f * s, -0.124f * s, -0.147f * s, -0.126f * s, -0.156f * s, -0.131f * s); cubicTo(-0.164f * s, -0.135f * s, -0.17f * s, -0.141f * s, -0.177f * s, -0.147f * s); cubicTo(-0.184f * s, -0.154f * s, -0.192f * s, -0.163f * s, -0.197f * s, -0.17f * s); cubicTo(-0.203f * s, -0.177f * s, -0.206f * s, -0.186f * s, -0.208f * s, -0.189f * s); cubicTo(-0.21f * s, -0.192f * s, -0.209f * s, -0.188f * s, -0.209f * s, -0.187f * s); cubicTo(-0.209f * s, -0.187f * s, -0.211f * s, -0.184f * s, -0.208f * s, -0.187f * s); cubicTo(-0.205f * s, -0.19f * s, -0.199f * s, -0.199f * s, -0.191f * s, -0.206f * s); cubicTo(-0.184f * s, -0.213f * s, -0.173f * s, -0.222f * s, -0.164f * s, -0.228f * s); cubicTo(-0.155f * s, -0.234f * s, -0.147f * s, -0.239f * s, -0.138f * s, -0.243f * s); cubicTo(-0.129f * s, -0.246f * s, -0.121f * s, -0.248f * s, -0.11f * s, -0.249f * s); cubicTo(-0.099f * s, -0.25f * s, -0.085f * s, -0.251f * s, -0.071f * s, -0.251f * s); cubicTo(-0.058f * s, -0.251f * s, -0.042f * s, -0.25f * s, -0.029f * s, -0.249f * s); cubicTo(-0.016f * s, -0.248f * s, -0.004f * s, -0.247f * s, 0.005f * s, -0.245f * s); cubicTo(0.014f * s, -0.243f * s, 0.02f * s, -0.241f * s, 0.027f * s, -0.238f * s); cubicTo(0.034f * s, -0.236f * s, 0.04f * s, -0.232f * s, 0.047f * s, -0.228f * s); cubicTo(0.054f * s, -0.224f * s, 0.062f * s, -0.219f * s, 0.069f * s, -0.215f * s); cubicTo(0.076f * s, -0.211f * s, 0.065f * s, -0.198f * s, 0.088f * s, -0.205f * s); cubicTo(0.111f * s, -0.212f * s, 0.193f * s, -0.227f * s, 0.206f * s, -0.256f * s); cubicTo(0.22f * s, -0.285f * s, 0.178f * s, -0.358f * s, 0.168f * s, -0.38f * s); cubicTo(0.159f * s, -0.401f * s, 0.158f * s, -0.383f * s, 0.15f * s, -0.387f * s); cubicTo(0.142f * s, -0.39f * s, 0.132f * s, -0.395f * s, 0.119f * s, -0.399f * s); cubicTo(0.107f * s, -0.403f * s, 0.091f * s, -0.409f * s, 0.076f * s, -0.412f * s); cubicTo(0.06f * s, -0.416f * s, 0.043f * s, -0.419f * s, 0.026f * s, -0.42f * s); cubicTo(0.009f * s, -0.421f * s, -0.007f * s, -0.421f * s, -0.025f * s, -0.421f * s); cubicTo(-0.043f * s, -0.42f * s, -0.062f * s, -0.42f * s, -0.081f * s, -0.418f * s); cubicTo(-0.1f * s, -0.416f * s, -0.121f * s, -0.414f * s, -0.141f * s, -0.409f * s); cubicTo(-0.161f * s, -0.405f * s, -0.181f * s, -0.399f * s, -0.2f * s, -0.39f * s); cubicTo(-0.219f * s, -0.382f * s, -0.236f * s, -0.371f * s, -0.253f * s, -0.36f * s); cubicTo(-0.27f * s, -0.348f * s, -0.286f * s, -0.336f * s, -0.302f * s, -0.321f * s); cubicTo(-0.317f * s, -0.307f * s, -0.332f * s, -0.292f * s, -0.343f * s, -0.272f * s); cubicTo(-0.355f * s, -0.253f * s, -0.366f * s, -0.228f * s, -0.369f * s, -0.205f * s); cubicTo(-0.371f * s, -0.182f * s, -0.365f * s, -0.155f * s, -0.359f * s, -0.134f * s); cubicTo(-0.352f * s, -0.112f * s, -0.34f * s, -0.094f * s, -0.328f * s, -0.077f * s); cubicTo(-0.316f * s, -0.059f * s, -0.301f * s, -0.044f * s, -0.285f * s, -0.029f * s); cubicTo(-0.269f * s, -0.014f * s, -0.25f * s, 0.001f * s, -0.23f * s, 0.011f * s); cubicTo(-0.21f * s, 0.022f * s, -0.186f * s, 0.029f * s, -0.163f * s, 0.035f * s); cubicTo(-0.141f * s, 0.041f * s, -0.117f * s, 0.043f * s, -0.096f * s, 0.046f * s); cubicTo(-0.075f * s, 0.049f * s, -0.055f * s, 0.051f * s, -0.038f * s, 0.054f * s); cubicTo(-0.02f * s, 0.057f * s, -0.007f * s, 0.06f * s, 0.008f * s, 0.065f * s); cubicTo(0.023f * s, 0.07f * s, 0.037f * s, 0.076f * s, 0.051f * s, 0.081f * s); cubicTo(0.064f * s, 0.087f * s, 0.08f * s, 0.093f * s, 0.091f * s, 0.099f * s); cubicTo(0.101f * s, 0.104f * s, 0.109f * s, 0.11f * s, 0.114f * s, 0.113f * s); cubicTo(0.119f * s, 0.116f * s, 0.118f * s, 0.115f * s, 0.119f * s, 0.117f * s); cubicTo(0.12f * s, 0.121f * s, 0.121f * s, 0.124f * s, 0.12f * s, 0.131f * s); cubicTo(0.12f * s, 0.139f * s, 0.12f * s, 0.152f * s, 0.118f * s, 0.162f * s); cubicTo(0.116f * s, 0.172f * s, 0.112f * s, 0.184f * s, 0.108f * s, 0.192f * s); cubicTo(0.105f * s, 0.2f * s, 0.103f * s, 0.204f * s, 0.097f * s, 0.21f * s); cubicTo(0.091f * s, 0.216f * s, 0.083f * s, 0.222f * s, 0.07f * s, 0.228f * s); cubicTo(0.057f * s, 0.235f * s, 0.037f * s, 0.243f * s, 0.019f * s, 0.249f * s); cubicTo(0.0f * s, 0.255f * s, -0.023f * s, 0.261f * s, -0.041f * s, 0.266f * s); cubicTo(-0.06f * s, 0.271f * s, -0.078f * s, 0.274f * s, -0.095f * s, 0.277f * s); cubicTo(-0.111f * s, 0.28f * s, -0.125f * s, 0.282f * s, -0.14f * s, 0.283f * s); cubicTo(-0.155f * s, 0.284f * s, -0.171f * s, 0.284f * s, -0.186f * s, 0.283f * s); cubicTo(-0.2f * s, 0.282f * s, -0.215f * s, 0.28f * s, -0.228f * s, 0.278f * s); cubicTo(-0.241f * s, 0.275f * s, -0.251f * s, 0.274f * s, -0.262f * s, 0.27f * s); cubicTo(-0.272f * s, 0.265f * s, -0.282f * s, 0.26f * s, -0.292f * s, 0.252f * s); cubicTo(-0.302f * s, 0.243f * s, -0.313f * s, 0.231f * s, -0.323f * s, 0.219f * s); cubicTo(-0.333f * s, 0.208f * s, -0.343f * s, 0.193f * s, -0.351f * s, 0.183f * s); cubicTo(-0.359f * s, 0.173f * s, -0.364f * s, 0.162f * s, -0.371f * s, 0.157f * s); cubicTo(-0.378f * s, 0.152f * s, -0.387f * s, 0.149f * s, -0.392f * s, 0.152f * s); close() }
    val p2 = Path().apply { moveTo(0.32f * s, -0.276f * s); cubicTo(0.32f * s, -0.225f * s, 0.266f * s, -0.184f * s, 0.2f * s, -0.184f * s); cubicTo(0.134f * s, -0.184f * s, 0.08f * s, -0.225f * s, 0.08f * s, -0.276f * s); cubicTo(0.08f * s, -0.327f * s, 0.134f * s, -0.369f * s, 0.2f * s, -0.369f * s); cubicTo(0.266f * s, -0.369f * s, 0.32f * s, -0.327f * s, 0.32f * s, -0.276f * s); close() }
    val p3 = Path().apply { moveTo(-0.412f * s, 0.112f * s); cubicTo(-0.417f * s, 0.114f * s, -0.42f * s, 0.123f * s, -0.42f * s, 0.132f * s); cubicTo(-0.419f * s, 0.141f * s, -0.414f * s, 0.15f * s, -0.41f * s, 0.163f * s); cubicTo(-0.406f * s, 0.176f * s, -0.402f * s, 0.193f * s, -0.396f * s, 0.209f * s); cubicTo(-0.39f * s, 0.226f * s, -0.384f * s, 0.245f * s, -0.374f * s, 0.262f * s); cubicTo(-0.364f * s, 0.278f * s, -0.351f * s, 0.295f * s, -0.337f * s, 0.308f * s); cubicTo(-0.322f * s, 0.322f * s, -0.304f * s, 0.332f * s, -0.287f * s, 0.341f * s); cubicTo(-0.269f * s, 0.35f * s, -0.25f * s, 0.357f * s, -0.23f * s, 0.363f * s); cubicTo(-0.21f * s, 0.37f * s, -0.189f * s, 0.374f * s, -0.168f * s, 0.377f * s); cubicTo(-0.147f * s, 0.38f * s, -0.125f * s, 0.382f * s, -0.103f * s, 0.381f * s); cubicTo(-0.08f * s, 0.38f * s, -0.056f * s, 0.378f * s, -0.032f * s, 0.374f * s); cubicTo(-0.007f * s, 0.37f * s, 0.019f * s, 0.364f * s, 0.045f * s, 0.357f * s); cubicTo(0.07f * s, 0.35f * s, 0.096f * s, 0.342f * s, 0.12f * s, 0.33f * s); cubicTo(0.143f * s, 0.318f * s, 0.168f * s, 0.305f * s, 0.187f * s, 0.287f * s); cubicTo(0.206f * s, 0.27f * s, 0.221f * s, 0.246f * s, 0.233f * s, 0.224f * s); cubicTo(0.245f * s, 0.201f * s, 0.251f * s, 0.177f * s, 0.256f * s, 0.153f * s); cubicTo(0.261f * s, 0.13f * s, 0.263f * s, 0.106f * s, 0.261f * s, 0.082f * s); cubicTo(0.259f * s, 0.059f * s, 0.255f * s, 0.033f * s, 0.245f * s, 0.011f * s); cubicTo(0.235f * s, -0.01f * s, 0.217f * s, -0.031f * s, 0.201f * s, -0.047f * s); cubicTo(0.184f * s, -0.063f * s, 0.164f * s, -0.073f * s, 0.146f * s, -0.083f * s); cubicTo(0.128f * s, -0.093f * s, 0.11f * s, -0.1f * s, 0.091f * s, -0.107f * s); cubicTo(0.073f * s, -0.115f * s, 0.055f * s, -0.122f * s, 0.035f * s, -0.128f * s); cubicTo(0.015f * s, -0.135f * s, -0.007f * s, -0.14f * s, -0.028f * s, -0.144f * s); cubicTo(-0.05f * s, -0.148f * s, -0.074f * s, -0.15f * s, -0.093f * s, -0.153f * s); cubicTo(-0.113f * s, -0.155f * s, -0.131f * s, -0.158f * s, -0.145f * s, -0.161f * s); cubicTo(-0.159f * s, -0.164f * s, -0.167f * s, -0.167f * s, -0.176f * s, -0.171f * s); cubicTo(-0.184f * s, -0.175f * s, -0.19f * s, -0.181f * s, -0.197f * s, -0.187f * s); cubicTo(-0.204f * s, -0.194f * s, -0.212f * s, -0.203f * s, -0.217f * s, -0.21f * s); cubicTo(-0.223f * s, -0.217f * s, -0.226f * s, -0.226f * s, -0.228f * s, -0.229f * s); cubicTo(-0.23f * s, -0.232f * s, -0.229f * s, -0.228f * s, -0.229f * s, -0.227f * s); cubicTo(-0.229f * s, -0.227f * s, -0.231f * s, -0.224f * s, -0.228f * s, -0.227f * s); cubicTo(-0.225f * s, -0.23f * s, -0.219f * s, -0.239f * s, -0.212f * s, -0.246f * s); cubicTo(-0.204f * s, -0.253f * s, -0.193f * s, -0.262f * s, -0.184f * s, -0.268f * s); cubicTo(-0.175f * s, -0.274f * s, -0.167f * s, -0.279f * s, -0.158f * s, -0.283f * s); cubicTo(-0.149f * s, -0.286f * s, -0.141f * s, -0.288f * s, -0.13f * s, -0.289f * s); cubicTo(-0.119f * s, -0.29f * s, -0.105f * s, -0.291f * s, -0.091f * s, -0.291f * s); cubicTo(-0.078f * s, -0.291f * s, -0.062f * s, -0.29f * s, -0.049f * s, -0.289f * s); cubicTo(-0.036f * s, -0.288f * s, -0.024f * s, -0.287f * s, -0.015f * s, -0.285f * s); cubicTo(-0.006f * s, -0.283f * s, 0.0f * s, -0.281f * s, 0.007f * s, -0.278f * s); cubicTo(0.014f * s, -0.276f * s, 0.02f * s, -0.272f * s, 0.027f * s, -0.268f * s); cubicTo(0.034f * s, -0.264f * s, 0.042f * s, -0.259f * s, 0.049f * s, -0.255f * s); cubicTo(0.056f * s, -0.251f * s, 0.045f * s, -0.238f * s, 0.068f * s, -0.245f * s); cubicTo(0.09f * s, -0.252f * s, 0.173f * s, -0.267f * s, 0.186f * s, -0.296f * s); cubicTo(0.2f * s, -0.325f * s, 0.158f * s, -0.398f * s, 0.148f * s, -0.42f * s); cubicTo(0.139f * s, -0.442f * s, 0.138f * s, -0.424f * s, 0.13f * s, -0.427f * s); cubicTo(0.122f * s, -0.43f * s, 0.112f * s, -0.435f * s, 0.099f * s, -0.439f * s); cubicTo(0.087f * s, -0.443f * s, 0.071f * s, -0.449f * s, 0.056f * s, -0.453f * s); cubicTo(0.04f * s, -0.456f * s, 0.023f * s, -0.459f * s, 0.006f * s, -0.46f * s); cubicTo(-0.011f * s, -0.462f * s, -0.027f * s, -0.461f * s, -0.045f * s, -0.461f * s); cubicTo(-0.063f * s, -0.461f * s, -0.082f * s, -0.46f * s, -0.101f * s, -0.458f * s); cubicTo(-0.12f * s, -0.456f * s, -0.141f * s, -0.454f * s, -0.161f * s, -0.449f * s); cubicTo(-0.181f * s, -0.445f * s, -0.201f * s, -0.439f * s, -0.22f * s, -0.43f * s); cubicTo(-0.239f * s, -0.422f * s, -0.256f * s, -0.412f * s, -0.273f * s, -0.4f * s); cubicTo(-0.29f * s, -0.389f * s, -0.307f * s, -0.376f * s, -0.322f * s, -0.361f * s); cubicTo(-0.337f * s, -0.347f * s, -0.352f * s, -0.332f * s, -0.364f * s, -0.312f * s); cubicTo(-0.375f * s, -0.293f * s, -0.386f * s, -0.268f * s, -0.389f * s, -0.245f * s); cubicTo(-0.391f * s, -0.222f * s, -0.386f * s, -0.195f * s, -0.379f * s, -0.174f * s); cubicTo(-0.372f * s, -0.152f * s, -0.36f * s, -0.134f * s, -0.348f * s, -0.117f * s); cubicTo(-0.336f * s, -0.099f * s, -0.321f * s, -0.084f * s, -0.305f * s, -0.069f * s); cubicTo(-0.289f * s, -0.054f * s, -0.27f * s, -0.04f * s, -0.25f * s, -0.029f * s); cubicTo(-0.23f * s, -0.018f * s, -0.206f * s, -0.011f * s, -0.183f * s, -0.005f * s); cubicTo(-0.161f * s, 0.001f * s, -0.137f * s, 0.003f * s, -0.116f * s, 0.006f * s); cubicTo(-0.095f * s, 0.009f * s, -0.075f * s, 0.011f * s, -0.058f * s, 0.014f * s); cubicTo(-0.04f * s, 0.017f * s, -0.027f * s, 0.02f * s, -0.012f * s, 0.025f * s); cubicTo(0.003f * s, 0.029f * s, 0.017f * s, 0.035f * s, 0.03f * s, 0.041f * s); cubicTo(0.044f * s, 0.047f * s, 0.06f * s, 0.053f * s, 0.071f * s, 0.059f * s); cubicTo(0.081f * s, 0.064f * s, 0.089f * s, 0.07f * s, 0.094f * s, 0.073f * s); cubicTo(0.099f * s, 0.076f * s, 0.098f * s, 0.074f * s, 0.099f * s, 0.077f * s); cubicTo(0.1f * s, 0.08f * s, 0.101f * s, 0.084f * s, 0.1f * s, 0.091f * s); cubicTo(0.1f * s, 0.099f * s, 0.1f * s, 0.112f * s, 0.098f * s, 0.122f * s); cubicTo(0.096f * s, 0.132f * s, 0.092f * s, 0.144f * s, 0.088f * s, 0.152f * s); cubicTo(0.085f * s, 0.16f * s, 0.083f * s, 0.164f * s, 0.077f * s, 0.17f * s); cubicTo(0.071f * s, 0.176f * s, 0.063f * s, 0.182f * s, 0.05f * s, 0.188f * s); cubicTo(0.037f * s, 0.194f * s, 0.017f * s, 0.202f * s, -0.001f * s, 0.209f * s); cubicTo(-0.02f * s, 0.215f * s, -0.043f * s, 0.221f * s, -0.061f * s, 0.226f * s); cubicTo(-0.08f * s, 0.231f * s, -0.098f * s, 0.234f * s, -0.115f * s, 0.237f * s); cubicTo(-0.131f * s, 0.24f * s, -0.145f * s, 0.242f * s, -0.16f * s, 0.243f * s); cubicTo(-0.176f * s, 0.243f * s, -0.191f * s, 0.243f * s, -0.206f * s, 0.243f * s); cubicTo(-0.22f * s, 0.242f * s, -0.235f * s, 0.24f * s, -0.248f * s, 0.238f * s); cubicTo(-0.261f * s, 0.235f * s, -0.271f * s, 0.234f * s, -0.282f * s, 0.229f * s); cubicTo(-0.292f * s, 0.225f * s, -0.302f * s, 0.22f * s, -0.312f * s, 0.212f * s); cubicTo(-0.322f * s, 0.203f * s, -0.333f * s, 0.191f * s, -0.343f * s, 0.179f * s); cubicTo(-0.353f * s, 0.168f * s, -0.363f * s, 0.153f * s, -0.371f * s, 0.143f * s); cubicTo(-0.379f * s, 0.133f * s, -0.384f * s, 0.122f * s, -0.391f * s, 0.117f * s); cubicTo(-0.398f * s, 0.112f * s, -0.407f * s, 0.109f * s, -0.412f * s, 0.112f * s); close() }
    val p4 = Path().apply { moveTo(0.3f * s, -0.316f * s); cubicTo(0.3f * s, -0.265f * s, 0.246f * s, -0.224f * s, 0.18f * s, -0.224f * s); cubicTo(0.114f * s, -0.224f * s, 0.06f * s, -0.265f * s, 0.06f * s, -0.316f * s); cubicTo(0.06f * s, -0.367f * s, 0.114f * s, -0.409f * s, 0.18f * s, -0.409f * s); cubicTo(0.246f * s, -0.409f * s, 0.3f * s, -0.367f * s, 0.3f * s, -0.316f * s); close() }
    val p5 = Path().apply { moveTo(0.175f * s, -0.349f * s); cubicTo(0.177f * s, -0.34f * s, 0.157f * s, -0.329f * s, 0.131f * s, -0.325f * s); cubicTo(0.104f * s, -0.32f * s, 0.082f * s, -0.323f * s, 0.08f * s, -0.332f * s); cubicTo(0.079f * s, -0.341f * s, 0.099f * s, -0.352f * s, 0.125f * s, -0.356f * s); cubicTo(0.151f * s, -0.361f * s, 0.174f * s, -0.358f * s, 0.175f * s, -0.349f * s); close() }
    val p6 = Path().apply { moveTo(0.22f * s, -0.236f * s); quadraticTo(0.256f * s, -0.204f * s, 0.292f * s, -0.236f * s) }
    drawPath(p0, sil ?: Color(0xFFE8372E), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF499D4A))
    drawPath(p2, sil ?: Color(0xFF499D4A))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p3, Color(0xFF5BC45C))
        }
        clipPath(p2) {
            drawPath(p4, Color(0xFF5BC45C))
        }
        clipPath(p1) {
            drawCircle(Color(0xFF2E9E44), 0.037f * s, Offset(-0.198f * s, 0.343f * s))
            drawCircle(Color(0xFF2E9E44), 0.047f * s, Offset(0.042f * s, 0.323f * s))
            drawCircle(Color(0xFF2E9E44), 0.048f * s, Offset(0.197f * s, 0.178f * s))
            drawCircle(Color(0xFF2E9E44), 0.048f * s, Offset(0.128f * s, 0.028f * s))
            drawCircle(Color(0xFF2E9E44), 0.048f * s, Offset(-0.085f * s, -0.033f * s))
            drawCircle(Color(0xFF2E9E44), 0.048f * s, Offset(-0.263f * s, -0.124f * s))
            drawCircle(Color(0xFF2E9E44), 0.048f * s, Offset(-0.247f * s, -0.264f * s))
            drawCircle(Color(0xFF2E9E44), 0.05f * s, Offset(-0.076f * s, -0.334f * s))
        }
        clipPath(p2) {
            drawCircle(Color(0xFF2E9E44), 0.037f * s, Offset(-0.198f * s, 0.343f * s))
            drawCircle(Color(0xFF2E9E44), 0.047f * s, Offset(0.042f * s, 0.323f * s))
            drawCircle(Color(0xFF2E9E44), 0.048f * s, Offset(0.197f * s, 0.178f * s))
            drawCircle(Color(0xFF2E9E44), 0.048f * s, Offset(0.128f * s, 0.028f * s))
            drawCircle(Color(0xFF2E9E44), 0.048f * s, Offset(-0.085f * s, -0.033f * s))
            drawCircle(Color(0xFF2E9E44), 0.048f * s, Offset(-0.263f * s, -0.124f * s))
            drawCircle(Color(0xFF2E9E44), 0.048f * s, Offset(-0.247f * s, -0.264f * s))
            drawCircle(Color(0xFF2E9E44), 0.05f * s, Offset(-0.076f * s, -0.334f * s))
        }
        drawPath(p5, Color(0x80FFFFFF))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.044f * s, Offset(0.2f * s, -0.316f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.044f * s, Offset(0.2f * s, -0.316f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.026f * s, Offset(0.213f * s, -0.308f * s))
        drawCircle(Color(0xFFFFFFFF), 0.009f * s, Offset(0.203f * s, -0.319f * s))
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artTrefle(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.049f * s); quadraticTo(0.025f * s, 0.238f * s, 0.169f * s, 0.39f * s) }
    val p1 = Path().apply { moveTo(0.0f * s, -0.066f * s); cubicTo(0.013f * s, 0.037f * s, 0.045f * s, 0.185f * s, -0.032f * s, 0.263f * s); cubicTo(-0.09f * s, 0.321f * s, -0.155f * s, 0.308f * s, -0.194f * s, 0.269f * s); cubicTo(-0.232f * s, 0.231f * s, -0.232f * s, 0.185f * s, -0.213f * s, 0.147f * s); cubicTo(-0.252f * s, 0.166f * s, -0.297f * s, 0.166f * s, -0.335f * s, 0.127f * s); cubicTo(-0.374f * s, 0.089f * s, -0.387f * s, 0.024f * s, -0.329f * s, -0.034f * s); cubicTo(-0.252f * s, -0.111f * s, -0.103f * s, -0.079f * s, 0.0f * s, -0.066f * s); close() }
    val p2 = Path().apply { moveTo(-0.012f * s, -0.03f * s); cubicTo(0.001f * s, 0.073f * s, 0.033f * s, 0.221f * s, -0.044f * s, 0.299f * s); cubicTo(-0.102f * s, 0.357f * s, -0.167f * s, 0.344f * s, -0.205f * s, 0.305f * s); cubicTo(-0.244f * s, 0.266f * s, -0.244f * s, 0.221f * s, -0.225f * s, 0.183f * s); cubicTo(-0.264f * s, 0.202f * s, -0.309f * s, 0.202f * s, -0.347f * s, 0.163f * s); cubicTo(-0.386f * s, 0.125f * s, -0.399f * s, 0.06f * s, -0.341f * s, 0.002f * s); cubicTo(-0.264f * s, -0.075f * s, -0.115f * s, -0.043f * s, -0.012f * s, -0.03f * s); close() }
    val p3 = Path().apply { moveTo(-0.148f * s, 0.168f * s); cubicTo(-0.127f * s, 0.158f * s, -0.105f * s, 0.16f * s, -0.099f * s, 0.173f * s); cubicTo(-0.093f * s, 0.186f * s, -0.105f * s, 0.204f * s, -0.127f * s, 0.214f * s); cubicTo(-0.148f * s, 0.224f * s, -0.17f * s, 0.221f * s, -0.176f * s, 0.209f * s); cubicTo(-0.182f * s, 0.196f * s, -0.169f * s, 0.178f * s, -0.148f * s, 0.168f * s); close() }
    val p4 = Path().apply { moveTo(0.0f * s, -0.066f * s); cubicTo(0.103f * s, -0.079f * s, 0.252f * s, -0.111f * s, 0.329f * s, -0.034f * s); cubicTo(0.387f * s, 0.024f * s, 0.374f * s, 0.089f * s, 0.336f * s, 0.127f * s); cubicTo(0.297f * s, 0.166f * s, 0.252f * s, 0.166f * s, 0.213f * s, 0.147f * s); cubicTo(0.232f * s, 0.185f * s, 0.232f * s, 0.231f * s, 0.194f * s, 0.269f * s); cubicTo(0.155f * s, 0.308f * s, 0.09f * s, 0.321f * s, 0.032f * s, 0.263f * s); cubicTo(-0.045f * s, 0.185f * s, -0.013f * s, 0.037f * s, 0.0f * s, -0.066f * s); close() }
    val p5 = Path().apply { moveTo(0.036f * s, -0.054f * s); cubicTo(0.139f * s, -0.067f * s, 0.288f * s, -0.099f * s, 0.365f * s, -0.022f * s); cubicTo(0.423f * s, 0.036f * s, 0.41f * s, 0.101f * s, 0.371f * s, 0.139f * s); cubicTo(0.333f * s, 0.178f * s, 0.288f * s, 0.178f * s, 0.249f * s, 0.159f * s); cubicTo(0.268f * s, 0.197f * s, 0.268f * s, 0.243f * s, 0.229f * s, 0.281f * s); cubicTo(0.191f * s, 0.32f * s, 0.126f * s, 0.333f * s, 0.068f * s, 0.275f * s); cubicTo(-0.009f * s, 0.197f * s, 0.023f * s, 0.049f * s, 0.036f * s, -0.054f * s); close() }
    val p6 = Path().apply { moveTo(0.234f * s, 0.082f * s); cubicTo(0.224f * s, 0.061f * s, 0.226f * s, 0.039f * s, 0.239f * s, 0.033f * s); cubicTo(0.252f * s, 0.027f * s, 0.27f * s, 0.039f * s, 0.28f * s, 0.061f * s); cubicTo(0.29f * s, 0.082f * s, 0.288f * s, 0.104f * s, 0.275f * s, 0.11f * s); cubicTo(0.262f * s, 0.115f * s, 0.244f * s, 0.103f * s, 0.234f * s, 0.082f * s); close() }
    val p7 = Path().apply { moveTo(0.0f * s, -0.066f * s); cubicTo(-0.103f * s, -0.053f * s, -0.252f * s, -0.021f * s, -0.329f * s, -0.098f * s); cubicTo(-0.387f * s, -0.156f * s, -0.374f * s, -0.221f * s, -0.335f * s, -0.26f * s); cubicTo(-0.297f * s, -0.298f * s, -0.252f * s, -0.298f * s, -0.213f * s, -0.279f * s); cubicTo(-0.232f * s, -0.318f * s, -0.232f * s, -0.363f * s, -0.194f * s, -0.402f * s); cubicTo(-0.155f * s, -0.44f * s, -0.09f * s, -0.453f * s, -0.032f * s, -0.395f * s); cubicTo(0.045f * s, -0.318f * s, 0.013f * s, -0.169f * s, 0.0f * s, -0.066f * s); close() }
    val p8 = Path().apply { moveTo(-0.036f * s, -0.078f * s); cubicTo(-0.139f * s, -0.065f * s, -0.287f * s, -0.033f * s, -0.365f * s, -0.11f * s); cubicTo(-0.423f * s, -0.168f * s, -0.41f * s, -0.233f * s, -0.371f * s, -0.272f * s); cubicTo(-0.333f * s, -0.31f * s, -0.287f * s, -0.31f * s, -0.249f * s, -0.291f * s); cubicTo(-0.268f * s, -0.33f * s, -0.268f * s, -0.375f * s, -0.229f * s, -0.414f * s); cubicTo(-0.191f * s, -0.452f * s, -0.126f * s, -0.465f * s, -0.068f * s, -0.407f * s); cubicTo(0.009f * s, -0.33f * s, -0.023f * s, -0.181f * s, -0.036f * s, -0.078f * s); close() }
    val p9 = Path().apply { moveTo(-0.234f * s, -0.214f * s); cubicTo(-0.224f * s, -0.193f * s, -0.226f * s, -0.171f * s, -0.239f * s, -0.165f * s); cubicTo(-0.252f * s, -0.159f * s, -0.27f * s, -0.172f * s, -0.28f * s, -0.193f * s); cubicTo(-0.29f * s, -0.214f * s, -0.287f * s, -0.236f * s, -0.275f * s, -0.242f * s); cubicTo(-0.262f * s, -0.248f * s, -0.244f * s, -0.235f * s, -0.234f * s, -0.214f * s); close() }
    val p10 = Path().apply { moveTo(0.0f * s, -0.066f * s); cubicTo(-0.013f * s, -0.169f * s, -0.045f * s, -0.318f * s, 0.032f * s, -0.395f * s); cubicTo(0.09f * s, -0.453f * s, 0.155f * s, -0.44f * s, 0.194f * s, -0.402f * s); cubicTo(0.232f * s, -0.363f * s, 0.232f * s, -0.318f * s, 0.213f * s, -0.279f * s); cubicTo(0.252f * s, -0.298f * s, 0.297f * s, -0.298f * s, 0.336f * s, -0.26f * s); cubicTo(0.374f * s, -0.221f * s, 0.387f * s, -0.156f * s, 0.329f * s, -0.098f * s); cubicTo(0.252f * s, -0.021f * s, 0.103f * s, -0.053f * s, 0.0f * s, -0.066f * s); close() }
    val p11 = Path().apply { moveTo(0.012f * s, -0.102f * s); cubicTo(-0.001f * s, -0.205f * s, -0.033f * s, -0.354f * s, 0.044f * s, -0.431f * s); cubicTo(0.102f * s, -0.489f * s, 0.167f * s, -0.476f * s, 0.206f * s, -0.437f * s); cubicTo(0.244f * s, -0.399f * s, 0.244f * s, -0.354f * s, 0.225f * s, -0.315f * s); cubicTo(0.264f * s, -0.334f * s, 0.309f * s, -0.334f * s, 0.348f * s, -0.296f * s); cubicTo(0.386f * s, -0.257f * s, 0.399f * s, -0.192f * s, 0.341f * s, -0.134f * s); cubicTo(0.264f * s, -0.057f * s, 0.115f * s, -0.089f * s, 0.012f * s, -0.102f * s); close() }
    val p12 = Path().apply { moveTo(0.148f * s, -0.3f * s); cubicTo(0.127f * s, -0.29f * s, 0.105f * s, -0.293f * s, 0.099f * s, -0.305f * s); cubicTo(0.093f * s, -0.318f * s, 0.106f * s, -0.336f * s, 0.127f * s, -0.346f * s); cubicTo(0.148f * s, -0.356f * s, 0.17f * s, -0.354f * s, 0.176f * s, -0.341f * s); cubicTo(0.182f * s, -0.328f * s, 0.169f * s, -0.31f * s, 0.148f * s, -0.3f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.142f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF4F9A3A), style = Stroke(0.07f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF328E3B))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFF3FB24A))
        }
        drawPath(p3, Color(0x8CFFFFFF))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawLine(Color(0x73FFFFFF), Offset(-0.06f * s, -0.006f * s), Offset(-0.161f * s, 0.095f * s), 0.03f * s, StrokeCap.Round)
    }
    drawPath(p4, sil ?: Color(0xFF328E3B))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFF3FB24A))
        }
        drawPath(p6, Color(0x8CFFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawLine(Color(0x73FFFFFF), Offset(0.06f * s, -0.006f * s), Offset(0.161f * s, 0.095f * s), 0.03f * s, StrokeCap.Round)
    }
    drawPath(p7, sil ?: Color(0xFF328E3B))
    if (sil == null) {
        clipPath(p7) {
            drawPath(p8, Color(0xFF3FB24A))
        }
        drawPath(p9, Color(0x8CFFFFFF))
    }
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawLine(Color(0x73FFFFFF), Offset(-0.06f * s, -0.126f * s), Offset(-0.161f * s, -0.228f * s), 0.03f * s, StrokeCap.Round)
    }
    drawPath(p10, sil ?: Color(0xFF328E3B))
    if (sil == null) {
        clipPath(p10) {
            drawPath(p11, Color(0xFF3FB24A))
        }
        drawPath(p12, Color(0x8CFFFFFF))
    }
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawLine(Color(0x73FFFFFF), Offset(0.06f * s, -0.126f * s), Offset(0.161f * s, -0.228f * s), 0.03f * s, StrokeCap.Round)
    }
}

private fun DrawScope.artPalmier(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.258f * s, 0.38f * s); cubicTo(0.258f * s, 0.409f * s, 0.143f * s, 0.432f * s, 0.0f * s, 0.432f * s); cubicTo(-0.142f * s, 0.432f * s, -0.258f * s, 0.409f * s, -0.258f * s, 0.38f * s); cubicTo(-0.258f * s, 0.352f * s, -0.142f * s, 0.329f * s, 0.0f * s, 0.329f * s); cubicTo(0.143f * s, 0.329f * s, 0.258f * s, 0.352f * s, 0.258f * s, 0.38f * s); close() }
    val p1 = Path().apply { moveTo(-0.06f * s, 0.372f * s); quadraticTo(0.06f * s, 0.105f * s, 0.026f * s, -0.178f * s) }
    val p2 = Path().apply { moveTo(0.026f * s, -0.196f * s); quadraticTo(-0.084f * s, -0.382f * s, 0.215f * s, -0.434f * s); quadraticTo(0.166f * s, -0.312f * s, 0.026f * s, -0.196f * s); close() }
    val p3 = Path().apply { moveTo(0.026f * s, -0.196f * s); quadraticTo(0.087f * s, -0.43f * s, 0.427f * s, -0.258f * s); quadraticTo(0.249f * s, -0.225f * s, 0.026f * s, -0.196f * s); close() }
    val p4 = Path().apply { moveTo(0.026f * s, -0.196f * s); quadraticTo(0.203f * s, -0.344f * s, 0.342f * s, 0.022f * s); quadraticTo(0.201f * s, -0.084f * s, 0.026f * s, -0.196f * s); close() }
    val p5 = Path().apply { moveTo(0.026f * s, -0.196f * s); quadraticTo(0.216f * s, -0.256f * s, 0.123f * s, 0.031f * s); quadraticTo(0.06f * s, -0.049f * s, 0.026f * s, -0.196f * s); close() }
    val p6 = Path().apply { moveTo(-0.026f * s, -0.196f * s); quadraticTo(0.084f * s, -0.382f * s, -0.215f * s, -0.434f * s); quadraticTo(-0.166f * s, -0.312f * s, -0.026f * s, -0.196f * s); close() }
    val p7 = Path().apply { moveTo(-0.026f * s, -0.196f * s); quadraticTo(-0.087f * s, -0.43f * s, -0.426f * s, -0.258f * s); quadraticTo(-0.249f * s, -0.225f * s, -0.026f * s, -0.196f * s); close() }
    val p8 = Path().apply { moveTo(-0.026f * s, -0.196f * s); quadraticTo(-0.203f * s, -0.344f * s, -0.342f * s, 0.022f * s); quadraticTo(-0.201f * s, -0.084f * s, -0.026f * s, -0.196f * s); close() }
    val p9 = Path().apply { moveTo(-0.026f * s, -0.196f * s); quadraticTo(-0.216f * s, -0.256f * s, -0.123f * s, 0.031f * s); quadraticTo(-0.06f * s, -0.049f * s, -0.026f * s, -0.196f * s); close() }
    drawPath(p0, sil ?: Color(0xFFF2D48B))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.182f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFB07A45), style = Stroke(0.11f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawLine(Color(0xFF7A4A2A), Offset(-0.057f * s, 0.275f * s), Offset(0.02f * s, 0.254f * s), 0.024f * s, StrokeCap.Round)
        drawLine(Color(0xFF7A4A2A), Offset(-0.027f * s, 0.166f * s), Offset(0.05f * s, 0.145f * s), 0.024f * s, StrokeCap.Round)
        drawLine(Color(0xFF7A4A2A), Offset(-0.01f * s, 0.056f * s), Offset(0.067f * s, 0.035f * s), 0.024f * s, StrokeCap.Round)
        drawLine(Color(0xFF7A4A2A), Offset(-0.005f * s, -0.055f * s), Offset(0.072f * s, -0.076f * s), 0.024f * s, StrokeCap.Round)
    }
    drawPath(p2, sil ?: Color(0xFF3FAE49))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF52C25A))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF3FAE49))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF52C25A))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF3FAE49))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF52C25A))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF3FAE49))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFF52C25A))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF7A4A2A), 0.047f * s, Offset(-0.017f * s, -0.144f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.047f * s, Offset(-0.017f * s, -0.144f * s), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF7A4A2A), 0.047f * s, Offset(0.077f * s, -0.135f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.047f * s, Offset(0.077f * s, -0.135f * s), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artTracteur(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.264f * s, -0.077f * s); lineTo(0.264f * s, -0.295f * s) }
    val p1 = Path().apply { moveTo(-0.026f * s, -0.077f * s); quadraticTo(-0.026f * s, -0.095f * s, -0.008f * s, -0.095f * s); lineTo(0.264f * s, -0.095f * s); quadraticTo(0.319f * s, -0.095f * s, 0.36f * s, -0.06f * s); lineTo(0.393f * s, -0.033f * s); quadraticTo(0.427f * s, -0.004f * s, 0.427f * s, 0.041f * s); lineTo(0.427f * s, 0.15f * s); quadraticTo(0.427f * s, 0.177f * s, 0.4f * s, 0.177f * s); lineTo(-0.008f * s, 0.177f * s); quadraticTo(-0.026f * s, 0.177f * s, -0.026f * s, 0.159f * s); close() }
    val p2 = Path().apply { moveTo(-0.044f * s, -0.104f * s); quadraticTo(-0.044f * s, -0.122f * s, -0.026f * s, -0.122f * s); lineTo(0.246f * s, -0.122f * s); quadraticTo(0.301f * s, -0.122f * s, 0.342f * s, -0.088f * s); lineTo(0.374f * s, -0.061f * s); quadraticTo(0.409f * s, -0.032f * s, 0.409f * s, 0.014f * s); lineTo(0.409f * s, 0.122f * s); quadraticTo(0.409f * s, 0.15f * s, 0.382f * s, 0.15f * s); lineTo(-0.026f * s, 0.15f * s); quadraticTo(-0.044f * s, 0.15f * s, -0.044f * s, 0.131f * s); close() }
    val p3 = Path().apply { moveTo(-0.357f * s, -0.403f * s); lineTo(0.033f * s, -0.403f * s); cubicTo(0.051f * s, -0.403f * s, 0.065f * s, -0.389f * s, 0.065f * s, -0.372f * s); lineTo(0.065f * s, -0.367f * s); cubicTo(0.065f * s, -0.35f * s, 0.051f * s, -0.335f * s, 0.033f * s, -0.335f * s); lineTo(-0.357f * s, -0.335f * s); cubicTo(-0.374f * s, -0.335f * s, -0.388f * s, -0.35f * s, -0.388f * s, -0.367f * s); lineTo(-0.388f * s, -0.372f * s); cubicTo(-0.388f * s, -0.389f * s, -0.374f * s, -0.403f * s, -0.357f * s, -0.403f * s); close() }
    val p4 = Path().apply { moveTo(-0.352f * s, -0.322f * s); quadraticTo(-0.352f * s, -0.34f * s, -0.334f * s, -0.34f * s); lineTo(-0.008f * s, -0.34f * s); quadraticTo(0.01f * s, -0.34f * s, 0.01f * s, -0.322f * s); lineTo(0.01f * s, 0.159f * s); quadraticTo(0.01f * s, 0.177f * s, -0.008f * s, 0.177f * s); lineTo(-0.334f * s, 0.177f * s); quadraticTo(-0.352f * s, 0.177f * s, -0.352f * s, 0.159f * s); close() }
    val p5 = Path().apply { moveTo(-0.37f * s, -0.349f * s); quadraticTo(-0.37f * s, -0.367f * s, -0.352f * s, -0.367f * s); lineTo(-0.026f * s, -0.367f * s); quadraticTo(-0.008f * s, -0.367f * s, -0.008f * s, -0.349f * s); lineTo(-0.008f * s, 0.131f * s); quadraticTo(-0.008f * s, 0.15f * s, -0.026f * s, 0.15f * s); lineTo(-0.352f * s, 0.15f * s); quadraticTo(-0.37f * s, 0.15f * s, -0.37f * s, 0.131f * s); close() }
    val p6 = Path().apply { moveTo(-0.266f * s, -0.285f * s); lineTo(-0.058f * s, -0.285f * s); cubicTo(-0.045f * s, -0.285f * s, -0.035f * s, -0.275f * s, -0.035f * s, -0.263f * s); lineTo(-0.035f * s, -0.091f * s); cubicTo(-0.035f * s, -0.078f * s, -0.045f * s, -0.068f * s, -0.058f * s, -0.068f * s); lineTo(-0.266f * s, -0.068f * s); cubicTo(-0.278f * s, -0.068f * s, -0.289f * s, -0.078f * s, -0.289f * s, -0.091f * s); lineTo(-0.289f * s, -0.263f * s); cubicTo(-0.289f * s, -0.275f * s, -0.278f * s, -0.285f * s, -0.266f * s, -0.285f * s); close() }
    val p7 = Path().apply { moveTo(0.441f * s, 0.032f * s); cubicTo(0.441f * s, 0.054f * s, 0.429f * s, 0.073f * s, 0.414f * s, 0.073f * s); cubicTo(0.399f * s, 0.073f * s, 0.387f * s, 0.054f * s, 0.387f * s, 0.032f * s); cubicTo(0.387f * s, 0.009f * s, 0.399f * s, -0.009f * s, 0.414f * s, -0.009f * s); cubicTo(0.429f * s, -0.009f * s, 0.441f * s, 0.009f * s, 0.441f * s, 0.032f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.132f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF41465F), style = Stroke(0.06f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF328E3B))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFF3FB24A))
        }
        drawOval(Color(0x80FFFFFF), Offset(0.01f * s, -0.063f * s), Size(0.145f * s, 0.027f * s))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2A8A34))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF328E3B))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFF3FB24A))
        }
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFBFE3FF))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFFFF2B0))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF41465F), 0.245f * s, Offset(-0.189f * s, 0.159f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.245f * s, Offset(-0.189f * s, 0.159f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFD23F), 0.109f * s, Offset(-0.189f * s, 0.159f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.109f * s, Offset(-0.189f * s, 0.159f * s), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF41465F), 0.036f * s, Offset(-0.189f * s, 0.159f * s))
    }
    drawCircle(sil ?: Color(0xFF41465F), 0.127f * s, Offset(0.291f * s, 0.258f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.127f * s, Offset(0.291f * s, 0.258f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFD23F), 0.059f * s, Offset(0.291f * s, 0.258f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.059f * s, Offset(0.291f * s, 0.258f * s), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artDauphin(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.299f * s, 0.15f * s); cubicTo(-0.295f * s, 0.154f * s, -0.286f * s, 0.154f * s, -0.278f * s, 0.151f * s); cubicTo(-0.271f * s, 0.149f * s, -0.264f * s, 0.142f * s, -0.254f * s, 0.136f * s); cubicTo(-0.245f * s, 0.13f * s, -0.233f * s, 0.122f * s, -0.221f * s, 0.116f * s); cubicTo(-0.208f * s, 0.109f * s, -0.195f * s, 0.101f * s, -0.181f * s, 0.095f * s); cubicTo(-0.168f * s, 0.089f * s, -0.154f * s, 0.083f * s, -0.138f * s, 0.078f * s); cubicTo(-0.123f * s, 0.072f * s, -0.105f * s, 0.067f * s, -0.089f * s, 0.062f * s); cubicTo(-0.073f * s, 0.058f * s, -0.057f * s, 0.054f * s, -0.042f * s, 0.051f * s); cubicTo(-0.027f * s, 0.047f * s, -0.015f * s, 0.045f * s, -0.001f * s, 0.042f * s); cubicTo(0.014f * s, 0.038f * s, 0.028f * s, 0.035f * s, 0.043f * s, 0.032f * s); cubicTo(0.057f * s, 0.028f * s, 0.073f * s, 0.025f * s, 0.087f * s, 0.021f * s); cubicTo(0.1f * s, 0.018f * s, 0.112f * s, 0.016f * s, 0.123f * s, 0.013f * s); cubicTo(0.134f * s, 0.011f * s, 0.142f * s, 0.009f * s, 0.151f * s, 0.006f * s); cubicTo(0.16f * s, 0.004f * s, 0.169f * s, 0.002f * s, 0.178f * s, -0.001f * s); cubicTo(0.187f * s, -0.003f * s, 0.197f * s, -0.005f * s, 0.204f * s, -0.008f * s); cubicTo(0.212f * s, -0.01f * s, 0.218f * s, -0.013f * s, 0.225f * s, -0.015f * s); cubicTo(0.232f * s, -0.018f * s, 0.239f * s, -0.02f * s, 0.249f * s, -0.021f * s); cubicTo(0.258f * s, -0.022f * s, 0.271f * s, -0.021f * s, 0.283f * s, -0.022f * s); cubicTo(0.295f * s, -0.022f * s, 0.309f * s, -0.022f * s, 0.321f * s, -0.023f * s); cubicTo(0.333f * s, -0.025f * s, 0.345f * s, -0.027f * s, 0.355f * s, -0.029f * s); cubicTo(0.365f * s, -0.031f * s, 0.375f * s, -0.034f * s, 0.383f * s, -0.036f * s); cubicTo(0.39f * s, -0.038f * s, 0.394f * s, -0.036f * s, 0.401f * s, -0.041f * s); cubicTo(0.408f * s, -0.045f * s, 0.422f * s, -0.056f * s, 0.422f * s, -0.064f * s); cubicTo(0.422f * s, -0.072f * s, 0.407f * s, -0.083f * s, 0.401f * s, -0.087f * s); cubicTo(0.394f * s, -0.092f * s, 0.39f * s, -0.09f * s, 0.383f * s, -0.092f * s); cubicTo(0.375f * s, -0.094f * s, 0.366f * s, -0.096f * s, 0.358f * s, -0.099f * s); cubicTo(0.35f * s, -0.101f * s, 0.342f * s, -0.105f * s, 0.336f * s, -0.109f * s); cubicTo(0.33f * s, -0.113f * s, 0.327f * s, -0.118f * s, 0.322f * s, -0.125f * s); cubicTo(0.317f * s, -0.132f * s, 0.315f * s, -0.14f * s, 0.307f * s, -0.149f * s); cubicTo(0.299f * s, -0.158f * s, 0.289f * s, -0.17f * s, 0.276f * s, -0.178f * s); cubicTo(0.264f * s, -0.187f * s, 0.249f * s, -0.194f * s, 0.234f * s, -0.2f * s); cubicTo(0.219f * s, -0.206f * s, 0.205f * s, -0.21f * s, 0.188f * s, -0.214f * s); cubicTo(0.172f * s, -0.217f * s, 0.155f * s, -0.22f * s, 0.137f * s, -0.221f * s); cubicTo(0.119f * s, -0.221f * s, 0.099f * s, -0.22f * s, 0.08f * s, -0.217f * s); cubicTo(0.061f * s, -0.214f * s, 0.041f * s, -0.209f * s, 0.022f * s, -0.203f * s); cubicTo(0.003f * s, -0.196f * s, -0.017f * s, -0.188f * s, -0.036f * s, -0.179f * s); cubicTo(-0.055f * s, -0.17f * s, -0.074f * s, -0.159f * s, -0.092f * s, -0.147f * s); cubicTo(-0.109f * s, -0.135f * s, -0.126f * s, -0.122f * s, -0.14f * s, -0.107f * s); cubicTo(-0.155f * s, -0.092f * s, -0.168f * s, -0.074f * s, -0.179f * s, -0.058f * s); cubicTo(-0.191f * s, -0.042f * s, -0.2f * s, -0.026f * s, -0.21f * s, -0.011f * s); cubicTo(-0.22f * s, 0.004f * s, -0.229f * s, 0.017f * s, -0.238f * s, 0.031f * s); cubicTo(-0.247f * s, 0.044f * s, -0.256f * s, 0.058f * s, -0.264f * s, 0.07f * s); cubicTo(-0.272f * s, 0.083f * s, -0.28f * s, 0.096f * s, -0.286f * s, 0.105f * s); cubicTo(-0.292f * s, 0.115f * s, -0.298f * s, 0.122f * s, -0.3f * s, 0.129f * s); cubicTo(-0.303f * s, 0.137f * s, -0.303f * s, 0.147f * s, -0.299f * s, 0.15f * s); close() }
    val p1 = Path().apply { moveTo(0.075f * s, -0.144f * s); cubicTo(0.003f * s, -0.174f * s, -0.061f * s, -0.239f * s, -0.137f * s, -0.284f * s); cubicTo(-0.164f * s, -0.197f * s, -0.145f * s, -0.121f * s, -0.122f * s, -0.062f * s); close() }
    val p2 = Path().apply { moveTo(-0.274f * s, 0.136f * s); quadraticTo(-0.305f * s, 0.015f * s, -0.422f * s, 0.061f * s); quadraticTo(-0.391f * s, 0.182f * s, -0.274f * s, 0.136f * s); close() }
    val p3 = Path().apply { moveTo(-0.274f * s, 0.136f * s); quadraticTo(-0.395f * s, 0.168f * s, -0.35f * s, 0.284f * s); quadraticTo(-0.229f * s, 0.253f * s, -0.274f * s, 0.136f * s); close() }
    val p4 = Path().apply { moveTo(0.094f * s, -0.004f * s); quadraticTo(-0.007f * s, 0.071f * s, 0.044f * s, 0.186f * s); quadraticTo(0.145f * s, 0.111f * s, 0.094f * s, -0.004f * s); close() }
    val p5 = Path().apply { moveTo(-0.327f * s, 0.117f * s); cubicTo(-0.324f * s, 0.121f * s, -0.314f * s, 0.12f * s, -0.306f * s, 0.118f * s); cubicTo(-0.299f * s, 0.116f * s, -0.292f * s, 0.109f * s, -0.283f * s, 0.103f * s); cubicTo(-0.273f * s, 0.097f * s, -0.261f * s, 0.089f * s, -0.249f * s, 0.082f * s); cubicTo(-0.237f * s, 0.075f * s, -0.224f * s, 0.068f * s, -0.21f * s, 0.062f * s); cubicTo(-0.196f * s, 0.055f * s, -0.182f * s, 0.05f * s, -0.167f * s, 0.045f * s); cubicTo(-0.151f * s, 0.039f * s, -0.134f * s, 0.034f * s, -0.118f * s, 0.029f * s); cubicTo(-0.102f * s, 0.025f * s, -0.085f * s, 0.021f * s, -0.071f * s, 0.017f * s); cubicTo(-0.056f * s, 0.014f * s, -0.043f * s, 0.012f * s, -0.029f * s, 0.008f * s); cubicTo(-0.015f * s, 0.005f * s, 0.0f * s, 0.002f * s, 0.014f * s, -0.002f * s); cubicTo(0.029f * s, -0.005f * s, 0.045f * s, -0.009f * s, 0.058f * s, -0.012f * s); cubicTo(0.072f * s, -0.015f * s, 0.084f * s, -0.017f * s, 0.095f * s, -0.02f * s); cubicTo(0.105f * s, -0.023f * s, 0.113f * s, -0.025f * s, 0.123f * s, -0.027f * s); cubicTo(0.132f * s, -0.029f * s, 0.141f * s, -0.032f * s, 0.15f * s, -0.034f * s); cubicTo(0.159f * s, -0.036f * s, 0.168f * s, -0.038f * s, 0.176f * s, -0.041f * s); cubicTo(0.184f * s, -0.043f * s, 0.189f * s, -0.046f * s, 0.197f * s, -0.048f * s); cubicTo(0.204f * s, -0.051f * s, 0.211f * s, -0.053f * s, 0.22f * s, -0.054f * s); cubicTo(0.23f * s, -0.055f * s, 0.243f * s, -0.054f * s, 0.255f * s, -0.055f * s); cubicTo(0.267f * s, -0.055f * s, 0.28f * s, -0.055f * s, 0.292f * s, -0.057f * s); cubicTo(0.304f * s, -0.058f * s, 0.316f * s, -0.06f * s, 0.327f * s, -0.062f * s); cubicTo(0.337f * s, -0.064f * s, 0.347f * s, -0.067f * s, 0.354f * s, -0.069f * s); cubicTo(0.362f * s, -0.071f * s, 0.366f * s, -0.069f * s, 0.373f * s, -0.074f * s); cubicTo(0.379f * s, -0.078f * s, 0.394f * s, -0.09f * s, 0.394f * s, -0.097f * s); cubicTo(0.393f * s, -0.105f * s, 0.379f * s, -0.116f * s, 0.372f * s, -0.121f * s); cubicTo(0.366f * s, -0.125f * s, 0.361f * s, -0.123f * s, 0.354f * s, -0.125f * s); cubicTo(0.347f * s, -0.127f * s, 0.337f * s, -0.129f * s, 0.329f * s, -0.132f * s); cubicTo(0.322f * s, -0.135f * s, 0.313f * s, -0.138f * s, 0.307f * s, -0.142f * s); cubicTo(0.301f * s, -0.147f * s, 0.298f * s, -0.151f * s, 0.294f * s, -0.158f * s); cubicTo(0.289f * s, -0.165f * s, 0.286f * s, -0.173f * s, 0.278f * s, -0.182f * s); cubicTo(0.271f * s, -0.191f * s, 0.26f * s, -0.203f * s, 0.248f * s, -0.212f * s); cubicTo(0.236f * s, -0.22f * s, 0.22f * s, -0.227f * s, 0.206f * s, -0.233f * s); cubicTo(0.191f * s, -0.239f * s, 0.176f * s, -0.243f * s, 0.16f * s, -0.247f * s); cubicTo(0.144f * s, -0.25f * s, 0.126f * s, -0.254f * s, 0.108f * s, -0.254f * s); cubicTo(0.09f * s, -0.255f * s, 0.071f * s, -0.254f * s, 0.051f * s, -0.25f * s); cubicTo(0.032f * s, -0.247f * s, 0.013f * s, -0.242f * s, -0.007f * s, -0.236f * s); cubicTo(-0.026f * s, -0.229f * s, -0.046f * s, -0.221f * s, -0.065f * s, -0.212f * s); cubicTo(-0.083f * s, -0.203f * s, -0.103f * s, -0.193f * s, -0.12f * s, -0.181f * s); cubicTo(-0.138f * s, -0.169f * s, -0.154f * s, -0.155f * s, -0.169f * s, -0.14f * s); cubicTo(-0.183f * s, -0.125f * s, -0.196f * s, -0.108f * s, -0.208f * s, -0.092f * s); cubicTo(-0.219f * s, -0.076f * s, -0.229f * s, -0.059f * s, -0.238f * s, -0.044f * s); cubicTo(-0.248f * s, -0.029f * s, -0.257f * s, -0.016f * s, -0.266f * s, -0.003f * s); cubicTo(-0.275f * s, 0.011f * s, -0.284f * s, 0.025f * s, -0.292f * s, 0.037f * s); cubicTo(-0.3f * s, 0.05f * s, -0.308f * s, 0.062f * s, -0.314f * s, 0.072f * s); cubicTo(-0.32f * s, 0.082f * s, -0.326f * s, 0.089f * s, -0.329f * s, 0.096f * s); cubicTo(-0.331f * s, 0.104f * s, -0.331f * s, 0.113f * s, -0.327f * s, 0.117f * s); close() }
    val p6 = Path().apply { moveTo(0.047f * s, -0.177f * s); cubicTo(-0.025f * s, -0.207f * s, -0.09f * s, -0.272f * s, -0.166f * s, -0.317f * s); cubicTo(-0.192f * s, -0.23f * s, -0.173f * s, -0.154f * s, -0.151f * s, -0.096f * s); close() }
    val p7 = Path().apply { moveTo(-0.302f * s, 0.103f * s); quadraticTo(-0.334f * s, -0.018f * s, -0.45f * s, 0.027f * s); quadraticTo(-0.419f * s, 0.149f * s, -0.302f * s, 0.103f * s); close() }
    val p8 = Path().apply { moveTo(-0.302f * s, 0.103f * s); quadraticTo(-0.424f * s, 0.134f * s, -0.378f * s, 0.251f * s); quadraticTo(-0.257f * s, 0.22f * s, -0.302f * s, 0.103f * s); close() }
    val p9 = Path().apply { moveTo(0.065f * s, -0.037f * s); quadraticTo(-0.035f * s, 0.038f * s, 0.016f * s, 0.153f * s); quadraticTo(0.116f * s, 0.078f * s, 0.065f * s, -0.037f * s); close() }
    val p10 = Path().apply { moveTo(0.503f * s, 0.023f * s); cubicTo(0.517f * s, 0.078f * s, 0.341f * s, 0.168f * s, 0.111f * s, 0.226f * s); cubicTo(-0.12f * s, 0.283f * s, -0.317f * s, 0.286f * s, -0.331f * s, 0.231f * s); cubicTo(-0.345f * s, 0.177f * s, -0.169f * s, 0.086f * s, 0.062f * s, 0.029f * s); cubicTo(0.292f * s, -0.029f * s, 0.49f * s, -0.031f * s, 0.503f * s, 0.023f * s); close() }
    val p11 = Path().apply { moveTo(0.001f * s, -0.11f * s); cubicTo(0.006f * s, -0.1f * s, -0.029f * s, -0.075f * s, -0.076f * s, -0.054f * s); cubicTo(-0.124f * s, -0.033f * s, -0.166f * s, -0.024f * s, -0.17f * s, -0.034f * s); cubicTo(-0.174f * s, -0.044f * s, -0.14f * s, -0.069f * s, -0.092f * s, -0.09f * s); cubicTo(-0.045f * s, -0.111f * s, -0.003f * s, -0.12f * s, 0.001f * s, -0.11f * s); close() }
    val p12 = Path().apply { moveTo(0.236f * s, -0.136f * s); cubicTo(0.241f * s, -0.116f * s, 0.229f * s, -0.095f * s, 0.209f * s, -0.09f * s); cubicTo(0.189f * s, -0.085f * s, 0.169f * s, -0.097f * s, 0.164f * s, -0.118f * s); cubicTo(0.159f * s, -0.138f * s, 0.171f * s, -0.158f * s, 0.191f * s, -0.163f * s); cubicTo(0.211f * s, -0.168f * s, 0.231f * s, -0.156f * s, 0.236f * s, -0.136f * s); close() }
    val p13 = Path().apply { moveTo(0.234f * s, -0.127f * s); cubicTo(0.237f * s, -0.116f * s, 0.23f * s, -0.104f * s, 0.218f * s, -0.101f * s); cubicTo(0.206f * s, -0.098f * s, 0.195f * s, -0.105f * s, 0.192f * s, -0.117f * s); cubicTo(0.189f * s, -0.128f * s, 0.196f * s, -0.14f * s, 0.207f * s, -0.143f * s); cubicTo(0.219f * s, -0.146f * s, 0.231f * s, -0.139f * s, 0.234f * s, -0.127f * s); close() }
    val p14 = Path().apply { moveTo(0.21f * s, -0.131f * s); cubicTo(0.211f * s, -0.127f * s, 0.208f * s, -0.123f * s, 0.204f * s, -0.122f * s); cubicTo(0.2f * s, -0.121f * s, 0.196f * s, -0.124f * s, 0.195f * s, -0.128f * s); cubicTo(0.194f * s, -0.132f * s, 0.196f * s, -0.136f * s, 0.201f * s, -0.137f * s); cubicTo(0.205f * s, -0.138f * s, 0.209f * s, -0.135f * s, 0.21f * s, -0.131f * s); close() }
    val p15 = Path().apply { moveTo(0.249f * s, -0.058f * s); quadraticTo(0.314f * s, -0.026f * s, 0.378f * s, -0.058f * s) }
    val p16 = Path().apply { moveTo(0.19f * s, -0.052f * s); cubicTo(0.193f * s, -0.042f * s, 0.183f * s, -0.031f * s, 0.168f * s, -0.027f * s); cubicTo(0.154f * s, -0.023f * s, 0.14f * s, -0.028f * s, 0.137f * s, -0.039f * s); cubicTo(0.135f * s, -0.049f * s, 0.145f * s, -0.06f * s, 0.159f * s, -0.063f * s); cubicTo(0.174f * s, -0.067f * s, 0.188f * s, -0.062f * s, 0.19f * s, -0.052f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF2671BC))
    drawPath(p1, sil ?: Color(0xFF2671BC))
    drawPath(p2, sil ?: Color(0xFF2671BC))
    drawPath(p3, sil ?: Color(0xFF2671BC))
    drawPath(p4, sil ?: Color(0xFF2671BC))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p5, Color(0xFF2F8DEB))
        }
        clipPath(p1) {
            drawPath(p6, Color(0xFF2F8DEB))
        }
        clipPath(p2) {
            drawPath(p7, Color(0xFF2F8DEB))
        }
        clipPath(p3) {
            drawPath(p8, Color(0xFF2F8DEB))
        }
        clipPath(p4) {
            drawPath(p9, Color(0xFF2F8DEB))
        }
        clipPath(p0) {
            drawPath(p10, Color(0xFFCFE8FF))
        }
        clipPath(p1) {
            drawPath(p10, Color(0xFFCFE8FF))
        }
        clipPath(p2) {
            drawPath(p10, Color(0xFFCFE8FF))
        }
        clipPath(p3) {
            drawPath(p10, Color(0xFFCFE8FF))
        }
        clipPath(p4) {
            drawPath(p10, Color(0xFFCFE8FF))
        }
        drawPath(p11, Color(0x73FFFFFF))
    }
    drawPath(p12, sil ?: Color(0xFFFFFFFF))
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p13, Color(0xFF2B2D42))
        drawPath(p14, Color(0xFFFFFFFF))
    }
    drawPath(p15, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p16, Color(0xCCFFB0C8))
    }
}

private fun DrawScope.artOiseau(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.227f * s, 0.13f * s); quadraticTo(-0.377f * s, 0.089f * s, -0.434f * s, 0.233f * s); quadraticTo(-0.284f * s, 0.274f * s, -0.227f * s, 0.13f * s); close() }
    val p1 = Path().apply { moveTo(-0.21f * s, 0.164f * s); quadraticTo(-0.373f * s, 0.19f * s, -0.383f * s, 0.354f * s); quadraticTo(-0.219f * s, 0.329f * s, -0.21f * s, 0.164f * s); close() }
    val p2 = Path().apply { moveTo(-0.02f * s, 0.268f * s); lineTo(-0.02f * s, 0.363f * s) }
    val p3 = Path().apply { moveTo(0.119f * s, 0.268f * s); lineTo(0.119f * s, 0.363f * s) }
    val p4 = Path().apply { moveTo(0.291f * s, 0.043f * s); cubicTo(0.291f * s, 0.186f * s, 0.156f * s, 0.302f * s, -0.011f * s, 0.302f * s); cubicTo(-0.178f * s, 0.302f * s, -0.313f * s, 0.186f * s, -0.313f * s, 0.043f * s); cubicTo(-0.313f * s, -0.1f * s, -0.178f * s, -0.216f * s, -0.011f * s, -0.216f * s); cubicTo(0.156f * s, -0.216f * s, 0.291f * s, -0.1f * s, 0.291f * s, 0.043f * s); close() }
    val p5 = Path().apply { moveTo(0.343f * s, -0.207f * s); cubicTo(0.343f * s, -0.112f * s, 0.258f * s, -0.035f * s, 0.153f * s, -0.035f * s); cubicTo(0.048f * s, -0.035f * s, -0.037f * s, -0.112f * s, -0.037f * s, -0.207f * s); cubicTo(-0.037f * s, -0.303f * s, 0.048f * s, -0.38f * s, 0.153f * s, -0.38f * s); cubicTo(0.258f * s, -0.38f * s, 0.343f * s, -0.303f * s, 0.343f * s, -0.207f * s); close() }
    val p6 = Path().apply { moveTo(0.27f * s, 0.0f * s); cubicTo(0.27f * s, 0.143f * s, 0.134f * s, 0.259f * s, -0.033f * s, 0.259f * s); cubicTo(-0.2f * s, 0.259f * s, -0.335f * s, 0.143f * s, -0.335f * s, 0.0f * s); cubicTo(-0.335f * s, -0.143f * s, -0.2f * s, -0.259f * s, -0.033f * s, -0.259f * s); cubicTo(0.134f * s, -0.259f * s, 0.27f * s, -0.143f * s, 0.27f * s, 0.0f * s); close() }
    val p7 = Path().apply { moveTo(0.322f * s, -0.25f * s); cubicTo(0.322f * s, -0.155f * s, 0.236f * s, -0.078f * s, 0.131f * s, -0.078f * s); cubicTo(0.027f * s, -0.078f * s, -0.059f * s, -0.155f * s, -0.059f * s, -0.25f * s); cubicTo(-0.059f * s, -0.346f * s, 0.027f * s, -0.423f * s, 0.131f * s, -0.423f * s); cubicTo(0.236f * s, -0.423f * s, 0.322f * s, -0.346f * s, 0.322f * s, -0.25f * s); close() }
    val p8 = Path().apply { moveTo(-0.069f * s, -0.151f * s); cubicTo(-0.062f * s, -0.139f * s, -0.072f * s, -0.119f * s, -0.093f * s, -0.107f * s); cubicTo(-0.114f * s, -0.095f * s, -0.136f * s, -0.096f * s, -0.144f * s, -0.108f * s); cubicTo(-0.151f * s, -0.12f * s, -0.14f * s, -0.14f * s, -0.119f * s, -0.152f * s); cubicTo(-0.098f * s, -0.164f * s, -0.076f * s, -0.163f * s, -0.069f * s, -0.151f * s); close() }
    val p9 = Path().apply { moveTo(0.133f * s, -0.355f * s); cubicTo(0.137f * s, -0.343f * s, 0.118f * s, -0.326f * s, 0.091f * s, -0.317f * s); cubicTo(0.064f * s, -0.307f * s, 0.039f * s, -0.308f * s, 0.035f * s, -0.319f * s); cubicTo(0.031f * s, -0.33f * s, 0.05f * s, -0.347f * s, 0.077f * s, -0.357f * s); cubicTo(0.103f * s, -0.367f * s, 0.129f * s, -0.366f * s, 0.133f * s, -0.355f * s); close() }
    val p10 = Path().apply { moveTo(0.055f * s, -0.021f * s); cubicTo(0.082f * s, 0.03f * s, 0.035f * s, 0.107f * s, -0.049f * s, 0.152f * s); cubicTo(-0.133f * s, 0.197f * s, -0.223f * s, 0.192f * s, -0.25f * s, 0.142f * s); cubicTo(-0.277f * s, 0.091f * s, -0.23f * s, 0.014f * s, -0.146f * s, -0.031f * s); cubicTo(-0.062f * s, -0.076f * s, 0.028f * s, -0.071f * s, 0.055f * s, -0.021f * s); close() }
    val p11 = Path().apply { moveTo(0.324f * s, -0.251f * s); quadraticTo(0.309f * s, -0.259f * s, 0.309f * s, -0.242f * s); lineTo(0.309f * s, -0.164f * s); quadraticTo(0.309f * s, -0.147f * s, 0.325f * s, -0.152f * s); lineTo(0.43f * s, -0.185f * s); quadraticTo(0.447f * s, -0.19f * s, 0.431f * s, -0.198f * s); close() }
    drawPath(p0, sil ?: Color(0xFF1F6FC8))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF1F6FC8))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFFF8A1F), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFFF8A1F), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2671BC))
    drawPath(p5, sil ?: Color(0xFF2671BC))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p6, Color(0xFF2F8DEB))
        }
        clipPath(p5) {
            drawPath(p7, Color(0xFF2F8DEB))
        }
        clipPath(p4) {
            drawOval(Color(0xFFCFE8FF), Offset(-0.227f * s, 0.13f * s), Size(0.691f * s, 0.242f * s))
        }
        clipPath(p5) {
            drawOval(Color(0xFFCFE8FF), Offset(-0.227f * s, 0.13f * s), Size(0.691f * s, 0.242f * s))
        }
        drawPath(p8, Color(0x8CFFFFFF))
        drawPath(p9, Color(0x80FFFFFF))
    }
    drawPath(p10, sil ?: Color(0xFF1F6FC8))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFFFF8A1F))
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.06f * s, Offset(0.188f * s, -0.233f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.06f * s, Offset(0.188f * s, -0.233f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.035f * s, Offset(0.206f * s, -0.227f * s))
        drawCircle(Color(0xFFFFFFFF), 0.012f * s, Offset(0.192f * s, -0.243f * s))
        drawOval(Color(0xCCFFB0C8), Offset(0.08f * s, -0.138f * s), Size(0.078f * s, 0.052f * s))
    }
}

private fun DrawScope.artHippocampe(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.306f * s, -0.273f * s); cubicTo(0.306f * s, -0.283f * s, 0.287f * s, -0.298f * s, 0.279f * s, -0.303f * s); cubicTo(0.271f * s, -0.308f * s, 0.266f * s, -0.304f * s, 0.258f * s, -0.304f * s); cubicTo(0.249f * s, -0.305f * s, 0.239f * s, -0.305f * s, 0.228f * s, -0.306f * s); cubicTo(0.218f * s, -0.307f * s, 0.205f * s, -0.308f * s, 0.194f * s, -0.31f * s); cubicTo(0.182f * s, -0.311f * s, 0.171f * s, -0.312f * s, 0.16f * s, -0.316f * s); cubicTo(0.148f * s, -0.319f * s, 0.138f * s, -0.324f * s, 0.125f * s, -0.33f * s); cubicTo(0.112f * s, -0.336f * s, 0.099f * s, -0.344f * s, 0.083f * s, -0.35f * s); cubicTo(0.067f * s, -0.356f * s, 0.05f * s, -0.364f * s, 0.031f * s, -0.365f * s); cubicTo(0.012f * s, -0.367f * s, -0.012f * s, -0.367f * s, -0.031f * s, -0.361f * s); cubicTo(-0.05f * s, -0.355f * s, -0.069f * s, -0.341f * s, -0.083f * s, -0.329f * s); cubicTo(-0.098f * s, -0.316f * s, -0.108f * s, -0.3f * s, -0.117f * s, -0.285f * s); cubicTo(-0.127f * s, -0.269f * s, -0.134f * s, -0.254f * s, -0.14f * s, -0.236f * s); cubicTo(-0.147f * s, -0.219f * s, -0.154f * s, -0.202f * s, -0.158f * s, -0.183f * s); cubicTo(-0.163f * s, -0.164f * s, -0.166f * s, -0.142f * s, -0.167f * s, -0.123f * s); cubicTo(-0.169f * s, -0.104f * s, -0.168f * s, -0.085f * s, -0.169f * s, -0.07f * s); cubicTo(-0.17f * s, -0.054f * s, -0.171f * s, -0.041f * s, -0.171f * s, -0.03f * s); cubicTo(-0.172f * s, -0.02f * s, -0.174f * s, -0.014f * s, -0.174f * s, -0.005f * s); cubicTo(-0.173f * s, 0.004f * s, -0.172f * s, 0.012f * s, -0.169f * s, 0.021f * s); cubicTo(-0.166f * s, 0.03f * s, -0.162f * s, 0.04f * s, -0.159f * s, 0.048f * s); cubicTo(-0.155f * s, 0.057f * s, -0.151f * s, 0.064f * s, -0.148f * s, 0.069f * s); cubicTo(-0.145f * s, 0.075f * s, -0.141f * s, 0.077f * s, -0.139f * s, 0.081f * s); cubicTo(-0.137f * s, 0.085f * s, -0.136f * s, 0.087f * s, -0.136f * s, 0.094f * s); cubicTo(-0.137f * s, 0.101f * s, -0.14f * s, 0.11f * s, -0.143f * s, 0.122f * s); cubicTo(-0.147f * s, 0.134f * s, -0.154f * s, 0.148f * s, -0.158f * s, 0.163f * s); cubicTo(-0.162f * s, 0.179f * s, -0.167f * s, 0.198f * s, -0.167f * s, 0.214f * s); cubicTo(-0.168f * s, 0.23f * s, -0.165f * s, 0.247f * s, -0.162f * s, 0.261f * s); cubicTo(-0.159f * s, 0.276f * s, -0.154f * s, 0.288f * s, -0.148f * s, 0.301f * s); cubicTo(-0.142f * s, 0.313f * s, -0.136f * s, 0.324f * s, -0.128f * s, 0.335f * s); cubicTo(-0.121f * s, 0.345f * s, -0.113f * s, 0.355f * s, -0.103f * s, 0.363f * s); cubicTo(-0.094f * s, 0.372f * s, -0.083f * s, 0.379f * s, -0.072f * s, 0.386f * s); cubicTo(-0.062f * s, 0.393f * s, -0.05f * s, 0.399f * s, -0.038f * s, 0.404f * s); cubicTo(-0.026f * s, 0.409f * s, -0.013f * s, 0.413f * s, 0.0f * s, 0.416f * s); cubicTo(0.012f * s, 0.418f * s, 0.026f * s, 0.42f * s, 0.039f * s, 0.419f * s); cubicTo(0.051f * s, 0.418f * s, 0.064f * s, 0.414f * s, 0.076f * s, 0.409f * s); cubicTo(0.087f * s, 0.405f * s, 0.099f * s, 0.398f * s, 0.109f * s, 0.392f * s); cubicTo(0.119f * s, 0.385f * s, 0.128f * s, 0.378f * s, 0.136f * s, 0.37f * s); cubicTo(0.144f * s, 0.362f * s, 0.153f * s, 0.354f * s, 0.158f * s, 0.345f * s); cubicTo(0.163f * s, 0.335f * s, 0.166f * s, 0.323f * s, 0.168f * s, 0.313f * s); cubicTo(0.169f * s, 0.303f * s, 0.167f * s, 0.293f * s, 0.166f * s, 0.284f * s); cubicTo(0.165f * s, 0.275f * s, 0.162f * s, 0.267f * s, 0.161f * s, 0.261f * s); cubicTo(0.16f * s, 0.255f * s, 0.163f * s, 0.253f * s, 0.158f * s, 0.247f * s); cubicTo(0.153f * s, 0.242f * s, 0.138f * s, 0.227f * s, 0.129f * s, 0.228f * s); cubicTo(0.121f * s, 0.23f * s, 0.111f * s, 0.248f * s, 0.107f * s, 0.255f * s); cubicTo(0.104f * s, 0.262f * s, 0.108f * s, 0.264f * s, 0.109f * s, 0.27f * s); cubicTo(0.109f * s, 0.276f * s, 0.111f * s, 0.284f * s, 0.111f * s, 0.289f * s); cubicTo(0.112f * s, 0.295f * s, 0.112f * s, 0.301f * s, 0.111f * s, 0.305f * s); cubicTo(0.11f * s, 0.309f * s, 0.11f * s, 0.31f * s, 0.107f * s, 0.312f * s); cubicTo(0.105f * s, 0.315f * s, 0.101f * s, 0.318f * s, 0.095f * s, 0.321f * s); cubicTo(0.09f * s, 0.325f * s, 0.082f * s, 0.33f * s, 0.075f * s, 0.333f * s); cubicTo(0.068f * s, 0.336f * s, 0.059f * s, 0.339f * s, 0.053f * s, 0.341f * s); cubicTo(0.047f * s, 0.342f * s, 0.043f * s, 0.342f * s, 0.038f * s, 0.341f * s); cubicTo(0.033f * s, 0.341f * s, 0.029f * s, 0.339f * s, 0.023f * s, 0.336f * s); cubicTo(0.017f * s, 0.333f * s, 0.009f * s, 0.329f * s, 0.003f * s, 0.324f * s); cubicTo(-0.003f * s, 0.32f * s, -0.01f * s, 0.314f * s, -0.015f * s, 0.309f * s); cubicTo(-0.02f * s, 0.304f * s, -0.024f * s, 0.299f * s, -0.027f * s, 0.294f * s); cubicTo(-0.03f * s, 0.289f * s, -0.031f * s, 0.285f * s, -0.033f * s, 0.279f * s); cubicTo(-0.035f * s, 0.274f * s, -0.036f * s, 0.268f * s, -0.037f * s, 0.262f * s); cubicTo(-0.037f * s, 0.257f * s, -0.037f * s, 0.25f * s, -0.036f * s, 0.246f * s); cubicTo(-0.036f * s, 0.242f * s, -0.034f * s, 0.24f * s, -0.032f * s, 0.237f * s); cubicTo(-0.029f * s, 0.234f * s, -0.026f * s, 0.235f * s, -0.021f * s, 0.231f * s); cubicTo(-0.015f * s, 0.227f * s, -0.008f * s, 0.221f * s, 0.001f * s, 0.213f * s); cubicTo(0.01f * s, 0.204f * s, 0.023f * s, 0.194f * s, 0.033f * s, 0.18f * s); cubicTo(0.043f * s, 0.166f * s, 0.054f * s, 0.147f * s, 0.061f * s, 0.129f * s); cubicTo(0.068f * s, 0.112f * s, 0.072f * s, 0.092f * s, 0.076f * s, 0.075f * s); cubicTo(0.08f * s, 0.057f * s, 0.082f * s, 0.042f * s, 0.083f * s, 0.027f * s); cubicTo(0.085f * s, 0.011f * s, 0.085f * s, -0.002f * s, 0.084f * s, -0.016f * s); cubicTo(0.084f * s, -0.03f * s, 0.083f * s, -0.045f * s, 0.078f * s, -0.06f * s); cubicTo(0.073f * s, -0.074f * s, 0.065f * s, -0.092f * s, 0.056f * s, -0.105f * s); cubicTo(0.048f * s, -0.117f * s, 0.036f * s, -0.129f * s, 0.027f * s, -0.137f * s); cubicTo(0.018f * s, -0.145f * s, 0.01f * s, -0.149f * s, 0.004f * s, -0.152f * s); cubicTo(-0.001f * s, -0.156f * s, -0.005f * s, -0.155f * s, -0.006f * s, -0.157f * s); cubicTo(-0.007f * s, -0.159f * s, -0.005f * s, -0.161f * s, -0.001f * s, -0.164f * s); cubicTo(0.002f * s, -0.167f * s, 0.01f * s, -0.172f * s, 0.015f * s, -0.175f * s); cubicTo(0.021f * s, -0.178f * s, 0.028f * s, -0.18f * s, 0.032f * s, -0.182f * s); cubicTo(0.036f * s, -0.183f * s, 0.036f * s, -0.183f * s, 0.038f * s, -0.185f * s); cubicTo(0.04f * s, -0.187f * s, 0.04f * s, -0.191f * s, 0.046f * s, -0.195f * s); cubicTo(0.052f * s, -0.199f * s, 0.063f * s, -0.205f * s, 0.075f * s, -0.209f * s); cubicTo(0.086f * s, -0.213f * s, 0.102f * s, -0.217f * s, 0.116f * s, -0.221f * s); cubicTo(0.13f * s, -0.224f * s, 0.144f * s, -0.227f * s, 0.157f * s, -0.23f * s); cubicTo(0.17f * s, -0.233f * s, 0.182f * s, -0.235f * s, 0.194f * s, -0.236f * s); cubicTo(0.206f * s, -0.238f * s, 0.218f * s, -0.239f * s, 0.228f * s, -0.24f * s); cubicTo(0.239f * s, -0.241f * s, 0.249f * s, -0.241f * s, 0.258f * s, -0.241f * s); cubicTo(0.266f * s, -0.242f * s, 0.271f * s, -0.238f * s, 0.279f * s, -0.243f * s); cubicTo(0.287f * s, -0.248f * s, 0.306f * s, -0.263f * s, 0.306f * s, -0.273f * s); close() }
    val p1 = Path().apply { moveTo(-0.134f * s, -0.032f * s); quadraticTo(-0.181f * s, -0.162f * s, -0.306f * s, -0.101f * s); quadraticTo(-0.258f * s, 0.029f * s, -0.134f * s, -0.032f * s); close() }
    val p2 = Path().apply { moveTo(-0.048f * s, -0.359f * s); cubicTo(-0.048f * s, -0.34f * s, -0.063f * s, -0.324f * s, -0.082f * s, -0.324f * s); cubicTo(-0.101f * s, -0.324f * s, -0.117f * s, -0.34f * s, -0.117f * s, -0.359f * s); cubicTo(-0.117f * s, -0.378f * s, -0.101f * s, -0.393f * s, -0.082f * s, -0.393f * s); cubicTo(-0.063f * s, -0.393f * s, -0.048f * s, -0.378f * s, -0.048f * s, -0.359f * s); close() }
    val p3 = Path().apply { moveTo(0.021f * s, -0.385f * s); cubicTo(0.021f * s, -0.366f * s, 0.006f * s, -0.35f * s, -0.013f * s, -0.35f * s); cubicTo(-0.032f * s, -0.35f * s, -0.048f * s, -0.366f * s, -0.048f * s, -0.385f * s); cubicTo(-0.048f * s, -0.404f * s, -0.032f * s, -0.419f * s, -0.013f * s, -0.419f * s); cubicTo(0.006f * s, -0.419f * s, 0.021f * s, -0.404f * s, 0.021f * s, -0.385f * s); close() }
    val p4 = Path().apply { moveTo(0.284f * s, -0.316f * s); cubicTo(0.284f * s, -0.326f * s, 0.265f * s, -0.341f * s, 0.257f * s, -0.346f * s); cubicTo(0.249f * s, -0.351f * s, 0.245f * s, -0.347f * s, 0.236f * s, -0.347f * s); cubicTo(0.228f * s, -0.348f * s, 0.217f * s, -0.348f * s, 0.207f * s, -0.349f * s); cubicTo(0.196f * s, -0.35f * s, 0.184f * s, -0.351f * s, 0.172f * s, -0.353f * s); cubicTo(0.161f * s, -0.354f * s, 0.15f * s, -0.355f * s, 0.138f * s, -0.359f * s); cubicTo(0.127f * s, -0.362f * s, 0.116f * s, -0.367f * s, 0.103f * s, -0.373f * s); cubicTo(0.091f * s, -0.379f * s, 0.077f * s, -0.387f * s, 0.062f * s, -0.393f * s); cubicTo(0.046f * s, -0.399f * s, 0.029f * s, -0.407f * s, 0.01f * s, -0.408f * s); cubicTo(-0.009f * s, -0.41f * s, -0.033f * s, -0.41f * s, -0.052f * s, -0.404f * s); cubicTo(-0.071f * s, -0.398f * s, -0.09f * s, -0.384f * s, -0.105f * s, -0.372f * s); cubicTo(-0.119f * s, -0.359f * s, -0.129f * s, -0.343f * s, -0.139f * s, -0.328f * s); cubicTo(-0.148f * s, -0.312f * s, -0.155f * s, -0.296f * s, -0.162f * s, -0.279f * s); cubicTo(-0.169f * s, -0.262f * s, -0.175f * s, -0.245f * s, -0.18f * s, -0.226f * s); cubicTo(-0.184f * s, -0.207f * s, -0.187f * s, -0.185f * s, -0.189f * s, -0.166f * s); cubicTo(-0.191f * s, -0.147f * s, -0.19f * s, -0.128f * s, -0.191f * s, -0.113f * s); cubicTo(-0.191f * s, -0.097f * s, -0.192f * s, -0.084f * s, -0.193f * s, -0.073f * s); cubicTo(-0.194f * s, -0.063f * s, -0.196f * s, -0.057f * s, -0.195f * s, -0.048f * s); cubicTo(-0.195f * s, -0.039f * s, -0.193f * s, -0.031f * s, -0.191f * s, -0.022f * s); cubicTo(-0.188f * s, -0.013f * s, -0.184f * s, -0.003f * s, -0.18f * s, 0.005f * s); cubicTo(-0.176f * s, 0.014f * s, -0.173f * s, 0.021f * s, -0.169f * s, 0.026f * s); cubicTo(-0.166f * s, 0.032f * s, -0.163f * s, 0.034f * s, -0.161f * s, 0.038f * s); cubicTo(-0.159f * s, 0.042f * s, -0.157f * s, 0.044f * s, -0.158f * s, 0.051f * s); cubicTo(-0.158f * s, 0.058f * s, -0.161f * s, 0.068f * s, -0.165f * s, 0.079f * s); cubicTo(-0.168f * s, 0.091f * s, -0.175f * s, 0.105f * s, -0.179f * s, 0.12f * s); cubicTo(-0.183f * s, 0.136f * s, -0.188f * s, 0.155f * s, -0.189f * s, 0.171f * s); cubicTo(-0.189f * s, 0.187f * s, -0.186f * s, 0.204f * s, -0.183f * s, 0.219f * s); cubicTo(-0.18f * s, 0.233f * s, -0.175f * s, 0.245f * s, -0.17f * s, 0.258f * s); cubicTo(-0.164f * s, 0.27f * s, -0.157f * s, 0.281f * s, -0.15f * s, 0.292f * s); cubicTo(-0.142f * s, 0.302f * s, -0.134f * s, 0.312f * s, -0.125f * s, 0.32f * s); cubicTo(-0.115f * s, 0.329f * s, -0.105f * s, 0.336f * s, -0.094f * s, 0.343f * s); cubicTo(-0.083f * s, 0.35f * s, -0.071f * s, 0.356f * s, -0.059f * s, 0.361f * s); cubicTo(-0.047f * s, 0.366f * s, -0.034f * s, 0.37f * s, -0.022f * s, 0.373f * s); cubicTo(-0.009f * s, 0.375f * s, 0.004f * s, 0.377f * s, 0.017f * s, 0.376f * s); cubicTo(0.03f * s, 0.375f * s, 0.043f * s, 0.371f * s, 0.054f * s, 0.366f * s); cubicTo(0.066f * s, 0.362f * s, 0.077f * s, 0.355f * s, 0.087f * s, 0.349f * s); cubicTo(0.097f * s, 0.342f * s, 0.106f * s, 0.335f * s, 0.115f * s, 0.327f * s); cubicTo(0.123f * s, 0.319f * s, 0.131f * s, 0.311f * s, 0.136f * s, 0.302f * s); cubicTo(0.142f * s, 0.292f * s, 0.145f * s, 0.28f * s, 0.146f * s, 0.27f * s); cubicTo(0.147f * s, 0.26f * s, 0.145f * s, 0.25f * s, 0.144f * s, 0.241f * s); cubicTo(0.143f * s, 0.232f * s, 0.141f * s, 0.225f * s, 0.139f * s, 0.218f * s); cubicTo(0.138f * s, 0.212f * s, 0.142f * s, 0.21f * s, 0.137f * s, 0.204f * s); cubicTo(0.131f * s, 0.199f * s, 0.116f * s, 0.184f * s, 0.108f * s, 0.185f * s); cubicTo(0.099f * s, 0.187f * s, 0.089f * s, 0.205f * s, 0.086f * s, 0.212f * s); cubicTo(0.082f * s, 0.219f * s, 0.087f * s, 0.221f * s, 0.087f * s, 0.227f * s); cubicTo(0.088f * s, 0.233f * s, 0.089f * s, 0.241f * s, 0.09f * s, 0.247f * s); cubicTo(0.09f * s, 0.252f * s, 0.09f * s, 0.258f * s, 0.089f * s, 0.262f * s); cubicTo(0.089f * s, 0.266f * s, 0.088f * s, 0.267f * s, 0.086f * s, 0.269f * s); cubicTo(0.083f * s, 0.272f * s, 0.079f * s, 0.275f * s, 0.074f * s, 0.278f * s); cubicTo(0.069f * s, 0.282f * s, 0.061f * s, 0.287f * s, 0.053f * s, 0.29f * s); cubicTo(0.046f * s, 0.293f * s, 0.038f * s, 0.296f * s, 0.032f * s, 0.298f * s); cubicTo(0.026f * s, 0.299f * s, 0.021f * s, 0.299f * s, 0.016f * s, 0.299f * s); cubicTo(0.011f * s, 0.298f * s, 0.007f * s, 0.296f * s, 0.001f * s, 0.293f * s); cubicTo(-0.004f * s, 0.29f * s, -0.012f * s, 0.286f * s, -0.018f * s, 0.281f * s); cubicTo(-0.025f * s, 0.277f * s, -0.032f * s, 0.271f * s, -0.037f * s, 0.266f * s); cubicTo(-0.042f * s, 0.261f * s, -0.045f * s, 0.256f * s, -0.048f * s, 0.251f * s); cubicTo(-0.051f * s, 0.246f * s, -0.053f * s, 0.242f * s, -0.054f * s, 0.236f * s); cubicTo(-0.056f * s, 0.231f * s, -0.058f * s, 0.225f * s, -0.058f * s, 0.219f * s); cubicTo(-0.059f * s, 0.214f * s, -0.059f * s, 0.207f * s, -0.058f * s, 0.203f * s); cubicTo(-0.057f * s, 0.199f * s, -0.056f * s, 0.197f * s, -0.053f * s, 0.194f * s); cubicTo(-0.051f * s, 0.192f * s, -0.048f * s, 0.192f * s, -0.042f * s, 0.188f * s); cubicTo(-0.037f * s, 0.184f * s, -0.029f * s, 0.178f * s, -0.02f * s, 0.17f * s); cubicTo(-0.011f * s, 0.161f * s, 0.001f * s, 0.151f * s, 0.011f * s, 0.137f * s); cubicTo(0.021f * s, 0.123f * s, 0.032f * s, 0.104f * s, 0.039f * s, 0.087f * s); cubicTo(0.047f * s, 0.069f * s, 0.051f * s, 0.049f * s, 0.054f * s, 0.032f * s); cubicTo(0.058f * s, 0.015f * s, 0.06f * s, -0.001f * s, 0.062f * s, -0.016f * s); cubicTo(0.063f * s, -0.032f * s, 0.064f * s, -0.045f * s, 0.063f * s, -0.059f * s); cubicTo(0.062f * s, -0.073f * s, 0.061f * s, -0.088f * s, 0.057f * s, -0.103f * s); cubicTo(0.052f * s, -0.117f * s, 0.043f * s, -0.135f * s, 0.035f * s, -0.148f * s); cubicTo(0.026f * s, -0.16f * s, 0.014f * s, -0.172f * s, 0.006f * s, -0.18f * s); cubicTo(-0.003f * s, -0.188f * s, -0.012f * s, -0.192f * s, -0.017f * s, -0.195f * s); cubicTo(-0.023f * s, -0.199f * s, -0.027f * s, -0.198f * s, -0.027f * s, -0.2f * s); cubicTo(-0.028f * s, -0.202f * s, -0.026f * s, -0.204f * s, -0.023f * s, -0.207f * s); cubicTo(-0.019f * s, -0.21f * s, -0.012f * s, -0.215f * s, -0.006f * s, -0.218f * s); cubicTo(-0.001f * s, -0.221f * s, 0.007f * s, -0.223f * s, 0.011f * s, -0.225f * s); cubicTo(0.015f * s, -0.226f * s, 0.014f * s, -0.226f * s, 0.017f * s, -0.228f * s); cubicTo(0.019f * s, -0.23f * s, 0.018f * s, -0.234f * s, 0.024f * s, -0.238f * s); cubicTo(0.03f * s, -0.242f * s, 0.042f * s, -0.247f * s, 0.053f * s, -0.252f * s); cubicTo(0.065f * s, -0.256f * s, 0.081f * s, -0.26f * s, 0.095f * s, -0.264f * s); cubicTo(0.108f * s, -0.267f * s, 0.123f * s, -0.27f * s, 0.135f * s, -0.273f * s); cubicTo(0.148f * s, -0.276f * s, 0.16f * s, -0.278f * s, 0.172f * s, -0.279f * s); cubicTo(0.184f * s, -0.281f * s, 0.196f * s, -0.282f * s, 0.207f * s, -0.283f * s); cubicTo(0.217f * s, -0.284f * s, 0.228f * s, -0.284f * s, 0.236f * s, -0.284f * s); cubicTo(0.245f * s, -0.285f * s, 0.249f * s, -0.281f * s, 0.257f * s, -0.286f * s); cubicTo(0.265f * s, -0.291f * s, 0.284f * s, -0.306f * s, 0.284f * s, -0.316f * s); close() }
    val p5 = Path().apply { moveTo(-0.155f * s, -0.075f * s); quadraticTo(-0.203f * s, -0.205f * s, -0.327f * s, -0.144f * s); quadraticTo(-0.28f * s, -0.014f * s, -0.155f * s, -0.075f * s); close() }
    val p6 = Path().apply { moveTo(-0.069f * s, -0.402f * s); cubicTo(-0.069f * s, -0.383f * s, -0.085f * s, -0.367f * s, -0.104f * s, -0.367f * s); cubicTo(-0.123f * s, -0.367f * s, -0.138f * s, -0.383f * s, -0.138f * s, -0.402f * s); cubicTo(-0.138f * s, -0.421f * s, -0.123f * s, -0.436f * s, -0.104f * s, -0.436f * s); cubicTo(-0.085f * s, -0.436f * s, -0.069f * s, -0.421f * s, -0.069f * s, -0.402f * s); close() }
    val p7 = Path().apply { moveTo(-0.001f * s, -0.428f * s); cubicTo(-0.001f * s, -0.409f * s, -0.016f * s, -0.393f * s, -0.035f * s, -0.393f * s); cubicTo(-0.054f * s, -0.393f * s, -0.069f * s, -0.409f * s, -0.069f * s, -0.428f * s); cubicTo(-0.069f * s, -0.447f * s, -0.054f * s, -0.462f * s, -0.035f * s, -0.462f * s); cubicTo(-0.016f * s, -0.462f * s, -0.001f * s, -0.447f * s, -0.001f * s, -0.428f * s); close() }
    val p8 = Path().apply { moveTo(0.027f * s, -0.336f * s); cubicTo(0.031f * s, -0.327f * s, 0.019f * s, -0.315f * s, 0.001f * s, -0.308f * s); cubicTo(-0.017f * s, -0.302f * s, -0.034f * s, -0.304f * s, -0.037f * s, -0.313f * s); cubicTo(-0.04f * s, -0.322f * s, -0.029f * s, -0.334f * s, -0.011f * s, -0.341f * s); cubicTo(0.007f * s, -0.347f * s, 0.024f * s, -0.345f * s, 0.027f * s, -0.336f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.072f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF2671BC))
    drawPath(p1, sil ?: Color(0xFF2671BC))
    drawPath(p2, sil ?: Color(0xFF2671BC))
    drawPath(p3, sil ?: Color(0xFF2671BC))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p4, Color(0xFF2F8DEB))
        }
        clipPath(p1) {
            drawPath(p5, Color(0xFF2F8DEB))
        }
        clipPath(p2) {
            drawPath(p6, Color(0xFF2F8DEB))
        }
        clipPath(p3) {
            drawPath(p7, Color(0xFF2F8DEB))
        }
    }
    clipPath(p0) {
        drawLine(sil ?: Color(0x80FFFFFF), Offset(-0.203f * s, -0.032f * s), Offset(0.107f * s, -0.015f * s), 0.03f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0x80FFFFFF), Offset(-0.194f * s, 0.105f * s), Offset(0.115f * s, 0.122f * s), 0.03f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0x80FFFFFF), Offset(-0.254f * s, 0.225f * s), Offset(0.055f * s, 0.243f * s), 0.03f * s, StrokeCap.Round)
    }
    clipPath(p1) {
        drawLine(sil ?: Color(0x80FFFFFF), Offset(-0.203f * s, -0.032f * s), Offset(0.107f * s, -0.015f * s), 0.03f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0x80FFFFFF), Offset(-0.194f * s, 0.105f * s), Offset(0.115f * s, 0.122f * s), 0.03f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0x80FFFFFF), Offset(-0.254f * s, 0.225f * s), Offset(0.055f * s, 0.243f * s), 0.03f * s, StrokeCap.Round)
    }
    clipPath(p2) {
        drawLine(sil ?: Color(0x80FFFFFF), Offset(-0.203f * s, -0.032f * s), Offset(0.107f * s, -0.015f * s), 0.03f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0x80FFFFFF), Offset(-0.194f * s, 0.105f * s), Offset(0.115f * s, 0.122f * s), 0.03f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0x80FFFFFF), Offset(-0.254f * s, 0.225f * s), Offset(0.055f * s, 0.243f * s), 0.03f * s, StrokeCap.Round)
    }
    clipPath(p3) {
        drawLine(sil ?: Color(0x80FFFFFF), Offset(-0.203f * s, -0.032f * s), Offset(0.107f * s, -0.015f * s), 0.03f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0x80FFFFFF), Offset(-0.194f * s, 0.105f * s), Offset(0.115f * s, 0.122f * s), 0.03f * s, StrokeCap.Round)
        drawLine(sil ?: Color(0x80FFFFFF), Offset(-0.254f * s, 0.225f * s), Offset(0.055f * s, 0.243f * s), 0.03f * s, StrokeCap.Round)
    }
    if (sil == null) {
        drawPath(p8, Color(0x80FFFFFF))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.043f * s, Offset(0.021f * s, -0.282f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.043f * s, Offset(0.021f * s, -0.282f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.025f * s, Offset(0.034f * s, -0.277f * s))
        drawCircle(Color(0xFFFFFFFF), 0.009f * s, Offset(0.024f * s, -0.288f * s))
        drawOval(Color(0xCCFFB0C8), Offset(-0.026f * s, -0.225f * s), Size(0.06f * s, 0.041f * s))
    }
}

private fun DrawScope.artFlocon(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.392f * s); lineTo(0.0f * s, 0.392f * s) }
    val p1 = Path().apply { moveTo(0.339f * s, -0.196f * s); lineTo(-0.339f * s, 0.196f * s) }
    val p2 = Path().apply { moveTo(0.339f * s, 0.196f * s); lineTo(-0.339f * s, -0.196f * s) }
    val p3 = Path().apply { moveTo(0.0f * s, -0.204f * s); lineTo(-0.119f * s, -0.287f * s) }
    val p4 = Path().apply { moveTo(0.0f * s, -0.204f * s); lineTo(0.119f * s, -0.287f * s) }
    val p5 = Path().apply { moveTo(0.176f * s, -0.102f * s); lineTo(0.189f * s, -0.246f * s) }
    val p6 = Path().apply { moveTo(0.176f * s, -0.102f * s); lineTo(0.308f * s, -0.041f * s) }
    val p7 = Path().apply { moveTo(0.176f * s, 0.102f * s); lineTo(0.308f * s, 0.041f * s) }
    val p8 = Path().apply { moveTo(0.176f * s, 0.102f * s); lineTo(0.189f * s, 0.246f * s) }
    val p9 = Path().apply { moveTo(0.0f * s, 0.204f * s); lineTo(0.119f * s, 0.287f * s) }
    val p10 = Path().apply { moveTo(0.0f * s, 0.204f * s); lineTo(-0.119f * s, 0.287f * s) }
    val p11 = Path().apply { moveTo(-0.176f * s, 0.102f * s); lineTo(-0.189f * s, 0.246f * s) }
    val p12 = Path().apply { moveTo(-0.176f * s, 0.102f * s); lineTo(-0.308f * s, 0.041f * s) }
    val p13 = Path().apply { moveTo(-0.176f * s, -0.102f * s); lineTo(-0.308f * s, -0.041f * s) }
    val p14 = Path().apply { moveTo(-0.176f * s, -0.102f * s); lineTo(-0.189f * s, -0.246f * s) }
    val p15 = Path().apply { moveTo(-0.011f * s, -0.09f * s); quadraticTo(0.0f * s, -0.102f * s, 0.012f * s, -0.09f * s); lineTo(0.025f * s, -0.075f * s); quadraticTo(0.036f * s, -0.063f * s, 0.053f * s, -0.059f * s); lineTo(0.072f * s, -0.055f * s); quadraticTo(0.089f * s, -0.051f * s, 0.083f * s, -0.035f * s); lineTo(0.078f * s, -0.016f * s); quadraticTo(0.072f * s, 0.0f * s, 0.078f * s, 0.016f * s); lineTo(0.083f * s, 0.035f * s); quadraticTo(0.089f * s, 0.051f * s, 0.072f * s, 0.055f * s); lineTo(0.053f * s, 0.059f * s); quadraticTo(0.036f * s, 0.063f * s, 0.025f * s, 0.075f * s); lineTo(0.012f * s, 0.09f * s); quadraticTo(0.0f * s, 0.102f * s, -0.011f * s, 0.09f * s); lineTo(-0.025f * s, 0.075f * s); quadraticTo(-0.036f * s, 0.063f * s, -0.053f * s, 0.059f * s); lineTo(-0.072f * s, 0.055f * s); quadraticTo(-0.088f * s, 0.051f * s, -0.083f * s, 0.035f * s); lineTo(-0.077f * s, 0.016f * s); quadraticTo(-0.072f * s, 0.0f * s, -0.077f * s, -0.016f * s); lineTo(-0.083f * s, -0.035f * s); quadraticTo(-0.088f * s, -0.051f * s, -0.072f * s, -0.055f * s); lineTo(-0.053f * s, -0.059f * s); quadraticTo(-0.036f * s, -0.063f * s, -0.025f * s, -0.075f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p13, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p14, sil ?: Color(0xFF2B2D42), style = Stroke(0.137f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p13, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p14, sil ?: Color(0xFF2F8DEB), style = Stroke(0.065f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF5FB0FF), 0.047f * s, Offset(0.0f * s, -0.392f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.047f * s, Offset(0.0f * s, -0.392f * s), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF5FB0FF), 0.047f * s, Offset(0.339f * s, -0.196f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.047f * s, Offset(0.339f * s, -0.196f * s), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF5FB0FF), 0.047f * s, Offset(0.339f * s, 0.196f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.047f * s, Offset(0.339f * s, 0.196f * s), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF5FB0FF), 0.047f * s, Offset(0.0f * s, 0.392f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.047f * s, Offset(0.0f * s, 0.392f * s), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF5FB0FF), 0.047f * s, Offset(-0.339f * s, 0.196f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.047f * s, Offset(-0.339f * s, 0.196f * s), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFF5FB0FF), 0.047f * s, Offset(-0.339f * s, -0.196f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.047f * s, Offset(-0.339f * s, -0.196f * s), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p15, sil ?: Color(0xFFCFE8FF))
    drawPath(p15, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artAvion(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.4f * s, -0.043f * s); quadraticTo(-0.417f * s, -0.043f * s, -0.417f * s, -0.061f * s); lineTo(-0.417f * s, -0.321f * s); quadraticTo(-0.417f * s, -0.339f * s, -0.4f * s, -0.339f * s); lineTo(-0.339f * s, -0.339f * s); quadraticTo(-0.321f * s, -0.339f * s, -0.314f * s, -0.323f * s); lineTo(-0.181f * s, -0.059f * s); quadraticTo(-0.174f * s, -0.043f * s, -0.191f * s, -0.043f * s); close() }
    val p1 = Path().apply { moveTo(0.321f * s, -0.122f * s); quadraticTo(0.434f * s, -0.122f * s, 0.434f * s, -0.009f * s); quadraticTo(0.434f * s, 0.104f * s, 0.321f * s, 0.104f * s); lineTo(-0.261f * s, 0.104f * s); quadraticTo(-0.434f * s, 0.078f * s, -0.434f * s, -0.061f * s); quadraticTo(-0.434f * s, -0.122f * s, -0.365f * s, -0.122f * s); close() }
    val p2 = Path().apply { moveTo(0.304f * s, -0.156f * s); quadraticTo(0.417f * s, -0.156f * s, 0.417f * s, -0.043f * s); quadraticTo(0.417f * s, 0.07f * s, 0.304f * s, 0.07f * s); lineTo(-0.278f * s, 0.07f * s); quadraticTo(-0.452f * s, 0.043f * s, -0.452f * s, -0.096f * s); quadraticTo(-0.452f * s, -0.156f * s, -0.382f * s, -0.156f * s); close() }
    val p3 = Path().apply { moveTo(0.261f * s, -0.082f * s); quadraticTo(0.261f * s, -0.096f * s, 0.274f * s, -0.096f * s); lineTo(0.352f * s, -0.096f * s); quadraticTo(0.365f * s, -0.096f * s, 0.371f * s, -0.084f * s); lineTo(0.394f * s, -0.038f * s); quadraticTo(0.4f * s, -0.026f * s, 0.387f * s, -0.026f * s); lineTo(0.274f * s, -0.026f * s); quadraticTo(0.261f * s, -0.026f * s, 0.261f * s, -0.039f * s); close() }
    val p4 = Path().apply { moveTo(-0.391f * s, 0.016f * s); quadraticTo(-0.382f * s, -0.009f * s, -0.356f * s, -0.005f * s); lineTo(-0.269f * s, 0.006f * s); quadraticTo(-0.243f * s, 0.009f * s, -0.26f * s, 0.029f * s); lineTo(-0.331f * s, 0.111f * s); quadraticTo(-0.347f * s, 0.13f * s, -0.374f * s, 0.13f * s); lineTo(-0.408f * s, 0.13f * s); quadraticTo(-0.434f * s, 0.13f * s, -0.425f * s, 0.106f * s); close() }
    val p5 = Path().apply { moveTo(0.172f * s, 0.061f * s); quadraticTo(0.191f * s, 0.043f * s, 0.165f * s, 0.043f * s); lineTo(-0.096f * s, 0.043f * s); quadraticTo(-0.122f * s, 0.043f * s, -0.136f * s, 0.065f * s); lineTo(-0.299f * s, 0.317f * s); quadraticTo(-0.313f * s, 0.339f * s, -0.287f * s, 0.339f * s); lineTo(-0.148f * s, 0.339f * s); quadraticTo(-0.122f * s, 0.339f * s, -0.103f * s, 0.321f * s); close() }
    val p6 = Path().apply { moveTo(0.122f * s, 0.174f * s); cubicTo(0.146f * s, 0.174f * s, 0.165f * s, 0.193f * s, 0.165f * s, 0.217f * s); cubicTo(0.165f * s, 0.241f * s, 0.146f * s, 0.261f * s, 0.122f * s, 0.261f * s); lineTo(0.0f * s, 0.261f * s); cubicTo(-0.024f * s, 0.261f * s, -0.043f * s, 0.241f * s, -0.043f * s, 0.217f * s); cubicTo(-0.043f * s, 0.193f * s, -0.024f * s, 0.174f * s, 0.0f * s, 0.174f * s); close() }
    drawPath(p0, sil ?: Color(0xFF1F6FC8))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2671BC))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFF2F8DEB))
        }
        clipPath(p1) {
            drawRoundRect(Color(0xFFEAF5FF), Offset(-0.452f * s, 0.026f * s), Size(0.903f * s, 0.174f * s), CornerRadius(0.0f * s))
        }
        drawOval(Color(0x80FFFFFF), Offset(-0.243f * s, -0.109f * s), Size(0.313f * s, 0.026f * s))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFBFE3FF))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFBFE3FF), 0.028f * s, Offset(-0.226f * s, -0.035f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.028f * s, Offset(-0.226f * s, -0.035f * s), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFBFE3FF), 0.028f * s, Offset(-0.122f * s, -0.035f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.028f * s, Offset(-0.122f * s, -0.035f * s), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFBFE3FF), 0.028f * s, Offset(-0.017f * s, -0.035f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.028f * s, Offset(-0.017f * s, -0.035f * s), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFBFE3FF), 0.028f * s, Offset(0.087f * s, -0.035f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.028f * s, Offset(0.087f * s, -0.035f * s), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF1F6FC8))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF1F6FC8))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFC7CDD8))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artVelo(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.071f * s, 0.129f * s); cubicTo(-0.071f * s, 0.227f * s, -0.151f * s, 0.307f * s, -0.249f * s, 0.307f * s); cubicTo(-0.348f * s, 0.307f * s, -0.428f * s, 0.227f * s, -0.428f * s, 0.129f * s); cubicTo(-0.428f * s, 0.03f * s, -0.348f * s, -0.05f * s, -0.249f * s, -0.05f * s); cubicTo(-0.151f * s, -0.05f * s, -0.071f * s, 0.03f * s, -0.071f * s, 0.129f * s); close() }
    val p1 = Path().apply { moveTo(0.428f * s, 0.129f * s); cubicTo(0.428f * s, 0.227f * s, 0.348f * s, 0.307f * s, 0.25f * s, 0.307f * s); cubicTo(0.151f * s, 0.307f * s, 0.071f * s, 0.227f * s, 0.071f * s, 0.129f * s); cubicTo(0.071f * s, 0.03f * s, 0.151f * s, -0.05f * s, 0.25f * s, -0.05f * s); cubicTo(0.348f * s, -0.05f * s, 0.428f * s, 0.03f * s, 0.428f * s, 0.129f * s); close() }
    val p2 = Path().apply { moveTo(-0.036f * s, 0.129f * s); lineTo(-0.116f * s, -0.148f * s) }
    val p3 = Path().apply { moveTo(-0.116f * s, -0.148f * s); lineTo(0.178f * s, -0.148f * s) }
    val p4 = Path().apply { moveTo(-0.036f * s, 0.129f * s); lineTo(0.178f * s, -0.148f * s) }
    val p5 = Path().apply { moveTo(-0.036f * s, 0.129f * s); lineTo(-0.249f * s, 0.129f * s) }
    val p6 = Path().apply { moveTo(-0.116f * s, -0.148f * s); lineTo(-0.249f * s, 0.129f * s) }
    val p7 = Path().apply { moveTo(0.178f * s, -0.148f * s); lineTo(0.25f * s, 0.129f * s) }
    val p8 = Path().apply { moveTo(-0.116f * s, -0.148f * s); lineTo(-0.125f * s, -0.21f * s) }
    val p9 = Path().apply { moveTo(0.178f * s, -0.148f * s); lineTo(0.16f * s, -0.263f * s) }
    val p10 = Path().apply { moveTo(0.143f * s, -0.272f * s); lineTo(0.267f * s, -0.281f * s) }
    val p11 = Path().apply { moveTo(-0.071f * s, -0.261f * s); cubicTo(-0.055f * s, -0.261f * s, -0.043f * s, -0.248f * s, -0.043f * s, -0.232f * s); cubicTo(-0.043f * s, -0.217f * s, -0.055f * s, -0.204f * s, -0.071f * s, -0.204f * s); lineTo(-0.196f * s, -0.204f * s); cubicTo(-0.212f * s, -0.204f * s, -0.225f * s, -0.217f * s, -0.225f * s, -0.232f * s); cubicTo(-0.225f * s, -0.248f * s, -0.212f * s, -0.261f * s, -0.196f * s, -0.261f * s); close() }
    drawCircle(sil ?: Color(0xFFBFE0FF), 0.178f * s, Offset(-0.25f * s, 0.129f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.178f * s, Offset(-0.25f * s, 0.129f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF41465F), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF41465F), 0.04f * s, Offset(-0.25f * s, 0.129f * s))
    }
    drawCircle(sil ?: Color(0xFFBFE0FF), 0.178f * s, Offset(0.25f * s, 0.129f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.178f * s, Offset(0.25f * s, 0.129f * s), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF41465F), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF41465F), 0.04f * s, Offset(0.25f * s, 0.129f * s))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2F8DEB), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2F8DEB), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2F8DEB), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF2F8DEB), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF2F8DEB), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF2F8DEB), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.107f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.107f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.107f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF41465F), style = Stroke(0.035f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFF41465F), style = Stroke(0.035f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF41465F), style = Stroke(0.035f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF41465F))
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFEAF5FF), 0.058f * s, Offset(-0.036f * s, 0.129f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.058f * s, Offset(-0.036f * s, 0.129f * s), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF41465F), 0.018f * s, Offset(-0.036f * s, 0.129f * s))
    }
}

private fun DrawScope.artSeau(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.312f * s, -0.054f * s); cubicTo(-0.304f * s, -0.34f * s, 0.202f * s, -0.34f * s, 0.21f * s, -0.054f * s) }
    val p1 = Path().apply { moveTo(0.05f * s, -0.003f * s); lineTo(0.236f * s, -0.222f * s) }
    val p2 = Path().apply { moveTo(0.349f * s, -0.419f * s); lineTo(0.362f * s, -0.408f * s); cubicTo(0.388f * s, -0.385f * s, 0.391f * s, -0.345f * s, 0.368f * s, -0.319f * s); lineTo(0.319f * s, -0.262f * s); cubicTo(0.296f * s, -0.236f * s, 0.256f * s, -0.233f * s, 0.229f * s, -0.256f * s); lineTo(0.217f * s, -0.267f * s); cubicTo(0.19f * s, -0.29f * s, 0.188f * s, -0.33f * s, 0.21f * s, -0.356f * s); lineTo(0.26f * s, -0.413f * s); cubicTo(0.283f * s, -0.439f * s, 0.323f * s, -0.442f * s, 0.349f * s, -0.419f * s); close() }
    val p3 = Path().apply { moveTo(-0.334f * s, -0.02f * s); quadraticTo(-0.337f * s, -0.045f * s, -0.312f * s, -0.045f * s); lineTo(0.21f * s, -0.045f * s); quadraticTo(0.236f * s, -0.045f * s, 0.232f * s, -0.02f * s); lineTo(0.176f * s, 0.376f * s); quadraticTo(0.168f * s, 0.435f * s, 0.109f * s, 0.435f * s); lineTo(-0.211f * s, 0.435f * s); quadraticTo(-0.27f * s, 0.435f * s, -0.278f * s, 0.376f * s); close() }
    val p4 = Path().apply { moveTo(-0.355f * s, -0.054f * s); quadraticTo(-0.358f * s, -0.079f * s, -0.333f * s, -0.079f * s); lineTo(0.189f * s, -0.079f * s); quadraticTo(0.215f * s, -0.079f * s, 0.211f * s, -0.054f * s); lineTo(0.155f * s, 0.343f * s); quadraticTo(0.147f * s, 0.401f * s, 0.088f * s, 0.401f * s); lineTo(-0.232f * s, 0.401f * s); quadraticTo(-0.291f * s, 0.401f * s, -0.299f * s, 0.343f * s); close() }
    val p5 = Path().apply { moveTo(-0.19f * s, 0.15f * s); cubicTo(-0.195f * s, 0.206f * s, -0.208f * s, 0.25f * s, -0.22f * s, 0.249f * s); cubicTo(-0.231f * s, 0.248f * s, -0.237f * s, 0.202f * s, -0.232f * s, 0.147f * s); cubicTo(-0.227f * s, 0.091f * s, -0.214f * s, 0.047f * s, -0.202f * s, 0.048f * s); cubicTo(-0.191f * s, 0.049f * s, -0.185f * s, 0.095f * s, -0.19f * s, 0.15f * s); close() }
    val p6 = Path().apply { moveTo(0.236f * s, -0.1f * s); cubicTo(0.261f * s, -0.1f * s, 0.282f * s, -0.079f * s, 0.282f * s, -0.054f * s); cubicTo(0.282f * s, -0.028f * s, 0.261f * s, -0.007f * s, 0.236f * s, -0.007f * s); lineTo(-0.337f * s, -0.007f * s); cubicTo(-0.363f * s, -0.007f * s, -0.384f * s, -0.028f * s, -0.384f * s, -0.054f * s); cubicTo(-0.384f * s, -0.079f * s, -0.363f * s, -0.1f * s, -0.337f * s, -0.1f * s); close() }
    val p7 = Path().apply { moveTo(0.219f * s, -0.125f * s); cubicTo(0.244f * s, -0.125f * s, 0.265f * s, -0.105f * s, 0.265f * s, -0.079f * s); cubicTo(0.265f * s, -0.053f * s, 0.244f * s, -0.033f * s, 0.219f * s, -0.033f * s); lineTo(-0.354f * s, -0.033f * s); cubicTo(-0.38f * s, -0.033f * s, -0.4f * s, -0.053f * s, -0.4f * s, -0.079f * s); cubicTo(-0.4f * s, -0.105f * s, -0.38f * s, -0.125f * s, -0.354f * s, -0.125f * s); close() }
    val p8 = Path().apply { moveTo(-0.056f * s, 0.118f * s); quadraticTo(-0.051f * s, 0.106f * s, -0.046f * s, 0.118f * s); lineTo(-0.029f * s, 0.158f * s); quadraticTo(-0.024f * s, 0.17f * s, -0.011f * s, 0.171f * s); lineTo(0.033f * s, 0.175f * s); quadraticTo(0.045f * s, 0.176f * s, 0.036f * s, 0.184f * s); lineTo(0.003f * s, 0.213f * s); quadraticTo(-0.007f * s, 0.222f * s, -0.004f * s, 0.234f * s); lineTo(0.006f * s, 0.277f * s); quadraticTo(0.009f * s, 0.289f * s, -0.002f * s, 0.283f * s); lineTo(-0.04f * s, 0.26f * s); quadraticTo(-0.051f * s, 0.254f * s, -0.062f * s, 0.26f * s); lineTo(-0.099f * s, 0.283f * s); quadraticTo(-0.11f * s, 0.289f * s, -0.107f * s, 0.277f * s); lineTo(-0.098f * s, 0.234f * s); quadraticTo(-0.095f * s, 0.222f * s, -0.104f * s, 0.213f * s); lineTo(-0.137f * s, 0.184f * s); quadraticTo(-0.147f * s, 0.176f * s, -0.134f * s, 0.175f * s); lineTo(-0.091f * s, 0.171f * s); quadraticTo(-0.078f * s, 0.17f * s, -0.073f * s, 0.158f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.107f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF41465F), style = Stroke(0.035f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFFF8A1F), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFE8372E))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2671BC))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFF2F8DEB))
        }
        drawPath(p5, Color(0x8CFFFFFF))
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF498DCC))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFF5BB0FF))
        }
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFFFD23F))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artRobot(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.174f * s, 0.104f * s); lineTo(-0.321f * s, 0.226f * s) }
    val p1 = Path().apply { moveTo(-0.282f * s, 0.243f * s); cubicTo(-0.282f * s, 0.27f * s, -0.304f * s, 0.291f * s, -0.33f * s, 0.291f * s); cubicTo(-0.356f * s, 0.291f * s, -0.378f * s, 0.27f * s, -0.378f * s, 0.243f * s); cubicTo(-0.378f * s, 0.217f * s, -0.356f * s, 0.196f * s, -0.33f * s, 0.196f * s); cubicTo(-0.304f * s, 0.196f * s, -0.282f * s, 0.217f * s, -0.282f * s, 0.243f * s); close() }
    val p2 = Path().apply { moveTo(0.174f * s, 0.104f * s); lineTo(0.321f * s, 0.226f * s) }
    val p3 = Path().apply { moveTo(0.378f * s, 0.243f * s); cubicTo(0.378f * s, 0.27f * s, 0.357f * s, 0.291f * s, 0.33f * s, 0.291f * s); cubicTo(0.304f * s, 0.291f * s, 0.282f * s, 0.27f * s, 0.282f * s, 0.243f * s); cubicTo(0.282f * s, 0.217f * s, 0.304f * s, 0.196f * s, 0.33f * s, 0.196f * s); cubicTo(0.357f * s, 0.196f * s, 0.378f * s, 0.217f * s, 0.378f * s, 0.243f * s); close() }
    val p4 = Path().apply { moveTo(-0.117f * s, 0.304f * s); lineTo(-0.082f * s, 0.304f * s); cubicTo(-0.068f * s, 0.304f * s, -0.056f * s, 0.316f * s, -0.056f * s, 0.33f * s); lineTo(-0.056f * s, 0.374f * s); cubicTo(-0.056f * s, 0.388f * s, -0.068f * s, 0.4f * s, -0.082f * s, 0.4f * s); lineTo(-0.117f * s, 0.4f * s); cubicTo(-0.132f * s, 0.4f * s, -0.143f * s, 0.388f * s, -0.143f * s, 0.374f * s); lineTo(-0.143f * s, 0.33f * s); cubicTo(-0.143f * s, 0.316f * s, -0.132f * s, 0.304f * s, -0.117f * s, 0.304f * s); close() }
    val p5 = Path().apply { moveTo(0.083f * s, 0.304f * s); lineTo(0.117f * s, 0.304f * s); cubicTo(0.132f * s, 0.304f * s, 0.143f * s, 0.316f * s, 0.143f * s, 0.33f * s); lineTo(0.143f * s, 0.374f * s); cubicTo(0.143f * s, 0.388f * s, 0.132f * s, 0.4f * s, 0.117f * s, 0.4f * s); lineTo(0.083f * s, 0.4f * s); cubicTo(0.068f * s, 0.4f * s, 0.057f * s, 0.388f * s, 0.057f * s, 0.374f * s); lineTo(0.057f * s, 0.33f * s); cubicTo(0.057f * s, 0.316f * s, 0.068f * s, 0.304f * s, 0.083f * s, 0.304f * s); close() }
    val p6 = Path().apply { moveTo(-0.174f * s, 0.365f * s); lineTo(-0.096f * s, 0.365f * s); cubicTo(-0.076f * s, 0.365f * s, -0.061f * s, 0.38f * s, -0.061f * s, 0.4f * s); lineTo(-0.061f * s, 0.4f * s); cubicTo(-0.061f * s, 0.419f * s, -0.076f * s, 0.434f * s, -0.096f * s, 0.434f * s); lineTo(-0.174f * s, 0.434f * s); cubicTo(-0.193f * s, 0.434f * s, -0.208f * s, 0.419f * s, -0.208f * s, 0.4f * s); lineTo(-0.208f * s, 0.4f * s); cubicTo(-0.208f * s, 0.38f * s, -0.193f * s, 0.365f * s, -0.174f * s, 0.365f * s); close() }
    val p7 = Path().apply { moveTo(0.096f * s, 0.365f * s); lineTo(0.174f * s, 0.365f * s); cubicTo(0.193f * s, 0.365f * s, 0.209f * s, 0.38f * s, 0.209f * s, 0.4f * s); lineTo(0.209f * s, 0.4f * s); cubicTo(0.209f * s, 0.419f * s, 0.193f * s, 0.434f * s, 0.174f * s, 0.434f * s); lineTo(0.096f * s, 0.434f * s); cubicTo(0.076f * s, 0.434f * s, 0.061f * s, 0.419f * s, 0.061f * s, 0.4f * s); lineTo(0.061f * s, 0.4f * s); cubicTo(0.061f * s, 0.38f * s, 0.076f * s, 0.365f * s, 0.096f * s, 0.365f * s); close() }
    val p8 = Path().apply { moveTo(0.0f * s, -0.295f * s); lineTo(0.0f * s, -0.374f * s) }
    val p9 = Path().apply { moveTo(-0.256f * s, -0.217f * s); lineTo(-0.239f * s, -0.217f * s); cubicTo(-0.229f * s, -0.217f * s, -0.221f * s, -0.209f * s, -0.221f * s, -0.2f * s); lineTo(-0.221f * s, -0.104f * s); cubicTo(-0.221f * s, -0.095f * s, -0.229f * s, -0.087f * s, -0.239f * s, -0.087f * s); lineTo(-0.256f * s, -0.087f * s); cubicTo(-0.266f * s, -0.087f * s, -0.274f * s, -0.095f * s, -0.274f * s, -0.104f * s); lineTo(-0.274f * s, -0.2f * s); cubicTo(-0.274f * s, -0.209f * s, -0.266f * s, -0.217f * s, -0.256f * s, -0.217f * s); close() }
    val p10 = Path().apply { moveTo(0.239f * s, -0.217f * s); lineTo(0.256f * s, -0.217f * s); cubicTo(0.266f * s, -0.217f * s, 0.274f * s, -0.209f * s, 0.274f * s, -0.2f * s); lineTo(0.274f * s, -0.104f * s); cubicTo(0.274f * s, -0.095f * s, 0.266f * s, -0.087f * s, 0.256f * s, -0.087f * s); lineTo(0.239f * s, -0.087f * s); cubicTo(0.229f * s, -0.087f * s, 0.222f * s, -0.095f * s, 0.222f * s, -0.104f * s); lineTo(0.222f * s, -0.2f * s); cubicTo(0.222f * s, -0.209f * s, 0.229f * s, -0.217f * s, 0.239f * s, -0.217f * s); close() }
    val p11 = Path().apply { moveTo(-0.139f * s, 0.043f * s); lineTo(0.139f * s, 0.043f * s); cubicTo(0.168f * s, 0.043f * s, 0.191f * s, 0.067f * s, 0.191f * s, 0.096f * s); lineTo(0.191f * s, 0.278f * s); cubicTo(0.191f * s, 0.307f * s, 0.168f * s, 0.33f * s, 0.139f * s, 0.33f * s); lineTo(-0.139f * s, 0.33f * s); cubicTo(-0.168f * s, 0.33f * s, -0.191f * s, 0.307f * s, -0.191f * s, 0.278f * s); lineTo(-0.191f * s, 0.096f * s); cubicTo(-0.191f * s, 0.067f * s, -0.168f * s, 0.043f * s, -0.139f * s, 0.043f * s); close() }
    val p12 = Path().apply { moveTo(-0.156f * s, 0.017f * s); lineTo(0.122f * s, 0.017f * s); cubicTo(0.15f * s, 0.017f * s, 0.174f * s, 0.041f * s, 0.174f * s, 0.07f * s); lineTo(0.174f * s, 0.252f * s); cubicTo(0.174f * s, 0.281f * s, 0.15f * s, 0.304f * s, 0.122f * s, 0.304f * s); lineTo(-0.156f * s, 0.304f * s); cubicTo(-0.185f * s, 0.304f * s, -0.208f * s, 0.281f * s, -0.208f * s, 0.252f * s); lineTo(-0.208f * s, 0.07f * s); cubicTo(-0.208f * s, 0.041f * s, -0.185f * s, 0.017f * s, -0.156f * s, 0.017f * s); close() }
    val p13 = Path().apply { moveTo(-0.087f * s, 0.096f * s); lineTo(0.087f * s, 0.096f * s); cubicTo(0.106f * s, 0.096f * s, 0.122f * s, 0.111f * s, 0.122f * s, 0.13f * s); lineTo(0.122f * s, 0.209f * s); cubicTo(0.122f * s, 0.228f * s, 0.106f * s, 0.243f * s, 0.087f * s, 0.243f * s); lineTo(-0.087f * s, 0.243f * s); cubicTo(-0.106f * s, 0.243f * s, -0.122f * s, 0.228f * s, -0.122f * s, 0.209f * s); lineTo(-0.122f * s, 0.13f * s); cubicTo(-0.122f * s, 0.111f * s, -0.106f * s, 0.096f * s, -0.087f * s, 0.096f * s); close() }
    val p14 = Path().apply { moveTo(-0.156f * s, -0.304f * s); lineTo(0.156f * s, -0.304f * s); cubicTo(0.2f * s, -0.304f * s, 0.235f * s, -0.269f * s, 0.235f * s, -0.226f * s); lineTo(0.235f * s, -0.078f * s); cubicTo(0.235f * s, -0.035f * s, 0.2f * s, 0.0f * s, 0.156f * s, 0.0f * s); lineTo(-0.156f * s, 0.0f * s); cubicTo(-0.199f * s, 0.0f * s, -0.235f * s, -0.035f * s, -0.235f * s, -0.078f * s); lineTo(-0.235f * s, -0.226f * s); cubicTo(-0.235f * s, -0.269f * s, -0.199f * s, -0.304f * s, -0.156f * s, -0.304f * s); close() }
    val p15 = Path().apply { moveTo(-0.174f * s, -0.339f * s); lineTo(0.139f * s, -0.339f * s); cubicTo(0.182f * s, -0.339f * s, 0.217f * s, -0.304f * s, 0.217f * s, -0.261f * s); lineTo(0.217f * s, -0.113f * s); cubicTo(0.217f * s, -0.07f * s, 0.182f * s, -0.035f * s, 0.139f * s, -0.035f * s); lineTo(-0.174f * s, -0.035f * s); cubicTo(-0.217f * s, -0.035f * s, -0.252f * s, -0.07f * s, -0.252f * s, -0.113f * s); lineTo(-0.252f * s, -0.261f * s); cubicTo(-0.252f * s, -0.304f * s, -0.217f * s, -0.339f * s, -0.174f * s, -0.339f * s); close() }
    val p16 = Path().apply { moveTo(-0.124f * s, -0.258f * s); cubicTo(-0.12f * s, -0.247f * s, -0.135f * s, -0.231f * s, -0.158f * s, -0.223f * s); cubicTo(-0.18f * s, -0.215f * s, -0.202f * s, -0.217f * s, -0.206f * s, -0.228f * s); cubicTo(-0.21f * s, -0.24f * s, -0.195f * s, -0.255f * s, -0.172f * s, -0.264f * s); cubicTo(-0.15f * s, -0.272f * s, -0.128f * s, -0.269f * s, -0.124f * s, -0.258f * s); close() }
    val p17 = Path().apply { moveTo(-0.065f * s, -0.069f * s); lineTo(0.065f * s, -0.069f * s); cubicTo(0.077f * s, -0.069f * s, 0.087f * s, -0.06f * s, 0.087f * s, -0.048f * s); lineTo(0.087f * s, -0.048f * s); cubicTo(0.087f * s, -0.036f * s, 0.077f * s, -0.026f * s, 0.065f * s, -0.026f * s); lineTo(-0.065f * s, -0.026f * s); cubicTo(-0.077f * s, -0.026f * s, -0.087f * s, -0.036f * s, -0.087f * s, -0.048f * s); lineTo(-0.087f * s, -0.048f * s); cubicTo(-0.087f * s, -0.06f * s, -0.077f * s, -0.069f * s, -0.065f * s, -0.069f * s); close() }
    val p18 = Path().apply { moveTo(0.043f * s, -0.391f * s); cubicTo(0.043f * s, -0.367f * s, 0.024f * s, -0.347f * s, 0.0f * s, -0.347f * s); cubicTo(-0.024f * s, -0.347f * s, -0.043f * s, -0.367f * s, -0.043f * s, -0.391f * s); cubicTo(-0.043f * s, -0.415f * s, -0.024f * s, -0.434f * s, 0.0f * s, -0.434f * s); cubicTo(0.024f * s, -0.434f * s, 0.043f * s, -0.415f * s, 0.043f * s, -0.391f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.142f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFFC7CDD8), style = Stroke(0.07f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFFFD23F))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.142f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFC7CDD8), style = Stroke(0.07f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFFFD23F))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFC7CDD8))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFC7CDD8))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF8E9AB4))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF8E9AB4))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.112f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFC7CDD8), style = Stroke(0.04f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFC7CDD8))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFFC7CDD8))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF2671BC))
    if (sil == null) {
        clipPath(p11) {
            drawPath(p12, Color(0xFF2F8DEB))
        }
        drawOval(Color(0x80FFFFFF), Offset(-0.148f * s, 0.061f * s), Size(0.035f * s, 0.087f * s))
    }
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p13, sil ?: Color(0xFFCFE6FF))
    drawPath(p13, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFFE8372E), 0.03f * s, Offset(-0.061f * s, 0.169f * s))
        drawCircle(Color(0xFFFFD23F), 0.03f * s, Offset(0.009f * s, 0.169f * s))
        drawCircle(Color(0xFF45B649), 0.03f * s, Offset(0.078f * s, 0.169f * s))
    }
    drawPath(p14, sil ?: Color(0xFF2671BC))
    if (sil == null) {
        clipPath(p14) {
            drawPath(p15, Color(0xFF2F8DEB))
        }
        drawPath(p16, Color(0x8CFFFFFF))
    }
    drawPath(p14, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.074f * s, Offset(-0.087f * s, -0.174f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.074f * s, Offset(-0.087f * s, -0.174f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.043f * s, Offset(-0.079f * s, -0.155f * s))
        drawCircle(Color(0xFFFFFFFF), 0.015f * s, Offset(-0.096f * s, -0.174f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.074f * s, Offset(0.087f * s, -0.174f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.074f * s, Offset(0.087f * s, -0.174f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.043f * s, Offset(0.079f * s, -0.155f * s))
        drawCircle(Color(0xFFFFFFFF), 0.015f * s, Offset(0.063f * s, -0.174f * s))
    }
    drawPath(p17, sil ?: Color(0xFFFFFFFF))
    drawPath(p17, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p18, sil ?: Color(0xFFE8372E))
    drawPath(p18, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCasquette(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.435f * s, 0.109f * s); cubicTo(-0.457f * s, -0.08f * s, -0.304f * s, -0.24f * s, -0.115f * s, -0.24f * s); cubicTo(0.082f * s, -0.24f * s, 0.191f * s, -0.109f * s, 0.213f * s, 0.152f * s); quadraticTo(-0.115f * s, 0.182f * s, -0.435f * s, 0.109f * s); close() }
    val p1 = Path().apply { moveTo(-0.453f * s, 0.072f * s); cubicTo(-0.475f * s, -0.117f * s, -0.322f * s, -0.277f * s, -0.133f * s, -0.277f * s); cubicTo(0.063f * s, -0.277f * s, 0.173f * s, -0.146f * s, 0.194f * s, 0.116f * s); quadraticTo(-0.133f * s, 0.145f * s, -0.453f * s, 0.072f * s); close() }
    val p2 = Path().apply { moveTo(-0.115f * s, -0.255f * s); quadraticTo(0.016f * s, -0.051f * s, 0.031f * s, 0.196f * s); lineTo(0.249f * s, 0.196f * s); lineTo(0.249f * s, -0.328f * s); close() }
    val p3 = Path().apply { moveTo(-0.478f * s, 0.021f * s); quadraticTo(-0.115f * s, 0.109f * s, 0.249f * s, 0.08f * s); lineTo(0.249f * s, 0.254f * s); lineTo(-0.478f * s, 0.254f * s); close() }
    val p4 = Path().apply { moveTo(-0.115f * s, -0.255f * s); quadraticTo(-0.217f * s, -0.051f * s, -0.231f * s, 0.196f * s) }
    val p5 = Path().apply { moveTo(-0.115f * s, -0.255f * s); quadraticTo(0.016f * s, -0.051f * s, 0.031f * s, 0.196f * s) }
    val p6 = Path().apply { moveTo(-0.245f * s, -0.059f * s); cubicTo(-0.268f * s, -0.027f * s, -0.3f * s, -0.009f * s, -0.317f * s, -0.021f * s); cubicTo(-0.333f * s, -0.032f * s, -0.328f * s, -0.068f * s, -0.305f * s, -0.101f * s); cubicTo(-0.282f * s, -0.134f * s, -0.25f * s, -0.151f * s, -0.233f * s, -0.14f * s); cubicTo(-0.217f * s, -0.128f * s, -0.222f * s, -0.092f * s, -0.245f * s, -0.059f * s); close() }
    val p7 = Path().apply { moveTo(-0.078f * s, 0.152f * s); cubicTo(0.045f * s, 0.036f * s, 0.307f * s, 0.036f * s, 0.424f * s, 0.182f * s); cubicTo(0.46f * s, 0.232f * s, 0.416f * s, 0.276f * s, 0.365f * s, 0.269f * s); cubicTo(0.22f * s, 0.24f * s, 0.06f * s, 0.232f * s, -0.078f * s, 0.152f * s); close() }
    val p8 = Path().apply { moveTo(-0.093f * s, 0.131f * s); cubicTo(0.031f * s, 0.014f * s, 0.293f * s, 0.014f * s, 0.409f * s, 0.16f * s); cubicTo(0.445f * s, 0.211f * s, 0.402f * s, 0.254f * s, 0.351f * s, 0.247f * s); cubicTo(0.205f * s, 0.218f * s, 0.045f * s, 0.211f * s, -0.093f * s, 0.131f * s); close() }
    val p9 = Path().apply { moveTo(0.254f * s, 0.106f * s); cubicTo(0.252f * s, 0.115f * s, 0.215f * s, 0.113f * s, 0.172f * s, 0.102f * s); cubicTo(0.129f * s, 0.092f * s, 0.096f * s, 0.076f * s, 0.099f * s, 0.068f * s); cubicTo(0.101f * s, 0.059f * s, 0.137f * s, 0.061f * s, 0.18f * s, 0.071f * s); cubicTo(0.223f * s, 0.082f * s, 0.256f * s, 0.098f * s, 0.254f * s, 0.106f * s); close() }
    val p10 = Path().apply { moveTo(-0.078f * s, -0.244f * s); cubicTo(-0.078f * s, -0.228f * s, -0.095f * s, -0.215f * s, -0.115f * s, -0.215f * s); cubicTo(-0.135f * s, -0.215f * s, -0.151f * s, -0.228f * s, -0.151f * s, -0.244f * s); cubicTo(-0.151f * s, -0.26f * s, -0.135f * s, -0.273f * s, -0.115f * s, -0.273f * s); cubicTo(-0.095f * s, -0.273f * s, -0.078f * s, -0.26f * s, -0.078f * s, -0.244f * s); close() }
    val p11 = Path().apply { moveTo(-0.086f * s, -0.259f * s); cubicTo(-0.086f * s, -0.242f * s, -0.102f * s, -0.229f * s, -0.122f * s, -0.229f * s); cubicTo(-0.142f * s, -0.229f * s, -0.158f * s, -0.242f * s, -0.158f * s, -0.259f * s); cubicTo(-0.158f * s, -0.275f * s, -0.142f * s, -0.288f * s, -0.122f * s, -0.288f * s); cubicTo(-0.102f * s, -0.288f * s, -0.086f * s, -0.275f * s, -0.086f * s, -0.259f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2671BC))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF2F8DEB))
        }
        clipPath(p0) {
            drawPath(p2, Color(0xFF5BAEFF))
            drawPath(p3, Color(0xFF1F6FC9))
        }
    }
    clipPath(p0) {
        drawPath(p4, sil ?: Color(0xFF1B63B8), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p5, sil ?: Color(0xFF1B63B8), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    if (sil == null) {
        drawPath(p6, Color(0x8CFFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF1959A1))
    if (sil == null) {
        clipPath(p7) {
            drawPath(p8, Color(0xFF1F6FC9))
        }
        drawPath(p9, Color(0x66FFFFFF))
    }
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF1959A1))
    if (sil == null) {
        clipPath(p10) {
            drawPath(p11, Color(0xFF1F6FC9))
        }
    }
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artPrune(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.253f * s); quadraticTo(0.0f * s, -0.333f * s, 0.045f * s, -0.396f * s) }
    val p1 = Path().apply { moveTo(0.036f * s, -0.333f * s); quadraticTo(0.192f * s, -0.249f * s, 0.277f * s, -0.404f * s); quadraticTo(0.121f * s, -0.488f * s, 0.036f * s, -0.333f * s); close() }
    val p2 = Path().apply { moveTo(0.0f * s, -0.271f * s); cubicTo(0.134f * s, -0.351f * s, 0.321f * s, -0.226f * s, 0.348f * s, 0.006f * s); cubicTo(0.375f * s, 0.238f * s, 0.232f * s, 0.434f * s, 0.0f * s, 0.434f * s); cubicTo(-0.232f * s, 0.434f * s, -0.375f * s, 0.238f * s, -0.348f * s, 0.006f * s); cubicTo(-0.321f * s, -0.226f * s, -0.134f * s, -0.351f * s, 0.0f * s, -0.271f * s); close() }
    val p3 = Path().apply { moveTo(-0.022f * s, -0.315f * s); cubicTo(0.112f * s, -0.396f * s, 0.299f * s, -0.271f * s, 0.326f * s, -0.039f * s); cubicTo(0.352f * s, 0.193f * s, 0.21f * s, 0.389f * s, -0.022f * s, 0.389f * s); cubicTo(-0.254f * s, 0.389f * s, -0.397f * s, 0.193f * s, -0.37f * s, -0.039f * s); cubicTo(-0.343f * s, -0.271f * s, -0.156f * s, -0.396f * s, -0.022f * s, -0.315f * s); close() }
    val p4 = Path().apply { moveTo(0.027f * s, -0.253f * s); cubicTo(0.178f * s, -0.083f * s, 0.178f * s, 0.22f * s, 0.045f * s, 0.416f * s) }
    val p5 = Path().apply { moveTo(-0.135f * s, -0.076f * s); cubicTo(-0.121f * s, 0.001f * s, -0.137f * s, 0.069f * s, -0.171f * s, 0.075f * s); cubicTo(-0.205f * s, 0.081f * s, -0.244f * s, 0.023f * s, -0.258f * s, -0.055f * s); cubicTo(-0.271f * s, -0.132f * s, -0.255f * s, -0.2f * s, -0.221f * s, -0.206f * s); cubicTo(-0.187f * s, -0.212f * s, -0.148f * s, -0.154f * s, -0.135f * s, -0.076f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF7A4A2A), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF3FAE49))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF6239A0))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF7A47C8))
        }
    }
    drawPath(p4, sil ?: Color(0x8C4B2A8A), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p5, Color(0x66FFFFFF))
        drawOval(Color(0x8CFFFFFF), Offset(-0.244f * s, 0.127f * s), Size(0.043f * s, 0.043f * s))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artBonbon(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.153f * s, -0.054f * s); lineTo(-0.378f * s, -0.243f * s); quadraticTo(-0.468f * s, -0.18f * s, -0.414f * s, 0.0f * s); quadraticTo(-0.468f * s, 0.18f * s, -0.378f * s, 0.243f * s); lineTo(-0.153f * s, 0.054f * s); close() }
    val p1 = Path().apply { moveTo(-0.139f * s, -0.081f * s); lineTo(-0.364f * s, -0.27f * s); quadraticTo(-0.454f * s, -0.207f * s, -0.4f * s, -0.027f * s); quadraticTo(-0.454f * s, 0.153f * s, -0.364f * s, 0.216f * s); lineTo(-0.139f * s, 0.027f * s); close() }
    val p2 = Path().apply { moveTo(0.153f * s, -0.054f * s); lineTo(0.378f * s, -0.243f * s); quadraticTo(0.468f * s, -0.18f * s, 0.414f * s, 0.0f * s); quadraticTo(0.468f * s, 0.18f * s, 0.378f * s, 0.243f * s); lineTo(0.153f * s, 0.054f * s); close() }
    val p3 = Path().apply { moveTo(0.139f * s, -0.081f * s); lineTo(0.364f * s, -0.27f * s); quadraticTo(0.454f * s, -0.207f * s, 0.4f * s, -0.027f * s); quadraticTo(0.454f * s, 0.153f * s, 0.364f * s, 0.216f * s); lineTo(0.139f * s, 0.027f * s); close() }
    val p4 = Path().apply { moveTo(0.243f * s, 0.0f * s); cubicTo(0.243f * s, 0.109f * s, 0.134f * s, 0.198f * s, 0.0f * s, 0.198f * s); cubicTo(-0.134f * s, 0.198f * s, -0.243f * s, 0.109f * s, -0.243f * s, 0.0f * s); cubicTo(-0.243f * s, -0.109f * s, -0.134f * s, -0.198f * s, 0.0f * s, -0.198f * s); cubicTo(0.134f * s, -0.198f * s, 0.243f * s, -0.109f * s, 0.243f * s, 0.0f * s); close() }
    val p5 = Path().apply { moveTo(0.225f * s, -0.036f * s); cubicTo(0.225f * s, 0.073f * s, 0.116f * s, 0.162f * s, -0.018f * s, 0.162f * s); cubicTo(-0.152f * s, 0.162f * s, -0.261f * s, 0.073f * s, -0.261f * s, -0.036f * s); cubicTo(-0.261f * s, -0.145f * s, -0.152f * s, -0.234f * s, -0.018f * s, -0.234f * s); cubicTo(0.116f * s, -0.234f * s, 0.225f * s, -0.145f * s, 0.225f * s, -0.036f * s); close() }
    val p6 = Path().apply { moveTo(-0.104f * s, -0.126f * s); cubicTo(-0.098f * s, -0.115f * s, -0.106f * s, -0.098f * s, -0.124f * s, -0.088f * s); cubicTo(-0.141f * s, -0.078f * s, -0.16f * s, -0.079f * s, -0.166f * s, -0.09f * s); cubicTo(-0.172f * s, -0.101f * s, -0.163f * s, -0.117f * s, -0.146f * s, -0.127f * s); cubicTo(-0.129f * s, -0.137f * s, -0.11f * s, -0.137f * s, -0.104f * s, -0.126f * s); close() }
    drawPath(p0, sil ?: Color(0xFF9D82D0))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFB697F2))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawLine(Color(0x99FFFFFF), Offset(-0.18f * s, -0.036f * s), Offset(-0.36f * s, -0.144f * s), 0.025f * s, StrokeCap.Round)
        drawLine(Color(0x99FFFFFF), Offset(-0.18f * s, 0.036f * s), Offset(-0.36f * s, 0.144f * s), 0.025f * s, StrokeCap.Round)
    }
    drawPath(p2, sil ?: Color(0xFF9D82D0))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFB697F2))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawLine(Color(0x99FFFFFF), Offset(0.18f * s, -0.036f * s), Offset(0.36f * s, -0.144f * s), 0.025f * s, StrokeCap.Round)
        drawLine(Color(0x99FFFFFF), Offset(0.18f * s, 0.036f * s), Offset(0.36f * s, 0.144f * s), 0.025f * s, StrokeCap.Round)
    }
    drawPath(p4, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFF8B5CD6))
        }
        clipPath(p4) {
            drawLine(Color(0x8CFFFFFF), Offset(-0.18f * s, 0.27f * s), Offset(0.045f * s, -0.27f * s), 0.07f * s, StrokeCap.Round)
            drawLine(Color(0x8CFFFFFF), Offset(0.0f * s, 0.27f * s), Offset(0.225f * s, -0.27f * s), 0.07f * s, StrokeCap.Round)
        }
        drawPath(p6, Color(0xB3FFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artPoulpe(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.175f * s, 0.087f * s); cubicTo(-0.238f * s, 0.23f * s, -0.366f * s, 0.262f * s, -0.382f * s, 0.151f * s) }
    val p1 = Path().apply { moveTo(-0.064f * s, 0.103f * s); cubicTo(-0.079f * s, 0.23f * s, -0.159f * s, 0.357f * s, -0.238f * s, 0.333f * s) }
    val p2 = Path().apply { moveTo(0.064f * s, 0.103f * s); cubicTo(0.08f * s, 0.23f * s, 0.159f * s, 0.357f * s, 0.239f * s, 0.333f * s) }
    val p3 = Path().apply { moveTo(0.175f * s, 0.087f * s); cubicTo(0.239f * s, 0.23f * s, 0.366f * s, 0.262f * s, 0.382f * s, 0.151f * s) }
    val p4 = Path().apply { moveTo(0.27f * s, -0.12f * s); cubicTo(0.27f * s, 0.03f * s, 0.149f * s, 0.151f * s, 0.0f * s, 0.151f * s); cubicTo(-0.149f * s, 0.151f * s, -0.27f * s, 0.03f * s, -0.27f * s, -0.12f * s); cubicTo(-0.27f * s, -0.269f * s, -0.149f * s, -0.39f * s, 0.0f * s, -0.39f * s); cubicTo(0.149f * s, -0.39f * s, 0.27f * s, -0.269f * s, 0.27f * s, -0.12f * s); close() }
    val p5 = Path().apply { moveTo(0.25f * s, -0.16f * s); cubicTo(0.25f * s, -0.01f * s, 0.129f * s, 0.111f * s, -0.02f * s, 0.111f * s); cubicTo(-0.169f * s, 0.111f * s, -0.29f * s, -0.01f * s, -0.29f * s, -0.16f * s); cubicTo(-0.29f * s, -0.309f * s, -0.169f * s, -0.43f * s, -0.02f * s, -0.43f * s); cubicTo(0.129f * s, -0.43f * s, 0.25f * s, -0.309f * s, 0.25f * s, -0.16f * s); close() }
    val p6 = Path().apply { moveTo(-0.12f * s, -0.274f * s); cubicTo(-0.095f * s, -0.238f * s, -0.092f * s, -0.197f * s, -0.113f * s, -0.182f * s); cubicTo(-0.135f * s, -0.167f * s, -0.173f * s, -0.184f * s, -0.198f * s, -0.22f * s); cubicTo(-0.223f * s, -0.256f * s, -0.226f * s, -0.297f * s, -0.205f * s, -0.312f * s); cubicTo(-0.183f * s, -0.327f * s, -0.145f * s, -0.31f * s, -0.12f * s, -0.274f * s); close() }
    val p7 = Path().apply { moveTo(-0.056f * s, -0.008f * s); quadraticTo(0.0f * s, 0.055f * s, 0.056f * s, -0.008f * s) }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.172f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.172f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.172f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.172f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF8B5CD6), style = Stroke(0.1f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF8B5CD6), style = Stroke(0.1f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF8B5CD6), style = Stroke(0.1f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF8B5CD6), style = Stroke(0.1f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFF8B5CD6))
        }
        drawPath(p6, Color(0x8CFFFFFF))
        drawOval(Color(0x8CFFFFFF), Offset(-0.234f * s, -0.139f * s), Size(0.038f * s, 0.038f * s))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.072f * s, Offset(-0.103f * s, -0.12f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.072f * s, Offset(-0.103f * s, -0.12f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.042f * s, Offset(-0.096f * s, -0.102f * s))
        drawCircle(Color(0xFFFFFFFF), 0.014f * s, Offset(-0.112f * s, -0.121f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.072f * s, Offset(0.103f * s, -0.12f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.072f * s, Offset(0.103f * s, -0.12f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.042f * s, Offset(0.096f * s, -0.102f * s))
        drawCircle(Color(0xFFFFFFFF), 0.014f * s, Offset(0.08f * s, -0.121f * s))
    }
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(-0.242f * s, -0.056f * s), Size(0.072f * s, 0.048f * s))
        drawOval(Color(0x66FF4F86), Offset(0.171f * s, -0.056f * s), Size(0.072f * s, 0.048f * s))
    }
}

private fun DrawScope.artChauvesouris(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.081f * s, -0.032f * s); cubicTo(0.161f * s, -0.258f * s, 0.363f * s, -0.291f * s, 0.436f * s, -0.145f * s); quadraticTo(0.347f * s, -0.097f * s, 0.371f * s, 0.08f * s); quadraticTo(0.282f * s, 0.016f * s, 0.25f * s, 0.226f * s); quadraticTo(0.161f * s, 0.064f * s, 0.097f * s, 0.129f * s); close() }
    val p1 = Path().apply { moveTo(0.069f * s, -0.061f * s); cubicTo(0.149f * s, -0.287f * s, 0.351f * s, -0.319f * s, 0.423f * s, -0.174f * s); quadraticTo(0.335f * s, -0.125f * s, 0.359f * s, 0.052f * s); quadraticTo(0.27f * s, -0.012f * s, 0.238f * s, 0.197f * s); quadraticTo(0.149f * s, 0.036f * s, 0.085f * s, 0.101f * s); close() }
    val p2 = Path().apply { moveTo(-0.081f * s, -0.032f * s); cubicTo(-0.161f * s, -0.258f * s, -0.363f * s, -0.291f * s, -0.435f * s, -0.145f * s); quadraticTo(-0.347f * s, -0.097f * s, -0.371f * s, 0.08f * s); quadraticTo(-0.282f * s, 0.016f * s, -0.25f * s, 0.226f * s); quadraticTo(-0.161f * s, 0.064f * s, -0.097f * s, 0.129f * s); close() }
    val p3 = Path().apply { moveTo(-0.068f * s, -0.061f * s); cubicTo(-0.149f * s, -0.287f * s, -0.351f * s, -0.319f * s, -0.423f * s, -0.174f * s); quadraticTo(-0.335f * s, -0.125f * s, -0.359f * s, 0.052f * s); quadraticTo(-0.27f * s, -0.012f * s, -0.238f * s, 0.197f * s); quadraticTo(-0.149f * s, 0.036f * s, -0.085f * s, 0.101f * s); close() }
    val p4 = Path().apply { moveTo(0.105f * s, 0.177f * s); cubicTo(0.105f * s, 0.253f * s, 0.058f * s, 0.314f * s, 0.0f * s, 0.314f * s); cubicTo(-0.058f * s, 0.314f * s, -0.105f * s, 0.253f * s, -0.105f * s, 0.177f * s); cubicTo(-0.105f * s, 0.101f * s, -0.058f * s, 0.04f * s, 0.0f * s, 0.04f * s); cubicTo(0.058f * s, 0.04f * s, 0.105f * s, 0.101f * s, 0.105f * s, 0.177f * s); close() }
    val p5 = Path().apply { moveTo(0.093f * s, 0.153f * s); cubicTo(0.093f * s, 0.229f * s, 0.046f * s, 0.29f * s, -0.012f * s, 0.29f * s); cubicTo(-0.07f * s, 0.29f * s, -0.117f * s, 0.229f * s, -0.117f * s, 0.153f * s); cubicTo(-0.117f * s, 0.077f * s, -0.07f * s, 0.016f * s, -0.012f * s, 0.016f * s); cubicTo(0.046f * s, 0.016f * s, 0.093f * s, 0.077f * s, 0.093f * s, 0.153f * s); close() }
    val p6 = Path().apply { moveTo(-0.144f * s, -0.092f * s); quadraticTo(-0.161f * s, -0.081f * s, -0.161f * s, -0.101f * s); lineTo(-0.161f * s, -0.303f * s); quadraticTo(-0.161f * s, -0.323f * s, -0.148f * s, -0.308f * s); lineTo(-0.038f * s, -0.185f * s); quadraticTo(-0.024f * s, -0.17f * s, -0.041f * s, -0.159f * s); close() }
    val p7 = Path().apply { moveTo(-0.122f * s, -0.151f * s); quadraticTo(-0.133f * s, -0.145f * s, -0.133f * s, -0.157f * s); lineTo(-0.137f * s, -0.246f * s); quadraticTo(-0.137f * s, -0.258f * s, -0.129f * s, -0.249f * s); lineTo(-0.073f * s, -0.187f * s); quadraticTo(-0.064f * s, -0.178f * s, -0.075f * s, -0.172f * s); close() }
    val p8 = Path().apply { moveTo(0.144f * s, -0.092f * s); quadraticTo(0.161f * s, -0.081f * s, 0.161f * s, -0.101f * s); lineTo(0.161f * s, -0.303f * s); quadraticTo(0.161f * s, -0.323f * s, 0.148f * s, -0.308f * s); lineTo(0.038f * s, -0.185f * s); quadraticTo(0.024f * s, -0.17f * s, 0.041f * s, -0.159f * s); close() }
    val p9 = Path().apply { moveTo(0.122f * s, -0.151f * s); quadraticTo(0.133f * s, -0.145f * s, 0.134f * s, -0.157f * s); lineTo(0.137f * s, -0.246f * s); quadraticTo(0.137f * s, -0.258f * s, 0.129f * s, -0.249f * s); lineTo(0.073f * s, -0.187f * s); quadraticTo(0.065f * s, -0.178f * s, 0.076f * s, -0.172f * s); close() }
    val p10 = Path().apply { moveTo(0.169f * s, 0.0f * s); cubicTo(0.169f * s, 0.084f * s, 0.094f * s, 0.153f * s, 0.0f * s, 0.153f * s); cubicTo(-0.093f * s, 0.153f * s, -0.169f * s, 0.084f * s, -0.169f * s, 0.0f * s); cubicTo(-0.169f * s, -0.085f * s, -0.093f * s, -0.153f * s, 0.0f * s, -0.153f * s); cubicTo(0.094f * s, -0.153f * s, 0.169f * s, -0.085f * s, 0.169f * s, 0.0f * s); close() }
    val p11 = Path().apply { moveTo(0.153f * s, -0.032f * s); cubicTo(0.153f * s, 0.052f * s, 0.077f * s, 0.121f * s, -0.016f * s, 0.121f * s); cubicTo(-0.11f * s, 0.121f * s, -0.185f * s, 0.052f * s, -0.185f * s, -0.032f * s); cubicTo(-0.185f * s, -0.117f * s, -0.11f * s, -0.186f * s, -0.016f * s, -0.186f * s); cubicTo(0.077f * s, -0.186f * s, 0.153f * s, -0.117f * s, 0.153f * s, -0.032f * s); close() }
    val p12 = Path().apply { moveTo(-0.053f * s, -0.097f * s); cubicTo(-0.047f * s, -0.087f * s, -0.055f * s, -0.072f * s, -0.071f * s, -0.063f * s); cubicTo(-0.086f * s, -0.054f * s, -0.103f * s, -0.055f * s, -0.108f * s, -0.065f * s); cubicTo(-0.114f * s, -0.074f * s, -0.106f * s, -0.089f * s, -0.091f * s, -0.098f * s); cubicTo(-0.075f * s, -0.107f * s, -0.058f * s, -0.107f * s, -0.053f * s, -0.097f * s); close() }
    val p13 = Path().apply { moveTo(-0.04f * s, 0.064f * s); quadraticTo(0.0f * s, 0.105f * s, 0.04f * s, 0.064f * s) }
    drawPath(p0, sil ?: Color(0xFF583396))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF6E40BC))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF583396))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF6E40BC))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFF8B5CD6))
        }
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF8B5CD6))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p7, Color(0xFFFFB3D1))
    }
    drawPath(p8, sil ?: Color(0xFF8B5CD6))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p9, Color(0xFFFFB3D1))
    }
    drawPath(p10, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p10) {
            drawPath(p11, Color(0xFF8B5CD6))
        }
        drawPath(p12, Color(0x8CFFFFFF))
    }
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.048f * s, Offset(-0.065f * s, -0.016f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.048f * s, Offset(-0.065f * s, -0.016f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.028f * s, Offset(-0.06f * s, -0.004f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(-0.07f * s, -0.017f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.048f * s, Offset(0.065f * s, -0.016f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.048f * s, Offset(0.065f * s, -0.016f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.028f * s, Offset(0.06f * s, -0.004f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(0.049f * s, -0.017f * s))
    }
    drawPath(p13, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artLavande(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, 0.396f * s); lineTo(-0.215f * s, 0.024f * s) }
    val p1 = Path().apply { moveTo(-0.172f * s, 0.099f * s); lineTo(-0.111f * s, 0.024f * s); quadraticTo(-0.096f * s, -0.037f * s, -0.156f * s, -0.035f * s); quadraticTo(-0.141f * s, -0.095f * s, -0.2f * s, -0.093f * s); quadraticTo(-0.186f * s, -0.154f * s, -0.245f * s, -0.152f * s); quadraticTo(-0.23f * s, -0.213f * s, -0.29f * s, -0.211f * s); quadraticTo(-0.275f * s, -0.271f * s, -0.335f * s, -0.269f * s); quadraticTo(-0.338f * s, -0.327f * s, -0.415f * s, -0.322f * s); quadraticTo(-0.457f * s, -0.258f * s, -0.409f * s, -0.226f * s); quadraticTo(-0.44f * s, -0.176f * s, -0.381f * s, -0.158f * s); quadraticTo(-0.412f * s, -0.108f * s, -0.352f * s, -0.09f * s); quadraticTo(-0.384f * s, -0.04f * s, -0.324f * s, -0.022f * s); quadraticTo(-0.355f * s, 0.028f * s, -0.296f * s, 0.046f * s); quadraticTo(-0.327f * s, 0.096f * s, -0.267f * s, 0.114f * s); lineTo(-0.172f * s, 0.099f * s); close() }
    val p2 = Path().apply { moveTo(-0.196f * s, 0.083f * s); lineTo(-0.135f * s, 0.008f * s); quadraticTo(-0.12f * s, -0.053f * s, -0.18f * s, -0.051f * s); quadraticTo(-0.165f * s, -0.111f * s, -0.224f * s, -0.109f * s); quadraticTo(-0.21f * s, -0.17f * s, -0.269f * s, -0.168f * s); quadraticTo(-0.254f * s, -0.228f * s, -0.314f * s, -0.226f * s); quadraticTo(-0.299f * s, -0.287f * s, -0.359f * s, -0.285f * s); quadraticTo(-0.362f * s, -0.343f * s, -0.439f * s, -0.338f * s); quadraticTo(-0.481f * s, -0.274f * s, -0.433f * s, -0.242f * s); quadraticTo(-0.464f * s, -0.192f * s, -0.405f * s, -0.174f * s); quadraticTo(-0.436f * s, -0.124f * s, -0.376f * s, -0.106f * s); quadraticTo(-0.408f * s, -0.056f * s, -0.348f * s, -0.038f * s); quadraticTo(-0.379f * s, 0.012f * s, -0.32f * s, 0.03f * s); quadraticTo(-0.351f * s, 0.08f * s, -0.291f * s, 0.098f * s); lineTo(-0.196f * s, 0.083f * s); close() }
    val p3 = Path().apply { moveTo(-0.278f * s, 0.13f * s); lineTo(-0.208f * s, 0.035f * s); lineTo(-0.092f * s, 0.022f * s); moveTo(-0.306f * s, 0.062f * s); lineTo(-0.245f * s, -0.028f * s); lineTo(-0.136f * s, -0.036f * s); moveTo(-0.335f * s, -0.006f * s); lineTo(-0.281f * s, -0.091f * s); lineTo(-0.181f * s, -0.095f * s); moveTo(-0.363f * s, -0.074f * s); lineTo(-0.318f * s, -0.155f * s); lineTo(-0.226f * s, -0.153f * s); moveTo(-0.391f * s, -0.142f * s); lineTo(-0.355f * s, -0.218f * s); lineTo(-0.271f * s, -0.212f * s); moveTo(-0.42f * s, -0.21f * s); lineTo(-0.391f * s, -0.281f * s); lineTo(-0.315f * s, -0.27f * s) }
    val p4 = Path().apply { moveTo(-0.289f * s, -0.052f * s); cubicTo(-0.278f * s, -0.031f * s, -0.273f * s, -0.012f * s, -0.278f * s, -0.009f * s); cubicTo(-0.284f * s, -0.005f * s, -0.299f * s, -0.019f * s, -0.31f * s, -0.04f * s); cubicTo(-0.322f * s, -0.06f * s, -0.327f * s, -0.08f * s, -0.321f * s, -0.083f * s); cubicTo(-0.316f * s, -0.086f * s, -0.301f * s, -0.072f * s, -0.289f * s, -0.052f * s); close() }
    val p5 = Path().apply { moveTo(0.0f * s, 0.396f * s); lineTo(0.215f * s, 0.024f * s) }
    val p6 = Path().apply { moveTo(0.172f * s, 0.099f * s); lineTo(0.267f * s, 0.114f * s); quadraticTo(0.327f * s, 0.096f * s, 0.296f * s, 0.046f * s); quadraticTo(0.356f * s, 0.028f * s, 0.324f * s, -0.022f * s); quadraticTo(0.384f * s, -0.04f * s, 0.352f * s, -0.09f * s); quadraticTo(0.412f * s, -0.108f * s, 0.381f * s, -0.158f * s); quadraticTo(0.441f * s, -0.176f * s, 0.409f * s, -0.226f * s); quadraticTo(0.457f * s, -0.258f * s, 0.415f * s, -0.322f * s); quadraticTo(0.338f * s, -0.327f * s, 0.335f * s, -0.269f * s); quadraticTo(0.275f * s, -0.271f * s, 0.29f * s, -0.211f * s); quadraticTo(0.231f * s, -0.213f * s, 0.245f * s, -0.152f * s); quadraticTo(0.186f * s, -0.154f * s, 0.2f * s, -0.093f * s); quadraticTo(0.141f * s, -0.095f * s, 0.156f * s, -0.035f * s); quadraticTo(0.096f * s, -0.037f * s, 0.111f * s, 0.024f * s); lineTo(0.172f * s, 0.099f * s); close() }
    val p7 = Path().apply { moveTo(0.174f * s, 0.07f * s); lineTo(0.269f * s, 0.085f * s); quadraticTo(0.329f * s, 0.068f * s, 0.297f * s, 0.017f * s); quadraticTo(0.357f * s, 0.0f * s, 0.326f * s, -0.051f * s); quadraticTo(0.386f * s, -0.068f * s, 0.354f * s, -0.119f * s); quadraticTo(0.414f * s, -0.136f * s, 0.382f * s, -0.187f * s); quadraticTo(0.442f * s, -0.204f * s, 0.411f * s, -0.255f * s); quadraticTo(0.459f * s, -0.287f * s, 0.417f * s, -0.351f * s); quadraticTo(0.34f * s, -0.355f * s, 0.336f * s, -0.298f * s); quadraticTo(0.277f * s, -0.3f * s, 0.292f * s, -0.239f * s); quadraticTo(0.232f * s, -0.241f * s, 0.247f * s, -0.181f * s); quadraticTo(0.188f * s, -0.183f * s, 0.202f * s, -0.122f * s); quadraticTo(0.143f * s, -0.124f * s, 0.157f * s, -0.064f * s); quadraticTo(0.098f * s, -0.066f * s, 0.113f * s, -0.005f * s); lineTo(0.174f * s, 0.07f * s); close() }
    val p8 = Path().apply { moveTo(0.092f * s, 0.022f * s); lineTo(0.209f * s, 0.035f * s); lineTo(0.278f * s, 0.13f * s); moveTo(0.137f * s, -0.036f * s); lineTo(0.245f * s, -0.028f * s); lineTo(0.306f * s, 0.062f * s); moveTo(0.181f * s, -0.095f * s); lineTo(0.282f * s, -0.091f * s); lineTo(0.335f * s, -0.006f * s); moveTo(0.226f * s, -0.153f * s); lineTo(0.318f * s, -0.155f * s); lineTo(0.363f * s, -0.074f * s); moveTo(0.271f * s, -0.212f * s); lineTo(0.355f * s, -0.218f * s); lineTo(0.391f * s, -0.142f * s); moveTo(0.315f * s, -0.27f * s); lineTo(0.391f * s, -0.281f * s); lineTo(0.42f * s, -0.21f * s) }
    val p9 = Path().apply { moveTo(0.244f * s, -0.079f * s); cubicTo(0.232f * s, -0.058f * s, 0.217f * s, -0.044f * s, 0.212f * s, -0.047f * s); cubicTo(0.206f * s, -0.051f * s, 0.211f * s, -0.07f * s, 0.223f * s, -0.091f * s); cubicTo(0.234f * s, -0.111f * s, 0.249f * s, -0.125f * s, 0.255f * s, -0.122f * s); cubicTo(0.26f * s, -0.118f * s, 0.255f * s, -0.099f * s, 0.244f * s, -0.079f * s); close() }
    val p10 = Path().apply { moveTo(0.0f * s, 0.396f * s); lineTo(0.0f * s, -0.033f * s) }
    val p11 = Path().apply { moveTo(0.0f * s, 0.052f * s); lineTo(0.09f * s, 0.018f * s); quadraticTo(0.133f * s, -0.027f * s, 0.081f * s, -0.055f * s); quadraticTo(0.124f * s, -0.1f * s, 0.071f * s, -0.128f * s); quadraticTo(0.114f * s, -0.173f * s, 0.062f * s, -0.201f * s); quadraticTo(0.105f * s, -0.246f * s, 0.052f * s, -0.274f * s); quadraticTo(0.095f * s, -0.319f * s, 0.043f * s, -0.347f * s); quadraticTo(0.069f * s, -0.399f * s, 0.0f * s, -0.433f * s); quadraticTo(-0.069f * s, -0.399f * s, -0.043f * s, -0.347f * s); quadraticTo(-0.095f * s, -0.319f * s, -0.052f * s, -0.274f * s); quadraticTo(-0.105f * s, -0.246f * s, -0.062f * s, -0.201f * s); quadraticTo(-0.114f * s, -0.173f * s, -0.071f * s, -0.128f * s); quadraticTo(-0.124f * s, -0.1f * s, -0.081f * s, -0.055f * s); quadraticTo(-0.133f * s, -0.027f * s, -0.09f * s, 0.018f * s); lineTo(0.0f * s, 0.052f * s); close() }
    val p12 = Path().apply { moveTo(-0.013f * s, 0.027f * s); lineTo(0.077f * s, -0.008f * s); quadraticTo(0.12f * s, -0.053f * s, 0.068f * s, -0.081f * s); quadraticTo(0.111f * s, -0.126f * s, 0.059f * s, -0.154f * s); quadraticTo(0.101f * s, -0.199f * s, 0.049f * s, -0.227f * s); quadraticTo(0.092f * s, -0.272f * s, 0.04f * s, -0.3f * s); quadraticTo(0.083f * s, -0.345f * s, 0.03f * s, -0.373f * s); quadraticTo(0.056f * s, -0.425f * s, -0.013f * s, -0.459f * s); quadraticTo(-0.082f * s, -0.425f * s, -0.056f * s, -0.373f * s); quadraticTo(-0.108f * s, -0.345f * s, -0.065f * s, -0.3f * s); quadraticTo(-0.118f * s, -0.272f * s, -0.075f * s, -0.227f * s); quadraticTo(-0.127f * s, -0.199f * s, -0.084f * s, -0.154f * s); quadraticTo(-0.137f * s, -0.126f * s, -0.094f * s, -0.081f * s); quadraticTo(-0.146f * s, -0.053f * s, -0.103f * s, -0.008f * s); lineTo(-0.013f * s, 0.027f * s); close() }
    val p13 = Path().apply { moveTo(-0.107f * s, 0.027f * s); lineTo(0.0f * s, -0.021f * s); lineTo(0.108f * s, 0.027f * s); moveTo(-0.098f * s, -0.046f * s); lineTo(0.0f * s, -0.094f * s); lineTo(0.098f * s, -0.046f * s); moveTo(-0.088f * s, -0.119f * s); lineTo(0.0f * s, -0.167f * s); lineTo(0.089f * s, -0.119f * s); moveTo(-0.079f * s, -0.193f * s); lineTo(0.0f * s, -0.24f * s); lineTo(0.079f * s, -0.193f * s); moveTo(-0.07f * s, -0.266f * s); lineTo(0.0f * s, -0.313f * s); lineTo(0.07f * s, -0.266f * s); moveTo(-0.06f * s, -0.339f * s); lineTo(0.0f * s, -0.386f * s); lineTo(0.06f * s, -0.339f * s) }
    val p14 = Path().apply { moveTo(-0.056f * s, 0.224f * s); lineTo(0.056f * s, 0.224f * s); cubicTo(0.068f * s, 0.224f * s, 0.077f * s, 0.234f * s, 0.077f * s, 0.246f * s); lineTo(0.077f * s, 0.263f * s); cubicTo(0.077f * s, 0.275f * s, 0.068f * s, 0.285f * s, 0.056f * s, 0.285f * s); lineTo(-0.056f * s, 0.285f * s); cubicTo(-0.068f * s, 0.285f * s, -0.077f * s, 0.275f * s, -0.077f * s, 0.263f * s); lineTo(-0.077f * s, 0.246f * s); cubicTo(-0.077f * s, 0.234f * s, -0.068f * s, 0.224f * s, -0.056f * s, 0.224f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF4C9A3A), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFF8B5CD6))
        }
    }
    clipPath(p1) {
        drawPath(p3, sil ?: Color(0xB34B2A8A), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    if (sil == null) {
        drawPath(p4, Color(0x80FFFFFF))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF4C9A3A), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFF8B5CD6))
        }
    }
    clipPath(p6) {
        drawPath(p8, sil ?: Color(0xB34B2A8A), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    if (sil == null) {
        drawPath(p9, Color(0x80FFFFFF))
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.122f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF4C9A3A), style = Stroke(0.05f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p11) {
            drawPath(p12, Color(0xFF8B5CD6))
        }
    }
    clipPath(p11) {
        drawPath(p13, sil ?: Color(0xB34B2A8A), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    if (sil == null) {
        drawOval(Color(0x80FFFFFF), Offset(-0.051f * s, -0.18f * s), Size(0.024f * s, 0.086f * s))
    }
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p14, sil ?: Color(0xFFE3A85A))
    drawPath(p14, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artPensee(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.018f * s, -0.212f * s); cubicTo(0.052f * s, -0.107f * s, 0.005f * s, 0.003f * s, -0.086f * s, 0.033f * s); cubicTo(-0.178f * s, 0.062f * s, -0.28f * s, 0.001f * s, -0.314f * s, -0.104f * s); cubicTo(-0.348f * s, -0.21f * s, -0.302f * s, -0.319f * s, -0.21f * s, -0.349f * s); cubicTo(-0.119f * s, -0.379f * s, -0.017f * s, -0.317f * s, 0.018f * s, -0.212f * s); close() }
    val p1 = Path().apply { moveTo(0.005f * s, -0.242f * s); cubicTo(0.039f * s, -0.137f * s, -0.008f * s, -0.028f * s, -0.099f * s, 0.002f * s); cubicTo(-0.191f * s, 0.032f * s, -0.293f * s, -0.029f * s, -0.327f * s, -0.135f * s); cubicTo(-0.361f * s, -0.24f * s, -0.315f * s, -0.35f * s, -0.223f * s, -0.379f * s); cubicTo(-0.132f * s, -0.409f * s, -0.03f * s, -0.348f * s, 0.005f * s, -0.242f * s); close() }
    val p2 = Path().apply { moveTo(-0.18f * s, -0.255f * s); cubicTo(-0.17f * s, -0.223f * s, -0.174f * s, -0.192f * s, -0.19f * s, -0.187f * s); cubicTo(-0.206f * s, -0.182f * s, -0.228f * s, -0.204f * s, -0.238f * s, -0.236f * s); cubicTo(-0.249f * s, -0.268f * s, -0.244f * s, -0.298f * s, -0.228f * s, -0.303f * s); cubicTo(-0.212f * s, -0.308f * s, -0.191f * s, -0.287f * s, -0.18f * s, -0.255f * s); close() }
    val p3 = Path().apply { moveTo(0.314f * s, -0.104f * s); cubicTo(0.28f * s, 0.001f * s, 0.178f * s, 0.062f * s, 0.086f * s, 0.033f * s); cubicTo(-0.005f * s, 0.003f * s, -0.052f * s, -0.107f * s, -0.018f * s, -0.212f * s); cubicTo(0.017f * s, -0.317f * s, 0.119f * s, -0.379f * s, 0.21f * s, -0.349f * s); cubicTo(0.302f * s, -0.319f * s, 0.348f * s, -0.21f * s, 0.314f * s, -0.104f * s); close() }
    val p4 = Path().apply { moveTo(0.301f * s, -0.135f * s); cubicTo(0.267f * s, -0.029f * s, 0.165f * s, 0.032f * s, 0.073f * s, 0.002f * s); cubicTo(-0.018f * s, -0.028f * s, -0.065f * s, -0.137f * s, -0.031f * s, -0.242f * s); cubicTo(0.004f * s, -0.348f * s, 0.106f * s, -0.409f * s, 0.197f * s, -0.379f * s); cubicTo(0.289f * s, -0.35f * s, 0.335f * s, -0.24f * s, 0.301f * s, -0.135f * s); close() }
    val p5 = Path().apply { moveTo(0.169f * s, -0.236f * s); cubicTo(0.158f * s, -0.204f * s, 0.137f * s, -0.182f * s, 0.121f * s, -0.187f * s); cubicTo(0.105f * s, -0.192f * s, 0.1f * s, -0.223f * s, 0.111f * s, -0.255f * s); cubicTo(0.121f * s, -0.287f * s, 0.142f * s, -0.308f * s, 0.158f * s, -0.303f * s); cubicTo(0.174f * s, -0.298f * s, 0.179f * s, -0.268f * s, 0.169f * s, -0.236f * s); close() }
    val p6 = Path().apply { moveTo(-0.076f * s, 0.107f * s); cubicTo(-0.098f * s, 0.191f * s, -0.196f * s, 0.238f * s, -0.293f * s, 0.212f * s); cubicTo(-0.391f * s, 0.185f * s, -0.452f * s, 0.096f * s, -0.43f * s, 0.012f * s); cubicTo(-0.407f * s, -0.071f * s, -0.31f * s, -0.118f * s, -0.212f * s, -0.092f * s); cubicTo(-0.115f * s, -0.065f * s, -0.053f * s, 0.024f * s, -0.076f * s, 0.107f * s); close() }
    val p7 = Path().apply { moveTo(-0.089f * s, 0.077f * s); cubicTo(-0.111f * s, 0.161f * s, -0.209f * s, 0.207f * s, -0.307f * s, 0.181f * s); cubicTo(-0.404f * s, 0.155f * s, -0.465f * s, 0.066f * s, -0.443f * s, -0.018f * s); cubicTo(-0.42f * s, -0.102f * s, -0.323f * s, -0.148f * s, -0.225f * s, -0.122f * s); cubicTo(-0.128f * s, -0.096f * s, -0.067f * s, -0.007f * s, -0.089f * s, 0.077f * s); close() }
    val p8 = Path().apply { moveTo(-0.291f * s, 0.003f * s); cubicTo(-0.303f * s, 0.024f * s, -0.323f * s, 0.035f * s, -0.336f * s, 0.028f * s); cubicTo(-0.348f * s, 0.021f * s, -0.349f * s, -0.002f * s, -0.337f * s, -0.023f * s); cubicTo(-0.324f * s, -0.044f * s, -0.305f * s, -0.055f * s, -0.292f * s, -0.048f * s); cubicTo(-0.28f * s, -0.04f * s, -0.279f * s, -0.018f * s, -0.291f * s, 0.003f * s); close() }
    val p9 = Path().apply { moveTo(0.43f * s, 0.012f * s); cubicTo(0.452f * s, 0.096f * s, 0.391f * s, 0.185f * s, 0.294f * s, 0.212f * s); cubicTo(0.196f * s, 0.238f * s, 0.098f * s, 0.191f * s, 0.076f * s, 0.107f * s); cubicTo(0.054f * s, 0.024f * s, 0.115f * s, -0.065f * s, 0.212f * s, -0.092f * s); cubicTo(0.31f * s, -0.118f * s, 0.407f * s, -0.071f * s, 0.43f * s, 0.012f * s); close() }
    val p10 = Path().apply { moveTo(0.417f * s, -0.018f * s); cubicTo(0.439f * s, 0.066f * s, 0.378f * s, 0.155f * s, 0.28f * s, 0.181f * s); cubicTo(0.183f * s, 0.207f * s, 0.085f * s, 0.161f * s, 0.063f * s, 0.077f * s); cubicTo(0.041f * s, -0.007f * s, 0.102f * s, -0.096f * s, 0.199f * s, -0.122f * s); cubicTo(0.297f * s, -0.148f * s, 0.394f * s, -0.102f * s, 0.417f * s, -0.018f * s); close() }
    val p11 = Path().apply { moveTo(0.337f * s, -0.023f * s); cubicTo(0.349f * s, -0.002f * s, 0.348f * s, 0.021f * s, 0.336f * s, 0.028f * s); cubicTo(0.323f * s, 0.035f * s, 0.303f * s, 0.024f * s, 0.291f * s, 0.003f * s); cubicTo(0.279f * s, -0.018f * s, 0.28f * s, -0.04f * s, 0.292f * s, -0.048f * s); cubicTo(0.305f * s, -0.055f * s, 0.325f * s, -0.044f * s, 0.337f * s, -0.023f * s); close() }
    val p12 = Path().apply { moveTo(0.235f * s, 0.165f * s); cubicTo(0.235f * s, 0.27f * s, 0.13f * s, 0.356f * s, 0.0f * s, 0.356f * s); cubicTo(-0.13f * s, 0.356f * s, -0.235f * s, 0.27f * s, -0.235f * s, 0.165f * s); cubicTo(-0.235f * s, 0.059f * s, -0.13f * s, -0.027f * s, 0.0f * s, -0.027f * s); cubicTo(0.13f * s, -0.027f * s, 0.235f * s, 0.059f * s, 0.235f * s, 0.165f * s); close() }
    val p13 = Path().apply { moveTo(0.218f * s, 0.13f * s); cubicTo(0.218f * s, 0.236f * s, 0.113f * s, 0.321f * s, -0.017f * s, 0.321f * s); cubicTo(-0.147f * s, 0.321f * s, -0.253f * s, 0.236f * s, -0.253f * s, 0.13f * s); cubicTo(-0.253f * s, 0.024f * s, -0.147f * s, -0.062f * s, -0.017f * s, -0.062f * s); cubicTo(0.113f * s, -0.062f * s, 0.218f * s, 0.024f * s, 0.218f * s, 0.13f * s); close() }
    val p14 = Path().apply { moveTo(-0.098f * s, 0.194f * s); cubicTo(-0.108f * s, 0.221f * s, -0.131f * s, 0.238f * s, -0.149f * s, 0.231f * s); cubicTo(-0.167f * s, 0.225f * s, -0.173f * s, 0.197f * s, -0.164f * s, 0.17f * s); cubicTo(-0.154f * s, 0.143f * s, -0.131f * s, 0.126f * s, -0.113f * s, 0.133f * s); cubicTo(-0.095f * s, 0.139f * s, -0.088f * s, 0.167f * s, -0.098f * s, 0.194f * s); close() }
    val p15 = Path().apply { moveTo(0.096f * s, -0.01f * s); cubicTo(0.096f * s, 0.043f * s, 0.053f * s, 0.086f * s, 0.0f * s, 0.086f * s); cubicTo(-0.053f * s, 0.086f * s, -0.096f * s, 0.043f * s, -0.096f * s, -0.01f * s); cubicTo(-0.096f * s, -0.063f * s, -0.053f * s, -0.106f * s, 0.0f * s, -0.106f * s); cubicTo(0.053f * s, -0.106f * s, 0.096f * s, -0.063f * s, 0.096f * s, -0.01f * s); close() }
    drawPath(p0, sil ?: Color(0xFF553193))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF6A3DB8))
        }
        drawPath(p2, Color(0x8CFFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF553193))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFF6A3DB8))
        }
        drawPath(p5, Color(0x8CFFFFFF))
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFF8B5CD6))
        }
        drawPath(p8, Color(0x8CFFFFFF))
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p9) {
            drawPath(p10, Color(0xFF8B5CD6))
        }
        drawPath(p11, Color(0x8CFFFFFF))
    }
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFF8D71C0))
    if (sil == null) {
        clipPath(p12) {
            drawPath(p13, Color(0xFFB08DF0))
        }
        drawPath(p14, Color(0x8CFFFFFF))
    }
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawLine(Color(0xFF3E2275), Offset(-0.035f * s, 0.095f * s), Offset(-0.087f * s, 0.252f * s), 0.024f * s, StrokeCap.Round)
        drawLine(Color(0xFF3E2275), Offset(0.0f * s, 0.112f * s), Offset(0.0f * s, 0.278f * s), 0.024f * s, StrokeCap.Round)
        drawLine(Color(0xFF3E2275), Offset(0.035f * s, 0.095f * s), Offset(0.087f * s, 0.252f * s), 0.024f * s, StrokeCap.Round)
    }
    drawPath(p15, sil ?: Color(0xFFFFD23F))
    drawPath(p15, sil ?: Color(0xFF2B2D42), style = Stroke(0.028f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFFFF8A1F), 0.031f * s, Offset(0.0f * s, -0.01f * s))
    }
}

private fun DrawScope.artDiamant(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.23f * s, -0.317f * s); quadraticTo(-0.204f * s, -0.341f * s, -0.169f * s, -0.341f * s); lineTo(0.169f * s, -0.341f * s); quadraticTo(0.204f * s, -0.341f * s, 0.23f * s, -0.317f * s); lineTo(0.418f * s, -0.135f * s); quadraticTo(0.444f * s, -0.11f * s, 0.419f * s, -0.085f * s); lineTo(0.025f * s, 0.325f * s); quadraticTo(0.0f * s, 0.351f * s, -0.025f * s, 0.325f * s); lineTo(-0.419f * s, -0.085f * s); quadraticTo(-0.444f * s, -0.11f * s, -0.418f * s, -0.135f * s); close() }
    val p1 = Path().apply { moveTo(-0.433f * s, -0.11f * s); quadraticTo(-0.444f * s, -0.11f * s, -0.436f * s, -0.118f * s); lineTo(-0.212f * s, -0.334f * s); quadraticTo(-0.204f * s, -0.341f * s, -0.202f * s, -0.331f * s); lineTo(-0.17f * s, -0.121f * s); quadraticTo(-0.169f * s, -0.11f * s, -0.179f * s, -0.11f * s); close() }
    val p2 = Path().apply { moveTo(-0.202f * s, -0.331f * s); quadraticTo(-0.204f * s, -0.341f * s, -0.193f * s, -0.341f * s); lineTo(0.194f * s, -0.341f * s); quadraticTo(0.204f * s, -0.341f * s, 0.203f * s, -0.331f * s); lineTo(0.17f * s, -0.121f * s); quadraticTo(0.169f * s, -0.11f * s, 0.158f * s, -0.11f * s); lineTo(-0.158f * s, -0.11f * s); quadraticTo(-0.169f * s, -0.11f * s, -0.17f * s, -0.121f * s); close() }
    val p3 = Path().apply { moveTo(0.203f * s, -0.331f * s); quadraticTo(0.204f * s, -0.341f * s, 0.212f * s, -0.334f * s); lineTo(0.436f * s, -0.118f * s); quadraticTo(0.444f * s, -0.11f * s, 0.433f * s, -0.11f * s); lineTo(0.179f * s, -0.11f * s); quadraticTo(0.169f * s, -0.11f * s, 0.17f * s, -0.121f * s); close() }
    val p4 = Path().apply { moveTo(-0.436f * s, -0.103f * s); quadraticTo(-0.444f * s, -0.11f * s, -0.433f * s, -0.11f * s); lineTo(-0.179f * s, -0.11f * s); quadraticTo(-0.169f * s, -0.11f * s, -0.165f * s, -0.1f * s); lineTo(-0.004f * s, 0.341f * s); quadraticTo(0.0f * s, 0.351f * s, -0.007f * s, 0.343f * s); close() }
    val p5 = Path().apply { moveTo(-0.165f * s, -0.1f * s); quadraticTo(-0.169f * s, -0.11f * s, -0.158f * s, -0.11f * s); lineTo(0.158f * s, -0.11f * s); quadraticTo(0.169f * s, -0.11f * s, 0.165f * s, -0.1f * s); lineTo(0.004f * s, 0.341f * s); quadraticTo(0.0f * s, 0.351f * s, -0.004f * s, 0.341f * s); close() }
    val p6 = Path().apply { moveTo(0.165f * s, -0.1f * s); quadraticTo(0.169f * s, -0.11f * s, 0.179f * s, -0.11f * s); lineTo(0.433f * s, -0.11f * s); quadraticTo(0.444f * s, -0.11f * s, 0.436f * s, -0.103f * s); lineTo(0.007f * s, 0.343f * s); quadraticTo(0.0f * s, 0.351f * s, 0.004f * s, 0.341f * s); close() }
    val p7 = Path().apply { moveTo(-0.11f * s, -0.28f * s); quadraticTo(-0.106f * s, -0.288f * s, -0.098f * s, -0.288f * s); lineTo(-0.044f * s, -0.288f * s); quadraticTo(-0.035f * s, -0.288f * s, -0.039f * s, -0.28f * s); lineTo(-0.085f * s, -0.189f * s); quadraticTo(-0.089f * s, -0.181f * s, -0.098f * s, -0.181f * s); lineTo(-0.142f * s, -0.181f * s); quadraticTo(-0.151f * s, -0.181f * s, -0.147f * s, -0.19f * s); close() }
    drawPath(p0, sil ?: Color(0xFF6A3DB3))
    drawPath(p1, sil ?: Color(0xFFA27BEA))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFCDB1FA))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFA27BEA))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF7B4DC7))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF9467DD))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF6A3DB3))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p7, Color(0x8CFFFFFF))
    }
}

private fun DrawScope.artBaguette(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.326f * s, 0.397f * s); cubicTo(-0.345f * s, 0.423f * s, -0.381f * s, 0.429f * s, -0.408f * s, 0.411f * s); cubicTo(-0.434f * s, 0.392f * s, -0.44f * s, 0.356f * s, -0.422f * s, 0.33f * s); lineTo(-0.109f * s, -0.117f * s); cubicTo(-0.091f * s, -0.143f * s, -0.054f * s, -0.15f * s, -0.028f * s, -0.131f * s); cubicTo(-0.001f * s, -0.113f * s, 0.005f * s, -0.076f * s, -0.014f * s, -0.05f * s); close() }
    val p1 = Path().apply { moveTo(-0.327f * s, 0.372f * s); cubicTo(-0.345f * s, 0.399f * s, -0.382f * s, 0.405f * s, -0.408f * s, 0.387f * s); cubicTo(-0.435f * s, 0.368f * s, -0.441f * s, 0.332f * s, -0.423f * s, 0.305f * s); lineTo(-0.11f * s, -0.141f * s); cubicTo(-0.091f * s, -0.168f * s, -0.055f * s, -0.174f * s, -0.029f * s, -0.156f * s); cubicTo(-0.002f * s, -0.137f * s, 0.004f * s, -0.101f * s, -0.014f * s, -0.074f * s); close() }
    val p2 = Path().apply { moveTo(-0.381f * s, 0.204f * s); lineTo(-0.222f * s, 0.315f * s); cubicTo(-0.222f * s, 0.315f * s, -0.222f * s, 0.315f * s, -0.222f * s, 0.315f * s); lineTo(-0.333f * s, 0.475f * s); cubicTo(-0.333f * s, 0.475f * s, -0.333f * s, 0.475f * s, -0.333f * s, 0.475f * s); lineTo(-0.493f * s, 0.363f * s); cubicTo(-0.493f * s, 0.363f * s, -0.493f * s, 0.363f * s, -0.493f * s, 0.363f * s); lineTo(-0.381f * s, 0.204f * s); cubicTo(-0.381f * s, 0.204f * s, -0.381f * s, 0.204f * s, -0.381f * s, 0.204f * s); close() }
    val p3 = Path().apply { moveTo(-0.169f * s, -0.099f * s); lineTo(-0.01f * s, 0.012f * s); cubicTo(-0.01f * s, 0.012f * s, -0.01f * s, 0.012f * s, -0.01f * s, 0.012f * s); lineTo(-0.049f * s, 0.068f * s); cubicTo(-0.049f * s, 0.068f * s, -0.049f * s, 0.068f * s, -0.049f * s, 0.068f * s); lineTo(-0.208f * s, -0.044f * s); cubicTo(-0.208f * s, -0.044f * s, -0.208f * s, -0.044f * s, -0.208f * s, -0.044f * s); lineTo(-0.169f * s, -0.099f * s); cubicTo(-0.169f * s, -0.099f * s, -0.169f * s, -0.099f * s, -0.169f * s, -0.099f * s); close() }
    val p4 = Path().apply { moveTo(0.151f * s, -0.415f * s); quadraticTo(0.184f * s, -0.434f * s, 0.177f * s, -0.396f * s); lineTo(0.163f * s, -0.318f * s); quadraticTo(0.156f * s, -0.279f * s, 0.185f * s, -0.254f * s); lineTo(0.245f * s, -0.201f * s); quadraticTo(0.274f * s, -0.175f * s, 0.235f * s, -0.169f * s); lineTo(0.156f * s, -0.159f * s); quadraticTo(0.118f * s, -0.154f * s, 0.102f * s, -0.118f * s); lineTo(0.07f * s, -0.045f * s); quadraticTo(0.054f * s, -0.009f * s, 0.038f * s, -0.044f * s); lineTo(0.003f * s, -0.116f * s); quadraticTo(-0.014f * s, -0.151f * s, -0.053f * s, -0.155f * s); lineTo(-0.132f * s, -0.163f * s); quadraticTo(-0.171f * s, -0.167f * s, -0.142f * s, -0.194f * s); lineTo(-0.085f * s, -0.249f * s); quadraticTo(-0.057f * s, -0.276f * s, -0.065f * s, -0.314f * s); lineTo(-0.082f * s, -0.391f * s); quadraticTo(-0.09f * s, -0.43f * s, -0.056f * s, -0.411f * s); lineTo(0.014f * s, -0.373f * s); quadraticTo(0.048f * s, -0.355f * s, 0.082f * s, -0.375f * s); close() }
    val p5 = Path().apply { moveTo(0.154f * s, -0.454f * s); quadraticTo(0.188f * s, -0.473f * s, 0.181f * s, -0.435f * s); lineTo(0.167f * s, -0.357f * s); quadraticTo(0.16f * s, -0.318f * s, 0.189f * s, -0.293f * s); lineTo(0.248f * s, -0.24f * s); quadraticTo(0.277f * s, -0.214f * s, 0.239f * s, -0.208f * s); lineTo(0.16f * s, -0.198f * s); quadraticTo(0.121f * s, -0.193f * s, 0.106f * s, -0.157f * s); lineTo(0.074f * s, -0.084f * s); quadraticTo(0.058f * s, -0.048f * s, 0.041f * s, -0.083f * s); lineTo(0.007f * s, -0.155f * s); quadraticTo(-0.01f * s, -0.19f * s, -0.049f * s, -0.194f * s); lineTo(-0.128f * s, -0.202f * s); quadraticTo(-0.167f * s, -0.206f * s, -0.139f * s, -0.233f * s); lineTo(-0.081f * s, -0.288f * s); quadraticTo(-0.053f * s, -0.315f * s, -0.061f * s, -0.353f * s); lineTo(-0.078f * s, -0.431f * s); quadraticTo(-0.087f * s, -0.469f * s, -0.052f * s, -0.45f * s); lineTo(0.018f * s, -0.412f * s); quadraticTo(0.052f * s, -0.394f * s, 0.086f * s, -0.414f * s); close() }
    val p6 = Path().apply { moveTo(0.032f * s, -0.309f * s); cubicTo(0.008f * s, -0.298f * s, -0.017f * s, -0.3f * s, -0.024f * s, -0.315f * s); cubicTo(-0.031f * s, -0.33f * s, -0.017f * s, -0.351f * s, 0.008f * s, -0.362f * s); cubicTo(0.032f * s, -0.373f * s, 0.057f * s, -0.371f * s, 0.064f * s, -0.356f * s); cubicTo(0.071f * s, -0.341f * s, 0.057f * s, -0.32f * s, 0.032f * s, -0.309f * s); close() }
    val p7 = Path().apply { moveTo(0.424f * s, -0.267f * s); quadraticTo(0.424f * s, -0.267f * s, 0.424f * s, -0.267f * s); lineTo(0.408f * s, -0.215f * s); quadraticTo(0.408f * s, -0.215f * s, 0.408f * s, -0.215f * s); lineTo(0.44f * s, -0.172f * s); quadraticTo(0.44f * s, -0.172f * s, 0.44f * s, -0.172f * s); lineTo(0.389f * s, -0.187f * s); quadraticTo(0.389f * s, -0.187f * s, 0.389f * s, -0.187f * s); lineTo(0.345f * s, -0.155f * s); quadraticTo(0.345f * s, -0.155f * s, 0.345f * s, -0.155f * s); lineTo(0.36f * s, -0.207f * s); quadraticTo(0.36f * s, -0.207f * s, 0.36f * s, -0.207f * s); lineTo(0.329f * s, -0.25f * s); quadraticTo(0.329f * s, -0.25f * s, 0.329f * s, -0.25f * s); lineTo(0.38f * s, -0.235f * s); quadraticTo(0.38f * s, -0.235f * s, 0.38f * s, -0.235f * s); close() }
    val p8 = Path().apply { moveTo(-0.256f * s, -0.315f * s); quadraticTo(-0.256f * s, -0.315f * s, -0.256f * s, -0.315f * s); lineTo(-0.27f * s, -0.27f * s); quadraticTo(-0.27f * s, -0.27f * s, -0.27f * s, -0.27f * s); lineTo(-0.242f * s, -0.233f * s); quadraticTo(-0.242f * s, -0.233f * s, -0.242f * s, -0.233f * s); lineTo(-0.286f * s, -0.248f * s); quadraticTo(-0.286f * s, -0.248f * s, -0.286f * s, -0.248f * s); lineTo(-0.323f * s, -0.219f * s); quadraticTo(-0.323f * s, -0.219f * s, -0.323f * s, -0.219f * s); lineTo(-0.309f * s, -0.264f * s); quadraticTo(-0.309f * s, -0.264f * s, -0.309f * s, -0.264f * s); lineTo(-0.337f * s, -0.3f * s); quadraticTo(-0.337f * s, -0.3f * s, -0.337f * s, -0.3f * s); lineTo(-0.293f * s, -0.286f * s); quadraticTo(-0.293f * s, -0.286f * s, -0.293f * s, -0.286f * s); close() }
    drawPath(p0, sil ?: Color(0xFF5D399D))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF7447C4))
        }
        clipPath(p0) {
            drawPath(p2, Color(0xFFFFFFFF))
            drawPath(p3, Color(0xFFC9A9F7))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFA182CC))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFC9A3FF))
        }
        drawPath(p6, Color(0xB3FFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFFFD23F))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFFFD23F))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artChapeaumagique(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.434f * s, 0.278f * s); cubicTo(0.434f * s, 0.345f * s, 0.24f * s, 0.4f * s, 0.0f * s, 0.4f * s); cubicTo(-0.24f * s, 0.4f * s, -0.434f * s, 0.345f * s, -0.434f * s, 0.278f * s); cubicTo(-0.434f * s, 0.211f * s, -0.24f * s, 0.156f * s, 0.0f * s, 0.156f * s); cubicTo(0.24f * s, 0.156f * s, 0.434f * s, 0.211f * s, 0.434f * s, 0.278f * s); close() }
    val p1 = Path().apply { moveTo(0.417f * s, 0.243f * s); cubicTo(0.417f * s, 0.31f * s, 0.223f * s, 0.365f * s, -0.017f * s, 0.365f * s); cubicTo(-0.257f * s, 0.365f * s, -0.452f * s, 0.31f * s, -0.452f * s, 0.243f * s); cubicTo(-0.452f * s, 0.176f * s, -0.257f * s, 0.122f * s, -0.017f * s, 0.122f * s); cubicTo(0.223f * s, 0.122f * s, 0.417f * s, 0.176f * s, 0.417f * s, 0.243f * s); close() }
    val p2 = Path().apply { moveTo(-0.269f * s, 0.278f * s); cubicTo(-0.226f * s, 0.017f * s, -0.087f * s, -0.243f * s, 0.104f * s, -0.4f * s); cubicTo(0.174f * s, -0.156f * s, 0.243f * s, 0.061f * s, 0.269f * s, 0.278f * s); quadraticTo(0.0f * s, 0.365f * s, -0.269f * s, 0.278f * s); close() }
    val p3 = Path().apply { moveTo(-0.287f * s, 0.252f * s); cubicTo(-0.243f * s, -0.009f * s, -0.104f * s, -0.269f * s, 0.087f * s, -0.426f * s); cubicTo(0.156f * s, -0.182f * s, 0.226f * s, 0.035f * s, 0.252f * s, 0.252f * s); quadraticTo(-0.017f * s, 0.339f * s, -0.287f * s, 0.252f * s); close() }
    val p4 = Path().apply { moveTo(-0.347f * s, 0.165f * s); quadraticTo(0.0f * s, 0.261f * s, 0.348f * s, 0.165f * s); lineTo(0.348f * s, 0.452f * s); lineTo(-0.347f * s, 0.452f * s); close() }
    val p5 = Path().apply { moveTo(-0.347f * s, 0.165f * s); quadraticTo(0.0f * s, 0.261f * s, 0.348f * s, 0.165f * s) }
    val p6 = Path().apply { moveTo(-0.031f * s, -0.085f * s); quadraticTo(-0.026f * s, -0.096f * s, -0.021f * s, -0.085f * s); lineTo(-0.004f * s, -0.047f * s); quadraticTo(0.001f * s, -0.037f * s, 0.012f * s, -0.036f * s); lineTo(0.053f * s, -0.031f * s); quadraticTo(0.065f * s, -0.029f * s, 0.056f * s, -0.022f * s); lineTo(0.026f * s, 0.006f * s); quadraticTo(0.018f * s, 0.014f * s, 0.02f * s, 0.026f * s); lineTo(0.028f * s, 0.066f * s); quadraticTo(0.03f * s, 0.077f * s, 0.02f * s, 0.072f * s); lineTo(-0.016f * s, 0.051f * s); quadraticTo(-0.026f * s, 0.046f * s, -0.036f * s, 0.051f * s); lineTo(-0.072f * s, 0.072f * s); quadraticTo(-0.082f * s, 0.077f * s, -0.08f * s, 0.066f * s); lineTo(-0.072f * s, 0.026f * s); quadraticTo(-0.07f * s, 0.014f * s, -0.078f * s, 0.006f * s); lineTo(-0.108f * s, -0.022f * s); quadraticTo(-0.117f * s, -0.029f * s, -0.106f * s, -0.031f * s); lineTo(-0.064f * s, -0.036f * s); quadraticTo(-0.053f * s, -0.037f * s, -0.048f * s, -0.047f * s); close() }
    val p7 = Path().apply { moveTo(0.058f * s, -0.285f * s); quadraticTo(0.061f * s, -0.291f * s, 0.064f * s, -0.285f * s); lineTo(0.074f * s, -0.263f * s); quadraticTo(0.077f * s, -0.256f * s, 0.084f * s, -0.256f * s); lineTo(0.108f * s, -0.253f * s); quadraticTo(0.115f * s, -0.252f * s, 0.11f * s, -0.247f * s); lineTo(0.092f * s, -0.231f * s); quadraticTo(0.087f * s, -0.226f * s, 0.088f * s, -0.219f * s); lineTo(0.093f * s, -0.196f * s); quadraticTo(0.094f * s, -0.189f * s, 0.088f * s, -0.192f * s); lineTo(0.067f * s, -0.204f * s); quadraticTo(0.061f * s, -0.207f * s, 0.055f * s, -0.204f * s); lineTo(0.034f * s, -0.192f * s); quadraticTo(0.028f * s, -0.189f * s, 0.029f * s, -0.196f * s); lineTo(0.034f * s, -0.219f * s); quadraticTo(0.035f * s, -0.226f * s, 0.03f * s, -0.231f * s); lineTo(0.012f * s, -0.247f * s); quadraticTo(0.007f * s, -0.252f * s, 0.014f * s, -0.253f * s); lineTo(0.038f * s, -0.256f * s); quadraticTo(0.045f * s, -0.256f * s, 0.048f * s, -0.263f * s); close() }
    drawPath(p0, sil ?: Color(0xFF593397))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF6F40BD))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF6F4AAB))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF8B5CD6))
        }
    }
    clipPath(p2) {
        if (sil == null) {
            drawPath(p4, Color(0xFFFFD23F))
        }
        drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    clipPath(p2) {
        drawPath(p6, sil ?: Color(0xFFFFD23F))
        drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p7, sil ?: Color(0xFFFFD23F))
        drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

private fun DrawScope.artDonut(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.433f * s, 0.0f * s); cubicTo(0.433f * s, 0.239f * s, 0.239f * s, 0.433f * s, 0.0f * s, 0.433f * s); cubicTo(-0.239f * s, 0.433f * s, -0.433f * s, 0.239f * s, -0.433f * s, 0.0f * s); cubicTo(-0.433f * s, -0.239f * s, -0.239f * s, -0.433f * s, 0.0f * s, -0.433f * s); cubicTo(0.239f * s, -0.433f * s, 0.433f * s, -0.239f * s, 0.433f * s, 0.0f * s); close(); moveTo(0.138f * s, 0.0f * s); cubicTo(0.138f * s, -0.076f * s, 0.076f * s, -0.138f * s, 0.0f * s, -0.138f * s); cubicTo(-0.076f * s, -0.138f * s, -0.138f * s, -0.076f * s, -0.138f * s, 0.0f * s); cubicTo(-0.138f * s, 0.076f * s, -0.076f * s, 0.138f * s, 0.0f * s, 0.138f * s); cubicTo(0.076f * s, 0.138f * s, 0.138f * s, 0.076f * s, 0.138f * s, 0.0f * s); close() }
    val p1 = Path().apply { moveTo(0.415f * s, -0.037f * s); cubicTo(0.415f * s, 0.203f * s, 0.221f * s, 0.397f * s, -0.018f * s, 0.397f * s); cubicTo(-0.258f * s, 0.397f * s, -0.452f * s, 0.203f * s, -0.452f * s, -0.037f * s); cubicTo(-0.452f * s, -0.276f * s, -0.258f * s, -0.47f * s, -0.018f * s, -0.47f * s); cubicTo(0.221f * s, -0.47f * s, 0.415f * s, -0.276f * s, 0.415f * s, -0.037f * s); close(); moveTo(0.12f * s, -0.037f * s); cubicTo(0.12f * s, -0.113f * s, 0.058f * s, -0.175f * s, -0.018f * s, -0.175f * s); cubicTo(-0.095f * s, -0.175f * s, -0.157f * s, -0.113f * s, -0.157f * s, -0.037f * s); cubicTo(-0.157f * s, 0.04f * s, -0.095f * s, 0.101f * s, -0.018f * s, 0.101f * s); cubicTo(0.058f * s, 0.101f * s, 0.12f * s, 0.04f * s, 0.12f * s, -0.037f * s); close() }
    val p2 = Path().apply { moveTo(0.387f * s, 0.0f * s); cubicTo(0.387f * s, 0.022f * s, 0.378f * s, 0.045f * s, 0.37f * s, 0.065f * s); cubicTo(0.361f * s, 0.085f * s, 0.344f * s, 0.103f * s, 0.333f * s, 0.121f * s); cubicTo(0.323f * s, 0.14f * s, 0.314f * s, 0.157f * s, 0.306f * s, 0.176f * s); cubicTo(0.298f * s, 0.196f * s, 0.295f * s, 0.219f * s, 0.285f * s, 0.239f * s); cubicTo(0.276f * s, 0.259f * s, 0.265f * s, 0.282f * s, 0.249f * s, 0.297f * s); cubicTo(0.233f * s, 0.311f * s, 0.21f * s, 0.321f * s, 0.189f * s, 0.327f * s); cubicTo(0.168f * s, 0.334f * s, 0.143f * s, 0.332f * s, 0.122f * s, 0.336f * s); cubicTo(0.101f * s, 0.339f * s, 0.081f * s, 0.341f * s, 0.061f * s, 0.346f * s); cubicTo(0.041f * s, 0.352f * s, 0.021f * s, 0.363f * s, 0.0f * s, 0.369f * s); cubicTo(-0.021f * s, 0.375f * s, -0.045f * s, 0.382f * s, -0.067f * s, 0.38f * s); cubicTo(-0.089f * s, 0.378f * s, -0.111f * s, 0.369f * s, -0.13f * s, 0.358f * s); cubicTo(-0.149f * s, 0.346f * s, -0.164f * s, 0.326f * s, -0.18f * s, 0.311f * s); cubicTo(-0.196f * s, 0.297f * s, -0.209f * s, 0.281f * s, -0.225f * s, 0.269f * s); cubicTo(-0.242f * s, 0.256f * s, -0.262f * s, 0.248f * s, -0.28f * s, 0.235f * s); cubicTo(-0.298f * s, 0.222f * s, -0.32f * s, 0.21f * s, -0.333f * s, 0.192f * s); cubicTo(-0.346f * s, 0.175f * s, -0.356f * s, 0.153f * s, -0.36f * s, 0.131f * s); cubicTo(-0.364f * s, 0.109f * s, -0.359f * s, 0.085f * s, -0.357f * s, 0.063f * s); cubicTo(-0.355f * s, 0.041f * s, -0.35f * s, 0.021f * s, -0.35f * s, 0.0f * s); cubicTo(-0.35f * s, -0.021f * s, -0.355f * s, -0.041f * s, -0.357f * s, -0.063f * s); cubicTo(-0.359f * s, -0.085f * s, -0.364f * s, -0.109f * s, -0.36f * s, -0.131f * s); cubicTo(-0.356f * s, -0.152f * s, -0.346f * s, -0.175f * s, -0.333f * s, -0.192f * s); cubicTo(-0.32f * s, -0.21f * s, -0.298f * s, -0.222f * s, -0.28f * s, -0.235f * s); cubicTo(-0.262f * s, -0.248f * s, -0.242f * s, -0.256f * s, -0.225f * s, -0.269f * s); cubicTo(-0.209f * s, -0.281f * s, -0.196f * s, -0.297f * s, -0.18f * s, -0.311f * s); cubicTo(-0.164f * s, -0.326f * s, -0.149f * s, -0.346f * s, -0.13f * s, -0.358f * s); cubicTo(-0.111f * s, -0.369f * s, -0.089f * s, -0.378f * s, -0.067f * s, -0.38f * s); cubicTo(-0.045f * s, -0.382f * s, -0.021f * s, -0.375f * s, 0.0f * s, -0.369f * s); cubicTo(0.021f * s, -0.363f * s, 0.041f * s, -0.352f * s, 0.061f * s, -0.346f * s); cubicTo(0.081f * s, -0.341f * s, 0.101f * s, -0.339f * s, 0.122f * s, -0.335f * s); cubicTo(0.143f * s, -0.332f * s, 0.168f * s, -0.334f * s, 0.189f * s, -0.327f * s); cubicTo(0.21f * s, -0.321f * s, 0.233f * s, -0.311f * s, 0.249f * s, -0.296f * s); cubicTo(0.265f * s, -0.282f * s, 0.276f * s, -0.259f * s, 0.285f * s, -0.239f * s); cubicTo(0.295f * s, -0.219f * s, 0.298f * s, -0.196f * s, 0.306f * s, -0.176f * s); cubicTo(0.314f * s, -0.157f * s, 0.323f * s, -0.14f * s, 0.333f * s, -0.121f * s); cubicTo(0.344f * s, -0.103f * s, 0.361f * s, -0.085f * s, 0.37f * s, -0.065f * s); cubicTo(0.378f * s, -0.045f * s, 0.387f * s, -0.022f * s, 0.387f * s, 0.0f * s); close(); moveTo(0.184f * s, 0.0f * s); cubicTo(0.184f * s, -0.102f * s, 0.102f * s, -0.184f * s, 0.0f * s, -0.184f * s); cubicTo(-0.102f * s, -0.184f * s, -0.184f * s, -0.102f * s, -0.184f * s, 0.0f * s); cubicTo(-0.184f * s, 0.102f * s, -0.102f * s, 0.184f * s, 0.0f * s, 0.184f * s); cubicTo(0.102f * s, 0.184f * s, 0.184f * s, 0.102f * s, 0.184f * s, 0.0f * s); close() }
    val p3 = Path().apply { moveTo(0.374f * s, -0.028f * s); cubicTo(0.374f * s, -0.006f * s, 0.365f * s, 0.017f * s, 0.356f * s, 0.037f * s); cubicTo(0.347f * s, 0.058f * s, 0.33f * s, 0.075f * s, 0.32f * s, 0.094f * s); cubicTo(0.309f * s, 0.112f * s, 0.3f * s, 0.129f * s, 0.292f * s, 0.149f * s); cubicTo(0.284f * s, 0.168f * s, 0.281f * s, 0.192f * s, 0.271f * s, 0.211f * s); cubicTo(0.262f * s, 0.232f * s, 0.251f * s, 0.254f * s, 0.235f * s, 0.269f * s); cubicTo(0.219f * s, 0.284f * s, 0.196f * s, 0.293f * s, 0.175f * s, 0.3f * s); cubicTo(0.154f * s, 0.306f * s, 0.13f * s, 0.305f * s, 0.108f * s, 0.308f * s); cubicTo(0.087f * s, 0.311f * s, 0.068f * s, 0.313f * s, 0.047f * s, 0.319f * s); cubicTo(0.027f * s, 0.324f * s, 0.008f * s, 0.336f * s, -0.014f * s, 0.341f * s); cubicTo(-0.035f * s, 0.347f * s, -0.059f * s, 0.355f * s, -0.081f * s, 0.353f * s); cubicTo(-0.103f * s, 0.351f * s, -0.125f * s, 0.342f * s, -0.144f * s, 0.33f * s); cubicTo(-0.163f * s, 0.319f * s, -0.178f * s, 0.299f * s, -0.194f * s, 0.284f * s); cubicTo(-0.209f * s, 0.269f * s, -0.222f * s, 0.254f * s, -0.239f * s, 0.241f * s); cubicTo(-0.256f * s, 0.228f * s, -0.276f * s, 0.22f * s, -0.294f * s, 0.207f * s); cubicTo(-0.312f * s, 0.195f * s, -0.334f * s, 0.182f * s, -0.347f * s, 0.165f * s); cubicTo(-0.36f * s, 0.147f * s, -0.37f * s, 0.125f * s, -0.374f * s, 0.103f * s); cubicTo(-0.378f * s, 0.082f * s, -0.372f * s, 0.057f * s, -0.371f * s, 0.035f * s); cubicTo(-0.369f * s, 0.014f * s, -0.364f * s, -0.007f * s, -0.364f * s, -0.028f * s); cubicTo(-0.364f * s, -0.049f * s, -0.369f * s, -0.069f * s, -0.371f * s, -0.091f * s); cubicTo(-0.372f * s, -0.112f * s, -0.378f * s, -0.137f * s, -0.374f * s, -0.159f * s); cubicTo(-0.37f * s, -0.18f * s, -0.36f * s, -0.203f * s, -0.347f * s, -0.22f * s); cubicTo(-0.334f * s, -0.237f * s, -0.312f * s, -0.25f * s, -0.294f * s, -0.263f * s); cubicTo(-0.276f * s, -0.275f * s, -0.256f * s, -0.284f * s, -0.239f * s, -0.296f * s); cubicTo(-0.222f * s, -0.309f * s, -0.209f * s, -0.324f * s, -0.194f * s, -0.339f * s); cubicTo(-0.178f * s, -0.354f * s, -0.163f * s, -0.374f * s, -0.144f * s, -0.385f * s); cubicTo(-0.125f * s, -0.397f * s, -0.103f * s, -0.406f * s, -0.081f * s, -0.408f * s); cubicTo(-0.059f * s, -0.41f * s, -0.035f * s, -0.402f * s, -0.014f * s, -0.396f * s); cubicTo(0.008f * s, -0.391f * s, 0.027f * s, -0.379f * s, 0.047f * s, -0.374f * s); cubicTo(0.068f * s, -0.368f * s, 0.087f * s, -0.366f * s, 0.108f * s, -0.363f * s); cubicTo(0.13f * s, -0.36f * s, 0.154f * s, -0.362f * s, 0.175f * s, -0.355f * s); cubicTo(0.196f * s, -0.349f * s, 0.219f * s, -0.339f * s, 0.235f * s, -0.324f * s); cubicTo(0.251f * s, -0.309f * s, 0.262f * s, -0.287f * s, 0.271f * s, -0.267f * s); cubicTo(0.281f * s, -0.247f * s, 0.284f * s, -0.224f * s, 0.292f * s, -0.204f * s); cubicTo(0.3f * s, -0.184f * s, 0.309f * s, -0.168f * s, 0.32f * s, -0.149f * s); cubicTo(0.33f * s, -0.13f * s, 0.347f * s, -0.113f * s, 0.356f * s, -0.093f * s); cubicTo(0.365f * s, -0.073f * s, 0.374f * s, -0.049f * s, 0.374f * s, -0.028f * s); close(); moveTo(0.171f * s, -0.028f * s); cubicTo(0.171f * s, -0.13f * s, 0.088f * s, -0.212f * s, -0.014f * s, -0.212f * s); cubicTo(-0.116f * s, -0.212f * s, -0.198f * s, -0.13f * s, -0.198f * s, -0.028f * s); cubicTo(-0.198f * s, 0.074f * s, -0.116f * s, 0.157f * s, -0.014f * s, 0.157f * s); cubicTo(0.088f * s, 0.157f * s, 0.171f * s, 0.074f * s, 0.171f * s, -0.028f * s); close() }
    val p4 = Path().apply { moveTo(0.305f * s, -0.09f * s); cubicTo(0.314f * s, -0.085f * s, 0.317f * s, -0.073f * s, 0.312f * s, -0.065f * s); cubicTo(0.307f * s, -0.056f * s, 0.296f * s, -0.053f * s, 0.287f * s, -0.058f * s); lineTo(0.215f * s, -0.099f * s); cubicTo(0.206f * s, -0.104f * s, 0.203f * s, -0.116f * s, 0.208f * s, -0.125f * s); cubicTo(0.213f * s, -0.133f * s, 0.224f * s, -0.136f * s, 0.233f * s, -0.131f * s); close() }
    val p5 = Path().apply { moveTo(0.268f * s, 0.104f * s); cubicTo(0.275f * s, 0.096f * s, 0.286f * s, 0.095f * s, 0.294f * s, 0.102f * s); cubicTo(0.302f * s, 0.108f * s, 0.303f * s, 0.12f * s, 0.296f * s, 0.128f * s); lineTo(0.243f * s, 0.191f * s); cubicTo(0.236f * s, 0.199f * s, 0.225f * s, 0.2f * s, 0.217f * s, 0.194f * s); cubicTo(0.209f * s, 0.187f * s, 0.208f * s, 0.175f * s, 0.215f * s, 0.168f * s); close() }
    val p6 = Path().apply { moveTo(0.09f * s, 0.251f * s); cubicTo(0.1f * s, 0.255f * s, 0.105f * s, 0.265f * s, 0.101f * s, 0.275f * s); cubicTo(0.098f * s, 0.284f * s, 0.087f * s, 0.289f * s, 0.078f * s, 0.286f * s); lineTo(0.0f * s, 0.257f * s); cubicTo(-0.01f * s, 0.254f * s, -0.015f * s, 0.243f * s, -0.011f * s, 0.234f * s); cubicTo(-0.008f * s, 0.224f * s, 0.003f * s, 0.219f * s, 0.012f * s, 0.223f * s); close() }
    val p7 = Path().apply { moveTo(-0.137f * s, 0.197f * s); cubicTo(-0.128f * s, 0.192f * s, -0.117f * s, 0.195f * s, -0.112f * s, 0.204f * s); cubicTo(-0.107f * s, 0.213f * s, -0.11f * s, 0.224f * s, -0.119f * s, 0.229f * s); lineTo(-0.191f * s, 0.271f * s); cubicTo(-0.199f * s, 0.276f * s, -0.211f * s, 0.273f * s, -0.216f * s, 0.264f * s); cubicTo(-0.221f * s, 0.255f * s, -0.218f * s, 0.244f * s, -0.209f * s, 0.239f * s); close() }
    val p8 = Path().apply { moveTo(-0.239f * s, 0.051f * s); cubicTo(-0.234f * s, 0.06f * s, -0.237f * s, 0.071f * s, -0.246f * s, 0.076f * s); cubicTo(-0.254f * s, 0.081f * s, -0.266f * s, 0.078f * s, -0.271f * s, 0.069f * s); lineTo(-0.312f * s, -0.003f * s); cubicTo(-0.317f * s, -0.011f * s, -0.314f * s, -0.023f * s, -0.306f * s, -0.028f * s); cubicTo(-0.297f * s, -0.033f * s, -0.285f * s, -0.03f * s, -0.28f * s, -0.021f * s); close() }
    val p9 = Path().apply { moveTo(-0.151f * s, -0.207f * s); cubicTo(-0.141f * s, -0.205f * s, -0.135f * s, -0.195f * s, -0.137f * s, -0.185f * s); cubicTo(-0.138f * s, -0.175f * s, -0.148f * s, -0.168f * s, -0.158f * s, -0.17f * s); lineTo(-0.24f * s, -0.185f * s); cubicTo(-0.25f * s, -0.186f * s, -0.256f * s, -0.196f * s, -0.255f * s, -0.206f * s); cubicTo(-0.253f * s, -0.216f * s, -0.243f * s, -0.223f * s, -0.233f * s, -0.221f * s); close() }
    val p10 = Path().apply { moveTo(0.02f * s, -0.317f * s); cubicTo(0.028f * s, -0.324f * s, 0.039f * s, -0.323f * s, 0.046f * s, -0.315f * s); cubicTo(0.053f * s, -0.307f * s, 0.052f * s, -0.296f * s, 0.044f * s, -0.289f * s); lineTo(-0.02f * s, -0.236f * s); cubicTo(-0.028f * s, -0.229f * s, -0.039f * s, -0.23f * s, -0.046f * s, -0.238f * s); cubicTo(-0.052f * s, -0.246f * s, -0.051f * s, -0.258f * s, -0.044f * s, -0.264f * s); close() }
    val p11 = Path().apply { moveTo(0.243f * s, -0.145f * s); cubicTo(0.247f * s, -0.136f * s, 0.242f * s, -0.125f * s, 0.232f * s, -0.121f * s); cubicTo(0.223f * s, -0.118f * s, 0.212f * s, -0.123f * s, 0.209f * s, -0.132f * s); lineTo(0.18f * s, -0.21f * s); cubicTo(0.177f * s, -0.22f * s, 0.182f * s, -0.231f * s, 0.191f * s, -0.234f * s); cubicTo(0.201f * s, -0.238f * s, 0.212f * s, -0.233f * s, 0.215f * s, -0.223f * s); close() }
    val p12 = Path().apply { moveTo(0.249f * s, 0.042f * s); cubicTo(0.249f * s, 0.052f * s, 0.241f * s, 0.06f * s, 0.231f * s, 0.06f * s); cubicTo(0.22f * s, 0.06f * s, 0.212f * s, 0.052f * s, 0.212f * s, 0.042f * s); lineTo(0.212f * s, -0.041f * s); cubicTo(0.212f * s, -0.052f * s, 0.22f * s, -0.06f * s, 0.231f * s, -0.06f * s); cubicTo(0.241f * s, -0.06f * s, 0.249f * s, -0.052f * s, 0.249f * s, -0.041f * s); close() }
    val p13 = Path().apply { moveTo(-0.189f * s, -0.235f * s); cubicTo(-0.156f * s, -0.203f * s, -0.145f * s, -0.162f * s, -0.163f * s, -0.144f * s); cubicTo(-0.181f * s, -0.126f * s, -0.221f * s, -0.138f * s, -0.254f * s, -0.17f * s); cubicTo(-0.286f * s, -0.203f * s, -0.298f * s, -0.243f * s, -0.28f * s, -0.261f * s); cubicTo(-0.262f * s, -0.28f * s, -0.221f * s, -0.268f * s, -0.189f * s, -0.235f * s); close() }
    drawPath(p0, sil ?: Color(0xFFBA9252))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFE9B666))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFFF86B3))
        }
        drawPath(p4, Color(0xFFFFFFFF))
        drawPath(p5, Color(0xFFFFD23F))
        drawPath(p6, Color(0xFF4FC3F7))
        drawPath(p7, Color(0xFFFFFFFF))
        drawPath(p8, Color(0xFF7B4FD0))
        drawPath(p9, Color(0xFFFFD23F))
        drawPath(p10, Color(0xFF4FC3F7))
        drawPath(p11, Color(0xFF7B4FD0))
        drawPath(p12, Color(0xFFFFD23F))
        drawPath(p13, Color(0x8CFFFFFF))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artMacaron(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.305f * s, -0.044f * s); lineTo(0.305f * s, -0.044f * s); cubicTo(0.359f * s, -0.044f * s, 0.403f * s, 0.0f * s, 0.403f * s, 0.054f * s); lineTo(0.403f * s, 0.064f * s); cubicTo(0.403f * s, 0.118f * s, 0.359f * s, 0.162f * s, 0.305f * s, 0.162f * s); lineTo(-0.305f * s, 0.162f * s); cubicTo(-0.359f * s, 0.162f * s, -0.403f * s, 0.118f * s, -0.403f * s, 0.064f * s); lineTo(-0.403f * s, 0.054f * s); cubicTo(-0.403f * s, 0.0f * s, -0.359f * s, -0.044f * s, -0.305f * s, -0.044f * s); close() }
    val p1 = Path().apply { moveTo(-0.432f * s, 0.162f * s); quadraticTo(-0.37f * s, 0.084f * s, -0.309f * s, 0.162f * s); quadraticTo(-0.247f * s, 0.084f * s, -0.185f * s, 0.162f * s); quadraticTo(-0.123f * s, 0.084f * s, -0.062f * s, 0.162f * s); quadraticTo(0.0f * s, 0.084f * s, 0.062f * s, 0.162f * s); quadraticTo(0.124f * s, 0.084f * s, 0.185f * s, 0.162f * s); quadraticTo(0.247f * s, 0.084f * s, 0.309f * s, 0.162f * s); quadraticTo(0.371f * s, 0.084f * s, 0.432f * s, 0.162f * s); cubicTo(0.432f * s, 0.339f * s, 0.275f * s, 0.408f * s, 0.0f * s, 0.408f * s); cubicTo(-0.275f * s, 0.408f * s, -0.432f * s, 0.339f * s, -0.432f * s, 0.162f * s); close() }
    val p2 = Path().apply { moveTo(-0.452f * s, 0.133f * s); quadraticTo(-0.39f * s, 0.054f * s, -0.328f * s, 0.133f * s); quadraticTo(-0.267f * s, 0.054f * s, -0.205f * s, 0.133f * s); quadraticTo(-0.143f * s, 0.054f * s, -0.081f * s, 0.133f * s); quadraticTo(-0.02f * s, 0.054f * s, 0.042f * s, 0.133f * s); quadraticTo(0.104f * s, 0.054f * s, 0.166f * s, 0.133f * s); quadraticTo(0.227f * s, 0.054f * s, 0.289f * s, 0.133f * s); quadraticTo(0.351f * s, 0.054f * s, 0.413f * s, 0.133f * s); cubicTo(0.413f * s, 0.31f * s, 0.256f * s, 0.378f * s, -0.02f * s, 0.378f * s); cubicTo(-0.295f * s, 0.378f * s, -0.452f * s, 0.31f * s, -0.452f * s, 0.133f * s); close() }
    val p3 = Path().apply { moveTo(-0.432f * s, -0.054f * s); cubicTo(-0.432f * s, -0.29f * s, -0.255f * s, -0.408f * s, 0.0f * s, -0.408f * s); cubicTo(0.256f * s, -0.408f * s, 0.432f * s, -0.29f * s, 0.432f * s, -0.054f * s); quadraticTo(0.371f * s, 0.025f * s, 0.309f * s, -0.054f * s); quadraticTo(0.247f * s, 0.025f * s, 0.185f * s, -0.054f * s); quadraticTo(0.124f * s, 0.025f * s, 0.062f * s, -0.054f * s); quadraticTo(0.0f * s, 0.025f * s, -0.062f * s, -0.054f * s); quadraticTo(-0.123f * s, 0.025f * s, -0.185f * s, -0.054f * s); quadraticTo(-0.247f * s, 0.025f * s, -0.309f * s, -0.054f * s); quadraticTo(-0.37f * s, 0.025f * s, -0.432f * s, -0.054f * s); close() }
    val p4 = Path().apply { moveTo(-0.452f * s, -0.093f * s); cubicTo(-0.452f * s, -0.329f * s, -0.275f * s, -0.447f * s, -0.02f * s, -0.447f * s); cubicTo(0.236f * s, -0.447f * s, 0.413f * s, -0.329f * s, 0.413f * s, -0.093f * s); quadraticTo(0.351f * s, -0.015f * s, 0.289f * s, -0.093f * s); quadraticTo(0.227f * s, -0.015f * s, 0.166f * s, -0.093f * s); quadraticTo(0.104f * s, -0.015f * s, 0.042f * s, -0.093f * s); quadraticTo(-0.02f * s, -0.015f * s, -0.081f * s, -0.093f * s); quadraticTo(-0.143f * s, -0.015f * s, -0.205f * s, -0.093f * s); quadraticTo(-0.267f * s, -0.015f * s, -0.328f * s, -0.093f * s); quadraticTo(-0.39f * s, -0.015f * s, -0.452f * s, -0.093f * s); close() }
    val p5 = Path().apply { moveTo(-0.157f * s, -0.305f * s); cubicTo(-0.146f * s, -0.286f * s, -0.164f * s, -0.255f * s, -0.196f * s, -0.236f * s); cubicTo(-0.229f * s, -0.217f * s, -0.265f * s, -0.217f * s, -0.276f * s, -0.236f * s); cubicTo(-0.287f * s, -0.255f * s, -0.269f * s, -0.285f * s, -0.236f * s, -0.304f * s); cubicTo(-0.203f * s, -0.323f * s, -0.167f * s, -0.323f * s, -0.157f * s, -0.305f * s); close() }
    drawPath(p0, sil ?: Color(0xFFFFF1DC))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFFFF86B3))
        }
        clipPath(p1) {
            drawRoundRect(Color(0xFFFF6FA3), Offset(-0.491f * s, -0.034f * s), Size(0.983f * s, 0.295f * s), CornerRadius(0.0f * s))
        }
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFFF86B3))
        }
        clipPath(p3) {
            drawRoundRect(Color(0xFFFF6FA3), Offset(-0.491f * s, -0.133f * s), Size(0.983f * s, 0.197f * s), CornerRadius(0.0f * s))
        }
        drawPath(p5, Color(0x8CFFFFFF))
        drawOval(Color(0x8CFFFFFF), Offset(-0.334f * s, -0.172f * s), Size(0.039f * s, 0.039f * s))
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artBarbapapa(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, 0.175f * s); lineTo(0.0f * s, 0.389f * s) }
    val p1 = Path().apply { moveTo(-0.109f * s, -0.373f * s); cubicTo(-0.086f * s, -0.412f * s, -0.045f * s, -0.435f * s, 0.0f * s, -0.435f * s); cubicTo(0.045f * s, -0.435f * s, 0.086f * s, -0.412f * s, 0.11f * s, -0.373f * s); cubicTo(0.152f * s, -0.388f * s, 0.199f * s, -0.379f * s, 0.233f * s, -0.35f * s); cubicTo(0.268f * s, -0.321f * s, 0.284f * s, -0.277f * s, 0.277f * s, -0.232f * s); cubicTo(0.319f * s, -0.216f * s, 0.35f * s, -0.179f * s, 0.357f * s, -0.135f * s); cubicTo(0.365f * s, -0.091f * s, 0.349f * s, -0.046f * s, 0.315f * s, -0.017f * s); cubicTo(0.337f * s, 0.023f * s, 0.337f * s, 0.07f * s, 0.314f * s, 0.109f * s); cubicTo(0.292f * s, 0.148f * s, 0.251f * s, 0.172f * s, 0.206f * s, 0.173f * s); cubicTo(0.197f * s, 0.217f * s, 0.166f * s, 0.253f * s, 0.124f * s, 0.269f * s); cubicTo(0.082f * s, 0.284f * s, 0.035f * s, 0.276f * s, 0.0f * s, 0.248f * s); cubicTo(-0.035f * s, 0.276f * s, -0.082f * s, 0.284f * s, -0.124f * s, 0.269f * s); cubicTo(-0.166f * s, 0.253f * s, -0.197f * s, 0.217f * s, -0.206f * s, 0.173f * s); cubicTo(-0.251f * s, 0.172f * s, -0.292f * s, 0.148f * s, -0.314f * s, 0.109f * s); cubicTo(-0.337f * s, 0.07f * s, -0.337f * s, 0.023f * s, -0.315f * s, -0.017f * s); cubicTo(-0.349f * s, -0.046f * s, -0.365f * s, -0.091f * s, -0.357f * s, -0.135f * s); cubicTo(-0.349f * s, -0.179f * s, -0.319f * s, -0.216f * s, -0.277f * s, -0.232f * s); cubicTo(-0.284f * s, -0.277f * s, -0.268f * s, -0.321f * s, -0.233f * s, -0.35f * s); cubicTo(-0.199f * s, -0.379f * s, -0.152f * s, -0.388f * s, -0.109f * s, -0.373f * s); close() }
    val p2 = Path().apply { moveTo(-0.126f * s, -0.406f * s); cubicTo(-0.103f * s, -0.445f * s, -0.061f * s, -0.468f * s, -0.016f * s, -0.468f * s); cubicTo(0.028f * s, -0.468f * s, 0.07f * s, -0.445f * s, 0.093f * s, -0.406f * s); cubicTo(0.135f * s, -0.421f * s, 0.182f * s, -0.412f * s, 0.217f * s, -0.383f * s); cubicTo(0.251f * s, -0.354f * s, 0.268f * s, -0.31f * s, 0.261f * s, -0.265f * s); cubicTo(0.303f * s, -0.249f * s, 0.333f * s, -0.212f * s, 0.341f * s, -0.168f * s); cubicTo(0.349f * s, -0.124f * s, 0.333f * s, -0.079f * s, 0.299f * s, -0.05f * s); cubicTo(0.321f * s, -0.01f * s, 0.32f * s, 0.037f * s, 0.298f * s, 0.076f * s); cubicTo(0.275f * s, 0.115f * s, 0.234f * s, 0.139f * s, 0.189f * s, 0.14f * s); cubicTo(0.181f * s, 0.184f * s, 0.15f * s, 0.22f * s, 0.108f * s, 0.236f * s); cubicTo(0.066f * s, 0.251f * s, 0.018f * s, 0.243f * s, -0.016f * s, 0.215f * s); cubicTo(-0.051f * s, 0.243f * s, -0.098f * s, 0.251f * s, -0.141f * s, 0.236f * s); cubicTo(-0.183f * s, 0.22f * s, -0.214f * s, 0.184f * s, -0.222f * s, 0.14f * s); cubicTo(-0.267f * s, 0.139f * s, -0.308f * s, 0.115f * s, -0.331f * s, 0.076f * s); cubicTo(-0.353f * s, 0.037f * s, -0.354f * s, -0.01f * s, -0.332f * s, -0.05f * s); cubicTo(-0.366f * s, -0.079f * s, -0.382f * s, -0.124f * s, -0.374f * s, -0.168f * s); cubicTo(-0.366f * s, -0.212f * s, -0.336f * s, -0.249f * s, -0.294f * s, -0.265f * s); cubicTo(-0.301f * s, -0.31f * s, -0.284f * s, -0.354f * s, -0.25f * s, -0.383f * s); cubicTo(-0.215f * s, -0.412f * s, -0.168f * s, -0.421f * s, -0.126f * s, -0.406f * s); close() }
    val p3 = Path().apply { moveTo(-0.247f * s, -0.072f * s); quadraticTo(-0.148f * s, -0.237f * s, 0.017f * s, -0.155f * s); quadraticTo(0.099f * s, -0.105f * s, 0.033f * s, -0.039f * s) }
    val p4 = Path().apply { moveTo(-0.165f * s, 0.076f * s); quadraticTo(0.0f * s, 0.159f * s, 0.165f * s, 0.043f * s); quadraticTo(0.231f * s, -0.023f * s, 0.181f * s, -0.114f * s) }
    val p5 = Path().apply { moveTo(-0.174f * s, -0.249f * s); cubicTo(-0.163f * s, -0.234f * s, -0.173f * s, -0.209f * s, -0.195f * s, -0.194f * s); cubicTo(-0.218f * s, -0.178f * s, -0.244f * s, -0.177f * s, -0.255f * s, -0.192f * s); cubicTo(-0.265f * s, -0.207f * s, -0.256f * s, -0.232f * s, -0.233f * s, -0.248f * s); cubicTo(-0.211f * s, -0.263f * s, -0.184f * s, -0.264f * s, -0.174f * s, -0.249f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.147f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFFF3DFB6), style = Stroke(0.075f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFFFF86B3))
        }
    }
    clipPath(p1) {
        drawPath(p3, sil ?: Color(0x8CFFFFFF), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p4, sil ?: Color(0x8CFFFFFF), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    if (sil == null) {
        drawPath(p5, Color(0x99FFFFFF))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artSucette(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.125f * s, 0.158f * s); lineTo(-0.231f * s, 0.384f * s) }
    val p1 = Path().apply { moveTo(0.293f * s, 0.027f * s); cubicTo(0.218f * s, 0.189f * s, 0.025f * s, 0.259f * s, -0.137f * s, 0.183f * s); cubicTo(-0.299f * s, 0.108f * s, -0.369f * s, -0.085f * s, -0.293f * s, -0.247f * s); cubicTo(-0.218f * s, -0.409f * s, -0.025f * s, -0.479f * s, 0.137f * s, -0.403f * s); cubicTo(0.299f * s, -0.328f * s, 0.369f * s, -0.135f * s, 0.293f * s, 0.027f * s); close() }
    val p2 = Path().apply { moveTo(0.292f * s, -0.014f * s); cubicTo(0.217f * s, 0.147f * s, 0.024f * s, 0.218f * s, -0.138f * s, 0.142f * s); cubicTo(-0.3f * s, 0.066f * s, -0.37f * s, -0.126f * s, -0.294f * s, -0.288f * s); cubicTo(-0.219f * s, -0.45f * s, -0.026f * s, -0.52f * s, 0.136f * s, -0.444f * s); cubicTo(0.298f * s, -0.369f * s, 0.368f * s, -0.176f * s, 0.292f * s, -0.014f * s); close() }
    val p3 = Path().apply { moveTo(0.021f * s, -0.1f * s); lineTo(0.021f * s, -0.092f * s); lineTo(0.018f * s, -0.084f * s); lineTo(0.012f * s, -0.077f * s); lineTo(0.003f * s, -0.071f * s); lineTo(-0.008f * s, -0.067f * s); lineTo(-0.02f * s, -0.067f * s); lineTo(-0.034f * s, -0.071f * s); lineTo(-0.046f * s, -0.079f * s); lineTo(-0.056f * s, -0.09f * s); lineTo(-0.064f * s, -0.105f * s); lineTo(-0.067f * s, -0.123f * s); lineTo(-0.065f * s, -0.142f * s); lineTo(-0.057f * s, -0.16f * s); lineTo(-0.044f * s, -0.177f * s); lineTo(-0.027f * s, -0.19f * s); lineTo(-0.005f * s, -0.198f * s); lineTo(0.019f * s, -0.2f * s); lineTo(0.043f * s, -0.196f * s); lineTo(0.067f * s, -0.185f * s); lineTo(0.088f * s, -0.167f * s); lineTo(0.103f * s, -0.143f * s); lineTo(0.113f * s, -0.116f * s); lineTo(0.114f * s, -0.085f * s); lineTo(0.107f * s, -0.054f * s); lineTo(0.092f * s, -0.026f * s); lineTo(0.069f * s, -0.001f * s); lineTo(0.04f * s, 0.017f * s); lineTo(0.006f * s, 0.027f * s); lineTo(-0.031f * s, 0.028f * s); lineTo(-0.068f * s, 0.018f * s); lineTo(-0.102f * s, -0.001f * s); lineTo(-0.13f * s, -0.029f * s); lineTo(-0.151f * s, -0.064f * s); lineTo(-0.161f * s, -0.105f * s); lineTo(-0.161f * s, -0.148f * s); lineTo(-0.149f * s, -0.191f * s); lineTo(-0.126f * s, -0.23f * s); lineTo(-0.092f * s, -0.262f * s); lineTo(-0.051f * s, -0.285f * s); lineTo(-0.004f * s, -0.296f * s); lineTo(0.046f * s, -0.294f * s); lineTo(0.094f * s, -0.28f * s); lineTo(0.138f * s, -0.252f * s); lineTo(0.174f * s, -0.213f * s); lineTo(0.199f * s, -0.166f * s); lineTo(0.21f * s, -0.112f * s); lineTo(0.208f * s, -0.056f * s); lineTo(0.19f * s, -0.002f * s); lineTo(0.158f * s, 0.047f * s); lineTo(0.114f * s, 0.086f * s); lineTo(0.06f * s, 0.113f * s); lineTo(0.0f * s, 0.125f * s); lineTo(-0.062f * s, 0.121f * s); lineTo(-0.122f * s, 0.1f * s); lineTo(-0.176f * s, 0.064f * s); lineTo(-0.218f * s, 0.014f * s); lineTo(-0.247f * s, -0.046f * s); lineTo(-0.259f * s, -0.113f * s); lineTo(-0.253f * s, -0.181f * s); lineTo(-0.23f * s, -0.247f * s); lineTo(-0.189f * s, -0.305f * s); lineTo(-0.134f * s, -0.351f * s) }
    val p4 = Path().apply { moveTo(-0.086f * s, -0.339f * s); cubicTo(-0.083f * s, -0.327f * s, -0.096f * s, -0.313f * s, -0.116f * s, -0.308f * s); cubicTo(-0.136f * s, -0.302f * s, -0.154f * s, -0.308f * s, -0.157f * s, -0.32f * s); cubicTo(-0.161f * s, -0.333f * s, -0.147f * s, -0.347f * s, -0.128f * s, -0.352f * s); cubicTo(-0.108f * s, -0.358f * s, -0.089f * s, -0.352f * s, -0.086f * s, -0.339f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.142f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFFF3DFB6), style = Stroke(0.07f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p1) {
            drawPath(p2, Color(0xFFFF86B3))
        }
    }
    clipPath(p1) {
        drawPath(p3, sil ?: Color(0xFFFFFFFF), style = Stroke(0.055f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    if (sil == null) {
        drawPath(p4, Color(0xB3FFFFFF))
    }
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artFlamant(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, 0.145f * s); lineTo(0.0f * s, 0.394f * s) }
    val p1 = Path().apply { moveTo(0.083f * s, 0.385f * s); cubicTo(0.097f * s, 0.385f * s, 0.108f * s, 0.396f * s, 0.108f * s, 0.41f * s); cubicTo(0.108f * s, 0.424f * s, 0.097f * s, 0.435f * s, 0.083f * s, 0.435f * s); lineTo(-0.034f * s, 0.435f * s); cubicTo(-0.047f * s, 0.435f * s, -0.058f * s, 0.424f * s, -0.058f * s, 0.41f * s); cubicTo(-0.058f * s, 0.396f * s, -0.047f * s, 0.385f * s, -0.034f * s, 0.385f * s); close() }
    val p2 = Path().apply { moveTo(0.124f * s, -0.055f * s); cubicTo(0.315f * s, -0.105f * s, 0.282f * s, -0.254f * s, 0.116f * s, -0.254f * s); cubicTo(-0.017f * s, -0.254f * s, 0.016f * s, -0.37f * s, 0.166f * s, -0.37f * s) }
    val p3 = Path().apply { moveTo(-0.374f * s, -0.129f * s); cubicTo(-0.274f * s, -0.096f * s, -0.208f * s, -0.196f * s, -0.034f * s, -0.196f * s); cubicTo(0.166f * s, -0.196f * s, 0.265f * s, -0.088f * s, 0.249f * s, 0.028f * s); cubicTo(0.232f * s, 0.145f * s, 0.099f * s, 0.186f * s, -0.05f * s, 0.178f * s); cubicTo(-0.2f * s, 0.169f * s, -0.299f * s, 0.037f * s, -0.374f * s, -0.129f * s); close() }
    val p4 = Path().apply { moveTo(-0.391f * s, -0.163f * s); cubicTo(-0.291f * s, -0.129f * s, -0.224f * s, -0.229f * s, -0.05f * s, -0.229f * s); cubicTo(0.149f * s, -0.229f * s, 0.249f * s, -0.121f * s, 0.232f * s, -0.005f * s); cubicTo(0.216f * s, 0.111f * s, 0.083f * s, 0.153f * s, -0.067f * s, 0.145f * s); cubicTo(-0.216f * s, 0.136f * s, -0.316f * s, 0.003f * s, -0.391f * s, -0.163f * s); close() }
    val p5 = Path().apply { moveTo(-0.001f * s, -0.141f * s); cubicTo(0.001f * s, -0.13f * s, -0.027f * s, -0.115f * s, -0.063f * s, -0.109f * s); cubicTo(-0.099f * s, -0.103f * s, -0.13f * s, -0.107f * s, -0.132f * s, -0.118f * s); cubicTo(-0.134f * s, -0.129f * s, -0.106f * s, -0.144f * s, -0.07f * s, -0.15f * s); cubicTo(-0.034f * s, -0.156f * s, -0.003f * s, -0.152f * s, -0.001f * s, -0.141f * s); close() }
    val p6 = Path().apply { moveTo(-0.233f * s, -0.038f * s); quadraticTo(-0.093f * s, 0.122f * s, 0.116f * s, 0.078f * s); quadraticTo(-0.043f * s, -0.027f * s, -0.233f * s, -0.038f * s); close() }
    val p7 = Path().apply { moveTo(0.247f * s, -0.37f * s); cubicTo(0.247f * s, -0.334f * s, 0.218f * s, -0.305f * s, 0.182f * s, -0.305f * s); cubicTo(0.147f * s, -0.305f * s, 0.118f * s, -0.334f * s, 0.118f * s, -0.37f * s); cubicTo(0.118f * s, -0.406f * s, 0.147f * s, -0.435f * s, 0.182f * s, -0.435f * s); cubicTo(0.218f * s, -0.435f * s, 0.247f * s, -0.406f * s, 0.247f * s, -0.37f * s); close() }
    val p8 = Path().apply { moveTo(0.216f * s, -0.412f * s); cubicTo(0.315f * s, -0.42f * s, 0.382f * s, -0.362f * s, 0.373f * s, -0.287f * s); cubicTo(0.324f * s, -0.329f * s, 0.265f * s, -0.329f * s, 0.224f * s, -0.32f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.127f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFFF2639B), style = Stroke(0.055f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFF2639B))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.147f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFFF86B3), style = Stroke(0.075f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFFF86B3))
        }
        drawPath(p5, Color(0x8CFFFFFF))
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p6, Color(0xFFF2639B))
    }
    drawPath(p7, sil ?: Color(0xFFFF86B3))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFFFE3EE))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        clipPath(p8) {
            drawRoundRect(Color(0xFF2B2D42), Offset(0.315f * s, -0.437f * s), Size(0.166f * s, 0.166f * s), CornerRadius(0.0f * s))
        }
        drawCircle(Color(0xFF2B2D42), 0.025f * s, Offset(0.182f * s, -0.387f * s))
        drawCircle(Color(0xFFFFFFFF), 0.007f * s, Offset(0.175f * s, -0.396f * s))
    }
}

private fun DrawScope.artCrevette(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.07f * s, -0.123f * s); lineTo(-0.105f * s, -0.024f * s) }
    val p1 = Path().apply { moveTo(-0.158f * s, -0.139f * s); lineTo(-0.135f * s, -0.037f * s) }
    val p2 = Path().apply { moveTo(-0.248f * s, -0.103f * s); lineTo(-0.174f * s, -0.029f * s) }
    val p3 = Path().apply { moveTo(0.159f * s, -0.105f * s); quadraticTo(0.284f * s, -0.203f * s, 0.146f * s, -0.366f * s) }
    val p4 = Path().apply { moveTo(0.159f * s, -0.105f * s); quadraticTo(0.462f * s, -0.025f * s, 0.397f * s, -0.203f * s) }
    val p5 = Path().apply { moveTo(-0.205f * s, 0.252f * s); quadraticTo(-0.095f * s, 0.316f * s, -0.052f * s, 0.197f * s); quadraticTo(-0.162f * s, 0.133f * s, -0.205f * s, 0.252f * s); close() }
    val p6 = Path().apply { moveTo(-0.205f * s, 0.252f * s); quadraticTo(-0.248f * s, 0.371f * s, -0.124f * s, 0.392f * s); quadraticTo(-0.08f * s, 0.274f * s, -0.205f * s, 0.252f * s); close() }
    val p7 = Path().apply { moveTo(-0.205f * s, 0.252f * s); quadraticTo(-0.139f * s, 0.379f * s, -0.007f * s, 0.324f * s); quadraticTo(-0.072f * s, 0.197f * s, -0.205f * s, 0.252f * s); close() }
    val p8 = Path().apply { moveTo(0.229f * s, -0.104f * s); cubicTo(0.256f * s, -0.139f * s, 0.18f * s, -0.192f * s, 0.145f * s, -0.225f * s); cubicTo(0.111f * s, -0.258f * s, 0.067f * s, -0.286f * s, 0.024f * s, -0.303f * s); cubicTo(-0.02f * s, -0.319f * s, -0.07f * s, -0.327f * s, -0.115f * s, -0.325f * s); cubicTo(-0.161f * s, -0.323f * s, -0.209f * s, -0.311f * s, -0.249f * s, -0.292f * s); cubicTo(-0.289f * s, -0.273f * s, -0.327f * s, -0.243f * s, -0.356f * s, -0.211f * s); cubicTo(-0.384f * s, -0.178f * s, -0.407f * s, -0.137f * s, -0.42f * s, -0.097f * s); cubicTo(-0.433f * s, -0.057f * s, -0.438f * s, -0.012f * s, -0.434f * s, 0.029f * s); cubicTo(-0.43f * s, 0.07f * s, -0.417f * s, 0.112f * s, -0.398f * s, 0.147f * s); cubicTo(-0.379f * s, 0.181f * s, -0.35f * s, 0.214f * s, -0.32f * s, 0.236f * s); cubicTo(-0.29f * s, 0.259f * s, -0.237f * s, 0.285f * s, -0.216f * s, 0.282f * s); cubicTo(-0.195f * s, 0.28f * s, -0.187f * s, 0.241f * s, -0.193f * s, 0.222f * s); cubicTo(-0.2f * s, 0.202f * s, -0.239f * s, 0.187f * s, -0.255f * s, 0.166f * s); cubicTo(-0.271f * s, 0.146f * s, -0.283f * s, 0.122f * s, -0.289f * s, 0.098f * s); cubicTo(-0.296f * s, 0.075f * s, -0.298f * s, 0.049f * s, -0.295f * s, 0.027f * s); cubicTo(-0.292f * s, 0.004f * s, -0.284f * s, -0.019f * s, -0.273f * s, -0.038f * s); cubicTo(-0.263f * s, -0.057f * s, -0.247f * s, -0.074f * s, -0.231f * s, -0.086f * s); cubicTo(-0.215f * s, -0.098f * s, -0.195f * s, -0.107f * s, -0.176f * s, -0.112f * s); cubicTo(-0.158f * s, -0.117f * s, -0.137f * s, -0.117f * s, -0.119f * s, -0.114f * s); cubicTo(-0.101f * s, -0.111f * s, -0.083f * s, -0.104f * s, -0.069f * s, -0.095f * s); cubicTo(-0.054f * s, -0.086f * s, -0.042f * s, -0.072f * s, -0.033f * s, -0.059f * s); cubicTo(-0.024f * s, -0.046f * s, -0.058f * s, -0.008f * s, -0.015f * s, -0.015f * s); cubicTo(0.029f * s, -0.023f * s, 0.202f * s, -0.069f * s, 0.229f * s, -0.104f * s); close() }
    val p9 = Path().apply { moveTo(0.213f * s, -0.136f * s); cubicTo(0.239f * s, -0.171f * s, 0.163f * s, -0.225f * s, 0.129f * s, -0.258f * s); cubicTo(0.095f * s, -0.291f * s, 0.051f * s, -0.318f * s, 0.008f * s, -0.335f * s); cubicTo(-0.036f * s, -0.352f * s, -0.086f * s, -0.359f * s, -0.132f * s, -0.358f * s); cubicTo(-0.177f * s, -0.356f * s, -0.225f * s, -0.343f * s, -0.265f * s, -0.324f * s); cubicTo(-0.305f * s, -0.305f * s, -0.343f * s, -0.276f * s, -0.372f * s, -0.243f * s); cubicTo(-0.401f * s, -0.211f * s, -0.423f * s, -0.17f * s, -0.437f * s, -0.13f * s); cubicTo(-0.45f * s, -0.09f * s, -0.454f * s, -0.044f * s, -0.45f * s, -0.003f * s); cubicTo(-0.447f * s, 0.037f * s, -0.433f * s, 0.08f * s, -0.414f * s, 0.114f * s); cubicTo(-0.395f * s, 0.149f * s, -0.366f * s, 0.181f * s, -0.336f * s, 0.204f * s); cubicTo(-0.306f * s, 0.227f * s, -0.253f * s, 0.253f * s, -0.232f * s, 0.25f * s); cubicTo(-0.211f * s, 0.248f * s, -0.203f * s, 0.208f * s, -0.21f * s, 0.189f * s); cubicTo(-0.216f * s, 0.17f * s, -0.255f * s, 0.155f * s, -0.271f * s, 0.134f * s); cubicTo(-0.287f * s, 0.113f * s, -0.299f * s, 0.089f * s, -0.306f * s, 0.066f * s); cubicTo(-0.312f * s, 0.043f * s, -0.314f * s, 0.017f * s, -0.311f * s, -0.006f * s); cubicTo(-0.309f * s, -0.029f * s, -0.3f * s, -0.052f * s, -0.29f * s, -0.07f * s); cubicTo(-0.279f * s, -0.089f * s, -0.263f * s, -0.106f * s, -0.247f * s, -0.118f * s); cubicTo(-0.231f * s, -0.131f * s, -0.211f * s, -0.14f * s, -0.192f * s, -0.144f * s); cubicTo(-0.174f * s, -0.149f * s, -0.153f * s, -0.149f * s, -0.135f * s, -0.147f * s); cubicTo(-0.117f * s, -0.144f * s, -0.099f * s, -0.136f * s, -0.085f * s, -0.127f * s); cubicTo(-0.071f * s, -0.118f * s, -0.058f * s, -0.105f * s, -0.049f * s, -0.092f * s); cubicTo(-0.04f * s, -0.078f * s, -0.075f * s, -0.04f * s, -0.031f * s, -0.048f * s); cubicTo(0.013f * s, -0.055f * s, 0.186f * s, -0.101f * s, 0.213f * s, -0.136f * s); close() }
    val p10 = Path().apply { moveTo(-0.123f * s, -0.189f * s); cubicTo(-0.125f * s, -0.178f * s, -0.162f * s, -0.176f * s, -0.206f * s, -0.184f * s); cubicTo(-0.25f * s, -0.191f * s, -0.284f * s, -0.207f * s, -0.282f * s, -0.218f * s); cubicTo(-0.28f * s, -0.229f * s, -0.243f * s, -0.231f * s, -0.199f * s, -0.223f * s); cubicTo(-0.155f * s, -0.216f * s, -0.121f * s, -0.2f * s, -0.123f * s, -0.189f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.107f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.107f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.107f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFFF2639B), style = Stroke(0.035f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFF2639B), style = Stroke(0.035f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFF2639B), style = Stroke(0.035f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.102f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.102f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFF2639B), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFF2639B), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFFF9EC5))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFFF9EC5))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFFF9EC5))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p8) {
            drawPath(p9, Color(0xFFFF86B3))
        }
        clipPath(p8) {
            drawLine(Color(0xBFD9487F), Offset(-0.083f * s, -0.064f * s), Offset(0.038f * s, -0.334f * s), 0.024f * s, StrokeCap.Round)
            drawLine(Color(0xBFD9487F), Offset(-0.146f * s, -0.086f * s), Offset(-0.204f * s, -0.341f * s), 0.024f * s, StrokeCap.Round)
            drawLine(Color(0xBFD9487F), Offset(-0.221f * s, -0.061f * s), Offset(-0.392f * s, -0.207f * s), 0.024f * s, StrokeCap.Round)
            drawLine(Color(0xBFD9487F), Offset(-0.271f * s, 0.014f * s), Offset(-0.457f * s, 0.002f * s), 0.024f * s, StrokeCap.Round)
            drawLine(Color(0xBFD9487F), Offset(-0.265f * s, 0.118f * s), Offset(-0.385f * s, 0.196f * s), 0.024f * s, StrokeCap.Round)
        }
        drawPath(p10, Color(0x99FFFFFF))
    }
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.041f * s, Offset(0.092f * s, -0.176f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.041f * s, Offset(0.092f * s, -0.176f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.024f * s, Offset(0.104f * s, -0.167f * s))
        drawCircle(Color(0xFFFFFFFF), 0.008f * s, Offset(0.095f * s, -0.178f * s))
    }
}

private fun DrawScope.artFleur(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, -0.066f * s); quadraticTo(0.016f * s, 0.174f * s, 0.0f * s, 0.399f * s) }
    val p1 = Path().apply { moveTo(0.016f * s, 0.255f * s); quadraticTo(0.207f * s, 0.294f * s, 0.273f * s, 0.11f * s); quadraticTo(0.082f * s, 0.07f * s, 0.016f * s, 0.255f * s); close() }
    val p2 = Path().apply { moveTo(0.0f * s, 0.319f * s); quadraticTo(-0.062f * s, 0.162f * s, -0.225f * s, 0.206f * s); quadraticTo(-0.163f * s, 0.363f * s, 0.0f * s, 0.319f * s); close() }
    val p3 = Path().apply { moveTo(0.112f * s, -0.283f * s); cubicTo(0.112f * s, -0.199f * s, 0.062f * s, -0.131f * s, 0.0f * s, -0.131f * s); cubicTo(-0.062f * s, -0.131f * s, -0.112f * s, -0.199f * s, -0.112f * s, -0.283f * s); cubicTo(-0.112f * s, -0.367f * s, -0.062f * s, -0.436f * s, 0.0f * s, -0.436f * s); cubicTo(0.062f * s, -0.436f * s, 0.112f * s, -0.367f * s, 0.112f * s, -0.283f * s); close() }
    val p4 = Path().apply { moveTo(0.1f * s, -0.307f * s); cubicTo(0.1f * s, -0.223f * s, 0.05f * s, -0.155f * s, -0.012f * s, -0.155f * s); cubicTo(-0.074f * s, -0.155f * s, -0.124f * s, -0.223f * s, -0.124f * s, -0.307f * s); cubicTo(-0.124f * s, -0.391f * s, -0.074f * s, -0.46f * s, -0.012f * s, -0.46f * s); cubicTo(0.05f * s, -0.46f * s, 0.1f * s, -0.391f * s, 0.1f * s, -0.307f * s); close() }
    val p5 = Path().apply { moveTo(0.187f * s, -0.065f * s); cubicTo(0.107f * s, -0.039f * s, 0.027f * s, -0.066f * s, 0.008f * s, -0.125f * s); cubicTo(-0.012f * s, -0.184f * s, 0.038f * s, -0.253f * s, 0.118f * s, -0.279f * s); cubicTo(0.198f * s, -0.305f * s, 0.279f * s, -0.278f * s, 0.298f * s, -0.219f * s); cubicTo(0.317f * s, -0.16f * s, 0.268f * s, -0.091f * s, 0.187f * s, -0.065f * s); close() }
    val p6 = Path().apply { moveTo(0.175f * s, -0.089f * s); cubicTo(0.095f * s, -0.063f * s, 0.015f * s, -0.09f * s, -0.004f * s, -0.149f * s); cubicTo(-0.024f * s, -0.208f * s, 0.026f * s, -0.277f * s, 0.106f * s, -0.303f * s); cubicTo(0.186f * s, -0.329f * s, 0.266f * s, -0.302f * s, 0.286f * s, -0.243f * s); cubicTo(0.305f * s, -0.184f * s, 0.255f * s, -0.115f * s, 0.175f * s, -0.089f * s); close() }
    val p7 = Path().apply { moveTo(0.004f * s, 0.073f * s); cubicTo(-0.046f * s, 0.005f * s, -0.045f * s, -0.08f * s, 0.005f * s, -0.116f * s); cubicTo(0.055f * s, -0.153f * s, 0.136f * s, -0.127f * s, 0.185f * s, -0.059f * s); cubicTo(0.235f * s, 0.009f * s, 0.234f * s, 0.094f * s, 0.184f * s, 0.131f * s); cubicTo(0.134f * s, 0.167f * s, 0.053f * s, 0.141f * s, 0.004f * s, 0.073f * s); close() }
    val p8 = Path().apply { moveTo(-0.009f * s, 0.049f * s); cubicTo(-0.058f * s, -0.019f * s, -0.057f * s, -0.104f * s, -0.007f * s, -0.14f * s); cubicTo(0.043f * s, -0.177f * s, 0.124f * s, -0.151f * s, 0.173f * s, -0.083f * s); cubicTo(0.223f * s, -0.015f * s, 0.222f * s, 0.07f * s, 0.172f * s, 0.107f * s); cubicTo(0.122f * s, 0.143f * s, 0.041f * s, 0.117f * s, -0.009f * s, 0.049f * s); close() }
    val p9 = Path().apply { moveTo(-0.185f * s, -0.059f * s); cubicTo(-0.136f * s, -0.127f * s, -0.055f * s, -0.153f * s, -0.005f * s, -0.116f * s); cubicTo(0.046f * s, -0.08f * s, 0.046f * s, 0.005f * s, -0.003f * s, 0.073f * s); cubicTo(-0.053f * s, 0.141f * s, -0.134f * s, 0.167f * s, -0.184f * s, 0.131f * s); cubicTo(-0.234f * s, 0.094f * s, -0.235f * s, 0.009f * s, -0.185f * s, -0.059f * s); close() }
    val p10 = Path().apply { moveTo(-0.197f * s, -0.083f * s); cubicTo(-0.148f * s, -0.151f * s, -0.067f * s, -0.177f * s, -0.017f * s, -0.14f * s); cubicTo(0.034f * s, -0.104f * s, 0.034f * s, -0.019f * s, -0.015f * s, 0.049f * s); cubicTo(-0.065f * s, 0.117f * s, -0.146f * s, 0.143f * s, -0.196f * s, 0.107f * s); cubicTo(-0.246f * s, 0.07f * s, -0.247f * s, -0.015f * s, -0.197f * s, -0.083f * s); close() }
    val p11 = Path().apply { moveTo(-0.118f * s, -0.279f * s); cubicTo(-0.038f * s, -0.253f * s, 0.012f * s, -0.184f * s, -0.008f * s, -0.125f * s); cubicTo(-0.027f * s, -0.066f * s, -0.107f * s, -0.039f * s, -0.187f * s, -0.065f * s); cubicTo(-0.267f * s, -0.091f * s, -0.317f * s, -0.16f * s, -0.298f * s, -0.219f * s); cubicTo(-0.278f * s, -0.278f * s, -0.198f * s, -0.305f * s, -0.118f * s, -0.279f * s); close() }
    val p12 = Path().apply { moveTo(-0.13f * s, -0.303f * s); cubicTo(-0.05f * s, -0.277f * s, 0.0f * s, -0.208f * s, -0.02f * s, -0.149f * s); cubicTo(-0.039f * s, -0.09f * s, -0.119f * s, -0.063f * s, -0.199f * s, -0.089f * s); cubicTo(-0.279f * s, -0.115f * s, -0.329f * s, -0.184f * s, -0.31f * s, -0.243f * s); cubicTo(-0.29f * s, -0.302f * s, -0.21f * s, -0.329f * s, -0.13f * s, -0.303f * s); close() }
    val p13 = Path().apply { moveTo(0.1f * s, -0.123f * s); cubicTo(0.1f * s, -0.067f * s, 0.055f * s, -0.022f * s, 0.0f * s, -0.022f * s); cubicTo(-0.055f * s, -0.022f * s, -0.1f * s, -0.067f * s, -0.1f * s, -0.123f * s); cubicTo(-0.1f * s, -0.178f * s, -0.055f * s, -0.223f * s, 0.0f * s, -0.223f * s); cubicTo(0.055f * s, -0.223f * s, 0.1f * s, -0.178f * s, 0.1f * s, -0.123f * s); close() }
    val p14 = Path().apply { moveTo(0.088f * s, -0.147f * s); cubicTo(0.088f * s, -0.091f * s, 0.043f * s, -0.046f * s, -0.012f * s, -0.046f * s); cubicTo(-0.067f * s, -0.046f * s, -0.112f * s, -0.091f * s, -0.112f * s, -0.147f * s); cubicTo(-0.112f * s, -0.202f * s, -0.067f * s, -0.247f * s, -0.012f * s, -0.247f * s); cubicTo(0.043f * s, -0.247f * s, 0.088f * s, -0.202f * s, 0.088f * s, -0.147f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.127f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF4C9A3A), style = Stroke(0.055f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF3FAE49))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF52B65B))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFFF86B3))
        }
        drawOval(Color(0x99FFFFFF), Offset(-0.056f * s, -0.387f * s), Size(0.032f * s, 0.08f * s))
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFFFF86B3))
        }
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p7) {
            drawPath(p8, Color(0xFFFF86B3))
        }
    }
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p9) {
            drawPath(p10, Color(0xFFFF86B3))
        }
    }
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p11) {
            drawPath(p12, Color(0xFFFF86B3))
        }
    }
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p13, sil ?: Color(0xFFCCA832))
    if (sil == null) {
        clipPath(p13) {
            drawPath(p14, Color(0xFFFFD23F))
        }
        drawOval(Color(0x8CFFFFFF), Offset(-0.056f * s, -0.175f * s), Size(0.04f * s, 0.04f * s))
    }
    drawPath(p13, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCoquillage(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.0f * s, 0.397f * s); lineTo(-0.389f * s, -0.094f * s); quadraticTo(-0.411f * s, -0.203f * s, -0.316f * s, -0.261f * s); quadraticTo(-0.288f * s, -0.368f * s, -0.177f * s, -0.378f * s); quadraticTo(-0.103f * s, -0.461f * s, 0.0f * s, -0.42f * s); quadraticTo(0.103f * s, -0.461f * s, 0.177f * s, -0.378f * s); quadraticTo(0.288f * s, -0.368f * s, 0.316f * s, -0.261f * s); quadraticTo(0.411f * s, -0.203f * s, 0.389f * s, -0.094f * s); close() }
    val p1 = Path().apply { moveTo(-0.018f * s, 0.369f * s); lineTo(-0.407f * s, -0.121f * s); quadraticTo(-0.43f * s, -0.23f * s, -0.335f * s, -0.288f * s); quadraticTo(-0.306f * s, -0.396f * s, -0.195f * s, -0.405f * s); quadraticTo(-0.122f * s, -0.489f * s, -0.018f * s, -0.447f * s); quadraticTo(0.085f * s, -0.489f * s, 0.159f * s, -0.405f * s); quadraticTo(0.27f * s, -0.396f * s, 0.298f * s, -0.288f * s); quadraticTo(0.393f * s, -0.23f * s, 0.37f * s, -0.121f * s); close() }
    val p2 = Path().apply { moveTo(0.0f * s, 0.397f * s); quadraticTo(0.0f * s, 0.397f * s, 0.0f * s, 0.397f * s); lineTo(-0.316f * s, -0.261f * s); quadraticTo(-0.316f * s, -0.261f * s, -0.316f * s, -0.261f * s); lineTo(-0.324f * s, -0.412f * s); quadraticTo(-0.324f * s, -0.412f * s, -0.324f * s, -0.412f * s); lineTo(-0.177f * s, -0.378f * s); quadraticTo(-0.177f * s, -0.378f * s, -0.177f * s, -0.378f * s); close() }
    val p3 = Path().apply { moveTo(0.0f * s, 0.397f * s); quadraticTo(0.0f * s, 0.397f * s, 0.0f * s, 0.397f * s); lineTo(0.0f * s, -0.42f * s); quadraticTo(0.0f * s, -0.42f * s, 0.0f * s, -0.42f * s); lineTo(0.116f * s, -0.516f * s); quadraticTo(0.116f * s, -0.516f * s, 0.116f * s, -0.516f * s); lineTo(0.177f * s, -0.378f * s); quadraticTo(0.177f * s, -0.378f * s, 0.177f * s, -0.378f * s); close() }
    val p4 = Path().apply { moveTo(0.0f * s, 0.397f * s); quadraticTo(0.0f * s, 0.397f * s, 0.0f * s, 0.397f * s); lineTo(0.316f * s, -0.261f * s); quadraticTo(0.316f * s, -0.261f * s, 0.316f * s, -0.261f * s); lineTo(0.463f * s, -0.225f * s); quadraticTo(0.463f * s, -0.225f * s, 0.463f * s, -0.225f * s); lineTo(0.389f * s, -0.094f * s); quadraticTo(0.389f * s, -0.094f * s, 0.389f * s, -0.094f * s); close() }
    val p5 = Path().apply { moveTo(-0.144f * s, -0.204f * s); cubicTo(-0.123f * s, -0.169f * s, -0.125f * s, -0.13f * s, -0.147f * s, -0.118f * s); cubicTo(-0.169f * s, -0.105f * s, -0.203f * s, -0.123f * s, -0.223f * s, -0.158f * s); cubicTo(-0.243f * s, -0.193f * s, -0.242f * s, -0.232f * s, -0.22f * s, -0.245f * s); cubicTo(-0.198f * s, -0.257f * s, -0.164f * s, -0.239f * s, -0.144f * s, -0.204f * s); close() }
    val p6 = Path().apply { moveTo(-0.165f * s, 0.336f * s); quadraticTo(-0.174f * s, 0.305f * s, -0.142f * s, 0.305f * s); lineTo(0.142f * s, 0.305f * s); quadraticTo(0.174f * s, 0.305f * s, 0.166f * s, 0.336f * s); lineTo(0.146f * s, 0.403f * s); quadraticTo(0.138f * s, 0.434f * s, 0.106f * s, 0.434f * s); lineTo(-0.105f * s, 0.434f * s); quadraticTo(-0.138f * s, 0.434f * s, -0.146f * s, 0.403f * s); close() }
    drawPath(p0, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFF86B3))
        }
        clipPath(p0) {
            drawPath(p2, Color(0xFFFFAFCF))
            drawPath(p3, Color(0xFFFFAFCF))
            drawPath(p4, Color(0xFFFFAFCF))
            drawLine(Color(0xCCD9487F), Offset(0.0f * s, 0.397f * s), Offset(-0.389f * s, -0.094f * s), 0.025f * s, StrokeCap.Round)
            drawLine(Color(0xCCD9487F), Offset(0.0f * s, 0.397f * s), Offset(-0.316f * s, -0.261f * s), 0.025f * s, StrokeCap.Round)
            drawLine(Color(0xCCD9487F), Offset(0.0f * s, 0.397f * s), Offset(-0.177f * s, -0.378f * s), 0.025f * s, StrokeCap.Round)
            drawLine(Color(0xCCD9487F), Offset(0.0f * s, 0.397f * s), Offset(0.0f * s, -0.42f * s), 0.025f * s, StrokeCap.Round)
            drawLine(Color(0xCCD9487F), Offset(0.0f * s, 0.397f * s), Offset(0.177f * s, -0.378f * s), 0.025f * s, StrokeCap.Round)
            drawLine(Color(0xCCD9487F), Offset(0.0f * s, 0.397f * s), Offset(0.316f * s, -0.261f * s), 0.025f * s, StrokeCap.Round)
            drawLine(Color(0xCCD9487F), Offset(0.0f * s, 0.397f * s), Offset(0.389f * s, -0.094f * s), 0.025f * s, StrokeCap.Round)
        }
        drawPath(p5, Color(0x80FFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFF2639B))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artCadeau(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.332f * s, 0.012f * s); lineTo(0.332f * s, 0.012f * s); cubicTo(0.352f * s, 0.012f * s, 0.369f * s, 0.028f * s, 0.369f * s, 0.049f * s); lineTo(0.369f * s, 0.38f * s); cubicTo(0.369f * s, 0.401f * s, 0.352f * s, 0.417f * s, 0.332f * s, 0.417f * s); lineTo(-0.332f * s, 0.417f * s); cubicTo(-0.352f * s, 0.417f * s, -0.369f * s, 0.401f * s, -0.369f * s, 0.38f * s); lineTo(-0.369f * s, 0.049f * s); cubicTo(-0.369f * s, 0.028f * s, -0.352f * s, 0.012f * s, -0.332f * s, 0.012f * s); close() }
    val p1 = Path().apply { moveTo(-0.35f * s, -0.016f * s); lineTo(0.314f * s, -0.016f * s); cubicTo(0.334f * s, -0.016f * s, 0.35f * s, 0.0f * s, 0.35f * s, 0.021f * s); lineTo(0.35f * s, 0.353f * s); cubicTo(0.35f * s, 0.373f * s, 0.334f * s, 0.39f * s, 0.314f * s, 0.39f * s); lineTo(-0.35f * s, 0.39f * s); cubicTo(-0.371f * s, 0.39f * s, -0.387f * s, 0.373f * s, -0.387f * s, 0.353f * s); lineTo(-0.387f * s, 0.021f * s); cubicTo(-0.387f * s, 0.0f * s, -0.371f * s, -0.016f * s, -0.35f * s, -0.016f * s); close() }
    val p2 = Path().apply { moveTo(-0.074f * s, 0.012f * s); lineTo(0.074f * s, 0.012f * s); cubicTo(0.074f * s, 0.012f * s, 0.074f * s, 0.012f * s, 0.074f * s, 0.012f * s); lineTo(0.074f * s, 0.417f * s); cubicTo(0.074f * s, 0.417f * s, 0.074f * s, 0.417f * s, 0.074f * s, 0.417f * s); lineTo(-0.074f * s, 0.417f * s); cubicTo(-0.074f * s, 0.417f * s, -0.074f * s, 0.417f * s, -0.074f * s, 0.417f * s); lineTo(-0.074f * s, 0.012f * s); cubicTo(-0.074f * s, 0.012f * s, -0.074f * s, 0.012f * s, -0.074f * s, 0.012f * s); close() }
    val p3 = Path().apply { moveTo(-0.396f * s, -0.2f * s); lineTo(0.397f * s, -0.2f * s); cubicTo(0.417f * s, -0.2f * s, 0.433f * s, -0.184f * s, 0.433f * s, -0.164f * s); lineTo(0.433f * s, -0.016f * s); cubicTo(0.433f * s, 0.004f * s, 0.417f * s, 0.021f * s, 0.397f * s, 0.021f * s); lineTo(-0.396f * s, 0.021f * s); cubicTo(-0.417f * s, 0.021f * s, -0.433f * s, 0.004f * s, -0.433f * s, -0.016f * s); lineTo(-0.433f * s, -0.164f * s); cubicTo(-0.433f * s, -0.184f * s, -0.417f * s, -0.2f * s, -0.396f * s, -0.2f * s); close() }
    val p4 = Path().apply { moveTo(-0.415f * s, -0.233f * s); lineTo(0.378f * s, -0.233f * s); cubicTo(0.399f * s, -0.233f * s, 0.415f * s, -0.216f * s, 0.415f * s, -0.196f * s); lineTo(0.415f * s, -0.048f * s); cubicTo(0.415f * s, -0.028f * s, 0.399f * s, -0.011f * s, 0.378f * s, -0.011f * s); lineTo(-0.415f * s, -0.011f * s); cubicTo(-0.435f * s, -0.011f * s, -0.452f * s, -0.028f * s, -0.452f * s, -0.048f * s); lineTo(-0.452f * s, -0.196f * s); cubicTo(-0.452f * s, -0.216f * s, -0.435f * s, -0.233f * s, -0.415f * s, -0.233f * s); close() }
    val p5 = Path().apply { moveTo(-0.074f * s, -0.2f * s); lineTo(0.074f * s, -0.2f * s); cubicTo(0.074f * s, -0.2f * s, 0.074f * s, -0.2f * s, 0.074f * s, -0.2f * s); lineTo(0.074f * s, 0.021f * s); cubicTo(0.074f * s, 0.021f * s, 0.074f * s, 0.021f * s, 0.074f * s, 0.021f * s); lineTo(-0.074f * s, 0.021f * s); cubicTo(-0.074f * s, 0.021f * s, -0.074f * s, 0.021f * s, -0.074f * s, 0.021f * s); lineTo(-0.074f * s, -0.2f * s); cubicTo(-0.074f * s, -0.2f * s, -0.074f * s, -0.2f * s, -0.074f * s, -0.2f * s); close() }
    val p6 = Path().apply { moveTo(0.0f * s, -0.2f * s); cubicTo(-0.092f * s, -0.422f * s, -0.35f * s, -0.496f * s, -0.341f * s, -0.33f * s); cubicTo(-0.332f * s, -0.2f * s, -0.129f * s, -0.2f * s, 0.0f * s, -0.2f * s); close() }
    val p7 = Path().apply { moveTo(0.0f * s, -0.2f * s); cubicTo(0.092f * s, -0.422f * s, 0.35f * s, -0.496f * s, 0.341f * s, -0.33f * s); cubicTo(0.332f * s, -0.2f * s, 0.129f * s, -0.2f * s, 0.0f * s, -0.2f * s); close() }
    val p8 = Path().apply { moveTo(0.069f * s, -0.2f * s); cubicTo(0.069f * s, -0.167f * s, 0.038f * s, -0.141f * s, 0.0f * s, -0.141f * s); cubicTo(-0.038f * s, -0.141f * s, -0.069f * s, -0.167f * s, -0.069f * s, -0.2f * s); cubicTo(-0.069f * s, -0.234f * s, -0.038f * s, -0.26f * s, 0.0f * s, -0.26f * s); cubicTo(0.038f * s, -0.26f * s, 0.069f * s, -0.234f * s, 0.069f * s, -0.2f * s); close() }
    drawPath(p0, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFF86B3))
        }
        drawOval(Color(0x73FFFFFF), Offset(-0.3f * s, 0.058f * s), Size(0.046f * s, 0.184f * s))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFFFD23F))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFCC5982))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFFF6FA3))
        }
        drawOval(Color(0x80FFFFFF), Offset(-0.387f * s, -0.15f * s), Size(0.148f * s, 0.046f * s))
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFFFD23F))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFFFD23F))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFFFD23F))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFFFD23F))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artRobe(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.12f * s, -0.424f * s); quadraticTo(0.0f * s, -0.313f * s, 0.12f * s, -0.424f * s); lineTo(0.295f * s, -0.35f * s); lineTo(0.323f * s, -0.212f * s); lineTo(0.157f * s, -0.175f * s); lineTo(0.138f * s, -0.018f * s); lineTo(0.433f * s, 0.369f * s); quadraticTo(0.347f * s, 0.48f * s, 0.26f * s, 0.369f * s); quadraticTo(0.173f * s, 0.48f * s, 0.087f * s, 0.369f * s); quadraticTo(0.0f * s, 0.48f * s, -0.087f * s, 0.369f * s); quadraticTo(-0.173f * s, 0.48f * s, -0.26f * s, 0.369f * s); quadraticTo(-0.347f * s, 0.48f * s, -0.433f * s, 0.369f * s); lineTo(-0.138f * s, -0.018f * s); lineTo(-0.157f * s, -0.175f * s); lineTo(-0.323f * s, -0.212f * s); lineTo(-0.295f * s, -0.35f * s); close() }
    val p1 = Path().apply { moveTo(-0.138f * s, -0.452f * s); quadraticTo(-0.018f * s, -0.341f * s, 0.101f * s, -0.452f * s); lineTo(0.277f * s, -0.378f * s); lineTo(0.304f * s, -0.24f * s); lineTo(0.138f * s, -0.203f * s); lineTo(0.12f * s, -0.046f * s); lineTo(0.415f * s, 0.341f * s); quadraticTo(0.328f * s, 0.452f * s, 0.242f * s, 0.341f * s); quadraticTo(0.155f * s, 0.452f * s, 0.068f * s, 0.341f * s); quadraticTo(-0.018f * s, 0.452f * s, -0.105f * s, 0.341f * s); quadraticTo(-0.192f * s, 0.452f * s, -0.278f * s, 0.341f * s); quadraticTo(-0.365f * s, 0.452f * s, -0.452f * s, 0.341f * s); lineTo(-0.157f * s, -0.046f * s); lineTo(-0.175f * s, -0.203f * s); lineTo(-0.341f * s, -0.24f * s); lineTo(-0.313f * s, -0.378f * s); close() }
    val p2 = Path().apply { moveTo(-0.052f * s, -0.287f * s); cubicTo(-0.059f * s, -0.268f * s, -0.074f * s, -0.256f * s, -0.086f * s, -0.26f * s); cubicTo(-0.098f * s, -0.265f * s, -0.102f * s, -0.284f * s, -0.095f * s, -0.303f * s); cubicTo(-0.088f * s, -0.322f * s, -0.073f * s, -0.334f * s, -0.061f * s, -0.33f * s); cubicTo(-0.049f * s, -0.325f * s, -0.045f * s, -0.306f * s, -0.052f * s, -0.287f * s); close() }
    val p3 = Path().apply { moveTo(-0.138f * s, -0.064f * s); lineTo(0.138f * s, -0.064f * s); cubicTo(0.154f * s, -0.064f * s, 0.166f * s, -0.052f * s, 0.166f * s, -0.037f * s); lineTo(0.166f * s, 0.0f * s); cubicTo(0.166f * s, 0.015f * s, 0.154f * s, 0.028f * s, 0.138f * s, 0.028f * s); lineTo(-0.138f * s, 0.028f * s); cubicTo(-0.154f * s, 0.028f * s, -0.166f * s, 0.015f * s, -0.166f * s, 0.0f * s); lineTo(-0.166f * s, -0.037f * s); cubicTo(-0.166f * s, -0.052f * s, -0.154f * s, -0.064f * s, -0.138f * s, -0.064f * s); close() }
    val p4 = Path().apply { moveTo(0.046f * s, -0.018f * s); cubicTo(0.046f * s, 0.007f * s, 0.026f * s, 0.028f * s, 0.0f * s, 0.028f * s); cubicTo(-0.025f * s, 0.028f * s, -0.046f * s, 0.007f * s, -0.046f * s, -0.018f * s); cubicTo(-0.046f * s, -0.044f * s, -0.025f * s, -0.064f * s, 0.0f * s, -0.064f * s); cubicTo(0.026f * s, -0.064f * s, 0.046f * s, -0.044f * s, 0.046f * s, -0.018f * s); close() }
    drawPath(p0, sil ?: Color(0xFFCC6B8F))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFF86B3))
        }
        clipPath(p0) {
            drawCircle(Color(0xFFFFFFFF), 0.037f * s, Offset(-0.203f * s, 0.203f * s))
            drawCircle(Color(0xFFFFFFFF), 0.037f * s, Offset(0.0f * s, 0.148f * s))
            drawCircle(Color(0xFFFFFFFF), 0.037f * s, Offset(0.203f * s, 0.221f * s))
            drawCircle(Color(0xFFFFFFFF), 0.037f * s, Offset(-0.092f * s, 0.332f * s))
            drawCircle(Color(0xFFFFFFFF), 0.037f * s, Offset(0.129f * s, 0.369f * s))
            drawCircle(Color(0xFFFFFFFF), 0.037f * s, Offset(-0.314f * s, 0.35f * s))
        }
        drawPath(p2, Color(0x99FFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFE8508E))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.028f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFFFD3E4))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artPain(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.157f * s, -0.324f * s); cubicTo(0.236f * s, -0.38f * s, 0.346f * s, -0.361f * s, 0.402f * s, -0.281f * s); cubicTo(0.457f * s, -0.202f * s, 0.438f * s, -0.092f * s, 0.358f * s, -0.036f * s); lineTo(-0.157f * s, 0.324f * s); cubicTo(-0.236f * s, 0.38f * s, -0.346f * s, 0.361f * s, -0.402f * s, 0.281f * s); cubicTo(-0.457f * s, 0.202f * s, -0.438f * s, 0.092f * s, -0.358f * s, 0.036f * s); close() }
    val p1 = Path().apply { moveTo(0.115f * s, -0.352f * s); cubicTo(0.195f * s, -0.407f * s, 0.304f * s, -0.388f * s, 0.36f * s, -0.308f * s); cubicTo(0.416f * s, -0.229f * s, 0.396f * s, -0.119f * s, 0.317f * s, -0.064f * s); lineTo(-0.198f * s, 0.297f * s); cubicTo(-0.278f * s, 0.353f * s, -0.388f * s, 0.333f * s, -0.443f * s, 0.254f * s); cubicTo(-0.499f * s, 0.175f * s, -0.48f * s, 0.065f * s, -0.4f * s, 0.009f * s); close() }
    val p2 = Path().apply { moveTo(-0.245f * s, 0.262f * s); quadraticTo(-0.151f * s, 0.173f * s, -0.209f * s, 0.056f * s); quadraticTo(-0.304f * s, 0.146f * s, -0.245f * s, 0.262f * s); close() }
    val p3 = Path().apply { moveTo(-0.094f * s, 0.156f * s); quadraticTo(0.001f * s, 0.067f * s, -0.058f * s, -0.05f * s); quadraticTo(-0.152f * s, 0.04f * s, -0.094f * s, 0.156f * s); close() }
    val p4 = Path().apply { moveTo(0.058f * s, 0.05f * s); quadraticTo(0.152f * s, -0.04f * s, 0.094f * s, -0.156f * s); quadraticTo(-0.001f * s, -0.066f * s, 0.058f * s, 0.05f * s); close() }
    val p5 = Path().apply { moveTo(0.209f * s, -0.056f * s); quadraticTo(0.304f * s, -0.146f * s, 0.246f * s, -0.262f * s); quadraticTo(0.151f * s, -0.173f * s, 0.209f * s, -0.056f * s); close() }
    val p6 = Path().apply { moveTo(-0.189f * s, 0.053f * s); cubicTo(-0.181f * s, 0.064f * s, -0.209f * s, 0.096f * s, -0.251f * s, 0.125f * s); cubicTo(-0.293f * s, 0.154f * s, -0.333f * s, 0.17f * s, -0.34f * s, 0.159f * s); cubicTo(-0.347f * s, 0.149f * s, -0.32f * s, 0.117f * s, -0.278f * s, 0.087f * s); cubicTo(-0.236f * s, 0.058f * s, -0.196f * s, 0.043f * s, -0.189f * s, 0.053f * s); close() }
    drawPath(p0, sil ?: Color(0xFF955B26))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFBA7230))
        }
    }
    clipPath(p0) {
        drawPath(p2, sil ?: Color(0xFFF6DDA4))
        drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p3, sil ?: Color(0xFFF6DDA4))
        drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p4, sil ?: Color(0xFFF6DDA4))
        drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p5, sil ?: Color(0xFFF6DDA4))
        drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    if (sil == null) {
        drawPath(p6, Color(0x80FFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artChien(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.204f * s, 0.011f * s); cubicTo(-0.234f * s, 0.132f * s, -0.308f * s, 0.217f * s, -0.368f * s, 0.202f * s); cubicTo(-0.428f * s, 0.187f * s, -0.453f * s, 0.077f * s, -0.423f * s, -0.043f * s); cubicTo(-0.393f * s, -0.164f * s, -0.319f * s, -0.249f * s, -0.259f * s, -0.234f * s); cubicTo(-0.199f * s, -0.219f * s, -0.174f * s, -0.109f * s, -0.204f * s, 0.011f * s); close() }
    val p1 = Path().apply { moveTo(-0.216f * s, -0.013f * s); cubicTo(-0.246f * s, 0.108f * s, -0.32f * s, 0.193f * s, -0.38f * s, 0.178f * s); cubicTo(-0.44f * s, 0.163f * s, -0.465f * s, 0.053f * s, -0.435f * s, -0.067f * s); cubicTo(-0.405f * s, -0.188f * s, -0.331f * s, -0.274f * s, -0.271f * s, -0.259f * s); cubicTo(-0.211f * s, -0.244f * s, -0.186f * s, -0.134f * s, -0.216f * s, -0.013f * s); close() }
    val p2 = Path().apply { moveTo(0.423f * s, -0.043f * s); cubicTo(0.453f * s, 0.077f * s, 0.428f * s, 0.187f * s, 0.368f * s, 0.202f * s); cubicTo(0.308f * s, 0.217f * s, 0.234f * s, 0.132f * s, 0.204f * s, 0.011f * s); cubicTo(0.174f * s, -0.109f * s, 0.199f * s, -0.219f * s, 0.259f * s, -0.234f * s); cubicTo(0.319f * s, -0.249f * s, 0.393f * s, -0.164f * s, 0.423f * s, -0.043f * s); close() }
    val p3 = Path().apply { moveTo(0.411f * s, -0.067f * s); cubicTo(0.441f * s, 0.053f * s, 0.416f * s, 0.163f * s, 0.356f * s, 0.178f * s); cubicTo(0.296f * s, 0.193f * s, 0.222f * s, 0.108f * s, 0.192f * s, -0.013f * s); cubicTo(0.162f * s, -0.134f * s, 0.187f * s, -0.244f * s, 0.247f * s, -0.259f * s); cubicTo(0.307f * s, -0.274f * s, 0.381f * s, -0.188f * s, 0.411f * s, -0.067f * s); close() }
    val p4 = Path().apply { moveTo(0.298f * s, 0.0f * s); cubicTo(0.298f * s, 0.151f * s, 0.164f * s, 0.273f * s, 0.0f * s, 0.273f * s); cubicTo(-0.164f * s, 0.273f * s, -0.297f * s, 0.151f * s, -0.297f * s, 0.0f * s); cubicTo(-0.297f * s, -0.151f * s, -0.164f * s, -0.273f * s, 0.0f * s, -0.273f * s); cubicTo(0.164f * s, -0.273f * s, 0.298f * s, -0.151f * s, 0.298f * s, 0.0f * s); close() }
    val p5 = Path().apply { moveTo(0.281f * s, -0.04f * s); cubicTo(0.281f * s, 0.111f * s, 0.148f * s, 0.233f * s, -0.016f * s, 0.233f * s); cubicTo(-0.18f * s, 0.233f * s, -0.313f * s, 0.111f * s, -0.313f * s, -0.04f * s); cubicTo(-0.313f * s, -0.191f * s, -0.18f * s, -0.313f * s, -0.016f * s, -0.313f * s); cubicTo(0.148f * s, -0.313f * s, 0.281f * s, -0.191f * s, 0.281f * s, -0.04f * s); close() }
    val p6 = Path().apply { moveTo(-0.024f * s, -0.08f * s); cubicTo(-0.024f * s, -0.023f * s, -0.074f * s, 0.024f * s, -0.137f * s, 0.024f * s); cubicTo(-0.199f * s, 0.024f * s, -0.249f * s, -0.023f * s, -0.249f * s, -0.08f * s); cubicTo(-0.249f * s, -0.138f * s, -0.199f * s, -0.185f * s, -0.137f * s, -0.185f * s); cubicTo(-0.074f * s, -0.185f * s, -0.024f * s, -0.138f * s, -0.024f * s, -0.08f * s); close() }
    val p7 = Path().apply { moveTo(-0.11f * s, -0.233f * s); cubicTo(-0.102f * s, -0.217f * s, -0.119f * s, -0.193f * s, -0.147f * s, -0.18f * s); cubicTo(-0.175f * s, -0.167f * s, -0.204f * s, -0.169f * s, -0.212f * s, -0.185f * s); cubicTo(-0.219f * s, -0.201f * s, -0.202f * s, -0.225f * s, -0.174f * s, -0.238f * s); cubicTo(-0.146f * s, -0.251f * s, -0.117f * s, -0.249f * s, -0.11f * s, -0.233f * s); close() }
    val p8 = Path().apply { moveTo(0.161f * s, 0.105f * s); cubicTo(0.161f * s, 0.176f * s, 0.089f * s, 0.233f * s, 0.0f * s, 0.233f * s); cubicTo(-0.089f * s, 0.233f * s, -0.161f * s, 0.176f * s, -0.161f * s, 0.105f * s); cubicTo(-0.161f * s, 0.033f * s, -0.089f * s, -0.024f * s, 0.0f * s, -0.024f * s); cubicTo(0.089f * s, -0.024f * s, 0.161f * s, 0.033f * s, 0.161f * s, 0.105f * s); close() }
    val p9 = Path().apply { moveTo(0.0f * s, 0.129f * s); lineTo(0.0f * s, 0.129f * s); cubicTo(0.029f * s, 0.129f * s, 0.052f * s, 0.152f * s, 0.052f * s, 0.181f * s); lineTo(0.052f * s, 0.197f * s); cubicTo(0.052f * s, 0.226f * s, 0.029f * s, 0.249f * s, 0.0f * s, 0.249f * s); lineTo(0.0f * s, 0.249f * s); cubicTo(-0.029f * s, 0.249f * s, -0.052f * s, 0.226f * s, -0.052f * s, 0.197f * s); lineTo(-0.052f * s, 0.181f * s); cubicTo(-0.052f * s, 0.152f * s, -0.029f * s, 0.129f * s, 0.0f * s, 0.129f * s); close() }
    val p10 = Path().apply { moveTo(0.0f * s, 0.129f * s); lineTo(0.0f * s, 0.233f * s) }
    val p11 = Path().apply { moveTo(0.0f * s, 0.064f * s); lineTo(0.0f * s, 0.129f * s); quadraticTo(-0.04f * s, 0.185f * s, -0.096f * s, 0.145f * s) }
    val p12 = Path().apply { moveTo(0.0f * s, 0.129f * s); quadraticTo(0.04f * s, 0.185f * s, 0.097f * s, 0.145f * s) }
    drawPath(p0, sil ?: Color(0xFF623B22))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF7A4A2A))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF623B22))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF7A4A2A))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF87542F))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFA9693B))
        }
        clipPath(p4) {
            drawPath(p6, Color(0xFF7A4A2A))
        }
        drawPath(p7, Color(0x8CFFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.052f * s, Offset(-0.137f * s, -0.088f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.052f * s, Offset(-0.137f * s, -0.088f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.03f * s, Offset(-0.129f * s, -0.075f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(-0.14f * s, -0.089f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.052f * s, Offset(0.137f * s, -0.088f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.052f * s, Offset(0.137f * s, -0.088f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.03f * s, Offset(0.129f * s, -0.075f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(0.117f * s, -0.089f * s))
    }
    drawPath(p8, sil ?: Color(0xFFF0D2A8))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFFF7A9A))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0x592B2D42), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0xFF2B2D42), Offset(-0.068f * s, 0.004f * s), Size(0.137f * s, 0.088f * s))
        drawCircle(Color(0xFFFFFFFF), 0.013f * s, Offset(-0.024f * s, 0.034f * s))
    }
}

private fun DrawScope.artSinge(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.21f * s, 0.032f * s); cubicTo(-0.21f * s, 0.095f * s, -0.26f * s, 0.145f * s, -0.323f * s, 0.145f * s); cubicTo(-0.385f * s, 0.145f * s, -0.435f * s, 0.095f * s, -0.435f * s, 0.032f * s); cubicTo(-0.435f * s, -0.03f * s, -0.385f * s, -0.081f * s, -0.323f * s, -0.081f * s); cubicTo(-0.26f * s, -0.081f * s, -0.21f * s, -0.03f * s, -0.21f * s, 0.032f * s); close() }
    val p1 = Path().apply { moveTo(-0.222f * s, 0.008f * s); cubicTo(-0.222f * s, 0.07f * s, -0.272f * s, 0.121f * s, -0.335f * s, 0.121f * s); cubicTo(-0.397f * s, 0.121f * s, -0.448f * s, 0.07f * s, -0.448f * s, 0.008f * s); cubicTo(-0.448f * s, -0.054f * s, -0.397f * s, -0.105f * s, -0.335f * s, -0.105f * s); cubicTo(-0.272f * s, -0.105f * s, -0.222f * s, -0.054f * s, -0.222f * s, 0.008f * s); close() }
    val p2 = Path().apply { moveTo(-0.262f * s, 0.032f * s); cubicTo(-0.262f * s, 0.066f * s, -0.289f * s, 0.093f * s, -0.323f * s, 0.093f * s); cubicTo(-0.356f * s, 0.093f * s, -0.383f * s, 0.066f * s, -0.383f * s, 0.032f * s); cubicTo(-0.383f * s, -0.001f * s, -0.356f * s, -0.028f * s, -0.323f * s, -0.028f * s); cubicTo(-0.289f * s, -0.028f * s, -0.262f * s, -0.001f * s, -0.262f * s, 0.032f * s); close() }
    val p3 = Path().apply { moveTo(0.436f * s, 0.032f * s); cubicTo(0.436f * s, 0.095f * s, 0.385f * s, 0.145f * s, 0.323f * s, 0.145f * s); cubicTo(0.26f * s, 0.145f * s, 0.21f * s, 0.095f * s, 0.21f * s, 0.032f * s); cubicTo(0.21f * s, -0.03f * s, 0.26f * s, -0.081f * s, 0.323f * s, -0.081f * s); cubicTo(0.385f * s, -0.081f * s, 0.436f * s, -0.03f * s, 0.436f * s, 0.032f * s); close() }
    val p4 = Path().apply { moveTo(0.423f * s, 0.008f * s); cubicTo(0.423f * s, 0.07f * s, 0.373f * s, 0.121f * s, 0.311f * s, 0.121f * s); cubicTo(0.248f * s, 0.121f * s, 0.198f * s, 0.07f * s, 0.198f * s, 0.008f * s); cubicTo(0.198f * s, -0.054f * s, 0.248f * s, -0.105f * s, 0.311f * s, -0.105f * s); cubicTo(0.373f * s, -0.105f * s, 0.423f * s, -0.054f * s, 0.423f * s, 0.008f * s); close() }
    val p5 = Path().apply { moveTo(0.383f * s, 0.032f * s); cubicTo(0.383f * s, 0.066f * s, 0.356f * s, 0.093f * s, 0.323f * s, 0.093f * s); cubicTo(0.289f * s, 0.093f * s, 0.262f * s, 0.066f * s, 0.262f * s, 0.032f * s); cubicTo(0.262f * s, -0.001f * s, 0.289f * s, -0.028f * s, 0.323f * s, -0.028f * s); cubicTo(0.356f * s, -0.028f * s, 0.383f * s, -0.001f * s, 0.383f * s, 0.032f * s); close() }
    val p6 = Path().apply { moveTo(0.307f * s, 0.0f * s); cubicTo(0.307f * s, 0.16f * s, 0.169f * s, 0.29f * s, 0.0f * s, 0.29f * s); cubicTo(-0.169f * s, 0.29f * s, -0.306f * s, 0.16f * s, -0.306f * s, 0.0f * s); cubicTo(-0.306f * s, -0.16f * s, -0.169f * s, -0.29f * s, 0.0f * s, -0.29f * s); cubicTo(0.169f * s, -0.29f * s, 0.307f * s, -0.16f * s, 0.307f * s, 0.0f * s); close() }
    val p7 = Path().apply { moveTo(0.29f * s, -0.04f * s); cubicTo(0.29f * s, 0.12f * s, 0.153f * s, 0.25f * s, -0.016f * s, 0.25f * s); cubicTo(-0.185f * s, 0.25f * s, -0.323f * s, 0.12f * s, -0.323f * s, -0.04f * s); cubicTo(-0.323f * s, -0.201f * s, -0.185f * s, -0.331f * s, -0.016f * s, -0.331f * s); cubicTo(0.153f * s, -0.331f * s, 0.29f * s, -0.201f * s, 0.29f * s, -0.04f * s); close() }
    val p8 = Path().apply { moveTo(-0.128f * s, -0.214f * s); cubicTo(-0.118f * s, -0.196f * s, -0.132f * s, -0.17f * s, -0.159f * s, -0.154f * s); cubicTo(-0.186f * s, -0.138f * s, -0.216f * s, -0.14f * s, -0.226f * s, -0.157f * s); cubicTo(-0.236f * s, -0.175f * s, -0.223f * s, -0.201f * s, -0.196f * s, -0.217f * s); cubicTo(-0.168f * s, -0.232f * s, -0.138f * s, -0.231f * s, -0.128f * s, -0.214f * s); close() }
    val p9 = Path().apply { moveTo(0.0f * s, -0.064f * s); cubicTo(-0.027f * s, -0.064f * s, -0.048f * s, -0.126f * s, -0.081f * s, -0.137f * s); cubicTo(-0.113f * s, -0.148f * s, -0.165f * s, -0.146f * s, -0.193f * s, -0.129f * s); cubicTo(-0.222f * s, -0.111f * s, -0.245f * s, -0.067f * s, -0.25f * s, -0.032f * s); cubicTo(-0.255f * s, 0.003f * s, -0.241f * s, 0.046f * s, -0.226f * s, 0.081f * s); cubicTo(-0.211f * s, 0.116f * s, -0.185f * s, 0.148f * s, -0.161f * s, 0.177f * s); cubicTo(-0.137f * s, 0.207f * s, -0.107f * s, 0.242f * s, -0.081f * s, 0.258f * s); cubicTo(-0.054f * s, 0.274f * s, -0.027f * s, 0.274f * s, 0.0f * s, 0.274f * s); cubicTo(0.027f * s, 0.274f * s, 0.054f * s, 0.274f * s, 0.081f * s, 0.258f * s); cubicTo(0.108f * s, 0.242f * s, 0.137f * s, 0.207f * s, 0.161f * s, 0.177f * s); cubicTo(0.186f * s, 0.148f * s, 0.211f * s, 0.116f * s, 0.226f * s, 0.081f * s); cubicTo(0.241f * s, 0.046f * s, 0.255f * s, 0.003f * s, 0.25f * s, -0.032f * s); cubicTo(0.245f * s, -0.067f * s, 0.222f * s, -0.111f * s, 0.194f * s, -0.129f * s); cubicTo(0.165f * s, -0.146f * s, 0.113f * s, -0.148f * s, 0.081f * s, -0.137f * s); cubicTo(0.048f * s, -0.126f * s, 0.027f * s, -0.064f * s, 0.0f * s, -0.064f * s); close() }
    val p10 = Path().apply { moveTo(-0.081f * s, 0.177f * s); quadraticTo(0.0f * s, 0.25f * s, 0.081f * s, 0.177f * s) }
    drawPath(p0, sil ?: Color(0xFF6E4828))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF8A5A32))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p2, Color(0xFFE7B98A))
    }
    drawPath(p3, sil ?: Color(0xFF6E4828))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFF8A5A32))
        }
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p5, Color(0xFFE7B98A))
    }
    drawPath(p6, sil ?: Color(0xFF6E4828))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFF8A5A32))
        }
        drawPath(p8, Color(0x8CFFFFFF))
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFF2D3A6))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.052f * s, Offset(-0.105f * s, -0.04f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.052f * s, Offset(-0.105f * s, -0.04f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.03f * s, Offset(-0.097f * s, -0.027f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(-0.109f * s, -0.041f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.052f * s, Offset(0.105f * s, -0.04f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.052f * s, Offset(0.105f * s, -0.04f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.03f * s, Offset(0.097f * s, -0.027f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(0.085f * s, -0.041f * s))
        drawOval(Color(0xFF9B6B45), Offset(-0.046f * s, 0.103f * s), Size(0.027f * s, 0.035f * s))
        drawOval(Color(0xFF9B6B45), Offset(0.019f * s, 0.103f * s), Size(0.027f * s, 0.035f * s))
    }
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(-0.218f * s, 0.106f * s), Size(0.065f * s, 0.045f * s))
        drawOval(Color(0x66FF4F86), Offset(0.153f * s, 0.106f * s), Size(0.065f * s, 0.045f * s))
    }
}

private fun DrawScope.artHibou(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.044f * s, 0.404f * s); cubicTo(-0.044f * s, 0.423f * s, -0.079f * s, 0.439f * s, -0.122f * s, 0.439f * s); cubicTo(-0.166f * s, 0.439f * s, -0.201f * s, 0.423f * s, -0.201f * s, 0.404f * s); cubicTo(-0.201f * s, 0.384f * s, -0.166f * s, 0.369f * s, -0.122f * s, 0.369f * s); cubicTo(-0.079f * s, 0.369f * s, -0.044f * s, 0.384f * s, -0.044f * s, 0.404f * s); close() }
    val p1 = Path().apply { moveTo(0.201f * s, 0.404f * s); cubicTo(0.201f * s, 0.423f * s, 0.166f * s, 0.439f * s, 0.123f * s, 0.439f * s); cubicTo(0.079f * s, 0.439f * s, 0.044f * s, 0.423f * s, 0.044f * s, 0.404f * s); cubicTo(0.044f * s, 0.384f * s, 0.079f * s, 0.369f * s, 0.123f * s, 0.369f * s); cubicTo(0.166f * s, 0.369f * s, 0.201f * s, 0.384f * s, 0.201f * s, 0.404f * s); close() }
    val p2 = Path().apply { moveTo(-0.321f * s, -0.167f * s); quadraticTo(-0.35f * s, -0.148f * s, -0.347f * s, -0.183f * s); lineTo(-0.327f * s, -0.41f * s); quadraticTo(-0.324f * s, -0.445f * s, -0.293f * s, -0.429f * s); lineTo(-0.119f * s, -0.339f * s); quadraticTo(-0.087f * s, -0.323f * s, -0.117f * s, -0.303f * s); close() }
    val p3 = Path().apply { moveTo(0.321f * s, -0.167f * s); quadraticTo(0.35f * s, -0.148f * s, 0.347f * s, -0.183f * s); lineTo(0.327f * s, -0.41f * s); quadraticTo(0.324f * s, -0.445f * s, 0.293f * s, -0.429f * s); lineTo(0.119f * s, -0.339f * s); quadraticTo(0.088f * s, -0.323f * s, 0.117f * s, -0.303f * s); close() }
    val p4 = Path().apply { moveTo(0.333f * s, 0.045f * s); cubicTo(0.333f * s, 0.248f * s, 0.184f * s, 0.412f * s, 0.0f * s, 0.412f * s); cubicTo(-0.184f * s, 0.412f * s, -0.333f * s, 0.248f * s, -0.333f * s, 0.045f * s); cubicTo(-0.333f * s, -0.158f * s, -0.184f * s, -0.323f * s, 0.0f * s, -0.323f * s); cubicTo(0.184f * s, -0.323f * s, 0.333f * s, -0.158f * s, 0.333f * s, 0.045f * s); close() }
    val p5 = Path().apply { moveTo(0.315f * s, 0.001f * s); cubicTo(0.315f * s, 0.204f * s, 0.166f * s, 0.369f * s, -0.017f * s, 0.369f * s); cubicTo(-0.201f * s, 0.369f * s, -0.35f * s, 0.204f * s, -0.35f * s, 0.001f * s); cubicTo(-0.35f * s, -0.202f * s, -0.201f * s, -0.366f * s, -0.017f * s, -0.366f * s); cubicTo(0.166f * s, -0.366f * s, 0.315f * s, -0.202f * s, 0.315f * s, 0.001f * s); close() }
    val p6 = Path().apply { moveTo(-0.172f * s, -0.257f * s); cubicTo(-0.164f * s, -0.242f * s, -0.174f * s, -0.221f * s, -0.195f * s, -0.209f * s); cubicTo(-0.216f * s, -0.197f * s, -0.239f * s, -0.199f * s, -0.248f * s, -0.213f * s); cubicTo(-0.256f * s, -0.228f * s, -0.246f * s, -0.25f * s, -0.225f * s, -0.262f * s); cubicTo(-0.204f * s, -0.274f * s, -0.181f * s, -0.272f * s, -0.172f * s, -0.257f * s); close() }
    val p7 = Path().apply { moveTo(0.193f * s, 0.22f * s); cubicTo(0.193f * s, 0.321f * s, 0.106f * s, 0.404f * s, 0.0f * s, 0.404f * s); cubicTo(-0.106f * s, 0.404f * s, -0.192f * s, 0.321f * s, -0.192f * s, 0.22f * s); cubicTo(-0.192f * s, 0.118f * s, -0.106f * s, 0.036f * s, 0.0f * s, 0.036f * s); cubicTo(0.106f * s, 0.036f * s, 0.193f * s, 0.118f * s, 0.193f * s, 0.22f * s); close() }
    val p8 = Path().apply { moveTo(-0.131f * s, 0.124f * s); quadraticTo(-0.079f * s, 0.189f * s, -0.026f * s, 0.124f * s) }
    val p9 = Path().apply { moveTo(0.026f * s, 0.124f * s); quadraticTo(0.079f * s, 0.189f * s, 0.131f * s, 0.124f * s) }
    val p10 = Path().apply { moveTo(-0.21f * s, 0.229f * s); quadraticTo(-0.157f * s, 0.294f * s, -0.105f * s, 0.229f * s) }
    val p11 = Path().apply { moveTo(-0.052f * s, 0.229f * s); quadraticTo(0.0f * s, 0.294f * s, 0.053f * s, 0.229f * s) }
    val p12 = Path().apply { moveTo(0.105f * s, 0.229f * s); quadraticTo(0.158f * s, 0.294f * s, 0.21f * s, 0.229f * s) }
    val p13 = Path().apply { moveTo(-0.131f * s, 0.334f * s); quadraticTo(-0.079f * s, 0.399f * s, -0.026f * s, 0.334f * s) }
    val p14 = Path().apply { moveTo(0.026f * s, 0.334f * s); quadraticTo(0.079f * s, 0.399f * s, 0.131f * s, 0.334f * s) }
    val p15 = Path().apply { moveTo(-0.237f * s, 0.129f * s); cubicTo(-0.257f * s, 0.253f * s, -0.312f * s, 0.347f * s, -0.359f * s, 0.34f * s); cubicTo(-0.407f * s, 0.332f * s, -0.43f * s, 0.225f * s, -0.41f * s, 0.101f * s); cubicTo(-0.391f * s, -0.023f * s, -0.336f * s, -0.117f * s, -0.288f * s, -0.11f * s); cubicTo(-0.24f * s, -0.102f * s, -0.218f * s, 0.004f * s, -0.237f * s, 0.129f * s); close() }
    val p16 = Path().apply { moveTo(0.41f * s, 0.101f * s); cubicTo(0.43f * s, 0.225f * s, 0.407f * s, 0.332f * s, 0.359f * s, 0.34f * s); cubicTo(0.312f * s, 0.347f * s, 0.257f * s, 0.253f * s, 0.237f * s, 0.129f * s); cubicTo(0.218f * s, 0.004f * s, 0.241f * s, -0.102f * s, 0.288f * s, -0.11f * s); cubicTo(0.336f * s, -0.117f * s, 0.391f * s, -0.023f * s, 0.41f * s, 0.101f * s); close() }
    val p17 = Path().apply { moveTo(0.004f * s, -0.13f * s); cubicTo(0.004f * s, -0.055f * s, -0.06f * s, 0.005f * s, -0.14f * s, 0.005f * s); cubicTo(-0.22f * s, 0.005f * s, -0.284f * s, -0.055f * s, -0.284f * s, -0.13f * s); cubicTo(-0.284f * s, -0.205f * s, -0.22f * s, -0.266f * s, -0.14f * s, -0.266f * s); cubicTo(-0.06f * s, -0.266f * s, 0.004f * s, -0.205f * s, 0.004f * s, -0.13f * s); close() }
    val p18 = Path().apply { moveTo(0.284f * s, -0.13f * s); cubicTo(0.284f * s, -0.055f * s, 0.22f * s, 0.005f * s, 0.14f * s, 0.005f * s); cubicTo(0.06f * s, 0.005f * s, -0.004f * s, -0.055f * s, -0.004f * s, -0.13f * s); cubicTo(-0.004f * s, -0.205f * s, 0.06f * s, -0.266f * s, 0.14f * s, -0.266f * s); cubicTo(0.22f * s, -0.266f * s, 0.284f * s, -0.205f * s, 0.284f * s, -0.13f * s); close() }
    val p19 = Path().apply { moveTo(-0.045f * s, -0.044f * s); quadraticTo(-0.052f * s, -0.06f * s, -0.035f * s, -0.06f * s); lineTo(0.035f * s, -0.06f * s); quadraticTo(0.053f * s, -0.06f * s, 0.045f * s, -0.044f * s); lineTo(0.008f * s, 0.029f * s); quadraticTo(0.0f * s, 0.045f * s, -0.008f * s, 0.029f * s); close() }
    drawPath(p0, sil ?: Color(0xFFFF8A1F))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFFF8A1F))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF7A4A2A))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF7A4A2A))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF87542F))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFA9693B))
        }
        drawPath(p6, Color(0x8CFFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p7, Color(0xFFF2D8B0))
    }
    drawPath(p8, sil ?: Color(0xFFB98A5A), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFB98A5A), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFFB98A5A), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFFB98A5A), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFFB98A5A), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p13, sil ?: Color(0xFFB98A5A), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p14, sil ?: Color(0xFFB98A5A), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p15, sil ?: Color(0xFF7A4A2A))
    drawPath(p15, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p16, sil ?: Color(0xFF7A4A2A))
    drawPath(p16, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p17, sil ?: Color(0xFFF2D8B0))
    drawPath(p17, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p18, sil ?: Color(0xFFF2D8B0))
    drawPath(p18, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.092f * s, Offset(-0.14f * s, -0.13f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.092f * s, Offset(-0.14f * s, -0.13f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.053f * s, Offset(-0.122f * s, -0.107f * s))
        drawCircle(Color(0xFFFFFFFF), 0.018f * s, Offset(-0.142f * s, -0.131f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.092f * s, Offset(0.14f * s, -0.13f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.092f * s, Offset(0.14f * s, -0.13f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.053f * s, Offset(0.122f * s, -0.107f * s))
        drawCircle(Color(0xFFFFFFFF), 0.018f * s, Offset(0.101f * s, -0.131f * s))
    }
    drawPath(p19, sil ?: Color(0xFFFF8A1F))
    drawPath(p19, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artEscargot(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.257f * s, -0.054f * s); quadraticTo(0.241f * s, -0.146f * s, 0.218f * s, -0.239f * s) }
    val p1 = Path().apply { moveTo(0.334f * s, -0.054f * s); quadraticTo(0.365f * s, -0.146f * s, 0.395f * s, -0.223f * s) }
    val p2 = Path().apply { moveTo(-0.436f * s, 0.285f * s); cubicTo(-0.359f * s, 0.246f * s, -0.267f * s, 0.177f * s, -0.19f * s, 0.162f * s); lineTo(0.118f * s, 0.116f * s); cubicTo(0.18f * s, 0.1f * s, 0.195f * s, 0.008f * s, 0.226f * s, -0.054f * s); cubicTo(0.249f * s, -0.123f * s, 0.349f * s, -0.123f * s, 0.372f * s, -0.038f * s); cubicTo(0.403f * s, 0.054f * s, 0.388f * s, 0.162f * s, 0.357f * s, 0.223f * s); cubicTo(0.341f * s, 0.277f * s, 0.303f * s, 0.308f * s, 0.241f * s, 0.308f * s); lineTo(-0.344f * s, 0.308f * s); quadraticTo(-0.421f * s, 0.308f * s, -0.436f * s, 0.285f * s); close() }
    val p3 = Path().apply { moveTo(-0.451f * s, 0.254f * s); cubicTo(-0.375f * s, 0.216f * s, -0.282f * s, 0.146f * s, -0.205f * s, 0.131f * s); lineTo(0.103f * s, 0.085f * s); cubicTo(0.164f * s, 0.069f * s, 0.18f * s, -0.023f * s, 0.211f * s, -0.085f * s); cubicTo(0.234f * s, -0.154f * s, 0.334f * s, -0.154f * s, 0.357f * s, -0.069f * s); cubicTo(0.388f * s, 0.023f * s, 0.372f * s, 0.131f * s, 0.341f * s, 0.193f * s); cubicTo(0.326f * s, 0.246f * s, 0.288f * s, 0.277f * s, 0.226f * s, 0.277f * s); lineTo(-0.359f * s, 0.277f * s); quadraticTo(-0.436f * s, 0.277f * s, -0.451f * s, 0.254f * s); close() }
    val p4 = Path().apply { moveTo(0.315f * s, 0.139f * s); quadraticTo(0.349f * s, 0.185f * s, 0.384f * s, 0.139f * s) }
    val p5 = Path().apply { moveTo(0.157f * s, -0.038f * s); cubicTo(0.157f * s, 0.11f * s, 0.036f * s, 0.231f * s, -0.113f * s, 0.231f * s); cubicTo(-0.262f * s, 0.231f * s, -0.382f * s, 0.11f * s, -0.382f * s, -0.038f * s); cubicTo(-0.382f * s, -0.187f * s, -0.262f * s, -0.308f * s, -0.113f * s, -0.308f * s); cubicTo(0.036f * s, -0.308f * s, 0.157f * s, -0.187f * s, 0.157f * s, -0.038f * s); close() }
    val p6 = Path().apply { moveTo(0.137f * s, -0.077f * s); cubicTo(0.137f * s, 0.072f * s, 0.017f * s, 0.193f * s, -0.132f * s, 0.193f * s); cubicTo(-0.281f * s, 0.193f * s, -0.401f * s, 0.072f * s, -0.401f * s, -0.077f * s); cubicTo(-0.401f * s, -0.226f * s, -0.281f * s, -0.346f * s, -0.132f * s, -0.346f * s); cubicTo(0.017f * s, -0.346f * s, 0.137f * s, -0.226f * s, 0.137f * s, -0.077f * s); close() }
    val p7 = Path().apply { moveTo(-0.237f * s, -0.122f * s); cubicTo(-0.262f * s, -0.092f * s, -0.295f * s, -0.079f * s, -0.311f * s, -0.093f * s); cubicTo(-0.328f * s, -0.107f * s, -0.321f * s, -0.142f * s, -0.296f * s, -0.171f * s); cubicTo(-0.272f * s, -0.2f * s, -0.238f * s, -0.213f * s, -0.222f * s, -0.199f * s); cubicTo(-0.206f * s, -0.186f * s, -0.213f * s, -0.151f * s, -0.237f * s, -0.122f * s); close() }
    val p8 = Path().apply { moveTo(-0.105f * s, -0.034f * s); quadraticTo(-0.115f * s, -0.038f * s, -0.117f * s, -0.045f * s); quadraticTo(-0.12f * s, -0.051f * s, -0.117f * s, -0.059f * s); quadraticTo(-0.115f * s, -0.067f * s, -0.106f * s, -0.074f * s); quadraticTo(-0.098f * s, -0.08f * s, -0.086f * s, -0.081f * s); quadraticTo(-0.073f * s, -0.082f * s, -0.06f * s, -0.075f * s); quadraticTo(-0.047f * s, -0.069f * s, -0.039f * s, -0.054f * s); quadraticTo(-0.031f * s, -0.04f * s, -0.031f * s, -0.021f * s); quadraticTo(-0.032f * s, -0.002f * s, -0.043f * s, 0.015f * s); quadraticTo(-0.055f * s, 0.033f * s, -0.075f * s, 0.043f * s); quadraticTo(-0.096f * s, 0.053f * s, -0.121f * s, 0.05f * s); quadraticTo(-0.145f * s, 0.048f * s, -0.167f * s, 0.033f * s); quadraticTo(-0.189f * s, 0.017f * s, -0.201f * s, -0.01f * s); quadraticTo(-0.213f * s, -0.036f * s, -0.209f * s, -0.067f * s); quadraticTo(-0.206f * s, -0.098f * s, -0.185f * s, -0.125f * s); quadraticTo(-0.165f * s, -0.152f * s, -0.133f * s, -0.165f * s); quadraticTo(-0.1f * s, -0.179f * s, -0.063f * s, -0.173f * s); quadraticTo(-0.026f * s, -0.168f * s, 0.006f * s, -0.144f * s); quadraticTo(0.037f * s, -0.119f * s, 0.052f * s, -0.08f * s); quadraticTo(0.067f * s, -0.041f * s, 0.061f * s, 0.002f * s); quadraticTo(0.054f * s, 0.045f * s, 0.025f * s, 0.081f * s); quadraticTo(-0.004f * s, 0.117f * s, -0.049f * s, 0.134f * s); quadraticTo(-0.094f * s, 0.151f * s, -0.143f * s, 0.143f * s); quadraticTo(-0.193f * s, 0.134f * s, -0.233f * s, 0.101f * s); quadraticTo(-0.274f * s, 0.068f * s, -0.292f * s, 0.016f * s); quadraticTo(-0.311f * s, -0.035f * s, -0.292f * s, -0.146f * s) }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.117f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.117f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFFDDA56B), style = Stroke(0.045f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFDDA56B), style = Stroke(0.045f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.046f * s, Offset(0.218f * s, -0.254f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.046f * s, Offset(0.218f * s, -0.254f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.027f * s, Offset(0.232f * s, -0.245f * s))
        drawCircle(Color(0xFFFFFFFF), 0.009f * s, Offset(0.222f * s, -0.257f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.046f * s, Offset(0.395f * s, -0.239f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.046f * s, Offset(0.395f * s, -0.239f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.027f * s, Offset(0.409f * s, -0.229f * s))
        drawCircle(Color(0xFFFFFFFF), 0.009f * s, Offset(0.399f * s, -0.241f * s))
    }
    drawPath(p2, sil ?: Color(0xFFBA8B5A))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFDDA56B))
        }
        drawOval(Color(0x8CFFFFFF), Offset(0.291f * s, -0.073f * s), Size(0.038f * s, 0.054f * s))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(0.284f * s, 0.158f * s), Size(0.054f * s, 0.038f * s))
    }
    drawPath(p5, sil ?: Color(0xFF87542F))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFFA9693B))
        }
        drawPath(p7, Color(0x8CFFFFFF))
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF6B3E20), style = Stroke(0.034f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artGland(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.233f * s, -0.081f * s); cubicTo(-0.295f * s, 0.112f * s, -0.215f * s, 0.312f * s, -0.138f * s, 0.433f * s); cubicTo(-0.005f * s, 0.38f * s, 0.177f * s, 0.265f * s, 0.24f * s, 0.073f * s); close() }
    val p1 = Path().apply { moveTo(-0.243f * s, -0.123f * s); cubicTo(-0.306f * s, 0.07f * s, -0.225f * s, 0.27f * s, -0.149f * s, 0.391f * s); cubicTo(-0.016f * s, 0.338f * s, 0.167f * s, 0.223f * s, 0.229f * s, 0.031f * s); close() }
    val p2 = Path().apply { moveTo(-0.141f * s, 0.155f * s); cubicTo(-0.146f * s, 0.205f * s, -0.164f * s, 0.245f * s, -0.182f * s, 0.243f * s); cubicTo(-0.2f * s, 0.241f * s, -0.21f * s, 0.199f * s, -0.205f * s, 0.148f * s); cubicTo(-0.199f * s, 0.098f * s, -0.181f * s, 0.058f * s, -0.163f * s, 0.06f * s); cubicTo(-0.145f * s, 0.062f * s, -0.135f * s, 0.104f * s, -0.141f * s, 0.155f * s); close() }
    val p3 = Path().apply { moveTo(0.089f * s, -0.266f * s); quadraticTo(0.117f * s, -0.354f * s, 0.187f * s, -0.389f * s) }
    val p4 = Path().apply { moveTo(-0.325f * s, -0.033f * s); cubicTo(-0.272f * s, -0.287f * s, -0.086f * s, -0.323f * s, 0.089f * s, -0.266f * s); cubicTo(0.264f * s, -0.209f * s, 0.393f * s, -0.071f * s, 0.287f * s, 0.166f * s); quadraticTo(-0.045f * s, 0.145f * s, -0.325f * s, -0.033f * s); close() }
    val p5 = Path().apply { moveTo(-0.331f * s, -0.074f * s); cubicTo(-0.278f * s, -0.328f * s, -0.092f * s, -0.364f * s, 0.083f * s, -0.307f * s); cubicTo(0.258f * s, -0.25f * s, 0.387f * s, -0.112f * s, 0.281f * s, 0.125f * s); quadraticTo(-0.051f * s, 0.104f * s, -0.331f * s, -0.074f * s); close() }
    val p6 = Path().apply { moveTo(-0.198f * s, -0.186f * s); quadraticTo(-0.148f * s, -0.073f * s, -0.041f * s, -0.134f * s) }
    val p7 = Path().apply { moveTo(-0.041f * s, -0.134f * s); quadraticTo(0.009f * s, -0.021f * s, 0.117f * s, -0.083f * s) }
    val p8 = Path().apply { moveTo(0.117f * s, -0.083f * s); quadraticTo(0.167f * s, 0.03f * s, 0.274f * s, -0.032f * s) }
    val p9 = Path().apply { moveTo(-0.151f * s, -0.064f * s); quadraticTo(-0.101f * s, 0.049f * s, 0.007f * s, -0.013f * s) }
    val p10 = Path().apply { moveTo(0.007f * s, -0.013f * s); quadraticTo(0.057f * s, 0.1f * s, 0.164f * s, 0.039f * s) }
    val p11 = Path().apply { moveTo(-0.308f * s, -0.115f * s); quadraticTo(-0.258f * s, -0.002f * s, -0.151f * s, -0.064f * s) }
    val p12 = Path().apply { moveTo(0.164f * s, 0.039f * s); quadraticTo(0.214f * s, 0.152f * s, 0.321f * s, 0.09f * s) }
    val p13 = Path().apply { moveTo(-0.077f * s, -0.215f * s); cubicTo(-0.075f * s, -0.192f * s, -0.093f * s, -0.172f * s, -0.118f * s, -0.168f * s); cubicTo(-0.143f * s, -0.165f * s, -0.166f * s, -0.181f * s, -0.169f * s, -0.204f * s); cubicTo(-0.171f * s, -0.227f * s, -0.153f * s, -0.247f * s, -0.128f * s, -0.251f * s); cubicTo(-0.103f * s, -0.254f * s, -0.08f * s, -0.238f * s, -0.077f * s, -0.215f * s); close() }
    drawPath(p0, sil ?: Color(0xFFA16F3C))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFC98B4B))
        }
        drawPath(p2, Color(0x8CFFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.132f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF5E3A20), style = Stroke(0.06f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF623B22))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFF7A4A2A))
        }
    }
    clipPath(p4) {
        drawPath(p6, sil ?: Color(0xFF4E2D17), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p7, sil ?: Color(0xFF4E2D17), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p8, sil ?: Color(0xFF4E2D17), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p9, sil ?: Color(0xFF4E2D17), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p10, sil ?: Color(0xFF4E2D17), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p11, sil ?: Color(0xFF4E2D17), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(p12, sil ?: Color(0xFF4E2D17), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
    if (sil == null) {
        drawPath(p13, Color(0x66FFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artPommedepin(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.067f * s, -0.302f * s); quadraticTo(0.081f * s, -0.358f * s, 0.132f * s, -0.396f * s) }
    val p1 = Path().apply { moveTo(0.211f * s, 0.072f * s); cubicTo(0.171f * s, 0.231f * s, 0.039f * s, 0.335f * s, -0.084f * s, 0.304f * s); cubicTo(-0.207f * s, 0.273f * s, -0.275f * s, 0.12f * s, -0.236f * s, -0.039f * s); cubicTo(-0.196f * s, -0.197f * s, -0.064f * s, -0.301f * s, 0.059f * s, -0.27f * s); cubicTo(0.182f * s, -0.239f * s, 0.25f * s, -0.086f * s, 0.211f * s, 0.072f * s); close() }
    val p2 = Path().apply { moveTo(-0.146f * s, 0.229f * s); lineTo(0.005f * s, 0.267f * s); lineTo(-0.016f * s, 0.355f * s); quadraticTo(-0.036f * s, 0.434f * s, -0.118f * s, 0.439f * s); quadraticTo(-0.188f * s, 0.397f * s, -0.168f * s, 0.317f * s); close() }
    val p3 = Path().apply { moveTo(-0.194f * s, 0.099f * s); lineTo(-0.043f * s, 0.136f * s); lineTo(-0.064f * s, 0.224f * s); quadraticTo(-0.084f * s, 0.304f * s, -0.166f * s, 0.309f * s); quadraticTo(-0.236f * s, 0.266f * s, -0.216f * s, 0.186f * s); close() }
    val p4 = Path().apply { moveTo(-0.043f * s, 0.136f * s); lineTo(0.109f * s, 0.174f * s); lineTo(0.087f * s, 0.262f * s); quadraticTo(0.067f * s, 0.342f * s, -0.015f * s, 0.347f * s); quadraticTo(-0.084f * s, 0.304f * s, -0.064f * s, 0.224f * s); close() }
    val p5 = Path().apply { moveTo(-0.242f * s, -0.032f * s); lineTo(-0.09f * s, 0.006f * s); lineTo(-0.112f * s, 0.094f * s); quadraticTo(-0.132f * s, 0.173f * s, -0.214f * s, 0.178f * s); quadraticTo(-0.284f * s, 0.136f * s, -0.264f * s, 0.056f * s); close() }
    val p6 = Path().apply { moveTo(-0.09f * s, 0.006f * s); lineTo(0.061f * s, 0.044f * s); lineTo(0.039f * s, 0.131f * s); quadraticTo(0.019f * s, 0.211f * s, -0.062f * s, 0.216f * s); quadraticTo(-0.132f * s, 0.173f * s, -0.112f * s, 0.094f * s); close() }
    val p7 = Path().apply { moveTo(0.061f * s, 0.044f * s); lineTo(0.212f * s, 0.082f * s); lineTo(0.191f * s, 0.169f * s); quadraticTo(0.171f * s, 0.249f * s, 0.089f * s, 0.254f * s); quadraticTo(0.019f * s, 0.211f * s, 0.039f * s, 0.131f * s); close() }
    val p8 = Path().apply { moveTo(-0.29f * s, -0.162f * s); lineTo(-0.138f * s, -0.125f * s); lineTo(-0.16f * s, -0.037f * s); quadraticTo(-0.18f * s, 0.043f * s, -0.262f * s, 0.048f * s); quadraticTo(-0.332f * s, 0.005f * s, -0.312f * s, -0.075f * s); close() }
    val p9 = Path().apply { moveTo(-0.138f * s, -0.125f * s); lineTo(0.013f * s, -0.087f * s); lineTo(-0.009f * s, 0.001f * s); quadraticTo(-0.029f * s, 0.081f * s, -0.11f * s, 0.086f * s); quadraticTo(-0.18f * s, 0.043f * s, -0.16f * s, -0.037f * s); close() }
    val p10 = Path().apply { moveTo(0.013f * s, -0.087f * s); lineTo(0.165f * s, -0.049f * s); lineTo(0.143f * s, 0.039f * s); quadraticTo(0.123f * s, 0.118f * s, 0.041f * s, 0.123f * s); quadraticTo(-0.029f * s, 0.081f * s, -0.009f * s, 0.001f * s); close() }
    val p11 = Path().apply { moveTo(0.165f * s, -0.049f * s); lineTo(0.316f * s, -0.011f * s); lineTo(0.294f * s, 0.076f * s); quadraticTo(0.274f * s, 0.156f * s, 0.193f * s, 0.161f * s); quadraticTo(0.123f * s, 0.118f * s, 0.143f * s, 0.039f * s); close() }
    val p12 = Path().apply { moveTo(-0.186f * s, -0.255f * s); lineTo(-0.035f * s, -0.217f * s); lineTo(-0.057f * s, -0.13f * s); quadraticTo(-0.077f * s, -0.05f * s, -0.158f * s, -0.045f * s); quadraticTo(-0.228f * s, -0.088f * s, -0.208f * s, -0.167f * s); close() }
    val p13 = Path().apply { moveTo(-0.035f * s, -0.217f * s); lineTo(0.117f * s, -0.179f * s); lineTo(0.095f * s, -0.092f * s); quadraticTo(0.075f * s, -0.012f * s, -0.007f * s, -0.007f * s); quadraticTo(-0.077f * s, -0.05f * s, -0.057f * s, -0.13f * s); close() }
    val p14 = Path().apply { moveTo(0.117f * s, -0.179f * s); lineTo(0.268f * s, -0.142f * s); lineTo(0.246f * s, -0.054f * s); quadraticTo(0.226f * s, 0.026f * s, 0.145f * s, 0.031f * s); quadraticTo(0.075f * s, -0.012f * s, 0.095f * s, -0.092f * s); close() }
    val p15 = Path().apply { moveTo(-0.083f * s, -0.348f * s); lineTo(0.069f * s, -0.31f * s); lineTo(0.047f * s, -0.222f * s); quadraticTo(0.027f * s, -0.143f * s, -0.055f * s, -0.138f * s); quadraticTo(-0.124f * s, -0.18f * s, -0.105f * s, -0.26f * s); close() }
    val p16 = Path().apply { moveTo(0.069f * s, -0.31f * s); lineTo(0.22f * s, -0.272f * s); lineTo(0.198f * s, -0.185f * s); quadraticTo(0.178f * s, -0.105f * s, 0.097f * s, -0.1f * s); quadraticTo(0.027f * s, -0.143f * s, 0.047f * s, -0.222f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.132f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF5E3A20), style = Stroke(0.06f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p1, Color(0xFF6B3E20))
    }
    drawPath(p2, sil ?: Color(0xFFA06232))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFBE7F43))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFA06232))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFA06232))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFBE7F43))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFA06232))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFFBE7F43))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFA06232))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFFBE7F43))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFFA06232))
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFFA06232))
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p13, sil ?: Color(0xFFBE7F43))
    drawPath(p13, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p14, sil ?: Color(0xFFA06232))
    drawPath(p14, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p15, sil ?: Color(0xFFBE7F43))
    drawPath(p15, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p16, sil ?: Color(0xFFA06232))
    drawPath(p16, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artValise(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.159f * s, -0.185f * s); lineTo(-0.159f * s, -0.325f * s); quadraticTo(-0.159f * s, -0.391f * s, -0.094f * s, -0.391f * s); lineTo(0.094f * s, -0.391f * s); quadraticTo(0.159f * s, -0.391f * s, 0.159f * s, -0.325f * s); lineTo(0.159f * s, -0.185f * s) }
    val p1 = Path().apply { moveTo(-0.337f * s, 0.358f * s); lineTo(-0.262f * s, 0.358f * s); cubicTo(-0.247f * s, 0.358f * s, -0.234f * s, 0.371f * s, -0.234f * s, 0.386f * s); lineTo(-0.234f * s, 0.405f * s); cubicTo(-0.234f * s, 0.421f * s, -0.247f * s, 0.433f * s, -0.262f * s, 0.433f * s); lineTo(-0.337f * s, 0.433f * s); cubicTo(-0.352f * s, 0.433f * s, -0.365f * s, 0.421f * s, -0.365f * s, 0.405f * s); lineTo(-0.365f * s, 0.386f * s); cubicTo(-0.365f * s, 0.371f * s, -0.352f * s, 0.358f * s, -0.337f * s, 0.358f * s); close() }
    val p2 = Path().apply { moveTo(0.262f * s, 0.358f * s); lineTo(0.337f * s, 0.358f * s); cubicTo(0.353f * s, 0.358f * s, 0.365f * s, 0.371f * s, 0.365f * s, 0.386f * s); lineTo(0.365f * s, 0.405f * s); cubicTo(0.365f * s, 0.421f * s, 0.353f * s, 0.433f * s, 0.337f * s, 0.433f * s); lineTo(0.262f * s, 0.433f * s); cubicTo(0.247f * s, 0.433f * s, 0.234f * s, 0.421f * s, 0.234f * s, 0.405f * s); lineTo(0.234f * s, 0.386f * s); cubicTo(0.234f * s, 0.371f * s, 0.247f * s, 0.358f * s, 0.262f * s, 0.358f * s); close() }
    val p3 = Path().apply { moveTo(-0.346f * s, -0.185f * s); lineTo(0.346f * s, -0.185f * s); cubicTo(0.393f * s, -0.185f * s, 0.431f * s, -0.147f * s, 0.431f * s, -0.1f * s); lineTo(0.431f * s, 0.293f * s); cubicTo(0.431f * s, 0.339f * s, 0.393f * s, 0.377f * s, 0.346f * s, 0.377f * s); lineTo(-0.346f * s, 0.377f * s); cubicTo(-0.393f * s, 0.377f * s, -0.431f * s, 0.339f * s, -0.431f * s, 0.293f * s); lineTo(-0.431f * s, -0.1f * s); cubicTo(-0.431f * s, -0.147f * s, -0.393f * s, -0.185f * s, -0.346f * s, -0.185f * s); close() }
    val p4 = Path().apply { moveTo(-0.37f * s, -0.227f * s); lineTo(0.323f * s, -0.227f * s); cubicTo(0.37f * s, -0.227f * s, 0.407f * s, -0.189f * s, 0.407f * s, -0.142f * s); lineTo(0.407f * s, 0.251f * s); cubicTo(0.407f * s, 0.297f * s, 0.37f * s, 0.335f * s, 0.323f * s, 0.335f * s); lineTo(-0.37f * s, 0.335f * s); cubicTo(-0.416f * s, 0.335f * s, -0.454f * s, 0.297f * s, -0.454f * s, 0.251f * s); lineTo(-0.454f * s, -0.142f * s); cubicTo(-0.454f * s, -0.189f * s, -0.416f * s, -0.227f * s, -0.37f * s, -0.227f * s); close() }
    val p5 = Path().apply { moveTo(-0.255f * s, -0.107f * s); cubicTo(-0.249f * s, -0.09f * s, -0.264f * s, -0.069f * s, -0.288f * s, -0.06f * s); cubicTo(-0.313f * s, -0.051f * s, -0.337f * s, -0.058f * s, -0.343f * s, -0.075f * s); cubicTo(-0.35f * s, -0.092f * s, -0.335f * s, -0.113f * s, -0.311f * s, -0.122f * s); cubicTo(-0.286f * s, -0.131f * s, -0.262f * s, -0.124f * s, -0.255f * s, -0.107f * s); close() }
    val p6 = Path().apply { moveTo(-0.164f * s, -0.072f * s); lineTo(-0.08f * s, -0.072f * s); cubicTo(-0.067f * s, -0.072f * s, -0.056f * s, -0.062f * s, -0.056f * s, -0.049f * s); lineTo(-0.056f * s, 0.017f * s); cubicTo(-0.056f * s, 0.03f * s, -0.067f * s, 0.04f * s, -0.08f * s, 0.04f * s); lineTo(-0.164f * s, 0.04f * s); cubicTo(-0.177f * s, 0.04f * s, -0.187f * s, 0.03f * s, -0.187f * s, 0.017f * s); lineTo(-0.187f * s, -0.049f * s); cubicTo(-0.187f * s, -0.062f * s, -0.177f * s, -0.072f * s, -0.164f * s, -0.072f * s); close() }
    val p7 = Path().apply { moveTo(0.08f * s, -0.072f * s); lineTo(0.164f * s, -0.072f * s); cubicTo(0.177f * s, -0.072f * s, 0.187f * s, -0.062f * s, 0.187f * s, -0.049f * s); lineTo(0.187f * s, 0.017f * s); cubicTo(0.187f * s, 0.03f * s, 0.177f * s, 0.04f * s, 0.164f * s, 0.04f * s); lineTo(0.08f * s, 0.04f * s); cubicTo(0.067f * s, 0.04f * s, 0.056f * s, 0.03f * s, 0.056f * s, 0.017f * s); lineTo(0.056f * s, -0.049f * s); cubicTo(0.056f * s, -0.062f * s, 0.067f * s, -0.072f * s, 0.08f * s, -0.072f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.127f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFF6B3E20), style = Stroke(0.055f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF6B3E20))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF6B3E20))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF87542F))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFA9693B))
        }
        clipPath(p3) {
            drawRoundRect(Color(0xFF6B3E20), Offset(-0.318f * s, -0.185f * s), Size(0.094f * s, 0.562f * s), CornerRadius(0.0f * s))
            drawRoundRect(Color(0xFF6B3E20), Offset(0.225f * s, -0.185f * s), Size(0.094f * s, 0.562f * s), CornerRadius(0.0f * s))
            drawRoundRect(Color(0x996B3E20), Offset(-0.468f * s, -0.016f * s), Size(0.936f * s, 0.042f * s), CornerRadius(0.0f * s))
        }
        drawPath(p5, Color(0x8CFFFFFF))
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFFFFD23F))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFFFFD23F))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artGuitare(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(0.33f * s, -0.426f * s); lineTo(0.381f * s, -0.384f * s); cubicTo(0.397f * s, -0.37f * s, 0.399f * s, -0.347f * s, 0.386f * s, -0.331f * s); lineTo(0.322f * s, -0.255f * s); cubicTo(0.308f * s, -0.239f * s, 0.285f * s, -0.237f * s, 0.269f * s, -0.25f * s); lineTo(0.218f * s, -0.293f * s); cubicTo(0.202f * s, -0.306f * s, 0.2f * s, -0.33f * s, 0.213f * s, -0.345f * s); lineTo(0.278f * s, -0.422f * s); cubicTo(0.291f * s, -0.437f * s, 0.314f * s, -0.44f * s, 0.33f * s, -0.426f * s); close() }
    val p1 = Path().apply { moveTo(0.257f * s, -0.336f * s); lineTo(0.305f * s, -0.296f * s); cubicTo(0.309f * s, -0.292f * s, 0.31f * s, -0.286f * s, 0.306f * s, -0.282f * s); lineTo(0.074f * s, -0.004f * s); cubicTo(0.07f * s, 0.0f * s, 0.064f * s, 0.001f * s, 0.059f * s, -0.003f * s); lineTo(0.011f * s, -0.044f * s); cubicTo(0.007f * s, -0.047f * s, 0.006f * s, -0.053f * s, 0.01f * s, -0.058f * s); lineTo(0.243f * s, -0.335f * s); cubicTo(0.246f * s, -0.339f * s, 0.252f * s, -0.34f * s, 0.257f * s, -0.336f * s); close() }
    val p2 = Path().apply { moveTo(0.073f * s, -0.068f * s); cubicTo(0.098f * s, -0.046f * s, 0.131f * s, -0.022f * s, 0.144f * s, 0.003f * s); cubicTo(0.156f * s, 0.028f * s, 0.156f * s, 0.058f * s, 0.148f * s, 0.082f * s); cubicTo(0.139f * s, 0.106f * s, 0.12f * s, 0.131f * s, 0.094f * s, 0.145f * s); cubicTo(0.069f * s, 0.16f * s, 0.01f * s, 0.152f * s, -0.004f * s, 0.172f * s); cubicTo(-0.017f * s, 0.191f * s, 0.012f * s, 0.23f * s, 0.013f * s, 0.262f * s); cubicTo(0.014f * s, 0.293f * s, 0.018f * s, 0.334f * s, 0.004f * s, 0.363f * s); cubicTo(-0.009f * s, 0.391f * s, -0.038f * s, 0.421f * s, -0.069f * s, 0.431f * s); cubicTo(-0.101f * s, 0.44f * s, -0.149f * s, 0.432f * s, -0.185f * s, 0.42f * s); cubicTo(-0.221f * s, 0.408f * s, -0.255f * s, 0.383f * s, -0.285f * s, 0.358f * s); cubicTo(-0.315f * s, 0.333f * s, -0.345f * s, 0.304f * s, -0.363f * s, 0.271f * s); cubicTo(-0.381f * s, 0.238f * s, -0.398f * s, 0.191f * s, -0.394f * s, 0.159f * s); cubicTo(-0.39f * s, 0.126f * s, -0.365f * s, 0.093f * s, -0.339f * s, 0.074f * s); cubicTo(-0.314f * s, 0.056f * s, -0.273f * s, 0.052f * s, -0.241f * s, 0.048f * s); cubicTo(-0.21f * s, 0.044f * s, -0.167f * s, 0.066f * s, -0.15f * s, 0.049f * s); cubicTo(-0.133f * s, 0.032f * s, -0.151f * s, -0.025f * s, -0.141f * s, -0.052f * s); cubicTo(-0.131f * s, -0.079f * s, -0.11f * s, -0.103f * s, -0.088f * s, -0.116f * s); cubicTo(-0.066f * s, -0.128f * s, -0.036f * s, -0.133f * s, -0.009f * s, -0.125f * s); cubicTo(0.018f * s, -0.118f * s, 0.047f * s, -0.089f * s, 0.073f * s, -0.068f * s); close() }
    val p3 = Path().apply { moveTo(0.081f * s, -0.11f * s); cubicTo(0.106f * s, -0.088f * s, 0.139f * s, -0.064f * s, 0.152f * s, -0.039f * s); cubicTo(0.164f * s, -0.014f * s, 0.164f * s, 0.016f * s, 0.156f * s, 0.04f * s); cubicTo(0.148f * s, 0.064f * s, 0.128f * s, 0.089f * s, 0.102f * s, 0.104f * s); cubicTo(0.077f * s, 0.118f * s, 0.018f * s, 0.11f * s, 0.005f * s, 0.13f * s); cubicTo(-0.009f * s, 0.149f * s, 0.02f * s, 0.188f * s, 0.021f * s, 0.22f * s); cubicTo(0.023f * s, 0.251f * s, 0.026f * s, 0.292f * s, 0.012f * s, 0.321f * s); cubicTo(-0.001f * s, 0.349f * s, -0.03f * s, 0.379f * s, -0.061f * s, 0.389f * s); cubicTo(-0.093f * s, 0.399f * s, -0.141f * s, 0.39f * s, -0.177f * s, 0.378f * s); cubicTo(-0.213f * s, 0.366f * s, -0.247f * s, 0.341f * s, -0.277f * s, 0.316f * s); cubicTo(-0.306f * s, 0.292f * s, -0.337f * s, 0.262f * s, -0.355f * s, 0.229f * s); cubicTo(-0.373f * s, 0.196f * s, -0.39f * s, 0.149f * s, -0.386f * s, 0.117f * s); cubicTo(-0.382f * s, 0.084f * s, -0.357f * s, 0.051f * s, -0.331f * s, 0.032f * s); cubicTo(-0.306f * s, 0.014f * s, -0.265f * s, 0.01f * s, -0.233f * s, 0.006f * s); cubicTo(-0.202f * s, 0.002f * s, -0.158f * s, 0.024f * s, -0.142f * s, 0.007f * s); cubicTo(-0.125f * s, -0.01f * s, -0.143f * s, -0.067f * s, -0.133f * s, -0.094f * s); cubicTo(-0.123f * s, -0.121f * s, -0.102f * s, -0.145f * s, -0.08f * s, -0.158f * s); cubicTo(-0.058f * s, -0.17f * s, -0.028f * s, -0.175f * s, -0.001f * s, -0.167f * s); cubicTo(0.026f * s, -0.159f * s, 0.055f * s, -0.131f * s, 0.081f * s, -0.11f * s); close() }
    val p4 = Path().apply { moveTo(-0.266f * s, 0.192f * s); cubicTo(-0.289f * s, 0.237f * s, -0.322f * s, 0.266f * s, -0.338f * s, 0.257f * s); cubicTo(-0.354f * s, 0.249f * s, -0.348f * s, 0.205f * s, -0.324f * s, 0.161f * s); cubicTo(-0.301f * s, 0.116f * s, -0.268f * s, 0.087f * s, -0.252f * s, 0.096f * s); cubicTo(-0.236f * s, 0.105f * s, -0.242f * s, 0.148f * s, -0.266f * s, 0.192f * s); close() }
    val p5 = Path().apply { moveTo(-0.081f * s, 0.226f * s); cubicTo(-0.106f * s, 0.255f * s, -0.151f * s, 0.259f * s, -0.181f * s, 0.234f * s); cubicTo(-0.211f * s, 0.209f * s, -0.215f * s, 0.165f * s, -0.19f * s, 0.135f * s); cubicTo(-0.164f * s, 0.105f * s, -0.12f * s, 0.101f * s, -0.09f * s, 0.126f * s); cubicTo(-0.06f * s, 0.151f * s, -0.056f * s, 0.196f * s, -0.081f * s, 0.226f * s); close() }
    val p6 = Path().apply { moveTo(-0.099f * s, 0.211f * s); cubicTo(-0.116f * s, 0.232f * s, -0.146f * s, 0.234f * s, -0.166f * s, 0.217f * s); cubicTo(-0.187f * s, 0.2f * s, -0.189f * s, 0.17f * s, -0.172f * s, 0.149f * s); cubicTo(-0.155f * s, 0.129f * s, -0.125f * s, 0.126f * s, -0.105f * s, 0.143f * s); cubicTo(-0.084f * s, 0.16f * s, -0.082f * s, 0.191f * s, -0.099f * s, 0.211f * s); close() }
    val p7 = Path().apply { moveTo(-0.278f * s, 0.234f * s); lineTo(-0.164f * s, 0.33f * s); cubicTo(-0.157f * s, 0.336f * s, -0.156f * s, 0.346f * s, -0.162f * s, 0.353f * s); lineTo(-0.17f * s, 0.363f * s); cubicTo(-0.175f * s, 0.37f * s, -0.186f * s, 0.371f * s, -0.193f * s, 0.365f * s); lineTo(-0.307f * s, 0.269f * s); cubicTo(-0.314f * s, 0.263f * s, -0.315f * s, 0.253f * s, -0.31f * s, 0.246f * s); lineTo(-0.301f * s, 0.236f * s); cubicTo(-0.296f * s, 0.229f * s, -0.285f * s, 0.228f * s, -0.278f * s, 0.234f * s); close() }
    drawPath(p0, sil ?: Color(0xFF4E2D17))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF6B3E20))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF9A5F30))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFFC0773C))
        }
        drawPath(p4, Color(0x8CFFFFFF))
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF8A5230))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p6, Color(0xFF2B2D42))
    }
    drawPath(p7, sil ?: Color(0xFF4E2D17))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artOeuf(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.202f * s, -0.372f * s); cubicTo(-0.162f * s, -0.408f * s, -0.109f * s, -0.425f * s, -0.056f * s, -0.42f * s); cubicTo(-0.003f * s, -0.414f * s, 0.045f * s, -0.387f * s, 0.077f * s, -0.344f * s); cubicTo(0.131f * s, -0.37f * s, 0.195f * s, -0.365f * s, 0.243f * s, -0.329f * s); cubicTo(0.292f * s, -0.293f * s, 0.316f * s, -0.234f * s, 0.307f * s, -0.175f * s); cubicTo(0.391f * s, -0.139f * s, 0.441f * s, -0.052f * s, 0.432f * s, 0.039f * s); cubicTo(0.423f * s, 0.129f * s, 0.355f * s, 0.204f * s, 0.266f * s, 0.222f * s); cubicTo(0.251f * s, 0.27f * s, 0.216f * s, 0.31f * s, 0.17f * s, 0.332f * s); cubicTo(0.124f * s, 0.354f * s, 0.071f * s, 0.355f * s, 0.024f * s, 0.335f * s); cubicTo(-0.03f * s, 0.413f * s, -0.132f * s, 0.442f * s, -0.219f * s, 0.404f * s); cubicTo(-0.305f * s, 0.367f * s, -0.354f * s, 0.273f * s, -0.334f * s, 0.18f * s); cubicTo(-0.38f * s, 0.158f * s, -0.413f * s, 0.117f * s, -0.424f * s, 0.067f * s); cubicTo(-0.436f * s, 0.018f * s, -0.426f * s, -0.034f * s, -0.396f * s, -0.074f * s); cubicTo(-0.443f * s, -0.138f * s, -0.446f * s, -0.224f * s, -0.402f * s, -0.291f * s); cubicTo(-0.359f * s, -0.357f * s, -0.279f * s, -0.39f * s, -0.202f * s, -0.372f * s); close() }
    val p1 = Path().apply { moveTo(-0.226f * s, -0.42f * s); cubicTo(-0.186f * s, -0.455f * s, -0.133f * s, -0.472f * s, -0.08f * s, -0.467f * s); cubicTo(-0.027f * s, -0.462f * s, 0.021f * s, -0.434f * s, 0.053f * s, -0.391f * s); cubicTo(0.107f * s, -0.418f * s, 0.171f * s, -0.412f * s, 0.219f * s, -0.377f * s); cubicTo(0.268f * s, -0.341f * s, 0.292f * s, -0.281f * s, 0.283f * s, -0.222f * s); cubicTo(0.367f * s, -0.186f * s, 0.418f * s, -0.1f * s, 0.408f * s, -0.009f * s); cubicTo(0.399f * s, 0.082f * s, 0.331f * s, 0.156f * s, 0.242f * s, 0.174f * s); cubicTo(0.227f * s, 0.223f * s, 0.192f * s, 0.263f * s, 0.146f * s, 0.285f * s); cubicTo(0.1f * s, 0.306f * s, 0.047f * s, 0.308f * s, 0.0f * s, 0.288f * s); cubicTo(-0.054f * s, 0.366f * s, -0.155f * s, 0.394f * s, -0.242f * s, 0.357f * s); cubicTo(-0.329f * s, 0.319f * s, -0.378f * s, 0.226f * s, -0.358f * s, 0.133f * s); cubicTo(-0.403f * s, 0.11f * s, -0.436f * s, 0.069f * s, -0.448f * s, 0.02f * s); cubicTo(-0.46f * s, -0.029f * s, -0.449f * s, -0.081f * s, -0.419f * s, -0.122f * s); cubicTo(-0.467f * s, -0.185f * s, -0.469f * s, -0.272f * s, -0.426f * s, -0.338f * s); cubicTo(-0.383f * s, -0.405f * s, -0.303f * s, -0.437f * s, -0.226f * s, -0.42f * s); close() }
    val p2 = Path().apply { moveTo(-0.243f * s, -0.257f * s); cubicTo(-0.234f * s, -0.244f * s, -0.248f * s, -0.219f * s, -0.274f * s, -0.201f * s); cubicTo(-0.299f * s, -0.183f * s, -0.328f * s, -0.179f * s, -0.337f * s, -0.192f * s); cubicTo(-0.346f * s, -0.205f * s, -0.332f * s, -0.23f * s, -0.306f * s, -0.248f * s); cubicTo(-0.281f * s, -0.266f * s, -0.252f * s, -0.27f * s, -0.243f * s, -0.257f * s); close() }
    val p3 = Path().apply { moveTo(0.127f * s, -0.016f * s); cubicTo(0.127f * s, 0.089f * s, 0.042f * s, 0.174f * s, -0.062f * s, 0.174f * s); cubicTo(-0.167f * s, 0.174f * s, -0.252f * s, 0.089f * s, -0.252f * s, -0.016f * s); cubicTo(-0.252f * s, -0.121f * s, -0.167f * s, -0.206f * s, -0.062f * s, -0.206f * s); cubicTo(0.042f * s, -0.206f * s, 0.127f * s, -0.121f * s, 0.127f * s, -0.016f * s); close() }
    val p4 = Path().apply { moveTo(0.108f * s, -0.054f * s); cubicTo(0.108f * s, 0.051f * s, 0.023f * s, 0.136f * s, -0.081f * s, 0.136f * s); cubicTo(-0.186f * s, 0.136f * s, -0.271f * s, 0.051f * s, -0.271f * s, -0.054f * s); cubicTo(-0.271f * s, -0.159f * s, -0.186f * s, -0.244f * s, -0.081f * s, -0.244f * s); cubicTo(0.023f * s, -0.244f * s, 0.108f * s, -0.159f * s, 0.108f * s, -0.054f * s); close() }
    val p5 = Path().apply { moveTo(-0.09f * s, -0.119f * s); cubicTo(-0.069f * s, -0.089f * s, -0.069f * s, -0.052f * s, -0.091f * s, -0.037f * s); cubicTo(-0.112f * s, -0.022f * s, -0.147f * s, -0.035f * s, -0.168f * s, -0.065f * s); cubicTo(-0.189f * s, -0.095f * s, -0.188f * s, -0.131f * s, -0.167f * s, -0.146f * s); cubicTo(-0.145f * s, -0.161f * s, -0.111f * s, -0.149f * s, -0.09f * s, -0.119f * s); close() }
    drawPath(p0, sil ?: Color(0xFFD8D7D1))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFF6F4EE))
        }
        drawPath(p2, Color(0x8CFFFFFF))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFD6A31A))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFFFC21F))
        }
        drawPath(p5, Color(0x8CFFFFFF))
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artLapin(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.033f * s, -0.173f * s); cubicTo(-0.008f * s, -0.022f * s, -0.031f * s, 0.108f * s, -0.082f * s, 0.116f * s); cubicTo(-0.134f * s, 0.124f * s, -0.195f * s, 0.008f * s, -0.219f * s, -0.144f * s); cubicTo(-0.243f * s, -0.296f * s, -0.221f * s, -0.426f * s, -0.169f * s, -0.434f * s); cubicTo(-0.118f * s, -0.442f * s, -0.057f * s, -0.325f * s, -0.033f * s, -0.173f * s); close() }
    val p1 = Path().apply { moveTo(-0.046f * s, -0.209f * s); cubicTo(-0.022f * s, -0.058f * s, -0.044f * s, 0.072f * s, -0.096f * s, 0.08f * s); cubicTo(-0.147f * s, 0.088f * s, -0.208f * s, -0.028f * s, -0.232f * s, -0.18f * s); cubicTo(-0.256f * s, -0.332f * s, -0.234f * s, -0.461f * s, -0.183f * s, -0.47f * s); cubicTo(-0.131f * s, -0.478f * s, -0.07f * s, -0.361f * s, -0.046f * s, -0.209f * s); close() }
    val p2 = Path().apply { moveTo(-0.086f * s, -0.148f * s); cubicTo(-0.07f * s, -0.05f * s, -0.078f * s, 0.033f * s, -0.102f * s, 0.037f * s); cubicTo(-0.127f * s, 0.041f * s, -0.159f * s, -0.036f * s, -0.175f * s, -0.134f * s); cubicTo(-0.19f * s, -0.232f * s, -0.183f * s, -0.314f * s, -0.158f * s, -0.318f * s); cubicTo(-0.134f * s, -0.322f * s, -0.101f * s, -0.246f * s, -0.086f * s, -0.148f * s); close() }
    val p3 = Path().apply { moveTo(0.219f * s, -0.144f * s); cubicTo(0.195f * s, 0.008f * s, 0.134f * s, 0.124f * s, 0.082f * s, 0.116f * s); cubicTo(0.031f * s, 0.108f * s, 0.009f * s, -0.022f * s, 0.033f * s, -0.173f * s); cubicTo(0.057f * s, -0.325f * s, 0.118f * s, -0.442f * s, 0.169f * s, -0.434f * s); cubicTo(0.221f * s, -0.426f * s, 0.243f * s, -0.296f * s, 0.219f * s, -0.144f * s); close() }
    val p4 = Path().apply { moveTo(0.205f * s, -0.18f * s); cubicTo(0.181f * s, -0.028f * s, 0.12f * s, 0.088f * s, 0.069f * s, 0.08f * s); cubicTo(0.017f * s, 0.072f * s, -0.005f * s, -0.058f * s, 0.019f * s, -0.209f * s); cubicTo(0.043f * s, -0.361f * s, 0.104f * s, -0.478f * s, 0.156f * s, -0.47f * s); cubicTo(0.207f * s, -0.461f * s, 0.229f * s, -0.332f * s, 0.205f * s, -0.18f * s); close() }
    val p5 = Path().apply { moveTo(0.175f * s, -0.134f * s); cubicTo(0.159f * s, -0.036f * s, 0.127f * s, 0.041f * s, 0.102f * s, 0.037f * s); cubicTo(0.078f * s, 0.033f * s, 0.07f * s, -0.05f * s, 0.086f * s, -0.148f * s); cubicTo(0.101f * s, -0.246f * s, 0.134f * s, -0.322f * s, 0.158f * s, -0.318f * s); cubicTo(0.183f * s, -0.314f * s, 0.19f * s, -0.232f * s, 0.175f * s, -0.134f * s); close() }
    val p6 = Path().apply { moveTo(0.278f * s, 0.191f * s); cubicTo(0.278f * s, 0.325f * s, 0.154f * s, 0.434f * s, 0.0f * s, 0.434f * s); cubicTo(-0.154f * s, 0.434f * s, -0.278f * s, 0.325f * s, -0.278f * s, 0.191f * s); cubicTo(-0.278f * s, 0.058f * s, -0.154f * s, -0.051f * s, 0.0f * s, -0.051f * s); cubicTo(0.154f * s, -0.051f * s, 0.278f * s, 0.058f * s, 0.278f * s, 0.191f * s); close() }
    val p7 = Path().apply { moveTo(0.26f * s, 0.151f * s); cubicTo(0.26f * s, 0.285f * s, 0.136f * s, 0.393f * s, -0.018f * s, 0.393f * s); cubicTo(-0.172f * s, 0.393f * s, -0.296f * s, 0.285f * s, -0.296f * s, 0.151f * s); cubicTo(-0.296f * s, 0.017f * s, -0.172f * s, -0.091f * s, -0.018f * s, -0.091f * s); cubicTo(0.136f * s, -0.091f * s, 0.26f * s, 0.017f * s, 0.26f * s, 0.151f * s); close() }
    val p8 = Path().apply { moveTo(-0.133f * s, 0.012f * s); cubicTo(-0.124f * s, 0.027f * s, -0.138f * s, 0.051f * s, -0.164f * s, 0.066f * s); cubicTo(-0.19f * s, 0.081f * s, -0.218f * s, 0.081f * s, -0.226f * s, 0.066f * s); cubicTo(-0.235f * s, 0.051f * s, -0.221f * s, 0.026f * s, -0.195f * s, 0.012f * s); cubicTo(-0.169f * s, -0.003f * s, -0.142f * s, -0.003f * s, -0.133f * s, 0.012f * s); close() }
    val p9 = Path().apply { moveTo(-0.027f * s, 0.295f * s); lineTo(0.027f * s, 0.295f * s); cubicTo(0.038f * s, 0.295f * s, 0.047f * s, 0.304f * s, 0.047f * s, 0.314f * s); lineTo(0.047f * s, 0.365f * s); cubicTo(0.047f * s, 0.376f * s, 0.038f * s, 0.385f * s, 0.027f * s, 0.385f * s); lineTo(-0.027f * s, 0.385f * s); cubicTo(-0.038f * s, 0.385f * s, -0.047f * s, 0.376f * s, -0.047f * s, 0.365f * s); lineTo(-0.047f * s, 0.314f * s); cubicTo(-0.047f * s, 0.304f * s, -0.038f * s, 0.295f * s, -0.027f * s, 0.295f * s); close() }
    val p10 = Path().apply { moveTo(0.0f * s, 0.218f * s); lineTo(0.0f * s, 0.263f * s) }
    val p11 = Path().apply { moveTo(0.0f * s, 0.263f * s); quadraticTo(-0.045f * s, 0.317f * s, -0.099f * s, 0.281f * s) }
    val p12 = Path().apply { moveTo(0.0f * s, 0.263f * s); quadraticTo(0.045f * s, 0.317f * s, 0.099f * s, 0.281f * s) }
    val p13 = Path().apply { moveTo(-0.032f * s, 0.194f * s); quadraticTo(-0.049f * s, 0.173f * s, -0.022f * s, 0.173f * s); lineTo(0.022f * s, 0.173f * s); quadraticTo(0.049f * s, 0.173f * s, 0.032f * s, 0.194f * s); lineTo(0.017f * s, 0.211f * s); quadraticTo(0.0f * s, 0.232f * s, -0.017f * s, 0.211f * s); close() }
    drawPath(p0, sil ?: Color(0xFFDDDCD6))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFF6F4EE))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p2, Color(0xFFFFB6C9))
    }
    drawPath(p3, sil ?: Color(0xFFDDDCD6))
    if (sil == null) {
        clipPath(p3) {
            drawPath(p4, Color(0xFFF6F4EE))
        }
    }
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p5, Color(0xFFFFB6C9))
    }
    drawPath(p6, sil ?: Color(0xFFDDDCD6))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFFF6F4EE))
        }
        drawPath(p8, Color(0x8CFFFFFF))
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.054f * s, Offset(-0.117f * s, 0.138f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.054f * s, Offset(-0.117f * s, 0.138f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.031f * s, Offset(-0.109f * s, 0.151f * s))
        drawCircle(Color(0xFFFFFFFF), 0.011f * s, Offset(-0.121f * s, 0.137f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.054f * s, Offset(0.117f * s, 0.138f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.054f * s, Offset(0.117f * s, 0.138f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.031f * s, Offset(0.109f * s, 0.151f * s))
        drawCircle(Color(0xFFFFFFFF), 0.011f * s, Offset(0.097f * s, 0.137f * s))
        drawOval(Color(0x66FF4F86), Offset(-0.247f * s, 0.209f * s), Size(0.081f * s, 0.054f * s))
        drawOval(Color(0x66FF4F86), Offset(0.166f * s, 0.209f * s), Size(0.081f * s, 0.054f * s))
    }
    drawPath(p9, sil ?: Color(0xFFFFFFFF))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawLine(Color(0x662B2D42), Offset(0.0f * s, 0.295f * s), Offset(0.0f * s, 0.384f * s), 0.014f * s, StrokeCap.Round)
    }
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p13, sil ?: Color(0xFFFF7A9A))
    drawPath(p13, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artMouton(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.23f * s, 0.291f * s); cubicTo(-0.23f * s, 0.309f * s, -0.245f * s, 0.324f * s, -0.263f * s, 0.324f * s); cubicTo(-0.28f * s, 0.324f * s, -0.295f * s, 0.309f * s, -0.295f * s, 0.291f * s); lineTo(-0.295f * s, 0.059f * s); cubicTo(-0.295f * s, 0.041f * s, -0.28f * s, 0.027f * s, -0.263f * s, 0.027f * s); cubicTo(-0.245f * s, 0.027f * s, -0.23f * s, 0.041f * s, -0.23f * s, 0.059f * s); close() }
    val p1 = Path().apply { moveTo(-0.11f * s, 0.291f * s); cubicTo(-0.11f * s, 0.309f * s, -0.125f * s, 0.324f * s, -0.142f * s, 0.324f * s); cubicTo(-0.16f * s, 0.324f * s, -0.174f * s, 0.309f * s, -0.174f * s, 0.291f * s); lineTo(-0.174f * s, 0.059f * s); cubicTo(-0.174f * s, 0.041f * s, -0.16f * s, 0.027f * s, -0.142f * s, 0.027f * s); cubicTo(-0.125f * s, 0.027f * s, -0.11f * s, 0.041f * s, -0.11f * s, 0.059f * s); close() }
    val p2 = Path().apply { moveTo(0.05f * s, 0.291f * s); cubicTo(0.05f * s, 0.309f * s, 0.036f * s, 0.324f * s, 0.018f * s, 0.324f * s); cubicTo(0.0f * s, 0.324f * s, -0.014f * s, 0.309f * s, -0.014f * s, 0.291f * s); lineTo(-0.014f * s, 0.059f * s); cubicTo(-0.014f * s, 0.041f * s, 0.0f * s, 0.027f * s, 0.018f * s, 0.027f * s); cubicTo(0.036f * s, 0.027f * s, 0.05f * s, 0.041f * s, 0.05f * s, 0.059f * s); close() }
    val p3 = Path().apply { moveTo(0.178f * s, 0.291f * s); cubicTo(0.178f * s, 0.309f * s, 0.164f * s, 0.324f * s, 0.146f * s, 0.324f * s); cubicTo(0.129f * s, 0.324f * s, 0.114f * s, 0.309f * s, 0.114f * s, 0.291f * s); lineTo(0.114f * s, 0.059f * s); cubicTo(0.114f * s, 0.041f * s, 0.129f * s, 0.027f * s, 0.146f * s, 0.027f * s); cubicTo(0.164f * s, 0.027f * s, 0.178f * s, 0.041f * s, 0.178f * s, 0.059f * s); close() }
    val p4 = Path().apply { moveTo(-0.242f * s, -0.255f * s); cubicTo(-0.223f * s, -0.293f * s, -0.187f * s, -0.319f * s, -0.145f * s, -0.323f * s); cubicTo(-0.103f * s, -0.327f * s, -0.062f * s, -0.309f * s, -0.037f * s, -0.275f * s); cubicTo(-0.003f * s, -0.3f * s, 0.041f * s, -0.305f * s, 0.08f * s, -0.289f * s); cubicTo(0.119f * s, -0.273f * s, 0.147f * s, -0.238f * s, 0.153f * s, -0.197f * s); cubicTo(0.197f * s, -0.187f * s, 0.232f * s, -0.154f * s, 0.243f * s, -0.11f * s); cubicTo(0.255f * s, -0.067f * s, 0.241f * s, -0.021f * s, 0.207f * s, 0.01f * s); cubicTo(0.216f * s, 0.051f * s, 0.202f * s, 0.095f * s, 0.17f * s, 0.123f * s); cubicTo(0.139f * s, 0.152f * s, 0.094f * s, 0.162f * s, 0.053f * s, 0.149f * s); cubicTo(0.035f * s, 0.187f * s, -0.002f * s, 0.212f * s, -0.044f * s, 0.216f * s); cubicTo(-0.086f * s, 0.221f * s, -0.127f * s, 0.202f * s, -0.152f * s, 0.168f * s); cubicTo(-0.186f * s, 0.193f * s, -0.23f * s, 0.199f * s, -0.269f * s, 0.183f * s); cubicTo(-0.307f * s, 0.167f * s, -0.335f * s, 0.132f * s, -0.342f * s, 0.09f * s); cubicTo(-0.386f * s, 0.081f * s, -0.42f * s, 0.047f * s, -0.432f * s, 0.004f * s); cubicTo(-0.443f * s, -0.04f * s, -0.429f * s, -0.086f * s, -0.396f * s, -0.116f * s); cubicTo(-0.405f * s, -0.158f * s, -0.39f * s, -0.201f * s, -0.359f * s, -0.23f * s); cubicTo(-0.327f * s, -0.259f * s, -0.282f * s, -0.268f * s, -0.242f * s, -0.255f * s); close() }
    val p5 = Path().apply { moveTo(-0.258f * s, -0.295f * s); cubicTo(-0.239f * s, -0.333f * s, -0.203f * s, -0.359f * s, -0.161f * s, -0.363f * s); cubicTo(-0.119f * s, -0.367f * s, -0.078f * s, -0.349f * s, -0.053f * s, -0.315f * s); cubicTo(-0.019f * s, -0.34f * s, 0.025f * s, -0.345f * s, 0.064f * s, -0.329f * s); cubicTo(0.103f * s, -0.313f * s, 0.131f * s, -0.279f * s, 0.137f * s, -0.237f * s); cubicTo(0.181f * s, -0.227f * s, 0.216f * s, -0.194f * s, 0.227f * s, -0.15f * s); cubicTo(0.239f * s, -0.107f * s, 0.225f * s, -0.061f * s, 0.191f * s, -0.03f * s); cubicTo(0.2f * s, 0.011f * s, 0.186f * s, 0.055f * s, 0.154f * s, 0.083f * s); cubicTo(0.123f * s, 0.112f * s, 0.078f * s, 0.122f * s, 0.037f * s, 0.109f * s); cubicTo(0.019f * s, 0.147f * s, -0.018f * s, 0.172f * s, -0.06f * s, 0.176f * s); cubicTo(-0.102f * s, 0.18f * s, -0.143f * s, 0.162f * s, -0.168f * s, 0.128f * s); cubicTo(-0.202f * s, 0.153f * s, -0.246f * s, 0.159f * s, -0.285f * s, 0.143f * s); cubicTo(-0.323f * s, 0.127f * s, -0.351f * s, 0.092f * s, -0.358f * s, 0.05f * s); cubicTo(-0.402f * s, 0.041f * s, -0.436f * s, 0.007f * s, -0.448f * s, -0.036f * s); cubicTo(-0.459f * s, -0.08f * s, -0.445f * s, -0.126f * s, -0.412f * s, -0.156f * s); cubicTo(-0.421f * s, -0.198f * s, -0.406f * s, -0.241f * s, -0.375f * s, -0.27f * s); cubicTo(-0.343f * s, -0.299f * s, -0.298f * s, -0.308f * s, -0.258f * s, -0.295f * s); close() }
    val p6 = Path().apply { moveTo(-0.164f * s, -0.057f * s); cubicTo(-0.17f * s, -0.041f * s, -0.186f * s, -0.032f * s, -0.203f * s, -0.033f * s); cubicTo(-0.219f * s, -0.035f * s, -0.232f * s, -0.049f * s, -0.234f * s, -0.065f * s); cubicTo(-0.236f * s, -0.082f * s, -0.226f * s, -0.098f * s, -0.211f * s, -0.103f * s) }
    val p7 = Path().apply { moveTo(-0.072f * s, 0.039f * s); cubicTo(-0.078f * s, 0.024f * s, -0.072f * s, 0.006f * s, -0.058f * s, -0.003f * s); cubicTo(-0.044f * s, -0.012f * s, -0.025f * s, -0.011f * s, -0.013f * s, 0.001f * s); cubicTo(-0.001f * s, 0.012f * s, 0.002f * s, 0.03f * s, -0.007f * s, 0.045f * s) }
    val p8 = Path().apply { moveTo(0.028f * s, -0.102f * s); cubicTo(0.014f * s, -0.094f * s, -0.005f * s, -0.096f * s, -0.016f * s, -0.109f * s); cubicTo(-0.028f * s, -0.121f * s, -0.029f * s, -0.139f * s, -0.02f * s, -0.153f * s); cubicTo(-0.011f * s, -0.167f * s, 0.007f * s, -0.173f * s, 0.022f * s, -0.167f * s) }
    val p9 = Path().apply { moveTo(-0.156f * s, -0.202f * s); cubicTo(-0.147f * s, -0.186f * s, -0.155f * s, -0.165f * s, -0.174f * s, -0.154f * s); cubicTo(-0.194f * s, -0.143f * s, -0.216f * s, -0.146f * s, -0.225f * s, -0.161f * s); cubicTo(-0.234f * s, -0.177f * s, -0.226f * s, -0.198f * s, -0.206f * s, -0.209f * s); cubicTo(-0.187f * s, -0.22f * s, -0.164f * s, -0.217f * s, -0.156f * s, -0.202f * s); close() }
    val p10 = Path().apply { moveTo(0.434f * s, 0.06f * s); cubicTo(0.422f * s, 0.143f * s, 0.359f * s, 0.203f * s, 0.294f * s, 0.194f * s); cubicTo(0.228f * s, 0.185f * s, 0.184f * s, 0.109f * s, 0.196f * s, 0.026f * s); cubicTo(0.207f * s, -0.057f * s, 0.27f * s, -0.117f * s, 0.336f * s, -0.108f * s); cubicTo(0.402f * s, -0.099f * s, 0.445f * s, -0.024f * s, 0.434f * s, 0.06f * s); close() }
    val p11 = Path().apply { moveTo(0.422f * s, 0.032f * s); cubicTo(0.41f * s, 0.115f * s, 0.347f * s, 0.175f * s, 0.282f * s, 0.166f * s); cubicTo(0.216f * s, 0.156f * s, 0.172f * s, 0.081f * s, 0.184f * s, -0.002f * s); cubicTo(0.195f * s, -0.085f * s, 0.258f * s, -0.145f * s, 0.324f * s, -0.136f * s); cubicTo(0.39f * s, -0.127f * s, 0.433f * s, -0.052f * s, 0.422f * s, 0.032f * s); close() }
    val p12 = Path().apply { moveTo(0.289f * s, -0.037f * s); cubicTo(0.282f * s, -0.018f * s, 0.267f * s, -0.007f * s, 0.254f * s, -0.011f * s); cubicTo(0.242f * s, -0.016f * s, 0.237f * s, -0.035f * s, 0.244f * s, -0.053f * s); cubicTo(0.251f * s, -0.072f * s, 0.266f * s, -0.084f * s, 0.279f * s, -0.079f * s); cubicTo(0.291f * s, -0.075f * s, 0.296f * s, -0.056f * s, 0.289f * s, -0.037f * s); close() }
    val p13 = Path().apply { moveTo(0.257f * s, -0.059f * s); cubicTo(0.268f * s, -0.039f * s, 0.244f * s, -0.007f * s, 0.205f * s, 0.014f * s); cubicTo(0.166f * s, 0.035f * s, 0.126f * s, 0.036f * s, 0.116f * s, 0.016f * s); cubicTo(0.105f * s, -0.003f * s, 0.129f * s, -0.036f * s, 0.168f * s, -0.057f * s); cubicTo(0.207f * s, -0.077f * s, 0.247f * s, -0.078f * s, 0.257f * s, -0.059f * s); close() }
    val p14 = Path().apply { moveTo(0.367f * s, -0.101f * s); cubicTo(0.367f * s, -0.073f * s, 0.336f * s, -0.049f * s, 0.299f * s, -0.049f * s); cubicTo(0.261f * s, -0.049f * s, 0.231f * s, -0.073f * s, 0.231f * s, -0.101f * s); cubicTo(0.231f * s, -0.13f * s, 0.261f * s, -0.153f * s, 0.299f * s, -0.153f * s); cubicTo(0.336f * s, -0.153f * s, 0.367f * s, -0.13f * s, 0.367f * s, -0.101f * s); close() }
    val p15 = Path().apply { moveTo(0.359f * s, -0.117f * s); cubicTo(0.359f * s, -0.089f * s, 0.328f * s, -0.065f * s, 0.291f * s, -0.065f * s); cubicTo(0.253f * s, -0.065f * s, 0.223f * s, -0.089f * s, 0.223f * s, -0.117f * s); cubicTo(0.223f * s, -0.146f * s, 0.253f * s, -0.17f * s, 0.291f * s, -0.17f * s); cubicTo(0.328f * s, -0.17f * s, 0.359f * s, -0.146f * s, 0.359f * s, -0.117f * s); close() }
    val p16 = Path().apply { moveTo(0.339f * s, 0.115f * s); quadraticTo(0.371f * s, 0.163f * s, 0.403f * s, 0.115f * s) }
    drawPath(p0, sil ?: Color(0xFF3F4258))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF3F4258))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF3F4258))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF3F4258))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFD8D7D1))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFF6F4EE))
        }
    }
    drawPath(p6, sil ?: Color(0x472B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0x472B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0x472B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p9, Color(0x8CFFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF323546))
    if (sil == null) {
        clipPath(p10) {
            drawPath(p11, Color(0xFF3F4258))
        }
        drawPath(p12, Color(0x4DFFFFFF))
    }
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p13, sil ?: Color(0xFF3F4258))
    drawPath(p13, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p14, sil ?: Color(0xFFDDDCD6))
    if (sil == null) {
        clipPath(p14) {
            drawPath(p15, Color(0xFFF6F4EE))
        }
    }
    drawPath(p14, sil ?: Color(0xFF2B2D42), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.042f * s, Offset(0.363f * s, 0.019f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.042f * s, Offset(0.363f * s, 0.019f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.024f * s, Offset(0.375f * s, 0.027f * s))
        drawCircle(Color(0xFFFFFFFF), 0.008f * s, Offset(0.366f * s, 0.016f * s))
    }
    drawPath(p16, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artPanda(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.175f * s, -0.258f * s); cubicTo(-0.175f * s, -0.187f * s, -0.233f * s, -0.129f * s, -0.304f * s, -0.129f * s); cubicTo(-0.376f * s, -0.129f * s, -0.433f * s, -0.187f * s, -0.433f * s, -0.258f * s); cubicTo(-0.433f * s, -0.329f * s, -0.376f * s, -0.387f * s, -0.304f * s, -0.387f * s); cubicTo(-0.233f * s, -0.387f * s, -0.175f * s, -0.329f * s, -0.175f * s, -0.258f * s); close() }
    val p1 = Path().apply { moveTo(-0.189f * s, -0.286f * s); cubicTo(-0.189f * s, -0.215f * s, -0.247f * s, -0.157f * s, -0.318f * s, -0.157f * s); cubicTo(-0.389f * s, -0.157f * s, -0.447f * s, -0.215f * s, -0.447f * s, -0.286f * s); cubicTo(-0.447f * s, -0.357f * s, -0.389f * s, -0.415f * s, -0.318f * s, -0.415f * s); cubicTo(-0.247f * s, -0.415f * s, -0.189f * s, -0.357f * s, -0.189f * s, -0.286f * s); close() }
    val p2 = Path().apply { moveTo(0.433f * s, -0.258f * s); cubicTo(0.433f * s, -0.187f * s, 0.376f * s, -0.129f * s, 0.304f * s, -0.129f * s); cubicTo(0.233f * s, -0.129f * s, 0.175f * s, -0.187f * s, 0.175f * s, -0.258f * s); cubicTo(0.175f * s, -0.329f * s, 0.233f * s, -0.387f * s, 0.304f * s, -0.387f * s); cubicTo(0.376f * s, -0.387f * s, 0.433f * s, -0.329f * s, 0.433f * s, -0.258f * s); close() }
    val p3 = Path().apply { moveTo(0.42f * s, -0.286f * s); cubicTo(0.42f * s, -0.215f * s, 0.362f * s, -0.157f * s, 0.291f * s, -0.157f * s); cubicTo(0.219f * s, -0.157f * s, 0.161f * s, -0.215f * s, 0.161f * s, -0.286f * s); cubicTo(0.161f * s, -0.357f * s, 0.219f * s, -0.415f * s, 0.291f * s, -0.415f * s); cubicTo(0.362f * s, -0.415f * s, 0.42f * s, -0.357f * s, 0.42f * s, -0.286f * s); close() }
    val p4 = Path().apply { moveTo(0.415f * s, 0.037f * s); cubicTo(0.415f * s, 0.23f * s, 0.229f * s, 0.387f * s, 0.0f * s, 0.387f * s); cubicTo(-0.229f * s, 0.387f * s, -0.415f * s, 0.23f * s, -0.415f * s, 0.037f * s); cubicTo(-0.415f * s, -0.157f * s, -0.229f * s, -0.313f * s, 0.0f * s, -0.313f * s); cubicTo(0.229f * s, -0.313f * s, 0.415f * s, -0.157f * s, 0.415f * s, 0.037f * s); close() }
    val p5 = Path().apply { moveTo(0.397f * s, -0.009f * s); cubicTo(0.397f * s, 0.184f * s, 0.211f * s, 0.341f * s, -0.018f * s, 0.341f * s); cubicTo(-0.248f * s, 0.341f * s, -0.433f * s, 0.184f * s, -0.433f * s, -0.009f * s); cubicTo(-0.433f * s, -0.203f * s, -0.248f * s, -0.36f * s, -0.018f * s, -0.36f * s); cubicTo(0.211f * s, -0.36f * s, 0.397f * s, -0.203f * s, 0.397f * s, -0.009f * s); close() }
    val p6 = Path().apply { moveTo(-0.092f * s, -0.048f * s); cubicTo(-0.061f * s, 0.016f * s, -0.074f * s, 0.086f * s, -0.121f * s, 0.108f * s); cubicTo(-0.167f * s, 0.129f * s, -0.229f * s, 0.094f * s, -0.259f * s, 0.03f * s); cubicTo(-0.289f * s, -0.035f * s, -0.276f * s, -0.105f * s, -0.23f * s, -0.126f * s); cubicTo(-0.184f * s, -0.148f * s, -0.122f * s, -0.113f * s, -0.092f * s, -0.048f * s); close() }
    val p7 = Path().apply { moveTo(0.259f * s, 0.03f * s); cubicTo(0.229f * s, 0.094f * s, 0.167f * s, 0.129f * s, 0.121f * s, 0.108f * s); cubicTo(0.075f * s, 0.086f * s, 0.062f * s, 0.016f * s, 0.092f * s, -0.048f * s); cubicTo(0.122f * s, -0.113f * s, 0.184f * s, -0.148f * s, 0.23f * s, -0.126f * s); cubicTo(0.276f * s, -0.105f * s, 0.289f * s, -0.035f * s, 0.259f * s, 0.03f * s); close() }
    val p8 = Path().apply { moveTo(-0.201f * s, -0.203f * s); cubicTo(-0.192f * s, -0.187f * s, -0.206f * s, -0.163f * s, -0.233f * s, -0.147f * s); cubicTo(-0.259f * s, -0.132f * s, -0.288f * s, -0.132f * s, -0.297f * s, -0.147f * s); cubicTo(-0.306f * s, -0.163f * s, -0.292f * s, -0.188f * s, -0.265f * s, -0.203f * s); cubicTo(-0.239f * s, -0.218f * s, -0.21f * s, -0.218f * s, -0.201f * s, -0.203f * s); close() }
    val p9 = Path().apply { moveTo(-0.042f * s, 0.108f * s); quadraticTo(-0.069f * s, 0.083f * s, -0.032f * s, 0.083f * s); lineTo(0.032f * s, 0.083f * s); quadraticTo(0.069f * s, 0.083f * s, 0.042f * s, 0.108f * s); lineTo(0.027f * s, 0.122f * s); quadraticTo(0.0f * s, 0.148f * s, -0.027f * s, 0.122f * s); close() }
    val p10 = Path().apply { moveTo(0.0f * s, 0.148f * s); lineTo(0.0f * s, 0.184f * s) }
    val p11 = Path().apply { moveTo(0.0f * s, 0.184f * s); quadraticTo(-0.046f * s, 0.258f * s, -0.111f * s, 0.212f * s) }
    val p12 = Path().apply { moveTo(0.0f * s, 0.184f * s); quadraticTo(0.046f * s, 0.258f * s, 0.111f * s, 0.212f * s) }
    drawPath(p0, sil ?: Color(0xFF242736))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF34374D))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF242736))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF34374D))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFFDDDCD6))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFFF6F4EE))
        }
        drawPath(p6, Color(0xFF34374D))
        drawPath(p7, Color(0xFF34374D))
        drawPath(p8, Color(0x8CFFFFFF))
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.048f * s, Offset(-0.175f * s, -0.018f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.048f * s, Offset(-0.175f * s, -0.018f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.028f * s, Offset(-0.168f * s, -0.009f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(-0.179f * s, -0.021f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.048f * s, Offset(0.175f * s, -0.018f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.048f * s, Offset(0.175f * s, -0.018f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.028f * s, Offset(0.168f * s, -0.009f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(0.157f * s, -0.021f * s))
    }
    drawPath(p9, sil ?: Color(0xFF34374D))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.024f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p11, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p12, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(-0.346f * s, 0.12f * s), Size(0.083f * s, 0.055f * s))
        drawOval(Color(0x66FF4F86), Offset(0.263f * s, 0.12f * s), Size(0.083f * s, 0.055f * s))
    }
}

private fun DrawScope.artPingouin(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.021f * s, 0.371f * s); cubicTo(-0.021f * s, 0.394f * s, -0.07f * s, 0.414f * s, -0.129f * s, 0.414f * s); cubicTo(-0.189f * s, 0.414f * s, -0.237f * s, 0.394f * s, -0.237f * s, 0.371f * s); cubicTo(-0.237f * s, 0.347f * s, -0.189f * s, 0.327f * s, -0.129f * s, 0.327f * s); cubicTo(-0.07f * s, 0.327f * s, -0.021f * s, 0.347f * s, -0.021f * s, 0.371f * s); close() }
    val p1 = Path().apply { moveTo(0.237f * s, 0.371f * s); cubicTo(0.237f * s, 0.394f * s, 0.189f * s, 0.414f * s, 0.129f * s, 0.414f * s); cubicTo(0.07f * s, 0.414f * s, 0.022f * s, 0.394f * s, 0.022f * s, 0.371f * s); cubicTo(0.022f * s, 0.347f * s, 0.07f * s, 0.327f * s, 0.129f * s, 0.327f * s); cubicTo(0.189f * s, 0.327f * s, 0.237f * s, 0.347f * s, 0.237f * s, 0.371f * s); close() }
    val p2 = Path().apply { moveTo(-0.291f * s, 0.052f * s); cubicTo(-0.32f * s, 0.153f * s, -0.371f * s, 0.227f * s, -0.405f * s, 0.217f * s); cubicTo(-0.44f * s, 0.207f * s, -0.444f * s, 0.117f * s, -0.415f * s, 0.017f * s); cubicTo(-0.387f * s, -0.084f * s, -0.335f * s, -0.158f * s, -0.301f * s, -0.148f * s); cubicTo(-0.267f * s, -0.138f * s, -0.262f * s, -0.048f * s, -0.291f * s, 0.052f * s); close() }
    val p3 = Path().apply { moveTo(-0.304f * s, 0.027f * s); cubicTo(-0.333f * s, 0.127f * s, -0.384f * s, 0.201f * s, -0.418f * s, 0.191f * s); cubicTo(-0.453f * s, 0.181f * s, -0.457f * s, 0.091f * s, -0.428f * s, -0.009f * s); cubicTo(-0.399f * s, -0.11f * s, -0.348f * s, -0.183f * s, -0.314f * s, -0.174f * s); cubicTo(-0.28f * s, -0.164f * s, -0.275f * s, -0.074f * s, -0.304f * s, 0.027f * s); close() }
    val p4 = Path().apply { moveTo(0.415f * s, 0.017f * s); cubicTo(0.444f * s, 0.117f * s, 0.44f * s, 0.207f * s, 0.406f * s, 0.217f * s); cubicTo(0.371f * s, 0.227f * s, 0.32f * s, 0.153f * s, 0.291f * s, 0.052f * s); cubicTo(0.262f * s, -0.048f * s, 0.267f * s, -0.138f * s, 0.301f * s, -0.148f * s); cubicTo(0.335f * s, -0.158f * s, 0.387f * s, -0.084f * s, 0.415f * s, 0.017f * s); close() }
    val p5 = Path().apply { moveTo(0.403f * s, -0.009f * s); cubicTo(0.431f * s, 0.091f * s, 0.427f * s, 0.181f * s, 0.393f * s, 0.191f * s); cubicTo(0.358f * s, 0.201f * s, 0.307f * s, 0.127f * s, 0.278f * s, 0.027f * s); cubicTo(0.249f * s, -0.074f * s, 0.254f * s, -0.164f * s, 0.288f * s, -0.174f * s); cubicTo(0.322f * s, -0.183f * s, 0.374f * s, -0.11f * s, 0.403f * s, -0.009f * s); close() }
    val p6 = Path().apply { moveTo(0.0f * s, -0.414f * s); cubicTo(0.172f * s, -0.414f * s, 0.293f * s, -0.302f * s, 0.31f * s, -0.112f * s); cubicTo(0.345f * s, 0.078f * s, 0.336f * s, 0.233f * s, 0.25f * s, 0.319f * s); cubicTo(0.172f * s, 0.379f * s, -0.172f * s, 0.379f * s, -0.25f * s, 0.319f * s); cubicTo(-0.336f * s, 0.233f * s, -0.345f * s, 0.078f * s, -0.31f * s, -0.112f * s); cubicTo(-0.293f * s, -0.302f * s, -0.172f * s, -0.414f * s, 0.0f * s, -0.414f * s); close() }
    val p7 = Path().apply { moveTo(-0.021f * s, -0.457f * s); cubicTo(0.151f * s, -0.457f * s, 0.271f * s, -0.345f * s, 0.289f * s, -0.155f * s); cubicTo(0.323f * s, 0.035f * s, 0.315f * s, 0.19f * s, 0.228f * s, 0.276f * s); cubicTo(0.151f * s, 0.336f * s, -0.194f * s, 0.336f * s, -0.271f * s, 0.276f * s); cubicTo(-0.358f * s, 0.19f * s, -0.366f * s, 0.035f * s, -0.332f * s, -0.155f * s); cubicTo(-0.314f * s, -0.345f * s, -0.194f * s, -0.457f * s, -0.021f * s, -0.457f * s); close() }
    val p8 = Path().apply { moveTo(-0.135f * s, -0.332f * s); cubicTo(-0.126f * s, -0.315f * s, -0.134f * s, -0.292f * s, -0.155f * s, -0.28f * s); cubicTo(-0.176f * s, -0.268f * s, -0.2f * s, -0.272f * s, -0.21f * s, -0.289f * s); cubicTo(-0.219f * s, -0.305f * s, -0.21f * s, -0.328f * s, -0.19f * s, -0.34f * s); cubicTo(-0.169f * s, -0.352f * s, -0.144f * s, -0.348f * s, -0.135f * s, -0.332f * s); close() }
    val p9 = Path().apply { moveTo(0.0f * s, -0.086f * s); cubicTo(-0.023f * s, -0.086f * s, -0.043f * s, -0.149f * s, -0.069f * s, -0.164f * s); cubicTo(-0.095f * s, -0.178f * s, -0.129f * s, -0.182f * s, -0.155f * s, -0.172f * s); cubicTo(-0.181f * s, -0.162f * s, -0.211f * s, -0.135f * s, -0.224f * s, -0.103f * s); cubicTo(-0.237f * s, -0.072f * s, -0.234f * s, -0.027f * s, -0.233f * s, 0.017f * s); cubicTo(-0.231f * s, 0.062f * s, -0.233f * s, 0.119f * s, -0.215f * s, 0.164f * s); cubicTo(-0.198f * s, 0.208f * s, -0.165f * s, 0.259f * s, -0.129f * s, 0.284f * s); cubicTo(-0.093f * s, 0.31f * s, -0.043f * s, 0.319f * s, 0.0f * s, 0.319f * s); cubicTo(0.043f * s, 0.319f * s, 0.093f * s, 0.31f * s, 0.129f * s, 0.284f * s); cubicTo(0.165f * s, 0.259f * s, 0.198f * s, 0.208f * s, 0.215f * s, 0.164f * s); cubicTo(0.233f * s, 0.119f * s, 0.231f * s, 0.062f * s, 0.233f * s, 0.017f * s); cubicTo(0.234f * s, -0.027f * s, 0.237f * s, -0.072f * s, 0.224f * s, -0.103f * s); cubicTo(0.211f * s, -0.135f * s, 0.181f * s, -0.162f * s, 0.155f * s, -0.172f * s); cubicTo(0.129f * s, -0.182f * s, 0.095f * s, -0.178f * s, 0.069f * s, -0.164f * s); cubicTo(0.043f * s, -0.149f * s, 0.023f * s, -0.086f * s, 0.0f * s, -0.086f * s); close() }
    val p10 = Path().apply { moveTo(-0.057f * s, -0.023f * s); quadraticTo(-0.073f * s, -0.043f * s, -0.047f * s, -0.043f * s); lineTo(0.047f * s, -0.043f * s); quadraticTo(0.073f * s, -0.043f * s, 0.058f * s, -0.023f * s); lineTo(0.016f * s, 0.031f * s); quadraticTo(0.0f * s, 0.052f * s, -0.016f * s, 0.031f * s); close() }
    drawPath(p0, sil ?: Color(0xFFFF8A1F))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFFF8A1F))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF242739))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF343852))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF242739))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFF343852))
        }
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF242739))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFF343852))
        }
        drawPath(p8, Color(0x59FFFFFF))
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFF6F4EE))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.052f * s, Offset(-0.112f * s, -0.103f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.052f * s, Offset(-0.112f * s, -0.103f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.03f * s, Offset(-0.104f * s, -0.093f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(-0.116f * s, -0.107f * s))
    }
    drawCircle(sil ?: Color(0xFFFFFFFF), 0.052f * s, Offset(0.112f * s, -0.103f * s))
    drawCircle(sil ?: Color(0xFF2B2D42), 0.052f * s, Offset(0.112f * s, -0.103f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.03f * s, Offset(0.104f * s, -0.093f * s))
        drawCircle(Color(0xFFFFFFFF), 0.01f * s, Offset(0.093f * s, -0.107f * s))
    }
    drawPath(p10, sil ?: Color(0xFFFF8A1F))
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawOval(Color(0x66FF4F86), Offset(-0.215f * s, -0.016f * s), Size(0.069f * s, 0.048f * s))
        drawOval(Color(0x66FF4F86), Offset(0.146f * s, -0.016f * s), Size(0.069f * s, 0.048f * s))
    }
}

private fun DrawScope.artFusee(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.092f * s, 0.161f * s); cubicTo(-0.145f * s, 0.253f * s, -0.061f * s, 0.329f * s, 0.0f * s, 0.436f * s); cubicTo(0.061f * s, 0.329f * s, 0.145f * s, 0.253f * s, 0.092f * s, 0.161f * s); close() }
    val p1 = Path().apply { moveTo(-0.046f * s, 0.161f * s); cubicTo(-0.076f * s, 0.237f * s, -0.023f * s, 0.283f * s, 0.0f * s, 0.344f * s); cubicTo(0.023f * s, 0.283f * s, 0.077f * s, 0.237f * s, 0.046f * s, 0.161f * s); close() }
    val p2 = Path().apply { moveTo(-0.153f * s, -0.073f * s); quadraticTo(-0.153f * s, -0.099f * s, -0.168f * s, -0.077f * s); lineTo(-0.329f * s, 0.162f * s); quadraticTo(-0.344f * s, 0.184f * s, -0.344f * s, 0.211f * s); lineTo(-0.344f * s, 0.226f * s); quadraticTo(-0.344f * s, 0.253f * s, -0.323f * s, 0.236f * s); lineTo(-0.174f * s, 0.116f * s); quadraticTo(-0.153f * s, 0.1f * s, -0.153f * s, 0.073f * s); close() }
    val p3 = Path().apply { moveTo(0.153f * s, -0.073f * s); quadraticTo(0.153f * s, -0.099f * s, 0.168f * s, -0.077f * s); lineTo(0.329f * s, 0.162f * s); quadraticTo(0.344f * s, 0.184f * s, 0.344f * s, 0.211f * s); lineTo(0.344f * s, 0.226f * s); quadraticTo(0.344f * s, 0.253f * s, 0.324f * s, 0.236f * s); lineTo(0.174f * s, 0.116f * s); quadraticTo(0.153f * s, 0.1f * s, 0.153f * s, 0.073f * s); close() }
    val p4 = Path().apply { moveTo(-0.095f * s, 0.103f * s); quadraticTo(-0.099f * s, 0.084f * s, -0.08f * s, 0.084f * s); lineTo(0.08f * s, 0.084f * s); quadraticTo(0.1f * s, 0.084f * s, 0.095f * s, 0.103f * s); lineTo(0.081f * s, 0.157f * s); quadraticTo(0.077f * s, 0.176f * s, 0.057f * s, 0.176f * s); lineTo(-0.057f * s, 0.176f * s); quadraticTo(-0.076f * s, 0.176f * s, -0.081f * s, 0.157f * s); close() }
    val p5 = Path().apply { moveTo(0.0f * s, -0.436f * s); cubicTo(0.115f * s, -0.375f * s, 0.176f * s, -0.222f * s, 0.176f * s, -0.054f * s); lineTo(0.176f * s, 0.1f * s); lineTo(-0.176f * s, 0.1f * s); lineTo(-0.176f * s, -0.054f * s); cubicTo(-0.176f * s, -0.222f * s, -0.115f * s, -0.375f * s, 0.0f * s, -0.436f * s); close() }
    val p6 = Path().apply { moveTo(-0.019f * s, -0.471f * s); cubicTo(0.096f * s, -0.409f * s, 0.157f * s, -0.256f * s, 0.157f * s, -0.088f * s); lineTo(0.157f * s, 0.065f * s); lineTo(-0.195f * s, 0.065f * s); lineTo(-0.195f * s, -0.088f * s); cubicTo(-0.195f * s, -0.256f * s, -0.134f * s, -0.409f * s, -0.019f * s, -0.471f * s); close() }
    val p7 = Path().apply { moveTo(-0.23f * s, -0.49f * s); lineTo(0.23f * s, -0.49f * s); lineTo(0.23f * s, -0.23f * s); quadraticTo(0.0f * s, -0.184f * s, -0.23f * s, -0.23f * s); close() }
    val p8 = Path().apply { moveTo(0.088f * s, -0.099f * s); cubicTo(0.088f * s, -0.051f * s, 0.049f * s, -0.011f * s, 0.0f * s, -0.011f * s); cubicTo(-0.049f * s, -0.011f * s, -0.088f * s, -0.051f * s, -0.088f * s, -0.099f * s); cubicTo(-0.088f * s, -0.148f * s, -0.049f * s, -0.187f * s, 0.0f * s, -0.187f * s); cubicTo(0.049f * s, -0.187f * s, 0.088f * s, -0.148f * s, 0.088f * s, -0.099f * s); close() }
    val p9 = Path().apply { moveTo(0.063f * s, -0.099f * s); cubicTo(0.063f * s, -0.065f * s, 0.035f * s, -0.037f * s, 0.0f * s, -0.037f * s); cubicTo(-0.035f * s, -0.037f * s, -0.063f * s, -0.065f * s, -0.063f * s, -0.099f * s); cubicTo(-0.063f * s, -0.134f * s, -0.035f * s, -0.162f * s, 0.0f * s, -0.162f * s); cubicTo(0.035f * s, -0.162f * s, 0.063f * s, -0.134f * s, 0.063f * s, -0.099f * s); close() }
    val p10 = Path().apply { moveTo(-0.008f * s, -0.131f * s); cubicTo(-0.002f * s, -0.12f * s, -0.003f * s, -0.107f * s, -0.011f * s, -0.103f * s); cubicTo(-0.019f * s, -0.098f * s, -0.031f * s, -0.103f * s, -0.038f * s, -0.114f * s); cubicTo(-0.044f * s, -0.125f * s, -0.042f * s, -0.138f * s, -0.034f * s, -0.142f * s); cubicTo(-0.026f * s, -0.147f * s, -0.015f * s, -0.142f * s, -0.008f * s, -0.131f * s); close() }
    drawPath(p0, sil ?: Color(0xFFFF8A1F))
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p1, Color(0xFFFFD23F))
    }
    drawPath(p2, sil ?: Color(0xFF7A8299))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF7A8299))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF59627C))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFFD4D2CD))
    if (sil == null) {
        clipPath(p5) {
            drawPath(p6, Color(0xFFF6F4EE))
        }
        clipPath(p5) {
            drawPath(p7, Color(0xFF7A8299))
            drawRoundRect(Color(0xFF7A8299), Offset(-0.23f * s, 0.008f * s), Size(0.459f * s, 0.038f * s), CornerRadius(0.0f * s))
        }
        drawOval(Color(0x8CFFFFFF), Offset(-0.126f * s, -0.145f * s), Size(0.038f * s, 0.153f * s))
    }
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF8A94AD))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFBFE3FF))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawPath(p10, Color(0xB3FFFFFF))
    }
}

private fun DrawScope.artAmpoule(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.115f * s, 0.204f * s); cubicTo(-0.115f * s, 0.097f * s, -0.31f * s, 0.044f * s, -0.31f * s, -0.151f * s); cubicTo(-0.31f * s, -0.319f * s, -0.168f * s, -0.434f * s, 0.0f * s, -0.434f * s); cubicTo(0.168f * s, -0.434f * s, 0.31f * s, -0.319f * s, 0.31f * s, -0.151f * s); cubicTo(0.31f * s, 0.044f * s, 0.115f * s, 0.097f * s, 0.115f * s, 0.204f * s); close() }
    val p1 = Path().apply { moveTo(-0.137f * s, 0.159f * s); cubicTo(-0.137f * s, 0.053f * s, -0.332f * s, 0.0f * s, -0.332f * s, -0.195f * s); cubicTo(-0.332f * s, -0.363f * s, -0.19f * s, -0.478f * s, -0.022f * s, -0.478f * s); cubicTo(0.146f * s, -0.478f * s, 0.288f * s, -0.363f * s, 0.288f * s, -0.195f * s); cubicTo(0.288f * s, 0.0f * s, 0.093f * s, 0.053f * s, 0.093f * s, 0.159f * s); close() }
    val p2 = Path().apply { moveTo(-0.139f * s, -0.235f * s); cubicTo(-0.163f * s, -0.192f * s, -0.2f * s, -0.168f * s, -0.221f * s, -0.18f * s); cubicTo(-0.243f * s, -0.192f * s, -0.24f * s, -0.237f * s, -0.215f * s, -0.279f * s); cubicTo(-0.191f * s, -0.321f * s, -0.154f * s, -0.346f * s, -0.133f * s, -0.334f * s); cubicTo(-0.112f * s, -0.321f * s, -0.114f * s, -0.277f * s, -0.139f * s, -0.235f * s); close() }
    val p3 = Path().apply { moveTo(-0.062f * s, 0.186f * s); lineTo(-0.062f * s, 0.027f * s); quadraticTo(-0.097f * s, -0.115f * s, 0.0f * s, -0.115f * s); quadraticTo(0.097f * s, -0.115f * s, 0.062f * s, 0.027f * s); lineTo(0.062f * s, 0.186f * s) }
    val p4 = Path().apply { moveTo(-0.106f * s, 0.186f * s); lineTo(0.106f * s, 0.186f * s); cubicTo(0.121f * s, 0.186f * s, 0.133f * s, 0.198f * s, 0.133f * s, 0.213f * s); lineTo(0.133f * s, 0.239f * s); cubicTo(0.133f * s, 0.254f * s, 0.121f * s, 0.266f * s, 0.106f * s, 0.266f * s); lineTo(-0.106f * s, 0.266f * s); cubicTo(-0.121f * s, 0.266f * s, -0.133f * s, 0.254f * s, -0.133f * s, 0.239f * s); lineTo(-0.133f * s, 0.213f * s); cubicTo(-0.133f * s, 0.198f * s, -0.121f * s, 0.186f * s, -0.106f * s, 0.186f * s); close() }
    val p5 = Path().apply { moveTo(-0.12f * s, 0.164f * s); lineTo(0.093f * s, 0.164f * s); cubicTo(0.108f * s, 0.164f * s, 0.12f * s, 0.176f * s, 0.12f * s, 0.191f * s); lineTo(0.12f * s, 0.217f * s); cubicTo(0.12f * s, 0.232f * s, 0.108f * s, 0.244f * s, 0.093f * s, 0.244f * s); lineTo(-0.12f * s, 0.244f * s); cubicTo(-0.134f * s, 0.244f * s, -0.146f * s, 0.232f * s, -0.146f * s, 0.217f * s); lineTo(-0.146f * s, 0.191f * s); cubicTo(-0.146f * s, 0.176f * s, -0.134f * s, 0.164f * s, -0.12f * s, 0.164f * s); close() }
    val p6 = Path().apply { moveTo(-0.106f * s, 0.257f * s); lineTo(0.106f * s, 0.257f * s); cubicTo(0.121f * s, 0.257f * s, 0.133f * s, 0.269f * s, 0.133f * s, 0.284f * s); lineTo(0.133f * s, 0.31f * s); cubicTo(0.133f * s, 0.325f * s, 0.121f * s, 0.337f * s, 0.106f * s, 0.337f * s); lineTo(-0.106f * s, 0.337f * s); cubicTo(-0.121f * s, 0.337f * s, -0.133f * s, 0.325f * s, -0.133f * s, 0.31f * s); lineTo(-0.133f * s, 0.284f * s); cubicTo(-0.133f * s, 0.269f * s, -0.121f * s, 0.257f * s, -0.106f * s, 0.257f * s); close() }
    val p7 = Path().apply { moveTo(-0.12f * s, 0.235f * s); lineTo(0.093f * s, 0.235f * s); cubicTo(0.108f * s, 0.235f * s, 0.12f * s, 0.247f * s, 0.12f * s, 0.261f * s); lineTo(0.12f * s, 0.288f * s); cubicTo(0.12f * s, 0.303f * s, 0.108f * s, 0.315f * s, 0.093f * s, 0.315f * s); lineTo(-0.12f * s, 0.315f * s); cubicTo(-0.134f * s, 0.315f * s, -0.146f * s, 0.303f * s, -0.146f * s, 0.288f * s); lineTo(-0.146f * s, 0.261f * s); cubicTo(-0.146f * s, 0.247f * s, -0.134f * s, 0.235f * s, -0.12f * s, 0.235f * s); close() }
    val p8 = Path().apply { moveTo(-0.115f * s, 0.328f * s); lineTo(0.115f * s, 0.328f * s); quadraticTo(0.097f * s, 0.434f * s, 0.0f * s, 0.434f * s); quadraticTo(-0.097f * s, 0.434f * s, -0.115f * s, 0.328f * s); close() }
    val p9 = Path().apply { moveTo(-0.124f * s, 0.31f * s); lineTo(0.106f * s, 0.31f * s); quadraticTo(0.089f * s, 0.416f * s, -0.009f * s, 0.416f * s); quadraticTo(-0.106f * s, 0.416f * s, -0.124f * s, 0.31f * s); close() }
    drawPath(p0, sil ?: Color(0xFFE6DDBA))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFFFFF6CF))
        }
        drawPath(p2, Color(0x8CFFFFFF))
        drawOval(Color(0x8CFFFFFF), Offset(-0.248f * s, -0.115f * s), Size(0.035f * s, 0.035f * s))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFFF9A1F), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF7B8292))
    if (sil == null) {
        clipPath(p4) {
            drawPath(p5, Color(0xFF9AA3B7))
        }
    }
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF9298A5))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFFB6BECE))
        }
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF474E63))
    if (sil == null) {
        clipPath(p8) {
            drawPath(p9, Color(0xFF59627C))
        }
    }
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.artHorloge(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.124f * s, -0.406f * s); cubicTo(-0.092f * s, -0.365f * s, -0.114f * s, -0.293f * s, -0.175f * s, -0.246f * s); cubicTo(-0.235f * s, -0.199f * s, -0.31f * s, -0.195f * s, -0.342f * s, -0.236f * s); cubicTo(-0.374f * s, -0.277f * s, -0.352f * s, -0.349f * s, -0.292f * s, -0.396f * s); cubicTo(-0.231f * s, -0.443f * s, -0.157f * s, -0.448f * s, -0.124f * s, -0.406f * s); close() }
    val p1 = Path().apply { moveTo(-0.137f * s, -0.432f * s); cubicTo(-0.105f * s, -0.391f * s, -0.127f * s, -0.319f * s, -0.188f * s, -0.272f * s); cubicTo(-0.248f * s, -0.225f * s, -0.323f * s, -0.221f * s, -0.355f * s, -0.262f * s); cubicTo(-0.387f * s, -0.303f * s, -0.365f * s, -0.375f * s, -0.305f * s, -0.422f * s); cubicTo(-0.244f * s, -0.469f * s, -0.169f * s, -0.473f * s, -0.137f * s, -0.432f * s); close() }
    val p2 = Path().apply { moveTo(0.342f * s, -0.236f * s); cubicTo(0.31f * s, -0.195f * s, 0.235f * s, -0.199f * s, 0.175f * s, -0.246f * s); cubicTo(0.115f * s, -0.293f * s, 0.092f * s, -0.365f * s, 0.124f * s, -0.406f * s); cubicTo(0.157f * s, -0.448f * s, 0.232f * s, -0.443f * s, 0.292f * s, -0.396f * s); cubicTo(0.352f * s, -0.349f * s, 0.374f * s, -0.277f * s, 0.342f * s, -0.236f * s); close() }
    val p3 = Path().apply { moveTo(0.329f * s, -0.262f * s); cubicTo(0.297f * s, -0.221f * s, 0.222f * s, -0.225f * s, 0.162f * s, -0.272f * s); cubicTo(0.102f * s, -0.319f * s, 0.079f * s, -0.391f * s, 0.111f * s, -0.432f * s); cubicTo(0.144f * s, -0.473f * s, 0.219f * s, -0.469f * s, 0.279f * s, -0.422f * s); cubicTo(0.339f * s, -0.375f * s, 0.361f * s, -0.303f * s, 0.329f * s, -0.262f * s); close() }
    val p4 = Path().apply { moveTo(-0.25f * s, 0.419f * s); cubicTo(-0.267f * s, 0.438f * s, -0.297f * s, 0.44f * s, -0.317f * s, 0.422f * s); cubicTo(-0.336f * s, 0.405f * s, -0.338f * s, 0.375f * s, -0.32f * s, 0.355f * s); lineTo(-0.243f * s, 0.269f * s); cubicTo(-0.225f * s, 0.249f * s, -0.195f * s, 0.248f * s, -0.175f * s, 0.265f * s); cubicTo(-0.156f * s, 0.283f * s, -0.154f * s, 0.313f * s, -0.172f * s, 0.332f * s); close() }
    val p5 = Path().apply { moveTo(0.32f * s, 0.355f * s); cubicTo(0.338f * s, 0.375f * s, 0.336f * s, 0.405f * s, 0.317f * s, 0.422f * s); cubicTo(0.297f * s, 0.44f * s, 0.267f * s, 0.438f * s, 0.25f * s, 0.419f * s); lineTo(0.172f * s, 0.332f * s); cubicTo(0.154f * s, 0.313f * s, 0.156f * s, 0.283f * s, 0.176f * s, 0.265f * s); cubicTo(0.195f * s, 0.248f * s, 0.225f * s, 0.249f * s, 0.243f * s, 0.269f * s); close() }
    val p6 = Path().apply { moveTo(0.354f * s, -0.027f * s); cubicTo(0.354f * s, 0.168f * s, 0.196f * s, 0.327f * s, 0.0f * s, 0.327f * s); cubicTo(-0.195f * s, 0.327f * s, -0.354f * s, 0.168f * s, -0.354f * s, -0.027f * s); cubicTo(-0.354f * s, -0.223f * s, -0.195f * s, -0.382f * s, 0.0f * s, -0.382f * s); cubicTo(0.196f * s, -0.382f * s, 0.354f * s, -0.223f * s, 0.354f * s, -0.027f * s); close() }
    val p7 = Path().apply { moveTo(0.333f * s, -0.071f * s); cubicTo(0.333f * s, 0.125f * s, 0.174f * s, 0.283f * s, -0.022f * s, 0.283f * s); cubicTo(-0.217f * s, 0.283f * s, -0.376f * s, 0.125f * s, -0.376f * s, -0.071f * s); cubicTo(-0.376f * s, -0.266f * s, -0.217f * s, -0.425f * s, -0.022f * s, -0.425f * s); cubicTo(0.174f * s, -0.425f * s, 0.333f * s, -0.266f * s, 0.333f * s, -0.071f * s); close() }
    val p8 = Path().apply { moveTo(-0.209f * s, -0.242f * s); cubicTo(-0.236f * s, -0.209f * s, -0.273f * s, -0.194f * s, -0.292f * s, -0.21f * s); cubicTo(-0.31f * s, -0.225f * s, -0.302f * s, -0.264f * s, -0.275f * s, -0.297f * s); cubicTo(-0.247f * s, -0.33f * s, -0.21f * s, -0.344f * s, -0.192f * s, -0.329f * s); cubicTo(-0.174f * s, -0.314f * s, -0.181f * s, -0.274f * s, -0.209f * s, -0.242f * s); close() }
    val p9 = Path().apply { moveTo(0.259f * s, -0.027f * s); cubicTo(0.259f * s, 0.116f * s, 0.143f * s, 0.232f * s, 0.0f * s, 0.232f * s); cubicTo(-0.143f * s, 0.232f * s, -0.259f * s, 0.116f * s, -0.259f * s, -0.027f * s); cubicTo(-0.259f * s, -0.171f * s, -0.143f * s, -0.287f * s, 0.0f * s, -0.287f * s); cubicTo(0.143f * s, -0.287f * s, 0.259f * s, -0.171f * s, 0.259f * s, -0.027f * s); close() }
    drawPath(p0, sil ?: Color(0xFF666C7A))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF808799))
        }
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF666C7A))
    if (sil == null) {
        clipPath(p2) {
            drawPath(p3, Color(0xFF808799))
        }
    }
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF808799))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF808799))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF808693))
    if (sil == null) {
        clipPath(p6) {
            drawPath(p7, Color(0xFFA0A7B8))
        }
        drawPath(p8, Color(0x8CFFFFFF))
    }
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFFFFFFFF))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.026f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.0f * s, -0.2f * s), Offset(0.0f * s, -0.243f * s), 0.035f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.173f * s, -0.028f * s), Offset(0.216f * s, -0.028f * s), 0.035f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.0f * s, 0.145f * s), Offset(0.0f * s, 0.188f * s), 0.035f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.173f * s, -0.028f * s), Offset(-0.216f * s, -0.028f * s), 0.035f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.0f * s, -0.028f * s), Offset(-0.112f * s, -0.092f * s), 0.045f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.0f * s, -0.028f * s), Offset(0.165f * s, -0.123f * s), 0.036f * s, StrokeCap.Round)
    if (sil == null) {
        drawCircle(Color(0xFF2B2D42), 0.031f * s, Offset(0.0f * s, -0.028f * s))
    }
}

private fun DrawScope.artCle(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.088f * s, -0.114f * s); cubicTo(-0.143f * s, -0.059f * s, -0.233f * s, -0.059f * s, -0.288f * s, -0.114f * s); cubicTo(-0.343f * s, -0.169f * s, -0.343f * s, -0.258f * s, -0.288f * s, -0.314f * s); cubicTo(-0.233f * s, -0.369f * s, -0.143f * s, -0.369f * s, -0.088f * s, -0.314f * s); cubicTo(-0.033f * s, -0.258f * s, -0.033f * s, -0.169f * s, -0.088f * s, -0.114f * s); close() }
    val p1 = Path().apply { moveTo(-0.055f * s, -0.081f * s); lineTo(0.329f * s, 0.303f * s) }
    val p2 = Path().apply { moveTo(0.159f * s, 0.163f * s); lineTo(0.048f * s, 0.274f * s) }
    val p3 = Path().apply { moveTo(0.27f * s, 0.274f * s); lineTo(0.189f * s, 0.355f * s) }
    val p4 = Path().apply { moveTo(-0.188f * s, -0.335f * s); cubicTo(-0.211f * s, -0.335f * s, -0.23f * s, -0.344f * s, -0.23f * s, -0.354f * s); cubicTo(-0.23f * s, -0.365f * s, -0.211f * s, -0.373f * s, -0.188f * s, -0.373f * s); cubicTo(-0.165f * s, -0.373f * s, -0.146f * s, -0.365f * s, -0.146f * s, -0.354f * s); cubicTo(-0.146f * s, -0.344f * s, -0.165f * s, -0.335f * s, -0.188f * s, -0.335f * s); close() }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.182f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFF2B2D42), style = Stroke(0.182f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.182f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.182f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p0, sil ?: Color(0xFFAEB7C9), style = Stroke(0.11f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p1, sil ?: Color(0xFFAEB7C9), style = Stroke(0.11f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFAEB7C9), style = Stroke(0.11f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p3, sil ?: Color(0xFFAEB7C9), style = Stroke(0.11f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    if (sil == null) {
        drawLine(Color(0x8CFFFFFF), Offset(-0.018f * s, -0.088f * s), Offset(0.329f * s, 0.259f * s), 0.024f * s, StrokeCap.Round)
        drawPath(p4, Color(0x99FFFFFF))
    }
}

private fun DrawScope.artPiano(s: Float, sil: Color?) {
    val p0 = Path().apply { moveTo(-0.352f * s, -0.434f * s); lineTo(0.352f * s, -0.434f * s); cubicTo(0.387f * s, -0.434f * s, 0.416f * s, -0.405f * s, 0.416f * s, -0.37f * s); lineTo(0.416f * s, -0.172f * s); cubicTo(0.416f * s, -0.137f * s, 0.387f * s, -0.108f * s, 0.352f * s, -0.108f * s); lineTo(-0.352f * s, -0.108f * s); cubicTo(-0.387f * s, -0.108f * s, -0.416f * s, -0.137f * s, -0.416f * s, -0.172f * s); lineTo(-0.416f * s, -0.37f * s); cubicTo(-0.416f * s, -0.405f * s, -0.387f * s, -0.434f * s, -0.352f * s, -0.434f * s); close() }
    val p1 = Path().apply { moveTo(-0.37f * s, -0.47f * s); lineTo(0.334f * s, -0.47f * s); cubicTo(0.369f * s, -0.47f * s, 0.398f * s, -0.442f * s, 0.398f * s, -0.407f * s); lineTo(0.398f * s, -0.208f * s); cubicTo(0.398f * s, -0.173f * s, 0.369f * s, -0.145f * s, 0.334f * s, -0.145f * s); lineTo(-0.37f * s, -0.145f * s); cubicTo(-0.405f * s, -0.145f * s, -0.434f * s, -0.173f * s, -0.434f * s, -0.208f * s); lineTo(-0.434f * s, -0.407f * s); cubicTo(-0.434f * s, -0.442f * s, -0.405f * s, -0.47f * s, -0.37f * s, -0.47f * s); close() }
    val p2 = Path().apply { moveTo(-0.352f * s, -0.126f * s); lineTo(0.352f * s, -0.126f * s); cubicTo(0.367f * s, -0.126f * s, 0.38f * s, -0.114f * s, 0.38f * s, -0.099f * s); lineTo(0.38f * s, 0.262f * s); cubicTo(0.38f * s, 0.277f * s, 0.367f * s, 0.289f * s, 0.352f * s, 0.289f * s); lineTo(-0.352f * s, 0.289f * s); cubicTo(-0.367f * s, 0.289f * s, -0.379f * s, 0.277f * s, -0.379f * s, 0.262f * s); lineTo(-0.379f * s, -0.099f * s); cubicTo(-0.379f * s, -0.114f * s, -0.367f * s, -0.126f * s, -0.352f * s, -0.126f * s); close() }
    val p3 = Path().apply { moveTo(-0.289f * s, -0.126f * s); lineTo(-0.253f * s, -0.126f * s); cubicTo(-0.243f * s, -0.126f * s, -0.235f * s, -0.118f * s, -0.235f * s, -0.108f * s); lineTo(-0.235f * s, 0.09f * s); cubicTo(-0.235f * s, 0.1f * s, -0.243f * s, 0.108f * s, -0.253f * s, 0.108f * s); lineTo(-0.289f * s, 0.108f * s); cubicTo(-0.299f * s, 0.108f * s, -0.307f * s, 0.1f * s, -0.307f * s, 0.09f * s); lineTo(-0.307f * s, -0.108f * s); cubicTo(-0.307f * s, -0.118f * s, -0.299f * s, -0.126f * s, -0.289f * s, -0.126f * s); close() }
    val p4 = Path().apply { moveTo(-0.181f * s, -0.126f * s); lineTo(-0.145f * s, -0.126f * s); cubicTo(-0.135f * s, -0.126f * s, -0.126f * s, -0.118f * s, -0.126f * s, -0.108f * s); lineTo(-0.126f * s, 0.09f * s); cubicTo(-0.126f * s, 0.1f * s, -0.135f * s, 0.108f * s, -0.145f * s, 0.108f * s); lineTo(-0.181f * s, 0.108f * s); cubicTo(-0.191f * s, 0.108f * s, -0.199f * s, 0.1f * s, -0.199f * s, 0.09f * s); lineTo(-0.199f * s, -0.108f * s); cubicTo(-0.199f * s, -0.118f * s, -0.191f * s, -0.126f * s, -0.181f * s, -0.126f * s); close() }
    val p5 = Path().apply { moveTo(0.036f * s, -0.126f * s); lineTo(0.072f * s, -0.126f * s); cubicTo(0.082f * s, -0.126f * s, 0.09f * s, -0.118f * s, 0.09f * s, -0.108f * s); lineTo(0.09f * s, 0.09f * s); cubicTo(0.09f * s, 0.1f * s, 0.082f * s, 0.108f * s, 0.072f * s, 0.108f * s); lineTo(0.036f * s, 0.108f * s); cubicTo(0.026f * s, 0.108f * s, 0.018f * s, 0.1f * s, 0.018f * s, 0.09f * s); lineTo(0.018f * s, -0.108f * s); cubicTo(0.018f * s, -0.118f * s, 0.026f * s, -0.126f * s, 0.036f * s, -0.126f * s); close() }
    val p6 = Path().apply { moveTo(0.145f * s, -0.126f * s); lineTo(0.181f * s, -0.126f * s); cubicTo(0.191f * s, -0.126f * s, 0.199f * s, -0.118f * s, 0.199f * s, -0.108f * s); lineTo(0.199f * s, 0.09f * s); cubicTo(0.199f * s, 0.1f * s, 0.191f * s, 0.108f * s, 0.181f * s, 0.108f * s); lineTo(0.145f * s, 0.108f * s); cubicTo(0.135f * s, 0.108f * s, 0.127f * s, 0.1f * s, 0.127f * s, 0.09f * s); lineTo(0.127f * s, -0.108f * s); cubicTo(0.127f * s, -0.118f * s, 0.135f * s, -0.126f * s, 0.145f * s, -0.126f * s); close() }
    val p7 = Path().apply { moveTo(0.253f * s, -0.126f * s); lineTo(0.289f * s, -0.126f * s); cubicTo(0.299f * s, -0.126f * s, 0.307f * s, -0.118f * s, 0.307f * s, -0.108f * s); lineTo(0.307f * s, 0.09f * s); cubicTo(0.307f * s, 0.1f * s, 0.299f * s, 0.108f * s, 0.289f * s, 0.108f * s); lineTo(0.253f * s, 0.108f * s); cubicTo(0.243f * s, 0.108f * s, 0.235f * s, 0.1f * s, 0.235f * s, 0.09f * s); lineTo(0.235f * s, -0.108f * s); cubicTo(0.235f * s, -0.118f * s, 0.243f * s, -0.126f * s, 0.253f * s, -0.126f * s); close() }
    val p8 = Path().apply { moveTo(-0.325f * s, 0.343f * s); lineTo(-0.271f * s, 0.343f * s); cubicTo(-0.256f * s, 0.343f * s, -0.244f * s, 0.356f * s, -0.244f * s, 0.371f * s); lineTo(-0.244f * s, 0.407f * s); cubicTo(-0.244f * s, 0.422f * s, -0.256f * s, 0.434f * s, -0.271f * s, 0.434f * s); lineTo(-0.325f * s, 0.434f * s); cubicTo(-0.34f * s, 0.434f * s, -0.352f * s, 0.422f * s, -0.352f * s, 0.407f * s); lineTo(-0.352f * s, 0.371f * s); cubicTo(-0.352f * s, 0.356f * s, -0.34f * s, 0.343f * s, -0.325f * s, 0.343f * s); close() }
    val p9 = Path().apply { moveTo(0.271f * s, 0.343f * s); lineTo(0.325f * s, 0.343f * s); cubicTo(0.34f * s, 0.343f * s, 0.352f * s, 0.356f * s, 0.352f * s, 0.371f * s); lineTo(0.352f * s, 0.407f * s); cubicTo(0.352f * s, 0.422f * s, 0.34f * s, 0.434f * s, 0.325f * s, 0.434f * s); lineTo(0.271f * s, 0.434f * s); cubicTo(0.256f * s, 0.434f * s, 0.244f * s, 0.422f * s, 0.244f * s, 0.407f * s); lineTo(0.244f * s, 0.371f * s); cubicTo(0.244f * s, 0.356f * s, 0.256f * s, 0.343f * s, 0.271f * s, 0.343f * s); close() }
    val p10 = Path().apply { moveTo(-0.379f * s, 0.271f * s); lineTo(0.38f * s, 0.271f * s); cubicTo(0.4f * s, 0.271f * s, 0.416f * s, 0.287f * s, 0.416f * s, 0.307f * s); lineTo(0.416f * s, 0.325f * s); cubicTo(0.416f * s, 0.345f * s, 0.4f * s, 0.361f * s, 0.38f * s, 0.361f * s); lineTo(-0.379f * s, 0.361f * s); cubicTo(-0.399f * s, 0.361f * s, -0.416f * s, 0.345f * s, -0.416f * s, 0.325f * s); lineTo(-0.416f * s, 0.307f * s); cubicTo(-0.416f * s, 0.287f * s, -0.399f * s, 0.271f * s, -0.379f * s, 0.271f * s); close() }
    val p11 = Path().apply { moveTo(-0.398f * s, 0.244f * s); lineTo(0.361f * s, 0.244f * s); cubicTo(0.381f * s, 0.244f * s, 0.398f * s, 0.26f * s, 0.398f * s, 0.28f * s); lineTo(0.398f * s, 0.298f * s); cubicTo(0.398f * s, 0.318f * s, 0.381f * s, 0.334f * s, 0.361f * s, 0.334f * s); lineTo(-0.398f * s, 0.334f * s); cubicTo(-0.418f * s, 0.334f * s, -0.434f * s, 0.318f * s, -0.434f * s, 0.298f * s); lineTo(-0.434f * s, 0.28f * s); cubicTo(-0.434f * s, 0.26f * s, -0.418f * s, 0.244f * s, -0.398f * s, 0.244f * s); close() }
    drawPath(p0, sil ?: Color(0xFF292B3C))
    if (sil == null) {
        clipPath(p0) {
            drawPath(p1, Color(0xFF3A3D55))
        }
        drawOval(Color(0x59FFFFFF), Offset(-0.361f * s, -0.381f * s), Size(0.181f * s, 0.04f * s))
    }
    drawPath(p0, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p2, sil ?: Color(0xFFF6F4EE))
    drawPath(p2, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.271f * s, -0.108f * s), Offset(-0.271f * s, 0.271f * s), 0.022f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.163f * s, -0.108f * s), Offset(-0.163f * s, 0.271f * s), 0.022f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(-0.054f * s, -0.108f * s), Offset(-0.054f * s, 0.271f * s), 0.022f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.054f * s, -0.108f * s), Offset(0.054f * s, 0.271f * s), 0.022f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.163f * s, -0.108f * s), Offset(0.163f * s, 0.271f * s), 0.022f * s, StrokeCap.Round)
    drawLine(sil ?: Color(0xFF2B2D42), Offset(0.271f * s, -0.108f * s), Offset(0.271f * s, 0.271f * s), 0.022f * s, StrokeCap.Round)
    drawPath(p3, sil ?: Color(0xFF3A3D55))
    drawPath(p3, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p4, sil ?: Color(0xFF3A3D55))
    drawPath(p4, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p5, sil ?: Color(0xFF3A3D55))
    drawPath(p5, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p6, sil ?: Color(0xFF3A3D55))
    drawPath(p6, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p7, sil ?: Color(0xFF3A3D55))
    drawPath(p7, sil ?: Color(0xFF2B2D42), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p8, sil ?: Color(0xFF3A3D55))
    drawPath(p8, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p9, sil ?: Color(0xFF3A3D55))
    drawPath(p9, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(p10, sil ?: Color(0xFF292B3C))
    if (sil == null) {
        clipPath(p10) {
            drawPath(p11, Color(0xFF3A3D55))
        }
    }
    drawPath(p10, sil ?: Color(0xFF2B2D42), style = Stroke(0.036f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}
