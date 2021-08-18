/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.cookbook.browser.help

import zakadabar.cookbook.resource.strings
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.buttonPrimary
import zakadabar.core.browser.help.withHelp
import zakadabar.core.browser.toast.toastSuccess

class TextHelpModal : ZkElement() {

    override fun onCreate() {
        super.onCreate()
        + withHelp(strings.exampleHelpContent) {
            buttonPrimary(strings.exampleButton) {
                toastSuccess { strings.clicked }
            }
        }
    }

}