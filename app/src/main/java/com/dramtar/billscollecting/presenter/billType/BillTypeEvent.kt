package com.dramtar.billscollecting.presenter.billType

import com.dramtar.billscollecting.domain.BillTypeData


sealed class BillTypeEvent {
    data class BillTypeSelected(val id: String) : BillTypeEvent()
    data class BillTypeDeleted(val data: BillTypeData) : BillTypeEvent()
    data class CompleteBillType(val name: String) : BillTypeEvent()
    object AddBillType: BillTypeEvent()
}
