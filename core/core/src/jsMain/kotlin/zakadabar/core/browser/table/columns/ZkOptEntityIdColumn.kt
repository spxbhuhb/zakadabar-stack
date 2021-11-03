/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.resource.localizedStrings
import kotlin.reflect.KProperty1

open class ZkOptEntityIdColumn<T : BaseBo, IT>(
    table: ZkTable<T>,
    val prop: KProperty1<T, EntityId<IT>?>
) : ZkOptEntityIdColumnV2<T, IT>(
    table,
    { row -> prop.get(row) }
) {

    override fun onCreate() {
        label = localizedStrings.getNormalized(prop.name)
        super.onCreate()
    }

}