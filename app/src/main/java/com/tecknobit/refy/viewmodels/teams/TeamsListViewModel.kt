package com.tecknobit.refy.viewmodels.teams

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screens.TeamsListScreen
import com.tecknobit.refy.viewmodels.RefyViewModel
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.returnTeams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The **TeamsListViewModel** class is the support class used by [TeamsListScreen] to communicate
 * with the backend and to execute the refreshing routines to update the UI data and working with the
 * [Team]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see RefyViewModel
 * @see TeamViewModelHelper
 *
 */
class TeamsListViewModel: TeamViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    /**
     * **_teams** -> the current teams list displayed
     */
    private val _teams = MutableStateFlow<SnapshotStateList<Team>>(
        value = mutableStateListOf()
    )
    val teams: StateFlow<List<Team>> = _teams

    /**
     * Function to execute the request to get the teams list
     *
     * No-any params required
     */
    fun getTeams() {
        sendFetchRequest(
            currentContext = TeamsListScreen::class.java,
            request = {
                requester.getTeams()
            },
            onSuccess = { response ->
                _teams.value = returnTeams(response.getJSONArray(RESPONSE_MESSAGE_KEY))
                    .toMutableStateList()
                localUser.setTeams(_teams.value)
            }
        )
    }

}