/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table

import kotlinx.coroutines.Job
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.log
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

open class ZkTablePreload<T : Any>(val loader: suspend () -> T) : ReadOnlyProperty<ZkTable<*>, T> {

    protected lateinit var table: ZkTable<*>

    protected lateinit var value: T

    internal lateinit var job: Job

    operator fun provideDelegate(table: ZkTable<*>, prop: KProperty<*>): ReadOnlyProperty<ZkTable<*>, T> {
        this.table = table
        table.preloads += this

        job = io {
            try {
                value = loader()
            } catch (ex: Exception) {
                log(ex)
                throw ex
            }
        }

        return this
    }

    override fun getValue(thisRef: ZkTable<*>, property: KProperty<*>) = value

}