package com.kcharignon.petitsjeux

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.kcharignon.petitsjeux.core.AndroidFeedback
import com.kcharignon.petitsjeux.core.PetitsJeuxApp
import com.kcharignon.petitsjeux.core.save.FileProfileStore
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val store = FileProfileStore(File(filesDir, "saves"))
        setContent {
            PetitsJeuxApp(
                store = remember { store },
                createFeedback = { settings -> AndroidFeedback(applicationContext, { settings().sound }, { settings().vibration }) },
            )
        }
    }
}
