package com.pandey.isptoolkit

import com.pandey.isptoolkit.core.calculator.LinkBudgetCalculator
import com.pandey.isptoolkit.core.calculator.LinkBudgetStatus
import com.pandey.isptoolkit.core.calculator.Wavelength
import org.junit.Assert.*
import org.junit.Test

class LinkBudgetCalculatorTest {

    @Test
    fun testLinkBudgetPassStatus() {
        val res = LinkBudgetCalculator.calculate(
            oltTxDbm = 3.0,
            ontSensitivityDbm = -27.0,
            ontOverloadLimitDbm = -8.0,
            wavelength = Wavelength.WL_1490,
            fiberLengthKm = 5.0,
            connectorCount = 4,
            spliceCount = 2,
            splitterLossDb = 13.8,
            safetyMarginDb = 3.0
        )

        // Fiber loss = 5 * 0.28 = 1.4 dB
        // Connectors = 4 * 0.5 = 2.0 dB
        // Splices = 2 * 0.1 = 0.2 dB
        // Total loss = 1.4 + 2.0 + 0.2 + 13.8 + 3.0 = 20.4 dB
        // Est RX = 3.0 - 20.4 = -17.4 dBm
        // Margin = -17.4 - (-27.0) = 9.6 dB -> PASS
        assertEquals(20.4, res.totalOpticalLossDb, 0.01)
        assertEquals(-17.4, res.estimatedRxDbm, 0.01)
        assertEquals(9.6, res.powerMarginDb, 0.01)
        assertEquals(LinkBudgetStatus.PASS, res.status)
    }

    @Test
    fun testLinkBudgetBelowSensitivityStatus() {
        val res = LinkBudgetCalculator.calculate(
            oltTxDbm = 3.0,
            ontSensitivityDbm = -20.0, // High sensitivity threshold
            ontOverloadLimitDbm = -5.0,
            wavelength = Wavelength.WL_1490,
            fiberLengthKm = 25.0, // Long span
            connectorCount = 6,
            spliceCount = 10,
            splitterLossDb = 17.5,
            safetyMarginDb = 3.0
        )

        assertEquals(LinkBudgetStatus.BELOW_SENSITIVITY, res.status)
    }
}
