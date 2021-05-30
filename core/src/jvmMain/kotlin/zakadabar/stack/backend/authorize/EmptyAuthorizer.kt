/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.data.entity.EntityBo

/**
 * Rejects everything.
 *
 * Throws [Forbidden] when rejected.
 */
open class EmptyAuthorizer<T : EntityBo<T>> : Authorizer<T>