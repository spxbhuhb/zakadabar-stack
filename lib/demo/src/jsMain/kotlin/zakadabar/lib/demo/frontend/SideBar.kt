/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend

import zakadabar.lib.accounts.frontend.accounts.Accounts
import zakadabar.lib.accounts.frontend.roles.Roles
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBar

class SideBar : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        + group(translate<Accounts>()) {
            + item<Accounts>()
            + item<Roles>()
        }

    }

}



