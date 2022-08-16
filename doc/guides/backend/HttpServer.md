# HTTP Server

<div data-zk-enrich="Note" data-zk-flavour="Success" data-zk-title="TODO">
Document the new installStatic method.
</div>

## Static Resources

The stack serves static resources from the directory set in the configuration file
(etc/stack.server.yaml):

```yaml
staticResources: ./var/static
```

There files from this directory are available directly under the "/" path.

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="No Sub-folders">

As per Ktor mechanism, the sub-folders of `staticResources` are not
served by default. See the next section about how to include them.

</div>

## Sub-Folders

To serve sub-folders, override the `onInstallStatic` method in one of your
modules.

This example adds the `fonts` folder to the served resources.

```kotlin
override fun onInstallStatic(route: Any) {
    with (route as Route) {
        static("fonts") {
            files("fonts")
        }
    }
}
```

## Authorising Static Resources

When you want to authorize use of the static resources, you can use a Ktor interceptor.

```kotlin
class DocFilesModule(
    private val namespace: String
) : RoutedModule {

    val settings by setting<Settings>(namespace)

    val publicPathPattern = Regex("/api/content/[a-z]{2}/(welcome|hardware|software)/.*")

    override fun onInstallStatic(route: Any) {
        route as Route
        with(route) {
            static("/api/$namespace") {

                intercept(ApplicationCallPipeline.Call) {
                    val protected = ! call.request.path().matches(publicPathPattern)
                    if (protected && ! call.executor().hasRole(PortalRoles.simplexionDeveloper)) {
                        throw Unauthorized()
                    } else {
                        proceed()
                    }
                }

                staticRootFolder = File(settings.root)
                files(".")
            }
        }
    }
}
```

