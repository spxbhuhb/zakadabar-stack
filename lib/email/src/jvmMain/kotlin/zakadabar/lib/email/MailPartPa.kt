/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import zakadabar.core.util.default
import zakadabar.lib.blobs.persistence.BlobExposedPa

class MailPartPa(
   referenceTable : MailTable,
   table : MailPartTable = MailPartTable(referenceTable = referenceTable)
) : BlobExposedPa<MailPart, Mail>(
   table = table
) {
   override fun newInstance() = default<MailPart> {  }
}

