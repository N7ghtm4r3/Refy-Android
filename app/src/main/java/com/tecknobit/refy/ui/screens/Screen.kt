package com.tecknobit.refy.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.helpers.SessionManager
import com.tecknobit.refy.ui.getCompleteMediaItemUrl
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.utilities.ExpandTeamMembers
import com.tecknobit.refy.utilities.ItemDescription
import com.tecknobit.refy.utilities.drawOneSideBorder
import com.tecknobit.refy.utilities.isItemOwner
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.IDENTIFIER_KEY
import com.tecknobit.refycore.records.Team.MAX_TEAMS_DISPLAYED

/**
 * The **Screen** class is useful to give the basic structure for a Refy's screen to display
 * the [RefyItem]'s list
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see SessionManager
 */
@Structure
abstract class Screen : SessionManager {

    /**
     * *screenViewModel* -> the support view model used in the screen to manage the requests to the backend
     */
    protected lateinit var screenViewModel: EquinoxViewModel

    /**
     * *currentScreenContext* -> the current context of the screen displayed
     */
    protected lateinit var currentScreenContext: Class<*>

    /**
     * *context* -> the support context instance
     */
    protected lateinit var context: Context

    companion object {

        /**
         * *isServerOffline* -> state to manage the server offline scenario
         */
        lateinit var isServerOffline: MutableState<Boolean>

        /**
         * *haveBeenDisconnected* -> when the account has been deleted and the session needs to
         * be detached from the device
         */
        lateinit var haveBeenDisconnected: MutableState<Boolean>

    }

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @Composable
    abstract fun ShowContent()

    /**
     * Function to set the action to execute when the [FloatingActionButton] has been clicked
     *
     * No-any params required
     */
    @Composable
    abstract fun SetFabAction()

    /**
     * Function to execute the fab action previously set
     *
     * No-any params required
     */
    abstract fun executeFabAction()

    /**
     * Function to restart the refresher of the screen displayed
     *
     * No-any params required
     */
    fun restartScreenRefreshing() {
        if(::screenViewModel.isInitialized) {
            screenViewModel.setActiveContext(currentScreenContext)
            screenViewModel.restartRefresher()
        }
    }

    /**
     * Function to suspend the refresher of the screen displayed
     *
     * No-any params required
     */
    fun suspendScreenRefreshing() {
        screenViewModel.suspendRefresher()
    }

    /**
     * Function to create a [Card] to display the [RefyItem]'s details
     *
     * @param item: the item to display
     * @param borderColor: the color to apply to one border
     * @param onClick: the action to execute when the card has been clicked
     * @param onDoubleClick: the action to execute when the card has been clicked twice
     * @param onLongClick: the action to execute when the card has been clicked for a long period
     * @param title: the title of the item
     * @param description: the description of the item
     * @param teams: the teams where the item is shared
     * @param optionsBar: the options bar available for the item
     */
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    @NonRestartableComposable
    protected fun ItemCard(
        item: RefyItem,
        borderColor: Color? = null,
        onClick: () -> Unit,
        onDoubleClick: (() -> Unit)? = null,
        onLongClick: () -> Unit,
        title: String,
        description: String?,
        teams: List<Team>,
        optionsBar: @Composable () -> Unit
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick,
                onLongClick = if(isItemOwner(item))
                    onLongClick
                else
                    null
            )
        Card(
            modifier = if(borderColor != null)
                modifier.drawOneSideBorder(
                    width = 10.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp
                    )
                )
            else
                modifier,
            shape = RoundedCornerShape(
                size = 8.dp
            )
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 16.dp,
                            start = if (borderColor == null)
                                16.dp
                            else
                                21.dp,
                            end = 16.dp,
                            bottom = 5.dp
                        )
                ) {
                    Text(
                        text = title,
                        fontFamily = displayFontFamily,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontStyle = AppTypography.titleMedium.fontStyle
                    )
                    ItemDescription(
                        description = description
                    )
                    if(teams.isNotEmpty()) {
                        TeamSections(
                            teams = teams
                        )
                    }
                }
                optionsBar.invoke()
            }
        }
    }

    /**
     * Function to display the section relating the teams where the item is shared
     *
     * @param teams: the teams where the item is shared
     */
    @Composable
    @NonRestartableComposable
    private fun TeamSections(
        teams: List<Team>
    ) {
        val expandTeamMembers = remember { mutableStateOf(false) }
        LazyRow(
            modifier = Modifier
                .padding(
                    top = 5.dp,
                    bottom = 5.dp
                )
        ) {
            item {
                ExpandTeamMembers(
                    viewModel = screenViewModel,
                    show = expandTeamMembers,
                    teams = teams
                )
                PicturesRow(
                    pictures = {
                        val profiles = mutableListOf<String>()
                        teams.forEach { team ->
                            profiles.add(team.logoPic)
                        }
                        profiles
                    },
                    onClick = { expandTeamMembers.value = true }
                )
            }
        }
    }

    /**
     * Function to create a [Card] to display the [RefyItem]'s details
     *
     * @param onClick: the action to execute when the card has been clicked
     * @param pictures: the pictures to display in a row
     * @param pictureSize: the size to apply to the pictures
     */
    @Composable
    @NonRestartableComposable
    protected fun PicturesRow(
        onClick: (() -> Unit)? = null,
        pictures: () -> List<String>,
        pictureSize: Dp = 25.dp
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    enabled = onClick != null,
                    onClick = {
                        onClick?.invoke()
                    }
                )
        ) {
            pictures.invoke().forEachIndexed { index, picture ->
                if(index == MAX_TEAMS_DISPLAYED)
                    return@forEachIndexed
                AsyncImage(
                    modifier = Modifier
                        .padding(
                            start = index * 15.dp
                        )
                        .clip(CircleShape)
                        .size(pictureSize),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(
                            getCompleteMediaItemUrl(
                                relativeMediaUrl = picture
                            )
                        )
                        .crossfade(enable = true)
                        .crossfade(500)
                        //.error() //TODO: TO SET THE ERROR IMAGE CORRECTLY
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }

    /**
     * Function to nav to a dedicated [Activity] related to the item
     *
     * @param itemId: the identifier of the item
     * @param destination: the destination to reach
     */
    protected fun navToDedicatedItemActivity(
        itemId: String,
        destination: Class<*>
    ) {
        val intent = Intent(context, destination)
        intent.putExtra(IDENTIFIER_KEY, itemId)
        context.startActivity(intent)
    }

}