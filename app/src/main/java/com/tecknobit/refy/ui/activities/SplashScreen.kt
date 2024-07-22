package com.tecknobit.refy.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecknobit.refycore.records.RefyUser

// TODO: SET IT CORRECTLY ALSO IN MANIFEST AND ITS THEME
@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity() {

    companion object {

        // TODO: TO INIT CORRECTLY
        val user = RefyUser()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        setContent {

        }
    }
}