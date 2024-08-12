package com.tecknobit.refy.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refycore.records.LinksCollection.returnCollections
import com.tecknobit.refycore.records.Team.returnTeams
import com.tecknobit.refycore.records.links.RefyLink.returnLinks

@Structure
abstract class RefyViewModel(
    snackbarHostState: SnackbarHostState
) : EquinoxViewModel (
    snackbarHostState = snackbarHostState
) {

    fun setCurrentUserOwnedLinks(
        forceRefresh: Boolean = false
    ) {
        if(localUser.links.isEmpty() || forceRefresh) {
            requester.sendRequest(
                request = {
                    requester.getLinks(
                        ownedOnly = true
                    )
                },
                onSuccess = { response ->
                    localUser.links = returnLinks(response.getJSONArray(RESPONSE_MESSAGE_KEY))
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    fun setCurrentUserOwnedCollections(
        forceRefresh: Boolean = false
    ) {
        if(localUser.collections.isEmpty() || forceRefresh) {
            requester.sendRequest(
                request = {
                    requester.getCollections(
                        ownedOnly = true
                    )
                },
                onSuccess = { response ->
                    localUser.collections = returnCollections(response.getJSONArray(RESPONSE_MESSAGE_KEY))
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    fun setCurrentUserOwnedTeams(
        forceRefresh: Boolean = false
    ) {
        if(localUser.teams.isEmpty() || forceRefresh) {
            requester.sendRequest(
                request = {
                    requester.getTeams(
                        ownedOnly = true
                    )
                },
                onSuccess = { response ->
                    localUser.teams = returnTeams(response.getJSONArray(RESPONSE_MESSAGE_KEY))
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}