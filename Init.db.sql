-- Create schemas for different services
CREATE SCHEMA IF NOT EXISTS user_service;
CREATE SCHEMA IF NOT EXISTS itinerary_service;

-- Grant permissions
GRANT ALL PRIVILEGES ON SCHEMA user_service TO travel_user;
GRANT ALL PRIVILEGES ON SCHEMA itinerary_service TO travel_user;

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";