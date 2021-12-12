CREATE TABLE accounts (
  id uuid NOT NULL,
  account_number varchar(255) NOT NULL,
  current_balance float8 NOT NULL,
  bank_account_name varchar(255) NOT NULL,
  created_date timestamp NOT NULL,
  created_by varchar(255),
  last_modified_date  timestamp NOT NULL,
  last_modified_by varchar(255),
  version integer NOT NULL,
  CONSTRAINT account_id PRIMARY KEY (id),
  CONSTRAINT account_number_key UNIQUE (account_number)
);

CREATE TABLE transactions (
  id uuid NOT NULL,
  amount float8 NOT NULL,
  tax_collected float8 NOT NULL,
  currency varchar(255) NOT NULL,
  origin_account varchar(255) NOT NULL,
  destination_account varchar(255) NOT NULL,
  description varchar(255) NOT NULL,
  created_date timestamp NOT NULL,
  created_by varchar(255),
  last_modified_date  timestamp NOT NULL,
  last_modified_by varchar(255),
  version integer NOT NULL,
  CONSTRAINT transaction_id PRIMARY KEY (id),
  CONSTRAINT fk_origin_account FOREIGN KEY (origin_account)
    REFERENCES accounts (account_number) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
  CONSTRAINT fk_destination_account FOREIGN KEY (destination_account)
    REFERENCES accounts (account_number) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);

INSERT INTO accounts (id, account_number, current_balance, bank_account_name, created_date, created_by, last_modified_date, version)
    VALUES('a82aec77-d20f-4712-8e77-435737e72110', '12345600', 10000, 'Name', current_timestamp, 'admin', current_timestamp, 1);

INSERT INTO accounts (id, account_number, current_balance, bank_account_name, created_date, created_by, last_modified_date, version)
    VALUES('44f5ba6f-f1b5-4130-801e-b6a1aadeada9', '12345601', 500, 'Name', current_timestamp, 'admin', current_timestamp, 1);

INSERT INTO transactions (id, amount, tax_collected, currency, origin_account, destination_account, description, created_date, created_by, last_modified_date, version)
	VALUES ('fc2ff7a9-59f4-4a33-918f-f974a7668dc1', 5000, 250, 'USD', '12345600', '12345601', 'Loan Pay', current_timestamp, 'admin', current_timestamp, 1);

INSERT INTO transactions (id, amount, tax_collected, currency, origin_account, destination_account, description, created_date, created_by, last_modified_date, version)
	VALUES ('69f3fbc7-2c5b-4293-b99e-d21952a0acc8', 2500, 125, 'USD', '12345600', '12345601', 'Loan Pay', current_timestamp, 'admin', current_timestamp, 1);

INSERT INTO transactions (id, amount, tax_collected, currency, origin_account, destination_account, description, created_date, created_by, last_modified_date, version)
	VALUES ('8e94dbf0-2526-4271-b7d3-4f25dc21ec1b', 300, 15, 'USD', '12345600', '12345601', 'Loan Pay', current_timestamp, 'admin', current_timestamp, 1);

INSERT INTO transactions (id, amount, tax_collected, currency, origin_account, destination_account, description, created_date, created_by, last_modified_date, version)
	VALUES ('97eeeec7-52a1-4963-bca5-37b4c4d263db', 5000, 250, 'USD', '12345601', '12345600', 'hi', current_timestamp, 'admin', current_timestamp, 1);

