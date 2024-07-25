package com.tecknobit.refy.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.utilities.OptionsBar
import com.tecknobit.refy.ui.utilities.TeamsUtilities
import com.tecknobit.refy.ui.viewmodels.teams.TeamsListViewModel
import com.tecknobit.refycore.records.Team

class TeamsListScreen: Screen(), TeamsUtilities {

    private val viewModel = TeamsListViewModel()

    private lateinit var teams: List<Team>

    init {
        viewModel.setActiveContext(this::class.java)
    }

    @Composable
    override fun ShowContent() {
        screenViewModel = viewModel
        viewModel.getTeams()
        teams = viewModel.teams.collectAsState().value
        if(teams.isEmpty()) {
            EmptyListUI(
                icon = Icons.Default.GroupOff,
                subText = stringResource(R.string.you_re_not_on_any_team)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = teams,
                    key = { team -> team.id }
                ) { team ->

                }
            }
        }
    }

    @Composable
    override fun SetFabAction() {
        TODO("Not yet implemented")
    }

    override fun executeFabAction() {
        TODO("Not yet implemented")
    }

    @Composable
    private fun OptionsBar(
        team: Team
    ) {
        val addToTeam = remember { mutableStateOf(false) }
        val deleteTeam = remember { mutableStateOf(false) }
        OptionsBar(
            options = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    DeleteTeamButton(
                        viewModel = viewModel,
                        deleteTeam = deleteTeam,
                        team = team,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
    }

}