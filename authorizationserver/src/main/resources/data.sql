INSERT IGNORE INTO user VALUES (1,'test@test.com','test','test','$2a$10$rJETdZGhwXsdzYAYZyvAw.AQBIMQ4GYKbxvI80Y30Rb.KUXAxBxJq','0666666666666666');

INSERT IGNORE INTO authority VALUES ('ADMIN','Admin');
INSERT IGNORE INTO authority VALUES ('USER','normal user');

INSERT IGNORE INTO user_authority(user_id, authority_id) VALUES (1,'USER');
INSERT IGNORE INTO user_authority(user_id, authority_id) VALUES (1,'ADMIN');


