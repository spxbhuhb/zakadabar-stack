/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages

import org.intellij.markdown.html.resolveToStringSafe
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import zakadabar.lib.examples.frontend.modal.ModalExamples
import zakadabar.lib.examples.frontend.toast.ToastExamples
import zakadabar.lib.markdown.frontend.flavour.ZkMarkdownContext
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.note.ZkNote
import zakadabar.stack.frontend.builtin.note.ZkNoteStyles

class ContentContext(
    viewName: String,
    val path: String
) : ZkMarkdownContext(baseURI = "/$viewName/$path") {

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
            dest.contains("/guides/") -> resolveToStringSafe("/ContentPages/guides/", dest.substringAfter("/guides/").trim('/'))
            dest.endsWith(".png") -> resolveToStringSafe("/content/$path", dest.trim('/'))
            dest.endsWith(".jpg") -> resolveToStringSafe("/content/$path", dest.trim('/'))
            else -> baseURI?.resolveToStringSafe(dest) ?: dest
        }
    }

    override fun enrich(htmlElement: HTMLElement): ZkElement? {
        val type = htmlElement.dataset["zkEnrich"] ?: return null

        return when (type) {

            "InfoNote" -> ZkNote(htmlElement) {
                className = ZkNoteStyles.info
                title = htmlElement.dataset["zkTitle"]
                text = htmlElement.innerText
                htmlElement.innerText = ""
            } marginLeft - 14

            "ModalExamples" -> ModalExamples(htmlElement)

            "ToastExamples" -> ToastExamples(htmlElement)

            else -> null
        }
    }
}
