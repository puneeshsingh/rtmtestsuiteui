package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoggedOutPage {

	WebDriver driver;
	
	By logOutMsg = By.xpath("//h1[contains(text(),'See you next time!')]");

	public LoggedOutPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getPageTitle() {
		return driver.findElement(logOutMsg).getText();
	}
	
}
