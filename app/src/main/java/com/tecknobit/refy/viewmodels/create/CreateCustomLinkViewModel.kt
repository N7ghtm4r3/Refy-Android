package com.tecknobit.refy.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refycore.records.links.CustomRefyLink
import com.tecknobit.refycore.records.links.CustomRefyLink.EXPIRED_TIME_KEY
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime.NO_EXPIRATION
import com.tecknobit.refycore.records.links.CustomRefyLink.UNIQUE_ACCESS_KEY

class CreateCustomLinkViewModel(
    snackbarHostState: SnackbarHostState
): CreateItemViewModel<CustomRefyLink>(
    snackbarHostState = snackbarHostState
) {

    private val newResource = Pair("", "")

    lateinit var expiredTime: MutableState<ExpiredTime>

    private var resources: MutableMap<String, String> = mutableMapOf()

    lateinit var resourcesSupportList: SnapshotStateList<Pair<String, String>>

    private var fields: Map<String, String> = mutableMapOf()

    lateinit var fieldsSupportList: SnapshotStateList<Pair<String, String>>

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

    fun addNewItem(
        supportList: SnapshotStateList<Pair<String, String>>
    ) {
        supportList.add(newResource)
    }

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

    fun removeItem(
        supportList: SnapshotStateList<Pair<String, String>>,
        index: Int
    ) {
        supportList.removeAt(index)
    }

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

    private fun hasUniqueAccess() : Boolean {
        return itemDedicatedList.contains(UNIQUE_ACCESS_KEY)
    }

    private fun getExpiredTime() : ExpiredTime {
        return if(::expiredTime.isInitialized)
            expiredTime.value
        else
            NO_EXPIRATION
    }

}