package com.pandey.isptoolkit

import com.pandey.isptoolkit.core.calculator.CascadedSplitterCalculator
import org.junit.Assert.*
import org.junit.Test

class CascadedSplitterCalculatorTest {

    @Test
    fun testMultiStageCascadedChain() {
        val oltPower = 8.0
        val stages = listOf(
            CascadedSplitterCalculator.ChainStage.Plc(splitRatio = 4),
            CascadedSplitterCalculator.ChainStage.Plc(splitRatio = 8),
            CascadedSplitterCalculator.ChainStage.Plc(splitRatio = 2)
        )

        val res = CascadedSplitterCalculator.calculate(inputDbm = oltPower, stages = stages)

        assertEquals(3, res.size)
        assertTrue(res.last().outputDbm < 0.0)
    }
}
