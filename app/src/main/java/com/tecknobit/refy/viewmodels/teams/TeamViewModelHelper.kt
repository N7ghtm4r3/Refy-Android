package com.tecknobit.refy.viewmodels.teams

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.viewmodels.RefyViewModel
import com.tecknobit.refycore.records.Team

/**
 * The **TeamViewModelHelper** class is the support class used by the inherited view models
 * to communicate with the backend and to execute the refreshing routines to update the UI data and
 * working with the [Team]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see RefyViewModel
 *
 */
@Structure
abstract class TeamViewModelHelper(
    snackbarHostState: SnackbarHostState
): RefyViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * Function to execute the request to manage the links of a team
     *
     * @param team: the team where manage the links list
     * @param links: the links identifiers of the links to add
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun manageTeamLinks(
        team: Team,
        links: List<String>,
        onSuccess: () -> Unit,
    ) {
        val teamLinks = team.linkIds.toMutableList()
        teamLinks.addAll(links)
        requester.sendRequest(
            request = {
                requester.manageTeamLinks(
                    team = team,
                    links = teamLinks
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to execute the request to manage the collections of a team
     *
     * @param team: the team where manage the collections list
     * @param collections: the collections identifiers of the collections to add
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun manageTeamCollections(
        team: Team,
        collections: List<String>,
        onSuccess: () -> Unit
    ) {
        val teamCollections = team.collectionsIds.toMutableList()
        teamCollections.addAll(collections)
        requester.sendRequest(
            request = {
                requester.manageTeamCollections(
                    team = team,
                    collections = teamCollections
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to execute the request to leave from a team
     *
     * @param team: the team from leave
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun leaveTeam(
        team: Team,
        onSuccess: () -> Unit,
    ) {
        requester.sendRequest(
            request = {
                requester.leave(
                    team = team
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to execute the request to delete a team
     *
     * @param team: the team to delete
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun deleteTeam(
        team: Team,
        onSuccess: () -> Unit,
    ) {
        requester.sendRequest(
            request = {
                requester.deleteTeam(
                    team = team
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

}