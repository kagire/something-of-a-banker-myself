# Bank app

## Launch

1) Set up postgres  
if no local instance - use docker
```
docker run --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=1234 -d postgres
```

2) set up elasticsearch  
disable ssl, if no local instance - use docker
```
docker run -d --name elasticsearch -p 9200:9200 elasticsearch:8.9.0 elasticsearch -Ediscovery.type=single-node -Expack.security.enabled=false -Expack.security.http.ssl.enabled=false
```

3) set up redis  
if no local instance - use docker
```
docker run -d --name redis -p 6379:6379 redis
```

4) application start  
- run prebuilt jar ([relese jar](https://github.com/kagire/something-of-a-banker-myself/releases/tag/v1))
- or run starter via IDE ([main class](https://github.com/kagire/something-of-a-banker-myself/blob/main/src/main/java/bank/BankApplication.java))

## Notes

1) Docker **IS REQUIRED** for tests (testcontainers inside)

2) DB change - i added new table for independent balance incrementing
```sql
CREATE TABLE "ACCOUNT_DEPOSIT_INCREMENT" (
    "ACCOUNT_ID" BIGINT PRIMARY KEY REFERENCES "ACCOUNT"("ID") ON DELETE CASCADE,
    "INITIAL_BALANCE" DECIMAL NOT NULL,
    "CURRENT_BALANCE" DECIMAL NOT NULL,
    "LAST_INCREMENT_AT" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```
more: [migration schema](https://github.com/kagire/something-of-a-banker-myself/blob/main/src/main/resources/db/migration/V1_1__init_schema.sql)

3) Used tool for caching - redis

4) Used tool for search - elasticsearch


