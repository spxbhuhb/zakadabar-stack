/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.http

enum class BlobCreateState {
    Starting,
    Progress,
    Done,
    Error,
    Abort
}