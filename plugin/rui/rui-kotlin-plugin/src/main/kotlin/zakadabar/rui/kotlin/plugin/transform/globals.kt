/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform

/**
 * Number of Rui related constructor arguments.
 *
 * 1. ruiAdapter
 * 2. ruiPatchExternal
 */
const val RUI_FRAGMENT_ARGUMENT_COUNT = 2
const val RUI_FRAGMENT_ARGUMENT_INDEX_ADAPTER = 0
const val RUI_FRAGMENT_ARGUMENT_INDEX_PATCH_EXTERNAL = 1

const val RUI_CREATE = "ruiCreate"
const val RUI_MOUNT = "ruiMount"
const val RUI_PATCH = "ruiPatch"
const val RUI_UNMOUNT = "ruiUnmount"
const val RUI_DISPOSE = "ruiDispose"

const val RUI_INVALIDATE = "ruiInvalidate"
const val RUI_DIRTY = "ruiDirty"

const val RUI_ADAPTER = "ruiAdapter"
const val RUI_PATCH_EXTERNAL = "ruiPatchExternal"
const val RUI_ANCHOR = "ruiAnchor"
const val RUI_MASK = "mask"

const val RUI_BLOCK = "ruiBlock"
const val RUI_BRANCH = "ruiBranch"
const val RUI_CALL = "ruiCall"
const val RUI_FOR_LOOP = "ruiForLoop"
const val RUI_WHEN = "ruiWhen"
