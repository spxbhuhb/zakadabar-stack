/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

internal actual class OutputStream(val out: (ByteArray)->Unit, val close: ()->Unit) {

    actual fun write(b: ByteArray) = out.invoke(b)
    actual fun close() = close.invoke()

}
