@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.singleitem

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.utilities.ItemDescription
import com.tecknobit.refy.ui.utilities.RefyLinkUtilities
import com.tecknobit.refy.ui.utilities.TeamsUtilities
import com.tecknobit.refy.ui.utilities.getItemRelations
import com.tecknobit.refy.ui.viewmodels.teams.TeamActivityViewModel
import com.tecknobit.refycore.records.Team

class TeamActivity : SingleItemActivity<Team>(
    items = user.teams,
    invalidMessage = R.string.invalid_team
), RefyLinkUtilities, TeamsUtilities {

    private lateinit var viewModel: TeamActivityViewModel

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RefyTheme {
                initItemFromIntent()
                if(invalidItem)
                    InvalidItemUi()
                else {
                    InitViewModel()
                    Scaffold(
                        topBar = {
                            LargeTopAppBar(
                                colors = TopAppBarDefaults.largeTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                navigationIcon = { NavButton() },
                                title = {
                                    Text(
                                        text = item!!.title
                                    )
                                },
                                actions = {
                                    val links = getItemRelations(
                                        userList = user.links,
                                        linkList = item!!.links
                                    )
                                    val addLinks = remember { mutableStateOf(false) }
                                    AddLinksButton(
                                        viewModel = viewModel,
                                        show = addLinks,
                                        links = links,
                                        team = item!!,
                                        tint = iconsColor
                                    )
                                    val addCollections = remember { mutableStateOf(false) }
                                    val collections = getItemRelations(
                                        userList = user.collections,
                                        linkList = item!!.collections
                                    )
                                    AddCollectionsButton(
                                        viewModel = viewModel,
                                        show = addCollections,
                                        collections = collections,
                                        team = item!!,
                                        tint = iconsColor
                                    )
                                    val deleteTeam = remember { mutableStateOf(false) }
                                    if(item!!.isTheAuthor(user)) {
                                        DeleteTeamButton(
                                            activity = this@TeamActivity,
                                            viewModel = viewModel,
                                            deleteTeam = deleteTeam,
                                            team = item!!,
                                            tint = iconsColor
                                        )
                                    }
                                }
                            )
                        }
                    ) { paddingValues ->
                        TeamContent(
                            paddingValues = paddingValues
                        )
                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    override fun InitViewModel() {
        viewModel = TeamActivityViewModel(
            snackbarHostState = snackbarHostState,
            initialTeam = item!!
        )
        viewModel.setActiveContext(this::class.java)
        viewModel.refreshTeam()
        item = viewModel.team.collectAsState().value
    }

    @Composable
    @NonRestartableComposable
    private fun TeamContent(
        paddingValues: PaddingValues
    ) {
        overlineColor = MaterialTheme.colorScheme.primary
        Column {
            ItemDescription(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                description = item!!.description
            )
            HorizontalDivider()
            LazyColumn (
                modifier = Modifier
                    .padding(
                        all = 16.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = item!!.links,
                    key = { link -> link.id }
                ) { link ->
                    RefyLinkCollectionCard(
                        link = link,
                        removeAction = {
                            viewModel.removeLinkFromTeam(
                                link = link
                            )
                        }
                    )
                }
            }
        }
    }

}
