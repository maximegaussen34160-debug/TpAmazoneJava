package com.example.demo.config;

import com.example.demo.produit.model.Product;
import com.example.demo.produit.repository.ProductRepository;
import com.example.demo.User.model.User;
import com.example.demo.User.repository.UserRepository;
import com.example.demo.User.service.PasswordHasher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initProducts(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                productRepository.save(new Product(null, "iPhone 15", 999.99, "Smartphone Apple dernière génération", 50, "https://boulanger.scene7.com/is/image/Boulanger/0195949037146_h_f_l_0?wid=535&hei=535&resMode=sharp2&op_usm=1.75,0.3,2,0&fmt=png-alpha"));
                productRepository.save(new Product(null, "MacBook Pro", 1999.99, "Ordinateur portable Apple M3", 25, "https://cdsassets.apple.com/live/SZLF0YNV/images/sp/111339_sp818-mbp13touch-space-select-202005.png"));
                productRepository.save(new Product(null, "AirPods Pro", 279.99, "Écouteurs sans fil avec réduction de bruit", 100, "https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/airpods-pro-3-hero-select-202509_FMT_WHH?wid=1200&hei=630&fmt=jpeg&qlt=95&.v=1758077264181"));
                productRepository.save(new Product(null, "iPad Air", 699.99, "Tablette Apple 10.9 pouces", 40, "https://media.ldlc.com/r1600/ld/products/00/06/13/36/LD0006133652_0006133676.jpg"));
                productRepository.save(new Product(null, "Apple Watch", 449.99, "Montre connectée Series 9", 60, "https://www.apple.com/assets-www/en_WW/watch/og/watch_og_1ff2ee953.png"));
                System.out.println("✅ 5 produits de test créés !");
            }
        };
    }

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordHasher passwordHasher) {
        return args -> {
            if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@admin.com");
                admin.setSalt("ADMIN_SALT_123");
                // Hash: "admin123" + salt, puis + pepper côté serveur
                String hashedPassword = passwordHasher.addPepperToHash(passwordHasher.hash("admin123" + "ADMIN_SALT_123"));
                admin.setPass(hashedPassword);
                admin.setRole("ADMIN");
                userRepository.save(admin);
                System.out.println("✅ Compte admin créé: admin@admin.com / admin123");
            }
        };
    }
}