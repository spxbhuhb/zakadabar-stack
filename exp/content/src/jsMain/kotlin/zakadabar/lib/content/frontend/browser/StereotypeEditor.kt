/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.content.frontend.browser

import zakadabar.lib.content.data.StereotypeBo
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.default
import zakadabar.stack.resources.localized

class StereotypeEditor : ZkCrudTarget<StereotypeBo>() {
    init {
        companion = StereotypeBo.Companion
        boClass = StereotypeBo::class
        editorClass = StereotypeForm::class
    }
}

class StereotypeForm : ZkForm<StereotypeBo>() {

    var localizations = ZkElement()

    override fun onCreate() {
        super.onCreate()

        + section {
            + bo::id
            + select(bo::parent) { StereotypeBo.all().by { it.name.localized } }
            + bo::name
        }

        + localizations

        bo.localizations.forEach {
            + StereotypeLocalizationForm(localizations).apply { bo = it }
        }

        + ZkButton(iconSource = ZkIcons.add) {
            + StereotypeLocalizationForm(localizations).apply { bo = default { } }
        }
    }

    override suspend fun onSubmitStart() {
        super.onSubmitStart()

        bo.localizations = localizations.find<StereotypeLocalizationForm>().map { it.bo }

    }

    override fun validate(submit: Boolean): Boolean {

        var subOk = true
        val commonOk = super.validate(submit)

        localizations.find<StereotypeLocalizationForm>().forEach {
            subOk = subOk && it.validate(submit)
        }

        return commonOk && subOk

    }
}