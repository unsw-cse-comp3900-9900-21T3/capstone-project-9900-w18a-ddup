package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.entity.DbFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

public interface DbFileService {
    /**
     * generate a random link for download
     *
     * @param id file id
     * @return link
     */
    String generateDownloadLink(Long id);

    String generateDownloadLink(@NotNull DbFile dbFile);

    /**
     * use link to download mapped file
     *
     * @param link download link
     * @return source
     */
    ResponseEntity<InputStreamResource> downloadFile(String link);
}
