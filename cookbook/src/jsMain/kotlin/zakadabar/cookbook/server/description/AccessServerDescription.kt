/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.server.description

import zakadabar.core.server.ServerDescriptionQuery
import zakadabar.core.browser.application.application

class AccessServerDescription {

    fun fromApplication() {
        val serverDescription = application.serverDescription
        println(serverDescription.name)
        println(serverDescription.version)
        println(serverDescription.defaultLocale)
    }

    suspend fun fromServer() {
        val serverDescription = ServerDescriptionQuery().execute()
        println(serverDescription.name)
        println(serverDescription.version)
        println(serverDescription.defaultLocale)
    }

}