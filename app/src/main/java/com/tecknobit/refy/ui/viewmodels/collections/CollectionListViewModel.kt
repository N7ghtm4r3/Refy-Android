package com.tecknobit.refy.ui.viewmodels.collections

import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screens.CollectionListScreen
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.RefyLink
import com.tecknobit.refycore.records.RefyUser
import com.tecknobit.refycore.records.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class CollectionListViewModel : LinksCollectionViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    private val _collections = MutableStateFlow<List<LinksCollection>>(
        value = emptyList()
    )
    val collections: StateFlow<List<LinksCollection>> = _collections

    fun getCollections() {
        execRefreshingRoutine(
            currentContext = CollectionListScreen::class.java,
            routine = {
                // TODO: MAKE REQUEST THEN
                _collections.value = listOf(
                    LinksCollection(
                        "id",
                        RefyUser(),
                        "gg",
                        "#F6ED0E",
                        "ggagaga"
                    ),
                    LinksCollection(
                        "id1",
                        RefyUser(),
                        "gggagag",
                        "#DE646E",
                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                        listOf(
                            Team("id12", "Ciaogwegw2", RefyUser(),
                                "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                            ),
                            Team("35525", "Ciaogwegw22", RefyUser(),
                                "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                            ),
                            Team("bs", "breberbebre", RefyUser(),
                                "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                            ),
                            Team("355bsdb25", "breberbeb", RefyUser(),
                                "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                            ),
                            Team("355bvbesb25", "Ciabreogbwegw22", RefyUser(),
                                "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                            )
                        ),
                        listOf(
                            RefyLink(
                                "id",
                                "tille",
                                null,//"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                                "https://github.com/N7ghtm4r3"
                            ),
                            RefyLink(
                                "idf",
                                "PRova",
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                                "https://github.com/N7ghtm4r3"
                            )
                        )
                    )
                )
                user.collections = _collections.value
            },
            repeatRoutine = false // TODO: TO REMOVE
        )
    }

}