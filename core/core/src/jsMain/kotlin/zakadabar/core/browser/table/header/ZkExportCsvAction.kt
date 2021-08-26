/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.header

import zakadabar.core.browser.button.ZkButton
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIcons

class ZkExportCsvAction(
    onExecute: () -> Unit
) : ZkButton(ZkIcons.fileDownload, ZkFlavour.Primary, buttonSize = 24, iconSize = 18, round = true, onClick = onExecute)