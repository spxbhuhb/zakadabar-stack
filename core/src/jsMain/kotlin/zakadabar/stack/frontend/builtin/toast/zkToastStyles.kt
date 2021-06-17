/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.toast

import zakadabar.stack.frontend.resources.css.*

val zkToastStyles by cssStyleSheet(ZkToastStyles())

open class ZkToastStyles : ZkCssStyleSheet() {

    /**
     * Style for [ZkToastContainer].
     */
    open val appToastContainer by cssClass {
        + Position.fixed
        + Display.flex
        + FlexDirection.column
        + JustifyContent.flexEnd

        right = 20.px
        bottom = 20.px
        zIndex = 2000.zIndex
    }

    open val toastOuter by cssClass {
        backgroundColor = theme.backgroundColor
        marginRight = 10.px
        marginBottom = 10.px
    }

    /**
     * Style for one toast.
     */
    open val toastInner by cssClass {
        + Display.flex
        + FlexDirection.row
        + AlignItems.center
        boxShadow = theme.boxShadow
        borderRadius = 2.px
    }

    /**
     * For the icon at the left side of the toast.
     */
    open val iconContainer by cssClass {
        + Display.flex
        + FlexDirection.row
        + JustifyContent.center
        + AlignItems.center
        + AlignSelf.stretch

        width = 32.px
    }

    /**
     * For the text of the toast.
     */
    open val text by cssClass {
        paddingTop = 8.px
        paddingBottom = 8.px
        paddingLeft = 10.px
        paddingRight = 10.px
        flexGrow = 1.0
    }

    /**
     * For the close icon.
     */
    open val closeIcon by cssClass {
        fill = theme.textColor
        marginRight = 6.px
    }

    open val primaryInner by cssClass {
        border = "1px solid ${theme.primaryColor}"
        backgroundColor = theme.primaryColor + "20"
    }

    open val primaryIcon by cssClass {
        backgroundColor = theme.primaryColor
        fill = theme.primaryPair
    }

    open val secondaryInner by cssClass {
        border = "1px solid ${theme.secondaryColor}"
        backgroundColor = theme.secondaryColor + "20"
    }

    open val secondaryIcon by cssClass {
        backgroundColor = theme.secondaryColor
        fill = theme.secondaryPair
    }

    open val successInner by cssClass {
        border = "1px solid ${theme.successColor}"
        backgroundColor = theme.successColor + "20"
    }

    open val successIcon by cssClass {
        backgroundColor = theme.successColor
        fill = theme.successPair
    }

    open val warningInner by cssClass {
        border = "1px solid ${theme.warningColor}"
        backgroundColor = theme.warningColor + "20"
    }

    open val warningIcon by cssClass {
        backgroundColor = theme.warningColor
        fill = theme.warningPair
    }

    open val dangerInner by cssClass {
        border = "1px solid ${theme.dangerColor}"
        backgroundColor = theme.dangerColor + "20"
    }

    open val dangerIcon by cssClass {
        backgroundColor = theme.dangerColor
        fill = theme.dangerPair
    }

    open val infoInner by cssClass {
        border = "1px solid ${theme.infoColor}"
        backgroundColor = theme.infoColor + "20"
    }

    open val infoIcon by cssClass {
        backgroundColor = theme.infoColor
        fill = theme.infoPair
    }

}