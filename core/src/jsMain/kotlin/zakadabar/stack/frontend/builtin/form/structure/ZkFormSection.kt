/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.structure

import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.elements.ZkElement

class ZkFormSection(
    private val title: String,
    private val summary: String,
    private val builder: ZkElement.() -> Unit
) : ZkElement() {

    override fun init() = build {
        className = ZkFormStyles.section

        + div(ZkFormStyles.sectionTitle) {
            + title
        }

        + div(ZkFormStyles.sectionSummary) {
            + summary
        }

        builder()
    }

}