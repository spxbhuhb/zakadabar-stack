/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

import zakadabar.stack.data.BaseBo

expect inline fun <reified T : BaseBo> default(builder: T.() -> Unit): T
