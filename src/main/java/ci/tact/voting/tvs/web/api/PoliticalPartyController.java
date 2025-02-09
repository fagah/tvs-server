package ci.tact.voting.tvs.web.api;

import ci.tact.voting.tvs.domain.PoliticalParty;
import ci.tact.voting.tvs.service.PoliticalPartyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/political-parties")
@RequiredArgsConstructor
@Tag(name = "Political Party", description = "Political Party management APIs")
public class PoliticalPartyController {

    private final PoliticalPartyService service;

    @Operation(
        summary = "Retrieve all political parties",
        description = "Get a list of all registered political parties. The response is a list of party objects."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved the list of parties",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PoliticalParty.class))
    )
    @GetMapping
    public ResponseEntity<List<PoliticalParty>> getAllParties() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Retrieve a political party by ID",
        description = "Get a political party by its ID. The response is a party object."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved the party",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PoliticalParty.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Party not found",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PoliticalParty> getPartyById(
            @Parameter(description = "ID of the party to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
        summary = "Create a new political party",
        description = "Create a new political party. The response is the created party object."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Successfully created the party",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PoliticalParty.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<PoliticalParty> createParty(
            @Parameter(description = "Party object to create") @Valid @RequestBody PoliticalParty party) {
        return new ResponseEntity<>(service.create(party), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Update a political party",
        description = "Update an existing political party by its ID. The response is the updated party object."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully updated the party",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PoliticalParty.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Party not found",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<PoliticalParty> updateParty(
            @Parameter(description = "ID of the party to update") @PathVariable Long id,
            @Parameter(description = "Updated party object") @Valid @RequestBody PoliticalParty party) {
        return ResponseEntity.ok(service.update(id, party));
    }

    @Operation(
        summary = "Delete a political party",
        description = "Delete a political party by its ID."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Successfully deleted the party"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Party not found",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParty(
            @Parameter(description = "ID of the party to delete") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}