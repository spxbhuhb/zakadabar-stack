/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.ship

import zakadabar.demo.Demo
import zakadabar.stack.backend.builtin.blob.BlobTable

object ShipImageTable : BlobTable("t_${Demo.shid}_ship_blobs", ShipTable)