# Deployment

## Docker

## OpenBSD

The zkBuild gradle task produces a zip file that contains all you need to deploy a Zakadabar based application on
OpenBSD.

### Installation

1. create a directory for the server
1. unzip the distribution package in the directory
1. set permissions:
   * read access on `etc`, `lib`, `var\static` for the server account
   * write access on `var\log`
1. set server parameters in `etc\zakadabar.stack.server.yml`

## Windows

