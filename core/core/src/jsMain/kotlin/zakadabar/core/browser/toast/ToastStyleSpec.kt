/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.toast

import zakadabar.core.resource.css.CssStyleSpec
import zakadabar.core.resource.css.ZkCssStyleRule

interface ToastStyleSpec : CssStyleSpec {

    /**
     * Style for [ZkToastContainer].
     */
     val appToastContainer : ZkCssStyleRule

     val toastOuter : ZkCssStyleRule

    /**
     * Style for one toast.
     */
     val toastInner : ZkCssStyleRule

    /**
     * For the icon at the left side of the toast.
     */
     val iconContainer : ZkCssStyleRule

    /**
     * For the text of the toast.
     */
     val text : ZkCssStyleRule

    /**
     * For the close icon.
     */
     val closeIcon : ZkCssStyleRule
     
     val primaryInner : ZkCssStyleRule

     val primaryIcon  : ZkCssStyleRule

     val secondaryInner  : ZkCssStyleRule

     val secondaryIcon  : ZkCssStyleRule

     val successInner  : ZkCssStyleRule

     val successIcon  : ZkCssStyleRule

     val warningInner  : ZkCssStyleRule

     val warningIcon  : ZkCssStyleRule

     val dangerInner  : ZkCssStyleRule

     val dangerIcon  : ZkCssStyleRule

     val infoInner  : ZkCssStyleRule

     val infoIcon  : ZkCssStyleRule
    
}