package com.tecknobit.refy.ui.viewmodels.teams

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.refy.ui.activities.session.singleitem.TeamActivity
import com.tecknobit.refycore.records.RefyLink
import com.tecknobit.refycore.records.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TeamActivityViewModel(
    snackbarHostState: SnackbarHostState,
    initialTeam: Team
): TeamViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    private val _team = MutableStateFlow(
        value = initialTeam
    )
    val team: StateFlow<Team> = _team

    fun refreshTeam() {
        execRefreshingRoutine(
            currentContext = TeamActivity::class.java,
            routine = {
                // TODO: MAKE THE REQUEST THEN

            }
        )
    }


    fun removeLinkFromTeam(
        link: RefyLink
    ) {
        // TODO: MAKE THE REQUEST THEN
    }

}