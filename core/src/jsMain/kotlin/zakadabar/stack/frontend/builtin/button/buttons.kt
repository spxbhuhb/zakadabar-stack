/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.button

import zakadabar.stack.frontend.resources.ZkFlavour

fun buttonPrimary(text: String, onClick: () -> Unit) = ZkButton(text, onClick = onClick)
fun buttonSecondary(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Secondary, onClick = onClick)
fun buttonSuccess(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Success, onClick = onClick)
fun buttonWarning(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Warning, onClick = onClick)
fun buttonDanger(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Danger, onClick = onClick)
fun buttonInfo(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Info, onClick = onClick)