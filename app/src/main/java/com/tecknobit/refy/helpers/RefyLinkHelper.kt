package com.tecknobit.refy.helpers

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.SplashScreen.Companion.user
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.bodyFontFamily
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.ui.viewmodel.LinkListViewModel
import com.tecknobit.refycore.records.RefyLink
import com.tecknobit.refycore.records.Team

class RefyLinkHelper(
    val viewModel: LinkListViewModel, // TODO:CHECK IF HIERARCHY WITH VIEWMODEL TO MANAGE THE REFYLINK
) {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun RefyLinkCard(
        link: RefyLink
    ) {
        val context = LocalContext.current
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .combinedClickable(
                    onClick = {
                        openLink(
                            context = context,
                            link = link
                        )
                    },
                    onLongClick = {
                        // TODO: EDIT LINK
                    }
                )
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 5.dp
                        )
                ) {
                    Text(
                        text = link.title,
                        fontFamily = displayFontFamily,
                        fontSize = 25.sp,
                        fontStyle = AppTypography.titleMedium.fontStyle
                    )
                    link.description?.let { description ->
                        Text(
                            modifier = Modifier
                                .heightIn(
                                    max = 75.dp
                                )
                                .verticalScroll(rememberScrollState()),
                            text = description,
                            fontFamily = bodyFontFamily,
                            fontStyle = AppTypography.bodySmall.fontStyle,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
                OptionsBar(
                    context = context,
                    link = link
                )
            }
        }
    }

    @Composable
    private fun OptionsBar(
        context: Context,
        link: RefyLink
    ) {
        val addToGroup = remember { mutableStateOf(false) }
        val addToCollection = remember { mutableStateOf(false) }
        val deleteLink = remember { mutableStateOf(false) }
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                AnimatedVisibility(
                    visible = user.teams.isNotEmpty() || link.teams.isNotEmpty()
                ) {
                    OptionButton(
                        icon = Icons.Default.GroupAdd,
                        show = addToGroup,
                        optionAction = {
                            AddLinkToTeam(
                                show = addToGroup,
                                link = link
                            )
                        }
                    )
                }
                AnimatedVisibility(
                    visible = user.collections.isNotEmpty()
                ) {
                    OptionButton(
                        icon = Icons.Default.AttachFile,
                        show = addToCollection,
                        optionAction = {

                        }
                    )
                }
                IconButton(
                    onClick = {
                        shareLink(
                            context = context,
                            link = link
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                OptionButton(
                    icon = Icons.Default.Delete,
                    show = deleteLink,
                    optionAction = {

                    },
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    @Composable
    private fun OptionButton(
        icon: ImageVector,
        show: MutableState<Boolean>,
        optionAction: @Composable () -> Unit,
        tint: Color = LocalContentColor.current
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

    @Composable
    private fun AddLinkToTeam(
        show: MutableState<Boolean>,
        link: RefyLink
    ) {
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = show
        )
        val teamsId = mutableListOf<String>()
        EquinoxAlertDialog(
            show = show,
            icon = Icons.Default.GroupAdd,
            title = stringResource(R.string.add_link_to_team),
            text = {
                val teams = mutableListOf<Team>()
                teams.addAll(user.teams)
                teams.addAll(link.teams)
                LazyColumn (
                    modifier = Modifier
                        .heightIn(
                            max = 150.dp
                        )
                ) {
                    // TODO: TO FIX 
                    items(
                        items = teams,
                        key = { team -> team.id }
                    ) { team ->
                        var selected = remember {
                            teams.contains(team)
                        }
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selected,
                                onCheckedChange = {
                                    selected = it
                                    if(selected)
                                        teamsId.add(team.id)
                                    else
                                        teamsId.remove(team.id)
                                }
                            )
                            Text(
                                text = team.name
                            )
                        }
                        HorizontalDivider()
                    }
                }
            },
            dismissText = stringResource(R.string.dismiss),
            confirmAction = {
                viewModel.addLinkToTeam(
                    link = link,
                    teams = teamsId,
                    onSuccess = { show.value = false },
                )
            },
            confirmText = stringResource(R.string.add),
        )
    }

    private fun openLink(
        context: Context,
        link: RefyLink
    ) {
        val intent = Intent()
        intent.data = link.referenceLink.toUri()
        intent.action = Intent.ACTION_VIEW
        context.startActivity(intent)
    }

    private fun shareLink(
        context: Context,
        link: RefyLink
    ) {
        val intent = Intent()
        intent.type = "text/plain"
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, "${link.title}\n${link.referenceLink}")
        context.startActivity(Intent.createChooser(intent, null))
    }

}

