@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.singleitem

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.ui.utilities.ExpandTeamMembers
import com.tecknobit.refy.ui.utilities.LinksCollectionUtilities
import com.tecknobit.refy.ui.utilities.RefyLinkUtilities
import com.tecknobit.refy.ui.utilities.getItemRelations
import com.tecknobit.refy.ui.viewmodels.collections.CollectionActivityViewModel
import com.tecknobit.refycore.records.LinksCollection

class CollectionActivity : SingleItemActivity<LinksCollection>(
    items = user.collections,
    invalidMessage = R.string.invalid_collection
), RefyLinkUtilities, LinksCollectionUtilities {

    private lateinit var viewModel: CollectionActivityViewModel

    private var collectionColor: Color = Color.Red

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            initItemFromIntent()
            RefyTheme {
                if(invalidItem)
                    InvalidItemUi()
                else {
                    InitViewModel()
                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                        topBar = {
                            LargeTopAppBar(
                                navigationIcon = {
                                    iconsColor = LocalContentColor.current
                                    IconButton(
                                        onClick = { finish() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = null
                                        )
                                    }
                                },
                                title = {
                                    Text(
                                        text = item!!.title
                                    )
                                },
                                colors = TopAppBarDefaults.largeTopAppBarColors(
                                    containerColor = collectionColor
                                ),
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
                                        collection = item!!,
                                        tint = iconsColor
                                    )
                                    val teams = getItemRelations(
                                        userList = user.teams,
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
                            )
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
                                    containerColor = collectionColor
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
                    ) { paddingValues ->
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
                                RefyLinkCollectionCard(
                                    link = link,
                                    removeAction = {
                                        viewModel.removeLinkFromCollection(
                                            link = link
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    override fun InitViewModel() {
        viewModel = CollectionActivityViewModel(
            snackbarHostState = snackbarHostState,
            initialCollection = item!!
        )
        viewModel.setActiveContext(this::class.java)
        viewModel.refreshCollection()
        item = viewModel.collection.collectAsState().value
        collectionColor = item!!.color.toColor()
        overlineColor = collectionColor
        hasTeams = item!!.hasTeams()
    }

}
