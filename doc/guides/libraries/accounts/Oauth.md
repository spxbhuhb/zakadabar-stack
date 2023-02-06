# OAuth2 authentication

This lib supports Oauth2 authentication method with id_token processing.

## configure settings

Config section is in the 'lib.accounts.yaml' under the optional 'oauth' key

### Oauth2 related parameters

For details see [ModuleSettings](/lib/accounts/src/commonMain/kotlin/zakadabar/lib/accounts/data/ModuleSettings.kt)

sample yaml section using google's provider
```yaml
oauth:
  - 
    name: google
    displayName: Google
    svgIcon:
    authorizationEndpoint: https://accounts.google.com/o/oauth2/v2/auth
    tokenEndpoint: https://oauth2.googleapis.com/token
    tokenRequestMethod: POST
    jwksUri: https://www.googleapis.com/oauth2/v3/certs
    clientId: ****
    clientSecret: ****
    scopes: [ email, profile ]
    autoRegister: true
    externalApps: [ ]
    claims:
      accountName: email
      fullName: name
      email: email
```
- multiple providers are allowed
- claims are mapped to local db AccountPrivate fields

### login and callback endpoints

    /api/auth/<name>/login

Browser redirects to provider's authorizationEndpoint.

    /api/auth/<name>/callback

Provider redirects back here after authentication.
Callback URL with full protocol and host declaration must be registered as allowed redirect_url in the provider.
After successful authentication a session created for authenticated user and the browser redirects again to the root page.

### external app (android app) using this provider

login syntax:

    /api/auth/<name>/login?app=<external-app-link>

After successful authentication the app with following parameter syntax will open

    <external-app-link>?sessionKey=<sessonKey>&sessionId=<sessionId>

Inside the app using sessionKey as cookie name and sessionId as cookie value allows communication in authorized session.
The external-app-link must be in the configuration yaml externalApps list. It can be app-link or deep-link.

### Extra validations
If the project requires additional checking, there is a callback option

```kotlin
    val authBl by module<AuthProviderBl>()

    authBl.callback = { executor, token ->
        println("Account Id: ${executor.accountId}")
        println("Access token: ${token.accessToken}")
        println("JWT claims: ${token.idToken.claims}")
    }
```

This callback be called after processing request and before creating authenticated session. 