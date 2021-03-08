/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import zakadabar.demo.data.PortDto
import zakadabar.demo.data.SeaDto
import zakadabar.demo.data.SpeedDto
import zakadabar.demo.data.ship.ShipDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.data.builtin.AccountPublicDto
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.fields.ZkImagesField
import zakadabar.stack.frontend.builtin.form.fields.ZkRecordSelectFilter
import zakadabar.stack.frontend.util.io

class Form : ZkForm<ShipDto>() {

    private lateinit var seas: List<SeaDto>
    var port: PortDto? = null

    override fun onCreate() {
        io {
            coroutineScope {
                this.launch { seas = SeaDto.all() }
                this.launch { port = dto.port?.let { PortDto.read(it) } }
            }.join()

            build(dto.name, Strings.ship) {

                + row {
                    + basics() marginRight 8
                    + description()
                }

                + images()

            }
        }
    }

    private fun basics() = section(Strings.basics, Strings.shipBasicsExplanation) {

        + dto::id
        + dto::name
        + dto::hasPirateFlag

        + select(dto::speed) {
            SpeedDto.all().map { it.id to it.description }
        }

        + select(dto::captain) {
            AccountPublicDto.all().map { it.id to it.fullName }
        }

        portSelect()
    }

    private fun portSelect() {

        // Create the select for "port" and initialize it if we have a value.
        // This does not add the select to the form yet, that's at the
        // end of the function.

        val select = select(dto::port) {
            val portId = dto::port.get() ?: return@select emptyList()
            val port = PortDto.read(portId)
            PortDto.all().filter { it.sea == port.sea }.map { it.id to it.name }
        }

        // Create a filter which shows "seas". When the filter changes we have
        // to update the "port" select we've just created above.

        + ZkRecordSelectFilter(
            this@Form,
            label = Strings.seas, // as this form field is not bound to a DTO field it needs a label
            getValue = { seas.firstOrNull { it.id == port?.id }?.id }, // get the selected "sea" from "port" (if we have a port)
            options = suspend { seas.map { it.id to it.name } }, // options for the "sea" select
            onSelected = { value ->
                io {
                    val seaId = value?.first
                    val ports = PortDto.all().filter { it.sea == seaId }

                    val portId = dto::port.get()
                    if (portId != null) {
                        val port = PortDto.read(portId)
                        if (port.sea != seaId) dto::port.set(null)
                    }

                    select
                        .apply { items = ports.sortedBy { it.name }.map { it.id to it.name } }
                        .render(dto.port)
                }
            }
        )

        // add the port select to the form

        + select
    }

    private fun description() = section(Strings.description, Strings.shipDescriptionExplanation, fieldGrid = false) {
        style { flexGrow = "1" }
        autoLabel = false
        + textarea(dto::description)
        autoLabel = true
    }

    private fun images() = section(Strings.images, Strings.shipImagesExplanation, fieldGrid = false) {
        + ZkImagesField(this@Form, dto.id, imageCountMax = 1)
    }


}
