/*
 * Copyright © 2020, Simplexion, Hungary and contributors
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
package zakadabar.stack.frontend.comm.rest

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.frontend.errors.FetchError
import zakadabar.stack.frontend.errors.ensure
import zakadabar.stack.frontend.util.json

suspend fun <T> getChildrenOf(parentId : Long?, path : String, serializer : KSerializer<T>): List<T> {
    val responsePromise = window.fetch("/api/" + if (parentId == null) path else "${path}?parent=$parentId")
    val response = responsePromise.await()

    ensure(response.ok) { FetchError(response) }

    val textPromise = response.text()
    val text = textPromise.await()

    return json.decodeFromString(ListSerializer(serializer), text)
}