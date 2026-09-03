package com.pandey.isptoolkit.core.calculator

object FiberLossCalculator {

    data class WavelengthPreset(
        val nm: Int,
        val attenuationDb: Double,
        val description: String
    )

    val presets = listOf(
        WavelengthPreset(1310, 0.35, "GPON Upstream / Standard SM"),
        WavelengthPreset(1490, 0.25, "GPON Downstream"),
        WavelengthPreset(1550, 0.22, "DWDM / Long Haul"),
        WavelengthPreset(1625, 0.22, "OTDR Test / Monitoring")
    )

    data class FiberLossResult(
        val wavelengthNm: Int,
        val fiberLengthKm: Double,
        val fiberLossDb: Double,
        val connectorCount: Int,
        val connectorLossDb: Double,
        val spliceCount: Int,
        val spliceLossDb: Double,
        val totalLossDb: Double,
        val outputDbm: Double,
        val status: String
    )

    fun calculate(
        inputDbm: Double,
        wavelengthNm: Int,
        fiberLengthKm: Double,
        connectorCount: Int,
        connectorLossDbEach: Double = 0.5,
        spliceCount: Int,
        spliceLossDbEach: Double = 0.1
    ): FiberLossResult {
        val preset = presets.firstOrNull { it.nm == wavelengthNm } ?: presets[0]
        val fiberLoss = preset.attenuationDb * fiberLengthKm
        val connectorLoss = connectorCount * connectorLossDbEach
        val spliceLoss = spliceCount * spliceLossDbEach
        val total = fiberLoss + connectorLoss + spliceLoss
        val output = inputDbm - total
        return FiberLossResult(
            wavelengthNm = wavelengthNm,
            fiberLengthKm = fiberLengthKm,
            fiberLossDb = fiberLoss,
            connectorCount = connectorCount,
            connectorLossDb = connectorLoss,
            spliceCount = spliceCount,
            spliceLossDb = spliceLoss,
            totalLossDb = total,
            outputDbm = output,
            status = OpticalPowerCalculator.classify(output)
        )
    }
}
