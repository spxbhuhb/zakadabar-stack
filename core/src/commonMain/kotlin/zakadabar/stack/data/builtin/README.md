This directory contains the DTOs for basic account, role and session management.

* [AccountPublic](AccountPublic.kt) account information available for public or for any member
* [BlobDto](BlobDto.kt) metadata for binary content as files, images
* [LoginDto](LoginDto.kt) contains account name and password to perform authentication
* [PasswordChangeDto](PasswordChangeDto.kt) old and new password to perform a password change
* [PrincipalDto](PrincipalDto.kt) authentication data
* [RoleDto](RoleDto.kt) describes a single role with a name, used for authorization
* [RoleGrantDto](RoleGrantDto.kt) is a link between a role and an account, when exists the account has the given role
