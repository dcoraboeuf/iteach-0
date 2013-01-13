-- Adds the additional columns
ALTER TABLE COORDINATES ADD COLUMN SCHOOL INTEGER NULL;
ALTER TABLE COORDINATES ADD COLUMN STUDENT INTEGER NULL;

-- Drops constraint
ALTER TABLE COORDINATES DROP CONSTRAINT UQ_COORDINATES;

-- Adds the additional constraints
ALTER TABLE COORDINATES ADD CONSTRAINT FK_COORDINATE_SCHOOL FOREIGN KEY (SCHOOL) REFERENCES SCHOOLS (ID) ON DELETE CASCADE;
ALTER TABLE COORDINATES ADD CONSTRAINT FK_COORDINATE_STUDENT FOREIGN KEY (STUDENT) REFERENCES STUDENTS (ID) ON DELETE CASCADE;

-- Clean-up
DELETE FROM COORDINATES WHERE ENTITY_TYPE = 'SCHOOLS' AND ENTITY_ID NOT IN (SELECT ID FROM SCHOOLS);
DELETE FROM COORDINATES WHERE ENTITY_TYPE = 'STUDENTS' AND ENTITY_ID NOT IN (SELECT ID FROM STUDENTS);

-- Copy
UPDATE COORDINATES SET SCHOOL = ENTITY_ID WHERE ENTITY_TYPE = 'SCHOOLS';
UPDATE COORDINATES SET STUDENT = ENTITY_ID WHERE ENTITY_TYPE = 'STUDENTS';

-- Removes the entity columns
ALTER TABLE COORDINATES DROP COLUMN ENTITY_TYPE;
ALTER TABLE COORDINATES DROP COLUMN ENTITY_ID;

-- @mysql

-- Adds the additional columns
ALTER TABLE COORDINATES ADD COLUMN SCHOOL INTEGER NULL;
ALTER TABLE COORDINATES ADD COLUMN STUDENT INTEGER NULL;

-- Drops constraint
ALTER TABLE COORDINATES DROP INDEX UQ_COORDINATES;

-- Adds the additional constraints
ALTER TABLE COORDINATES ADD CONSTRAINT FK_COORDINATE_SCHOOL FOREIGN KEY (SCHOOL) REFERENCES SCHOOLS (ID) ON DELETE CASCADE;
ALTER TABLE COORDINATES ADD CONSTRAINT FK_COORDINATE_STUDENT FOREIGN KEY (STUDENT) REFERENCES STUDENTS (ID) ON DELETE CASCADE;

-- Clean-up
DELETE FROM COORDINATES WHERE ENTITY_TYPE = 'SCHOOLS' AND ENTITY_ID NOT IN (SELECT ID FROM SCHOOLS);
DELETE FROM COORDINATES WHERE ENTITY_TYPE = 'STUDENTS' AND ENTITY_ID NOT IN (SELECT ID FROM STUDENTS);

-- Copy
UPDATE COORDINATES SET SCHOOL = ENTITY_ID WHERE ENTITY_TYPE = 'SCHOOLS';
UPDATE COORDINATES SET STUDENT = ENTITY_ID WHERE ENTITY_TYPE = 'STUDENTS';

-- Removes the entity columns
ALTER TABLE COORDINATES DROP COLUMN ENTITY_TYPE;
ALTER TABLE COORDINATES DROP COLUMN ENTITY_ID;