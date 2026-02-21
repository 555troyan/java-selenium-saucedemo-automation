![UI Tests](https://github.com)

# 🛒 SauceDemo UI Automation Framework
Фреймворк для автоматизации тестирования магазина [SauceDemo](https://www.saucedemo.com) на **Java 17**.

## 🔗 Ресурсы
*   **GitHub:** [://github.com](https://://github.com)
*   **Allure Report:** [555troyan.github.io/java-selenium-saucedemo/](https://555troyan.github.io)

## 🛠 Стек и архитектура
*   **Core:** Selenium WebDriver, JUnit 5, Maven.
*   **Patterns:** **Page Object Model (POM)** и **ThreadLocal** (полная изоляция потоков при параллельном запуске).
*   **Stability:** Внедрены **Explicit Waits** и **JS-клики** для устранения Flaky-тестов в Headless-режиме.
*   **Reporting:** Интеграция с Allure Report (шаги, история, графики).

## 🏗 Docker & CI/CD
*   **Контейнеризация:** Настроен `Dockerfile` на базе `maven-chrome` для запуска в изолированной Linux-среде.
*   **Pipeline:** GitHub Actions автоматизирует сборку образа, запуск тестов в 3 потока и деплой отчета на GitHub Pages.

## 🧪 Тест-кейсы
*   **Авторизация:** Позитивные/негативные сценарии, обход SQL-инъекций.
*   **Корзина:** Полный цикл (Add -> Badge Verify -> Remove -> Empty Verify).

## 📥 Запуск
```bash
# Локально
mvn clean test

# Через Docker
docker build -t sauce-tests .
docker run --rm sauce-tests
