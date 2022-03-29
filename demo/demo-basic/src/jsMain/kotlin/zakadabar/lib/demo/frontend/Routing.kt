/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.demo.frontend

import zakadabar.core.browser.application.ZkAppRouting
import zakadabar.lib.demo.frontend.pages.DemoCrud
import zakadabar.lib.email.MailCrud
import zakadabar.lib.examples.frontend.crud.BuiltinCrud
import zakadabar.lib.examples.frontend.crud.ExampleReferenceCrud
import zakadabar.lib.schedule.JobCrud

class Routing : ZkAppRouting(DefaultLayout, Home) {

    init {
        + Home
        + DemoCrud()
        + BuiltinCrud()
        + ExampleReferenceCrud()
        + MailCrud()
        + JobCrud()

        zakadabar.lib.accounts.browser.install(this)
        zakadabar.lib.i18n.browser.install(this)
    }

}