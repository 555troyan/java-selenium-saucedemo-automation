![UI Tests](https://github.com)

# 🛒 SauceDemo UI Automation Framework
... далее твой текст ...




# 🛒 SauceDemo UI & API Automation Framework

Автоматизация тестирования учебного магазина [SauceDemo](https://www.saucedemo.com) на стеке **Java + Selenium + JUnit 5**.

## 🛠 Стек технологий
*   **Java 17** (LTS)
*   **Selenium WebDriver** (UI Automation)
*   **Rest Assured** (API Testing)
*   **JUnit 5** (Test Runner)
*   **Maven** (Build Tool)
*   **Allure Report** (Reporting)
*   **Docker** (Containerization)

## 🏗 Архитектура
В проекте реализован паттерн **Page Object Model (POM)**. Логика страниц отделена от логики тестов, что обеспечивает легкую поддержку кода.

## 🚀 Как запустить

### Локально (Maven)
Для запуска всех тестов в параллельном режиме:
```bash
mvn test
mvn allure:serve
docker build -t sauce-tests .
docker run --rm sauce-tests

