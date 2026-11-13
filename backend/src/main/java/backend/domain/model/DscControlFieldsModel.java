package backend.domain.model;

import java.util.List;

/** Fields of a Debian .dsc file used by the build guide. */
public record DscControlFieldsModel(List<String> binaries, String buildDepends) {
    public static DscControlFieldsModel empty() {
        return new DscControlFieldsModel(List.of(), "");
    }
}
