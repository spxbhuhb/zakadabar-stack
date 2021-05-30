/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.builtin

import org.jetbrains.exposed.sql.selectAll

class ExampleReferenceExposedPa : ExampleReferenceExposedPaGen() {

    fun count() = table.selectAll().count()

}