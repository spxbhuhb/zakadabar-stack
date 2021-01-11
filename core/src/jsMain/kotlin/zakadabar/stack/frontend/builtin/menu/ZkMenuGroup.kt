/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.menu

import zakadabar.stack.frontend.elements.ZkClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.plusAssign

class ZkMenuGroup(
    private val text: String,
    builder: ZkElement.() -> Unit
) : ZkElement() {

    init {
        + column {
            + div(ZkMenuStyles.groupTitle) {
                + text
                on(buildContext, "click") { _ -> childElements.first().toggle() }
            }
            + zke(ZkMenuStyles.groupContent) {
                classList += ZkClasses.zkClasses.hidden
                this.builder()
            }
        }
    }

}