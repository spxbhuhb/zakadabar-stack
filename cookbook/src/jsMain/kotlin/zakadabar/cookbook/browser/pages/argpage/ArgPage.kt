/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.pages.argpage

import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.builtin.button.buttonPrimary
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.pages.ZkArgPage
import zakadabar.stack.frontend.resources.css.px
import zakadabar.stack.frontend.resources.theme
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.util.default
import zakadabar.stack.util.fourRandomInt

/**
 * This example shows how to create a page with arguments passed in the
 * URL. In this case the user can jump into the middle of the application
 * to open a parameterized page.
 *
 * [ZkArgPage.open] and [ZkArgPage.route] contains the argument handling.
 *
 * There are two properties: [argsOrNull] and [args].
 *
 * [argsOrNull] contains either null (in case there was no arguments
 * in the URL or it was mal-formatted) or an arguments instance.
 *
 * [args] contains the arguments instance, throws an exception on read if
 * the URL does not contain the arguments or they are mal-formed.
 */
class ArgPage : ZkArgPage<Args>(
    Args.serializer()
) {

    override fun onCreate() {
        super.onCreate()

        + zkLayoutStyles.p1

        if (argsOrNull == null) argsOrNull = default { }

        + column {
            + div { + "a1 = ${args.a1}" }
            + div { + "a2 = ${args.a2}" }
        } marginBottom theme.spacingStep.px

        + buttonPrimary("Reload") {
            target<ArgPage>().open(args.copy(a1 = fourRandomInt()[0], a2 = args.a2 + 1))
        }
    }

}