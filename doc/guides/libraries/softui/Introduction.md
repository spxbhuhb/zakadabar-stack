# Library: Soft UI

A UI theme inspired by [Soft UI](https://www.creative-tim.com/product/soft-ui-dashboard-react) from [Creative Tim](https://www.creative-tim.com/)

## Setup

**common**

1. add the gradle dependency

**browser**

1. call the install function from `main.kt`
2. use the soft ui themes in `initTheme`

### Common

#### gradle

```kotlin
implementation("hu.simplexion.zakadabar:softui:$stackVersion")
```

### Browser

```kotlin
fun main() {

  application = ZkApplication()

  zakadabar.softui.browser.install()
  
  io {

    with(application) {

      initSession()

      initTheme(SuiLightTheme(), SuiDarkTheme())

      initLocale(strings, defaultLocale = "en")

      initRouting(Routing())

      run()

    }

  }

}
```