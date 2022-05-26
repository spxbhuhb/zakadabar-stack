# SideBar With Notification Icons

```yaml
level: Beginner
targets:
  - browser
tags:
  - sidebar
  - icons
  - notification icons
```

Add `itemWithNotification` to the sidebar with the following params:
 - text
 - `NotificationIcon`
 - `target`, or `onClick` function
 
Change the number in the counter with calling the `redraw` function on the `NotificationIcon`. The function has an Int parameter, that number will be in the counter. If the number is 0, the counter won't show up.

<div data-zk-enrich="SideBarWithNotificationIcons"></div>


## Guides

- [SideBar](/doc/guides/browser/builtin/SideBar.md)

## Code

- [SideBarWithNotificationIcons.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/sidebar/icons/SideBarWithNotificationIcons.kt)



