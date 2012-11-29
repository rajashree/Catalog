-- adding extra information for tracking progress of a import.
ALTER TABLE t_data_files ADD host varchar(256) NULL;

ALTER TABLE t_data_files ADD startTime datetime NULL;

ALTER TABLE t_data_files ADD endTime datetime NULL;

ALTER TABLE t_data_files ADD currentRow int NOT NULL DEFAULT(-1);

ALTER TABLE t_data_files ADD numRows int NOT NULL DEFAULT(0);
