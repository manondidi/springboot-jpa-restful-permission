/*
 Navicat Premium Data Transfer

 Source Server         : centos-docker-mysql
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : 192.168.56.101:3306
 Source Schema         : springboot-mvc-jpa

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 17/12/2019 12:58:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence`  (
  `next_val` bigint(20) NULL DEFAULT NULL
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Fixed;

-- ----------------------------
-- Records of hibernate_sequence
-- ----------------------------
INSERT INTO `hibernate_sequence` VALUES (23);
INSERT INTO `hibernate_sequence` VALUES (1);
INSERT INTO `hibernate_sequence` VALUES (1);
INSERT INTO `hibernate_sequence` VALUES (1);

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`  (
  `id` bigint(20) NOT NULL,
  `available` bit(1) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `parent_id` bigint(20) NULL DEFAULT NULL,
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKjpo0wq99a03xl6oxm1gcwmbol`(`parent_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES (1, b'1', '角色查看', NULL, 'role:view');
INSERT INTO `t_permission` VALUES (2, b'1', '权限查看', NULL, 'permission:view');
INSERT INTO `t_permission` VALUES (3, b'1', '角色添加', 1, 'role:add');
INSERT INTO `t_permission` VALUES (4, b'1', '角色编辑', 1, 'role:update');
INSERT INTO `t_permission` VALUES (5, b'1', '角色删除', 1, 'role:delete');
INSERT INTO `t_permission` VALUES (6, b'1', '权限添加', 2, 'permission:add');
INSERT INTO `t_permission` VALUES (7, b'1', '权限编辑', 2, 'permission:update');
INSERT INTO `t_permission` VALUES (8, b'1', '权限删除', 2, 'permission:delete');
INSERT INTO `t_permission` VALUES (9, b'1', '用户查看', NULL, 'user:view');
INSERT INTO `t_permission` VALUES (10, b'1', '用户封禁', 9, 'user:ban');
INSERT INTO `t_permission` VALUES (11, b'1', '用户解封', 9, 'user:unban');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` bigint(20) NOT NULL,
  `available` bit(1) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (11, b'1', 'desc', 'role11');
INSERT INTO `t_role` VALUES (16, b'1', 'desc', 'Role6');
INSERT INTO `t_role` VALUES (8, b'1', 'desc', 'Role2');
INSERT INTO `t_role` VALUES (7, b'1', 'desc', 'Role1');
INSERT INTO `t_role` VALUES (18, b'1', 'desc', 'Role6');
INSERT INTO `t_role` VALUES (17, b'1', 'desc', 'Role6');
INSERT INTO `t_role` VALUES (15, b'1', 'desc', 'Role6');
INSERT INTO `t_role` VALUES (1, b'1', '超级管理员', 'super_admin');
INSERT INTO `t_role` VALUES (22, b'1', 'desc', 'Role6');

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission`  (
  `permission_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  INDEX `FKjobmrl6dorhlfite4u34hciik`(`permission_id`) USING BTREE,
  INDEX `FK90j038mnbnthgkc17mqnoilu9`(`role_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Fixed;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission` VALUES (5, 11);
INSERT INTO `t_role_permission` VALUES (3, 11);
INSERT INTO `t_role_permission` VALUES (11, 1);
INSERT INTO `t_role_permission` VALUES (1, 18);
INSERT INTO `t_role_permission` VALUES (10, 1);
INSERT INTO `t_role_permission` VALUES (9, 1);
INSERT INTO `t_role_permission` VALUES (8, 1);
INSERT INTO `t_role_permission` VALUES (7, 1);
INSERT INTO `t_role_permission` VALUES (6, 1);
INSERT INTO `t_role_permission` VALUES (5, 1);
INSERT INTO `t_role_permission` VALUES (4, 1);
INSERT INTO `t_role_permission` VALUES (3, 1);
INSERT INTO `t_role_permission` VALUES (2, 1);
INSERT INTO `t_role_permission` VALUES (1, 22);
INSERT INTO `t_role_permission` VALUES (2, 22);
INSERT INTO `t_role_permission` VALUES (1, 1);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) NOT NULL,
  `mail` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `tel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `ban_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `ban_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_28ky8wn6p9tvfx9cryjxcyd26`(`mail`) USING BTREE,
  UNIQUE INDEX `UK_b5q5wl2w4kiod1gyimirwmfse`(`user_name`) USING BTREE,
  UNIQUE INDEX `UK_3hpr52cenkaqqwqkiuohx9js4`(`tel`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '790512128@qq.com', '1234567', 'manondidi', NULL, '18050400657', NULL, NULL);
INSERT INTO `t_user` VALUES (2, 'czq@163.com', '123456', 'czq', NULL, '13290749569', 'reason', '2019-12-02 10:18:08');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  INDEX `FKa9c8iiy6ut0gnx491fqx4pxam`(`role_id`) USING BTREE,
  INDEX `FKq5un6x7ecoef5w1n39cop66kl`(`user_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Fixed;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1, 1);
INSERT INTO `t_user_role` VALUES (1, 11);

SET FOREIGN_KEY_CHECKS = 1;
