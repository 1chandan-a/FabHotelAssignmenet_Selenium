# SauceDemo Automation (FabHotelAssignment)

## Overview
Automation framework to test the end-to-end purchase flow on https://www.saucedemo.com  
**Tech Stack:** Selenium WebDriver 4.30.0, TestNG, Maven, Page Object Model (POM), ExtentReports, Apache PDFBox

---

## Prerequisites
- ✅ Java 17 installed and on PATH
- ✅ Maven 3.9+ installed and on PATH
- ✅ Internet access (WebDriverManager downloads browser drivers automatically)
- ✅ Firefox or Chrome browser installed

---

## Project Structure
```
FebLabAssignment/
├── .github/
│   └── workflows/
│       └── github_ci.yml          # GitHub Actions CI/CD Pipeline
├── src/
│   ├── main/java/
│   │   ├── pages/                 # Page Object Model classes
│   │   │   ├── LoginPage.java
│   │   │   ├── InventoryPage.java
│   │   │   ├── CartPage.java
│   │   │   ├── CheckoutInformationPage.java
│   │   │   ├── CheckoutOverviewPage.java
│   │   │   └── CheckoutCompletePage.java
│   │   └── utils/                 # Utility classes
│   │       ├── ConfigUtil.java
│   │       ├── DriverManager.java
│   │       ├── WaitUtil.java
│   │       ├── ScreenshotUtil.java
│   │       ├── PdfUtil.java
│   │       ├── LoggerUtil.java
│   │       ├── TestListener.java
│   │       └── RetryAnalyzer.java
│   └── test/java/tests/
│       ├── BaseTest.java           # Base test class with setup/teardown
│       └── EndToEndPurchaseTest.java
├── pom.xml                         # Maven configuration (Selenium 4.30.0)
├── testng.xml                      # TestNG suite configuration (Firefox)
└── README.md                       # This file
```

---

## Test Execution Flow

### Test Scenario: End-to-End Purchase Flow
```
1. Login with valid credentials
   ↓
2. Verify Inventory page is displayed
   ↓
3. Sort products by Price (Low to High)
   ↓
4. Add cheapest & most expensive products to cart
   ↓
5. Verify cart count = 2
   ↓
6. Navigate to Cart & Checkout
   ↓
7. Enter shipping information
   ↓
8. Verify order summary
   ↓
9. Generate PDF order summary
   ↓
10. Verify PDF contains product details
   ↓
11. Complete checkout
   ↓
12. Verify order confirmation message
   ✅ TEST PASSED
```

---

## Test Execution Commands

### 1️⃣ **Run All Tests (Firefox via testng.xml)**
```powershell
mvn test
```
**What it does:**
- Executes testng.xml configuration
- Runs on Firefox browser
- Generates reports in `test-output/` and `target/surefire-reports/`

---

### 2️⃣ **Run Specific Test with Chrome (Non-Headless)**
```powershell
mvn test "-Dtest=tests.EndToEndPurchaseTest" -Dbrowser=chrome -Dheadless=false
```
**Parameters:**
- `-Dtest=tests.EndToEndPurchaseTest` - Specific test class
- `-Dbrowser=chrome` - Use Chrome browser
- `-Dheadless=false` - Show browser window

---

### 3️⃣ **Run Specific Test with Firefox**
```powershell
mvn test "-Dtest=tests.EndToEndPurchaseTest" -Dbrowser=firefox -Dheadless=false
```

---

### 4️⃣ **Run with Clean Build**
```powershell
mvn clean test
```
**Removes:**
- `target/` directory
- Old compiled classes
- Previous reports

---

### 5️⃣ **Run Tests & Skip Report Generation**
```powershell
mvn test -DskipTests=false
```

---

## Test Reports & Artifacts

### Report Locations:
| Report Type | Location | Format |
|-------------|----------|--------|
| **ExtentReport** | `test-output/ExtentReport.html` | HTML (Interactive) |
| **Surefire Reports** | `target/surefire-reports/` | XML |
| **Screenshots** | `test-output/screenshots/` | PNG |
| **PDF Order Summary** | `test-output/pdf/Order_Summary.pdf` | PDF |

### View Reports:
```powershell
# Open ExtentReport in browser
start test-output/ExtentReport.html

# List all artifacts
ls test-output/
```

---

## GitHub CI/CD Pipeline Integration

### Automated Pipeline Features:
✅ **Auto-Executes on:**
- Push to `main`, `master`, or `develop` branches
- Pull requests to these branches

✅ **Pipeline Actions:**
1. Checks out code
2. Sets up Java 17 environment
3. Installs Maven dependencies
4. Runs all tests (`mvn test`)
5. Generates and uploads reports
6. Publishes test results

### Setup Instructions:
```powershell
# 1. Initialize Git (if not done)
cd d:\FebLabAssignment
git init

# 2. Add all files
git add .

# 3. Create initial commit
git commit -m "Initial commit: End-to-End Purchase Test Automation"

# 4. Add GitHub remote
git remote add origin https://github.com/YOUR_USERNAME/FebLabAssignment.git

# 5. Push to GitHub
git branch -M main
git push -u origin main
```

### View Pipeline Results:
1. Go to your GitHub repo
2. Click **Actions** tab
3. See workflow runs with ✅ or ❌ status
4. Click on run → **Artifacts** to download reports

---

## Key Features

### Page Object Model (POM)
- Separates page logic from tests
- Easy maintenance & reusability
- Located in: `src/main/java/pages/`

### Test Reliability
- **RetryAnalyzer**: Auto-retries failed tests (max 2 times)
- **TestListener**: Captures screenshots on failure
- **WaitUtil**: Explicit waits for element visibility

### Reporting
- **ExtentReports**: Interactive HTML reports with screenshots
- **Surefire**: XML reports for CI/CD integration
- **PDFBox**: PDF generation for order summaries

### Browser Support
- ✅ Firefox
- ✅ Chrome

---

## Configuration

### Update Test Parameters
Edit `src/main/resources/config.properties`:
```properties
base.url=https://www.saucedemo.com
explicit.wait.seconds=15
user.name=standard_user
```

### Update testng.xml
Edit `testng.xml` to:
- Change browser
- Add/remove test classes
- Configure test parameters

---

## Troubleshooting

### Issue: Tests fail with timeout
**Solution:** Increase explicit wait in `config.properties`
```properties
explicit.wait.seconds=20
```

### Issue: Element not found
**Solution:** Check if product names match exactly in InventoryPage  
Verify locators in browser DevTools

### Issue: PDF generation fails
**Solution:** Ensure `test-output/pdf/` directory exists
Check disk space availability

### Issue: Screenshot not captured
**Solution:** Verify `test-output/screenshots/` directory permissions
Check browser driver compatibility