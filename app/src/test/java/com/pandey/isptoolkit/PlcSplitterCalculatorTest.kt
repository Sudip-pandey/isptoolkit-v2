package com.pandey.isptoolkit

import com.pandey.isptoolkit.core.calculator.PlcSplitterCalculator
import org.junit.Assert.*
import org.junit.Test

class PlcSplitterCalculatorTest {

    @Test
    fun testSplitterCalculations() {
        val input = 8.0

        // 1:2 split -> theoretical 3.0, practical 3.3 -> output 4.7
        val res1to2 = PlcSplitterCalculator.calculate(inputDbm = input, splitRatio = 2)
        assertEquals(3.0, res1to2.theoreticalLossDb, 0.01)
        assertEquals(3.3, res1to2.practicalLossDb, 0.01)
        assertEquals(4.7, res1to2.outputDbm, 0.01)

        // 1:4 split -> theoretical 6.0, practical 6.5 -> output 1.5
        val res1to4 = PlcSplitterCalculator.calculate(inputDbm = input, splitRatio = 4)
        assertEquals(6.0, res1to4.theoreticalLossDb, 0.01)
        assertEquals(6.5, res1to4.practicalLossDb, 0.01)
        assertEquals(1.5, res1to4.outputDbm, 0.01)

        // 1:8 split -> theoretical 9.0, practical 9.8 -> output -1.8
        val res1to8 = PlcSplitterCalculator.calculate(inputDbm = input, splitRatio = 8)
        assertEquals(9.0, res1to8.theoreticalLossDb, 0.01)
        assertEquals(9.8, res1to8.practicalLossDb, 0.01)
        assertEquals(-1.8, res1to8.outputDbm, 0.01)
    }

    @Test
    fun testAvailableRatios() {
        val ratios = PlcSplitterCalculator.availableRatios()
        assertTrue(ratios.contains(2))
        assertTrue(ratios.contains(4))
        assertTrue(ratios.contains(8))
        assertTrue(ratios.contains(16))
        assertTrue(ratios.contains(32))
        assertTrue(ratios.contains(64))
    }
}
