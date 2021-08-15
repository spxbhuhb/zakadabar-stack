/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.demo.frontend

import zakadabar.lib.demo.frontend.pages.ContentNavTest
import zakadabar.lib.demo.frontend.pages.TestCrud
import zakadabar.core.browser.application.ZkAppRouting

class Routing : ZkAppRouting(DefaultLayout, Home) {

    init {
        + Home
        + TestCrud()
        + ContentNavTest()

        zakadabar.lib.accounts.frontend.install(this)
        zakadabar.lib.i18n.frontend.install(this)
        zakadabar.lib.content.frontend.browser.install(this)
    }

}