package com.tecknobit.refy.helpers

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.FolderCopy
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.screens.CollectionListScreen
import com.tecknobit.refy.ui.screens.LinkListScreen
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.screens.TeamsListScreen

class NavigationHelper private constructor() {

    companion object {

        private val navigationTabs = listOf(
            NavigationTab(
                icon = Icons.AutoMirrored.Filled.List,
                screen = LinkListScreen(),
                name = R.string.links,
                onFabClick = { screen ->
                    screen.executeFabAction()
                },
                content = { screen ->
                    screen.ShowContent()
                }
            ),
            NavigationTab(
                icon = Icons.Default.FolderCopy,
                screen = CollectionListScreen(),
                name = R.string.collections,
                onFabClick = { screen ->
                    screen.executeFabAction()
                },
                content = { screen ->
                    screen.ShowContent()
                }
            ),
            NavigationTab(
                icon = Icons.Default.Groups,
                screen = TeamsListScreen(),
                name = R.string.teams,
                onFabClick = { screen ->
                    screen.executeFabAction()
                },
                content = { screen ->
                    screen.ShowContent()
                }
            ),
            NavigationTab(
                icon = Icons.Default.DashboardCustomize,
                name = R.string.custom,
                onFabClick = {
                    // TODO: MAKE FAB ACTION
                },
                content = {
                    EmptyListUI(
                        icon = Icons.Default.Warning,
                        subText = stringResource(id = R.string.app_name)
                    )
                },
            )
        )

        var activeTab: MutableState<NavigationTab> = mutableStateOf(navigationTabs[0])

        fun getInstance() : NavigationHelper {
            return NavigationHelper()
        }

    }

    data class NavigationTab(
        val screen: Screen = LinkListScreen(), // TODO: TO REMOVE
        val icon: ImageVector,
        val name: Int,
        val onFabClick: (Screen) -> Unit,
        val content: @Composable ColumnScope.(Screen) -> Unit,
    )

    @Composable
    fun BottomNavigationBar() {
        NavigationBar {
            var selectedItem by remember { mutableIntStateOf(0) }
            navigationTabs.forEachIndexed { index, navTab ->
                val selected = selectedItem == index
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = navTab.icon,
                            contentDescription = null,
                            tint = if(selected)
                                MaterialTheme.colorScheme.primary
                            else
                                LocalContentColor.current
                        )
                    },
                    selected = selected,
                    onClick = {
                        activeTab.value = navTab
                        selectedItem = index
                    }
                )
            }
        }
    }

}