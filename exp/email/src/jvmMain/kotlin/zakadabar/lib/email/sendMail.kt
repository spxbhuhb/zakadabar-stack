/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

fun sendMail(text : String) {
    val prop = Properties()
    prop["mail.smtp.auth"] = true
    prop["mail.smtp.starttls.enable"] = "true"
    prop["mail.smtp.host"] = "smtp.gmail.com"
    prop["mail.smtp.port"] = "587"
    prop["mail.smtp.protocol"] = "smtp"

    val session = Session.getInstance(prop, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication("noreply@simplexion.hu", "blabla")
        }
    })

    val message = MimeMessage(session)
    message.setFrom(InternetAddress("noreply@simplexion.hu"))
    message.setRecipients(
        Message.RecipientType.TO, InternetAddress.parse("noreply@simplexion.hu")
    )

    message.setSubject(text, "utf-8")

    val mimeBodyPart = MimeBodyPart()
    mimeBodyPart.setContent(text, "text/html")

    val multipart = MimeMultipart()
    multipart.addBodyPart(mimeBodyPart)

    message.setContent(multipart)

    Transport.send(message)
}