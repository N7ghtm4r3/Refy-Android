package com.tecknobit.refy.ui.viewmodels.teams

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.refy.ui.activities.session.singleitem.TeamActivity
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.RefyLink
import com.tecknobit.refycore.records.RefyUser
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.RefyTeamMember
import com.tecknobit.refycore.records.Team.RefyTeamMember.TeamRole
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

    fun removeCollectionFromTeam(
        collection: LinksCollection
    ) {
        // TODO: MAKE THE REQUEST THEN
    }

    fun changeMemberRole(
        member: RefyUser,
        role: TeamRole,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    fun removeMember(
        member: RefyTeamMember
    ) {
        // TODO: MAKE THE REQUEST THEN
    }

}