package com.tecknobit.refy.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.toHex
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