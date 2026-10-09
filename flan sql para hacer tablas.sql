-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Dec 04, 2025 at 03:49 AM
-- Server version: 11.8.3-MariaDB-log
-- PHP Version: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `foroflan`
--

CREATE IF NOT EXISTS DATABASE foroflan;
USE foroflan;

CREATE USER 'forumuser'@'localhost' IDENTIFIED BY 'iloveflan';
GRANT ALL PRIVILEGES ON foroflan.* TO 'forumuser'@'localhost';

-- --------------------------------------------------------

--
-- Table structure for table `NOTIFICATION`
--

CREATE TABLE `NOTIFICATION` (
  `notification_ID` int(11) NOT NULL,
  `is_read` tinyint(1) DEFAULT 0,
  `title` varchar(200) NOT NULL,
  `body` text NOT NULL,
  `user_ID` int(11) NOT NULL,
  `post_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `NOTIFICATION`
--

INSERT INTO `NOTIFICATION` (`notification_ID`, `is_read`, `title`, `body`, `user_ID`, `post_ID`) VALUES
(1, 1, 'se hizo el primer post', '', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `POST`
--

CREATE TABLE `POST` (
  `post_ID` int(11) NOT NULL,
  `title` varchar(200) NOT NULL,
  `body` text NOT NULL,
  `post_date` timestamp NULL DEFAULT current_timestamp(),
  `edit` tinyint(1) DEFAULT 0,
  `private` tinyint(1) DEFAULT 0,
  `user_ID` int(11) NOT NULL,
  `post_parent_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `POST`
--

INSERT INTO `POST` (`post_ID`, `title`, `body`, `post_date`, `edit`, `private`, `user_ID`, `post_parent_ID`) VALUES
(1, 'primer post', 'post de prueba', '2025-11-29 01:34:36', 0, 0, 1, NULL),
(2, 'post privado de la libertad', 'post para zionistas de corazon', '2025-11-30 02:46:05', 0, 1, 2, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `USURATO`
--

CREATE TABLE `USURATO` (
  `user_ID` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `bio` text DEFAULT NULL,
  `profile_picture` varchar(30) DEFAULT 'pfp/bepis',
  `password` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `USURATO`
--

INSERT INTO `USURATO` (`user_ID`, `username`, `bio`, `password`) VALUES
(1, 'wagoogus', 'ツツツツツ', 'p0ssw4rd'),
(2, 'shampoo', 'Goooooooooggggg', 'zion');

-- --------------------------------------------------------

--
-- Table structure for table `VISIBILITY`
--

CREATE TABLE `VISIBILITY` (
  `user_ID` int(11) NOT NULL,
  `post_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `VISIBILITY`
--

INSERT INTO `VISIBILITY` (`user_ID`, `post_ID`) VALUES
(1, 2),
(2, 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `NOTIFICATION`
--
ALTER TABLE `NOTIFICATION`
  ADD PRIMARY KEY (`notification_ID`),
  ADD KEY `post_ID` (`post_ID`),
  ADD KEY `idx_notification_user` (`user_ID`);

--
-- Indexes for table `POST`
--
ALTER TABLE `POST`
  ADD PRIMARY KEY (`post_ID`),
  ADD KEY `idx_post_user` (`user_ID`),
  ADD KEY `idx_post_parent` (`post_parent_ID`);

--
-- Indexes for table `USURATO`
--
ALTER TABLE `USURATO`
  ADD PRIMARY KEY (`user_ID`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `VISIBILITY`
--
ALTER TABLE `VISIBILITY`
  ADD PRIMARY KEY (`user_ID`,`post_ID`),
  ADD KEY `idx_visibility_user` (`user_ID`),
  ADD KEY `idx_visibility_post` (`post_ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `NOTIFICATION`
--
ALTER TABLE `NOTIFICATION`
  MODIFY `notification_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `POST`
--
ALTER TABLE `POST`
  MODIFY `post_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `USURATO`
--
ALTER TABLE `USURATO`
  MODIFY `user_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `NOTIFICATION`
--
ALTER TABLE `NOTIFICATION`
  ADD CONSTRAINT `NOTIFICATION_ibfk_1` FOREIGN KEY (`user_ID`) REFERENCES `USURATO` (`user_ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `NOTIFICATION_ibfk_2` FOREIGN KEY (`post_ID`) REFERENCES `POST` (`post_ID`) ON DELETE CASCADE;

--
-- Constraints for table `POST`
--
ALTER TABLE `POST`
  ADD CONSTRAINT `POST_ibfk_1` FOREIGN KEY (`user_ID`) REFERENCES `USURATO` (`user_ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_post_parent` FOREIGN KEY (`post_parent_ID`) REFERENCES `POST` (`post_ID`) ON DELETE CASCADE;

--
-- Constraints for table `VISIBILITY`
--
ALTER TABLE `VISIBILITY`
  ADD CONSTRAINT `VISIBILITY_ibfk_1` FOREIGN KEY (`user_ID`) REFERENCES `USURATO` (`user_ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `VISIBILITY_ibfk_2` FOREIGN KEY (`post_ID`) REFERENCES `POST` (`post_ID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
