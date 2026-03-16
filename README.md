## 🚀 Запуск проекта

### Backend (Spring)

#### Локальный запуск
```bash
# собрать проект
cd backend

gradle war

# Запустить окружение
cd ..
cd infra

docker compose up -d

Поднимутся контейнеры для frontend, postgress и tomcat

Открыть http://localhost

# Запустить тесты
gradle test

