package com.tecknobit.refy.ui.utilities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.viewmodels.teams.TeamViewModelHelper
import com.tecknobit.refycore.records.Team

interface TeamsUtilities {

    @Composable
    @NonRestartableComposable
    fun DeleteTeamButton(
        viewModel: TeamViewModelHelper,
        deleteTeam: MutableState<Boolean>,
        team: Team,
        tint: Color
    ) {
        DeleteItemButton(
            show = deleteTeam,
            deleteAction = {
                DeleteTeam(
                    show = deleteTeam,
                    team = team,
                    viewModel = viewModel
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun DeleteTeam(
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        team: Team
    ) {
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = show
        )
        EquinoxAlertDialog(
            show = show,
            icon = Icons.Default.Delete,
            title = stringResource(R.string.delete_team),
            text = stringResource(R.string.delete_team_message),
            dismissText = stringResource(R.string.dismiss),
            confirmAction = {
                viewModel.deleteTeam(
                    team = team,
                    onSuccess = {
                        show.value = false
                    }
                )
            },
            confirmText = stringResource(R.string.confirm),
        )
    }

}