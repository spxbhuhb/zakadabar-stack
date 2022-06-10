/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.toast

import zakadabar.core.resource.css.*

var zkToastStyles : ToastStyleSpec by cssStyleSheet(ZkToastStyles())

open class ZkToastStyles : ZkCssStyleSheet(), ToastStyleSpec {

    override val appToastContainer by cssClass {
        + Position.fixed
        + Display.flex
        + FlexDirection.column
        + JustifyContent.flexEnd

        right = 20.px
        bottom = 20.px
        zIndex = 2200.zIndex
    }

    override val toastOuter by cssClass {
        backgroundColor = theme.backgroundColor
        marginRight = 10.px
        marginBottom = 10.px
    }

    override val toastInner by cssClass {
        + Display.flex
        + FlexDirection.row
        + AlignItems.center
        boxShadow = theme.boxShadow
        borderRadius = 2.px
    }

    override val iconContainer by cssClass {
        + Display.flex
        + FlexDirection.row
        + JustifyContent.center
        + AlignItems.center
        + AlignSelf.stretch

        width = 32.px
    }

    override val text by cssClass {
        paddingTop = 8.px
        paddingBottom = 8.px
        paddingLeft = 10.px
        paddingRight = 10.px
        flexGrow = 1.0
    }

    /**
     * For the close icon.
     */
    override val closeIcon by cssClass {
        fill = theme.textColor
        marginRight = 6.px
    }

    override val primaryInner by cssClass {
        border = "1px solid ${theme.primaryColor}"
        backgroundColor = theme.primaryColor + "20"
    }

    override val primaryIcon by cssClass {
        backgroundColor = theme.primaryColor
        fill = theme.primaryPair
    }

    override val secondaryInner by cssClass {
        border = "1px solid ${theme.secondaryColor}"
        backgroundColor = theme.secondaryColor + "20"
    }

    override val secondaryIcon by cssClass {
        backgroundColor = theme.secondaryColor
        fill = theme.secondaryPair
    }

    override val successInner by cssClass {
        border = "1px solid ${theme.successColor}"
        backgroundColor = theme.successColor + "20"
    }

    override val successIcon by cssClass {
        backgroundColor = theme.successColor
        fill = theme.successPair
    }

    override val warningInner by cssClass {
        border = "1px solid ${theme.warningColor}"
        backgroundColor = theme.warningColor + "20"
    }

    override val warningIcon by cssClass {
        backgroundColor = theme.warningColor
        fill = theme.warningPair
    }

    override val dangerInner by cssClass {
        border = "1px solid ${theme.dangerColor}"
        backgroundColor = theme.dangerColor + "20"
    }

    override val dangerIcon by cssClass {
        backgroundColor = theme.dangerColor
        fill = theme.dangerPair
    }

    override val infoInner by cssClass {
        border = "1px solid ${theme.infoColor}"
        backgroundColor = theme.infoColor + "20"
    }

    override val infoIcon by cssClass {
        backgroundColor = theme.infoColor
        fill = theme.infoPair
    }

}