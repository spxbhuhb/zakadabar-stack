/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend

import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.page.ZkPage
import zakadabar.lib.schedule.JobSummaryTable

object JobSummary : ZkPage() {

    override fun onCreate() {
        + div(zkLayoutStyles.p1) {
            + JobSummaryTable()
        }
    }

}