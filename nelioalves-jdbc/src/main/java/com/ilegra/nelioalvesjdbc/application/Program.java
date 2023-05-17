package com.ilegra.nelioalvesjdbc.application;

import com.ilegra.nelioalvesjdbc.model.dao.Dao;
import com.ilegra.nelioalvesjdbc.model.dao.DaoFactory;
import com.ilegra.nelioalvesjdbc.model.entities.Department;
import com.ilegra.nelioalvesjdbc.model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {

        Department department = new Department(1, "Books");
        Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.0, department);

        Dao<Seller> sellerDao = DaoFactory.createSellerDao();

        System.out.println(seller);
    }
}