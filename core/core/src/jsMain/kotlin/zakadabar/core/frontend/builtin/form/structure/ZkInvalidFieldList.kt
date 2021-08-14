/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.form.structure

import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.form.ZkFormStyles
import zakadabar.core.frontend.builtin.form.fields.ZkFieldBase
import zakadabar.core.frontend.util.marginBottom
import zakadabar.core.frontend.util.plusAssign
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