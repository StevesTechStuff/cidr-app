package com.stevestechstuff.cidr.util

import kotlin.math.pow

data class CidrOutput(
    val networkAddress: String,
    val subnetMask: String,
    val wildcardMask: String,
    val firstUsable: String,
    val lastUsable: String,
    val broadcastAddress: String,
    val totalIps: String,
    val usableHosts: String,
    val prefixLength: Int,
    val isEdgeCase: Boolean,
    val binaryIpAndMask: String
)

data class SplitOutput(
    val network: String,
    val firstUsable: String,
    val lastUsable: String,
    val broadcast: String,
    val usableHosts: String
)

data class CheatSheetItem(
    val cidr: String,
    val subnetMask: String,
    val totalIps: String,
    val usableHosts: String
)

object CidrUtils {

    // Regex to match valid IPv4 / CIDR e.g., 192.168.1.0/24
    private val CIDR_REGEX = Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/([0-9]|[1-2][0-9]|3[0-2])$")

    fun isValidCidr(input: String): Boolean {
        return input.trim().matches(CIDR_REGEX)
    }

    fun parseIPv4ToUInt(ipAddress: String): UInt {
        val parts = ipAddress.split(".")
        var result = 0u
        for (i in 0..3) {
            result = (result shl 8) or parts[i].toUInt()
        }
        return result
    }

    fun formatUIntToIPv4(ipAddress: UInt): String {
        return "${(ipAddress shr 24) and 255u}.${(ipAddress shr 16) and 255u}.${(ipAddress shr 8) and 255u}.${ipAddress and 255u}"
    }

    fun calculateMaskUInt(prefixLength: Int): UInt {
        if (prefixLength == 0) return 0u
        return (0xFFFFFFFFu) shl (32 - prefixLength)
    }

    fun calculateCidrOutput(input: String): CidrOutput? {
        if (!isValidCidr(input)) return null

        val parts = input.trim().split("/")
        val ipStr = parts[0]
        val prefix = parts[1].toInt()

        val ipUInt = parseIPv4ToUInt(ipStr)
        val maskUInt = calculateMaskUInt(prefix)

        val networkUInt = ipUInt and maskUInt
        val wildcardUInt = maskUInt.inv()
        val broadcastUInt = networkUInt or wildcardUInt

        val totalIpsLong = if (prefix == 0) 4294967296L else 1L shl (32 - prefix)
        
        val isEdgeCase31 = prefix == 31
        val isEdgeCase32 = prefix == 32

        val usableHostsLong = when {
            isEdgeCase32 -> 1L
            isEdgeCase31 -> 2L
            prefix == 0 -> totalIpsLong - 2L // Roughly 4.2 billion hosts
            else -> totalIpsLong - 2L
        }

        val firstUsableUInt = when {
            isEdgeCase32 -> networkUInt
            isEdgeCase31 -> networkUInt
            else -> networkUInt + 1u
        }

        val lastUsableUInt = when {
            isEdgeCase32 -> networkUInt
            isEdgeCase31 -> broadcastUInt
            else -> broadcastUInt - 1u
        }

        val ipBinary = String.format("%32s", ipUInt.toString(2)).replace(' ', '0')
        val maskBinary = String.format("%32s", maskUInt.toString(2)).replace(' ', '0')
        
        val binaryRepresentation = buildString {
            append("IP:   ")
            append(ipBinary.chunked(8).joinToString("."))
            append("\n")
            append("Mask: ")
            append(maskBinary.chunked(8).joinToString("."))
        }

        return CidrOutput(
            networkAddress = formatUIntToIPv4(networkUInt),
            subnetMask = formatUIntToIPv4(maskUInt),
            wildcardMask = formatUIntToIPv4(wildcardUInt),
            firstUsable = formatUIntToIPv4(firstUsableUInt),
            lastUsable = formatUIntToIPv4(lastUsableUInt),
            broadcastAddress = formatUIntToIPv4(broadcastUInt),
            totalIps = if (prefix == 0) "4,294,967,296" else String.format("%,d", totalIpsLong),
            usableHosts = if (prefix == 0) "4,294,967,294" else String.format("%,d", usableHostsLong),
            prefixLength = prefix,
            isEdgeCase = isEdgeCase31 || isEdgeCase32,
            binaryIpAndMask = binaryRepresentation
        )
    }
    
    fun getSiblingSubnets(baseCidr: String, count: Int = 20): List<SplitOutput>? {
        if (!isValidCidr(baseCidr)) return null

        val parts = baseCidr.split("/")
        val baseIpStr = parts[0]
        val prefix = parts[1].toInt()

        if (prefix == 0) return emptyList()

        val baseIpUInt = parseIPv4ToUInt(baseIpStr)
        val baseMask = calculateMaskUInt(prefix)
        val networkStartUInt = baseIpUInt and baseMask

        val subnetSize = 1uL shl (32 - prefix)
        
        val splits = mutableListOf<SplitOutput>()

        for (i in 0 until count) {
            val offset = i.toULong() * subnetSize
            val currentNetworkULong = networkStartUInt.toULong() + offset
            if (currentNetworkULong > 0xFFFFFFFFuL) break

            val currentNetworkUInt = currentNetworkULong.toUInt()
            val newCidrString = "${formatUIntToIPv4(currentNetworkUInt)}/$prefix"
            val output = calculateCidrOutput(newCidrString) ?: continue
            
            splits.add(
                SplitOutput(
                    network = newCidrString,
                    firstUsable = output.firstUsable,
                    lastUsable = output.lastUsable,
                    broadcast = output.broadcastAddress,
                    usableHosts = output.usableHosts
                )
            )
        }
        return splits
    }

    fun getCheatSheet(): List<CheatSheetItem> {
        val list = mutableListOf<CheatSheetItem>()
        for (i in 32 downTo 1) {
            val maskUInt = calculateMaskUInt(i)
            val totalIps = 1L shl (32 - i)
            val usableHosts = when(i) {
                32 -> 1L
                31 -> 2L
                else -> totalIps - 2L
            }
            list.add(
                CheatSheetItem(
                    cidr = "/$i",
                    subnetMask = formatUIntToIPv4(maskUInt),
                    totalIps = String.format("%,d", totalIps),
                    usableHosts = String.format("%,d", usableHosts)
                )
            )
        }
        return list
    }
}
