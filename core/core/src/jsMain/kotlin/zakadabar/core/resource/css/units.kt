/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource.css

import zakadabar.core.util.PublicApi

@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("EOL: 2021.8.1  -  use '.px' instead")
fun stringOrPx(value: Any?) = if (value is String) value else "${value}px"

@PublicApi
inline val Number.px
    get() = "${this}px"

@PublicApi
inline val Number.percent
    get() = "${this}%"

@PublicApi
inline val Number.pt
    get() = "${this}pt"

@PublicApi
inline val Number.em
    get() = "${this}em"

@PublicApi
inline val Number.rem
    get() = "${this}rem"

@PublicApi
inline val Number.vh
    get() = "${this}vh"

@PublicApi
inline val Number.vw
    get() = "${this}vw"

@PublicApi
inline val Number.fr
    get() = "${this}fr"

@PublicApi
inline val Number.opacity
    get() = "$this"

@PublicApi
inline val Number.zIndex
    get() = "$this"

@PublicApi
inline val Number.weight
    get() = "$this"

