package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection con;
    public SellerDaoJDBC(Connection con) {
        this.con = con;
    }

    @Override
    public void insert(Seller department) {

    }

    @Override
    public void update(Seller department) {

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
                Department dep = new Department();
                dep.setId(set.getInt("DepartmentId"));
                dep.setName(set.getString("DepName"));
                Seller seller = new Seller();
                seller.setId(set.getInt("Id"));
                seller.setName(set.getString("Name"));
                seller.setEmail(set.getString("Email"));
                seller.setBaseSalary(set.getDouble("BaseSalary"));
                seller.setBirthDate(set.getDate("BirthDate"));
                seller.setDepartment(dep);
                return seller;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeSQLWrapper(set);
            DB.closeSQLWrapper(statement);
        }
        return null;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
