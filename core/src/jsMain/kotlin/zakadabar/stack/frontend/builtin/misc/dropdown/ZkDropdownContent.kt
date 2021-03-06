/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.misc.dropdown

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.misc.dropdown.ZkDropdownClasses.Companion.dropdownClasses

/**
 * Wrapper around the content of the dropdown.
 *
 * @since 2020.9.14
 */
class ZkDropdownContent(
    private val content: ZkElement
) : ZkElement() {

    override fun onCreate() {

        // kept it this way so we have a clear separation and wrapper around the content

        this withCss dropdownClasses.dropdownContent build {
            + content
        }

    }

}