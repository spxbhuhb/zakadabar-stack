/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.internal.dom.Node
import zakadabar.lib.xlsx.internal.putCount

internal class SharedStrings : Node("sst"), Part {

    override val partName = "/xl/sharedStrings.xml"
    override val contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml"
    override val relType = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings"

    private val strings = mutableMapOf<String, Int>()

    init {
        this["xmlns"] = "http://schemas.openxmlformats.org/spreadsheetml/2006/main"
    }

    fun addString(str: String) : Int {

        val strId = strings.getOrPut(str) {

            + node("si") {
                + node("t", str)
            }

            putCount() - 1

        }

        return strId
    }

}

