/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import zakadabar.lib.examples.data.builtin.BuiltinDto
import zakadabar.stack.frontend.application.ZkApplication.t
import zakadabar.stack.frontend.builtin.table.ZkTable

class BuiltinTable : ZkTable<zakadabar.lib.examples.data.builtin.BuiltinDto>() {

    override fun onConfigure() {

        // Sets the CRUD the table uses. When set add and double click on rows
        // calls this crud to create a new record or open the record for update.

        crud = BuiltinCrud

        // Set the title of the table.

        titleText = t("builtin")

        // Enable the add button (plus icon in the header).
        // Enable search (input field in the header).
        // Enable CSV export (download icon in the header).

        add = true
        search = true
        export = true

        // Add columns to the table. Column types are automatically
        // derived from the property type.

        + zakadabar.lib.examples.data.builtin.BuiltinDto::id
        + zakadabar.lib.examples.data.builtin.BuiltinDto::booleanValue
        + zakadabar.lib.examples.data.builtin.BuiltinDto::doubleValue
        + zakadabar.lib.examples.data.builtin.BuiltinDto::enumSelectValue
        + zakadabar.lib.examples.data.builtin.BuiltinDto::instantValue
        + zakadabar.lib.examples.data.builtin.BuiltinDto::optInstantValue
        + zakadabar.lib.examples.data.builtin.BuiltinDto::stringValue
        + zakadabar.lib.examples.data.builtin.BuiltinDto::uuidValue
        + zakadabar.lib.examples.data.builtin.BuiltinDto::optUuidValue

        // Add a custom column

        + custom {
            label = t("custom")
            render = { row ->
                if ((row.id.toLong() % 2L) == 0L) {
                    + t("odd")
                } else {
                    + t("even")
                }
            }
        }

        // Add an actions column, this will contain a "Details" link
        // that calls openUpdate of the crud.

        + actions()
    }

}