package com.tecknobit.refy.ui.viewmodels.teams

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refycore.records.Team

@Structure
abstract class TeamViewModelHelper(
    snackbarHostState: SnackbarHostState
): EquinoxViewModel(
    snackbarHostState = snackbarHostState
) {

    fun deleteTeam(
        team: Team,
        onSuccess: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

}