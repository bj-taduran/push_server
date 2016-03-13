-- Please DO NOT delete sample data. This is used by acceptance tests. Deleting any might cause broken builds --
INSERT INTO Application (id, name, dateCreated, dateDeleted, mode) VALUES ('4471dc68-9ee9-47bb-93a7-c058f743f9dd', 'Development Admin App','2013-01-23 16:39:45','2013-01-23 16:39:45','DEVELOPMENT');
INSERT INTO Application (id, name, dateCreated, dateDeleted, mode) VALUES ('47a63d95-9582-41a3-9aa0-34fe2d97c428', 'Production Admin App','2013-01-23 16:39:45','2013-01-23 16:39:45','PRODUCTION');
INSERT INTO Application (id, name, dateCreated, dateDeleted, mode) VALUES ('17026fb0-70ea-11e2-bcfd-0800200c9a66', 'MTT Admin Development App','2013-01-23 16:39:45','2013-01-23 16:39:45','DEVELOPMENT');
INSERT INTO Tag (dateCreated, dateDeleted, dateUpdated, name, app_Id) VALUES ('2013-01-23 16:39:45', null, null, 'sample tag', '4471dc68-9ee9-47bb-93a7-c058f743f9dd');
INSERT INTO Tag (dateCreated, dateDeleted, dateUpdated, name, app_Id) VALUES ('2013-01-23 16:39:45', null, null, 'another sample tag', '4471dc68-9ee9-47bb-93a7-c058f743f9dd');
INSERT INTO Recipient (channelType, dateCreated, receiver, app_id) VALUES ('IOS', '2013-01-23 16:39:45', 'a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516abc', '4471dc68-9ee9-47bb-93a7-c058f743f9dd');
INSERT INTO TAGS_RECIPIENTS (tag_id, recipients_id) VALUES (1, 1);
INSERT INTO APNSChannel (name, type, cert, app_id, password) VALUES('apns name', 'IOS', 'src/main/resources/PushSample.p12', '4471dc68-9ee9-47bb-93a7-c058f743f9dd', 'art!ckm0nk5y');
INSERT INTO User (username, name) VALUES('admin', 'Admin');
INSERT INTO User (username, name) VALUES('mtt-admin', 'MTT Admin');
INSERT INTO USERS_APPS (user_id, applications_id) VALUES(1, '4471dc68-9ee9-47bb-93a7-c058f743f9dd');
INSERT INTO USERS_APPS (user_id, applications_id) VALUES(1, '47a63d95-9582-41a3-9aa0-34fe2d97c428');
INSERT INTO USERS_APPS (user_id, applications_id) VALUES(2, '17026fb0-70ea-11e2-bcfd-0800200c9a66');
ALTER TABLE 'm2push'.'TAGS_RECIPIENTS' DROP FOREIGN KEY 'FKC3538D605B0BB645' ;
ALTER TABLE 'm2push'.'TAGS_RECIPIENTS' ADD CONSTRAINT 'FKC3538D605B0BB645'  FOREIGN KEY ('recipients_id' )  REFERENCES 'm2push'.'Recipient' ('id' )  ON DELETE CASCADE;
ALTER TABLE 'm2push'.'MESSAGE_RECIPIENTS' DROP FOREIGN KEY 'FKFDB5E0D25B0BB645' ;
ALTER TABLE 'm2push'.'MESSAGE_RECIPIENTS'  ADD CONSTRAINT 'FKFDB5E0D25B0BB645'  FOREIGN KEY ('recipients_id' )  REFERENCES 'm2push'.'Recipient' ('id' )  ON DELETE CASCADE;
