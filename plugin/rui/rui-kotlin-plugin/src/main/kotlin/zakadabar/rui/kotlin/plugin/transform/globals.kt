/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform

/**
 * Number of Rui related constructor arguments.
 *
 * 1. ruiAdapter
 * 2. ruiAnchor
 * 3. ruiPatchState
 */
const val RUI_CLASS_RUI_ARGUMENTS = 1

const val RUI_CLASS_PATCH_STATE_INDEX = 2

const val RUI_PARAMETER_MASK = "mask"

const val RUI_FUN_CREATE = "ruiCreate"
const val RUI_FUN_PATCH = "ruiPatch"
const val RUI_FUN_DISPOSE = "ruiDispose"

const val RUI_INVALIDATE = "ruiInvalidate"
const val RUI_DIRTY = "ruiDirty"

const val RUI_BLOCK = "ruiBlock"
const val RUI_BRANCH = "ruiBranch"
const val RUI_CALL = "ruiCall"
const val RUI_FOR_LOOP = "ruiForLoop"
const val RUI_WHEN = "ruiWhen"
