/*
 Navicat Premium Data Transfer

 Source Server         : 本机数据库
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : home_work

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 27/02/2021 14:24:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `province_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '省份ID',
  `city_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '城市ID',
  `area_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '县/区ID',
  `mobile` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号码',
  `address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '详细地址',
  `contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人',
  `is_default` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否是默认（0否，1默认）',
  `created_time` bigint(20) UNSIGNED NOT NULL COMMENT '创建时间',
  `updated_time` bigint(20) UNSIGNED NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for areas
-- ----------------------------
DROP TABLE IF EXISTS `areas`;
CREATE TABLE `areas`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `area_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域ID',
  `area` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域名称',
  `city_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '城市ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10000 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '行政区域县区信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cities
-- ----------------------------
DROP TABLE IF EXISTS `cities`;
CREATE TABLE `cities`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `city_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '城市ID',
  `city` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '城市名称',
  `province_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '省份ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 346 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '行政区域地州市信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` bigint(20) UNSIGNED NOT NULL DEFAULT 1 AUTO_INCREMENT COMMENT '主键ID',
  `goods_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品描述',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格, 单位为元',
  `small_pic` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '小图',
  `detail_pic` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '详情图片',
  `stock_count` int(10) UNSIGNED NOT NULL COMMENT '剩余库存',
  `status` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '商品状态，1-正常，2-下架，3-删除',
  `created_time` bigint(20) UNSIGNED NOT NULL COMMENT '创建时间',
  `updated_time` bigint(20) UNSIGNED NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 149187842867981 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` bigint(20) NOT NULL COMMENT '订单id',
  `payment` decimal(20, 2) NOT NULL COMMENT '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `payment_type` tinyint(1) NOT NULL COMMENT '支付类型，1、在线支付，2、货到付款',
  `status` tinyint(1) NOT NULL COMMENT '状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价',
  `payment_time` bigint(20) NOT NULL COMMENT '付款时间',
  `consign_time` bigint(20) NOT NULL COMMENT '发货时间',
  `end_time` bigint(20) NOT NULL COMMENT '交易完成时间',
  `close_time` bigint(20) NOT NULL COMMENT '交易关闭时间',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `address_id` bigint(20) NOT NULL COMMENT '地址id',
  `create_time` bigint(20) NOT NULL COMMENT '订单创建时间',
  `update_time` bigint(20) NOT NULL COMMENT '订单更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `payment_type`(`payment_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_goods_relation
-- ----------------------------
DROP TABLE IF EXISTS `order_goods_relation`;
CREATE TABLE `order_goods_relation`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单ID',
  `goods_id` bigint(20) UNSIGNED NOT NULL COMMENT '商品ID',
  `shipping_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '物流名称',
  `shipping_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '物流单号',
  `created_time` bigint(20) UNSIGNED NOT NULL COMMENT '创建时间',
  `updated_time` bigint(20) UNSIGNED NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for provinces
-- ----------------------------
DROP TABLE IF EXISTS `provinces`;
CREATE TABLE `provinces`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `province_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '省份ID',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '省份名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '省份信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码，加密存储',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `birthday` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '生日',
  `sex` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别，0男，1女',
  `head_pic` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像地址',
  `phone` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '注册手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '注册邮箱',
  `status` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '使用状态（0非正常 1正常）',
  `is_mobile_check` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '手机是否验证 （0否  1是）',
  `is_email_check` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '邮箱是否检测（0否  1是）',
  `created_time` bigint(20) UNSIGNED NOT NULL COMMENT '创建时间',
  `updated_time` bigint(20) UNSIGNED NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
