/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.form

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.marginBottom
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.css.ZkCssStyleRule
import zakadabar.core.resource.theme

class ZkFormSection(
    val title: String? = null,
    val summary: String? = null,
    val css: ZkCssStyleRule? = null,
    val builder: ZkElement.() -> Unit
) : ZkElement() {

    override fun onCreate() {
        + zkFormStyles.section

        classList += css

        if (title != null) {
            + div(zkFormStyles.sectionTitle) {
                + title
            } marginBottom (if (summary == null) theme.spacingStep / 2 else 0)
        }

        if (summary != null) {
            + div(zkFormStyles.sectionSummary) {
                + summary
            } marginBottom theme.spacingStep / 2
        }

        builder()
    }

}