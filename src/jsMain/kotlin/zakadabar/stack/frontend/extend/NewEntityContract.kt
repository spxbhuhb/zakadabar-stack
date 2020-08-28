/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.extend

import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.frontend.builtin.icon.Icons

/**
 * A contract for entity builders.
 */
abstract class NewEntityContract : ScopedViewContract() {

    open fun supportsParent(dto: EntityDto?) = true

    abstract val name: String

    open val icon = Icons.description.simple18

}