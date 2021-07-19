/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // these are library functions offered for the public

package zakadabar.stack.frontend.builtin.button

import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.resources.localizedStrings

fun buttonPrimary(text: String, onClick: () -> Unit) = ZkButton(text, onClick = onClick)
fun buttonSecondary(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Secondary, onClick = onClick)
fun buttonSuccess(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Success, onClick = onClick)
fun buttonWarning(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Warning, onClick = onClick)
fun buttonDanger(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Danger, onClick = onClick)
fun buttonInfo(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Info, onClick = onClick)
fun buttonCustom(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Custom, onClick = onClick)

fun buttonPrimary(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Primary)
fun buttonSecondary(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Secondary)
fun buttonSuccess(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Success)
fun buttonWarning(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Warning)
fun buttonDanger(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Danger)
fun buttonInfo(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Info)
fun buttonCustom(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Custom)

fun button(target: ZkAppRouting.ZkTarget, flavour: ZkFlavour) =
    ZkButton(
        localizedStrings.getNormalized(target.viewName),
        url = application.routing.toLocalUrl(target),
        flavour = flavour,
        onClick = {
            application.changeNavState(target)
        }
    )
