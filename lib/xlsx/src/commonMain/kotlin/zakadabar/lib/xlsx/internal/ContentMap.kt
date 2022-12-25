/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

internal typealias ContentMap = HashMap<String, ()->Any>
internal expect fun ContentMap.generateZip(zipContent: ByteArray.() -> Unit)

