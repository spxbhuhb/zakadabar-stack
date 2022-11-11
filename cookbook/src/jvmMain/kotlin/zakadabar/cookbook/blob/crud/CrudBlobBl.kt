/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.blob.crud

import io.ktor.features.*
import zakadabar.cookbook.blob.crud.CrudBlob
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.authorize.Executor
import zakadabar.core.data.EntityId
import zakadabar.lib.blobs.business.BlobBlBase

open class CrudBlobBl : BlobBlBase<CrudBlob, ExampleReferenceBo>(
    CrudBlob::class,
    CrudBlobPa()
) {
    override val authorizer by provider()

    override fun create(executor: Executor, bo: CrudBlob): CrudBlob {
        if (bo.size > 50000) throw BadRequestException("size limit is 50k bytes")
        if ((pa as CrudBlobPa).count() >= 1000) throw IllegalStateException("table limit reached")
        return super.create(executor, bo)
    }

    override fun writeContent(executor: Executor, blobId: EntityId<CrudBlob>, length : Long, bytes : ByteArray) = pa.withTransaction {
        if (length > 50000) throw BadRequestException("size limit is 50k bytes")
        super.writeContent(executor, blobId, length, bytes)
    }
}