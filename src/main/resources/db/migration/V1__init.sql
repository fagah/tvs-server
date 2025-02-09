-- Political Parties
CREATE TABLE political_party (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Party Representatives (Users)
CREATE TABLE party_representative (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    party_id BIGINT REFERENCES political_party(id),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP WITH TIME ZONE
);

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

-- Vote Results per Party per Polling Station
CREATE TABLE vote_result (
    id BIGSERIAL PRIMARY KEY,
    polling_station_id BIGINT REFERENCES polling_station(id),
    party_id BIGINT REFERENCES political_party(id),
    votes_count INTEGER NOT NULL,
    submitted_by_id BIGINT REFERENCES party_representative(id),
    submission_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, VERIFIED, DISPUTED
    verification_time TIMESTAMP WITH TIME ZONE,
    verified_by_id BIGINT REFERENCES party_representative(id),
    image_proof_url TEXT,
    notes TEXT,
    CONSTRAINT valid_votes_count CHECK (votes_count >= 0)
);

-- Polling Station Assignment (which representatives are assigned to which stations)
CREATE TABLE polling_station_assignment (
    id BIGSERIAL PRIMARY KEY,
    polling_station_id BIGINT REFERENCES polling_station(id),
    representative_id BIGINT REFERENCES party_representative(id),
    assigned_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(polling_station_id, representative_id)
);

-- Create indexes for better query performance
CREATE INDEX idx_vote_result_polling_station ON vote_result(polling_station_id);
CREATE INDEX idx_vote_result_party ON vote_result(party_id);
CREATE INDEX idx_vote_result_status ON vote_result(status);
CREATE INDEX idx_polling_station_region ON polling_station(region_id);