/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.form.structure

import zakadabar.core.data.BaseBo
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.ZkElementMode
import zakadabar.core.frontend.builtin.button.ZkButton
import zakadabar.core.frontend.builtin.form.ZkForm
import zakadabar.core.frontend.builtin.form.ZkFormStyles
import zakadabar.core.frontend.builtin.modal.ZkConfirmDialog
import zakadabar.core.frontend.resources.ZkFlavour
import zakadabar.core.frontend.util.io
import zakadabar.core.resources.localizedStrings
import zakadabar.core.text.capitalized

open class ZkFormButtons<T : BaseBo>(
    private val form: ZkForm<T>,
    private val execute: () -> Unit = { form.submit() },
    private val submitLabel: String? = null
) : ZkElement() {

    override fun onCreate() {
        when (form.mode) {
            ZkElementMode.Create ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: localizedStrings.save, execute)
                    + progressIndicator()
                }

            ZkElementMode.Read -> {
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    form.openUpdate?.let { + submitButton(submitLabel ?: localizedStrings.edit) { it(form.bo) } }
                    + progressIndicator()
                }
            }
            ZkElementMode.Update ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: localizedStrings.save, execute)
                    + progressIndicator()
                }
            ZkElementMode.Delete ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: localizedStrings.delete, execute)
                    + progressIndicator()
                }
            ZkElementMode.Action -> {
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: localizedStrings.execute, execute)
                    + progressIndicator()
                }
            }
            ZkElementMode.Query -> {
            }
            ZkElementMode.Other -> {
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(submitLabel ?: localizedStrings.execute, execute)
                    + progressIndicator()
                }
            }
        }
    }

    open fun backButton() =
        ZkButton(localizedStrings.back, ZkFlavour.Secondary) {
            var touched = false
            form.fields.forEach { touched = touched || it.touched }

            if (touched) {
                io {
                    if (ZkConfirmDialog(localizedStrings.confirmation.capitalized(), localizedStrings.notSaved).run()) {
                        form.onBack()
                    }
                }
            } else {
                form.onBack()
            }

        } marginRight 10

    open fun submitButton(text: String, onClick: (() -> Unit)? = null) =
        ZkButton(text, onClick = onClick).also { form.submitButton = it } marginRight 10

    open fun progressIndicator() = ZkButton(localizedStrings.processing, ZkFlavour.Disabled).also {
        form.progressIndicator = it
        it.hide()
    }
}