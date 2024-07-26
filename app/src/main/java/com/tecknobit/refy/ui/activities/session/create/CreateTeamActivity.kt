@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.create

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.viewmodels.create.CreateTeamViewModel
import com.tecknobit.refycore.records.Team

class CreateTeamActivity : CreateActivity<Team, CreateTeamViewModel>(
    items = user.teams,
    invalidMessage = R.string.invalid_team
) {

    init {
        viewModel = CreateTeamViewModel(
            snackbarHostState = snackbarHostState
        )
    }

    @Composable
    override fun ActivityContent() {
        super.ActivityContent()
        ScaffoldContent(
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .paint(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(item?.logoPic)
                            .crossfade(enable = true)
                            .crossfade(500)
                            .error(R.drawable.ic_launcher_background) //TODO: TO SET THE ERROR IMAGE CORRECTLY
                            .build()
                    ),
                    contentScale = ContentScale.FillBounds
                )
                .clickable {

                },
            placeholder = R.string.team_name
        ) {

        }
    }

}
