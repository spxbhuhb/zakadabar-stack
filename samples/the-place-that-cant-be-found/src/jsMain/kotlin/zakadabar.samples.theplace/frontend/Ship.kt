/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.frontend.builtin.navigation.NavState
import zakadabar.stack.frontend.builtin.navigation.Route
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.util.launch

object Ship : Route() {

    lateinit var dto: EntityRecordDto

    override fun element(newState: NavState): ComplexElement {
        return read()
    }

    private fun read(): ComplexElement {

        val ce = ComplexElement()

        launch {
            dto = EntityRecordDto.read(12)
            ce build {
                + dto.name
            }
        }

        return ce
    }

}