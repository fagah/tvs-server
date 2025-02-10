-- src/main/resources/db/migration/V2__create_role_tables.sql

-- Administrative Regions
CREATE TABLE region (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    level INTEGER NOT NULL, -- 1: Country, 2: Province, 3: District, etc.
    parent_id BIGINT REFERENCES region(id)
);

-- Polling Stations
CREATE TABLE polling_station (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    region_id BIGINT REFERENCES region(id),
    address TEXT,
    registered_voters INTEGER NOT NULL DEFAULT 0,
    coordinates POINT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Polling Station Assignment
CREATE TABLE polling_station_assignment (
    id BIGSERIAL PRIMARY KEY,
    polling_station_id BIGINT REFERENCES polling_station(id),
    user_id BIGINT REFERENCES users(id),
    assigned_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_station_user UNIQUE(polling_station_id, user_id)
);

-- Vote Results per Party per Polling Station
CREATE TABLE vote_result (
    id BIGSERIAL PRIMARY KEY,
    polling_station_id BIGINT REFERENCES polling_station(id),
    party_id BIGINT REFERENCES political_party(id),
    votes_count INTEGER NOT NULL,
    submitted_by_id BIGINT REFERENCES users(id),
    submission_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, VERIFIED, DISPUTED
    verification_time TIMESTAMP WITH TIME ZONE,
    verified_by_id BIGINT REFERENCES users(id),
    image_proof_url TEXT,
    notes TEXT,
    CONSTRAINT valid_votes_count CHECK (votes_count >= 0)
);

-- Create indexes for better query performance
CREATE INDEX idx_vote_result_polling_station ON vote_result(polling_station_id);
CREATE INDEX idx_vote_result_party ON vote_result(party_id);
CREATE INDEX idx_vote_result_status ON vote_result(status);
CREATE INDEX idx_polling_station_region ON polling_station(region_id);