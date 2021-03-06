/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import zakadabar.demo.data.ship.SearchShipsQuery
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.ZkPage
import zakadabar.stack.frontend.builtin.form.ZkFormMode
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBar
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.io

object ShipSearch : ZkPage() {

    // This page shows a form for the query parameters and a table that
    // shows the query results. When the user clicks on the "Run Query"
    // button the runQuery method is called and the table is updated
    // with the results.

    val form = SearchForm(::runQuery)
    val table = SearchResult()

    // When you use "object" you have to initialize the DOM structure from
    // an init block and you have to override cleanup, so the underlying structure
    // won't be removed. The init and cleanup functions will be called again
    // and again and you don't want them to change the object.

    init {
        // "default" is an inline reified function. It gets the type of DTO to
        // create from the generic parameter of the form class, creates a
        // new instance, creates a schema and calls setDefaults of the schema
        // to set the default values.

        build {
            form.dto = default()
            form.mode = ZkFormMode.Query
            width("100%")

            + column {
                + ZkTitleBar(Strings.searchShips)
                + form
                + table
            }
        }
    }

    private fun runQuery(query: SearchShipsQuery) {
        io {
            table.setData(query.execute())
        }
    }

}