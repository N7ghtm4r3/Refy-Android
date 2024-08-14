package com.tecknobit.refy.ui.viewmodels.teams

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.activities.session.singleitem.TeamActivity
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.RefyTeamMember
import com.tecknobit.refycore.records.Team.RefyTeamMember.TeamRole
import com.tecknobit.refycore.records.links.RefyLink
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
                requester.sendRequest(
                    request = {
                        requester.getTeam(
                            team = _team.value
                        )
                    },
                    onSuccess = { hResponse ->
                        _team.value = Team(hResponse.getJSONObject(RESPONSE_MESSAGE_KEY))
                    },
                    onFailure = { showSnackbarMessage(it) }
                )
            }
        )
    }

    fun removeLinkFromTeam(
        link: RefyLink
    ) {
        val teamLinks = _team.value.linkIds
        teamLinks.remove(link.id)
        requester.sendRequest(
            request = {
                requester.manageTeamLinks(
                    team = _team.value,
                    links = teamLinks
                )
            },
            onSuccess = {},
            onFailure = { showSnackbarMessage(it) }
        )
    }

    fun removeCollectionFromTeam(
        collection: LinksCollection
    ) {
        val teamCollections = _team.value.collectionsIds
        teamCollections.remove(collection.id)
        requester.sendRequest(
            request = {
                requester.manageTeamCollections(
                    team = _team.value,
                    collections = teamCollections
                )
            },
            onSuccess = {},
            onFailure = { showSnackbarMessage(it) }
        )
    }

    fun changeMemberRole(
        member: RefyTeamMember,
        role: TeamRole,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.changeMemberRole(
                    team = _team.value,
                    member = member,
                    role = role
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    fun removeMember(
        member: RefyTeamMember
    ) {
        requester.sendRequest(
            request = {
                requester.removeMember(
                    team = _team.value,
                    member = member
                )
            },
            onSuccess = { },
            onFailure = { showSnackbarMessage(it) }
        )
    }

}