// GÉNÉRÉ par tools/design/make-vehicles.js : ne pas modifier à la main (la CI vérifie que ce fichier est à jour).
// Les véhicules de Parking Malin, dessinés de dessus, nez vers le haut, unité = `s` (côté d'une case en pixels), centrés en (0, 0).
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
    SEDAN(2),
    HATCH(2),
    SPORT(2),
    PICKUP(3),
    BOX(3),
    VAN(3),
    BUS(4),
    TANKER(4);

    /** Les camions et les bus sont un peu plus larges que les voitures. */
    val widthScale: Float get() = if (len >= 3) 1.075f else 1f

    companion object {
        /** Les modèles de [len] cases. */
        fun ofLength(len: Int): List<VehicleModel> = entries.filter { it.len == len }
    }
}

/** Épaisseur de la « tranche » sombre, en cases. */
const val VEHICLE_DEPTH = 0.15f

/** Dessous : roues et silhouette sombre (à décaler vers le bas de l'écran, hors rotation). */
fun DrawScope.drawVehicleLower(model: VehicleModel, body: Color, s: Float) {
    when (model) {
        VehicleModel.SEDAN -> lowerSedan(body, s)
        VehicleModel.HATCH -> lowerHatch(body, s)
        VehicleModel.SPORT -> lowerSport(body, s)
        VehicleModel.PICKUP -> lowerPickup(body, s)
        VehicleModel.BOX -> lowerBox(body, s)
        VehicleModel.VAN -> lowerVan(body, s)
        VehicleModel.BUS -> lowerBus(body, s)
        VehicleModel.TANKER -> lowerTanker(body, s)
    }
}

/** Silhouette pleine, d'une seule couleur : sert à rougir tout le véhicule (flash de faute) sans en dénaturer la couleur. */
fun DrawScope.drawVehicleSilhouette(model: VehicleModel, color: Color, s: Float) {
    when (model) {
        VehicleModel.SEDAN -> drawPath(Path().apply { moveTo(-0.3f * s, -0.9f * s); cubicTo(-0.18f * s, -0.925f * s, 0.18f * s, -0.925f * s, 0.3f * s, -0.9f * s); cubicTo(0.38f * s, -0.86f * s, 0.4f * s, -0.7f * s, 0.4f * s, -0.52f * s); lineTo(0.4f * s, 0.55f * s); cubicTo(0.4f * s, 0.78f * s, 0.36f * s, 0.88f * s, 0.28f * s, 0.9f * s); cubicTo(0.15f * s, 0.93f * s, -0.15f * s, 0.93f * s, -0.28f * s, 0.9f * s); cubicTo(-0.36f * s, 0.88f * s, -0.4f * s, 0.78f * s, -0.4f * s, 0.55f * s); lineTo(-0.4f * s, -0.52f * s); cubicTo(-0.4f * s, -0.7f * s, -0.38f * s, -0.86f * s, -0.3f * s, -0.9f * s); close() }, color)
        VehicleModel.HATCH -> drawPath(Path().apply { moveTo(-0.31f * s, -0.9f * s); cubicTo(-0.18f * s, -0.925f * s, 0.18f * s, -0.925f * s, 0.31f * s, -0.9f * s); cubicTo(0.38f * s, -0.86f * s, 0.4f * s, -0.7f * s, 0.4f * s, -0.54f * s); lineTo(0.4f * s, 0.62f * s); cubicTo(0.4f * s, 0.8f * s, 0.37f * s, 0.88f * s, 0.3f * s, 0.9f * s); cubicTo(0.15f * s, 0.915f * s, -0.15f * s, 0.915f * s, -0.3f * s, 0.9f * s); cubicTo(-0.37f * s, 0.88f * s, -0.4f * s, 0.8f * s, -0.4f * s, 0.62f * s); lineTo(-0.4f * s, -0.54f * s); cubicTo(-0.4f * s, -0.7f * s, -0.38f * s, -0.86f * s, -0.31f * s, -0.9f * s); close() }, color)
        VehicleModel.SPORT -> drawPath(Path().apply { moveTo(-0.24f * s, -0.9f * s); cubicTo(-0.12f * s, -0.93f * s, 0.12f * s, -0.93f * s, 0.24f * s, -0.9f * s); cubicTo(0.34f * s, -0.84f * s, 0.4f * s, -0.66f * s, 0.4f * s, -0.44f * s); lineTo(0.4f * s, 0.48f * s); cubicTo(0.4f * s, 0.74f * s, 0.37f * s, 0.88f * s, 0.3f * s, 0.9f * s); cubicTo(0.15f * s, 0.93f * s, -0.15f * s, 0.93f * s, -0.3f * s, 0.9f * s); cubicTo(-0.37f * s, 0.88f * s, -0.4f * s, 0.74f * s, -0.4f * s, 0.48f * s); lineTo(-0.4f * s, -0.44f * s); cubicTo(-0.4f * s, -0.66f * s, -0.34f * s, -0.84f * s, -0.24f * s, -0.9f * s); close() }, color)
        VehicleModel.PICKUP -> drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.3f * s); cubicTo(0.4f * s, 1.38f * s, 0.37f * s, 1.4f * s, 0.3f * s, 1.4f * s); lineTo(-0.3f * s, 1.4f * s); cubicTo(-0.37f * s, 1.4f * s, -0.4f * s, 1.38f * s, -0.4f * s, 1.3f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, color)
        VehicleModel.BOX -> drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.3f * s); cubicTo(0.4f * s, 1.38f * s, 0.37f * s, 1.4f * s, 0.3f * s, 1.4f * s); lineTo(-0.3f * s, 1.4f * s); cubicTo(-0.37f * s, 1.4f * s, -0.4f * s, 1.38f * s, -0.4f * s, 1.3f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, color)
        VehicleModel.VAN -> drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.15f * s); cubicTo(0.4f * s, 1.32f * s, 0.36f * s, 1.4f * s, 0.28f * s, 1.4f * s); cubicTo(0.15f * s, 1.42f * s, -0.15f * s, 1.42f * s, -0.28f * s, 1.4f * s); cubicTo(-0.36f * s, 1.4f * s, -0.4f * s, 1.32f * s, -0.4f * s, 1.15f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, color)
        VehicleModel.BUS -> drawPath(Path().apply { moveTo(-0.32f * s, -1.9f * s); cubicTo(-0.2f * s, -1.925f * s, 0.2f * s, -1.925f * s, 0.32f * s, -1.9f * s); cubicTo(0.38f * s, -1.87f * s, 0.4f * s, -1.8f * s, 0.4f * s, -1.7f * s); lineTo(0.4f * s, 1.7f * s); cubicTo(0.4f * s, 1.82f * s, 0.36f * s, 1.9f * s, 0.3f * s, 1.9f * s); lineTo(-0.3f * s, 1.9f * s); cubicTo(-0.36f * s, 1.9f * s, -0.4f * s, 1.82f * s, -0.4f * s, 1.7f * s); lineTo(-0.4f * s, -1.7f * s); cubicTo(-0.4f * s, -1.8f * s, -0.38f * s, -1.87f * s, -0.32f * s, -1.9f * s); close() }, color)
        VehicleModel.TANKER -> drawPath(Path().apply { moveTo(-0.3f * s, -1.9f * s); cubicTo(-0.18f * s, -1.925f * s, 0.18f * s, -1.925f * s, 0.3f * s, -1.9f * s); cubicTo(0.38f * s, -1.86f * s, 0.4f * s, -1.7f * s, 0.4f * s, -1.5f * s); lineTo(0.4f * s, 1.85f * s); cubicTo(0.4f * s, 1.88f * s, 0.38f * s, 1.9f * s, 0.34f * s, 1.9f * s); lineTo(-0.34f * s, 1.9f * s); cubicTo(-0.38f * s, 1.9f * s, -0.4f * s, 1.88f * s, -0.4f * s, 1.85f * s); lineTo(-0.4f * s, -1.5f * s); cubicTo(-0.4f * s, -1.7f * s, -0.38f * s, -1.86f * s, -0.3f * s, -1.9f * s); close() }, color)
    }
}

/** Dessus : la carrosserie et tous ses détails. */
fun DrawScope.drawVehicleUpper(model: VehicleModel, body: Color, s: Float) {
    when (model) {
        VehicleModel.SEDAN -> upperSedan(body, s)
        VehicleModel.HATCH -> upperHatch(body, s)
        VehicleModel.SPORT -> upperSport(body, s)
        VehicleModel.PICKUP -> upperPickup(body, s)
        VehicleModel.BOX -> upperBox(body, s)
        VehicleModel.VAN -> upperVan(body, s)
        VehicleModel.BUS -> upperBus(body, s)
        VehicleModel.TANKER -> upperTanker(body, s)
    }
}

private fun DrawScope.lowerSedan(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -0.69f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -0.5f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -0.69f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -0.5f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.33f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.52f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.33f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.52f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -0.9f * s); cubicTo(-0.18f * s, -0.925f * s, 0.18f * s, -0.925f * s, 0.3f * s, -0.9f * s); cubicTo(0.38f * s, -0.86f * s, 0.4f * s, -0.7f * s, 0.4f * s, -0.52f * s); lineTo(0.4f * s, 0.55f * s); cubicTo(0.4f * s, 0.78f * s, 0.36f * s, 0.88f * s, 0.28f * s, 0.9f * s); cubicTo(0.15f * s, 0.93f * s, -0.15f * s, 0.93f * s, -0.28f * s, 0.9f * s); cubicTo(-0.36f * s, 0.88f * s, -0.4f * s, 0.78f * s, -0.4f * s, 0.55f * s); lineTo(-0.4f * s, -0.52f * s); cubicTo(-0.4f * s, -0.7f * s, -0.38f * s, -0.86f * s, -0.3f * s, -0.9f * s); close() }, tone(body, d = 0.45f))
}

private fun DrawScope.upperSedan(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -0.72f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -0.53f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -0.72f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -0.53f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.3f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.49f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.3f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.49f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -0.9f * s); cubicTo(-0.18f * s, -0.925f * s, 0.18f * s, -0.925f * s, 0.3f * s, -0.9f * s); cubicTo(0.38f * s, -0.86f * s, 0.4f * s, -0.7f * s, 0.4f * s, -0.52f * s); lineTo(0.4f * s, 0.55f * s); cubicTo(0.4f * s, 0.78f * s, 0.36f * s, 0.88f * s, 0.28f * s, 0.9f * s); cubicTo(0.15f * s, 0.93f * s, -0.15f * s, 0.93f * s, -0.28f * s, 0.9f * s); cubicTo(-0.36f * s, 0.88f * s, -0.4f * s, 0.78f * s, -0.4f * s, 0.55f * s); lineTo(-0.4f * s, -0.52f * s); cubicTo(-0.4f * s, -0.7f * s, -0.38f * s, -0.86f * s, -0.3f * s, -0.9f * s); close() }, Brush.linearGradient(0.0f to tone(body, l = 0.3f), 0.3f to tone(body, l = 0.08f), 0.75f to body, 1.0f to tone(body, d = 0.22f), start = Offset(-0.4f * s, 0.0f * s), end = Offset(0.4f * s, 0.0f * s)))
    drawOval(tone(Color(0xFFFFFFFF), a = 0.3f), Offset(-0.28f * s, -0.79f * s), Size(0.3f * s, 0.18f * s))
    drawPath(Path().apply { moveTo(-0.32f * s, -0.4f * s); quadraticTo(0.0f * s, -0.47f * s, 0.32f * s, -0.4f * s); lineTo(0.27f * s, -0.08f * s); lineTo(-0.27f * s, -0.08f * s); close() }, Brush.linearGradient(0.0f to Color(0xFF7FA3D6), 1.0f to Color(0xFF1E3050), start = Offset(0.0f * s, -0.47f * s), end = Offset(0.0f * s, -0.08f * s)))
    drawPath(Path().apply { moveTo(-0.32f * s, -0.4f * s); quadraticTo(0.0f * s, -0.47f * s, 0.32f * s, -0.4f * s); lineTo(0.27f * s, -0.08f * s); lineTo(-0.27f * s, -0.08f * s); close() }, tone(body, d = 0.45f), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.26f * s, -0.39f * s); lineTo(-0.15f * s, -0.41f * s); lineTo(-0.14f * s, -0.095f * s); lineTo(-0.24f * s, -0.095f * s); close() }, tone(Color(0xFFFFFFFF), a = 0.34f))
    drawRoundRect(Brush.linearGradient(0.0f to tone(body, l = 0.22f), 1.0f to tone(body, l = 0.08f), start = Offset(0.0f * s, -0.1f * s), end = Offset(0.0f * s, 0.36f * s)), Offset(-0.29f * s, -0.1f * s), Size(0.58f * s, 0.46f * s), CornerRadius(0.15f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.29f * s, -0.1f * s), Size(0.58f * s, 0.46f * s), CornerRadius(0.15f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.18f), Offset(-0.24f * s, -0.05f * s), Size(0.174f * s, 0.36f * s), CornerRadius(0.09f * s))
    drawPath(Path().apply { moveTo(-0.25f * s, 0.41f * s); lineTo(0.25f * s, 0.41f * s); lineTo(0.21f * s, 0.53f * s); quadraticTo(0.0f * s, 0.58f * s, -0.21f * s, 0.53f * s); close() }, Brush.linearGradient(0.0f to tone(body, d = 0.42f), 1.0f to tone(body, d = 0.6f), start = Offset(0.0f * s, 0.41f * s), end = Offset(0.0f * s, 0.58f * s)))
    drawPath(Path().apply { moveTo(-0.25f * s, 0.41f * s); lineTo(0.25f * s, 0.41f * s); lineTo(0.21f * s, 0.53f * s); quadraticTo(0.0f * s, 0.58f * s, -0.21f * s, 0.53f * s); close() }, tone(body, d = 0.55f), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(Color(0xFFF4F6FA), a = 0.95f), Offset(-0.25f * s, -0.915f * s), Size(0.5f * s, 0.05f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFFFFFFF), Offset(-0.355f * s, -0.875f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(-0.355f * s, -0.875f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFFFFFFF), Offset(0.185f * s, -0.875f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(0.185f * s, -0.875f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFE5352B), Offset(-0.36f * s, 0.81f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFE5352B), Offset(0.22f * s, 0.81f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -0.9f * s); cubicTo(-0.18f * s, -0.925f * s, 0.18f * s, -0.925f * s, 0.3f * s, -0.9f * s); cubicTo(0.38f * s, -0.86f * s, 0.4f * s, -0.7f * s, 0.4f * s, -0.52f * s); lineTo(0.4f * s, 0.55f * s); cubicTo(0.4f * s, 0.78f * s, 0.36f * s, 0.88f * s, 0.28f * s, 0.9f * s); cubicTo(0.15f * s, 0.93f * s, -0.15f * s, 0.93f * s, -0.28f * s, 0.9f * s); cubicTo(-0.36f * s, 0.88f * s, -0.4f * s, 0.78f * s, -0.4f * s, 0.55f * s); lineTo(-0.4f * s, -0.52f * s); cubicTo(-0.4f * s, -0.7f * s, -0.38f * s, -0.86f * s, -0.3f * s, -0.9f * s); close() }, tone(body, d = 0.5f), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.3f * s, -0.9f * s); cubicTo(-0.18f * s, -0.925f * s, 0.18f * s, -0.925f * s, 0.3f * s, -0.9f * s); cubicTo(0.38f * s, -0.86f * s, 0.4f * s, -0.7f * s, 0.4f * s, -0.52f * s); lineTo(0.4f * s, 0.55f * s); cubicTo(0.4f * s, 0.78f * s, 0.36f * s, 0.88f * s, 0.28f * s, 0.9f * s); cubicTo(0.15f * s, 0.93f * s, -0.15f * s, 0.93f * s, -0.28f * s, 0.9f * s); cubicTo(-0.36f * s, 0.88f * s, -0.4f * s, 0.78f * s, -0.4f * s, 0.55f * s); lineTo(-0.4f * s, -0.52f * s); cubicTo(-0.4f * s, -0.7f * s, -0.38f * s, -0.86f * s, -0.3f * s, -0.9f * s); close() }, Brush.linearGradient(0.0f to tone(Color(0xFFFFFFFF), a = 0.7f), 0.4f to tone(Color(0xFFFFFFFF), a = 0.0f), 0.62f to tone(Color(0xFF000000), a = 0.0f), 1.0f to tone(Color(0xFF000000), a = 0.4f), start = Offset(-0.4f * s, -0.9f * s), end = Offset(0.32f * s, 0.9f * s)), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.lowerHatch(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -0.69f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -0.5f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -0.69f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -0.5f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.31f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.5f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.31f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.5f * s))
    drawPath(Path().apply { moveTo(-0.31f * s, -0.9f * s); cubicTo(-0.18f * s, -0.925f * s, 0.18f * s, -0.925f * s, 0.31f * s, -0.9f * s); cubicTo(0.38f * s, -0.86f * s, 0.4f * s, -0.7f * s, 0.4f * s, -0.54f * s); lineTo(0.4f * s, 0.62f * s); cubicTo(0.4f * s, 0.8f * s, 0.37f * s, 0.88f * s, 0.3f * s, 0.9f * s); cubicTo(0.15f * s, 0.915f * s, -0.15f * s, 0.915f * s, -0.3f * s, 0.9f * s); cubicTo(-0.37f * s, 0.88f * s, -0.4f * s, 0.8f * s, -0.4f * s, 0.62f * s); lineTo(-0.4f * s, -0.54f * s); cubicTo(-0.4f * s, -0.7f * s, -0.38f * s, -0.86f * s, -0.31f * s, -0.9f * s); close() }, tone(body, d = 0.45f))
}

private fun DrawScope.upperHatch(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -0.72f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -0.53f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -0.72f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -0.53f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.28f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.47f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.28f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.47f * s))
    drawPath(Path().apply { moveTo(-0.31f * s, -0.9f * s); cubicTo(-0.18f * s, -0.925f * s, 0.18f * s, -0.925f * s, 0.31f * s, -0.9f * s); cubicTo(0.38f * s, -0.86f * s, 0.4f * s, -0.7f * s, 0.4f * s, -0.54f * s); lineTo(0.4f * s, 0.62f * s); cubicTo(0.4f * s, 0.8f * s, 0.37f * s, 0.88f * s, 0.3f * s, 0.9f * s); cubicTo(0.15f * s, 0.915f * s, -0.15f * s, 0.915f * s, -0.3f * s, 0.9f * s); cubicTo(-0.37f * s, 0.88f * s, -0.4f * s, 0.8f * s, -0.4f * s, 0.62f * s); lineTo(-0.4f * s, -0.54f * s); cubicTo(-0.4f * s, -0.7f * s, -0.38f * s, -0.86f * s, -0.31f * s, -0.9f * s); close() }, Brush.linearGradient(0.0f to tone(body, l = 0.3f), 0.3f to tone(body, l = 0.08f), 0.75f to body, 1.0f to tone(body, d = 0.22f), start = Offset(-0.4f * s, 0.0f * s), end = Offset(0.4f * s, 0.0f * s)))
    drawOval(tone(Color(0xFFFFFFFF), a = 0.3f), Offset(-0.26f * s, -0.785f * s), Size(0.28f * s, 0.17f * s))
    drawPath(Path().apply { moveTo(-0.34f * s, -0.46f * s); quadraticTo(0.0f * s, -0.54f * s, 0.34f * s, -0.46f * s); lineTo(0.3f * s, -0.14f * s); lineTo(-0.3f * s, -0.14f * s); close() }, Brush.linearGradient(0.0f to Color(0xFF7FA3D6), 1.0f to Color(0xFF1E3050), start = Offset(0.0f * s, -0.54f * s), end = Offset(0.0f * s, -0.14f * s)))
    drawPath(Path().apply { moveTo(-0.34f * s, -0.46f * s); quadraticTo(0.0f * s, -0.54f * s, 0.34f * s, -0.46f * s); lineTo(0.3f * s, -0.14f * s); lineTo(-0.3f * s, -0.14f * s); close() }, tone(body, d = 0.45f), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.28f * s, -0.45f * s); lineTo(-0.17f * s, -0.47f * s); lineTo(-0.17f * s, -0.155f * s); lineTo(-0.27f * s, -0.155f * s); close() }, tone(Color(0xFFFFFFFF), a = 0.34f))
    drawRoundRect(Brush.linearGradient(0.0f to tone(body, l = 0.22f), 1.0f to tone(body, l = 0.08f), start = Offset(0.0f * s, -0.16f * s), end = Offset(0.0f * s, 0.5f * s)), Offset(-0.31f * s, -0.16f * s), Size(0.62f * s, 0.66f * s), CornerRadius(0.16f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.31f * s, -0.16f * s), Size(0.62f * s, 0.66f * s), CornerRadius(0.16f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.18f), Offset(-0.26f * s, -0.11f * s), Size(0.186f * s, 0.56f * s), CornerRadius(0.096f * s))
    drawPath(Path().apply { moveTo(-0.27f * s, 0.56f * s); lineTo(0.27f * s, 0.56f * s); lineTo(0.23f * s, 0.68f * s); quadraticTo(0.0f * s, 0.73f * s, -0.23f * s, 0.68f * s); close() }, Brush.linearGradient(0.0f to tone(body, d = 0.42f), 1.0f to tone(body, d = 0.6f), start = Offset(0.0f * s, 0.56f * s), end = Offset(0.0f * s, 0.73f * s)))
    drawPath(Path().apply { moveTo(-0.27f * s, 0.56f * s); lineTo(0.27f * s, 0.56f * s); lineTo(0.23f * s, 0.68f * s); quadraticTo(0.0f * s, 0.73f * s, -0.23f * s, 0.68f * s); close() }, tone(body, d = 0.55f), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(body, d = 0.35f), Offset(-0.3f * s, 0.76f * s), Size(0.6f * s, 0.07f * s), CornerRadius(0.035f * s))
    drawRoundRect(tone(Color(0xFFF4F6FA), a = 0.95f), Offset(-0.25f * s, -0.915f * s), Size(0.5f * s, 0.05f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFFFFFFF), Offset(-0.365f * s, -0.875f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(-0.365f * s, -0.875f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFFFFFFF), Offset(0.195f * s, -0.875f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(0.195f * s, -0.875f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFE5352B), Offset(-0.37f * s, 0.83f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFE5352B), Offset(0.23f * s, 0.83f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawPath(Path().apply { moveTo(-0.31f * s, -0.9f * s); cubicTo(-0.18f * s, -0.925f * s, 0.18f * s, -0.925f * s, 0.31f * s, -0.9f * s); cubicTo(0.38f * s, -0.86f * s, 0.4f * s, -0.7f * s, 0.4f * s, -0.54f * s); lineTo(0.4f * s, 0.62f * s); cubicTo(0.4f * s, 0.8f * s, 0.37f * s, 0.88f * s, 0.3f * s, 0.9f * s); cubicTo(0.15f * s, 0.915f * s, -0.15f * s, 0.915f * s, -0.3f * s, 0.9f * s); cubicTo(-0.37f * s, 0.88f * s, -0.4f * s, 0.8f * s, -0.4f * s, 0.62f * s); lineTo(-0.4f * s, -0.54f * s); cubicTo(-0.4f * s, -0.7f * s, -0.38f * s, -0.86f * s, -0.31f * s, -0.9f * s); close() }, tone(body, d = 0.5f), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.31f * s, -0.9f * s); cubicTo(-0.18f * s, -0.925f * s, 0.18f * s, -0.925f * s, 0.31f * s, -0.9f * s); cubicTo(0.38f * s, -0.86f * s, 0.4f * s, -0.7f * s, 0.4f * s, -0.54f * s); lineTo(0.4f * s, 0.62f * s); cubicTo(0.4f * s, 0.8f * s, 0.37f * s, 0.88f * s, 0.3f * s, 0.9f * s); cubicTo(0.15f * s, 0.915f * s, -0.15f * s, 0.915f * s, -0.3f * s, 0.9f * s); cubicTo(-0.37f * s, 0.88f * s, -0.4f * s, 0.8f * s, -0.4f * s, 0.62f * s); lineTo(-0.4f * s, -0.54f * s); cubicTo(-0.4f * s, -0.7f * s, -0.38f * s, -0.86f * s, -0.31f * s, -0.9f * s); close() }, Brush.linearGradient(0.0f to tone(Color(0xFFFFFFFF), a = 0.7f), 0.4f to tone(Color(0xFFFFFFFF), a = 0.0f), 0.62f to tone(Color(0xFF000000), a = 0.0f), 1.0f to tone(Color(0xFF000000), a = 0.4f), start = Offset(-0.4f * s, -0.9f * s), end = Offset(0.32f * s, 0.9f * s)), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.lowerSport(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -0.71f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -0.52f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -0.71f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -0.52f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.33f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.52f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.33f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.52f * s))
    drawPath(Path().apply { moveTo(-0.24f * s, -0.9f * s); cubicTo(-0.12f * s, -0.93f * s, 0.12f * s, -0.93f * s, 0.24f * s, -0.9f * s); cubicTo(0.34f * s, -0.84f * s, 0.4f * s, -0.66f * s, 0.4f * s, -0.44f * s); lineTo(0.4f * s, 0.48f * s); cubicTo(0.4f * s, 0.74f * s, 0.37f * s, 0.88f * s, 0.3f * s, 0.9f * s); cubicTo(0.15f * s, 0.93f * s, -0.15f * s, 0.93f * s, -0.3f * s, 0.9f * s); cubicTo(-0.37f * s, 0.88f * s, -0.4f * s, 0.74f * s, -0.4f * s, 0.48f * s); lineTo(-0.4f * s, -0.44f * s); cubicTo(-0.4f * s, -0.66f * s, -0.34f * s, -0.84f * s, -0.24f * s, -0.9f * s); close() }, tone(body, d = 0.45f))
}

private fun DrawScope.upperSport(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -0.74f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -0.55f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -0.74f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -0.55f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.3f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.49f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.3f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.49f * s))
    drawPath(Path().apply { moveTo(-0.24f * s, -0.9f * s); cubicTo(-0.12f * s, -0.93f * s, 0.12f * s, -0.93f * s, 0.24f * s, -0.9f * s); cubicTo(0.34f * s, -0.84f * s, 0.4f * s, -0.66f * s, 0.4f * s, -0.44f * s); lineTo(0.4f * s, 0.48f * s); cubicTo(0.4f * s, 0.74f * s, 0.37f * s, 0.88f * s, 0.3f * s, 0.9f * s); cubicTo(0.15f * s, 0.93f * s, -0.15f * s, 0.93f * s, -0.3f * s, 0.9f * s); cubicTo(-0.37f * s, 0.88f * s, -0.4f * s, 0.74f * s, -0.4f * s, 0.48f * s); lineTo(-0.4f * s, -0.44f * s); cubicTo(-0.4f * s, -0.66f * s, -0.34f * s, -0.84f * s, -0.24f * s, -0.9f * s); close() }, Brush.linearGradient(0.0f to tone(body, l = 0.34f), 0.3f to tone(body, l = 0.08f), 0.75f to body, 1.0f to tone(body, d = 0.26f), start = Offset(-0.4f * s, 0.0f * s), end = Offset(0.4f * s, 0.0f * s)))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.9f), Offset(-0.17f * s, -0.92f * s), Size(0.08f * s, 1.8f * s), CornerRadius(0.02f * s))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.9f), Offset(0.09f * s, -0.92f * s), Size(0.08f * s, 1.8f * s), CornerRadius(0.02f * s))
    drawPath(Path().apply { moveTo(-0.31f * s, -0.26f * s); quadraticTo(0.0f * s, -0.33f * s, 0.31f * s, -0.26f * s); lineTo(0.27f * s, -0.03f * s); lineTo(-0.27f * s, -0.03f * s); close() }, Brush.linearGradient(0.0f to Color(0xFF7FA3D6), 1.0f to Color(0xFF1E3050), start = Offset(0.0f * s, -0.33f * s), end = Offset(0.0f * s, -0.03f * s)))
    drawPath(Path().apply { moveTo(-0.31f * s, -0.26f * s); quadraticTo(0.0f * s, -0.33f * s, 0.31f * s, -0.26f * s); lineTo(0.27f * s, -0.03f * s); lineTo(-0.27f * s, -0.03f * s); close() }, tone(body, d = 0.45f), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.25f * s, -0.25f * s); lineTo(-0.14f * s, -0.27f * s); lineTo(-0.14f * s, -0.045f * s); lineTo(-0.24f * s, -0.045f * s); close() }, tone(Color(0xFFFFFFFF), a = 0.34f))
    drawRoundRect(Brush.linearGradient(0.0f to tone(body, l = 0.22f), 1.0f to tone(body, l = 0.04f), start = Offset(0.0f * s, -0.04f * s), end = Offset(0.0f * s, 0.36f * s)), Offset(-0.26f * s, -0.04f * s), Size(0.52f * s, 0.4f * s), CornerRadius(0.14f * s))
    drawRoundRect(tone(body, d = 0.35f), Offset(-0.26f * s, -0.04f * s), Size(0.52f * s, 0.4f * s), CornerRadius(0.14f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.2f), Offset(-0.2f * s, 0.0f * s), Size(0.13f * s, 0.3f * s), CornerRadius(0.06f * s))
    drawPath(Path().apply { moveTo(-0.23f * s, 0.37f * s); lineTo(0.23f * s, 0.37f * s); lineTo(0.2f * s, 0.49f * s); quadraticTo(0.0f * s, 0.54f * s, -0.2f * s, 0.49f * s); close() }, Brush.linearGradient(0.0f to tone(body, d = 0.42f), 1.0f to tone(body, d = 0.6f), start = Offset(0.0f * s, 0.37f * s), end = Offset(0.0f * s, 0.54f * s)))
    drawPath(Path().apply { moveTo(-0.23f * s, 0.37f * s); lineTo(0.23f * s, 0.37f * s); lineTo(0.2f * s, 0.49f * s); quadraticTo(0.0f * s, 0.54f * s, -0.2f * s, 0.49f * s); close() }, tone(body, d = 0.55f), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(body, d = 0.5f), Offset(-0.38f * s, 0.7f * s), Size(0.76f * s, 0.11f * s), CornerRadius(0.045f * s))
    drawRoundRect(Color(0xFFFFFFFF), Offset(-0.335f * s, -0.865f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(-0.335f * s, -0.865f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFFFFFFF), Offset(0.165f * s, -0.865f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(0.165f * s, -0.865f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFE5352B), Offset(-0.37f * s, 0.85f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFE5352B), Offset(0.23f * s, 0.85f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawPath(Path().apply { moveTo(-0.24f * s, -0.9f * s); cubicTo(-0.12f * s, -0.93f * s, 0.12f * s, -0.93f * s, 0.24f * s, -0.9f * s); cubicTo(0.34f * s, -0.84f * s, 0.4f * s, -0.66f * s, 0.4f * s, -0.44f * s); lineTo(0.4f * s, 0.48f * s); cubicTo(0.4f * s, 0.74f * s, 0.37f * s, 0.88f * s, 0.3f * s, 0.9f * s); cubicTo(0.15f * s, 0.93f * s, -0.15f * s, 0.93f * s, -0.3f * s, 0.9f * s); cubicTo(-0.37f * s, 0.88f * s, -0.4f * s, 0.74f * s, -0.4f * s, 0.48f * s); lineTo(-0.4f * s, -0.44f * s); cubicTo(-0.4f * s, -0.66f * s, -0.34f * s, -0.84f * s, -0.24f * s, -0.9f * s); close() }, tone(body, d = 0.5f), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.24f * s, -0.9f * s); cubicTo(-0.12f * s, -0.93f * s, 0.12f * s, -0.93f * s, 0.24f * s, -0.9f * s); cubicTo(0.34f * s, -0.84f * s, 0.4f * s, -0.66f * s, 0.4f * s, -0.44f * s); lineTo(0.4f * s, 0.48f * s); cubicTo(0.4f * s, 0.74f * s, 0.37f * s, 0.88f * s, 0.3f * s, 0.9f * s); cubicTo(0.15f * s, 0.93f * s, -0.15f * s, 0.93f * s, -0.3f * s, 0.9f * s); cubicTo(-0.37f * s, 0.88f * s, -0.4f * s, 0.74f * s, -0.4f * s, 0.48f * s); lineTo(-0.4f * s, -0.44f * s); cubicTo(-0.4f * s, -0.66f * s, -0.34f * s, -0.84f * s, -0.24f * s, -0.9f * s); close() }, Brush.linearGradient(0.0f to tone(Color(0xFFFFFFFF), a = 0.7f), 0.4f to tone(Color(0xFFFFFFFF), a = 0.0f), 0.62f to tone(Color(0xFF000000), a = 0.0f), 1.0f to tone(Color(0xFF000000), a = 0.4f), start = Offset(-0.4f * s, -0.9f * s), end = Offset(0.32f * s, 0.9f * s)), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.lowerPickup(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -1.14f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -0.95f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -1.14f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -0.95f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.71f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.9f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.71f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.9f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.3f * s); cubicTo(0.4f * s, 1.38f * s, 0.37f * s, 1.4f * s, 0.3f * s, 1.4f * s); lineTo(-0.3f * s, 1.4f * s); cubicTo(-0.37f * s, 1.4f * s, -0.4f * s, 1.38f * s, -0.4f * s, 1.3f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, tone(body, d = 0.45f))
}

private fun DrawScope.upperPickup(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -1.17f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -0.98f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -1.17f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -0.98f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.68f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.87f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.68f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.87f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, -0.18f * s); lineTo(-0.4f * s, -0.18f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, Brush.linearGradient(0.0f to tone(body, l = 0.3f), 0.3f to tone(body, l = 0.08f), 0.75f to body, 1.0f to tone(body, d = 0.22f), start = Offset(-0.4f * s, 0.0f * s), end = Offset(0.4f * s, 0.0f * s)))
    drawOval(tone(Color(0xFFFFFFFF), a = 0.3f), Offset(-0.28f * s, -1.3f * s), Size(0.3f * s, 0.2f * s))
    drawPath(Path().apply { moveTo(-0.32f * s, -0.84f * s); quadraticTo(0.0f * s, -0.92f * s, 0.32f * s, -0.84f * s); lineTo(0.29f * s, -0.52f * s); lineTo(-0.29f * s, -0.52f * s); close() }, Brush.linearGradient(0.0f to Color(0xFF7FA3D6), 1.0f to Color(0xFF1E3050), start = Offset(0.0f * s, -0.92f * s), end = Offset(0.0f * s, -0.52f * s)))
    drawPath(Path().apply { moveTo(-0.32f * s, -0.84f * s); quadraticTo(0.0f * s, -0.92f * s, 0.32f * s, -0.84f * s); lineTo(0.29f * s, -0.52f * s); lineTo(-0.29f * s, -0.52f * s); close() }, tone(body, d = 0.45f), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.26f * s, -0.83f * s); lineTo(-0.15f * s, -0.85f * s); lineTo(-0.16f * s, -0.535f * s); lineTo(-0.26f * s, -0.535f * s); close() }, tone(Color(0xFFFFFFFF), a = 0.34f))
    drawRoundRect(Brush.linearGradient(0.0f to tone(body, l = 0.22f), 1.0f to tone(body, l = 0.08f), start = Offset(0.0f * s, -0.54f * s), end = Offset(0.0f * s, -0.2f * s)), Offset(-0.3f * s, -0.54f * s), Size(0.6f * s, 0.34f * s), CornerRadius(0.12f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.3f * s, -0.54f * s), Size(0.6f * s, 0.34f * s), CornerRadius(0.12f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.18f), Offset(-0.25f * s, -0.49f * s), Size(0.18f * s, 0.24f * s), CornerRadius(0.072f * s))
    drawRoundRect(Brush.linearGradient(0.0f to tone(body, l = 0.22f), 0.3f to tone(body, l = 0.08f), 0.75f to body, 1.0f to tone(body, d = 0.24f), start = Offset(-0.4f * s, 0.0f * s), end = Offset(0.4f * s, 0.0f * s)), Offset(-0.4f * s, -0.2f * s), Size(0.8f * s, 1.6f * s), CornerRadius(0.09f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.4f * s, -0.2f * s), Size(0.8f * s, 1.6f * s), CornerRadius(0.09f * s), style = Stroke(0.018f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Brush.linearGradient(0.0f to Color(0xFF3A405A), 1.0f to Color(0xFF262B40), start = Offset(0.0f * s, -0.09f * s), end = Offset(0.0f * s, 1.29f * s)), Offset(-0.3f * s, -0.09f * s), Size(0.6f * s, 1.38f * s), CornerRadius(0.05f * s))
    drawRoundRect(Color(0xFFE2B878), Offset(-0.2f * s, 0.12f * s), Size(0.34f * s, 0.36f * s), CornerRadius(0.05f * s))
    drawRoundRect(Color(0xFF8C6535), Offset(-0.2f * s, 0.12f * s), Size(0.34f * s, 0.36f * s), CornerRadius(0.05f * s), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFC99A5C), Offset(-0.05f * s, 0.62f * s), Size(0.26f * s, 0.26f * s), CornerRadius(0.045f * s))
    drawRoundRect(Color(0xFF8C6535), Offset(-0.05f * s, 0.62f * s), Size(0.26f * s, 0.26f * s), CornerRadius(0.045f * s), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(body, d = 0.35f), Offset(-0.4f * s, 1.33f * s), Size(0.8f * s, 0.07f * s), CornerRadius(0.035f * s))
    drawRoundRect(tone(Color(0xFFF4F6FA), a = 0.95f), Offset(-0.25f * s, -1.415f * s), Size(0.5f * s, 0.05f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFFFFFFF), Offset(-0.365f * s, -1.375f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(-0.365f * s, -1.375f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFFFFFFF), Offset(0.195f * s, -1.375f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(0.195f * s, -1.375f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFE5352B), Offset(-0.37f * s, 1.33f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFE5352B), Offset(0.23f * s, 1.33f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.3f * s); cubicTo(0.4f * s, 1.38f * s, 0.37f * s, 1.4f * s, 0.3f * s, 1.4f * s); lineTo(-0.3f * s, 1.4f * s); cubicTo(-0.37f * s, 1.4f * s, -0.4f * s, 1.38f * s, -0.4f * s, 1.3f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, tone(body, d = 0.5f), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.3f * s); cubicTo(0.4f * s, 1.38f * s, 0.37f * s, 1.4f * s, 0.3f * s, 1.4f * s); lineTo(-0.3f * s, 1.4f * s); cubicTo(-0.37f * s, 1.4f * s, -0.4f * s, 1.38f * s, -0.4f * s, 1.3f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, Brush.linearGradient(0.0f to tone(Color(0xFFFFFFFF), a = 0.7f), 0.4f to tone(Color(0xFFFFFFFF), a = 0.0f), 0.62f to tone(Color(0xFF000000), a = 0.0f), 1.0f to tone(Color(0xFF000000), a = 0.4f), start = Offset(-0.4f * s, -1.4f * s), end = Offset(0.32f * s, 1.4f * s)), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.lowerBox(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -1.19f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -1.0f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -1.19f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -1.0f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.76f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.95f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.76f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.95f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.3f * s); cubicTo(0.4f * s, 1.38f * s, 0.37f * s, 1.4f * s, 0.3f * s, 1.4f * s); lineTo(-0.3f * s, 1.4f * s); cubicTo(-0.37f * s, 1.4f * s, -0.4f * s, 1.38f * s, -0.4f * s, 1.3f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, tone(body, d = 0.45f))
}

private fun DrawScope.upperBox(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -1.22f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -1.03f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -1.22f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -1.03f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.73f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.92f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.73f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.92f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, -0.5f * s); lineTo(-0.4f * s, -0.5f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, Brush.linearGradient(0.0f to tone(body, l = 0.3f), 0.3f to tone(body, l = 0.08f), 0.75f to body, 1.0f to tone(body, d = 0.22f), start = Offset(-0.4f * s, 0.0f * s), end = Offset(0.4f * s, 0.0f * s)))
    drawOval(tone(Color(0xFFFFFFFF), a = 0.3f), Offset(-0.28f * s, -1.36f * s), Size(0.3f * s, 0.16f * s))
    drawPath(Path().apply { moveTo(-0.34f * s, -1.22f * s); quadraticTo(0.0f * s, -1.3f * s, 0.34f * s, -1.22f * s); lineTo(0.3f * s, -0.9f * s); lineTo(-0.3f * s, -0.9f * s); close() }, Brush.linearGradient(0.0f to Color(0xFF7FA3D6), 1.0f to Color(0xFF1E3050), start = Offset(0.0f * s, -1.3f * s), end = Offset(0.0f * s, -0.9f * s)))
    drawPath(Path().apply { moveTo(-0.34f * s, -1.22f * s); quadraticTo(0.0f * s, -1.3f * s, 0.34f * s, -1.22f * s); lineTo(0.3f * s, -0.9f * s); lineTo(-0.3f * s, -0.9f * s); close() }, tone(body, d = 0.45f), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.28f * s, -1.21f * s); lineTo(-0.17f * s, -1.23f * s); lineTo(-0.17f * s, -0.915f * s); lineTo(-0.27f * s, -0.915f * s); close() }, tone(Color(0xFFFFFFFF), a = 0.34f))
    drawRoundRect(Brush.linearGradient(0.0f to tone(body, l = 0.22f), 1.0f to tone(body, l = 0.08f), start = Offset(0.0f * s, -0.91f * s), end = Offset(0.0f * s, -0.53f * s)), Offset(-0.31f * s, -0.91f * s), Size(0.62f * s, 0.38f * s), CornerRadius(0.1f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.31f * s, -0.91f * s), Size(0.62f * s, 0.38f * s), CornerRadius(0.1f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.18f), Offset(-0.26f * s, -0.86f * s), Size(0.186f * s, 0.28f * s), CornerRadius(0.06f * s))
    drawRoundRect(Brush.linearGradient(0.0f to tone(body, l = 0.78f), 0.35f to tone(body, l = 0.68f), 0.8f to tone(body, l = 0.56f), 1.0f to tone(body, l = 0.4f), start = Offset(-0.4f * s, 0.0f * s), end = Offset(0.4f * s, 0.0f * s)), Offset(-0.4f * s, -0.48f * s), Size(0.8f * s, 1.9f * s), CornerRadius(0.09f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.4f * s, -0.48f * s), Size(0.8f * s, 1.9f * s), CornerRadius(0.09f * s), style = Stroke(0.018f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(body, Offset(-0.15f * s, -0.38f * s), Size(0.3f * s, 1.72f * s), CornerRadius(0.06f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.15f * s, -0.38f * s), Size(0.3f * s, 1.72f * s), CornerRadius(0.06f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.3f), Offset(-0.08f * s, -0.3f * s), Size(0.16f * s, 1.56f * s), CornerRadius(0.04f * s))
    drawRoundRect(tone(Color(0xFF000000), a = 0.5f), Offset(-0.4f * s, -0.5f * s), Size(0.8f * s, 0.05f * s), CornerRadius(0.025f * s))
    drawRoundRect(tone(Color(0xFFF4F6FA), a = 0.95f), Offset(-0.25f * s, -1.415f * s), Size(0.5f * s, 0.05f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFFFFFFF), Offset(-0.365f * s, -1.375f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(-0.365f * s, -1.375f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFFFFFFF), Offset(0.195f * s, -1.375f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(0.195f * s, -1.375f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFE5352B), Offset(-0.37f * s, 1.34f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFE5352B), Offset(0.23f * s, 1.34f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.3f * s); cubicTo(0.4f * s, 1.38f * s, 0.37f * s, 1.4f * s, 0.3f * s, 1.4f * s); lineTo(-0.3f * s, 1.4f * s); cubicTo(-0.37f * s, 1.4f * s, -0.4f * s, 1.38f * s, -0.4f * s, 1.3f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, tone(body, d = 0.5f), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.3f * s); cubicTo(0.4f * s, 1.38f * s, 0.37f * s, 1.4f * s, 0.3f * s, 1.4f * s); lineTo(-0.3f * s, 1.4f * s); cubicTo(-0.37f * s, 1.4f * s, -0.4f * s, 1.38f * s, -0.4f * s, 1.3f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, Brush.linearGradient(0.0f to tone(Color(0xFFFFFFFF), a = 0.7f), 0.4f to tone(Color(0xFFFFFFFF), a = 0.0f), 0.62f to tone(Color(0xFF000000), a = 0.0f), 1.0f to tone(Color(0xFF000000), a = 0.4f), start = Offset(-0.4f * s, -1.4f * s), end = Offset(0.32f * s, 1.4f * s)), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.lowerVan(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -1.11f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -0.92f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -1.11f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -0.92f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.71f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.9f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.71f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.9f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.15f * s); cubicTo(0.4f * s, 1.32f * s, 0.36f * s, 1.4f * s, 0.28f * s, 1.4f * s); cubicTo(0.15f * s, 1.42f * s, -0.15f * s, 1.42f * s, -0.28f * s, 1.4f * s); cubicTo(-0.36f * s, 1.4f * s, -0.4f * s, 1.32f * s, -0.4f * s, 1.15f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, tone(body, d = 0.45f))
}

private fun DrawScope.upperVan(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -1.14f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -0.95f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -1.14f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -0.95f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.68f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.87f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.68f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.87f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.15f * s); cubicTo(0.4f * s, 1.32f * s, 0.36f * s, 1.4f * s, 0.28f * s, 1.4f * s); cubicTo(0.15f * s, 1.42f * s, -0.15f * s, 1.42f * s, -0.28f * s, 1.4f * s); cubicTo(-0.36f * s, 1.4f * s, -0.4f * s, 1.32f * s, -0.4f * s, 1.15f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, Brush.linearGradient(0.0f to tone(body, l = 0.3f), 0.3f to tone(body, l = 0.08f), 0.75f to body, 1.0f to tone(body, d = 0.22f), start = Offset(-0.4f * s, 0.0f * s), end = Offset(0.4f * s, 0.0f * s)))
    drawOval(tone(Color(0xFFFFFFFF), a = 0.3f), Offset(-0.28f * s, -1.31f * s), Size(0.3f * s, 0.18f * s))
    drawPath(Path().apply { moveTo(-0.34f * s, -1.02f * s); quadraticTo(0.0f * s, -1.11f * s, 0.34f * s, -1.02f * s); lineTo(0.3f * s, -0.66f * s); lineTo(-0.3f * s, -0.66f * s); close() }, Brush.linearGradient(0.0f to Color(0xFF7FA3D6), 1.0f to Color(0xFF1E3050), start = Offset(0.0f * s, -1.11f * s), end = Offset(0.0f * s, -0.66f * s)))
    drawPath(Path().apply { moveTo(-0.34f * s, -1.02f * s); quadraticTo(0.0f * s, -1.11f * s, 0.34f * s, -1.02f * s); lineTo(0.3f * s, -0.66f * s); lineTo(-0.3f * s, -0.66f * s); close() }, tone(body, d = 0.45f), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.28f * s, -1.01f * s); lineTo(-0.17f * s, -1.03f * s); lineTo(-0.17f * s, -0.675f * s); lineTo(-0.27f * s, -0.675f * s); close() }, tone(Color(0xFFFFFFFF), a = 0.34f))
    drawRoundRect(Brush.linearGradient(0.0f to tone(body, l = 0.2f), 1.0f to tone(body, l = 0.06f), start = Offset(0.0f * s, -0.68f * s), end = Offset(0.0f * s, 1.22f * s)), Offset(-0.34f * s, -0.68f * s), Size(0.68f * s, 1.9f * s), CornerRadius(0.15f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.34f * s, -0.68f * s), Size(0.68f * s, 1.9f * s), CornerRadius(0.15f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.2f), Offset(-0.27f * s, -0.6f * s), Size(0.12f * s, 1.74f * s), CornerRadius(0.05f * s))
    drawRoundRect(tone(body, l = 0.3f), Offset(-0.22f * s, -0.35f * s), Size(0.44f * s, 0.58f * s), CornerRadius(0.09f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.22f * s, -0.35f * s), Size(0.44f * s, 0.58f * s), CornerRadius(0.09f * s), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.22f), Offset(-0.14f * s, -0.27f * s), Size(0.2f * s, 0.42f * s), CornerRadius(0.05f * s))
    drawRoundRect(Brush.linearGradient(0.0f to Color(0xFF7FA3D6), 1.0f to Color(0xFF1E3050), start = Offset(0.0f * s, 1.02f * s), end = Offset(0.0f * s, 1.16f * s)), Offset(-0.27f * s, 1.02f * s), Size(0.54f * s, 0.14f * s), CornerRadius(0.05f * s))
    drawRoundRect(tone(body, d = 0.45f), Offset(-0.27f * s, 1.02f * s), Size(0.54f * s, 0.14f * s), CornerRadius(0.05f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(Color(0xFFF4F6FA), a = 0.95f), Offset(-0.25f * s, -1.415f * s), Size(0.5f * s, 0.05f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFFFFFFF), Offset(-0.355f * s, -1.375f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(-0.355f * s, -1.375f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFFFFFFF), Offset(0.185f * s, -1.375f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(0.185f * s, -1.375f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFE5352B), Offset(-0.36f * s, 1.33f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFE5352B), Offset(0.22f * s, 1.33f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.15f * s); cubicTo(0.4f * s, 1.32f * s, 0.36f * s, 1.4f * s, 0.28f * s, 1.4f * s); cubicTo(0.15f * s, 1.42f * s, -0.15f * s, 1.42f * s, -0.28f * s, 1.4f * s); cubicTo(-0.36f * s, 1.4f * s, -0.4f * s, 1.32f * s, -0.4f * s, 1.15f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, tone(body, d = 0.5f), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.4f * s); cubicTo(-0.18f * s, -1.425f * s, 0.18f * s, -1.425f * s, 0.3f * s, -1.4f * s); cubicTo(0.38f * s, -1.36f * s, 0.4f * s, -1.2f * s, 0.4f * s, -1.0f * s); lineTo(0.4f * s, 1.15f * s); cubicTo(0.4f * s, 1.32f * s, 0.36f * s, 1.4f * s, 0.28f * s, 1.4f * s); cubicTo(0.15f * s, 1.42f * s, -0.15f * s, 1.42f * s, -0.28f * s, 1.4f * s); cubicTo(-0.36f * s, 1.4f * s, -0.4f * s, 1.32f * s, -0.4f * s, 1.15f * s); lineTo(-0.4f * s, -1.0f * s); cubicTo(-0.4f * s, -1.2f * s, -0.38f * s, -1.36f * s, -0.3f * s, -1.4f * s); close() }, Brush.linearGradient(0.0f to tone(Color(0xFFFFFFFF), a = 0.7f), 0.4f to tone(Color(0xFFFFFFFF), a = 0.0f), 0.62f to tone(Color(0xFF000000), a = 0.0f), 1.0f to tone(Color(0xFF000000), a = 0.4f), start = Offset(-0.4f * s, -1.4f * s), end = Offset(0.32f * s, 1.4f * s)), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.lowerBus(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -1.44f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -1.25f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -1.44f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -1.25f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 1.01f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 1.2f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 1.01f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 1.2f * s))
    drawPath(Path().apply { moveTo(-0.32f * s, -1.9f * s); cubicTo(-0.2f * s, -1.925f * s, 0.2f * s, -1.925f * s, 0.32f * s, -1.9f * s); cubicTo(0.38f * s, -1.87f * s, 0.4f * s, -1.8f * s, 0.4f * s, -1.7f * s); lineTo(0.4f * s, 1.7f * s); cubicTo(0.4f * s, 1.82f * s, 0.36f * s, 1.9f * s, 0.3f * s, 1.9f * s); lineTo(-0.3f * s, 1.9f * s); cubicTo(-0.36f * s, 1.9f * s, -0.4f * s, 1.82f * s, -0.4f * s, 1.7f * s); lineTo(-0.4f * s, -1.7f * s); cubicTo(-0.4f * s, -1.8f * s, -0.38f * s, -1.87f * s, -0.32f * s, -1.9f * s); close() }, tone(body, d = 0.45f))
}

private fun DrawScope.upperBus(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -1.47f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -1.28f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -1.47f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -1.28f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.98f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 1.17f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.98f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 1.17f * s))
    drawRoundRect(Brush.linearGradient(0.0f to tone(body, l = 0.3f), 0.3f to tone(body, l = 0.08f), 0.75f to body, 1.0f to tone(body, d = 0.26f), start = Offset(-0.4f * s, 0.0f * s), end = Offset(0.4f * s, 0.0f * s)), Offset(-0.4f * s, -1.9f * s), Size(0.8f * s, 3.8f * s), CornerRadius(0.2f * s))
    drawRoundRect(Brush.linearGradient(0.0f to Color(0xFF3E5C93), 1.0f to Color(0xFF27406B), start = Offset(0.0f * s, -1.42f * s), end = Offset(0.0f * s, 1.5f * s)), Offset(-0.4f * s, -1.42f * s), Size(0.1f * s, 2.92f * s), CornerRadius(0.05f * s))
    drawRoundRect(tone(body, d = 0.5f), Offset(-0.4f * s, -1.42f * s), Size(0.1f * s, 2.92f * s), CornerRadius(0.05f * s), style = Stroke(0.014f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Brush.linearGradient(0.0f to Color(0xFF3E5C93), 1.0f to Color(0xFF27406B), start = Offset(0.0f * s, -1.42f * s), end = Offset(0.0f * s, 1.5f * s)), Offset(0.3f * s, -1.42f * s), Size(0.1f * s, 2.92f * s), CornerRadius(0.05f * s))
    drawRoundRect(tone(body, d = 0.5f), Offset(0.3f * s, -1.42f * s), Size(0.1f * s, 2.92f * s), CornerRadius(0.05f * s), style = Stroke(0.014f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Brush.linearGradient(0.0f to tone(body, l = 0.2f), 1.0f to tone(body, l = 0.04f), start = Offset(0.0f * s, -1.52f * s), end = Offset(0.0f * s, 1.82f * s)), Offset(-0.31f * s, -1.52f * s), Size(0.62f * s, 3.34f * s), CornerRadius(0.12f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.31f * s, -1.52f * s), Size(0.62f * s, 3.34f * s), CornerRadius(0.12f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.2f), Offset(-0.25f * s, -1.45f * s), Size(0.14f * s, 3.2f * s), CornerRadius(0.06f * s))
    drawRoundRect(tone(body, l = 0.42f), Offset(-0.15f * s, -0.9f * s), Size(0.3f * s, 0.46f * s), CornerRadius(0.07f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.15f * s, -0.9f * s), Size(0.3f * s, 0.46f * s), CornerRadius(0.07f * s), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(body, l = 0.42f), Offset(-0.15f * s, 0.25f * s), Size(0.3f * s, 0.46f * s), CornerRadius(0.07f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.15f * s, 0.25f * s), Size(0.3f * s, 0.46f * s), CornerRadius(0.07f * s), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.33f * s, -1.5f * s); quadraticTo(0.0f * s, -1.64f * s, 0.33f * s, -1.5f * s); lineTo(0.37f * s, -1.8f * s); quadraticTo(0.0f * s, -1.9f * s, -0.37f * s, -1.8f * s); close() }, Brush.linearGradient(0.0f to Color(0xFF7FA3D6), 1.0f to Color(0xFF1E3050), start = Offset(0.0f * s, -1.88f * s), end = Offset(0.0f * s, -1.5f * s)))
    drawPath(Path().apply { moveTo(-0.33f * s, -1.5f * s); quadraticTo(0.0f * s, -1.64f * s, 0.33f * s, -1.5f * s); lineTo(0.37f * s, -1.8f * s); quadraticTo(0.0f * s, -1.9f * s, -0.37f * s, -1.8f * s); close() }, tone(body, d = 0.45f), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.58f * s); lineTo(-0.15f * s, -1.6f * s); lineTo(-0.21f * s, -1.8f * s); lineTo(-0.34f * s, -1.78f * s); close() }, tone(Color(0xFFFFFFFF), a = 0.34f))
    drawRoundRect(Brush.linearGradient(0.0f to Color(0xFF7FA3D6), 1.0f to Color(0xFF1E3050), start = Offset(0.0f * s, 1.74f * s), end = Offset(0.0f * s, 1.84f * s)), Offset(-0.28f * s, 1.74f * s), Size(0.56f * s, 0.1f * s), CornerRadius(0.04f * s))
    drawRoundRect(Color(0xFFFFFFFF), Offset(-0.355f * s, -1.885f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(-0.355f * s, -1.885f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFFFFFFF), Offset(0.185f * s, -1.885f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(0.185f * s, -1.885f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFE5352B), Offset(-0.36f * s, 1.84f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFE5352B), Offset(0.22f * s, 1.84f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawPath(Path().apply { moveTo(-0.32f * s, -1.9f * s); cubicTo(-0.2f * s, -1.925f * s, 0.2f * s, -1.925f * s, 0.32f * s, -1.9f * s); cubicTo(0.38f * s, -1.87f * s, 0.4f * s, -1.8f * s, 0.4f * s, -1.7f * s); lineTo(0.4f * s, 1.7f * s); cubicTo(0.4f * s, 1.82f * s, 0.36f * s, 1.9f * s, 0.3f * s, 1.9f * s); lineTo(-0.3f * s, 1.9f * s); cubicTo(-0.36f * s, 1.9f * s, -0.4f * s, 1.82f * s, -0.4f * s, 1.7f * s); lineTo(-0.4f * s, -1.7f * s); cubicTo(-0.4f * s, -1.8f * s, -0.38f * s, -1.87f * s, -0.32f * s, -1.9f * s); close() }, tone(body, d = 0.5f), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.32f * s, -1.9f * s); cubicTo(-0.2f * s, -1.925f * s, 0.2f * s, -1.925f * s, 0.32f * s, -1.9f * s); cubicTo(0.38f * s, -1.87f * s, 0.4f * s, -1.8f * s, 0.4f * s, -1.7f * s); lineTo(0.4f * s, 1.7f * s); cubicTo(0.4f * s, 1.82f * s, 0.36f * s, 1.9f * s, 0.3f * s, 1.9f * s); lineTo(-0.3f * s, 1.9f * s); cubicTo(-0.36f * s, 1.9f * s, -0.4f * s, 1.82f * s, -0.4f * s, 1.7f * s); lineTo(-0.4f * s, -1.7f * s); cubicTo(-0.4f * s, -1.8f * s, -0.38f * s, -1.87f * s, -0.32f * s, -1.9f * s); close() }, Brush.linearGradient(0.0f to tone(Color(0xFFFFFFFF), a = 0.7f), 0.4f to tone(Color(0xFFFFFFFF), a = 0.0f), 0.62f to tone(Color(0xFF000000), a = 0.0f), 1.0f to tone(Color(0xFF000000), a = 0.4f), start = Offset(-0.4f * s, -1.9f * s), end = Offset(0.32f * s, 1.9f * s)), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.lowerTanker(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -1.64f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -1.45f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -1.64f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -1.45f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.81f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 1.0f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.81f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 1.0f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 1.31f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 1.5f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 1.31f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 1.5f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.9f * s); cubicTo(-0.18f * s, -1.925f * s, 0.18f * s, -1.925f * s, 0.3f * s, -1.9f * s); cubicTo(0.38f * s, -1.86f * s, 0.4f * s, -1.7f * s, 0.4f * s, -1.5f * s); lineTo(0.4f * s, 1.85f * s); cubicTo(0.4f * s, 1.88f * s, 0.38f * s, 1.9f * s, 0.34f * s, 1.9f * s); lineTo(-0.34f * s, 1.9f * s); cubicTo(-0.38f * s, 1.9f * s, -0.4f * s, 1.88f * s, -0.4f * s, 1.85f * s); lineTo(-0.4f * s, -1.5f * s); cubicTo(-0.4f * s, -1.7f * s, -0.38f * s, -1.86f * s, -0.3f * s, -1.9f * s); close() }, tone(body, d = 0.45f))
}

private fun DrawScope.upperTanker(body: Color, s: Float) {
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, -1.67f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, -1.48f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, -1.67f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, -1.48f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 0.78f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 0.97f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 0.78f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 0.97f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(-0.445f * s, 1.28f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(-0.4f * s, 1.47f * s))
    drawRoundRect(Color(0xFF1E2433), Offset(0.275f * s, 1.28f * s), Size(0.17f * s, 0.38f * s), CornerRadius(0.08f * s))
    drawCircle(Color(0xFF4A5470), 0.026f * s, Offset(0.4f * s, 1.47f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.9f * s); cubicTo(-0.18f * s, -1.925f * s, 0.18f * s, -1.925f * s, 0.3f * s, -1.9f * s); cubicTo(0.38f * s, -1.86f * s, 0.4f * s, -1.7f * s, 0.4f * s, -1.5f * s); lineTo(0.4f * s, -0.98f * s); lineTo(-0.4f * s, -0.98f * s); lineTo(-0.4f * s, -1.5f * s); cubicTo(-0.4f * s, -1.7f * s, -0.38f * s, -1.86f * s, -0.3f * s, -1.9f * s); close() }, Brush.linearGradient(0.0f to tone(body, l = 0.3f), 0.3f to tone(body, l = 0.08f), 0.75f to body, 1.0f to tone(body, d = 0.22f), start = Offset(-0.4f * s, 0.0f * s), end = Offset(0.4f * s, 0.0f * s)))
    drawOval(tone(Color(0xFFFFFFFF), a = 0.3f), Offset(-0.28f * s, -1.82f * s), Size(0.3f * s, 0.16f * s))
    drawPath(Path().apply { moveTo(-0.34f * s, -1.66f * s); quadraticTo(0.0f * s, -1.74f * s, 0.34f * s, -1.66f * s); lineTo(0.3f * s, -1.36f * s); lineTo(-0.3f * s, -1.36f * s); close() }, Brush.linearGradient(0.0f to Color(0xFF7FA3D6), 1.0f to Color(0xFF1E3050), start = Offset(0.0f * s, -1.74f * s), end = Offset(0.0f * s, -1.36f * s)))
    drawPath(Path().apply { moveTo(-0.34f * s, -1.66f * s); quadraticTo(0.0f * s, -1.74f * s, 0.34f * s, -1.66f * s); lineTo(0.3f * s, -1.36f * s); lineTo(-0.3f * s, -1.36f * s); close() }, tone(body, d = 0.45f), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.28f * s, -1.65f * s); lineTo(-0.17f * s, -1.67f * s); lineTo(-0.17f * s, -1.375f * s); lineTo(-0.27f * s, -1.375f * s); close() }, tone(Color(0xFFFFFFFF), a = 0.34f))
    drawRoundRect(Brush.linearGradient(0.0f to tone(body, l = 0.22f), 1.0f to tone(body, l = 0.08f), start = Offset(0.0f * s, -1.37f * s), end = Offset(0.0f * s, -1.01f * s)), Offset(-0.31f * s, -1.37f * s), Size(0.62f * s, 0.36f * s), CornerRadius(0.1f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.31f * s, -1.37f * s), Size(0.62f * s, 0.36f * s), CornerRadius(0.1f * s), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.18f), Offset(-0.26f * s, -1.32f * s), Size(0.186f * s, 0.26f * s), CornerRadius(0.06f * s))
    drawRoundRect(Brush.linearGradient(0.0f to Color(0xFF8C97AC), 0.2f to Color(0xFFE6EBF4), 0.4f to Color(0xFFFFFFFF), 0.7f to Color(0xFFC5CDDC), 1.0f to Color(0xFF76819A), start = Offset(-0.39f * s, 0.0f * s), end = Offset(0.39f * s, 0.0f * s)), Offset(-0.39f * s, -0.92f * s), Size(0.78f * s, 2.82f * s), CornerRadius(0.36f * s))
    drawRoundRect(Color(0xFF5E6880), Offset(-0.39f * s, -0.92f * s), Size(0.78f * s, 2.82f * s), CornerRadius(0.36f * s), style = Stroke(0.02f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawLine(tone(Color(0xFF5E6880), a = 0.45f), Offset(-0.38f * s, -0.4f * s), Offset(0.38f * s, -0.4f * s), 0.022f * s, StrokeCap.Round)
    drawLine(tone(Color(0xFF5E6880), a = 0.45f), Offset(-0.38f * s, 0.5f * s), Offset(0.38f * s, 0.5f * s), 0.022f * s, StrokeCap.Round)
    drawLine(tone(Color(0xFF5E6880), a = 0.45f), Offset(-0.38f * s, 1.4f * s), Offset(0.38f * s, 1.4f * s), 0.022f * s, StrokeCap.Round)
    drawRoundRect(body, Offset(-0.39f * s, 0.0f * s), Size(0.78f * s, 0.32f * s), CornerRadius(0.0f * s))
    drawRoundRect(tone(body, d = 0.3f), Offset(-0.39f * s, 0.0f * s), Size(0.78f * s, 0.05f * s), CornerRadius(0.0f * s))
    drawCircle(Color(0xFF5E6880), 0.14f * s, Offset(0.0f * s, -0.55f * s))
    drawCircle(Color(0xFFB4BDCE), 0.085f * s, Offset(0.0f * s, -0.55f * s))
    drawCircle(Color(0xFF5E6880), 0.14f * s, Offset(0.0f * s, 1.15f * s))
    drawCircle(Color(0xFFB4BDCE), 0.085f * s, Offset(0.0f * s, 1.15f * s))
    drawRoundRect(tone(Color(0xFFFFFFFF), a = 0.55f), Offset(-0.2f * s, -0.4f * s), Size(0.07f * s, 1.4f * s), CornerRadius(0.035f * s))
    drawRoundRect(tone(Color(0xFFF4F6FA), a = 0.95f), Offset(-0.25f * s, -1.915f * s), Size(0.5f * s, 0.05f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFFFFFFF), Offset(-0.365f * s, -1.875f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(-0.365f * s, -1.875f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFFFFFFF), Offset(0.195f * s, -1.875f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s))
    drawRoundRect(tone(body, d = 0.4f), Offset(0.195f * s, -1.875f * s), Size(0.17f * s, 0.09f * s), CornerRadius(0.045f * s), style = Stroke(0.016f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawRoundRect(Color(0xFFE5352B), Offset(-0.35f * s, 1.84f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawRoundRect(Color(0xFFE5352B), Offset(0.21f * s, 1.84f * s), Size(0.14f * s, 0.06f * s), CornerRadius(0.025f * s))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.9f * s); cubicTo(-0.18f * s, -1.925f * s, 0.18f * s, -1.925f * s, 0.3f * s, -1.9f * s); cubicTo(0.38f * s, -1.86f * s, 0.4f * s, -1.7f * s, 0.4f * s, -1.5f * s); lineTo(0.4f * s, 1.85f * s); cubicTo(0.4f * s, 1.88f * s, 0.38f * s, 1.9f * s, 0.34f * s, 1.9f * s); lineTo(-0.34f * s, 1.9f * s); cubicTo(-0.38f * s, 1.9f * s, -0.4f * s, 1.88f * s, -0.4f * s, 1.85f * s); lineTo(-0.4f * s, -1.5f * s); cubicTo(-0.4f * s, -1.7f * s, -0.38f * s, -1.86f * s, -0.3f * s, -1.9f * s); close() }, tone(body, d = 0.5f), style = Stroke(0.022f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(Path().apply { moveTo(-0.3f * s, -1.9f * s); cubicTo(-0.18f * s, -1.925f * s, 0.18f * s, -1.925f * s, 0.3f * s, -1.9f * s); cubicTo(0.38f * s, -1.86f * s, 0.4f * s, -1.7f * s, 0.4f * s, -1.5f * s); lineTo(0.4f * s, 1.85f * s); cubicTo(0.4f * s, 1.88f * s, 0.38f * s, 1.9f * s, 0.34f * s, 1.9f * s); lineTo(-0.34f * s, 1.9f * s); cubicTo(-0.38f * s, 1.9f * s, -0.4f * s, 1.88f * s, -0.4f * s, 1.85f * s); lineTo(-0.4f * s, -1.5f * s); cubicTo(-0.4f * s, -1.7f * s, -0.38f * s, -1.86f * s, -0.3f * s, -1.9f * s); close() }, Brush.linearGradient(0.0f to tone(Color(0xFFFFFFFF), a = 0.7f), 0.4f to tone(Color(0xFFFFFFFF), a = 0.0f), 0.62f to tone(Color(0xFF000000), a = 0.0f), 1.0f to tone(Color(0xFF000000), a = 0.4f), start = Offset(-0.4f * s, -1.9f * s), end = Offset(0.32f * s, 1.9f * s)), style = Stroke(0.03f * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
}
