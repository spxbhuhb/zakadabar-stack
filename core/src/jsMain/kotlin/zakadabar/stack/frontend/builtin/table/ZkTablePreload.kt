/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import zakadabar.stack.frontend.util.log
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ZkTablePreload<T : Any>(val loader: suspend () -> T) : ReadOnlyProperty<ZkTable<*>, T> {

    protected lateinit var table: ZkTable<*>

    protected lateinit var value: T

    internal lateinit var job: Job

    operator fun provideDelegate(table: ZkTable<*>, prop: KProperty<*>): ReadOnlyProperty<ZkTable<*>, T> {
        this.table = table
        table.preloads += this

        job = GlobalScope.launch {
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