/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import zakadabar.stack.data.record.RecordDtoCompanion

abstract class EntityDtoCompanion<T : EntityDto<T>> : RecordDtoCompanion<T>()