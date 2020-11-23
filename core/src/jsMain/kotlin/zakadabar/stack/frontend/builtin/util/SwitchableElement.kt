/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util

import zakadabar.stack.frontend.elements.ZkElement

/**
 * A frontend element to use in [SwitchView][zakadabar.stack.frontend.components.composite.SwitchView].
 *
 *
 */
abstract class SwitchableElement : ZkElement() {

    lateinit var switchView: SwitchView

}