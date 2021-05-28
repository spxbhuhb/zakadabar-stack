/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.locales

import zakadabar.stack.data.builtin.resources.LocaleBo
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<LocaleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = stringStore.locales
        crud = Locales

        + LocaleBo::id
        + LocaleBo::name
        + LocaleBo::description

        + actions()
    }

}