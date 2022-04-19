/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.demo.frontend

import zakadabar.cookbook.sqlite.bundle.ExampleBundleCrud
import zakadabar.core.browser.application.ZkAppRouting
import zakadabar.lib.demo.frontend.pages.DemoCrud
import zakadabar.lib.demo.frontend.pages.TableSandbox

class Routing : ZkAppRouting(DefaultLayout, Home) {

    init {
        + Home
        + DemoCrud()

        + TableSandbox<Int>()
        + ExampleBundleCrud()

        zakadabar.lib.accounts.browser.install(this)
        zakadabar.lib.i18n.browser.install(this)
    }

}