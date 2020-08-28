/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

expect class CommonDateTime {

    override fun toString(): String

    fun asFileTimeStamp(): String

    fun millisSinceEpoch(): Long

    companion object {
        fun now(): CommonDateTime

        fun fromMillis(millis: Long): CommonDateTime
    }

}