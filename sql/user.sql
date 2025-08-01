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

 Date: 18/07/2025 21:22:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('103e4b8f2fd57be79ffc175ba3c2d66a', 'hongpan', '$2a$10$TXSa5C6zgVzYA18EfarNOOieNJe5wli/KtGTAMdgB9QH45UgyUiEy', 0, NULL);
INSERT INTO `user` VALUES ('3d0316e41ffef3733b3555452471347b', 'hp', '$2a$10$jFXRAUZkBLaUpWX99MeXVuiKjg1/kgQkpYQpt3SvAsGwJNZrns4mC', 0, '12345678901');

SET FOREIGN_KEY_CHECKS = 1;
