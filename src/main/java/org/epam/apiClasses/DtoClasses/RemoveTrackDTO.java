package org.epam.apiClasses.DtoClasses;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RemoveTrackDTO {
    private List<String> uris;
}