package cl.felipe.gonzalez.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TransferenciaCuentasTest {
	private WebDriver driver;
	//ChromeDriver
	@BeforeClass
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://demo.testfire.net/login.jsp");
	}
	//Login web
	@Test
	public void loginTest() {
		WebElement campoUsuario = driver.findElement(By.id("uid"));
        campoUsuario.sendKeys("jsmith");

        WebElement campoContraseña = driver.findElement(By.id("passw"));
        campoContraseña.sendKeys("Demo1234");

        WebElement botonIniciarSesion = driver.findElement(By.name("btnSubmit"));
        botonIniciarSesion.click();
	}
	//click TransferFunds
	@Test(dependsOnMethods = "loginTest")
	private void clickTransferFunds() {
	    WebElement menuTransferFunds = driver.findElement(By.id("MenuHyperLink3"));
	    menuTransferFunds.click();
	}
	//Realizar Transferencia desde "Checking" a "Credit Card"
	@Test(dependsOnMethods = "clickTransferFunds")
	private void realizarTransferenciaCuentas() {
	    WebElement cuentaOrigen = driver.findElement(By.id("fromAccount"));
	    Select selectCuentaOrigen = new Select(cuentaOrigen);
	    selectCuentaOrigen.selectByVisibleText("800003 Checking");

	    WebElement cuentaDestino = driver.findElement(By.id("toAccount"));
	    Select selectCuentaDestino = new Select(cuentaDestino);
	    selectCuentaDestino.selectByVisibleText("4539082039396288 Credit Card");
	}
	//agregar monto "100" a transferencia
	@Test(dependsOnMethods = "realizarTransferenciaCuentas")
	private void agregarMontoTransferencia() {
	    WebElement campoMonto = driver.findElement(By.id("transferAmount"));
	    campoMonto.sendKeys("100");
	}
	
	//click "transfer money"
	@Test(dependsOnMethods = "agregarMontoTransferencia")
	private void hacerClickTransferMoney() {
	    WebElement botonTransferir = driver.findElement(By.xpath("//input[@value='Transfer Money']"));
	    botonTransferir.click();
	}
	//Validacion mensaje fin transferencia con valor "100"
	@Test(dependsOnMethods = "hacerClickTransferMoney")
	public void validarMensajeTest() {
	    WebElement mensajeExito = driver.findElement(By.xpath("//span[contains(text(), 'was successfully transferred')]"));
	    String mensaje = mensajeExito.getText();

	    // assert validacion "100.0"
	    Assert.assertTrue(mensaje.contains("100.0"), "El mensaje no contiene la cantidad esperada");
	}
	//cierre sesion Logout
	@Test(dependsOnMethods = "validarMensajeTest")
	public void cerrarSesionTest() {
	    cerrarSesion();
	}
	
	private void cerrarSesion() {
	    WebElement enlaceSignOff = driver.findElement(By.id("LoginLink"));
	    enlaceSignOff.click();
	}

}
