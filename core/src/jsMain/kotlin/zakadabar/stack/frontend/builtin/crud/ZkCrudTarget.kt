/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.crud

import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
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
open class ZkCrudTarget<T : EntityBo<T>> : ZkAppRouting.ZkTarget, ZkCrud<T> {

    override var viewName = "${this::class.simpleName}"

    lateinit var companion: EntityBoCompanion<T>
    lateinit var boClass: KClass<T>
    lateinit var editorClass: KClass<out ZkCrudEditor<T>>
    lateinit var tableClass: KClass<out ZkTable<T>>

    @Deprecated("EOL: 2021.7.1 use editorClass instead", ReplaceWith("editorClass"))
    var pageClass
       get() = editorClass
       set(value) {
           editorClass = value
       }

    override fun openAll() = application.changeNavState(this, "all")
    override fun openCreate() = application.changeNavState(this, "create")
    override fun openRead(recordId: EntityId<T>) = application.changeNavState(this, "read", "id=$recordId")
    override fun openUpdate(recordId: EntityId<T>) = application.changeNavState(this, "update", "id=$recordId")
    override fun openDelete(recordId: EntityId<T>) = application.changeNavState(this, "delete", "id=$recordId")

    @Suppress("UNCHECKED_CAST") // got lost in generics hell, probably fine
    override fun route(routing: ZkAppRouting, state: ZkNavState): ZkElement {
        if (state.segments.size == 2) return all()
        return when (state.segments[2]) {
            "all" -> all()
            "create" -> create()
            "read" -> read(state.recordId as EntityId<T>)
            "update" -> update(state.recordId as EntityId<T>)
            "delete" -> delete(state.recordId as EntityId<T>)
            else -> routeNonCrud(routing, state)
        }
    }

    open fun routeNonCrud(routing: ZkAppRouting, state: ZkNavState): ZkElement = NYI()

    open fun all(): ZkElement {

        val container = ZkElement()

        io {
            val table = tableClass.newInstance()
            table.crud = this@ZkCrudTarget
            table.setData(companion.comm.all())

            container build {
                + zkPageStyles.fixed
                + table
            }
        }

        return container
    }

    open fun create(): ZkElement {
        val container = ZkElement()

        io {
            val bo = boClass.newInstance()
            bo.schema().setDefaults()

            val editor = editorClass.newInstance()
            editor.bo = bo
            editor.openUpdate = { openUpdate(it.id) }
            editor.mode = ZkElementMode.Create

            onBeforeCreate(editor)

            editor as ZkElement

            container build {
                + zkPageStyles.scrollable
                + div(zkPageStyles.content) {
                    + editor
                }
            }
        }

        return container
    }

    /**
     * Called after the editor is created but before it is added to the
     * page. Provides place for last-step adjustments.
     */
    open suspend fun onBeforeCreate(editor : ZkCrudEditor<T>) {

    }

    open fun read(recordId: EntityId<T>): ZkElement {

        val container = ZkElement()

        io {
            val editor = editorClass.newInstance()
            editor.bo = companion.read(recordId)
            editor.openUpdate = { openUpdate(it.id) }
            editor.mode = ZkElementMode.Read

            editor as ZkElement

            container build {
                + zkPageStyles.scrollable
                + div(zkPageStyles.content) {
                    + editor
                }
            }

        }

        return container
    }

    open fun update(recordId: EntityId<T>): ZkElement {

        val container = ZkElement()

        io {
            val editor = editorClass.newInstance()
            editor.bo = companion.read(recordId)
            editor.openUpdate = { openUpdate(it.id) }
            editor.mode = ZkElementMode.Update

            editor as ZkElement

            container build {
                + zkPageStyles.scrollable
                + div(zkPageStyles.content) {
                    + editor
                }
            }

        }

        return container
    }

    open fun delete(recordId: EntityId<T>): ZkElement {

        val container = ZkElement()

        io {
            val editor = editorClass.newInstance()
            editor.bo = companion.read(recordId)
            editor.openUpdate = { openUpdate(it.id) }
            editor.mode = ZkElementMode.Delete

            editor as ZkElement

            container build {
                + zkPageStyles.scrollable
                + div(zkPageStyles.content) {
                    + editor
                }
            }

        }

        return container
    }
}
