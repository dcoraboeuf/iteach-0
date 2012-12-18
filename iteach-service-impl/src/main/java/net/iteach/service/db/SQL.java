package net.iteach.service.db;

public interface SQL {
	
	String USER_CREATE = "INSERT INTO USERS (MODE, IDENTIFIER, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES (:mode, :identifier, :password, :email, :firstName, :lastName)";

	String USER_BY_OPENID = "SELECT ID, EMAIL, FIRSTNAME, LASTNAME FROM USERS WHERE MODE = 'openid' AND IDENTIFIER = :identifier";

	String USER_BY_PASSWORD = "SELECT ID, PASSWORD, EMAIL, FIRSTNAME, LASTNAME FROM USERS WHERE MODE = 'password' AND IDENTIFIER = :identifier";
	
	String SCHOOLS_FOR_TEACHER = "SELECT * FROM SCHOOLS WHERE TEACHER = :teacher ORDER BY NAME";

	String SCHOOL_CREATE = "INSERT INTO SCHOOLS (TEACHER, NAME, COLOR) VALUES (:teacher, :name, :color)";

	String SCHOOL_DELETE = "DELETE FROM SCHOOLS WHERE TEACHER = :teacher AND ID = :id";

	String SCHOOL_UPDATE = "UPDATE SCHOOLS SET NAME = :name, COLOR = :color WHERE TEACHER = :teacher AND ID = :id";

}
