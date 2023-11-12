package org.epam.enumeration.pageLinksEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LinksEnum {
    LOGIN_PAGE_URL("https://accounts.spotify.com/en/login"),
    MAIN_PAGE_URL("https://open.spotify.com/"),
    API_BASE_URI("https://api.spotify.com/v1"),
    AUTH_URL( "https://accounts.spotify.com/authorize"),
    TOKEN_URL ("https://accounts.spotify.com/api/token"),
    CALLBACK_URL("http://localhost:8888/");

    private final String url;
}