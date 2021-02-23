/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import kotlinx.browser.document
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSelectElement
import zakadabar.demo.data.PortDto
import zakadabar.demo.data.SeaDto
import zakadabar.demo.data.ShipDto
import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.data.builtin.AccountPublicDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.fields.Images
import zakadabar.stack.frontend.builtin.form.fields.ValidatedOptRecordSelect
import zakadabar.stack.frontend.elements.ZkClasses.Companion.zkClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.CoreStrings
import zakadabar.stack.frontend.util.escape
import zakadabar.stack.frontend.util.launch
import kotlin.reflect.KProperty

class Form : ZkForm<ShipDto>() {

    private lateinit var seas: List<SeaDto>
    var port: PortDto? = null

    override fun init() = launchBuild {

        title = dto.name
        super.init()

        coroutineScope {
            this.launch { seas = SeaDto.all() }
            this.launch { port = dto.port?.let { PortDto.read(it) } }
        }.join()

        + column(ZkFormStyles.contentContainer) {
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
            SpeedDto.all().map { it.id to it.description }
        }

        + select(dto::captain) {
            AccountPublicDto.all().map { it.id to it.fullName }
        }

        + div(ZkFormStyles.fieldLabel) { + Strings.seas }
        + PreSelect {
            getValue = { seas.firstOrNull { it.id == port?.id }?.id }
            options = { seas.map { it.id to it.name } }
            onSelected = { seaId ->
                launch {
                    val ports = PortDto.all().filter { it.sea == seaId } // this should be done with a query
                    val portElement = dto::port.find()
                    portElement.render(ports.sortedBy { it.name }.map { it.id to it.name })
                }
            }
        }
        + div(ZkFormStyles.fieldBottomBorder)

        + select(dto::port) {
            // ports are initialized by the PreSelect below
            emptyList()
        }

    }

    private fun description() = section(Strings.description, Strings.shipDescriptionExplanation, fieldGrid = false) {
        style { flexGrow = "1" }
        autoLabel = false
        + textarea(dto::description) cssClass zkClasses.h100
        autoLabel = true
    }

    private fun images() = section(Strings.images, fieldGrid = false) {
        + Images(this@Form, dto.id)
    }

    fun KProperty<RecordId<*>?>.find() = this@Form.find(this)

    fun ZkElement.find(prop: KProperty<RecordId<*>?>): ValidatedOptRecordSelect<*> {
        childElements.forEach {
            if (it is ValidatedOptRecordSelect<*> && it.prop.name == prop.name) return it
            if (it.childElements.isEmpty()) return@forEach
            it.find(prop)
        }
        throw NoSuchElementException(prop.name)
    }

    class PreSelect() : ZkElement(
        element = document.createElement("select") as HTMLElement
    ) {
        var sortOptions: Boolean = true
        var getValue: () -> Long? = { null }
        var onSelected: (RecordId<*>?) -> Unit = { }
        var options: suspend () -> List<Pair<RecordId<*>, String>> = { emptyList() }

        constructor(builder: PreSelect.() -> Unit) : this() {
            builder()
        }

        override fun init(): ZkElement {
            className = ZkFormStyles.select

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
