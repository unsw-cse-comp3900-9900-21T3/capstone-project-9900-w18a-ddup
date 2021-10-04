package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.api.error.ResourceNotFoundException;
import dev.shawnking07.ecomm_system_backend.entity.DbFile;
import dev.shawnking07.ecomm_system_backend.repository.DbFileRepository;
import dev.shawnking07.ecomm_system_backend.service.DbFileService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.util.Optional;

@Service
public class DbFileServiceImpl implements DbFileService {
    private final static String baseKey = "file.link.";
    private final RedisTemplate<String, Object> redisTemplate;
    private final DbFileRepository dbFileRepository;

    public DbFileServiceImpl(DbFileRepository dbFileRepository, RedisTemplate<String, Object> redisTemplate) {
        this.dbFileRepository = dbFileRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String generateDownloadLink(Long id) {
        String s = RandomStringUtils.randomAlphanumeric(10);
        redisTemplate.opsForValue().set(baseKey + s, id);
        return s;
    }

    @Override
    public String generateDownloadLink(@NotNull DbFile dbFile) {
        return generateDownloadLink(dbFile.getId());
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadFile(String link) {
        String id = (String) redisTemplate.opsForValue().get(baseKey + link);
        if (id == null) throw new ResourceNotFoundException("wrong link");
        Optional<DbFile> byId = dbFileRepository.findById(Long.valueOf(id));
        if (byId.isEmpty()) throw new ResourceNotFoundException("wrong link");
        DbFile dbFile = byId.get();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, dbFile.getFiletype());
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + dbFile.getFilename());

        redisTemplate.delete(baseKey + link); // remove redis key

        return new ResponseEntity<>(
                new InputStreamResource(new ByteArrayInputStream(dbFile.getContent())),
                httpHeaders,
                HttpStatus.OK);
    }
}
