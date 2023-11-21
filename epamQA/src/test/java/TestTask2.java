import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class TestTask2 {
    private WebDriver driver;
    private PastebinHomePage2 homePage;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://pastebin.com/");
        homePage = new PastebinHomePage2(driver);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCreateNewPaste() {
        String code = """
                git config --global user.name  "New Sheriff in Town"
                git reset $(git commit-tree HEAD^{tree} -m "Legacy code")
                git push origin master --force""";
        String syntax = "Bash";
        String expiration = "10 Minutes";
        String title = "how to gain dominance among developers";

        homePage.createPaste(code, syntax, expiration, title);

        // Verify that the paste is created successfully
        assertTrue("Title does not match.", homePage.isTitleMatches(title));
        assertTrue("Syntax is not suspended for bash.", homePage.isSyntaxSuspended(syntax));
        assertTrue("Code does not match.", homePage.isCodeMatch(code));
    }
}

class PastebinHomePage2 {

    private final WebDriver driver;

    public PastebinHomePage2(WebDriver driver) {
        this.driver = driver;
    }

    public void createPaste(String code, String syntax, String expiration, String title){

        // Apply polit. conf.
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                .elementToBeClickable(By.xpath("//*[@id='qc-cmp2-ui']/div[2]/div/button[2]")))
                .click();

        // Enter code
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                .elementToBeClickable(By.id("postform-text")))
                .sendKeys(code);

        // to scrolling down (without it exception)
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,500)", "");

        // Select syntax
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                .elementToBeClickable(By.id("select2-postform-format-container")))
                .click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                .elementToBeClickable(By.xpath("//li[text()='" + syntax + "']")))
                .click();

        // Select expiration
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                .elementToBeClickable(By.id("select2-postform-expiration-container")))
                .click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                .elementToBeClickable(By.xpath("//li[text()='" + expiration + "']")))
                .click();

        // to scrolling down (without it exception)
        js.executeScript("window.scrollBy(0,500)", "");

        // Enter title
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                .elementToBeClickable(By.id("postform-name")))
                .sendKeys(title);

        // Click on 'Create New Paste' button
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                .elementToBeClickable(By.xpath("//button[text()='Create New Paste']")))
                .click();
    }

    // Browser page title matches 'Paste Name / Title
    public boolean isTitleMatches(String title) {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions
                .visibilityOfAllElements());
        String titleOnPage = driver.getTitle();
        title = title + " - Pastebin.com";
        return title.equals(titleOnPage);
    }

    // Syntax is suspended for bash
    public boolean isSyntaxSuspended(String syntax) {
        String syntaxOnPage = driver.findElement(By.xpath("//a[text()='Bash']")).getText();
        return syntaxOnPage.equals(syntax);
    }

    // Check that the code matches the one from paragraph 2
    public boolean isCodeMatch(String code) {
        String codeOnPage = driver.findElement(By.xpath("//ol[@class='bash']")).getText();
        return codeOnPage.equals(code);
    }
}
