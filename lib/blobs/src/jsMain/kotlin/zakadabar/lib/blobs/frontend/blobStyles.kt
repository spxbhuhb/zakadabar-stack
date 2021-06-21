/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.frontend

import zakadabar.stack.frontend.resources.css.*

val blobStyles by cssStyleSheet(BlobStyles())

class BlobStyles : ZkCssStyleSheet() {

    val attachmentField by cssClass {
        + Display.grid
        + AlignItems.center

        gridTemplateColumns = "repeat(4, max-content)"
        gap = theme.spacingStep.px
    }

    val attachmentEntry by cssClass {
        + Display.contents
    }

    val imageDropArea by cssClass {
        + BoxSizing.borderBox
        + Display.flex
        + JustifyContent.center
        + AlignItems.center

        flexGrow = 1.0

        width = 100.percent
        height = 100.percent

        color = theme.textColor
        fill = theme.textColor

        padding = 20.px

        borderRadius = theme.cornerRadius.px
        border = "1px dotted lightgray"

        on(":hover") {
            backgroundColor = theme.hoverBackgroundColor
            color = theme.hoverTextColor
            fill = theme.hoverTextColor
        }
    }

    val imageDropAreaMessage by cssClass {
        fontSize = 14.px
        fontWeight = 400.weight
        paddingLeft = 6.px
    }

    val outerView by cssClass {
        + BoxSizing.borderBox

        + Position.absolute

        + Display.flex
        + FlexDirection.row
        + JustifyContent.center
        + AlignItems.center

        top = 0.px
        left = 0.px
        height = 100.vh
        width = 100.vw
        background = "rgba(0, 0, 0, 0.8)"
        zIndex = 1000.zIndex
        outline = "none" // this is here because we have a tabindex on ZkFullScreenImageView
    }

    val image by cssClass {
        maxWidth = 100.percent
        maxHeight = 100.percent
    }

    val imageName by cssClass {
        + AlignSelf.center
        marginBlockStart = 4.px
        marginBlockEnd = 4.px
        fontSize = 85.percent
    }

    val imageMimeType by cssClass {
        + AlignSelf.center
        fontSize = 70.percent
    }

    val actions by cssClass {
        + Display.flex
        + FlexDirection.row
        + JustifyContent.spaceAround
    }

    val closeButton by cssClass {
        + Position.absolute
        top = 20.px
        right = 20.px
        zIndex = 1001.zIndex
    }

    val deleteButton by cssClass {
        + Position.absolute
        top = 20.px
        left = 20.px
        zIndex = 1001.zIndex
    }

}