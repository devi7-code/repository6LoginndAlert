package Testing;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.chrome.ChromeDriver;
import util.TestUtil;
import util.Util;

public class TestCase_01  {
static WebDriver driver;
 String username,password;static String actualBoxMsg; static String baseUrl;	
	@BeforeMethod
	public void SetUp() {
		System.setProperty("webdriver.chrome.driver",Util.FIREFOX_PATH);
		 driver = new ChromeDriver();
		baseUrl = Util.BASE_URL;
		driver.manage().timeouts().pageLoadTimeout(Util.WAIT_TIME, TimeUnit.SECONDS);
	driver.manage().timeouts()
		.implicitlyWait(Util.WAIT_TIME, TimeUnit.SECONDS);
	driver.manage().deleteAllCookies();
	driver.manage().window().maximize();
	driver.get(baseUrl + "/V4/");
	
	}
	@DataProvider
	public Object[][] getLoginData() {
		Object data[][]=TestUtil.getTestData("Sheet1");
		return data;
	}
	
	@Test(dataProvider="getLoginData")
	public void LoginTest(String username,String password) throws IOException, AWTException {
	
WebElement user=driver.findElement(By.name("uid")); 
user.clear();
user.sendKeys(username);  
WebElement pass=driver.findElement(By.name("password"));
pass.clear();
pass.sendKeys(password);  
driver.findElement(By.name("btnLogin")).click();
if(isAlertPresent(driver)) {
	
	Alert alt = driver.switchTo().alert();
   	actualBoxMsg = alt.getText(); 
	alt.accept();
	System.out.println("excuting not here");				
	assertEquals("Pop up box appeared promptly for the incorrect",actualBoxMsg,Util.EXPECT_ERROR);
	
}else {
	System.out.println("excuting here");
	String title=driver.getTitle();
	System.out.println("titlee is"+title);
	assertEquals("Page opened",title,Util.EXPECT_TITLE);
	
	String pageText = driver.findElement(By.tagName("tbody")).getText();
	
	String[] parts = pageText.split(":");
	String dynamicText = parts[1];
	
	assertTrue(dynamicText.substring(1, 5).equals("mngr"));
	String remain = dynamicText.substring(dynamicText.length() - 4);
	assertTrue(remain.matches("[0-9]+"));
	takescreenshot();
}
	
	}
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
	
	public void takescreenshot() throws IOException {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileHandler.copy(scrFile, new File(Util.File));
		

	}
	public static boolean isAlertPresent(WebDriver driver) throws IOException {
		// TODO Auto-generated method stub
		try{
			  driver.switchTo().alert();
			  return true;
			 }catch(NoAlertPresentException ex){
			  return false;
			 }

}
}
