/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.backend.Server
import zakadabar.stack.backend.server
import zakadabar.stack.data.CommBase

open class TestCompanion {

    open fun setup(builder : () -> Unit) {
        server = Server("test")
        builder()
        server.main(arrayOf("--test", "--settings", "./template/test/stack.server.yaml"))

        CommBase.baseUrl = "http://127.0.0.1:8888"
    }

    open fun teardown() {
        server.ktorServer.stop(1000, 10000)
    }

}