-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 22, 2025 at 09:47 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

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
(1, 1, '1', 's', 'Meals', 1, 0, '2025-07-22', 'D:\\\\\\\\a5.PNG', NULL),
(2, 1, '8', 'jj', 'Meals', 2, 0, '2025-07-22', 'D:\\\\\\\\p1.JPG', NULL),
(3, 1, '3', 'rtg', 'Meals', 1, 0, '2025-07-22', 'D:\\\\\\\\aa1.PNG', NULL),
(4, 1, '2', 'gg', 'Meals', 2, 0, '2025-07-22', 'C:\\\\\\\\Users\\\\\\\\Youtech BD\\\\\\\\OneDrive\\\\\\\\Pictures\\\\\\\\pale red.jpg', NULL),
(5, 1, '7', 'rrr', 'Drinks', 2, 0, '2025-07-22', 'D:\\\\\\\\aa2.PNG', NULL),
(6, 1, '7', 'rrr', 'Drinks', 1, 0, '2025-07-22', 'D:\\\\\\\\\\\\\\\\aa2.PNG', NULL),
(7, 1, '11', 'www', 'Meals', 1, 0, '2025-07-22', 'C:\\\\\\\\Users\\\\\\\\Youtech BD\\\\\\\\OneDrive\\\\\\\\Pictures\\\\\\\\pale red.jpg', NULL),
(8, 1, '12', 'aaa', 'Drinks', 2, 0, '2025-07-22', 'C:\\\\\\\\Users\\\\\\\\Youtech BD\\\\\\\\OneDrive\\\\\\\\Pictures\\\\\\\\pale red.jpg', NULL);

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
(1, '1', 's', 'Meals', 0, 0, 'Available', 'D:\\\\a5.PNG', '2025-07-18'),
(2, '23', 'fg', 'Drinks', 33, 44, 'Unavailable', 'D:\\\\aaaa8.PNG', '2025-07-18'),
(3, '6', 'ttt', 'Drinks', 6, 777, 'Available', 'D:\\\\aaa7.PNG', '2025-07-18'),
(4, '8', 'jj', 'Meals', 4, 0, 'Available', 'D:\\\\p1.JPG', '2025-07-18'),
(5, '3', 'rtg', 'Meals', 32, 0, 'Available', 'D:\\\\aa1.PNG', '2025-07-19'),
(6, '7', 'rrr', 'Drinks', 0, 0, 'Available', 'D:\\\\\\\\aa2.PNG', '2025-07-20'),
(7, '2', 'gg', 'Meals', 64, 0, 'Available', 'C:\\\\Users\\\\Youtech BD\\\\OneDrive\\\\Pictures\\\\pale red.jpg', '2025-07-22'),
(8, '11', 'www', 'Meals', 443, 0, 'Available', 'C:\\\\Users\\\\Youtech BD\\\\OneDrive\\\\Pictures\\\\pale red.jpg', '2025-07-22'),
(9, '12', 'aaa', 'Drinks', 98, 0, 'Available', 'C:\\\\Users\\\\Youtech BD\\\\OneDrive\\\\Pictures\\\\pale red.jpg', '2025-07-22');

-- --------------------------------------------------------

--
-- Table structure for table `receipt`
--

CREATE TABLE `receipt` (
  `id` int(11) DEFAULT NULL,
  `customer_id` int(100) NOT NULL,
  `total` double NOT NULL,
  `date` date DEFAULT NULL,
  `em_username` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `employee`
--
ALTER TABLE `employee`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
