package net.iteach.service.db;

public interface SQL {
	
	String SCHOOLS_FOR_TEACHER = "SELECT ID, NAME FROM SCHOOLS WHERE TEACHER = :teacher ORDER BY NAME";
	String TEACHER_CREATE = "INSERT INTO TEACHERS (IDENTIFIER, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES (:identifier, :password, :email, :firstName, :lastName)";

}
