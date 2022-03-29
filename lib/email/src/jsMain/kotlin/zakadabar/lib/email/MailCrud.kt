/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.application.target
import zakadabar.core.browser.crud.ZkCrudTarget
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.modal.ZkModalBase
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.util.io
import zakadabar.core.data.BaseBo
import zakadabar.core.resource.localized
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.lib.email.Mail


class MailCrud : ZkCrudTarget<Mail>() {
    init {
        companion = Mail.Companion
        boClass = Mail::class
        editorClass = MailForm::class
        tableClass = MailTable::class
    }
}

class MailForm : ZkForm<Mail>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<MailForm>()) {
            + section {
                + bo::id
                + bo::createdBy query { AccountPrivateBo.all().by { it.fullName } }
                + bo::createdAt
                + bo::status
                + bo::recipients
                + bo::sentAt
                + bo::sensitive
            }
        }
    }
}

class MailTable : ZkTable<Mail>() {

    override fun onConfigure() {

        crud = target<MailCrud>()

        titleText = localized<MailTable>()

        add = true
        search = true
        export = true
        
        + Mail::id
        // Mail::createdBy // record id and opt record id is not supported yet 
        + Mail::createdAt
        + Mail::status
        + Mail::recipients
        + Mail::sentAt
        + Mail::sensitive
        
        + actions()
    }

    override fun onAddRow() {
        SendMailDialog().launch()
    }

}

class MailData(
    var recipient: String,
    var subject: String,
    var content: String
) : BaseBo

open class SendMailDialog : ZkModalBase<Boolean>() {

    override var titleText : String? = "Send Mail"
    override var addButtons = false

    override fun buildContent() {
        + SendMailForm().apply {
            onBack = { io { channel.send(false) } }
            mode = ZkElementMode.Create
            bo = MailData("", "", "")
        }
    }

}

class SendMailForm : ZkForm<MailData>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<SendMailForm>()) {
            + section {
                + bo::recipient
                + bo::subject
                + bo::content
            }
        }
    }

    override fun submit() {

        // override submit as actually don't want to submit this form
        // we want to send an e-mail

        io {
            if (! validate(true)) return@io
            sendMail(bo.recipient, bo.subject, bo.content)
            onBack()
        }
    }
}

