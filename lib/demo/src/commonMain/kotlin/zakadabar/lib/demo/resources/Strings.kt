/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("PropertyName", "unused")

package zakadabar.lib.demo.resources

import zakadabar.stack.resources.ZkBuiltinStrings

internal val strings = Strings()

class Strings : ZkBuiltinStrings() {

    val home by "Demo Home"

}
