package com.tecknobit.refy.ui.utilities

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.bodyFontFamily
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.ui.viewmodels.teams.TeamActivityViewModel
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.RefyUser
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.RefyTeamMember
import com.tecknobit.refycore.records.Team.RefyTeamMember.TeamRole
import com.tecknobit.refycore.records.Team.RefyTeamMember.TeamRole.ADMIN

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

fun <T: RefyItem> getItemRelations(
    userList: List<T>,
    linkList: List<T>
): List<T> {
    val containers = mutableListOf<T>()
    containers.addAll(userList)
    containers.removeAll(linkList)
    return containers
}

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
    viewModel.SuspendUntilElementOnScreen(
        elementVisible = show
    )
    val ids = mutableListOf<String>()
    EquinoxAlertDialog(
        show = show,
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
        confirmAction = { confirmAction.invoke(ids) },
        confirmText = stringResource(R.string.add),
    )
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@NonRestartableComposable
fun ExpandTeamMembers(
    viewModel: EquinoxViewModel,
    show: MutableState<Boolean>,
    teams: List<Team>
) {
    viewModel.SuspendUntilElementOnScreen(
        elementVisible = show
    )
    if(show.value) {
        ModalBottomSheet(
            onDismissRequest = { show.value = false }
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
                        UserPlaque(
                            user = member
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
@NonRestartableComposable
fun TeamMemberPlaque(
    team: Team,
    member: RefyTeamMember,
    viewModel: TeamActivityViewModel
) {
    val isMemberAdmin = team.isAdmin(member)
    val currentUserIsAdmin = team.isAdmin(user)
    val isAuthorizedUser = team.isMaintainer(user) && member.id != user.id
    val enableOption = (((isMemberAdmin && currentUserIsAdmin) || (isAuthorizedUser && !isMemberAdmin))
            && !team.isTheAuthor(member))
    UserPlaque(
        user = member,
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

@Composable
@NonRestartableComposable
fun UserPlaque(
    colors: ListItemColors = ListItemDefaults.colors(),
    profilePicSize: Dp = 50.dp,
    user: RefyUser,
    supportingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    ListItem(
        colors = colors,
        leadingContent = {
            Logo(
                picSize = profilePicSize,
                picUrl = user.profilePic
            )
        },
        headlineContent = {
            Text(
                text = user.completeName
            )
        },
        supportingContent = supportingContent,
        overlineContent = {
            Text(
                text = user.tagName
            )
        },
        trailingContent = trailingContent
    )
}

@Composable
@NonRestartableComposable
private fun RolesMenu(
    enableOption: Boolean,
    viewModel: TeamActivityViewModel,
    member: RefyUser
) {
    val role = ((member) as RefyTeamMember).role
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
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = changeRole
        )
        DropdownMenu(
            expanded = changeRole.value,
            onDismissRequest = { changeRole.value = false }
        ) {
            TeamRole.entries.forEach { role ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = role.name
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

@Composable
@NonRestartableComposable
fun Logo(
    modifier: Modifier = Modifier,
    picSize: Dp = 50.dp,
    addShadow: Boolean = false,
    shape: Shape = CircleShape,
    picUrl: String
) {
    AsyncImage(
        modifier = modifier
            .clip(shape)
            .size(picSize)
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
            //.error() //TODO: TO SET THE ERROR IMAGE CORRECTLY
            .build(),
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )
}