package com.tecknobit.refy.helpers

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.FolderCopy
import androidx.compose.material.icons.filled.Groups
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
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.screens.CollectionListScreen
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.screens.TeamsListScreen
import com.tecknobit.refy.ui.screens.links.CustomLinksScreen
import com.tecknobit.refy.ui.screens.links.LinkListScreen

/**
 * The **NavigationHelper** class is useful to manage the navigation between the [MainActivity] and
 * the [Screen] of items
 * No-any params required
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class NavigationHelper private constructor() {

    companion object {

        /**
         * **navigationTabs** -> list of [Screen] tabs
         */
        private val navigationTabs = listOf(
            NavigationTab(
                icon = Icons.AutoMirrored.Filled.List,
                screen = LinkListScreen(),
                name = R.string.links
            ),
            NavigationTab(
                icon = Icons.Default.FolderCopy,
                screen = CollectionListScreen(),
                name = R.string.collections
            ),
            NavigationTab(
                icon = Icons.Default.Groups,
                screen = TeamsListScreen(),
                name = R.string.teams
            ),
            NavigationTab(
                icon = Icons.Default.DashboardCustomize,
                screen = CustomLinksScreen(),
                name = R.string.custom
            )
        )

        /**
         * **activeTab** -> the current active tab shown
         */
        var activeTab: MutableState<NavigationTab> = mutableStateOf(navigationTabs[0])

        /**
         * Function to reset the [activeTab] to [LinkListScreen]
         *
         * No-any params required
         */
        fun resetFirstTab() {
            activeTab.value = navigationTabs[0]
        }

        /**
         * Function to get the singleton instance of [NavigationHelper]
         *
         * No-params required
         */
        fun getInstance() : NavigationHelper {
            return NavigationHelper()
        }

    }

    /**
     * The **NavigationTab** data class represents the navigation tab used by the [NavigationHelper]
     * to display the [Screen]
     *
     * @param screen: the screen to display
     * @param icon: the representative icon of the screen
     * @param name: the name of the screen
     * @param onFabClick: the action to execute when the FAB button is clicked
     * @param content: the content of the screen to display
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    data class NavigationTab(
        val screen: Screen,
        val icon: ImageVector,
        val name: Int,
        val onFabClick: (Screen) -> Unit = {
            screen.executeFabAction()
        },
        val content: @Composable ColumnScope.(Screen) -> Unit = {
            screen.ShowContent()
        }
    )

    /**
     * Function to create the bottom navigation bar to navigate in the application
     *
     * No-any params required
     */
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
                        activeTab.value.screen.suspendScreenRefreshing()
                        activeTab.value = navTab
                        selectedItem = index
                    }
                )
            }
        }
    }

}