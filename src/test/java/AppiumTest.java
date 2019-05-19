import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AppiumTest{

    static AndroidDriver driver;
    static WebDriverWait wait;

    //1. Запустить экран мобильного устройства.
    //2. Открыть приложение телефонная книга – список контактов
    @BeforeClass
    public static void setUp() throws MalformedURLException{
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "6.0");
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Android emulator");
        caps.setCapability("avd","Nexus4");
        caps.setCapability(MobileCapabilityType.APPLICATION_NAME, "Контакты");
        caps.setCapability("appPackage", "com.android.contacts");
        caps.setCapability("appActivity", ".activities.PeopleActivity");
        caps.setCapability("resetKeyboard", true);
        caps.setCapability("unicodeKeyboard", true);
        driver = new AndroidDriver (new URL("http://0.0.0.0:4723/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 5);
    }


    @Test
    public void addContact_test() {
        //для случая, когда список контактов пуст
        while (driver.findElementsById("com.google.android.gms:id/suc_layout_title").size() > 0) {
            driver.navigate().back();
        }
        //3. Добавить контакт, указав имя, фамилию, номер телефона.
        driver.findElementByAccessibilityId("Добавить контакт").click();
        driver.findElement(By.id("com.android.contacts:id/left_button")).click();
        driver.findElement(By.id("com.android.contacts:id/expansion_view")).click();
        WebElement a = driver.findElementByXPath("//*[@class = 'android.widget.EditText'][2]");
        a.click();
        a.sendKeys("Кирилл");
        WebElement b = driver.findElementByXPath("//*[@class = 'android.widget.EditText'][4]");
        b.click();
        b.sendKeys("Сидоров");
        WebElement c = driver.findElement(By.id("com.android.contacts:id/phone_numbers"));
        c.click();
        c.sendKeys("84957654321");
        driver.findElementByAccessibilityId("Сохранить").click();
        //4. Открыть список контактов, в строке поиска ввести данные добавленного контакта.
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("com.android.contacts:id/text"))));
        driver.navigate().back();
        driver.findElementByAccessibilityId("Поиск").click();
        WebElement d = driver.findElementById("com.android.contacts:id/search_view");
        d.click();
        d.sendKeys("Кирилл Сидоров");
        //5. Проверить, что контакт найден, проверить что данные совпадают.
        WebElement f = driver.findElementById("com.android.contacts:id/cliv_name_textview");
        Assert.assertNotNull(f);
        f.click();
        String g = driver.findElementById("com.android.contacts:id/header").getText();
        Assert.assertEquals("84957654321", g);
        String e = driver.findElementById("com.android.contacts:id/photo_touch_intercept_overlay").getAttribute("contentDescription");
        Assert.assertEquals("Кирилл Сидоров", e);
        //6. Удалить созданный контакт.
        driver.findElementByAccessibilityId("Ещё").click();
        driver.findElementByXPath("//*[@class='android.widget.LinearLayout'][1]").click();
        driver.findElementById("android:id/button1").click();
    }

    //закрываем приложение
    @AfterClass
    public static void tearDown(){
        driver.closeApp();
    }
}