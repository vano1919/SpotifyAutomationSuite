package org.epam.enumeration.supportedBrowsersEnums;

import org.epam.invoker.WebDriverInvoker;
import org.epam.invoker.inplementations.ChromeInvoker;
import org.openqa.selenium.WebDriver;

public enum SupportedBrowsers {

    CHROME ( new ChromeInvoker ( ) );

    private final WebDriverInvoker webDriverInvoker;

    SupportedBrowsers ( WebDriverInvoker webDriverInvoker ) {
        this.webDriverInvoker = webDriverInvoker;
    }

    public WebDriver getWebDriver () {
        return webDriverInvoker.invokeWebDriver ( );
    }
}