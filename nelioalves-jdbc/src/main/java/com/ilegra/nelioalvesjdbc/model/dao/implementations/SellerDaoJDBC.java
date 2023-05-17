package com.ilegra.nelioalvesjdbc.model.dao.implementations;

import com.ilegra.nelioalvesjdbc.db.DB;
import com.ilegra.nelioalvesjdbc.db.exception.DbException;
import com.ilegra.nelioalvesjdbc.model.dao.SellerDao;
import com.ilegra.nelioalvesjdbc.model.entities.Department;
import com.ilegra.nelioalvesjdbc.model.entities.Seller;

import java.sql.*;
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
    public void insert(Seller seller) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
                    "INSERT INTO seller " +
                        "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                        "VALUES " +
                        "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
                );

            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0){ //Se uma linha é alterada
                ResultSet rs = st.getGeneratedKeys(); //Pega o id da linha alterada
                if (rs.next()) { //
                    int id = rs.getInt(1);
                    seller.setId(id); //Insere o id no objeto (acredito que o certo seria retornar outro objeto)
                }
                DB.closeResultSet(rs);
            } else {
                //Pois uma linha deveria ser criada no banco de dados
                throw new DbException("Unexpected Error! No rows affected!");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void update(Seller seller) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement("UPDATE seller " +
                    "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                    "WHERE Id = ?");

            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());

            st.executeUpdate();
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement("DELETE FROM seller WHERE Id = ?");
            pst.setInt(1, id);

            pst.executeUpdate();
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
        }
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
                                                //O select pega as colunas da segunda tabela que não estão na primeira
                                                //O where que determina que é pelo id.
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
                    //o where usa a coluna da primeira coluna que é chave estrangeira
            st.setInt(1, department.getId());

            rs = st.executeQuery();

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>(); //para evitar repetição de objetos da segunda tabela
                                                            //devido a múltiplas referências na linha da primeira

            while (rs.next()){

                int departmentId = rs.getInt("DepartmentId");
                Department dep = map.get(departmentId); //tenta extrair do map o valor da coluna usada como restrição
                if(dep == null){ //se não conseguiu foi porque não havia
                    dep = instantiateDepartment(rs); //cria o valor então
                    map.put(departmentId, dep); //insere no map o valor criado
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
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = connection.prepareStatement("SELECT seller.*, department.Name as DepName "+
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "ORDER BY Name");
                    //Como as outras buscas, mas sem restrição nenhuma.
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
