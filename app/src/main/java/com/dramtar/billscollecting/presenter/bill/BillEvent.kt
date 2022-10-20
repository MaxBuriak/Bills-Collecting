package com.dramtar.billscollecting.presenter.bill

import com.dramtar.billscollecting.domain.BillData

sealed class BillEvent {
    data class AddBill(val amount: Double, val date: Long): BillEvent()
    data class DeleteBill(val data: BillData): BillEvent()
}