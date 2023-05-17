package com.ilegra.nelioalvesjdbc.application;

import com.ilegra.nelioalvesjdbc.model.dao.Dao;
import com.ilegra.nelioalvesjdbc.model.dao.DaoFactory;
import com.ilegra.nelioalvesjdbc.model.entities.Seller;

public class Program {
    public static void main(String[] args) {

        Dao<Seller> sellerDao = DaoFactory.createSellerDao();

        System.out.println("==== Test 1: seller findById =====");
        Seller seller = sellerDao.findById(3);

        System.out.println(seller);
    }
}