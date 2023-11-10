package org.epam.apiClasses.DtoClasses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AddTrackDTO {
    private List<String> uris;
    private Integer position;
}
