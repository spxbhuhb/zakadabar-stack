/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

/**
 * Produces fragment. Branches, loops and higher-order functions get fragment factories
 * to produce the necessary fragments on-demand.
 */
class RuiFragmentFactory<AT, FT : RuiFragment<AT>>(
    val builder: () -> FT
)
