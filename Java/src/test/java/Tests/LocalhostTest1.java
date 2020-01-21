package Tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;

public class LocalhostTest1
{
    private static WebDriver driver;
    private static BrowserMobProxy proxy;

    @BeforeClass
    public static void setup()
    {
        String path =  System.getProperty("user.dir") + "/webdriver/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", path);

        // старт прокси
        proxy = new BrowserMobProxyServer();
        proxy.start();

        // получить объект Selenium
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        // настройка для драйвера
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        // создание драйвера
        driver = new ChromeDriver(capabilities);
        driver.manage().window().maximize();

        // включить более детальный захват HAR
        proxy.newHar("localhost:8888");
    }

    @Test
    public void testSearch()
    {
        driver.get("http://localhost:8888/");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // получить данные HAR
        Har har = proxy.getHar();
        System.out.println(har);

        Assert.assertTrue(true);
    }

    @AfterClass
    public static void tearDown()
    {
        driver.close();
        driver.quit();
    }
}
