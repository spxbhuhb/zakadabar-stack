/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

class Rels(val root : String = "/", ref: String = "") : SimpleDomElement("Relationships"), Part {

    override val partName = "${root}_rels/$ref.rels"
    override val contentType = ""
    override val relType = ""

    private var relId = 0

    init {
        attributes["xmlns"] = "http://schemas.openxmlformats.org/package/2006/relationships"
    }

    fun addRel(rel: Part) : String {

        val relId = "rId${++relId}"

        childNodes += of("Relationship" ,
            "Id" to relId,
            "Type" to rel.relType,
            "Target"  to rel.partName.substringAfter(root)
        )

        return relId
    }

}