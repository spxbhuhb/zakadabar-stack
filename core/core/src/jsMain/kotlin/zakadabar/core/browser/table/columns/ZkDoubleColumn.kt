/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.resource.localizedStrings
import kotlin.reflect.KProperty1

open class ZkDoubleColumn<T : BaseBo>(
    table: ZkTable<T>,
    val prop: KProperty1<T, Double>
) : ZkDoubleColumnV2<T>(
    table,
    { row -> prop.get(row) }
) {

    override fun onCreate() {
        label = localizedStrings.getNormalized(prop.name)
        super.onCreate()
    }

}