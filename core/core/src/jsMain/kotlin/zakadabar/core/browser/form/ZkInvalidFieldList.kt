/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.form

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.field.ZkFieldBase
import zakadabar.core.browser.util.marginBottom
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.localizedStrings

open class ZkInvalidFieldList : ZkElement() {

    override fun onCreate() {
        classList += ZkFormStyles.invalidFieldList
    }

    fun show(invalid: List<ZkFieldBase<*>>) {
        super.show()

        clear()

        + column {
            + div(ZkFormStyles.invalidFieldListInto) { + localizedStrings.invalidFieldsExplanation } marginBottom 8
            + div { + invalid.joinToString(", ") { it.labelText ?: it.propName } }
        }
    }

}