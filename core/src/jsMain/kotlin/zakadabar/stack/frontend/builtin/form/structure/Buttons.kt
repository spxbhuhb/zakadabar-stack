/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.structure

import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.FormMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.CoreStrings

class Buttons<T : DtoBase>(
    private val form: ZkForm<T>
) : ZkElement() {
    override fun init() = build {
        when (form.mode) {
            FormMode.Create ->
                + row(ZkFormStyles.buttons) {
                    + ZkButton(CoreStrings.back) { Application.back() } marginRight 10
                    + ZkButton(CoreStrings.save) { form.submit() }
                }

            FormMode.Read -> {
                + row(ZkFormStyles.buttons) {
                    + ZkButton(CoreStrings.back) { Application.back() } marginRight 10
                    form.openUpdate?.let { + ZkButton(CoreStrings.edit) { it(form.dto) } }
                }
            }
            FormMode.Update ->
                + row(ZkFormStyles.buttons) {
                    + ZkButton(CoreStrings.back) { Application.back() } marginRight 10
                    + ZkButton(CoreStrings.save) { form.submit() }
                }
            FormMode.Delete ->
                + row(ZkFormStyles.buttons) {
                    + ZkButton(CoreStrings.back) { Application.back() } marginRight 10
                    + ZkButton(CoreStrings.delete) { form.submit() }
                }
        }

    }
}