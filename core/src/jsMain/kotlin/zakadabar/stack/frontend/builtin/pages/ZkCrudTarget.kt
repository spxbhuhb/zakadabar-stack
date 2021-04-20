/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages

import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkNavState
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.misc.NYI
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.newInstance
import zakadabar.stack.frontend.util.plusAssign
import kotlin.reflect.KClass

/**
 * Provides common functions used in most CRUD implementations.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkCrudTarget<T : RecordDto<T>> : ZkAppRouting.ZkTarget {

    override var viewName = "${this::class.simpleName}"

    lateinit var companion: RecordDtoCompanion<T>
    lateinit var dtoClass: KClass<T>
    lateinit var pageClass: KClass<out ZkCrudPage<T>>
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

    open fun openAll() = ZkApplication.changeNavState(allPath)
    open fun openCreate() = ZkApplication.changeNavState(createPath)
    open fun openRead(recordId: RecordId<T>) = ZkApplication.changeNavState(readPath, "id=$recordId")
    open fun openUpdate(recordId: RecordId<T>) = ZkApplication.changeNavState(updatePath, "id=$recordId")
    open fun openDelete(recordId: RecordId<T>) = ZkApplication.changeNavState(deletePath, "id=$recordId")

    override fun route(routing: ZkAppRouting, state: ZkNavState) = when (state.urlPath) {
        allPath -> all()
        createPath -> create()
        readPath -> read(state.recordId)
        updatePath -> update(state.recordId)
        deletePath -> delete(state.recordId)
        else -> routeNonCrud(routing, state)
    }

    open fun routeNonCrud(routing: ZkAppRouting, state: ZkNavState): ZkElement = NYI()

    open fun all(): ZkElement = ZkElement.launchBuildNew {

        classList += ZkPageStyles.fixed

        + tableClass.newInstance().setData(companion.comm.all())
    }

    open fun create(): ZkElement {
        val container = ZkElement()

        container.classList += ZkPageStyles.scrollable

        val dto = dtoClass.newInstance()
        dto.schema().setDefaults()

        val page = pageClass.newInstance()
        page.dto = dto
        page.openUpdate = { openUpdate(it.id) }
        page.mode = ZkElementMode.Create

        page as ZkElement

        container build {
            + div(ZkPageStyles.content) {
                + page
            }
        }

        return container
    }

    open fun read(recordId: Long): ZkElement {

        val container = ZkElement()
        container.classList += ZkPageStyles.scrollable

        io {
            val page = pageClass.newInstance()
            page.dto = companion.read(recordId)
            page.openUpdate = { openUpdate(it.id) }
            page.mode = ZkElementMode.Read

            page as ZkElement

            container build {
                + div(ZkPageStyles.content) {
                    + page
                }
            }

        }

        return container
    }

    open fun update(recordId: Long): ZkElement {

        val container = ZkElement()

        container.classList += ZkPageStyles.scrollable

        io {
            val page = pageClass.newInstance()
            page.dto = companion.read(recordId)
            page.openUpdate = { openUpdate(it.id) }
            page.mode = ZkElementMode.Update

            page as ZkElement

            container build {
                + div(ZkPageStyles.content) {
                    + page
                }
            }

        }

        return container
    }

    open fun delete(recordId: Long): ZkElement {

        val container = ZkElement()

        container.classList += ZkPageStyles.scrollable

        io {
            val page = pageClass.newInstance()
            page.dto = companion.read(recordId)
            page.openUpdate = { openUpdate(it.id) }
            page.mode = ZkElementMode.Delete

            page as ZkElement

            container build {
                + div(ZkPageStyles.content) {
                    + page
                }
            }

        }

        return container
    }
}
