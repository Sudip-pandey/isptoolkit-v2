package com.pandey.isptoolkit.core.calculator

object CouplerCalculator {

    data class CouplerResult(
        val portARatio: Double,
        val portBRatio: Double,
        val portAOutputDbm: Double,
        val portBOutputDbm: Double,
        val portAStatus: String,
        val portBStatus: String
    )

    fun calculate(inputDbm: Double, portAPercent: Double): CouplerResult {
        val portBPercent = 100.0 - portAPercent
        val portALoss = -10 * Math.log10(portAPercent / 100.0)
        val portBLoss = -10 * Math.log10(portBPercent / 100.0)
        val portAOut = inputDbm - portALoss
        val portBOut = inputDbm - portBLoss
        return CouplerResult(
            portARatio = portAPercent,
            portBRatio = portBPercent,
            portAOutputDbm = portAOut,
            portBOutputDbm = portBOut,
            portAStatus = OpticalPowerCalculator.classify(portAOut),
            portBStatus = OpticalPowerCalculator.classify(portBOut)
        )
    }

    fun presetRatios() = listOf(50.0, 55.0, 70.0, 80.0, 90.0, 95.0)
}
