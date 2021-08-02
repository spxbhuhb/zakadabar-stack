/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.module

import zakadabar.stack.util.InstanceStore

open class ModuleStore : InstanceStore<CommonModule>() {

    override operator fun plusAssign(instance: CommonModule) {
        instances.add(instance)
        instance.onModuleLoad()
        moduleLogger.info("loaded module $instance")
    }

}