package com.pandey.isptoolkit

import com.pandey.isptoolkit.core.calculator.Ipv6AddressType
import com.pandey.isptoolkit.core.calculator.SubnetCalculator
import org.junit.Assert.*
import org.junit.Test

class SubnetCalculatorTest {

    @Test
    fun testIpv4Subnet24() {
        val res = SubnetCalculator.calculateIpv4("192.168.1.100", 24)
        assertNotNull(res)
        assertEquals("192.168.1.0", res!!.networkAddress)
        assertEquals("192.168.1.255", res.broadcastAddress)
        assertEquals("255.255.255.0", res.netmask)
        assertEquals("0.0.0.255", res.wildcardMask)
        assertEquals("192.168.1.1", res.firstUsableIp)
        assertEquals("192.168.1.254", res.lastUsableIp)
        assertEquals(256L, res.totalHosts)
        assertEquals(254L, res.usableHosts)
        assertEquals("Class C", res.ipClass)
    }

    @Test
    fun testIpv4Subnet31() {
        // RFC 3021 /31 point to point
        val res = SubnetCalculator.calculateIpv4("10.0.0.2", 31)
        assertNotNull(res)
        assertEquals("10.0.0.2", res!!.networkAddress)
        assertEquals("10.0.0.3", res.broadcastAddress)
        assertEquals(2L, res.usableHosts)
    }

    @Test
    fun testIpv4Subnet32() {
        val res = SubnetCalculator.calculateIpv4("10.0.0.5", 32)
        assertNotNull(res)
        assertEquals("10.0.0.5", res!!.networkAddress)
        assertEquals("10.0.0.5", res.broadcastAddress)
        assertEquals(1L, res.usableHosts)
    }

    @Test
    fun testIpv6Classifications() {
        val linkLocal = SubnetCalculator.classifyIpv6("fe80::1ff:fe23:4567:890a")
        assertNotNull(linkLocal)
        assertEquals(Ipv6AddressType.LINK_LOCAL, linkLocal!!.type)

        val loopback = SubnetCalculator.classifyIpv6("::1")
        assertNotNull(loopback)
        assertEquals(Ipv6AddressType.LOOPBACK, loopback!!.type)
    }
}
