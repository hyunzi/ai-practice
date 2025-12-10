# Working with Stripe Issuing cards

Learn how to integrate Stripe Issuing with Financial Accounts for platforms.

> #### Accounts v2 API compatibility
> 
> The Accounts v2 API doesn’t support Issuing workflows. If you have accounts created with Accounts v2, you can use Accounts v1 to manage the `treasury` and `card_issuing` capabilities.

[Stripe Issuing](https://docs.stripe.com/issuing.md) lets you create physical and virtual cards using a financial account as the source of funds.

## Enable Issuing on connected accounts

Request the `card_issuing` [account capability](https://docs.stripe.com/connect/account-capabilities.md) for the connected accounts on your platform and provide the [required information](https://docs.stripe.com/issuing/connect.md#required-verification-information) for onboarding.

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTED_ACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "capabilities[treasury][requested]"=true \
  -d "capabilities[card_issuing][requested]"=true \
  -d "capabilities[transfers][requested]"=true
```

```cli
stripe accounts update {{CONNECTED_ACCOUNT_ID}} \
  -d "capabilities[treasury][requested]"=true \
  -d "capabilities[card_issuing][requested]"=true \
  -d "capabilities[transfers][requested]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.update(
  '{{CONNECTED_ACCOUNT_ID}}',
  {
    capabilities: {
      treasury: {requested: true},
      card_issuing: {requested: true},
      transfers: {requested: true},
    },
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account = client.v1.accounts.update(
  "{{CONNECTED_ACCOUNT_ID}}",
  {
    "capabilities": {
      "treasury": {"requested": True},
      "card_issuing": {"requested": True},
      "transfers": {"requested": True},
    },
  },
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->accounts->update(
  '{{CONNECTED_ACCOUNT_ID}}',
  [
    'capabilities' => [
      'treasury' => ['requested' => true],
      'card_issuing' => ['requested' => true],
      'transfers' => ['requested' => true],
    ],
  ]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountUpdateParams params =
  AccountUpdateParams.builder()
    .setCapabilities(
      AccountUpdateParams.Capabilities.builder()
        .setCardIssuing(
          AccountUpdateParams.Capabilities.CardIssuing.builder()
            .setRequested(true)
            .build()
        )
        .setTransfers(
          AccountUpdateParams.Capabilities.Transfers.builder().setRequested(true).build()
        )
        .build()
    )
    .putExtraParam("capabilities[treasury][requested]", true)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account = client.v1().accounts().update("{{CONNECTED_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.update(
  '{{CONNECTED_ACCOUNT_ID}}',
  {
    capabilities: {
      treasury: {
        requested: true,
      },
      card_issuing: {
        requested: true,
      },
      transfers: {
        requested: true,
      },
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.AccountUpdateParams{
  Capabilities: &stripe.AccountUpdateCapabilitiesParams{
    CardIssuing: &stripe.AccountUpdateCapabilitiesCardIssuingParams{
      Requested: stripe.Bool(true),
    },
    Transfers: &stripe.AccountUpdateCapabilitiesTransfersParams{
      Requested: stripe.Bool(true),
    },
  },
}
params.AddExtra("capabilities[treasury][requested]", true)
result, err := sc.V1Accounts.Update(context.TODO(), "{{CONNECTED_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountUpdateOptions
{
    Capabilities = new AccountCapabilitiesOptions
    {
        CardIssuing = new AccountCapabilitiesCardIssuingOptions { Requested = true },
        Transfers = new AccountCapabilitiesTransfersOptions { Requested = true },
    },
};
options.AddExtraParam("capabilities[treasury][requested]", true);
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Update("{{CONNECTED_ACCOUNT_ID}}", options);
```

If successful, the response returns the connected account [Account object](https://docs.stripe.com/api/accounts/object.md) with the `capabilities` hash listing the requested capabilities as `active`.

If you haven’t already, also request access to the `card_issuing` feature on the financial account.

```curl
curl https://api.stripe.com/v1/treasury/financial_accounts/{{TREASURYFINANCIALACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d "treasury[access][requested]"=true \
  -d "card_issuing[access][requested]"=true
```

```cli
stripe treasury financial_accounts update {{TREASURYFINANCIALACCOUNT_ID}} \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  -d "treasury[access][requested]"=true \
  -d "card_issuing[access][requested]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

financial_account = client.v1.treasury.financial_accounts.update(
  '{{TREASURYFINANCIALACCOUNT_ID}}',
  {
    treasury: {access: {requested: true}},
    card_issuing: {access: {requested: true}},
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
financial_account = client.v1.treasury.financial_accounts.update(
  "{{TREASURYFINANCIALACCOUNT_ID}}",
  {
    "treasury": {"access": {"requested": True}},
    "card_issuing": {"access": {"requested": True}},
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$financialAccount = $stripe->treasury->financialAccounts->update(
  '{{TREASURYFINANCIALACCOUNT_ID}}',
  [
    'treasury' => ['access' => ['requested' => true]],
    'card_issuing' => ['access' => ['requested' => true]],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

FinancialAccountUpdateParams params =
  FinancialAccountUpdateParams.builder()
    .putExtraParam("treasury[access][requested]", true)
    .putExtraParam("card_issuing[access][requested]", true)
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

FinancialAccount financialAccount =
  client.v1().treasury().financialAccounts().update(
    "{{TREASURYFINANCIALACCOUNT_ID}}",
    params,
    requestOptions
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const financialAccount = await stripe.treasury.financialAccounts.update(
  '{{TREASURYFINANCIALACCOUNT_ID}}',
  {
    treasury: {
      access: {
        requested: true,
      },
    },
    card_issuing: {
      access: {
        requested: true,
      },
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
params := &stripe.TreasuryFinancialAccountUpdateParams{}
params.AddExtra("treasury[access][requested]", true)
params.AddExtra("card_issuing[access][requested]", true)
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryFinancialAccounts.Update(
  context.TODO(), "{{TREASURYFINANCIALACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.FinancialAccountUpdateOptions();
options.AddExtraParam("treasury[access][requested]", true);
options.AddExtraParam("card_issuing[access][requested]", true);
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.FinancialAccounts;
Stripe.Treasury.FinancialAccount financialAccount = service.Update(
    "{{TREASURYFINANCIALACCOUNT_ID}}",
    options,
    requestOptions);
```

If successful, the response returns the financial account object with the features listed in the `active_features` or `pending_features` array.

## Create a card

After the `card_issuing` capability is active, the sellers and service providers that own your platform’s connected accounts can create cardholders and cards. You can issue cards only through the API.

A [Cardholder object](https://docs.stripe.com/api/.md#issuing_cardholder_object) represents an individual or business entity that you can issue cards to. You can begin by creating a `Cardholder` with name, billing information, and whether they’re an `individual` or `company`.

```curl
curl https://api.stripe.com/v1/issuing/cardholders \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d name="Jenny Rosen" \
  --data-urlencode email="jenny.rosen@example.com" \
  --data-urlencode phone_number="+18008675309" \
  -d status=active \
  -d type=individual \
  -d "individual[first_name]"=Jenny \
  -d "individual[last_name]"=Rosen \
  -d "individual[dob][day]"=1 \
  -d "individual[dob][month]"=11 \
  -d "individual[dob][year]"=1981 \
  -d "billing[address][line1]"="1234 Main Street" \
  -d "billing[address][city]"="San Francisco" \
  -d "billing[address][state]"=CA \
  -d "billing[address][postal_code]"=94111 \
  -d "billing[address][country]"=US
```

```cli
stripe issuing cardholders create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --name="Jenny Rosen" \
  --email="jenny.rosen@example.com" \
  --phone-number="+18008675309" \
  --status=active \
  --type=individual \
  -d "individual[first_name]"=Jenny \
  -d "individual[last_name]"=Rosen \
  -d "individual[dob][day]"=1 \
  -d "individual[dob][month]"=11 \
  -d "individual[dob][year]"=1981 \
  -d "billing[address][line1]"="1234 Main Street" \
  -d "billing[address][city]"="San Francisco" \
  -d "billing[address][state]"=CA \
  -d "billing[address][postal_code]"=94111 \
  -d "billing[address][country]"=US
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

cardholder = client.v1.issuing.cardholders.create(
  {
    name: 'Jenny Rosen',
    email: 'jenny.rosen@example.com',
    phone_number: '+18008675309',
    status: 'active',
    type: 'individual',
    individual: {
      first_name: 'Jenny',
      last_name: 'Rosen',
      dob: {
        day: 1,
        month: 11,
        year: 1981,
      },
    },
    billing: {
      address: {
        line1: '1234 Main Street',
        city: 'San Francisco',
        state: 'CA',
        postal_code: '94111',
        country: 'US',
      },
    },
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
cardholder = client.v1.issuing.cardholders.create(
  {
    "name": "Jenny Rosen",
    "email": "jenny.rosen@example.com",
    "phone_number": "+18008675309",
    "status": "active",
    "type": "individual",
    "individual": {
      "first_name": "Jenny",
      "last_name": "Rosen",
      "dob": {"day": 1, "month": 11, "year": 1981},
    },
    "billing": {
      "address": {
        "line1": "1234 Main Street",
        "city": "San Francisco",
        "state": "CA",
        "postal_code": "94111",
        "country": "US",
      },
    },
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$cardholder = $stripe->issuing->cardholders->create(
  [
    'name' => 'Jenny Rosen',
    'email' => 'jenny.rosen@example.com',
    'phone_number' => '+18008675309',
    'status' => 'active',
    'type' => 'individual',
    'individual' => [
      'first_name' => 'Jenny',
      'last_name' => 'Rosen',
      'dob' => [
        'day' => 1,
        'month' => 11,
        'year' => 1981,
      ],
    ],
    'billing' => [
      'address' => [
        'line1' => '1234 Main Street',
        'city' => 'San Francisco',
        'state' => 'CA',
        'postal_code' => '94111',
        'country' => 'US',
      ],
    ],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CardholderCreateParams params =
  CardholderCreateParams.builder()
    .setName("Jenny Rosen")
    .setEmail("jenny.rosen@example.com")
    .setPhoneNumber("+18008675309")
    .setStatus(CardholderCreateParams.Status.ACTIVE)
    .setType(CardholderCreateParams.Type.INDIVIDUAL)
    .setIndividual(
      CardholderCreateParams.Individual.builder()
        .setFirstName("Jenny")
        .setLastName("Rosen")
        .setDob(
          CardholderCreateParams.Individual.Dob.builder()
            .setDay(1L)
            .setMonth(11L)
            .setYear(1981L)
            .build()
        )
        .build()
    )
    .setBilling(
      CardholderCreateParams.Billing.builder()
        .setAddress(
          CardholderCreateParams.Billing.Address.builder()
            .setLine1("1234 Main Street")
            .setCity("San Francisco")
            .setState("CA")
            .setPostalCode("94111")
            .setCountry("US")
            .build()
        )
        .build()
    )
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Cardholder cardholder =
  client.v1().issuing().cardholders().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const cardholder = await stripe.issuing.cardholders.create(
  {
    name: 'Jenny Rosen',
    email: 'jenny.rosen@example.com',
    phone_number: '+18008675309',
    status: 'active',
    type: 'individual',
    individual: {
      first_name: 'Jenny',
      last_name: 'Rosen',
      dob: {
        day: 1,
        month: 11,
        year: 1981,
      },
    },
    billing: {
      address: {
        line1: '1234 Main Street',
        city: 'San Francisco',
        state: 'CA',
        postal_code: '94111',
        country: 'US',
      },
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
params := &stripe.IssuingCardholderCreateParams{
  Name: stripe.String("Jenny Rosen"),
  Email: stripe.String("jenny.rosen@example.com"),
  PhoneNumber: stripe.String("+18008675309"),
  Status: stripe.String(stripe.IssuingCardholderStatusActive),
  Type: stripe.String(stripe.IssuingCardholderTypeIndividual),
  Individual: &stripe.IssuingCardholderCreateIndividualParams{
    FirstName: stripe.String("Jenny"),
    LastName: stripe.String("Rosen"),
    DOB: &stripe.IssuingCardholderCreateIndividualDOBParams{
      Day: stripe.Int64(1),
      Month: stripe.Int64(11),
      Year: stripe.Int64(1981),
    },
  },
  Billing: &stripe.IssuingCardholderCreateBillingParams{
    Address: &stripe.AddressParams{
      Line1: stripe.String("1234 Main Street"),
      City: stripe.String("San Francisco"),
      State: stripe.String("CA"),
      PostalCode: stripe.String("94111"),
      Country: stripe.String("US"),
    },
  },
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1IssuingCardholders.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Issuing.CardholderCreateOptions
{
    Name = "Jenny Rosen",
    Email = "jenny.rosen@example.com",
    PhoneNumber = "+18008675309",
    Status = "active",
    Type = "individual",
    Individual = new Stripe.Issuing.CardholderIndividualOptions
    {
        FirstName = "Jenny",
        LastName = "Rosen",
        Dob = new Stripe.Issuing.CardholderIndividualDobOptions
        {
            Day = 1,
            Month = 11,
            Year = 1981,
        },
    },
    Billing = new Stripe.Issuing.CardholderBillingOptions
    {
        Address = new AddressOptions
        {
            Line1 = "1234 Main Street",
            City = "San Francisco",
            State = "CA",
            PostalCode = "94111",
            Country = "US",
        },
    },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Issuing.Cardholders;
Stripe.Issuing.Cardholder cardholder = service.Create(options, requestOptions);
```

If successful, the response returns the newly created `Cardholder` object.

Create a [Card](https://docs.stripe.com/api/.md#issuing_card_object) and assign it to both the `Cardholder` you just created and a financial account. To assign the cardholder and financial account, specify the cardholder ID in the `cardholder` parameter and the financial account ID in the `financial_account` parameter of the `/v1/issuing/cards` request.

```curl
curl https://api.stripe.com/v1/issuing/cards \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d cardholder={{CARDHOLDER_ID}} \
  -d financial_account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  -d currency=usd \
  -d type=virtual \
  -d status=active
```

```cli
stripe issuing cards create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --cardholder={{CARDHOLDER_ID}} \
  --financial-account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  --currency=usd \
  --type=virtual \
  --status=active
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

card = client.v1.issuing.cards.create(
  {
    cardholder: '{{CARDHOLDER_ID}}',
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    currency: 'usd',
    type: 'virtual',
    status: 'active',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
card = client.v1.issuing.cards.create(
  {
    "cardholder": "{{CARDHOLDER_ID}}",
    "financial_account": "{{TREASURYFINANCIALACCOUNT_ID}}",
    "currency": "usd",
    "type": "virtual",
    "status": "active",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$card = $stripe->issuing->cards->create(
  [
    'cardholder' => '{{CARDHOLDER_ID}}',
    'financial_account' => '{{TREASURYFINANCIALACCOUNT_ID}}',
    'currency' => 'usd',
    'type' => 'virtual',
    'status' => 'active',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CardCreateParams params =
  CardCreateParams.builder()
    .setCardholder("{{CARDHOLDER_ID}}")
    .setFinancialAccount("{{TREASURYFINANCIALACCOUNT_ID}}")
    .setCurrency("usd")
    .setType(CardCreateParams.Type.VIRTUAL)
    .setStatus(CardCreateParams.Status.ACTIVE)
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Card card = client.v1().issuing().cards().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const card = await stripe.issuing.cards.create(
  {
    cardholder: '{{CARDHOLDER_ID}}',
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    currency: 'usd',
    type: 'virtual',
    status: 'active',
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
params := &stripe.IssuingCardCreateParams{
  Cardholder: stripe.String("{{CARDHOLDER_ID}}"),
  FinancialAccount: stripe.String("{{TREASURYFINANCIALACCOUNT_ID}}"),
  Currency: stripe.String(stripe.CurrencyUSD),
  Type: stripe.String(stripe.IssuingCardTypeVirtual),
  Status: stripe.String(stripe.IssuingCardStatusActive),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1IssuingCards.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Issuing.CardCreateOptions
{
    Cardholder = "{{CARDHOLDER_ID}}",
    FinancialAccount = "{{TREASURYFINANCIALACCOUNT_ID}}",
    Currency = "usd",
    Type = "virtual",
    Status = "active",
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Issuing.Cards;
Stripe.Issuing.Card card = service.Create(options, requestOptions);
```

If successful, the response returns the newly created `Card` object.

## Handle authorizations

Review the [Issuing authorizations](https://docs.stripe.com/issuing/purchases/authorizations.md) guide to properly handle authorizations.

### Create test authorizations

You can test the cards you just issued by following the steps in [Testing Issuing](https://docs.stripe.com/issuing/testing.md) to simulate purchases.

If the financial account associated with the issued card has [outbound_flows](https://docs.stripe.com/api/treasury/financial_accounts/create.md#create_financial_account-platform_restrictions-outbound_flows) restricted, authorizations on the card aren’t allowed.

See the [Issuing transactions](https://docs.stripe.com/issuing/purchases/transactions.md#handling-other-transactions) guide for information on different transaction types you might test against.

## Handle captures and refunds

See the [Issuing transactions](https://docs.stripe.com/issuing/purchases/transactions.md) guide to learn how to handle refunds and captures.

## Handle disputes

See the [Issuing disputes](https://docs.stripe.com/issuing/purchases/disputes.md) guide to learn how to properly handle disputes.
