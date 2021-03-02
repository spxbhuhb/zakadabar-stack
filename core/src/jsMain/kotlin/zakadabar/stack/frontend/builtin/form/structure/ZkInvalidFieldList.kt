/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.structure

import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.fields.ZkFieldBase
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.marginBottom
import zakadabar.stack.frontend.elements.plusAssign
import zakadabar.stack.frontend.resources.CoreStrings

open class ZkInvalidFieldList : ZkElement() {

    override fun init(): ZkInvalidFieldList {
        classList += ZkFormStyles.invalidFieldList
        return this
    }

    fun show(invalid: List<ZkFieldBase<*, *>>) {
        super.show()

        clear()

        + column {
            + div(ZkFormStyles.invalidFieldListInto) { + CoreStrings.invalidFieldsExplanation } marginBottom 8
            + div { + invalid.joinToString(", ") { it.label } }
        }
    }

}