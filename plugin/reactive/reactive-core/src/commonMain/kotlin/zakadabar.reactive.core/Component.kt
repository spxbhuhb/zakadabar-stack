/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.core

/**
 * [Guide](https://zakadabar.io/resolve?type=guide&class=zakadabar.reactive.core.Component)
 * [Recipes](https://zakadabar.io/resolve?type=recipes&class=zakadabar.reactive.core.Component)
 *
 * Base class for all reactive components.
 * 
 * @property  c  Function to create the component.
 * @property  p  Function to patch the component.
 * @property  s  Function to set component variables by mask.
 */
open class Component {
    open var c : () -> Unit = {  }
    open var p : (Int) -> Unit = {  }
    open var s : (Int, Any?) -> Unit = { _, _ -> }
}