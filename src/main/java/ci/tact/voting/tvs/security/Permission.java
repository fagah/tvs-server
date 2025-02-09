package ci.tact.voting.tvs.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    // User Management
    USER_READ("user:read"),
    USER_CREATE("user:create"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),

    // Political Party Management
    PARTY_READ("party:read"),
    PARTY_CREATE("party:create"),
    PARTY_UPDATE("party:update"),
    PARTY_DELETE("party:delete"),

    // Vote Management
    VOTE_READ("vote:read"),
    VOTE_CREATE("vote:create"),
    VOTE_UPDATE("vote:update"),
    VOTE_DELETE("vote:delete"),

    // Report Management
    REPORT_READ("report:read"),
    REPORT_CREATE("report:create"),
    REPORT_EXPORT("report:export");

    private final String permission;
}