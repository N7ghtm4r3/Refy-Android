package com.tecknobit.refy.ui.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.refycore.records.links.CustomRefyLink

class CreateCustomLinkViewModel(
    snackbarHostState: SnackbarHostState
): CreateItemViewModel<CustomRefyLink>(
    snackbarHostState = snackbarHostState
) {

    override fun initExistingItem(
        item: CustomRefyLink?
    ) {
        if(item != null)
            existingItem = item
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
        onSuccess.invoke()
    }

}