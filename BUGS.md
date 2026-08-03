# Task-Flow — список багов и недоработок

Дата анализа: 31.07.2026
Ветка: `main`, коммит `bd435b1`
Анализ статический (код не запускался), покрыт весь `src/main/java` (162 файла) и миграции Flyway.

Приоритеты:
- **BLOCKER** — функциональность не работает / приложение падает
- **CRITICAL** — дыра в правах доступа или потеря данных
- **MAJOR** — бизнес-процесс работает неправильно
- **MINOR** — качество, консистентность, безопасность конфигурации

---

## BLOCKER

### B-1. Бесконечная рекурсия при удалении колонки доски
**Файл:** `boardColumn/service/impl/BoardColumnServiceImpl.java:76-79`

```java
public void delete(Long id) {
    BoardColumn boardColumn = find(id);
    delete(boardColumn.getId());   // вызывает сам себя
}
```

Метод рекурсивно вызывает сам себя.
**Как воспроизвести:** `DELETE /api/v1/board-columns/{id}` → `StackOverflowError`, 500.
**Исправление:** `boardColumnRepository.delete(boardColumn);`

---

### B-2. Конфликт роутов в ProjectMemberController — DELETE не работает
**Файл:** `projectMember/controller/ProjectMemberController.java:63` и `:74`

```java
@DeleteMapping("/{memberId}")   // removeMember
@DeleteMapping("/{projectId}")  // leaveProject
```

Для Spring это один и тот же URL-паттерн. При запросе выбрасывается
`IllegalStateException: Ambiguous handler methods mapped for ...`.
**Как воспроизвести:** любой `DELETE /api/v1/project-members/{id}` → 500.
**Исправление:** развести пути, например `DELETE /api/v1/project-members/{memberId}` и
`DELETE /api/v1/project-members/projects/{projectId}/leave`.

---

### B-3. Контроллер проектов замаплен на путь пользователей
**Файл:** `project/controller/ProjectController.java:21`

```java
@RequestMapping(PathUtils.USERS)   // = /api/v1/users
```

Проекты доступны по `/api/v1/users`, в одном пространстве с `UserController` и
`ProfileController` (`user/controller/UserController.java:21`, `ProfileController.java:23`).
`GET /api/v1/users/{id}` — это «получить проект», а не пользователя.
**Исправление:** добавить `PathUtils.PROJECT = V1 + "/projects"` и использовать его.

---

### B-4. NPE вместо 404 при работе с несуществующим id
`DefaultCrudService.get()` (`base/service/impl/DefaultCrudService.java:33`) возвращает `null`,
но результат используется без проверки:

| Файл | Строка | Метод |
|---|---|---|
| `project/service/impl/ProjectServiceImpl.java` | 60 | `delete` |
| `project/service/impl/ProjectServiceImpl.java` | 71 | `archive` |
| `project/service/impl/ProjectServiceImpl.java` | 79 | `unarchive` |
| `task/service/impl/TaskServiceImpl.java` | 88 | `delete` |
| `task/service/impl/TaskServiceImpl.java` | 95 | `updatePosition` |
| `tag/service/impl/TagServiceImpl.java` | 36 | `delete` |

**Как воспроизвести:** `PATCH /api/v1/users/999999/archive` → `NullPointerException`, 500 вместо 404.
**Исправление:** использовать `find()` вместо `get()` (он бросает `NotFoundException`).

---

### B-5. Удаление проекта падает на внешних ключах
**Файл:** `project/service/impl/ProjectServiceImpl.java:59-66`, миграция
`db/migration/V1/V1.0000000001__initial_tables.sql:37,51,96`

`repository.delete(project)` — жёсткое удаление, но у `project_member`, `board`, `tag`
нет `ON DELETE CASCADE` и нет каскада на уровне JPA.
**Как воспроизвести:** создать проект → создать в нём доску → удалить проект →
`DataIntegrityViolationException`, 500.
**Исправление:** каскадное удаление в миграции либо soft-delete на уровне сервиса.

---

### B-6. OAuth2 Google: редирект после успешного входа ведёт в никуда
**Файлы:** `security/SecurityConfig.java:52`, `auth/controller/OAuth2SuccessController.java:20,29`

- SecurityConfig редиректит на `/api/auth/oauth2/success`
- Реальный путь контроллера: `/api/v1/auth/oauth2/oauth2/success`
  (в `@RequestMapping` дублируется сегмент `/oauth2`: `PathUtils.AUTH_OAUTH_2 + "/oauth2"`)

Два несоответствия сразу — вход через Google завершается 404.
**Исправление:** убрать лишний `+ "/oauth2"` и указать в `defaultSuccessUrl` актуальный путь.

---

## CRITICAL — права доступа

### C-1. Участники проекта не имеют доступа к проекту
**Файл:** `project/service/impl/ProjectServiceImpl.java:40-55`

```java
public List<Project> findAll() {
    return repository.findAllByOwner(currentUser);   // только свои
}

public Project find(Long id) {
    ...
    if (!project.getOwner().getId().equals(currentUser.getId())) {
        throw new NotFoundException(...);            // только владелец
    }
}
```

Доступ считается по `project.owner_id`, а не по таблице `project_member`.
**Последствие:** пользователь принял приглашение, получил роль `EDITOR` — и не видит проект
ни в списке, ни по прямой ссылке. Весь модуль `projectMember` фактически не подключён к бизнес-логике.
**Исправление:** проверка доступа через `ProjectMemberRepository` со статусом `ACCEPTED`;
владелец — это просто участник с ролью `OWNER` (такая запись уже создаётся в
`ProjectServiceImpl.create:90-95`).

---

### C-2. Роли ProjectRole и BoardRole нигде не проверяются
Значения `OWNER / EDITOR / VIEWER` и `EDITOR / VIEWER` записываются в БД и **не читаются
ни в одном сервисе**. `@PreAuthorize` во всём проекте используется 3 раза и только в
`user/controller/AdminUserController.java:34,44,54`.

`VIEWER` имеет те же возможности, что и `EDITOR`.

Ни в одном из перечисленных мест нет проверки прав:

| Модуль | Файл | Что можно сделать в чужом проекте |
|---|---|---|
| Задачи | `task/service/impl/TaskServiceImpl.java` (весь класс) | создать, изменить, переставить, назначить исполнителей и теги |
| Доски | `board/service/impl/BoardServiceImpl.java:37` | создать доску в любом проекте |
| Колонки | `boardColumn/service/impl/BoardColumnServiceImpl.java:34` | создать колонку на любой доске |
| Теги | `tag/service/impl/TagServiceImpl.java` | создать/удалить тег в любом проекте |
| Комментарии | `comment/service/impl/CommentServiceImpl.java:38,87` | читать и писать комментарии к любой задаче |
| Вложения | `attachment/service/impl/AttachmentServiceImpl.java` | загрузить файл вне контекста проекта |

**Исправление:** единая точка проверки, например
`AccessGuard.requireProjectRole(projectId, EDITOR)` / `requireBoardRole(boardId, EDITOR)`,
вызываемая в начале каждой мутирующей операции. Плюс правило: любой id из URL
валидируется на принадлежность проекту текущего пользователя.

---

### C-3. Можно принять или отклонить чужое приглашение
**Файл:** `projectMember/service/impl/ProjectMemberServiceImpl.java:89-103`

```java
public ProjectMember respondToInvitation(Long memberId, InvitationStatus status) {
    ProjectMember member = find(memberId);   // нет сверки с текущим пользователем
```

`memberId` берётся из URL и не сверяется с `getCurrentUser()`.
**Как воспроизвести:** `PATCH /api/v1/project-members/{чужой memberId}/respond` с телом
`{"status":"ACCEPTED"}` → чужое приглашение принято.
**Исправление:** проверять `member.getUser().getId().equals(currentUser.getId())`, иначе 403.

---

### C-4. Управление участниками доступно любому пользователю
**Файл:** `projectMember/service/impl/ProjectMemberServiceImpl.java:43, 72, 107`

`invite`, `updateRole`, `removeMember` не проверяют, что вызывающий — `OWNER` проекта.
Любой авторизованный пользователь может приглашать людей в чужой проект, менять им роли
и удалять участников.
**Исправление:** проверка роли `OWNER` (или `OWNER`/админ проекта) в начале каждого метода.

---

### C-5. Управление участниками доски без проверки прав
**Файл:** `boardMember/service/impl/BoardMemberServiceImpl.java:37, 63, 70`

`invite`, `updateRole`, `removeMember` — нет проверки прав вызывающего.
Дополнительно в `invite`:
- параметр `email` (строка 37) принимается и **нигде не используется**;
- сообщение об ошибке на строке 50 не соответствует условию
  («Участник не добавлен в эту доску» при проверке принадлежности проекту);
- нет проверки на повторное приглашение → падает на unique-констрейнте
  `board_member(board_id, project_member_id)` с 500 вместо 409.

---

### C-6. Внутренние ошибки утекают клиенту
**Файл:** `common/GlobalExceptionHandler.java:65-75`

`handleGeneric(Exception)` кладёт `ex.getMessage()` в тело ответа — наружу уходят
тексты SQL-ошибок и внутренние детали.

Дополнительно этот обработчик перехватывает `AccessDeniedException`, которую бросает
`CommentServiceImpl.java:66,80` → клиент получает **500 вместо 403**.

**Исправление:** отдельные обработчики для `AccessDeniedException` (403),
`ConstraintViolationException` (400), `DataIntegrityViolationException` (409);
в `handleGeneric` возвращать обезличенное сообщение, детали писать только в лог.

---

### C-7. Секреты в репозитории
**Файл:** `src/main/resources/application.yaml:19, 52`

```yaml
password: 1234
security.jwt.secret: "1ewj+l/KMCUOpR4HH98V2fRtozeFm/VU2tNTze+w8i4="
```

Пароль БД и JWT-секрет закоммичены в git в открытом виде. JWT-секрет скомпрометирован —
с ним можно подписать токен для любого пользователя.
**Исправление:** вынести в переменные окружения, ротировать секрет, почистить историю git.

---

## MAJOR — бизнес-логика

### M-1. Позиции не пересчитываются при удалении → перестановка ломается
**Файлы:**
- `task/service/impl/TaskServiceImpl.java:94-118`
- `board/service/impl/BoardServiceImpl.java:59-83`
- `boardColumn/service/impl/BoardColumnServiceImpl.java:48-72`

Алгоритм предполагает, что `position` строго равна индексу элемента в списке:

```java
columnTasks.remove((int) oldPosition);
columnTasks.add(newPosition, task);
```

Но при удалении задачи/доски/колонки позиции остальных элементов не пересчитываются.
**Как воспроизвести:** создать 5 задач (позиции 0–4) → удалить задачу с позицией 1 →
в колонке остаются позиции `0,2,3,4`, а в списке 4 элемента → попытка переставить задачу
с позицией 4 даёт `IndexOutOfBoundsException` или переставляет не ту задачу.
**Исправление:** пересчёт позиций (`0..n-1`) после каждого удаления; общий `PositionService`
вместо трёх копий одного алгоритма.

---

### M-2. Нет операции «перенести задачу в другую колонку»
**Файлы:** `task/controller/TaskController.java`, `task/mapper/TaskMapper.java:55`

Основной сценарий канбана (drag & drop между колонками) сейчас возможен только через
`PUT /api/v1/task/{id}` со сменой `boardColumnId`, при этом `position` в маппере игнорируется.
**Последствие:** в целевой колонке появляется задача с позицией из старой колонки (дубль),
в исходной колонке остаётся дыра. Оба списка после этого едут (см. M-1).
**Исправление:** отдельная операция `moveTask(taskId, targetColumnId, position)` с пересчётом
позиций в обеих колонках в одной транзакции.

---

### M-3. Гонка при вычислении позиции нового элемента
**Файлы:** `task/service/impl/TaskServiceImpl.java:81`, `board/service/impl/BoardServiceImpl.java:41`,
`boardColumn/service/impl/BoardColumnServiceImpl.java:38`

```java
task.setPosition(repository.countByBoardColumnId(...));
```

При двух параллельных запросах обе задачи получат одинаковую позицию.
В БД нет уникального индекса на `(board_column_id, position)`, `(board_id, position)`,
`(project_id, position)` — конфликт не будет замечен.
**Исправление:** уникальные индексы + пересчёт, либо генерация позиции через `max(position)+1`
в одном SQL-запросе.

---

### M-4. Удаление задачи не выведено в API
**Файл:** `task/controller/TaskController.java`

`TaskServiceImpl.delete` (строка 87) реализован, но `@DeleteMapping` в контроллере нет —
задачу невозможно удалить. Также у метода нет `@Transactional`.

---

### M-5. Дублирование доски не копирует содержимое
**Файл:** `board/service/impl/BoardServiceImpl.java:100-112`

Копируются только `name`, `description`, `position`, `status`. Колонки и задачи не копируются —
получается пустая доска с тем же названием.
**Исправление:** либо копировать структуру (колонки + задачи + теги), либо переименовать
операцию, чтобы она не вводила в заблуждение.

---

### M-6. Статус ARCHIVED ничего не запрещает
**Файлы:** `project/service/impl/ProjectServiceImpl.java:70-82`, `board/service/impl/BoardServiceImpl.java:92-96`

- В архивном проекте можно создавать доски, колонки, задачи, комментарии — проверок
  `ProjectStatus` нет ни в одном сервисе.
- В архивной доске можно создавать колонки и задачи — проверок `BoardStatus` нет.
- У доски есть `archive`, но **нет `unarchive`** — вернуть доску из архива невозможно.
- `ProjectServiceImpl.delete:61` разрешает удалять только `ACTIVE` проект и запрещает
  удалять архивный — логика перевёрнута относительно обычного сценария
  (сначала архивируем, потом удаляем).

---

### M-7. Разная валидация на разных путях назначения исполнителей и тегов
Назначить исполнителя/тег можно двумя способами, и они проверяют разное:

| Путь | Файл | Проверка |
|---|---|---|
| `PUT /api/v1/task/{id}/users` | `TaskServiceImpl.addUsersToTask:134` | проверяет членство в проекте ✔ |
| `PUT /api/v1/task/{id}/purpose-tags` | `TaskServiceImpl.addTagsToTask:120` | проверяет принадлежность тега проекту ✔ |
| `POST /api/v1/task` (создание) | `TaskMapper.assigneeIdToUsers:70`, `tagIdsToTags:84` | **не проверяет ничего** ✘ |

**Как воспроизвести:** при создании задачи передать `assignees` постороннего пользователя
и `tags` из чужого проекта — они будут привязаны.
**Исправление:** вынести проверки в сервис и применять на всех путях.

---

### M-8. TaskDto: один исполнитель вместо коллекции, недетерминированное чтение
**Файлы:** `task/dtos/TaskDto.java:17`, `task/mapper/TaskMapper.java:69-81`, `task/model/Task.java:60`

- В сущности `Collection<User> assignees` (HashSet), в DTO — один `Long assignees`.
- При чтении возвращается `users.iterator().next()` из `HashSet` — **случайный** исполнитель
  из нескольких, порядок не гарантирован.
- При создании задачи можно назначить только одного исполнителя.

**Исправление:** либо `Set<Long> assignees` в DTO, либо явно ограничить модель одним исполнителем.

---

### M-9. Приглашённого, но не принявшего приглашение, можно назначить на задачу
**Файлы:** `projectMember/repository/ProjectMemberRepository.java:17`,
`task/service/impl/TaskServiceImpl.java:137`

`existsByProjectIdAndUserId` не фильтрует по `invitation_status`. Пользователь в статусе
`PENDING` считается участником проекта.
**Исправление:** `existsByProjectIdAndUserIdAndInvitationStatus(..., ACCEPTED)`.

---

### M-10. Комментарии: нет проверки доступа и целостности дерева
**Файл:** `comment/service/impl/CommentServiceImpl.java:38-57`

- Нет проверки, что пользователь имеет доступ к задаче — комментировать и читать
  комментарии можно к любой задаче в системе (`findByTaskId:87` тоже без проверки).
- `parentId` не проверяется на принадлежность той же задаче — можно построить ветку
  из комментариев к разным задачам.
- Удаление комментария с ответами упадёт на FK `comment.parent_id`
  (`V1.0000000001__initial_tables.sql:117`, каскада нет).

---

### M-11. Вложения не связаны с задачей
**Файлы:** `attachment/model/Attachment.java`, `attachment/service/impl/AttachmentServiceImpl.java`

В таблице `attachments` есть колонка `task_id` (`V1.0000000001__initial_tables.sql:128`),
но в сущности соответствующего поля нет. Файлы загружаются «в никуда», привязать их
к задаче невозможно.
Также нет удаления вложения — файлы в MinIO/S3 остаются навсегда.

---

## MINOR

### N-1. Отсутствует валидация в DTO задач и тегов
**Файлы:** `task/dtos/TaskDto.java`, `tag/dtos/TagDto.java`

Стоит `@Valid` в контроллерах, но в самих DTO нет ни одной аннотации!
(`@NotBlank`, `@NotNull`, `@Size`). Проверка срабатывает только на уровне Hibernate
при flush → 500 вместо 400 с понятным сообщением.
Для сравнения, в `board/dtos`, `boardColumn/dtos`, `auth/dtos` валидация есть.

Дополнительно: `title` в БД `varchar(50)`, а в DTO ограничения нет — длинный заголовок
даст ошибку БД.

### N-2. Один DTO на три разные операции
**Файл:** `task/dtos/UpdateDto.java`

`UpdateDto(position, idTags, idUsers)` используется для смены позиции, назначения тегов
и назначения исполнителей — в каждом случае два из трёх полей лишние и не валидируются.
`position` объявлена как `Long`, хотя в сущности `Integer` (приведение в
`TaskServiceImpl.java:98`). При этом рядом лежит неиспользуемый `UpdatePosition.java`.

### N-3. Endpoint-слой возвращает входной запрос вместо результата
**Файл:** `task/endpoint/impl/TaskEndpointImpl.java:51-78`

Все методы возвращают тот же `request`, который приняли, а не актуальное состояние задачи.
Клиент не видит фактический результат операции (например, скорректированную позицию).

### N-4. Дублирование кода перестановки позиций
Один и тот же алгоритм скопирован в трёх местах (см. M-1). Любое исправление придётся
вносить трижды.

### N-5. Непоследовательные типы исключений
В одних сервисах используется `NotFoundException` (`base/exception`), в других —
`jakarta.persistence.EntityNotFoundException`
(`BoardServiceImpl.java:39`, `BoardColumnServiceImpl.java:36`, `TaskMapper.java:66,73,88`).
Второй тип не обрабатывается в `GlobalExceptionHandler` → 500 вместо 404.

Также `IllegalArgumentException` используется для бизнес-конфликтов
(`ProjectMemberServiceImpl.java:55,59,76,80,93,111,127`) — по смыслу это 409 Conflict,
а отдаётся 400.

### N-6. Отсутствуют `@Transactional` на изменяющих методах
`BoardMemberServiceImpl.updateRole:63`, `removeMember:70`, `TaskServiceImpl.delete:87`,
`ProjectMemberServiceImpl.getByProject:134`, `BoardServiceImpl.findByProjectId:86`.

### N-7. Конфигурация
**Файл:** `src/main/resources/application.yaml`
- `show-sql: true` и `logging.level.org.hibernate.SQL: debug` в основном профиле — SQL в проде.
- `spring.jpa.open-in-view` не выключен (по умолчанию `true`) — скрывает N+1 запросы.
- Нет профилей `dev`/`prod`, все настройки в одном файле.

### N-8. Тесты отсутствуют полностью
Каталога `src/test` в проекте нет. Ни одного юнит- или интеграционного теста.

### N-9. Мелочи в модели
- `Project.tags` (`project/model/Project.java:46`) не инициализировано (`null` вместо пустой
  коллекции) — в отличие от `Task.tags`/`Task.assignees`.
- `Comment` наследует `BaseEntity` и дублирует поля `createdAt`/`updatedAt` вручную
  (`comment/model/Comment.java:45-51`), хотя есть `AuditableEntity`.
- `CommentServiceImpl.java:65,79` сравнивает сущности через `equals` на ленивом прокси —
  надёжнее сравнивать `getId()`.
- Опечатки в Swagger-описаниях: «Возвращяет», «Обнововить», «приглащение», «Укадите»
  (`TaskController`, `ProjectController`, `ProjectMemberController`).

---

## Рекомендуемый порядок работ

1. **Спринт 1 (блокеры, ~1 день):** B-1 … B-6. Без этого часть API не отвечает.
2. **Спринт 2 (доступ, ключевое):** C-1 — переписать доступ к проекту через `ProjectMember`;
   затем C-2 — ввести единый `AccessGuard` и расставить проверки ролей; C-3, C-4, C-5.
3. **Спринт 3 (безопасность):** C-6, C-7.
4. **Спринт 4 (канбан-механика):** M-1, M-2, M-3, M-4 — вынести позиционирование в общий сервис.
5. **Спринт 5 (жизненный цикл):** M-5, M-6, M-7, M-8, M-9, M-10, M-11.
6. **Дальше:** MINOR + покрытие тестами (в первую очередь — позиционирование и права доступа).
