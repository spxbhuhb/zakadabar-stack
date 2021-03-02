/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.structure

import kotlinx.browser.window
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormMode
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.CoreStrings

open class ZkFormButtons<T : DtoBase>(
    private val form: ZkForm<T>
) : ZkElement() {
    override fun init() = build {
        when (form.mode) {
            ZkFormMode.Create ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(CoreStrings.save) { form.submit() }
                }

            ZkFormMode.Read -> {
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    form.openUpdate?.let { + submitButton(CoreStrings.edit) { it(form.dto) } }
                }
            }
            ZkFormMode.Update ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(CoreStrings.save) { form.submit() }
                }
            ZkFormMode.Delete ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(CoreStrings.delete) { form.submit() }
                }
            ZkFormMode.Action -> {
            }
            ZkFormMode.Query -> {
            }
        }

    }

    open fun backButton() =
        ZkButton(CoreStrings.back) {
            var touched = false
            form.fields.forEach { touched = touched || it.touched }

            if (touched) {
                // TODO replace this with a styled dialog
                if (window.confirm(CoreStrings.notSaved)) Application.back()
            } else {
                Application.back()
            }

        } marginRight 10

    open fun submitButton(text: String, onClick: (() -> Unit)? = null) =
        ZkButton(text, onClick).also { form.submitButton = it }

}