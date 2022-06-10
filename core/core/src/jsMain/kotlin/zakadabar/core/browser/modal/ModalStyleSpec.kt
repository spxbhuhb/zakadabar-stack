/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.modal

import zakadabar.core.resource.css.CssStyleSpec
import zakadabar.core.resource.css.ZkCssStyleRule

interface ModalStyleSpec : CssStyleSpec {
    val modalContainer: ZkCssStyleRule
    val modal: ZkCssStyleRule
    val title: ZkCssStyleRule
    val content: ZkCssStyleRule
    val buttons: ZkCssStyleRule
}