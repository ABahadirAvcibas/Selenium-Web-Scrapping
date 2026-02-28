import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class FirstSeleniumTest {

    @Test
    public void openGoogle() {
       // in order to set Chrome Driver
        WebDriver driver = new ChromeDriver();

       // open Google
        driver.get("https://www.google.com");
        System.out.println("Sayfa Başlığı: " + driver.getTitle());
        driver.quit();
    }
}