package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
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
public class NoteTests extends CloudStorageApplicationTests {

    @FindBy(id = "nav-notes-tab")
    private WebElement noteTab;

    @FindBy(id = "nav-tab")
    private WebElement navTab;

    @FindBy(id = "addNoteButton")
    private WebElement addNoteButton;

    @FindBy(id = "note-title")
    private WebElement inputNoteTitle;

    @FindBy(id = "note-description")
    private WebElement inputNoteDescription;

    @FindBy(id = "save-note-changes")
    private WebElement noteSubmitButton;

    @FindBy(id = "redirectHomeSuccess")
    private WebElement redirectLink;

    @FindBy(id = "userTable")
    private WebElement notesTable;

    @FindBy(id = "table-note-title")
    private WebElement tableNoteTitle;

    @FindBy(id = "table-note-description")
    private WebElement tableNoteDescription;

    @FindBy(id = "edit-note-button")
    private WebElement editNoteButton;

    @FindBy(id = "delete-note-button")
    private WebElement deleteNoteButton;

    private String testNoteTitle = "Test Title";
    private String testNoteDescription = "Test Description";

    private String testEditNoteTitle = "Edited Title";
    private String testEditNoteDescription = "Edited Description";

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        PageFactory.initElements(driver, this);
        doLogIn("note", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOf(noteTab));
        noteTab.click();
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
        doMockSignUp("note", "Test", "note", "123");
        driver.quit();
    }

    private Note submitNoteForm(String title, String description) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOf(inputNoteTitle));
        inputNoteTitle.click();
        inputNoteTitle.clear();
        inputNoteTitle.sendKeys(title);
        inputNoteDescription.click();
        inputNoteDescription.clear();
        inputNoteDescription.sendKeys(description);
        noteSubmitButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(redirectLink));
        redirectLink.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(navTab));
        noteTab.click();
        return new Note(title, description, null);
    }

    @Test
    @Order(1)
    public void testAddingNote() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOf(addNoteButton));
        addNoteButton.click();

        Note note = submitNoteForm(testNoteTitle, testNoteDescription);

        webDriverWait.until(ExpectedConditions.visibilityOf(notesTable));
        Assertions.assertEquals(note.getTitle(), tableNoteTitle.getText());
        Assertions.assertEquals(note.getDescription(), tableNoteDescription.getText());
    }

    @Test
    @Order(2)
    public void testEditingNote() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOf(notesTable));
        Assertions.assertEquals(testNoteTitle, tableNoteTitle.getText());
        Assertions.assertEquals(testNoteDescription, tableNoteDescription.getText());

        webDriverWait.until(ExpectedConditions.visibilityOf(editNoteButton));
        editNoteButton.click();

        Note editedNote = submitNoteForm(testEditNoteTitle, testEditNoteDescription);

        webDriverWait.until(ExpectedConditions.visibilityOf(notesTable));
        Assertions.assertEquals(editedNote.getTitle(), tableNoteTitle.getText());
        Assertions.assertEquals(editedNote.getDescription(), tableNoteDescription.getText());
    }

    @Test
    @Order(3)
    public void testDeleteNote() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOf(notesTable));
        Assertions.assertEquals(testEditNoteTitle, tableNoteTitle.getText());
        Assertions.assertEquals(testEditNoteDescription, tableNoteDescription.getText());

        deleteNoteButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(redirectLink));
        redirectLink.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(navTab));
        noteTab.click();

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            tableNoteTitle.getText();
            tableNoteDescription.getText();
        });
    }
}
