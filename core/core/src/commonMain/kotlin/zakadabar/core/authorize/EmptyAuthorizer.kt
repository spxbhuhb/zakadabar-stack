/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import zakadabar.core.data.BaseBo

/**
 * Rejects everything.
 *
 * Throws [Forbidden] when rejected.
 */
open class EmptyAuthorizer<T : BaseBo> : BusinessLogicAuthorizer<T>