import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest {
    private WebDriver driver;
    private String browser;

    public LoginTest(String browser) {
        this.browser = browser;
    }

    @BeforeMethod
    public void setUp() {
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        } else {
            throw new IllegalArgumentException("Invalid browser specified");
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, char expectedResult) {
        driver.get("https://www.saucedemo.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Locate elements according to website
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        // Clear and input credentials
        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);

        loginButton.click();

        switch (expectedResult) {
            case 'T': // Successful Login
                try {
                    WebElement successIndicator = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));
                    Assert.assertEquals(successIndicator.getText(), "Products", "Login was expected to be successful");
                    System.out.println("Login successful for user: " + username);
                } catch (Exception e) {
                    Assert.fail("Expected successful login, but it failed!");
                }
                break;

            case 'F': // Failed Login
                try {
                    WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
                    Assert.assertTrue(errorMessage.isDisplayed(), "Error message should be displayed for failed login");
                    System.out.println("Login failed as expected for user: " + username);
                } catch (Exception e) {
                    Assert.fail("Expected login to fail, but no error message was found!");
                }
                break;

            case 'E': // Empty Credentials
                try {
                    WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
                    Assert.assertTrue(errorMessage.isDisplayed(), "Error message should appear for empty credentials");
                    System.out.println("Validation error displayed for empty credentials");
                } catch (Exception e) {
                    Assert.fail("Expected validation error for empty credentials!");
                }
                break;

            default:
                Assert.fail("Invalid test scenario specified");
        }
    }

    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        return new Object[][]{
                {"standard_user", "secret_sauce", 'T'},  // for valid credentials
                {"invalid_user", "wrong_password", 'F'}, // for invalid credentials
                {"", "", 'E'}                            // for empty credentials
        };
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Factory
    public Object[] createTestInstances() {
        return new Object[]{
                new LoginTest("chrome"),
                new LoginTest("edge")
        };
    }
}
