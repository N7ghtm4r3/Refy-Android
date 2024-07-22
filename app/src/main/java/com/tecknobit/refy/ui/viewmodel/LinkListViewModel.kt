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
                        "https://github.com/N7ghtm4r3"
                    )
                )
            },
            repeatRoutine = false // TODO: TO REMOVE
        )
    }

}