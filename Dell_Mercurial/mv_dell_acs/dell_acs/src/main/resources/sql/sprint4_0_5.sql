/**
 * t_taxonomy and t_taxonomy_category related changes;
 * Updated the type to and position
*/

UPDATE t_taxonomy SET type = 6000 WHERE type = 1000 OR type IS NULL;

UPDATE t_taxonomy_category SET position = 0 WHERE position IS NULL;

/**
  * t_documents related changes
  * Updating types of old documents where it is null setting them to be of type document that is 2000
  * JIRA TICKET - https://jira.marketvine.com/browse/CS-450
  * Fixes the TICKET - https://jira.marketvine.com/browse/CS-461 - Unable to create documents
*/

UPDATE t_documents set type = 2000 WHERE type IS NULL;