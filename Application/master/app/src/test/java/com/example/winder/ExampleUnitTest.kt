package com.example.winder

import org.junit.Test
import org.junit.Assert.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import org.junit.Rule
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
internal class ExampleUnitTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()
    private val windMock = mock<LocationForecastService>()
    private val windModel = LocationForecastViewModel(windMock)

    @Test
    fun test_calculateProduction() {
        assertEquals(25.0, windModel.calculateProduction(18f, 25.0), 0.0)
        assertEquals(5.5, windModel.calculateProduction(10f, 10.0), 0.1)
    }

    @Test
    fun test_calculateNegativeWindProduction() {
        assertEquals(0.0, windModel.calculateProduction(-3f, 10.0), 0.0)
    }

    @Test
    fun test_calculateZeroWindProduction() {
        assertEquals(0.0, windModel.calculateProduction(0.0f, 10.0), 0.0)
    }

    @Test
    fun test_calculateLowWindProduction() {
        assertEquals(0.0, windModel.calculateProduction(4f, 10.0), 0.0)
    }

    @Test
    fun test_calculateMaxWindProduction() {
        assertEquals(0.0, windModel.calculateProduction(25f, 10.0), 0.0)
        assertEquals(0.0, windModel.calculateProduction(26f, 10.0), 0.0)
    }

}
