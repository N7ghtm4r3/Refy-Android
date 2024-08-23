package com.tecknobit.refy.utilities

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GroupRemove
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material.RichText
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.getCompleteMediaItemUrl
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.bodyFontFamily
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.viewmodels.teams.TeamActivityViewModel
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.RefyUser
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.RefyTeamMember
import com.tecknobit.refycore.records.Team.RefyTeamMember.TeamRole
import com.tecknobit.refycore.records.Team.RefyTeamMember.TeamRole.ADMIN

/**
 * Function to create an options bar for the cards of the [RefyItem]
 *
 * @param options: the row of the options available
 */
@Composable
@NonRestartableComposable
fun OptionsBar(
    options: @Composable RowScope.() -> Unit
) {
    LineDivider()
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        content = options
    )
}

/**
 * Function to create a custom [HorizontalDivider]
 *
 * No-any params required
 */
@Composable
@NonRestartableComposable
fun LineDivider() {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    HorizontalDivider(
        color = if(isSystemInDarkTheme)
            MaterialTheme.colorScheme.tertiary
        else
            MaterialTheme.colorScheme.outlineVariant,
        thickness = if(isSystemInDarkTheme)
            0.5.dp
        else
            DividerDefaults.Thickness
    )
}

/**
 * Function to display the description of an item
 *
 * @param modifier: the modifier to apply to the section
 * @param description: the description value
 * @param fontSize: the size to apply to the font
 */
@Composable
@NonRestartableComposable
@SuppressLint("ModifierParameter")
fun ItemDescription(
    modifier: Modifier = Modifier
        .heightIn(
            max = 120.dp
        ),
    description: String?,
    fontSize: TextUnit = 16.sp
) {
    description?.let {
        val state = rememberRichTextState()
        state.config.linkColor = MaterialTheme.colorScheme.primary
        state.setMarkdown(description)
        RichText(
            modifier = modifier,
            textAlign = TextAlign.Justify,
            color = LocalContentColor.current,
            fontFamily = bodyFontFamily,
            fontSize = fontSize,
            fontStyle = AppTypography.bodyMedium.fontStyle,
            state = state
        )
    }
}

/**
 * Function to get whether the current [localUser] is the owner of the specified item
 *
 * @param item: the item to check whether the user is its owner
 *
 * @return whether the current [localUser] is the owner of the specified item as boolean
 */
fun <T : RefyItem> isItemOwner(
    item: T
): Boolean {
    return item.owner.id == localUser.userId
}

/**
 * Function to get a list of items that not already belongs to a container such [LinksCollection] or
 * [Team], so exclude them from the owned by the user
 *
 * @param userList: the list of the user owned items
 * @param currentAttachments: the current attachments list of the item
 *
 * @return list of items that not already belongs to a container as [List] of [T]
 *
 * @param T: the [RefyItem] type
 */
fun <T: RefyItem> getItemRelations(
    userList: List<T>,
    currentAttachments: List<T>
): List<T> {
    val attachments = mutableListOf<T>()
    attachments.addAll(userList)
    attachments.removeAll { attachment ->
        currentAttachments.any { currentAttachment ->
            attachment.id == currentAttachment.id
        }
    }
    return attachments
}

/**
 * Function to draw just one side of a component such [Card]
 *
 * @param width: the width of the border to color
 * @param color: the color of the border
 * @param shape: the shape of the border
 */
fun Modifier.drawOneSideBorder(
    width: Dp,
    color: Color,
    shape: Shape = RectangleShape
) = this
    .clip(shape)
    .drawWithContent {
        val widthPx = width.toPx()
        drawContent()
        drawLine(
            color = color,
            start = Offset(widthPx / 2, 0f),
            end = Offset(widthPx / 2, size.height),
            strokeWidth = widthPx
        )
    }

/**
 * Function to execute the action to add links to a teams
 *
 * @param show: whether show the [EquinoxAlertDialog] where is possible chose the links
 * @param viewModel: the view model used to execute this operation
 * @param icon: the representative icon to use
 * @param availableItems: the list of available items identifiers where share with a container
 * @param title: the resource identifier of the title text
 * @param confirmAction: the action to execute when the user clicks the confirm button
 */
@Composable
@NonRestartableComposable
fun AddItemToContainer(
    show: MutableState<Boolean>,
    viewModel: EquinoxViewModel,
    icon: ImageVector,
    availableItems: List<RefyItem>,
    title: Int,
    confirmAction: (List<String>) -> Unit
) {
    if(show.value)
        viewModel.suspendRefresher()
    val resetLayout = {
        show.value = false
        viewModel.restartRefresher()
    }
    val ids = mutableListOf<String>()
    EquinoxAlertDialog(
        show = show,
        onDismissAction = resetLayout,
        icon = icon,
        title = stringResource(title),
        text = {
            LazyColumn (
                modifier = Modifier
                    .heightIn(
                        max = 150.dp
                    )
            ) {
                items(
                    items = availableItems,
                    key = { item -> item.id }
                ) { item ->
                    var selected by remember { mutableStateOf(ids.contains(item.id)) }
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selected,
                            onCheckedChange = {
                                selected = it
                                if(selected)
                                    ids.add(item.id)
                                else
                                    ids.remove(item.id)
                            }
                        )
                        Text(
                            text = item.title
                        )
                    }
                    HorizontalDivider()
                }
            }
        },
        dismissText = stringResource(R.string.dismiss),
        confirmAction = {
            confirmAction.invoke(ids)
            resetLayout.invoke()
        },
        confirmText = stringResource(R.string.add),
    )
}

/**
 * Function to create a button to delete an item
 *
 * @param show: whether show the warn [EquinoxAlertDialog] about the item deletion
 * @param deleteAction: the action to execute to delete the item
 * @param tint: the tint for the [OptionButton]
 */
@Composable
@NonRestartableComposable
fun DeleteItemButton(
    show: MutableState<Boolean>,
    deleteAction: @Composable () -> Unit,
    tint: Color = MaterialTheme.colorScheme.error
) {
    OptionButton(
        icon = Icons.Default.Delete,
        show = show,
        optionAction = deleteAction,
        tint = tint
    )
}

/**
 * Function to create an option button to execute any actions
 *
 * @param icon: the representative icon to use
 * @param visible: whether the button must be visible or not
 * @param show: whether show the warn [EquinoxAlertDialog] about the action
 * @param optionAction: the option action to execute
 * @param tint: the tint for the [OptionButton]
 */
@Composable
@NonRestartableComposable
fun OptionButton(
    icon: ImageVector,
    visible: (() -> Boolean) = { true },
    show: MutableState<Boolean>,
    optionAction: @Composable () -> Unit,
    tint: Color = LocalContentColor.current,
) {
    AnimatedVisibility(
        visible = visible.invoke(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        IconButton(
            onClick = { show.value = true }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint
            )
        }
        optionAction.invoke()
    }
}

/**
 * Function to create a [ModalBottomSheet] to display the teams where an item is shared
 *
 * @param viewModel: the view model used to in the screen where this function has been invoked
 * @param show: whether show the [ModalBottomSheet]
 * @param teams: the teams list
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@NonRestartableComposable
fun ExpandTeamMembers(
    viewModel: EquinoxViewModel,
    show: MutableState<Boolean>,
    teams: List<Team>
) {
    val resetLayout = {
        show.value = false
        viewModel.restartRefresher()
    }
    if(show.value) {
        viewModel.suspendRefresher()
        ModalBottomSheet(
            onDismissRequest = resetLayout
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                teams.forEach { team ->
                    item {
                        Text(
                            modifier = Modifier
                                .padding(
                                    top = 16.dp,
                                    start = 16.dp
                                ),
                            text = team.title,
                            fontFamily = displayFontFamily
                        )
                    }
                    items(
                        items = team.members,
                        key = { member -> member.id + team.id }
                    ) { member ->
                        DefaultPlaque(
                            profilePic = member.profilePic,
                            completeName = member.completeName,
                            tagName = member.completeName,
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

/**
 * Function to create an [Team.RefyTeamMember] details plaque
 *
 * @param team: the team of the member
 * @param member: the member from get the details to show
 * @param viewModel: the view model used to execute this operation
 */
@Composable
@NonRestartableComposable
fun TeamMemberPlaque(
    team: Team,
    member: RefyTeamMember,
    viewModel: TeamActivityViewModel
) {
    val isAuthorizedUser = team.isAdmin(localUser.userId) && member.id != localUser.userId
    val enableOption = isAuthorizedUser && !team.isTheAuthor(member.id)
    DefaultPlaque(
        profilePic = member.profilePic,
        completeName = member.completeName,
        tagName = member.tagName,
        supportingContent = {
            RolesMenu(
                enableOption = enableOption,
                viewModel = viewModel,
                member = member
            )
        },
        trailingContent = if(enableOption) {
            {
                IconButton(
                    onClick = {
                        viewModel.removeMember(
                            member = member
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.GroupRemove,
                        contentDescription = null
                    )
                }
            }
        } else
            null
    )
    HorizontalDivider()
}

/**
 * Function to create an [RefyUser] details plaque
 *
 * @param colors: the colors for the [ListItem] component
 * @param profilePicSize: the size of the profile picture
 * @param user: the user from get the details to show
 * @param supportingContent: the content to place in the supporting zone
 * @param trailingContent: the content to place in the traling zone
 */
@Composable
@NonRestartableComposable
fun UserPlaque(
    colors: ListItemColors = ListItemDefaults.colors(),
    profilePicSize: Dp = 50.dp,
    user: RefyUser,
    supportingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    DefaultPlaque(
        colors = colors,
        profilePicSize = profilePicSize,
        profilePic = user.profilePic,
        completeName = user.completeName,
        tagName = user.completeName,
        supportingContent = supportingContent,
        trailingContent = trailingContent
    )
}

/**
 * Function to create an [RefyUser] details plaque
 *
 * @param colors: the colors for the [ListItem] component
 * @param profilePicSize: the size of the profile picture
 * @param completeName: the complete name of the user
 * @param tagName: the tag name of the user
 * @param supportingContent: the content to place in the supporting zone
 * @param trailingContent: the content to place in the traling zone
 */
@Composable
@NonRestartableComposable
fun DefaultPlaque(
    colors: ListItemColors = ListItemDefaults.colors(),
    profilePicSize: Dp = 50.dp,
    profilePic: String,
    completeName: String,
    tagName: String,
    supportingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    ListItem(
        colors = colors,
        leadingContent = {
            Logo(
                picSize = profilePicSize,
                picUrl = getCompleteMediaItemUrl(
                    relativeMediaUrl = profilePic
                )
            )
        },
        headlineContent = {
            Text(
                text = completeName
            )
        },
        supportingContent = supportingContent,
        overlineContent = {
            Text(
                text = tagName
            )
        },
        trailingContent = trailingContent
    )
}

/**
 * Function to display the roles menu to change the role of a member
 *
 * @param enableOption: whether the option is enabled
 * @param viewModel: the view model used to execute this operation
 * @param member: the member to change his/her role
 */
@Composable
@NonRestartableComposable
private fun RolesMenu(
    enableOption: Boolean,
    viewModel: TeamActivityViewModel,
    member: RefyTeamMember
) {
    val role = (member).role
    val changeRole = remember { mutableStateOf(false) }
    Column {
        Text(
            modifier = Modifier
                .clickable(
                    enabled = enableOption
                ) {
                    changeRole.value = true
                },
            text = role.name,
            color = if(role == ADMIN)
                MaterialTheme.colorScheme.error
            else
                Color.Unspecified
        )
        if(changeRole.value)
            viewModel.suspendRefresher()
        val resetLayout = {
            changeRole.value = false
            viewModel.restartRefresher()
        }
        DropdownMenu(
            expanded = changeRole.value,
            onDismissRequest = resetLayout
        ) {
            TeamRole.entries.forEach { role ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = role.name,
                            color = if(role == ADMIN)
                                MaterialTheme.colorScheme.error
                            else
                                Color.Unspecified
                        )
                    },
                    onClick = {
                        viewModel.changeMemberRole(
                            member = member,
                            role = role,
                            onSuccess = { changeRole.value = false }
                        )
                    }
                )
            }
        }
    }
}

/**
 * Function to create and display a logo
 *
 * @param modifier: the modifier to apply to the [AsyncImage] component
 * @param picSize: the size of the picture
 * @param addShadow: whether add the shadow effect to the logo
 * @param onClick: if set, the action to execute when the logo is clicked
 * @param shape: the shape of the logo
 * @param picUrl: the profile pic url
 */
@Composable
@NonRestartableComposable
fun Logo(
    modifier: Modifier = Modifier,
    picSize: Dp = 50.dp,
    addShadow: Boolean = false,
    onClick: (() -> Unit)? = null,
    shape: Shape = CircleShape,
    picUrl: String
) {
    AsyncImage(
        modifier = modifier
            .clip(shape)
            .size(picSize)
            .clickable(
                enabled = onClick != null,
            ) {
                onClick?.invoke()
            }
            .then(
                if (addShadow) {
                    Modifier.shadow(
                        elevation = 5.dp,
                        shape = shape
                    )
                } else
                    Modifier
            ),
        model = ImageRequest.Builder(LocalContext.current)
            .data(picUrl)
            .crossfade(enable = true)
            .crossfade(500)
            .error(R.drawable.error_logo)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}