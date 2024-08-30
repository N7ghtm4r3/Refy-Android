package com.tecknobit.refy.viewmodels.links

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.screens.links.LinkListScreen
import com.tecknobit.refycore.helpers.RefyInputValidator.isDescriptionValid
import com.tecknobit.refycore.helpers.RefyInputValidator.isLinkResourceValid
import com.tecknobit.refycore.records.links.RefyLink
import com.tecknobit.refycore.records.links.RefyLink.returnLinks
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * The **LinkListViewModel** class is the support class used by [LinkListScreen] to communicate
 * with the backend and to execute the refreshing routines to update the UI data and working with the
 * [RefyLink]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see LinksViewModelHelper
 * @see LinksViewModel
 */
class LinkListViewModel : LinksViewModel<RefyLink>() {

    init {
        _links = MutableStateFlow(
            value = mutableStateListOf()
        )
        links = _links
    }

    /**
     * Function to execute the request to get the links list
     *
     * No-any params required
     */
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

    /**
     * Function to execute the request to create a new link
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
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

    /**
     * Wrapper function to execute the request to edit an existing link
     *
     * @param link: the link to edit
     * @param onSuccess: the action to execute if the request has been successful
     */
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

    /**
     * Function to check whether the details of the link are valid to be used as payload
     *
     * @return whether the details of the link are valid to be used as payload as boolean
     */
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

    /**
     * Function to share the link with collections
     *
     * @param link: the link to share with the collections
     * @param collections: the collections identifiers where add the link
     * @param onSuccess: the action to execute if the request has been successful
     */
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

    /**
     * Function to share the link with teams
     *
     * @param link: the link to share with the teams
     * @param teams: the teams identifiers where share the link
     * @param onSuccess: the action to execute if the request has been successful
     */
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

    /**
     * Function to execute the request to delete a link
     *
     * @param link: the link to delete
     * @param onSuccess: the action to execute if the link has been deleted
     */
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