/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.form.modal

import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.SmallExampleBo
import zakadabar.cookbook.resource.strings
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.button.buttonPrimary
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.modal.ZkModalBase
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.browser.util.io
import zakadabar.core.util.default

open class FormInModalOpen : ZkElement() {

    override fun onCreate() {
        super.onCreate()
        + buttonPrimary(strings.open) {
            FormDialog().launch()
        }
    }
}

open class FormDialog : ZkModalBase<Boolean>() {

    override var titleText : String? = strings.formInModal
    override var addButtons = false

    override fun buildContent() {

        // Set onBack, so it will close the dialog, instead of going back.
        // Also, set onSuccess, so the dialog will be closed
        // when the record create is a success. OnSuccess is not part
        // of ZkForm, I added it to FormInModal.

        + FormInModal().apply {
            onBack = { io { channel.send(false) } }
            onSuccess = {
                io { channel.send(true) }
                toastSuccess { "Submit success: ${bo.stringValue} ${bo.enumSelectValue}" }
            }
        }
    }

}

class FormInModal: ZkForm<SmallExampleBo>() {

    lateinit var onSuccess : (bo : SmallExampleBo) -> Unit
    override var mode = ZkElementMode.Other
    override var bo : SmallExampleBo = default()
    override var schema = bo.schema()

    override fun onCreate() {
        super.onCreate()

        + cookbookStyles.inlineForm

        build("", "", addButtons = true) {
            + section {
                + bo::stringValue
                + bo::enumSelectValue
            }
        }
    }

    override fun submit() {

        // override submit as actually don't want to submit this form
        // this way the validation is active bu there is no communication
        // with the server

        io {
            println(schema.validate(mode == ZkElementMode.Create).dump())
            if (! validate(true)) return@io
            onSuccess(bo)
        }
    }
}