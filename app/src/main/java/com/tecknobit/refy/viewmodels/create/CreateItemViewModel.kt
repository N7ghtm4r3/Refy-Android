package com.tecknobit.refy.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refycore.records.RefyItem

/**
 * The **CreateItemViewModel** class is the support class used by the inherited view models
 * to communicate with the backend for the creation or the editing of a [RefyItem]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 *
 * @param T: the type of the [RefyItem]
 */
@Structure
abstract class CreateItemViewModel <T : RefyItem> (
    snackbarHostState: SnackbarHostState
) : EquinoxViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * **existingItem** -> whether the item already exists, so is an editing action
     */
    protected var existingItem: T? = null

    /**
     * **itemName** -> the name of the item
     */
    lateinit var itemName: MutableState<String>

    /**
     * **itemDescription** -> the description of the item
     */
    lateinit var itemDescription: MutableState<String>

    /**
     * **itemDescriptionError** -> whether the [itemDescription] field is not valid
     */
    lateinit var itemDescriptionError: MutableState<Boolean>

    /**
     * **itemDedicatedList** -> a dedicated list of the item, useful to manage for example links, collections
     * or members list attached to the item
     */
    val itemDedicatedList: SnapshotStateList<String> = mutableStateListOf()

    /**
     * Function to initializing the [existingItem] if exists, null otherwise
     *
     * @param item: the item value with initializing the [existingItem] if exists, null otherwise
     */
    abstract fun initExistingItem(
        item : T?
    )

    /**
     * Wrapper function to execute the request to create or edit an item
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun manageItem(
        onSuccess: () -> Unit
    ) {
        if(existingItem == null) {
            createItem(
                onSuccess = onSuccess
            )
        } else {
            editItem(
                onSuccess = onSuccess
            )
        }
    }

    /**
     * Function to execute the request to create a new item
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    protected abstract fun createItem(
        onSuccess: () -> Unit
    )

    /**
     * Function to execute the request to edit an existing item
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    protected abstract fun editItem(
        onSuccess: () -> Unit
    )

}