/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.lib.content.data.ContentBo
import zakadabar.lib.i18n.persistence.LocaleExposedTableGen

object ContentTable : ExposedPaTable<ContentBo>(
    tableName = "content"
) {

    val status = reference("status", StatusTable)
    val folder = bool("folder")
    val parent = reference("parent", this).nullable()
    val master = reference("master", this).nullable()
    val position = long("position")
    val locale = reference("locale", LocaleExposedTableGen).nullable()
    val title = varchar("title", 100)
    val seoTitle = varchar("seo_title", 100).index()

}