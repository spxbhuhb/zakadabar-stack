/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.builtin

import org.jetbrains.exposed.sql.selectAll

class BuiltinExposedPa : BuiltinExposedPaGen() {

    fun count() = table.selectAll().count()

}