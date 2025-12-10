# Moving money using DebitReversal objects

Learn how you can retrieve funds taken out of a financial account from an external account holder.

Returning the funds from a [ReceivedDebit](https://docs.stripe.com/api/treasury/received_debits.md) creates a [DebitReversal](https://docs.stripe.com/api/treasury/debit_reversals.md). You can get the funds back from a `ReceivedDebit` in only some scenarios (detailed in the following table). Whether you can return the funds of a `ReceivedDebit` depends on the network and source flow.

The `reversal_details` sub-hash on the `ReceivedDebit` resource can have the following combination of values, which determines whether you can return the `ReceivedDebit` funds.

| RESTRICTED REASON        | DEADLINE (EPOCH TIMESTAMP) | EXAMPLE SCENARIO                                                                                                                                                                                                                                                        |
| ------------------------ | -------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `null`                   | 7940828047                 | A `ReceivedDebit` that you can return funds from, but only until the timestamp in `deadline`. ACH `ReceivedDebits` have a deadline that determines how long you have to return them.                                                                                    |
| `deadline_passed`        | 1629480538                 | A `ReceivedDebit` whose funds were returnable before the timestamp in `deadline`, but is no longer returnable using the API because the `deadline` has passed. ACH `ReceivedDebits` have a limited time of when they’re returnable using the API after they’re created. |
| `already_reversed`       | null                       | A `ReceivedDebit` that’s already been returned. It might have a non-null `deadline` value.                                                                                                                                                                              |
| `source_flow_restricted` | null                       | A `ReceivedDebit` that can’t be returned because its `source_flow` isn’t reversible.                                                                                                                                                                                    |

## Return deadlines

You have approximately 1 business day to return ACH debits using the API after receipt. After this time, ACH debit funds might still be returnable but funds return isn’t guaranteed. Contact support to request a return of funds if the reversal deadline has passed.

To create returns of `ReceivedDebit` funds produced by activity on `Issuing` cards, see the [Issuing disputes](https://docs.stripe.com/issuing/purchases/disputes.md) guide.

## Create a DebitReversal 

Use `POST /v1/treasury/debit_reversals` to create a `DebitReversal`. Specify the ID of the `ReceivedDebit` to reverse with the `received_debit` parameter in the body of the request.

> You can’t update `DebitReversals`, so you must set any optional [metadata](https://docs.stripe.com/api/treasury/debit_reversals/object.md#debit_reversal_object-metadata) on creation.

The following request creates a `DebitReversal` based on the `ReceivedDebit` ID value on the required `received_debit` parameter. The request also sets an optional metadata value.

```curl
curl https://api.stripe.com/v1/treasury/debit_reversals \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d received_debit={{RECEIVED_DEBIT_ID}} \
  -d "metadata[reason]"=Because
```

```cli
stripe treasury debit_reversals create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --received-debit={{RECEIVED_DEBIT_ID}} \
  -d "metadata[reason]"=Because
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

debit_reversal = client.v1.treasury.debit_reversals.create(
  {
    received_debit: '{{RECEIVED_DEBIT_ID}}',
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
debit_reversal = client.v1.treasury.debit_reversals.create(
  {"received_debit": "{{RECEIVED_DEBIT_ID}}", "metadata": {"reason": "Because"}},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$debitReversal = $stripe->treasury->debitReversals->create(
  [
    'received_debit' => '{{RECEIVED_DEBIT_ID}}',
    'metadata' => ['reason' => 'Because'],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

DebitReversalCreateParams params =
  DebitReversalCreateParams.builder()
    .setReceivedDebit("{{RECEIVED_DEBIT_ID}}")
    .putMetadata("reason", "Because")
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

DebitReversal debitReversal =
  client.v1().treasury().debitReversals().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const debitReversal = await stripe.treasury.debitReversals.create(
  {
    received_debit: '{{RECEIVED_DEBIT_ID}}',
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
params := &stripe.TreasuryDebitReversalCreateParams{
  ReceivedDebit: stripe.String("{{RECEIVED_DEBIT_ID}}"),
}
params.AddMetadata("reason", "Because")
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryDebitReversals.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.DebitReversalCreateOptions
{
    ReceivedDebit = "{{RECEIVED_DEBIT_ID}}",
    Metadata = new Dictionary<string, string> { { "reason", "Because" } },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.DebitReversals;
Stripe.Treasury.DebitReversal debitReversal = service.Create(options, requestOptions);
```

If successful, the response returns the new `DebitReversal` object.

```json
{
  "id": "{{DEBIT_REVERSAL_ID}}",
  "object": "debit_reversal",
  "amount": 1000,
  "currency": "usd",
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  "hosted_regulatory_receipt_url": "https://payments.stripe.com/regulatory-receipt/{{URL_ID}}",
  "linked_flows": null,
  "livemode": false,
  "metadata": {},
  "network": "ach",
  "received_debit": "{{RECEIVED_DEBIT_ID}}",
  "resolution": null,
  "status": "processing",
  "status_transitions": {
    "completed_at": null
  },
  "transaction": "{{TRANSACTION_ID}}"
}
```

## Retrieve a DebitReversal 

Use `GET /v1/treasury/debit_reversals/{{DEBIT_REVERSAL_ID}}` to retrieve the `DebitReversal` with the associated ID.

```curl
curl https://api.stripe.com/v1/treasury/debit_reversals/{{DEBIT_REVERSAL_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe treasury debit_reversals retrieve {{DEBIT_REVERSAL_ID}} \
  --stripe-account {{CONNECTEDACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

debit_reversal = client.v1.treasury.debit_reversals.retrieve(
  '{{DEBIT_REVERSAL_ID}}',
  {},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
debit_reversal = client.v1.treasury.debit_reversals.retrieve(
  "{{DEBIT_REVERSAL_ID}}",
  options={"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$debitReversal = $stripe->treasury->debitReversals->retrieve(
  '{{DEBIT_REVERSAL_ID}}',
  [],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

DebitReversalRetrieveParams params = DebitReversalRetrieveParams.builder().build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

DebitReversal debitReversal =
  client.v1().treasury().debitReversals().retrieve(
    "{{DEBIT_REVERSAL_ID}}",
    params,
    requestOptions
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const debitReversal = await stripe.treasury.debitReversals.retrieve(
  '{{DEBIT_REVERSAL_ID}}',
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TreasuryDebitReversalRetrieveParams{}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryDebitReversals.Retrieve(
  context.TODO(), "{{DEBIT_REVERSAL_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.DebitReversals;
Stripe.Treasury.DebitReversal debitReversal = service.Get(
    "{{DEBIT_REVERSAL_ID}}",
    null,
    requestOptions);
```

If successful, the response returns the identified `DebitReversal`.

#### JSON (commented)

```json
{
  "id": "{{DEBIT_REVERSAL_ID}}",
  "object": "debit_reversal",
  "livemode": true | false,
  "created": "{{Timestamp}}",
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  "amount": 1000,
  "currency": "usd",
  // the ReceivedDebit being returned
  "received_debit": "{{RECEIVED_DEBIT_ID}}",
  // whether funds have been returned depending on the DebitReversal outcome
  "resolution": null | "won",
  "status": "processing" | "canceled" | "completed",
  "status_transitions": {
    "processing_at": "{{Timestamp}}",
    "canceled_at": null | "{{Timestamp}}",
    "completed_at": null | "{{Timestamp}}"
  },
  // Transaction representing balance impact of the DebitReversal
  "transaction": "{{TRANSACTION_ID}}",
  // A unique, Stripe-hosted direct link to the regulatory receipt for the DebitReversal
  "hosted_regulatory_receipt_url": "{{Url}}",
  // A map of String-String intended for users to use custom data
  "metadata": {}
}
```

#### JSON

```json
{
  "id": "{{DEBIT_REVERSAL_ID}}",
  "object": "debit_reversal",
  "livemode": false,
  "created": 123456,
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  "amount": 1000,
  "currency": "usd",
  "received_debit": "{{RECEIVED_DEBIT_ID}}",
  "resolution": null,
  "status": "completed",
  "status_transitions": {
    "processing_at": 123456,
    "canceled_at": null,
    "completed_at": null
  },
  "transaction": "{{TRANSACTION_ID}}",
  "hosted_regulatory_receipt_url": "https://example.com",
  "metadata": {}
}
```

## List DebitReversals 

Use `GET /v1/treasury/debit_reversals` to retrieve a list of `DebitReversals` for the financial account with the ID provided in the required `financial_account` parameter. You can filter the list by standard list parameters, `status`, or by `ReceivedDebit` ID using the `received_debit` parameter.

```
{
  // Standard list parameters
  "limit", "starting_after", "ending_before",
  // Filter by financial account (Required)
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  // Filter by `status`
  "status": "processing" | "canceled" | "completed"
  // Filter by ReceivedDebit
  "received_debit": "{{RECEIVED_DEBIT_ID}}",
}
```

The following request retrieves the last three [DebitReversal objects](https://docs.stripe.com/api/treasury/debit_reversals/object.md) for the identified financial account.

```curl
curl -G https://api.stripe.com/v1/treasury/debit_reversals \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d financial_account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  -d limit=3
```

```cli
stripe treasury debit_reversals list  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --financial-account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  --limit=3
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

debit_reversals = client.v1.treasury.debit_reversals.list(
  {
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    limit: 3,
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
debit_reversals = client.v1.treasury.debit_reversals.list(
  {"financial_account": "{{TREASURYFINANCIALACCOUNT_ID}}", "limit": 3},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$debitReversals = $stripe->treasury->debitReversals->all(
  [
    'financial_account' => '{{TREASURYFINANCIALACCOUNT_ID}}',
    'limit' => 3,
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

DebitReversalListParams params =
  DebitReversalListParams.builder()
    .setFinancialAccount("{{TREASURYFINANCIALACCOUNT_ID}}")
    .setLimit(3L)
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

StripeCollection<DebitReversal> stripeCollection =
  client.v1().treasury().debitReversals().list(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const debitReversals = await stripe.treasury.debitReversals.list(
  {
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    limit: 3,
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
params := &stripe.TreasuryDebitReversalListParams{
  FinancialAccount: stripe.String("{{TREASURYFINANCIALACCOUNT_ID}}"),
}
params.Limit = stripe.Int64(3)
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result := sc.V1TreasuryDebitReversals.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.DebitReversalListOptions
{
    FinancialAccount = "{{TREASURYFINANCIALACCOUNT_ID}}",
    Limit = 3,
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.DebitReversals;
StripeList<Stripe.Treasury.DebitReversal> debitReversals = service.List(
    options,
    requestOptions);
```

## Test DebitReversals 

To test `DebitReversals`, you must first create a [test ReceivedDebit](https://docs.stripe.com/financial-accounts/connect/moving-money/out-of/debit-reversals.md#test-received-debit). Afterwards, use `POST /v1/treasury/debit_reversals` and specify the test `ReceivedDebit` ID in the `received_debit` parameter to create a test `DebitReversal`.

## DebitReversal webhooks 

Stripe emits the following `DebitReversal` events to your [webhook](https://docs.stripe.com/webhooks.md) endpoint:

- `treasury.debit_reversal.created` on `DebitReversal` creation.
- `treasury.debit_reversal.completed` when the `DebitReversal` completes.
