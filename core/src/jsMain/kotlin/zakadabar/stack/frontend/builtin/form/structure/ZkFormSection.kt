/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.structure

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.util.plusAssign

class ZkFormSection(
    private val title: String? = null,
    private val summary: String? = null,
    private val css: String? = null,
    private val builder: ZkElement.() -> Unit
) : ZkElement() {

    override fun onCreate() {
        className = ZkFormStyles.section

        css?.let { classList += it }

        if (title != null) {
            + div(ZkFormStyles.sectionTitle) {
                + title
            }
        }

        if (summary != null) {
            + div(ZkFormStyles.sectionSummary) {
                + summary
            }
        }

        builder()
    }

}