@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.refy.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupOff
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.session.create.CreateTeamActivity
import com.tecknobit.refy.ui.activities.session.singleitem.TeamActivity
import com.tecknobit.refy.ui.getCompleteMediaItemUrl
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.ui.utilities.ItemDescription
import com.tecknobit.refy.ui.utilities.Logo
import com.tecknobit.refy.ui.utilities.OptionsBar
import com.tecknobit.refy.ui.utilities.RefyLinkUtilities
import com.tecknobit.refy.ui.utilities.TeamsUtilities
import com.tecknobit.refy.ui.utilities.getItemRelations
import com.tecknobit.refy.ui.viewmodels.teams.TeamsListViewModel
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.links.RefyLink

class TeamsListScreen: Screen(), TeamsUtilities, RefyLinkUtilities<RefyLink> {

    private val viewModel = TeamsListViewModel()

    private lateinit var teams: List<Team>

    @Composable
    override fun ShowContent() {
        viewModel.setActiveContext(this::class.java)
        screenViewModel = viewModel
        viewModel.getTeams()
        teams = viewModel.teams.collectAsState().value
        SetFabAction()
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
                    TeamCard(
                        team = team
                    )
                }
            }
        }
    }

    @Composable
    override fun SetFabAction() {
        context = LocalContext.current
    }

    override fun executeFabAction() {
        context.startActivity(Intent(context, CreateTeamActivity::class.java))
    }

    @Composable
    @NonRestartableComposable
    private fun TeamCard(
        team: Team
    ) {
        val isAdmin = team.isAdmin(localUser.userId)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .combinedClickable(
                    onClick = {
                        navToDedicatedItemActivity(
                            itemId = team.id,
                            destination = TeamActivity::class.java
                        )
                    },
                    onLongClick = if (isAdmin) {
                        {
                            navToDedicatedItemActivity(
                                itemId = team.id,
                                destination = CreateTeamActivity::class.java
                            )
                        }
                    } else
                        null
                ),
            shape = RoundedCornerShape(
                size = 8.dp
            )
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 5.dp,
                            bottom = 5.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    TeamDetails(
                        team = team
                    )
                }
                OptionsBar(
                    isMaintainer = isAdmin,
                    team = team
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun TeamDetails(
        team: Team
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            leadingContent = {
                Logo(
                    picSize = 115.dp,
                    shape = RoundedCornerShape(
                        size = 10.dp
                    ),
                    addShadow = true,
                    picUrl = getCompleteMediaItemUrl(
                        relativeMediaUrl = team.logoPic
                    )
                )
            },
            overlineContent = {
                PicturesRow(
                    pictures = {
                        val profiles = mutableListOf<String>()
                        team.members.forEach { member ->
                            profiles.add(member.profilePic)
                        }
                        profiles
                    },
                    pictureSize = 20.dp
                )
            },
            headlineContent = {
                Text(
                    modifier = Modifier
                        .padding(
                            bottom = 5.dp
                        ),
                    text = team.title,
                    fontSize = 20.sp,
                    fontFamily = displayFontFamily,
                    fontStyle = AppTypography.titleMedium.fontStyle
                )
            },
            supportingContent = {
                ItemDescription(
                    description = team.description
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun OptionsBar(
        isMaintainer: Boolean,
        team: Team
    ) {
        val addLinks = remember { mutableStateOf(false) }
        val addCollections = remember { mutableStateOf(false) }
        val leaveTeam = remember { mutableStateOf(false) }
        val deleteTeam = remember { mutableStateOf(false) }
        OptionsBar(
            options = {
                AnimatedVisibility(
                    visible = isMaintainer,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row {
                        val iconsColor = LocalContentColor.current
                        val links = getItemRelations(
                            userList = localUser.links,
                            linkList = team.links
                        )
                        AddLinksButton(
                            viewModel = viewModel,
                            show = addLinks,
                            links = links,
                            team = team,
                            tint = iconsColor
                        )
                        val collections = getItemRelations(
                            userList = localUser.collections,
                            linkList = team.collections
                        )
                        AddCollectionsButton(
                            viewModel = viewModel,
                            show = addCollections,
                            collections = collections,
                            team = team,
                            tint = iconsColor
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Row {
                        if(team.isTheAuthor(localUser.userId)) {
                            DeleteTeamButton(
                                activity = null,
                                viewModel = viewModel,
                                deleteTeam = deleteTeam,
                                team = team,
                                tint = MaterialTheme.colorScheme.error
                            )
                        } else {
                            LeaveTeamButton(
                                activity = null,
                                viewModel = viewModel,
                                leaveTeam = leaveTeam,
                                team = team,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        )
    }

}