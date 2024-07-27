package com.tecknobit.refy.ui.activities.session.singleitem

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.ui.utilities.ItemDescription
import com.tecknobit.refy.ui.utilities.Logo
import com.tecknobit.refy.ui.utilities.OptionsBar
import com.tecknobit.refy.ui.utilities.RefyLinkUtilities
import com.tecknobit.refy.ui.utilities.TeamsUtilities
import com.tecknobit.refy.ui.utilities.getItemRelations
import com.tecknobit.refy.ui.viewmodels.teams.TeamActivityViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.Team

class TeamActivity : SingleItemActivity<Team>(
    items = user.teams,
    invalidMessage = R.string.invalid_team
), RefyLinkUtilities, TeamsUtilities {

    private lateinit var viewModel: TeamActivityViewModel

    private lateinit var linksExpanded: MutableState<Boolean>

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RefyTheme {
                activityColorTheme = MaterialTheme.colorScheme.primary
                DisplayItem(
                    topBarColor = MaterialTheme.colorScheme.primaryContainer,
                    title = {
                        Box {
                            Logo(
                                modifier = Modifier
                                    .padding(
                                        top = 30.dp
                                    )
                                    .align(Alignment.BottomStart),
                                picSize = 100.dp,
                                picUrl = item!!.logoPic
                            )
                            Text(
                                modifier = Modifier
                                    .padding(
                                        start = 20.dp
                                    ),
                                text = item!!.title
                            )
                        }
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
                    },
                    floatingActionButton = {
                        AnimatedVisibility(
                            visible = item!!.members.size > 1,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            FloatingActionButton(
                                onClick = { /*TODO*/ }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.People,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    content = { paddingValues ->
                        TeamContent(
                            paddingValues = paddingValues
                        )
                    }
                )
                linksExpanded = remember { mutableStateOf(item!!.links.isNotEmpty()) }
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
            ItemsSection(
                isSectionVisible = item!!.links.isNotEmpty(),
                header = R.string.links,
                visible = linksExpanded.value
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
            HorizontalDivider()
            ItemsSection(
                isSectionVisible = item!!.collections.isNotEmpty(),
                header = R.string.collections,
                visible = !linksExpanded.value
            ) {
                items(
                    items = item!!.collections,
                    key = { collection -> collection.id }
                ) { collection ->
                    LinksCollectionTeamCard(
                        collection = collection,
                        removeAction = {
                            viewModel.removeCollectionFromTeam(
                                collection = collection
                            )
                        }
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ItemsSection(
        isSectionVisible: Boolean,
        header: Int,
        visible: Boolean,
        items: LazyListScope.() -> Unit
    ) {
        if(isSectionVisible) {
            ControlSectionHeader(
                header = header,
                iconCheck = visible
            )
            AnimatedVisibility(
                visible = visible
            ) {
                LazyColumn (
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    items.invoke(this)
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ControlSectionHeader(
        header: Int,
        iconCheck: Boolean
    ) {
        Row (
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(header),
                fontFamily = displayFontFamily,
                style = AppTypography.titleLarge,
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(
                onClick = { linksExpanded.value = !linksExpanded.value }
            ) {
                Icon(
                    imageVector = if(iconCheck)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun LinksCollectionTeamCard(
        collection: LinksCollection,
        removeAction: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(
                size = 8.dp
            ),
            onClick = {

            }
        ) {
            Column {
                TopBarDetails(
                    item = collection
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 5.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 5.dp
                        )
                ) {
                    Text(
                        text = collection.title,
                        fontFamily = displayFontFamily,
                        fontSize = 25.sp,
                        fontStyle = AppTypography.titleMedium.fontStyle
                    )
                    ItemDescription(
                        description = collection.description
                    )
                }
                OptionsBar(
                    options = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            IconButton(
                                onClick = removeAction
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                )
            }
        }
    }

}
