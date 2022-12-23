/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

typealias ContentMap = HashMap<String, ()->ByteArray>

fun ContentMap.saveXlsx(name: String) = saveZip(name, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")

expect fun ContentMap.saveZip(name: String, contentType: String = "application/zip")
