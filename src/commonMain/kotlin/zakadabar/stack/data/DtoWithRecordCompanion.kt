/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data

import zakadabar.stack.extend.*

abstract class DtoWithRecordCompanion<T : DtoWithRecordContract<T>> : DtoWithRecordCompanionContract<T> {

    override lateinit var comm: RecordRestCommContract<T>

}
