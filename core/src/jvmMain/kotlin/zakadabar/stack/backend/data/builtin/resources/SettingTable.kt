/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import org.jetbrains.exposed.dao.id.LongIdTable

object SettingTable : LongIdTable("settings") {

    var namespace = varchar("namespace", 100)
    val className = varchar("path", 100)
    val descriptor = text("descriptor")

    // I decided not to have a toBo here to avoid confusion
    // about the descriptor. SettingBackend performs quite
    // a few special operations, so it is better to have
    // everything there.
}