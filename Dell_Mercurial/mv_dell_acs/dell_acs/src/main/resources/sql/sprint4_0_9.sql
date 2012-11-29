/*
  Alter the [t_events] table to add new column.
*/
ALTER TABLE t_events
  ADD status int DEFAULT 1 WITH VALUES;