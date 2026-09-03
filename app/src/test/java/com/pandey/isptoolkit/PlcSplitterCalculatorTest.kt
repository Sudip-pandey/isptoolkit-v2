package com.pandey.isptoolkit

import com.pandey.isptoolkit.core.calculator.PlcSplitterCalculator
import org.junit.Assert.*
import org.junit.Test

class PlcSplitterCalculatorTest {

    @Test
    fun testIdealSplitterLosses() {
        val input = 8.0

        // 1:2 split loss ≈ 3.01 dB -> ideal output ≈ 4.99 dBm
        val res1to2 = PlcSplitterCalculator.calculate(inputDbm = input, splitRatioStr = "1:2", insertionLossDb = 0.0, excessLossDb = 0.0, connectorLossDb = 0.0, spliceLossDb = 0.0)
        assertEquals(4.9897, res1to2.idealOutputDbm, 0.01)

        // 1:4 split loss ≈ 6.02 dB -> ideal output ≈ 1.98 dBm
        val res1to4 = PlcSplitterCalculator.calculate(inputDbm = input, splitRatioStr = "1:4", insertionLossDb = 0.0, excessLossDb = 0.0, connectorLossDb = 0.0, spliceLossDb = 0.0)
        assertEquals(1.9794, res1to4.idealOutputDbm, 0.01)

        // 1:8 split loss ≈ 9.03 dB -> ideal output ≈ -1.03 dBm
        val res1to8 = PlcSplitterCalculator.calculate(inputDbm = input, splitRatioStr = "1:8", insertionLossDb = 0.0, excessLossDb = 0.0, connectorLossDb = 0.0, spliceLossDb = 0.0)
        assertEquals(-1.0308, res1to8.idealOutputDbm, 0.01)

        // 1:16 -> ideal output ≈ -4.04 dBm
        val res1to16 = PlcSplitterCalculator.calculate(inputDbm = input, splitRatioStr = "1:16", insertionLossDb = 0.0, excessLossDb = 0.0, connectorLossDb = 0.0, spliceLossDb = 0.0)
        assertEquals(-4.0412, res1to16.idealOutputDbm, 0.01)

        // 1:32 -> ideal output ≈ -7.05 dBm
        val res1to32 = PlcSplitterCalculator.calculate(inputDbm = input, splitRatioStr = "1:32", insertionLossDb = 0.0, excessLossDb = 0.0, connectorLossDb = 0.0, spliceLossDb = 0.0)
        assertEquals(-7.0515, res1to32.idealOutputDbm, 0.01)

        // 1:64 -> ideal output ≈ -10.06 dBm
        val res1to64 = PlcSplitterCalculator.calculate(inputDbm = input, splitRatioStr = "1:64", insertionLossDb = 0.0, excessLossDb = 0.0, connectorLossDb = 0.0, spliceLossDb = 0.0)
        assertEquals(-10.0618, res1to64.idealOutputDbm, 0.01)
    }

    @Test
    fun testPracticalSplitterLosses() {
        val input = 8.0
        // 1:4 with 0.8 insertion, 0.2 excess, 0.5 connector, 0.1 splice -> total add. loss = 1.6 dB
        // Estimated practical output = 1.98 - 1.6 = 0.38 dBm
        val res = PlcSplitterCalculator.calculate(
            inputDbm = input,
            splitRatioStr = "1:4",
            insertionLossDb = 0.8,
            excessLossDb = 0.2,
            connectorLossDb = 0.5,
            spliceLossDb = 0.1
        )
        assertEquals(1.6, res.totalAdditionalLossDb, 0.001)
        assertEquals(0.3794, res.estimatedPracticalOutputDbm, 0.01)
    }
}
