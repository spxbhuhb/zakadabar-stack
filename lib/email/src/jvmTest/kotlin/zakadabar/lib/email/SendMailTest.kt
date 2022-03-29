/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.subethamail.wiser.Wiser
import zakadabar.core.module.modules
import zakadabar.lib.accounts.testing.AuthTestCompanionBase
import zakadabar.lib.schedule.business.WorkerBl
import zakadabar.lib.schedule.data.Job
import zakadabar.lib.schedule.data.JobStatus
import javax.mail.Part
import javax.mail.internet.MimeMultipart
import javax.mail.util.SharedByteArrayInputStream
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.fail

class SendMailTest {

    companion object : AuthTestCompanionBase() {

        val wiser = Wiser()

        override fun addModules() {
            super.addModules()

            zakadabar.lib.schedule.install()
            modules += WorkerBl("worker1")

            install()
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

        val mailId = buildMail("noreply@simplexion.hu", "Hello", "World")

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
        val mailId = buildMail("noreply@simplexion.hu", "Hello", html, "text/html")

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
        val mailId = buildMail(
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

    @Test
    fun `send as a Job`() = runBlocking {

        wiser.messages.clear()

        val jobId = sendMail("noreply@simplexion.hu", "Subject", "Content")

        val startTime = System.nanoTime()
        while (System.nanoTime() - startTime < 3_000_000_000) {
            val job = Job.read(jobId)
            when (job.status) {
                JobStatus.Succeeded -> break
                JobStatus.Running -> delay(10)
                JobStatus.Pending -> delay(10)
                JobStatus.Cancelled -> fail("job status is Cancelled")
                JobStatus.Failed -> fail("job status is Failed, message: ${job.lastFailMessage}, data: ${job.lastFailData}")
            }
        }

        assertEquals(1, wiser.messages.size)

        val message = wiser.messages.first()
        val mimeMessage = message.mimeMessage
        val content = mimeMessage.content as MimeMultipart

        assertEquals("Subject", mimeMessage.subject)
        assertEquals(1, content.count)

        val part = content.getBodyPart(0)

        assertEquals("text/plain; charset=UTF-8", part.contentType)
        assertEquals("Content", part.content)


    }

}