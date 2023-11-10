package org.epam.apiClasses.DtoClasses;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePlaylistDTO {

    private String name;
    private String description;
    private boolean isPublic;

}

