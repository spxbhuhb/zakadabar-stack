/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.locales

import hu.simplexion.rf.leltar.frontend.pages.roles.Roles
import zakadabar.stack.data.builtin.account.RoleDto
import zakadabar.stack.data.builtin.resources.LocaleDto
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<LocaleDto>() {

    private val roles by preload { RoleDto.allAsMap() }

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = stringStore.locales
        crud = Roles

        + LocaleDto::id
        + LocaleDto::name
        + LocaleDto::description

        + actions()
    }

}