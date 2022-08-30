/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

class RuiImplicit1<BT,VT>(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiParent : RuiFragment<BT>,
    override val ruiExternalPatch: (it: RuiFragment<BT>) -> Unit,
    override val fragment: RuiFragment<BT>,
    var v0 : VT
) : RuiGeneratedFragment<BT>