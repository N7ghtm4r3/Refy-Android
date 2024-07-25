package com.tecknobit.refy.ui.viewmodel.collections

import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screen.CollectionListScreen
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.RefyLink
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
                        "gg",
                        "#F6ED0E",
                        "ggagaga"
                    ),
                    LinksCollection(
                        "id1",
                        "gggagag",
                        "#DE646E",
                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                        listOf(
                            Team("id12", "Ciaogwegw2", "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg"),
                            Team("35525", "Ciaogwegw22", "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg"),
                            Team("bs", "breberbebre", "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg"),
                            Team("355bsdb25", "breberbeb", "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg"),
                            Team("355bvbesb25", "Ciabreogbwegw22", "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg")
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