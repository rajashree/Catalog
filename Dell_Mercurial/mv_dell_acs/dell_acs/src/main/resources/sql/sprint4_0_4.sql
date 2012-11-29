/**
 * Alter table script for DOCUMENTS
 * Added 3 fields : type, body and url
 * respective Users.
 */
ALTER TABLE t_documents
  ADD type numeric(4) NOT NULL
  DEFAULT 2000;

ALTER TABLE t_documents
  ADD body ntext;

ALTER TABLE t_documents
  ADD url ntext;

ALTER TABLE t_documents
  ADD status int DEFAULT 1;
