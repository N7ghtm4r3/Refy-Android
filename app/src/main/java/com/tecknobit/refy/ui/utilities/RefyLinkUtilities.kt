package com.tecknobit.refy.ui.utilities

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.core.net.toUri
import com.tecknobit.refycore.records.RefyLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface RefyLinkUtilities {

    fun openLink(
        context: Context,
        link: RefyLink
    ) {
        val intent = Intent()
        intent.data = link.referenceLink.toUri()
        intent.action = Intent.ACTION_VIEW
        context.startActivity(intent)
    }

    fun showLinkReference(
        snackbarHostState: SnackbarHostState,
        link: RefyLink
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            snackbarHostState.showSnackbar(link.referenceLink)
        }
    }

    fun shareLink(
        context: Context,
        link: RefyLink
    ) {
        val intent = Intent()
        intent.type = "text/plain"
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, "${link.title}\n${link.referenceLink}")
        context.startActivity(Intent.createChooser(intent, null))
    }

    @Composable
    @NonRestartableComposable
    fun ShareButton(
        context: Context,
        link: RefyLink
    ) {
        IconButton(
            onClick = {
                shareLink(
                    context = context,
                    link = link
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null
            )
        }
    }

    @Composable
    @NonRestartableComposable
    fun ViewLinkReferenceButton(
        snackbarHostState: SnackbarHostState,
        link: RefyLink
    ) {
        IconButton(
            onClick = {
                showLinkReference(
                    snackbarHostState = snackbarHostState,
                    link = link
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = null
            )
        }
    }

}