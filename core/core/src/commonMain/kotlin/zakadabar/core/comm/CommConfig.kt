/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.serialization.Serializable
import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.business.EntityBusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityBo
import zakadabar.core.module.modules
import zakadabar.core.util.Lock
import zakadabar.core.util.use

/**
 * Specifies the URL or part of the URL for communication.
 *
 * @property  baseUrl     Base url of the communication, keeps the `/api/${namespace}/${operation}` part intact.
 * @property  namespace   Use this namespace instead of the one from the BO.
 * @property  fullUrl     Use this URL without adding/removing anything. For queries the query is appended.
 * @property  local       Locate and use a local Business Logic for the call, do not go over HTTP.
 *
 */
@Serializable
data class CommConfig(
    val baseUrl: String? = null,
    val namespace: String? = null,
    val fullUrl: String? = null,
    val local: Boolean = false
) {

    companion object {

        /**
         * Coroutine scope for comm coroutines.
         */
        var commScope = makeCommScope()

        /**
         * This lock is used when comm config instances are replaced. The reason
         * for the lock is to make sure that multithreaded platforms perform the
         * necessary synchronization.
         */
        var configLock = Lock()

        /**
         * A global communication configuration.
         *
         * Access to this variable is thread safe, [configLock] is used to
         * synchronize get and set.
         */
        var global = CommConfig()
            get() = configLock.use { field }
            set(value) = configLock.use { field = value }

        /**
         * Checks if a call should be shortcut locally, calling the BL directly.
         * Javascript does not support local calls at the moment, only JVM does.
         */
        fun isLocal(call: CommConfig?, bo: CommConfig?): Boolean {
            if (call != null) return call.local
            if (bo != null) return bo.local
            return global.local
        }

        /**
         * Finds an entity BL for the given namespace.
         *
         * @throws NoSuchElementException when there is no module for the given namespace
         */
        fun <T : EntityBo<T>> localEntityBl(namespace: String, call: CommConfig?, bo: CommConfig?): EntityBusinessLogicCommon<T>? {

            if (! isLocal(call, bo)) return null

            val callNamespace = call?.namespace ?: bo?.namespace ?: namespace
            return modules.first { it.namespace == callNamespace }
        }

        /**
         * Finds a common BL for the given namespace. Common BL covers action and query, for entities
         * use [localEntityBl] instead.
         *
         * @throws NoSuchElementException when there is no module for the given namespace
         */
        fun localCommonBl(namespace: String, call: CommConfig?, bo: CommConfig?): BusinessLogicCommon<BaseBo>? {

            if (! isLocal(call, bo)) return null

            val callNamespace = call?.namespace ?: bo?.namespace ?: namespace
            return modules.first { it.namespace == callNamespace }
        }

        /**
         * Merges [CommConfig]s with request specific parts to create the URL for a comm call.
         */
        fun merge(function: String, namespace: String, call: CommConfig?, bo: CommConfig?): String {

            val callBase = call?.baseUrl ?: bo?.baseUrl ?: global.baseUrl ?: ""
            val callNamespace = call?.namespace ?: bo?.namespace ?: namespace

            return "${callBase}/api/${callNamespace}$function"
        }
    }
}