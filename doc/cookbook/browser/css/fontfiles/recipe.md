# Using Font Files

```yaml
level: Beginner
targets:
  - jvm
  - browser
tags:
  - css
  - font
```

## Backend

1. Create a backend module and use `onInstallStatic` (see code below).
1. Add this backend module to your server.

## Browser

1. Create the directory `jsMain/resources/fonts`
1. Copy your font files into this directory.
1. Add the font styles:
   1. Copy the CSS style sheet for the fonts into the `fonts` directory and add reference to `index.html`.
   1. Alternatively, you can add the styles directly to `index.html`.

To reference the CSS file form index.html:

```html
<link href="/fonts/fonts.css" rel="stylesheet">
```

## Guides

- [Introduction: Browser](/doc/guides/browser/Introduction.md)
- [HTTP Server](/doc/guides/backend/HttpServer.md)

## Code

- [index-link.html](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/css/fontfiles/index-link.html)
- [index-inline.html](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/css/fontfiles/index-inline.html)
- [FontFiles.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/browser/css/fontfiles/FontFiles.kt)
