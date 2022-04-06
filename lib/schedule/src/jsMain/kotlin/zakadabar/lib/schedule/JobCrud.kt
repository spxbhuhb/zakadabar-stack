/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import zakadabar.core.browser.application.target
import zakadabar.core.browser.crud.ZkCrudTarget
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.localized
import zakadabar.lib.schedule.data.Job


class JobCrud : ZkCrudTarget<Job>() {
    init {
        companion = Job.Companion
        boClass = Job::class
        editorClass = JobForm::class
        tableClass = JobTable::class
    }
}

class JobForm : ZkForm<Job>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<JobForm>()) {
            + section {
                + bo::id
                + bo::status
                + bo::createdBy
                + bo::createdAt
                + bo::startAt
                + bo::completedAt
                + bo::specific
                + bo::actionNamespace
                + bo::actionType
                + bo::actionData
                + bo::worker
                + bo::failCount
                + bo::retryCount
                + bo::retryInterval
                + bo::lastFailedAt
                + bo::lastFailMessage
                + bo::lastFailData
                + bo::deleteOnSuccess
                + bo::progress
                + bo::progressText
                + bo::lastProgressAt
                + bo::responseData
                + bo::origin
            }
        }
    }
}

class JobTable : ZkTable<Job>() {

    override fun onConfigure() {

        crud = target<JobCrud>()

        titleText = localized<JobTable>()

        add = true
        search = true
        export = true
        
        + Job::id
        + Job::status
        + Job::createdBy
        + Job::createdAt
        + Job::startAt
        + Job::completedAt
        + Job::specific
        + Job::actionNamespace
        + Job::actionType
        + Job::actionData
        + Job::worker
        + Job::failCount
        + Job::retryCount
        + Job::retryInterval
        + Job::lastFailedAt
        + Job::lastFailMessage
        + Job::lastFailData
        + Job::deleteOnSuccess
        + Job::progress
        + Job::progressText
        + Job::lastProgressAt
        + Job::responseData
        + Job::origin
        
        + actions()
    }
}