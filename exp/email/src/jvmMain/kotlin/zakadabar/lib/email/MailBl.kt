/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.setting.setting
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.module.module
import java.util.*
import javax.activation.DataHandler
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource

open class MailBl : EntityBusinessLogicBase<Mail>(
    boClass = Mail::class
) {

    private val settings by setting<MailSettings>()

    override val pa = MailPa()

    val partBl by module<MailPartBl>()

    override val authorizer by provider() // FIXME change default provider of MailBl

//    = SimpleRoleAuthorizer {
//        allReads = appRoles.securityOfficer
//        allWrites = appRoles.securityOfficer
//    }

    override val router = router {
        action(Process::class, ::process)
    }

    open fun process(executor: Executor, action: Process): ActionStatusBo {

        val mail = pa.read(action.mail)

        return try {

            if (mail.status != MailStatus.Sent) {
                send(mail)
                mail.status = MailStatus.Sent
            }

            ActionStatusBo(true)

        } catch (ex : Exception) {
            mail.status = MailStatus.RetryWait
            ex.printStackTrace() // FIXME log mail send errors through a logger
            ActionStatusBo(false)
        }
    }

    open fun send(mail : Mail) {

        val prop = Properties()

        prop["mail.smtp.auth"] = settings.auth
        prop["mail.smtp.starttls.enable"] = if (settings.tls) "true" else "false"
        prop["mail.smtp.host"] = settings.host
        prop["mail.smtp.port"] = settings.port
        prop["mail.smtp.protocol"] = settings.protocol
        prop["mail.debug"] = settings.debug

        val session = Session.getInstance(prop, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(settings.username, settings.password)
            }
        })

        val message = MimeMessage(session)
        message.setFrom(InternetAddress(settings.username))
        message.setRecipients(
            Message.RecipientType.TO, InternetAddress.parse(mail.recipients)
        )

        message.setSubject(mail.subject, "utf-8")

        val multipart = MimeMultipart()

        partBl.byReference(mail.id).forEach { part ->

            val bodyPart = MimeBodyPart()
            val content = partBl.pa.readContent(part.id)

            if (part.disposition == "body") {
                when (part.mimeType) {
                    "text/plain" -> bodyPart.setText(content.decodeToString(), "UTF-8")
                    "text/html" -> bodyPart.setContent(content.decodeToString(), "text/html; charset=UTF-8")
                    else -> throw IllegalStateException("body mime type (${part.mimeType} must be 'text/plain' or 'text/html}")
                }
                bodyPart.disposition = Part.INLINE
            } else {
                bodyPart.dataHandler = DataHandler(ByteArrayDataSource(content, part.mimeType))
                bodyPart.fileName = part.name
                bodyPart.disposition = part.disposition.ifEmpty { Part.ATTACHMENT }
            }

            multipart.addBodyPart(bodyPart)
        }

        message.setContent(multipart)

        Transport.send(message)
    }


}