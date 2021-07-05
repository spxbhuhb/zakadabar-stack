# Upgrade 2021-06 To 2021-07

This upgrade performs the following steps:

1. create `account_state` table
1. create `account_credential` table
1. copy data from `account_private`  to `account_state`
1. copy data from `account_private`  to `account_credential`
1. remove non-used columns from `account_private`

## Running

1. stop the server
1. back up the database   
1. copy the jar file into the `bin` directory
1. run upgrade command (see below)
1. delete the jar file from the `bin` directory
1. upgrade server JAR and `var/static`   
1. start up the server
1. check results

```shell
java -jar 2021.6-to-2021.7.5.jar --settings ../etc/stack.server.yaml
```