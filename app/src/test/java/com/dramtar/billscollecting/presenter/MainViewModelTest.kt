package com.dramtar.billscollecting.presenter

import com.dramtar.billscollecting.data.repository.FakeRepository
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var fakeRepository: FakeRepository
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeRepository()
        mainViewModel = MainViewModel(fakeRepository)
    }

    @Test
    fun getCSVFileName_correctFileName() {
        val formattedPeriodOfTime = "fmt_time"
        mainViewModel.getCSVFileName()
    }
}