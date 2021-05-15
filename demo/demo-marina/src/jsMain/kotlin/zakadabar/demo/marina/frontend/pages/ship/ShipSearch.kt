/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.frontend.pages.ship

import zakadabar.demo.marina.data.ship.SearchShipsQuery
import zakadabar.demo.marina.resources.Strings
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.io

object ShipSearch : ZkPage() {

    // This page shows a form for the query parameters and a table that
    // shows the query results. When the user clicks on the "Run Query"
    // button the runQuery method is called and the table is updated
    // with the results.

    val form = SearchForm(ShipSearch::runQuery)
    val table = SearchResult()

    override fun onCreate() {
        super.onCreate()

        // "default" is an inline reified function. It gets the type of DTO to
        // create from the generic parameter of the form class, creates a
        // new instance, creates a schema and calls setDefaults of the schema
        // to set the default values.

        form.dto = default()
        form.mode = ZkElementMode.Query


        + form marginBottom 8
        + table

        titleText = Strings.searchShips

    }

    private fun runQuery(query: SearchShipsQuery) {
        io {
            table.setData(query.execute())
        }
    }

}