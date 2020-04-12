package tqs.midterm;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.seljup.SeleniumExtension;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

@ExtendWith(SeleniumExtension.class)
class ChromeJupiterTest {

    // Assert that the list of countries is loaded from the API
    @Test
    void countries_areLoaded(ChromeDriver driver){
        driver.get("http://localhost:3000/");

        driver.manage().window().setSize(new Dimension(945, 1060));

        Select select = new Select(driver.findElement(By.id("countrySelector")));

        // Wait for the page to load the API information
        {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[2]/div/select[1]/option[2]")));
        }

        assertThat(select.getOptions().size(),greaterThan(1));
    }

    // Assert that the list of cities is loaded from the API
    @Test
    void cities_areLoaded(ChromeDriver driver){
        driver.get("http://localhost:3000/");

        driver.manage().window().setSize(new Dimension(945, 1060));
        {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[2]/div/select[1]/option[2]")));
        }
        // Select the first country from the drop down select
        {
            WebElement dropdown = driver.findElement(By.cssSelector(".locationSelector:nth-child(1)"));
            dropdown.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/select[1]/option[2]")).click();
        }
        {
            WebElement element = driver.findElement(By.cssSelector(".locationSelector:nth-child(1)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).click().perform();
        }
        driver.findElement(By.cssSelector(".locationSelector:nth-child(1)")).click();
        // Wait for cities to load
        {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"citySelector\"]/option[2]")));
        }

        Select select = new Select(driver.findElement(By.id("citySelector")));
        assertThat(select.getOptions().size(),greaterThan(1));
    }

    // Assert that the city selection is initially disabled and becomes enabled after selecting a country
    @Test
    void citySelector_disableTest(ChromeDriver driver) {
        driver.get("http://localhost:3000/");

        driver.manage().window().setSize(new Dimension(945, 1060));


        WebElement citySelector = driver.findElement(By.id("citySelector"));
        // Assert that citySelector is initially disabled
        Assert.assertThat(citySelector.getAttribute("disabled"),is("true"));

        // Wait for the page to load the API information
        {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[2]/div/select[1]/option[2]")));
        }
        // Select the first country from the drop down select
        {
            WebElement dropdown = driver.findElement(By.cssSelector(".locationSelector:nth-child(1)"));
            dropdown.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/select[1]/option[2]")).click();
        }
        {
            WebElement element = driver.findElement(By.cssSelector(".locationSelector:nth-child(1)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).click().perform();
        }
        driver.findElement(By.cssSelector(".locationSelector:nth-child(1)")).click();

        // Assert that the city selector becomes available (disabled attribute no longer present)
        Assertions.assertNull(citySelector.getAttribute("disabled"));

        driver.close();
    }

    // Assert that the search button is initially disabled and becomes enabled after selecting a country and city
    @Test
    void searchBtn_disabledTest(ChromeDriver driver) {
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(945, 1060));

        WebElement searchBtn = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/button"));
        // Assert that searchBtn is initially disabled
        Assert.assertThat(searchBtn.getAttribute("disabled"),is("true"));

        // Wait for the page to load the API information
        {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[2]/div/select[1]/option[2]")));
        }
        // Select the first country from the drop down select
        {
            WebElement dropdown = driver.findElement(By.cssSelector(".locationSelector:nth-child(1)"));
            dropdown.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/select[1]/option[2]")).click();
        }
        {
            WebElement element = driver.findElement(By.cssSelector(".locationSelector:nth-child(1)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).click().perform();
        }
        driver.findElement(By.cssSelector(".locationSelector:nth-child(1)")).click();
        // Wait for cities to load
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"citySelector\"]/option[2]")));
        }
        // Select city
        {
            WebElement dropdown = driver.findElement(By.id("citySelector"));
            dropdown.findElement(By.xpath("//*[@id=\"citySelector\"]/option[2]")).click();
        }
        {
            WebElement element = driver.findElement(By.id("citySelector"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).click().perform();
        }
        driver.findElement(By.id("citySelector")).click();

        // Assert that the city selector becomes available
        Assertions.assertNull(searchBtn.getAttribute("disabled"));

        driver.close();
    }

    // Assert that results are being retrieved
    @Test
    void gotResults(ChromeDriver driver){
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(945, 1060));

        // Wait for the page to load the API information
        {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[2]/div/select[1]/option[2]")));
        }
        // Select the first country from the drop down select
        {
            WebElement dropdown = driver.findElement(By.cssSelector(".locationSelector:nth-child(1)"));
            dropdown.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/select[1]/option[2]")).click();
        }
        {
            WebElement element = driver.findElement(By.cssSelector(".locationSelector:nth-child(1)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).click().perform();
        }
        driver.findElement(By.cssSelector(".locationSelector:nth-child(1)")).click();

        // Wait for cities to load
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"citySelector\"]/option[2]")));
        }
        // Select city
        {
            WebElement dropdown = driver.findElement(By.id("citySelector"));
            dropdown.findElement(By.xpath("//*[@id=\"citySelector\"]/option[2]")).click();
        }
        {
            WebElement element = driver.findElement(By.id("citySelector"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).click().perform();
        }
        driver.findElement(By.id("citySelector")).click();

        driver.findElement(By.cssSelector(".locationSearchBtn")).click();

        // Wait for results to load
        {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div/div[2]/div/div/div[1]")));
        }

        try{
            driver.findElement(By.cssSelector(".locationDiv"));
        } catch (NoSuchElementException e){
            fail();
        }


        driver.close();
    }

}
