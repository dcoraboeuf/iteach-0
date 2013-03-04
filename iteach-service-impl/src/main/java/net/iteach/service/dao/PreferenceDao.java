package net.iteach.service.dao;

public interface PreferenceDao {
    String getValue(int userId, String name);

    void storeValue(int userId, String name, String value);
}
