# Moving money using ReceivedCredit objects

Learn how to move money into a financial account from another financial account or bank account.

When funds move into a financial account, Stripe creates a corresponding [ReceivedCredit](https://docs.stripe.com/api/treasury/received_credits.md) object on the account. A `ReceivedCredit` contains information on how the funds were sent and from what account, where possible. You can send funds to a financial account with the account’s routing and account numbers for `ach` and `us_domestic_wire`, or the financial account ID for transfers between financial accounts.

When the origin of the funds is another financial account, the `ReceivedCredit` contains a `linked_flows.source_flow` reference to the originating money movement. In this case, the source `OutboundPayment` has `stripe` as its `network` value.

## Retrieve a ReceivedCredit 

Use `GET /v1/treasury/received_credits/{{RECEIVED_CREDIT_ID}}` to retrieve the [ReceivedCredit](https://docs.stripe.com/api/treasury/received_credits.md) with the specified ID.

The following request retrieves the `ReceivedCredit` with the specified ID. The response for this request includes expanded [Transaction object](https://docs.stripe.com/api/treasury/transactions.md) details.

```curl
curl -G https://api.stripe.com/v1/treasury/received_credits/{{RECEIVED_CREDIT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d "expand[]"=transaction
```

```cli
stripe treasury received_credits retrieve {{RECEIVED_CREDIT_ID}} \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  -d "expand[0]"=transaction
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

received_credit = client.v1.treasury.received_credits.retrieve(
  '{{RECEIVED_CREDIT_ID}}',
  {expand: ['transaction']},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
received_credit = client.v1.treasury.received_credits.retrieve(
  "{{RECEIVED_CREDIT_ID}}",
  {"expand": ["transaction"]},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$receivedCredit = $stripe->treasury->receivedCredits->retrieve(
  '{{RECEIVED_CREDIT_ID}}',
  ['expand' => ['transaction']],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ReceivedCreditRetrieveParams params =
  ReceivedCreditRetrieveParams.builder().addExpand("transaction").build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

ReceivedCredit receivedCredit =
  client.v1().treasury().receivedCredits().retrieve(
    "{{RECEIVED_CREDIT_ID}}",
    params,
    requestOptions
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const receivedCredit = await stripe.treasury.receivedCredits.retrieve(
  '{{RECEIVED_CREDIT_ID}}',
  {
    expand: ['transaction'],
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
params := &stripe.TreasuryReceivedCreditRetrieveParams{}
params.AddExpand("transaction")
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryReceivedCredits.Retrieve(
  context.TODO(), "{{RECEIVED_CREDIT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.ReceivedCreditGetOptions
{
    Expand = new List<string> { "transaction" },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.ReceivedCredits;
Stripe.Treasury.ReceivedCredit receivedCredit = service.Get(
    "{{RECEIVED_CREDIT_ID}}",
    options,
    requestOptions);
```

If successful, the response provides the requested `ReceivedCredit` object. Some of the parameters in the response have additional details that are only returned when you add them as values to the `expand[]` parameter of your request. The fields that you can expand have an `Expandable` comment in the following response example. See [Expanding Responses](https://docs.stripe.com/api/expanding_objects.md) to learn more about expanding object responses.

#### JSON (commented)

```json
{
  "id": "{{RECEIVED_CREDIT_ID}}",
  "object": "received_credit",
  "livemode": true | false,
  "created": "{{Timestamp}}",
  // The FinancialAccount that received the funds
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}", // Expandable
  "amount": 1000,
  "currency": "usd",
  // The description of this movement sent by the originator
  "description": "Testing",
  // ReceivedCredits are created with a status of either `succeeded` (approved) or `failed`
  // (declined). When failed, no Transaction is created. The failure reason can be found
  // in the "failure_code" field.
  "status": "succeeded" | "failed",
  // The network that was used for this movement
  "network": "ach" | "stripe" | "us_domestic_wire",
  // Information about the originating account of the movement
  "received_payment_method_details": {
    "type": "us_bank_account" | "balance" | "financial_account",
    // Only set if type is `us_bank_account`.
    // This contains information of the source external bank account.
    "us_bank_account": null | {
      "bank_name": "{{String}}",
      "routing_number": "12341234",
      "last4": "6789"
    },
    // Only set if type is `financial_account`.
    // This contains information of the source FinancialAccount.
    "financial_account": nil | {
      "id": "{{FINANCIAL_ACCOUNT_ID}}"
    },
    // Only set if type is `balance`.
    // This is only set when the source is a Payout.
    "balance": null | "payments",
    "billing_details": null | {
      "name": null | "{{String}}",
      "phone": null | "{{String}}",
      "email": null | "{{String}}",
      "address": null | {
        "line1": null | "{{String}}",
        "line2": null | "{{String}}",
        "city": null | "{{String}}",
        "state": null | "{{String}}",
        "postal_code": null | "{{String}}",
        "country": null | "{{String}}"
      }
    }
  },
  // A unique, Stripe-hosted direct link to the regulatory receipt for the ReceivedCredit
  "hosted_regulatory_receipt_url": "{{URL}}",
  "reversal_details": {
    "restricted_reason": null | "source_flow_restricted" | "network_restricted" | "deadline_passed" | "already_reversed",
    "deadline": null | "{{Timestamp}}"
  },
  "linked_flows": {
    // When the platform can see both source and destination
    // accounts, we link to the originating flow
    // When the `network` type is `stripe`, this could be an OutboundPayment originated from another merchant, a
    // Payout originated from the same merchant (a balance transfer from payments),
    // or the result of reversing a Stripe network ReceivedCredit by the recipient
    // whom funds were sent to in the past.
    // When the `network` type is `ach`, this could be either nil or a Payout.
    "source_flow": null | "{{OUTBOUND_PAYMENT_ID}}" | "{{PAYOUT_ID}}",
    "source_flow_type": null | "outbound_payment" | "payout",
    // Includable by expanding linked_flows['source_flow_details']. When included, this field will
    // either be an OutboundPayment or a Payout.
    "source_flow_details": null,
    // CreditReversals allow you to reverse a ReceivedCredit as long as it's before the reversal_details['deadline']
    // If reversed, the ReceivedCredit will link to the CreditReversal.
    "credit_reversal": null | "{{CREDIT_REVERSAL_ID}}"
  },
  // Currently, the only failure reasons for ReceivedCredits are due to restrictions on the account.
  "failure_code": null |
    "account_closed" |
    "account_frozen",
  // Transaction created by the ReceivedCredit. Created synchronously.
  "transaction": "{{TRANSACTION_ID}}" // Expandable
}
```

#### JSON

```json
{
  "id": "{{RECEIVED_CREDIT_ID}}",
  "object": "received_credit",
  "livemode": false,
  "created": 123456,
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  "amount": 1000,
  "currency": "usd",
  "description": "Testing",
  "status": "succeeded",
  "network": "stripe",
  "received_payment_method_details": {
    "type": "financial_account",
    "us_bank_account": null,
    "financial_account": {
      "id": "{{FINANCIAL_ACCOUNT_ID}}"
    },
    "balance": "payments",
    "billing_details": null
  },
  "hosted_regulatory_receipt_url": "https://example.com",
  "reversal_details": {
    "restricted_reason": null,
    "deadline": null
  },
  "linked_flows": {
    "source_flow": "{{OUTBOUND_PAYMENT_ID}}",
    "source_flow_type": "outbound_payment",
    "source_flow_details": null,
    "credit_reversal": "{{CREDIT_REVERSAL_ID}}"
  },
  "failure_code": null,
  "failure_message": null,
  "transaction": "{{TRANSACTION_ID}}"
}
```

## List ReceivedCredits 

Use `GET /v1/treasury/received_credits` to retrieve all of the `ReceivedCredits` for the financial account with the ID of the required `financial_account` parameter. You can filter the list with the standard list parameters, by `status`, or by `linked_flows.source_flow_type`.

```
{
  // Standard list parameters
  "limit", "starting_after", "ending_before",
  // Filter by FinancialAccount (required)
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  // Filter by status
  "status": "succeeded" | "failed",
  // Filter by `source_flow_type`
  "linked_flows.source_flow_type": nil | "payout" | "outbound_payment"
}
```

The following request retrieves the `ReceivedCredits` that have a status of `failed` for the specified financial account.

```curl
curl -G https://api.stripe.com/v1/treasury/received_credits \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d financial_account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  -d status=failed
```

```cli
stripe treasury received_credits list  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --financial-account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  --status=failed
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

received_credits = client.v1.treasury.received_credits.list(
  {
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    status: 'failed',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
received_credits = client.v1.treasury.received_credits.list(
  {"financial_account": "{{TREASURYFINANCIALACCOUNT_ID}}", "status": "failed"},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$receivedCredits = $stripe->treasury->receivedCredits->all(
  [
    'financial_account' => '{{TREASURYFINANCIALACCOUNT_ID}}',
    'status' => 'failed',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ReceivedCreditListParams params =
  ReceivedCreditListParams.builder()
    .setFinancialAccount("{{TREASURYFINANCIALACCOUNT_ID}}")
    .setStatus(ReceivedCreditListParams.Status.FAILED)
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

StripeCollection<ReceivedCredit> stripeCollection =
  client.v1().treasury().receivedCredits().list(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const receivedCredits = await stripe.treasury.receivedCredits.list(
  {
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    status: 'failed',
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
params := &stripe.TreasuryReceivedCreditListParams{
  FinancialAccount: stripe.String("{{TREASURYFINANCIALACCOUNT_ID}}"),
  Status: stripe.String(stripe.TreasuryReceivedCreditStatusFailed),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result := sc.V1TreasuryReceivedCredits.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.ReceivedCreditListOptions
{
    FinancialAccount = "{{TREASURYFINANCIALACCOUNT_ID}}",
    Status = "failed",
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.ReceivedCredits;
StripeList<Stripe.Treasury.ReceivedCredit> receivedCredits = service.List(
    options,
    requestOptions);
```

If successful, the response includes the [ReceivedCredit](https://docs.stripe.com/api/treasury/received_credits.md) objects that match the criteria specified in the request.

## Test ReceivedCredits 

Use `POST /v1/test_helpers/treasury/received_credits` to simulate receiving funds in a financial account. To simulate a bank transfer from an account outside of Stripe to your financial account, set `initiating_payment_method_details` to the values of the external bank account, and set `network` to `ach` or `us_domestic_wire`.

The following request creates a test `ReceivedCredit` from an external bank account using an `OutboundPayment` between two financial accounts on the same platform.

```curl
curl https://api.stripe.com/v1/test_helpers/treasury/received_credits \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d financial_account={{DESTINATION_FINANCIAL_ACCOUNT_ID}} \
  -d network=ach \
  -d amount=1234 \
  -d currency=usd
```

```cli
stripe test_helpers treasury received_credits create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --financial-account={{DESTINATION_FINANCIAL_ACCOUNT_ID}} \
  --network=ach \
  --amount=1234 \
  --currency=usd
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

received_credit = client.v1.test_helpers.treasury.received_credits.create(
  {
    financial_account: '{{DESTINATION_FINANCIAL_ACCOUNT_ID}}',
    network: 'ach',
    amount: 1234,
    currency: 'usd',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
received_credit = client.v1.test_helpers.treasury.received_credits.create(
  {
    "financial_account": "{{DESTINATION_FINANCIAL_ACCOUNT_ID}}",
    "network": "ach",
    "amount": 1234,
    "currency": "usd",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$receivedCredit = $stripe->testHelpers->treasury->receivedCredits->create(
  [
    'financial_account' => '{{DESTINATION_FINANCIAL_ACCOUNT_ID}}',
    'network' => 'ach',
    'amount' => 1234,
    'currency' => 'usd',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ReceivedCreditCreateParams params =
  ReceivedCreditCreateParams.builder()
    .setFinancialAccount("{{DESTINATION_FINANCIAL_ACCOUNT_ID}}")
    .setNetwork(ReceivedCreditCreateParams.Network.ACH)
    .setAmount(1234L)
    .setCurrency("usd")
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

ReceivedCredit receivedCredit =
  client.v1().testHelpers().treasury().receivedCredits().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const receivedCredit = await stripe.testHelpers.treasury.receivedCredits.create(
  {
    financial_account: '{{DESTINATION_FINANCIAL_ACCOUNT_ID}}',
    network: 'ach',
    amount: 1234,
    currency: 'usd',
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
params := &stripe.TestHelpersTreasuryReceivedCreditCreateParams{
  FinancialAccount: stripe.String("{{DESTINATION_FINANCIAL_ACCOUNT_ID}}"),
  Network: stripe.String(stripe.TreasuryReceivedCreditNetworkACH),
  Amount: stripe.Int64(1234),
  Currency: stripe.String(stripe.CurrencyUSD),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TestHelpersTreasuryReceivedCredits.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.TestHelpers.Treasury.ReceivedCreditCreateOptions
{
    FinancialAccount = "{{DESTINATION_FINANCIAL_ACCOUNT_ID}}",
    Network = "ach",
    Amount = 1234,
    Currency = "usd",
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.TestHelpers.Treasury.ReceivedCredits;
Stripe.Treasury.ReceivedCredit receivedCredit = service.Create(options, requestOptions);
```

If successful, the response returns a `ReceivedCredit` object. The following is an example of a response for a bank transfer.

```json
{
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  "network": "ach",
  "amount": "1234",
  "currency": "usd",
  "description": "Test",
  "source_details": {
    "type": "aba",
    "aba": {
      "country": "US",
      "routing_number": "12341234",
      "account_number": "0123456789",
      "account_holder_name": "Jenny Rosen",
    }
  }
}
```

## ReceivedCredit webhooks 

Stripe emits the following `ReceivedCredit` events to your [webhook](https://docs.stripe.com/webhooks.md) endpoint:

- `treasury.received_credit.created` on `ReceivedCredit` creation.
- `treasury.received_credit.{{new_status}}` when an `ReceivedCredit` changes status. Available status value options include:
  - `treasury.received_credit.succeeded`
  - `treasury.received_credit.failed`
