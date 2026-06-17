# How to Run - Mobile Automation Framework

## Prerequisites

1. **Java 17** installed and `JAVA_HOME` set
2. **Maven 3.8+** installed
3. **Node.js 18+** installed (for Appium)
4. **Android SDK** installed via Android Studio
5. **Appium 2.x** installed globally: `npm install -g appium`
6. **UiAutomator2 driver**: `appium driver install uiautomator2`
7. **Android Emulator** created (e.g., Pixel_6, API 34)

---

## Step 1: Set Environment Variables (PowerShell)

```powershell
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
$env:Path = "$env:ANDROID_HOME\emulator;$env:ANDROID_HOME\platform-tools;$env:ANDROID_HOME\build-tools\34.0.0;$env:Path"
```

---

## Step 2: Start the Android Emulator

```powershell
emulator -avd Pixel_6 -gpu host
```

> Wait until the emulator fully boots (home screen visible).

---

## Step 3: Start Appium Server

Open a **new terminal** and run:

```powershell
appium --address 127.0.0.1 --port 4723 --relaxed-security
```

---

## Step 4: Run Tests

### Run All Smoke Tests
```powershell
mvn test -Dsuite.xml=testng-smoke.xml -Dfw.platform=ANDROID -Dfw.execution.mode=LOCAL
```

### Run All Regression Tests
```powershell
mvn test -Dsuite.xml=testng-regression.xml -Dfw.platform=ANDROID -Dfw.execution.mode=LOCAL
```

### Run a Specific Test Class
```powershell
mvn test -Dtest=com.enterprise.mobile.tests.smoke.AppLaunchTest -Dfw.platform=ANDROID -Dfw.execution.mode=LOCAL
```

### Run a Specific Test Method
```powershell
mvn test -Dtest=com.enterprise.mobile.tests.login.LoginTests#testValidLogin -Dfw.platform=ANDROID -Dfw.execution.mode=LOCAL
```

### Run with a Specific Environment
```powershell
mvn test -Dfw.platform=ANDROID -Dfw.execution.mode=LOCAL -Dfw.environment=QA
```

---

## Running on BrowserStack (Cloud)

### Set Credentials
```powershell
$env:BROWSERSTACK_USERNAME = "your_username"
$env:BROWSERSTACK_ACCESS_KEY = "your_access_key"
```

### Run on BrowserStack
```powershell
mvn test -Dsuite.xml=testng-smoke.xml -Dfw.platform=ANDROID -Dfw.execution.mode=BROWSERSTACK -Dfw.browserstack.app.url=bs://your_app_id
```

### Run on a Specific BrowserStack Device
```powershell
mvn test -Dfw.platform=ANDROID -Dfw.execution.mode=BROWSERSTACK -Dfw.browserstack.app.url=bs://your_app_id -Dfw.browserstack.device="Samsung Galaxy S23" -Dfw.browserstack.os.version="13.0"
```

---

## Useful Commands

| Command | Purpose |
|---------|---------|
| `mvn compile test-compile` | Compile without running tests |
| `mvn dependency:resolve` | Download all dependencies |
| `adb devices` | Verify emulator is connected |
| `appium --version` | Check Appium version |
| `emulator -list-avds` | List available emulators |

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `emulator` not recognized | Set `$env:Path` to include `$env:ANDROID_HOME\emulator` |
| Appium connection refused | Ensure Appium is running on port 4723 |
| Emulator not found by adb | Run `adb kill-server && adb start-server` |
| App not installed | APK is auto-installed from `src/test/resources/apps/` |
| Tests timeout | Increase wait in `config.properties` or check emulator boot |
