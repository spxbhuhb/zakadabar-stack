/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("EOL: 2021.8.1  -  use function from zakadabar.stack.module instead")
inline fun <reified T : Any> module(noinline selector: (T) -> Boolean = { true }) = zakadabar.stack.module.module(selector)
