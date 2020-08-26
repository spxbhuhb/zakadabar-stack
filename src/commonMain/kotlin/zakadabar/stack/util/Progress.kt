/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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