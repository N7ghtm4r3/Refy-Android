package com.tecknobit.refy.ui.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.refycore.records.Team

class CreateTeamViewModel(
    snackbarHostState: SnackbarHostState
) : CreateItemViewModel<Team>(
    snackbarHostState = snackbarHostState
) {

    override fun initExistingItem(
        item: Team?
    ) {
        if(item != null) {
            existingItem = item
            existingItem!!.members.forEach { link ->
                idsList.add(link.id)
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