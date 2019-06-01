/*
 * Sample test class
 */
package com.pingidentity;

import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import pages.HomePage;
import pages.LoggedOutPage;
import pages.LoginPage;
import pages.TaskHome;

public class TestRTMLogin {

	static String baseURL="https://www.rememberthemilk.com/app/";
	static WebDriver driver;
	LoginPage loginObj;
	HomePage homePage;
	LoggedOutPage logOutPage;
	TaskHome taskObj;
	
	Workbook wb;
	Sheet sh1;
	int numOfRows;
	
	String taskName = "This IS MY FIRST TASK";
	String newtaskName = "This IS MY FIRST TASK Renamed";

	@BeforeClass
	public static void testPreSettings() {
		System.setProperty("webdriver.chrome.driver",
				"/Users/heavenonearth/Documents/Programming/code/Selenium/chromedriver");

		driver = new ChromeDriver();
		driver.get(baseURL);
		driver.manage().window().maximize();
	}

	// Tests that the login page is loaded
	@Test
	public void testAloginPageLoaded() {
		loginObj = new LoginPage(driver);
		assertTrue(loginObj.getPageTitle().equals("Remember The Milk - Login"));
	}

	// Test with login credentials
	@Test
	public void testBloginPageCredentials() throws InterruptedException {

		loginObj = new LoginPage(driver);
		loginObj.loginToRememberTheMilk("puneesh.singh", "test1234");
		Thread.sleep(2000);
		homePage = new HomePage(driver);
		assertTrue(homePage.getPageFooter().contains("Remember The Milk"));
	}

	// Test to create a New Task
	@Test
	public void testCcreateNewTask() throws InterruptedException {

		taskObj = new TaskHome(driver);
		taskObj.createNewTask(taskName);
		assertTrue(taskObj.validateTaskAdded(taskName).equals(taskName));

	}

	// Test to update the Task Name
	@Test
	public void testDupdateNewTaskName() throws InterruptedException {
		taskObj = new TaskHome(driver);
		taskObj.updateTask(taskName, newtaskName);
		// assertTrue(taskObj.validateTaskAdded(newtaskName).equals(newtaskName));
	}

	// Test to delete a task to trash
	@Test
	public void testEdeleteTaskToTrash() throws InterruptedException {
		taskObj = new TaskHome(driver);
		//taskObj.deleteTask(newtaskName);
		assertTrue(taskObj.verifyDeletedTask(newtaskName).equals(newtaskName));
	}

	@DataProvider(name="loginCredentials")
	public Object[][] TestDataFeed(){
		try {
			wb = Workbook.getWorkbook(new File("/RTMTestSuite/TestData/logindata.xlsx"));
			sh1 = wb.getSheet(0);
			numOfRows = sh1.getRows();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Object[][] logindata = new Object[numOfRows][sh1.getColumns()];
		
		for(int i=0;i<numOfRows;i++) {
			logindata[i][0] = sh1.getCell(0, i).getContents();
			logindata[i][1] = sh1.getCell(1, i).getContents();
		}
		return logindata;
	}
	
	@AfterClass
	public static void tearDownSettings() throws InterruptedException {
		Thread.sleep(2000);
		driver.quit();
	}
}
