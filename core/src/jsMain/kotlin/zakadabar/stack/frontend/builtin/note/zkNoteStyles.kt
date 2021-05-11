/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.note

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkNoteStyles by cssStyleSheet(ZkNoteStyles())

open class ZkNoteStyles : ZkCssStyleSheet<ZkTheme>() {

    open val noteOuter by cssClass {
        backgroundColor = theme.backgroundColor
        marginRight = 10
        marginBottom = 10
    }

    open val noteInner by cssClass {
        display = "flex"
        flexDirection = "column"
        boxShadow = theme.boxShadow
        borderRadius = 2
    }

    open val titleOuter by cssClass {
        alignSelf = "stretch"
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        paddingTop = 4
        paddingBottom = 4
        paddingRight = 10
    }

    open val titleIcon by cssClass {
        marginLeft = 8
        marginRight = 8
    }

    open val contentOuter by cssClass {
        paddingTop = 8
        paddingBottom = 8
        paddingLeft = 10
        paddingRight = 10
        flexGrow = 1
    }

    open val primaryInner by cssClass {
        border = "1px solid ${theme.primaryColor}"
        backgroundColor = theme.primaryColor + "20"
    }

    open val primaryTitle by cssClass {
        backgroundColor = theme.primaryColor
        color = theme.primaryPair
        fill = theme.primaryPair
    }

    open val secondaryInner by cssClass {
        border = "1px solid ${theme.secondaryColor}"
        backgroundColor = theme.secondaryColor + "20"
    }

    open val secondaryTitle by cssClass {
        backgroundColor = theme.secondaryColor
        color = theme.secondaryPair
        fill = theme.secondaryPair
    }

    open val successInner by cssClass {
        border = "1px solid ${theme.successColor}"
        backgroundColor = theme.successColor + "20"
    }

    open val successTitle by cssClass {
        backgroundColor = theme.successColor
        color = theme.successPair
        fill = theme.successPair
    }

    open val warningInner by cssClass {
        border = "1px solid ${theme.warningColor}"
        backgroundColor = theme.warningColor + "20"
    }

    open val warningTitle by cssClass {
        backgroundColor = theme.warningColor
        color = theme.warningPair
        fill = theme.warningPair
    }

    open val dangerInner by cssClass {
        border = "1px solid ${theme.dangerColor}"
        backgroundColor = theme.dangerColor + "20"
    }

    open val dangerTitle by cssClass {
        backgroundColor = theme.dangerColor
        color = theme.dangerPair
        fill = theme.dangerPair
    }

    open val infoInner by cssClass {
        border = "1px solid ${theme.infoColor}"
        backgroundColor = theme.infoColor + "20"
    }

    open val infoTitle by cssClass {
        backgroundColor = theme.infoColor
        color = theme.infoPair
        fill = theme.infoPair
    }

}