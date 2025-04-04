
# 📰 El Pais Scraper Framework

A robust and scalable automation framework that scrapes articles from [El Pais](https://elpais.com/), analyzes the content, translates it into English, and validates key aspects using **Selenium**, **Cucumber BDD**, **TestNG**, and cloud execution via **BrowserStack**.

---

## 🚀 Tech Stack

- 🧪 **Selenium WebDriver**
- 🌐 **BrowserStack Integration**
- 🧾 **Cucumber BDD** with Gherkin Feature Files
- 🧪 **TestNG** (not JUnit)
- ☕ **Java 11+**
- 🛠 **Custom Translator API & Text Analyzer**
- 📄 **.env Configuration** with dotenv

---

## 📁 Project Structure

```
com.elPaisScraper
├── api                    # Translation API and models
│   ├── TranslateRequest
│   ├── TranslateResponse
│   └── Translator
│
├── driver                 # BrowserStack and Driver Factory logic
│   ├── BrowserStackDriverManager
│   ├── BrowserType
│   ├── DriverFactory
│   └── DriverManager
│
├── model                  # Data models
│   └── Article
│
├── scraper                # Article scraping logic
│   └── ArticleFetcher
│
├── util                   # Utility classes
│   ├── ImageDownloader
│   ├── LanguageVerifier
│   ├── PrintHelper
│   ├── SupportingUtilities
│   └── TextAnalyzer

resources
├── Images/                # Downloaded article-related images
├── .env.template          # Sample env file for config
├── .env                   # Actual environment variables
└── rerun.txt              # Failed scenario tracker

test
├── java
│   └── elPaisScraper
│       ├── base
│       │   ├── Hooks
│       │   └── RetryAnalyzer
│       ├── runner
│       │   └── TestRunner
│       └── steps
│           └── StepDefinitions
│
└── resources
    └── features
        └── El Pais Scraper.feature
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the Repo

```bash
git clone https://github.com/iamragavendrans/BrowserStack.git
cd BrowserStack
```

### 2️⃣ Configure BrowserStack Credentials

Rename `.env.template` → `.env` and update it with your keys:

```env
BROWSERSTACK_USERNAME=your_username
BROWSERSTACK_ACCESS_KEY=your_access_key
```

### 3️⃣ Install Dependencies

Make sure you have **Java 11+**, Maven, and a proper IDE like IntelliJ.

Then run:

```bash
mvn clean install
```

---

## 🧪 Running the Tests

You can run the tests using **TestNG**:

```bash
mvn clean test
```

Or from your IDE, execute `TestRunner.java` under `runner` package.

---

## 🧠 Features Covered

- ✅ Web scraping of latest El Pais articles
- ✅ Language detection and English translation
- ✅ Text analysis and keyword validation
- ✅ Download article-related images
- ✅ Cucumber BDD with readable Gherkin steps
- ✅ Cross-browser cloud execution via BrowserStack
- ✅ Retry mechanism for flaky tests
- ✅ Test lifecycle management via Hooks

---

## 📌 Best Practices Followed

- Follows **Page Object + Factory Pattern**
- **Environment-safe** config with `.env`
- **Scalable Driver Factory** with multi-browser support
- **RetryAnalyzer** for failure re-runs
- Utilities separated from core logic
- TestNG-based execution flow

---
