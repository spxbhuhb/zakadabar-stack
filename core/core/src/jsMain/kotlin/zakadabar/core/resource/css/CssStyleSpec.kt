/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource.css

/**
 * Style specifications are interfaces that specify which classes a style sheet
 * should provide for the components. This is important only when you design
 * your component with replaceable styles.
 */
interface CssStyleSpec {
    var attachOnRefresh : Boolean
    fun attach()
    fun detach()
    fun resetParameters()
    fun onThemeChange()
}