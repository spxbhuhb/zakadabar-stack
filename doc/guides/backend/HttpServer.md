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
