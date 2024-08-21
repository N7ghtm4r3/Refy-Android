package com.tecknobit.refy.viewmodels.links

import androidx.compose.runtime.toMutableStateList
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.screens.links.CustomLinksScreen
import com.tecknobit.refycore.records.links.CustomRefyLink

class CustomLinksViewModel: LinksViewModel<CustomRefyLink>() {

    override fun getLinks() {
        sendFetchRequest(
            currentContext = CustomLinksScreen::class.java,
            request = {
                requester.getCustomLinks()
            },
            onSuccess = { response ->
                _links.value = CustomRefyLink.returnCustomLinks(
                    response.getJSONArray(RESPONSE_MESSAGE_KEY)
                ).toMutableStateList()
                localUser.setCustomLinks(_links.value)
            }
        )
    }

    override fun addNewLink(
        onSuccess: () -> Unit
    ) {
        // TODO: TO IGNORE AT THE MOMENT
    }

    override fun editLink(
        link: CustomRefyLink,
        onSuccess: () -> Unit
    ) {
        // TODO: TO IGNORE AT THE MOMENT
    }

    override fun linkDetailsValidated(): Boolean {
        return false
    }

    override fun addLinkToCollections(
        link: CustomRefyLink,
        collections: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: TO IGNORE AT THE MOMENT
    }

    override fun addLinkToTeams(
        link: CustomRefyLink,
        teams: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: TO IGNORE AT THE MOMENT
    }

    override fun deleteLink(
        link: CustomRefyLink,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.deleteCustomLink(
                    link = link
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

}