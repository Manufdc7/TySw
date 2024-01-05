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

public class AhorcadoFallarPalabraTest {
	
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
  public void ahorcadoFallarPalabra() {
    driver.get("http://127.0.0.1:4200/login");
    driver.manage().window().setSize(new Dimension(784, 824));
    driver.findElement(By.id("usuario")).click();
    driver.findElement(By.id("usuario")).sendKeys("oscare088");
    driver.findElement(By.id("contraseña")).click();
    driver.findElement(By.id("contraseña")).sendKeys("12345");
    driver.findElement(By.name("submit")).click();
    driver.findElement(By.cssSelector(".btn-lg")).click();
    driver.findElement(By.id("#introduceLetra")).click();
    driver.findElement(By.id("#introduceLetra")).sendKeys("a");
    driver.findElement(By.cssSelector(".btn:nth-child(2)")).click();
    driver.findElement(By.id("#introduceLetra")).click();
    driver.findElement(By.id("#introduceLetra")).sendKeys("e");
    driver.findElement(By.cssSelector(".btn:nth-child(2)")).click();
    driver.findElement(By.id("#introduceLetra")).click();
    driver.findElement(By.id("#introduceLetra")).sendKeys("o");
    driver.findElement(By.cssSelector(".btn:nth-child(2)")).click();
    driver.findElement(By.id("#introducePalabra")).click();
    driver.findElement(By.id("#introducePalabra")).sendKeys("amigo");
    driver.findElement(By.cssSelector(".btn:nth-child(4)")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".btn:nth-child(4)"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
    driver.close();
  }
}
