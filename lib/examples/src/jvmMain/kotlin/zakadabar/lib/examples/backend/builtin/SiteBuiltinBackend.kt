/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.lib.examples.backend.builtin

import io.ktor.routing.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import zakadabar.lib.examples.data.builtin.BuiltinDto
import zakadabar.lib.examples.data.builtin.ExampleQuery
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.data.DataConflictException
import zakadabar.stack.data.entity.EntityId

/**
 * This backend stores a limited number of records in the memory for testing
 * purposes.
 *
 * The backend uses a single mutex for synchronization and blocking methods, but
 * in this case that is fine as the operations are really fast (in-memory only,
 * on a very short list).
 */
object SiteBuiltinBackend : EntityBackend<BuiltinDto>() {

    override val boClass = BuiltinDto::class

    internal val mutex = Mutex()

    private var nextId = 1L
    internal var recordStore = mutableListOf<BuiltinDto>()

    suspend fun clip() {
        recordStore = recordStore.sortedByDescending { it.instantValue }.take(100).toMutableList()
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.query(ExampleQuery::class, ::query)
    }

    override fun all(executor: Executor) = runBlocking {

        authorize(true)

        mutex.withLock {
            recordStore
        }
    }

    override fun create(executor: Executor, dto: BuiltinDto) = runBlocking {

        authorize(true)

        // this is here to test conflict exception
        if (dto.stringValue == "conflict") throw DataConflictException("stringValueConflict")

        mutex.withLock {
            dto.id = EntityId(nextId++)
            recordStore.add(dto)
            clip()
        }

        dto
    }

    override fun read(executor: Executor, EntityId: EntityId<BuiltinDto>) = runBlocking {

        authorize(true)

        mutex.withLock {
            recordStore.first { it.id == EntityId }
        }
    }

    override fun update(executor: Executor, dto: BuiltinDto) = runBlocking {

        authorize(true)

        mutex.withLock {
            val index = recordStore.indexOfFirst { it.id == dto.id }
            if (index == -1) throw NoSuchElementException()
            recordStore[index] = dto
        }

        dto
    }

    override fun delete(executor: Executor, EntityId: EntityId<BuiltinDto>) = runBlocking {

        authorize(true)

        mutex.withLock {
            recordStore.removeAll { it.id == EntityId }
        }

        Unit
    }

    private fun filter(dto: BuiltinDto, query : ExampleQuery) : Boolean {
        if (query.booleanValue != null && dto.booleanValue != query.booleanValue) return false
        if (query.enumSelectValue != null && dto.enumSelectValue != query.enumSelectValue) return false
        if (query.intValue != null && dto.intValue != query.intValue) return false
        if (query.stringValue != null && dto.stringValue != query.stringValue) return false

        return true
    }

    private fun query(executor: Executor, query: ExampleQuery) = runBlocking {

        authorize(true)

        mutex.withLock {
            recordStore.filter { filter(it, query) }
        }
    }
}