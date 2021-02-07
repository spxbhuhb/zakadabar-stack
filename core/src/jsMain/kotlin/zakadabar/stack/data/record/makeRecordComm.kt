/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.record

actual fun <T : RecordDto<T>> makeRecordComm(recordDtoCompanion: RecordDtoCompanion<T>): RecordCommInterface<T> {
    return RecordComm(recordDtoCompanion.recordType, recordDtoCompanion.serializer())
}