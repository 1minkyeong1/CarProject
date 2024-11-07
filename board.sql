--
-- �亯���� �޼� �ְ� ����¡ ó���� ������ �Խ��� board���̺� ����

CREATE TABLE board(

    b_idx number  PRIMARY KEY, -- �Խ����� ���� ������(�۹�ȣ)

    b_id  varchar2(20) not null, -- ���� �ۼ��� ����� ���̵�

    b_pw  varchar2(10), -- �ۼ��ϴ� ���� ��й�ȣ 

    b_name varchar2(20), -- ���� �ۼ��� ����� �̸�

    b_email varchar2(50), -- ���� �ۼ��� ����� �̸���

    b_title varchar2(100), -- �ۼ��ϴ� ���� ����

    b_content varchar2(4000), -- �ۼ��ϴ� ���� ����

    b_group number, -- �ֱ� �� �亯�� �׷����� �����ټ� �ִ� �׷��ȣ

    b_level number, -- �ۼ��� �亯���� �鿩���� ���� ���� ��

    b_date Date, -- ���� �ۼ��� ��¥

    b_cnt number, -- �� ��ȸ�� 

       -- id �÷��� ȸ�����̺� member��  id�÷��� ���� �ܷ�Ű�� �����մϴ�.

    CONSTRAINT FK_BOARD_b_ID FOREIGN KEY(b_id)

    REFERENCES member(id) ON DELETE CASCADE

);

?

?

����.

?

--ON DELETE CASCADE

--�����Ǵ� �θ� ���̺� �࿡ ���� DELETE�� ����Ѵ�.

--

--��, �����Ǵ� �θ� ���̺� ���� �����Ǹ� ���������� �ڽ� ���̺� �� ���� �����ȴٴ� �ǹ��̴�.

?
-- ������ ����

create sequence border_b_idx

       increment BY 1

       start with 1

       minvalue 1

       maxvalue 9999

       nocycle

       nocache

       noorder;
       
       

--  �Խ��� ����� �ӽ��ۼ�

INSERT INTO board (b_idx, b_id, b_pw, b_name, b_email, b_title, b_content, b_group, b_level, b_date, b_cnt)

SELECT border_b_idx.nextval, -- b_idx ���� �������� �ڵ� ����

       m.id AS b_id,           -- member ���̺��� id�� b_id�� ����

       '1234' AS b_pw,    -- �⺻ ��й�ȣ �� ����

       '��õ��' AS b_name,       -- member ���̺��� name�� b_name���� ����

       m.email AS b_email,     -- member ���̺��� email�� b_email�� ����

       '������3' AS b_title,     -- �⺻ ����

       '�۳���3' AS b_content, -- �⺻ ����

       1 AS b_group,           -- �⺻ �׷� ��ȣ (�ʿ信 ���� ���� ����)

       0 AS b_level,           -- �⺻ �鿩���� ����

       SYSDATE AS b_date,      -- ���� ��¥�� �ۼ� ��¥�� ����

       0 AS b_cnt              -- ��ȸ���� 0���� ����

FROM member m;


commit; -- ���� �ݿ� 



select * from board;


select * from member;

select email, name, id from member where id='admin';