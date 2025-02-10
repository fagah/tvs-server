package ci.tact.voting.tvs.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
public class PermissionEntity {
    
    @Id
    @Column(name = "permission_name")
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String category;

    @CreationTimestamp
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public PermissionEntity(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }
}