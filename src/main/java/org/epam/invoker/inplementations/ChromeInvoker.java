package org.epam.invoker.inplementations;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.epam.invoker.WebDriverInvoker;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeInvoker implements WebDriverInvoker {

    @Override
    public WebDriver invokeWebDriver () {
        WebDriverManager.chromedriver ( ).setup ( );
        return new ChromeDriver ( );
    }


}
