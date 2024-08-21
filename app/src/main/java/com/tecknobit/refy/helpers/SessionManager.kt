package com.tecknobit.refy.helpers

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.tecknobit.equinoxcompose.components.ErrorUI
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.auth.ConnectActivity
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.screens.Screen.Companion.haveBeenDisconnected
import com.tecknobit.refy.ui.screens.Screen.Companion.isServerOffline

/**
 * The **SessionManager** interface is useful to display the correct content based on the current scenario
 * such server offline or device disconnected
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface SessionManager {

    /**
     * Function to display the correct content based on the current scenario such server offline or
     * device disconnected
     *
     * @param context: the current context where the function has been invoked
     * @param content: the content to display in a normal scenario
     */
    @Composable
    fun ManagedContent(
        context: Context,
        content: @Composable () -> Unit
    ) {
        InstantiateSessionFlags()
        AnimatedVisibility(
            visible = isServerOffline.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ServerOfflineUi()
        }
        AnimatedVisibility(
            visible = !isServerOffline.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if(haveBeenDisconnected.value)
                haveBeenDisconnected(context)
            else
                content.invoke()
        }
    }

    /**
     * Function to instantiate the session flags to manage the different scenarios
     *
     * No-any params required
     */
    @Composable
    private fun InstantiateSessionFlags() {
        isServerOffline = remember { mutableStateOf(false) }
        haveBeenDisconnected = remember { mutableStateOf(false) }
    }

    /**
     * Function to display the content when the server is offline
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun ServerOfflineUi() {
        ErrorUI(
            errorIcon = Icons.Default.Warning,
            errorMessage = stringResource(R.string.server_currently_offline),
            retryText = ""
        )
    }

    /**
     * Function to disconnect the current [localUser] from the session and navigate to the [ConnectActivity]
     *
     * @param context: the current context where the function has been invoked
     */
    private fun haveBeenDisconnected(
        context: Context
    ) {
        localUser.clear()
        context.startActivity(Intent(context, ConnectActivity::class.java))
    }

}