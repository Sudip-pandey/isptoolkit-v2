package com.pandey.isptoolkit

import com.pandey.isptoolkit.core.calculator.SubnetCalculator
import org.junit.Assert.*
import org.junit.Test

class SubnetCalculatorTest {

    @Test
    fun testIpv4SubnetCalculationClassC() {
        val result = SubnetCalculator.calculate("192.168.1.50", 24)

        assertNotNull(result)
        assertEquals("192.168.1.0", result!!.networkAddress)
        assertEquals("192.168.1.255", result.broadcastAddress)
        assertEquals("255.255.255.0", result.subnetMask)
        assertEquals("192.168.1.1", result.firstHost)
        assertEquals("192.168.1.254", result.lastHost)
        assertEquals(256L, result.totalHosts)
        assertEquals(254L, result.usableHosts)
    }

    @Test
    fun testIpv4SubnetCalculationSlash30() {
        val result = SubnetCalculator.calculate("10.0.0.1", 30)

        assertNotNull(result)
        assertEquals("10.0.0.0", result!!.networkAddress)
        assertEquals("10.0.0.3", result.broadcastAddress)
        assertEquals("10.0.0.1", result.firstHost)
        assertEquals("10.0.0.2", result.lastHost)
        assertEquals(4L, result.totalHosts)
        assertEquals(2L, result.usableHosts)
    }

    @Test
    fun testIpv4SubnetCalculationSlash32() {
        val result = SubnetCalculator.calculate("10.0.0.1", 32)

        assertNotNull(result)
        assertEquals("10.0.0.1", result!!.networkAddress)
        assertEquals("10.0.0.1", result.broadcastAddress)
        assertEquals(1L, result.totalHosts)
        assertEquals(1L, result.usableHosts)
    }
}
