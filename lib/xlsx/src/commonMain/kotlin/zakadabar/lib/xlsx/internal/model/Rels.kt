/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.internal.dom.Node

internal class Rels(val root : String = "/", ref: String = "") : Node( "Relationships"), Part {

    override val partName = "${root}_rels/$ref.rels"
    override val contentType = ""
    override val relType = ""

    init {
        this["xmlns"] = "http://schemas.openxmlformats.org/package/2006/relationships"
    }
    fun addRel(rel: Part) : String {

        val relId = "rId${size + 1}"

        + Node("Relationship") {
            this["Id"] = relId
            this["Type"] = rel.relType
            this["Target"] = rel.partName.substringAfter(root)
        }

        return relId
    }

}
