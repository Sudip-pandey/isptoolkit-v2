package com.pandey.isptoolkit.core.calculator

object CascadedSplitterCalculator {

    sealed class ChainStage {
        data class Plc(val splitRatio: Int) : ChainStage()
        data class Coupler(val portAPercent: Double) : ChainStage()
        data class FiberSpan(val lengthKm: Double, val wavelengthNm: Int = 1490) : ChainStage()
    }

    data class StageResult(
        val stageIndex: Int,
        val description: String,
        val lossDb: Double,
        val outputDbm: Double,
        val status: String
    )

    fun calculate(inputDbm: Double, stages: List<ChainStage>): List<StageResult> {
        var current = inputDbm
        return stages.mapIndexed { index, stage ->
            val (loss, desc) = when (stage) {
                is ChainStage.Plc -> {
                    val r = PlcSplitterCalculator.calculate(current, stage.splitRatio)
                    Pair(r.practicalLossDb, "PLC 1:${stage.splitRatio}")
                }
                is ChainStage.Coupler -> {
                    val r = CouplerCalculator.calculate(current, stage.portAPercent)
                    Pair(current - r.portAOutputDbm, "Coupler ${stage.portAPercent.toInt()}/${(100 - stage.portAPercent).toInt()}")
                }
                is ChainStage.FiberSpan -> {
                    val r = FiberLossCalculator.calculate(current, stage.wavelengthNm, stage.lengthKm, 2, 0.5, 1, 0.1)
                    Pair(r.totalLossDb, "Fiber ${stage.lengthKm}km @${stage.wavelengthNm}nm")
                }
            }
            current -= loss
            StageResult(
                stageIndex = index + 1,
                description = desc,
                lossDb = loss,
                outputDbm = current,
                status = OpticalPowerCalculator.classify(current)
            )
        }
    }
}
