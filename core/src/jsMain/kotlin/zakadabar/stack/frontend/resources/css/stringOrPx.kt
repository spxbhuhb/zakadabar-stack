/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.resources.css

fun stringOrPx(value: Any?) = if (value is String) value else "${value}px"
