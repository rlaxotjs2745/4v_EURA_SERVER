CREATE TABLE TB_USER (
    IDX_USER	INT UNSIGNED NOT NULL AUTO_INCREMENT,
        USER_NAME	VARCHAR(20),
        USER_ID	VARCHAR(400),
        USER_PWD	VARCHAR(100),
        USER_PHONE	VARCHAR(20)	,
        EQ_TYPE01	TINYINT DEFAULT 0,
        EQ_TYPE02	TINYINT DEFAULT 0,
        EQ_TYPE03	TINYINT DEFAULT 0,
        EQ_TYPE04	TINYINT DEFAULT 0,
        EQ_TYPE05	TINYINT DEFAULT 0,
        EQ_TYPE06	TINYINT DEFAULT 0,
        EQ_TYPE07	TINYINT DEFAULT 0,
        EQ_TYPE08	TINYINT DEFAULT 0,
        EQ_TYPE09	TINYINT DEFAULT 0,
        EQ_TYPE10	TINYINT DEFAULT 0,
        USER_STATUS	TINYINT DEFAULT 0,
        PRIVACY_TERMS	TINYINT DEFAULT 0,
        SERVICE_USE_TERMS	TINYINT DEFAULT 0,
        PROFILE_Y	TINYINT DEFAULT 0,
        TEMP_PW_Y	TINYINT DEFAULT 0,
        TEMP_PW	VARCHAR(100)	,
        TEMP_PW_ISSUE_DT	DATETIME,
        REG_DT	DATETIME,
        LAST_PW_UPD_DT	DATETIME,
        LAST_UPD_DT	DATETIME,
    PRIMARY KEY(IDX_USER)
) ENGINE=MYISAM CHARSET=utf8;


CREATE TABLE TB_PROFILE_INFO (
                                 IDX_PROFILE_INFO	INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                 IDX_USER	INT UNSIGNED,
                                     FILE_PATH	VARCHAR(1000),
                                     FILE_NAME	VARCHAR(100),
                                     REG_DT	DATETIME,
                                     PRIMARY KEY(IDX_PROFILE_INFO)
) ENGINE=MYISAM CHARSET=utf8;

CREATE TABLE TB_MEETING (
                            IDX_MEETING	INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                 IDX_USER	INT UNSIGNED,
                                     MT_NAME	VARCHAR(100),
                                     MT_START_DT	DATETIME,
                                     MT_END_DT	DATETIME,
                                     MT_INFO	VARCHAR(3000),
                                     MT_STATUS	TINYINT DEFAULT 0,
                                     IS_LIVE	TINYINT	DEFAULT 0,
                                     REG_DT	DATETIME,
                                     LAST_UPD_DT	DATETIME,
                                     PRIMARY KEY(IDX_MEETING)
) ENGINE=MYISAM CHARSET=utf8;

CREATE TABLE TB_MEETING_USER_JOIN (
                                      IDX_MEETING_USER_JOIN	INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                      IDX_USER	INT UNSIGNED,
                                          IDX_MEETING	INT UNSIGNED,
                                          USER_EMAIL	VARCHAR(400),
                                          REG_DT	DATETIME,
                                          PRIMARY KEY(IDX_MEETING_USER_JOIN)
) ENGINE=MYISAM CHARSET=utf8;

CREATE TABLE TB_ATTACHMENT_FILE_INFO_JOIN (
                                         IDX_ATTACHMENT_FILE_INFO_JOIN	INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                         IDX_MEETING	INT UNSIGNED,
                                             FILE_PATH	VARCHAR(1000),
                                             FILE_NAME	VARCHAR(100),
                                             FILE_SIZE	INT UNSIGNED,
                                             REG_DT	DATETIME,
                                             PRIMARY KEY(IDX_ATTACHMENT_FILE_INFO_JOIN)
) ENGINE=MYISAM CHARSET=utf8;

