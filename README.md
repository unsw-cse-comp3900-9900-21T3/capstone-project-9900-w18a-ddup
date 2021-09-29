# E-commerce System Backend

**implement with Spring Framework**

---

## Imported Packages

- Spring Boot
- Spring Security
- Spring Cache
- Spring Data JPA
- PostgreSQL Driver
- Jackson (Serialization and Deserialization)
- Lettuce (Redis connection driver)
- OpenAPI (generate OpenAPI Doc)
- problem-spring-web (beautify http error output)
- ModelMapper (Convert between DTO and entity)
- Embedded H2 Database (test)
- Embedded Redis (test)
- jib (compile docker image)

## Basic Flow Structure

```mermaid
graph LR
A[Client] --> |http| B[Controller]
B--> |http|A
B --> C[Security Filter]
C --> D[Service]
D --> B
D --> E[Cache]
E --> D
D --> F[Repository]
F --> D
F --> G[Database]
G --> F
```

## Development Workflow

```mermaid
graph LR
A[local repo] -->|git push| B[remote repo]
B --> |github action|C{gradle test}
C -->|Pass| D[gradle jib build]
C -->|Fail| E[send notification]
D --> F[push docker image to hub]
F -->|ssh to server|G[docker-compose up]
G --> H[host OpenAPI doc && backend]
```

## Some Development Tips

- About Getter and Setter, if the object is mutable, always create a new instance. For example, `Date` and `byte[]`.
- Use `BigDecimal` when handling price value.
- Use `ModelMapper` for converting `DTO` and `Entity`. Make sure the names of properties are exactly same.
- Cache has not been tested!! [TODO]
- `User.email` is mapped to `username`