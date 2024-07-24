package com.tecknobit.refy.ui.activities.session.collection

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.res.stringResource
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinox.environment.records.EquinoxItem.IDENTIFIER_KEY
import com.tecknobit.equinoxcompose.components.ErrorUI
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.getLinksCollection
import com.tecknobit.refycore.records.LinksCollection

@Structure
abstract class CollectionBaseActivity : ComponentActivity() {

    protected val snackbarHostState = SnackbarHostState()

    private var collectionId: String? = null

    protected var linksCollection: LinksCollection? = null

    protected var collectionExists = false

    protected var invalidCollection = false

    fun initCollectionFromIntent() {
        collectionId = intent.getStringExtra(IDENTIFIER_KEY)
        if(collectionId != null) {
            linksCollection = user.collections.getLinksCollection(
                collectionId = collectionId
            )
            if(linksCollection == null)
                invalidCollection = true
            else
                collectionExists = true
        }
    }

    @Composable
    @NonRestartableComposable
    fun InvalidCollectionUi() {
        ErrorUI(
            errorMessage = stringResource(R.string.invalid_collection),
            retryText = ""
        )
    }

}