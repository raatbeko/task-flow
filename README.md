# umay-back-core

Spring Boot backend core — готовая база для быстрого старта новых проектов. Содержит всё необходимое: авторизацию, работу с файлами, миграции БД, CRUD-инфраструктуру. Достаточно добавить свои сущности и бизнес-логику.

## Стек

- **Java 25** + **Spring Boot 4.0.2**
- **Spring Security** — JWT + Google OAuth2
- **PostgreSQL** + **Flyway** (миграции)
- **MinIO** (хранение файлов, профиль `minio`) / **S3** (профиль `s3`)
- **QueryDSL** — типобезопасные запросы
- **MapStruct** — маппинг DTO
- **Lombok**
- **Swagger UI** — `http://localhost:8080/swagger-ui.html`

## Быстрый старт

### 1. Требования

- Java 25
- PostgreSQL (база `core`, пользователь `postgres`, пароль `postgres`)
- MinIO (endpoint `http://localhost:9000`, bucket `core`)

### 2. Переменные окружения

```env
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
```

### 3. Запуск

```bash
./mvnw spring-boot:run
```

Flyway автоматически накатит миграции при старте.

## Структура проекта

```
src/main/java/kg/core/
├── auth/           # JWT-авторизация, OAuth2 (Google), регистрация/логин
├── user/           # Пользователи: CRUD, профиль, роли (ADMIN/USER)
├── attachment/     # Загрузка и хранение файлов
├── storage/        # FileStorageService: MinIO и S3 реализации
├── security/       # SecurityConfig, JwtFilter, OAuth2UserService
├── base/           # Базовые классы: BaseEntity, CrudService, пагинация, исключения
├── annotation/     # @SearchParams и резолвер для фильтрации/сортировки
└── common/         # GlobalExceptionHandler
```

## Как добавить новую сущность

1. Создай модель, унаследовав `BaseEntity` / `AuditableEntity`
2. Создай репозиторий, унаследовав `BaseRepository<T, ID>`
3. Создай сервис, унаследовав `DefaultCrudService<T, ID, REQ, RESP>`
4. Добавь контроллер и DTO
5. Добавь Flyway-миграцию в `src/main/resources/db/migration/`

## Авторизация

| Метод | Endpoint | Описание |
|-------|----------|----------|
| POST | `/api/auth/register` | Регистрация |
| POST | `/api/auth/login` | Вход (JWT) |
| POST | `/api/auth/refresh` | Обновление токена |
| GET | `/login/oauth2/code/google` | Google OAuth2 callback |

JWT-токен передаётся в заголовке: `Authorization: Bearer <token>`

## Профили

- `minio` (по умолчанию) — локальное хранилище файлов через MinIO
- `s3` — AWS S3

Переключить: `spring.profiles.active=s3` в `application.yaml`.

