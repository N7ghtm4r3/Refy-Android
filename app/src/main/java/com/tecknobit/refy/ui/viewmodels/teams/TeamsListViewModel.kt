package com.tecknobit.refy.ui.viewmodels.teams

import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screens.TeamsListScreen
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.RefyLink
import com.tecknobit.refycore.records.RefyUser
import com.tecknobit.refycore.records.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TeamsListViewModel: TeamViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    private val _teams = MutableStateFlow<List<Team>>(
        value = emptyList()
    )
    val teams: StateFlow<List<Team>> = _teams

    fun getTeams() {
        execRefreshingRoutine(
            currentContext = TeamsListScreen::class.java,
            routine = {
                // TODO: MAKE REQUEST THEN
                _teams.value = listOf(
                    Team("id12", "Ciaogwegw2", RefyUser("h"),
                        "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                        listOf(),
                        listOf(
                            RefyLink(
                                "id",
                                "tille",
                                null,//"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                                "https://github.com/N7ghtm4r3"
                            ),
                            RefyLink(
                                "id1",
                                "tille",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                "https://github.com/N7ghtm4r3"
                            ),
                            RefyLink(
                                "idq1",
                                "tille",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                "https://github.com/N7ghtm4r3"
                            )
                        ),
                        listOf<LinksCollection>(
                            LinksCollection(
                                "id1",
                                RefyUser(
                                    "GEGWEGWHWHG",
                                    "Greg",
                                    "Godzilla",
                                    "greg@godzilla",
                                    "https://media-assets.wired.it/photos/64f6faa946c2835bd21c9fd3/4:3/w_2880,h_2160,c_limit/ezgif-3-f91e25fbf3.jpg",
                                    "@godzilla"
                                ),
                                "gggagag",
                                "#DE646E",
                                "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                listOf(
                                    Team(
                                        "id12", "Ciaogwegw2", RefyUser(),
                                        "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    ),
                                    Team(
                                        "35525", "Ciaogwegw22", RefyUser(),
                                        "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    ),
                                    Team(
                                        "bs", "breberbebre", RefyUser(),
                                        "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    ),
                                    Team(
                                        "355bsdb25", "breberbeb", RefyUser(),
                                        "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    ),
                                    Team(
                                        "355bvbesb25", "Ciabreogbwegw22", RefyUser(),
                                        "https://cdn.mos.cms.futurecdn.net/9UmWCbyxpKaEGXjwFG7dXo-1200-80.jpg",
                                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                                    )
                                )
                            )
                        )
                    ),
                    Team(
                        "35525",
                        "Ciaogwegw22",
                        RefyUser("h"),
                        "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg",
                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper.vavavav avavavava",
                    )
                )
                user.teams = _teams.value
            },
            repeatRoutine = false // TODO: TO REMOVE
        )
    }

    fun createJoinLink(
        onSuccess: (RefyLink) -> Unit,
    ) {
        // TODO: TO MAKE REQUEST THEN (get from the response)
        val refyLink = RefyLink()
        onSuccess.invoke(refyLink)
    }

}