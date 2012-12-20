package net.iteach.service.db;

public interface SQL {
	
	// Users
	
	String USER_CREATE = "INSERT INTO USERS (MODE, IDENTIFIER, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES (:mode, :identifier, :password, :email, :firstName, :lastName)";

	String USER_BY_OPENID = "SELECT ID, EMAIL, FIRSTNAME, LASTNAME FROM USERS WHERE MODE = 'openid' AND IDENTIFIER = :identifier";

	String USER_BY_PASSWORD = "SELECT ID, PASSWORD, EMAIL, FIRSTNAME, LASTNAME FROM USERS WHERE MODE = 'password' AND IDENTIFIER = :identifier";
	
	// Schools
	
	String SCHOOLS_FOR_TEACHER = "SELECT * FROM SCHOOLS WHERE TEACHER = :teacher ORDER BY NAME";

	String SCHOOL_CREATE = "INSERT INTO SCHOOLS (TEACHER, NAME, COLOR) VALUES (:teacher, :name, :color)";

	String SCHOOL_DELETE = "DELETE FROM SCHOOLS WHERE TEACHER = :teacher AND ID = :id";

	String SCHOOL_UPDATE = "UPDATE SCHOOLS SET NAME = :name, COLOR = :color WHERE TEACHER = :teacher AND ID = :id";
	
	// Students
	
	String STUDENTS_FOR_TEACHER = "SELECT S.*, H.* FROM STUDENTS S INNER JOIN SCHOOLS H ON S.SCHOOL = H.ID WHERE H.TEACHER = :teacher ORDER BY S.LASTNAME";

	String STUDENT_CREATE = "INSERT INTO STUDENTS (SCHOOL, SUBJECT, FIRSTNAME, LASTNAME) VALUES (:school, :subject, :firstname, :lastname)";

	String STUDENT_DELETE = "DELETE FROM STUDENTS WHERE ID = :id";

	String STUDENT_UPDATE = "UPDATE STUDENTS SET SCHOOL = :school, SUBJECT = :subject, FIRSTNAME = :firstname, LASTNAME = :lastname WHERE ID = :id";

}
