/*
Navicat MySQL Data Transfer

Source Server         : mysql57
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : blog

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2020-08-07 21:20:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for articles
-- ----------------------------
DROP TABLE IF EXISTS `articles`;
CREATE TABLE `articles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(30) NOT NULL,
  `content` longtext NOT NULL,
  `create_time` datetime NOT NULL,
  `description` varchar(255) NOT NULL,
  `last_edit_time` datetime NOT NULL,
  `top` bit(1) NOT NULL DEFAULT b'0',
  `type` varchar(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  `view_count` int(11) NOT NULL DEFAULT '0',
  `picture_url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `articles_ibfk_1` (`user_id`),
  CONSTRAINT `articles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `profiles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for article_labels
-- ----------------------------
DROP TABLE IF EXISTS `article_labels`;
CREATE TABLE `article_labels` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL,
  `label_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `article_id` (`article_id`),
  KEY `label_id` (`label_id`),
  CONSTRAINT `article_labels_ibfk_1` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `article_labels_ibfk_2` FOREIGN KEY (`label_id`) REFERENCES `labels` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `content` varchar(255) NOT NULL,
  `left_time` datetime NOT NULL,
  `article_id` int(11) NOT NULL,
  `parent_id` int(11) DEFAULT '0',
  `nickname` varchar(20) DEFAULT NULL,
  `reply_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `article_id` (`article_id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for github_token
-- ----------------------------
DROP TABLE IF EXISTS `github_token`;
CREATE TABLE `github_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(255) NOT NULL,
  `repository_url` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `open_id` (`open_id`) USING BTREE,
  KEY `user_id` (`user_id`),
  CONSTRAINT `github_token_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `profiles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for labels
-- ----------------------------
DROP TABLE IF EXISTS `labels`;
CREATE TABLE `labels` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label_name` varchar(20) NOT NULL,
  `label_color` varchar(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `labels_ibfk_1` (`user_id`),
  CONSTRAINT `labels_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `profiles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for localauth
-- ----------------------------
DROP TABLE IF EXISTS `localauth`;
CREATE TABLE `localauth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `email` varchar(32) NOT NULL,
  `password` varchar(50) NOT NULL,
  `user_id` int(11) NOT NULL,
  `salt` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `localauth_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `profiles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission` varchar(30) NOT NULL,
  `permission_name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for profiles
-- ----------------------------
DROP TABLE IF EXISTS `profiles`;
CREATE TABLE `profiles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(100) NOT NULL,
  `slogan` varchar(100) DEFAULT NULL,
  `background` varchar(100) NOT NULL,
  `create_time` datetime NOT NULL,
  `nickname` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for qq_token
-- ----------------------------
DROP TABLE IF EXISTS `qq_token`;
CREATE TABLE `qq_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(50) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `open_id` (`open_id`),
  KEY `user_id` (`user_id`) USING BTREE,
  CONSTRAINT `qq_token_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `profiles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user_permissions
-- ----------------------------
DROP TABLE IF EXISTS `user_permissions`;
CREATE TABLE `user_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `permission_id` (`permission_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_permissions_ibfk_1` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_permissions_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `profiles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for visit_count
-- ----------------------------
DROP TABLE IF EXISTS `visit_count`;
CREATE TABLE `visit_count` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `today` varchar(25) NOT NULL,
  `count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for visit_history
-- ----------------------------
DROP TABLE IF EXISTS `visit_history`;
CREATE TABLE `visit_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `request_url` varchar(255) NOT NULL,
  `classMethod` varchar(255) NOT NULL,
  `ip` varchar(50) NOT NULL,
  `visit_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


INSERT INTO `permissions` VALUES ('1', 'query', '具有查询权限');
INSERT INTO `permissions` VALUES ('2', 'update', '具有更新权限');
INSERT INTO `permissions` VALUES ('3', 'delete', '具有删除权限');
INSERT INTO `permissions` VALUES ('4', 'add', '具有添加权限');
INSERT INTO `permissions` VALUES ('5', 'manage', '管理整站权限');

INSERT INTO `profiles` VALUES ('1', '/images/default-avatar.svg', '', '/images/default-bg.jpg', '2020-08-04 19:15:16', '管理员');

INSERT INTO `localauth` VALUES ('1', 'admin', 'admin@qq.com', 'YW4jvNIl8KwF8fOubvFoAHiIqCOObbb8FQTxdtLISF4=', '1', '24b6db90-df88-4ee6-9c82-28e7a1c77b1a');

INSERT INTO `user_permissions` VALUES ('1', '1', '1');
INSERT INTO `user_permissions` VALUES ('2', '2', '1');
INSERT INTO `user_permissions` VALUES ('3', '3', '1');
INSERT INTO `user_permissions` VALUES ('4', '4', '1');
INSERT INTO `user_permissions` VALUES ('5', '5', '1');
