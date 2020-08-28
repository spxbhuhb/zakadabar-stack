/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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