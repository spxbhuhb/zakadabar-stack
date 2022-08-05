/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import zakadabar.rui.kotlin.plugin.transform.RuiClassSymbols

interface RuiFragmentBuilder : RuiBuilder {

    val symbolMap : RuiClassSymbols
    val propertyBuilder : RuiPropertyBuilder

    /**
     * Builds the fragment. Runs after all classes in the module fragment are
     * transformed into RuiClass. This ensures that references to other Rui
     * classes can be resolved properly.
     */
    fun build() {
        throw NotImplementedError()
    }

}