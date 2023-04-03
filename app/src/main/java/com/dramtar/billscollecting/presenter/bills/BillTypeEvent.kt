package com.dramtar.billscollecting.presenter.bills

import com.dramtar.billscollecting.domain.BillTypeData

sealed class BillTypeEvent {
    data class Selected(val data: BillTypeData) : BillTypeEvent()
    data class Deleted(val data: BillTypeData) : BillTypeEvent()
    data class Complete(val name: String) : BillTypeEvent()
    object Add : BillTypeEvent()
}
