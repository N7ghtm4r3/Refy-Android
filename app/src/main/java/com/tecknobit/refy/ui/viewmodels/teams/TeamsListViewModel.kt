package com.tecknobit.refy.ui.viewmodels.teams

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screens.TeamsListScreen
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.returnTeams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class TeamsListViewModel: TeamViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    private val _teams = MutableStateFlow<SnapshotStateList<Team>>(
        value = mutableStateListOf()
    )
    val teams: StateFlow<List<Team>> = _teams

    fun getTeams() {
        execRefreshingRoutine(
            currentContext = TeamsListScreen::class.java,
            routine = {
                requester.sendRequest(
                    request = {
                        requester.getTeams()
                    },
                    onSuccess = { response ->
                        _teams.value = returnTeams(response.getJSONArray(RESPONSE_MESSAGE_KEY))
                            .toMutableStateList()
                        localUser.setTeams(_teams.value)
                    },
                    onFailure = { showSnackbarMessage(it) }
                )
            }
        )
    }

}