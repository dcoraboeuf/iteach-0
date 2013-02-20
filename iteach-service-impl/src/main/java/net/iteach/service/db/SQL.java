package net.iteach.service.db;

public interface SQL {
	
	// Users
	
	String USER_ADMINISTRATOR_COUNT = "SELECT COUNT(ID) FROM USERS WHERE ADMINISTRATOR IS TRUE";

	String USER_BY_IDENTIFIER = "SELECT ID FROM USERS WHERE IDENTIFIER = :identifier";
	String USER_ID_BY_EMAIL = "SELECT ID FROM USERS WHERE EMAIL = :email";
	String USER_SUMMARY_BY_EMAIL = "SELECT ID, FIRSTNAME, LASTNAME, EMAIL FROM USERS WHERE EMAIL = :email";
	String USER_SUMMARY_BY_ID = "SELECT ID, FIRSTNAME, LASTNAME, EMAIL FROM USERS WHERE ID = :id";

	String USER_CREATE = "INSERT INTO USERS (ADMINISTRATOR, VERIFIED, MODE, IDENTIFIER, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES (:administrator, :verified, :mode, :identifier, :password, :email, :firstName, :lastName)";

	String USER_BY_OPENID = "SELECT ID, EMAIL, FIRSTNAME, LASTNAME, ADMINISTRATOR FROM USERS WHERE MODE = 'openid' AND IDENTIFIER = :identifier AND VERIFIED IS TRUE";

	String USER_BY_PASSWORD = "SELECT ID, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, ADMINISTRATOR FROM USERS WHERE MODE = 'password' AND IDENTIFIER = :identifier AND VERIFIED IS TRUE";
	
	String USER_SET_VERIFIED = "UPDATE USERS SET VERIFIED = TRUE WHERE ID = :id";
	String USER_CHANGE_PASSWORD = "UPDATE USERS SET PASSWORD = :newpassword WHERE ID = :id AND PASSWORD = :oldpassword";
	
	// Schools
	
	String SCHOOLS_FOR_TEACHER = "SELECT * FROM SCHOOLS WHERE TEACHER = :teacher ORDER BY NAME";

	String SCHOOL_CREATE = "INSERT INTO SCHOOLS (TEACHER, NAME, COLOR, HRATE) VALUES (:teacher, :name, :color, :hourlyRate)";

	String SCHOOL_DELETE = "DELETE FROM SCHOOLS WHERE TEACHER = :teacher AND ID = :id";

	String SCHOOL_UPDATE = "UPDATE SCHOOLS SET NAME = :name, COLOR = :color, HRATE = :hourlyRate WHERE TEACHER = :teacher AND ID = :id";

	String STUDENTS_FOR_SCHOOL = "SELECT ID, NAME, SUBJECT, DISABLED FROM STUDENTS WHERE SCHOOL = :id ORDER BY NAME";

	String SCHOOL_DETAILS = "SELECT * FROM SCHOOLS WHERE ID = :id";
	
	// Students
	
	String STUDENTS_FOR_TEACHER = "SELECT S.*, H.ID AS SCHOOL_ID, H.NAME AS SCHOOL_NAME, H.COLOR AS SCHOOL_COLOR, H.HRATE AS SCHOOL_HRATE FROM STUDENTS S INNER JOIN SCHOOLS H ON S.SCHOOL = H.ID WHERE H.TEACHER = :teacher AND S.DISABLED IS FALSE ORDER BY S.NAME";
	
	String STUDENT_DETAILS = "SELECT S.*, H.ID AS SCHOOL_ID, H.NAME AS SCHOOL_NAME, H.COLOR AS SCHOOL_COLOR, H.HRATE AS SCHOOL_HRATE FROM STUDENTS S INNER JOIN SCHOOLS H ON S.SCHOOL = H.ID WHERE S.ID = :id";

	String STUDENT_CREATE = "INSERT INTO STUDENTS (SCHOOL, SUBJECT, NAME, DISABLED) VALUES (:school, :subject, :name, false)";

	String STUDENT_DELETE = "DELETE FROM STUDENTS WHERE ID = :id";

	String STUDENT_UPDATE = "UPDATE STUDENTS SET SCHOOL = :school, SUBJECT = :subject, NAME = :name WHERE ID = :id";
	
	// Lessons
	
	String LESSONS = "SELECT L.*, S.ID AS STUDENT_ID, S.SUBJECT AS STUDENT_SUBJECT, S.NAME AS STUDENT_NAME, S.DISABLED AS STUDENT_DISABLED, H.ID AS SCHOOL_ID, H.NAME AS SCHOOL_NAME, H.COLOR AS SCHOOL_COLOR, H.HRATE AS SCHOOL_HRATE FROM LESSONS L INNER JOIN STUDENTS S ON S.ID = L.STUDENT INNER JOIN SCHOOLS H ON H.ID = S.SCHOOL AND H.TEACHER = :teacher WHERE CONCAT(PDATE,'T',PFROM) >= :from AND CONCAT(PDATE,'T','PTO') <= :to";

	String LESSON_CREATE = "INSERT INTO LESSONS (STUDENT, PDATE, PFROM, PTO, LOCATION) VALUES (:student, :date, :from, :to, :location)";
	
	String LESSON_UPDATE = "UPDATE LESSONS SET STUDENT = :student, PDATE = :date, PFROM = :from, PTO = :to, LOCATION = :location WHERE ID = :id";

	String LESSON_DELETE = "DELETE FROM LESSONS WHERE ID = :id";

	String LESSONS_FOR_STUDENT = "SELECT * FROM LESSONS WHERE STUDENT = :id AND PDATE >= :from AND PDATE <= :to ORDER BY PFROM";

	String STUDENT_TOTAL_HOURS = "SELECT PFROM, PTO FROM LESSONS WHERE STUDENT = :id";

	String LESSON_DETAILS = "SELECT L.*, S.ID AS STUDENT_ID, S.SUBJECT AS STUDENT_SUBJECT, S.NAME AS STUDENT_NAME, S.DISABLED AS STUDENT_DISABLED, H.ID AS SCHOOL_ID, H.NAME AS SCHOOL_NAME, H.COLOR AS SCHOOL_COLOR, H.HRATE AS SCHOOL_HRATE FROM LESSONS L INNER JOIN STUDENTS S ON S.ID = L.STUDENT INNER JOIN SCHOOLS H ON H.ID = S.SCHOOL WHERE L.ID = :id";

	String LESSON_RANGE = "SELECT PDATE, PFROM, PTO FROM LESSONS WHERE ID = :id";
	
	String LESSON_RANGE_UPDATE = "UPDATE LESSONS SET PDATE = :date, PFROM = :from, PTO = :to WHERE ID = :id";
	
	// Security checks

	String TEACHER_FOR_SCHOOL = "SELECT TEACHER FROM SCHOOLS WHERE TEACHER = :teacher AND ID = :school";

	String TEACHER_FOR_STUDENT = "SELECT H.TEACHER FROM STUDENTS S INNER JOIN SCHOOLS H ON H.ID = S.SCHOOL AND H.TEACHER = :teacher WHERE S.ID = :student";

	String TEACHER_FOR_LESSON = "SELECT H.TEACHER FROM LESSONS L INNER JOIN STUDENTS S ON S.ID = L.STUDENT INNER JOIN SCHOOLS H ON H.ID = S.SCHOOL AND H.TEACHER = :teacher WHERE L.ID = :lesson";
	
	// Tokens

	String TOKEN_SAVE = "INSERT INTO TOKENS (TOKEN, TOKENTYPE, TOKENKEY, CREATION) VALUES (:token, :tokentype, :tokenkey, :creation)";
	String TOKEN_CHECK = "SELECT TOKENKEY, CREATION FROM TOKENS WHERE TOKEN = :token AND TOKENTYPE = :tokentype ORDER BY CREATION DESC LIMIT 1";
	String TOKEN_DELETE = "DELETE FROM TOKENS WHERE TOKENTYPE = :tokentype AND TOKENKEY = :tokenkey";
	String TOKEN_CLEANUP = "DELETE FROM TOKENS WHERE CREATION < :creation";
	
	// IDs
		
	String SCHOOL_IDS_FOR_TEACHER = "SELECT ID FROM SCHOOLS WHERE TEACHER = :teacher";
	String STUDENT_IDS_FOR_TEACHER = "SELECT S.ID FROM STUDENTS S INNER JOIN SCHOOLS H ON S.SCHOOL = H.ID WHERE H.TEACHER = :teacher";
	String LESSON_IDS_FOR_TEACHER = "SELECT L.ID FROM LESSONS L INNER JOIN STUDENTS S ON S.ID = L.STUDENT INNER JOIN SCHOOLS H ON H.ID = S.SCHOOL AND H.TEACHER = :teacher";
	
	// Profile

	String PROFILE = "SELECT * FROM USERS WHERE ID = :id";
	
	// Administrator queries

	String ADMIN_ACCOUNTS = "SELECT * FROM USERS ORDER BY LASTNAME, FIRSTNAME";
	String ADMIN_ACCOUNT_BY_ID = PROFILE;
	String ADMIN_ACCOUNT_DELETE = "DELETE FROM USERS WHERE ID = :id";
	
	// Reports
	
	String REPORT_MONTHLY = "SELECT L.PDATE, L.PFROM, L.PTO, L.STUDENT AS STUDENT, S.NAME AS STUDENT_NAME, S.SCHOOL AS SCHOOL, H.NAME AS SCHOOL_NAME, H.COLOR AS SCHOOL_COLOR, H.HRATE AS SCHOOL_HRATE FROM LESSONS L INNER JOIN STUDENTS S ON S.ID = L.STUDENT INNER JOIN SCHOOLS H ON H.ID = S.SCHOOL WHERE H.TEACHER = :teacher ORDER BY H.NAME, S.NAME";
	
	// Configuration

	String CONFIGURATION_GET = "SELECT VALUE FROM CONFIGURATION WHERE NAME = :name";

	String CONFIGURATION_DELETE = "DELETE FROM CONFIGURATION WHERE NAME = :name";
	String CONFIGURATION_SET = "INSERT INTO CONFIGURATION (NAME, VALUE) VALUES (:name, :value)";

    // Preferences

    String PREF_GET = "SELECT VALUE FROM PREFERENCES WHERE USERID = :user AND NAME = :name";
    String PREF_DELETE = "DELETE FROM PREFERENCES WHERE USERID = :user AND NAME = :name";
    String PREF_SET = "INSERT INTO PREFERENCES (USERID, NAME, VALUE) VALUES (:user, :name, :value)";
}
