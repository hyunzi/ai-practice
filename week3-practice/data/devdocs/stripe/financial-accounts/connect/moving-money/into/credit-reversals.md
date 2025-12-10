# Moving money using CreditReversal objects

Learn how you can return funds from received credits that add money to your financial account.

Reversing a [ReceivedCredit](https://docs.stripe.com/api/treasury/received_credits.md) creates a [CreditReversal](https://docs.stripe.com/api/treasury/credit_reversals.md). You can reverse `ReceivedCredits` only in some scenarios (detailed in the following table). Whether you can reverse a `ReceivedCredit` depends on the network and source flow.

The `reversal_details` sub-hash on the `ReceivedCredit` object can have the following combination of values, which determines if you can reverse the `ReceivedCredit`.

| RESTRICTED REASON        | DEADLINE (EPOCH TIMESTAMP) | EXAMPLE SCENARIO                                                                                                                                                                                                                    |
| ------------------------ | -------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `source_flow_restricted` | `null`                     | A Stripe network `ReceivedCredit` that’s the result of a flow other than an `OutboundPayment`. Stripe restricts users from reversing such `ReceivedCredits`.                                                                        |
| `network_restricted`     | `null`                     | Network constraints prevent Stripe from allowing reversal on some `ReceivedCredits`, such as a `ReceivedCredit` from a wire transfer.                                                                                               |
| `null`                   | `{{TIMESTAMP}}`            | A `ReceivedCredit`, which is reversible, but only until the timestamp in `deadline`. ACH `ReceivedCredits` have a deadline that determines how long you have to reverse them.                                                       |
| `deadline_passed`        | `{{TIMESTAMP}}`            | A `ReceivedCredit` that’s reversible before the timestamp in `deadline`, but is no longer reversible because the `deadline` has passed. ACH `ReceivedCredits` have a limited time of when they’re reversible after they’re created. |
| `already_reversed`       | `null`                     | A `ReceivedCredit` that’s already reversed has this `restricted_reason`. It might have a non-null `deadline` value.                                                                                                                 |
| `null`                   | `null`                     | You can reverse `ReceivedCredits` anytime if they have `null` for both `restricted_reason` and `deadline`.                                                                                                                          |

## Create a CreditReversal 

Use `POST /v1/treasury/credit_reversals` to create a `CreditReversal`. Set the `received_credit` parameter in the body of the request to the value of the `ReceivedCredit` ID to reverse.

> You can’t update `CreditReversals`, so you must set any optional [metadata](https://docs.stripe.com/api/treasury/credit_reversals/create.md#create_credit_reversal-metadata) on creation.

The following request creates a `CreditReversal` based on the `ReceivedCredit` ID value on the required `received_credit` parameter. The request also sets an optional metadata value.

```curl
curl https://api.stripe.com/v1/treasury/credit_reversals \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d received_credit={{RECEIVED_CREDIT_ID}} \
  -d "metadata[reason]"=Because
```

```cli
stripe treasury credit_reversals create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --received-credit={{RECEIVED_CREDIT_ID}} \
  -d "metadata[reason]"=Because
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_reversal = client.v1.treasury.credit_reversals.create(
  {
    received_credit: '{{RECEIVED_CREDIT_ID}}',
    metadata: {reason: 'Because'},
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
credit_reversal = client.v1.treasury.credit_reversals.create(
  {"received_credit": "{{RECEIVED_CREDIT_ID}}", "metadata": {"reason": "Because"}},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditReversal = $stripe->treasury->creditReversals->create(
  [
    'received_credit' => '{{RECEIVED_CREDIT_ID}}',
    'metadata' => ['reason' => 'Because'],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditReversalCreateParams params =
  CreditReversalCreateParams.builder()
    .setReceivedCredit("{{RECEIVED_CREDIT_ID}}")
    .putMetadata("reason", "Because")
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

CreditReversal creditReversal =
  client.v1().treasury().creditReversals().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const creditReversal = await stripe.treasury.creditReversals.create(
  {
    received_credit: '{{RECEIVED_CREDIT_ID}}',
    metadata: {
      reason: 'Because',
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
params := &stripe.TreasuryCreditReversalCreateParams{
  ReceivedCredit: stripe.String("{{RECEIVED_CREDIT_ID}}"),
}
params.AddMetadata("reason", "Because")
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryCreditReversals.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.CreditReversalCreateOptions
{
    ReceivedCredit = "{{RECEIVED_CREDIT_ID}}",
    Metadata = new Dictionary<string, string> { { "reason", "Because" } },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.CreditReversals;
Stripe.Treasury.CreditReversal creditReversal = service.Create(options, requestOptions);
```

If successful, the response returns the new `CreditReversal` object.

```json
{
    "id": "{{CREDIT_REVERSAL_ID}}",
    "object": "credit_reversal",
    "amount": 1000,
    "currency": "usd",
    "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
    "hosted_regulatory_receipt_url": "https://payments.stripe.com/regulatory-receipt/{{URL_ID}}",
    "livemode": false,
    "metadata": {
        "csr_id": "CSR-12"
    },
    "network": "ach",
    "received_credit": "{{RECEIVED_CREDIT_ID}}",
    "status": "processing",
    "status_transitions": {
        "posted_at": null
    },
    "transaction": "{{TRANSACTION_ID}}"
}
```

## Retrieve a CreditReversal 

Use `GET /v1/treasury/credit_reversals/{{CREDIT_REVERSAL_ID}}` to retrieve the `CreditReversal` with the associated ID.

```curl
curl https://api.stripe.com/v1/treasury/credit_reversals/{{CREDIT_REVERSAL_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe treasury credit_reversals retrieve {{CREDIT_REVERSAL_ID}} \
  --stripe-account {{CONNECTEDACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_reversal = client.v1.treasury.credit_reversals.retrieve(
  '{{CREDIT_REVERSAL_ID}}',
  {},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
credit_reversal = client.v1.treasury.credit_reversals.retrieve(
  "{{CREDIT_REVERSAL_ID}}",
  options={"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditReversal = $stripe->treasury->creditReversals->retrieve(
  '{{CREDIT_REVERSAL_ID}}',
  [],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditReversalRetrieveParams params = CreditReversalRetrieveParams.builder().build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

CreditReversal creditReversal =
  client.v1().treasury().creditReversals().retrieve(
    "{{CREDIT_REVERSAL_ID}}",
    params,
    requestOptions
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const creditReversal = await stripe.treasury.creditReversals.retrieve(
  '{{CREDIT_REVERSAL_ID}}',
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TreasuryCreditReversalRetrieveParams{}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryCreditReversals.Retrieve(
  context.TODO(), "{{CREDIT_REVERSAL_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.CreditReversals;
Stripe.Treasury.CreditReversal creditReversal = service.Get(
    "{{CREDIT_REVERSAL_ID}}",
    null,
    requestOptions);
```

The response returns the specific `CreditReversal` object.

#### JSON (commented)

```json
{
  "id": "{{CREDIT_REVERSAL_ID}}",
  "object": "credit_reversal",
  "livemode": "{{Boolean}}",
  "created": "{{Timestamp}}",
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  "amount": 1000,
  "currency": "usd",
  // The ReceivedCredit that was reversed
  "received_credit": "{{RECEIVED_CREDIT_ID}}",
  // The rails used to reversed. Always the same as that of the ReceivedCredit
  "network": "ach",
  "status": "processing" | "posted",
  "status_transitions": {
    "posted_at": null | "{{Timestamp}}",
  },
  // Transaction representing balance impact of the CreditReversal
  "transaction": "{{TRANSACTION_ID}}",
  // A unique, Stripe-hosted direct link to the regulatory receipt for the CreditReversal
  "hosted_regulatory_receipt_url": "{{Url}}",
  // A map of String-String intended for users to use custom data
  "metadata": {},
}
```

#### JSON

```json
{
  "id": "{{CREDIT_REVERSAL_ID}}",
  "object": "credit_reversal",
  "livemode": "{{Boolean}}",
  "created": "{{Timestamp}}",
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  "amount": 1000,
  "currency": "usd",
  "received_credit": "{{RECEIVED_CREDIT_ID}}",
  "network": "ach",
  "status": "posted",
  "status_transitions": {
    "posted_at": "{{Timestamp}}",
  },
  "transaction": "{{TRANSACTION_ID}}",
  "hosted_regulatory_receipt_url": "{{Url}}",
  "metadata": {},
}
```

## List CreditReversals 

Use `GET /v1/treasury/credit_reversals` to retrieve a list of `CreditReversals` for the financial account with the ID provided in the required `financial_account` parameter. You can filter the list by standard list parameters, `status`, or by `ReceivedCredit` ID using the `received_credit` parameter.

```
{
  // Standard list parameters
  "limit", "starting_after", "ending_before",
  // Filter by status
  "status": "processing" | "posted",
  // Filter by FinancialAccount (Required)
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  // Filter by ReceivedCredit
  "received_credit": "{{RECEIVED_CREDIT_ID}}"
}
```

The following request returns the three most recent credit reversals with a status of `posted` for the specified financial account.

```curl
curl -G https://api.stripe.com/v1/treasury/credit_reversals \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d limit=3 \
  -d status=posted \
  -d financial_account="{{TREASURYFINANCIALACCOUNT_ID}}"
```

```cli
stripe treasury credit_reversals list  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --limit=3 \
  --status=posted \
  --financial-account="{{TREASURYFINANCIALACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_reversals = client.v1.treasury.credit_reversals.list(
  {
    limit: 3,
    status: 'posted',
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
credit_reversals = client.v1.treasury.credit_reversals.list(
  {
    "limit": 3,
    "status": "posted",
    "financial_account": "{{TREASURYFINANCIALACCOUNT_ID}}",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditReversals = $stripe->treasury->creditReversals->all(
  [
    'limit' => 3,
    'status' => 'posted',
    'financial_account' => '{{TREASURYFINANCIALACCOUNT_ID}}',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditReversalListParams params =
  CreditReversalListParams.builder()
    .setLimit(3L)
    .setStatus(CreditReversalListParams.Status.POSTED)
    .setFinancialAccount("{{TREASURYFINANCIALACCOUNT_ID}}")
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

StripeCollection<CreditReversal> stripeCollection =
  client.v1().treasury().creditReversals().list(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const creditReversals = await stripe.treasury.creditReversals.list(
  {
    limit: 3,
    status: 'posted',
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
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
params := &stripe.TreasuryCreditReversalListParams{
  Status: stripe.String(stripe.TreasuryCreditReversalStatusPosted),
  FinancialAccount: stripe.String("{{TREASURYFINANCIALACCOUNT_ID}}"),
}
params.Limit = stripe.Int64(3)
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result := sc.V1TreasuryCreditReversals.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.CreditReversalListOptions
{
    Limit = 3,
    Status = "posted",
    FinancialAccount = "{{TREASURYFINANCIALACCOUNT_ID}}",
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.CreditReversals;
StripeList<Stripe.Treasury.CreditReversal> creditReversals = service.List(
    options,
    requestOptions);
```

If successful, the response returns the relevant list of [CreditReversal objects](https://docs.stripe.com/api/treasury/credit_reversals.md).

## Test CreditReversals 

To test CreditReversals, you must first create [test ReceivedCredits](https://docs.stripe.com/financial-accounts/connect/moving-money/into/credit-reversals.md#testingrc). Then use `POST /v1/treasury/credit_reversals` and specify the test `ReceivedCredit` ID in the `received_credit` parameter to create a test `CreditReversal`.

## CreditReversal webhooks 

Stripe emits the following `CreditReversal` events to your [webhook](https://docs.stripe.com/webhooks.md) endpoint:

- `treasury.credit_reversal.created` on `CreditReversal` creation.
- `treasury.credit_reversal.posted` when the `CreditReversal` posts.
