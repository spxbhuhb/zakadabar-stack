/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.crud

import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.ZkNavState
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.misc.NYI
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.newInstance
import kotlin.reflect.KClass

/**
 * Routing target for CRUD pages: all, create, read, update, delete.
 * "all" uses table, others use form. Intended for top-level pages.
 * If you would like to include a crud on a page, use [ZkInlineCrud].
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkCrudTarget<T : RecordDto<T>> : ZkAppRouting.ZkTarget, ZkCrud<T> {

    override var viewName = "${this::class.simpleName}"

    lateinit var companion: RecordDtoCompanion<T>
    lateinit var dtoClass: KClass<T>
    lateinit var pageClass: KClass<out ZkCrudEditor<T>>
    lateinit var tableClass: KClass<out ZkTable<T>>

    override fun openAll() = application.changeNavState(this, "all")
    override fun openCreate() = application.changeNavState(this, "create")
    override fun openRead(recordId: RecordId<T>) = application.changeNavState(this, "read", "id=$recordId")
    override fun openUpdate(recordId: RecordId<T>) = application.changeNavState(this, "update", "id=$recordId")
    override fun openDelete(recordId: RecordId<T>) = application.changeNavState(this, "delete", "id=$recordId")

    @Suppress("UNCHECKED_CAST") // got lost in generics hell, probably fine
    override fun route(routing: ZkAppRouting, state: ZkNavState): ZkElement {
        if (state.segments.size == 2) return all()
        return when (state.segments[2]) {
            "all" -> all()
            "create" -> create()
            "read" -> read(state.recordId as RecordId<T>)
            "update" -> update(state.recordId as RecordId<T>)
            "delete" -> delete(state.recordId as RecordId<T>)
            else -> routeNonCrud(routing, state)
        }
    }

    open fun routeNonCrud(routing: ZkAppRouting, state: ZkNavState): ZkElement = NYI()

    open fun all(): ZkElement = ZkElement.launchBuildNew {

        + zkPageStyles.fixed

        val table = tableClass.newInstance()
        table.crud = this@ZkCrudTarget
        table.setData(companion.comm.all())

        + table
    }

    open fun create(): ZkElement {
        val container = ZkElement()

        val dto = dtoClass.newInstance()
        dto.schema().setDefaults()

        val page = pageClass.newInstance()
        page.dto = dto
        page.openUpdate = { openUpdate(it.id) }
        page.mode = ZkElementMode.Create

        page as ZkElement

        container build {
            + zkPageStyles.scrollable
            + div(zkPageStyles.content) {
                + page
            }
        }

        return container
    }

    open fun read(recordId: RecordId<T>): ZkElement {

        val container = ZkElement()

        io {
            val page = pageClass.newInstance()
            page.dto = companion.read(recordId)
            page.openUpdate = { openUpdate(it.id) }
            page.mode = ZkElementMode.Read

            page as ZkElement

            container build {
                + zkPageStyles.scrollable
                + div(zkPageStyles.content) {
                    + page
                }
            }

        }

        return container
    }

    open fun update(recordId: RecordId<T>): ZkElement {

        val container = ZkElement()

        io {
            val page = pageClass.newInstance()
            page.dto = companion.read(recordId)
            page.openUpdate = { openUpdate(it.id) }
            page.mode = ZkElementMode.Update

            page as ZkElement

            container build {
                + zkPageStyles.scrollable
                + div(zkPageStyles.content) {
                    + page
                }
            }

        }

        return container
    }

    open fun delete(recordId: RecordId<T>): ZkElement {

        val container = ZkElement()

        io {
            val page = pageClass.newInstance()
            page.dto = companion.read(recordId)
            page.openUpdate = { openUpdate(it.id) }
            page.mode = ZkElementMode.Delete

            page as ZkElement

            container build {
                + zkPageStyles.scrollable
                + div(zkPageStyles.content) {
                    + page
                }
            }

        }

        return container
    }
}