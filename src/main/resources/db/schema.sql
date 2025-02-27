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

