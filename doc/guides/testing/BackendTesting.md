# Backend Testing

## API testing

The following setup lets you test API with:

- actual Ktor engine,
- H2 database,
- JVM client.

To create the environment for your tests:

1. add configuration files
1. add a test server setup
1. add a companion to your test class

### Gradle Dependencies

```kotlin
sourceSets["commonTest"].dependencies {
    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))
    implementation(kotlin("test-junit"))
}

sourceSets["jvmTest"].dependencies {
    implementation("io.ktor:ktor-server-netty:${Versions.ktor}")
    implementation("com.h2database:h2:${Versions.h2}")
}
```

### Configuration Files

The following picture shows how to add the configuration files for your tests.

![Test Config](test-config.png)

**lib.accounts.yaml** 

- Password of "so" account is "so".

```yaml
initialSoPassword: so
```

**stack.server.yaml**

- H2 in-memory database.
- Ktor runs on 8888.

```yaml
database:
  driverClassName: org.h2.Driver
  jdbcUrl: "jdbc:h2:mem:"
# Use this to save the DB for inspection. Remember to delete it before runs!
#  jdbcUrl: "jdbc:h2:./app/var/db"
  username: test
  password: Almafa.12

traceRouting: false
staticResources: ./var/static

ktor:
  port: 8888
```

### Test Class Without Authorization

Add a companion object to your test class extending [TestCompanionBase](/core/core/src/jvmMain/kotlin/zakadabar/core/testing/TestCompanionBase.kt).

[TestCompanionBase](/core/core/src/jvmMain/kotlin/zakadabar/core/testing/TestCompanionBase.kt):

- create and start a test Ktor Server with a test H2 instance
- set `CommConfig.baseUrl` to the created Ktor instance, see [Comm](/doc/guides/common/Comm.md) for details

In the tests you can work just as you would do in a frontend module.

```kotlin
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.server.server
import zakadabar.core.server.testing.TestCompanionBase
import zakadabar.core.util.default

class MyBlTest {

    companion object : TestCompanionBase() {

        override fun addModules() {
            modules += MyBl
        }

        override fun onAfterStarted() {
            // this code runs after the server has been started
            // optional
        }
        
        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }
    
    @Test
    fun testMyFunction() = runBlocking {

        val testBo = default<MyBo> {  }.create()
        
        // ... more test code follows ...
    }
    
}
```

### Test Class With Authorization

For this you need to add the dependency for `Lib: Accounts`.

Add a companion object to your test class, extending [AuthTestCompanionBase](/lib/accounts/src/jvmMain/kotlin/zakadabar/lib/accounts/testing/AuthTestCompanionBase.kt).

[AuthTestCompanionBase](/lib/accounts/src/jvmMain/kotlin/zakadabar/lib/accounts/testing/AuthTestCompanionBase.kt):

- create and start a test Ktor Server with a test H2 instance
- set `CommConfig.baseUrl` to the created Ktor instance, see [Comm](/doc/guides/common/Comm.md) for details
- install the `Lib: Accounts` library module
- adds a [SimpleRoleAuthorizerProvider](/core/core/src/commonMain/kotlin/zakadabar/core/authorize/SimpleRoleAuthorizerProvider.kt) with `all = roles.siteMember`.
- performs a login for the `so` account

In the tests you can work just as you would do in a frontend module.

```kotlin
class MyBlTest {

    companion object : AuthTestCompanionBase() {

        override fun addModules() {
            super.addModules() // to install lib:accounts
            modules += MyBl
        }

        override fun onAfterStarted() {
            super.onAfterStarted() // to perform login
            // this code runs after the server has been started, optional
        }
        
        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }
    
    @Test
    fun testByLocalizedPath() = runBlocking {

        val testBo = default<MyBo> {  }.create()
        
        // ... more test code follows ...
    }
    
}
```

### Convenience Executors

`TestCompanionBase` defines three `Executors` for convenient backend testing:

- `mockAnonymous`
- `mockLoggedIn`
- `mockSo`

You can pass these as the first parameter when you call BL functions directly;

```kotlin
@Test
fun `index and search`() = runBlocking {
    val bl = modules.first<LuceneBl>()

    bl.updateIndex(mockSo, UpdateIndex())
}
```

### Troubleshooting

If you don't see output/error of your test, add this to `build.gradle.kts`:

```kotlin
tasks.withType<Test> {
    testLogging {
        events(TestLogEvent.STANDARD_OUT, TestLogEvent.STANDARD_ERROR, TestLogEvent.FAILED)
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}
```

This usually means that [Configuration Files](#configuration-files) are missing:

```text
FAILURE: Build failed with an exception.
* What went wrong:
Execution failed for task ':core:jvmTest'.
> Process 'Gradle Test Executor 1' finished with non-zero exit value 1
  This problem might be caused by incorrect test process configuration.
 
```

This means that `super.addModules()` is missing when you use `AuthTestCompanionBase`:

```text
io.ktor.client.features.ClientRequestException: Client request(http://127.0.0.1:8888/api/zkl-session/action/LoginAction) invalid: 404 Not Found. Text: ""
	at io.ktor.client.features.DefaultResponseValidationKt$addDefaultResponseValidation$1$1.invokeSuspend(DefaultResponseValidation.kt:47)
	...
```

This means that `template/test/lib.accounts.yaml` is missing when you use `AuthTestCompanionBase`:

```text
io.ktor.client.features.ClientRequestException: Client request(http://127.0.0.1:8888/api/zkl-session/action/LoginAction) invalid: 401 Unauthorized. Text: "{}"
	at io.ktor.client.features.DefaultResponseValidationKt$addDefaultResponseValidation$1$1.invokeSuspend(DefaultResponseValidation.kt:47)
	...
```
