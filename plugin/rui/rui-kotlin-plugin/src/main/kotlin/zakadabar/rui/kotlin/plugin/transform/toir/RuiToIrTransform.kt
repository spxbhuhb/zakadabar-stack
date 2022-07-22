/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.toir

import zakadabar.rui.kotlin.plugin.model.*

class RuiToIrTransform(
    private val ruiClass: RuiClass,
) {

    fun transform() {
        ruiClass.builder.finalize()
    }

    fun transformBlock(ruiBlock: RuiBlock) {

    }

    fun transformForBlock(ruiForLoop: RuiForLoop) {

    }

    fun transformCall(ruiCall: RuiCall) {
    }

    fun transformWhen(ruiWhen: RuiWhen) {
    }

    fun transformBranch(ruiBranch: RuiBranch) {

    }

    fun transformExpression(ruiExpression: RuiExpression) {
    }

    fun transformDeclaration(ruiDeclaration: RuiDeclaration) {

    }

    fun transformStateVariable() {

    }


}