package com.tecknobit.refy.ui.activities.session

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.res.stringResource
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinox.environment.records.EquinoxItem.IDENTIFIER_KEY
import com.tecknobit.equinoxcompose.components.ErrorUI
import com.tecknobit.refy.ui.getRefyItem
import com.tecknobit.refycore.records.RefyItem

@Structure
abstract class RefyItemBaseActivity <T : RefyItem> (
    val items: List<T>,
    val invalidMessage: Int
) : ComponentActivity() {

    protected val snackbarHostState = SnackbarHostState()

    private var itemId: String? = null

    protected var item: T? = null

    protected var itemExists = false

    protected var invalidItem = false

    fun initItemFromIntent() {
        itemId = intent.getStringExtra(IDENTIFIER_KEY)
        if(itemId != null) {
            item = items.getRefyItem(
                itemId = itemId
            )
            if(item == null)
                invalidItem = true
            else
                itemExists = true
        }
    }

    @Composable
    @NonRestartableComposable
    fun InvalidItemUi() {
        ErrorUI(
            errorMessage = stringResource(invalidMessage),
            retryText = ""
        )
    }

}