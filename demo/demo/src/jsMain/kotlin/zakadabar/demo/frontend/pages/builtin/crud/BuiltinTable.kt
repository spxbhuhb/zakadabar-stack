/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.builtin.crud

import zakadabar.demo.data.builtin.BuiltinDto
import zakadabar.demo.data.speed.SpeedDto
import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkApplication.t
import zakadabar.stack.frontend.builtin.table.ZkTable

class BuiltinTable : ZkTable<BuiltinDto>() {

    override fun onConfigure() {
        title = t("builtin")

        add = true
        search = true
        export = true

        crud = BuiltinCrud

        + BuiltinDto::id
        + BuiltinDto::booleanValue
        + BuiltinDto::doubleValue
        + BuiltinDto::enumSelectValue
        + BuiltinDto::instantValue
        + BuiltinDto::stringValue
        + BuiltinDto::uuidValue
        + BuiltinDto::optUuidValue

        + custom {
            label = t("custom")
            render = { row ->
                if ((row.id % 2L) == 0L) {
                    + t("odd")
                } else {
                    + t("even")
                }
            }
        }
    }

}