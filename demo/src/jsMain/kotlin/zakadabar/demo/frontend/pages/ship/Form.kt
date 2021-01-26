/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSelectElement
import zakadabar.demo.data.SeaDto
import zakadabar.demo.data.ShipDto
import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.data.builtin.AccountPublicDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.form.FormClasses
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.fields.Images
import zakadabar.stack.frontend.elements.ZkClasses.Companion.zkClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.CoreStrings
import zakadabar.stack.frontend.util.escape
import zakadabar.stack.frontend.util.launch

class Form : ZkForm<ShipDto>() {

    override fun init() = build {
        + header(Strings.ships)
        + column {
            + row {
                + basics() marginRight 8
                + description()
            }
            + images()
            + buttons()
        }
    }

    private fun basics() = section(Strings.basics, Strings.shipBasicsExplanation) {

        ifNotCreate {
            + dto::id
        }

        + dto::name
        + dto::hasPirateFlag

        + select(dto::speed) {
            SpeedDto.all()
                .map { it.id to it.description }
                .sortedBy { it.second }
        }

        + select(dto::captain) {
            AccountPublicDto.all().sortedBy { it.fullName }.map { it.id to it.fullName }
        }

        autoLabel = false
        val ports = select(dto::port) {
            // ports are initialized by the PreSelect below
            emptyList()
        }

        + Strings.sea
        + PreSelect {
            options = suspend { SeaDto.all().sortedBy { it.name }.map { it.id to it.name } }
            onSelected = {

            }
        }

        + Strings.port
        + ports
    }

    private fun description() = section(Strings.description, Strings.shipDescriptionExplanation, fieldGrid = false) {
        style { flexGrow = "1" }
        + textarea(dto::description) cssClass zkClasses.h100
    }

    private fun images() = section(Strings.images, fieldGrid = false) {
        + Images(this@Form, dto.id)
    }

    class PreSelect() : ZkElement(
        element = document.createElement("select") as HTMLElement
    ) {
        var sortOptions: Boolean = true
        var onSelected: (RecordId<*>?) -> Unit = { }
        var options: suspend () -> List<Pair<RecordId<*>, String>> = { emptyList() }

        constructor(builder: PreSelect.() -> Unit) : this() {
            builder()
        }

        fun getValue(): Long? = 0L

        override fun init(): ZkElement {
            className = FormClasses.formClasses.select

            launch {
                val items = if (sortOptions) options().sortedBy { it.second } else options()

                val value = getValue()

                var s = if (value == null || value == 0L) {
                    """<option value="0" selected>${CoreStrings.notSelected}</option>"""
                } else {
                    """<option value="0">${CoreStrings.notSelected}</option>"""
                }

                items.forEach {
                    s += if (it.first == value) {
                        """<option value="${it.first}" selected>${escape(it.second)}</option>"""
                    } else {
                        """<option value="${it.first}">${escape(it.second)}</option>"""
                    }
                }

                element.innerHTML = s
            }

            on("input") { _ ->
                onSelected((element as HTMLSelectElement).value.toLongOrNull())
            }

            return this
        }
    }

}
