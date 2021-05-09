# HTTP Server

<div data-zk-enrich="Note" data-zk-flavour="Success" data-zk-title="TODO">
Document the new installStatic method.
</div>

## Add Static Resources

The stack serves static resources from the directory set in the configuration file
(etc/zakadabar.stack.server.yaml):

```yaml
staticResources: ./var/static
```

All files from this directory are available directly under the "/" path.
