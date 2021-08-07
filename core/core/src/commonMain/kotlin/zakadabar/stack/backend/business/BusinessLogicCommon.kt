/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.business

import zakadabar.stack.alarm.AlarmSupport
import zakadabar.stack.backend.RoutedModule
import zakadabar.stack.backend.audit.Auditor
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.AuthorizerDelegate
import zakadabar.stack.backend.route.Router
import zakadabar.stack.backend.validate.Validator
import zakadabar.stack.data.BaseBo
import zakadabar.stack.module.CommonModule
import zakadabar.stack.util.PublicApi

/**
 * Base class for entity backends. Supports CRUD, actions and queries.
 */
abstract class BusinessLogicCommon<T : BaseBo> : CommonModule, RoutedModule {

    /**
     * The namespace this backend serves. Must be unique in a server. Default
     * is the namespace of the BO class. Namespace clashes abort server
     * startup.
     */
    abstract val namespace: String

    // -------------------------------------------------------------------------
    // Router
    // -------------------------------------------------------------------------

    /**
     * Routes incoming requests to the proper processor function.
     */
    open val router: Router<T> = router { }

    /**
     * Provides a router when none specified explicitly.
     */
    open fun routerProvider() = routerProvider

    /**
     * Convenience function to get a router from a provider and run a
     * customization function.
     */
    fun router(build: Router<T>.() -> Unit): Router<T> {
        val r = routerProvider().businessLogicRouter(this)
        r.build()
        return r
    }

    // -------------------------------------------------------------------------
    // Validator
    // -------------------------------------------------------------------------

    /**
     * Create and update operations validate the received BO with this validator
     * by calling the `validateCreate` and `validateUpdate` methods.
     */
    @PublicApi
    open val validator: Validator<T> = validator { }

    /**
     * Provides an validator in none specified explicitly.
     */
    open fun validatorProvider() = validatorProvider

    /**
     * Convenience function to get a validator from a provider and run a
     * customization function.
     */
    @PublicApi
    fun validator(build: Validator<T>.() -> Unit): Validator<T> {
        val r = validatorProvider().businessLogicRouter(this)
        r.build()
        return r
    }

    // -------------------------------------------------------------------------
    // Authorizer
    // -------------------------------------------------------------------------

    /**
     * Authorizer to call for operation authorization.
     */
    abstract val authorizer: Authorizer<T>

    /**
     * Convenience function to get an authorizer from a provider, and customize
     * it with a function.
     */
    fun provider(build: (Authorizer<T>.() -> Unit)? = null) = AuthorizerDelegate(build)

    // -------------------------------------------------------------------------
    // Auditor
    // -------------------------------------------------------------------------

    /**
     * Audit records (logs) are created by this auditor.
     */
    open val auditor: Auditor<T> = auditor { }

    /**
     * Provides an auditor in none specified explicitly.
     */
    open fun auditorProvider() = auditorProvider

    /**
     * Convenience function to get an auditor from the provider and run a
     * customization function.
     */
    fun auditor(build: Auditor<T>.() -> Unit): Auditor<T> {
        val a = auditorProvider().businessLogicAuditor(this)
        a.build()
        return a
    }

    // -------------------------------------------------------------------------
    // Alarm Support
    // -------------------------------------------------------------------------

    /**
     * Provides functions to create and manage alarms.
     */
    open val alarmSupport: AlarmSupport = alarmSupport { }

    /**
     * Provides an alarm support in none specified explicitly.
     */
    open fun alarmSupportProvider() = alarmSupportProvider

    /**
     * Convenience function to get an alarm support from the provider and run a
     * customization function.
     */
    fun alarmSupport(build: AlarmSupport.() -> Unit): AlarmSupport {
        val a = alarmSupportProvider().businessLogicAlarmSupport(this)
        a.build()
        return a
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    override fun onModuleLoad() {}

    override fun onModuleStart() {
        authorizer.onModuleStart()
    }

    override fun onInstallRoutes(route: Any) {
        router.installRoutes(route)
    }


}