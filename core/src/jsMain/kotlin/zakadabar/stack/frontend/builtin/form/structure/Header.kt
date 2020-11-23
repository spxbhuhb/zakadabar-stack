/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.structure

import zakadabar.stack.frontend.builtin.form.FormClasses.Companion.formClasses
import zakadabar.stack.frontend.elements.ZkElement

class Header(
    private val title: String
) : ZkElement() {

    override fun init() = build {
        + div(formClasses.headerTitle) {
            + title
        }
    }

}