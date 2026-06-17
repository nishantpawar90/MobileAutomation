"""
Generate a comprehensive PDF document explaining the Mobile Automation Framework.
Uses reportlab for PDF generation.
"""
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import inch, cm
from reportlab.lib.colors import HexColor, black, white
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle,
    PageBreak, ListFlowable, ListItem, KeepTogether
)
from reportlab.lib.enums import TA_CENTER, TA_LEFT, TA_JUSTIFY
from reportlab.platypus.tableofcontents import TableOfContents
from datetime import datetime

# Output file
OUTPUT_FILE = "Mobile_Automation_Framework_Documentation.pdf"

def create_styles():
    styles = getSampleStyleSheet()
    
    styles.add(ParagraphStyle(
        name='CoverTitle',
        parent=styles['Title'],
        fontSize=28,
        leading=34,
        alignment=TA_CENTER,
        spaceAfter=20,
        textColor=HexColor('#1a237e')
    ))
    
    styles.add(ParagraphStyle(
        name='CoverSubtitle',
        parent=styles['Normal'],
        fontSize=16,
        leading=20,
        alignment=TA_CENTER,
        spaceAfter=10,
        textColor=HexColor('#37474f')
    ))
    
    styles.add(ParagraphStyle(
        name='SectionTitle',
        parent=styles['Heading1'],
        fontSize=18,
        leading=22,
        spaceBefore=20,
        spaceAfter=12,
        textColor=HexColor('#1a237e'),
        borderWidth=1,
        borderColor=HexColor('#1a237e'),
        borderPadding=5
    ))
    
    styles.add(ParagraphStyle(
        name='SubSection',
        parent=styles['Heading2'],
        fontSize=14,
        leading=18,
        spaceBefore=14,
        spaceAfter=8,
        textColor=HexColor('#283593')
    ))
    
    styles.add(ParagraphStyle(
        name='SubSubSection',
        parent=styles['Heading3'],
        fontSize=12,
        leading=15,
        spaceBefore=10,
        spaceAfter=6,
        textColor=HexColor('#3949ab')
    ))
    
    styles.add(ParagraphStyle(
        name='BodyText2',
        parent=styles['Normal'],
        fontSize=10,
        leading=14,
        spaceAfter=8,
        alignment=TA_JUSTIFY
    ))
    
    styles.add(ParagraphStyle(
        name='CodeBlock',
        parent=styles['Normal'],
        fontSize=8,
        leading=10,
        fontName='Courier',
        backColor=HexColor('#f5f5f5'),
        borderWidth=0.5,
        borderColor=HexColor('#e0e0e0'),
        borderPadding=6,
        spaceAfter=10,
        leftIndent=10
    ))
    
    styles.add(ParagraphStyle(
        name='BulletItem',
        parent=styles['Normal'],
        fontSize=10,
        leading=14,
        leftIndent=20,
        bulletIndent=10,
        spaceAfter=4
    ))
    
    return styles

def build_document():
    doc = SimpleDocTemplate(
        OUTPUT_FILE,
        pagesize=A4,
        rightMargin=1.5*cm,
        leftMargin=1.5*cm,
        topMargin=2*cm,
        bottomMargin=2*cm
    )
    
    styles = create_styles()
    story = []
    
    # ===== COVER PAGE =====
    story.append(Spacer(1, 3*inch))
    story.append(Paragraph("Enterprise Mobile Automation Framework", styles['CoverTitle']))
    story.append(Spacer(1, 0.3*inch))
    story.append(Paragraph("Comprehensive Technical Documentation", styles['CoverSubtitle']))
    story.append(Spacer(1, 0.5*inch))
    story.append(Paragraph("Appium-Based Production-Ready Framework<br/>for Android &amp; iOS Applications", styles['CoverSubtitle']))
    story.append(Spacer(1, 1*inch))
    story.append(Paragraph(f"Version: 1.0.0", styles['CoverSubtitle']))
    story.append(Paragraph(f"Date: {datetime.now().strftime('%B %d, %Y')}", styles['CoverSubtitle']))
    story.append(Paragraph("Author: Nishant Pawar", styles['CoverSubtitle']))
    story.append(Paragraph("Repository: github.com/nishantpawar90/MobileAutomation", styles['CoverSubtitle']))
    story.append(PageBreak())
    
    # ===== TABLE OF CONTENTS =====
    story.append(Paragraph("Table of Contents", styles['SectionTitle']))
    story.append(Spacer(1, 0.2*inch))
    toc_items = [
        "1. Framework Overview & Architecture",
        "2. Technology Stack & Dependencies",
        "3. Project Structure",
        "4. Appium Configuration & Setup",
        "5. Mobile Environment Setup",
        "6. Driver Management & Factory Pattern",
        "7. Page Object Model (POM) Implementation",
        "8. Test Execution Strategy",
        "9. BrowserStack Cloud Integration",
        "10. Reporting & Evidence Capture",
        "11. Data-Driven Testing",
        "12. Enterprise Features",
        "13. GitHub Actions CI/CD Integration",
        "14. Configuration Management",
        "15. Running the Framework",
        "16. Changing the Test Device (Pixel 6 to Samsung Galaxy, etc.)",
        "17. Configuring & Running Tests on BrowserStack"
    ]
    for item in toc_items:
        story.append(Paragraph(item, styles['BodyText2']))
    story.append(PageBreak())
    
    # ===== 1. FRAMEWORK OVERVIEW =====
    story.append(Paragraph("1. Framework Overview & Architecture", styles['SectionTitle']))
    story.append(Paragraph(
        "This is a production-ready, enterprise-grade Mobile Automation Framework built on top of Appium, "
        "designed to automate both Android and iOS native mobile applications. The framework follows "
        "industry best practices including the Page Object Model (POM), Factory Design Pattern, "
        "Builder Pattern, Singleton Pattern, and implements thread-safe parallel execution capabilities.",
        styles['BodyText2']
    ))
    story.append(Paragraph("Key Architectural Principles:", styles['SubSection']))
    principles = [
        "<b>Separation of Concerns:</b> Test logic, page interactions, driver management, and configuration are completely decoupled into independent layers.",
        "<b>Single Responsibility:</b> Each class handles exactly one responsibility - DriverFactory creates drivers, DriverManager manages lifecycle, BasePage handles element interactions.",
        "<b>Open/Closed Principle:</b> Framework is extensible (add new pages, new platforms) without modifying existing code.",
        "<b>Thread Safety:</b> All shared state uses ThreadLocal variables enabling safe parallel execution across multiple devices.",
        "<b>Configuration-Driven:</b> Every behavior can be controlled via properties files, system properties, or environment variables without code changes."
    ]
    for p in principles:
        story.append(Paragraph(f"• {p}", styles['BulletItem']))
    story.append(Spacer(1, 0.2*inch))
    
    story.append(Paragraph("Architecture Layers:", styles['SubSection']))
    layers_data = [
        ['Layer', 'Responsibility', 'Key Classes'],
        ['Test Layer', 'Test cases, assertions, test data', 'BaseTest, LoginTests, HomePageTests, CheckoutE2ETest'],
        ['Page Object Layer', 'Element locators, page interactions', 'BasePage, LoginPage, ProductCatalogPage, CartPage'],
        ['Driver Layer', 'Driver creation, lifecycle, capabilities', 'DriverFactory, DriverManager, DeviceConfig'],
        ['Config Layer', 'Properties, secrets, constants', 'ConfigManager, SecretManager, FrameworkConstants'],
        ['Reporting Layer', 'Screenshots, videos, reports', 'TestListener, ScreenshotUtils, VideoRecorder'],
        ['Data Layer', 'Test data from JSON, Excel, DB, Faker', 'JsonDataReader, ExcelDataReader, TestDataGenerator'],
        ['Enterprise Layer', 'Performance, network logs, self-healing', 'PerformanceMonitor, NetworkLogCapture, SelfHealingLocator'],
        ['Utility Layer', 'Gestures, waits, API client', 'MobileGestures, WaitUtils, ApiClient'],
    ]
    t = Table(layers_data, colWidths=[1.3*inch, 2.3*inch, 3.2*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#1a237e')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,0), 9),
        ('FONTSIZE', (0,1), (-1,-1), 8),
        ('ALIGN', (0,0), (-1,0), 'CENTER'),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 5),
        ('RIGHTPADDING', (0,0), (-1,-1), 5),
        ('TOPPADDING', (0,0), (-1,-1), 4),
        ('BOTTOMPADDING', (0,0), (-1,-1), 4),
    ]))
    story.append(t)
    story.append(PageBreak())
    
    # ===== 2. TECHNOLOGY STACK =====
    story.append(Paragraph("2. Technology Stack & Dependencies", styles['SectionTitle']))
    story.append(Paragraph(
        "The framework leverages a carefully selected set of modern, actively maintained libraries "
        "that together provide a comprehensive mobile test automation solution.",
        styles['BodyText2']
    ))
    
    tech_data = [
        ['Component', 'Technology', 'Version', 'Purpose'],
        ['Language', 'Java', '17 (LTS)', 'Core programming language with modern features (records, sealed classes, pattern matching)'],
        ['Build Tool', 'Maven', '3.9+', 'Dependency management, build lifecycle, profiles for different execution modes'],
        ['Mobile Automation', 'Appium Java Client', '9.3.0', 'Mobile automation protocol - drives Android/iOS via W3C WebDriver protocol'],
        ['WebDriver Protocol', 'Selenium', '4.19.1', 'W3C WebDriver implementation, element interactions, wait strategies'],
        ['Test Framework', 'TestNG', '7.9.0', 'Test organization, parameterization, groups, parallel execution, listeners'],
        ['Android Driver', 'UiAutomator2', '7.6.1', 'Android automation engine - fast, reliable element identification'],
        ['iOS Driver', 'XCUITest', 'Latest', 'iOS automation engine - Apple native test framework integration'],
        ['Reporting (HTML)', 'Extent Reports', '5.1.1', 'Rich HTML reports with screenshots, categories, dashboard'],
        ['Reporting (Allure)', 'Allure TestNG', '2.25.0', 'Detailed step-by-step reports with attachments, history, trends'],
        ['Logging', 'Log4j2', '2.23.0', 'Structured logging with console + file appenders, async support'],
        ['JSON Processing', 'Jackson', '2.16.1', 'Test data reading, API response parsing, config deserialization'],
        ['API Testing', 'REST Assured', '5.4.0', 'Backend API validation alongside mobile UI tests'],
        ['Database', 'MongoDB Driver', '4.11.1', 'Test data retrieval from database, state verification'],
        ['Test Data', 'DataFaker', '2.1.0', 'Dynamic realistic test data generation (names, addresses, emails)'],
        ['Excel Data', 'Apache POI', '5.2.5', 'Excel-based test data reading for data-driven tests'],
        ['AOP', 'AspectJ', '1.9.21', 'Allure step annotation weaving at compile time'],
        ['Cloud Device Farm', 'BrowserStack', 'API v2', 'Real device testing in cloud - 3000+ device/OS combinations'],
    ]
    t = Table(tech_data, colWidths=[1.1*inch, 1.2*inch, 0.6*inch, 3.9*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#1a237e')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,0), 8),
        ('FONTSIZE', (0,1), (-1,-1), 7),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 4),
        ('RIGHTPADDING', (0,0), (-1,-1), 4),
        ('TOPPADDING', (0,0), (-1,-1), 3),
        ('BOTTOMPADDING', (0,0), (-1,-1), 3),
    ]))
    story.append(t)
    story.append(PageBreak())
    
    # ===== 3. PROJECT STRUCTURE =====
    story.append(Paragraph("3. Project Structure", styles['SectionTitle']))
    story.append(Paragraph(
        "The framework follows Maven standard directory layout with clear separation between "
        "main source (framework code) and test source (test cases).",
        styles['BodyText2']
    ))
    story.append(Paragraph(
        "MobileAutomation/<br/>"
        "├── .github/workflows/mobile-automation.yml &nbsp;&nbsp;# GitHub Actions CI/CD pipeline<br/>"
        "├── pom.xml &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Maven build configuration<br/>"
        "├── testng.xml &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Default test suite<br/>"
        "├── testng-smoke.xml &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Smoke test suite (7 critical tests)<br/>"
        "├── testng-regression.xml &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Full regression suite<br/>"
        "├── testng-browserstack.xml &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# BrowserStack device matrix<br/>"
        "├── src/main/java/com/enterprise/mobile/<br/>"
        "│&nbsp;&nbsp; ├── api/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# REST Assured API client<br/>"
        "│&nbsp;&nbsp; ├── config/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# ConfigManager, FrameworkConstants, SecretManager<br/>"
        "│&nbsp;&nbsp; ├── data/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# JsonDataReader, ExcelDataReader, TestDataGenerator, MongoDBClient<br/>"
        "│&nbsp;&nbsp; ├── driver/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# DriverFactory, DriverManager, DeviceConfig, BrowserStackCapabilities<br/>"
        "│&nbsp;&nbsp; ├── enterprise/ &nbsp;&nbsp;# PerformanceMonitor, NetworkLogCapture, SelfHealingLocator<br/>"
        "│&nbsp;&nbsp; ├── enums/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Platform, ExecutionMode, Environment, SwipeDirection, WaitStrategy<br/>"
        "│&nbsp;&nbsp; ├── exceptions/ &nbsp;&nbsp;# Custom exception hierarchy<br/>"
        "│&nbsp;&nbsp; ├── listeners/ &nbsp;&nbsp;&nbsp;# TestListener, RetryAnalyzer, AnnotationTransformer<br/>"
        "│&nbsp;&nbsp; ├── pages/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# BasePage + android/ + ios/ page objects<br/>"
        "│&nbsp;&nbsp; ├── reporting/ &nbsp;&nbsp;&nbsp;# ExtentReportManager, AllureReportManager, ScreenshotUtils<br/>"
        "│&nbsp;&nbsp; └── utils/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# VideoRecorder, WaitUtils, MobileGestures<br/>"
        "├── src/main/resources/config/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Properties files per environment<br/>"
        "├── src/test/java/.../tests/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Test classes (login, home, e2e, smoke)<br/>"
        "└── src/test/resources/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Test data (JSON), APK files, allure config",
        styles['CodeBlock']
    ))
    story.append(PageBreak())
    
    # ===== 4. APPIUM CONFIGURATION =====
    story.append(Paragraph("4. Appium Configuration & Setup", styles['SectionTitle']))
    story.append(Paragraph(
        "Appium is the core automation engine that enables cross-platform mobile testing. This framework uses "
        "Appium 2.x architecture with the java-client 9.3.0, which communicates with automation drivers "
        "(UiAutomator2 for Android, XCUITest for iOS) via the W3C WebDriver protocol.",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("4.1 Appium Server Requirements", styles['SubSection']))
    appium_reqs = [
        "<b>Appium Server:</b> Version 2.x+ (installed globally via npm: <font face='Courier'>npm install -g appium</font>)",
        "<b>UiAutomator2 Driver:</b> <font face='Courier'>appium driver install uiautomator2</font> - Provides Android automation",
        "<b>XCUITest Driver:</b> <font face='Courier'>appium driver install xcuitest</font> - Provides iOS automation",
        "<b>Server URL:</b> Default http://127.0.0.1:4723 (configurable in config.properties)",
        "<b>Protocol:</b> W3C WebDriver (Appium 2.x dropped JSONWP/Mobile JSON Wire Protocol)"
    ]
    for item in appium_reqs:
        story.append(Paragraph(f"• {item}", styles['BulletItem']))
    
    story.append(Paragraph("4.2 Appium Desired Capabilities - Android", styles['SubSection']))
    story.append(Paragraph(
        "The framework configures UiAutomator2Options with the following capabilities for Android testing:",
        styles['BodyText2']
    ))
    caps_data = [
        ['Capability', 'Value', 'Purpose'],
        ['automationName', 'UiAutomator2', 'Specifies the automation engine for Android'],
        ['deviceName', 'emulator-5554', 'Target device (emulator or real device serial)'],
        ['platformVersion', '14', 'Android API level / OS version'],
        ['app', 'src/test/resources/apps/SauceLabs-MyDemoApp.apk', 'Path to the APK under test'],
        ['appPackage', 'com.saucelabs.mydemoapp.android', 'Target app package name'],
        ['appActivity', '...view.activities.SplashActivity', 'Launch activity of the app'],
        ['autoGrantPermissions', 'true', 'Auto-accept runtime permissions'],
        ['noReset', 'false', 'Do not preserve app state between sessions'],
        ['fullReset', 'false', 'Do not uninstall app between sessions'],
        ['noSign', 'true', 'Skip APK re-signing (avoids apksigner dependency)'],
    ]
    t = Table(caps_data, colWidths=[1.5*inch, 2.5*inch, 2.8*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#283593')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,-1), 8),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 4),
        ('RIGHTPADDING', (0,0), (-1,-1), 4),
    ]))
    story.append(t)
    
    story.append(Paragraph("4.3 Appium Desired Capabilities - iOS", styles['SubSection']))
    ios_caps = [
        ['Capability', 'Value', 'Purpose'],
        ['automationName', 'XCUITest', 'Apple native automation engine'],
        ['deviceName', 'iPhone 14', 'Target iOS simulator or real device'],
        ['platformVersion', '16.4', 'iOS version'],
        ['app', 'src/test/resources/apps/ios-app.app', 'Path to .app or .ipa file'],
        ['bundleId', 'com.example.app', 'App bundle identifier'],
        ['autoAcceptAlerts', 'true', 'Auto-accept system popups and alerts'],
        ['wdaLocalPort', 'Auto-assigned', 'Unique port per device for parallel execution'],
    ]
    t = Table(ios_caps, colWidths=[1.5*inch, 2.5*inch, 2.8*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#283593')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,-1), 8),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 4),
        ('RIGHTPADDING', (0,0), (-1,-1), 4),
    ]))
    story.append(t)
    
    story.append(Paragraph("4.4 Selenium Version Pinning (Critical)", styles['SubSection']))
    story.append(Paragraph(
        "Appium java-client 9.3.0 declares a dependency range [4.19.0, 5.0) for Selenium. Maven resolves this to "
        "the latest available (4.45.0+), which removed the ContextAware interface that Appium still uses. "
        "The framework uses &lt;dependencyManagement&gt; to pin selenium-api, selenium-remote-driver, and "
        "selenium-support to exactly 4.19.1, preventing ClassNotFoundException at runtime.",
        styles['BodyText2']
    ))
    story.append(PageBreak())
    
    # ===== 5. MOBILE ENVIRONMENT SETUP =====
    story.append(Paragraph("5. Mobile Environment Setup", styles['SectionTitle']))
    
    story.append(Paragraph("5.1 Android Environment Setup", styles['SubSection']))
    story.append(Paragraph("Prerequisites for local Android testing:", styles['BodyText2']))
    android_setup = [
        "<b>Java JDK 17+</b> - Required for compilation and execution (JAVA_HOME must be set)",
        "<b>Android SDK</b> - Install via Android Studio or command-line tools. Set ANDROID_HOME environment variable",
        "<b>Platform Tools</b> - ADB (Android Debug Bridge) for device communication",
        "<b>Build Tools 34.0.0</b> - Required for APK operations",
        "<b>System Image</b> - android-34;google_apis;x86_64 for emulator",
        "<b>Android Emulator</b> - Create AVD: <font face='Courier'>avdmanager create avd -n Pixel_6 -k 'system-images;android-34;google_apis;x86_64'</font>",
        "<b>Appium Server</b> - <font face='Courier'>npm install -g appium</font> (requires Node.js 18+)",
        "<b>UiAutomator2 Driver</b> - <font face='Courier'>appium driver install uiautomator2</font>",
    ]
    for item in android_setup:
        story.append(Paragraph(f"• {item}", styles['BulletItem']))
    
    story.append(Paragraph("Environment Variables Required:", styles['SubSubSection']))
    story.append(Paragraph(
        "JAVA_HOME=C:\\path\\to\\jdk-17<br/>"
        "ANDROID_HOME=C:\\Users\\&lt;user&gt;\\AppData\\Local\\Android\\Sdk<br/>"
        "PATH=%ANDROID_HOME%\\platform-tools;%ANDROID_HOME%\\emulator;%ANDROID_HOME%\\build-tools\\34.0.0",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("Starting the Environment:", styles['SubSubSection']))
    story.append(Paragraph(
        "# 1. Start Android Emulator<br/>"
        "emulator -avd Pixel_6 -no-snapshot-load<br/><br/>"
        "# 2. Start Appium Server<br/>"
        "appium --address 127.0.0.1 --port 4723<br/><br/>"
        "# 3. Verify device connection<br/>"
        "adb devices  # Should show: emulator-5554    device",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("5.2 iOS Environment Setup", styles['SubSection']))
    ios_setup = [
        "<b>macOS</b> - Required (iOS development only supported on Mac)",
        "<b>Xcode 15+</b> - Includes iOS Simulator and development tools",
        "<b>Xcode Command Line Tools</b> - <font face='Courier'>xcode-select --install</font>",
        "<b>Carthage/CocoaPods</b> - WebDriverAgent dependency management",
        "<b>WebDriverAgent</b> - Appium's iOS automation server (auto-installed by xcuitest driver)",
        "<b>XCUITest Driver</b> - <font face='Courier'>appium driver install xcuitest</font>",
        "<b>ios-deploy</b> - For real device deployment: <font face='Courier'>npm install -g ios-deploy</font>",
    ]
    for item in ios_setup:
        story.append(Paragraph(f"• {item}", styles['BulletItem']))
    
    story.append(Paragraph("5.3 Target Application", styles['SubSection']))
    story.append(Paragraph(
        "The framework is configured to test <b>Sauce Labs My Demo App v2.2.0</b> - a realistic e-commerce "
        "mobile application with product catalog, login, cart, and checkout flows. This provides a "
        "production-like testing scenario.",
        styles['BodyText2']
    ))
    app_details = [
        ['Property', 'Value'],
        ['App Name', 'Sauce Labs My Demo App'],
        ['Version', '2.2.0 (Build 25)'],
        ['Package', 'com.saucelabs.mydemoapp.android'],
        ['Launch Activity', 'com.saucelabs.mydemoapp.android.view.activities.SplashActivity'],
        ['APK Size', '17.1 MB'],
        ['Valid Credentials', 'bob@example.com / 10203040'],
        ['Features', 'Product catalog, Login, Cart, Checkout, Navigation drawer'],
    ]
    t = Table(app_details, colWidths=[1.8*inch, 5*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#283593')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,-1), 9),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t)
    story.append(PageBreak())
    
    # ===== 6. DRIVER MANAGEMENT =====
    story.append(Paragraph("6. Driver Management & Factory Pattern", styles['SectionTitle']))
    
    story.append(Paragraph("6.1 DriverFactory (Factory Method Pattern)", styles['SubSection']))
    story.append(Paragraph(
        "The DriverFactory class is the single entry point for creating Appium driver instances. It uses "
        "the Factory Method pattern to create the appropriate driver (AndroidDriver or IOSDriver) based on "
        "the configured platform and execution mode.",
        styles['BodyText2']
    ))
    story.append(Paragraph("Execution Modes Supported:", styles['SubSubSection']))
    modes = [
        "<b>LOCAL:</b> Connects to a local Appium server (http://127.0.0.1:4723). Tests run on local emulator/simulator or connected USB device.",
        "<b>BROWSERSTACK:</b> Connects to BrowserStack's cloud hub (hub-cloud.browserstack.com). Tests run on real devices in the cloud.",
        "<b>SAUCELABS:</b> Placeholder for Sauce Labs integration (extensible architecture)."
    ]
    for m in modes:
        story.append(Paragraph(f"• {m}", styles['BulletItem']))
    
    story.append(Paragraph("Driver Creation Flow:", styles['SubSubSection']))
    story.append(Paragraph(
        "1. BaseTest.setUp() calls DriverFactory.createDriver()<br/>"
        "2. DriverFactory reads platform (ANDROID/IOS) and mode (LOCAL/BROWSERSTACK) from config<br/>"
        "3. Creates UiAutomator2Options or XCUITestOptions with capabilities<br/>"
        "4. Instantiates AndroidDriver or IOSDriver with server URL + options<br/>"
        "5. Configures implicit wait timeout<br/>"
        "6. Stores driver in DriverManager (ThreadLocal) for thread-safe access<br/>"
        "7. Returns the driver to the test class",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("6.2 DriverManager (ThreadLocal Pattern)", styles['SubSection']))
    story.append(Paragraph(
        "DriverManager uses Java's ThreadLocal to store one AppiumDriver instance per thread. "
        "This is critical for parallel execution - each test method running in its own thread "
        "gets its own isolated driver session. Methods: setDriver(), getDriver(), removeDriver() "
        "(which also calls driver.quit()).",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("6.3 DeviceConfig (Builder Pattern)", styles['SubSection']))
    story.append(Paragraph(
        "DeviceConfig is a POJO with Builder pattern that encapsulates device-specific parameters "
        "for parallel execution. It holds: platform, deviceName, platformVersion, udid, systemPort "
        "(Android), wdaLocalPort (iOS). When running tests on multiple devices simultaneously, "
        "each device needs unique ports to avoid conflicts.",
        styles['BodyText2']
    ))
    story.append(PageBreak())
    
    # ===== 7. PAGE OBJECT MODEL =====
    story.append(Paragraph("7. Page Object Model (POM) Implementation", styles['SectionTitle']))
    
    story.append(Paragraph("7.1 BasePage - Abstract Foundation", styles['SubSection']))
    story.append(Paragraph(
        "Every page object extends BasePage, which provides all common mobile interactions "
        "with built-in wait strategies. BasePage obtains the driver from DriverManager (ThreadLocal), "
        "initializes Appium PageFactory, and exposes protected methods for element interaction.",
        styles['BodyText2']
    ))
    story.append(Paragraph("Key BasePage Features:", styles['SubSubSection']))
    base_features = [
        "<b>Automatic Wait Strategies:</b> Every interaction uses explicit waits - CLICKABLE (for clicks), VISIBLE (for text entry/read), PRESENCE (for attribute checks), NONE (immediate)",
        "<b>FluentWait Support:</b> Configurable polling interval (500ms default) with ignored exceptions (NoSuchElementException, StaleElementReferenceException)",
        "<b>Platform-Aware:</b> isAndroid(), isIOS(), platformLocator() for cross-platform page objects",
        "<b>Gesture Integration:</b> Each page has access to MobileGestures (swipe, tap, long press, drag-drop)",
        "<b>Keyboard Handling:</b> hideKeyboard() uses 'mobile: hideKeyboard' Appium command (platform-specific)",
        "<b>PageFactory Integration:</b> AppiumFieldDecorator auto-initializes @FindBy annotated elements",
    ]
    for f in base_features:
        story.append(Paragraph(f"• {f}", styles['BulletItem']))
    
    story.append(Paragraph("7.2 Android Page Objects", styles['SubSection']))
    story.append(Paragraph("Each page object represents one screen/fragment of the application:", styles['BodyText2']))
    pages_data = [
        ['Page Object', 'Screen', 'Key Methods', 'Locator Strategy'],
        ['ProductCatalogPage', 'Home/Product listing', 'getProductTitles(), tapProductByName(), openMenu(), tapCart()', 'By.id, AccessibilityId'],
        ['NavigationMenuPage', 'Side drawer menu', 'openMenu(), goToLogin(), logout(), goToCatalog()', 'AccessibilityId, XPath+text'],
        ['LoginPage', 'Login form', 'login(), enterUsername(), enterPassword(), tapLogin()', 'By.id (resource-id)'],
        ['ProductDetailPage', 'Product detail', 'addToCart(), setQuantity(), getProductPrice()', 'By.id (resource-id)'],
        ['CartPage', 'Shopping cart', 'isCartEmpty(), getTotalPrice(), proceedToCheckout()', 'By.id (resource-id)'],
        ['CheckoutInfoPage', 'Shipping form', 'fillShippingAddress(), tapToPayment()', 'By.id (resource-id)'],
    ]
    t = Table(pages_data, colWidths=[1.3*inch, 1.2*inch, 2.5*inch, 1.8*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#283593')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,-1), 7.5),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 3),
        ('RIGHTPADDING', (0,0), (-1,-1), 3),
    ]))
    story.append(t)
    
    story.append(Paragraph("7.3 Locator Strategies Used", styles['SubSection']))
    locator_info = [
        "<b>By.id (resource-id):</b> Primary strategy - fastest, most reliable. Example: By.id('com.saucelabs.mydemoapp.android:id/loginBtn')",
        "<b>AppiumBy.accessibilityId (content-desc):</b> Used for elements with content-description. Cross-platform compatible. Example: AppiumBy.accessibilityId('View menu')",
        "<b>AppiumBy.xpath:</b> Fallback for complex scenarios combining text + resource-id. Example: //android.widget.TextView[@text='Log In' and @resource-id='...itemTV']",
    ]
    for item in locator_info:
        story.append(Paragraph(f"• {item}", styles['BulletItem']))
    story.append(PageBreak())
    
    # ===== 8. TEST EXECUTION =====
    story.append(Paragraph("8. Test Execution Strategy", styles['SectionTitle']))
    
    story.append(Paragraph("8.1 Test Lifecycle (TestNG)", styles['SubSection']))
    story.append(Paragraph(
        "The test execution follows TestNG's lifecycle managed by BaseTest and TestListener:",
        styles['BodyText2']
    ))
    story.append(Paragraph(
        "@BeforeMethod (BaseTest.setUp) → Creates Appium driver session<br/>"
        "@BeforeMethod (TestClass.initPages) → Instantiates page objects<br/>"
        "↓<br/>"
        "@Test → Executes test logic with page object interactions<br/>"
        "↓<br/>"
        "@AfterMethod (BaseTest.tearDown) → Calls DriverManager.removeDriver() (quits driver)<br/>"
        "<br/>"
        "Each test gets a FRESH driver session (app reinstalled/reset), ensuring test isolation.",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("8.2 TestNG Suite Configuration", styles['SubSection']))
    story.append(Paragraph(
        "TestNG XML files define which test classes to run, with device parameters passed to BaseTest:",
        styles['BodyText2']
    ))
    story.append(Paragraph(
        "&lt;suite name=\"Smoke Test Suite\" verbose=\"2\"&gt;<br/>"
        "&nbsp;&nbsp;&lt;test name=\"Android Smoke Tests\"&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;parameter name=\"platform\" value=\"ANDROID\"/&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;parameter name=\"deviceName\" value=\"emulator-5554\"/&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;parameter name=\"platformVersion\" value=\"14\"/&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;groups&gt;&lt;run&gt;&lt;include name=\"smoke\"/&gt;&lt;/run&gt;&lt;/groups&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;classes&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;class name=\"...LoginTests\"/&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;class name=\"...HomePageTests\"/&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;class name=\"...CheckoutE2ETest\"/&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;/classes&gt;<br/>"
        "&nbsp;&nbsp;&lt;/test&gt;<br/>"
        "&lt;/suite&gt;",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("8.3 Test Groups", styles['SubSection']))
    groups_data = [
        ['Group', 'Purpose', 'Tests Included'],
        ['smoke', 'Critical path validation (run on every commit)', 'Login, Catalog loads, Prices displayed, Products visible, E2E purchase'],
        ['regression', 'Full feature coverage', 'All smoke + empty fields, locked user, quantity controls, scroll, multi-product cart'],
        ['login', 'Authentication-specific tests', 'Valid login, invalid login, empty fields, locked user, saved credentials'],
        ['catalog', 'Product browsing tests', 'Catalog loads, products displayed, prices, tap product, scroll'],
        ['e2e', 'End-to-end user journeys', 'Complete purchase flow, multi-product cart, checkout validation'],
    ]
    t = Table(groups_data, colWidths=[1*inch, 2.3*inch, 3.5*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#283593')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,-1), 8),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 4),
    ]))
    story.append(t)
    
    story.append(Paragraph("8.4 Maven Execution Commands", styles['SubSection']))
    story.append(Paragraph(
        "# Run smoke tests locally on Android<br/>"
        "mvn test -Dsurefire.suiteXmlFiles=testng-smoke.xml -Dfw.platform=ANDROID -Dfw.execution.mode=LOCAL<br/><br/>"
        "# Run regression tests on BrowserStack<br/>"
        "mvn test -Pregression -Dfw.execution.mode=BROWSERSTACK<br/><br/>"
        "# Run specific test class<br/>"
        "mvn test -Dtest=com.enterprise.mobile.tests.login.LoginTests -Dfw.platform=ANDROID -Dfw.execution.mode=LOCAL<br/><br/>"
        "# Run with Maven profile<br/>"
        "mvn test -Psmoke  &nbsp;&nbsp;# Uses testng-smoke.xml<br/>"
        "mvn test -Pregression  &nbsp;&nbsp;# Uses testng-regression.xml<br/>"
        "mvn test -Pbrowserstack  &nbsp;&nbsp;# Uses testng-browserstack.xml",
        styles['CodeBlock']
    ))
    story.append(PageBreak())
    
    # ===== 9. BROWSERSTACK =====
    story.append(Paragraph("9. BrowserStack Cloud Integration", styles['SectionTitle']))
    story.append(Paragraph(
        "BrowserStack App Automate enables running tests on 3000+ real device and OS combinations "
        "without maintaining a physical device lab. The framework integrates seamlessly with BrowserStack.",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("9.1 How BrowserStack Integration Works", styles['SubSection']))
    story.append(Paragraph(
        "1. <b>Authentication:</b> Username and access key are retrieved from environment variables (BROWSERSTACK_USERNAME, BROWSERSTACK_ACCESS_KEY) via SecretManager<br/><br/>"
        "2. <b>App Upload:</b> APK/IPA is uploaded to BrowserStack via REST API, returning a bs://app-id URL<br/><br/>"
        "3. <b>Capabilities:</b> BrowserStackCapabilities class builds the bstack:options map with device name, OS version, project/build/session names, and feature flags (video, network logs, debug)<br/><br/>"
        "4. <b>Connection:</b> DriverFactory creates the driver with URL: https://USERNAME:ACCESS_KEY@hub-cloud.browserstack.com/wd/hub<br/><br/>"
        "5. <b>Execution:</b> Tests run on real devices in BrowserStack cloud, same test code as local<br/><br/>"
        "6. <b>Results:</b> Video recordings, network logs, and device logs are available in BrowserStack dashboard",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("9.2 BrowserStack Capabilities Configuration", styles['SubSection']))
    bs_caps = [
        ['Capability', 'Value', 'Purpose'],
        ['userName', 'From env: BROWSERSTACK_USERNAME', 'Authentication'],
        ['accessKey', 'From env: BROWSERSTACK_ACCESS_KEY', 'Authentication'],
        ['projectName', 'Mobile Automation', 'Organizes runs in BrowserStack dashboard'],
        ['buildName', 'Build-{timestamp}', 'Groups test sessions into a build'],
        ['sessionName', 'Test-{thread-name}', 'Identifies individual test sessions'],
        ['debug', 'true', 'Enables text logs on BrowserStack'],
        ['networkLogs', 'true', 'Captures HTTP network traffic'],
        ['video', 'true', 'Records video of test execution'],
        ['appiumVersion', '2.4.1', 'Specifies Appium version on BrowserStack'],
        ['idleTimeout', '300', 'Max idle time (seconds) before session termination'],
        ['deviceName', 'Google Pixel 6 / iPhone 14', 'Target real device model'],
        ['osVersion', '13.0 / 16', 'Target OS version'],
    ]
    t = Table(bs_caps, colWidths=[1.4*inch, 2.5*inch, 2.9*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#283593')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,-1), 8),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 4),
    ]))
    story.append(t)
    
    story.append(Paragraph("9.3 Security - Secret Management", styles['SubSection']))
    story.append(Paragraph(
        "Credentials are NEVER stored in source code or properties files. The SecretManager resolves "
        "secrets in priority order: (1) Environment variables (set in CI/CD), (2) JVM system properties "
        "(-DBROWSERSTACK_USERNAME=xxx), (3) Encrypted local store (for development). "
        "In GitHub Actions, secrets are configured as repository secrets and injected as environment variables.",
        styles['BodyText2']
    ))
    story.append(PageBreak())
    
    # ===== 10. REPORTING =====
    story.append(Paragraph("10. Reporting & Evidence Capture", styles['SectionTitle']))
    
    story.append(Paragraph("10.1 Multi-Layer Reporting", styles['SubSection']))
    report_layers = [
        "<b>Allure Reports:</b> Step-by-step execution with @Step annotations, attachments (screenshots, page source, logs), history trends, severity/priority categorization. Deployed to GitHub Pages automatically.",
        "<b>Extent Reports:</b> Rich HTML dashboard with pie charts, test timelines, category views. Screenshots embedded inline on failure. Report saved to test-output/reports/.",
        "<b>TestNG Reports:</b> Standard XML/HTML reports in target/surefire-reports/. Used for CI/CD pass/fail determination.",
        "<b>Console Logging:</b> Structured Log4j2 output with test name, timestamps, and context. Logs saved to logs/framework.log.",
    ]
    for item in report_layers:
        story.append(Paragraph(f"• {item}", styles['BulletItem']))
    
    story.append(Paragraph("10.2 Screenshot Capture", styles['SubSection']))
    story.append(Paragraph(
        "Screenshots are captured automatically on every test failure via TestListener.onTestFailure(). "
        "Three formats are generated simultaneously: (1) PNG file saved to test-output/screenshots/, "
        "(2) Base64 string embedded in Extent Report, (3) byte[] attached to Allure Report. "
        "Manual capture is also available via ScreenshotUtils.captureScreenshot(testName).",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("10.3 Video Recording", styles['SubSection']))
    story.append(Paragraph(
        "Video recording uses Appium's native screen recording APIs (startRecordingScreen / stopRecordingScreen). "
        "For Android: 1280x720 resolution, 5Mbps bitrate, 10-minute max. For iOS: Medium quality, 10-minute max. "
        "Videos are Base64-decoded and saved as MP4 files in test-output/videos/. On failure, video filename "
        "includes '_FAILED' suffix for easy identification. Controlled by config: video.recording=true.",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("10.4 Performance Monitoring", styles['SubSection']))
    story.append(Paragraph(
        "PerformanceMonitor tracks execution time for every test method (startTimer/stopTimer). "
        "Additionally, it can capture Android device metrics: memory usage, CPU utilization, and battery "
        "consumption via Appium's 'mobile: getPerformanceData' command. All metrics are attached to "
        "Allure reports for performance regression detection.",
        styles['BodyText2']
    ))
    story.append(PageBreak())
    
    # ===== 11. DATA-DRIVEN TESTING =====
    story.append(Paragraph("11. Data-Driven Testing", styles['SectionTitle']))
    
    story.append(Paragraph("11.1 JSON Test Data (Jackson)", styles['SubSection']))
    story.append(Paragraph(
        "Test data is stored in src/test/resources/testdata/ as JSON files. JsonDataReader uses Jackson "
        "ObjectMapper to deserialize into Maps, Lists, or POJOs. Example: login/valid-credentials.json "
        "contains {\"username\": \"bob@example.com\", \"password\": \"10203040\"}. Tests load data via "
        "JsonDataReader.readDataAsMap(\"login/valid-credentials.json\").",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("11.2 Dynamic Data Generation (DataFaker)", styles['SubSection']))
    story.append(Paragraph(
        "TestDataGenerator wraps the DataFaker library to produce realistic random data for each test run. "
        "This prevents test flakiness from hardcoded values and increases coverage. Available generators: "
        "generateEmail(), generateFullName(), generateStreetAddress(), generateCity(), generateZipCode(), "
        "generateCountry(), generatePhoneNumber(), generatePassword(). Used in checkout E2E tests for "
        "shipping address generation.",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("11.3 Excel Data (Apache POI)", styles['SubSection']))
    story.append(Paragraph(
        "ExcelDataReader supports .xlsx files for large data-driven test suites. Reads sheets into "
        "List&lt;Map&lt;String, String&gt;&gt; for TestNG DataProvider integration. Useful for parameterized "
        "test scenarios with many combinations.",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("11.4 MongoDB Integration", styles['SubSection']))
    story.append(Paragraph(
        "MongoDBClient provides connection to MongoDB for: (1) Retrieving test data from collections, "
        "(2) Verifying backend state after mobile actions, (3) Setting up test preconditions. "
        "Connection string is configured in properties; database name is configurable per environment.",
        styles['BodyText2']
    ))
    story.append(PageBreak())
    
    # ===== 12. ENTERPRISE FEATURES =====
    story.append(Paragraph("12. Enterprise Features", styles['SectionTitle']))
    
    story.append(Paragraph("12.1 Self-Healing Locators", styles['SubSection']))
    story.append(Paragraph(
        "SelfHealingLocator implements automatic locator recovery. When a primary locator fails (element not found), "
        "it tries alternative locators (by different strategy - id, accessibilityId, xpath, className). "
        "Successful alternatives are cached in a ConcurrentHashMap for the session duration. "
        "A healing report is generated at suite end showing all healed locators, enabling proactive maintenance.",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("12.2 Network Log Capture", styles['SubSection']))
    story.append(Paragraph(
        "NetworkLogCapture records HTTP network activity during test execution using Appium's performance "
        "logging. Captured logs include timestamps, request/response data, and are attached to Allure "
        "reports on failure. This helps diagnose issues caused by API failures or slow backend responses.",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("12.3 Mobile Gestures (W3C Actions API)", styles['SubSection']))
    story.append(Paragraph(
        "MobileGestures implements all common mobile interactions using the W3C Actions API "
        "(compatible with Appium 2.x). This replaces the deprecated TouchAction/MultiTouchAction APIs.",
        styles['BodyText2']
    ))
    gestures_list = [
        "<b>tap(x, y) / tap(element):</b> Single finger tap at coordinates or element center",
        "<b>doubleTap(x, y) / doubleTap(element):</b> Two rapid taps for zoom or selection",
        "<b>longPress(element, durationMs):</b> Press and hold for context menus",
        "<b>swipe(direction):</b> Swipe UP/DOWN/LEFT/RIGHT with configurable edge offset (20%) and duration (800ms)",
        "<b>scrollToElement(element, direction, maxAttempts):</b> Scroll until element becomes visible",
        "<b>scrollWithinElement(container, direction):</b> Scroll inside a specific scrollable container",
        "<b>dragAndDrop(source, target):</b> Drag from one element to another",
        "<b>pinchZoom(scale):</b> Two-finger pinch gesture for zoom in/out",
    ]
    for g in gestures_list:
        story.append(Paragraph(f"• {g}", styles['BulletItem']))
    
    story.append(Paragraph("12.4 API Validation Layer", styles['SubSection']))
    story.append(Paragraph(
        "ApiClient (REST Assured) enables hybrid testing: validate backend state alongside mobile UI tests. "
        "Example: After adding a product to cart via mobile UI, verify the cart API returns the correct item. "
        "Supports GET, POST, PUT, DELETE with authentication token management.",
        styles['BodyText2']
    ))
    story.append(PageBreak())
    
    # ===== 13. GITHUB ACTIONS =====
    story.append(Paragraph("13. GitHub Actions CI/CD Integration", styles['SectionTitle']))
    story.append(Paragraph(
        "The framework uses GitHub Actions for continuous integration and delivery. The workflow is defined in "
        ".github/workflows/mobile-automation.yml and provides automated test execution on every commit.",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("13.1 Trigger Conditions", styles['SubSection']))
    triggers = [
        "<b>Push to main/develop:</b> Automatically runs smoke tests on BrowserStack",
        "<b>Pull Request to main:</b> Validates changes before merge with full smoke suite",
        "<b>Manual Dispatch:</b> UI-triggered with selectable Platform, Environment, Test Suite, and Execution Mode",
    ]
    for t_item in triggers:
        story.append(Paragraph(f"• {t_item}", styles['BulletItem']))
    
    story.append(Paragraph("13.2 Workflow Steps", styles['SubSection']))
    steps = [
        "1. <b>Checkout Code</b> - actions/checkout@v4",
        "2. <b>Setup JDK 17</b> - actions/setup-java@v4 with Temurin distribution + Maven cache",
        "3. <b>Upload App to BrowserStack</b> - Uploads APK via BrowserStack REST API, captures app_url",
        "4. <b>Run Tests</b> - Executes mvn test with platform, environment, mode, and suite parameters",
        "5. <b>Generate Allure Report</b> - Builds HTML report from allure-results",
        "6. <b>Deploy to GitHub Pages</b> - Publishes Allure report to gh-pages branch (accessible via URL)",
        "7. <b>Upload Artifacts</b> - Stores test-output (reports, screenshots, logs) for 30 days",
        "8. <b>Upload Videos</b> - Stores failure videos for 7 days (only on failure)",
        "9. <b>Slack Notification</b> - Sends pass/fail notification with platform, device, suite, and branch info",
    ]
    for s in steps:
        story.append(Paragraph(f"&nbsp;&nbsp;{s}", styles['BulletItem']))
    
    story.append(Paragraph("13.3 Device Matrix Strategy", styles['SubSection']))
    story.append(Paragraph(
        "The workflow uses a matrix strategy to run tests on multiple device configurations in parallel:",
        styles['BodyText2']
    ))
    matrix_data = [
        ['Platform', 'Device', 'OS Version'],
        ['ANDROID', 'Google Pixel 6', '13.0'],
        ['IOS', 'iPhone 14', '16'],
    ]
    t = Table(matrix_data, colWidths=[1.5*inch, 2.5*inch, 1.5*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#283593')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,-1), 9),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('ALIGN', (0,0), (-1,-1), 'CENTER'),
    ]))
    story.append(t)
    
    story.append(Paragraph("13.4 GitHub Secrets Required", styles['SubSection']))
    secrets_data = [
        ['Secret Name', 'Purpose'],
        ['BROWSERSTACK_USERNAME', 'BrowserStack account username for API authentication'],
        ['BROWSERSTACK_ACCESS_KEY', 'BrowserStack access key (from browserstack.com/accounts/settings)'],
        ['SLACK_WEBHOOK_URL', 'Slack incoming webhook URL for notifications'],
        ['GITHUB_TOKEN', 'Auto-provided by GitHub for Pages deployment'],
    ]
    t = Table(secrets_data, colWidths=[2.5*inch, 4.3*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#283593')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,-1), 9),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t)
    story.append(PageBreak())
    
    # ===== 14. CONFIGURATION =====
    story.append(Paragraph("14. Configuration Management", styles['SectionTitle']))
    story.append(Paragraph(
        "The ConfigManager implements a layered configuration system with override priority:",
        styles['BodyText2']
    ))
    story.append(Paragraph(
        "System Properties (-Dfw.xxx) > Environment Config (qa.properties) > Base Config (config.properties) > Defaults",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("14.1 Configuration Files", styles['SubSection']))
    config_files = [
        ['File', 'Purpose'],
        ['config.properties', 'Base configuration - platform, timeouts, Appium URL, device caps, app paths'],
        ['qa.properties', 'QA environment overrides (default active environment)'],
        ['dev.properties', 'Development environment overrides'],
        ['stage.properties', 'Staging environment overrides'],
        ['prod.properties', 'Production environment overrides'],
    ]
    t = Table(config_files, colWidths=[2*inch, 4.8*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#283593')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,-1), 9),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('LEFTPADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t)
    
    story.append(Paragraph("14.2 System Property Overrides", styles['SubSection']))
    story.append(Paragraph(
        "Any property can be overridden at runtime via -Dfw.{key}={value}. The 'fw.' prefix is stripped and "
        "the value replaces the file-based property. Common overrides:",
        styles['BodyText2']
    ))
    story.append(Paragraph(
        "-Dfw.platform=ANDROID|IOS<br/>"
        "-Dfw.execution.mode=LOCAL|BROWSERSTACK|SAUCELABS<br/>"
        "-Dfw.environment=QA|DEV|STAGE|PROD<br/>"
        "-Dfw.browserstack.app.url=bs://xxxxxx",
        styles['CodeBlock']
    ))
    story.append(PageBreak())
    
    # ===== 15. RUNNING THE FRAMEWORK =====
    story.append(Paragraph("15. Running the Framework - Complete Guide", styles['SectionTitle']))
    
    story.append(Paragraph("15.1 Local Execution (Step by Step)", styles['SubSection']))
    story.append(Paragraph(
        "# Step 1: Start Android Emulator<br/>"
        "emulator -avd Pixel_6<br/><br/>"
        "# Step 2: Start Appium Server<br/>"
        "appium<br/><br/>"
        "# Step 3: Run Smoke Tests<br/>"
        "mvn test -Dsurefire.suiteXmlFiles=testng-smoke.xml -Dfw.platform=ANDROID -Dfw.execution.mode=LOCAL<br/><br/>"
        "# Step 4: View Reports<br/>"
        "# Open: test-output/reports/ExtentReport.html<br/>"
        "# Or generate Allure: mvn allure:serve",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("15.2 BrowserStack Execution", styles['SubSection']))
    story.append(Paragraph(
        "# Set credentials as environment variables<br/>"
        "set BROWSERSTACK_USERNAME=your_username<br/>"
        "set BROWSERSTACK_ACCESS_KEY=your_access_key<br/><br/>"
        "# Run on BrowserStack<br/>"
        "mvn test -Pbrowserstack -Dfw.execution.mode=BROWSERSTACK -Dfw.browserstack.app.url=bs://app-id",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("15.3 Test Results Summary (Current)", styles['SubSection']))
    story.append(Paragraph(
        "The smoke suite (7 tests) executes successfully against the Sauce Labs My Demo App on Android 14 emulator:",
        styles['BodyText2']
    ))
    results_data = [
        ['Test', 'Status', 'Duration', 'Category'],
        ['testInvalidLogin', 'PASSED', '~5s', 'smoke, login'],
        ['testLoginPageElements', 'PASSED', '~0.5s', 'smoke, login'],
        ['testSuccessfulLogin', 'PASSED', '~5s', 'smoke, login'],
        ['testProductCatalogLoads', 'PASSED', '~1.4s', 'smoke, catalog'],
        ['testProductPricesDisplayed', 'PASSED', '~1.3s', 'smoke, catalog'],
        ['testProductsAreDisplayed', 'PASSED', '~2.2s', 'smoke, catalog'],
        ['testCompletePurchaseFlow', 'PASSED', '~40s', 'smoke, e2e'],
    ]
    t = Table(results_data, colWidths=[2.2*inch, 1*inch, 1*inch, 1.8*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#1a237e')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,-1), 9),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [HexColor('#e8f5e9'), HexColor('#c8e6c9')]),
        ('ALIGN', (1,0), (2,-1), 'CENTER'),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t)
    story.append(Spacer(1, 0.3*inch))
    story.append(Paragraph(
        "<b>Result: 7/7 tests PASSED | 0 failures | 0 skipped | Total time: ~160 seconds</b>",
        styles['BodyText2']
    ))
    story.append(PageBreak())
    
    # ===== 16. CHANGING DEVICE =====
    story.append(Paragraph("16. Changing the Test Device (e.g., Pixel 6 to Samsung Galaxy)", styles['SectionTitle']))
    story.append(Paragraph(
        "The framework supports running tests on any Android or iOS device — physical or virtual. "
        "Below is a complete guide to switch from the default Pixel 6 emulator to a different device "
        "such as a Samsung Galaxy S23.",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("16.1 Local Execution - Changing the Android Emulator", styles['SubSection']))
    story.append(Paragraph("Step 1: Create a New AVD (Android Virtual Device)", styles['SubSubSection']))
    story.append(Paragraph(
        "# List available system images<br/>"
        "sdkmanager --list | findstr system-images<br/><br/>"
        "# Install a system image (if not already installed)<br/>"
        "sdkmanager \"system-images;android-34;google_apis;x86_64\"<br/><br/>"
        "# Create a Samsung-like AVD (use any name you prefer)<br/>"
        "avdmanager create avd -n Samsung_Galaxy_S23 -k \"system-images;android-34;google_apis;x86_64\" -d \"pixel_6\"<br/><br/>"
        "# Or create via Android Studio: Tools > Device Manager > Create Device > Phone > Galaxy S23<br/>"
        "# Select a system image > Finish",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("Step 2: Start the New Emulator", styles['SubSubSection']))
    story.append(Paragraph(
        "# List available AVDs<br/>"
        "emulator -list-avds<br/><br/>"
        "# Start the Samsung emulator<br/>"
        "emulator -avd Samsung_Galaxy_S23",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("Step 3: Update Configuration", styles['SubSubSection']))
    story.append(Paragraph(
        "You have THREE options to change the device. Choose one:",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("<b>Option A: Update config.properties (permanent change)</b>", styles['BodyText2']))
    story.append(Paragraph(
        "# File: src/main/resources/config/config.properties<br/>"
        "android.device.name=emulator-5554 &nbsp;&nbsp;&nbsp;# Keep as emulator-5554 (ADB serial, same for all emulators)<br/>"
        "android.platform.version=14 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Update if changing Android version",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("<b>Option B: Pass as Maven system property (per-run override)</b>", styles['BodyText2']))
    story.append(Paragraph(
        "mvn test -Dsuite.xml=testng-smoke.xml \\<br/>"
        "&nbsp;&nbsp;-Dfw.platform=ANDROID \\<br/>"
        "&nbsp;&nbsp;-Dfw.execution.mode=LOCAL \\<br/>"
        "&nbsp;&nbsp;-Dfw.android.device.name=emulator-5554 \\<br/>"
        "&nbsp;&nbsp;-Dfw.android.platform.version=14",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("<b>Option C: Update TestNG XML parameters (suite-level)</b>", styles['BodyText2']))
    story.append(Paragraph(
        "&lt;test name=\"Samsung Galaxy S23 Tests\"&gt;<br/>"
        "&nbsp;&nbsp;&lt;parameter name=\"platform\" value=\"ANDROID\"/&gt;<br/>"
        "&nbsp;&nbsp;&lt;parameter name=\"deviceName\" value=\"emulator-5554\"/&gt;<br/>"
        "&nbsp;&nbsp;&lt;parameter name=\"platformVersion\" value=\"14\"/&gt;<br/>"
        "&nbsp;&nbsp;&lt;classes&gt;...&lt;/classes&gt;<br/>"
        "&lt;/test&gt;",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("16.2 Physical Device - USB Connected Samsung", styles['SubSection']))
    story.append(Paragraph(
        "To run on a real physical Samsung device connected via USB:",
        styles['BodyText2']
    ))
    steps_physical = [
        "<b>Enable Developer Options:</b> Settings > About Phone > Tap 'Build Number' 7 times",
        "<b>Enable USB Debugging:</b> Settings > Developer Options > USB Debugging = ON",
        "<b>Connect via USB</b> and accept the 'Allow USB debugging' prompt on device",
        "<b>Verify connection:</b> Run <font face='Courier'>adb devices</font> — device should show with a serial like 'R5CR1234567'",
        "<b>Update config.properties:</b> Set <font face='Courier'>android.device.name=R5CR1234567</font> (use actual serial from adb devices)",
        "<b>Run tests:</b> Same Maven command, device will be targeted automatically",
    ]
    for item in steps_physical:
        story.append(Paragraph(f"• {item}", styles['BulletItem']))
    
    story.append(Paragraph("16.3 BrowserStack - Changing Cloud Device", styles['SubSection']))
    story.append(Paragraph(
        "For BrowserStack cloud execution, change the device in the workflow or via Maven properties:",
        styles['BodyText2']
    ))
    
    bs_devices_data = [
        ['Device Name (BrowserStack)', 'OS Version', 'Maven Property Override'],
        ['Samsung Galaxy S23', '13.0', '-Dfw.browserstack.device=\"Samsung Galaxy S23\" -Dfw.browserstack.os.version=13.0'],
        ['Samsung Galaxy S24 Ultra', '14.0', '-Dfw.browserstack.device=\"Samsung Galaxy S24 Ultra\" -Dfw.browserstack.os.version=14.0'],
        ['Samsung Galaxy A54', '13.0', '-Dfw.browserstack.device=\"Samsung Galaxy A54\" -Dfw.browserstack.os.version=13.0'],
        ['Google Pixel 8', '14.0', '-Dfw.browserstack.device=\"Google Pixel 8\" -Dfw.browserstack.os.version=14.0'],
        ['OnePlus 12', '14.0', '-Dfw.browserstack.device=\"OnePlus 12\" -Dfw.browserstack.os.version=14.0'],
        ['iPhone 15 Pro', '17', '-Dfw.browserstack.device=\"iPhone 15 Pro\" -Dfw.browserstack.os.version=17'],
    ]
    t = Table(bs_devices_data, colWidths=[1.8*inch, 0.8*inch, 4.2*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#283593')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,-1), 7.5),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 4),
        ('RIGHTPADDING', (0,0), (-1,-1), 4),
    ]))
    story.append(t)
    story.append(Spacer(1, 0.2*inch))
    
    story.append(Paragraph(
        "To update the GitHub Actions matrix for a different device, edit "
        "<font face='Courier'>.github/workflows/mobile-automation.yml</font> under the "
        "<font face='Courier'>browserstack-test</font> job's matrix section:",
        styles['BodyText2']
    ))
    story.append(Paragraph(
        "strategy:<br/>"
        "&nbsp;&nbsp;matrix:<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;include:<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- platform: ANDROID<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;device: \"Samsung Galaxy S23\" &nbsp;# ← Change device here<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;os_version: \"13.0\" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# ← Change OS version<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- platform: IOS<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;device: \"iPhone 15 Pro\"<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;os_version: \"17\"",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("16.4 Multi-Device Parallel Execution", styles['SubSection']))
    story.append(Paragraph(
        "To run tests simultaneously on multiple devices, add multiple &lt;test&gt; blocks in a TestNG XML file, "
        "each with different device parameters. Each test block gets its own thread with isolated driver:",
        styles['BodyText2']
    ))
    story.append(Paragraph(
        "&lt;suite name=\"Multi-Device\" parallel=\"tests\" thread-count=\"3\"&gt;<br/>"
        "&nbsp;&nbsp;&lt;test name=\"Samsung Galaxy S23\"&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;parameter name=\"platform\" value=\"ANDROID\"/&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;parameter name=\"deviceName\" value=\"emulator-5554\"/&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;parameter name=\"platformVersion\" value=\"14\"/&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;classes&gt;...&lt;/classes&gt;<br/>"
        "&nbsp;&nbsp;&lt;/test&gt;<br/>"
        "&nbsp;&nbsp;&lt;test name=\"Pixel 8\"&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;parameter name=\"platform\" value=\"ANDROID\"/&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;parameter name=\"deviceName\" value=\"emulator-5556\"/&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;parameter name=\"platformVersion\" value=\"14\"/&gt;<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;&lt;classes&gt;...&lt;/classes&gt;<br/>"
        "&nbsp;&nbsp;&lt;/test&gt;<br/>"
        "&lt;/suite&gt;",
        styles['CodeBlock']
    ))
    story.append(Paragraph(
        "<b>Note:</b> Each emulator must use a different port (5554, 5556, 5558...). Start multiple emulators with: "
        "<font face='Courier'>emulator -avd Samsung_Galaxy_S23 -port 5556</font>",
        styles['BodyText2']
    ))
    story.append(PageBreak())
    
    # ===== 17. BROWSERSTACK CONFIGURATION =====
    story.append(Paragraph("17. Configuring & Running Tests on BrowserStack", styles['SectionTitle']))
    story.append(Paragraph(
        "BrowserStack App Automate allows you to run mobile tests on 3000+ real devices in the cloud. "
        "This section provides step-by-step instructions to configure and execute tests on BrowserStack.",
        styles['BodyText2']
    ))
    
    story.append(Paragraph("17.1 Prerequisites", styles['SubSection']))
    bs_prereqs = [
        "<b>BrowserStack Account:</b> Sign up at <font face='Courier'>https://www.browserstack.com/</font> (free trial available with 100 min)",
        "<b>Username &amp; Access Key:</b> Found at <font face='Courier'>https://www.browserstack.com/accounts/settings</font>",
        "<b>App uploaded to BrowserStack:</b> Upload your APK/IPA to get a <font face='Courier'>bs://</font> app URL",
    ]
    for item in bs_prereqs:
        story.append(Paragraph(f"• {item}", styles['BulletItem']))
    
    story.append(Paragraph("17.2 Step 1: Upload Your App to BrowserStack", styles['SubSection']))
    story.append(Paragraph(
        "Upload your APK file using cURL (or the BrowserStack dashboard):",
        styles['BodyText2']
    ))
    story.append(Paragraph(
        "curl -u \"YOUR_USERNAME:YOUR_ACCESS_KEY\" \\<br/>"
        "&nbsp;&nbsp;-X POST \"https://api-cloud.browserstack.com/app-automate/upload\" \\<br/>"
        "&nbsp;&nbsp;-F \"file=@src/test/resources/apps/SauceLabs-MyDemoApp.apk\" \\<br/>"
        "&nbsp;&nbsp;-F \"custom_id=MyDemoApp\"<br/><br/>"
        "# Response:<br/>"
        "# {\"app_url\": \"bs://a1b2c3d4e5f6...\", \"custom_id\": \"MyDemoApp\"}<br/>"
        "# Save the app_url — you'll need it to run tests",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("17.3 Step 2: Set Environment Variables", styles['SubSection']))
    story.append(Paragraph(
        "Set credentials as environment variables (NEVER hardcode in source code):",
        styles['BodyText2']
    ))
    story.append(Paragraph(
        "# Windows (Command Prompt)<br/>"
        "set BROWSERSTACK_USERNAME=your_username<br/>"
        "set BROWSERSTACK_ACCESS_KEY=your_access_key<br/><br/>"
        "# Windows (PowerShell)<br/>"
        "$env:BROWSERSTACK_USERNAME = \"your_username\"<br/>"
        "$env:BROWSERSTACK_ACCESS_KEY = \"your_access_key\"<br/><br/>"
        "# Linux/macOS<br/>"
        "export BROWSERSTACK_USERNAME=your_username<br/>"
        "export BROWSERSTACK_ACCESS_KEY=your_access_key",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("17.4 Step 3: Run Tests on BrowserStack", styles['SubSection']))
    story.append(Paragraph(
        "Execute the Maven command with BrowserStack execution mode:",
        styles['BodyText2']
    ))
    story.append(Paragraph(
        "# Run smoke tests on BrowserStack (uses default device from testng-browserstack.xml)<br/>"
        "mvn test -Pbrowserstack -Dfw.execution.mode=BROWSERSTACK \\<br/>"
        "&nbsp;&nbsp;-Dfw.browserstack.app.url=bs://a1b2c3d4e5f6<br/><br/>"
        "# Run on a specific device<br/>"
        "mvn test -Pbrowserstack -Dfw.execution.mode=BROWSERSTACK \\<br/>"
        "&nbsp;&nbsp;-Dfw.browserstack.app.url=bs://a1b2c3d4e5f6 \\<br/>"
        "&nbsp;&nbsp;-Dfw.browserstack.device=\"Samsung Galaxy S23\" \\<br/>"
        "&nbsp;&nbsp;-Dfw.browserstack.os.version=13.0<br/><br/>"
        "# Run regression suite with 3 parallel threads<br/>"
        "mvn test -Pregression -Dfw.execution.mode=BROWSERSTACK \\<br/>"
        "&nbsp;&nbsp;-Dfw.browserstack.app.url=bs://a1b2c3d4e5f6 \\<br/>"
        "&nbsp;&nbsp;-Dthread.count=3",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("17.5 Step 4: View Results on BrowserStack Dashboard", styles['SubSection']))
    story.append(Paragraph(
        "After test execution, view results at <font face='Courier'>https://app-automate.browserstack.com/dashboard</font>. "
        "Each test session includes:",
        styles['BodyText2']
    ))
    bs_dashboard = [
        "<b>Video Recording:</b> Full video replay of test execution on the real device",
        "<b>Network Logs:</b> HTTP request/response capture during the test",
        "<b>Device Logs:</b> Logcat (Android) or Console logs (iOS)",
        "<b>Screenshots:</b> Captured at each Appium command",
        "<b>Appium Logs:</b> Complete Appium server logs for debugging",
        "<b>Text Logs:</b> Custom debug logs from the framework",
    ]
    for item in bs_dashboard:
        story.append(Paragraph(f"• {item}", styles['BulletItem']))
    
    story.append(Paragraph("17.6 Step 5: Configure GitHub Actions for BrowserStack", styles['SubSection']))
    story.append(Paragraph(
        "To run BrowserStack tests automatically in CI/CD, configure GitHub repository secrets:",
        styles['BodyText2']
    ))
    story.append(Paragraph(
        "1. Go to your GitHub repository: Settings > Secrets and variables > Actions<br/>"
        "2. Click 'New repository secret' and add:<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;• Name: BROWSERSTACK_USERNAME &nbsp;&nbsp;Value: your_username<br/>"
        "&nbsp;&nbsp;&nbsp;&nbsp;• Name: BROWSERSTACK_ACCESS_KEY &nbsp;&nbsp;Value: your_access_key<br/>"
        "3. (Optional) Add: SLACK_WEBHOOK_URL for test notifications<br/>"
        "4. Go to Actions tab > 'Mobile Automation CI/CD' > 'Run workflow'<br/>"
        "5. Select: Execution Mode = BROWSERSTACK, choose Platform and Test Suite<br/>"
        "6. Click 'Run workflow' — tests will execute on real cloud devices",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("17.7 How the Framework Connects to BrowserStack (Internal Flow)", styles['SubSection']))
    story.append(Paragraph(
        "When execution.mode=BROWSERSTACK, the following happens internally:",
        styles['BodyText2']
    ))
    flow_steps = [
        "1. <b>ConfigManager</b> reads execution.mode=BROWSERSTACK from system property",
        "2. <b>DriverFactory.createDriver()</b> detects BROWSERSTACK mode",
        "3. <b>SecretManager</b> retrieves username/key from env vars (BROWSERSTACK_USERNAME, BROWSERSTACK_ACCESS_KEY)",
        "4. <b>BrowserStackCapabilities</b> builds the bstack:options map with device, OS, project name, video=true, networkLogs=true",
        "5. <b>DriverFactory</b> constructs URL: https://username:key@hub-cloud.browserstack.com/wd/hub",
        "6. <b>AndroidDriver/IOSDriver</b> is created with the remote BrowserStack URL + capabilities",
        "7. Tests execute identically to local — same Page Objects, same assertions",
        "8. On completion, video/logs are available in BrowserStack dashboard",
    ]
    for item in flow_steps:
        story.append(Paragraph(f"&nbsp;&nbsp;{item}", styles['BulletItem']))
    
    story.append(Paragraph("17.8 BrowserStack Configuration Properties", styles['SubSection']))
    story.append(Paragraph(
        "These properties in config.properties control BrowserStack behavior:",
        styles['BodyText2']
    ))
    story.append(Paragraph(
        "# BrowserStack Connection<br/>"
        "browserstack.url=https://hub-cloud.browserstack.com/wd/hub<br/>"
        "browserstack.app.url=bs://app-id &nbsp;&nbsp;&nbsp;# Override with -Dfw.browserstack.app.url=bs://xxx<br/>"
        "browserstack.build.name=Mobile-Automation-Build<br/>"
        "browserstack.project.name=Enterprise-Mobile-Framework<br/><br/>"
        "# Credentials (via environment variables - NEVER in this file)<br/>"
        "# BROWSERSTACK_USERNAME=your_username<br/>"
        "# BROWSERSTACK_ACCESS_KEY=your_access_key<br/><br/>"
        "# BrowserStack Features (set in BrowserStackCapabilities.java)<br/>"
        "# debug=true &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Enable text logs<br/>"
        "# networkLogs=true &nbsp;&nbsp;&nbsp;# Capture HTTP traffic<br/>"
        "# video=true &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Record video<br/>"
        "# appiumVersion=2.4.1 # Appium version on BrowserStack<br/>"
        "# idleTimeout=300 &nbsp;&nbsp;&nbsp;&nbsp;# Max idle seconds before kill",
        styles['CodeBlock']
    ))
    
    story.append(Paragraph("17.9 Supported BrowserStack Devices", styles['SubSection']))
    story.append(Paragraph(
        "Browse the full device list at: <font face='Courier'>https://www.browserstack.com/list-of-browsers-and-platforms/app_automate</font>",
        styles['BodyText2']
    ))
    bs_popular = [
        ['Device', 'Platform', 'OS Versions Available'],
        ['Samsung Galaxy S24 Ultra', 'Android', '14.0'],
        ['Samsung Galaxy S23', 'Android', '13.0, 14.0'],
        ['Samsung Galaxy A54', 'Android', '13.0'],
        ['Google Pixel 8 Pro', 'Android', '14.0'],
        ['Google Pixel 7', 'Android', '13.0'],
        ['OnePlus 12', 'Android', '14.0'],
        ['iPhone 15 Pro Max', 'iOS', '17'],
        ['iPhone 14', 'iOS', '16'],
        ['iPad Pro 12.9 2022', 'iOS', '16'],
    ]
    t = Table(bs_popular, colWidths=[2.2*inch, 1*inch, 1.8*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), HexColor('#283593')),
        ('TEXTCOLOR', (0,0), (-1,0), white),
        ('FONTSIZE', (0,0), (-1,-1), 9),
        ('GRID', (0,0), (-1,-1), 0.5, HexColor('#bdbdbd')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [white, HexColor('#f5f5f5')]),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('LEFTPADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t)
    
    # Build the PDF
    doc.build(story)
    print(f"\n✅ PDF generated successfully: {OUTPUT_FILE}")
    print(f"   Location: D:\\eclipse-workspace\\MobileAutomation\\{OUTPUT_FILE}")

if __name__ == "__main__":
    build_document()
