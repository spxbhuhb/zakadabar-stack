/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.application

import zakadabar.core.authorize.AccountPublicBo
import zakadabar.core.server.ServerDescriptionQuery
import zakadabar.core.data.EntityId

/**
 * This session manager is for applications that does not use sessions.
 */
class EmptySessionManager : ZkSessionManager {

    override suspend fun init() {

        application.executor = ZkExecutor(
            account = AccountPublicBo(
                accountId = EntityId(),
                accountName = "anonymous",
                fullName = "Anonymous",
                email = null,
                phone = null,
                theme = null,
                locale = ""
            ),
            anonymous = true,
            roles = emptyList()
        )

        application.serverDescription = ServerDescriptionQuery().execute()
    }

    override suspend fun renew() {
        // should never reach this point as there is no session manager and
        // the server will never return with 440 Login Timeout
        throw NotImplementedError()
    }
}