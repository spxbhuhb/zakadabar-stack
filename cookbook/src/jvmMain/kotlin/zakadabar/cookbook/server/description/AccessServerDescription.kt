/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.server.description

import zakadabar.stack.backend.server
import zakadabar.stack.data.builtin.misc.ServerDescriptionQuery

class AccessServerDescription {

    fun fromLocalServer() {
        val serverDescription = server.description
        println(serverDescription.name)
        println(serverDescription.version)
        println(serverDescription.defaultLocale)
    }

    suspend fun fromRemoteServer() {
        val serverDescription = ServerDescriptionQuery().execute()
        println(serverDescription.name)
        println(serverDescription.version)
        println(serverDescription.defaultLocale)
    }
    
}