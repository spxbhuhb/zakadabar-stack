/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.lucene.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema


/**
 * @param index Path to the index directory Lucene uses.
 * @param docs  Path to the directory that contains documents to index.
 */
@Serializable
class LuceneSettings(

    var index : String,
    var docs : String

) : BaseBo {

    override fun schema() = BoSchema.NO_VALIDATION

}