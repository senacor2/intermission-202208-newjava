#!/bin/bash
# Bash script to create transactions for benchmark purpose.

for index in {1..100}
do
    echo "Create transaction $index ..."
    curl -X POST -H "Content-Type: application/json" -d @create_req.json http://localhost:8081/accounts/75849dbc-7dd7-43f3-bf48-b6e64cc975cc/transactions
done
