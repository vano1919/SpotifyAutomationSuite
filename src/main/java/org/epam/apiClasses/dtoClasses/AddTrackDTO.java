package org.epam.apiClasses.dtoClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddTrackDTO {
    private List<String> uris;
    private Integer position;

    public AddTrackDTO(List<String> uris) {
        this.uris = uris;
        this.position = null;
    }

    // If any additional methods are needed, they can be added here.
}
