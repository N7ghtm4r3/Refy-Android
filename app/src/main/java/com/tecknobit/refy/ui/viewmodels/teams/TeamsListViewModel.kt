package com.tecknobit.refy.ui.viewmodels.teams

import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screens.TeamsListScreen
import com.tecknobit.refycore.records.RefyUser
import com.tecknobit.refycore.records.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TeamsListViewModel: TeamViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    private val _teams = MutableStateFlow<List<Team>>(
        value = emptyList()
    )
    val teams: StateFlow<List<Team>> = _teams

    fun getTeams() {
        execRefreshingRoutine(
            currentContext = TeamsListScreen::class.java,
            routine = {
                // TODO: MAKE REQUEST THEN
                _teams.value = listOf(
                    Team("id12", "Ciaogwegw2", RefyUser(), "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg"),
                    Team("35525", "Ciaogwegw22", RefyUser(),  "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg")
                )
                user.teams = _teams.value
            },
            repeatRoutine = false // TODO: TO REMOVE
        )
    }

}