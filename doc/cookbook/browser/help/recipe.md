# Add Help Icon

```yaml
level: Beginner
targets:
  - browser
tags:
  - help
  - icon
```

To add help to an element:

1. Add a help provider to the application.
1. Use the `withHelp` extension function.

```kotlin
modules += TextHelpProvider()
```

## Example

<div data-zk-enrich="TextHelpModal"></div>

## Guides

- [Help](/doc/guides/browser/builtin/Help.md)

## Code

- [TextHelpModal.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/help/TextHelpModal.kt)
