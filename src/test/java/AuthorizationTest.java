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

public class AuthorizationTest {
    private WebDriver webDriver;


    @BeforeTest
    public void beforeTestStage() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");

        webDriver = WebDriverManager
                .chromedriver()
                .capabilities(chromeOptions)
                .create();

    }

    @Test
    public void checkCaptchaRequirementsTest() {
        webDriver.get("https://habr.com/ru/articles/");
        webDriver.manage().window().maximize();
        try {
            TimeUnit.SECONDS.sleep(3);

            // 1 действие - нажимаем по кнопке 'Войти'
            By signInButtonLocator = By.xpath("//a//button" +
                    "[@class=\"btn btn_solid btn_small tm-header-user-menu__login\"]");
            WebElement signInButtonElement = webDriver.findElement(signInButtonLocator);
            signInButtonElement.click();
            TimeUnit.SECONDS.sleep(3);

            // 2 действие - вводим нашу почту
            By inputEmailFieldLocator = By.xpath("//div//input[@type=\"email\"]");
            WebElement inputEmailFieldElement = webDriver.findElement(inputEmailFieldLocator);
            String email = "qwerty@gmail.com";
            inputEmailFieldElement.sendKeys(email);
            TimeUnit.SECONDS.sleep(2);

            // 3 действие - вводим наш пароль
            By inputFieldPasswordLocator = By.xpath("//div//input[@type=\"password\"]");
            WebElement inputPasswordFieldElement = webDriver.findElement(inputFieldPasswordLocator);
            String password = "qwerty";
            inputPasswordFieldElement.sendKeys(password);
            TimeUnit.SECONDS.sleep(2);

            // 4 действие - нажимаем по кнопке 'Войти' на странице входа
            By submitButtonLocator = By.xpath("//div//button[@form=\"ident-form\"]");
            WebElement submitButtonElement = webDriver.findElement(submitButtonLocator);
            submitButtonElement.click();
            TimeUnit.SECONDS.sleep(3);

            By warningMessageLocator = By.xpath("//div[@class=\"s-error\"]");
            WebElement warningMessageElement = webDriver.findElement(warningMessageLocator);

            // Проверка на наличие предупреждения о прохождении капчи
            String warningMessageText = warningMessageElement.getText();
            Assert.assertEquals(warningMessageText, "Необходимо пройти капчу");

        } catch (InterruptedException e) {
            Assert.fail("Тест был прерван: " + e.getMessage());
        } catch (NoSuchElementException e) {
            Assert.fail("Возможно сообщение c предупреждением еще не успело появиться: " + e.getMessage() +
                    "\n\nПопробуйте увеличить время паузы.");
        }
    }

    @AfterTest
    public void afterTestStage() {
        webDriver.quit();
    }
}
