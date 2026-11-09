package com.kcharignon.petitsjeux

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.kcharignon.petitsjeux.core.AndroidFeedback
import com.kcharignon.petitsjeux.core.PetitsJeuxApp
import com.kcharignon.petitsjeux.core.save.FileProfileStore
import com.kcharignon.petitsjeux.core.update.AndroidApkInstaller
import com.kcharignon.petitsjeux.core.update.GitHubUpdateApi
import com.kcharignon.petitsjeux.core.update.UpdateController
import com.kcharignon.petitsjeux.core.update.installedVersion
import java.io.File

class MainActivity : ComponentActivity() {
    private var update: UpdateController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val store = FileProfileStore(File(filesDir, "saves"))
        setContent {
            val scope = rememberCoroutineScope()
            val controller = remember {
                UpdateController(
                    api = GitHubUpdateApi(),
                    installer = AndroidApkInstaller(applicationContext),
                    installedVersionCode = installedVersion(applicationContext).second,
                    workDir = File(cacheDir, "updates"),
                    scope = scope,
                )
            }
            update = controller
            PetitsJeuxApp(
                store = remember { store },
                createFeedback = { settings -> AndroidFeedback(applicationContext, { settings().sound }, { settings().vibration }) },
                update = controller,
            )
        }
    }

    override fun onResume() {
        super.onResume()
        // le joueur revient de la page « Autoriser » d'Android : l'installation continue toute seule
        update?.resume()
    }
}
