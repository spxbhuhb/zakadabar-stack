/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.form

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.form.ZkFormStyles
import zakadabar.core.resource.css.ZkCssStyleRule
import zakadabar.core.resource.theme
import zakadabar.core.browser.util.marginBottom
import zakadabar.core.browser.util.plusAssign

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