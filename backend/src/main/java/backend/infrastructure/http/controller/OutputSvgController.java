package backend.infrastructure.http.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/output/svg")
public class OutputSvgController {

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getSvg(@PathVariable("filename") String filename) {
        if (filename == null || filename.isBlank() || filename.contains("/") || filename.contains("\\") || !filename.endsWith(".svg")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid svg filename");
        }

        Path svgPath = resolveSvgPath(filename);
        if (!Files.isRegularFile(svgPath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "svg not found");
        }

        Resource resource = new FileSystemResource(svgPath);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/svg+xml"))
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);
    }

    private Path resolveSvgPath(String filename) {
        Path[] candidates = new Path[] {
                Paths.get("output", "svg", filename),
                Paths.get("..", "output", "svg", filename)
        };
        for (Path candidate : candidates) {
            Path normalized = candidate.toAbsolutePath().normalize();
            if (Files.isRegularFile(normalized)) {
                return normalized;
            }
        }
        return candidates[0].toAbsolutePath().normalize();
    }
}
