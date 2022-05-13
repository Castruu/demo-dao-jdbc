package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection con;
    public SellerDaoJDBC(Connection con) {
        this.con = con;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(
                    "INSERT INTO seller " +
                            "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                            "VALUES " +
                            "(?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, seller.getName());
            statement.setString(2, seller.getEmail());
            statement.setDate(3, new Date(seller.getBirthDate().getTime()));
            statement.setDouble(4, seller.getBaseSalary());
            statement.setInt(5, seller.getDepartment().getId());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) {
                ResultSet set = statement.getGeneratedKeys();
                if(set.next()) {
                    int id = set.getInt(1);
                    seller.setId(id);
                }
                DB.closeSQLWrapper(set);
            } else {
                throw new DbException("Unexpected error! No rows affected");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeSQLWrapper(statement);
        }
    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet set = null;
        try {
            statement = con.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE seller.Id = ?;"
            );
            statement.setInt(1, id);
            set = statement.executeQuery();
            if(set.next()) {
                Department dep = instantiateDepartment(set);
                return instantiateSeller(set, dep);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeSQLWrapper(set);
            DB.closeSQLWrapper(statement);
        }
        return null;
    }

    private Seller instantiateSeller(ResultSet set, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(set.getInt("Id"));
        seller.setName(set.getString("Name"));
        seller.setEmail(set.getString("Email"));
        seller.setBaseSalary(set.getDouble("BaseSalary"));
        seller.setBirthDate(set.getDate("BirthDate"));
        seller.setDepartment(dep);
        return seller;
    }

    private Department instantiateDepartment(ResultSet set) throws SQLException {
        Department dep = new Department();
        dep.setId(set.getInt("DepartmentId"));
        dep.setName(set.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement statement = null;
        ResultSet set = null;
        try {
            statement = con.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "ORDER BY id;"
            );
            set = statement.executeQuery();
            return getSellerQueryList(set);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeSQLWrapper(set);
            DB.closeSQLWrapper(statement);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement statement = null;
        ResultSet set = null;
        try {
            statement = con.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE department.Id = ? " +
                            "ORDER BY Name;"
            );
            statement.setInt(1, department.getId());
            set = statement.executeQuery();
            return getSellerQueryList(set);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeSQLWrapper(set);
            DB.closeSQLWrapper(statement);
        }
    }

    private List<Seller> getSellerQueryList(ResultSet set) throws SQLException{
        List<Seller> list = new ArrayList<>();
        Map<Integer, Department> map = new HashMap<>();
        while (set.next()) {
            int departmentId = set.getInt("DepartmentId");
            Department dep = map.get(departmentId);
            if(dep == null) {
                dep = instantiateDepartment(set);
                map.put(departmentId, dep);
            }
            Seller seller = instantiateSeller(set, dep);
            list.add(seller);
        }
        return list;
    }
}
