/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import org.jetbrains.exposed.sql.Table

object TextBlockTable : Table("content_text") {

    val content = reference("content", ContentTable).index()
    val stereotype = varchar("stereotype", 100)
    val value = text("value")

}