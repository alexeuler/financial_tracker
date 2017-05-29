CREATE OR REPLACE FUNCTION update_modified_column()
  RETURNS TRIGGER AS
$BODY$
BEGIN
  NEW.updated_at = now();
RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql;
