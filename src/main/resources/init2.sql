



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
  "approval_user_id" int4,
  "end_time" timestamp(6),
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
COMMENT ON COLUMN "t_stu_homework"."approval_user_id" IS '批阅人员';
COMMENT ON COLUMN "t_stu_homework"."comments" IS '作业评语';
COMMENT ON COLUMN "t_stu_homework"."end_time" IS '加时后结束时间';
COMMENT ON COLUMN "t_stu_homework"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_stu_homework"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_stu_homework"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_stu_homework"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_stu_homework"."delete_flag" IS '删除标志0：未删除1：已删除';
COMMENT ON COLUMN "t_stu_homework"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_stu_homework"."delete_user" IS '删除人员';


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
COMMENT ON COLUMN "t_new_exercise"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_new_exercise"."update_user" IS '修改人员';
COMMENT ON COLUMN "t_new_exercise"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_new_exercise"."create_user" IS '创建人员';
COMMENT ON COLUMN "t_new_exercise"."delete_flag" IS '删除标志0：未删除1：已删除';
COMMENT ON COLUMN "t_new_exercise"."delete_time" IS '删除时间';
COMMENT ON COLUMN "t_new_exercise"."delete_user" IS '删除人员';


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

ALTER TABLE t_homework ALTER COLUMN delete_flag TYPE int4 USING delete_flag::int4;
ALTER TABLE t_exercise_info ALTER COLUMN delete_flag TYPE int4 USING delete_flag::int4;
ALTER TABLE t_course_contents ALTER COLUMN update_user TYPE int4 USING update_user::int4;
ALTER TABLE t_course_contents ALTER COLUMN create_user TYPE int4 USING create_user::int4;
ALTER TABLE t_course_contents ADD delete_time timestamp NULL;
ALTER TABLE t_course_contents ADD delete_flag int4 NULL DEFAULT 0;
COMMENT ON COLUMN t_course_contents.delete_time IS '删除时间' ;
ALTER TABLE t_course_contents ADD delete_user int4 NULL;
COMMENT ON COLUMN t_course_contents.delete_user IS '删除人员' ;
ALTER TABLE t_homework_distribution_student ALTER COLUMN delete_flag TYPE int4 USING delete_flag::int4;
ALTER TABLE public.t_stu_homework ALTER COLUMN delete_flag TYPE int4 USING delete_flag::int4;
ALTER TABLE public.t_stu_homework_info ALTER COLUMN delete_flag TYPE int4 USING delete_flag::int4;
ALTER TABLE t_homework_model ALTER COLUMN delete_flag TYPE int4 USING delete_flag::int4;
ALTER TABLE t_resources ALTER COLUMN create_user TYPE int4 USING create_user::int4;
ALTER TABLE t_resources ALTER COLUMN delete_user TYPE int4 USING delete_user::int4;
ALTER TABLE t_resources ALTER COLUMN update_user TYPE int4 USING update_user::int4;

ALTER TABLE t_course_catalogue ALTER COLUMN create_user TYPE int4 USING create_user::int4;
ALTER TABLE t_course_catalogue ALTER COLUMN delete_user TYPE int4 USING delete_user::int4;
ALTER TABLE t_course_catalogue ALTER COLUMN update_user TYPE int4 USING update_user::int4;

ALTER TABLE t_catalogue_process ALTER COLUMN delete_flag TYPE int4 USING delete_flag::int4;

ALTER TABLE t_catalogue_resources ALTER COLUMN create_user TYPE int4 USING create_user::int4;
ALTER TABLE t_catalogue_resources ALTER COLUMN delete_user TYPE int4 USING delete_user::int4;
ALTER TABLE t_catalogue_resources ALTER COLUMN update_user TYPE int4 USING update_user::int4;
ALTER TABLE t_homework_distribution ALTER COLUMN delete_flag TYPE int4 USING delete_flag::int4;


ALTER TABLE t_new_exercise ADD exercise_status int4 NULL DEFAULT 0;
COMMENT ON COLUMN t_new_exercise.exercise_status IS '练习题状态 0：是练习题 1：非练习题' ;
ALTER TABLE t_new_exercise ADD show_answer int4 NULL DEFAULT 0;
COMMENT ON COLUMN t_new_exercise.show_answer IS '是否显示答案 0：显示答案 1：不显示答案' ;



