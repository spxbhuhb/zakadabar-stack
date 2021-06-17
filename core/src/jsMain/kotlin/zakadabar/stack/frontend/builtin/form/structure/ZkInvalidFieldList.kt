/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.structure

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.fields.ZkFieldBase
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.frontend.util.plusAssign
import zakadabar.stack.resources.localizedStrings

open class ZkInvalidFieldList : ZkElement() {

    override fun onCreate() {
        classList += ZkFormStyles.invalidFieldList
    }

    fun show(invalid: List<ZkFieldBase<*, *>>) {
        super.show()

        clear()

        + column {
            + div(ZkFormStyles.invalidFieldListInto) { + localizedStrings.invalidFieldsExplanation } marginBottom 8
            + div { + invalid.joinToString(", ") { it.labelText ?: it.propName } }
        }
    }

}