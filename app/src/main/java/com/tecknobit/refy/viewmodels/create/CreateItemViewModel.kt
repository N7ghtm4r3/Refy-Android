package com.tecknobit.refy.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refycore.records.RefyItem

@Structure
abstract class CreateItemViewModel <T : RefyItem> (
    snackbarHostState: SnackbarHostState
) : EquinoxViewModel(
    snackbarHostState = snackbarHostState
) {

    protected var existingItem: T? = null

    lateinit var itemName: MutableState<String>

    lateinit var itemDescription: MutableState<String>

    lateinit var itemDescriptionError: MutableState<Boolean>

    val itemDedicatedList: SnapshotStateList<String> = mutableStateListOf()

    abstract fun initExistingItem(
        item : T?
    )

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

    protected abstract fun createItem(
        onSuccess: () -> Unit
    )

    protected abstract fun editItem(
        onSuccess: () -> Unit
    )

}