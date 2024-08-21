package com.tecknobit.refy.ui.activities.session

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinox.environment.records.EquinoxUser.ApplicationTheme
import com.tecknobit.equinox.environment.records.EquinoxUser.ApplicationTheme.Dark
import com.tecknobit.equinox.environment.records.EquinoxUser.ApplicationTheme.Light
import com.tecknobit.equinox.inputs.InputValidator.LANGUAGES_SUPPORTED
import com.tecknobit.equinox.inputs.InputValidator.isEmailValid
import com.tecknobit.equinox.inputs.InputValidator.isPasswordValid
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.refy.R
import com.tecknobit.refy.helpers.NavigationHelper.Companion.resetFirstTab
import com.tecknobit.refy.ui.activities.navigation.SplashScreen
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.getFilePath
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.utilities.Logo
import com.tecknobit.refy.ui.viewmodels.ProfileActivityViewModel

class ProfileActivity : ComponentActivity() {

    private val snackbarHostState = SnackbarHostState()

    private val viewModel = ProfileActivityViewModel(
        snackbarHostState = snackbarHostState
    )

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setActiveContext(this::class.java)
        setContent {
            RefyTheme(
                colorStatusBar = true
            ) {
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) {
                    Column {
                        CustomTopBar()
                        Column (
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                        ) {
                            EmailSection()
                            PasswordSection()
                            LanguageSection()
                            ThemeSection()
                            LogoutSection()
                            DeleteSection()
                        }
                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun CustomTopBar() {
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(225.dp),
        ) {
            val profilePic = remember { mutableStateOf(localUser.profilePic) }
            val photoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { imageUri ->
                    if(imageUri != null) {
                        val imagePath = getFilePath(
                            context = this@ProfileActivity,
                            uri = imageUri
                        )
                        viewModel.changeProfilePic(
                            imagePath = imagePath!!,
                            profilePic = profilePic
                        )
                    }
                }
            )
            Logo(
                modifier = Modifier
                    .fillMaxSize(),
                picUrl = profilePic.value,
                shape = RectangleShape,
                onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ImageOnly)) }
            )
            IconButton(
                onClick = { finish() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
            Column (
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        bottom = 10.dp
                    )
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = localUser.tagName,
                    fontSize = 14.sp
                )
                Text(
                    text = localUser.completeName,
                    fontSize = 20.sp
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun EmailSection() {
        val showChangeEmailAlert = remember { mutableStateOf(false) }
        var userEmail by remember { mutableStateOf(localUser.email) }
        viewModel.newEmail = remember { mutableStateOf("") }
        viewModel.newEmailError = remember { mutableStateOf(false) }
        val resetEmailLayout = {
            viewModel.newEmail.value = ""
            viewModel.newEmailError.value = false
            showChangeEmailAlert.value = false
        }
        UserInfo(
            header = R.string.email,
            info = userEmail,
            onClick = { showChangeEmailAlert.value = true }
        )
        EquinoxAlertDialog(
            onDismissAction = resetEmailLayout,
            icon = Icons.Default.Email,
            show = showChangeEmailAlert,
            title = getString(R.string.change_email),
            text = {
                EquinoxOutlinedTextField(
                    value = viewModel.newEmail,
                    label = getString(R.string.new_email),
                    mustBeInLowerCase = true,
                    errorText = getString(R.string.email_not_valid),
                    isError = viewModel.newEmailError,
                    validator = { isEmailValid(it) }
                )
            },
            confirmAction = {
                viewModel.changeEmail(
                    onSuccess = {
                        userEmail = viewModel.newEmail.value
                        resetEmailLayout.invoke()
                    }
                )
            },
            confirmText = stringResource(id = R.string.confirm),
            dismissText = stringResource(id = R.string.dismiss)
        )
    }

    @Composable
    @NonRestartableComposable
    private fun PasswordSection() {
        val showChangePasswordAlert = remember { mutableStateOf(false) }
        viewModel.newPassword = remember { mutableStateOf("") }
        viewModel.newPasswordError = remember { mutableStateOf(false) }
        val resetPasswordLayout = {
            viewModel.newPassword.value = ""
            viewModel.newPasswordError.value = false
            showChangePasswordAlert.value = false
        }
        var hiddenPassword by remember { mutableStateOf(true) }
        UserInfo(
            header = R.string.password,
            info = "****",
            onClick = { showChangePasswordAlert.value = true }
        )
        EquinoxAlertDialog(
            onDismissAction = resetPasswordLayout,
            icon = Icons.Default.Password,
            show = showChangePasswordAlert,
            title = stringResource(R.string.change_password),
            text = {
                EquinoxOutlinedTextField(
                    value = viewModel.newPassword,
                    label = stringResource(id = R.string.new_password),
                    trailingIcon = {
                        IconButton(
                            onClick = { hiddenPassword = !hiddenPassword }
                        ) {
                            Icon(
                                imageVector = if(hiddenPassword)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if(hiddenPassword)
                        PasswordVisualTransformation()
                    else
                        VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    errorText = stringResource(id = R.string.password_not_valid),
                    isError = viewModel.newPasswordError,
                    validator = { isPasswordValid(it) }
                )
            },
            confirmAction = {
                viewModel.changePassword(
                    onSuccess = resetPasswordLayout
                )
            },
            confirmText = stringResource(id = R.string.confirm),
            dismissText = stringResource(id = R.string.dismiss)
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LanguageSection() {
        val changeLanguage = remember { mutableStateOf(false) }
        UserInfo(
            header = R.string.language,
            info = LANGUAGES_SUPPORTED[localUser.language]!!,
            onClick = { changeLanguage.value = true }
        )
        ChangeLanguage(
            changeLanguage = changeLanguage
        )
    }

    @Composable
    @NonRestartableComposable
    private fun ThemeSection() {
        val changeTheme = remember { mutableStateOf(false) }
        UserInfo(
            header = R.string.theme,
            info = localUser.theme.name,
            buttonText = R.string.change,
            onClick = { changeTheme.value = true }
        )
        ChangeTheme(
            changeTheme = changeTheme
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LogoutSection() {
        val showLogoutAlert = remember { mutableStateOf(false) }
        UserInfo(
            header = R.string.disconnect,
            info = stringResource(id = R.string.logout),
            buttonText = R.string.execute,
            onClick = { showLogoutAlert.value = true }
        )
        EquinoxAlertDialog(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            show = showLogoutAlert,
            title = stringResource(id = R.string.logout),
            text = stringResource(id = R.string.logout_message),
            confirmAction = {
                viewModel.clearSession {
                    navToSplash()
                }
            },
            confirmText = stringResource(id = R.string.confirm),
            dismissText = stringResource(id = R.string.dismiss),
        )
    }

    @Composable
    @NonRestartableComposable
    private fun DeleteSection() {
        val showDeleteAlert = remember { mutableStateOf(false) }
        UserInfo(
            header = R.string.account_deletion,
            info = stringResource(id = R.string.delete),
            buttonText = R.string.execute,
            buttonColors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            onClick = { showDeleteAlert.value = true }
        )
        EquinoxAlertDialog(
            icon = Icons.Default.Cancel,
            show = showDeleteAlert,
            title = stringResource(id = R.string.delete),
            text = stringResource(id = R.string.delete_message),
            confirmAction = {
                viewModel.deleteAccount {
                    navToSplash()
                }
            },
            confirmText = stringResource(id = R.string.confirm),
            dismissText = stringResource(id = R.string.dismiss),
        )
    }

    /**
     * Function to display a specific info details of the user
     *
     * @param header: the header of the info to display
     * @param info: the info details value to display
     * @param buttonText: the text of the setting button
     * @param onClick: the action to execute when the [buttonText] has been clicked
     */
    @Composable
    private fun UserInfo(
        header: Int,
        info: String,
        buttonText: Int = R.string.edit,
        buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
        onClick: () -> Unit
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    all = 12.dp
                )
        ) {
            Text(
                text = stringResource(header),
                fontSize = 18.sp
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 5.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = info,
                    fontSize = 20.sp,
                    fontFamily = displayFontFamily
                )
                Button(
                    modifier = Modifier
                        .height(25.dp),
                    colors = buttonColors,
                    onClick = onClick,
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(
                        start = 10.dp,
                        end = 10.dp,
                        top = 0.dp,
                        bottom = 0.dp
                    ),
                    elevation = ButtonDefaults.buttonElevation(2.dp)
                ) {
                    Text(
                        text = stringResource(buttonText),
                        fontSize = 12.sp
                    )
                }
            }
        }
        HorizontalDivider()
    }

    /**
     * Function to allow the user to change the current language setting
     *
     * @param changeLanguage: the state whether display this section
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ChangeLanguage(
        changeLanguage: MutableState<Boolean>
    ) {
        ChangeInfo(
            showModal = changeLanguage
        ) {
            LANGUAGES_SUPPORTED.keys.forEach { language ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.changeLanguage(
                                newLanguage = language,
                                onSuccess = {
                                    changeLanguage.value = false
                                    navToSplash()
                                }
                            )
                        }
                        .padding(
                            all = 16.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = if (localUser.language == language)
                            MaterialTheme.colorScheme.primary
                        else
                            LocalContentColor.current
                    )
                    Text(
                        text = LANGUAGES_SUPPORTED[language]!!,
                        fontFamily = displayFontFamily
                    )
                }
                HorizontalDivider()
            }
        }
    }

    /**
     * Function to allow the user to change the current theme setting
     *
     * @param changeTheme: the state whether display this section
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ChangeTheme(
        changeTheme: MutableState<Boolean>
    ) {
        ChangeInfo(
            showModal = changeTheme
        ) {
            ApplicationTheme.entries.forEach { theme ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.changeTheme(
                                newTheme = theme,
                                onChange = {
                                    changeTheme.value = false
                                    navToSplash()
                                }
                            )
                        }
                        .padding(
                            all = 16.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = when(theme) {
                            Light -> Icons.Default.LightMode
                            Dark -> Icons.Default.DarkMode
                            else -> Icons.Default.AutoMode
                        },
                        contentDescription = null,
                        tint = if (localUser.theme == theme)
                            MaterialTheme.colorScheme.primary
                        else
                            LocalContentColor.current
                    )
                    Text(
                        text = theme.toString(),
                        fontFamily = displayFontFamily
                    )
                }
                HorizontalDivider()
            }
        }
    }

    /**
     * Function to allow the user to change a current setting
     *
     * @param showModal: the state whether display the [ModalBottomSheet]
     * @param sheetState: the state to apply to the [ModalBottomSheet]
     * @param onDismissRequest: the action to execute when the the [ModalBottomSheet] has been dismissed
     * @param content: the content to display
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ChangeInfo(
        showModal: MutableState<Boolean>,
        sheetState: SheetState = rememberModalBottomSheetState(),
        onDismissRequest: () -> Unit = { showModal.value = false },
        content: @Composable ColumnScope.() -> Unit
    ) {
        if(showModal.value) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = onDismissRequest
            ) {
                Column (
                    content = content
                )
            }
        }
    }

    /**
     * Function to execute the back navigation from the [Splashscreen] activity after user changed any
     * setting which required the refresh of the [localUser]
     *
     * No-any params required
     */
    private fun navToSplash() {
        resetFirstTab()
        startActivity(Intent(this@ProfileActivity, SplashScreen::class.java))
    }
    
}
