/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import kotlinx.serialization.json.Json
import zakadabar.core.data.EntityId
import zakadabar.core.data.create
import zakadabar.core.util.PublicApi
import zakadabar.core.util.default
import zakadabar.lib.blobs.data.create
import zakadabar.lib.schedule.data.Job

/**
 * Create a mail with a plain text or HTML content and queue it for sending.
 *
 * @param  recipients       Recipients of the mail, RFC822 syntax (comma separated addresses).
 * @param  subject          Subject of the mail.
 * @param  content          Content of the mail.
 * @param  contentMimeType  Mime type of the content, defaults to "text/plain".
 * @param  attachments      Attachments of the mail, if any: (data, name, mimeType).
 */
suspend fun sendMail(
    recipients: String,
    subject: String,
    content: String,
    contentMimeType: String = "text/plain",
    attachments: List<Triple<ByteArray,String,String>> = emptyList()
) {

    val id = buildMail(recipients, subject, content, contentMimeType, attachments)

    default<Job> {
        actionNamespace = Process.boNamespace
        actionType = Process::class.simpleName!!
        actionData = Json.encodeToString(Process.serializer(), Process(id))
    }.create()

}

/**
 * Create a mail with a plain text or HTML content.
 *
 * **This method does not send the mail**, use [sendMail] for that.
 *
 * @param  recipients       Recipients of the mail, RFC822 syntax (comma separated addresses).
 * @param  subject          Subject of the mail.
 * @param  content          Content of the mail.
 * @param  contentMimeType  Mime type of the content, defaults to "text/plain".
 * @param  attachments      Attachments of the mail, if any: (data, name, mimeType).
 */
@PublicApi
suspend fun buildMail(
    recipients: String,
    subject: String,
    content: String,
    contentMimeType: String = "text/plain",
    attachments: List<Triple<ByteArray,String,String>> = emptyList()
) : EntityId<Mail> {

    val mail = Mail.create {
        this.recipients = recipients
        this.subject = subject
    }

    MailPart.create(content.encodeToByteArray()) {
        id = EntityId()
        disposition = "body"
        name = "body"
        reference = mail.id
        mimeType = contentMimeType
    }

    attachments.forEach {
        MailPart.create(it.first) {
            id = EntityId()
            name = it.second
            reference = mail.id
            mimeType = it.third
        }
    }

    mail.update {
        status = MailStatus.SendWait
    }

    return mail.id
}