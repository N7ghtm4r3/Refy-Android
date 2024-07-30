package com.tecknobit.refy.ui.activities.session.singleitem

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refycore.records.links.CustomRefyLink

class CustomLinkActivity: SingleItemActivity<CustomRefyLink>(
    items = user.customLinks,
    invalidMessage = R.string.invalid_custom_link
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RefyTheme {
                activityColorTheme = MaterialTheme.colorScheme.primary
                DisplayItem(
                    topBarColor = MaterialTheme.colorScheme.primaryContainer,
                    actions = {

                    },
                    floatingActionButton = {

                    },
                    content = { paddingValues ->

                    }
                )
            }
        }
    }

    @Composable
    override fun InitViewModel() {

    }

}