/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.misc.dropdown

import zakadabar.core.browser.ZkElement

/**
 * Wrapper around the content of the dropdown.
 *
 * @since 2020.9.14
 */
class ZkDropdownContent(
    val content: ZkElement
) : ZkElement() {

    override fun onCreate() {

        // kept it this way so we have a clear separation and wrapper around the content

        this css zkDropdownStyles.dropdownContent build {
            + content
        }

    }

}