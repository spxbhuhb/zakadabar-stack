/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.actions

import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.resources.ZkIcons

class ZkAddRowAction(
    onExecute: () -> Unit
) : ZkIconButton(ZkIcons.add, round = true, onClick = onExecute)