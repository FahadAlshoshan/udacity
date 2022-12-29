package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTests extends CloudStorageApplicationTests {
    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialTab;

    @FindBy(id = "nav-tab")
    private WebElement navTab;

    @FindBy(id = "addCredentialButton")
    private WebElement addCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement inputCredentialURL;

    @FindBy(id = "credential-username")
    private WebElement inputCredentialUsername;

    @FindBy(id = "credential-password")
    private WebElement inputCredentialPassword;

    @FindBy(id = "save-credential-changes")
    private WebElement credentialSubmitButton;

    @FindBy(id = "redirectHomeSuccess")
    private WebElement redirectLink;

    @FindBy(id = "credentialTable")
    private WebElement credentialsTable;

    @FindBy(id = "table-credential-url")
    private WebElement tableCredentialURL;

    @FindBy(id = "table-credential-username")
    private WebElement tableCredentialUsername;

    @FindBy(id = "table-credential-password")
    private WebElement tableCredentialPassword;

    @FindBy(id = "btnEditCredential")
    private WebElement editCredentialButton;

    @FindBy(id = "delete-credential-button")
    private WebElement deleteCredentialButton;

    private String testCredentialURL = "Test URL";
    private String testCredentialUsername = "Test Username";
    private String testCredentialPassword = "Test Password";

    private String testEditCredentialURL = "Edited URL";
    private String testEditCredentialUsername = "Edited Username";
    private String testEditCredentialPassword = "Edited Password";

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        PageFactory.initElements(driver, this);
        doLogIn("credential", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialTab));
        credentialTab.click();
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
        doMockSignUp("credential", "Test", "credential", "123");
        driver.quit();
    }

    private Credential submitCredentialForm(String URL, String username, String password) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOf(inputCredentialURL));

        inputCredentialURL.click();
        inputCredentialURL.clear();
        inputCredentialURL.sendKeys(URL);

        inputCredentialUsername.click();
        inputCredentialUsername.clear();
        inputCredentialUsername.sendKeys(username);

        inputCredentialPassword.click();
        inputCredentialPassword.clear();
        inputCredentialPassword.sendKeys(password);

        credentialSubmitButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(redirectLink));
        redirectLink.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(navTab));
        credentialTab.click();

        return new Credential(URL, username, password);
    }

    @Test
    @Order(1)
    public void testAddingCredential() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOf(addCredentialButton));
        addCredentialButton.click();

        Credential credential = submitCredentialForm(testCredentialURL, testCredentialUsername, testCredentialPassword);
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTable));

        Assertions.assertEquals(credential.getUrl(), tableCredentialURL.getText());
        Assertions.assertEquals(credential.getUsername(), tableCredentialUsername.getText());
        Assertions.assertNotEquals(credential.getPassword(), tableCredentialPassword.getText());
    }

    @Test
    @Order(2)

    public void testEditingCredential() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTable));

        Assertions.assertEquals(testCredentialURL, tableCredentialURL.getText());
        Assertions.assertEquals(testCredentialUsername, tableCredentialUsername.getText());
        Assertions.assertNotEquals(testCredentialPassword, tableCredentialPassword.getText());

        webDriverWait.until(ExpectedConditions.visibilityOf(editCredentialButton));
        editCredentialButton.click();

        Credential editCredential = submitCredentialForm(testEditCredentialURL,testEditCredentialUsername,testEditCredentialPassword);
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTable));

        Assertions.assertEquals(editCredential.getUrl(), tableCredentialURL.getText());
        Assertions.assertEquals(editCredential.getUsername(), tableCredentialUsername.getText());
        Assertions.assertNotEquals(editCredential.getPassword(), tableCredentialPassword.getText());
    }
    @Test
    @Order(3)
    public void testDeleteCredential() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTable));

        Assertions.assertEquals(testEditCredentialURL, tableCredentialURL.getText());
        Assertions.assertEquals(testEditCredentialUsername, tableCredentialUsername.getText());
        Assertions.assertNotEquals(testEditCredentialPassword, tableCredentialPassword.getText());

        deleteCredentialButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(redirectLink));
        redirectLink.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(navTab));
        credentialTab.click();

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            tableCredentialURL.getText();
            tableCredentialUsername.getText();
            tableCredentialPassword.getText();
        });
    }
}
