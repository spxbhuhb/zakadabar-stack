/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import org.jetbrains.exposed.sql.Table
import zakadabar.lib.i18n.backend.LocaleExposedTableGen

object StereotypeLocalizationTable : Table("content_stereotype_localization") {

    internal val stereotype = reference("stereotype", StereotypeExposedTableGen).index()
    internal val locale = reference("locale", LocaleExposedTableGen)
    internal val localizedName = text("localized_name").index()

}