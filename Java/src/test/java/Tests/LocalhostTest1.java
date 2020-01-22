package Tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;

public class LocalhostTest1
{
    public static WebDriver driver;
    public static BrowserMobProxy proxy;

    @BeforeClass
    public static void setup()
    {
        String path =  System.getProperty("user.dir") + "/webdriver/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", path);

        // старт прокси
        proxy = new BrowserMobProxyServer();
        proxy.setTrustAllServers(true);
        proxy.start();

        // получить объект Selenium
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        // настройка для драйвера
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        ChromeOptions options = new ChromeOptions();
        options.merge(capabilities);

        // создание драйвера
        driver = new ChromeDriver(options);

        // включить более детальный захват HAR
        proxy.newHar("localhost:8888");
    }

    @Test
    public void testSearch()
    {
        driver.get("http://localhost:8888/");

        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("admin");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("123");;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement button = driver.findElement(By.name("button"));
        button.click();

        // получить данные HAR
        Har har = proxy.getHar();
        System.out.println("HAR: " + har.getLog().getVersion());
        for (int i = 0; i < har.getLog().getEntries().size(); i++)
        {
            String link = har.getLog().getEntries().get(i).getRequest().getUrl();
            System.out.println("HAR LINK: " + link);
        }

        String result = driver.findElement(By.id("result")).getText();
        System.out.println("RESULT: " + result);
        Assert.assertEquals("Correct", result);
    }

    @AfterClass
    public static void tearDown()
    {
        driver.close();
        driver.quit();
    }
}
