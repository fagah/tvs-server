package ci.tact.voting.tvs.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    // User Management
    USER_CREATE("user:create", "Create new users", "USER"),
    USER_READ("user:read", "View user details", "USER"),
    USER_UPDATE("user:update", "Update user information", "USER"),
    USER_DELETE("user:delete", "Delete users", "USER"),

    // Role Management
    ROLE_CREATE("role:create", "Create new roles", "ROLE"),
    ROLE_READ("role:read", "View role details", "ROLE"),
    ROLE_UPDATE("role:update", "Update role information", "ROLE"),
    ROLE_DELETE("role:delete", "Delete roles", "ROLE"),

    // Party Management
    PARTY_CREATE("party:create", "Create political parties", "PARTY"),
    PARTY_READ("party:read", "View party details", "PARTY"),
    PARTY_UPDATE("party:update", "Update party information", "PARTY"),
    PARTY_DELETE("party:delete", "Delete parties", "PARTY"),

    // Station Management
    STATION_CREATE("station:create", "Create polling stations", "STATION"),
    STATION_READ("station:read", "View station details", "STATION"),
    STATION_UPDATE("station:update", "Update station information", "STATION"),
    STATION_DELETE("station:delete", "Delete stations", "STATION"),

    // Polling Station Management
    POLLING_STATION_CREATE("polling-station:create", "Create polling stations", "POLLING STATION"),
    POLLING_STATION_READ("polling-station:read", "View polling station details", "POLLING STATION"),
    POLLING_STATION_UPDATE("polling-station:update", "Update polling station information", "POLLING STATION"),
    POLLING_STATION_DELETE("polling-station:delete", "Delete polling stations", "POLLING STATION"),

    // Vote Management
    VOTE_CREATE("vote:create", "Submit vote results", "VOTE"),
    VOTE_READ("vote:read", "View vote results", "VOTE"),
    VOTE_UPDATE("vote:update", "Update vote results", "VOTE"),
    VOTE_VERIFY("vote:verify", "Verify vote results", "VOTE"),
    VOTE_DELETE("vote:delete", "Delete vote results", "VOTE"),

    // Report Management
    REPORT_CREATE("report:create", "Create reports", "REPORT"),
    REPORT_READ("report:read", "View reports", "REPORT"),
    REPORT_UPDATE("report:update", "Update reports", "REPORT"),
    REPORT_DELETE("report:delete", "Delete reports", "REPORT");

    private final String permission;
    private final String description;
    private final String category;
}