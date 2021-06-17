/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.frontend

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val blobStyles by cssStyleSheet(BlobStyles())

class BlobStyles : ZkCssStyleSheet() {

    val attachmentField by cssClass {
        display = "grid"
        gridTemplateColumns = "repeat(4, max-content)"
        gap = theme.spacingStep
        alignItems = "center"
    }

    val attachmentEntry by cssClass {
        display = "contents"
    }

    val imageDropArea by cssClass {
        boxSizing = "border-box"
        flexGrow = 1
        width = "100%"
        height = "100%"
        display = "flex"
        flexDirection = "row"
        justifyContent = "center"
        alignItems = "center"
        color = theme.textColor
        fill = theme.textColor

        padding = 20

        borderRadius = theme.cornerRadius
        border = "1px dotted lightgray"

        on(":hover") {
            backgroundColor = theme.hoverBackgroundColor
            color = theme.hoverTextColor
            fill = theme.hoverTextColor
        }
    }

    val imageDropAreaMessage by cssClass {
        fontSize = 14
        fontWeight = 400
        paddingLeft = 6
    }

    val outerView by cssClass {
        boxSizing = "border-box"
        position = "absolute"
        top = "0"
        left = "0"
        height = "100vh"
        width = "100vw"
        display = "flex"
        background = "rgba(0, 0, 0, 0.8)"
        zIndex = 1000
        justifyContent = "center"
        alignItems = "center"
        outline = "none" // this is here because we have a tabindex on ZkFullScreenImageView
    }

    val image by cssClass {
        maxWidth = "100%"
        maxHeight = "100%"
    }

    val actions by cssClass {
        display = "flex"
        flexDirection = "row"
        justifyContent = "space-around"
    }

    val closeButton by cssClass {
        position = "absolute"
        top = 20
        right = 20
        zIndex = 1001
    }

    val deleteButton by cssClass {
        position = "absolute"
        top = 20
        left = 20
        zIndex = 1001
    }

}