/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.note

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.icon.ZkIcon
import zakadabar.core.frontend.resources.ZkFlavour
import zakadabar.core.frontend.resources.ZkIcons
import zakadabar.core.frontend.resources.css.ZkCssStyleRule
import zakadabar.core.frontend.util.plusAssign

open class ZkNote(
    val flavour: ZkFlavour = ZkFlavour.Info,
    element: HTMLElement = document.createElement("div") as HTMLElement,
    val icon: ZkElement? = null,
    val titleClass: ZkCssStyleRule? = null,
    val innerClass: ZkCssStyleRule? = null
) : ZkElement(element) {

    open val titleContainer = ZkElement()
    open val contentContainer = ZkElement()

    constructor(flavour: ZkFlavour, title: String, text: String) : this(flavour) {
        this.title = title
        this.text = text
    }

    constructor(flavour: ZkFlavour, title: String, content: ZkElement) : this(flavour) {
        this.title = title
        this.content = content
    }

    constructor(element: HTMLElement, flavour: ZkFlavour, builder: ZkNote.() -> Unit) : this(flavour, element) {
        this.builder()
    }

    var title: String? = null
        set(value) {
            field = value
            titleContainer.clear()
            field?.let {
                titleContainer.innerText = it
            }
        }

    var text: String? = null
        set(value) {
            field = value
            contentContainer.clear()
            field?.let { contentContainer.innerText = it }
        }

    var content: ZkElement? = null
        set(value) {
            field = value
            contentContainer.clear()
            field?.let { contentContainer += it }
        }

    @Suppress("DuplicatedCode") // no idea how to bring these two together
    override fun onCreate() {
        super.onCreate()

        classList += zkNoteStyles.noteOuter

        val finalIcon: ZkElement
        val finalInnerClass: ZkCssStyleRule
        val finalTitleClass: ZkCssStyleRule

        when (flavour) {
            ZkFlavour.Primary -> {
                finalIcon = icon ?: ZkIcon(ZkIcons.info)
                finalTitleClass = titleClass ?: zkNoteStyles.primaryTitle
                finalInnerClass = innerClass ?: zkNoteStyles.primaryInner
            }
            ZkFlavour.Secondary -> {
                finalIcon = icon ?: ZkIcon(ZkIcons.info)
                finalTitleClass = titleClass ?: zkNoteStyles.secondaryTitle
                finalInnerClass = innerClass ?: zkNoteStyles.secondaryInner
            }
            ZkFlavour.Success -> {
                finalIcon = icon ?: ZkIcon(ZkIcons.checkCircle)
                finalTitleClass = titleClass ?: zkNoteStyles.successTitle
                finalInnerClass = innerClass ?: zkNoteStyles.successInner
            }
            ZkFlavour.Warning -> {
                finalIcon = icon ?: ZkIcon(ZkIcons.warningAmber)
                finalTitleClass = titleClass ?: zkNoteStyles.warningTitle
                finalInnerClass = innerClass ?: zkNoteStyles.warningInner
            }
            ZkFlavour.Danger -> {
                finalIcon = icon ?: ZkIcon(ZkIcons.report)
                finalTitleClass = titleClass ?: zkNoteStyles.dangerTitle
                finalInnerClass = innerClass ?: zkNoteStyles.dangerInner
            }
            ZkFlavour.Info -> {
                finalIcon = icon ?: ZkIcon(ZkIcons.info)
                finalTitleClass = titleClass ?: zkNoteStyles.infoTitle
                finalInnerClass = innerClass ?: zkNoteStyles.infoInner
            }
            else -> {
                finalIcon = requireNotNull(icon) { "toast icon cannot be null when flavour is Custom" }
                finalTitleClass = requireNotNull(titleClass) { "toast iconClass cannot be null when flavour is Custom" }
                finalInnerClass = requireNotNull(innerClass) { "toast innerClass cannot be null when flavour is Custom" }
            }
        }

        + div(zkNoteStyles.noteInner) {

            classList += finalInnerClass

            + row(zkNoteStyles.titleOuter) {
                classList += finalTitleClass
                + finalIcon css zkNoteStyles.titleIcon
                + titleContainer
            }

            + contentContainer css zkNoteStyles.contentOuter

        }

    }
}