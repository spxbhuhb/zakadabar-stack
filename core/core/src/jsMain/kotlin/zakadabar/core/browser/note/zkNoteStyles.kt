/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.note

import zakadabar.core.resource.css.*

var zkNoteStyles : NoteStyleSpec by cssStyleSheet(ZkNoteStyles())

open class ZkNoteStyles : ZkCssStyleSheet(), NoteStyleSpec {

    override val noteOuter by cssClass {
        backgroundColor = theme.backgroundColor
        marginRight = 10.px
        marginBottom = 10.px
    }

    override val noteInner by cssClass {
        + Display.flex
        + FlexDirection.column
        boxShadow = theme.boxShadow
        borderRadius = 2.px
    }

    override val titleOuter by cssClass {
        + Display.flex
        + FlexDirection.row
        + AlignItems.center
        + AlignSelf.stretch

        paddingTop = 4.px
        paddingBottom = 4.px
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
        + Display.none
    }

    override val primaryInner by cssClass {
        border = "1px solid ${theme.primaryColor}"
        backgroundColor = theme.primaryColor + "20"
    }

    override val primaryTitle by cssClass {
        backgroundColor = theme.primaryColor
        color = theme.primaryPair
        fill = theme.primaryPair
    }

    override val secondaryInner by cssClass {
        border = "1px solid ${theme.secondaryColor}"
        backgroundColor = theme.secondaryColor + "20"
    }

    override val secondaryTitle by cssClass {
        backgroundColor = theme.secondaryColor
        color = theme.secondaryPair
        fill = theme.secondaryPair
    }

    override val successInner by cssClass {
        border = "1px solid ${theme.successColor}"
        backgroundColor = theme.successColor + "20"
    }

    override val successTitle by cssClass {
        backgroundColor = theme.successColor
        color = theme.successPair
        fill = theme.successPair
    }

    override val warningInner by cssClass {
        border = "1px solid ${theme.warningColor}"
        backgroundColor = theme.warningColor + "20"
    }

    override val warningTitle by cssClass {
        backgroundColor = theme.warningColor
        color = theme.warningPair
        fill = theme.warningPair
    }

    override val dangerInner by cssClass {
        border = "1px solid ${theme.dangerColor}"
        backgroundColor = theme.dangerColor + "20"
    }

    override val dangerTitle by cssClass {
        backgroundColor = theme.dangerColor
        color = theme.dangerPair
        fill = theme.dangerPair
    }

    override val infoInner by cssClass {
        border = "1px solid ${theme.infoColor}"
        backgroundColor = theme.infoColor + "20"
    }

    override val infoTitle by cssClass {
        backgroundColor = theme.infoColor
        color = theme.infoPair
        fill = theme.infoPair
    }

}