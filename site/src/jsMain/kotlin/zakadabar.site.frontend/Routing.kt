/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.site.frontend

import zakadabar.cookbook.browser.pages.argpage.ArgPage
import zakadabar.site.frontend.cookbook.Cookbook
import zakadabar.site.frontend.pages.*
import zakadabar.stack.frontend.application.ZkAppRouting

const val contentNamespace = "content"

class Routing : ZkAppRouting(DefaultLayout, Landing) {

    init {
        + Landing
        + WhatsNew
        + Documentation
        + ChangeLog
        + GetStarted
        + GetHelp
        + Welcome
        + ShowCase
        + Roadmap
        + DocumentationIntro
        + FAQ
        + LegalNotices
        + Credits
        + ServicesAndSupport
        + Experimental
        + ProjectStatus
        + Versioning
        + Upgrade
        + BenderPage
        + Cookbook
        + BuildAndRelease

        + ArgPage()
    }

}