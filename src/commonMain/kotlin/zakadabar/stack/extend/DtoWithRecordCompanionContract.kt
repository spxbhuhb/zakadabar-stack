/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.extend

import kotlinx.serialization.KSerializer

/**
 * A contract for companion objects of classes with [DtoWithRecordContract].
 */
interface DtoWithRecordCompanionContract<T : DtoWithRecordContract<T>> {

    var comm: RecordRestCommContract<T>

    fun serializer(): KSerializer<T>

}