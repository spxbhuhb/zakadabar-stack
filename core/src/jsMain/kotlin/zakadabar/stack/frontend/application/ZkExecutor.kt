/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import zakadabar.stack.data.builtin.account.AccountPublicBo

class ZkExecutor(
    val account: AccountPublicBo,
    val anonymous: Boolean,
    val roles: List<String>
)