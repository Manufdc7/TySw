package edu.uclm.esi.backJuegos.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.openqa.selenium.Dimension;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.MethodOrderer;

import org.openqa.selenium.JavascriptExecutor;

import java.util.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @BeforeEach
  public void setUp() {
	System.setProperty("webdriver.chrome.driver", "C:\\Users\\oscar\\Downloads\\chromedriver-win32\\chromedriver-win32\\chromedriver.exe");
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @AfterEach
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void login() {
    driver.get("http://127.0.0.1:4200/login");
    driver.manage().window().setSize(new Dimension(784, 824));
    driver.findElement(By.id("usuario")).click();
    driver.findElement(By.id("usuario")).sendKeys("oscare088");
    driver.findElement(By.id("contraseña")).click();
    driver.findElement(By.id("contraseña")).sendKeys("12345");
    driver.findElement(By.name("submit")).click();
    driver.close();
  }
}
