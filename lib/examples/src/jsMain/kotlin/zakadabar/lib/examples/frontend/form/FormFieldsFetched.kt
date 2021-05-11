/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import zakadabar.lib.examples.data.builtin.BuiltinDto
import zakadabar.lib.examples.frontend.crud.BuiltinForm
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.util.io

/**
 * This example shows all built in form fields.
 */
object FormFieldsFetched : ZkPage() {

    override fun onCreate() {
        super.onCreate()

        io {
            // Get your id in a more logical way, fetching all records just to get
            // an id is plain wrong. I just needed one make this example working.
            val id = BuiltinDto.all().first().id

            val form = BuiltinForm()
            form.dto = BuiltinDto.read(id)
            form.mode = ZkElementMode.Action

            + div(zkPageStyles.content) {
                + form
            }
        }
    }

}