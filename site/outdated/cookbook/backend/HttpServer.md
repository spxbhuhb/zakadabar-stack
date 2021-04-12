# HTTP Server

## Add Static Resources

The stack serves static resources from the directory set in the configuration file
(etc/zakadabar-stack.yaml):

```yaml
staticResources: ./var/static
```

All files from this directory are available directly under the "/" path.