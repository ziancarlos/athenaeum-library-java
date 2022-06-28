CREATE DATABASE IF NOT EXISTS `athenaeum_library`
/*!40100 DEFAULT CHARACTER SET utf8mb3 */
/*!80016 DEFAULT ENCRYPTION='N' */;

USE `athenaeum_library`;

-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)

--

-- Host: localhost    Database: athenaeum_library

-- ------------------------------------------------------

-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */

;

/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */

;

/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */

;

/*!50503 SET NAMES utf8 */

;

/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */

;

/*!40103 SET TIME_ZONE='+00:00' */

;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */

;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */

;

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */

;

/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */

;

--

-- Temporary view structure for view `admin`

--

DROP TABLE IF EXISTS `admin`;

/*!50001 DROP VIEW IF EXISTS `admin`*/

;

SET @saved_cs_client = @@character_set_client;

/*!50503 SET character_set_client = utf8mb4 */

;

/*!50001 CREATE VIEW `admin` AS SELECT 
 1 AS `id`,
 1 AS `username`,
 1 AS `password`,
 1 AS `role`,
 1 AS `created_at`*/

;

SET character_set_client = @saved_cs_client;

--

-- Table structure for table `bookkeepings`

--

DROP TABLE IF EXISTS `bookkeepings`;

/*!40101 SET @saved_cs_client     = @@character_set_client */

;

/*!50503 SET character_set_client = utf8mb4 */

;

CREATE TABLE `bookkeepings` (
    `id` int NOT NULL AUTO_INCREMENT,
    `double_entry_type` enum('debit', 'kredit') NOT NULL,
    `transaction_type` enum('purchasing', 'fine', 'borrowing') NOT NULL,
    `amount` decimal(15, 2) NOT NULL,
    `payment_date` timestamp NOT NULL,
    `purchasing_id` int DEFAULT NULL,
    `borrowing_id` int DEFAULT NULL,
    `penalty_id` int DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`),
    UNIQUE KEY `purchasing_id` (`purchasing_id`),
    UNIQUE KEY `borrowing_id` (`borrowing_id`),
    UNIQUE KEY `penalty_id` (`penalty_id`),
    CONSTRAINT `bookkeepings_ibfk_1` FOREIGN KEY (`purchasing_id`) REFERENCES `purchasings` (`id`),
    CONSTRAINT `bookkeepings_ibfk_2` FOREIGN KEY (`borrowing_id`) REFERENCES `borrowings` (`id`),
    CONSTRAINT `bookkeepings_ibfk_3` FOREIGN KEY (`penalty_id`) REFERENCES `penalties` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Table structure for table `books`

--

DROP TABLE IF EXISTS `books`;

/*!40101 SET @saved_cs_client     = @@character_set_client */

;

/*!50503 SET character_set_client = utf8mb4 */

;

CREATE TABLE `books` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    `category_id` int NOT NULL,
    `availability` enum('available', 'unavailable') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`),
    KEY `category_id` (`category_id`),
    CONSTRAINT `books_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 12 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Table structure for table `borrowed_books`

--

DROP TABLE IF EXISTS `borrowed_books`;

/*!40101 SET @saved_cs_client     = @@character_set_client */

;

/*!50503 SET character_set_client = utf8mb4 */

;

CREATE TABLE `borrowed_books` (
    `borrow_id` int NOT NULL,
    `borrow_customer_id` int NOT NULL,
    `book_id` int NOT NULL,
    `price` decimal(15, 2) NOT NULL,
    `status` enum('returned', 'broken', 'lost', 'on-going') NOT NULL,
    `end_date` timestamp NOT NULL,
    `returned_date` timestamp NULL DEFAULT NULL,
    `penalty_id` int DEFAULT NULL,
    UNIQUE KEY `penalty_id` (`penalty_id`),
    KEY `borrowings_id` (`borrow_id`),
    KEY `borrowings_customer_id` (`borrow_customer_id`),
    KEY `book_id` (`book_id`),
    CONSTRAINT `borrowed_books_ibfk_1` FOREIGN KEY (`borrow_id`) REFERENCES `borrowings` (`id`),
    CONSTRAINT `borrowed_books_ibfk_2` FOREIGN KEY (`borrow_customer_id`) REFERENCES `users` (`id`),
    CONSTRAINT `borrowed_books_ibfk_3` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
    CONSTRAINT `borrowed_books_ibfk_4` FOREIGN KEY (`penalty_id`) REFERENCES `penalties` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Table structure for table `borrowings`

--

DROP TABLE IF EXISTS `borrowings`;

/*!40101 SET @saved_cs_client     = @@character_set_client */

;

/*!50503 SET character_set_client = utf8mb4 */

;

CREATE TABLE `borrowings` (
    `id` int NOT NULL AUTO_INCREMENT,
    `customer_id` int NOT NULL,
    `status` enum('on-going', 'completed') NOT NULL DEFAULT 'on-going',
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`),
    KEY `customer_id` (`customer_id`),
    CONSTRAINT `borrowings_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 9 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Temporary view structure for view `borrowings_bookkeepings`

--

DROP TABLE IF EXISTS `borrowings_bookkeepings`;

/*!50001 DROP VIEW IF EXISTS `borrowings_bookkeepings`*/

;

SET @saved_cs_client = @@character_set_client;

/*!50503 SET character_set_client = utf8mb4 */

;

/*!50001 CREATE VIEW `borrowings_bookkeepings` AS SELECT 
 1 AS `double_entry_type`,
 1 AS `transaction_type`,
 1 AS `amount`,
 1 AS `borrowing_id`,
 1 AS `payment_date`*/

;

SET character_set_client = @saved_cs_client;

--

-- Table structure for table `categories`

--

DROP TABLE IF EXISTS `categories`;

/*!40101 SET @saved_cs_client     = @@character_set_client */

;

/*!50503 SET character_set_client = utf8mb4 */

;

CREATE TABLE `categories` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(46) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB AUTO_INCREMENT = 18 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Temporary view structure for view `customers`

--

DROP TABLE IF EXISTS `customers`;

/*!50001 DROP VIEW IF EXISTS `customers`*/

;

SET @saved_cs_client = @@character_set_client;

/*!50503 SET character_set_client = utf8mb4 */

;

/*!50001 CREATE VIEW `customers` AS SELECT 
 1 AS `id`,
 1 AS `username`,
 1 AS `password`,
 1 AS `role`,
 1 AS `phone_number`,
 1 AS `created_at`,
 1 AS `blacklisted`*/

;

SET character_set_client = @saved_cs_client;

--

-- Temporary view structure for view `libarians`

--

DROP TABLE IF EXISTS `libarians`;

/*!50001 DROP VIEW IF EXISTS `libarians`*/

;

SET @saved_cs_client = @@character_set_client;

/*!50503 SET character_set_client = utf8mb4 */

;

/*!50001 CREATE VIEW `libarians` AS SELECT 
 1 AS `id`,
 1 AS `username`,
 1 AS `password`,
 1 AS `role`,
 1 AS `active`,
 1 AS `created_at`*/

;

SET character_set_client = @saved_cs_client;

--

-- Table structure for table `penalties`

--

DROP TABLE IF EXISTS `penalties`;

/*!40101 SET @saved_cs_client     = @@character_set_client */

;

/*!50503 SET character_set_client = utf8mb4 */

;

CREATE TABLE `penalties` (
    `id` int NOT NULL AUTO_INCREMENT,
    `penalty_type` enum('late', 'broken', 'lost') NOT NULL,
    `penalty_date` timestamp NOT NULL,
    `payment_status` enum('paid', 'unpaid') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 2 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Temporary view structure for view `penalties_bookkeepings`

--

DROP TABLE IF EXISTS `penalties_bookkeepings`;

/*!50001 DROP VIEW IF EXISTS `penalties_bookkeepings`*/

;

SET @saved_cs_client = @@character_set_client;

/*!50503 SET character_set_client = utf8mb4 */

;

/*!50001 CREATE VIEW `penalties_bookkeepings` AS SELECT 
 1 AS `double_entry_type`,
 1 AS `transaction_type`,
 1 AS `amount`,
 1 AS `purchasing_id`,
 1 AS `payment_date`*/

;

SET character_set_client = @saved_cs_client;

--

-- Table structure for table `purchasings`

--

DROP TABLE IF EXISTS `purchasings`;

/*!40101 SET @saved_cs_client     = @@character_set_client */

;

/*!50503 SET character_set_client = utf8mb4 */

;

CREATE TABLE `purchasings` (
    `id` int NOT NULL AUTO_INCREMENT,
    `supplier_name` varchar(30) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 8 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Temporary view structure for view `purchasings_bookkeepings`

--

DROP TABLE IF EXISTS `purchasings_bookkeepings`;

/*!50001 DROP VIEW IF EXISTS `purchasings_bookkeepings`*/

;

SET @saved_cs_client = @@character_set_client;

/*!50503 SET character_set_client = utf8mb4 */

;

/*!50001 CREATE VIEW `purchasings_bookkeepings` AS SELECT 
 1 AS `id`,
 1 AS `double_entry_type`,
 1 AS `transaction_type`,
 1 AS `amount`,
 1 AS `purchasing_id`,
 1 AS `payment_date`*/

;

SET character_set_client = @saved_cs_client;

--

-- Table structure for table `purchasings_books_details`

--

DROP TABLE IF EXISTS `purchasings_books_details`;

/*!40101 SET @saved_cs_client     = @@character_set_client */

;

/*!50503 SET character_set_client = utf8mb4 */

;

CREATE TABLE `purchasings_books_details` (
    `id` int NOT NULL AUTO_INCREMENT,
    `purchasing_price` decimal(15, 2) NOT NULL,
    `purchasing_id` int NOT NULL,
    `book_id` int NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`),
    UNIQUE KEY `book_id` (`book_id`),
    KEY `purchasing_id` (`purchasing_id`),
    CONSTRAINT `purchasings_books_details_ibfk_1` FOREIGN KEY (`purchasing_id`) REFERENCES `purchasings` (`id`),
    CONSTRAINT `purchasings_books_details_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Temporary view structure for view `purchasings_details`

--

DROP TABLE IF EXISTS `purchasings_details`;

/*!50001 DROP VIEW IF EXISTS `purchasings_details`*/

;

SET @saved_cs_client = @@character_set_client;

/*!50503 SET character_set_client = utf8mb4 */

;

/*!50001 CREATE VIEW `purchasings_details` AS SELECT 
 1 AS `id`,
 1 AS `supplier_name`,
 1 AS `total_books`,
 1 AS `total_amount`,
 1 AS `payment_date`*/

;

SET character_set_client = @saved_cs_client;

--

-- Table structure for table `users`

--

DROP TABLE IF EXISTS `users`;

/*!40101 SET @saved_cs_client     = @@character_set_client */

;

/*!50503 SET character_set_client = utf8mb4 */

;

CREATE TABLE `users` (
    `id` int NOT NULL AUTO_INCREMENT,
    `username` varchar(30) NOT NULL,
    `password` varchar(45) NOT NULL,
    `role` enum('customer', 'admin', 'libarian') NOT NULL,
    `phone_number` varchar(15) DEFAULT NULL,
    `active` enum('active', 'unactive') DEFAULT NULL,
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`),
    UNIQUE KEY `username` (`username`),
    UNIQUE KEY `phone_number` (`phone_number`)
) ENGINE = InnoDB AUTO_INCREMENT = 21 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Dumping routines for database 'athenaeum_library'

--

--

-- Final view structure for view `admin`

--

/*!50001 DROP VIEW IF EXISTS `admin`*/

;

/*!50001 SET @saved_cs_client          = @@character_set_client */

;

/*!50001 SET @saved_cs_results         = @@character_set_results */

;

/*!50001 SET @saved_col_connection     = @@collation_connection */

;

/*!50001 SET character_set_client      = utf8mb4 */

;

/*!50001 SET character_set_results     = utf8mb4 */

;

/*!50001 SET collation_connection      = utf8mb4_unicode_ci */

;

/*!50001 CREATE ALGORITHM=UNDEFINED */

/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */

/*!50001 VIEW `admin` AS select `users`.`id` AS `id`,`users`.`username` AS `username`,`users`.`password` AS `password`,`users`.`role` AS `role`,`users`.`created_at` AS `created_at` from `users` where (`users`.`role` = 'admin') */

;

/*!50001 SET character_set_client      = @saved_cs_client */

;

/*!50001 SET character_set_results     = @saved_cs_results */

;

/*!50001 SET collation_connection      = @saved_col_connection */

;

--

-- Final view structure for view `borrowings_bookkeepings`

--

/*!50001 DROP VIEW IF EXISTS `borrowings_bookkeepings`*/

;

/*!50001 SET @saved_cs_client          = @@character_set_client */

;

/*!50001 SET @saved_cs_results         = @@character_set_results */

;

/*!50001 SET @saved_col_connection     = @@collation_connection */

;

/*!50001 SET character_set_client      = utf8mb4 */

;

/*!50001 SET character_set_results     = utf8mb4 */

;

/*!50001 SET collation_connection      = utf8mb4_unicode_ci */

;

/*!50001 CREATE ALGORITHM=UNDEFINED */

/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */

/*!50001 VIEW `borrowings_bookkeepings` AS select `bookkeepings`.`double_entry_type` AS `double_entry_type`,`bookkeepings`.`transaction_type` AS `transaction_type`,`bookkeepings`.`amount` AS `amount`,`bookkeepings`.`borrowing_id` AS `borrowing_id`,`bookkeepings`.`payment_date` AS `payment_date` from `bookkeepings` where ((`bookkeepings`.`penalty_id` is null) and (`bookkeepings`.`purchasing_id` is null) and (`bookkeepings`.`borrowing_id` is not null)) */

;

/*!50001 SET character_set_client      = @saved_cs_client */

;

/*!50001 SET character_set_results     = @saved_cs_results */

;

/*!50001 SET collation_connection      = @saved_col_connection */

;

--

-- Final view structure for view `customers`

--

/*!50001 DROP VIEW IF EXISTS `customers`*/

;

/*!50001 SET @saved_cs_client          = @@character_set_client */

;

/*!50001 SET @saved_cs_results         = @@character_set_results */

;

/*!50001 SET @saved_col_connection     = @@collation_connection */

;

/*!50001 SET character_set_client      = utf8mb4 */

;

/*!50001 SET character_set_results     = utf8mb4 */

;

/*!50001 SET collation_connection      = utf8mb4_unicode_ci */

;

/*!50001 CREATE ALGORITHM=UNDEFINED */

/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */

/*!50001 VIEW `customers` AS select `users`.`id` AS `id`,`users`.`username` AS `username`,`users`.`password` AS `password`,`users`.`role` AS `role`,`users`.`phone_number` AS `phone_number`,`users`.`created_at` AS `created_at`,if((count((`penalties`.`payment_status` = 'unpaid')) > 0),'blacklisted','not-blacklisted') AS `blacklisted` from ((`users` left join `borrowed_books` on((`borrowed_books`.`borrow_customer_id` = `users`.`id`))) left join `penalties` on((`borrowed_books`.`penalty_id` = `penalties`.`id`))) group by `users`.`id` having (`users`.`role` = 'customer') */

;

/*!50001 SET character_set_client      = @saved_cs_client */

;

/*!50001 SET character_set_results     = @saved_cs_results */

;

/*!50001 SET collation_connection      = @saved_col_connection */

;

--

-- Final view structure for view `libarians`

--

/*!50001 DROP VIEW IF EXISTS `libarians`*/

;

/*!50001 SET @saved_cs_client          = @@character_set_client */

;

/*!50001 SET @saved_cs_results         = @@character_set_results */

;

/*!50001 SET @saved_col_connection     = @@collation_connection */

;

/*!50001 SET character_set_client      = utf8mb4 */

;

/*!50001 SET character_set_results     = utf8mb4 */

;

/*!50001 SET collation_connection      = utf8mb4_unicode_ci */

;

/*!50001 CREATE ALGORITHM=UNDEFINED */

/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */

/*!50001 VIEW `libarians` AS select `users`.`id` AS `id`,`users`.`username` AS `username`,`users`.`password` AS `password`,`users`.`role` AS `role`,`users`.`active` AS `active`,`users`.`created_at` AS `created_at` from `users` where (`users`.`role` = 'libarian') */

;

/*!50001 SET character_set_client      = @saved_cs_client */

;

/*!50001 SET character_set_results     = @saved_cs_results */

;

/*!50001 SET collation_connection      = @saved_col_connection */

;

--

-- Final view structure for view `penalties_bookkeepings`

--

/*!50001 DROP VIEW IF EXISTS `penalties_bookkeepings`*/

;

/*!50001 SET @saved_cs_client          = @@character_set_client */

;

/*!50001 SET @saved_cs_results         = @@character_set_results */

;

/*!50001 SET @saved_col_connection     = @@collation_connection */

;

/*!50001 SET character_set_client      = utf8mb4 */

;

/*!50001 SET character_set_results     = utf8mb4 */

;

/*!50001 SET collation_connection      = utf8mb4_unicode_ci */

;

/*!50001 CREATE ALGORITHM=UNDEFINED */

/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */

/*!50001 VIEW `penalties_bookkeepings` AS select `bookkeepings`.`double_entry_type` AS `double_entry_type`,`bookkeepings`.`transaction_type` AS `transaction_type`,`bookkeepings`.`amount` AS `amount`,`bookkeepings`.`purchasing_id` AS `purchasing_id`,`bookkeepings`.`payment_date` AS `payment_date` from `bookkeepings` where ((`bookkeepings`.`borrowing_id` is null) and (`bookkeepings`.`purchasing_id` is null) and (`bookkeepings`.`penalty_id` is not null)) */

;

/*!50001 SET character_set_client      = @saved_cs_client */

;

/*!50001 SET character_set_results     = @saved_cs_results */

;

/*!50001 SET collation_connection      = @saved_col_connection */

;

--

-- Final view structure for view `purchasings_bookkeepings`

--

/*!50001 DROP VIEW IF EXISTS `purchasings_bookkeepings`*/

;

/*!50001 SET @saved_cs_client          = @@character_set_client */

;

/*!50001 SET @saved_cs_results         = @@character_set_results */

;

/*!50001 SET @saved_col_connection     = @@collation_connection */

;

/*!50001 SET character_set_client      = utf8mb4 */

;

/*!50001 SET character_set_results     = utf8mb4 */

;

/*!50001 SET collation_connection      = utf8mb4_unicode_ci */

;

/*!50001 CREATE ALGORITHM=UNDEFINED */

/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */

/*!50001 VIEW `purchasings_bookkeepings` AS select `bookkeepings`.`id` AS `id`,`bookkeepings`.`double_entry_type` AS `double_entry_type`,`bookkeepings`.`transaction_type` AS `transaction_type`,`bookkeepings`.`amount` AS `amount`,`bookkeepings`.`purchasing_id` AS `purchasing_id`,`bookkeepings`.`payment_date` AS `payment_date` from `bookkeepings` where ((`bookkeepings`.`borrowing_id` is null) and (`bookkeepings`.`penalty_id` is null) and (`bookkeepings`.`purchasing_id` is not null)) */

;

/*!50001 SET character_set_client      = @saved_cs_client */

;

/*!50001 SET character_set_results     = @saved_cs_results */

;

/*!50001 SET collation_connection      = @saved_col_connection */

;

--

-- Final view structure for view `purchasings_details`

--

/*!50001 DROP VIEW IF EXISTS `purchasings_details`*/

;

/*!50001 SET @saved_cs_client          = @@character_set_client */

;

/*!50001 SET @saved_cs_results         = @@character_set_results */

;

/*!50001 SET @saved_col_connection     = @@collation_connection */

;

/*!50001 SET character_set_client      = utf8mb4 */

;

/*!50001 SET character_set_results     = utf8mb4 */

;

/*!50001 SET collation_connection      = utf8mb4_unicode_ci */

;

/*!50001 CREATE ALGORITHM=UNDEFINED */

/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */

/*!50001 VIEW `purchasings_details` AS select `purchasings`.`id` AS `id`,`purchasings`.`supplier_name` AS `supplier_name`,count(`purchasings_books_details`.`purchasing_id`) AS `total_books`,sum(`purchasings_books_details`.`purchasing_price`) AS `total_amount`,`bookkeepings`.`payment_date` AS `payment_date` from ((`purchasings` join `purchasings_books_details` on((`purchasings`.`id` = `purchasings_books_details`.`purchasing_id`))) join `bookkeepings` on((`purchasings`.`id` = `bookkeepings`.`purchasing_id`))) group by `purchasings`.`id` */

;

/*!50001 SET character_set_client      = @saved_cs_client */

;

/*!50001 SET character_set_results     = @saved_cs_results */

;

/*!50001 SET collation_connection      = @saved_col_connection */

;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */

;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */

;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */

;

/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */

;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */

;

/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */

;

/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */

;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */

;

-- Dump completed on 2022-06-22 21:39:19