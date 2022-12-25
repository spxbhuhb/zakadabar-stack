/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.internal.dom.Node

internal class ContentType : Node("Types",
"xmlns" to "http://schemas.openxmlformats.org/package/2006/content-types"
), Part {

    override val partName = "/[Content_Types].xml"
    override val contentType = ""
    override val relType = ""

    init {
        addDefault("rels", "application/vnd.openxmlformats-package.relationships+xml")
        addDefault("xml", "application/xml")
    }

    fun addDefault(extension: String, contentType: String) = + Node("Default" ,"Extension" to extension, "ContentType" to contentType)

    fun addPart(part: Part) = + Node("Override" , "PartName" to part.partName, "ContentType" to part.contentType)

}