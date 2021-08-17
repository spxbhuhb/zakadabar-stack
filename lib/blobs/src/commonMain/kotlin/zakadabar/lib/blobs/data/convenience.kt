/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import zakadabar.core.data.EntityBo
import zakadabar.core.util.default

suspend inline fun <reified T : BlobBo<T, RT>, reified RT : EntityBo<RT>> BlobBoCompanion<T, RT>.create(content: ByteArray, func: T.() -> Unit): T {
    return default<T> {
        func()
    }
        .create()
        .upload(content)
}

/**
 * Get an URL for the a BLOB.
 */
val BlobBo<*, *>.url
    get() = "/api/${getBoNamespace()}/blob/content/$id"