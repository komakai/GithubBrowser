import com.android.build.api.dsl.ApplicationExtension
import java.util.Properties
import java.io.InputStreamReader
import java.io.FileInputStream

fun Project.getLocalProperty(key: String, file: String = "local.properties"): String? {
    val properties = Properties()
    val localProperties = File(file)
    if (localProperties.isFile) {
        InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    } else {
        project.logger.warn("File \"$file\" not found")
        return null
    }

    return properties.getProperty(key)
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") version "2.3.5"
    alias(libs.plugins.hilt)
}

configure<ApplicationExtension> {
    namespace = "net.telepathix.githubbrowse"
    compileSdk = 36

    defaultConfig {
        applicationId = "net.telepathix.githubbrowse"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        val pat = getLocalProperty(key = "github.pat")
        buildConfigField("String", "GITHUB_PAT", "\"$pat\"")
        if (pat == null || pat.isEmpty()) {
            project.logger.warn("Property github.pat not defined in local.properties. Requests will be rate-limited!")
        }

        testInstrumentationRunner = "net.telepathix.githubbrowse.testing.GithubBrowserTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.navigation.ktx)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.hilt.android)
    implementation(libs.converter.gson)
    implementation(libs.timber)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.glide)

    ksp(libs.dagger.compiler)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.hilt.android.testing)
    testImplementation(project(":sharedtestcode"))

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(project(":sharedtestcode"))
}