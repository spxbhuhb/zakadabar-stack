/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.business

import zakadabar.core.alarm.AlarmSupport
import zakadabar.core.backend.RoutedModule
import zakadabar.core.backend.audit.Auditor
import zakadabar.core.backend.authorize.Authorizer
import zakadabar.core.backend.authorize.AuthorizerDelegate
import zakadabar.core.backend.authorize.Executor
import zakadabar.core.backend.route.Router
import zakadabar.core.backend.validate.Validator
import zakadabar.core.data.BaseBo
import zakadabar.core.data.action.ActionBo
import zakadabar.core.data.query.QueryBo
import zakadabar.core.module.CommonModule
import zakadabar.core.util.PublicApi

/**
 * Base class for entity backends. Supports CRUD, actions and queries.
 */
abstract class BusinessLogicCommon<T : BaseBo> : CommonModule, RoutedModule, ActionBusinessLogicWrapper, QueryBusinessLogicWrapper {

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

    // -------------------------------------------------------------------------
    // Wrappers for actions and queries
    // -------------------------------------------------------------------------

    override fun actionWrapper(executor: Executor, func: (Executor, BaseBo) -> Any?, bo: BaseBo): Any? {

        bo as ActionBo<*>

        validator.validateAction(executor, bo)

        authorizer.authorizeAction(executor, bo)

        val response = func(executor, bo)

        auditor.auditAction(executor, bo)

        return response

    }

    override fun queryWrapper(executor: Executor, func: (Executor, BaseBo) -> Any?, bo: BaseBo): Any? {

        bo as QueryBo<*>

        validator.validateQuery(executor, bo)

        authorizer.authorizeQuery(executor, bo)

        val response = func(executor, bo)

        auditor.auditQuery(executor, bo)

        return response
    }


}