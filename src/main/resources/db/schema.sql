/************************************
    # 테스트 테이블
************************************/

CREATE TABLE IF NOT EXISTS message (
   id BIGSERIAL PRIMARY KEY,
   message VARCHAR(255) NOT NULL
);

/************************************
    # user 정보테이블
************************************/


create table public.tb_users
(
    user_uuid              varchar(50)                                   not null
        primary key,
    user_id                varchar(100),
    user_email             varchar(255)                                  not null,
    user_name              varchar(50),
    user_pwd               varchar(255),
    user_hp                varchar(20),
    user_reg_dt            timestamp   default now(),
    user_un_reg_dt         timestamp,
    user_last_login_dt     timestamp,
    user_status            char        default 'Y'::bpchar,
    user_ci                varchar,
    user_role              varchar(10) default 'USER'::character varying not null,
    user_identification_yn varchar(255)
);

comment on table public.tb_users is '유저정보';
comment on column public.tb_users.user_uuid is '기본키';
comment on column public.tb_users.user_id is '유저 아이디';
comment on column public.tb_users.user_email is '유저 이메일 주소';
comment on column public.tb_users.user_name is '유저 성명';
comment on column public.tb_users.user_pwd is '유저 비밀번호';
comment on column public.tb_users.user_hp is '휴대전화정보';
comment on column public.tb_users.user_reg_dt is '유저 가입일';
comment on column public.tb_users.user_un_reg_dt is '유저 탈퇴일';
comment on column public.tb_users.user_last_login_dt is '유저마지막로그인일';
comment on column public.tb_users.user_status is '유저상태';
comment on column public.tb_users.user_ci is '유저 CI, DI 키값';
comment on column public.tb_users.user_role is '유저 역할';
comment on column public.tb_users.user_identification_yn is '본인인증여부';

alter table public.tb_users owner to root;



CREATE TABLE tb_marathon_course (
    seq               SERIAL PRIMARY KEY,
    mr_uuid           VARCHAR(100) NOT NULL,
    mr_course_version INTEGER DEFAULT 1,
    mr_course_type    VARCHAR(10),
    mr_course_type_etc_text    VARCHAR(10),
    mr_course_numeric VARCHAR(100),
    mr_course_price   DECIMAL(10,2),  -- 가격을 숫자로 저장
    mr_course_reg_dt  TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- 날짜 저장 개선
);

COMMENT ON TABLE tb_marathon_course IS '대회별 코스/참여인원/가격 정보';
COMMENT ON COLUMN tb_marathon_course.seq IS '기본 키 자동 증가 값';
COMMENT ON COLUMN tb_marathon_course.mr_uuid IS '대회 코드 uuid';
COMMENT ON COLUMN tb_marathon_course.mr_course_version IS '코스 생성 버전';
COMMENT ON COLUMN tb_marathon_course.mr_course_type IS '코스 종류';
COMMENT ON COLUMN tb_marathon_course.mr_course_type_etc_text IS '코스 기타 입력';
COMMENT ON COLUMN tb_marathon_course.mr_course_numeric IS '코스 모집인원';
COMMENT ON COLUMN tb_marathon_course.mr_course_price IS '코스 금액';
COMMENT ON COLUMN tb_marathon_course.mr_course_reg_dt IS '생성일';

CREATE TABLE tb_marathon_race (
  mr_uuid         VARCHAR(100) PRIMARY KEY,
  mr_name         VARCHAR(100),
  mr_start_dt   DATE,  -- 날짜는 DATE 타입 추천
  mr_location     VARCHAR(100),
  mr_content      TEXT,
  mr_company      VARCHAR(100),
  mr_reg_adm      VARCHAR(100),
  mr_reg_dt       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 자동 날짜 입력
  mr_mod_dt       TIMESTAMP,  -- 수정 시 자동 갱신
  mr_final_status CHAR(1) DEFAULT 'N',
  mr_use_yn       CHAR(1) NOT NULL DEFAULT 'Y',
  mr_etc_memo     TEXT,
  mr_homepage_url VARCHAR(100)
);
COMMENT ON TABLE tb_marathon_race IS '마라톤 대회정보';
COMMENT ON COLUMN tb_marathon_race.mr_uuid IS '대회 코드 uuid';
COMMENT ON COLUMN tb_marathon_race.mr_name IS '대회명';
COMMENT ON COLUMN tb_marathon_race.mr_start_dt IS '대회일자 YYYY-MM-DD';
COMMENT ON COLUMN tb_marathon_race.mr_location IS '대회 장소';
COMMENT ON COLUMN tb_marathon_race.mr_content IS '대회 소개-설명-안내';
COMMENT ON COLUMN tb_marathon_race.mr_company IS '대회 주관사';
COMMENT ON COLUMN tb_marathon_race.mr_reg_adm IS '대회 등록 관리자';
COMMENT ON COLUMN tb_marathon_race.mr_reg_dt IS '대회 관리 등록일';
COMMENT ON COLUMN tb_marathon_race.mr_mod_dt IS '대회 관리 수정일';
COMMENT ON COLUMN tb_marathon_race.mr_final_status IS '대회 종료 전N/후Y 여부';
COMMENT ON COLUMN tb_marathon_race.mr_use_yn IS '대회 활성화 여부';
COMMENT ON COLUMN tb_marathon_race.mr_etc_memo IS '대회 기타 정보';
COMMENT ON COLUMN tb_marathon_race.mr_homepage_url IS '대회 홈페이지 URL';