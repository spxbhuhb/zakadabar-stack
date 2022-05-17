/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.modal.ModalStyleSpec
import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Borders

open class SuiModalStyles : ZkCssStyleSheet(), ModalStyleSpec {

    override val modalContainer by cssClass {
        position = "fixed"
        top = 0.px
        left = 0.px
        height = 100.vh
        width = 100.vw
        + JustifyContent.center
        + AlignItems.center
        + Display.flex
        backgroundColor = "rgba(0,0,0,0.5)"
        zIndex = 1900.zIndex
    }

    override val modal by cssClass {
        background = theme.backgroundColor
        border = theme.border
        borderRadius = Borders.borderRadius.md
    }

    override val title by cssClass {
        paddingLeft = theme.spacingStep.px
        paddingRight = theme.spacingStep.px
        borderBottom = theme.border
    }

    override val content by cssClass {
        padding = theme.spacingStep.px
    }

    override val buttons by cssClass {
        + Display.flex
        + FlexDirection.row
        + JustifyContent.spaceAround
        paddingTop = theme.spacingStep.px
        paddingBottom = theme.spacingStep.px
    }

}