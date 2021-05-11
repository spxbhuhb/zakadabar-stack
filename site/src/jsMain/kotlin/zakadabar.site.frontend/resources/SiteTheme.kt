/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.lib.markdown.frontend.MarkdownThemeExt
import zakadabar.stack.frontend.resources.ZkTheme

interface SiteTheme : ZkTheme, MarkdownThemeExt {
    val developerLogo: String
}