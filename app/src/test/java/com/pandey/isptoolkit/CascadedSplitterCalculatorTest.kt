package com.pandey.isptoolkit

import com.pandey.isptoolkit.core.calculator.CascadeComponentType
import com.pandey.isptoolkit.core.calculator.CascadeStageInput
import com.pandey.isptoolkit.core.calculator.CascadedSplitterCalculator
import org.junit.Assert.*
import org.junit.Test

class CascadedSplitterCalculatorTest {

    @Test
    fun testMultiStageCascadedChain() {
        val oltPower = 8.0
        val stages = listOf(
            CascadeStageInput(id = "1", type = CascadeComponentType.PLC_SPLITTER, name = "1:4 PLC", ratioOrSpec = "1:4", insertionLossDb = 0.8),
            CascadeStageInput(id = "2", type = CascadeComponentType.PLC_SPLITTER, name = "1:8 PLC", ratioOrSpec = "1:8", insertionLossDb = 1.0),
            CascadeStageInput(id = "3", type = CascadeComponentType.PLC_SPLITTER, name = "1:2 PLC", ratioOrSpec = "1:2", insertionLossDb = 0.5)
        )

        val res = CascadedSplitterCalculator.calculateChain(initialTxDbm = oltPower, stageInputs = stages)

        assertEquals(3, res.stages.size)
        // Stage 1 output = 8.0 - (6.02 + 0.8 + 0.6) = 0.58 dBm
        // Stage 2 output = 0.58 - (9.03 + 1.0 + 0.6) = -10.05 dBm
        // Stage 3 output = -10.05 - (3.01 + 0.5 + 0.6) = -14.16 dBm
        assertTrue(res.finalRxDbm < 0.0)
        assertEquals(oltPower - res.finalRxDbm, res.totalChainLossDb, 0.01)
    }
}
