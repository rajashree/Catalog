ALTER TABLE t_documents ADD author NVARCHAR(255);

ALTER TABLE t_documents ADD source NVARCHAR(4000);

ALTER TABLE t_documents ADD abstractText NVARCHAR(MAX);

ALTER TABLE t_documents ADD publishDate datetime;