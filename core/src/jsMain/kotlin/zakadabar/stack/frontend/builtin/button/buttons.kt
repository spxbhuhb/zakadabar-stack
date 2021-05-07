/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.button

import zakadabar.stack.frontend.resources.ZkFlavour

fun primaryButton(text: String, onClick: () -> Unit) = ZkButton(text, onClick = onClick)

fun secondaryButton(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Secondary, onClick = onClick)

fun successButton(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Success, onClick = onClick)

fun warningButton(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Warning, onClick = onClick)

fun dangerButton(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Danger, onClick = onClick)

fun infoButton(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Info, onClick = onClick)