/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.liveview.browser

import zakadabar.lib.liveview.model.LiveViewElement

interface LvImpl<T : LiveViewElement> {

    var data : T

}