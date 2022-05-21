# Library: Soft UI

A UI theme inspired by [Soft UI](https://www.creative-tim.com/product/soft-ui-dashboard-react) from [Creative Tim](https://www.creative-tim.com/)

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Mixing Zk and Sui themes">

If you want to change between Zk and Sui themes on the same page, you have to refresh the page
after the theme change.

The "in-place" theme switch works only when the themes use the same styles which is not true between
Zk and Sui, therefore the switch does not work without refresh.

</div>

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