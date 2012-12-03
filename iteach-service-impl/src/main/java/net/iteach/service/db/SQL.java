package net.iteach.service.db;

public interface SQL {
	
	String SCHOOLS_FOR_TEACHER = "SELECT ID, NAME FROM SCHOOLS WHERE TEACHER = :teacher ORDER BY NAME";

}
