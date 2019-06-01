package pages;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
	WebDriverWait wait;
	WebDriver driver;

	By pageFooter = By.xpath("//div[@class='b-Rq-Di']");
	By arrowDropDown = By.xpath("//i[contains(text(),'arrow_drop_down')]");

	public HomePage(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, 10);
	}

	public String getPageFooter() {
		String text = null;
		try {
			text = wait.until(ExpectedConditions.visibilityOfElementLocated(pageFooter)).getText();
		} catch (NoSuchElementException e) {
			System.out.println("Could not find footer.");
		}
		return text;
	}
}
