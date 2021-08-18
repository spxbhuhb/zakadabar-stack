/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.help

import org.w3c.dom.HTMLElement

interface HelpProvider {
    fun showHelp(anchorElement : HTMLElement, args : Any)
}