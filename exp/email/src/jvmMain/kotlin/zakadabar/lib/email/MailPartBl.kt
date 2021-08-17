/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import zakadabar.lib.blobs.business.BlobBlBase

class MailPartBl(
   mailTable : MailTable
) : BlobBlBase<MailPart, Mail>(
   MailPart::class,
   MailPartPa(mailTable)
) {
   override val authorizer by provider()
}
