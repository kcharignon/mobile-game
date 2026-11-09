package com.kcharignon.petitsjeux.core.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcharignon.petitsjeux.core.Games
import com.kcharignon.petitsjeux.core.save.Avatar
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.core.save.Ranks
import com.kcharignon.petitsjeux.core.ui.AvatarChoice
import com.kcharignon.petitsjeux.core.ui.AvatarFace
import com.kcharignon.petitsjeux.core.ui.Backdrop
import com.kcharignon.petitsjeux.core.ui.ChunkyButton
import com.kcharignon.petitsjeux.core.ui.IconKind
import com.kcharignon.petitsjeux.core.ui.Modal
import com.kcharignon.petitsjeux.core.ui.Panel
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.core.ui.PjFont
import com.kcharignon.petitsjeux.core.ui.PjIcon
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.core.ui.consumeTaps
import com.kcharignon.petitsjeux.core.ui.drawStar
import com.kcharignon.petitsjeux.games.parking.ui.drawCar

/**
 * L'écran d'accueil de l'appli : « Qui joue ? ». Cinq emplacements, un par sauvegarde locale.
 * Un joueur existant se touche pour jouer ; un emplacement vide propose d'en créer un.
 */
@Composable
fun ProfilesScreen(
    profiles: List<Profile?>,
    onPlay: (slot: Int) -> Unit,
    onCreate: (slot: Int, name: String, avatar: Avatar) -> Unit,
    modifier: Modifier = Modifier,
    /** Pour les captures d'écran : ouvre tout de suite la fenêtre « Nouveau joueur » de cet emplacement. */
    initialCreating: Int? = null,
) {
    var creating by rememberSaveable { mutableStateOf(initialCreating) }
    Backdrop(
        modifier,
        overlay = {
            creating?.let { slot ->
                NewPlayerCard(
                    onCancel = { creating = null },
                    onCreate = { name, avatar ->
                        creating = null
                        onCreate(slot, name, avatar)
                    },
                )
            }
        },
    ) {
        Column(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxWidth().padding(top = 28.dp), contentAlignment = Alignment.Center) {
                Canvas(Modifier.fillMaxWidth().height(64.dp)) {
                    val s = 30.dp.toPx()
                    drawCar(2, Pj.CarColors[4], s, Offset(20.dp.toPx() + s, size.height / 2), 90f)
                    drawCar(2, Pj.CarColors[2], s, Offset(size.width - 20.dp.toPx() - s, size.height / 2), 270f)
                }
                PjText("Petits Jeux", size = 40.sp, weight = FontWeight.Bold, color = Color.White, outline = Pj.Ink, outlineWidth = 9.dp)
            }
            PjText("Qui joue ?", Modifier.fillMaxWidth().padding(top = 2.dp, bottom = 10.dp), size = 24.sp, align = TextAlign.Center)
            Column(
                Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                for (slot in 0 until Profile.SLOTS) {
                    val profile = profiles.getOrNull(slot)
                    if (profile != null) PlayerCard(profile, onClick = { onPlay(slot) }) else EmptySlotCard(onClick = { creating = slot })
                }
                PjText(
                    "Chaque joueur garde sa progression sur ce téléphone.\nAucune pub, aucun réseau.",
                    Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 16.dp),
                    size = 14.sp, weight = FontWeight.Medium, color = Pj.Ink.copy(alpha = 0.6f), align = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun PlayerCard(profile: Profile, onClick: () -> Unit) {
    val stars = Games.totalStars(profile)
    Panel(
        Modifier.fillMaxWidth().clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AvatarFace(profile.avatar, 68.dp)
            Column(Modifier.weight(1f)) {
                PjText(profile.name, size = 28.sp, weight = FontWeight.Bold, maxLines = 1)
                PjText(Ranks.of(stars).name, size = 15.sp, weight = FontWeight.Medium, color = Pj.InkSoft, maxLines = 1)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Canvas(Modifier.size(30.dp)) { drawStar(center, 13.dp.toPx(), on = true) }
                PjText("$stars", size = 20.sp, weight = FontWeight.Bold, color = Pj.StarDark)
            }
        }
    }
}

@Composable
private fun EmptySlotCard(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(94.dp)
            .background(Color.White.copy(alpha = 0.35f), RoundedCornerShape(30.dp))
            .drawBehind {
                drawRoundRect(
                    Color.White, Offset(1.5.dp.toPx(), 1.5.dp.toPx()), Size(size.width - 3.dp.toPx(), size.height - 3.dp.toPx()), CornerRadius(30.dp.toPx()),
                    style = Stroke(3.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(12.dp.toPx(), 10.dp.toPx()))),
                )
            }
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(Modifier.padding(horizontal = 18.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.size(68.dp).background(Color.White.copy(alpha = 0.55f), CircleShape), contentAlignment = Alignment.Center) {
                PjIcon(IconKind.PLUS, size = 30.dp, tint = Pj.InkSoft)
            }
            PjText("Nouveau joueur", size = 24.sp, color = Pj.Ink.copy(alpha = 0.75f))
        }
    }
}

/** « Nouveau joueur » : le prénom (12 lettres au plus) et l'animal. */
@Composable
fun NewPlayerCard(onCancel: () -> Unit, onCreate: (name: String, avatar: Avatar) -> Unit, initialName: String = "", initialAvatar: Avatar = Avatar.CAT) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var avatar by rememberSaveable { mutableStateOf(initialAvatar) }
    val valid = Profile.cleanName(name) != null
    fun submit() {
        if (valid) onCreate(Profile.cleanName(name)!!, avatar)
    }
    Modal(onDismiss = onCancel) {
        Panel(
            Modifier.padding(horizontal = 22.dp).fillMaxWidth().consumeTaps(),
            corner = 36.dp, ledge = 8.dp,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 22.dp),
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                PjText("Nouveau joueur", size = 30.sp, weight = FontWeight.Bold)
                PjText("Ton prénom", Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 6.dp), size = 15.sp, color = Pj.InkSoft)
                BasicTextField(
                    value = name,
                    onValueChange = { name = it.take(Profile.NAME_MAX).filterNot { c -> c == '\n' } },
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = PjFont, fontSize = 28.sp, fontWeight = FontWeight.SemiBold, color = Pj.Ink),
                    cursorBrush = SolidColor(Pj.Blue),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { submit() }),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { inner ->
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(58.dp)
                                .background(Color.White, RoundedCornerShape(18.dp))
                                .drawBehind { drawRoundRect(Pj.PanelEdge, cornerRadius = CornerRadius(18.dp.toPx()), style = Stroke(3.dp.toPx())) }
                                .padding(horizontal = 18.dp),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            if (name.isEmpty()) PjText("Écris ton prénom", size = 24.sp, weight = FontWeight.Medium, color = Pj.GrayDark)
                            inner()
                        }
                    },
                )
                PjText("Ton animal", Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 2.dp), size = 15.sp, color = Pj.InkSoft)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (rowItems in Avatar.entries.chunked(4)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            for (a in rowItems) AvatarChoice(a, selected = a == avatar, onClick = { avatar = a }, size = 58.dp)
                        }
                    }
                }
                ChunkyButton("C’est parti !", onClick = ::submit, modifier = Modifier.padding(top = 14.dp).fillMaxWidth(), enabled = valid, height = 62.dp, fontSize = 28.sp)
            }
        }
    }
}
