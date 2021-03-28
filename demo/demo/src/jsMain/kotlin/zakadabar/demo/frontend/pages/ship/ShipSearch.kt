/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import zakadabar.demo.data.ship.SearchShipsQuery
import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.layout.ZkLayoutStyles.grow
import zakadabar.stack.frontend.builtin.layout.ZkLayoutStyles.h100
import zakadabar.stack.frontend.builtin.layout.ZkLayoutStyles.w100
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBar
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign

object ShipSearch : ZkPage() {

    // This page shows a form for the query parameters and a table that
    // shows the query results. When the user clicks on the "Run Query"
    // button the runQuery method is called and the table is updated
    // with the results.

    val form = SearchForm(::runQuery)
    val table = SearchResult()

    override fun onCreate() {

        classList += w100
        classList += grow

        // "default" is an inline reified function. It gets the type of DTO to
        // create from the generic parameter of the form class, creates a
        // new instance, creates a schema and calls setDefaults of the schema
        // to set the default values.

        form.dto = default()
        form.mode = ZkElementMode.Query

        + column(h100) {
            + ZkTitleBar(Strings.searchShips)

            + div(grow) {
                buildElement.style.padding = "8px"
                buildElement.style.backgroundColor = ZkColors.Gray.c100

                + form marginBottom 8
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