/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.field.select

import zakadabar.core.browser.field.ZkSelectBaseV2

interface SelectRenderer<VT, FT : ZkSelectBaseV2<VT, FT>> {

    var field: ZkSelectBaseV2<VT, FT>

    val context get() = field.context

    val items get() = field.items

    fun readOnly(value: Boolean)

    fun onCreate() {  }

    fun onPause() {  }

    fun buildFieldValue()

    /**
     * Rebuilds the content of the select. When [hide] is `true` (default) the popup that
     * contains the possible options is closed by the render. This has no effect for renderers
     * without popup.
     */
    fun render(value: VT?, hide : Boolean = true)

    fun focusValue()

}