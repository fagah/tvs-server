package ci.tact.voting.tvs.domain;

import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "political_party")
@Getter
@Setter
@Schema(description = "Political Party Entity")
public class PoliticalParty {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the party")
    private Long id;

    @NotBlank
    @Size(min = 2, max = 20)
    @Column(nullable = false, unique = true)
    @Schema(description = "Unique code of the party", example = "PARTY1")
    private String code;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    @Schema(description = "Full name of the party", example = "Democratic Party")
    private String name;

    @Column(name = "created_at")
    @Schema(description = "Timestamp when the party was created")
    private ZonedDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
    }
}