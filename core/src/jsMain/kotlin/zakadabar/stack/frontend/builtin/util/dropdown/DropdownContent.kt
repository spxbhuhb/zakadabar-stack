/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util.dropdown

import zakadabar.stack.frontend.builtin.util.dropdown.DropdownClasses.Companion.dropdownClasses
import zakadabar.stack.frontend.elements.ZkElement

/**
 * Wrapper around the content of the dropdown.
 *
 * @since 2020.9.14
 */
class DropdownContent(
    private val content: ZkElement
) : ZkElement() {

    override fun init(): DropdownContent {

        // kept it this way so we have a clear separation and wrapper around the content

        this cssClass dropdownClasses.dropdownContent build {
            + content
        }

        return this
    }

}