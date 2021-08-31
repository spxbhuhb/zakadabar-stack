/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.form

import zakadabar.core.browser.field.ZkFieldStyles
import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.css.*

var zkFormStyles by cssStyleSheet(ZkFormStyles())

open class ZkFormStyles : ZkFieldStyles() {

    override var fieldHeight by cssParameter { 38 }

    override fun onConfigure() {
        super.onConfigure()
        fieldValue.marginBottom = 6.px
        selectedOption.height = (fieldHeight - 2).px// -2 to compensate border from container
    }

    // -------------------------------------------------------------------------
    // Containers and layouts
    // -------------------------------------------------------------------------

    open val outerContainer by cssClass {
        + BoxSizing.borderBox
        + Display.flex
        + FlexDirection.column

        width = 100.percent
    }

    open val contentContainer by cssClass {
        flexGrow = 1.0
    }

    open val form by cssClass {

    }

    open val onePanel by cssClass {
        + Display.grid

        gridTemplateColumns = 1.fr
        gap = theme.spacingStep.px
    }

    open val twoPanels by cssClass {
        + Display.grid

        gridTemplateColumns = "1fr 1fr"
        gap = theme.spacingStep.px
    }

    open val spanTwoPanels by cssClass {
        gridColumn = "1 / span 2"
    }

    // -------------------------------------------------------------------------
    // Buttons
    // -------------------------------------------------------------------------

    open val buttons by cssClass {
        marginBlockStart = (theme.spacingStep / 2).px
        marginBlockEnd = (theme.spacingStep / 2).px
    }

    // -------------------------------------------------------------------------
    // Section
    // -------------------------------------------------------------------------

    open val section by cssClass {
        + Display.flex
        + FlexDirection.column

        padding = 12.px
        paddingLeft = 20.px
        paddingBottom = 8.px
        marginBottom = (theme.spacingStep / 2).px

        boxShadow = theme.boxShadow
        borderRadius = theme.cornerRadius.px
        backgroundColor = theme.blockBackgroundColor
        border = theme.fixBorder
    }

    open val sectionTitle by cssClass {
        color = theme.textColor
        fontWeight = 500.weight
        paddingBottom = 4.px
    }

    open val sectionSummary by cssClass {
        color = theme.textColor
        paddingBottom = 16.px
    }

    // -------------------------------------------------------------------------
    // Invalid field list
    // -------------------------------------------------------------------------

    open val invalidFieldList by cssClass {
        padding = 12.px
        margin = 8.px
        boxShadow = theme.boxShadow
        borderRadius = theme.cornerRadius.px
        backgroundColor = ZkColors.white
    }

    open val invalidFieldListInto by cssClass {
        fontSize = 80.percent
        color = ZkColors.Gray.c600
        paddingBottom = 16.px
    }
}