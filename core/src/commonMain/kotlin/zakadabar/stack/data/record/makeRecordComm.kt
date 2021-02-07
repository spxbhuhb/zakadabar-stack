/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.record

/**
 * Global function to make a [RecordCommInterface]. It is here, because I want to use this in common
 * code but I want to implement it differently for different targets. As of now,
 * that means JavaScript has a proper implementation, Java has an empty one.
 * Technically it would be possible to have a Java client.
 */
expect fun <T : RecordDto<T>> makeRecordComm(recordDtoCompanion: RecordDtoCompanion<T>): RecordCommInterface<T>
