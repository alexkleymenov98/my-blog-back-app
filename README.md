## 🚀 Запуск проекта

### Backend (Spring)

#### Локальный запуск
```bash

# Запустить окружение
cd ..
cd infra

docker compose up -d

Поднимутся контейнеры для frontend, postgress

# собрать проект
cd backend_boot

mvn spring-boot:run

Открыть http://localhost

# Запустить тесты
mvn test

