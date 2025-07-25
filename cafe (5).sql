-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 25, 2025 at 09:37 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cafe`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `customer_id` int(100) NOT NULL,
  `prod_id` varchar(100) NOT NULL,
  `prod_name` varchar(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `quantity` int(100) NOT NULL,
  `price` double NOT NULL,
  `date` date NOT NULL,
  `image` varchar(500) NOT NULL,
  `em_username` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id`, `customer_id`, `prod_id`, `prod_name`, `type`, `quantity`, `price`, `date`, `image`, `em_username`) VALUES
(1, 1, '7', 'rrr', 'Drinks', 1, 0, '2025-07-24', 'D:\\\\\\\\aa2.PNG', ''),
(2, 1, '8', 'jj', 'Meals', 1, 0, '2025-07-24', 'D:\\\\\\\\p1.JPG', ''),
(3, 1, '1', 's', 'Meals', 1, 0, '2025-07-24', 'D:\\\\\\\\a5.PNG', ''),
(5, 1, '3', 'rtg', 'Meals', 1, 55, '2025-07-24', 'D:\\\\\\\\aa1.PNG', ''),
(6, 1, '6', 'ttt', 'Drinks', 1, 777, '2025-07-24', 'D:\\\\\\\\aaa7.PNG', ''),
(7, 1, '6', 'ttt', 'Drinks', 2, 1554, '2025-07-24', 'D:\\\\\\\\aaa7.PNG', ''),
(8, 1, '6', 'ttt', 'Drinks', 1, 777, '2025-07-25', 'D:\\\\\\\\aaa7.PNG', ''),
(9, 1, '3', 'rtg', 'Meals', 2, 110, '2025-07-25', 'D:\\\\\\\\aa1.PNG', ''),
(10, 1, '8', 'jj', 'Meals', 2, 0, '2025-07-25', 'D:\\\\\\\\p1.JPG', ''),
(11, 2, '8', 'jj', 'Meals', 2, 0, '2025-07-25', 'D:\\\\\\\\p1.JPG', ''),
(12, 2, '3', 'rtg', 'Meals', 2, 110, '2025-07-25', 'D:\\\\\\\\aa1.PNG', ''),
(13, 2, '6', 'ttt', 'Drinks', 2, 1554, '2025-07-25', 'D:\\\\\\\\aaa7.PNG', ''),
(14, 3, '3', 'rtg', 'Meals', 1, 55, '2025-07-25', 'D:\\\\\\\\aa1.PNG', ''),
(18, 4, '3', 'rtg', 'Meals', 1, 55, '2025-07-25', 'D:\\\\\\\\aa1.PNG', ''),
(19, 5, '3', 'rtg', 'Meals', 3, 165, '2025-07-25', 'D:\\\\\\\\aa1.PNG', ''),
(20, 6, '3', 'rtg', 'Meals', 1, 55, '2025-07-25', 'D:\\\\\\\\aa1.PNG', ''),
(21, 7, '3', 'rtg', 'Meals', 1, 55, '2025-07-25', 'D:\\\\\\\\aa1.PNG', ''),
(22, 7, '3', 'rtg', 'Meals', 1, 55, '2025-07-25', 'D:\\\\\\\\aa1.PNG', ''),
(23, 8, '2', 'mango juice', 'Drinks', 2, 10, '2025-07-26', 'D:\\\\\\\\\\\\\\\\javaVersion.PNG', NULL),
(24, 9, '2', 'mango juice', 'Drinks', 2, 10, '2025-07-26', 'D:\\\\\\\\\\\\\\\\javaVersion.PNG', NULL),
(25, 10, '7', 'rrr', 'Drinks', 1, 0, '2025-07-26', 'D:\\\\\\\\aa2.PNG', NULL),
(26, 11, '2', 'mango juice', 'Drinks', 1, 5, '2025-07-26', 'D:\\\\\\\\\\\\\\\\javaVersion.PNG', NULL),
(27, 12, '2', 'mango juice', 'Drinks', 2, 10, '2025-07-26', 'D:\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\javaVersion.PNG', NULL),
(28, 13, '2', 'mango juice', 'Drinks', 1, 5, '2025-07-26', 'D:\\\\\\\\\\\\\\\\javaVersion.PNG', 'System'),
(29, 13, '2', 'mango juice', 'Drinks', 1, 5, '2025-07-26', 'D:\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\javaVersion.PNG', NULL),
(30, 13, '3', 'rtg', 'Meals', 1, 55, '2025-07-26', 'D:\\\\\\\\aa1.PNG', NULL),
(31, 13, '3', 'rtg', 'Meals', 1, 55, '2025-07-26', 'D:\\\\\\\\\\\\\\\\aa1.PNG', NULL),
(32, 14, '2', 'mango juice', 'Drinks', 1, 5, '2025-07-26', 'D:\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\javaVersion.PNG', NULL),
(33, 14, '3', 'rtg', 'Meals', 2, 110, '2025-07-26', 'D:\\\\\\\\\\\\\\\\aa1.PNG', NULL),
(34, 15, '2', 'mango juice', 'Drinks', 1, 5, '2025-07-26', 'D:\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\javaVersion.PNG', NULL),
(35, 15, '3', 'rtg', 'Meals', 2, 110, '2025-07-26', 'D:\\\\\\\\\\\\\\\\aa1.PNG', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `question` varchar(100) NOT NULL,
  `answer` varchar(100) NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`id`, `username`, `password`, `question`, `answer`, `date`) VALUES
(1, 'admin', '123', 'What is your favorite Color?', 'blue', '2025-06-28 05:15:46'),
(2, 'admin1', '123', 'What is your favorite Color?', 'blue', '2025-06-28 05:17:51'),
(3, 'tanzela', '12345678', 'What is your favorite food?', 'coffee', '2025-07-16 18:00:00'),
(4, 'jannat', '123456789', 'What is your favorite Color?', 'red', '2025-07-17 18:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `prod_id` varchar(100) NOT NULL,
  `prod_name` varchar(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `stock` int(100) NOT NULL,
  `price` double NOT NULL,
  `status` varchar(100) NOT NULL,
  `image` varchar(500) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `prod_id`, `prod_name`, `type`, `stock`, `price`, `status`, `image`, `date`) VALUES
(1, '1', 's', 'Meals', 0, 0, 'Unavailable', 'D:a5.PNG', '2025-07-18'),
(2, '23', 'fg', 'Drinks', 33, 44, 'Unavailable', 'D:\\\\aaaa8.PNG', '2025-07-18'),
(4, '8', 'jj', 'Meals', 0, 0, 'Unavailable', 'D:\\p1.JPG', '2025-07-18'),
(5, '3', 'rtg', 'Meals', 9, 55, 'Available', 'D:\\\\\\\\aa1.PNG', '2025-07-19'),
(6, '7', 'rrr', 'Drinks', 0, 0, 'Unavailable', 'D:aa2.PNG', '2025-07-20'),
(8, '4', 'asdfgha', 'Meals', 7, 15, 'Available', 'D:\\\\\\\\a7.PNG', '2025-07-26');

-- --------------------------------------------------------

--
-- Table structure for table `receipt`
--

CREATE TABLE `receipt` (
  `id` int(11) NOT NULL,
  `customer_id` int(100) NOT NULL,
  `total` double NOT NULL,
  `date` date NOT NULL,
  `em_username` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `receipt`
--

INSERT INTO `receipt` (`id`, `customer_id`, `total`, `date`, `em_username`) VALUES
(1, 1, 3273, '2025-07-25', NULL),
(2, 2, 1664, '2025-07-25', NULL),
(3, 3, 55, '2025-07-25', NULL),
(4, 4, 55, '2025-07-25', NULL),
(5, 5, 165, '2025-07-25', NULL),
(6, 6, 55, '2025-07-25', NULL),
(7, 7, 110, '2025-07-25', NULL),
(8, 13, 120, '2025-07-26', 'System'),
(9, 14, 115, '2025-07-26', 'System'),
(10, 15, 115, '2025-07-26', 'System'),
(11, 16, 15, '2025-07-26', 'System'),
(12, 17, 85, '2025-07-26', 'System'),
(13, 16, 15, '2025-07-26', 'System');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `receipt`
--
ALTER TABLE `receipt`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT for table `employee`
--
ALTER TABLE `employee`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `receipt`
--
ALTER TABLE `receipt`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
