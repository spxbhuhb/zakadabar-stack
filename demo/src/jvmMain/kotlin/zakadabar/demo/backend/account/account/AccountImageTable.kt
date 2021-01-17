/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.account.account

import zakadabar.stack.backend.data.builtin.BlobTable

object AccountImageTable : BlobTable("account_images", AccountPrivateTable)