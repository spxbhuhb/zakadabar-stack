/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.infoButton
import zakadabar.stack.frontend.builtin.button.secondaryButton
import zakadabar.stack.frontend.builtin.note.secondaryNote
import zakadabar.stack.frontend.util.io

class ParallelDownload : ZkElement() {

    lateinit var fetched: List<String>

    val loading = infoButton("... loading ...") { } marginBottom 10

    override fun onCreate() {
        super.onCreate()

        io {
            coroutineScope {
                launch { fetched = serverMock(4, "item 1.") }
                launch { serverMock(2, "item 2.").forEach { + it } }
            }

            + div {
                fetched.forEach { + it }
            }

            - loading
        }

        + loading
    }

    suspend fun serverMock(seconds: Long, prefix: String): List<String> {
        delay(seconds * 1000)
        return (1..3).map { "$prefix$it " }
    }
}

class ParallelDownloadExample(
    element: HTMLElement
) : ZkElement(element) {

    val output = secondaryNote("Output", "")

    override fun onCreate() {
        + secondaryButton("Run Example") {
            output.content = ParallelDownload()
        } marginBottom 20

        + output
    }

}