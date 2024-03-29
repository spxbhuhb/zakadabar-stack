/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.crud.basic

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.resource.css.px

class BasicCrud : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        height = 400.px
        + zkLayoutStyles.fixBorder

        + BasicInlineCrud().apply { openAll() }
    }

}