/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.help

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.modal.modalMessage
import zakadabar.core.browser.util.io
import zakadabar.core.module.CommonModule
import zakadabar.core.resource.localized
import zakadabar.core.resource.localizedStrings

open class TextHelpProvider : CommonModule, HelpProvider {

    override fun showHelp(anchorElement: HTMLElement, args: Any) {
        val content = args.toString().localized
        io {
            modalMessage(localizedStrings.helpTitle, content)
        }
    }

}