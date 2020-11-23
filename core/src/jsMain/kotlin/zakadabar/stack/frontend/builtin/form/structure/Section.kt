/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.structure

import zakadabar.stack.frontend.builtin.form.FormClasses.Companion.formClasses
import zakadabar.stack.frontend.elements.ZkBuilder
import zakadabar.stack.frontend.elements.ZkElement

class Section(
    private val title: String,
    private val summary: String,
    private val builder: ZkBuilder.() -> Unit
) : ZkElement() {

    override fun init() = build {
        className = formClasses.section

        + div(formClasses.sectionTitle) {
            + title
        }

        + div(formClasses.sectionSummary) {
            + summary
        }

        builder()
    }

}