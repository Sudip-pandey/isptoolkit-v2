package com.pandey.isptoolkit

import com.pandey.isptoolkit.core.calculator.LinkBudgetCalculator
import org.junit.Assert.*
import org.junit.Test

class LinkBudgetCalculatorTest {

    @Test
    fun testLinkBudgetPassStatus() {
        val res = LinkBudgetCalculator.calculate(
            txPowerDbm = 3.0,
            totalLossDb = 20.4,
            receiverSensitivityDbm = -27.0,
            overloadDbm = -8.0
        )

        // Est RX = 3.0 - 20.4 = -17.4 dBm
        // Margin = -17.4 - (-27.0) = 9.6 dB -> EXCELLENT
        assertEquals(20.4, res.totalLossDb, 0.01)
        assertEquals(-17.4, res.rxPowerDbm, 0.01)
        assertEquals(9.6, res.linkMarginDb, 0.01)
        assertEquals(LinkBudgetCalculator.LinkStatus.EXCELLENT, res.status)
    }

    @Test
    fun testLinkBudgetBelowSensitivityStatus() {
        val res = LinkBudgetCalculator.calculate(
            txPowerDbm = 3.0,
            totalLossDb = 28.0,
            receiverSensitivityDbm = -20.0,
            overloadDbm = -5.0
        )

        assertEquals(LinkBudgetCalculator.LinkStatus.INSUFFICIENT, res.status)
    }
}
