package com.dramtar.billscollecting.presenter.billType

import com.dramtar.billscollecting.domain.BillTypeData


sealed class BillTypeEvent {
    data class Selected(val id: String) : BillTypeEvent()
    data class Deleted(val data: BillTypeData) : BillTypeEvent()
    data class Complete(val name: String) : BillTypeEvent()
    object Add: BillTypeEvent()
}
