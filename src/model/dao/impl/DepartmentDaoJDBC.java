package model.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection con = null;

    public DepartmentDaoJDBC(Connection con) {
        this.con = con;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(
                    "INSERT INTO department" +
                            "(Name) " +
                            "VALUES " +
                            "(?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, department.getName());
            int rowsAffected = statement.executeUpdate();
            ResultSet set = statement.getGeneratedKeys();
            if (rowsAffected > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    department.setId(id);
                }
            }
            else {
                throw new DbException("Unexpected error! No rows affected!");
            }
            DB.closeSQLWrapper(set);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeSQLWrapper(statement);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(
                    "UPDATE department " +
                            "SET Name = ? " +
                            "WHERE id = ?",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, department.getName());
            statement.setInt(2, department.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeSQLWrapper(statement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(
                    "DELETE FROM department " +
                            "WHERE Id = ?;"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeSQLWrapper(statement);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(
                    "SELECT department.* FROM department " +
                            "WHERE Id=?;"
            );
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                Department department = new Department();
                department.setId(set.getInt("Id"));
                department.setName(set.getString("Name"));
                return department;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeSQLWrapper(statement);
        }
        return null;
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(
                    "SELECT department.* FROM department " +
                            "ORDER BY Name"
            );
            ResultSet set = statement.executeQuery();
            List<Department> departments = new ArrayList<>();
            while (set.next()) {
                Department department = new Department();
                department.setId(set.getInt("Id"));
                department.setName(set.getString("Name"));
                departments.add(department);
            }
            return departments;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeSQLWrapper(statement);
        }
    }
}
