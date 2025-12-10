# B2B Payments integration guide

Build a B2B Payments integration with Issuing.

Check out our introductory guide to using [BaaS for SaaS Platforms](https://stripe.com/guides/introduction-to-banking-as-a-service).

Build a US B2B Payments integration by using Stripe [Issuing](https://docs.stripe.com/issuing/how-issuing-works.md) to create cards for your business, employees, or contractors to make purchases on your behalf.

By the end of this guide, you’ll know how to:

- Fund your Issuing Balance
- Create virtual cards for your own business
- Use these cards to spend funds from your Issuing Balance

## Before you begin

1. Sign up for a [Stripe account](https://dashboard.stripe.com/register).
1. [Activate Issuing](https://dashboard.stripe.com/issuing/activate) in a *sandbox* (A sandbox is an isolated test environment that allows you to test Stripe functionality in your account without affecting your live integration. Use sandboxes to safely experiment with new features and changes) environment from the Dashboard.

## Add funds

To spend money using cards, add funds to the Issuing balance on your account. This balance represents funds reserved for Issuing and is safely separated from your earnings, payouts, and funds from other Stripe products.

You can add funds from your [Dashboard](https://dashboard.stripe.com/balance/overview#issuing-summary).

## Create cardholders and cards

### Create a cardholder 

The [Cardholder](https://docs.stripe.com/api/.md#issuing_cardholder_object) is the company or business entity that’s authorized to use card funding by the Issuing balance. The `Cardholder` object includes relevant details, such as a [name](https://docs.stripe.com/api/issuing/cardholders/object.md#issuing_cardholder_object-name) to display on cards and a [billing](https://docs.stripe.com/api/issuing/cardholders/object.md#issuing_cardholder_object-billing) address, which is usually the business address.

The following API call creates a new `Cardholder`:

```curl
curl https://api.stripe.com/v1/issuing/cardholders \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d name="Company Card" \
  --data-urlencode email="company@example.com" \
  --data-urlencode phone_number="+18008675309" \
  -d status=active \
  -d type=company \
  -d "billing[address][line1]"="123 Main Street" \
  -d "billing[address][city]"="San Francisco" \
  -d "billing[address][state]"=CA \
  -d "billing[address][postal_code]"=94111 \
  -d "billing[address][country]"=US
```

```cli
stripe issuing cardholders create  \
  --name="Company Card" \
  --email="company@example.com" \
  --phone-number="+18008675309" \
  --status=active \
  --type=company \
  -d "billing[address][line1]"="123 Main Street" \
  -d "billing[address][city]"="San Francisco" \
  -d "billing[address][state]"=CA \
  -d "billing[address][postal_code]"=94111 \
  -d "billing[address][country]"=US
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

cardholder = client.v1.issuing.cardholders.create({
  name: 'Company Card',
  email: 'company@example.com',
  phone_number: '+18008675309',
  status: 'active',
  type: 'company',
  billing: {
    address: {
      line1: '123 Main Street',
      city: 'San Francisco',
      state: 'CA',
      postal_code: '94111',
      country: 'US',
    },
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
cardholder = client.v1.issuing.cardholders.create({
  "name": "Company Card",
  "email": "company@example.com",
  "phone_number": "+18008675309",
  "status": "active",
  "type": "company",
  "billing": {
    "address": {
      "line1": "123 Main Street",
      "city": "San Francisco",
      "state": "CA",
      "postal_code": "94111",
      "country": "US",
    },
  },
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$cardholder = $stripe->issuing->cardholders->create([
  'name' => 'Company Card',
  'email' => 'company@example.com',
  'phone_number' => '+18008675309',
  'status' => 'active',
  'type' => 'company',
  'billing' => [
    'address' => [
      'line1' => '123 Main Street',
      'city' => 'San Francisco',
      'state' => 'CA',
      'postal_code' => '94111',
      'country' => 'US',
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CardholderCreateParams params =
  CardholderCreateParams.builder()
    .setName("Company Card")
    .setEmail("company@example.com")
    .setPhoneNumber("+18008675309")
    .setStatus(CardholderCreateParams.Status.ACTIVE)
    .setType(CardholderCreateParams.Type.COMPANY)
    .setBilling(
      CardholderCreateParams.Billing.builder()
        .setAddress(
          CardholderCreateParams.Billing.Address.builder()
            .setLine1("123 Main Street")
            .setCity("San Francisco")
            .setState("CA")
            .setPostalCode("94111")
            .setCountry("US")
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Cardholder cardholder = client.v1().issuing().cardholders().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const cardholder = await stripe.issuing.cardholders.create({
  name: 'Company Card',
  email: 'company@example.com',
  phone_number: '+18008675309',
  status: 'active',
  type: 'company',
  billing: {
    address: {
      line1: '123 Main Street',
      city: 'San Francisco',
      state: 'CA',
      postal_code: '94111',
      country: 'US',
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.IssuingCardholderCreateParams{
  Name: stripe.String("Company Card"),
  Email: stripe.String("company@example.com"),
  PhoneNumber: stripe.String("+18008675309"),
  Status: stripe.String(stripe.IssuingCardholderStatusActive),
  Type: stripe.String(stripe.IssuingCardholderTypeCompany),
  Billing: &stripe.IssuingCardholderCreateBillingParams{
    Address: &stripe.AddressParams{
      Line1: stripe.String("123 Main Street"),
      City: stripe.String("San Francisco"),
      State: stripe.String("CA"),
      PostalCode: stripe.String("94111"),
      Country: stripe.String("US"),
    },
  },
}
result, err := sc.V1IssuingCardholders.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Issuing.CardholderCreateOptions
{
    Name = "Company Card",
    Email = "company@example.com",
    PhoneNumber = "+18008675309",
    Status = "active",
    Type = "company",
    Billing = new Stripe.Issuing.CardholderBillingOptions
    {
        Address = new AddressOptions
        {
            Line1 = "123 Main Street",
            City = "San Francisco",
            State = "CA",
            PostalCode = "94111",
            Country = "US",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Issuing.Cardholders;
Stripe.Issuing.Cardholder cardholder = service.Create(options);
```

Stripe returns a `Cardholder` object that contains the information you provided and sends the `issuing_cardholder.created` webhook event.

### Create a card 

Create a card and attach it to the `Cardholder` that you want to make the authorized user of the card.

In the following examples, we show you how to create a [virtual card](https://docs.stripe.com/issuing/cards/virtual.md). You can, however, create [physical cards](https://docs.stripe.com/issuing/cards/physical.md) and ship them to cardholders in live mode.

```curl
curl https://api.stripe.com/v1/issuing/cards \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d currency=usd \
  -d type=virtual \
  -d cardholder="{{ISSUINGCARDHOLDER_ID}}"
```

```cli
stripe issuing cards create  \
  --currency=usd \
  --type=virtual \
  --cardholder="{{ISSUINGCARDHOLDER_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

card = client.v1.issuing.cards.create({
  currency: 'usd',
  type: 'virtual',
  cardholder: '{{ISSUINGCARDHOLDER_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
card = client.v1.issuing.cards.create({
  "currency": "usd",
  "type": "virtual",
  "cardholder": "{{ISSUINGCARDHOLDER_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$card = $stripe->issuing->cards->create([
  'currency' => 'usd',
  'type' => 'virtual',
  'cardholder' => '{{ISSUINGCARDHOLDER_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CardCreateParams params =
  CardCreateParams.builder()
    .setCurrency("usd")
    .setType(CardCreateParams.Type.VIRTUAL)
    .setCardholder("{{ISSUINGCARDHOLDER_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Card card = client.v1().issuing().cards().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const card = await stripe.issuing.cards.create({
  currency: 'usd',
  type: 'virtual',
  cardholder: '{{ISSUINGCARDHOLDER_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.IssuingCardCreateParams{
  Currency: stripe.String(stripe.CurrencyUSD),
  Type: stripe.String(stripe.IssuingCardTypeVirtual),
  Cardholder: stripe.String("{{ISSUINGCARDHOLDER_ID}}"),
}
result, err := sc.V1IssuingCards.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Issuing.CardCreateOptions
{
    Currency = "usd",
    Type = "virtual",
    Cardholder = "{{ISSUINGCARDHOLDER_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Issuing.Cards;
Stripe.Issuing.Card card = service.Create(options);
```

Stripe returns a `Card` object on creation, and sends the `issuing_card.created` webhook event:

```json
{
  "id": "ic_1NvPjF2SSJdH5vn2OVbE7r0b",
  "object": "issuing.card",
  "brand": "Visa",
  ...
  "status": "inactive",
  "type": "virtual"
}
```

You need to activate the card before a user can use it. While you can activate virtual cards in the same API call you used to create it, you must activate physical cards separately. When ready, activate the card by marking the `status` as `active`:

```curl
curl https://api.stripe.com/v1/issuing/cards/ic_1NvPjF2SSJdH5vn2OVbE7r0b \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d status=active
```

```cli
stripe issuing cards update ic_1NvPjF2SSJdH5vn2OVbE7r0b \
  --status=active
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

card = client.v1.issuing.cards.update('ic_1NvPjF2SSJdH5vn2OVbE7r0b', {status: 'active'})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
card = client.v1.issuing.cards.update(
  "ic_1NvPjF2SSJdH5vn2OVbE7r0b",
  {"status": "active"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$card = $stripe->issuing->cards->update(
  'ic_1NvPjF2SSJdH5vn2OVbE7r0b',
  ['status' => 'active']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CardUpdateParams params =
  CardUpdateParams.builder().setStatus(CardUpdateParams.Status.ACTIVE).build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Card card = client.v1().issuing().cards().update("ic_1NvPjF2SSJdH5vn2OVbE7r0b", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const card = await stripe.issuing.cards.update(
  'ic_1NvPjF2SSJdH5vn2OVbE7r0b',
  {
    status: 'active',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.IssuingCardUpdateParams{
  Status: stripe.String(stripe.IssuingCardStatusActive),
}
result, err := sc.V1IssuingCards.Update(
  context.TODO(), "ic_1NvPjF2SSJdH5vn2OVbE7r0b", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Issuing.CardUpdateOptions { Status = "active" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Issuing.Cards;
Stripe.Issuing.Card card = service.Update("ic_1NvPjF2SSJdH5vn2OVbE7r0b", options);
```

At this point, there’s now an active card attached to a cardholder. See the [Issuing page](https://dashboard.stripe.com/issuing/overview) to view the card and cardholder information.

```json
{
  "id": "ic_1NvPjF2SSJdH5vn2OVbE7r0b",
  "object": "issuing.card",
  "brand": "Visa",
  ...
  "status": "active",
  "type": "virtual",
}
```

To learn more, see:

- [Virtual cards](https://docs.stripe.com/issuing/cards/virtual.md)
- [Physical cards](https://docs.stripe.com/issuing/cards/physical.md)
- [Use the Dashboard for Issuing with Connect](https://docs.stripe.com/issuing/connect.md#using-dashboard-issuing)
- [Create cards with the API](https://docs.stripe.com/api/issuing/cards.md)

## Use the card

### Create an authorization 

To observe the impact of card activity on the associated balance, generate a test authorization. You can do this in the **Issuing page** of the Dashboard, or with the following call to the [Authorization API](https://docs.stripe.com/api/issuing/authorizations.md):

```curl
curl https://api.stripe.com/v1/test_helpers/issuing/authorizations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d card="{{ISSUINGCARD_ID}}" \
  -d amount=1000 \
  -d authorization_method=chip \
  -d "merchant_data[category]"=taxicabs_limousines \
  -d "merchant_data[city]"="San Francisco" \
  -d "merchant_data[country]"=US \
  -d "merchant_data[name]"="Rocket Rides" \
  -d "merchant_data[network_id]"=1234567890 \
  -d "merchant_data[postal_code]"=94107 \
  -d "merchant_data[state]"=CA
```

```cli
stripe test_helpers issuing authorizations create  \
  --card="{{ISSUINGCARD_ID}}" \
  --amount=1000 \
  --authorization-method=chip \
  -d "merchant_data[category]"=taxicabs_limousines \
  -d "merchant_data[city]"="San Francisco" \
  -d "merchant_data[country]"=US \
  -d "merchant_data[name]"="Rocket Rides" \
  -d "merchant_data[network_id]"=1234567890 \
  -d "merchant_data[postal_code]"=94107 \
  -d "merchant_data[state]"=CA
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

authorization = client.v1.test_helpers.issuing.authorizations.create({
  card: '{{ISSUINGCARD_ID}}',
  amount: 1000,
  authorization_method: 'chip',
  merchant_data: {
    category: 'taxicabs_limousines',
    city: 'San Francisco',
    country: 'US',
    name: 'Rocket Rides',
    network_id: '1234567890',
    postal_code: '94107',
    state: 'CA',
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
authorization = client.v1.test_helpers.issuing.authorizations.create({
  "card": "{{ISSUINGCARD_ID}}",
  "amount": 1000,
  "authorization_method": "chip",
  "merchant_data": {
    "category": "taxicabs_limousines",
    "city": "San Francisco",
    "country": "US",
    "name": "Rocket Rides",
    "network_id": "1234567890",
    "postal_code": "94107",
    "state": "CA",
  },
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$authorization = $stripe->testHelpers->issuing->authorizations->create([
  'card' => '{{ISSUINGCARD_ID}}',
  'amount' => 1000,
  'authorization_method' => 'chip',
  'merchant_data' => [
    'category' => 'taxicabs_limousines',
    'city' => 'San Francisco',
    'country' => 'US',
    'name' => 'Rocket Rides',
    'network_id' => '1234567890',
    'postal_code' => '94107',
    'state' => 'CA',
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AuthorizationCreateParams params =
  AuthorizationCreateParams.builder()
    .setCard("{{ISSUINGCARD_ID}}")
    .setAmount(1000L)
    .setAuthorizationMethod(AuthorizationCreateParams.AuthorizationMethod.CHIP)
    .setMerchantData(
      AuthorizationCreateParams.MerchantData.builder()
        .setCategory(AuthorizationCreateParams.MerchantData.Category.TAXICABS_LIMOUSINES)
        .setCity("San Francisco")
        .setCountry("US")
        .setName("Rocket Rides")
        .setNetworkId("1234567890")
        .setPostalCode("94107")
        .setState("CA")
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Authorization authorization =
  client.v1().testHelpers().issuing().authorizations().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const authorization = await stripe.testHelpers.issuing.authorizations.create({
  card: '{{ISSUINGCARD_ID}}',
  amount: 1000,
  authorization_method: 'chip',
  merchant_data: {
    category: 'taxicabs_limousines',
    city: 'San Francisco',
    country: 'US',
    name: 'Rocket Rides',
    network_id: '1234567890',
    postal_code: '94107',
    state: 'CA',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TestHelpersIssuingAuthorizationCreateParams{
  Card: stripe.String("{{ISSUINGCARD_ID}}"),
  Amount: stripe.Int64(1000),
  AuthorizationMethod: stripe.String(stripe.IssuingAuthorizationAuthorizationMethodChip),
  MerchantData: &stripe.TestHelpersIssuingAuthorizationCreateMerchantDataParams{
    Category: stripe.String("taxicabs_limousines"),
    City: stripe.String("San Francisco"),
    Country: stripe.String("US"),
    Name: stripe.String("Rocket Rides"),
    NetworkID: stripe.String("1234567890"),
    PostalCode: stripe.String("94107"),
    State: stripe.String("CA"),
  },
}
result, err := sc.V1TestHelpersIssuingAuthorizations.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.TestHelpers.Issuing.AuthorizationCreateOptions
{
    Card = "{{ISSUINGCARD_ID}}",
    Amount = 1000,
    AuthorizationMethod = "chip",
    MerchantData = new Stripe.TestHelpers.Issuing.AuthorizationMerchantDataOptions
    {
        Category = "taxicabs_limousines",
        City = "San Francisco",
        Country = "US",
        Name = "Rocket Rides",
        NetworkId = "1234567890",
        PostalCode = "94107",
        State = "CA",
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.TestHelpers.Issuing.Authorizations;
Stripe.Issuing.Authorization authorization = service.Create(options);
```

After approval, Stripe creates an `Authorization` in a `pending` state while it waits for [capture](https://docs.stripe.com/issuing/purchases/transactions.md). Note the authorization `id` that you’ll use to capture the funds:

```json
{"id": "iauth_1NvPyY2SSJdH5vn2xZQE8C7k",
  "object": "issuing.authorization",
  "amount": 1000,
  ...
  "status": "pending",
  "transactions": [],
}
```

### Capture the funds 

Capture the funds using the following code:

```curl
curl -X POST https://api.stripe.com/v1/test_helpers/issuing/authorizations/{{ISSUINGAUTHORIZATION_ID}}/capture \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe test_helpers issuing authorizations capture {{ISSUINGAUTHORIZATION_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

authorization = client.v1.test_helpers.issuing.authorizations.capture('{{ISSUINGAUTHORIZATION_ID}}')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
authorization = client.v1.test_helpers.issuing.authorizations.capture(
  "{{ISSUINGAUTHORIZATION_ID}}",
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$authorization = $stripe->testHelpers->issuing->authorizations->capture(
  '{{ISSUINGAUTHORIZATION_ID}}',
  []
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AuthorizationCaptureParams params = AuthorizationCaptureParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Authorization authorization =
  client.v1().testHelpers().issuing().authorizations().capture(
    "{{ISSUINGAUTHORIZATION_ID}}",
    params
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const authorization = await stripe.testHelpers.issuing.authorizations.capture(
  '{{ISSUINGAUTHORIZATION_ID}}'
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TestHelpersIssuingAuthorizationCaptureParams{}
result, err := sc.V1TestHelpersIssuingAuthorizations.Capture(
  context.TODO(), "{{ISSUINGAUTHORIZATION_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.TestHelpers.Issuing.Authorizations;
Stripe.Issuing.Authorization authorization = service.Capture(
    "{{ISSUINGAUTHORIZATION_ID}}");
```

After the authorization is captured, Stripe creates an Issuing [Transaction](https://docs.stripe.com/issuing/purchases/transactions.md), the `status` of the authorization is set to `closed`.

## See also

- [Spending controls](https://docs.stripe.com/issuing/controls/spending-controls.md)
- [Issuing authorizations](https://docs.stripe.com/issuing/purchases/authorizations.md)
- [Issuing transactions](https://docs.stripe.com/issuing/purchases/transactions.md)
- [Working with Stripe Issuing cards and Financial Accounts for platforms](https://docs.stripe.com/financial-accounts/connect/account-management/issuing-cards.md)
- [Manage transaction fraud](https://docs.stripe.com/issuing/manage-fraud.md)
