package org.epam.apiClasses.dtoClasses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatePlaylistDTO {

    private String name;
    private String description;
    private boolean isPublic;

}

