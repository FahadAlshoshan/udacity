package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileTests extends CloudStorageApplicationTests {

    private final String largeFile = "upload5m.zip";
    private final String smallFile = "upload1kb.txt";

    @FindBy(id = "fileUpload")
    private WebElement fileSelectButton;

    @FindBy(id = "uploadButton")
    private WebElement uploadButton;

    @FindBy(id = "filesTable")
    private WebElement filesTable;

    @FindBy(id = "nav-tab")
    private WebElement navTab;

    @FindBy(id = "table-file-name")
    private WebElement tableFileName;

    @FindBy(id = "redirectHomeSuccess")
    private WebElement redirectLinkSuccess;

    @FindBy(id = "delete-file-button")
    private WebElement deleteFileButton;

    @FindBy(id = "redirectHomeFailed")
    private WebElement redirectHomeFailed;

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        PageFactory.initElements(driver, this);
        doLogIn("file", "123");
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
        driver = new ChromeDriver();
        PageFactory.initElements(driver, this);
        doMockSignUp("file", "Test", "file", "123");
        driver.quit();
    }

        @Test
        @Order(1)
    public void testLargeUpload() {
        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOf(fileSelectButton));
        fileSelectButton.sendKeys(new File(largeFile).getAbsolutePath());

        uploadButton.click();

        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }

        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
    }

    @Test
    @Order(2)
    public void testUpload() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOf(fileSelectButton));
        fileSelectButton.sendKeys(new File(smallFile).getAbsolutePath());

        uploadButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(redirectLinkSuccess));
        redirectLinkSuccess.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(navTab));

        Assertions.assertEquals(smallFile, tableFileName.getText());
    }

    @Test
    @Order(3)
    public void testUploadDuplicate() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOf(fileSelectButton));
        fileSelectButton.sendKeys(new File(smallFile).getAbsolutePath());

        uploadButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(redirectHomeFailed));
        Assertions.assertTrue(driver.getPageSource().contains("File name already exists."));
    }

    @Test
    @Order(4)
    public void testDeleteFile() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOf(navTab));
        Assertions.assertEquals(smallFile, tableFileName.getText());

        deleteFileButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(redirectLinkSuccess));
        redirectLinkSuccess.click();

        Assertions.assertThrows(NoSuchElementException.class, () -> tableFileName.getText());
    }
}
