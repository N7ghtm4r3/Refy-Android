package com.tecknobit.refy.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinox.environment.records.EquinoxUser.ApplicationTheme
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel

class ProfileActivityViewModel(
    snackbarHostState: SnackbarHostState
): EquinoxViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * **newEmail** -> the value of the new email to set
     */
    lateinit var newEmail: MutableState<String>

    /**
     * **newEmailError** -> whether the [newEmail] field is not valid
     */
    lateinit var newEmailError: MutableState<Boolean>

    /**
     * **newPassword** -> the value of the new password to set
     */
    lateinit var newPassword: MutableState<String>

    /**
     * **newPasswordError** -> whether the [newPassword] field is not valid
     */
    lateinit var newPasswordError: MutableState<Boolean>

    /**
     * Function to execute the profile pic change
     *
     * @param imagePath: the path of the image to set
     * @param profilePic: the state used to display the current profile pic
     */
    fun changeProfilePic(
        imagePath: String,
        profilePic: MutableState<String>
    ) {
        /*requester.sendRequest(
            request = {
                requester.changeProfilePic(
                    profilePic = File(imagePath)
                )
            },
            onSuccess = {
                profilePic.value = imagePath
                localUser.profilePic = it.getString(PROFILE_PIC_KEY)
            },
            onFailure = { showSnack(it) }
        )*/
    }

    /**
     * Function to execute the email change
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun changeEmail(
        onSuccess: () -> Unit
    ) {
        /*if (isEmailValid(newEmail.value)) {
            requester.sendRequest(
                request = {
                    requester.changeEmail(
                        newEmail = newEmail.value
                    )
                },
                onSuccess = {
                    localUser.email = newEmail.value
                    onSuccess.invoke()
                },
                onFailure = { showSnack(it) }
            )
        } else
            newEmailError.value = true*/
        onSuccess.invoke()
    }

    /**
     * Function to execute the password change
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun changePassword(
        onSuccess: () -> Unit
    ) {
        /*if (isPasswordValid(newPassword.value)) {
            requester.sendRequest(
                request = {
                    requester.changePassword(
                        newPassword = newPassword.value
                    )
                },
                onSuccess = { onSuccess.invoke() },
                onFailure = { showSnack(it) }
            )
        } else
            newPasswordError.value = true*/
        onSuccess.invoke()
    }

    /**
     * Function to execute the language change
     *
     * @param newLanguage: the new language of the user
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun changeLanguage(
        newLanguage: String,
        onSuccess: () -> Unit
    ) {
        /*requester.sendRequest(
            request = {
                requester.changeLanguage(
                    newLanguage = newLanguage
                )
            },
            onSuccess = {
                localUser.language = newLanguage
                onSuccess.invoke()
            },
            onFailure = { showSnack(it) }
        )*/
        onSuccess.invoke()
    }

    /**
     * Function to execute the theme change
     *
     * @param newTheme: the new theme of the user
     * @param onChange: the action to execute when the theme changed
     */
    fun changeTheme(
        newTheme: ApplicationTheme,
        onChange: () -> Unit
    ) {
        //localUser.theme = newTheme
        onChange.invoke()
    }

    /**
     * Function to execute the account deletion
     *
     * @param onDelete: the action to execute when the account has been deleted
     */
    fun deleteAccount(
        onDelete: () -> Unit
    ) {
        /*requester.sendRequest(
            request = { requester.deleteAccount() },
            onSuccess = {
                clearSession(
                    onClear = onDelete
                )
            },
            onFailure = { showSnack(it) }
        )*/
    }

    /**
     * Method to clear the current [localUser] session
     *
     * @param onClear: the action to execute when the session has been cleaned
     */
    fun clearSession(
        onClear: () -> Unit
    ) {
        //localUser.clear()
        onClear.invoke()
    }

}