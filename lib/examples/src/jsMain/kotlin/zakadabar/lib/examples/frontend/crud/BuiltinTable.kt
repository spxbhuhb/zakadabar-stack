/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import zakadabar.lib.examples.data.builtin.BuiltinDto
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.table.ZkTable

class BuiltinTable : ZkTable<BuiltinDto>() {

    override fun onConfigure() {

        // Set the title of the table.

        titleText = stringStore["builtin"]

        // Enable the add button (plus icon in the header).
        // Enable search (input field in the header).
        // Enable CSV export (download icon in the header).

        add = true
        search = true
        export = true

        // Add columns to the table. Column types are automatically
        // derived from the property type.

        + BuiltinDto::id
        + BuiltinDto::booleanValue
        + BuiltinDto::doubleValue
        + BuiltinDto::enumSelectValue
        + BuiltinDto::instantValue
        + BuiltinDto::optInstantValue
        + BuiltinDto::stringValue
        + BuiltinDto::uuidValue
        + BuiltinDto::optUuidValue

        // Add a custom column

        + custom {
            label = stringStore["custom"]
            render = { row ->
                if ((row.id.toLong() % 2L) == 0L) {
                    + stringStore["odd"]
                } else {
                    + stringStore["even"]
                }
            }
        }

        // Add an actions column, this will contain a "Details" link
        // that calls openUpdate of the crud.

        + actions()
    }

}