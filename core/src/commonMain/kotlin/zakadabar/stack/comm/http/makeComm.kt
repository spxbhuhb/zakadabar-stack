/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.http

import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion

expect fun <T : RecordDto<T>> makeComm(recordDtoCompanion: RecordDtoCompanion<T>): Comm<T>
