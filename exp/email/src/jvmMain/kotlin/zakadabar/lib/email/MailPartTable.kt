/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import zakadabar.lib.blobs.backend.BlobExposedTable

class MailPartTable(
   tableName : String = "mail_part",
   referenceTable : MailTable
) : BlobExposedTable<MailPart, Mail>(
   tableName = tableName,
   referenceTable = referenceTable
)