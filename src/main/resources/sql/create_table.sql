CREATE TABLE `TB_USER` (
  `IDX_USER` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(20) DEFAULT NULL COMMENT '회원명',
  `USER_ID` varchar(400) DEFAULT NULL COMMENT '회원ID',
  `USER_PWD` varchar(100) DEFAULT NULL COMMENT '비밀번호',
  `USER_PHONE` varchar(20) DEFAULT NULL COMMENT '회원 연락처',
  `EQ_TYPE01` tinyint(4) DEFAULT 0 COMMENT '감성지수 타입 1 - 외향성 지수 0:보통, 음수(그렇지 않다), 양수(그렇다)',
  `EQ_TYPE02` tinyint(4) DEFAULT 0 COMMENT '감성지수 타입 2 - 친화력 지수 0:보통, 음수(그렇지 않다), 양수(그렇다)',
  `EQ_TYPE03` tinyint(4) DEFAULT 0 COMMENT '감성지수 타입 3 - EQ 지수 예약',
  `EQ_TYPE04` tinyint(4) DEFAULT 0 COMMENT '감성지수 타입 4 - EQ 지수 예약',
  `EQ_TYPE05` tinyint(4) DEFAULT 0 COMMENT '감성지수 타입 5 - EQ 지수 예약',
  `EQ_TYPE06` tinyint(4) DEFAULT 0 COMMENT '감성지수 타입 6 - EQ 지수 예약',
  `EQ_TYPE07` tinyint(4) DEFAULT 0 COMMENT '감성지수 타입 7 - EQ 지수 예약',
  `EQ_TYPE08` tinyint(4) DEFAULT 0 COMMENT '감성지수 타입 8 - EQ 지수 예약',
  `EQ_TYPE09` tinyint(4) DEFAULT 0 COMMENT '감성지수 타입 9 - EQ 지수 예약',
  `EQ_TYPE10` tinyint(4) DEFAULT 0 COMMENT '감성지수 타입 10 - EQ 지수 예약',
  `USER_STATUS` tinyint(4) DEFAULT 0 COMMENT '회원 상태 - 0:가입(인증전), 1:인증완료(정회원), 2:휴면회원, 3:임의 탈퇴, 4:직권탈퇴',
  `PRIVACY_TERMS` tinyint(4) DEFAULT 0 COMMENT '개인정보 이용약관 동의 여부 - 0:아니오, 1:예',
  `SERVICE_USE_TERMS` tinyint(4) DEFAULT 0 COMMENT '서비스 이용약관 동의 여부 - 0:아니오, 1:예',
  `PROFILE_Y` tinyint(4) DEFAULT 0 COMMENT '사진 등록 여부 - 0:아니오, 1:예',
  `TEMP_PW_Y` tinyint(4) DEFAULT 0 COMMENT '임시 비밀번호 발급 여부 - 0:아니오, 1:예',
  `TEMP_PW` varchar(100) CHARACTER SET utf8mb3 DEFAULT NULL COMMENT '임시 비밀번호 - 발급이 예이면 값을 가짐',
  `TEMP_PW_ISSUE_DT` datetime DEFAULT NULL COMMENT '임시 비밀번호 발급 일시 - 사용 기간 제한위해 사용',
  `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
  `LAST_UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '수정 일시',
  `LAST_PW_UPD_DT` datetime DEFAULT NULL COMMENT '최종 패스워드 업데이트 일시',
  `AUTHKEY` int(11) DEFAULT 0 COMMENT '회원가입 인증 키',
  PRIMARY KEY (`IDX_USER`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='회원정보';

CREATE TABLE `TB_USER_LOGIN_HISTORY` (
  `idx` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `idx_user` int(11) unsigned NOT NULL COMMENT 'TB_USER IDX_USER',
  `remote_ip` varchar(30) NOT NULL COMMENT '접속 IP',
  `reg_dt` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록 일시',
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='회원 접속 정보 기록';

CREATE TABLE `TB_PROFILE_INFO` (
  `IDX_PROFILE_INFO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `IDX_USER` int(10) unsigned DEFAULT NULL COMMENT 'TB_USER IDX_USER',
  `FILE_PATH` varchar(1000) DEFAULT NULL,
  `FILE_NAME` varchar(100) DEFAULT NULL,
  `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
  PRIMARY KEY (`IDX_PROFILE_INFO`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='회원 사진 파일 정보';

CREATE TABLE `TB_MEETING` (
  `IDX_MEETING` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `IDX_USER` int(10) unsigned NOT NULL COMMENT '호스트 회원 INDEX - TB_USER IDX_USER',
  `MT_NAME` varchar(100) DEFAULT NULL COMMENT '미팅룸 명',
  `MT_START_DT` datetime DEFAULT NULL COMMENT '미팅 시작',
  `MT_END_DT` datetime DEFAULT NULL COMMENT '미팅 종료',
  `MT_REMIND_TYPE` tinyint(2) unsigned DEFAULT 0 COMMENT '되풀이 미팅 - 0:없음, 1:매일, 2:주, 3:월, 4:년',
  `MT_REMIND_COUNT` tinyint(1) DEFAULT 0 COMMENT '주기',
  `MT_REMIND_WEEK` varchar(20) DEFAULT NULL COMMENT '반복요일 - 일,월,화,수,목,금,토 = 1,2,3,4,5,6,7',
  `MT_REMIND_END` date DEFAULT NULL COMMENT '되풀이 미팅 종료일',
  `MT_INFO` varchar(3000) DEFAULT NULL COMMENT '미팅 설명',
  `MT_STATUS` tinyint(1) DEFAULT 0 COMMENT '미팅룸 상태 - 0:비공개, 1:공개, 2:취소, 3:삭제',
  `IS_LIVE` tinyint(1) DEFAULT 0 COMMENT '미팅 시작 여부 - 0:아니오, 1:예',
  `IS_LIVE_DT` datetime DEFAULT NULL COMMENT '미팅 시작 일시',
  `IS_FINISH` tinyint(1) DEFAULT 0 COMMENT '미팅 종료 여부 - 0:아니오, 1:예',
  `IS_FINISH_DT` datetime DEFAULT NULL COMMENT '미팅 종료 일시',
  `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
  `LAST_UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '수정 일시',
  `DELETE_STAT` tinyint(1) DEFAULT 0 COMMENT '삭제여부 - 0:미삭제, 1:삭제',
  `DEL_DT` datetime DEFAULT NULL COMMENT '삭제 일시',
  PRIMARY KEY (`IDX_MEETING`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='미팅룸 정보';

CREATE TABLE `TB_MEETING_USER_JOIN` (
  `IDX_MEETING_USER_JOIN` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `IDX_USER` int(10) unsigned DEFAULT NULL COMMENT '미팅 참가자 INDEX - TB_USER IDX_USER',
  `IDX_MEETING` int(10) unsigned DEFAULT NULL COMMENT '미팅룸 INDEX - TB_MEETING IDX_MEETING',
  `USER_EMAIL` varchar(400) DEFAULT NULL COMMENT '참가자 E-mail - 미등록 사용자도 초대해야 됨',
  `TOKEN` varchar(2000) DEFAULT NULL COMMENT 'AccessToken JWT',
  `SESSIONID` varchar(50) DEFAULT NULL COMMENT '미팅룸 고유 고정 값',
  `JOIN_DT` datetime DEFAULT NULL COMMENT '참가 일시',
  `IS_LIVE` tinyint(1) DEFAULT 0 COMMENT '참여 여부 - 0:미참여, 1:참여',
  `IS_ALIVE` tinyint(1) DEFAULT 0 COMMENT '감정분석 상태 - 0:미진행, 1:진행',
  `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
  `DELETE_STAT` tinyint(1) DEFAULT 0 COMMENT '삭제여부 - 0:미삭제, 1:삭제',
  `DEL_DT` datetime DEFAULT NULL COMMENT '삭제 일시',
  PRIMARY KEY (`IDX_MEETING_USER_JOIN`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='미팅에 초대된 참석자 연결 정보';

CREATE TABLE `TB_ATTACHMENT_FILE_INFO_JOIN` (
  `IDX_ATTACHMENT_FILE_INFO_JOIN` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `IDX_MEETING` int(10) unsigned DEFAULT NULL COMMENT '미팅룸 INDEX - TB_MEETING IDX_MEETING',
  `FILE_PATH` varchar(1000) DEFAULT NULL COMMENT '파일 경로',
  `FILE_NAME` varchar(100) DEFAULT NULL COMMENT '파일 이름',
  `FILE_SIZE` int(10) unsigned DEFAULT NULL COMMENT '파일 크기',
  `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
  `DELETE_STAT` tinyint(1) DEFAULT 0 COMMENT '삭제여부 - 0:미삭제, 1:삭제',
  `DEL_DT` datetime DEFAULT NULL COMMENT '삭제 일시',
  PRIMARY KEY (`IDX_ATTACHMENT_FILE_INFO_JOIN`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='미팅룸 첨부 파일 정보';

CREATE TABLE `TB_ANALYSIS` (
  `IDX_ANALYSIS` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `IDX_MEETING_USER_JOIN` int(10) DEFAULT NULL,
  `JOY` double DEFAULT NULL,
  `DISGUST` double DEFAULT NULL,
  `CONTEMPT` double DEFAULT NULL,
  `ANGER` double DEFAULT NULL,
  `FEAR` double DEFAULT NULL,
  `SURPRISE` double DEFAULT NULL,
  `VALENCE` double DEFAULT NULL,
  `SADNESS` double DEFAULT NULL,
  `ENGAGEMENT` double DEFAULT NULL,
  `TIMESTAMP` timestamp NULL DEFAULT NULL,
  `FILENAME` varchar(400) DEFAULT NULL,
  `SMILE` double DEFAULT NULL,
  `INNERBROWRAISE` double DEFAULT NULL,
  `BROWRAISE` double DEFAULT NULL,
  `BROWFURROW` double DEFAULT NULL,
  `NOSEWRINKLE` double DEFAULT NULL,
  `UPPERLIPRAISE` double DEFAULT NULL,
  `LIPCORNERDEPRESSOR` double DEFAULT NULL,
  `CHINRAISE` double DEFAULT NULL,
  `LIPPUCKER` double DEFAULT NULL,
  `LIPPRESS` double DEFAULT NULL,
  `LIPSUCK` double DEFAULT NULL,
  `MOUTHOPEN` double DEFAULT NULL,
  `SMIRK` double DEFAULT NULL,
  `EYECLOSURE` double DEFAULT NULL,
  `ATTENTION` double DEFAULT NULL,
  `LIDTIGHTEN` double DEFAULT NULL,
  `JAWDROP` double DEFAULT NULL,
  `DIMPLER` double DEFAULT NULL,
  `EYEWIDEN` double DEFAULT NULL,
  `CHEEKRAISE` double DEFAULT NULL,
  `LIPSTRETCH` double DEFAULT NULL,
  `REG_DT` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`IDX_ANALYSIS`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='감정 분석 데이터';

CREATE TABLE `TB_MEETING_MOVIE_FILE` (
  `IDX_MOVIE_FILE` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `IDX_MEETING` int(10) unsigned DEFAULT NULL COMMENT '미팅룸 INDEX - TB_MEETING IDX_MEETING',
  `FILE_PATH` varchar(1000) DEFAULT NULL COMMENT '파일 경로',
  `FILE_NAME` varchar(100) DEFAULT NULL COMMENT '파일 이름',
  `FILE_SIZE` int(10) unsigned DEFAULT NULL COMMENT '파일 크기',
  `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
  `DELETE_STAT` tinyint(1) DEFAULT 0 COMMENT '삭제여부 - 0:미삭제, 1:삭제',
  `DEL_DT` datetime DEFAULT NULL COMMENT '삭제 일시',
  PRIMARY KEY (`IDX_MOVIE_FILE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='미팅룸 동영상 파일 정보';

CREATE TABLE `TB_ANALYSIS_FILE` (
    `IDX_ANALYSIS_FILE_JOIN` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `IDX_ANALYSIS` bigint(20) DEFAULT NULL COMMENT '감정 분석 데이터 INDEX - TB_ANALYSIS IDX_ANALYSIS',
    `FILE_PATH` varchar(1000) DEFAULT NULL COMMENT '파일 경로',
    `FILE_NAME` varchar(100) DEFAULT NULL COMMENT '파일 이름',
    `FILE_SIZE` int(10) unsigned DEFAULT NULL COMMENT '파일 크기',
    `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
    `DELETE_STAT` tinyint(1) DEFAULT 0 COMMENT '삭제여부 - 0:미삭제, 1:삭제',
    `DEL_DT` datetime DEFAULT NULL COMMENT '삭제 일시',
    PRIMARY KEY (`IDX_ANALYSIS_FILE_JOIN`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='감정분석 데이터 내 파일 정보';

CREATE TABLE `TB_MAIL` (
  `IDX_MAIL_RESERVED` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `IDX_USER` int(10) unsigned DEFAULT NULL COMMENT '발송회원 ',
  `RECEIVER` varchar(400) DEFAULT NULL COMMENT '메일주소(회원메일) ',
  `SENDTIME` datetime DEFAULT current_timestamp() COMMENT '발송시간 ',
  `TITLE` varchar(1000) DEFAULT NULL COMMENT '메일 제목 ',
  `CONTENT` varchar(2000) DEFAULT NULL COMMENT '메일 내용 ',
  `MAIL_TYPE` tinyint(4) DEFAULT NULL COMMENT '0:회원가입인증 , 1:비밀번호찾기 , ...',
  PRIMARY KEY (`IDX_MAIL_RESERVED`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='메일 발송 정보 ';