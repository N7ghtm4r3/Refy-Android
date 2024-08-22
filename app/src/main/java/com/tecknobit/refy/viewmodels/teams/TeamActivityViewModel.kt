package com.tecknobit.refy.viewmodels.teams

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.activities.session.singleitem.TeamActivity
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.RefyTeamMember
import com.tecknobit.refycore.records.Team.RefyTeamMember.TeamRole
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The **TeamActivityViewModel** class is the support class used by [TeamActivity] to communicate
 * with the backend and to execute the refreshing routines to update the UI data and working with the
 * [Team]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 * @param initialTeam: the initial value of the [Team]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see RefyViewModel
 * @see TeamViewModelHelper
 */
class TeamActivityViewModel(
    snackbarHostState: SnackbarHostState,
    initialTeam: Team
): TeamViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    /**
     * **_team** -> the current team displayed
     */
    private val _team = MutableStateFlow(
        value = initialTeam
    )
    val team: StateFlow<Team> = _team

    /**
     * Function to execute the request to refresh the team displayed
     *
     * No-any params required
     */
    fun refreshTeam() {
        sendFetchRequest(
            currentContext = TeamActivity::class.java,
            request = {
                requester.getTeam(
                    team = _team.value
                )
            },
            onSuccess = { hResponse ->
                _team.value = Team(hResponse.getJSONObject(RESPONSE_MESSAGE_KEY))
            }
        )
    }

    /**
     * Function to execute the request to remove from the team a link
     *
     * @param link: the link to remove (the link will be not deleted)
     */
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

    /**
     * Function to execute the request to remove from the team a collection
     *
     * @param collection: the collection to remove (the collection will be not deleted)
     */
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

    /**
     * Function to execute the request to change the role of a member
     *
     * @param member: the member to change his/her role
     * @param role: the role to set
     * @param onSuccess: the action to execute if the request has been successful
     */
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

    /**
     * Function to execute the request to remove a member from the team
     *
     * @param member: the member to remove
     */
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