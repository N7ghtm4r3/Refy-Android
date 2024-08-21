package com.tecknobit.refy.ui.activities.session.singleitem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.CallSuper
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.R
import com.tecknobit.refy.helpers.NavigationHelper.Companion.activeTab
import com.tecknobit.refy.helpers.SessionManager
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.session.RefyItemBaseActivity
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.utilities.ExpandTeamMembers
import com.tecknobit.refy.utilities.LinksCollectionUtilities
import com.tecknobit.refy.utilities.RefyLinkUtilities
import com.tecknobit.refy.utilities.getItemRelations
import com.tecknobit.refy.viewmodels.collections.CollectionActivityViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.links.RefyLink

/**
 * The **CollectionActivity** class is useful to display a [LinksCollection]'s details and manage
 * that collection
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see RefyItemBaseActivity
 * @see SingleItemActivity
 * @see RefyLinkUtilities
 * @see LinksCollectionUtilities
 * @see SessionManager
 */
class CollectionActivity : SingleItemActivity<LinksCollection>(
    items = localUser.getCollections(false),
    invalidMessage = R.string.invalid_collection
), RefyLinkUtilities<RefyLink>, LinksCollectionUtilities {

    /**
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    private lateinit var viewModel: CollectionActivityViewModel

    /**
     * On create method
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     * If your ComponentActivity is annotated with {@link ContentView}, this will
     * call {@link #setContentView(int)} for you.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        prepareView()
        setContent {
            ContentView {
                item = viewModel.collection.collectAsState().value
                activityColorTheme = item!!.color.toColor()
                hasTeams = item!!.hasTeams()
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        LargeTopAppBar(
                            navigationIcon = { NavButton() },
                            title = {
                                Text(
                                    text = item!!.title
                                )
                            },
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = activityColorTheme
                            ),
                            actions = {
                                AnimatedVisibility(
                                    visible = item!!.canBeUpdatedByUser(localUser.userId),
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Row {
                                        val links = getItemRelations(
                                            userList = localUser.getLinks(true),
                                            currentAttachments = item!!.links
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
                                            userList = localUser.getTeams(true),
                                            currentAttachments = item!!.teams
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
                    }
                ) { paddingValues ->
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
            }
        }
    }

    /**
     * Function to prepare the view initializing the [item] by invoking the [initItemFromIntent]
     * method, will be initialized the [viewModel] and started its refreshing routine to refresh the
     * [item]
     *
     * No-any params required
     */
    @CallSuper
    override fun prepareView() {
        super.prepareView()
        if(itemExists) {
            viewModel = CollectionActivityViewModel(
                snackbarHostState = snackbarHostState,
                initialCollection = item!!
            )
            viewModel.setActiveContext(this::class.java)
            viewModel.refreshCollection()
        }
    }

    /**
     * Called as part of the activity lifecycle when the user no longer actively interacts with the
     * activity, but it is still visible on screen. The counterpart to {@link #onResume}.
     *
     * <p>When activity B is launched in front of activity A, this callback will
     * be invoked on A.  B will not be created until A's {@link #onPause} returns,
     * so be sure to not do anything lengthy here.
     *
     * <p>This callback is mostly used for saving any persistent state the
     * activity is editing, to present a "edit in place" model to the user and
     * making sure nothing is lost if there are not enough resources to start
     * the new activity without first killing this one.  This is also a good
     * place to stop things that consume a noticeable amount of CPU in order to
     * make the switch to the next activity as fast as possible.
     *
     * <p>On platform versions prior to {@link android.os.Build.VERSION_CODES#Q} this is also a good
     * place to try to close exclusive-access devices or to release access to singleton resources.
     * Starting with {@link android.os.Build.VERSION_CODES#Q} there can be multiple resumed
     * activities in the system at the same time, so {@link #onTopResumedActivityChanged(boolean)}
     * should be used for that purpose instead.
     *
     * <p>If an activity is launched on top, after receiving this call you will usually receive a
     * following call to {@link #onStop} (after the next activity has been resumed and displayed
     * above). However in some cases there will be a direct call back to {@link #onResume} without
     * going through the stopped state. An activity can also rest in paused state in some cases when
     * in multi-window mode, still visible to user.
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * Will be suspended the refresher of the current [activeTab]
     *
     * @see #onResume
     * @see #onSaveInstanceState
     * @see #onStop
     */
    override fun onPause() {
        super.onPause()
        viewModel.suspendRefresher()
    }

}
