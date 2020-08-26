```text
GET /apis/3a8627/entities/43    - json / EntityDTO
        /revisions/1            - binary / snapshot 1 content
        /revisions/last         - binary / last snapshot content

POST /apis/3a8627/entities      - json / EntityDTO
POST /apis/3a8627/entities/43   - binary / new snapshot content

PATCH /apis/3a8627/entities/43  - json / EntityDTO, change name, parent, status

GET /apis/3a8627/resolve/hello/stuff
```