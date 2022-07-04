CREATE DATABASE
    IF NOT EXISTS `athenaeum_library`
    /*!40100 DEFAULT CHARACTER SET utf8mb3 */
    /*!80016 DEFAULT ENCRYPTION='N' */
;

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

CREATE TABLE
    `bookkeepings` (
        `id` int NOT NULL AUTO_INCREMENT,
        `double_entry_type` enum('debit', 'kredit') NOT NULL,
        `transaction_type` enum(
            'purchasing',
            'fine',
            'borrowing'
        ) NOT NULL,
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
    ) ENGINE = InnoDB AUTO_INCREMENT = 20 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Dumping data for table `bookkeepings`

--

LOCK TABLES `bookkeepings` WRITE;

/*!40000 ALTER TABLE `bookkeepings` DISABLE KEYS */

;

INSERT INTO `bookkeepings`
VALUES (
        1,
        'kredit',
        'purchasing',
        49.00,
        '2022-06-27 12:37:29',
        8,
        NULL,
        NULL
    ), (
        2,
        'kredit',
        'purchasing',
        36.00,
        '2022-06-27 12:38:05',
        9,
        NULL,
        NULL
    ), (
        3,
        'debit',
        'borrowing',
        20.00,
        '2022-06-27 13:04:11',
        NULL,
        9,
        NULL
    ), (
        4,
        'debit',
        'borrowing',
        25.00,
        '2022-06-29 04:27:34',
        NULL,
        10,
        NULL
    ), (
        5,
        'debit',
        'borrowing',
        20.00,
        '2022-06-29 04:33:25',
        NULL,
        11,
        NULL
    ), (
        6,
        'debit',
        'borrowing',
        30.00,
        '2022-06-29 04:34:04',
        NULL,
        12,
        NULL
    ), (
        7,
        'debit',
        'borrowing',
        25.00,
        '2022-06-29 04:39:36',
        NULL,
        13,
        NULL
    ), (
        8,
        'debit',
        'borrowing',
        10.00,
        '2022-06-29 04:46:13',
        NULL,
        14,
        NULL
    ), (
        9,
        'debit',
        'fine',
        15.00,
        '2022-06-29 05:16:22',
        NULL,
        NULL,
        2
    ), (
        10,
        'debit',
        'fine',
        5.00,
        '2022-06-29 05:16:25',
        NULL,
        NULL,
        3
    ), (
        12,
        'debit',
        'fine',
        5.00,
        '2022-06-29 05:23:14',
        NULL,
        NULL,
        4
    ), (
        14,
        'debit',
        'fine',
        5.00,
        '2022-07-03 11:08:07',
        NULL,
        NULL,
        5
    ), (
        15,
        'debit',
        'fine',
        5.00,
        '2022-07-03 11:08:12',
        NULL,
        NULL,
        6
    ), (
        16,
        'debit',
        'fine',
        5.00,
        '2022-07-03 11:08:15',
        NULL,
        NULL,
        7
    ), (
        17,
        'debit',
        'fine',
        5.00,
        '2022-07-03 11:08:18',
        NULL,
        NULL,
        8
    ), (
        18,
        'debit',
        'fine',
        5.00,
        '2022-07-03 11:08:21',
        NULL,
        NULL,
        9
    ), (
        19,
        'debit',
        'fine',
        17.00,
        '2022-07-03 11:08:24',
        NULL,
        NULL,
        10
    );

/*!40000 ALTER TABLE `bookkeepings` ENABLE KEYS */

;

UNLOCK TABLES;

--

-- Table structure for table `books`

--

DROP TABLE IF EXISTS `books`;

/*!40101 SET @saved_cs_client     = @@character_set_client */

;

/*!50503 SET character_set_client = utf8mb4 */

;

CREATE TABLE
    `books` (
        `id` int NOT NULL AUTO_INCREMENT,
        `name` varchar(50) NOT NULL,
        `category_id` int NOT NULL,
        `availability` enum('available', 'unavailable') NOT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `id` (`id`),
        KEY `category_id` (`category_id`),
        CONSTRAINT `books_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
    ) ENGINE = InnoDB AUTO_INCREMENT = 17 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Dumping data for table `books`

--

LOCK TABLES `books` WRITE;

/*!40000 ALTER TABLE `books` DISABLE KEYS */

;

INSERT INTO `books`
VALUES (
        12,
        'Absalom, Absalom',
        19,
        'unavailable'
    ), (
        13,
        'East of Eden by John Steinbeck',
        19,
        'unavailable'
    ), (
        14,
        'The Sun Also Rises by Ernest Hemingway',
        22,
        'unavailable'
    ), (
        15,
        'Vile Bodies by Evelyn Waugh',
        21,
        'unavailable'
    ), (
        16,
        'Moab is my Washpot',
        21,
        'unavailable'
    );

/*!40000 ALTER TABLE `books` ENABLE KEYS */

;

UNLOCK TABLES;

--

-- Table structure for table `borrowed_books`

--

DROP TABLE IF EXISTS `borrowed_books`;

/*!40101 SET @saved_cs_client     = @@character_set_client */

;

/*!50503 SET character_set_client = utf8mb4 */

;

CREATE TABLE
    `borrowed_books` (
        `borrowing_id` int NOT NULL,
        `borrowing_customer_id` int NOT NULL,
        `book_id` int NOT NULL,
        `price` decimal(15, 2) NOT NULL,
        `status` enum(
            'returned',
            'broken',
            'lost',
            'on-going'
        ) NOT NULL,
        `end_date` timestamp NOT NULL,
        `returned_date` timestamp NULL DEFAULT NULL,
        KEY `borrowings_id` (`borrowing_id`),
        KEY `borrowings_customer_id` (`borrowing_customer_id`),
        KEY `book_id` (`book_id`),
        CONSTRAINT `borrowed_books_ibfk_1` FOREIGN KEY (`borrowing_id`) REFERENCES `borrowings` (`id`),
        CONSTRAINT `borrowed_books_ibfk_2` FOREIGN KEY (`borrowing_customer_id`) REFERENCES `users` (`id`),
        CONSTRAINT `borrowed_books_ibfk_3` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Dumping data for table `borrowed_books`

--

LOCK TABLES `borrowed_books` WRITE;

/*!40000 ALTER TABLE `borrowed_books` DISABLE KEYS */

;

INSERT INTO `borrowed_books`
VALUES (
        9,
        23,
        12,
        10.00,
        'lost',
        '2022-06-27 17:00:00',
        NULL
    ), (
        9,
        23,
        15,
        10.00,
        'returned',
        '2022-06-27 17:00:00',
        '2022-06-27 13:07:47'
    ), (
        10,
        24,
        15,
        15.00,
        'lost',
        '2022-06-23 17:00:00',
        NULL
    ), (
        10,
        24,
        16,
        10.00,
        'returned',
        '2022-06-26 17:00:00',
        '2022-06-29 04:31:01'
    ), (
        11,
        25,
        13,
        15.00,
        'returned',
        '2022-06-29 17:00:00',
        '2022-06-29 04:33:50'
    ), (
        11,
        25,
        16,
        5.00,
        'returned',
        '2022-06-29 17:00:00',
        '2022-06-29 04:33:46'
    ), (
        12,
        25,
        14,
        20.00,
        'returned',
        '2022-06-29 17:00:00',
        '2022-06-29 04:39:21'
    ), (
        12,
        25,
        13,
        10.00,
        'returned',
        '2022-06-29 17:00:00',
        '2022-06-29 04:39:18'
    ), (
        13,
        25,
        14,
        15.00,
        'on-going',
        '2022-06-26 17:00:00',
        NULL
    ), (
        13,
        25,
        13,
        10.00,
        'on-going',
        '2022-06-30 17:00:00',
        NULL
    ), (
        14,
        26,
        16,
        10.00,
        'on-going',
        '2022-06-26 17:00:00',
        NULL
    );

/*!40000 ALTER TABLE `borrowed_books` ENABLE KEYS */

;

UNLOCK TABLES;

--

-- Table structure for table `borrowings`

--

DROP TABLE IF EXISTS `borrowings`;

/*!40101 SET @saved_cs_client     = @@character_set_client */

;

/*!50503 SET character_set_client = utf8mb4 */

;

CREATE TABLE
    `borrowings` (
        `id` int NOT NULL AUTO_INCREMENT,
        `customer_id` int NOT NULL,
        `status` enum('on-going', 'completed') NOT NULL DEFAULT 'on-going',
        PRIMARY KEY (`id`),
        UNIQUE KEY `id` (`id`),
        KEY `customer_id` (`customer_id`),
        CONSTRAINT `borrowings_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
    ) ENGINE = InnoDB AUTO_INCREMENT = 15 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Dumping data for table `borrowings`

--

LOCK TABLES `borrowings` WRITE;

/*!40000 ALTER TABLE `borrowings` DISABLE KEYS */

;

INSERT INTO `borrowings`
VALUES (9, 23, 'completed'), (10, 24, 'completed'), (11, 25, 'completed'), (12, 25, 'completed'), (13, 25, 'on-going'), (14, 26, 'on-going');

/*!40000 ALTER TABLE `borrowings` ENABLE KEYS */

;

UNLOCK TABLES;

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

CREATE TABLE
    `categories` (
        `id` int NOT NULL AUTO_INCREMENT,
        `name` varchar(46) NOT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `id` (`id`),
        UNIQUE KEY `name` (`name`)
    ) ENGINE = InnoDB AUTO_INCREMENT = 23 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Dumping data for table `categories`

--

LOCK TABLES `categories` WRITE;

/*!40000 ALTER TABLE `categories` DISABLE KEYS */

;

INSERT INTO `categories`
VALUES (19, 'Action And Adventure'), (20, 'Classics'), (21, 'Historical Fiction'), (22, 'Uncategories');

/*!40000 ALTER TABLE `categories` ENABLE KEYS */

;

UNLOCK TABLES;

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

CREATE TABLE
    `penalties` (
        `id` int NOT NULL AUTO_INCREMENT,
        `penalty_type` enum('late', 'broken', 'lost') NOT NULL,
        `penalty_date` timestamp NOT NULL,
        `payment_status` enum('paid', 'unpaid') NOT NULL,
        `amount` decimal(15, 2) NOT NULL,
        `borrowed_book_book_id` int NOT NULL,
        `borrowed_book_borrowing_customer_id` int NOT NULL,
        `borrowed_book_borrowing_id` int NOT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `id` (`id`),
        KEY `borrowed_book_book_id` (`borrowed_book_book_id`),
        KEY `borrowed_book_borrowing_customer_id` (
            `borrowed_book_borrowing_customer_id`
        ),
        KEY `borrowed_book_borrowing_id` (`borrowed_book_borrowing_id`),
        CONSTRAINT `penalties_ibfk_1` FOREIGN KEY (`borrowed_book_book_id`) REFERENCES `borrowed_books` (`book_id`),
        CONSTRAINT `penalties_ibfk_2` FOREIGN KEY (`borrowed_book_book_id`) REFERENCES `borrowed_books` (`book_id`),
        CONSTRAINT `penalties_ibfk_3` FOREIGN KEY (
            `borrowed_book_borrowing_customer_id`
        ) REFERENCES `borrowed_books` (`borrowing_customer_id`),
        CONSTRAINT `penalties_ibfk_4` FOREIGN KEY (`borrowed_book_borrowing_id`) REFERENCES `borrowed_books` (`borrowing_id`)
    ) ENGINE = InnoDB AUTO_INCREMENT = 25 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Dumping data for table `penalties`

--

LOCK TABLES `penalties` WRITE;

/*!40000 ALTER TABLE `penalties` DISABLE KEYS */

;

INSERT INTO `penalties`
VALUES (
        2,
        'lost',
        '2022-06-27 13:13:05',
        'paid',
        15.00,
        12,
        23,
        9
    ), (
        3,
        'late',
        '2022-06-24 17:00:00',
        'paid',
        5.00,
        15,
        24,
        10
    ), (
        4,
        'late',
        '2022-06-25 17:00:00',
        'paid',
        5.00,
        15,
        24,
        10
    ), (
        5,
        'late',
        '2022-06-26 17:00:00',
        'paid',
        5.00,
        15,
        24,
        10
    ), (
        6,
        'late',
        '2022-06-27 17:00:00',
        'paid',
        5.00,
        15,
        24,
        10
    ), (
        7,
        'late',
        '2022-06-28 17:00:00',
        'paid',
        5.00,
        15,
        24,
        10
    ), (
        8,
        'late',
        '2022-06-27 17:00:00',
        'paid',
        5.00,
        16,
        24,
        10
    ), (
        9,
        'late',
        '2022-06-28 17:00:00',
        'paid',
        5.00,
        16,
        24,
        10
    ), (
        10,
        'lost',
        '2022-06-29 04:31:04',
        'paid',
        17.00,
        15,
        24,
        10
    ), (
        11,
        'late',
        '2022-06-28 17:00:00',
        'unpaid',
        5.00,
        14,
        25,
        13
    ), (
        12,
        'late',
        '2022-06-28 17:00:00',
        'unpaid',
        5.00,
        14,
        25,
        13
    ), (
        13,
        'late',
        '2022-06-27 17:00:00',
        'unpaid',
        5.00,
        16,
        26,
        14
    ), (
        14,
        'late',
        '2022-06-28 17:00:00',
        'unpaid',
        5.00,
        16,
        26,
        14
    ), (
        15,
        'late',
        '2022-06-29 17:00:00',
        'unpaid',
        5.00,
        14,
        25,
        13
    ), (
        16,
        'late',
        '2022-06-29 17:00:00',
        'unpaid',
        5.00,
        16,
        26,
        14
    ), (
        17,
        'late',
        '2022-06-30 17:00:00',
        'unpaid',
        5.00,
        14,
        25,
        13
    ), (
        18,
        'late',
        '2022-06-30 17:00:00',
        'unpaid',
        5.00,
        16,
        26,
        14
    ), (
        19,
        'late',
        '2022-07-01 17:00:00',
        'unpaid',
        5.00,
        14,
        25,
        13
    ), (
        20,
        'late',
        '2022-07-01 17:00:00',
        'unpaid',
        5.00,
        13,
        25,
        13
    ), (
        21,
        'late',
        '2022-07-01 17:00:00',
        'unpaid',
        5.00,
        16,
        26,
        14
    ), (
        22,
        'late',
        '2022-07-02 17:00:00',
        'unpaid',
        5.00,
        14,
        25,
        13
    ), (
        23,
        'late',
        '2022-07-02 17:00:00',
        'unpaid',
        5.00,
        13,
        25,
        13
    ), (
        24,
        'late',
        '2022-07-02 17:00:00',
        'unpaid',
        5.00,
        16,
        26,
        14
    );

/*!40000 ALTER TABLE `penalties` ENABLE KEYS */

;

UNLOCK TABLES;

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

CREATE TABLE
    `purchasings` (
        `id` int NOT NULL AUTO_INCREMENT,
        `supplier_name` varchar(30) NOT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `id` (`id`)
    ) ENGINE = InnoDB AUTO_INCREMENT = 10 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Dumping data for table `purchasings`

--

LOCK TABLES `purchasings` WRITE;

/*!40000 ALTER TABLE `purchasings` DISABLE KEYS */

;

INSERT INTO `purchasings`
VALUES (8, 'Gramedia Baju sama'), (9, 'Sahabat Maju jaya');

/*!40000 ALTER TABLE `purchasings` ENABLE KEYS */

;

UNLOCK TABLES;

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

CREATE TABLE
    `purchasings_books_details` (
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
    ) ENGINE = InnoDB AUTO_INCREMENT = 6 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Dumping data for table `purchasings_books_details`

--

LOCK TABLES `purchasings_books_details` WRITE;

/*!40000 ALTER TABLE `purchasings_books_details` DISABLE KEYS */

;

INSERT INTO
    `purchasings_books_details`
VALUES (1, 15.00, 8, 12), (2, 16.00, 8, 13), (3, 18.00, 8, 14), (4, 17.00, 9, 15), (5, 19.00, 9, 16);

/*!40000 ALTER TABLE `purchasings_books_details` ENABLE KEYS */

;

UNLOCK TABLES;

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

CREATE TABLE
    `users` (
        `id` int NOT NULL AUTO_INCREMENT,
        `username` varchar(30) NOT NULL,
        `password` varchar(45) NOT NULL,
        `role` enum(
            'customer',
            'admin',
            'libarian'
        ) NOT NULL,
        `phone_number` varchar(15) DEFAULT NULL,
        `active` enum('active', 'unactive') DEFAULT NULL,
        `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (`id`),
        UNIQUE KEY `id` (`id`),
        UNIQUE KEY `username` (`username`),
        UNIQUE KEY `phone_number` (`phone_number`)
    ) ENGINE = InnoDB AUTO_INCREMENT = 27 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */

;

--

-- Dumping data for table `users`

--

LOCK TABLES `users` WRITE;

/*!40000 ALTER TABLE `users` DISABLE KEYS */

;

INSERT INTO `users`
VALUES (
        21,
        'admin',
        '25f9e794323b453885f5181f1b624d0b',
        'admin',
        NULL,
        NULL,
        '2022-06-27 12:35:18'
    ), (
        23,
        'customer 1',
        '25f9e794323b453885f5181f1b624d0b',
        'customer',
        '+6281220487500',
        'active',
        '2022-06-27 13:02:25'
    ), (
        24,
        'customer 2',
        '25f9e794323b453885f5181f1b624d0b',
        'customer',
        '+6281230477550',
        'active',
        '2022-06-27 13:02:41'
    ), (
        25,
        'customer 3',
        '25f9e794323b453885f5181f1b624d0b',
        'customer',
        '+62812204875010',
        'active',
        '2022-06-29 04:31:58'
    ), (
        26,
        'customer 4',
        'e807f1fcf82d132f9bb018ca6738a19f',
        'customer',
        '+6281220487513',
        'active',
        '2022-06-29 04:46:01'
    );

/*!40000 ALTER TABLE `users` ENABLE KEYS */

;

UNLOCK TABLES;

--

-- Dumping events for database 'athenaeum_library'

--

--

-- Dumping routines for database 'athenaeum_library'

--

/*!50003 DROP PROCEDURE IF EXISTS `checkIfSpecifiedBorrowingIsCompleted` */

;

/*!50003 SET @saved_cs_client      = @@character_set_client */

;

/*!50003 SET @saved_cs_results     = @@character_set_results */

;

/*!50003 SET @saved_col_connection = @@collation_connection */

;

/*!50003 SET character_set_client  = utf8mb4 */

;

/*!50003 SET character_set_results = utf8mb4 */

;

/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */

;

/*!50003 SET @saved_sql_mode       = @@sql_mode */

;

/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */

;

DELIMITER ;;

CREATE DEFINER =`root`@`localhost` PROCEDURE `checkIfSpecifiedBorrowingIsCompleted`
(in borrowingId INT) 
BEGIN 
	DECLARE bookBorrowedWithOngoingStatus INT DEFAULT 0;
	SET bookBorrowedWithOngoingStatus = 0;
	SELECT
	    COUNT(*) INTO bookBorrowedWithOngoingStatus
	FROM borrowed_books
	WHERE
	    borrowing_id = borrowingId
	    AND status = '
		WHERE id = borrowingId;
		E 
	E
END IF; 

END ;

;

DELIMITER ;

/*!50003 SET sql_mode              = @saved_sql_mode */

;

/*!50003 SET character_set_client  = @saved_cs_client */

;

/*!50003 SET character_set_results = @saved_cs_results */

;

/*!50003 SET collation_connection  = @saved_col_connection */

;

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

/*!50001 VIEW `customers` AS select `users`.`id` AS `id`,`users`.`username` AS `username`,`users`.`password` AS `password`,`users`.`role` AS `role`,`users`.`phone_number` AS `phone_number`,`users`.`created_at` AS `created_at`,if((sum(if((`penalties`.`payment_status` = 'unpaid'),1,0)) = 0),'not-blacklisted','blacklisted') AS `blacklisted` from (`users` join `penalties` on((`users`.`id` = `penalties`.`borrowed_book_borrowing_customer_id`))) group by `users`.`id` having (`users`.`role` = 'customer') */

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

-- Dump completed on 2022-07-04 13:47:35

INSERT INTO
    admin(
        username,
        password,
        role,
        created_at
    )
VALUES (
        'admin',
        '25f9e794323b453885f5181f1b624d0b',
        'admin',
        NOW()
    );