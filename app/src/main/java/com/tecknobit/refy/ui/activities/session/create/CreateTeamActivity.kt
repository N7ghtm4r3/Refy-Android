package com.tecknobit.refy.ui.activities.session.create

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecknobit.refy.ui.theme.RefyTheme

class CreateTeamActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RefyTheme {

            }
        }
    }

}
