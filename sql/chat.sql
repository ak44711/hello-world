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

 Date: 18/07/2025 21:22:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `conversation_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role` enum('system','user') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `message_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `message_id`(`message_id`) USING BTREE,
  CONSTRAINT `chat_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `chat_ibfk_2` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
