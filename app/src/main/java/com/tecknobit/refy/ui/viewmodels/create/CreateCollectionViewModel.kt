package com.tecknobit.refy.ui.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.tecknobit.refycore.records.LinksCollection

class CreateCollectionViewModel(
    snackbarHostState: SnackbarHostState
) : CreateItemViewModel<LinksCollection>(
    snackbarHostState = snackbarHostState
) {

    lateinit var collectionColor: MutableState<Color>

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

    override fun createItem(
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    override fun editItem(
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        existingItem!!.id
        onSuccess.invoke()
    }

}