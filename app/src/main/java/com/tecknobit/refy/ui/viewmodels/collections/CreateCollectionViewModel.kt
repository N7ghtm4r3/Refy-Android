package com.tecknobit.refy.ui.viewmodels.collections

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refycore.records.LinksCollection

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

    private var existingCollection: LinksCollection? = null

    fun initExistingCollection(
        collection : LinksCollection?
    ) {
        if(collection != null) {
            existingCollection = collection
            existingCollection!!.links.forEach { link ->
                collectionLinks.add(link.id)
            }
        }
    }

    fun manageCollection(
        onSuccess: () -> Unit
    ) {
        if(existingCollection == null) {
            createCollection(
                onSuccess = onSuccess
            )
        } else {
            editCollection(
                onSuccess = onSuccess
            )
        }
    }

    private fun createCollection(
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    private fun editCollection(
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        existingCollection!!.id
        onSuccess.invoke()
    }

}