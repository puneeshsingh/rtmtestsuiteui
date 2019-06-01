package pages;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

	WebDriver driver;
	WebDriverWait wait;

	By userName = By.id("username");
	By password = By.id("password");
	By loginButton = By.id("login-button");

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, 15);
	}

	public void setUserName(String strUserName) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(userName)).sendKeys(strUserName);
		} catch (NoSuchElementException e) {
			System.out.println("Could not find username.");
		}
		// driver.findElement(userName).sendKeys(strUserName);
	}

	public void setPassword(String StrPassword) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(password)).sendKeys(StrPassword);
		} catch (NoSuchElementException e) {
			System.out.println("Could not find password.");
		}
		// driver.findElement(password).sendKeys(StrPassword);
	}

	public void clickLoginButton() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
		} catch (NoSuchElementException e) {
			System.out.println("Could not find Login Button.");
		}
		// driver.findElement(loginButton).click();
	}

	public String getPageTitle() {
		return driver.getTitle();
	}

	public void loginToRememberTheMilk(String userName, String password) {
		this.setUserName(userName);
		this.setPassword(password);
		this.clickLoginButton();
	}
}
