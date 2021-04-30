/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.lib.frontend.form

import zakadabar.demo.lib.data.builtin.BuiltinDto
import zakadabar.demo.lib.frontend.crud.BuiltinForm
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
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

            + div(ZkPageStyles.content) {
                + form
            }
        }
    }

}