# Working with balances and transactions

Learn about financial account balances and the effect transactions have on them.

[Financial accounts](https://docs.stripe.com/financial-accounts/connect/account-management/financial-accounts.md) have their own balance separate from the balance of the account they’re attached to (platform account or connected account). `Balance` objects record the amount of funds in a financial account and their state of availability. `Transaction` and `TransactionEntry` objects debit or credit the funds in that balance.

## Balances

A financial account has a balance of funds. The sum total of the balance isn’t always available for spending, however, as it might include pending transactions into or out of the financial account. The financial account balance contains three properties that define the availability of its funds:

- `cash`—funds the user can spend right now.
- `inbound_pending`—funds not spendable yet, but that will become available at a later time. The `inbound_pending` property is reserved for future functionality and always has a value of 0.
- `outbound_pending`—funds in the account, but not spendable because they’re being held for pending outbound flows.

Use `GET /v1/treasury/financial_accounts/{{FINANCIAL_ACCOUNT_ID}}` to retrieve the balance details of a financial account with the associated ID. Provide the `Stripe-Account` header if the financial account is attached to one of your connected accounts. If the financial account is attached to your platform account, don’t include the `Stripe-Account` header.

```curl
curl https://api.stripe.com/v1/treasury/financial_accounts/{{TREASURYFINANCIALACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe treasury financial_accounts retrieve {{TREASURYFINANCIALACCOUNT_ID}} \
  --stripe-account {{CONNECTEDACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

financial_account = client.v1.treasury.financial_accounts.retrieve(
  '{{TREASURYFINANCIALACCOUNT_ID}}',
  {},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
financial_account = client.v1.treasury.financial_accounts.retrieve(
  "{{TREASURYFINANCIALACCOUNT_ID}}",
  options={"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$financialAccount = $stripe->treasury->financialAccounts->retrieve(
  '{{TREASURYFINANCIALACCOUNT_ID}}',
  [],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

FinancialAccountRetrieveParams params = FinancialAccountRetrieveParams.builder().build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

FinancialAccount financialAccount =
  client.v1().treasury().financialAccounts().retrieve(
    "{{TREASURYFINANCIALACCOUNT_ID}}",
    params,
    requestOptions
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const financialAccount = await stripe.treasury.financialAccounts.retrieve(
  '{{TREASURYFINANCIALACCOUNT_ID}}',
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TreasuryFinancialAccountRetrieveParams{}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryFinancialAccounts.Retrieve(
  context.TODO(), "{{TREASURYFINANCIALACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.FinancialAccounts;
Stripe.Treasury.FinancialAccount financialAccount = service.Get(
    "{{TREASURYFINANCIALACCOUNT_ID}}",
    null,
    requestOptions);
```

If successful, the response is a [FinancialAccount](https://docs.stripe.com/api/treasury/financial_accounts.md) object with a `balance` hash that details the funds and their availability.

```json
{
  "object": "treasury.financial_account",
  "id": "{{FINANCIAL_ACCOUNT_ID}}",
  ...
  "balance": {
    // $90 is currently available for use,
    // with an additional $10 held in the outbound_pending sub-balance
    "cash": {"usd": 9000},
    "inbound_pending": {"usd": 0},
    "outbound_pending": {"usd": 1000}
  }
}
```

### Negative balances and overdrafts 

If your connected account has a negative balance (for example, if your financial account receives an ACH credit that gets reversed), you’re responsible for restoring it to 0 USD. After 180 days of staying negative, Stripe debits your platform. We also contact you if individual or aggregate balances exceed our risk limits.

We recommend that you monitor your connected accounts to retrieve funds for their negative balances. You can top up funds into your financial account using [Inbound Transfers](https://docs.stripe.com/financial-accounts/connect/moving-money/into/inbound-transfers.md) or [Stripe Payouts](https://docs.stripe.com/financial-accounts/connect/moving-money/payouts.md). Make sure that you regularly monitor your connected account balances and reach out promptly.

After a financial account has been negative for 180 days, Stripe debits funds from your platform’s financial account and emails you ahead of time. If the transfer fails due to insufficient funds, Stripe contacts you with next steps.

## Transactions

All changes to a balance have a corresponding [Transaction](https://docs.stripe.com/api/treasury/transactions.md) object that details money movements. Transactions affect only one balance and are in only one currency (currently, Financial Accounts for platforms supports only USD).

Each transaction points to the balance-affecting money movement object, such as an [OutboundTransfer](https://docs.stripe.com/api/treasury/outbound_transfers.md), [ReceivedCredit](https://docs.stripe.com/api/treasury/received_credits.md), or [ReceivedDebit](https://docs.stripe.com/api/treasury/received_debits.md).

### Transaction state machine

| Status   | State applied | Description                                                                                                                                                                        | Transitions to     |
| -------- | ------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------ |
| `open`   | initial       | This is the initial state for all transactions. The transaction results in updates to the sub-balance amounts, but the current balance isn’t affected until the transaction posts. | `posted` or `void` |
| `posted` | terminal      | Funds have successfully entered or left the account. The current balance was affected.                                                                                             | N/A                |
| `void`   | terminal      | The transaction never impacted the balance. For example, a transaction enters this state if an outbound payment was initiated but then canceled before the funds left the account. | N/A                |

The available `Transaction` endpoints enable you to retrieve specific transactions and list or filter transactions affecting a financial account. There are no webhooks available for transactions, but webhooks are available for the associated money movement objects (for example, `OutboundPayments`).

## Retrieve a transaction

Use `GET/v1/treasury/transactions/{{TRANSACTION_ID}}` to retrieve the transaction with the associated ID.

```curl
curl https://api.stripe.com/v1/treasury/transactions/txn_123 \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe treasury transactions retrieve txn_123 \
  --stripe-account {{CONNECTEDACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transaction = client.v1.treasury.transactions.retrieve(
  'txn_123',
  {},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transaction = client.v1.treasury.transactions.retrieve(
  "txn_123",
  options={"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transaction = $stripe->treasury->transactions->retrieve(
  'txn_123',
  [],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransactionRetrieveParams params = TransactionRetrieveParams.builder().build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Transaction transaction =
  client.v1().treasury().transactions().retrieve("txn_123", params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transaction = await stripe.treasury.transactions.retrieve(
  'txn_123',
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TreasuryTransactionRetrieveParams{}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryTransactions.Retrieve(context.TODO(), "txn_123", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.Transactions;
Stripe.Treasury.Transaction transaction = service.Get("txn_123", null, requestOptions);
```

If successful, the response returns the `Transaction` object.

#### JSON (commented)

```json
{
  "id": "{{TRANSACTION_ID}}",
  "object": "treasury.transaction",
  "created": "{{Timestamp}}",
  "livemode": false,
  // The FinancialAccount this Transaction impacts
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  // The flow responsible for this Transaction. Each Transaction is created
  // synchronously (that is, in the same API request for initiated objects) with
  // its flow.
  "flow": "{{FLOW_OBJECT_ID}}",
  "flow_type": "inbound_transfer" |
             "outbound_payment" |
             "outbound_transfer" |
             "received_credit" |
             "received_debit" |
             "received_hold" |
             "issuing_dispute" |
             "credit_reversal" |
             "debit_reversal" |
             "other"
  "flow_details": null, // Includable, see user_expandable polymorphic object
  // Transaction state machine: open → posted | void
  // Transactions transition to posted when the amount is non-zero.
  // Transactions transition to void when the amount is zero AND the
  // sub-balance amounts are also zero.
  // Transactions are immutable once (posted && inbound_pending = 0) || void
  "status": "open" | "posted" | "void",
  // posted_at: When status changed from open -> posted
  // void_at: When status changed from open -> void
  // At most one of these may be set, because both posted and
  // void are terminal
  "status_transitions": {
    "posted_at": "{{?Timestamp}}",
    "voided_at": "{{?Timestamp}}"
  },
  // Transactions impact a single currency.
  "currency": "usd",
  // When status:
  //   open: This describes the projected change to the current balance.
  //         It can still change
  //   posted: The actual change to the current balance.
  //         Can no longer change
  //   void: Always 0 (the actual change to the current balance).
  //         Can no longer change
  "amount": 10000,
  "balance_impact": {
    "cash": 0,
    "inbound_pending": 10000,
    "outbound_pending": 0
  },
  // Freeform en-US string that describes this Transaction
  "description": "check deposit",
  "treasury": {
    // Set when the transaction transitions to `posted`.
    // The `financial_account.account.balance` amount as of `posted_at`
    // in `currency`.
    //
    // [DEPRECATION WARNING]: this field will be removed in the future.
    // Please avoid using it in new integrations.
    "current_balance_amount": "{{?Integer}}",
  },
  "entries": { // includable
    "object": "list",
    "data": [
      {
        "id": "{{TRANSACTION_ENTRY_ID}}",
        "object": "treasury.transaction_entry",
        ...
      }
    ],
    "has_more": false,
    "url": "/v1/treasury/transaction_entries?financial_account={{FINANCIAL_ACCOUNT_ID}}&transaction={{TRANSACTION_ID}}",
  }
}
```

#### JSON

```json
{
  "id": "{{TRANSACTION_ID}}",
  "object": "treasury.transaction",
  "created": "{{Timestamp}}",
  "livemode": false,
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  "flow": "{{FLOW_OBJECT_ID}}",
  "flow_type": "inbound_transfer" |
             "outbound_payment" |
             "outbound_transfer" |
             "received_credit" |
             "received_debit" |
             "received_hold" |
             "issuing_dispute" |
             "credit_reversal" |
             "debit_reversal" |
             "other",
  "flow_details": null,
  "status": "open" | "posted" | "void",
  "status_transitions": {
    "posted_at": "{{?Timestamp}}",
    "voided_at": "{{?Timestamp}}"
  },
  "currency": "usd",
  "amount": 10000,
  "balance_impact": {
    "cash": 0,
    "inbound_pending": 10000,
    "outbound_pending": 0
  },
  "description": "check deposit",
  "treasury": {
    "current_balance_amount": "{{?Integer}}",
  },
  "entries": {
    "object": "list",
    "data": [
      {
        "id": "{{TRANSACTION_ENTRY_ID}}",
        "object": "treasury.transaction_entry",
      }
    ],
    "has_more": false,
    "url": "/v1/treasury/transaction_entries?financial_account={{FINANCIAL_ACCOUNT_ID}}&transaction={{TRANSACTION_ID}}",
  }
}
```

### List Transactions

Use `GET /v1/treasury/transactions` to list transactions for a financial account. Set the required `financial_account` parameter in the body to the value of the financial account ID to retrieve transactions for. Include additional parameters to filter the results returned.

In addition to the [standard set of list parameters](https://docs.stripe.com/api/pagination.md), you can filter transactions by the following.

- `status`
- `flow`
- Either `created` or `posted_at`, but not both

```json
{
  // Standard list parameters
  limit, starting_after, ending_before,
  // Filter by FinancialAccount, required
  financial_account: "{{FINANCIAL_ACCOUNT_ID}}"
  // Filter by status
  status: "open" | "posted" | "void",
  // Filter by flow
  flow: "{{FLOW_OBJECT_ID}}",
  // Order the results by the created or posted_at timestamps, default is `created`.
  // For order_by=posted_at, setting status='posted' is required
  order_by: "created" | "posted_at",
  // created can only be specified with order_by = 'created'
  created: {gt, gte, lt, lte},
  status_transitions: {
    // status_transitions.posted_at can only be specified with order_by = 'posted_at' and status = 'posted'
    posted_at: {gt, gte, lt, lte}
  }
}
```

The following request retrieves the three most recent transactions created on the financial account that have a `status` of `posted`.

```curl
curl -G https://api.stripe.com/v1/treasury/transactions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d financial_account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  -d limit=3 \
  -d status=posted \
  -d order_by=created
```

```cli
stripe treasury transactions list  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --financial-account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  --limit=3 \
  --status=posted \
  --order-by=created
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transactions = client.v1.treasury.transactions.list(
  {
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    limit: 3,
    status: 'posted',
    order_by: 'created',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transactions = client.v1.treasury.transactions.list(
  {
    "financial_account": "{{TREASURYFINANCIALACCOUNT_ID}}",
    "limit": 3,
    "status": "posted",
    "order_by": "created",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transactions = $stripe->treasury->transactions->all(
  [
    'financial_account' => '{{TREASURYFINANCIALACCOUNT_ID}}',
    'limit' => 3,
    'status' => 'posted',
    'order_by' => 'created',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransactionListParams params =
  TransactionListParams.builder()
    .setFinancialAccount("{{TREASURYFINANCIALACCOUNT_ID}}")
    .setLimit(3L)
    .setStatus(TransactionListParams.Status.POSTED)
    .setOrderBy(TransactionListParams.OrderBy.CREATED)
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

StripeCollection<Transaction> stripeCollection =
  client.v1().treasury().transactions().list(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transactions = await stripe.treasury.transactions.list(
  {
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    limit: 3,
    status: 'posted',
    order_by: 'created',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TreasuryTransactionListParams{
  FinancialAccount: stripe.String("{{TREASURYFINANCIALACCOUNT_ID}}"),
  Status: stripe.String(stripe.TreasuryTransactionStatusPosted),
  OrderBy: stripe.String("created"),
}
params.Limit = stripe.Int64(3)
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result := sc.V1TreasuryTransactions.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.TransactionListOptions
{
    FinancialAccount = "{{TREASURYFINANCIALACCOUNT_ID}}",
    Limit = 3,
    Status = "posted",
    OrderBy = "created",
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.Transactions;
StripeList<Stripe.Treasury.Transaction> transactions = service.List(
    options,
    requestOptions);
```

### Webhooks 

There are no webhooks for transactions because the various money movements that initiate a transaction have their own webhooks.

## Transaction entries

[TransactionEntry](https://docs.stripe.com/api/treasury/transaction_entries.md) objects are the most granular view of money movements that affect a financial account balance. A single flow of money comprises multiple individual money movements, each represented by a transaction. Transactions, in turn, are an aggregation of transaction entries. For example, when initiating an outbound payment of 10 USD at time T, funds are moved from the `cash` sub-balance to the `outbound_pending` sub-balance. The following `Transaction` object response demonstrates this initial event.

```json
{
  "id": "{{TRANSACTION_ID}}",
  "object": "treasury.transaction",
  "created": "{{T}}",
  ...
  "flow": "{{OUTBOUND_PAYMENT_ID}}",
  "flow_type": "outbound_payment",
  "status": "open",
  "amount": -1000,
  "currency": "usd",
  "balance_impact": {
    "cash": -1000,
    "inbound_pending": 0,
    "outbound_pending": 1000,
  },
  "entries": {
    "data": [
      {
        "id": "{{TRANSACTION_ENTRY_ID}}",
        "object": "treasury.transaction_entry",
        ...
        "created": "{{T}}",
        "effective_at": "{{T}}",
        "currency": "usd",
        "balance_impact": {
          "cash": -1000,
          "inbound_pending": 0,
          "outbound_pending": 1000,
        }
      }
    ],
    "has_more": false,
    "object": "list",
    "url": "/v1/treasury/transaction_entries?financial_account=fa_123&transaction=trxn_123",
  }
}
```

After the outbound payment posts at time T+1, the funds are deducted from `outbound_pending` and a new transaction entry is added to the transaction. The following `Transaction` response demonstrates this progression.

```json
{
  "id": "{{TRANSACTION_ID}}",
  "object": "treasury.transaction",
  "created": "{{T}}",
  ...
  "flow": "{{OUTBOUND_PAYMENT_ID}}",
  "flow_type": "outbound_payment",
  "status": "posted",
  "amount": -1000,
  "currency": "usd",
  "balance_impact": {
    "cash": -1000,
    "inbound_pending": 0,
    "outbound_pending": 0,
  },
  "entries": {
    "data": [
      {
        "id": "{{TRANSACTION_ENTRY_ID}}",
        "object": "treasury.transaction_entry",
        ...
        "created": "{{T+1}}",
        "effective_at": "{{T+1}}",
        "currency": "usd",
        "balance_impact": {
          "cash": 0,
          "inbound_pending": 0,
          "outbound_pending": -1000,
        }
      },
      {
        "id": "{{TRANSACTION_ENTRY_ID}}",
        "object": "treasury.transaction_entry",
        ...
        "created": "{{T}}",
        "effective_at": "{{T}}",
        "currency": "usd",
        "balance_impact": {
          "cash": -1000,
          "inbound_pending": 0,
          "outbound_pending": 1000,
        }
      }
    ],
    "has_more": false,
    "object": "list",
    "url": "/v1/treasury/transaction_entries?financial_account={{FINANCIAL_ACCOUNT_ID}}&transaction={{TRANSACTION_ID}}",
  }
}
```

As the preceding responses show, a transaction can contain multiple transaction entries. The available `TransactionEntry` endpoints enable you to retrieve specific transaction entries and list or filter them for a particular transaction.

A `Transaction` in the `void` status won’t have any new transaction entries added to it. A `Transaction` in the `posted` status where all `balance_impact` is to the `cash` sub-balance won’t have any new transaction entries added to it, either.

### Retrieve transaction entries

Use `GET /v1/treasury/transaction_entries/{{TRANSACTIONENTRY_ID}}` to retrieve details for the transaction entry with the associated ID.

```curl
curl https://api.stripe.com/v1/treasury/transaction_entries/{{TRANSACTION_ENTRY_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe treasury transaction_entries retrieve {{TRANSACTION_ENTRY_ID}} \
  --stripe-account {{CONNECTEDACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transaction_entry = client.v1.treasury.transaction_entries.retrieve(
  '{{TRANSACTION_ENTRY_ID}}',
  {},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transaction_entry = client.v1.treasury.transaction_entries.retrieve(
  "{{TRANSACTION_ENTRY_ID}}",
  options={"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transactionEntry = $stripe->treasury->transactionEntries->retrieve(
  '{{TRANSACTION_ENTRY_ID}}',
  [],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransactionEntryRetrieveParams params = TransactionEntryRetrieveParams.builder().build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

TransactionEntry transactionEntry =
  client.v1().treasury().transactionEntries().retrieve(
    "{{TRANSACTION_ENTRY_ID}}",
    params,
    requestOptions
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transactionEntry = await stripe.treasury.transactionEntries.retrieve(
  '{{TRANSACTION_ENTRY_ID}}',
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TreasuryTransactionEntryRetrieveParams{}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryTransactionEntries.Retrieve(
  context.TODO(), "{{TRANSACTION_ENTRY_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.TransactionEntries;
Stripe.Treasury.TransactionEntry transactionEntry = service.Get(
    "{{TRANSACTION_ENTRY_ID}}",
    null,
    requestOptions);
```

If successful, the response returns a `TransactionEntry` object with the following form.

```json
{
  "id": "{{TRANSACTION_ENTRY_ID}}",
  "object": "treasury.transaction_entry",
  "created": "{{Timestamp}}",
  "livemode": false,
  // The FinancialAccount this transaction entry impacts.
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  // The transaction that this transaction entry belongs to.
  "transaction": "{{TRANSACTION_ID}}",
  // The flow responsible for this transaction entry.
  "flow": "{{FLOW_OBJECT_ID}}",
  "flow_type": "inbound_transfer" |
             "outbound_payment" |
             "outbound_transfer" |
             "received_credit" |
             "received_debit" |
             "received_hold" |
             "issuing_dispute" |
             "credit_reversal" |
             "debit_reversal" |
             "other",
  "flow_details": null, // Includable, see user_expandable polymorphic object
  // type describes the specific money movement that generated the transaction entry.
  "type": "outbound_payment" |
        "outbound_payment_cancellation" |
        "outbound_payment_failure" |
        "outbound_payment_posting" |
        "outbound_payment_return" |
        "outbound_transfer" |
        "outbound_transfer_cancellation" |
        "outbound_transfer_failure" |
        "outbound_transfer_posting" |
        "outbound_transfer_return" |
        "received_credit" |
        "received_debit" |
        "received_hold" |
        "received_hold_release" |
        "credit_reversal" |
        "credit_reversal_posting" |
        "debit_reversal" |
        "stripe_fee" |
        "inbound_transfer" |
        "other",
  // effective_at describes when the transaction entry impacted, or will impact, the FinancialAccount's balance.
  "effective_at": "{{Timestamp}}",
  // `effective` if `effective`_at` is in the past, otherwise `scheduled`.`
  "status": "effective" | "scheduled",
  // Transaction entries impact a single currency.
  "currency": "usd",
  // balance_impact describes the change to each sub-balance for this transaction entry.
  "balance_impact": {
    "cash": 0,
    "inbound_pending": 10000,
    "outbound_pending": 0
  }
}
```

### List TransactionEntries

Use `GET /v1/treasury/transaction_entries` to list the transaction entries for a financial account. Set the required `financial_account` parameter in the body to the value of the financial account ID to retrieve transaction entries for. Include additional parameters if you want to filter the list.

In addition to the [standard set of list parameters](https://docs.stripe.com/api/pagination.md), you can filter transaction entries by:

- `transaction`
- Either `created` or `effective_at`, but not both

```json
{
  // Standard list parameters
  limit, starting_after, ending_before,
  // Filter by FinancialAccount, required
  financial_account: "fa_123"
  // Filter by transaction
  transaction: 'trxn_123',
  // Order the results by the created or effective_at timestamps, default is `created`.
  order_by: "created" | "effective_at",
  // created can only be specified with order_by = 'created'
  created: {gt, gte, lt, lte},
  // effective_at can only be specified with order_by = 'effective_at'
  effective_at: {gt, gte, lt, lte},
}
```

The following request retrieves the transaction entries created before `{{Timestamp}}` and orders them by `created` date.

```curl
curl -G https://api.stripe.com/v1/treasury/transaction_entries \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d financial_account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  -d order_by=created \
  -d "created[lt]"=1234567890
```

```cli
stripe treasury transaction_entries list  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --financial-account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  --order-by=created \
  -d "created[lt]"=1234567890
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transaction_entries = client.v1.treasury.transaction_entries.list(
  {
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    order_by: 'created',
    created: {lt: 1234567890},
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transaction_entries = client.v1.treasury.transaction_entries.list(
  {
    "financial_account": "{{TREASURYFINANCIALACCOUNT_ID}}",
    "order_by": "created",
    "created": {"lt": 1234567890},
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transactionEntries = $stripe->treasury->transactionEntries->all(
  [
    'financial_account' => '{{TREASURYFINANCIALACCOUNT_ID}}',
    'order_by' => 'created',
    'created' => ['lt' => 1234567890],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransactionEntryListParams params =
  TransactionEntryListParams.builder()
    .setFinancialAccount("{{TREASURYFINANCIALACCOUNT_ID}}")
    .setOrderBy(TransactionEntryListParams.OrderBy.CREATED)
    .setCreated(TransactionEntryListParams.Created.builder().setLt(1234567890L).build())
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

StripeCollection<TransactionEntry> stripeCollection =
  client.v1().treasury().transactionEntries().list(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transactionEntries = await stripe.treasury.transactionEntries.list(
  {
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    order_by: 'created',
    created: {
      lt: 1234567890,
    },
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TreasuryTransactionEntryListParams{
  FinancialAccount: stripe.String("{{TREASURYFINANCIALACCOUNT_ID}}"),
  OrderBy: stripe.String("created"),
  CreatedRange: &stripe.RangeQueryParams{LesserThan: 1234567890},
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result := sc.V1TreasuryTransactionEntries.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.TransactionEntryListOptions
{
    FinancialAccount = "{{TREASURYFINANCIALACCOUNT_ID}}",
    OrderBy = "created",
    Created = new DateRangeOptions
    {
        LessThan = DateTimeOffset.FromUnixTimeSeconds(1234567890).UtcDateTime,
    },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.TransactionEntries;
StripeList<Stripe.Treasury.TransactionEntry> transactionEntries = service.List(
    options,
    requestOptions);
```

### Webhooks

There are no webhooks for transaction entries because the various money movements that initiate a transaction entry have their own webhooks.
