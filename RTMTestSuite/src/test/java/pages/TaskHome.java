package pages;

import java.util.List;
 
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TaskHome {

	WebDriver driver;
	WebDriverWait wait;
	By createTaskBox = By.xpath("//div[@class='b-Qn']//following-sibling::div[1]");
	By inbox = By.xpath("//div[@title='Inbox']");
	By trash = By.xpath("//div[@title='Trash']");
	By dropDownOpt = By.xpath("//div[@class='Um-Lp-Mp Um-Vm-i-Bk-Sj']");

	By dropDownTrashOpt = By.xpath("//div[@class='Um-sn-Dn'][contains(text(),'Trash')]");
	By trashPageDeleteOpt = By.xpath(
			"//div[@class='Um-Lp-Mp Um-Vm-i-Ij-Sj' and @style='user-select: none;' and contains(text(),'Delete forever')]//parent::div");

	By trashPageDeleteButton = By.xpath("//div[@class='b-Gn-e-On']//button[@name='ok']");
	By taskPrecursor = By.xpath("//div[@class='b-fb-an-Gj b-fb-an-Nn']");
	By outerPageArea = By.xpath("//div[@class='b-ot-yd b-yd']");
	By taskName;
	By checkBoxOnInboxPage = By.xpath("//div[@class='b-dn-fb-Dn']//span[(@role='checkbox')]");

	public TaskHome(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, 15);
	}

	public void createNewTask(String taskName) throws InterruptedException {

		try {
			wait.until(ExpectedConditions.elementToBeClickable(inbox)).click();
		} catch (TimeoutException e) {
			System.out.println("Could not click inbox.");
		}

		try {
			List<WebElement> elements = driver.findElements(createTaskBox);

			for (int i = 0; i < elements.size(); i++) {
				if (elements.get(i).getText().equals("Add a task...")) {
					Actions action = new Actions(driver);
					action.moveToElement(elements.get(i)).click();
					Thread.sleep(2000);
					action.sendKeys(taskName + Keys.ENTER);
					action.build().perform();
				}
			}
		} catch (TimeoutException e) {
			System.out.println("Could not find create task box.");
		}

	}

	public String validateTaskAdded(String task) throws InterruptedException {
		String dXpath = "//span[contains(text(),'" + task + "')]";
		By taskName = By.xpath(dXpath);
		String text = null;

		try {
			wait.until(ExpectedConditions.elementToBeClickable(taskName)).click();
			text = driver.findElement(taskName).getText();
		} catch (TimeoutException e) {
			System.out.println("Could not find span containing Task Name.");
		}

		return text;
	}

	public void updateTask(String task, String newTaskName) {
		WebElement checkBox = null;
		String isChecked = null;
		String dXpath1, dXpath3;
		WebElement taskElement = null;

		try {
			wait.until(ExpectedConditions.elementToBeClickable(inbox)).click();
		} catch (TimeoutException e) {
			System.out.println("Unable to click Inbox.");
		}

		dXpath1 = "//span[contains(text(),'" + task + "')]";
		try {
			taskElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dXpath1)));
			taskElement.click();
		} catch (TimeoutException e) {
			System.out.println("Could not find span containing task name.");
		}

		try {
			checkBox = wait.until(ExpectedConditions.elementToBeClickable(checkBoxOnInboxPage));
			isChecked = checkBox.getAttribute("aria-checked");
		} catch (TimeoutException e) {
			System.out.println("Could not find check box containing task name.");
		}

		if (isChecked == "false") {
			Actions action1 = new Actions(driver);
			action1.moveToElement(taskElement).click().build().perform();
		}

		dXpath3 = "//div[@class='b-f-sm-kr']//div[@class='b-f-sm']";

		try {
			List<WebElement> elementListForDiv = wait
					.until(ExpectedConditions.numberOfElementsToBe(By.xpath(dXpath3), 16));

			WebElement element3 = wait.until(ExpectedConditions.elementToBeClickable(elementListForDiv.get(0)));

			Point element3LocPoint = element3.getLocation();

			int newXCoord = element3LocPoint.x + task.length() + 1;
			int yCoord = element3LocPoint.y;

			Actions action2 = new Actions(driver);
			action2.moveToElement(element3).click().sendKeys(Keys.ARROW_LEFT).sendKeys(Keys.ARROW_LEFT)
					.sendKeys(newTaskName + " " + Keys.ENTER).build().perform();
			action2.moveByOffset(newXCoord, yCoord).click().perform();

		} catch (TimeoutException e) {
			System.out.println("Could not find element list from division");
		}

	}

	public void deleteTask(String taskName, String newTaskName) {
		WebElement taskElement = null, elementCheckBox = null;
		String checkBoxSelected = null;
		List<WebElement> dropElements = null;
		String newTaskToDelete = newTaskName + " " + taskName;
		String dXpath = "//div[@class=\"b-fb-an-Gj b-fb-an-Nn\"]//span[contains(text(),'" + newTaskToDelete + "')]";
		String dXpath2 = "//div[@class='b-fb-an-Gj b-fb-an-oo-ro']//span[@role='checkbox']";

		try {
			wait.until(ExpectedConditions.elementToBeClickable(inbox)).click();
		} catch (TimeoutException e) {
			System.out.println("Could not click Inbox.");
		}

		try {
			taskElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dXpath)));
			taskElement.click();
		} catch (TimeoutException e) {
			System.out.println("Could not click span containing task name.");
		}
		try {
			elementCheckBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dXpath2)));
			checkBoxSelected = elementCheckBox.getAttribute("aria-checked");
		} catch (TimeoutException e) {
			System.out.println("Could not click check box containing task name.");
		}

		if (checkBoxSelected == "false") {
			try {
				taskElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dXpath)));
			} catch (TimeoutException e) {
				System.out.println("Could not click span containing task name.");
			}

			try {
				dropElements = driver.findElements(By.xpath(dXpath2));
				Actions action = new Actions(driver);
				action.moveToElement(dropElements.get(0)).click().build().perform();
			} catch (TimeoutException e) {
				System.out.println("Could not click check box containing task name.");
			}
		}

		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(dropDownOpt));
			dropElements = driver.findElements(dropDownOpt);

			Actions action = new Actions(driver);
			action.moveToElement(dropElements.get(8)).click().build().perform();

			action.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(dropDownTrashOpt))).click().build()
					.perform();
		} catch (TimeoutException e) {
			System.out.println("Could not click drop down option.");
		}

	}

	public String verifyDeletedTask(String tname) throws InterruptedException {
		String text = null;

		try {
			wait.until(ExpectedConditions.elementToBeClickable(trash)).click();
			text = validateTaskAdded(tname);
		} catch (TimeoutException e) {
			System.out.println("Could not click Inbox.");
		}

		return text;
	}

	public void deleteTaskPermanently() throws InterruptedException {
		List<WebElement> elements = null;
		WebElement deleteButton = null;

		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(trashPageDeleteOpt));
			elements = driver.findElements(trashPageDeleteOpt);
			Actions action = new Actions(driver);
			action.moveToElement(elements.get(0)).click();
			action.build().perform();
		} catch (TimeoutException e) {
			System.out.println("Could not click Trash Page Delete Option");
		}
		try {
			deleteButton = wait.until(ExpectedConditions.elementToBeClickable(trashPageDeleteButton));
			deleteButton.click();
		} catch (TimeoutException e) {
			System.out.println("Cpould noy click Trash Page Delete Button");
		}

	}

}
