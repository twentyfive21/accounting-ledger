
<p align="center">
  <img src="https://github.com/twentyfive21/accounting-ledger/assets/107441301/2f93ac24-595d-4cf3-ac0a-adecb8ce1caf" height="200">
</p>

# Capstone 1 - Accounting Ledger Application
---
## Overview : Welcome to the Financial Transaction Tracker repository! I'm thrilled to present this CLI application built using Java. Through user friendly screens  users can effortlessly manage their finances, from adding deposits to making payments (debits) and viewing transaction ledgers.

## Key Features:

- Efficiency: Simplifies financial tracking with easy-to-use screens.
- Functionality: Allows users to add deposits, make payments (debits), and view transaction ledgers.
- Data Integrity: All transactions are securely stored in a CSV file named transactions.csv, ensuring easy access and maintaining data integrity.
---

## Folder Structure: 
![homescreen](https://github.com/twentyfive21/accounting-ledger/assets/107441301/97bb6283-a824-41a2-aec3-559a0e58c688)
## Homscreen : This is where the main class is and the start of the application
![homescreenCode](https://github.com/twentyfive21/accounting-ledger/assets/107441301/d3b9836c-2119-4388-8cfb-7c6b51182c01)
## Error handling for homescreen :
![homescreen error](https://github.com/twentyfive21/accounting-ledger/assets/107441301/b881e78f-6348-4a45-98af-3869a81af2ab)
## Exit homscreen :
![exit homscreen](https://github.com/twentyfive21/accounting-ledger/assets/107441301/9068b0e2-1366-4cff-9021-a90d4e70c03c)

# ~~~~ Interesting piece of code ~~~~ 
## Efficient Input Handling:
- One of the intriguing aspects of this code is the method getUserInput, where I've implemented a streamlined approach to handle user input for both deposits and payments. Instead of having separate functions for deposit and payment inputs, I've consolidated them into one function. This not only reduces redundancy but also adheres to the DRY (Don't Repeat Yourself) principle, making the code more maintainable and clean.

## Dynamic Double Value:
- Another interesting feature is the dynamic handling of double values based on the transaction type. Rather than having separate logic to determine whether the value should be positive or negative, I've incorporated this functionality within the same method. By checking the transaction type (deposit or not), the code appropriately adjusts the sign of the price value. This approach enhances code readability and simplifies the implementation process.

## Integration with File Operations:
- Furthermore, this method seamlessly integrates with file operations, as it adds the transaction object to an ArrayList and writes the transaction details to a CSV file. This consolidation of functionality within a single method enhances code cohesion and facilitates easier maintenance in the future.

![interesting](https://github.com/twentyfive21/accounting-ledger/assets/107441301/4873e718-fd0b-478c-9686-c74d74a595f2)

---
## Deposit output / error-handling : 
### Output: ![deposit](https://github.com/twentyfive21/accounting-ledger/assets/107441301/09b3fd54-83c3-4df9-b23a-3e2e394cede1)
### Error handling : ![deposit error](https://github.com/twentyfive21/accounting-ledger/assets/107441301/1626b6b2-4786-41d8-bd04-6b457ded4980)

## Payment output / error-handling : 
### Output: ![payment](https://github.com/twentyfive21/accounting-ledger/assets/107441301/45a7ec7b-5b7a-4c5c-b630-63fc4936b3c9)
### Error handling : ![payment error](https://github.com/twentyfive21/accounting-ledger/assets/107441301/89df3484-66a5-48e0-a47c-b33b9a90ae1c)

# Ledger Homescreen / error handling : 
### Output : ![ledger home with output](https://github.com/twentyfive21/accounting-ledger/assets/107441301/6b22488e-69be-42a3-838a-1387f8cd928d)
### Error handling :![error ledger](https://github.com/twentyfive21/accounting-ledger/assets/107441301/77fb6049-3924-4a0d-8de4-f62c2baef390)

### Ledger deposit outputs : ![ledger deposits](https://github.com/twentyfive21/accounting-ledger/assets/107441301/28de7330-2157-4965-a1c3-b2c7c8142f33)
### Ledger payment outputs :![ledger payments](https://github.com/twentyfive21/accounting-ledger/assets/107441301/e264bbcd-015b-4ce1-ab17-edf55929c9db)
---
# Reports Homescreen:
### Reports output with option 1 : ![reports homscreen with output](https://github.com/twentyfive21/accounting-ledger/assets/107441301/833a9de3-f8d9-4547-8a78-6f2ce117a32d)
### Reports output with option 2 : ![report option2](https://github.com/twentyfive21/accounting-ledger/assets/107441301/f5039e5b-9faf-4dd4-90bd-b46db7b4d6b0)
### Reports output with option 3 : ![reports 3](https://github.com/twentyfive21/accounting-ledger/assets/107441301/a8329436-f72f-4477-82b3-3705d5b9bbcf)
### Reports output with option 4 : ![reports 4](https://github.com/twentyfive21/accounting-ledger/assets/107441301/cc7ab62e-662a-481d-aaee-54a343c94219)
### Reports output with option 5 : ![reports 5](https://github.com/twentyfive21/accounting-ledger/assets/107441301/15f2faee-c268-4b79-a74b-4a562d98b19c)
## Reports output with option 6 custom output 1 :![custom 1](https://github.com/twentyfive21/accounting-ledger/assets/107441301/0d134b12-536f-45b8-b089-0a5ad90104a7)
## Reports output with option 6 custom output 2 : ![custom 2](https://github.com/twentyfive21/accounting-ledger/assets/107441301/1de6f5ca-e6da-44c1-b616-fd1e358e0806)
## Reports output with option 6 custom output 3 :  ![custom 3](https://github.com/twentyfive21/accounting-ledger/assets/107441301/350ed4a1-3ba2-457c-8dd6-d02f67e4cb30)
---
## CSV transactions output : ![csv](https://github.com/twentyfive21/accounting-ledger/assets/107441301/7d71016f-1725-4019-9295-5c64861965d0)



