/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import zakadabar.core.util.PublicApi

/**
 * Gets the first text block of a given stereotype (or null if no such
 * block exists).
 *
 * @param  stereotype  The stereotype to get the block for.
 *
 * @return the text block or null if no text block exists for the stereotype
 */
@PublicApi
fun ContentBo.firstOrNull(stereotype : String?) =
    textBlocks.firstOrNull { it.stereotype == stereotype }?.value