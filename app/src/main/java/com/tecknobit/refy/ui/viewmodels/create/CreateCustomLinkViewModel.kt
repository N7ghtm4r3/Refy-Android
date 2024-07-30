package com.tecknobit.refy.ui.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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

    val NEW_RESOURCE = Pair(mutableStateOf(""), mutableStateOf(""))

    lateinit var expiredTime: MutableState<ExpiredTime>

    lateinit var resourcesSupportList: SnapshotStateList<MutableState<Pair<MutableState<String>, MutableState<String>>>>

    lateinit var resources: MutableMap<String, String>

    override fun initExistingItem(
        item: CustomRefyLink?
    ) {
        resourcesSupportList = mutableStateListOf()
        if(item != null) {
            existingItem = item
            if(existingItem!!.hasUniqueAccess())
                itemDedicatedList.add(UNIQUE_ACCESS_KEY)
            if(existingItem!!.expiredTime != NO_EXPIRATION)
                itemDedicatedList.add(EXPIRED_TIME_KEY)
            resources = existingItem!!.resources
        } else
            resources = mutableMapOf(
                Pair(NEW_RESOURCE.first.value, NEW_RESOURCE.second.value)
            )
        resources.entries.forEach { resource ->
            resourcesSupportList.add(
                mutableStateOf(Pair(
                    mutableStateOf(resource.key),
                    mutableStateOf(resource.value)
                ))
            )
        }
    }

    fun addNewResource() {
        resourcesSupportList.add(mutableStateOf(NEW_RESOURCE))
    }

    fun removeResource(
        index: Int
    ) {
        resourcesSupportList.removeAt(index)
    }

    override fun createItem(
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    override fun editItem(
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

}