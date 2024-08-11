package com.tecknobit.refy.ui.viewmodels

import android.content.Context
import android.content.Intent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.equinox.environment.records.EquinoxItem.IDENTIFIER_KEY
import com.tecknobit.equinox.environment.records.EquinoxUser
import com.tecknobit.equinox.environment.records.EquinoxUser.LANGUAGE_KEY
import com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY
import com.tecknobit.equinox.environment.records.EquinoxUser.SURNAME_KEY
import com.tecknobit.equinox.environment.records.EquinoxUser.TOKEN_KEY
import com.tecknobit.equinox.inputs.InputValidator.DEFAULT_LANGUAGE
import com.tecknobit.equinox.inputs.InputValidator.LANGUAGES_SUPPORTED
import com.tecknobit.equinox.inputs.InputValidator.isEmailValid
import com.tecknobit.equinox.inputs.InputValidator.isHostValid
import com.tecknobit.equinox.inputs.InputValidator.isNameValid
import com.tecknobit.equinox.inputs.InputValidator.isPasswordValid
import com.tecknobit.equinox.inputs.InputValidator.isServerSecretValid
import com.tecknobit.equinox.inputs.InputValidator.isSurnameValid
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.activities.session.MainActivity
import com.tecknobit.refycore.helpers.RefyInputValidator.isTagNameValid
import com.tecknobit.refycore.records.RefyUser.TAG_NAME_KEY

class ConnectViewModel (
    val context: Context,
    snackbarHostState: SnackbarHostState
) : EquinoxViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * **isSignUp** -> whether the auth request to execute is sign up or sign in
     */
    lateinit var isSignUp: MutableState<Boolean>

    /**
     * **host** -> the value of the host to reach
     */
    lateinit var host: MutableState<String>

    /**
     * **hostError** -> whether the [host] field is not valid
     */
    lateinit var hostError: MutableState<Boolean>

    /**
     * **serverSecret** -> the value of the server secret
     */
    lateinit var serverSecret: MutableState<String>

    /**
     * **serverSecretError** -> whether the [serverSecret] field is not valid
     */
    lateinit var serverSecretError: MutableState<Boolean>

    /**
     * **tagName** -> the tag name of the user
     */
    lateinit var tagName: MutableState<String>

    /**
     * **nameError** -> whether the [tagName] field is not valid
     */
    lateinit var tagNameError: MutableState<Boolean>

    /**
     * **name** -> the name of the user
     */
    lateinit var name: MutableState<String>

    /**
     * **nameError** -> whether the [name] field is not valid
     */
    lateinit var nameError: MutableState<Boolean>

    /**
     * **surname** -> the surname of the user
     */
    lateinit var surname: MutableState<String>

    /**
     * **surnameError** -> whether the [surname] field is not valid
     */
    lateinit var surnameError: MutableState<Boolean>

    /**
     * **email** -> the email of the user
     */
    lateinit var email: MutableState<String>

    /**
     * **emailError** -> whether the [email] field is not valid
     */
    lateinit var emailError: MutableState<Boolean>

    /**
     * **password** -> the password of the user
     */
    lateinit var password: MutableState<String>

    /**
     * **passwordError** -> whether the [password] field is not valid
     */
    lateinit var passwordError: MutableState<Boolean>

    /**
     * Wrapper function to execute the specific authentication request
     *
     * No-any params required
     */
    fun auth() {
        if (isSignUp.value)
            signUp()
        else
            signIn()
    }

    /**
     * Function to execute the sign-up authentication request, if successful the [localUser] will
     * be initialized with the data received by the request
     *
     * No-any params required
     */
    private fun signUp() {
        if (signUpFormIsValid()) {
            val currentLanguageTag = EquinoxUser.getValidUserLanguage()
            var language = LANGUAGES_SUPPORTED[currentLanguageTag]
            language = if (language == null)
                DEFAULT_LANGUAGE
            else
                currentLanguageTag
            requester.changeHost(host.value)
            requester.sendRequest(
                request = {
                    requester.signUp(
                        serverSecret = serverSecret.value,
                        name = name.value,
                        surname = surname.value,
                        email = email.value,
                        password = password.value,
                        language = language!!,
                        tagName = tagName.value
                    )
                },
                onSuccess = { response ->
                    launchApp(
                        tagName = tagName.value,
                        name = name.value,
                        surname = surname.value,
                        language = language!!,
                        response = response
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Function to validate the inputs for the [signUp] request
     *
     * No-any params required
     *
     * @return whether the inputs are valid as [Boolean]
     */
    private fun signUpFormIsValid(): Boolean {
        var isValid: Boolean = isHostValid(host.value)
        if (!isValid) {
            hostError.value = true
            return false
        }
        isValid = isServerSecretValid(serverSecret.value)
        if (!isValid) {
            serverSecretError.value = true
            return false
        }
        isValid = isTagNameValid(tagName.value)
        if (!isValid) {
            tagNameError.value = true
            return false
        }
        isValid = isNameValid(name.value)
        if (!isValid) {
            nameError.value = true
            return false
        }
        isValid = isSurnameValid(surname.value)
        if (!isValid) {
            surnameError.value = true
            return false
        }
        isValid = isEmailValid(email.value)
        if (!isValid) {
            emailError.value = true
            return false
        }
        isValid = isPasswordValid(password.value)
        if (!isValid) {
            passwordError.value = true
            return false
        }
        return true
    }

    /**
     * Function to execute the sign in authentication request, if successful the [localUser] will
     * be initialized with the data received by the request
     *
     * No-any params required
     */
    private fun signIn() {
        if (signInFormIsValid()) {
            requester.changeHost(host.value)
            requester.sendRequest(
                request = {
                    requester.signIn(
                        email = email.value,
                        password = password.value
                    )
                },
                onSuccess = { response ->
                    launchApp(
                        tagName = response.getString(TAG_NAME_KEY),
                        name = response.getString(NAME_KEY),
                        surname = response.getString(SURNAME_KEY),
                        language = response.getString(LANGUAGE_KEY),
                        response = response
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Function to validate the inputs for the [signIn] request
     *
     * No-any params required
     *
     * @return whether the inputs are valid as [Boolean]
     */
    private fun signInFormIsValid(): Boolean {
        var isValid: Boolean = isHostValid(host.value)
        if (!isValid) {
            hostError.value = true
            return false
        }
        isValid = isEmailValid(email.value)
        if (!isValid) {
            emailError.value = true
            return false
        }
        isValid = isPasswordValid(password.value)
        if (!isValid) {
            passwordError.value = true
            return false
        }
        return true
    }

    /**
     * Function to launch the application after the authentication request
     *
     * @param response: the response of the authentication request
     * @param name: the name of the user
     * @param surname: the surname of the user
     * @param language: the language of the user
     */
    private fun launchApp(
        response: JsonHelper,
        tagName: String,
        name: String,
        surname: String,
        language: String
    ) {
        requester.setUserCredentials(
            userId = response.getString(IDENTIFIER_KEY),
            userToken = response.getString(TOKEN_KEY)
        )
        localUser.insertNewUser(
            host.value,
            name,
            surname,
            email.value,
            password.value,
            language,
            response,
            tagName
        )
        context.startActivity(Intent(context, MainActivity::class.java))
    }

}