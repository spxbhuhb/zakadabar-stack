/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.structure

import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.form.FormMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.elements.ZkElement

class Buttons<T : RecordDto<T>>(
    private val form: ZkForm<T>
) : ZkElement() {
    override fun init() = build {
        when (form.mode) {
            FormMode.Create ->
                + row {
                    + SimpleButton("back") { Application.back() }
                    + SimpleButton("save") { form.submit() }
                }

            FormMode.Read -> {
                + row {
                    + SimpleButton("back") { Application.back() }
                    + SimpleButton("edit") { form.crud?.openUpdate(form.dto.id) }
                }
            }
            FormMode.Update ->
                + row {
                    + SimpleButton("back") { Application.back() }
                    + SimpleButton("save") { form.submit() }
                }
            FormMode.Delete ->
                + row {
                    + SimpleButton("back") { Application.back() }
                    + SimpleButton("delete") { form.submit() }
                }
        }

    }
}