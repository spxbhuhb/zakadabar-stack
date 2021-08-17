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
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.page.ZkPage
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.browser.toast.toastDanger
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.resource.css.OverflowY
import zakadabar.core.resource.css.Position
import zakadabar.core.resource.theme
import zakadabar.core.browser.util.default
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.log
import zakadabar.core.browser.util.plusAssign

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