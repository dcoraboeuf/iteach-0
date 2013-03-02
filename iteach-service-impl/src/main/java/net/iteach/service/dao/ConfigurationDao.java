package net.iteach.service.dao;

public interface ConfigurationDao {

    String getValue(String name);

    void setValue(String name, String value);
}
