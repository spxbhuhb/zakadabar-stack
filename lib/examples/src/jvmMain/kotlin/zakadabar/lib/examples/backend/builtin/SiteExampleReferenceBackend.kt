/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.lib.examples.backend.builtin

import io.ktor.routing.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import zakadabar.lib.examples.backend.builtin.SiteBuiltinBackend.mutex
import zakadabar.lib.examples.data.builtin.ExampleReferenceDto
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.data.entity.EntityId

/**
 * This backend stores a limited number of records in the memory for testing
 * purposes.
 *
 * The backend uses a single mutex for synchronization and blocking methods, but
 * in this case that is fine as the operations are really fast (in-memory only,
 * on a very short list).
 */
object SiteExampleReferenceBackend : EntityBackend<ExampleReferenceDto>() {

    override val boClass = ExampleReferenceDto::class

    private var nextId = 1L
    private var recordStore = mutableListOf<ExampleReferenceDto>()

    private fun clip() {
        if (recordStore.size < 100) return
        recordStore = recordStore.filter { ref ->
            SiteBuiltinBackend.recordStore.firstOrNull {
                it.recordSelectValue == ref.id || it.optRecordSelectValue == ref.id
            } != null
        }.toMutableList()
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = runBlocking {

        authorize(true)

        mutex.withLock {
            recordStore
        }
    }

    override fun create(executor: Executor, bo: ExampleReferenceDto) = runBlocking {

        authorize(true)

        mutex.withLock {
            bo.id = EntityId(nextId++)
            recordStore.add(bo)
            clip()
        }

        bo
    }

    override fun read(executor: Executor, entityId: EntityId<ExampleReferenceDto>) = runBlocking {

        authorize(true)

        mutex.withLock {
            recordStore.first { it.id == entityId }
        }
    }

    override fun update(executor: Executor, bo: ExampleReferenceDto) = runBlocking {

        authorize(true)

        mutex.withLock {
            val index = recordStore.indexOfFirst { it.id == bo.id }
            if (index == -1) throw NoSuchElementException()
            recordStore[index] = bo
        }

        bo
    }

    override fun delete(executor: Executor, entityId: EntityId<ExampleReferenceDto>) = runBlocking {

        authorize(true)

        mutex.withLock {
            recordStore.removeAll { it.id == entityId }
        }

        Unit
    }

}