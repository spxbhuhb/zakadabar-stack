## General

### Where are the CSS, HTML, JavaScript files?

In the Stack, there are none. For all those things we use Kotlin based solutions.

* JavaScript is obvious, the Kotlin compiler takes care of that.
* There is usually one, mostly constant, HTML file. For example
  see [index.html](../../../site/src/jsMain/resources/index.html) of the site.
* For theming and CSS we have our own implementation, please see [Themes, Css](/doc/guides/browser/structure/ThemesCss.md) in the
  documentation.
 
## Errors

### Gradle Is Stuck

This may happen when you try to start the backend and the frontend from IDEA. On some machines it happens, on some
others it doesn't. We have no IDEA (pun intended) why this is happening.

Open the `Terminal` of your IDEA (or a normal terminal of your operating system) and use `./gradlew run` from the project
directory. This will start the backend in the terminal and you'll be able to start the frontend from IDEA (or from
another terminal.)

### Backend Throws NoClassDefFoundError

This is mostly because the compiler replaced the .jar file under the JVM, and it did not realize what's happening.

Restart the backend.

![java-noclassdef](java-noclassdef.png)

### Exposed

#### No Transaction in Context

`Caused by: java.lang.IllegalStateException: No transaction in context.`

Means you forgot to add the transaction block for Exposed:

```kotlin
fun query(executor: Executor, query: ExampleQuery) {
    // this throws the exception
}
```

vs.

```kotlin
fun query(executor: Executor, query: ExampleQuery) = transaction {
  // this works
}
```

#### Loading from Left Join

When loading from a left join and there is no record to join with you may get an exception like this:

```text
java.lang.NullPointerException: Parameter specified as non-null is null: method zakadabar.discussions.dto.PostDto.<init>, parameter relations
	at zakadabar.discussions.dto.PostDto.<init>(PostDto.kt)
```

The solution is use this syntax:

```kotlin
TopicDto(
  relations = (it[RelationTable.relation] as String?) ?: ""
)
```

#### Cannot Import Exposed eq

No idea why this happens, Just add the import manually:

```import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq```

### NPM + Yarn lock - whatever wherever ...

```text
* What went wrong:
  Execution failed for task ':kotlinNpmInstall'.
>                 Process 'Resolving NPM dependencies using yarn' returns 1
> 
```

* Gradle Clean

### kotlinx.serializer stuff cannot be imported after Gradle clean

This happens if you commit with "organize imports" on. Add an `import kotlinx.serialization.*` to the files in error.

## Tips and Tricks

### ZkElement addKClass

If you set addKClass in [ZkElement](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/ZkElement.kt) to `true` you will
see the Kotlin class of your elements in the browser inspector.

Best place to add this setting is the application bootstrap.

```kotlin
ZkElement.addKClass = true
```

![kclass](kclass.png)

### Trace Ktor Routing

To trace Ktor routing set `tracerouting`
in [zakadabar.stack.server.yaml](../../../site/template/app/etc/zakadabar.stack.server.yaml) to true:

```yaml
traceRouting: true
```

### IntelliJ IDEA

* Use Double-Shift to find files fast.
* Use Command-B or Ctrl-B often. As we use Kotlin for everything it works very well.
* Use "Mark as Excluded" and "Show Excluded Files" to hide the boilerplate.

### Running the Webpack devServer in Continuous Mode

* Use the "jsBrowserRun" manually first.
* This will add a run configuration.
* Edit the run configuration and add "--continuous" to "Arguments".

### Focus on Inputs

If you try to focus on a form input field automatically, and it does not work put the focus code into an animation frame
like this:

```kotlin
window.requestAnimationFrame {
  dto::accountName.find().focus()
}
```