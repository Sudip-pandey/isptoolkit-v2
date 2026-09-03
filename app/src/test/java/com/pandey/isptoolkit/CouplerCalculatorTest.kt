package com.pandey.isptoolkit

import com.pandey.isptoolkit.core.calculator.CouplerCalculator
import org.junit.Assert.*
import org.junit.Test

class CouplerCalculatorTest {

    @Test
    fun testCoupler45_55_Ratio() {
        val input = 5.0
        val res = CouplerCalculator.calculate(
            inputDbm = input,
            percentPortA = 45.0,
            insertionLossDbPortA = 0.0,
            insertionLossDbPortB = 0.0
        )

        // 45% -> 5 + 10log10(0.45) ≈ 1.53 dBm
        assertEquals(1.5318, res.portA.idealOutputDbm, 0.01)

        // 55% -> 5 + 10log10(0.55) ≈ 2.40 dBm
        assertEquals(2.4033, res.portB.idealOutputDbm, 0.01)
    }

    @Test
    fun testCoupler50_50_Ratio() {
        val input = 10.0
        val res = CouplerCalculator.calculate(
            inputDbm = input,
            percentPortA = 50.0,
            insertionLossDbPortA = 0.0,
            insertionLossDbPortB = 0.0
        )

        // 50% split of 10 dBm (10 mW) = 5 mW each = 6.9897 dBm
        assertEquals(6.9897, res.portA.idealOutputDbm, 0.01)
        assertEquals(6.9897, res.portB.idealOutputDbm, 0.01)
    }
}
