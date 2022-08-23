INSERT INTO CUSTOMER (uuid, prename, lastname, date_of_birth)
VALUES ('acb3d816-3f85-4d8b-abb1-eb0a35867e7e', 'prename1', 'lastname1', '2022-08-01'),
       ('cc8a721b-120b-4ed8-abc5-8a104f619dec', 'prename2', 'lastname2', '2022-08-01');

INSERT INTO ACCOUNT (UUID, ACC_NUMBER, IBAN, CUSTOMER_ID)
VALUES ('75849dbc-7dd7-43f3-bf48-b6e64cc975cc', 1, 'DE01', 1),
       ('9c9050a9-2814-45f9-9652-a006cdf89740', 2, 'DE02', 2);

INSERT INTO BALANCE (UUID, VALUE_IN_CENTS, LAST_UPDATE, ACCOUNT_ID)
VALUES ('4650c43b-52b3-40c8-bb22-f7b16337edf0', 1000, NULL, 1),
       ('99413696-8300-47d8-9bbe-126b4e27408e', 30, NULL, 2);

