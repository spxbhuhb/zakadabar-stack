/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.localized
import zakadabar.lib.schedule.data.JobSummary
import zakadabar.lib.schedule.data.JobSummaryEntry

class JobSummaryTable : ZkTable<JobSummaryEntry>() {

    override fun onConfigure() {

        query = JobSummary()

        titleText = localized<JobTable>()

        add = true
        search = true
        export = true

        + JobSummaryEntry::jobId
        + JobSummaryEntry::status
        + JobSummaryEntry::startAt
        + JobSummaryEntry::completedAt
        + JobSummaryEntry::actionNamespace
        + JobSummaryEntry::actionType
        + JobSummaryEntry::failCount
        + JobSummaryEntry::retryCount
        + JobSummaryEntry::lastFailedAt

        + actions()
    }

    override fun getRowId(row: JobSummaryEntry): String = row.jobId.toString()
}