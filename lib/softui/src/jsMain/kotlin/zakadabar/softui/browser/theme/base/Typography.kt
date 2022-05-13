/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2 license.
 */
package zakadabar.softui.browser.theme.base

import zakadabar.core.resource.css.ZkCssStyleRule
import zakadabar.core.resource.css.ZkCssStyleSheet
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.css.weight

var typography by cssStyleSheet(Typography())

@Suppress("unused", "ClassName")
class Typography : ZkCssStyleSheet() {

    object baseProperties {
        const val fontFamily = """"Roboto", "Helvetica", "Arial", sans-serif"""
        val fontWeightLight = 300.weight
        val fontWeightRegular = 400.weight
        val fontWeightMedium = 500.weight
        val fontWeightBold = 700.weight
        val fontSizeXXS = pxToRem(10.4)
        val fontSizeXS = pxToRem(12)
        val fontSizeSM = pxToRem(14)
        val fontSizeMD = pxToRem(16)
        val fontSizeLG = pxToRem(18)
        val fontSizeXL = pxToRem(20)
    }

    fun ZkCssStyleRule.baseHeadingProperties() {
        fontFamily = baseProperties.fontFamily
        color = Colors.dark.main
        fontWeight = baseProperties.fontWeightMedium
    }

    fun ZkCssStyleRule.baseDisplayProperties() {
        fontFamily = baseProperties.fontFamily
        color = Colors.dark.main
        fontWeight = baseProperties.fontWeightLight
        lineHeight = "1.2"
    }

    val h1 by cssClass("h1") {
        fontSize = pxToRem(48)
        lineHeight = "1.25"
        baseHeadingProperties()
    }

    val h2 by cssClass("h2") {
        fontSize = pxToRem(36)
        lineHeight = "1.3"
        baseHeadingProperties()
    }

    val h3 by cssClass("h3") {
        fontSize = pxToRem(30)
        lineHeight = "1.375"
        baseHeadingProperties()
    }

    val h4 by cssClass("h4") {
        fontSize = pxToRem(24)
        lineHeight = "1.375"
        baseHeadingProperties()
    }

    val h5 by cssClass("h5") {
        fontSize = pxToRem(20)
        lineHeight = "1.375"
        baseHeadingProperties()
    }

    val h6 by cssClass("h6") {
        fontSize = pxToRem(16)
        lineHeight = "1.625"
        baseHeadingProperties()
    }

    val subtitle1 by cssClass {
        fontFamily = baseProperties.fontFamily
        fontSize = baseProperties.fontSizeXL
        fontWeight = baseProperties.fontWeightRegular
        lineHeight = "1.625"
    }

    val subtitle2 by cssClass("h1") {
        fontFamily = baseProperties.fontFamily
        fontSize = baseProperties.fontSizeMD
        fontWeight = baseProperties.fontWeightMedium
        lineHeight = "1.6"
    }

    val body1 by cssClass {
        fontFamily = baseProperties.fontFamily
        fontSize = baseProperties.fontSizeXL
        fontWeight = baseProperties.fontWeightRegular
        lineHeight = "1.625"
    }

    val body2 by cssClass {
        fontFamily = baseProperties.fontFamily
        fontSize = baseProperties.fontSizeMD
        fontWeight = baseProperties.fontWeightRegular
        lineHeight = "1.6"
    }

    val button by cssClass {
        fontFamily = baseProperties.fontFamily
        fontSize = baseProperties.fontSizeSM
        fontWeight = baseProperties.fontWeightBold
        lineHeight = "1.5"
        textTransform = "uppercase"
    }

    val caption by cssClass {
        fontFamily = baseProperties.fontFamily
        fontSize = baseProperties.fontSizeXS
        fontWeight = baseProperties.fontWeightRegular
        lineHeight = "1.25"
    }

    val overline by cssClass {
        fontFamily = baseProperties.fontFamily
    }

    val d1 by cssClass {
        fontSize = pxToRem(80)
        baseDisplayProperties()
    }

    val d2 by cssClass {
        fontSize = pxToRem(72)
        baseDisplayProperties()
    }

    val d3 by cssClass {
        fontSize = pxToRem(64)
        baseDisplayProperties()
    }

    val d4 by cssClass {
        fontSize = pxToRem(56)
        baseDisplayProperties()
    }

    val d5 by cssClass {
        fontSize = pxToRem(48)
        baseDisplayProperties()
    }

    val d6 by cssClass {
        fontSize = pxToRem(40)
        baseDisplayProperties()
    }

    object size {
        val xxs = baseProperties.fontSizeXXS
        val xs = baseProperties.fontSizeXS
        val sm = baseProperties.fontSizeSM
        val md = baseProperties.fontSizeMD
        val lg = baseProperties.fontSizeLG
        val xl = baseProperties.fontSizeXL
    }

    object lineHeight {
        const val sm = "1.25"
        const val md = "1.5"
        const val lg = "2"
    }
}