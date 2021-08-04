
## Cases

* no SESSION_COOKIE
    * new browser window
    * in application, not logged in
    * in application, logged in
* have SESSION_COOKIE
    * new browser window
    * in application
        * not logged in
            * session expires (delete from SQL table, restart server)
        * logged in
            * session expires (delete from SQL table, restart server)