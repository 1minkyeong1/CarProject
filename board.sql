-- 답변글을 달수 있고 페이징 처리가 가능한 게시판 board테이블 생성

CREATE TABLE board(

    b_idx number  PRIMARY KEY, -- 게시판의 글의 순서값(글번호)

    b_id  varchar2(20) not null, -- 글을 작성한 사람의 아이디

    b_pw  varchar2(10), -- 작성하는 글의 비밀번호 

    b_name varchar2(20), -- 글을 작성한 사람의 이름

    b_email varchar2(50), -- 글을 작성한 사람의 이메일

    b_title varchar2(100), -- 작성하는 글의 제목

    b_content varchar2(4000), -- 작성하는 글의 내용

    b_group number, -- 주글 과 답변글 그룹으로 묶어줄수 있는 그룹번호

    b_level number, -- 작성한 답변글의 들여쓰기 정도 레벨 값

    b_date Date, -- 글을 작성한 날짜

    b_cnt number, -- 글 조회수 

       -- id 컬럼을 회원테이블 member의  id컬럼에 대해 외래키로 지정합니다.

    CONSTRAINT FK_BOARD_b_ID FOREIGN KEY(b_id)

    REFERENCES member(id) ON DELETE CASCADE

);

?

?

참고.

?

--ON DELETE CASCADE

--참조되는 부모 테이블 행에 대한 DELETE를 허용한다.

--

--즉, 참조되는 부모 테이블 값이 삭제되면 연쇄적으로 자식 테이블 값 역시 삭제된다는 의미이다.

?
-- 시퀀스 생성

create sequence border_b_idx

       increment BY 1

       start with 1

       minvalue 1

       maxvalue 9999

       nocycle

       nocache

       noorder;
       
       

--  게시판 내용글 임시작성

INSERT INTO board (b_idx, b_id, b_pw, b_name, b_email, b_title, b_content, b_group, b_level, b_date, b_cnt)

SELECT border_b_idx.nextval, -- b_idx 값을 시퀀스로 자동 생성

       m.id AS b_id,           -- member 테이블의 id를 b_id로 삽입

       '1234' AS b_pw,    -- 기본 비밀번호 값 설정

       '김천재' AS b_name,       -- member 테이블의 name을 b_name으로 삽입

       m.email AS b_email,     -- member 테이블의 email을 b_email로 삽입

       '글제목3' AS b_title,     -- 기본 제목

       '글내용3' AS b_content, -- 기본 내용

       1 AS b_group,           -- 기본 그룹 번호 (필요에 따라 변경 가능)

       0 AS b_level,           -- 기본 들여쓰기 레벨

       SYSDATE AS b_date,      -- 현재 날짜를 작성 날짜로 설정

       0 AS b_cnt              -- 조회수를 0으로 설정

FROM member m;


commit; -- 영구 반영 



select * from board;


select * from member;

select email, name, id from member where id='admin';