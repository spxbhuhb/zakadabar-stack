/*
 * Copyright © 2020, Simplexion, Hungary and contributors
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
package zakadabar.stack.frontend.builtin.form.fields

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import zakadabar.stack.comm.websocket.util.PushContent
import zakadabar.stack.data.BlobDto
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.builtin.form.ValidatedForm
import zakadabar.stack.frontend.builtin.util.Thumbnail
import zakadabar.stack.frontend.builtin.util.droparea.DropArea
import kotlin.reflect.KMutableProperty0

class Blobs<T : RecordDto<T>>(
    private val form: ValidatedForm<T>,
    private val prop: KMutableProperty0<MutableList<BlobDto>>
) : FormField<Double>(
    element = document.createElement("div") as HTMLElement
) {

    override fun init() = build {
        prop.get().forEach {
            + Thumbnail(it)
        }

        + DropArea("", ::onProgress)
    }

    fun onProgress(dto: BlobDto, state: PushContent.PushState, progress: Long) {
        + ThumbNail()
    }

    override fun onValidated(report: ValidityReport) {
        val fails = report.fails[prop.name]
        if (fails == null) {
            isValid = true
            element.style.backgroundColor = "white"
        } else {
            isValid = false
            element.style.backgroundColor = "red"
        }
    }

}