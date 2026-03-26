import dao.UserDao;
import dao.UserDaoImpl;
import entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static util.UtilForHibernate.getSessionFactory;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl(getSessionFactory());
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println("Options: ");
            System.out.println("1. Add user");
            System.out.println("2. Update user");
            System.out.println("3. Delete user");
            System.out.println("4. Show all users");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();

                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setAge(age);
                    user.setCreatedAt(LocalDateTime.now());

                    userDao.save(user);
                    System.out.println("User added successfully!");
                }

                case 2 -> {
                    System.out.print("Enter id of user to update: ");
                    long id = scanner.nextLong();
                    scanner.nextLine();

                    System.out.print("Enter new name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter new email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter new age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();

                    User user = new User();
                    user.setId(id);
                    user.setName(name);
                    user.setEmail(email);
                    user.setAge(age);
                    user.setCreatedAt(LocalDateTime.now());

                    userDao.update(user);
                    System.out.println("User updated successfully!");
                }

                case 3 -> {
                    System.out.print("Enter user ID to delete: ");
                    long id = scanner.nextLong();
                    scanner.nextLine(); // consume newline

                    userDao.delete(id);
                    System.out.println("User deleted successfully!");
                }

                case 4 -> {
                    List<User> users = userDao.findAll();
                    if (users.isEmpty()) {
                        System.out.println("They are no users!");
                    } else {
                        System.out.println("Users");
                        for (User u : users) {
                            System.out.println(u.getId() + " " + u.getName() + " " + u.getEmail() + " " + u.getCreatedAt());
                        }
                    }
                }

                default -> System.out.println("Incorrect option");
            }
        }

        scanner.close();
    }
}
