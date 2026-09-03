package com.pandey.isptoolkit

import com.pandey.isptoolkit.core.calculator.OpticalPowerCalculator
import org.junit.Assert.*
import org.junit.Test

class OpticalPowerCalculatorTest {

    @Test
    fun testDbmToMwConversions() {
        // 0 dBm = 1 mW
        assertEquals(1.0, OpticalPowerCalculator.dbmToMw(0.0), 0.0001)

        // 10 dBm = 10 mW
        assertEquals(10.0, OpticalPowerCalculator.dbmToMw(10.0), 0.0001)

        // -10 dBm = 0.1 mW
        assertEquals(0.1, OpticalPowerCalculator.dbmToMw(-10.0), 0.0001)

        // 8 dBm ≈ 6.30957 mW
        assertEquals(6.30957, OpticalPowerCalculator.dbmToMw(8.0), 0.001)
    }

    @Test
    fun testMwToDbmConversions() {
        // 1 mW = 0 dBm
        assertEquals(0.0, OpticalPowerCalculator.mwToDbm(1.0), 0.0001)

        // 10 mW = 10 dBm
        assertEquals(10.0, OpticalPowerCalculator.mwToDbm(10.0), 0.0001)

        // 0.1 mW = -10 dBm
        assertEquals(-10.0, OpticalPowerCalculator.mwToDbm(0.1), 0.0001)

        // 0 or negative mW returns NaN
        assertTrue(OpticalPowerCalculator.mwToDbm(0.0).isNaN())
        assertTrue(OpticalPowerCalculator.mwToDbm(-5.0).isNaN())
    }

    @Test
    fun testMicroWConversions() {
        // 0 dBm = 1000 µW
        assertEquals(1000.0, OpticalPowerCalculator.dbmToMicroW(0.0), 0.001)

        // -30 dBm = 1 µW
        assertEquals(1.0, OpticalPowerCalculator.dbmToMicroW(-30.0), 0.001)
    }
}
