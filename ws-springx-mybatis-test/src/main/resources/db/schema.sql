create table PERSON_T
(
    ID    BIGINT      not null,
    NAME  VARCHAR(20) not null,
    AGE   INT         not null,
    SEX   VARCHAR(20) not null,
    PHONE VARCHAR(20)
);

alter table PERSON_T
    add constraint USER_T_PK
        primary key (ID);