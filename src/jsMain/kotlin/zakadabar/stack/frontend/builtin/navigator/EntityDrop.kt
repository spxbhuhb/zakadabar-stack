/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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