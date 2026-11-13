package backend.infrastructure.http.dto;

import java.util.List;

public record FindPackageGroupResponse(boolean found, String packageName, List<String> groups) {
}
