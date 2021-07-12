# Serving Font Files

**backend**

1. Create a backend module and use `onInstallStatic` (see code below).
1. Add this backend module to your server.

**browser**

1. Create the directory `jsMain/resources/fonts`
1. Copy your font files into this directory.
1. Add the font styles:
   1. Copy the CSS style sheet for the fonts into the `fonts` directory and add reference to `index.html`.
   1. Alternatively, you can add the styles directly to `index.html`.

## JavaScript - CSS file

```html
<link href="/fonts/fonts.css" rel="stylesheet">
```

## JavaScript - Inline

[index.html - inline](/src/jsMain/kotlin/zakadabar/cookbook/browser/css/fontfiles/index.html)

## JVM

[FontFiles.kt](/src/jvmMain/kotlin/zakadabar/cookbook/browser/css/fontfiles/FontFiles.kt)
