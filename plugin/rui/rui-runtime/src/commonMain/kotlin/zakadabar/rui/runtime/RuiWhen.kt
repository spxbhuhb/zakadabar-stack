/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

class RuiWhen(val select: () -> RuiFunWrapper) : RuiFragment() {
    var blockFun = ruiEmptyBlockFunc
    var block = ruiEmptyBlock

    override var ruiCreate = {
        blockFun = select()
        block = blockFun.func()
        block.ruiCreate()
    }

    override var ruiPatch = {
        val newBlockFun = select()
        if (newBlockFun == blockFun) {
            block.ruiPatch()
        } else {
            block.ruiDispose()
            blockFun = newBlockFun
            block = blockFun.func()
            block.ruiCreate()
        }
    }

    override var ruiDispose = {
        block.ruiDispose()
    }
}