/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

class ProgressEntry(
    val uuid: UUID,
    val current: Int = 0,
    val top: Int = 100,
    val state: ProgressState = when {
        current == top -> ProgressState.Done
        current == 0 -> ProgressState.Starting
        else -> ProgressState.Processing
    }
)

enum class ProgressState {
    Starting,
    Processing,
    Done
}

object Progress {
    private val progressMap = mutableMapOf<UUID, ProgressEntry>()

    operator fun plusAssign(entry: ProgressEntry) {
        progressMap[entry.uuid] = entry
    }

    operator fun minusAssign(uuid: UUID) {
        progressMap -= uuid
    }

    operator fun get(uuid: UUID) = progressMap[uuid]
}