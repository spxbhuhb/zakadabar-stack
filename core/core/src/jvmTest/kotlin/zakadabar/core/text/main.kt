/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.text

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val source = """
            * [Introduction](./Introduction.md)
            * [Common]()
                * [Modules](./common/Modules.md)
                * [Data](./common/Data.md)
                * [Strings](./common/Strings.md)
                * [URLs](./common/URLs.md)
            * [Backend]()
                * [Introduction](./backend/Introduction.md)
                * [Business Logic](./backend/BusinessLogic.md)
                * [Persistence API](./backend/PersistenceApi.md)
                * [Auditor](./backend/Auditor.md)
                * [Authorizer](./backend/Authorizer.md)
                * [Routing](./backend/Routing.md)
                * [Custom Backends](./backend/CustomBackends.md)
                * [Settings](./backend/Settings.md)
                * [Logging](./backend/Logging.md)
                * [HTTP Server](./backend/HttpServer.md)
            * [Android]()
                * [Introduction](./android/Introduction.md)
            * [Browser]()
                * [Introduction](./browser/Introduction.md)
                * [Structure]()
                    * [Themes, Css](./browser/structure/ThemesCss.md)
                    * [Elements](./browser/structure/Elements.md)
                    * [Layout](./browser/structure/Layout.md)
                    * [Routing](./browser/structure/Routing.md)
                    * [Services](./browser/structure/Services.md)
                    * [z-index](./browser/structure/zIndex.md)
                * [Built-In]()
                    * [Buttons](./browser/builtin/Buttons.md)
                    * [Dock](./browser/builtin/Dock.md)
                    * [Crud](./browser/builtin/Crud.md)
                    * [Forms](./browser/builtin/Forms.md)
                    * [Icons](./browser/builtin/Icons.md)
                    * [Modals](./browser/builtin/Modals.md)
                    * [Notes](./browser/builtin/Notes.md)
                    * [Pages](./browser/builtin/Pages.md)
                    * [SideBar](./browser/builtin/SideBar.md)
                    * [TabContainer](./browser/builtin/TabContainer.md)
                    * [Tables](./browser/builtin/Tables.md)
                    * [Toasts](./browser/builtin/Toasts.md)
                * [Util]()
                    * [Prototyping](./browser/util/Prototyping.md)
            * [Plug-And-Play]()
                * [Introduction](./plug-and-play/Introduction.md)
                * [Accounts]()
                    * [Introduction](./plug-and-play/accounts/Introduction.md)
                    * [Use](./plug-and-play/accounts/Use.md)
                    * [Sessions](./plug-and-play/accounts/Sessions.md)
                * [Blobs]()
                  * [Introduction](./plug-and-play/blobs/Introduction.md)
                * [I18N]()
                  * [Introduction](./plug-and-play/i18n/Introduction.md)
            * [Testing]()
                * [Backend Testing](./testing/BackendTesting.md)
            * [Tools]()
                * [Bender](./tools/Bender.md)
            * [Operation]()
                * [Deployment](./operation/Deployment.md)
            * [Site]()
                * [Introduction](./site/Introduction.md)
                * [Cookbook](./site/Cookbook.md)
        """.trimIndent()

    val text = StringBuilder()
    val items = mutableListOf<MarkdownNav.MarkdownNavItem>()

    MarkdownNav().parse(source).forEach {
        it.flatten(items, text)
    }

    println(text)
}

fun MarkdownNav.MarkdownNavItem.flatten(items: MutableList<MarkdownNav.MarkdownNavItem>, text: StringBuilder) {
    children.forEach {
        if (it.url.isNotBlank() && it.url != "./All.md") {
            val path = Paths.get("./doc/guides/${it.url}")
            text.append(Files.readAllBytes(path).decodeToString())
        }
        it.flatten(items, text)
    }
}