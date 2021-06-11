/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data


/**
 * Get an URL for the a BLOB.
 */
val BlobBo<*, *>.url
    get() = "/api/${getBoNamespace()}/blob/content/$id"