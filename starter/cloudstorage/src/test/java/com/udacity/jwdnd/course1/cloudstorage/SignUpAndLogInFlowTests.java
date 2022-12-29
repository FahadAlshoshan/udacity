package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignUpAndLogInFlowTests extends CloudStorageApplicationTests {
    @FindBy(id = "logoutButton")
    private WebElement LogOutButton;

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        PageFactory.initElements(driver, this);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @BeforeAll
    public void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @Test
    public void getLoginPage() {
        System.out.println("PORT: " + port);
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }

    @Test
    public void testHomePageInaccessibleWithoutSigningUp() {
        // Try to access home page
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertNotEquals(driver.getCurrentUrl(), "http://localhost:" + this.port + "/home");
    }

    @Test
    public void testLoggingOut() {
        // Create a test account
        doMockSignUp("LO", "Test", "LOGOUT", "123");
        doLogIn("LOGOUT", "123");
        // Try to access home page
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals(driver.getCurrentUrl(), "http://localhost:" + this.port + "/home");
        // Look for log-out button
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOf(LogOutButton));
        LogOutButton.click();
        // Try to access home page after log out
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertNotEquals(driver.getCurrentUrl(), "http://localhost:" + this.port + "/home");
    }
}
