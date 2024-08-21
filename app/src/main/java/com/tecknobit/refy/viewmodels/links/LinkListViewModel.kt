package com.tecknobit.refy.viewmodels.links

import androidx.compose.runtime.toMutableStateList
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.screens.links.LinkListScreen
import com.tecknobit.refycore.helpers.RefyInputValidator.isDescriptionValid
import com.tecknobit.refycore.helpers.RefyInputValidator.isLinkResourceValid
import com.tecknobit.refycore.records.links.RefyLink
import com.tecknobit.refycore.records.links.RefyLink.returnLinks

class LinkListViewModel : LinksViewModel<RefyLink>() {

    override fun getLinks() {
        sendFetchRequest(
            currentContext = LinkListScreen::class.java,
            request = {
                requester.getLinks()
            },
            onSuccess = { response ->
                _links.value = returnLinks(response.getJSONArray(RESPONSE_MESSAGE_KEY))
                    .toMutableStateList()
                localUser.setLinks(_links.value)
            }
        )
    }

    override fun addNewLink(
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.createLink(
                    referenceLink = linkReference.value,
                    description = linkDescription.value
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    override fun editLink(
        link: RefyLink,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.editLink(
                    link = link,
                    referenceLink = linkReference.value,
                    description = linkDescription.value
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    override fun linkDetailsValidated(): Boolean {
        if(!isLinkResourceValid(linkReference.value)) {
            linkReferenceError.value = true
            return false
        }
        if(!isDescriptionValid(linkDescription.value)) {
            linkDescriptionError.value = true
            return false
        }
        return true
    }

    override fun addLinkToCollections(
        link: RefyLink,
        collections: List<String>,
        onSuccess: () -> Unit
    ) {
        val linkCollections = link.collectionsIds.toMutableList()
        linkCollections.addAll(collections)
        requester.sendRequest(
            request = {
                requester.manageLinkCollections(
                    link = link,
                    collections = linkCollections
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    override fun addLinkToTeams(
        link: RefyLink,
        teams: List<String>,
        onSuccess: () -> Unit
    ) {
        val linkTeams = link.teamIds.toMutableList()
        linkTeams.addAll(teams)
        requester.sendRequest(
            request = {
                requester.manageLinkTeams(
                    link = link,
                    teams = linkTeams
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    override fun deleteLink(
        link: RefyLink,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.deleteLink(
                    link = link
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

}