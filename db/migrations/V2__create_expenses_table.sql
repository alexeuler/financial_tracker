CREATE TABLE expenses (
  id SERIAL PRIMARY KEY,
  amount BIGINT NOT NULL,
  description VARCHAR(255) NOT NULL,
  comment TEXT,
  occured_at TIMESTAMP NOT NULL,
  user_id INTEGER REFERENCES users NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT current_timestamp,
  updated_at TIMESTAMP NOT NULL DEFAULT current_timestamp
);