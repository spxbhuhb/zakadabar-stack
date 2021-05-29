/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.builtin.misc.ServerDescriptionBo
import zakadabar.stack.data.entity.EntityId

/**
 * This session manager is for applications that does not use sessions.
 */
class EmptySessionManager : ZkSessionManager {

    override suspend fun init() {

        application.executor = ZkExecutor(
            account = AccountPublicBo(
                id = EntityId(),
                accountName = "anonymous",
                fullName = "Anonymous",
                email = null,
                displayName = null,
                organizationName = null,
                theme = null,
                locale = ""
            ),
            anonymous = true,
            roles = emptyList()
        )

        application.serverDescription = ServerDescriptionBo(
            name = "",
            version =  "",
            defaultLocale = ""
        )

    }

    override suspend fun renew() {
        // should never reach this point as there is no session manager and
        // the server will never return with 440 Login Timeout
        throw NotImplementedError()
    }
}