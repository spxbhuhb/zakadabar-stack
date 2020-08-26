# HTTP Server

## Add Static Resources

The stack serves static resources from the directory set in the configuration file
(etc/zakadabar-stack.yaml):

```yaml
staticResources: ./var/static
```

All files from this directory are available directly under the "/" path.

## Access the Modules REST API

The REST API of the modules are available under `/<type>`. Examples:

```text
http://localhost:3000/2f1be8/forum/10

```

```
http://localhost:3000/apis/3a8627/entities
http://localhost:3000/apis/3a8627/entities?parent=9
```