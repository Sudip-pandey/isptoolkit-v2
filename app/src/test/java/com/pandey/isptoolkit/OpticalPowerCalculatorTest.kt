package com.pandey.isptoolkit

import com.pandey.isptoolkit.core.calculator.OpticalPowerCalculator
import org.junit.Assert.*
import org.junit.Test

class OpticalPowerCalculatorTest {

    @Test
    fun testDbmToMwConversions() {
        // 0 dBm = 1 mW
        assertEquals(1.0, OpticalPowerCalculator.dbmToMilliwatts(0.0), 0.0001)

        // 10 dBm = 10 mW
        assertEquals(10.0, OpticalPowerCalculator.dbmToMilliwatts(10.0), 0.0001)

        // -10 dBm = 0.1 mW
        assertEquals(0.1, OpticalPowerCalculator.dbmToMilliwatts(-10.0), 0.0001)

        // 8 dBm ≈ 6.30957 mW
        assertEquals(6.30957, OpticalPowerCalculator.dbmToMilliwatts(8.0), 0.001)
    }

    @Test
    fun testMwToDbmConversions() {
        // 1 mW = 0 dBm
        assertEquals(0.0, OpticalPowerCalculator.milliwattsToDbm(1.0), 0.0001)

        // 10 mW = 10 dBm
        assertEquals(10.0, OpticalPowerCalculator.milliwattsToDbm(10.0), 0.0001)

        // 0.1 mW = -10 dBm
        assertEquals(-10.0, OpticalPowerCalculator.milliwattsToDbm(0.1), 0.0001)
    }

    @Test
    fun testMicroWConversions() {
        // 0 dBm = 1000 µW
        assertEquals(1000.0, OpticalPowerCalculator.dbmToMicrowatts(0.0), 0.001)

        // -30 dBm = 1 µW
        assertEquals(1.0, OpticalPowerCalculator.dbmToMicrowatts(-30.0), 0.001)
    }
}
