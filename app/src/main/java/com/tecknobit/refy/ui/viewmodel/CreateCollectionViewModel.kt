package com.tecknobit.refy.ui.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel

class CreateCollectionViewModel(
    snackbarHostState: SnackbarHostState
) : EquinoxViewModel(
    snackbarHostState = snackbarHostState
) {

    lateinit var collectionColor: MutableState<Color>

    lateinit var collectionName: MutableState<String>

    lateinit var collectionDescription: MutableState<String>

    lateinit var collectionDescriptionError: MutableState<Boolean>

    val collectionLinks: SnapshotStateList<String> = mutableStateListOf()

    fun createCollection(
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

}