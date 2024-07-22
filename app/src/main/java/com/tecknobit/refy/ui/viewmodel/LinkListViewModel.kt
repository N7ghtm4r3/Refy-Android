package com.tecknobit.refy.ui.viewmodel

import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screen.LinkListScreen
import com.tecknobit.refycore.records.RefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LinkListViewModel(
) : EquinoxViewModel(
    snackbarHostState = snackbarHostState
) {

    private val _links = MutableStateFlow<List<RefyLink>>(
        value = emptyList()
    )
    val links: StateFlow<List<RefyLink>> = _links

    fun getLinks() {
        execRefreshingRoutine(
            currentContext = LinkListScreen::class.java,
            routine = {
                // TODO: MAKE REQUEST THEN
                _links.value = listOf(
                    RefyLink(
                        "id",
                        "tille",
                        null,//"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                        "https://github.com/N7ghtm4r3"
                    ),
                    RefyLink(
                        "id1",
                        "tille",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                        "https://github.com/N7ghtm4r3"
                    )
                )
            },
            repeatRoutine = false // TODO: TO REMOVE
        )
    }

    fun addLinkToTeam(
        link: RefyLink,
        teams: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    fun addLinkToCollection(
        link: RefyLink,
        collections: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    fun deleteLink(
        link: RefyLink,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

}