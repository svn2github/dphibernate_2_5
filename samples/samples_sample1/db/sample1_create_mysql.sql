-- ----------------------------------------------------------------------
-- MySQL Migration Toolkit
-- SQL Create Script
-- ----------------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `test_hibernateAdapter_dbo`
  CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `test_hibernateAdapter_dbo`;
-- -------------------------------------
-- Tables

DROP TABLE IF EXISTS `test_hibernateAdapter_dbo`.`UserAddressLinkTable`;
CREATE TABLE `test_hibernateAdapter_dbo`.`UserAddressLinkTable` (
  `userId` VARCHAR(50) NOT NULL,
  `addressId` VARCHAR(50) NOT NULL,
  CONSTRAINT `FK_UserAddressLinkTable_Users` FOREIGN KEY `FK_UserAddressLinkTable_Users` (`userId`)
    REFERENCES `test_hibernateAdapter_dbo`.`Users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_UserAddressLinkTable_UserAddress` FOREIGN KEY `FK_UserAddressLinkTable_UserAddress` (`addressId`)
    REFERENCES `test_hibernateAdapter_dbo`.`UserAddress` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `test_hibernateAdapter_dbo`.`UserWishlist`;
CREATE TABLE `test_hibernateAdapter_dbo`.`UserWishlist` (
  `id` VARCHAR(50) NOT NULL DEFAULT newid(),
  `type` VARCHAR(50) NOT NULL,
  `name` VARCHAR(50) NULL,
  `qty` INT(10) NULL DEFAULT 1,
  `userid` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_UserWishlist_Users` FOREIGN KEY `FK_UserWishlist_Users` (`userid`)
    REFERENCES `test_hibernateAdapter_dbo`.`Users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `test_hibernateAdapter_dbo`.`Users`;
CREATE TABLE `test_hibernateAdapter_dbo`.`Users` (
  `id` VARCHAR(50) NOT NULL,
  `firstName` VARCHAR(50) NULL,
  `lastName` VARCHAR(50) NULL,
  `connectInfoId` VARCHAR(50) NULL,
  `addressId` VARCHAR(50) NULL,
  PRIMARY KEY (`id`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `test_hibernateAdapter_dbo`.`UserConnectInfo`;
CREATE TABLE `test_hibernateAdapter_dbo`.`UserConnectInfo` (
  `userid` VARCHAR(50) NULL,
  `email` VARCHAR(255) NULL,
  `yahooIM` VARCHAR(255) NULL,
  `aolIM` VARCHAR(255) NULL,
  `id` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_UserConnectInfo_Users` FOREIGN KEY `FK_UserConnectInfo_Users` (`userid`)
    REFERENCES `test_hibernateAdapter_dbo`.`Users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `test_hibernateAdapter_dbo`.`UserAddress`;
CREATE TABLE `test_hibernateAdapter_dbo`.`UserAddress` (
  `userid` VARCHAR(50) NULL,
  `address1` VARCHAR(255) NULL,
  `address2` VARCHAR(255) NULL,
  `city` VARCHAR(255) NULL,
  `state` VARCHAR(255) NULL,
  `zip` VARCHAR(255) NULL,
  `id` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_UserAddress_Users` FOREIGN KEY `FK_UserAddress_Users` (`userid`)
    REFERENCES `test_hibernateAdapter_dbo`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;



SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------------------------------------------------
-- EOF

