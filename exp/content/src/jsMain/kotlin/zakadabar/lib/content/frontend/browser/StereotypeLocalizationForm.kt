/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import zakadabar.lib.content.data.StereotypeLocalizationBo
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.modal.withConfirm
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.resources.css.px
import zakadabar.stack.frontend.util.io

class StereotypeLocalizationForm(
    private val parent: ZkElement
) : ZkForm<StereotypeLocalizationBo>() {

    override fun onConfigure() {
        mode = ZkElementMode.Other
    }

    override fun onCreate() {
        super.onCreate()

        + grid {
            gridTemplateColumns = "1fr max-content"
            gridGap = 20.px

            + section {
                + select(bo::locale) { LocaleBo.all().by { it.name } }
                + bo::localizedName
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