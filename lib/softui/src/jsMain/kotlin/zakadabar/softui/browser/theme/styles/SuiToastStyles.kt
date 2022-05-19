/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.toast.ToastStyleSpec
import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Borders
import zakadabar.softui.browser.theme.base.BoxShadows
import zakadabar.softui.browser.theme.base.Colors
import zakadabar.softui.browser.theme.base.linearGradient

open class SuiToastStyles : ZkCssStyleSheet(), ToastStyleSpec {

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
        backgroundColor = "transparent"
        marginRight = 10.px
        marginBottom = 10.px
    }

    override val toastInner by cssClass {
        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        borderRadius = Borders.borderRadius.md
        boxShadow = BoxShadows.md
        minHeight = 52.px
        minWidth = 400.px

        fontSize = 14.px

        small {
            minWidth = 100.vw
        }
    }

    override val iconContainer by cssClass {
        + Display.flex
        + FlexDirection.row
        + JustifyContent.center
        + AlignItems.center
        + AlignSelf.stretch

        marginLeft = 8.px
        width = 32.px
    }

    override val text by cssClass {
        paddingTop = 8.px
        paddingBottom = 8.px
        paddingLeft = 8.px
        paddingRight = 10.px
        flexGrow = 1.0
    }

    /**
     * For the close icon.
     */
    override val closeIcon by cssClass {
        fill = Colors.white.main
        marginRight = 6.px
    }

    override val primaryInner by cssClass {
        border = "1px solid ${Colors.alertColors.dark.border}"
        color = theme.primaryPair
        backgroundImage = linearGradient(Colors.alertColors.primary.main, Colors.alertColors.primary.state)
    }

    override val primaryIcon by cssClass {
        fill = theme.primaryPair
    }

    override val secondaryInner by cssClass {
        border = "1px solid ${Colors.alertColors.secondary.border}"
        color = theme.secondaryPair
        backgroundImage = linearGradient(Colors.alertColors.secondary.main, Colors.alertColors.secondary.state)
    }

    override val secondaryIcon by cssClass {
        fill = theme.secondaryPair
    }

    override val successInner by cssClass {
        border = "1px solid ${Colors.alertColors.success.border}"
        color = theme.successPair
        backgroundImage = linearGradient(Colors.alertColors.success.main, Colors.alertColors.success.state)
    }

    override val successIcon by cssClass {
        fill = theme.successPair
    }

    override val warningInner by cssClass {
        border = "1px solid ${Colors.alertColors.warning.border}"
        color = theme.warningPair
        backgroundImage = linearGradient(Colors.alertColors.warning.main, Colors.alertColors.warning.state)
    }

    override val warningIcon by cssClass {
        fill = theme.warningPair
    }

    override val dangerInner by cssClass {
        border = "1px solid ${Colors.alertColors.danger.border}"
        color = theme.dangerPair
        backgroundImage = linearGradient(Colors.alertColors.danger.main, Colors.alertColors.danger.state)
    }

    override val dangerIcon by cssClass {
        fill = theme.dangerPair
    }

    override val infoInner by cssClass {
        border = "1px solid ${Colors.alertColors.info.border}"
        color = theme.infoPair
        backgroundImage = linearGradient(Colors.alertColors.info.main, Colors.alertColors.info.state)
    }

    override val infoIcon by cssClass {
        fill = theme.infoPair
    }


}