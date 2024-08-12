package com.tecknobit.refy.ui.activities.navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.tecknobit.refy.R
import com.tecknobit.refy.helpers.AndroidRefyLocalUser
import com.tecknobit.refy.ui.activities.session.ConnectActivity
import com.tecknobit.refy.ui.activities.session.MainActivity
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refycore.helpers.RefyRequester
import com.tecknobit.refycore.records.RefyUser
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity(), ImageLoaderFactory {

    companion object {

        // TODO: TO INIT CORRECTLY CHECK TO REPLACE WITH LOCALUSER INSTEAD
        val user = RefyUser("h1")

        lateinit var localUser: AndroidRefyLocalUser

        lateinit var requester: RefyRequester

    }

    /**
     * **appUpdateManager** the manager to check if there is an update available
     */
    private lateinit var appUpdateManager: AppUpdateManager

    /**
     * **launcher** the result registered for [appUpdateManager] and the action to execute if fails
     */
    private var launcher  = registerForActivityResult(StartIntentSenderForResult()) { result ->
        if (result.resultCode != RESULT_OK)
            launchApp(MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        localUser = AndroidRefyLocalUser(this)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(localUser.language))
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        Coil.imageLoader(applicationContext)
        Coil.setImageLoader(newImageLoader())
        setContent {
            RefyTheme {
                Column (
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.inversePrimary)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text (
                            text = getString(R.string.app_name),
                            color = Color.White,
                            style = AppTypography.displayLarge,
                            fontSize = 55.sp,
                        )
                    }
                    Row (
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(30.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "by Tecknobit",
                            color = Color.White,
                            fontFamily = displayFontFamily,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
            checkForUpdates()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

    /**
     * Return a new [ImageLoader].
     */
    override fun newImageLoader(): ImageLoader {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, validateSelfSignedCertificate(), SecureRandom())
        return ImageLoader.Builder(applicationContext)
            .okHttpClient {
                OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.socketFactory,
                        validateSelfSignedCertificate()[0] as X509TrustManager
                    )
                    .hostnameVerifier { _: String?, _: SSLSession? -> true }
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .build()
            }
            .addLastModifiedToFileCacheKey(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    /**
     * Method to validate a self-signed SLL certificate and bypass the checks of its validity<br></br>
     * No-any params required
     *
     * @return list of trust managers as [Array] of [TrustManager]
     * @apiNote this method disable all checks on the SLL certificate validity, so is recommended to
     * use for test only or in a private distribution on own infrastructure
     */
    private fun validateSelfSignedCertificate(): Array<TrustManager> {
        return arrayOf(
            @SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
            })
    }

    /**
     * Function to check if there are some update available to install
     *
     * No-any params required
     */
    private fun checkForUpdates() {
        val intentDestination = getFirstScreen()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UPDATE_AVAILABLE
            val isUpdateSupported = info.isImmediateUpdateAllowed
            if(isUpdateAvailable && isUpdateSupported) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    launcher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            } else
                launchApp(intentDestination)
        }.addOnFailureListener {
            launchApp(intentDestination)
        }
    }

    /**
     * Function to get the first activity to display, this is based on whether the [localUser]
     * is already authenticated or not
     *
     * No-any params required
     */
    private fun getFirstScreen() : Class<*> {
        val firstScreen = if (localUser.isAuthenticated) {
            setLocale()
            MainActivity::class.java
        } else
            ConnectActivity::class.java
        requester = RefyRequester(
            host = localUser.hostAddress,
            userId = localUser.userId,
            userToken = localUser.userToken
        )
        return firstScreen
    }

    /**
     * Function to set locale language for the application
     *
     * No-any params required
     */
    private fun setLocale() {
        val userLanguage = user.language
        val locale = if(userLanguage != null)
            Locale.forLanguageTag(userLanguage)
        else
            Locale.getDefault()
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    /**
     * Method to launch the app and the user session
     *
     * @param intentDestination: the intent to reach
     *
     */
    private fun launchApp(
        intentDestination: Class<*>
    ) {
        startActivity(Intent(this, intentDestination))
    }

}