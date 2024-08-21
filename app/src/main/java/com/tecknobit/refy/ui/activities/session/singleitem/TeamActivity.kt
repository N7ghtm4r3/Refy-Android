@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.singleitem

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.CallSuper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import com.tecknobit.refy.helpers.NavigationHelper.Companion.activeTab
import com.tecknobit.refy.helpers.SessionManager
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.session.RefyItemBaseActivity
import com.tecknobit.refy.ui.getCompleteMediaItemUrl
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.utilities.ItemDescription
import com.tecknobit.refy.utilities.Logo
import com.tecknobit.refy.utilities.OptionsBar
import com.tecknobit.refy.utilities.RefyLinkUtilities
import com.tecknobit.refy.utilities.TeamMemberPlaque
import com.tecknobit.refy.utilities.TeamsUtilities
import com.tecknobit.refy.utilities.drawOneSideBorder
import com.tecknobit.refy.utilities.getItemRelations
import com.tecknobit.refy.viewmodels.teams.TeamActivityViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.IDENTIFIER_KEY
import com.tecknobit.refycore.records.links.RefyLink

/**
 * The **TeamActivity** class is useful to display a [Team]'s details and manage
 * that team
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see RefyItemBaseActivity
 * @see SingleItemActivity
 * @see RefyLinkUtilities
 * @see TeamsUtilities
 * @see SessionManager
 */
class TeamActivity : SingleItemActivity<Team>(
    items = localUser.getTeams(false),
    invalidMessage = R.string.invalid_team
), RefyLinkUtilities<RefyLink>, TeamsUtilities {

    /**
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    private lateinit var viewModel: TeamActivityViewModel

    /**
     * *linksExpanded* -> whether the links section is expanded
     */
    private lateinit var linksExpanded: MutableState<Boolean>

    /**
     * *membersExpanded* -> whether the members section is expanded
     */
    private lateinit var membersExpanded: MutableState<Boolean>

    /**
     * *isUserAdmin* -> whether the current [localUser] is an admin of the current team ([item])
     */
    private var isUserAdmin: Boolean = false

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        prepareView()
        setContent {
            ContentView {
                item = viewModel.team.collectAsState().value
                activityColorTheme = MaterialTheme.colorScheme.primaryContainer
                isUserAdmin = item!!.isAdmin(localUser.userId)
                linksExpanded = remember { mutableStateOf(item!!.links.isNotEmpty()) }
                membersExpanded = remember { mutableStateOf(false) }
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        LargeTopAppBar(
                            navigationIcon = { NavButton() },
                            title = {
                                Box {
                                    Logo(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart),
                                        picSize = 65.dp,
                                        picUrl = getCompleteMediaItemUrl(
                                            relativeMediaUrl = item!!.logoPic
                                        )
                                    )
                                    Column (
                                        modifier = Modifier
                                            .fillMaxHeight(),
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .align(Alignment.End)
                                                .padding(
                                                    start = 35.dp
                                                ),
                                            text = item!!.title,
                                            fontSize = 24.sp
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = activityColorTheme
                            ),
                            actions = {
                                AnimatedVisibility(
                                    visible = isUserAdmin,
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
                                            team = item!!,
                                            tint = iconsColor
                                        )
                                        val addCollections = remember { mutableStateOf(false) }
                                        val collections = getItemRelations(
                                            userList = localUser.getCollections(true),
                                            currentAttachments = item!!.collections
                                        )
                                        AddCollectionsButton(
                                            viewModel = viewModel,
                                            show = addCollections,
                                            collections = collections,
                                            team = item!!,
                                            tint = iconsColor
                                        )
                                    }
                                }
                                if(item!!.isTheAuthor(localUser.userId)) {
                                    val deleteTeam = remember { mutableStateOf(false) }
                                    DeleteTeamButton(
                                        activity = this@TeamActivity,
                                        viewModel = viewModel,
                                        deleteTeam = deleteTeam,
                                        team = item!!,
                                        tint = iconsColor
                                    )
                                } else {
                                    val leaveTeam = remember { mutableStateOf(false) }
                                    LeaveTeamButton(
                                        activity = this@TeamActivity,
                                        viewModel = viewModel,
                                        leaveTeam = leaveTeam,
                                        team = item!!,
                                        tint = iconsColor
                                    )
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        AnimatedVisibility(
                            visible = item!!.members.size > 1,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            TeamMembers()
                            FloatingActionButton(
                                onClick = { membersExpanded.value = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.People,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    TeamContent(
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }

    /**
     * Function to display the content of the team
     *
     * @param paddingValues: the padding to use to correctly display the content
     */
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
            ItemsSection(
                isSectionVisible = item!!.links.isNotEmpty(),
                header = R.string.links,
                visible = linksExpanded.value
            ) {
                items(
                    items = item!!.links,
                    key = { link -> link.id }
                ) { link ->
                    RefyLinkContainerCard(
                        link = link,
                        hideOptions = !isUserAdmin,
                        removeAction = {
                            viewModel.removeLinkFromTeam(
                                link = link
                            )
                        }
                    )
                }
            }
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
                        collection = collection
                    )
                }
            }
        }
    }

    /**
     * Function to display an items section such links or collections section shared with the team
     *
     * @param isSectionVisible: whether the section can be visible, so the [items] is not empty
     * @param header: the resource identifier of the header text
     * @param visible: whether the section is currently visible
     * @param items: the items list to display
     */
    @Composable
    @NonRestartableComposable
    private fun ItemsSection(
        isSectionVisible: Boolean,
        header: Int,
        visible: Boolean,
        items: LazyListScope.() -> Unit
    ) {
        if(isSectionVisible) {
            HorizontalDivider()
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

    /**
     * Function to create the control header to hide or unhidden an [ItemsSection]
     *
     * @param header: the resource identifier of the header text
     * @param iconCheck: the icon for the button to hide or unhidden the section
     */
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

    /**
     * Function to create an [LinksCollection] card to display the details of that collection and
     * to give the rapid actions such removing from the team
     *
     * @param collection: the collection to display
     */
    @Composable
    @NonRestartableComposable
    private fun LinksCollectionTeamCard(
        collection: LinksCollection
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .drawOneSideBorder(
                    width = 10.dp,
                    color = collection.color.toColor(),
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp
                    )
                ),
            shape = RoundedCornerShape(
                size = 8.dp
            ),
            onClick = {
                localUser.setCollections(item!!.collections)
                val intent = Intent(this, CollectionActivity::class.java)
                intent.putExtra(IDENTIFIER_KEY, collection.id)
                startActivity(intent)
            }
        ) {
            Column {
                TopBarDetails(
                    item = collection,
                    overlineColor = collection.color.toColor()
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 5.dp,
                            start = 21.dp,
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
                AnimatedVisibility(
                    visible = isUserAdmin,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    OptionsBar(
                        options = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                IconButton(
                                    onClick = {
                                        viewModel.removeCollectionFromTeam(
                                            collection = collection
                                        )
                                    }
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

    /**
     * Function to display the members of the team displayed and, if authorized, removing or change
     * the role members
     *
     * No-any params required
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    @NonRestartableComposable
    private fun TeamMembers() {
        if(membersExpanded.value) {
            ModalBottomSheet(
                onDismissRequest = {
                    membersExpanded.value = false
                }
            ) {
                LazyColumn {
                    items(
                        items = item!!.members,
                        key = { member -> member.id }
                    ) { member ->
                        TeamMemberPlaque(
                            team = item!!,
                            member = member,
                            viewModel = viewModel
                        )
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
            viewModel = TeamActivityViewModel(
                snackbarHostState = snackbarHostState,
                initialTeam = item!!
            )
            viewModel.setActiveContext(this::class.java)
            viewModel.refreshTeam()
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

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or {@link #onPause}. This
     * is usually a hint for your activity to start interacting with the user, which is a good
     * indicator that the activity became active and ready to receive input. This sometimes could
     * also be a transit state toward another resting state. For instance, an activity may be
     * relaunched to {@link #onPause} due to configuration changes and the activity was visible,
     * but wasn’t the top-most activity of an activity task. {@link #onResume} is guaranteed to be
     * called before {@link #onPause} in this case which honors the activity lifecycle policy and
     * the activity eventually rests in {@link #onPause}.
     *
     * <p>On platform versions prior to {@link android.os.Build.VERSION_CODES#Q} this is also a good
     * place to try to open exclusive-access devices or to get access to singleton resources.
     * Starting  with {@link android.os.Build.VERSION_CODES#Q} there can be multiple resumed
     * activities in the system simultaneously, so {@link #onTopResumedActivityChanged(boolean)}
     * should be used for that purpose instead.
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * Will be restarted the refresher of the current [activeTab] suspended before
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     * @see #onTopResumedActivityChanged(boolean)
     */
    override fun onResume() {
        super.onResume()
        if(::viewModel.isInitialized) {
            viewModel.setActiveContext(this::class.java)
            viewModel.restartRefresher()
        }
    }

}
