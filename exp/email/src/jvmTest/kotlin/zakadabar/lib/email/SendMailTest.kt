/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.subethamail.wiser.Wiser
import zakadabar.stack.backend.testing.TestCompanionBase
import zakadabar.stack.module.modules
import javax.mail.Part
import javax.mail.internet.MimeMultipart
import javax.mail.util.SharedByteArrayInputStream
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class SendMailTest {

    companion object : TestCompanionBase() {

        val wiser = Wiser()

        override fun addModules() {
            modules += MailModuleBundle()
        }

        @BeforeClass
        @JvmStatic
        override fun setup() {
            super.setup()
            wiser.setPort(2500) // Default is 25
            wiser.start()
        }

        @AfterClass
        @JvmStatic
        override fun teardown() {
            wiser.stop()
            super.teardown()
        }
    }


    @Test
    fun `send plain text`() = runBlocking {

        wiser.messages.clear()

        val mailId = sendMail("noreply@simplexion.hu", "Hello", "World")

        Process(mailId).execute()

        assertEquals(1, wiser.messages.size)

        val message = wiser.messages.first()
        val mimeMessage = message.mimeMessage
        val content = mimeMessage.content as MimeMultipart

        assertEquals("Hello", mimeMessage.subject)
        assertEquals(1, content.count)

        val part = content.getBodyPart(0)

        assertEquals("text/plain; charset=UTF-8", part.contentType)
        assertEquals("World", part.content)
    }

    @Test
    fun `send html text`() = runBlocking {

        wiser.messages.clear()

        val html = "<html><body>World</body></html>"
        val mailId = sendMail("noreply@simplexion.hu", "Hello", html, "text/html")

        Process(mailId).execute()

        assertEquals(1, wiser.messages.size)

        val message = wiser.messages.first()
        val mimeMessage = message.mimeMessage
        val content = mimeMessage.content as MimeMultipart

        assertEquals("Hello", mimeMessage.subject)
        assertEquals(1, content.count)

        val part = content.getBodyPart(0)

        assertEquals("text/html; charset=UTF-8", part.contentType)
        assertEquals(html, part.content)
    }

    @Test
    fun `send html and attachments`() = runBlocking {

        wiser.messages.clear()

        val a1 = "attachment 1".encodeToByteArray()
        val a2 = "attachment 2".encodeToByteArray()

        val html = "<html><body>World</body></html>"
        val mailId = sendMail(
            "noreply@simplexion.hu", "Hello", html, "text/html",
            listOf(
                Triple(a1, "a1.bin", "application/binary"),
                Triple(a2, "a2.bin", "application/binary")
            )
        )

        Process(mailId).execute()

        assertEquals(1, wiser.messages.size)

        val message = wiser.messages.first()
        val mimeMessage = message.mimeMessage
        val content = mimeMessage.content as MimeMultipart

        assertEquals("Hello", mimeMessage.subject)
        assertEquals(3, content.count)

        var part = content.getBodyPart(0)

        assertEquals("text/html; charset=UTF-8", part.contentType)
        assertEquals(Part.INLINE, part.disposition)
        assertEquals(html, part.content)

        part = content.getBodyPart(1)

        assertEquals("application/binary; name=a1.bin", part.contentType)
        assertEquals("a1.bin", part.fileName)
        assertEquals(Part.ATTACHMENT, part.disposition)
        assertContentEquals(a1, (part.content as SharedByteArrayInputStream).readAllBytes())

        part = content.getBodyPart(2)

        assertEquals("application/binary; name=a2.bin", part.contentType)
        assertEquals("a2.bin", part.fileName)
        assertEquals(Part.ATTACHMENT, part.disposition)
        assertContentEquals(a2, (part.content as SharedByteArrayInputStream).readAllBytes())

    }

}