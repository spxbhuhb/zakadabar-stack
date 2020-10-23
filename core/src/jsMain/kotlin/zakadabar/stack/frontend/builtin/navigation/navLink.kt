/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.navigation

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.get
import org.w3c.dom.set
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.frontend.data.DtoFrontend
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass

@PublicApi
fun navLink(dtoFrontend: DtoFrontend<*>, queryClass: KClass<out QueryDto>, label: String) =
    navLink(dtoFrontend, queryClass.simpleName !!, label)

@PublicApi
fun navLink(dtoFrontend: DtoFrontend<*>, view: String, label: String): HTMLElement {
    val element = document.createElement("div") as HTMLElement
    element.dataset["target"] = "/view/${dtoFrontend.type}/$view" // to make debug easier
    element.innerText = label
    element.addEventListener("click", ::onClick)
    return element
}

@PublicApi
fun navLink(kClass: KClass<*>, label: String): HTMLElement {
    val element = document.createElement("div") as HTMLElement
    element.dataset["target"] = "/page/${kClass.simpleName}" // to make debug easier
    element.innerText = label
    element.addEventListener("click", ::onClick)
    return element
}

fun onClick(event: Event) {
    val eventTarget = event.target as? HTMLElement ?: return
    val linkTarget = eventTarget.dataset["target"] ?: return
    Navigation.changeLocation(linkTarget)
}