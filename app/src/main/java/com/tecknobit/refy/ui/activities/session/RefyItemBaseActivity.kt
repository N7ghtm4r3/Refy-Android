package com.tecknobit.refy.ui.activities.session

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinox.environment.records.EquinoxItem.IDENTIFIER_KEY
import com.tecknobit.equinoxcompose.components.ErrorUI
import com.tecknobit.refy.ui.getRefyItem
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refycore.records.RefyItem

/**
 * The **RefyItemBaseActivity** class is useful to give the base behavior of a [RefyItem]'s activity
 * to manage that item and other utilities such find it in the corresponding items list
 *
 * @param items: the items list
 * @param invalidMessage: the resource identifier of the invalid message to display when the item is
 * not valid or not found in [items] list
 *
 * @param T: the [RefyItem] of the current activity displayed
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 */
@Structure
abstract class RefyItemBaseActivity <T : RefyItem> (
    val items: List<T>,
    val invalidMessage: Int
) : ComponentActivity() {

    /**
     * *snackbarHostState* -> the host to launch the snackbar messages
     */
    protected val snackbarHostState = SnackbarHostState()

    /**
     * *itemId* -> the item identifier
     */
    private var itemId: String? = null

    /**
     * *item* -> the item corresponding that identifier if exist or null otherwise
     */
    protected var item: T? = null

    /**
     * *itemExists* -> whether that item has been found in the list
     */
    protected var itemExists = false

    /**
     * *invalidItem* -> whether the item is valid and so [itemExists] will be *true*
     */
    protected var invalidItem = false

    /**
     * Function to init the [item] searching it in the [items] list by its [itemId]
     *
     * No-any params required
     */
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

    /**
     * Function to display the error view when the item is not valid or has not been found
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    fun InvalidItemUi() {
        ErrorUI(
            errorMessage = stringResource(invalidMessage),
            retryText = ""
        )
    }

    /**
     * Function to create an header for an activity section
     *
     * @param header: the resource identifier of the header text
     */
    @Composable
    @NonRestartableComposable
    protected fun HeaderText(
        header: Int
    ) {
        Text(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp
                ),
            text = stringResource(header),
            fontFamily = displayFontFamily,
            style = AppTypography.titleLarge,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }

}