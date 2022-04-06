/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion

@Serializable
class JobSummary : QueryBo<List<JobSummaryEntry>> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(JobSummaryEntry.serializer()))

    companion object : QueryBoCompanion(Job.boNamespace)

}