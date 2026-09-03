package com.pandey.isptoolkit.core.calculator

object LinkBudgetCalculator {

    enum class LinkStatus { EXCELLENT, GOOD, MARGINAL, INSUFFICIENT }

    data class LinkBudgetResult(
        val txPowerDbm: Double,
        val totalLossDb: Double,
        val rxPowerDbm: Double,
        val receiverSensitivityDbm: Double,
        val overloadDbm: Double,
        val linkMarginDb: Double,
        val status: LinkStatus,
        val statusLabel: String
    )

    fun calculate(
        txPowerDbm: Double,
        totalLossDb: Double,
        receiverSensitivityDbm: Double,
        overloadDbm: Double = 0.0
    ): LinkBudgetResult {
        val rxPower = txPowerDbm - totalLossDb
        val margin = rxPower - receiverSensitivityDbm
        val status = when {
            rxPower > overloadDbm -> LinkStatus.INSUFFICIENT // overload
            margin >= 6.0 -> LinkStatus.EXCELLENT
            margin >= 3.0 -> LinkStatus.GOOD
            margin >= 0.0 -> LinkStatus.MARGINAL
            else -> LinkStatus.INSUFFICIENT
        }
        val label = when (status) {
            LinkStatus.EXCELLENT -> "Excellent"
            LinkStatus.GOOD -> "Good"
            LinkStatus.MARGINAL -> "Marginal"
            LinkStatus.INSUFFICIENT -> if (rxPower > overloadDbm) "Overload Risk" else "Insufficient Power"
        }
        return LinkBudgetResult(
            txPowerDbm = txPowerDbm,
            totalLossDb = totalLossDb,
            rxPowerDbm = rxPower,
            receiverSensitivityDbm = receiverSensitivityDbm,
            overloadDbm = overloadDbm,
            linkMarginDb = margin,
            status = status,
            statusLabel = label
        )
    }
}
