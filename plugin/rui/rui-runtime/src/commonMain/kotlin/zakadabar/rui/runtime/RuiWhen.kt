/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

class RuiWhen(val select: () -> RuiFunWrapper) : RuiFragment() {
    var blockFun = ruiEmptyBlockFunc
    var block = ruiEmptyBlock

    override var create = {
        blockFun = select()
        block = blockFun.func()
        block.create()
    }

    override var patch = {
        val newBlockFun = select()
        if (newBlockFun == blockFun) {
            block.patch()
        } else {
            block.dispose()
            blockFun = newBlockFun
            block = blockFun.func()
            block.create()
        }
    }

    override var dispose = {
        block.dispose()
    }
}