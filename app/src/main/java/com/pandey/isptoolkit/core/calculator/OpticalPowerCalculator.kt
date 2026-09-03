package com.pandey.isptoolkit.core.calculator

import kotlin.math.log10
import kotlin.math.pow

object OpticalPowerCalculator {
    fun dbmToMilliwatts(dbm: Double): Double = 10.0.pow(dbm / 10.0)
    fun dbmToMicrowatts(dbm: Double): Double = dbmToMilliwatts(dbm) * 1000.0
    fun milliwattsToDbm(mw: Double): Double = 10.0 * log10(mw)
    fun microwattsToDbm(uw: Double): Double = milliwattsToDbm(uw / 1000.0)

    data class OpticalPowerResult(
        val dbm: Double,
        val milliwatts: Double,
        val microwatts: Double,
        val status: String
    )

    fun classify(dbm: Double): String = when {
        dbm > 0 -> "Too High / Overload Risk"
        dbm >= -10 -> "Excellent"
        dbm >= -20 -> "Good"
        dbm >= -25 -> "Fair"
        dbm >= -30 -> "Weak"
        else -> "Too Low / LOS Risk"
    }

    fun calculate(dbm: Double): OpticalPowerResult {
        val mw = dbmToMilliwatts(dbm)
        val uw = dbmToMicrowatts(dbm)
        return OpticalPowerResult(
            dbm = dbm,
            milliwatts = mw,
            microwatts = uw,
            status = classify(dbm)
        )
    }
}
