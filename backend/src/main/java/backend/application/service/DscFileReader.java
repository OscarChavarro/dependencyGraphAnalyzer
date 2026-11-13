package backend.application.service;

import backend.domain.model.DscControlFieldsModel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Reads the Binary and Build-Depends* fields of a (possibly PGP-signed) .dsc file. */
@Component
public class DscFileReader {
    private static final Logger LOG = LoggerFactory.getLogger(DscFileReader.class);
    private static final List<String> BUILD_DEPENDS_FIELDS =
            List.of("Build-Depends", "Build-Depends-Arch", "Build-Depends-Indep");

    public DscControlFieldsModel read(Path dscFile) {
        if (!Files.isRegularFile(dscFile)) {
            return DscControlFieldsModel.empty();
        }
        try {
            return parse(Files.readAllLines(dscFile, StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOG.warn("Cannot read {}", dscFile, e);
            return DscControlFieldsModel.empty();
        }
    }

    static DscControlFieldsModel parse(List<String> lines) {
        Map<String, StringBuilder> fields = new LinkedHashMap<>();
        StringBuilder current = null;
        boolean inStanza = false;
        for (String line : lines) {
            if (line.startsWith("-----BEGIN PGP SIGNATURE")) {
                break;
            }
            if (line.startsWith("-----BEGIN PGP SIGNED") || line.startsWith("Hash:")) {
                continue;
            }
            if (line.isBlank()) {
                if (inStanza) {
                    break;
                }
                continue;
            }
            if (Character.isWhitespace(line.charAt(0))) {
                if (current != null) {
                    current.append(' ').append(line.trim());
                }
                continue;
            }
            int colon = line.indexOf(':');
            if (colon <= 0) {
                continue;
            }
            inStanza = true;
            current = new StringBuilder(line.substring(colon + 1).trim());
            fields.put(line.substring(0, colon).trim(), current);
        }

        List<String> binaries = fields.containsKey("Binary")
                ? Arrays.stream(fields.get("Binary").toString().split(","))
                        .map(String::trim)
                        .filter(name -> !name.isEmpty())
                        .toList()
                : List.of();
        List<String> buildDepends = new ArrayList<>();
        for (String field : BUILD_DEPENDS_FIELDS) {
            StringBuilder value = fields.get(field);
            if (value != null && !value.toString().isBlank()) {
                buildDepends.add(value.toString().trim());
            }
        }
        return new DscControlFieldsModel(binaries, String.join(", ", buildDepends));
    }
}
