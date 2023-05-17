package com.ilegra.nelioalvesjdbc.model.dao.factories;

import com.ilegra.nelioalvesjdbc.db.DB;
import com.ilegra.nelioalvesjdbc.model.dao.SellerDao;
import com.ilegra.nelioalvesjdbc.model.dao.implementations.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(DB.getConnection());
    }
}
