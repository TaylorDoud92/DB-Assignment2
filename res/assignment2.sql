SET SERVEROUTPUT ON


--Function to check flag
CREATE OR REPLACE FUNCTION func_check_payroll
 RETURN CHAR
 IS
 
 v_flag			CHAR(1);
 
 BEGIN
	
	SELECT Payroll
	INTO v_flag
	FROM PAYROLL_PROCESSING;
	
	RETURN v_flag;
	
 END;
 /
 
 DECLARE 
 flag			BOOLEAN;
 
 BEGIN
 flag:= func_check_payroll();
 IF flag = true then
 dbms_output.put_line('true');
 end if;
 END;
 /
 
 
--Procedure to set the flag to false
CREATE OR REPLACE PROCEDURE proc_set_flag_false AS
 
 BEGIN
  UPDATE PAYROLL_PROCESSING 
  SET Payroll = 'N';
 END;
 /
 
--Trigger for payroll entries.
CREATE OR REPLACE TRIGGER load_new_transactions_BIR
BEFORE INSERT ON PAYROLL_LOAD
FOR EACH ROW

DECLARE
 

BEGIN

INSERT INTO NEW_TRANSACTIONS (Transaction_no, Transaction_date, Description, Account_no, Transaction_type, Transaction_amount)
VALUES (wkis_seq.NEXTVAL,:NEW.Payroll_date,'Payroll Processed',2050,'C',:NEW.Amount);

INSERT INTO NEW_TRANSACTIONS (Transaction_no, Transaction_date, Description, Account_no, Transaction_type, Transaction_amount)
VALUES (wkis_seq.NEXTVAL,:NEW.Payroll_date,'Payroll Processed',4045,'D',:NEW.Amount);

NEW.STATUS:='G';


EXCEPTION
		WHEN OTHERS THEN
		NEW.STATUS = 'B';
	 END;

END;
/

--Procedure to set the flag to true
CREATE OR REPLACE PROCEDURE proc_set_flag_true AS
 
 BEGIN
  UPDATE PAYROLL_PROCESSING 
  SET Payroll = 'Y';
 END;
 /
 
 --Part3
 --Function to check month end Flag
CREATE OR REPLACED FUNCTION func_check_month_end
 RETURN BOOLEAN
 IS
 
 v_flag			CHAR(1);
 
 BEGIN
	
	SELECT Month_end
	INTO v_flag
	FROM PAYROLL_PROCESSING;
	
	IF v_flag = 'Y' THEN
	RETURN TRUE;
	ELSIF v_flag = 'N' THEN
	RETURN FALSE;
	END IF;
 END;
 /
 
 --Procedure to change the monthend flag to false;
CREATE OR REPLACE PROCEDURE proc_set_monthendflag_false AS
 
 BEGIN
  UPDATE PAYROLL_PROCESSING 
  SET Month_end = 'N';
 END;
 /
 
 --Procedure to balance the Revenue accounts
CREATE OR REPLACE PROCEDURE proc_balance_rev AS
 
 CURSOR cur_revenue IS
  SELECT *
  FROM ACCOUNT
  WHERE Account_type_code = 'RE';
  
  CURSOR cur_expense IS
   SELECT *
   FROM ACCOUNT
   WHERE Account_type_code = 'EX';
  
  v_count				NUMBER;
  
  BEGIN
   FOR rec_revenue IN cur_revenue LOOP
	
	v_count := wkis_seq.NEXTVAL;
	
	INSERT INTO NEW_TRANSACTIONS(Transaction_no, Transaction_date, Description, Account_no, Transaction_type, Transaction_amount)
	VALUES(v_count,sysdate, 'Month End Balancing',rec_revenue.account_no,'D',rec_revenue.account_balance);
	
	INSERT INTO NEW_TRANSACTIONS(Transaction_no, Transaction_date, Description, Account_no, Transaction_type, Transaction_amount)
	VALUES(v_count,sysdate, 'Month End Balancing',5555,'C',rec_revenue.account_balance);
	
   END LOOP;
   FOR rec_expense IN cur_expense LOOP
	
	v_count := wkis_seq.NEXTVAL;
	 
	INSERT INTO NEW_TRANSACTIONS(Transaction_no, Transaction_date, Description, Account_no, Transaction_type, Transaction_amount)
	VALUES(v_count,sysdate, 'Month End Balancing',rec_expense.account_no,'C',rec_expense.account_balance);
	
	INSERT INTO NEW_TRANSACTIONS(Transaction_no, Transaction_date, Description, Account_no, Transaction_type, Transaction_amount)
	VALUES(v_count,sysdate, 'Month End Balancing',5555,'D',rec_expense.account_balance);
   END LOOP;
   COMMIT;
   END;
   /
 
 
 
 
 
--Part 4
--Exporting Transactional Data to delimited file

CREATE OR REPLACE PROCEDURE proc_export
(p_alias IN )

DECLARE

CURSOR cur_trans IS
 SELECT * 
 FROM new_transactions
 
 
 
BEGIN


 
 --Procedure to change the monthend flag to true;
 CREATE OR REPLACE PROCEDURE proc_set_monthendflag_true AS
 
 BEGIN
  UPDATE PAYROLL_PROCESSING 
  SET Month_end = 'Y';
 END;
 /
 
 --Step 4
 --Export to File
 
 CREATE OR REPlACE procedure proc_populate_export_file
 (p_alias IN VARCHAR2, p_filename IN VARCHAR2)
 IS
 
 CURSOR cur_trans IS
 SELECT * 
 FROM new_transactions
 FOR UPDATE;

 v_file				UTL_FILE.FILE_TYPE;
 
 BEGIN
	 
	 SELECT USER
	 INTO v_user
	 FROM DUAL;
	 
	 v_file := UTL_FILE.FOPEN(p_alias,p_filename,'W');
	 
	 FOR rec_account IN cur_trans LOOP
	
	 UTL_FILE.PUT_LINE(v_file, USER || ';' || rec_account.Transaction_no || ';' || rec_account.transaction_date || ';' || rec_account.description || ';' ||rec_account.Account_no|| ';' || rec_account.transaction_type || ';' || rec_account.transaction_amount);
	 
	 END LOOP;
  	
	 UTL_FILE.fclose(v_file);
 END;
 /
s
 --Step 5
 --Access Log table creation
CREATE TABLE ACCESS_LOG
(Account_no				NUMBER,
 Account_name			VARCHAR2(30),
 Account_type_code		VARCHAR2(2),
 Account_balance		NUMBER,
 UNAME 					VARCHAR2(25),
 CDATE				 	DATE,
 ATYPE 	    			VARCHAR2(6)
);

--Logging Trigger
CREATE OR REPLACE TRIGGER log_trigger
AFTER INSERT OR DELETE OR UPDATE ON ACCOUNT
FOR EACH ROW

DECLARE

 Account_type					VARCHAR2(6);

BEGIN 

 IF INSERTING THEN

	Account_type:='INSERT';

 ELSIF UPDATING THEN

	Account_type:='UPDATE';

 ELSIF DELETING THEN

	Account_type:='DELETE';

 END IF;

 INSERT INTO ACCESS_LOG(Account_no, Account_name, Account_type_code, Account_balance, uname, cdate, atype)
 VALUES(:OLD.Account_no, :OLD.Account_name, :OLD.Account_type_code, :OLD.Account_balance,USER,sysdate, Account_type);

END;
/

 
 
 
 