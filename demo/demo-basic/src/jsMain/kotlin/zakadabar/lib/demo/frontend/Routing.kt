/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.demo.frontend

import zakadabar.lib.demo.frontend.pages.DemoCrud
import zakadabar.lib.examples.frontend.crud.BuiltinCrud
import zakadabar.lib.examples.frontend.crud.ExampleReferenceCrud
import zakadabar.core.browser.application.ZkAppRouting

class Routing : ZkAppRouting(DefaultLayout, Home) {

    init {
        + Home
        + DemoCrud()
        + BuiltinCrud()
        + ExampleReferenceCrud()

        zakadabar.lib.accounts.frontend.install(this)
        zakadabar.lib.i18n.frontend.install(this)
    }

}