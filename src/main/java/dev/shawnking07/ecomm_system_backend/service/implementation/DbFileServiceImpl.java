package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.api.error.ResourceNotFoundException;
import dev.shawnking07.ecomm_system_backend.config.ApplicationProperties;
import dev.shawnking07.ecomm_system_backend.entity.DbFile;
import dev.shawnking07.ecomm_system_backend.repository.DbFileRepository;
import dev.shawnking07.ecomm_system_backend.service.DbFileService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.util.Optional;

@Service
public class DbFileServiceImpl implements DbFileService {
    private final static String BASE_LINK_KEY = "file.link.";
    private final static String BASE_ID_KEY = "file.id.";
    private final StringRedisTemplate redisTemplate;
    private final DbFileRepository dbFileRepository;
    private final ApplicationProperties properties;

    public DbFileServiceImpl(DbFileRepository dbFileRepository, StringRedisTemplate redisTemplate, ApplicationProperties properties) {
        this.dbFileRepository = dbFileRepository;
        this.redisTemplate = redisTemplate;
        this.properties = properties;
    }

    @Override
    public String generateDownloadLink(Long id) {
        String link = redisTemplate.opsForValue().get(BASE_ID_KEY + id);
        if (StringUtils.isNotBlank(link)) return "/files/" + link;
        String s = RandomStringUtils.randomAlphanumeric(10);
        redisTemplate.opsForValue().set(BASE_LINK_KEY + s, String.valueOf(id), properties.getDownloadLinkExpire());
        redisTemplate.opsForValue().set(BASE_ID_KEY + id, s, properties.getDownloadLinkExpire());
        return "/files/" + s;
    }

    @Override
    public String generateDownloadLink(@NotNull DbFile dbFile) {
        return generateDownloadLink(dbFile.getId());
    }

    @Transactional
    @Override
    public ResponseEntity<InputStreamResource> downloadFile(String link) {
        String id = redisTemplate.opsForValue().get(BASE_LINK_KEY + link);
        if (id == null) {
            redisTemplate.delete(BASE_LINK_KEY + link);
            throw new ResourceNotFoundException("wrong link");
        }
        Optional<DbFile> byId = dbFileRepository.findById(Long.valueOf(id));
        if (byId.isEmpty()) {
            redisTemplate.delete(BASE_LINK_KEY + link);
            redisTemplate.delete(BASE_ID_KEY + id);
            throw new ResourceNotFoundException("wrong link");
        }
        DbFile dbFile = byId.get();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, dbFile.getFiletype());
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + dbFile.getFilename());

        return new ResponseEntity<>(
                new InputStreamResource(new ByteArrayInputStream(dbFile.getContent())),
                httpHeaders,
                HttpStatus.OK);
    }
}
