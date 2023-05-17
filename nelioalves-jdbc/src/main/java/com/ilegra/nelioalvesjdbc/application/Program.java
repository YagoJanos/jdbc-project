package com.ilegra.nelioalvesjdbc.application;

import com.ilegra.nelioalvesjdbc.model.dao.factories.DaoFactory;
import com.ilegra.nelioalvesjdbc.model.dao.SellerDao;
import com.ilegra.nelioalvesjdbc.model.entities.Department;
import com.ilegra.nelioalvesjdbc.model.entities.Seller;

import java.util.List;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("==== Test 1: seller findById =====");
        Seller seller = sellerDao.findById(3);

        System.out.println(seller);

        System.out.println("==== Test 2: seller findByDepartment =====");
        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);

        for(Seller obj : list){
            System.out.println(obj);
        }

        System.out.println("==== Test 3: seller findAll =====");
        list = sellerDao.findAll();

        for(Seller obj : list){
            System.out.println(obj);
        }
    }
}