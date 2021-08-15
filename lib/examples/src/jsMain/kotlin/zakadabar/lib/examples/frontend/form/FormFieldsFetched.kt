/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.lib.examples.frontend.crud.BuiltinForm
import zakadabar.core.data.EntityId
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.browser.util.io
import zakadabar.core.util.PublicApi

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