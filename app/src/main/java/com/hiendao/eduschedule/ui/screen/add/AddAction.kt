package com.hiendao.eduschedule.ui.screen.add

import com.hiendao.eduschedule.utils.entity.TypeAdd

sealed interface AddAction {
    data class OnTitleChange(val title: String): AddAction
    data object OnAdd : AddAction
    data class OnTypeChange(val type: TypeAdd): AddAction
}