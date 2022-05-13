package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.dao.impl.DepartmentDaoJDBC;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("TESTE 1 - FIND BY ID");
        Department department = departmentDao.findById(7);
        System.out.println(department);

        System.out.println("TESTE 2 - DELETE BY ID");
        departmentDao.deleteById(7);
        System.out.println("Deleted!");

        System.out.println("TESTE 3 - Inserted");
        Department department1 = new Department(null, "A");
        departmentDao.insert(department1);
        System.out.println("Inserted! Id = " + department1.getId());

        System.out.println("TESTE 4 - Updated");
        department.setName("B");
        departmentDao.update(department);
        System.out.println("Updated! New Name = " + department.getName());

        System.out.println("TESTE 5 - findAll");
        List<Department> departments = departmentDao.findAll();
        for(Department d : departments) {
            System.out.println(d);
        }

        sc.close();
    }

    private static void userTest(Scanner sc) {
        SellerDao sellerDao = DaoFactory.createSellerDao();


        System.out.println("===TEST 1: Seller findById===");
        Seller seller = sellerDao.findById(2);
        System.out.println(seller);


        System.out.println("===TEST 2: Seller findByDepartment===");
        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);
        list.forEach(System.out::println);

        System.out.println("===TEST 3: Seller findAll===");
        list = sellerDao.findAll();
        list.forEach(System.out::println);

        System.out.println("===TEST 4: Seller insert===");
        Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
        sellerDao.insert(newSeller);
        System.out.println("Inserted! New id = " + newSeller.getId());

        System.out.println("===TEST 5: Seller insert===");
        newSeller = sellerDao.findById(1);
        newSeller.setName("Martin");
        newSeller.setEmail("martin@gmail.com");
        sellerDao.update(newSeller);
        System.out.println("Updated");

        System.out.println("===TEST 5: Seller insert===");
        System.out.println("Enter id for deletion: ");
        int id = sc.nextInt();
        sellerDao.deleteById(id);
        System.out.println("Deleted ID = " + id);

    }

}
