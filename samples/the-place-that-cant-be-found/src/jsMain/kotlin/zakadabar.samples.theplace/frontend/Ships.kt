/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import zakadabar.samples.theplace.ThePlace
import zakadabar.samples.theplace.data.ShipDto
import zakadabar.samples.theplace.frontend.form.ValidatedForm
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.builtin.util.NYI
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement.Companion.buildNew
import zakadabar.stack.frontend.elements.ZkElement.Companion.launchBuildNew
import zakadabar.stack.frontend.util.launch

object Ships : ZkCrud(ThePlace.shid, "/ships") {

    override fun all() = launchBuildNew {
        val ships = ShipDto.all()
        + SimpleButton("new") { Ships.openCreate() }
        ships.forEach { + it.name }
    }

    override fun create() = buildNew {
        + "New Ship"
        + ShipForm(ShipDto(0, "", ""))
    }

    override fun read(recordId: Long) = launchBuildNew {

        + ShipForm(ShipDto.read(recordId))

        + SimpleButton("back") { Application.back() }
        + SimpleButton("edit") { openUpdate(recordId) }

    }

    override fun update(recordId: Long) = launchBuildNew {

        + ShipForm(ShipDto.read(recordId))

        + SimpleButton("back") { Application.back() }
        + SimpleButton("submit") { zkElement[ShipForm::class].submit() }

    }

    override fun delete(recordId: Long) = launchBuildNew {

        val dto = ShipDto.read(recordId)

        + ShipForm(dto)

        + SimpleButton("back") { Application.back() }
        + SimpleButton("submit") {
            launch {
                dto.delete()
                Application.back()
            }
        }

    }

    class ShipForm(dto: ShipDto) : ValidatedForm<ShipDto>(dto) {

        override fun init(): ValidatedForm<ShipDto> {

            this build {
                + NYI("header")
                + NYI("frame")
            }

            launch {
                setData(ShipDto.all())
            }

            this build {
                + dto::name

                val a0 = dto::speed
                a0.get()

                // definicio
                val a1 = ShipDto::speed

                // runtime
                val list = emptyList<ShipDto>()

                list.forEach {
                    + a1.get(it)
                }
            }

            return this
        }

        private fun setData(all: List<ShipDto>) {
            TODO("Not yet implemented")
        }

        fun submit() {

        }
    }

}

