/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import org.intellij.markdown.html.resolveToStringSafe
import zakadabar.lib.markdown.frontend.flavour.ZkMarkdownContext

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
            dest.endsWith(".png") -> resolveToStringSafe("/content/$path", dest.trim('/'))
            dest.endsWith(".jpg") -> resolveToStringSafe("/content/$path", dest.trim('/'))
            else -> baseURI?.resolveToStringSafe(dest) ?: dest
        }
    }

}
