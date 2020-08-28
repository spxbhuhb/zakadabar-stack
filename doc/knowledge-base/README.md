# Knowledge Base

## I can't find the CSS, HTML, JavaScript files, where are they?

In the Stack, there are none. For all those things we use Kotlin based solutions. JavaScript is
obvious, the Kotlin compiler takes care of that.

CSS is handled by [CssStyleSheet](../../src/jsMain/kotlin/zakadabar/stack/frontend/util/css.kt).

## Running the Webpack devServer in Continuous Mode

* Use the "jsBrowserRun" manually first.
* This will add a run configuration.
* Edit the run configuration and add "--continuous" to "Arguments".

## Backend Throws NoClassDefFoundError

This is mostly because the compiler replaced the .jar file under the JVM, and it did not realize what's happening.

Restart the backend.

![java-noclassdef](java-noclassdef.png)

## Exposed

### Loading from Left Join

When loading from a left join and there is no record to join with you may get an exception like this:

```text
java.lang.NullPointerException: Parameter specified as non-null is null: method zakadabar.discussions.dto.PostDto.<init>, parameter relations
	at zakadabar.discussions.dto.PostDto.<init>(PostDto.kt)
```
The solution is use this syntax:

```koltin
TopicDto(
    relations = (it[RelationTable.relation] as String?) ?: ""
)
```
 
### Cannot Import Exposed "eq"

No idea why this happens, Just add the import manually:

```import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq```

