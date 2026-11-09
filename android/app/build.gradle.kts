import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("io.github.takahirom.roborazzi")
}

// Version : l'étiquette android-vX.Y.Z donne le nom (X.Y.Z) ET le code de version (X*1_000_000 + Y*1_000 + Z), qui ne doit jamais
// baisser pour qu'une version s'installe par-dessus la précédente. Sans étiquette : « 0.0.0-dev » et ANDROID_VERSION_CODE (ou 1).
val versionTag: String? = System.getenv("ANDROID_VERSION_TAG")?.takeIf { it.isNotBlank() }
val tagMatch = versionTag?.let {
    Regex("""android-v(0|[1-9]\d{0,2})\.(0|[1-9]\d{0,2})\.(0|[1-9]\d{0,2})""").matchEntire(it)
        ?: throw GradleException("ANDROID_VERSION_TAG « $it » n'est pas de la forme android-vX.Y.Z.")
}
val appVersionName: String = tagMatch?.destructured?.let { (x, y, z) -> "$x.$y.$z" } ?: "0.0.0-dev"
val appVersionCode: Int = tagMatch?.destructured?.let { (x, y, z) -> x.toInt() * 1_000_000 + y.toInt() * 1_000 + z.toInt() }
    ?: (System.getenv("ANDROID_VERSION_CODE")?.toIntOrNull() ?: 1)

android {
    namespace = "com.kcharignon.petitsjeux"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kcharignon.petitsjeux"
        minSdk = 26
        targetSdk = 34
        versionCode = appVersionCode
        versionName = appVersionName
    }

    signingConfigs {
        // Clé de débogage PUBLIQUE, suivie par Git (mot de passe « android ») : elle ne protège rien, mais toutes les constructions de
        // débogage (locale, CI) ont ainsi la même signature et se mettent à jour par-dessus l'ancienne.
        getByName("debug") {
            storeFile = rootProject.file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        // Clé de publication : fournie par l'environnement (build.sh la décode d'un secret GitHub), jamais écrite dans le dépôt.
        create("release") {
            val keystore = System.getenv("ANDROID_KEYSTORE_FILE")
            if (!keystore.isNullOrBlank()) {
                storeFile = File(keystore)
                storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("ANDROID_KEY_ALIAS")
                keyPassword = System.getenv("ANDROID_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
    lint {
        checkReleaseBuilds = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests {
            // Robolectric (captures d'écran) a besoin des ressources de l'appli : polices, icônes.
            isIncludeAndroidResources = true
        }
    }
}

// Les captures d'écran (Robolectric) sont plus lentes : `make android-test` les saute, `make android-screenshots` (-Pscreenshots) les joue.
tasks.withType<Test>().configureEach {
    // Dans le conteneur Docker (identifiant sans entrée passwd) `user.home` vaut « ? » : Robolectric y télécharge ses jars Android ; on les range dans le cache de Gradle.
    System.getenv("GRADLE_USER_HOME")?.let {
        File("$it/home").mkdirs()
        systemProperty("user.home", "$it/home")
    }
    if (!project.hasProperty("screenshots")) exclude("**/screenshots/**") else include("**/screenshots/**")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2025.03.01")
    implementation(composeBom)

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.3")
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")

    testImplementation("junit:junit:4.13.2")
    // org.json de l'Android est un simple modèle dans les tests unitaires : on met la vraie bibliothèque.
    testImplementation("org.json:json:20250517")

    // Captures d'écran sans téléphone : Robolectric (rendu natif) + Roborazzi + tests Compose.
    testImplementation("org.robolectric:robolectric:4.16")
    testImplementation("io.github.takahirom.roborazzi:roborazzi:1.69.0")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-compose:1.69.0")
    testImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
