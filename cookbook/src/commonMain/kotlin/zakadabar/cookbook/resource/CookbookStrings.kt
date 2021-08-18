/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("PropertyName", "unused")

package zakadabar.cookbook.resource

import zakadabar.core.resource.ZkStringStore

internal val strings = CookbookStrings()

class CookbookStrings : ZkStringStore() {
    val exampleButton by "Example Button"
    val exampleHelpContent by "This is the content of the help dialog."
    val clicked by "A very successful button click."
}

