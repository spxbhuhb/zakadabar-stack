/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.translations

import hu.simplexion.rf.leltar.frontend.pages.roles.Roles
import zakadabar.stack.data.builtin.resources.TranslationDto
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<TranslationDto>() {

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = strings.roles
        crud = Roles

        + TranslationDto::id
        + TranslationDto::name
        + TranslationDto::value

        + actions()
    }

}