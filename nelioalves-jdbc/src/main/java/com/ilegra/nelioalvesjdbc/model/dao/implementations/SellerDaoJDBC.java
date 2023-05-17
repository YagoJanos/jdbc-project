package com.ilegra.nelioalvesjdbc.model.dao.implementations;

import com.ilegra.nelioalvesjdbc.db.DB;
import com.ilegra.nelioalvesjdbc.db.exception.DbException;
import com.ilegra.nelioalvesjdbc.model.dao.SellerDao;
import com.ilegra.nelioalvesjdbc.model.entities.Department;
import com.ilegra.nelioalvesjdbc.model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = connection.prepareStatement("SELECT seller.*, department.Name as DepName "+
                                                "FROM seller INNER JOIN department " +
                                                "ON seller.DepartmentId = department.Id " +
                                                "WHERE seller.Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()){
                Department dep = instantiateDepartment(rs);

                Seller seller = instantiateSeller(rs, dep);

                return seller;
            }
            return null;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = connection.prepareStatement("SELECT seller.*, department.Name as DepName "+
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE DepartmentId = ? " +
                    "ORDER BY Name");
            st.setInt(1, department.getId());

            rs = st.executeQuery();

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()){

                Department dep = map.get(rs.getInt("DepartmentId"));

                if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(rs, dep);

                sellerList.add(seller);
            }
            return sellerList;

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }



    private static Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("Department"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    private static Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setDepartment(dep);
        return seller;
    }
}