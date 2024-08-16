package com.tecknobit.refy.ui.viewmodels.teams

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.viewmodels.RefyViewModel
import com.tecknobit.refycore.records.Team

@Structure
abstract class TeamViewModelHelper(
    snackbarHostState: SnackbarHostState
): RefyViewModel(
    snackbarHostState = snackbarHostState
) {

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