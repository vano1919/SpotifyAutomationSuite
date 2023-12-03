package org.epam.apiClasses.dtoClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetrievePlaylistDTO {
    private String id;
    private String name;
    private String description;
    private boolean isPublic;
}


