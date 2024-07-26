package com.tecknobit.refy.ui.viewmodels.collections

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.tecknobit.refy.ui.viewmodels.create.CreateItemViewModel
import com.tecknobit.refycore.records.LinksCollection

class CreateCollectionViewModel(
    snackbarHostState: SnackbarHostState
) : CreateItemViewModel<LinksCollection>(
    snackbarHostState = snackbarHostState
) {

    lateinit var collectionColor: MutableState<Color>

    private var existingCollection: LinksCollection? = null

    override fun initExistingItem(
        item : LinksCollection?
    ) {
        if(item != null) {
            existingCollection = item
            existingCollection!!.links.forEach { link ->
                idsList.add(link.id)
            }
        }
    }

    override fun manageItem(
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