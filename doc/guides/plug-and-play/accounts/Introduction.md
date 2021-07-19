# Plug and Play: Accounts

Accounts is a module to add basic user account support to the application. It includes:

* Session handling.
* User functions:
    * Login.
    * Account update.
    * Change password.
* Administrator functions:
    * List accounts.
    * Add new account, update existing ones.
    * Grant and revoke roles.
    * Change password on any accounts.
    * Lock and unlock accounts.
    * List, add and change roles.
    
## Setup

To use accounts in your application:

**common**

1. add the gradle dependency

**backend**

1. (optional) define additional roles
1. add the module to your server configuration, for details see [Modules](../../backend/Modules.md)
1. configure settings, for more information, for details see [Settings](../../backend/Settings.md)

**frontend**

1. add the service to your application, see [Introduction: Browser](../../browser/Introduction.md)  
1. add the routing to your frontend, for details see [Routing](../../browser/structure/Routing.md)
1. add the navigation to your side to open the pages, see [SideBar](../../browser/builtin/SideBar.md)

### Common

#### gradle

```kotlin
implementation("hu.simplexion.zakadabar:accounts:$accountsVersion")
```

### Backend

#### additional roles

The `security-officer` role is added by default.

For additional roles, extend the RolesBase class and pass it for install (see below):

```kotlin
object Roles : AppRolesBase() {
    val myRole1 by "my-role-1"
    val myRole2 by "my-role-2"
}
```

#### add module

If you have additional roles and/or extended AccountPrivateBl:

```kotlin
zakadabar.lib.accounts.backend.install(
    roles = Roles,
    accountPrivateBl = MyAccountPrivateBl()
)
```

Otherwise, (this will use an instance of [AppRolesBase](/core/core-core/src/commonMain/kotlin/zakadabar/stack/authorize/roles.kt)):

```kotlin
zakadabar.lib.accounts.backend.install()
```

#### configure settings

For customized settings create a 'lib.account.yaml' file in your settings directory, 
check [ModuleSettings](/lib/accounts/src/commonMain/kotlin/zakadabar/lib/accounts/data/ModuleSettings.kt)
for description of parameters.

```yaml
initialSoPassword: so
# maxFailedLogins: 5
# sessionTimeout: 30
# updateDelay: 120
# expirationCheckInterval: 120
# loginActionRole: ""
# autoValidate: true
# enableAccountList: false
# emailInAccountPublic: false
# phoneInAccountPublic: false
```

### Frontend

#### application

```kotlin
zakadabar.lib.accounts.frontend.install(application)
```

#### routing

```kotlin
zakadabar.lib.accounts.frontend.install(this)
```

#### navigation (for sidebar)

```kotlin
ifAnonymous {
  + item<Login>()
}

withRole(StackRoles.securityOfficer) {
  + group(translate<Accounts>()) {
    + item<Accounts>()
    + item<Roles>()
  }
}

ifNotAnonymous {
  + item<Account>()
  + item(strings.logout) {
    io {
      LogoutAction().execute()
      window.location.href = "/"
    }
  }
}
```

## Database

The module uses SQL for data persistence. At first run it creates these SQL
objects automatically.

| Table | Content |
| --- | --- |
| `account_private` | Private account information. |
| `account_state` | Account state (locked, expired, etc.) |
| `account_credential` | Account credentials. |
| `role` | Roles defined in the system. |
| `role_grant` | Account - role pairs. |
| `session` | Client session data. |

Two accounts are automatically created:

- `so` - the security officer, initial password comes from the settings
- `anonymous` - the anonymous (public user), locked, and has no password

If the settings does not contain an initial `so` password, the account
will be locked with no password.

The `security-officer` role is automatically created and granted to `so`:
