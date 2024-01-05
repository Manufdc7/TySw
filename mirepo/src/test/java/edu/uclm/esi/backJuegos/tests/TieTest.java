package edu.uclm.esi.backJuegos.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Dimension;

import org.openqa.selenium.JavascriptExecutor;

import java.util.*;

public class TieTest {
	
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  
  @BeforeEach
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @AfterEach
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void tie() {
    driver.get("http://127.0.0.1:4200/login");
    driver.manage().window().setSize(new Dimension(1552, 840));
    driver.findElement(By.id("usuario")).click();
    driver.findElement(By.linkText("Jugar sin registrarse")).click();
    driver.findElement(By.id("juego")).click();
    {
      WebElement dropdown = driver.findElement(By.id("juego"));
      dropdown.findElement(By.xpath("//option[. = '4 en raya']")).click();
    }
    driver.findElement(By.cssSelector(".btn-lg")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(3)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(4)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(5)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(6)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(7)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(3)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(4)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(5)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(6)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(7)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(6)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(7)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(5)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(4)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(3)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(4)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(3)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(6)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(5)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(7)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(7)")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(7)"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(3)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(4)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(5)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(5)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(6)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(6)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(4)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(3)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".row:nth-child(1) > .cell:nth-child(7)")).click();
    driver.close();
  }
}
