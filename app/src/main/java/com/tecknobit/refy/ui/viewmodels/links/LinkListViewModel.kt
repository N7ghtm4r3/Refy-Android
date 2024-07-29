package com.tecknobit.refy.ui.viewmodels.links

import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.screens.links.LinkListScreen
import com.tecknobit.refycore.helpers.RefyInputValidator.isDescriptionValid
import com.tecknobit.refycore.helpers.RefyInputValidator.isLinkResourceValid
import com.tecknobit.refycore.records.RefyUser
import com.tecknobit.refycore.records.links.RefyLink

class LinkListViewModel : LinksViewModel<RefyLink>() {

    override fun getLinks() {
        execRefreshingRoutine(
            currentContext = LinkListScreen::class.java,
            routine = {
                // TODO: MAKE REQUEST THEN
                _links.value = listOf(
                    RefyLink(
                        "id",
                        "tille",
                        null,//"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                        "https://github.com/N7ghtm4r3",
                    ),
                    RefyLink(
                        "id1",
                        RefyUser("h"),
                        "tille",
                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                        "https://github.com/N7ghtm4r3",
                        listOf(),
                        listOf()
                    )
                )
                user.links = _links.value
            },
            repeatRoutine = false // TODO: TO REMOVE
        )
    }

    override fun addNewLink(
        onSuccess: () -> Unit
    ) {
        if(!isLinkResourceValid(linkReference.value)) {
            linkReferenceError.value = true
            return
        }
        if(!isDescriptionValid(linkDescription.value)) {
            linkDescriptionError.value = true
            return
        }
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    override fun editLink(
        link: RefyLink,
        onSuccess: () -> Unit
    ) {
        if(!isLinkResourceValid(linkReference.value)) {
            linkReferenceError.value = true
            return
        }
        if(!isDescriptionValid(linkDescription.value)) {
            linkDescriptionError.value = true
            return
        }
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    override fun addLinkToTeam(
        link: RefyLink,
        teams: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    override fun addLinkToCollection(
        link: RefyLink,
        collections: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    override fun deleteLink(
        link: RefyLink,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

}