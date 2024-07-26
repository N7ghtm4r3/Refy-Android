@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.create

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.getFilePath
import com.tecknobit.refy.ui.utilities.UserPlaque
import com.tecknobit.refy.ui.viewmodels.create.CreateTeamViewModel
import com.tecknobit.refycore.records.Team

class CreateTeamActivity : CreateActivity<Team, CreateTeamViewModel>(
    items = user.teams,
    invalidMessage = R.string.invalid_team
) {

    private lateinit var photoPicker: ActivityResultLauncher<PickVisualMediaRequest>

    init {
        viewModel = CreateTeamViewModel(
            snackbarHostState = snackbarHostState
        )
    }

    @Composable
    override fun ActivityContent() {
        super.ActivityContent()
        photoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { imageUri ->
                if(imageUri != null) {
                    viewModel.logoPic.value = getFilePath(
                        context = this@CreateTeamActivity,
                        uri = imageUri
                    )!!
                }
            }
        )
        viewModel.logoPic = remember {
            mutableStateOf(
                if(itemExists)
                    item!!.logoPic
                else
                    "" // TODO: SET THE DEFAULT PATH INSTEAD
            )
        }
        ScaffoldContent(
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            placeholder = R.string.team_name,
            extraContent = {
                if(viewModel.logoPic.value.isNotEmpty())
                    LogoSet()
                else
                    LogoNotSet()
            },
            customContent = {
                MembersSection()
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LogoSet() {
        val shape = RoundedCornerShape(
            size = 5.dp
        )
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .clip(
                    shape = shape
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = shape
                )
                .clickable {
                    photoPicker.launch(PickVisualMediaRequest(ImageOnly))
                },
            model = ImageRequest.Builder(LocalContext.current)
                .data(viewModel.logoPic.value)
                .crossfade(enable = true)
                .crossfade(500)
                //.error() //TODO: TO SET THE ERROR IMAGE CORRECTLY
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LogoNotSet() {

        val stroke = Stroke(
            width = 4f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        val color = MaterialTheme.colorScheme.primary
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .height(150.dp)
                .drawBehind {
                    drawRoundRect(
                        color = color,
                        style = stroke,
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )
                }
                .clip(
                    RoundedCornerShape(
                        size = 5.dp
                    )
                )
                .clickable {
                    photoPicker.launch(PickVisualMediaRequest(ImageOnly))
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(100.dp),
                imageVector = Icons.Default.ImageSearch,
                contentDescription = null,
                tint = color
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun MembersSection() {
        val keyboardController = LocalSoftwareKeyboardController.current
        viewModel.fetchCurrentUsers()
        val currentUsers = viewModel.currentUsers.collectAsState().value
        CustomSection(
            header = R.string.members
        ) {
            items(
                items = currentUsers,
                key = { member -> member.id }
            ) { member ->
                val checked = remember { mutableStateOf(viewModel.idsList.contains(member.id)) }
                UserPlaque(
                    user = member,
                    trailingContent = {
                        ItemCheckbox(
                            checked = checked,
                            keyboardController = keyboardController,
                            itemId = member.id
                        )
                    }
                )
                HorizontalDivider()
            }
        }
    }

    override fun canBeSaved(): Boolean {
        return super.canBeSaved() && viewModel.logoPic.value.isNotEmpty()
    }

}
