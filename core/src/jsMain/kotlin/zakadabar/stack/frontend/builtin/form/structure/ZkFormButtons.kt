/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.structure

import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.modal.ZkConfirmDialog
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.util.io

open class ZkFormButtons<T : DtoBase>(
    private val form: ZkForm<T>,
    private val execute: () -> Unit = { form.submit() },
    private val submitLabel: String? = null
) : ZkElement() {

    override fun onCreate() {
        when (form.mode) {
            ZkElementMode.Create ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: strings.save, execute)
                    + progressIndicator()
                }

            ZkElementMode.Read -> {
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    form.openUpdate?.let { + submitButton(submitLabel ?: strings.edit) { it(form.dto) } }
                    + progressIndicator()
                }
            }
            ZkElementMode.Update ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: strings.save, execute)
                    + progressIndicator()
                }
            ZkElementMode.Delete ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: strings.delete, execute)
                    + progressIndicator()
                }
            ZkElementMode.Action -> {
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: strings.execute, execute)
                    + progressIndicator()
                }
            }
            ZkElementMode.Query -> {
            }
            ZkElementMode.Other -> {
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: strings.execute, execute)
                    + progressIndicator()
                }
            }
        }
    }

    open fun backButton() =
        ZkButton(strings.back, ZkFlavour.Secondary) {
            var touched = false
            form.fields.forEach { touched = touched || it.touched }

            if (touched) {
                io {
                    if (ZkConfirmDialog(strings.confirmation.capitalize(), strings.notSaved).run()) {
                        ZkApplication.back()
                    }
                }
            } else {
                ZkApplication.back()
            }

        } marginRight 10

    open fun submitButton(text: String, onClick: (() -> Unit)? = null) =
        ZkButton(text, onClick = onClick).also { form.submitButton = it } marginRight 10

    open fun progressIndicator() = ZkButton(strings.processing, ZkFlavour.Disabled).also {
        form.progressIndicator = it
        it.hide()
    }
}