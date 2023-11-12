package org.epam.properties.convertors;

import org.epam.enumeration.supportedBrowsersEnums.SupportedBrowsers;

public class SupportedBrowserConverter {

    public static SupportedBrowsers valueOfWebBrowser(String webBrowserName) {
        if ("chrome".equalsIgnoreCase ( webBrowserName )) {
            return SupportedBrowsers.CHROME;
        } else {
            throw new IllegalArgumentException (
                    "This type of the browser is not supported");
        }
    }
}