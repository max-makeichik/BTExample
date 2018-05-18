package com.mm.btexample.util

import java.nio.ByteBuffer
import java.util.*

object UuidUtils {
    fun asUuid(bytes: ByteArray): UUID {
        val bb = ByteBuffer.wrap(bytes)
        val firstLong = bb.long
        val secondLong = bb.long
        return UUID(firstLong, secondLong)
    }

    fun asBytes(uuid: UUID): ByteArray {
        val bb = ByteBuffer.wrap(ByteArray(16))
        bb.putLong(uuid.mostSignificantBits)
        bb.putLong(uuid.leastSignificantBits)
        return bb.array()
    }
}