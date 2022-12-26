/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

/**
 * Simple class form zip data streaming
 */
internal expect class OutputStream {

    fun write(b: ByteArray)

    fun close()

}
