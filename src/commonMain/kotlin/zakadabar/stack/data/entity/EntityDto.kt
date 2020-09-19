/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import zakadabar.stack.data.record.RecordDto

/**
 * A contract for DTO classes that have a corresponding entity in the entity tree.
 */
interface EntityDto<T> : RecordDto<T> {

    val entityRecord: EntityRecordDto?

}