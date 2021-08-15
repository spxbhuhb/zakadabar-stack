/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend

import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.icon.ZkIcon
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.resource.ZkIcons
import zakadabar.core.browser.util.plusAssign

class CodeCopy(
    private val block: HTMLElement
) : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        classList += markdownStyles.codeCopy

        + div(markdownStyles.codeCopyIcon) {
            + ZkIcon(ZkIcons.contentCopy, 18)
        }

        block.parentElement!!.appendChild(this.element)

        on("click") {
            window.navigator.clipboard.writeText(block.innerText)
            toastSuccess(hideAfter = 750) { "Copied to clipboard!" }
        }
    }

}