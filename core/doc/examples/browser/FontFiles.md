# Font Files

1. Create the directory `jsMain/resources/fonts`
1. Copy your font files into this directory.
1. Add `onInstallStatic` to `jvmMain/src/zakadabar.examples.browser/Module.kt`
1. Add necessary styles to `index.html`

**Module.kt**

```kotlin
override fun onInstallStatic(route: Route) {
    with (route) {
        static("fonts") {
            files("fonts")
        }
    }
}
```

**index.html**

```html
<!doctype html>
<!--suppress ALL -->
<html lang="hu">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Simplexion</title>
    <style>
        html, body {
            width: 100%;
            height: 100%;
            margin: 0 !important;
            padding: 0 !important;
        }

        @font-face {
            font-family: 'Space Grotesk';
            src: url('/fonts/SpaceGrotesk-Bold.eot');
            src: url('/fonts/SpaceGrotesk-Bold.eot?#iefix') format('embedded-opentype'),
            url('/fonts/SpaceGrotesk-Bold.woff2') format('woff2'),
            url('/fonts/SpaceGrotesk-Bold.woff') format('woff'),
            url('/fonts/SpaceGrotesk-Bold.ttf') format('truetype'),
            url('/fonts/SpaceGrotesk-Bold.svg#SpaceGrotesk-Bold') format('svg');
            font-weight: bold;
            font-style: normal;
            font-display: swap;
        }
    </style>
    ...
```