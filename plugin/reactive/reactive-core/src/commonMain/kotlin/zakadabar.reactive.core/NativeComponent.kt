/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.core

/**
 * [Guide](https://zakadabar.io/resolve?type=guide&class=zakadabar.reactive.core.NativeComponent)
 * [Recipes](https://zakadabar.io/resolve?type=recipes&class=zakadabar.reactive.core.NativeComponent)
 *
 * Base class for manually written components.
 */
open class NativeComponent : Component() {
    override var c : () -> Unit = ::create
    override var p : (Int) -> Unit = ::patch
    override var s : (Int, Any?) -> Unit = ::set

    open fun create() {  }

    open fun patch(dirty : Int) {  }

    open fun set(mask : Int, value : Any?) {  }
}