package com.tecknobit.refy.ui.viewmodels.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.tecknobit.refycore.records.RefyUser
import com.tecknobit.refycore.records.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateTeamViewModel(
    snackbarHostState: SnackbarHostState
) : CreateItemViewModel<Team>(
    snackbarHostState = snackbarHostState
) {

    lateinit var logoPic: MutableState<String>

    private val _currentUsers = MutableStateFlow(
        value = emptyList<RefyUser>().toMutableStateList()
    )
    val currentUsers: StateFlow<SnapshotStateList<RefyUser>> = _currentUsers

    override fun initExistingItem(
        item: Team?
    ) {
        if(item != null) {
            existingItem = item
            existingItem!!.members.forEach { member ->
                itemDedicatedList.add(member.id)
            }
        }
    }

    fun fetchCurrentUsers() {
        // TODO: MAKE THE REQUEST THEN
        _currentUsers.value = mutableStateListOf(
            RefyUser(
                    "id",
            "User",
            "One",
            "p@gmail.com",
            "https://t4.ftcdn.net/jpg/03/86/82/73/360_F_386827376_uWOOhKGk6A4UVL5imUBt20Bh8cmODqzx.jpg",
            "@id"
            ),
            RefyUser(
                    "id3213",
            "User",
            "One",
            "p@gmail.com",
            "https://images.photowall.com/products/56987/outer-space-4.jpg?h=699&q=85",
            "@id3213"
            ),
            RefyUser(
                    "id2",
            "User",
            "One",
            "p@gmail.com",
            "https://images.photowall.com/products/56987/outer-space-4.jpg?h=699&q=85",
            "@id2"
            ),
            RefyUser(
                    "idwgewgw2",
            "User",
            "One",
            "p@gmail.com",
            "https://images.photowall.com/products/56987/outer-space-4.jpg?h=699&q=85",
            "@id2"
            ),
            RefyUser(
                    "igwegwgwegwegwegewgd2",
            "User",
            "LAST",
            "p@gmail.com",
            "https://images.photowall.com/products/56987/outer-space-4.jpg?h=699&q=85",
            "@id2"
            ),
            RefyUser(
                    "iegwgwed2",
            "User",
            "One",
            "p@gmail.com",
            "https://images.photowall.com/products/56987/outer-space-4.jpg?h=699&q=85",
            "@id2"
            )
        )
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
        existingItem!!.id
        onSuccess.invoke()
    }

}