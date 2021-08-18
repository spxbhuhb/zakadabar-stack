/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.help

import zakadabar.core.browser.ZkElement
import zakadabar.core.util.PublicApi

/**
 * Convenience for creating a [InlineHelpWrapper].
 *
 * @param  args            Arguments to pass to the help provider.
 * @param  contentBuilder  Function to build the content that has the help attached to it.
 */
@PublicApi
inline fun <reified T : Any> withHelp(args: T, noinline contentBuilder: () -> ZkElement?): InlineHelpWrapper<T> =
    InlineHelpWrapper(args, contentBuilder = contentBuilder)
