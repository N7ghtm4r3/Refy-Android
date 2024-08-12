package com.tecknobit.refy.ui.screens.links

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.platform.LocalContext
import com.tecknobit.refy.ui.activities.session.create.CreateCustomLinkActivity
import com.tecknobit.refy.ui.activities.session.singleitem.CustomLinkActivity
import com.tecknobit.refy.ui.viewmodels.links.CustomLinksViewModel
import com.tecknobit.refycore.records.links.CustomRefyLink

class CustomLinksScreen : LinksScreen<CustomRefyLink>(
    viewModel = CustomLinksViewModel()
) {

    @Composable
    override fun ShowContent() {
        viewModel.setActiveContext(this::class.java)
        LinksList()
    }

    @Composable
    override fun SetFabAction() {
        context = LocalContext.current
    }

    override fun executeFabAction() {
        context.startActivity(Intent(context, CreateCustomLinkActivity::class.java))
    }

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
            showCompleteOptionsBar = false
        )
    }

    @Composable
    override fun EditLink(
        editLink: MutableState<Boolean>,
        link: CustomRefyLink
    ) {
        navToDedicatedItemActivity(
            itemId = link.id,
            destination = CreateCustomLinkActivity::class.java
        )
    }

}