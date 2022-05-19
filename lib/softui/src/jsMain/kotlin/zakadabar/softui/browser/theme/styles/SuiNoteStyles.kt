/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.note.NoteStyleSpec
import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Borders
import zakadabar.softui.browser.theme.base.BoxShadows
import zakadabar.softui.browser.theme.base.Colors
import zakadabar.softui.browser.theme.base.linearGradient

open class SuiNoteStyles : ZkCssStyleSheet(), NoteStyleSpec {

    override val noteOuter by cssClass {
        backgroundColor = "transparent"
        marginRight = 10.px
        marginBottom = 10.px
    }

    override val noteInner by cssClass {
        + Display.flex
        + FlexDirection.column
        fontSize = 14.px
    }

    override val titleOuter by cssClass {
        + Display.flex
        + FlexDirection.row
        + AlignItems.center
        + AlignSelf.stretch

        fontWeight = 500.weight

        paddingTop = 8.px
        paddingBottom = 8.px
        paddingRight = 10.px
    }

    override val titleIcon by cssClass {
        marginLeft = 8.px
        marginRight = 8.px
    }

    override val contentOuter by cssClass {
        paddingTop = 8.px
        paddingBottom = 8.px
        paddingLeft = 16.px
        paddingRight = 10.px
        flexGrow = 1.0
    }

    override val separator by cssClass {
        height = 1.px
        backgroundImage = "linear-gradient(to right, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.5), rgba(255, 255, 255, 0))"
    }

    fun ZkCssStyleRule.inner(text : String, border : String) {
        color = theme.textColor
        borderLeft = "1px solid $border"
        borderRight = "1px solid $border"
        borderBottom = "1px solid $border"
        borderRadius = Borders.borderRadius.md
        boxShadow = BoxShadows.md
        backgroundColor = theme.blockBackgroundColor
    }
    
    fun ZkCssStyleRule.title(text: String) {
        color = text
        fill = text
        borderTopLeftRadius = Borders.borderRadius.md
        borderTopRightRadius = Borders.borderRadius.md
    }
    
    override val primaryInner by cssClass {
        inner(theme.primaryPair, Colors.alertColors.primary.border)
    }

    override val primaryTitle by cssClass {
        title(theme.primaryPair)
        backgroundImage = linearGradient(Colors.alertColors.primary.main, Colors.alertColors.primary.state)
    }

    override val secondaryInner by cssClass {
        inner(theme.secondaryPair, Colors.alertColors.secondary.border)
    }

    override val secondaryTitle by cssClass {
        title(theme.secondaryPair)
        backgroundImage = linearGradient(Colors.alertColors.secondary.main, Colors.alertColors.secondary.state)
    }

    override val successInner by cssClass {
        inner(theme.successPair, Colors.alertColors.success.border)
    }

    override val successTitle by cssClass {
        title(theme.successPair)
        backgroundImage = linearGradient(Colors.alertColors.success.main, Colors.alertColors.success.state)
    }

    override val warningInner by cssClass {
       inner(theme.warningPair, Colors.alertColors.warning.border)
    }

    override val warningTitle by cssClass {
        title(theme.warningPair)
        backgroundImage = linearGradient(Colors.alertColors.warning.main, Colors.alertColors.warning.state)
    }

    override val dangerInner by cssClass {
        inner(theme.dangerPair, Colors.alertColors.danger.border)
    }

    override val dangerTitle by cssClass {
        title(theme.dangerPair)
        backgroundImage = linearGradient(Colors.alertColors.danger.main, Colors.alertColors.danger.state)
    }

    override val infoInner by cssClass {
       inner(theme.infoPair, Colors.alertColors.info.border)
    }

    override val infoTitle by cssClass {
        title(theme.infoPair)
        backgroundImage = linearGradient(Colors.alertColors.info.main, Colors.alertColors.info.state)
    }

}