/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.form.structure

import zakadabar.demo.frontend.form.ValidatedForm
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.elements.ZkElement

class Buttons<T : RecordDto<T>>(
    private val form: ValidatedForm<T>
) : ZkElement() {
    override fun init() = build {
        when (form.mode) {
            ValidatedForm.Mode.Create -> {
                + SimpleButton("back") { Application.back() }
                + SimpleButton("save") { form.submit() }
            }
            ValidatedForm.Mode.Read -> {
                + SimpleButton("back") { Application.back() }
                + SimpleButton("edit") { form.crud.openUpdate(form.dto.id) }
            }
            ValidatedForm.Mode.Update -> {
                + SimpleButton("back") { Application.back() }
                + SimpleButton("save") { form.submit() }
            }
            ValidatedForm.Mode.Delete -> {
                + SimpleButton("back") { Application.back() }
                + SimpleButton("delete") { form.submit() }
            }
        }

    }
}