package com.dramtar.billscollecting.presenter

import java.io.File

sealed class UIUpdatingEvent {
    data class OpenCreatedCSV(val file: File): UIUpdatingEvent()
    object AddBillTypeClicked: UIUpdatingEvent()
    object NavigateToOverview: UIUpdatingEvent()
    object NavigateToTypeOverview: UIUpdatingEvent()
}
