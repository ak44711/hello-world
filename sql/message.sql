/*
 Navicat Premium Data Transfer

 Source Server         : hp-project
 Source Server Type    : MySQL
 Source Server Version : 50743
 Source Host           : 47.116.161.116:3306
 Source Schema         : helloworld

 Target Server Type    : MySQL
 Target Server Version : 50743
 File Encoding         : 65001

 Date: 18/07/2025 21:22:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` bigint(20) NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
