/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
// this file contains additions to the default Kotlin MPP webpack config
// you may see the resulting configuration in build/reports/webpack

if (config.devServer) {
    config.devServer = {
        ...config.devServer,
        // open: false, // comment this out to disable opening new browser windows at startup
        port: 3000,
        //host: "0.0.0.0", // comment this out to have the dev server listen on all interfaces
        proxy: {
            '/api': {  // proxy to the development backend
                target: 'http://localhost:8080',
                secure: 'false'
            }
        }
    }
}