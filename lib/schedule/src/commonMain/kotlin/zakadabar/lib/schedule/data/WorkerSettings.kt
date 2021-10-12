/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema
import zakadabar.core.util.UUID

/**
 * Settings for a worker.
 *
 * @param  dispatcherUrl    URL of the dispatcher.
 * @param  nodeUrl          The root URL of the node that creates the subscription.
 * @param  nodeId           The ID of the node that creates this subscription.
 * @param  actionNamespace  Filters the jobs this subscription applies to.
 * @param  actionType       Filters the jobs this subscription appies to.
 */
@Serializable
class WorkerSettings(

    var dispatcherUrl: String? = null,
    var nodeUrl: String? = null,
    var nodeId: UUID = UUID(),
    var actionNamespace: String? = null,
    var actionType: String? = null

) : BaseBo {

    override fun schema() = BoSchema {
        + ::dispatcherUrl default dispatcherUrl
        + ::nodeUrl default nodeUrl
        + ::nodeId default nodeId
        + ::actionNamespace default actionNamespace
        + ::actionType default actionType
    }

}