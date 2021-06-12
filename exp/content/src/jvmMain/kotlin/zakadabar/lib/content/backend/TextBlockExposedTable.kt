/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import org.jetbrains.exposed.sql.Table

object TextBlockExposedTable : Table("content_text") {

    internal val content = reference("content", ContentExposedTable).index()
    internal val stereotype = reference("stereotype", StereotypeExposedTableGen)
    internal val value = text("value")

}