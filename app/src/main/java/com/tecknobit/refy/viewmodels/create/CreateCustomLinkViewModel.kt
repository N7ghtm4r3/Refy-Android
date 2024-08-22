package com.tecknobit.refy.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refycore.records.links.CustomRefyLink
import com.tecknobit.refycore.records.links.CustomRefyLink.EXPIRED_TIME_KEY
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime.NO_EXPIRATION
import com.tecknobit.refycore.records.links.CustomRefyLink.UNIQUE_ACCESS_KEY

/**
 * The **CreateCustomLinkViewModel** class is the support class used by [CreateCustomLinkActivity]
 * to communicate with the backend for the creation or the editing of a [CustomRefyLink]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see CreateItemViewModel
 */
class CreateCustomLinkViewModel(
    snackbarHostState: SnackbarHostState
): CreateItemViewModel<CustomRefyLink>(
    snackbarHostState = snackbarHostState
) {

    /**
     * **newResource** -> an empty resource to add when the user request to add a new one
     */
    private val newResource = Pair("", "")

    /**
     * **expiredTime** -> the expiration time chosen for the custom link
     */
    lateinit var expiredTime: MutableState<ExpiredTime>

    /**
     * **resources** -> the resources attached to the custom link
     */
    private var resources: MutableMap<String, String> = mutableMapOf()

    /**
     * **resourcesSupportList** -> the support list for the [resources] used to manage the UI's workflow
     */
    lateinit var resourcesSupportList: SnapshotStateList<Pair<String, String>>

    /**
     * **fields** -> the fields attached to the custom link, used to protect the [resources] by a
     * validation form
     */
    private var fields: Map<String, String> = mutableMapOf()

    /**
     * **fieldsSupportList** -> the support list for the [fields] used to manage the UI's workflow
     */
    lateinit var fieldsSupportList: SnapshotStateList<Pair<String, String>>

    /**
     * Function to initializing the [existingItem] if exists, null otherwise
     *
     * @param item: the item value with initializing the [existingItem] if exists, null otherwise
     */
    override fun initExistingItem(
        item: CustomRefyLink?
    ) {
        resourcesSupportList = mutableStateListOf()
        fieldsSupportList = mutableStateListOf()
        if(item != null) {
            existingItem = item
            if(existingItem!!.hasUniqueAccess())
                itemDedicatedList.add(UNIQUE_ACCESS_KEY)
            if(existingItem!!.expiredTime != NO_EXPIRATION)
                itemDedicatedList.add(EXPIRED_TIME_KEY)
            resources = existingItem!!.resources
            fields = existingItem!!.fields
        } else
            resources[""] = ""
        loadSupportList(
            map = resources,
            supportList = resourcesSupportList
        )
        loadSupportList(
            map = fields,
            supportList = fieldsSupportList
        )
    }

    /**
     * Function to load a support list to manage the UI's workflow
     *
     * @param map: the map from load the support list
     * @param supportList: the support list to load
     */
    private fun loadSupportList(
        map: Map<String, String>,
        supportList: SnapshotStateList<Pair<String, String>>
    ) {
        map.entries.forEach { value ->
            supportList.add(
                Pair(
                    value.key,
                    value.value
                )
            )
        }
    }

    /**
     * Function to add a new resource to a support list
     *
     * @param supportList: the support list where add the [newResource]
     */
    fun addNewItem(
        supportList: SnapshotStateList<Pair<String, String>>
    ) {
        supportList.add(newResource)
    }

    /**
     * Function to add an item to the support list, this process to keep refreshed the item
     * (so when the user typing the characters sequence of the key for example), remove the current
     * one and replace at the same index the new item
     *
     *
     * @param supportList: the support list where add the item
     * @param index: the index where first remove then insert the item
     * @param key: the key of the item
     * @param value: the value of the item
     */
    fun addItem(
        supportList: SnapshotStateList<Pair<String, String>>,
        index: Int,
        key: String,
        value: String
    ) {
        removeItem(
            supportList = supportList,
            index = index
        )
        supportList.add(
            index = index,
            element = Pair(
                key,
                value
            )
        )
    }

    /**
     * Function to remove an item from a support list
     *
     * @param supportList: the support list from remove the item
     * @param index: the index of the item to remove
     */
    fun removeItem(
        supportList: SnapshotStateList<Pair<String, String>>,
        index: Int
    ) {
        supportList.removeAt(index)
    }

    /**
     * Function to execute the request to create a new item
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    override fun createItem(
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.createCustomLink(
                    title = itemName.value,
                    description = itemDescription.value,
                    resources = resourcesSupportList.toMap(),
                    fields = fieldsSupportList.toMap(),
                    hasUniqueAccess = hasUniqueAccess(),
                    expiredTime = getExpiredTime()
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to execute the request to edit an existing item
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    override fun editItem(
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.editCustomLink(
                    linkId = existingItem!!.id,
                    title = itemName.value,
                    description = itemDescription.value,
                    resources = resourcesSupportList.toMap(),
                    fields = fieldsSupportList.toMap(),
                    hasUniqueAccess = hasUniqueAccess(),
                    expiredTime = getExpiredTime()
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to get whether the custom link has set the unique access option
     *
     * No-any params required
     *
     * @return whether the custom link has set the unique access option as boolean
     */
    private fun hasUniqueAccess() : Boolean {
        return itemDedicatedList.contains(UNIQUE_ACCESS_KEY)
    }

    /**
     * Function to get expiration time chosen for the custom link
     *
     * No-any params required
     *
     * @return the expiration time chosen for the custom link as [ExpiredTime]
     */
    private fun getExpiredTime() : ExpiredTime {
        return if(::expiredTime.isInitialized)
            expiredTime.value
        else
            NO_EXPIRATION
    }

}