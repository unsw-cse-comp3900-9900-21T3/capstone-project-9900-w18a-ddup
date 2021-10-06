package dev.shawnking07.ecomm_system_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class DbFile extends BaseEntity {
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("file_content")
    @JsonIgnore
    @Column(length = 20971520)
    @Type(type = "org.hibernate.type.RowVersionType")
    @ToString.Exclude
    private byte[] content;
    private String filename;
    private String filetype;

    public byte[] getContent() {
        byte[] content = new byte[this.content.length];
        System.arraycopy(this.content, 0, content, 0, this.content.length);
        return content;
    }

    public void setContent(byte[] content) {
        byte[] content1 = new byte[content.length];
        System.arraycopy(content, 0, content1, 0, content.length);
        this.content = content1;
    }
}
