/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.stack.frontend.application.navigation.NavState
import zakadabar.stack.frontend.application.navigation.NavTargetProvider
import zakadabar.stack.frontend.elements.ZkElement

fun navTarget(func: (state: NavState) -> ZkElement) = NavTargetProvider(func)

open class AppRouting(val appModule: String) {

    fun target(func: (state: NavState) -> ZkElement) = NavTargetProvider(func)

}