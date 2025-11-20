package br.com.market.application;

import br.com.market.enums.Position;

import java.util.Scanner;

import br.com.market.db.DatabaseInitializer;
import br.com.market.model.Admin;
import br.com.market.model.Shopper;
import br.com.market.model.User;
import br.com.market.model.discount.Discount;
import br.com.market.model.discount.PaperDiscount;
import br.com.market.model.discount.DigitalDiscount;
import br.com.market.model.discount.CardDiscount;
import br.com.market.service.AdminService;
import br.com.market.service.LoginService;
import br.com.market.service.PaymentService;
import br.com.market.service.ShopperService;
import br.com.market.service.ProductService;

public class Main {

    static boolean program = true;

    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();

        boolean loginProcess = true;

        try (Scanner sc = new Scanner(System.in)) {
            while (loginProcess) {
                LoginService ls = new LoginService();
                System.out.println("");
                System.out.println("");
                System.out.println("");
                System.out.println("Press (1) to log in or (2) to register");

                String scAnswer = sc.nextLine();
                switch (scAnswer) {
                    case "1" -> {
                        System.out.println("");
                        System.out.println("===LOGIN===");
                        System.out.println("");
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Password: ");
                        String password = sc.nextLine();

                        if (ls.login(name, password) == null) {
                            System.out.println("Invalid credentials!");
                        } else {
                            User user = ls.login(name, password);

                            if (user.getPosition().equals(Position.ADMIN)) {
                                Admin admin = (Admin) user;
                                run(admin);
                                loginProcess = false;
                            }
                            if (user.getPosition().equals(Position.SHOPPER)) {
                                assert user instanceof Shopper;
                                Shopper shopper = (Shopper) user;
                                run(shopper);
                                loginProcess = false;
                            }
                        }
                    }
                    case "2" -> {
                        System.out.println("===REGISTER===");
                        System.out.println("");
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Password: ");
                        String password = sc.nextLine();

                        ls.createAccount(name, password);
                        System.out.println("User created!");
                    }
                    default -> System.out.println("Invalid option!");
                }
            }
        }
    }

    private static void run(Shopper user) {
        try (Scanner sc = new Scanner(System.in)) {
            while (program) {
                ProductService ps = new ProductService();
                ShopperService ss = new ShopperService();

                String scAnswer;
                System.out.println("");
                System.out.println("");
                System.out.println("");
                System.out.println("===MARKET SYSTEM===");
                System.out.println("Your money: $" + user.getMoney());
                System.out.println("Cart price: $" + user.getCartPrice());
                System.out.println("");
                System.out.println("What do you want to do?");
                System.out.println("(1) Add product to cart");
                System.out.println("(2) Remove product from cart");
                System.out.println("(3) List the cart");
                System.out.println("(4) Checkout");
                System.out.println("(5) Exit");
                System.out.print("Choose an option: ");
                scAnswer = sc.nextLine();

                switch (scAnswer) {
                    case "1" -> {
                        //LISTAR PRODUTOS
                        ps.listProducts();

                        //RECOLHER INFORMACOES
                        System.out.print("Enter the product name to add: ");
                        String productName = sc.nextLine();
                        System.out.print("Enter the product quantity to add: ");
                        int productQuantity = sc.nextInt();
                        sc.nextLine();

                        //ADICIONAR AO CARRINHO E REMOVER DO BANCO DE DADOS
                        user = ss.addProduct(ps.getProductByName(productName), productQuantity, user);
                        System.out.println("Product added to cart!");
                    }
                    case "2" -> {
                        if (!user.getCart().isEmpty()) {
                            System.out.println("Cart products: ");
                            user.listCart();

                            System.out.print("Enter the product name to remove: ");
                            String productName = sc.nextLine();
                            System.out.print("Enter the product quantity to remove: ");
                            int productQuantity = sc.nextInt();
                            sc.nextLine();

                            user = ss.removeProduct(ps.getProductByName(productName), productQuantity, user);

                            System.out.println("Product removed from cart!");
                        } else {
                            System.out.println("Empty cart!");
                        }
                    }
                    case "3" -> {
                        if (user.getCart().isEmpty()) {
                            System.out.println("Empty cart!");
                        } else {
                            System.out.println("Cart products: ");
                            user.listCart();
                        }
                    }
                    case "4" -> {
                        if (!user.getCart().isEmpty()) {
                            System.out.println("Select payment method: ");
                            System.out.println("(1) Cash (5% discount)");
                            System.out.println("(2) Digital payment (3% discount)");
                            System.out.println("(3) Credit card (regular price)");

                            String option = sc.nextLine();
                            Discount paymentOption;
                            switch (option) {
                                case "1" -> paymentOption = new PaperDiscount();
                                case "2" -> paymentOption = new DigitalDiscount();
                                case "3" -> paymentOption = new CardDiscount();
                                default -> {
                                    System.out.println("Invalid option");
                                    return;
                                }
                            }

                            PaymentService paymentService = new PaymentService(paymentOption);

                            if (user.getMoney() < user.getCartPrice()) {
                                System.out.println("Insufficient funds for this cart!");
                            } else {
                                System.out.println("");
                                System.out.println("");
                                System.out.println("");
                                System.out.println("Receipt: ");
                                user = paymentService.payment(user);
                                System.out.println("");
                                System.out.println("");
                                System.out.println("");
                                System.out.println("Remaining balance: " + user.getMoney());
                                program = false;
                            }
                        } else {
                            System.out.println("Empty cart!");
                        }
                    }
                    case "5" -> {
                        System.out.println("Are you sure you want to exit? (Y/N)");
                        scAnswer = sc.nextLine().toUpperCase();
                        if (scAnswer.equals("Y")) {
                            System.out.println("Exiting...");
                            user.cleanCart();
                            program = false;
                        } else {
                            System.out.println("Returning!");
                        }
                    }
                    default -> System.out.println("Invalid option!");
                }
            }
        } catch (Exception e) {
            for (StackTraceElement exception : e.getStackTrace()) {
                System.out.println(exception);
            }
            user.cleanCart();
            System.out.println(e.getMessage());
        }
    }

    private static void run(Admin user) {
        try (Scanner sc = new Scanner(System.in)) {
            while (program) {
                AdminService as = new AdminService();
                ProductService ps = new ProductService();

                String scAnswer;
                System.out.println("===MARKET CONTROL SYSTEM===");
                System.out.println("What do you want to do?");
                System.out.println("(1) Add a product to system");
                System.out.println("(2) Remove a product from system");
                System.out.println("(3) List products");
                System.out.println("(4) Add quantity to product");
                System.out.println("(5) Remove quantity from product");
                System.out.println("(6) Exit");
                System.out.print("Choose an option: ");
                scAnswer = sc.nextLine();

                switch (scAnswer) {
                    case "1" -> {
                        System.out.print("Product name: ");
                        String name = sc.nextLine();
                        System.out.print("Product price: ");
                        Double price = sc.nextDouble();
                        System.out.print("Product quantity: ");
                        Integer quantity = sc.nextInt();
                        sc.nextLine();

                        as.addProduct(name, price, quantity);
                        System.out.println("Product added to system!");
                    }
                    case "2" -> {
                        //listar produtos
                        ps.listProducts();

                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        as.removeProduct(name);
                        System.out.println("Product removed from system!");
                    }
                    case "3" ->  ps.listProducts();
                    case "4" -> {
                        ps.listProducts();

                        System.out.print("Product name: ");
                        String name = sc.nextLine();
                        System.out.print("Quantity to add: ");
                        Integer quantity = sc.nextInt();
                        sc.nextLine();

                        as.addQuantity(name, quantity);
                        System.out.println("Quantity added to product!");
                    }
                    case "5" -> {
                        ps.listProducts();

                        System.out.print("Product name: ");
                        String name = sc.nextLine();
                        System.out.print("Quantity to remove: ");
                        Integer quantity = sc.nextInt();
                        sc.nextLine();

                        as.removeQuantity(name, quantity);
                        System.out.println("Quantity removed from product!");
                    }
                    case "6" -> {
                        System.out.println("Are you sure you want to exit? (Y/N)");
                        scAnswer = sc.nextLine().toUpperCase();
                        if (scAnswer.equals("Y")) {
                            System.out.println("Exiting...");
                            program = false;
                        } else {
                            System.out.println("Returning!");
                        }
                    }
                    default ->  System.out.println("Invalid option!");
                }
            }
        }
    }
}
