/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.business

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.module.modules
import zakadabar.core.testing.TestCompanionBase
import zakadabar.core.util.default
import zakadabar.lib.schedule.data.Job
import zakadabar.lib.schedule.data.JobStatus
import kotlin.test.assertTrue

class DispatcherTest {

    companion object : TestCompanionBase() {

        override fun addModules() {
            zakadabar.lib.schedule.install()
            modules += WorkerBl()
            modules += WorkerBl("worker1")
            modules += WorkerBl("worker2")
            modules += ActionBl()
        }

        override fun onAfterStarted() {
            // this code runs after the server has been started
            // optional
        }

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun testJob() = runBlocking {

        val actionBl = modules.first<ActionBl>()
        actionBl.channel = Channel()

        val values = mutableListOf<Long>()

        for (i in 0..2) {
            default<Job> {
                status = JobStatus.Pending
                actionNamespace = Action.boNamespace
                actionType = Action::class.simpleName !!
                actionData = Json.encodeToString(Action.serializer(), Action(12L + i))
            }.create()
            values += 12L + i
        }

        var value: Long? = null

        for (i in 0..2) {
            for (j in 1..200) {
                value = actionBl.channel.tryReceive().getOrNull()
                if (value != null) break
                delay(10)
            }
            assertTrue(values.remove(value), "value = $value")
        }

        // wait until the job completes, so we avoid exceptions
        // FIXME this should be handled by onModuleStop

        modules.find<WorkerBl>().forEach { it.job?.join() }

    }


}
