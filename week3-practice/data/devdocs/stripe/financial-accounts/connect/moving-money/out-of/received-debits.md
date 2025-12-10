# Moving money using ReceivedDebit objects

Learn how external account holders can pull funds from a financial account.

Certain processes initiated outside of Financial Accounts for platforms result in money being pulled out of a financial account. This includes:

- Spending money on a card through [Stripe Issuing](https://docs.stripe.com/issuing/purchases/transactions.md#transactions-with-financial-accounts-for-platforms)
- Pulling money out of a financial account into an external account using ACH debits
- Pulling money out of a platform’s financial account into that platform’s Stripe Payments balance using [top-ups](https://docs.stripe.com/financial-accounts/connect/moving-money/payouts.md#top-ups)

These money movements result in the creation of `ReceivedDebit` objects. You don’t create `ReceivedDebits` directly, rather you observe `ReceivedDebit` object creation with webhooks. If there are insufficient funds in the account, the `ReceivedDebit` fails in most cases.

## Retrieve a ReceivedDebit 

Use `GET /v1/treasury/received_debits/{{RECEIVED_DEBIT_ID}}` to retrieve the `ReceivedDebit` with the associated ID.

```curl
curl https://api.stripe.com/v1/treasury/received_debits/{{RECEIVED_DEBIT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe treasury received_debits retrieve {{RECEIVED_DEBIT_ID}} \
  --stripe-account {{CONNECTEDACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

received_debit = client.v1.treasury.received_debits.retrieve(
  '{{RECEIVED_DEBIT_ID}}',
  {},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
received_debit = client.v1.treasury.received_debits.retrieve(
  "{{RECEIVED_DEBIT_ID}}",
  options={"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$receivedDebit = $stripe->treasury->receivedDebits->retrieve(
  '{{RECEIVED_DEBIT_ID}}',
  [],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ReceivedDebitRetrieveParams params = ReceivedDebitRetrieveParams.builder().build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

ReceivedDebit receivedDebit =
  client.v1().treasury().receivedDebits().retrieve(
    "{{RECEIVED_DEBIT_ID}}",
    params,
    requestOptions
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const receivedDebit = await stripe.treasury.receivedDebits.retrieve(
  '{{RECEIVED_DEBIT_ID}}',
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TreasuryReceivedDebitRetrieveParams{}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryReceivedDebits.Retrieve(
  context.TODO(), "{{RECEIVED_DEBIT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.ReceivedDebits;
Stripe.Treasury.ReceivedDebit receivedDebit = service.Get(
    "{{RECEIVED_DEBIT_ID}}",
    null,
    requestOptions);
```

If successful, the response returns the `ReceivedDebit` object with the associated ID. Some of the parameters in the response have additional details that are only returned when you add them as values to the `expand[]` parameter. The fields that you can expand have an “Expandable” comment in the following response example. See [Expanding Responses](https://docs.stripe.com/api/expanding_objects.md) to learn more about expanding object responses.

#### JSON (commented)

```json
{
  "id": "{{RECEIVED_DEBIT_ID}}",
  "object": "received_debit",
  "livemode": Boolean,
  "created": Timestamp,
  // The FinancialAccount funds have been pulled from
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}", // Expandable
  "amount": 1000,
  "currency": "usd",
  "description": "Testing",
  // ReceivedCredits are created with a status of either succeeded (approved) or failed
  // (declined). When failed, no Transaction is created. The failure reason can be found
  // under "failure_code".
  "status": "succeeded" | "failed",
  // The network that was used for this movement
  // Can only be ach today
  "network": "ach",
  // Information about how the debit was created.
  "received_payment_method_details": {
    "type": "us_bank_account" | "balance" | "financial_account",
    // Only set if type is `us_bank_account`.
    // This contains information of the source external bank account.
    "us_bank_account": nil | {
      "bank_name": String,
      "routing_number": "12341234",
      "last4": "6789"
    },
    // Only set if type is `financial_account`.
    // This contains information of the source FinancialAccount.
    "financial_account": nil | {
      "id": "{{DESTINATION_FINANCIAL_ACCOUNT_ID}}"
    },
    // Only set if type is `balance`.
    // This is only set when the source is a Payout.
    "balance": nil | "payments",
    "billing_details": nil | {
      "name": nil | String,
      "phone": nil | String,
      "email": nil | String,
      "address": nil | {
        "line1": nil | String,
        "line2": nil | String,
        "city": nil | String,
        "state": nil | String,
        "postal_code": nil | String,
        "country": nil | String
      }
    }
  },
  // A unique, Stripe-hosted direct link to the regulatory receipt for the ReceivedDebit
  "hosted_regulatory_receipt_url": Url,
  "reversal_details": {
    "restricted_reason": nil | "source_flow_restricted" | "network_restricted" | "deadline_passed" | "already_reversed",
    "deadline": nil | Timestamp,
  },
  "linked_flows": {
    // DebitReversals allow you to reverse a ReceivedDebit as long as it's before the reversal_details['deadline']
    // If reversed, the ReceivedDebit will link to the DebitReversal.
    "debit_reversal": nil | "{{DEBIT_REVERSAL_ID}}",
    "returns_flow": nil | "{{INBOUND_TRANSFER_ID}}",
  },
  // nil when status succeeded
  "failure_code": "insufficient_funds" |
                  "account_closed" |
                  "account_frozen",
  "failure_message": "The ReceivedDebit could not be completed because the Financial Account doesn't have a sufficient balance available. Please try again using an amount less than or equal to the Financial Account’s available balance." |
                     "Funds can't be sent or withdrawn from this Financial Account because it has been closed. Please re-open the account, or try again with another Financial Account." |
                     "Funds can't be sent or withdrawn from this Financial Account because it has been frozen.",
  // Transaction created by the ReceivedDebit. Only set for succeeded ReceivedDebits.
  "transaction": nil | "{{TRANSACTION_ID}}", // Expandable
}
```

#### JSON

```json
{
  "id": "{{RECEIVED_DEBIT_ID}}",
  "object": "received_debit",
  "livemode": false,
  "created": 123456,
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  "amount": 1000,
  "currency": "usd",
  "description": "Testing",
  "status": "succeeded" | "failed",
  "network": "ach",
  "received_payment_method_details": {
    "type": "financial_account",
    "financial_account": {
      "id": "{{DESTINATION_FINANCIAL_ACCOUNT_ID}}"
    },
    "balance": null,
    "billing_details": null
  },
  "hosted_regulatory_receipt_url": "https://example.com",
  "reversal_details": {
    "restricted_reason":"network_restricted",
    "deadline": 123456,
  },
  "linked_flows": {
    "debit_reversal": "{{DEBIT_REVERSAL_ID}}",
    "returns_flow": "{{INBOUND_TRANSFER_ID}}",
  },
  "failure_code": "account_frozen",
  "failure_message": "Funds can't be sent or withdrawn from this Financial Account because it has been frozen.",
  "transaction": "{{TRANSACTION_ID}}"
}
```

## List ReceivedDebits 

Use `GET /v1/treasury/received_debits` to retrieve all `ReceivedDebits` for a financial account. You must specify a financial account ID for the `financial_account` parameter. You can filter the results by the standard list parameters or by `status`.

```json
{
  // Standard list parameters
  "limit", "starting_after", "ending_before",
  // Filter by FinancialAccount (Required)
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  // Filter by status
  "status": "succeeded" | "failed"
}
```

The following request retrieves the last successful [ReceivedDebit object](https://docs.stripe.com/api/treasury/received_debits/object.md) that occurred before the provided `ReceivedDebit` for the financial account identified.

```curl
curl -G https://api.stripe.com/v1/treasury/received_debits \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d financial_account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  -d limit=1 \
  -d ending_before={{RECEIVED_DEBIT_ID}}
```

```cli
stripe treasury received_debits list  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --financial-account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  --limit=1 \
  --ending-before={{RECEIVED_DEBIT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

received_debits = client.v1.treasury.received_debits.list(
  {
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    limit: 1,
    ending_before: '{{RECEIVED_DEBIT_ID}}',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
received_debits = client.v1.treasury.received_debits.list(
  {
    "financial_account": "{{TREASURYFINANCIALACCOUNT_ID}}",
    "limit": 1,
    "ending_before": "{{RECEIVED_DEBIT_ID}}",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$receivedDebits = $stripe->treasury->receivedDebits->all(
  [
    'financial_account' => '{{TREASURYFINANCIALACCOUNT_ID}}',
    'limit' => 1,
    'ending_before' => '{{RECEIVED_DEBIT_ID}}',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ReceivedDebitListParams params =
  ReceivedDebitListParams.builder()
    .setFinancialAccount("{{TREASURYFINANCIALACCOUNT_ID}}")
    .setLimit(1L)
    .setEndingBefore("{{RECEIVED_DEBIT_ID}}")
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

StripeCollection<ReceivedDebit> stripeCollection =
  client.v1().treasury().receivedDebits().list(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const receivedDebits = await stripe.treasury.receivedDebits.list(
  {
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    limit: 1,
    ending_before: '{{RECEIVED_DEBIT_ID}}',
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
params := &stripe.TreasuryReceivedDebitListParams{
  FinancialAccount: stripe.String("{{TREASURYFINANCIALACCOUNT_ID}}"),
}
params.EndingBefore = stripe.String("{{RECEIVED_DEBIT_ID}}")
params.Limit = stripe.Int64(1)
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result := sc.V1TreasuryReceivedDebits.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.ReceivedDebitListOptions
{
    FinancialAccount = "{{TREASURYFINANCIALACCOUNT_ID}}",
    Limit = 1,
    EndingBefore = "{{RECEIVED_DEBIT_ID}}",
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.ReceivedDebits;
StripeList<Stripe.Treasury.ReceivedDebit> receivedDebits = service.List(
    options,
    requestOptions);
```

## Test ReceivedDebits 

Financial Accounts for platforms provides test endpoints for `ReceivedDebit` objects. In testing environments, use `POST /v1/test_helpers/treasury/received_debits` to simulate `ReceivedDebit` creation. You can’t create `ReceivedDebit` objects in live mode, so using this endpoint enables you to test the flow of funds when a third party initiates creation of a `ReceivedDebit`. Set `financial_account` to the ID of the financial account to send money from. Set `network` to `ach` and optionally provide the ABA financial address details for the `source_details.aba` parameter. As in live mode, test `ReceivedDebits` fail if there are insufficient funds available.

## ReceivedDebit webhooks 

Stripe emits the following `ReceivedDebit` events to your [webhook](https://docs.stripe.com/webhooks.md) endpoint:

- `treasury.received_debit.created` on `ReceivedDebit` creation.
