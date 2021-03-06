/*
 * Copyright © 2020, Simplexion, Hungary. All rights reserved.
 *
 * This source code contains proprietary information; it is provided under a
 * license agreement containing restrictions on use and distribution and are
 * also protected by copyright, patent, and other intellectual and industrial
 * property laws.
 */
package zakadabar.lib.examples.frontend.query

import zakadabar.lib.examples.data.builtin.ExampleQuery
import zakadabar.lib.examples.resources.strings
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.builtin.toast.toastDanger
import zakadabar.stack.frontend.builtin.toast.toastSuccess
import zakadabar.stack.frontend.resources.css.OverflowY
import zakadabar.stack.frontend.resources.css.Position
import zakadabar.stack.frontend.resources.theme
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.log
import zakadabar.stack.frontend.util.plusAssign

object QueryPage : ZkPage(css = zkPageStyles.fixed) {

    val form = QueryForm(QueryPage::runQuery)

    val table = ResultTable()

    override fun onConfigure() {
        setAppTitle = false
    }

    override fun onCreate() {
        super.onCreate()

        classList += zkLayoutStyles.grid
        gridTemplateRows = "max-content 1fr"

        form.bo = default()
        form.mode = ZkElementMode.Query

        + form marginBottom (theme.spacingStep)
        + div {
            + Position.relative
            + OverflowY.hidden
            + table
        }

    }

    private fun runQuery(query: ExampleQuery) {
        io {
            try {
                table.setData(query.execute())
                toastSuccess { strings.querySuccess }
            } catch (ex: Exception) {
                toastDanger { strings.queryFail }
                log(ex)
            }
        }
    }

}