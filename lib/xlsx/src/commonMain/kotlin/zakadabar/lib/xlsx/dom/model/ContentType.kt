/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom.model

import zakadabar.lib.xlsx.dom.Part
import zakadabar.lib.xlsx.dom.SimpleDomElement

internal class ContentType : SimpleDomElement("Types"), Part {

    override val partName = "/[Content_Types].xml"
    override val contentType = ""
    override val relType = ""

    init {
        attributes["xmlns"] = "http://schemas.openxmlformats.org/package/2006/content-types"

        addDefault("rels", "application/vnd.openxmlformats-package.relationships+xml")
        addDefault("xml", "application/xml")

    }

    fun addDefault(extension: String, contentType: String) {
        childNodes += of("Default" ,"Extension" to extension, "ContentType" to contentType)
    }

    fun addPart(part: Part) {
        childNodes += of("Override" , "PartName" to part.partName, "ContentType" to part.contentType)
    }

}