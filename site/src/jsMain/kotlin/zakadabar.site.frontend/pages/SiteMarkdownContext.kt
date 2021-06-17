/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages

import org.intellij.markdown.html.resolveToStringSafe
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import zakadabar.lib.examples.frontend.OperatorExample
import zakadabar.lib.examples.frontend.ParallelDownloadExample
import zakadabar.lib.examples.frontend.button.ButtonExamples
import zakadabar.lib.examples.frontend.crud.CrudBuiltinExample
import zakadabar.lib.examples.frontend.crud.CrudReferenceExample
import zakadabar.lib.examples.frontend.dock.DockBasicExample
import zakadabar.lib.examples.frontend.dock.DockRemoveExample
import zakadabar.lib.examples.frontend.form.FormBooleanExample
import zakadabar.lib.examples.frontend.form.FormDoubleExample
import zakadabar.lib.examples.frontend.form.FormRecordIdExample
import zakadabar.lib.examples.frontend.form.FormStringExample
import zakadabar.lib.examples.frontend.icon.IconExamples
import zakadabar.lib.examples.frontend.input.IntCheckboxListExample
import zakadabar.lib.examples.frontend.layout.TabContainerExample
import zakadabar.lib.examples.frontend.misc.NYIExample
import zakadabar.lib.examples.frontend.modal.ModalExamples
import zakadabar.lib.examples.frontend.note.NoteBasicExamples
import zakadabar.lib.examples.frontend.note.NoteFormExample
import zakadabar.lib.examples.frontend.pages.PageExample
import zakadabar.lib.examples.frontend.sidebar.SideBarExample
import zakadabar.lib.examples.frontend.sidebar.SideBarMarkdownExample
import zakadabar.lib.examples.frontend.table.TableExample
import zakadabar.lib.examples.frontend.theme.ThemeExample
import zakadabar.lib.examples.frontend.toast.ToastAutoHideExample
import zakadabar.lib.examples.frontend.toast.ToastBasicExamples
import zakadabar.lib.examples.frontend.toast.ToastCustomExample
import zakadabar.lib.examples.frontend.toast.ToastFormExample
import zakadabar.lib.markdown.frontend.flavour.ZkMarkdownContext
import zakadabar.site.frontend.components.ThemeShowCase
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.note.ZkNote
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.theme

class SiteMarkdownContext(
    viewName: String,
    val path: String
) : ZkMarkdownContext(baseURI = "/${application.locale}/$viewName/$path") {

    private val github = "https://github.com/spxbhuhb/zakadabar-stack/tree/master"
    private val coreSrc = "${github}/core/"
    private val libSrc = "${github}/lib/"
    private val siteSrc = "${github}/site/"

    override fun makeUrl(destination: CharSequence): String {

        val dest = destination.toString()

        return when {
            dest.startsWith('#') -> dest
            dest.startsWith("/src") -> resolveToStringSafe(coreSrc, dest.trim('/'))
            dest.contains("../lib/") -> resolveToStringSafe(libSrc, dest.substringAfter("../lib/").trim('/'))
            dest.contains("../site/") -> resolveToStringSafe(siteSrc, dest.substringAfter("../site/").trim('/'))
            dest.contains("/guides/") -> resolveToStringSafe("/${application.locale}/Documentation/guides/", dest.substringAfter("/guides/").trim('/'))
            dest.endsWith(".png") -> resolveToStringSafe("/api/content/$path", dest.trim('/'))
            dest.endsWith(".jpg") -> resolveToStringSafe("/api/content/$path", dest.trim('/'))
            else -> baseURI?.resolveToStringSafe(dest) ?: dest
        }
    }

    override fun enrich(htmlElement: HTMLElement): ZkElement? {
        val type = htmlElement.dataset["zkEnrich"] ?: return null
        val flavour = htmlElement.dataset["zkFlavour"]?.let { ZkFlavour.valueOf(it.capitalize()) } ?: ZkFlavour.Primary

        return when (type) {

            "ButtonExamples" -> ButtonExamples(htmlElement, flavour = flavour)

            "CrudBuiltinExample" -> CrudBuiltinExample(htmlElement)
            "CrudReferenceExample" -> CrudReferenceExample(htmlElement)

            "DockBasicExample" -> DockBasicExample(htmlElement)
            "DockRemoveExample" -> DockRemoveExample(htmlElement)

            "IntCheckboxListExample" -> IntCheckboxListExample(htmlElement)

            "FormBooleanExample" -> FormBooleanExample(htmlElement)
            "FormDoubleExample" -> FormDoubleExample(htmlElement)
            "FormRecordIdExample" -> FormRecordIdExample(htmlElement)
            "FormStringExample" -> FormStringExample(htmlElement)

            "IconExamples" -> IconExamples(htmlElement)

            "Note" -> ZkNote(htmlElement, flavour) {
                title = htmlElement.dataset["zkTitle"]
                content = zke { innerHTML = htmlElement.innerHTML }
                htmlElement.innerHTML = ""
            }

            "NoteBasicExamples" -> NoteBasicExamples(htmlElement)
            "NoteFormExample" -> NoteFormExample(htmlElement)

            "NYIExample" -> NYIExample(htmlElement)

            "ModalExamples" -> ModalExamples(htmlElement)

            "OperatorExample" -> OperatorExample(htmlElement)

            "PageExample" -> PageExample(htmlElement)

            "ParallelDownloadExample" -> ParallelDownloadExample(htmlElement)

            "SideBarExample" -> SideBarExample(htmlElement)
            "SideBarMarkdownExample" -> SideBarMarkdownExample(htmlElement)

            "TabContainerExample" -> TabContainerExample(htmlElement)

            "TableExample" -> TableExample(htmlElement)

            "ThemeExample" -> ThemeExample(htmlElement)
            "ThemeShowCase" -> ThemeShowCase(htmlElement)

            "ToastAutoHideExample" -> ToastAutoHideExample(htmlElement)
            "ToastCustomExample" -> ToastCustomExample(htmlElement)
            "ToastBasicExamples" -> ToastBasicExamples(htmlElement)
            "ToastFormExample" -> ToastFormExample(htmlElement)

            else -> null
        }?.marginBottom(theme.spacingStep)
    }

}