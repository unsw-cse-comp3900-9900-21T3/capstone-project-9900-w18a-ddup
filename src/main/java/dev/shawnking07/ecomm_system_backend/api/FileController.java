package dev.shawnking07.ecomm_system_backend.api;

import dev.shawnking07.ecomm_system_backend.service.DbFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FileController {
    private final DbFileService dbFileService;

    public FileController(DbFileService dbFileService) {
        this.dbFileService = dbFileService;
    }

    @Operation(summary = "User download files")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{link}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String link) {
        return dbFileService.downloadFile(link);
    }
}
