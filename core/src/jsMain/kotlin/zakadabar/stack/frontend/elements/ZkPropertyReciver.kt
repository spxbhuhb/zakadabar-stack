/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.util.NYI
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

interface ZkPropertyReceiver {

    fun add(kProperty0: KProperty0<RecordId<*>>): ZkElement = NYI()
    fun add(kProperty0: KMutableProperty0<String>): ZkElement = NYI()
    fun add(kProperty0: KMutableProperty0<Double>): ZkElement = NYI()

}