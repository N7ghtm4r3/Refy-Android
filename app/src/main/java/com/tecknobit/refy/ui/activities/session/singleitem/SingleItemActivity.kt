@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.singleitem

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.refy.ui.activities.session.RefyItemBaseActivity
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.ui.utilities.ItemDescription
import com.tecknobit.refy.ui.utilities.LineDivider
import com.tecknobit.refy.ui.utilities.OptionsBar
import com.tecknobit.refy.ui.utilities.RefyLinkUtilities
import com.tecknobit.refy.ui.utilities.UserPlaque
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.links.RefyLink

@Structure
abstract class SingleItemActivity <T : RefyItem> (
    items: List<T>,
    invalidMessage: Int
): RefyItemBaseActivity<T> (
    items = items,
    invalidMessage = invalidMessage
), RefyLinkUtilities<RefyLink> {

    protected var iconsColor: Color = Color.Red

    protected var hasTeams: Boolean = true

    protected var activityColorTheme: Color = Color.Red

    @Composable
    protected abstract fun InitViewModel()

    @Composable
    protected fun DisplayItem(
        topBarColor: Color? = MaterialTheme.colorScheme.primaryContainer,
        title: @Composable () -> Unit = {
            Text(
                text = item!!.title
            )
        },
        actions: @Composable RowScope.() -> Unit,
        floatingActionButton: @Composable () -> Unit,
        content: @Composable (PaddingValues) -> Unit
    ) {
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
                            navigationIcon = { NavButton() },
                            title = title,
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = if(topBarColor != null)
                                    topBarColor
                                else
                                    activityColorTheme
                            ),
                            actions = actions
                        )
                    },
                    floatingActionButton = floatingActionButton
                ) { paddingValues ->
                    content.invoke(paddingValues)
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun NavButton() {
        iconsColor = LocalContentColor.current
        IconButton(
            onClick = { finish() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    @NonRestartableComposable
    protected fun RefyLinkContainerCard(
        link: RefyLink,
        hideOptions: Boolean = false,
        removeAction: () -> Unit
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
                    onDoubleClick = {
                        showLinkReference(
                            snackbarHostState = snackbarHostState,
                            link = link
                        )
                    },
                ),
            shape = RoundedCornerShape(
                size = 8.dp
            )
        ) {
            Column {
                if(hasTeams) {
                    TopBarDetails(
                        item = link
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = if (hasTeams)
                                5.dp
                            else
                                16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 5.dp
                        )
                ) {
                    Text(
                        text = link.title,
                        fontFamily = displayFontFamily,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontStyle = AppTypography.titleMedium.fontStyle
                    )
                    ItemDescription(
                        description = link.description
                    )
                }
                OptionsBar(
                    options = {
                        if(!hideOptions) {
                            ShareButton(
                                context = context,
                                link = link
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Row {
                                ViewLinkReferenceButton(
                                    snackbarHostState = snackbarHostState,
                                    link = link
                                )
                                if(!hideOptions) {
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
                        }
                    }
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    protected fun TopBarDetails(
        item: RefyItem,
        overlineColor: Color = activityColorTheme
    ) {
        UserPlaque(
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                overlineColor = overlineColor
            ),
            profilePicSize = 45.dp,
            user = item.owner
        )
        LineDivider()
    }

}