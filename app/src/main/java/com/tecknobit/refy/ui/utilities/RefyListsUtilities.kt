package com.tecknobit.refy.ui.utilities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refycore.records.RefyUser
import com.tecknobit.refycore.records.Team

@Composable
@NonRestartableComposable
fun OptionsBar(
    options: @Composable RowScope.() -> Unit
) {
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
                            text = team.name,
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
fun UserPlaque(
    user: RefyUser
) {
    ListItem(
        leadingContent = {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.profilePic)
                    .crossfade(enable = true)
                    .crossfade(500)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        },
        headlineContent = {
            Text(
                text = user.completeName
            )
        },
        overlineContent = {
            Text(
                text = user.tagName
            )
        }
    )
}