object Versions{
    const val kotlinVersion = "1.4.0"
    const val appCompatVersion = "1.2.0"
    const val androidCoreVersion = "1.3.1"
    const val constraintLayoutVersion = "1.1.3"
    const val fragmentNavigationVersion = "2.3.0"
    const val liveDataVersion = "2.2.0"
    const val retrofitVersion = "2.9.0"
    const val roomVersion = "2.3.0-alpha02"
    const val hiltVersion = "2.28-alpha"
    const val hiltViewModelVersion = "1.0.0-alpha01"
    const val junitVersion = "4.12"
    const val junitExtensionVersion = "1.1.1"
    const val espressoCoreVersion = "3.2.0"
    const val gradleVersion = "4.0.1"
    const val pagingVersion = "3.0.0-alpha03"
    const val materialVersion = "1.2.1"
    const val navigationVersion = "2.3.0"
    const val glideVersion = "4.11.0"
    const val legacySupportVersion = "1.0.0"
    const val fireBaseVersion = "17.5.0"
    const val firebaseAuthVersion = "19.3.2"
    const val serviceAuthVersion = "18.1.0"
    const val googleServicePath = "4.3.3"
    const val facebookVersion = "[5,6)"
    const val imageSliderVersion = "1.3.9"
    const val transformationLayoutVersion = "1.0.6"
    const val shimmerVersion = "0.5.0"
    const val databindingCompilerVersion = "4.0.1"
    const val recyclerViewVersion = "1.1.0"
    const val mapsVersion = "17.0.0"
}

object Deps{

    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompatVersion}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.androidCoreVersion}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}"

    const val databindingCompiler = "com.android.databinding:compiler:${Versions.databindingCompilerVersion}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
    const val materialDesign = "com.google.android.material:material:${Versions.materialVersion}"
    const val navigation = "androidx.navigation:navigation-ui-ktx:${Versions.navigationVersion}"
    const val legacySupport = "androidx.legacy:legacy-support-v4:${Versions.legacySupportVersion}"
    const val imageSlider = "com.github.smarteist:autoimageslider:${Versions.imageSliderVersion}"
    const val transformationLayout = "com.github.skydoves:transformationlayout:${Versions.transformationLayoutVersion}"
    const val shimmer = "com.facebook.shimmer:shimmer:${Versions.shimmerVersion}"
    const val maps = "com.google.android.gms:play-services-maps:${Versions.mapsVersion}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerViewVersion}"

    const val fragmentNavigation = "androidx.navigation:navigation-fragment-ktx:${Versions.fragmentNavigationVersion}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.liveDataVersion}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}"

    const val room = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"

    const val hilt = "com.google.dagger:hilt-android:$${Versions.hiltVersion}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"
    const val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltViewModelVersion}"
    const val hiltViewModelCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltViewModelVersion}"

    const val paging = "androidx.paging:paging-runtime:${Versions.pagingVersion}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glideVersion}"

    const val fireBase = "com.google.firebase:firebase-analytics:${Versions.fireBaseVersion}"
    const val fireBaseAuth = "com.google.firebase:firebase-auth:${Versions.firebaseAuthVersion}"
    const val serviceAuth =  "com.google.android.gms:play-services-auth:${Versions.serviceAuthVersion}"
    const val facebook = "com.facebook.android:facebook-login:${Versions.facebookVersion}"

}

object ClassPath{
    const val hiltClassPath = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltVersion}"
    const val gradleClassPath = "com.android.tools.build:gradle:${Versions.gradleVersion}"
    const val gradlePluginClassPath = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val googleServicePath = "com.google.gms:google-services:${Versions.googleServicePath}"
    const val navigationSafeArg = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationVersion}"

}

object TestDeps{

    const val junit = "junit:junit:${Versions.junitVersion}"
    const val junitExtension = "androidx.test.ext:junit:${Versions.junitExtensionVersion}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCoreVersion}"

}