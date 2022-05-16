/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.note.NoteStyleSpec
import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Borders
import zakadabar.softui.browser.theme.base.BoxShadows

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
        paddingRight = 10.px
        paddingLeft = 10.px
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
        paddingLeft = 10.px
        paddingRight = 10.px
        flexGrow = 1.0
    }

    override val separator by cssClass {
        height = 1.px
        backgroundImage = "linear-gradient(to right, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.5), rgba(255, 255, 255, 0))"
    }

    fun ZkCssStyleRule.inner(text : String, background : String) {
        color = text
        backgroundColor = background
        borderRadius = Borders.borderRadius.md
        boxShadow = BoxShadows.md
    }
    
    fun ZkCssStyleRule.title(textColor : String) {
        color = textColor
        fill = textColor
    }
    
    override val primaryInner by cssClass {
        inner(theme.primaryPair, theme.primaryColor)
    }

    override val primaryTitle by cssClass {
        title(theme.primaryPair)
    }

    override val secondaryInner by cssClass {
        inner(theme.secondaryPair, theme.secondaryColor)
    }

    override val secondaryTitle by cssClass {
        title(theme.secondaryPair)
    }

    override val successInner by cssClass {
        inner(theme.successPair, theme.successColor)
    }

    override val successTitle by cssClass {
        title(theme.successPair)
    }

    override val warningInner by cssClass {
       inner(theme.warningPair, theme.warningColor)
    }

    override val warningTitle by cssClass {
        title(theme.warningPair)
    }

    override val dangerInner by cssClass {
        inner(theme.dangerPair, theme.dangerColor)
    }

    override val dangerTitle by cssClass {
        title(theme.dangerPair)
    }

    override val infoInner by cssClass {
       inner(theme.infoPair, theme.infoColor)
    }

    override val infoTitle by cssClass {
        title(theme.infoPair)
    }

}