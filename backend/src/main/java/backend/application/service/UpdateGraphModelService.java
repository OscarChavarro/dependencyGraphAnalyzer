package backend.application.service;

import backend.application.port.in.UpdateGraphModelUseCase;
import backend.domain.model.GraphModelGenerator;
import org.springframework.stereotype.Service;

@Service
public class UpdateGraphModelService implements UpdateGraphModelUseCase {
    @Override
    public void execute(GraphModelGenerator generator) {
        // Placeholder for orchestration with core internal API.
    }
}
