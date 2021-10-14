# Next

This page contains the changes included in the next release.

# Core

**added**

- `portCookie` server setting, when true, adds the port to the session cookie (default is false)

# Lib: Account

**added**

- session cookie now contains the port number by default (can be disabled with settings)

**changed**

- `LoginForm` new constructor parameter `setAppTitle`

**fixed**

- After session timeout login the header title change to LoginForm #74

