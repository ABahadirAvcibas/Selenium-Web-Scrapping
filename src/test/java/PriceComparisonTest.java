import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PriceComparisonTest {

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();

        try {
            List<Double> prices = new ArrayList<>();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // First website : Mediamarkt
            driver.get("https://www.mediamarkt.com.tr/tr/product/_apple-iphone-13-128-gb-akilli-telefon-starlight-mlpg3tua-1217605.html");
            WebElement priceMediaMarkt = driver.findElement(By.xpath("//span[@class='sc-8b815c14-0 iJYWto']"));
            prices.add(cleanPrice(priceMediaMarkt.getText()));
            System.out.println("MediaMarkt Price : " + prices.get(0));

            // Second website : Trendyol
            driver.get("https://www.trendyol.com/apple/iphone-13-128-gb-yildiz-isigi-cep-telefonu-apple-turkiye-garantili-p-150059024");
            WebElement priceTrendyol = driver.findElement(By.xpath("//span[@class='prc-dsc']"));
            prices.add(cleanPrice(priceTrendyol.getText()));
            System.out.println("Trendyol Price : " + prices.get(1));

            // Third website : n11
            driver.get("https://www.n11.com/urun/apple-iphone-13-128-gb-apple-turkiye-garantili-2141312");
            WebElement priceN11 = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='newPrice']/ins"))
            );
            prices.add(cleanPrice(priceN11.getText()));
            System.out.println("n11 Price : " + prices.get(2));

            // Price comparison
            double minPrice = prices.stream().min(Double::compareTo).orElse(0.0);
            double maxPrice = prices.stream().max(Double::compareTo).orElse(0.0);
            double avgPrice = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

            System.out.println("Cheapest: " + minPrice + " TL");
            System.out.println("Most Expensive: " + maxPrice + " TL");
            System.out.println("Mid: " + avgPrice + " TL");

        } catch (Exception e) {
            System.err.println("Error Occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static double cleanPrice(String priceText) {
        try {
            return Double.parseDouble(priceText
                    .replaceAll("[^0-9,]", "") // Just hold digits and commas
                    .replace(",", ".")        // Change comma with decimal point
                    .trim());
        } catch (NumberFormatException e) {
            System.err.println("Price conversion error : " + priceText);
            return 0.0; // If error occurs, take the price 0
        }
    }
}

