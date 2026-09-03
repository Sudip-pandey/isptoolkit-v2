package com.pandey.isptoolkit.core.calculator

object PlcSplitterCalculator {

    // Theoretical insertion loss for PLC splitters
    private val theoreticalLoss = mapOf(
        2 to 3.0, 4 to 6.0, 8 to 9.0, 16 to 12.0, 32 to 15.0, 64 to 18.0
    )

    // Practical excess loss margins
    private val excessLoss = mapOf(
        2 to 0.3, 4 to 0.5, 8 to 0.8, 16 to 1.0, 32 to 1.5, 64 to 2.0
    )

    data class SplitterResult(
        val splitRatio: Int,
        val theoreticalLossDb: Double,
        val practicalLossDb: Double,
        val outputDbm: Double,
        val status: String
    )

    fun calculate(inputDbm: Double, splitRatio: Int): SplitterResult {
        val theoretical = theoreticalLoss[splitRatio] ?: (10 * Math.log10(splitRatio.toDouble()))
        val practical = theoretical + (excessLoss[splitRatio] ?: 1.0)
        val output = inputDbm - practical
        return SplitterResult(
            splitRatio = splitRatio,
            theoreticalLossDb = theoretical,
            practicalLossDb = practical,
            outputDbm = output,
            status = OpticalPowerCalculator.classify(output)
        )
    }

    fun availableRatios() = listOf(2, 4, 8, 16, 32, 64)
}
