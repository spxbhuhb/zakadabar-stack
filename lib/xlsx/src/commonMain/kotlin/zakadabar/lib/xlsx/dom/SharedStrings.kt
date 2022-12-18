/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

class SharedStrings : SimpleDomElement("sst"), Part {

    override val partName = "/xl/sharedStrings.xml"
    override val contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml"
    override val relType = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings"

    private val strings = mutableMapOf<String, Int>()

    init {
        attributes["xmlns"] = "http://schemas.openxmlformats.org/spreadsheetml/2006/main"
        setCounts(0)
    }

    private fun setCounts(count: Int) {
        val cstr = count.toString()
        attributes["count"] = cstr
        attributes["uniqueCount"] = cstr
    }

    fun add(str: String) : Int {

        val strId = strings.getOrPut(str) {

            childNodes += SimpleDomElement.of("si").also {
                it += SimpleDomElement("t", str)
            }

            val count = strings.size
            setCounts(count + 1)
            count
        }

        return strId
    }


}

