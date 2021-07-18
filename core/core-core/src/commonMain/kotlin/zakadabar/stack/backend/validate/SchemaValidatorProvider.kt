/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.validate

import zakadabar.stack.backend.business.BusinessLogicCommon
import zakadabar.stack.data.BaseBo

class SchemaValidatorProvider : ValidatorProvider {

    override fun <T : BaseBo> businessLogicRouter(businessLogic : BusinessLogicCommon<T>) : Validator<T> {
        return SchemaValidator()
    }

}