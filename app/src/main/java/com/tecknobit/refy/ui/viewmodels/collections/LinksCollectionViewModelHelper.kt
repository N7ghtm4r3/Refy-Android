package com.tecknobit.refy.ui.viewmodels.collections

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.viewmodels.RefyViewModel
import com.tecknobit.refycore.records.LinksCollection

@Structure
abstract class LinksCollectionViewModelHelper (
    snackbarHostState: SnackbarHostState
) : RefyViewModel (
    snackbarHostState = snackbarHostState
) {

    fun addLinksToCollection(
        collection: LinksCollection,
        links: List<String>,
        onSuccess: () -> Unit,
    ) {
        val collectionLinks = collection.linkIds.toMutableList()
        collectionLinks.addAll(links)
        requester.sendRequest(
            request = {
                requester.manageCollectionLinks(
                    collection = collection,
                    links = collectionLinks
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    fun addTeamsToCollection(
        collection: LinksCollection,
        teams: List<String>,
        onSuccess: () -> Unit,
    ) {
        val collectionTeams = collection.teamIds.toMutableList()
        collectionTeams.addAll(teams)
        requester.sendRequest(
            request = {
                requester.manageCollectionTeams(
                    collection = collection,
                    teams = collectionTeams
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    fun deleteCollection(
        collection: LinksCollection,
        onSuccess: () -> Unit,
    ) {
        requester.sendRequest(
            request = {
                requester.deleteCollection(
                    collection = collection
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

}