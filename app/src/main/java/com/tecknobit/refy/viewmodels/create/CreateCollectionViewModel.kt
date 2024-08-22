package com.tecknobit.refy.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.toHex
import com.tecknobit.refycore.records.LinksCollection

/**
 * The **CreateCollectionViewModel** class is the support class used by [CreateCollectionActivity]
 * to communicate with the backend for the creation or the editing of a [LinksCollection]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see CreateItemViewModel
 */
class CreateCollectionViewModel(
    snackbarHostState: SnackbarHostState
) : CreateItemViewModel<LinksCollection>(
    snackbarHostState = snackbarHostState
) {

    /**
     * **collectionColor** -> the color of the collection
     */
    lateinit var collectionColor: MutableState<Color>

    /**
     * Function to initializing the [existingItem] if exists, null otherwise
     *
     * @param item: the item value with initializing the [existingItem] if exists, null otherwise
     */
    override fun initExistingItem(
        item : LinksCollection?
    ) {
        if(item != null) {
            existingItem = item
            existingItem!!.links.forEach { link ->
                itemDedicatedList.add(link.id)
            }
        }
    }

    /**
     * Function to execute the request to create a new item
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    override fun createItem(
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.createCollection(
                    color = collectionColor.value.toHex(),
                    title = itemName.value,
                    description = itemDescription.value,
                    links = itemDedicatedList
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to execute the request to edit an existing item
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    override fun editItem(
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.editCollection(
                    collectionId = existingItem!!.id,
                    color = collectionColor.value.toHex(),
                    title = itemName.value,
                    description = itemDescription.value,
                    links = itemDedicatedList
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

}