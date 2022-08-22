# senacor-intermission-new-java

## Target functionality

- Provide entities:
  - Customer (ID, Name)
  - Account (ID, IBAN, >Customer)
  - Balance (Value, Date, >Account)
  - Transaction (Value, Status, Date, Description, >Sender Acc., >Receiver Acc.)
- Provide APIs for:
  - Creating a transaction
  - Checking balance
  - Creating account
  - Get (all/one) accounts of customer
  - Get all transactions of account
  - DSGVO deletion (delete all account data)

