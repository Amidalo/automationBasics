import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class PublicationTest {
    private WebDriver webDriver;

    @BeforeTest
    public void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote=allow-origins=*");

        webDriver = WebDriverManager
                .chromedriver()
                .capabilities(chromeOptions)
                .create();
    }

    @Test(description = "Проверка наличия кода ошибки при желании неавторизованного пользователя написать публикацию")
    public void tryToWritePublication() {
        webDriver.get("https://habr.com/ru/articles/");
        webDriver.manage().window().maximize();

        try {
            TimeUnit.SECONDS.sleep(3);

            // 1 действие - нажимаем на ссылку 'Как стать автором'
            By howToBecomeAnAuthorLinkLocator = By.xpath("//a[@class=\"tm-header__become-author-btn\"]");
            WebElement howTOBecomeAnAuthorLInkElement = webDriver.findElement(howToBecomeAnAuthorLinkLocator);
            howTOBecomeAnAuthorLInkElement.click();
            TimeUnit.SECONDS.sleep(2);

            // 2 действие - нажимаем на кнопку 'Написать публикацию'
            By writePublicationButtonLocator = By.xpath("//*[@id=\"app\"]/div/div[2]/main/div/div/div/" +
                    "div[1]/div/div[3]/div/div/div/a[1]");
            WebElement writePublicationButtonElement = webDriver.findElement(writePublicationButtonLocator);
            writePublicationButtonElement.click();
            TimeUnit.SECONDS.sleep(5);

            // Проверка наличия кода ошибки о том, что для просмотра страницы необходимо авторизоваться
            By errorCodeLocator = By.xpath("//div[@class=\"tm-error-message__code\"]");
            WebElement errorMessageElement = webDriver.findElement(errorCodeLocator);
            String errorCodeText = errorMessageElement.getText();
            Assert.assertEquals(errorCodeText, "401");

        } catch (InterruptedException e) {
            Assert.fail("Тест был прерван: " + e.getMessage());
        } catch (NoSuchElementException e) {
            Assert.fail("Возможно сообщение c ошибкой еще не успело появиться: " + e.getMessage() +
                    "\n\nПопробуйте увеличить время паузы.");
        }
    }

    @AfterTest
    public void terminate() {
        webDriver.close();
    }



}
