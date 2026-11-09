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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcharignon.petitsjeux.core.Games
import com.kcharignon.petitsjeux.core.MiniGame
import com.kcharignon.petitsjeux.core.save.Profile
import com.kcharignon.petitsjeux.core.save.Ranks
import com.kcharignon.petitsjeux.core.save.Settings
import com.kcharignon.petitsjeux.core.ui.AvatarFace
import com.kcharignon.petitsjeux.core.ui.Backdrop
import com.kcharignon.petitsjeux.core.ui.ChunkyButton
import com.kcharignon.petitsjeux.core.ui.IconKind
import com.kcharignon.petitsjeux.core.ui.Modal
import com.kcharignon.petitsjeux.core.ui.Panel
import com.kcharignon.petitsjeux.core.ui.Pj
import com.kcharignon.petitsjeux.core.ui.PjIcon
import com.kcharignon.petitsjeux.core.ui.PjSwitch
import com.kcharignon.petitsjeux.core.ui.PjText
import com.kcharignon.petitsjeux.core.ui.RoundIconButton
import com.kcharignon.petitsjeux.core.ui.consumeTaps
import com.kcharignon.petitsjeux.core.ui.withTapSound
import com.kcharignon.petitsjeux.core.ui.drawStar

/** L'accueil d'un joueur : qui il est, son rang, et les jeux. */
@Composable
fun HubScreen(
    profile: Profile,
    onPlay: (MiniGame) -> Unit,
    onSwitchPlayer: () -> Unit,
    onSettingsChange: (Settings) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    /** Pour les captures d'écran : ouvre tout de suite les réglages. */
    initialSettingsOpen: Boolean = false,
) {
    var settingsOpen by rememberSaveable { mutableStateOf(initialSettingsOpen) }
    androidx.activity.compose.BackHandler(enabled = !settingsOpen, onBack = onSwitchPlayer)
    Backdrop(
        modifier,
        overlay = {
            if (settingsOpen) SettingsCard(profile, onSettingsChange, onDelete = { settingsOpen = false; onDelete() }, onClose = { settingsOpen = false })
        },
    ) {
        Column(Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            PlayerBar(profile, Modifier.padding(top = 20.dp))
            PjText("Choisis ton jeu", Modifier.padding(start = 10.dp, top = 18.dp, bottom = 10.dp), size = 22.sp, weight = FontWeight.Bold)
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                for (game in Games.all) GameCard(game, profile, onClick = { onPlay(game) })
                SoonCard()
                Box(Modifier.height(4.dp))
            }
            Row(Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                ChunkyButton("Changer de joueur", onSwitchPlayer, Modifier.weight(1f), color = Pj.Blue, dark = Pj.BlueDark, height = 60.dp, fontSize = 21.sp)
                RoundIconButton(IconKind.GEAR, onClick = { settingsOpen = true }, diameter = 60.dp)
            }
        }
    }
}

@Composable
private fun PlayerBar(profile: Profile, modifier: Modifier = Modifier) {
    val stars = Games.totalStars(profile)
    val rank = Ranks.of(stars)
    val next = Ranks.next(stars)
    Panel(modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            AvatarFace(profile.avatar, 64.dp)
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PjText(profile.name, Modifier.weight(1f), size = 26.sp, weight = FontWeight.Bold, maxLines = 1)
                    Canvas(Modifier.size(26.dp)) { drawStar(center, 11.dp.toPx(), on = true) }
                    PjText(" $stars", size = 22.sp, weight = FontWeight.Bold, color = Pj.StarDark)
                }
                PjText(rank.name, size = 15.sp, weight = FontWeight.Medium, color = Pj.InkSoft, maxLines = 1)
                Box(
                    Modifier.padding(top = 6.dp).fillMaxWidth().height(8.dp).background(Color(0xFFE9DDB8), CircleShape),
                ) {
                    Box(Modifier.fillMaxWidth(Ranks.progress(stars).coerceAtLeast(0.04f)).height(8.dp).background(Pj.Green, CircleShape))
                }
                if (next != null) {
                    PjText("Prochain rang à ${next.minStars} étoiles", Modifier.padding(top = 2.dp), size = 11.sp, weight = FontWeight.Medium, color = Pj.InkSoft)
                } else {
                    PjText("Rang maximum !", Modifier.padding(top = 2.dp), size = 11.sp, weight = FontWeight.Medium, color = Pj.GreenDark)
                }
            }
        }
    }
}

@Composable
private fun GameCard(game: MiniGame, profile: Profile, onClick: () -> Unit) {
    val save = profile.gameSave(game.id)
    Box(
        Modifier
            .fillMaxWidth()
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = withTapSound(onClick)),
    ) {
        Panel(Modifier.fillMaxWidth(), corner = 34.dp, fill = Pj.Orange, edge = Pj.OrangeDark, ledge = 10.dp) {
            Column(Modifier.fillMaxWidth().padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                game.Illustration(Modifier.fillMaxWidth().height(170.dp))
                PjText(game.title, Modifier.padding(top = 12.dp), size = 32.sp, weight = FontWeight.Bold, color = Color.White, outline = Color(0xFFB86A00), outlineWidth = 8.dp)
                PjText(game.tagline, Modifier.padding(top = 2.dp, bottom = 12.dp), size = 14.sp, weight = FontWeight.Medium, color = Color.White, align = TextAlign.Center)
            }
        }
        Row(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 22.dp, end = 22.dp)
                .background(Color.White.copy(alpha = 0.92f), CircleShape)
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Canvas(Modifier.size(20.dp)) { drawStar(center, 9.dp.toPx(), on = true) }
            PjText(game.progressLabel(save), size = 16.sp, weight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SoonCard() {
    Panel(Modifier.fillMaxWidth(), corner = 30.dp, fill = Pj.Gray, edge = Pj.GrayDark, ledge = 8.dp) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            Box(Modifier.size(68.dp).background(Color.White.copy(alpha = 0.7f), CircleShape), contentAlignment = Alignment.Center) {
                PjIcon(IconKind.QUESTION, size = 36.dp, tint = Color(0xFFA2A8BC))
            }
            Column(Modifier.weight(1f)) {
                PjText("Bientôt…", size = 26.sp, weight = FontWeight.Bold, color = Pj.GrayText)
                PjText("Un nouveau jeu arrive", size = 15.sp, weight = FontWeight.Medium, color = Color(0xFF8D93A8))
            }
            PjIcon(IconKind.LOCK, size = 28.dp, tint = Pj.GrayText)
        }
    }
}

/** Les réglages d'un joueur : sons, vibrations, et effacer (avec confirmation à deux temps). */
@Composable
fun SettingsCard(
    profile: Profile,
    onSettingsChange: (Settings) -> Unit,
    onDelete: () -> Unit,
    onClose: () -> Unit,
    /** Pour les captures d'écran : montre tout de suite la confirmation d'effacement. */
    initialConfirm: Boolean = false,
) {
    var confirm by rememberSaveable { mutableStateOf(initialConfirm) }
    Modal(onDismiss = onClose) {
        Panel(
            Modifier.padding(horizontal = 26.dp).fillMaxWidth().consumeTaps(),
            corner = 36.dp, ledge = 8.dp,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 22.dp),
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                PjText("Réglages de ${profile.name}", size = 26.sp, weight = FontWeight.Bold, maxLines = 1)
                SettingRow("Sons", profile.settings.sound, Modifier.padding(top = 18.dp)) { onSettingsChange(profile.settings.copy(sound = it)) }
                SettingRow("Vibrations", profile.settings.vibration, Modifier.padding(top = 10.dp)) { onSettingsChange(profile.settings.copy(vibration = it)) }
                if (!confirm) {
                    ChunkyButton("Fermer", onClose, Modifier.padding(top = 22.dp).fillMaxWidth(), height = 56.dp, fontSize = 24.sp)
                    ChunkyButton(
                        "Effacer ce joueur", { confirm = true }, Modifier.padding(top = 12.dp).fillMaxWidth(),
                        color = Pj.Slate, dark = Pj.SlateDark, height = 44.dp, fontSize = 16.sp,
                    )
                } else {
                    PjText(
                        "Effacer ${profile.name} et toute sa progression ? On ne peut pas revenir en arrière.",
                        Modifier.padding(top = 20.dp), size = 17.sp, weight = FontWeight.Medium, color = Pj.Ink, align = TextAlign.Center,
                    )
                    Row(Modifier.padding(top = 14.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        ChunkyButton("Non", { confirm = false }, Modifier.weight(1f), height = 52.dp, fontSize = 20.sp)
                        ChunkyButton("Oui, effacer", onDelete, Modifier.weight(1f), color = Pj.Red, dark = Pj.RedDark, height = 52.dp, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingRow(label: String, checked: Boolean, modifier: Modifier = Modifier, onChange: (Boolean) -> Unit) {
    Row(
        modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(22.dp)).padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PjText(label, Modifier.weight(1f), size = 22.sp)
        PjSwitch(checked, onChange)
    }
}
