/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.actions

import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons

class ZkAddRowAction(
    onExecute: () -> Unit
) : ZkButton(ZkIcons.add, ZkFlavour.Primary, buttonSize = 24, iconSize = 18, round = true, onClick = onExecute)