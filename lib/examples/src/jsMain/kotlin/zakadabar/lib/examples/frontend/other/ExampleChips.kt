/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.other

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.other.ZkChips
import zakadabar.core.browser.toast.toastInfo
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.iconSource

class ExampleChips: ZkElement() {

    override fun onCreate() {
        super.onCreate()

        + ZkChips("name")
    }
}

class ExampleChipsWithButton: ZkElement() {

    override fun onCreate() {
        super.onCreate()

        val cancelIcon by iconSource("""<path d="M12 2C6.47 2 2 6.47 2 12s4.47 10 10 10 10-4.47 10-10S17.53 2 12 2zm5 13.59L15.59 17 12 13.41 8.41 17 7 15.59 10.59 12 7 8.41 8.41 7 12 10.59 15.59 7 17 8.41 13.41 12 17 15.59z"/>""")
        val button = ZkButton(fill = false, border = false, round = true, flavour = ZkFlavour.Danger, iconSource = cancelIcon ) {
            toastInfo { "clicked on chips" }
        }
        + ZkChips(name = "name", button = button )
    }
}