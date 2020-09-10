/*
 * Copyright © 2020, Simplexion, Hungary and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package zakadabar.stack.frontend.elements

import org.w3c.dom.DOMTokenList
import org.w3c.dom.HTMLElement

operator fun DOMTokenList.plusAssign(token: String?) {
    if (token != null) this.add(token)
}

operator fun DOMTokenList.plusAssign(tokens: Array<out String>) {
    tokens.forEach { this.add(it) }
}

operator fun DOMTokenList.minusAssign(token: String?) {
    if (token != null) this.remove(token)
}

 infix fun HTMLElement.marginRight(size: Any): HTMLElement {
    this.style.marginRight = if (size is Int) "${size}px" else size.toString()
    return this
}

 infix fun HTMLElement.marginBottom(size: Any): HTMLElement {
    this.style.marginBottom = if (size is Int) "${size}px" else size.toString()
    return this
}

 infix fun HTMLElement.width(value: Any): HTMLElement {
    if (value == "100%") {
        classList += CoreClasses.coreClasses.w100
    } else {
        this.style.width = value.toString()
    }
    return this
}

 infix fun HTMLElement.height(value: Any): HTMLElement {
     if (value is String) {
         this.style.height = value
     } else {
         this.style.height = "${value}px"
     }
     return this
 }

 infix fun HTMLElement.flex(value: String): HTMLElement {
    if (value == "grow") {
        classList += CoreClasses.coreClasses.grow
    } else {
        throw RuntimeException("invalid flex value: $value")
    }
    return this
}