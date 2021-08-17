/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.resource

import zakadabar.core.resource.ZkStringStore


val blobStrings = Strings()

@Suppress("unused")
class Strings : ZkStringStore() {
    val starting by "starting ..."
    val uploading by "uploading: "
    val uploadError by "upload error"
    val uploadAbort by "upload aborted"
}