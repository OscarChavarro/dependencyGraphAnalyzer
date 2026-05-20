package backend.application.port.in;

import backend.domain.model.GraphModelGenerator;

public interface UpdateGraphModelUseCase {
    void execute(GraphModelGenerator generator);
}
