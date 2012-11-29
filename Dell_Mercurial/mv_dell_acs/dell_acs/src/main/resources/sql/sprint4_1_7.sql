/* Drop unique indexes of t_apikey_activity and create non- unique  */

DROP INDEX t_apikey_activity.IX_Activity_Requesturl;
DROP INDEX t_apikey_activity.IX_Activity_APIKey;

CREATE NONCLUSTERED INDEX IX_Activity_APIKey ON t_apikey_activity(apiKey);
CREATE NONCLUSTERED INDEX IX_Activity_Username ON t_apikey_activity(username);