/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.dom.*

internal class Rels(val root : String = "/", ref: String = "") : Node("Relationships",
"xmlns" to "http://schemas.openxmlformats.org/package/2006/relationships"
), Part {

    override val partName = "${root}_rels/$ref.rels"
    override val contentType = ""
    override val relType = ""

    fun addRel(rel: Part) : String {

        val relId = "rId${elements.size + 1}"

        + Node("Relationship",
            "Id" to relId,
            "Type" to rel.relType,
            "Target"  to rel.partName.substringAfter(root)
        )

        return relId
    }

}
