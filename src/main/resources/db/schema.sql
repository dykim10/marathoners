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

CREATE TABLE tb_users
(
    user_uuid              varchar(50) NOT NULL,
    user_id                varchar(100),
    user_email             varchar(255) NOT NULL,
    user_name              varchar(50) ,
    user_pwd               varchar(255),
    user_hp                varchar(20) ,
    user_reg_dt            timestamp    DEFAULT NOW(),
    user_un_reg_dt         timestamp   ,
    user_last_login_dt     timestamp   ,
    user_status            char(1)      DEFAULT 'Y',
    user_ci                varchar     ,
    user_role              varchar(10)  NOT NULL DEFAULT 'USER',
    user_identification_yn varchar(255),
    PRIMARY KEY (user_uuid)
);

COMMENT ON TABLE tb_users IS '유저정보';
COMMENT ON COLUMN tb_users.user_uuid IS '기본키';
COMMENT ON COLUMN tb_users.user_id IS '유저 아이디';
COMMENT ON COLUMN tb_users.user_email IS '유저 이메일 주소';
COMMENT ON COLUMN tb_users.user_name IS '유저 성명';
COMMENT ON COLUMN tb_users.user_pwd IS '유저 비밀번호';
COMMENT ON COLUMN tb_users.user_hp IS '휴대전화정보';
COMMENT ON COLUMN tb_users.user_reg_dt IS '유저 가입일';
COMMENT ON COLUMN tb_users.user_un_reg_dt IS '유저 탈퇴일';
COMMENT ON COLUMN tb_users.user_last_login_dt IS '유저마지막로그인일';
COMMENT ON COLUMN tb_users.user_status IS '유저상태';
COMMENT ON COLUMN tb_users.user_ci IS '유저 CI, DI 키값';
COMMENT ON COLUMN tb_users.user_role IS '유저 역할';
COMMENT ON COLUMN tb_users.user_identification_yn IS '본인인증여부';

