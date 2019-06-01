//Submitted by Puneesh Singh
package com.pingidentity;

import org.testng.annotations.AfterMethod;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import jxl.Sheet;
import jxl.Workbook;

import org.testng.annotations.BeforeMethod;

import org.testng.annotations.DataProvider;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.HomePage;
import pages.LoggedOutPage;
import pages.LoginPage;
import pages.TaskHome;

public class TestRMLoginUsingTestNG {

	static String baseURL = "https://www.rememberthemilk.com/app/";
	static WebDriver driver;
	LoginPage loginObj;
	HomePage homePage;
	LoggedOutPage logOutPage;
	TaskHome taskObj;
	Workbook wb;
	Sheet sh1;
	int numOfRows;

	// @BeforeClass
	@BeforeMethod
	public void beforeClass() {
		System.setProperty("webdriver.chrome.driver",
				"/Users/heavenonearth/Documents/Programming/code/Selenium/chromedriver");

		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(baseURL);
		driver.manage().window().maximize();
 	}

//Tests that the login page is loaded
	@Test(priority = 0)
	public void testLoginPageLoaded() {
		loginObj = new LoginPage(driver);
		Assert.assertTrue(loginObj.getPageTitle().equals("Remember The Milk - Login"));
	}

	// Test with login credentials
	@Test(priority = 1, dataProvider = "loginCredentials")
	public void testLoginWithCredentials(String username, String password) throws InterruptedException {
		Boolean assertFlag = false;
		;
		SoftAssert softAssert = new SoftAssert();
		loginMethod(username, password);

		try {
			assertFlag = homePage.getPageFooter().contains("Remember The Milk");
		} catch (NoSuchElementException noElement) {
			System.out.println("Home page not reached. This could be due to incorrect password.");
		} finally {
			softAssert.assertTrue(assertFlag);
		}
	}

	// Test to create a New Task
	@Test(priority = 2, dataProvider = "loginCredentials")
	public void testCreateNewTask(String username, String password, String taskName, String newTaskName)
			throws InterruptedException {
		loginMethod(username, password);
		createTaskMethod(taskName);
		Assert.assertTrue(taskObj.validateTaskAdded(taskName).equals(taskName));
	}

	// Test to update the Task Name
	@Test(priority = 3, dataProvider = "loginCredentials")
	public void testUpdateTaskName(String username, String password, String taskName, String newTaskName)
			throws InterruptedException {
		loginMethod(username, password);
		Thread.sleep(2000);
		updateTaskMethod(taskName, newTaskName);
	}

	// Test to delete a task to trash
	@Test(priority = 4, dataProvider = "loginCredentials")
	public void testDeleteTaskToTrash(String username, String password, String taskName, String newTaskName)
			throws InterruptedException {
		loginMethod(username, password);
		Thread.sleep(2000);
		String updatedTaskName = newTaskName + " " + taskName;
		deleteTaskMethod(taskName, newTaskName);
		Assert.assertTrue(taskObj.verifyDeletedTask(updatedTaskName).equals(updatedTaskName));
		taskObj.deleteTaskPermanently();
	}

	// This function will read the data from logindata.xls file and pass the data to
	// the caller method.
	@DataProvider(name = "loginCredentials")
	public Object[][] TestDataFeed(Method m) {
		try {
			wb = Workbook.getWorkbook(new File("logindata.xls"));
			sh1 = wb.getSheet(0);
			numOfRows = sh1.getRows();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Object[][] logindata = new Object[numOfRows][sh1.getColumns()];

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < sh1.getColumns(); j++) {
				logindata[i][j] = sh1.getCell(j, i).getContents();
			}

		}

		if (m.getName().equals("testLoginWithCredentials")) {
			return new Object[][] { { logindata[0][0], logindata[0][1] } };

		} else
			return new Object[][] { { logindata[0][0], logindata[0][1], logindata[0][2], logindata[0][3] },
					{ logindata[1][0], logindata[1][1], logindata[1][2], logindata[1][3] },
					{ logindata[2][0], logindata[2][1], logindata[2][2], logindata[2][3] } };
	}

	public void loginMethod(String username, String password) throws InterruptedException {
		loginObj = new LoginPage(driver);
		loginObj.loginToRememberTheMilk(username, password);
		Thread.sleep(2000);
		homePage = new HomePage(driver);
	}

	public void createTaskMethod(String taskName) throws InterruptedException {
		taskObj = new TaskHome(driver);
		taskObj.createNewTask(taskName);
	}

	public void updateTaskMethod(String taskName, String newTaskName) throws InterruptedException {
		taskObj = new TaskHome(driver);
		taskObj.updateTask(taskName, newTaskName);
	}

	public void deleteTaskMethod(String taskName, String newTaskName) throws InterruptedException {
		taskObj = new TaskHome(driver);
		taskObj.deleteTask(taskName, newTaskName);
	}

	@AfterMethod
	public void afterClass() throws InterruptedException {
		Thread.sleep(2000);
		driver.quit();
	}
}
