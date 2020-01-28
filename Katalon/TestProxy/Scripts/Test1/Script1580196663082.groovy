import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;

import org.openqa.selenium.*;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

BrowserMobProxy proxy = new BrowserMobProxyServer();
proxy.setTrustAllServers(true);
proxy.start(9091);

Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

DesiredCapabilities capabilities = new DesiredCapabilities();
capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

ChromeOptions options = new ChromeOptions();
options.addArguments("--ignore-certificate-errors", "--user-data-dir=somedirectory");

capabilities.setCapability(ChromeOptions.CAPABILITY, options);

String pathWebDriver =  System.getProperty("user.dir") + "/WebDriver/chromedriver.exe";
System.setProperty("webdriver.chrome.driver", pathWebDriver);

WebDriver driver = new ChromeDriver(capabilities);

proxy.newHar("www.google.com");

driver.get("https://www.google.com/");

WebElement search = driver.findElement(By.name("q"));
search.sendKeys("GeForce 1650");
search.sendKeys(Keys.ENTER);

try {
	Thread.sleep(5000);
} catch (InterruptedException e) {
	e.printStackTrace();
}


Har har = proxy.getHar();
System.out.println("HAR: " + har.getLog().getVersion());

for (int i = 0; i < har.getLog().getEntries().size(); i++)
{
	String link = har.getLog().getEntries().get(i).getRequest().getUrl();
	System.out.println("HAR LINK: " + link);
}

proxy.stop();
driver.close();
driver.quit();
