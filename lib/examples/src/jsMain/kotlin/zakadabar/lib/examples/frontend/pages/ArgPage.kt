/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.pages

import kotlinx.serialization.Serializable
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.page.ZkArgPage
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.browser.util.marginRight

/**
 * This example shows how to create a page with arguments passed in the
 * URL. In this case the user can jump into the middle of the application
 * to open a parameterized page.
 *
 * [ZkArgPage.open] and [ZkArgPage.route] contains the argument handling.
 *
 * The property "arg" contains either null (in case there was no arguments
 * in the URL) or the object with the arguments.
 *
 * When you refresh this page you will see one argument object as the
 * application just restarted. Then if you click on the menu item
 * "Built-in" / "ZkArgPage" the page will append the new arguments.
 *
 * If you open other pages and then come back through the menu the original
 * content is preserved as this is an object.
 *
 * If you want to clear the all the content at each open, use [ZkElement.clear]
 * from onResume. Please not that clear destroys children, so you can't use
 * them later.
 */
object ArgPage : ZkArgPage<ArgPage.Args>(
    Args.serializer()
) {

    override val factory = false

    val content = ZkElement() css zkPageStyles.content

    @Serializable
    class Args(
        val a1: Int,
        val a2: String
    )

    override fun onCreate() {
        super.onCreate()

        + content
    }

    override fun onResume() {
        super.onResume()

        val args = this.args ?: return

        content build {
            + div { + "a1 = ${args.a1}" } marginRight 20
            + div { + "a2 = ${args.a2}" }
        }
    }

}