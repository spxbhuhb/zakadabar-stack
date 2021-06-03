/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.lib.examples.frontend.crud.BuiltinForm
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.util.io
import zakadabar.stack.util.PublicApi

@PublicApi // example code
class FormFieldsFetched(
    private val entityId : EntityId<BuiltinBo>
) : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        io {
            val form = BuiltinForm()
            form.bo = BuiltinBo.read(entityId)
            form.mode = ZkElementMode.Update

            + div(zkPageStyles.content) {
                + form
            }
        }
    }

}