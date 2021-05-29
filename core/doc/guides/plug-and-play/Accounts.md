# Plug and Play: Accounts

Accounts is a simple module to add basic user account support to the application.

* Session handling.
* User functions:
    * Login.
    * Account information update.
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

1. add the gradle dependency,
1. add the module to your server configuration, for details see [Modules](../backend/Modules.md),
1. configure settings, for more information, for details see [Settings](../backend/Settings.md).
1. add the routing to your frontend, for details see [Routing](../browser/structure/Routing.md)
1. add the navigation to your side to open the pages, see [SideBar](../browser/builtin/SideBar.md)

**gradle**

```kotlin
implementation("hu.simplexion.zakadabar:lib-accounts:$accountsVersion")
```

**backend**

```kotlin
zakadabar.lib.accounts.backend.install()
```

**routing**

```kotlin
zakadabar.lib.accounts.frontend.install(this)
```

**navigation** (for sidebar)

```kotlin
withRole(StackRoles.securityOfficer) {
    + item<Accounts>()
    + item<Roles>()
}
```

## Database

The module uses SQL for data persistence. At first run it creates these SQL
objects automatically.

| Table | Content |
| --- | --- |
| `account_private` | Account entities. |
| `role` | Roles defined in the system. |
| `role_grant` | Account - role pairs. |
| `session` | Client session data. |

Two accounts are automatically created:

- `so` - the security officer, initial password comes from the settings
- `anonymous` - the anonymous (public user), locked, and has no password

If the settings does not contain an initial `so` password, the account
will be locked with no password.

Three roles are automatically created and granted to `so`:

- `security-officer` - responsible for security related management
- `site-admin` - responsible for business related management
- `site-member` - member of the site