/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.crud

import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.newInstance
import kotlin.reflect.KClass

/**
 * Provides common functions used in most CRUD implementations.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkInlineCrud<T : RecordDto<T>> : ZkElement(), ZkCrud<T> {

    lateinit var companion: RecordDtoCompanion<T>
    lateinit var dtoClass: KClass<T>
    lateinit var pageClass: KClass<out ZkCrudEditor<T>>
    lateinit var tableClass: KClass<out ZkTable<T>>

    override fun openAll() {

        clear()

        io {
            val table = tableClass.newInstance()

            table.crud = this
            table.setData(companion.comm.all())

            + table
        }
    }

    override fun openCreate() {

        clear()

        val dto = dtoClass.newInstance()
        dto.schema().setDefaults()

        val page = pageClass.newInstance()
        page.dto = dto
        page.openUpdate = { openUpdate(it.id) }
        page.mode = ZkElementMode.Create

        page as ZkElement

        + page
    }

    override fun openRead(recordId: RecordId<T>) {

        clear()

        io {
            val page = pageClass.newInstance()
            page.dto = companion.read(recordId)
            page.openUpdate = { openUpdate(it.id) }
            page.mode = ZkElementMode.Read

            page as ZkElement

            + page
        }
    }

    override fun openUpdate(recordId: RecordId<T>) {

        clear()

        io {
            val page = pageClass.newInstance()
            page.dto = companion.read(recordId)
            page.openUpdate = { openUpdate(it.id) }
            page.mode = ZkElementMode.Update

            page as ZkElement

            + page
        }
    }

    override fun openDelete(recordId: RecordId<T>) {

        clear()

        io {
            val page = pageClass.newInstance()
            page.dto = companion.read(recordId)
            page.openUpdate = { openUpdate(it.id) }
            page.mode = ZkElementMode.Delete

            page as ZkElement

            + page

        }
    }
}
