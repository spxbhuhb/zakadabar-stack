/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.entity.browser.frontend.util

import org.w3c.dom.DataTransfer
import org.w3c.dom.get
import org.w3c.files.File
import zakadabar.stack.frontend.comm.rest.EntityCache.launchCreateAndPush

class EntityDrop(
    private val parentId: Long?
) {

    private var bestStringValue: String? = null
    private var bestStringType: String? = null

    fun process(transfer: DataTransfer) {
        var index = 0
        val end = transfer.items.length

        while (index < end) {

            val item = transfer.items[index] ?: continue

            when (item.kind) {
                "string" -> choose(item.type, transfer.getData(item.type))
                "file" -> addFile(item.getAsFile() !!)
            }

            index ++
        }

// just add a text/plain entity when this happens
//        when (bestStringType) {
//            "text/html" -> addHtml(bestStringValue!!)
//            "text/plain" -> addText(bestStringValue!!)
//        }
    }

    private fun choose(mimeType: String, value: String) {
        if (mimeType == "text/plain" && mimeType != "text/html") {
            bestStringType = mimeType
            bestStringValue = value
        }
        if (mimeType == "text/html") {
            bestStringType = mimeType
            bestStringValue = value
        }
    }

    private fun addFile(file: File) = launchCreateAndPush(parentId, file)

}