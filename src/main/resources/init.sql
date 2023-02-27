    create table t_course_notice (
    id serial primary key,
    sender_id integer not null,
    sender_type integer not null,
    receiver_id integer not null,
    receiver_type integer not null,
    notice_content varchar not null,
    notice_type integer not null,
    create_time timestamp not null,
    course_id integer not null default 0,
    scene_id integer not null default 0,
    exercise_id integer not null default 0
    );

create table t_course_notice_user (
id serial primary key,
course_notice_id integer not null,
user_id integer not null
);

---班级表---
create table t_class (
id serial primary key,
class_name varchar not null,
class_desc varchar,
class_start varchar,
class_end varchar,
course_id integer not null,
creator integer not null,
delete_time timestamp,
delete_flag integer not null default 0,
class_is_open integer not null default 0
);
---班级学生表---
create table t_class_stu (
id serial primary key,
sclass_id integer not null,
user_id integer not null
);
--课程表--
create table t_course (
id serial primary key,
course_name varchar not null,
course_desc varchar,
course_outline varchar,
knowledge_tree text,
creator integer,
create_time timestamp,
is_open integer not null default 0,
cover_image varchar not null,
delete_time timestamp,
delete_flag integer not null default 0,
parent_id integer not null default 0,
is_finish integer not null default 0
);
--课程知识点表--
create table t_course_knowledge (
id serial primary key,
parent_id integer not null default 0,
course_id integer not null,
name varchar not null,
keyword varchar,
knowledge_desc text
);
--历史习题表
DROP TABLE IF EXISTS  "t_exercise";
CREATE TABLE t_exercise (
	id serial primary key,
	course_id int4 NOT NULL,
	scene_id int4 NULL,
	exercise_name varchar NOT NULL,
	exercise_desc text NULL,
	answer text NOT NULL,
	exercise_analysis text NULL,
	delete_time timestamp NULL,
	delete_flag int4 NOT NULL DEFAULT 0,
	parent_id int4 NOT NULL DEFAULT 0,
	creator int4 NULL,
	create_time timestamp NULL,
	update_time timestamp NULL
)
WITH (
	OIDS=FALSE
) ;

--习题表--
DROP TABLE IF EXISTS "t_new_exercise";
create table t_new_exercise (
id serial primary key ,
course_id integer not null,
parent_id integer not null default 0,
scene_id integer,
scene_name varchar(200),
element_type integer not null,
exercise_name varchar not null,
exercise_desc text,
auth_type integer default 2,
exercise_type integer,
exercise_level integer default 1,
stem text,
standard_answser text,
answer text ,
exercise_analysis text null,
sort_num integer DEFAULT 0,
exercise_status integer DEFAULT 0,
show_answer integer DEFAULT 0,
create_user integer,
create_time timestamp(6),
update_time timestamp(6),
update_user integer,
delete_flag integer DEFAULT 0,
delete_time timestamp(6),
delete_user integer
);
COMMENT ON COLUMN "t_new_exercise"."course_id" IS '课程id';
COMMENT ON COLUMN "t_new_exercise"."parent_id" IS '父类id';
COMMENT ON COLUMN "t_new_exercise"."scene_id" IS '场景id';
COMMENT ON COLUMN "t_new_exercise"."scene_name" IS '场景名称';
COMMENT ON COLUMN "t_new_exercise"."element_type" IS '0:试题，1:文件夹';
COMMENT ON COLUMN "t_new_exercise"."exercise_name" IS '习题名称';
COMMENT ON COLUMN "t_new_exercise"."exercise_desc" IS '习题描述';
COMMENT ON COLUMN "t_new_exercise"."auth_type" IS '1:私有，2:共享';
COMMENT ON COLUMN "t_new_exercise"."exercise_type" IS '试题类型 1：单选2：多选3：判断4：填空5：简答6：数据库题';
COMMENT ON COLUMN "t_new_exercise"."exercise_level" IS '试题难度 1：简单 2：一般3：困难';
COMMENT ON COLUMN "t_new_exercise"."stem" IS '题干';
COMMENT ON COLUMN "t_new_exercise"."standard_answser" IS '选择题为prefix，多选逗号隔开。判断题答案只有true false,简答程序题答具体案描';
COMMENT ON COLUMN "t_new_exercise"."answer" IS '数据库答案';
COMMENT ON COLUMN "t_new_exercise"."exercise_analysis" IS '答案解析';
COMMENT ON COLUMN "t_new_exercise"."sort_num" IS '序号';
COMMENT ON COLUMN "t_new_exercise"."exercise_status" IS '练习题状态 0：是练习题 1：非练习题';
COMMENT ON COLUMN "t_new_exercise"."show_answer" IS '是否显示答案 0：显示答案 1：不显示答案';
COMMENT ON COLUMN "t_new_exercise"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_new_exercise"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_new_exercise"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_new_exercise"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_new_exercise"."delete_flag" IS '删除标志0：未删除1：已删除';
COMMENT ON COLUMN "t_new_exercise"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_new_exercise"."delete_user" IS '删除人员';
--习题明细表--
create table t_exercise_info (
id serial primary key,
exercise_id integer not null,
prefix varchar,
content text,
create_user int4,
create_time timestamp(6),
update_time timestamp(6),
update_user int4,
delete_flag int4 DEFAULT 0,
delete_time timestamp(6),
delete_user int4
);
COMMENT ON COLUMN "t_exercise_info"."exercise_id" IS '试题id';
COMMENT ON COLUMN "t_exercise_info"."prefix" IS '选项前缀';
COMMENT ON COLUMN "t_exercise_info"."content" IS '选项内容';
COMMENT ON COLUMN "t_exercise_info"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_exercise_info"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_exercise_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_exercise_info"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_exercise_info"."delete_flag" IS '删除标志0：未删除1：已删除';
COMMENT ON COLUMN "t_exercise_info"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_exercise_info"."delete_user" IS '删除人员';
--习题知识点表--
create table t_exercise_knowledge (
id serial primary key,
exercise_id integer not null,
knowledge_id integer not null,
course_id integer not null
);
--通知表--
create table t_notice (
id serial primary key,
user_id integer not null,
create_time timestamp not null,
notice_content varchar not null,
role_type integer not null
);
--通知关联表---
create table t_notice_user (
id serial primary key,
user_id integer not null,
notice_id integer not null
);
--场景表--
create table t_scene (
id serial primary key,
course_id integer not null,
scene_name varchar not null,
scene_desc text,
init_shell text,
delete_time timestamp,
delete_flag integer not null default 0,
parent_id integer not null default 0
);
--场景详情表---
create table t_scene_detail (
id serial primary key,
scene_id integer not null,
table_name varchar not null,
table_detail text not null,
table_desc varchar
);
--成绩表--
create table t_score (
id serial primary key,
class_id integer not null,
exercise_id integer not null,
user_id integer not null,
create_time varchar null,
usage_time varchar null,
answer varchar null,
score integer null,
answer_execute_time integer not null default 0
);
--功能模块表--
create table t_sys_resource (
id serial primary key,
parent_id integer not null default 0,
resource_name varchar not null,
resource_desc varchar,
resource_key varchar not null,
resource_type integer not null default 0,
resource_path varchar,
is_delete integer not null default 0,
icon varchar,
sort integer not null
);
insert into t_sys_resource (id,parent_id, resource_name, resource_desc, resource_key, resource_type, resource_path, is_delete, icon, sort) values (1,0, '首页', '首页模块', '首页', 0, '/home', 0, 'icon-shouye', 1);
insert into t_sys_resource (id,parent_id, resource_name, resource_desc, resource_key, resource_type, resource_path, is_delete, icon, sort) values (2,0, '课程', '学生课程列表', '学生课程', 0, '/stu/course', 0, 'icon-shouye', 2);
insert into t_sys_resource (id,parent_id, resource_name, resource_desc, resource_key, resource_type, resource_path, is_delete, icon, sort) values (3,0, '课程', '教师课程列表', '教师课程', 0, '/teacher/list', 0, 'icon-shouye', 3);
insert into t_sys_resource (id,parent_id, resource_name, resource_desc, resource_key, resource_type, resource_path, is_delete, icon, sort) values (4,0, '班级', '教师班级列表', '教师班级', 0, '/teacher/sclassList', 0, 'icon-shouye', 4);
insert into t_sys_resource (id,parent_id, resource_name, resource_desc, resource_key, resource_type, resource_path, is_delete, icon, sort) values (5,0, '课程模板', '课程专家课程列表', '课程专家课程', 0, '/expert/list', 0, 'icon-shouye', 5);
insert into t_sys_resource (id,parent_id, resource_name, resource_desc, resource_key, resource_type, resource_path, is_delete, icon, sort) values (6,0, '公共库', '公共库', '公共库', 0, '/expert/public_library/scene', 1, 'icon-shouye', 5);
insert into t_sys_resource (id,parent_id, resource_name, resource_desc, resource_key, resource_type, resource_path, is_delete, icon, sort) values (10,0, '个人设置', '个人设置模块', '个人设置', 0, '/center', 0, 'icon-shouye', 6);
insert into t_sys_resource (id,parent_id, resource_name, resource_desc, resource_key, resource_type, resource_path, is_delete, icon, sort) values (20,0, '系统设置', '系统设置模块', '系统设置', 0, '/sys', 0, 'icon-shouye', 7);
insert into t_sys_resource (id,parent_id, resource_name, resource_desc, resource_key, resource_type, resource_path, is_delete, icon, sort) values (21,20, '模块管理', '模块管理模块', '模块管理', 0, '/sys/module', 0, 'icon-shouye', 8);
insert into t_sys_resource (id,parent_id, resource_name, resource_desc, resource_key, resource_type, resource_path, is_delete, icon, sort) values (22,20, '角色管理', '角色管理模块', '角色管理', 0, '/sys/role', 0, 'icon-shouye', 9);
insert into t_sys_resource (id,parent_id, resource_name, resource_desc, resource_key, resource_type, resource_path, is_delete, icon, sort) values (23,20, '用户管理', '用户管理模块', '用户管理', 0, '/sys/user', 0, 'icon-shouye', 10);
insert into t_sys_resource (id,parent_id, resource_name, resource_desc, resource_key, resource_type, resource_path, is_delete, icon, sort) values (24,0, '意见反馈', '意见反馈模块', '意见反馈', 0, '/feedback/list', 0, 'icon-shouye', 11);
alter sequence t_sys_resource_id_seq restart with 30;

--角色表--
create table t_sys_role (
id serial primary key,
role_name varchar not null,
role_desc varchar,
is_predefined integer not null default 0,
creator integer,
create_time timestamp,
operator integer,
update_time timestamp,
is_delete integer not null default 0
);
insert into t_sys_role (role_name, role_desc, is_predefined, creator, create_time, is_delete) values ('管理员', '系统管理员角色描述', 1, 0, '2021-04-21 18:00:00', 0);
insert into t_sys_role (role_name, role_desc, is_predefined, creator, create_time, is_delete) values ('课程专家', '课程专家角色描述', 1, 0, '2021-04-21 18:00:00', 0);
insert into t_sys_role (role_name, role_desc, is_predefined, creator, create_time, is_delete) values ('教师', '教师角色描述', 1, 0, '2021-04-21 18:00:00', 0);
insert into t_sys_role (role_name, role_desc, is_predefined, creator, create_time, is_delete) values ('学生', '学生角色描述', 1, 0, '2021-04-21 18:00:00', 0);

--模块角色关联表--
create table t_sys_role_resource (
id serial primary key,
role_id integer not null,
resource_id integer not null
);
insert into t_sys_role_resource (role_id, resource_id) values (1, 1);--首页--
insert into t_sys_role_resource (role_id, resource_id) values (1, 2);--学生课程--
insert into t_sys_role_resource (role_id, resource_id) values (1, 3);--教师课程--
insert into t_sys_role_resource (role_id, resource_id) values (1, 4);--班级列表--
insert into t_sys_role_resource (role_id, resource_id) values (1, 5);--课程专家课程--
insert into t_sys_role_resource (role_id, resource_id) values (1, 10);--个人中心--
insert into t_sys_role_resource (role_id, resource_id) values (1, 20);--系统设置--
insert into t_sys_role_resource (role_id, resource_id) values (1, 21);--模块管理--
insert into t_sys_role_resource (role_id, resource_id) values (1, 22);--角色管理--
insert into t_sys_role_resource (role_id, resource_id) values (1, 23);--用户管理--
insert into t_sys_role_resource (role_id, resource_id) values (1, 24);--意见反馈--

insert into t_sys_role_resource (role_id, resource_id) values (2, 1);--首页--
insert into t_sys_role_resource (role_id, resource_id) values (2, 5);--教师课程--
insert into t_sys_role_resource (role_id, resource_id) values (2, 10);--个人中心--

insert into t_sys_role_resource (role_id, resource_id) values (3, 1);--首页--
insert into t_sys_role_resource (role_id, resource_id) values (3, 3);--教师课程--
insert into t_sys_role_resource (role_id, resource_id) values (3, 4);--班级列表--
insert into t_sys_role_resource (role_id, resource_id) values (3, 10);--个人中心--

insert into t_sys_role_resource (role_id, resource_id) values (4, 1);--首页--
insert into t_sys_role_resource (role_id, resource_id) values (4, 2);--学生课程--
insert into t_sys_role_resource (role_id, resource_id) values (4, 10);--个人中心--


--用户表--
create table t_sys_user (
id serial primary key,
user_name varchar not null,
password varchar not null,
code varchar not null,
sex integer not null default 0,
mobile varchar,
avatar varchar,
nick_name varchar,
english_name varchar,
creator integer,
create_time timestamp,
operator integer,
update_time timestamp,
is_stop integer not null default 0,
role_type integer not null,
school varchar NULL,
major varchar NULL,
mail varchar NULL
);
INSERT INTO t_sys_user (user_name, password, code, avatar,creator, create_time, role_type) values ('admin', 'admin', 'admin', '/avatar/default.png', 0, '2021-04-21 18:00:00', 1);
INSERT INTO t_sys_user (user_name, password, code, avatar,creator, create_time, role_type) values ('kczj', 'kczj', 'kczj', '/avatar/default.png', 0, '2021-04-21 18:00:00', 2);
INSERT INTO t_sys_user (user_name, password, code, avatar,creator, create_time, role_type) values ('js', 'js', 'js', '/avatar/default.png', 0, '2021-04-21 18:00:00', 3);
INSERT INTO t_sys_user (user_name, password, code, avatar,creator, create_time, role_type) values ('xs', 'xs', 'xs', '/avatar/default.png', 0, '2021-04-21 18:00:00', 4);

--用户角色表---
create table t_sys_user_role (
id serial primary key,
role_id integer not null,
user_id integer not null
);
insert into t_sys_user_role (role_id, user_id) values (1, 1);
insert into t_sys_user_role (role_id, user_id) values (2, 2);
insert into t_sys_user_role (role_id, user_id) values (3, 2);
insert into t_sys_user_role (role_id, user_id) values (3, 3);
insert into t_sys_user_role (role_id, user_id) values (4, 3);
insert into t_sys_user_role (role_id, user_id) values (4, 4);

--意见反馈表---
create table t_feedback (
id serial primary key,
content text not null,
mobile varchar,
creator integer not null,
create_time timestamp
);

--课程用户表关联表（助教）
create table t_course_user (
id serial primary key,
user_id integer not null,
course_id integer not null
);

--单元测试表---
create table t_exam(
id serial primary key,
course_id integer not null,
test_name varchar not null,
test_desc varchar,
creator integer not null,
create_time timestamp,
delete_time timestamp,
delete_flag integer not null default 0,
exercise_source integer not null default 0
);

--单元测试 习题关联表---
create table t_exam_exercise(
id serial primary key,
exam_id integer not null,
exercise_id integer not null,
ordinal integer not null,
score integer not null default 10
);

--单元测试 课程/班级/作业关联表---
create table t_exam_class(
id serial primary key,
exam_id integer not null,
class_id integer not null,
course_id integer not null,
test_start varchar,
test_end varchar,
test_is_open boolean not null default false,
delete_time timestamp,
delete_flag integer not null default 0
);

--公共库，场景表--
create table t_public_scene (
id serial primary key,
scene_name varchar not null,
scene_desc text,
init_shell text,
creator integer not null,
create_time timestamp,
delete_time timestamp,
delete_flag integer not null default 0
);

--公共库，场景shell表---
create table t_public_scene_detail (
id serial primary key,
scene_id integer not null,
table_name varchar not null,
table_detail text not null,
table_desc varchar
);

--公共库，习题表--
create table t_public_exercise (
id serial primary key,
scene_id integer,
exercise_name varchar not null,
exercise_desc text,
exercise_analysis text null,
answer text not null,
creator integer not null,
create_time timestamp,
delete_time timestamp,
delete_flag integer not null default 0
);

create table t_exam_score (
id serial primary key,
class_id integer not null,
exam_id integer not null,
exercise_id integer not null,
user_id integer not null,
create_time varchar null,
usage_time varchar null,
answer varchar null,
score integer null,
answer_execute_time integer not null default 0,
exam_class_id integer not null
);
--课程目录表--
create table t_course_catalogue (
id serial primary key,
course_id integer not null,
parent_id integer not null,
catalogue_name varchar,
model integer not null default 2,
delete_flag integer not null default 0,
sort_num integer DEFAULT 0,
start_time timestamp,
end_time timestamp,
create_time timestamp,
create_user int4,
delete_time timestamp,
delete_user int4,
update_time timestamp,
update_user int4
);
COMMENT ON COLUMN "t_course_catalogue"."id" IS '主键';
COMMENT ON COLUMN "t_course_catalogue"."course_id" IS '课程id';
COMMENT ON COLUMN "t_course_catalogue"."parent_id" IS '上层id';
COMMENT ON COLUMN "t_course_catalogue"."catalogue_name" IS '目录名称';
COMMENT ON COLUMN "t_course_catalogue"."model" IS '模式';
COMMENT ON COLUMN "t_course_catalogue"."sort_num" IS '排序字段';
COMMENT ON COLUMN "t_course_catalogue"."start_time" IS '开始时间';
COMMENT ON COLUMN "t_course_catalogue"."end_time" IS '结束时间';
COMMENT ON COLUMN "t_course_catalogue"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_course_catalogue"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_course_catalogue"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_course_catalogue"."delete_user" IS '删除人员';
COMMENT ON COLUMN "t_course_catalogue"."update_time" IS '更新时间';
COMMENT ON COLUMN "t_course_catalogue"."update_user" IS '更新人员';
--资源库表--
CREATE TABLE "t_resources" (
  "id" serial primary key,
  "resources_name" varchar ,
  "resources_rename" varchar ,
  "resources_type" varchar ,
  "resources_url" varchar ,
  "resources_time" int4 DEFAULT 0,
  "resources_suffix" varchar ,
  "screenshot" varchar ,
  "page_num" int4,
  "resources_additional" integer default 2,
  "create_time" timestamp(6),
  "create_user" int4,
  "delete_flag" int4 DEFAULT 0,
  "delete_time" timestamp(6),
  "delete_user" int4 ,
  "update_time" timestamp(6),
  "update_user" int4,
  "resources_type_name" varchar(100) ,
  "resources_pdf_url" varchar(255),
  "sort_num" integer DEFAULT 0,
  "parent_id" int4 DEFAULT 0,
  "resources_size" int4 DEFAULT 0,
  "resources_retype" int4
)
;
COMMENT ON COLUMN "t_resources"."resources_name" IS '资源名称';
COMMENT ON COLUMN "t_resources"."resources_rename" IS '资源存储名称';
COMMENT ON COLUMN "t_resources"."resources_type" IS '资源类型';
COMMENT ON COLUMN "t_resources"."resources_url" IS '资源路径';
COMMENT ON COLUMN "t_resources"."resources_time" IS '资源时长';
COMMENT ON COLUMN "t_resources"."resources_suffix" IS '资源后缀';
COMMENT ON COLUMN "t_resources"."screenshot" IS '视频缩略图路径';
COMMENT ON COLUMN "t_resources"."page_num" IS 'ppt页数';
COMMENT ON COLUMN "t_resources"."sort_num" IS '排序';
COMMENT ON COLUMN "t_resources"."parent_id" IS '上层目录id';
COMMENT ON COLUMN "t_resources"."resources_type_name" IS '资源类型名称';
COMMENT ON COLUMN "t_resources"."resources_pdf_url" IS '文档类型资源转pdf后的路径';
COMMENT ON COLUMN "t_resources"."resources_additional" IS '是否为其他资源 1：是 2：否';
COMMENT ON COLUMN "t_resources"."resources_size" IS '资源大小';
COMMENT ON COLUMN "t_resources"."resources_retype" IS '转换后的资源类型';
--内容表--
CREATE TABLE "t_course_contents" (
  "id" serial primary key,
  "course_id" integer NOT NULL,
  "catalogue_id" integer NOT NULL,
  "contents" text,
  "tab_num" integer,
  "create_time" timestamp(6),
  "create_user" int4,
  "update_time" timestamp(6),
  "update_user" int4,
  "delete_time" timestamp(6),
  "delete_user" int4,
  "delete_flag" int4 DEFAULT 0
);
COMMENT ON COLUMN "t_course_contents"."id" IS '主键';
COMMENT ON COLUMN "t_course_contents"."course_id" IS '课程id';
COMMENT ON COLUMN "t_course_contents"."catalogue_id" IS '目录id';
COMMENT ON COLUMN "t_course_contents"."contents" IS '内容';
COMMENT ON COLUMN "t_course_contents"."tab_num" IS '页码备用';
COMMENT ON COLUMN "t_course_contents"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_course_contents"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_course_contents"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_course_contents"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_course_contents"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_course_contents"."delete_user" IS '删除人员';
-- ----------------------------
-- Table structure for t_catalogue_auth_class目录权限班级表
-- ----------------------------
DROP TABLE IF EXISTS "t_catalogue_auth_class";
CREATE TABLE "t_catalogue_auth_class" (
  "id" serial primary key,
  "course_id" integer NOT NULL,
  "catalogue_id" integer NOT NULL,
  "catalogue_name" varchar ,
  "class_id" integer NOT NULL,
  "class_name" varchar ,
  "auth_type" varchar(32),
  "flag" integer NOT NULL default '0'
)
;
COMMENT ON COLUMN "t_catalogue_auth_class"."course_id" IS '课程id';
COMMENT ON COLUMN "t_catalogue_auth_class"."catalogue_id" IS '目录id';
COMMENT ON COLUMN "t_catalogue_auth_class"."catalogue_name" IS '目录名称';
COMMENT ON COLUMN "t_catalogue_auth_class"."class_id" IS '班级id';
COMMENT ON COLUMN "t_catalogue_auth_class"."class_name" IS '班级名称';
COMMENT ON COLUMN "t_catalogue_auth_class"."auth_type" IS '权限类型：0：整个班级、1：部分学生';
ALTER TABLE "t_catalogue_auth_class" ADD CONSTRAINT "t_catalogue_auth_class_unique" UNIQUE ("course_id", "catalogue_id", "class_id");
COMMENT ON CONSTRAINT "t_catalogue_auth_class_unique" ON "t_catalogue_auth_class" IS '每个目录下每个课程下每个班级约束';
-- ----------------------------
-- Table structure for t_catalogue_auth_stu目录权限学生表
-- ----------------------------
DROP TABLE IF EXISTS "t_catalogue_auth_stu";
CREATE TABLE "t_catalogue_auth_stu" (
  "id" serial primary key,
  "course_id" integer NOT NULL,
  "catalogue_id" integer NOT NULL,
  "class_id" integer NOT NULL,
  "user_id" integer NOT NULL,
  "code" integer NOT NULL,
  "user_name" integer NOT NULL,
  "del_flag" integer NOT NULL default 0
)
;
COMMENT ON COLUMN "t_catalogue_auth_stu"."course_id" IS '课程id';
COMMENT ON COLUMN "t_catalogue_auth_stu"."catalogue_id" IS '目录id';
COMMENT ON COLUMN "t_catalogue_auth_stu"."class_id" IS '班级id';
COMMENT ON COLUMN "t_catalogue_auth_stu"."user_id" IS '学生id';
COMMENT ON COLUMN "t_catalogue_auth_stu"."code" IS '学号';
COMMENT ON COLUMN "t_catalogue_auth_stu"."user_name" IS '姓名';
COMMENT ON COLUMN "t_catalogue_auth_stu"."del_flag" IS '标记 0：未删除 1：已删除';
ALTER TABLE "t_catalogue_auth_stu" ADD CONSTRAINT "t_catalogue_ath_stu_unique" UNIQUE ("course_id", "catalogue_id", "class_id", "user_id");
COMMENT ON CONSTRAINT "t_catalogue_ath_stu_unique" ON "t_catalogue_auth_stu" IS '唯一约束';

--目录资源表--
--目录资源表--
CREATE TABLE "t_catalogue_resources" (
  "id" int8 primary key,
  "course_id" int4 NOT NULL,
  "catalogue_id" int4 NOT NULL,
  "resources_id" int4 NOT NULL,
  "task_type" int4 default 0,
  "process_set" numeric(10,2),
  "download_auth" int2,
  "is_speed" int2,
  "is_task" int2,
  "fast_forward" int2,
  "create_time" timestamp(6),
  "create_user" int4,
  "update_time" timestamp(6),
  "update_user" int4,
  "delete_flag" integer not null default 0,
 "delete_time" timestamp,
 "delete_user" int4
)
;
COMMENT ON COLUMN "t_catalogue_resources"."course_id" IS '课程id';
COMMENT ON COLUMN "t_catalogue_resources"."catalogue_id" IS '目录id';
COMMENT ON COLUMN "t_catalogue_resources"."resources_id" IS '资源id';
COMMENT ON COLUMN "t_catalogue_resources"."task_type" IS '任务类型 0:资源 1：作业';
COMMENT ON COLUMN "t_catalogue_resources"."process_set" IS '阈值';
COMMENT ON COLUMN "t_catalogue_resources"."download_auth" IS '下载权限0：可下载 1：不可下载';
COMMENT ON COLUMN "t_catalogue_resources"."is_speed" IS '是否倍速 0：可倍数观看，1：不可倍速观看';
COMMENT ON COLUMN "t_catalogue_resources"."is_task" IS '是否任务点 0: 否 1：是';
COMMENT ON COLUMN "t_catalogue_resources"."fast_forward" IS '是否快进0：可快进 1：不可快进';

CREATE TABLE "t_catalogue_process" (
  "id" serial primary key,
  "course_id" int4 NOT NULL,
  "catalogue_id" int4 NOT NULL,
  "contents_id" int4,
  "class_id" int4 NOT NULL,
  "user_id" int4 NOT NULL,
  "resources_id" int4 NOT NULL,
  "study_status" int2 DEFAULT 1,
  "progress" int4 DEFAULT 0,
  "duration" int8 DEFAULT 0,
  "task_type" int4 default 0,
  "update_time" timestamp(6),
  "update_user" int4,
  "create_time" timestamp(6),
  "create_user" int4,
  "delete_flag" int4 DEFAULT 0,
  "delete_time" timestamp(6),
  "delete_user" int4
)
;
COMMENT ON COLUMN "t_catalogue_process"."course_id" IS '课程id';
COMMENT ON COLUMN "t_catalogue_process"."catalogue_id" IS '目录id';
COMMENT ON COLUMN "t_catalogue_process"."contents_id" IS '内容id';
COMMENT ON COLUMN "t_catalogue_process"."class_id" IS '班级id';
COMMENT ON COLUMN "t_catalogue_process"."user_id" IS '学生id';
COMMENT ON COLUMN "t_catalogue_process"."resources_id" IS '资源id';
COMMENT ON COLUMN "t_catalogue_process"."study_status" IS '学习状态1：未学完 2：已学完';
COMMENT ON COLUMN "t_catalogue_process"."progress" IS '播放进度';
COMMENT ON COLUMN "t_catalogue_process"."duration" IS '观看时长';
COMMENT ON COLUMN "t_catalogue_process"."task_type" IS '任务类型 0:资源 1：作业';
COMMENT ON COLUMN "t_catalogue_process"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_catalogue_process"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_catalogue_process"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_catalogue_process"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_catalogue_process"."delete_flag" IS '删除标志0：未删除1：已删除';
COMMENT ON COLUMN "t_catalogue_process"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_catalogue_process"."delete_user" IS '删除人员';
--作业表--
CREATE TABLE "t_homework" (
  "id" serial primary key,
  "course_id" int4,
  "homework_name" varchar(200),
  "model_id" int4,
  "model_name" varchar(200),
  "start_time" timestamp(6),
  "end_time" timestamp(6),
  "allow_after" int4,
  "pass_mark" int2 DEFAULT 0,
  "score" numeric(10,1) DEFAULT 0.0,
  "redo_times" int4 DEFAULT 0,
  "max_score" int4 DEFAULT 1,
  "view_time" int4 DEFAULT 1,
  "ignore_case" int4 DEFAULT 2,
  "unselected_given" int4 DEFAULT 2,
  "create_time" timestamp(6),
  "create_user" int4,
  "update_time" timestamp(6),
  "update_user" int4,
  "delete_flag" int4 DEFAULT 0,
  "delete_time" timestamp(6),
  "delete_user" int4
)
;
COMMENT ON COLUMN "t_homework"."course_id" IS '课程id';
COMMENT ON COLUMN "t_homework"."homework_name" IS '作业名称';
COMMENT ON COLUMN "t_homework"."model_id" IS '作业模板id';
COMMENT ON COLUMN "t_homework"."model_name" IS '作业模板名称';
COMMENT ON COLUMN "t_homework"."start_time" IS '开始时间';
COMMENT ON COLUMN "t_homework"."end_time" IS '结束时间';
COMMENT ON COLUMN "t_homework"."allow_after" IS '1:允许补交2：不允许补交';
COMMENT ON COLUMN "t_homework"."pass_mark" IS '及格分数';
COMMENT ON COLUMN "t_homework"."score" IS '总分数';
COMMENT ON COLUMN "t_homework"."redo_times" IS '重做次数';
COMMENT ON COLUMN "t_homework"."max_score" IS '是否取最高成绩1：是2：否';
COMMENT ON COLUMN "t_homework"."view_time" IS '答案查看时间1：批阅后2：提交后3：作业结束后4：不允许';
COMMENT ON COLUMN "t_homework"."ignore_case" IS '是否填空题不区分大小写1：是2：否';
COMMENT ON COLUMN "t_homework"."unselected_given" IS '多选题未选全给一半分1：是2：否';
COMMENT ON COLUMN "t_homework"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_homework"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_homework"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_homework"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_homework"."delete_flag" IS '删除标志0：未删除1：已删除';
COMMENT ON COLUMN "t_homework"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_homework"."delete_user" IS '删除人员';

--作业模板表--
CREATE TABLE "t_homework_model" (
  "id" serial primary key,
  "course_id" int4,
  "parent_id" int4,
  "element_type" int4,
  "model_name" varchar(200),
  "auth_type" int4 DEFAULT 2,
  "granding_standard" int4 DEFAULT 1,
  "classify" int4 DEFAULT 1,
  "tgp" numeric(10,1) default 0.0,
  "create_time" timestamp(6),
  "create_user" int4,
  "update_time" timestamp(6),
  "update_user" int4,
  "delete_flag" int4 DEFAULT 0,
  "delete_time" timestamp(6),
  "delete_user" int4
)
;
COMMENT ON COLUMN "t_homework_model"."course_id" IS '课程id';
COMMENT ON COLUMN "t_homework_model"."parent_id" IS '父类id';
COMMENT ON COLUMN "t_homework_model"."element_type" IS '0:试题，1:文件夹';
COMMENT ON COLUMN "t_homework_model"."model_name" IS '作业模板名称';
COMMENT ON COLUMN "t_homework_model"."auth_type" IS '1:私有，2:共享';
COMMENT ON COLUMN "t_homework_model"."granding_standard" IS '评分机制 1：百分制2：自定义';
COMMENT ON COLUMN "t_homework_model"."classify" IS '题型归类1：是2：否';
COMMENT ON COLUMN "t_homework_model"."tgp" IS '总分';
COMMENT ON COLUMN "t_homework_model"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_homework_model"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_homework_model"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_homework_model"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_homework_model"."delete_flag" IS '删除标志0：未删除1：已删除';
COMMENT ON COLUMN "t_homework_model"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_homework_model"."delete_user" IS '删除人员';
--模板习题关联表--
DROP TABLE IF EXISTS "t_model_exercise";
CREATE TABLE "t_model_exercise" (
  "id" serial primary key,
  "model_id" int4,
  "exercise_id" int4,
  "exercise_score" numeric(10,1) DEFAULT 0.0,
  "exercise_type" integer,
  "exercise_style" int4,
  "exercise_order" int4 DEFAULT 0,
  "create_time" timestamp(6),
  "create_user" int4,
  "update_time" timestamp(6),
  "update_user" int4,
  "delete_flag" int4 DEFAULT 0,
  "delete_time" timestamp(6),
  "delete_user" int4
)
;
COMMENT ON COLUMN "t_model_exercise"."model_id" IS '模板id';
COMMENT ON COLUMN "t_model_exercise"."exercise_id" IS '习题id';
COMMENT ON COLUMN "t_model_exercise"."exercise_score" IS '习题分数';
COMMENT ON COLUMN "t_model_exercise"."exercise_style" IS '试题类型 1：主观 2：客观';
COMMENT ON COLUMN "t_model_exercise"."exercise_type" IS '试题类型 1：单选题2：多选题3：判断题4：填空题5：简答题6：SQL编程题';
COMMENT ON COLUMN "t_model_exercise"."exercise_order" IS '排序';
COMMENT ON COLUMN "t_model_exercise"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_model_exercise"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_model_exercise"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_model_exercise"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_model_exercise"."delete_flag" IS '删除标志0：未删除1：已删除';
COMMENT ON COLUMN "t_model_exercise"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_model_exercise"."delete_user" IS '删除人员';
--作业发放表--
CREATE TABLE "t_homework_distribution" (
  "id" serial primary key,
  "course_id" int4,
  "homework_id" int4 not null,
  "class_id" int4 not null,
  "class_name" varchar,
  "target_type" int4 DEFAULT 1 not null,
  "flag" int4 DEFAULT 2,
  "create_time" timestamp(6),
  "create_user" int4,
  "update_time" timestamp(6),
  "update_user" int4,
  "delete_flag" int4 DEFAULT 0,
  "delete_time" timestamp(6),
  "delete_user" int4
)
;
COMMENT ON COLUMN "t_homework_distribution"."course_id" IS '课程id';
COMMENT ON COLUMN "t_homework_distribution"."homework_id" IS '作业id';
COMMENT ON COLUMN "t_homework_distribution"."class_id" IS '班级id';
COMMENT ON COLUMN "t_homework_distribution"."class_name" IS '班级名字';
COMMENT ON COLUMN "t_homework_distribution"."target_type" IS '类型1：班级2：学生';
COMMENT ON COLUMN "t_homework_distribution"."flag" IS '标记1：发放2：未发放';
COMMENT ON COLUMN "t_homework_distribution"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_homework_distribution"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_homework_distribution"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_homework_distribution"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_homework_distribution"."delete_flag" IS '删除标志0：未删除1：已删除';
COMMENT ON COLUMN "t_homework_distribution"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_homework_distribution"."delete_user" IS '删除人员';


--作业发放学生表--
CREATE TABLE "t_homework_distribution_student" (
  "id" serial primary key,
  "distribution_id" int4 not null,
  "homework_id" int4 not null,
  "student_id" int4   not null,
  "student_name" varchar,
  "create_time" timestamp(6),
  "create_user" int4,
  "update_time" timestamp(6),
  "update_user" int4,
  "delete_flag" int4 DEFAULT 0,
  "delete_time" timestamp(6),
  "delete_user" int4
)
;
COMMENT ON COLUMN "t_homework_distribution_student"."distribution_id" IS '作业发放id';
COMMENT ON COLUMN "t_homework_distribution_student"."homework_id" IS '作业id';
COMMENT ON COLUMN "t_homework_distribution_student"."student_id" IS '学生的id';
COMMENT ON COLUMN "t_homework_distribution_student"."student_name" IS '学生的名字';
COMMENT ON COLUMN "t_homework_distribution_student"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_homework_distribution_student"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_homework_distribution_student"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_homework_distribution_student"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_homework_distribution_student"."delete_flag" IS '删除标志0：未删除1：已删除';
COMMENT ON COLUMN "t_homework_distribution_student"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_homework_distribution_student"."delete_user" IS '删除人员';
--学生作业表--
DROP TABLE IF EXISTS "t_stu_homework";
CREATE TABLE "t_stu_homework" (
  "id" serial primary key,
  "homework_id" int4,
  "student_id" int4,
  "course_id" int4,
  "student_name" varchar,
  "student_code" varchar,
  "class_id" int4 DEFAULT 1,
  "class_name" varchar,
  "score" numeric(10,1) DEFAULT 0.0,
  "homework_status" int4 DEFAULT 2,
  "check_status" int4 DEFAULT 2,
  "submit_time" timestamp(6),
  "approval_time" timestamp(6),
  "comments" varchar,
  "create_time" timestamp(6),
  "create_user" int4,
  "update_time" timestamp(6),
  "update_user" int4,
  "delete_flag" int4 DEFAULT 0,
  "delete_time" timestamp(6),
  "delete_user" int4
)
;
COMMENT ON COLUMN "t_stu_homework"."course_id" IS '课程id';
COMMENT ON COLUMN "t_stu_homework"."homework_id" IS '作业id';
COMMENT ON COLUMN "t_stu_homework"."student_id" IS '学生的id';
COMMENT ON COLUMN "t_stu_homework"."student_name" IS '学生的名字';
COMMENT ON COLUMN "t_stu_homework"."student_code" IS '学号';
COMMENT ON COLUMN "t_stu_homework"."class_id" IS '班级id';
COMMENT ON COLUMN "t_stu_homework"."class_name" IS '班级名称';
COMMENT ON COLUMN "t_stu_homework"."score" IS '分数';
COMMENT ON COLUMN "t_stu_homework"."homework_status" IS '作业状态1：已提交2：未提交3：打回未提交';
COMMENT ON COLUMN "t_stu_homework"."check_status" IS '批阅状态1：已批阅2：待批阅';
COMMENT ON COLUMN "t_stu_homework"."submit_time" IS '提交时间时间';
COMMENT ON COLUMN "t_stu_homework"."approval_time" IS '批阅时间';
COMMENT ON COLUMN "t_stu_homework"."comments" IS '作业评语';
COMMENT ON COLUMN "t_stu_homework"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_stu_homework"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_stu_homework"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_stu_homework"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_stu_homework"."delete_flag" IS '删除标志0：未删除1：已删除';
COMMENT ON COLUMN "t_stu_homework"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_stu_homework"."delete_user" IS '删除人员';

--学生作业明细表--
CREATE TABLE "t_stu_homework_info" (
  "id" serial primary key,
  "course_id" int4,
  "homework_id" int4,
  "homework_name" varchar,
  "model_id" int4,
  "model_name" varchar,
  "student_id" int4,
  "student_name" varchar,
  "student_code" varchar,
  "class_id" int4,
  "class_name" varchar,
  "exercise_id" int4,
  "exercise_score" numeric(10,1) DEFAULT 0.0,
  "exercise_result" text,
  "is_correct" int4 DEFAULT 1,
  "create_time" timestamp(6),
  "create_user" int4,
  "update_time" timestamp(6),
  "update_user" int4,
  "delete_flag" int4 DEFAULT 0,
  "delete_time" timestamp(6),
  "delete_user" int4
)
;
COMMENT ON COLUMN "t_stu_homework_info"."course_id" IS '课程id';
COMMENT ON COLUMN "t_stu_homework_info"."homework_id" IS '作业id';
COMMENT ON COLUMN "t_stu_homework_info"."homework_name" IS '作业名称';
COMMENT ON COLUMN "t_stu_homework_info"."model_id" IS '模板id';
COMMENT ON COLUMN "t_stu_homework_info"."model_name" IS '模板名称';
COMMENT ON COLUMN "t_stu_homework_info"."student_id" IS '学生ID';
COMMENT ON COLUMN "t_stu_homework_info"."student_name" IS '学生的名字';
COMMENT ON COLUMN "t_stu_homework_info"."student_code" IS '学号';
COMMENT ON COLUMN "t_stu_homework_info"."class_id" IS '班级id';
COMMENT ON COLUMN "t_stu_homework_info"."class_name" IS '班级名称';
COMMENT ON COLUMN "t_stu_homework_info"."exercise_id" IS '习题id';
COMMENT ON COLUMN "t_stu_homework_info"."exercise_score" IS '习题分数';
COMMENT ON COLUMN "t_stu_homework_info"."exercise_result" IS '习题答案';
COMMENT ON COLUMN "t_stu_homework_info"."is_correct" IS '是否正确1：是2：否3：半对';
COMMENT ON COLUMN "t_stu_homework_info"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_stu_homework_info"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_stu_homework_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_stu_homework_info"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_stu_homework_info"."delete_flag" IS '删除标志0：未删除1：已删除';
COMMENT ON COLUMN "t_stu_homework_info"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_stu_homework_info"."delete_user" IS '删除人员';

--题型表--
DROP TABLE IF EXISTS "t_exercise_type";
CREATE TABLE "t_exercise_type" (
  "id" serial primary key,
  "type_code" int4,
  "type_name" varchar,
  "type_style" int4
  );
COMMENT ON COLUMN "t_exercise_type"."type_code" IS '题型编码';
COMMENT ON COLUMN "t_exercise_type"."type_name" IS '题型名称';
COMMENT ON COLUMN "t_exercise_type"."type_style" IS '题型类型 1:主观题2:客观题';
insert into t_exercise_type("type_code","type_name","type_style")values (1,'单选题',2);
insert into t_exercise_type("type_code","type_name","type_style")values (2,'多选题',2);
insert into t_exercise_type("type_code","type_name","type_style")values (3,'判断题',2);
insert into t_exercise_type("type_code","type_name","type_style")values (4,'填空题',2);
insert into t_exercise_type("type_code","type_name","type_style")values (5,'简答题',1);
insert into t_exercise_type("type_code","type_name","type_style")values (6,'SQL编程题',2);

--习题知识点关联表--
CREATE TABLE t_new_exercise_knowledge (
	id serial NOT NULL,
	exercise_id int4 NOT NULL,
	knowledge_id int4 NOT NULL,
	course_id int4 NOT NULL,
	CONSTRAINT t_new_exercise_knowledge_pkey PRIMARY KEY (id)
)
WITH (
	OIDS=FALSE
) ;
COMMENT ON COLUMN "t_new_exercise_knowledge"."exercise_id" IS '习题id';
COMMENT ON COLUMN "t_new_exercise_knowledge"."knowledge_id" IS '知识点id';
COMMENT ON COLUMN "t_new_exercise_knowledge"."course_id" IS '课程id';

--作业中按照类型排序--
DROP TABLE IF EXISTS "t_model_exercise_type";
CREATE TABLE "t_model_exercise_type" (
  "id" serial primary key,
  "model_id" integer not null,
  "exercise_type" integer not null,
  "sort_num" integer DEFAULT 0,
  "update_time" timestamp(6),
  "update_user" int4
)
;
COMMENT ON COLUMN "t_model_exercise_type"."model_id" IS '模板id';
COMMENT ON COLUMN "t_model_exercise_type"."exercise_type" IS '习题类型';
COMMENT ON COLUMN "t_model_exercise_type"."sort_num" IS '排序';
COMMENT ON COLUMN "t_model_exercise_type"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_model_exercise_type"."update_user" IS '修改人员';