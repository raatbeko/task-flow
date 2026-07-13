package kg.core.project.endpoint.impl;

import kg.core.project.endpoint.ProjectEndpoint;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectEndpointImpl implements ProjectEndpoint {

}
