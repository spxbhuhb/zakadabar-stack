/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.table.actions

import zakadabar.core.frontend.builtin.button.ZkButton
import zakadabar.core.frontend.resources.ZkFlavour
import zakadabar.core.frontend.resources.ZkIcons

class ZkExportCsvAction(
    onExecute: () -> Unit
) : ZkButton(ZkIcons.fileDownload, ZkFlavour.Primary, buttonSize = 24, iconSize = 18, round = true, onClick = onExecute)