/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.structure

import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkBuiltinStrings.Companion.builtin
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.misc.processing.ZkProcessing
import zakadabar.stack.frontend.builtin.modal.ZkConfirmDialog
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
                    + submitButton(submitLabel ?: builtin.save, execute)
                    + progressIndicator()
                }

            ZkElementMode.Read -> {
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    form.openUpdate?.let { + submitButton(submitLabel ?: builtin.edit) { it(form.dto) } }
                    + progressIndicator()
                }
            }
            ZkElementMode.Update ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: builtin.save, execute)
                    + progressIndicator()
                }
            ZkElementMode.Delete ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: builtin.delete, execute)
                    + progressIndicator()
                }
            ZkElementMode.Action -> {
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: builtin.execute, execute)
                    + progressIndicator()
                }
            }
            ZkElementMode.Query -> {
            }
            ZkElementMode.Other -> {
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: builtin.execute, execute)
                    + progressIndicator()
                }
            }
        }
    }

    open fun backButton() =
        ZkButton(builtin.back) {
            var touched = false
            form.fields.forEach { touched = touched || it.touched }

            if (touched) {
                io {
                    if (ZkConfirmDialog(builtin.confirmation.capitalize(), builtin.notSaved).run()) {
                        ZkApplication.back()
                    }
                }
            } else {
                ZkApplication.back()
            }

        } marginRight 10

    open fun submitButton(text: String, onClick: (() -> Unit)? = null) =
        ZkButton(text, onClick).also { form.submitButton = it } marginRight 10

    open fun progressIndicator() = ZkProcessing().also {
        form.progressIndicator = it
        it.hide()
    }
}