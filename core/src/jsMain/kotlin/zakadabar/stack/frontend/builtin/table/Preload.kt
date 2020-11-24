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

class Preload<T : Any>(val loader: suspend () -> T) : ReadOnlyProperty<Table<*>, T> {

    protected lateinit var table: Table<*>

    protected lateinit var value: T

    internal lateinit var job: Job

    operator fun provideDelegate(table: Table<*>, prop: KProperty<*>): ReadOnlyProperty<Table<*>, T> {
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

    override fun getValue(thisRef: Table<*>, property: KProperty<*>) = value

}