/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

/**
 * Interface for objects that are unique in one scope.
 */
interface Unique {
    val uuid: UUID
}