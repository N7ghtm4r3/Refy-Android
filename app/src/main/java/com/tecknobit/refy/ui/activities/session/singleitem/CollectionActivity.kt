package com.tecknobit.refy.ui.activities.session.singleitem

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.ui.utilities.ExpandTeamMembers
import com.tecknobit.refy.ui.utilities.LinksCollectionUtilities
import com.tecknobit.refy.ui.utilities.RefyLinkUtilities
import com.tecknobit.refy.ui.utilities.getItemRelations
import com.tecknobit.refy.ui.viewmodels.collections.CollectionActivityViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.links.RefyLink

class CollectionActivity : SingleItemActivity<LinksCollection>(
    items = localUser.collections,
    invalidMessage = R.string.invalid_collection
), RefyLinkUtilities<RefyLink>, LinksCollectionUtilities {

    private lateinit var viewModel: CollectionActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DisplayItem(
                topBarColor = null,
                actions = {
                    AnimatedVisibility(
                        visible = item!!.canBeUpdatedByUser(localUser.userId),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Row {
                            val links = getItemRelations(
                                userList = localUser.links,
                                linkList = item!!.links
                            )
                            val addLinks = remember { mutableStateOf(false) }
                            AddLinksButton(
                                viewModel = viewModel,
                                show = addLinks,
                                links = links,
                                collection = item!!,
                                tint = iconsColor
                            )
                            val teams = getItemRelations(
                                userList = localUser.teams,
                                linkList = item!!.teams
                            )
                            val addTeams = remember { mutableStateOf(false) }
                            AddTeamsButton(
                                viewModel = viewModel,
                                show = addTeams,
                                teams = teams,
                                collection = item!!,
                                tint = iconsColor
                            )
                            val deleteCollection = remember { mutableStateOf(false) }
                            DeleteCollectionButton(
                                activity = this@CollectionActivity,
                                viewModel = viewModel,
                                deleteCollection = deleteCollection,
                                collection = item!!,
                                tint = iconsColor
                            )
                        }
                    }
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = hasTeams,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        val expandTeams = remember { mutableStateOf(false) }
                        FloatingActionButton(
                            onClick = { expandTeams.value = true },
                            containerColor = activityColorTheme
                        ) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = null
                            )
                        }
                        ExpandTeamMembers(
                            viewModel = viewModel,
                            show = expandTeams,
                            teams = item!!.teams
                        )
                    }
                },
                content = { paddingValues ->
                    val userCanUpdate = item!!.canBeUpdatedByUser(localUser.userId)
                    LazyColumn (
                        modifier = Modifier
                            .padding(
                                top = paddingValues.calculateTopPadding() + 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = item!!.links,
                            key = { link -> link.id }
                        ) { link ->
                            RefyLinkContainerCard(
                                link = link,
                                hideOptions = !userCanUpdate,
                                removeAction = {
                                    viewModel.removeLinkFromCollection(
                                        link = link
                                    )
                                }
                            )
                        }
                    }
                }
            )
        }
    }

    @Composable
    override fun InitViewModel() {
        viewModel = CollectionActivityViewModel(
            snackbarHostState = snackbarHostState,
            initialCollection = item!!
        )
        viewModel.setActiveContext(this::class.java)
        viewModel.refreshCollection()
        item = viewModel.collection.collectAsState().value
        activityColorTheme = item!!.color.toColor()
        hasTeams = item!!.hasTeams()
    }

}
