package com.tecknobit.refy.ui.viewmodels.create

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

    lateinit var itemName: MutableState<String>

    lateinit var itemDescription: MutableState<String>

    lateinit var itemDescriptionError: MutableState<Boolean>

    val idsList: SnapshotStateList<String> = mutableStateListOf()

    abstract fun initExistingItem(
        item : T?
    )

    abstract fun manageItem(
        onSuccess: () -> Unit
    )

}