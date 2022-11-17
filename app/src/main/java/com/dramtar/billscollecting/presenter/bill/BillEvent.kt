package com.dramtar.billscollecting.presenter.bill

import com.dramtar.billscollecting.domain.BillData

sealed class BillEvent {
    data class Add(val amount: Double, val date: Long): BillEvent()
    data class Delete(val data: BillData): BillEvent()
}