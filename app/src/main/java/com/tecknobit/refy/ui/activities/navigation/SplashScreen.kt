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
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import com.tecknobit.refy.ui.activities.session.MainActivity
import com.tecknobit.refycore.records.RefyUser
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

// TODO: SET IT CORRECTLY ALSO IN MANIFEST AND ITS THEME
@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity(), ImageLoaderFactory {

    companion object {

        // TODO: TO INIT CORRECTLY
        val user = RefyUser()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Coil.imageLoader(applicationContext)
        Coil.setImageLoader(newImageLoader())
        setContent {
            // TODO: MAKE THE REAL NAVIGATION
            startActivity(Intent(this, MainActivity::class.java))
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

}