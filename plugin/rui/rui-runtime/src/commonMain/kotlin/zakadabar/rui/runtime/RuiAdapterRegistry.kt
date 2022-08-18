/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

object RuiAdapterRegistry {

    private val factories = mutableListOf<RuiAdapterFactory>()

    fun register(factory : RuiAdapterFactory) {
        factories += factory
    }

    @Suppress("unused") // used by generated code
    fun adapterFor(vararg args : Any?) : RuiAdapter<*> {
        for (factory in factories) {
            factory.accept(*args)?.let {
                return it
            }
        }
        throw NotImplementedError("no adapter accepted the specified parameters")
    }

}