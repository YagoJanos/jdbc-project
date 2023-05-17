package com.ilegra.nelioalvesjdbc.model.dao;

import com.ilegra.nelioalvesjdbc.model.dao.impl.SellerDaoJDBC;
import com.ilegra.nelioalvesjdbc.model.entities.Seller;

public class DaoFactory {

    public static Dao<Seller> createSellerDao(){
        return new SellerDaoJDBC();
    }
}
