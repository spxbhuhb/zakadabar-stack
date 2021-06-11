/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

fun ContentCommonBo.firstOrNull(stereotype : ContentStereotypeBo) =
    textBlocks.firstOrNull { it.stereotype == stereotype.id }?.value