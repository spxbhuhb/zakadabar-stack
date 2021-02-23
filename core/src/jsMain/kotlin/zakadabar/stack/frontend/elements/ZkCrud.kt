/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.application.AppRouting
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.application.NavState
import zakadabar.stack.frontend.builtin.CoreClasses
import zakadabar.stack.frontend.builtin.form.FormMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.builtin.util.NYI
import zakadabar.stack.frontend.util.newInstance
import kotlin.reflect.KClass

/**
 * Provides common functions used in most CRUD implementations.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkCrud<T : RecordDto<T>> : AppRouting.ZkTarget {

    override var viewName = "${this::class.simpleName}"

    lateinit var companion: RecordDtoCompanion<T>
    lateinit var dtoClass: KClass<T>
    lateinit var formClass: KClass<out ZkForm<T>>
    lateinit var tableClass: KClass<out ZkTable<T>>

    val allPath
        get() = "/$viewName/all"
    val createPath
        get() = "/$viewName/create"
    val readPath
        get() = "/$viewName/read"
    val updatePath
        get() = "/$viewName/update"
    val deletePath
        get() = "/$viewName/delete"

    open fun openAll() = Application.changeNavState(allPath)
    open fun openCreate() = Application.changeNavState(createPath)
    open fun openRead(recordId: RecordId<T>) = Application.changeNavState(readPath, "id=$recordId")
    open fun openUpdate(recordId: RecordId<T>) = Application.changeNavState(updatePath, "id=$recordId")
    open fun openDelete(recordId: RecordId<T>) = Application.changeNavState(deletePath, "id=$recordId")

    override fun route(routing: AppRouting, state: NavState) = when (state.urlPath) {
        allPath -> all()
        createPath -> create()
        readPath -> read(state.recordId)
        updatePath -> update(state.recordId)
        deletePath -> delete(state.recordId)
        else -> routeNonCrud(routing, state)
    }

    open fun routeNonCrud(routing: AppRouting, state: NavState): ZkElement = NYI()

    open fun all(): ZkElement = ZkElement.launchBuildNew {

        classList += CoreClasses.coreClasses.layoutContent

        + tableClass.newInstance().setData(companion.comm.all())
    }

    open fun create(): ZkElement {

        val dto = dtoClass.newInstance()
        dto.schema().setDefaults()

        val form = formClass.newInstance()
        form.dto = dto
        form.openUpdate = { openUpdate(it.id) }
        form.mode = FormMode.Create

        return form
    }

    open fun read(recordId: Long): ZkElement = ZkElement.launchBuildNew {

        classList += CoreClasses.coreClasses.layoutContent

        val form = formClass.newInstance()
        form.dto = companion.read(recordId)
        form.openUpdate = { openUpdate(it.id) }
        form.mode = FormMode.Read

        + form
    }

    open fun update(recordId: Long): ZkElement = ZkElement.launchBuildNew {

        classList += CoreClasses.coreClasses.layoutContent

        val form = formClass.newInstance()
        form.dto = companion.read(recordId)
        form.openUpdate = { openUpdate(it.id) }
        form.mode = FormMode.Update

        + form
    }

    open fun delete(recordId: Long): ZkElement = ZkElement.launchBuildNew {

        classList += CoreClasses.coreClasses.layoutContent

        val form = formClass.newInstance()
        form.dto = companion.read(recordId)
        form.openUpdate = { openUpdate(it.id) }
        form.mode = FormMode.Delete

        + form
    }
}
