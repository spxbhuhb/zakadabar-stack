/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import zakadabar.core.data.EntityId
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion

@Serializable
class MailPart(
    override var id: EntityId<MailPart>,
    override var disposition: String,
    override var reference: EntityId<Mail>?,
    override var name: String,
    override var mimeType: String,
    override var size: Long
) : BlobBo<MailPart, Mail> {

   companion object : BlobBoCompanion<MailPart, Mail>("zk-mail-part")

   override fun getBoNamespace() = boNamespace
   override fun comm() = comm

}
