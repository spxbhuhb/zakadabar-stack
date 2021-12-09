/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.modal.withConfirm
import zakadabar.core.browser.util.io
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.px
import zakadabar.lib.content.data.TextBlockBo

class ContentTextForm(
    private val parent: ZkElement
) : ZkForm<TextBlockBo>() {

    override fun onConfigure() {
        mode = ZkElementMode.Other
    }

    override fun onCreate() {
        super.onCreate()

        + grid {
            gridTemplateColumns = "1fr max-content"
            gridGap = 20.px

            + section {
                + bo::stereotype.asSelect() options { textBlockStereotypes }
                + bo::value.asTextArea {
                    area.style.resize = "vertical"
                }
            }

            + ZkButton(ZkIcons.close, ZkFlavour.Danger, round = true) {
                io { withConfirm { parent -= this } }
            }

        }

    }

    override fun onInvalidSubmit() {
        // main form will report this
    }

}