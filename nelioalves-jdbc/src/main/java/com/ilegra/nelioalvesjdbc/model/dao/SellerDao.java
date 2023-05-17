package com.ilegra.nelioalvesjdbc.model.dao;

import com.ilegra.nelioalvesjdbc.model.entities.Department;
import com.ilegra.nelioalvesjdbc.model.entities.Seller;

import java.util.List;

public interface SellerDao extends Dao<Seller> {

    List<Seller> findByDepartment(Department department);
}
