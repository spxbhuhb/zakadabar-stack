/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // these are library functions offered for the public

package zakadabar.stack.frontend.builtin.button

import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.resources.ZkFlavour

fun primaryButton(text: String, onClick: () -> Unit) = ZkButton(text, onClick = onClick)
fun secondaryButton(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Secondary, onClick = onClick)
fun successButton(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Success, onClick = onClick)
fun warningButton(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Warning, onClick = onClick)
fun dangerButton(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Danger, onClick = onClick)
fun infoButton(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Info, onClick = onClick)
fun customButton(text: String, onClick: () -> Unit) = ZkButton(text, flavour = ZkFlavour.Custom, onClick = onClick)

fun primaryButton(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Primary)
fun secondaryButton(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Secondary)
fun successButton(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Success)
fun warningButton(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Warning)
fun dangerButton(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Danger)
fun infoButton(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Info)
fun customButton(target: ZkAppRouting.ZkTarget) = button(target, ZkFlavour.Custom)

fun button(target: ZkAppRouting.ZkTarget, flavour: ZkFlavour) =
    ZkButton(
        stringStore.getNormalized(target.viewName),
        url = application.routing.toLocalUrl(target),
        flavour = flavour,
        onClick = {
            application.changeNavState(target)
        }
    )
