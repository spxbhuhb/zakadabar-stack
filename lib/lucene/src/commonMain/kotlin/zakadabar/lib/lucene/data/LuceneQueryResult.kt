/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.lucene.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema


/**
 * A hit from the Lucene index.
 *
 * @param  path  Path to the document, relative to the documents root directory.
 * @param  title First line of the document.
 */
@Serializable
class LuceneQueryResult(

    var path : String,
    var title : String

) : BaseBo {

    override fun schema() = BoSchema.NO_VALIDATION

}