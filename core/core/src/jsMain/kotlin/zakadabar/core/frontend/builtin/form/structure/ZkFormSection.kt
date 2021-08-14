/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.form.structure

import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.form.ZkFormStyles
import zakadabar.core.frontend.resources.css.ZkCssStyleRule
import zakadabar.core.frontend.resources.theme
import zakadabar.core.frontend.util.marginBottom
import zakadabar.core.frontend.util.plusAssign

class ZkFormSection(
    private val title: String? = null,
    private val summary: String? = null,
    private val css: ZkCssStyleRule? = null,
    private val builder: ZkElement.() -> Unit
) : ZkElement() {

    override fun onCreate() {
        + ZkFormStyles.section

        classList += css

        if (title != null) {
            + div(ZkFormStyles.sectionTitle) {
                + title
            } marginBottom (if (summary == null) theme.spacingStep / 2 else 0)
        }

        if (summary != null) {
            + div(ZkFormStyles.sectionSummary) {
                + summary
            } marginBottom theme.spacingStep / 2
        }

        builder()
    }

}