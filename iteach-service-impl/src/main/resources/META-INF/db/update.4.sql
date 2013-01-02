DROP TABLE COORDINATES;

CREATE TABLE SCHOOL_COORDINATES (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	SCHOOL INTEGER NOT NULL,
	COORD_TYPE VARCHAR(20) NOT NULL,
	COORD_VALUE VARCHAR(150) NOT NULL,
	CONSTRAINT PK_SCHOOL_COORDINATES PRIMARY KEY (ID),
	CONSTRAINT FK_SCHOOL_COORDINATES_SCHOOL FOREIGN KEY (SCHOOL) REFERENCES SCHOOLS (ID) ON DELETE CASCADE,
	CONSTRAINT UQ_SCHOOL_COORDINATES UNIQUE (SCHOOL, COORD_TYPE)
);

CREATE TABLE STUDENT_COORDINATES (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	STUDENT INTEGER NOT NULL,
	COORD_TYPE VARCHAR(20) NOT NULL,
	COORD_VALUE VARCHAR(150) NOT NULL,
	CONSTRAINT PK_STUDENT_COORDINATES PRIMARY KEY (ID),
	CONSTRAINT FK_STUDENT_COORDINATES_STUDENT FOREIGN KEY (STUDENT) REFERENCES STUDENTS (ID) ON DELETE CASCADE,
	CONSTRAINT UQ_STUDENT_COORDINATES UNIQUE (STUDENT, COORD_TYPE)
);