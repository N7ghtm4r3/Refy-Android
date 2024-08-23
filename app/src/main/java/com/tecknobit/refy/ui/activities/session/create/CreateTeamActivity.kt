@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.create

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.annotation.CallSuper
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
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.session.RefyItemBaseActivity
import com.tecknobit.refy.ui.getCompleteMediaItemUrl
import com.tecknobit.refy.ui.getFilePath
import com.tecknobit.refy.utilities.DefaultPlaque
import com.tecknobit.refy.viewmodels.create.CreateTeamViewModel
import com.tecknobit.refycore.records.RefyUser.DEFAULT_PROFILE_PIC
import com.tecknobit.refycore.records.Team

/**
 * The **CreateTeamActivity** class is useful to create or edit a [Team]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see RefyItemBaseActivity
 * @see CreateActivity
 */
class CreateTeamActivity : CreateActivity<Team, CreateTeamViewModel>(
    items = localUser.getTeams(true),
    invalidMessage = R.string.invalid_team,
    scrollable = true
) {

    /**
     * *photoPicker* -> the launcher to choose the logo picture for the team
     */
    private lateinit var photoPicker: ActivityResultLauncher<PickVisualMediaRequest>

    init {
        viewModel = CreateTeamViewModel(
            snackbarHostState = snackbarHostState
        )
    }

    /**
     * Function to display the content of the activity
     *
     * No-any params required
     */
    @CallSuper
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
                    DEFAULT_PROFILE_PIC
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

    /**
     * Function to display the logo picture of the team
     *
     * No-any params required
     */
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
                .data(
                    if(itemExists && viewModel.logoPic.value.contains(item!!.id)) {
                        getCompleteMediaItemUrl(
                            relativeMediaUrl = viewModel.logoPic.value
                        )
                    } else
                        viewModel.logoPic.value
                )
                .crossfade(enable = true)
                .crossfade(500)
                .error(R.drawable.error_logo)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }

    /**
     * Function to display the section to choose the logo picture for the team
     *
     * No-any params required
     */
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

    /**
     * Function to display the section to choose the members of the team
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun MembersSection() {
        val keyboardController = LocalSoftwareKeyboardController.current
        viewModel.fetchCurrentUsers()
        val currentUsers = viewModel.potentialMembers.collectAsState().value
        CustomSection(
            header = R.string.members
        ) {
            items(
                items = currentUsers,
                key = { member -> member.id }
            ) { member ->
                val checked = remember { mutableStateOf(viewModel.itemDedicatedList.contains(member.id)) }
                DefaultPlaque(
                    profilePic = member.profilePic,
                    completeName = member.completeName,
                    tagName = member.tagName,
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

    /**
     * Function to check whether the current item can be saved because all the details has been
     * correctly filled
     *
     * No-any params required
     * @return whether the item can be saved as boolean
     */
    override fun canBeSaved(): Boolean {
        return super.canBeSaved() && viewModel.logoPic.value.isNotEmpty()
    }

}
