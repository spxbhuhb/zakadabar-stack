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

package zakadabar.stack.frontend.comm.rest

import kotlinx.serialization.KSerializer
import zakadabar.stack.extend.DtoWithEntityContract
import zakadabar.stack.extend.EntityRestCommContract
import zakadabar.stack.frontend.errors.FetchError
import zakadabar.stack.util.PublicApi

/**
 * REST communication functions for objects that implement [DtoWithEntityContract]
 *
 * @property  path  The path on which the server provides the REST
 *                  access to this data store, for example "1a2b3c/folder".
 *
 * @property  serializer  The serializer to serialize/deserialize objects
 *                        sent/received.
 */
@PublicApi
open class EntityRestComm<T : DtoWithEntityContract<T>>(
    path: String,
    serializer: KSerializer<T>
) : RecordRestComm<T>(path, serializer), EntityRestCommContract<T> {

    /**
     * Fetches children of an object.
     *
     * @param  parentId  Id of the parent object.
     *
     * @return  The list of objects fetched.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun getChildrenOf(parentId: Long?) = getChildrenOf(parentId, path, serializer)

}