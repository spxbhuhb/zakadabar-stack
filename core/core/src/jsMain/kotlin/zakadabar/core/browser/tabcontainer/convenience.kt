/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.tabcontainer

fun tabContainer(
    styles: TabContainerStyleSpec = zkTabContainerStyles,
    builder: (ZkTabContainer.() -> Unit)? = null
) =
    ZkTabContainer(styles, builder)