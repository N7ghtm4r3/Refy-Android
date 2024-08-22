package com.tecknobit.refy.ui.screens.links

import android.content.Intent
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.platform.LocalContext
import com.tecknobit.refy.helpers.SessionManager
import com.tecknobit.refy.ui.activities.session.create.CreateCustomLinkActivity
import com.tecknobit.refy.ui.activities.session.singleitem.CustomLinkActivity
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.viewmodels.links.CustomLinksViewModel
import com.tecknobit.refycore.records.links.CustomRefyLink

/**
 * The **CustomLinksScreen** class is useful to display the list of the [localUser]'s [CustomRefyLink]
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see Screen
 * @see LinksScreen
 * @see SessionManager
 */
class CustomLinksScreen : LinksScreen<CustomRefyLink>(
    viewModel = CustomLinksViewModel()
) {

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @Composable
    override fun ShowContent() {
        ManagedContent (
            context = LocalContext.current
        ) {
            val context = this::class.java
            currentScreenContext = context
            viewModel.setActiveContext(context)
            LinksList()
        }
    }

    /**
     * Function to set the action to execute when the [FloatingActionButton] has been clicked
     *
     * No-any params required
     */
    @Composable
    override fun SetFabAction() {
        context = LocalContext.current
    }

    /**
     * Function to execute the fab action previously set
     *
     * No-any params required
     */
    override fun executeFabAction() {
        context.startActivity(Intent(context, CreateCustomLinkActivity::class.java))
    }

    /**
     * Function to create a properly [Card] to display the link
     *
     * @param link: the link to display
     */
    @Composable
    @NonRestartableComposable
    override fun LinkCard(
        link: CustomRefyLink
    ) {
        RefyLinkCard(
            link = link,
            onClick = {
                navToDedicatedItemActivity(
                    itemId = link.id,
                    destination = CustomLinkActivity::class.java
                )
            },
            onLongClick = {
                navToDedicatedItemActivity(
                    itemId = link.id,
                    destination = CreateCustomLinkActivity::class.java
                )
            },
            showCompleteOptionsBar = false
        )
    }

}