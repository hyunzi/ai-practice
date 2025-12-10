# Manual currency prices

Present local currencies to customers with manual currency prices.

# Stripe-hosted page

> This is a Stripe-hosted page for when payment-ui is stripe-hosted. View the full page at https://docs.stripe.com/payments/checkout/localize-prices/manual-currency-prices?payment-ui=stripe-hosted.

Stripe supports manually defining prices in different currencies when creating [products](https://docs.stripe.com/products-prices/overview.md#get-started). However, Stripe recommends using [Adaptive Pricing](https://docs.stripe.com/payments/currencies/localize-prices/adaptive-pricing.md) instead of manual currency prices to reduce currency exchange rate fluctuation risk and to automatically enable support for over 100 local currencies.

Use manual currency prices instead of Adaptive Pricing when:

- Adaptive Pricing isn’t [supported](https://docs.stripe.com/payments/currencies/localize-prices/adaptive-pricing.md#restrictions) for your business or Checkout configuration.
- You’re supporting a region where you’re comfortable taking on fluctuations in the currency’s exchange rate.

Manually defined multi-currency prices override Adaptive Pricing for those currencies, even if it’s enabled.

## Create a multi-currency price [Dashboard] [Server-side]

#### Dashboard

1. Go to a product in the [Dashboard](https://dashboard.stripe.com/products?active=true).
1. Click  **+Add another price** to create a new price.
1. Fill in the price and select a currency. This first currency is the price’s default currency. Make sure all of your prices have the same default currency.
1. Click **+Add a price by currency** to search and select from supported currencies, adding them to your price.
1. Use the multi-currency price you created by passing its ID into [line items](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-line_items-price) when you create a Checkout Session.

#### API

Add multiple currencies to a Price by specifying `currency_options` when using the [Prices API](https://docs.stripe.com/api/prices/object.md#price_object-currency_options).

```curl
curl https://api.stripe.com/v1/prices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d currency=usd \
  -d unit_amount=1000 \
  -d "currency_options[eur][unit_amount]"=950 \
  -d "currency_options[jpy][unit_amount]"=1500 \
  -d "product_data[name]"="My Product"
```

```cli
stripe prices create  \
  --currency=usd \
  --unit-amount=1000 \
  -d "currency_options[eur][unit_amount]"=950 \
  -d "currency_options[jpy][unit_amount]"=1500 \
  -d "product_data[name]"="My Product"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.create({
  currency: 'usd',
  unit_amount: 1000,
  currency_options: {
    eur: {unit_amount: 950},
    jpy: {unit_amount: 1500},
  },
  product_data: {name: 'My Product'},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
price = client.v1.prices.create({
  "currency": "usd",
  "unit_amount": 1000,
  "currency_options": {"eur": {"unit_amount": 950}, "jpy": {"unit_amount": 1500}},
  "product_data": {"name": "My Product"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$price = $stripe->prices->create([
  'currency' => 'usd',
  'unit_amount' => 1000,
  'currency_options' => [
    'eur' => ['unit_amount' => 950],
    'jpy' => ['unit_amount' => 1500],
  ],
  'product_data' => ['name' => 'My Product'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PriceCreateParams params =
  PriceCreateParams.builder()
    .setCurrency("usd")
    .setUnitAmount(1000L)
    .putCurrencyOption(
      "eur",
      PriceCreateParams.CurrencyOption.builder().setUnitAmount(950L).build()
    )
    .putCurrencyOption(
      "jpy",
      PriceCreateParams.CurrencyOption.builder().setUnitAmount(1500L).build()
    )
    .setProductData(PriceCreateParams.ProductData.builder().setName("My Product").build())
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Price price = client.v1().prices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const price = await stripe.prices.create({
  currency: 'usd',
  unit_amount: 1000,
  currency_options: {
    eur: {
      unit_amount: 950,
    },
    jpy: {
      unit_amount: 1500,
    },
  },
  product_data: {
    name: 'My Product',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PriceCreateParams{
  Currency: stripe.String(stripe.CurrencyUSD),
  UnitAmount: stripe.Int64(1000),
  CurrencyOptions: map[string]*stripe.PriceCreateCurrencyOptionsParams{
    "eur": &stripe.PriceCreateCurrencyOptionsParams{UnitAmount: stripe.Int64(950)},
    "jpy": &stripe.PriceCreateCurrencyOptionsParams{UnitAmount: stripe.Int64(1500)},
  },
  ProductData: &stripe.PriceCreateProductDataParams{Name: stripe.String("My Product")},
}
result, err := sc.V1Prices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PriceCreateOptions
{
    Currency = "usd",
    UnitAmount = 1000,
    CurrencyOptions = new Dictionary<string, PriceCurrencyOptionsOptions>
    {
        { "eur", new PriceCurrencyOptionsOptions { UnitAmount = 950 } },
        { "jpy", new PriceCurrencyOptionsOptions { UnitAmount = 1500 } },
    },
    ProductData = new PriceProductDataOptions { Name = "My Product" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Create(options);
```

In this example, the Price is created in USD, with additional currency options in EUR and JPY.

## Create a Checkout Session [Server-side]

Create a Checkout Session using the multi-currency price:

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  success_url: 'https://example.com/success',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "mode": "payment",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Testing [Server-side] [Client-side]

To test local currency presentment for Checkout, Payment Links, and the [pricing table](https://docs.stripe.com/payments/checkout/pricing-table.md), pass in a location-formatted customer email that includes a suffix in a `+location_XX` format in the local part of the email. `XX` must be a valid [two-letter ISO country code](https://www.nationsonline.org/oneworld/country_code_list.htm).

For example, to test currency presentment for a customer in France, pass in an email such as `test+location_FR@example.com`.

When you visit the URL for a Checkout Session, Payment Link, or pricing table created with a location-formatted email, you see the same currency as a customer does in the specified country.

### Testing Checkout

When you create a Checkout Session, pass the location-formatted email as [customer_email](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-customer_email) to simulate Checkout from a particular country.

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --success-url="https://example.com/success" \
  --customer-email="test+location_FR@example.com"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  success_url: 'https://example.com/success',
  customer_email: 'test+location_FR@example.com',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "mode": "payment",
  "success_url": "https://example.com/success",
  "customer_email": "test+location_FR@example.com",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
  'customer_email' => 'test+location_FR@example.com',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .setCustomerEmail("test+location_FR@example.com")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  success_url: 'https://example.com/success',
  customer_email: 'test+location_FR@example.com',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
  CustomerEmail: stripe.String("test+location_FR@example.com"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
    CustomerEmail = "test+location_FR@example.com",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u <<YOUR_SECRET_KEY>>: \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  -d success_url="https://example.com/success" \
  --data-urlencode customer_email="test+location_FR@example.com"
```

You can also create a [Customer](https://docs.stripe.com/api/customers/create.md) and specify their email that contains the `+location_XX` suffix. Stripe test cards work as usual.

When it’s possible to present the customer’s local currency in Checkout, the [Checkout Session](https://docs.stripe.com/api/checkout/sessions/object.md) object changes. Fields like `currency`, `payment_method_types`, and `amount_total` reflect the local currency and price.

### Testing Payment Links

For Payment Links, pass the location-formatted email as the `prefilled_email` [URL parameter](https://docs.stripe.com/payment-links/customize.md#customize-checkout-with-url-parameters) to test currency presentment for customers in different countries.

### Testing Pricing table

For the pricing table, pass the location-formatted email as the [customer-email](https://docs.stripe.com/payments/checkout/pricing-table.md#customer-email) attribute to test currency presentment for customers in different countries.

## Optional: Specify a currency [Server-side]

When you use multi-currency Prices in a Session, Checkout automatically handles currency localization for your customers. However, you can override this behavior by specifying a currency when you create the Checkout Session.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d currency=eur \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  --currency=eur \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  currency: 'eur',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  success_url: 'https://example.com/success',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "currency": "eur",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "mode": "payment",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'currency' => 'eur',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setCurrency("eur")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  currency: 'eur',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Currency: stripe.String(stripe.CurrencyEUR),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Currency = "eur",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

In this example, the Checkout Session’s currency is always EUR (`eur`) regardless of the customer’s location.

## Local payment methods 

The Checkout Session presents customers with popular payment methods compatible with their local currencies. For example, for customers located in the Netherlands, the Checkout Session converts prices to EUR and also present popular Dutch payment methods like iDEAL.

You can configure which payment methods you accept in your [payment methods settings](https://dashboard.stripe.com/settings/payment_methods).

## Pricing tables 

Manual currency prices also work with [pricing tables](https://docs.stripe.com/payments/checkout/pricing-table.md). To render local currencies to customers viewing your pricing table, all of the pricing table’s Prices must include the customer’s local currency in their `currency_options`. They must also include a `tax_behavior` for the given currency if you’re using Stripe Tax.

## Supported integrations 

Checkout automatically presents the local currency to customers if all of the following are true:

- The Checkout Session’s prices, shipping rates, and discounts have the relevant currency in their `currency_options`.
- If a price on the Checkout Session has an upsell, the upsell’s price has the relevant currency in its `currency_options`.
- For a Checkout Session using Stripe Tax, the `tax_behavior` on the Checkout Session is specified for the relevant currency for all of the Checkout Session’s prices, shipping rates, and discounts.
- You didn’t specify a currency during Checkout Session creation.

If Checkout can’t localize the currency because the relevant currency option or `tax_behavior` is missing, the Session presents to the customer in the default currency. The default currency must be the same across all prices, shipping rates, and discounts.

### Restrictions 

Price localization isn’t available for Checkout Sessions that:

- Use manual tax rates.
- Use `payment_intent_data.application_fee_amount` or `payment_intent_data.transfer_data.amount`.

## Fees 

Stripe’s standard transaction fees apply to automatically converted transactions:

- Cards or payment methods fee
- International cards or payment methods fee (if applicable)
- Currency conversion fee

See the [pricing page](https://stripe.com/pricing) for more details about these fees.


# Embedded form

> This is a Embedded form for when payment-ui is embedded-form. View the full page at https://docs.stripe.com/payments/checkout/localize-prices/manual-currency-prices?payment-ui=embedded-form.

Stripe supports manually defining prices in different currencies when creating [products](https://docs.stripe.com/products-prices/overview.md#get-started). However, Stripe recommends using [Adaptive Pricing](https://docs.stripe.com/payments/currencies/localize-prices/adaptive-pricing.md) instead of manual currency prices to reduce currency exchange rate fluctuation risk and to automatically enable support for over 100 local currencies.

Use manual currency prices instead of Adaptive Pricing when:

- Adaptive Pricing isn’t [supported](https://docs.stripe.com/payments/currencies/localize-prices/adaptive-pricing.md#restrictions) for your business or Checkout configuration. Reach out to [adaptive-pricing-beta@stripe.com](mailto:adaptive-pricing-beta@stripe.com) to ask about joining the preview.
- You’re supporting a region where you’re comfortable taking on fluctuations in the currency’s exchange rate.

Manually defined multi-currency prices override Adaptive Pricing for those currencies, even if it’s enabled.

## Create a multi-currency price [Dashboard] [Server-side]

#### Dashboard

1. Go to a product in the [Dashboard](https://dashboard.stripe.com/products?active=true).
1. Click  **+Add another price** to create a new price.
1. Fill in the price and select a currency. This first currency is the price’s default currency. Make sure all of your prices have the same default currency.
1. Click **+Add a price by currency** to search and select from supported currencies, adding them to your price.
1. Use the multi-currency price you created by passing its ID into [line items](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-line_items-price) when you create a Checkout Session.

#### API

Add multiple currencies to a Price by specifying `currency_options` when using the [Prices API](https://docs.stripe.com/api/prices/object.md#price_object-currency_options).

```curl
curl https://api.stripe.com/v1/prices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d currency=usd \
  -d unit_amount=1000 \
  -d "currency_options[eur][unit_amount]"=950 \
  -d "currency_options[jpy][unit_amount]"=1500 \
  -d "product_data[name]"="My Product"
```

```cli
stripe prices create  \
  --currency=usd \
  --unit-amount=1000 \
  -d "currency_options[eur][unit_amount]"=950 \
  -d "currency_options[jpy][unit_amount]"=1500 \
  -d "product_data[name]"="My Product"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.create({
  currency: 'usd',
  unit_amount: 1000,
  currency_options: {
    eur: {unit_amount: 950},
    jpy: {unit_amount: 1500},
  },
  product_data: {name: 'My Product'},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
price = client.v1.prices.create({
  "currency": "usd",
  "unit_amount": 1000,
  "currency_options": {"eur": {"unit_amount": 950}, "jpy": {"unit_amount": 1500}},
  "product_data": {"name": "My Product"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$price = $stripe->prices->create([
  'currency' => 'usd',
  'unit_amount' => 1000,
  'currency_options' => [
    'eur' => ['unit_amount' => 950],
    'jpy' => ['unit_amount' => 1500],
  ],
  'product_data' => ['name' => 'My Product'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PriceCreateParams params =
  PriceCreateParams.builder()
    .setCurrency("usd")
    .setUnitAmount(1000L)
    .putCurrencyOption(
      "eur",
      PriceCreateParams.CurrencyOption.builder().setUnitAmount(950L).build()
    )
    .putCurrencyOption(
      "jpy",
      PriceCreateParams.CurrencyOption.builder().setUnitAmount(1500L).build()
    )
    .setProductData(PriceCreateParams.ProductData.builder().setName("My Product").build())
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Price price = client.v1().prices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const price = await stripe.prices.create({
  currency: 'usd',
  unit_amount: 1000,
  currency_options: {
    eur: {
      unit_amount: 950,
    },
    jpy: {
      unit_amount: 1500,
    },
  },
  product_data: {
    name: 'My Product',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PriceCreateParams{
  Currency: stripe.String(stripe.CurrencyUSD),
  UnitAmount: stripe.Int64(1000),
  CurrencyOptions: map[string]*stripe.PriceCreateCurrencyOptionsParams{
    "eur": &stripe.PriceCreateCurrencyOptionsParams{UnitAmount: stripe.Int64(950)},
    "jpy": &stripe.PriceCreateCurrencyOptionsParams{UnitAmount: stripe.Int64(1500)},
  },
  ProductData: &stripe.PriceCreateProductDataParams{Name: stripe.String("My Product")},
}
result, err := sc.V1Prices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PriceCreateOptions
{
    Currency = "usd",
    UnitAmount = 1000,
    CurrencyOptions = new Dictionary<string, PriceCurrencyOptionsOptions>
    {
        { "eur", new PriceCurrencyOptionsOptions { UnitAmount = 950 } },
        { "jpy", new PriceCurrencyOptionsOptions { UnitAmount = 1500 } },
    },
    ProductData = new PriceProductDataOptions { Name = "My Product" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Create(options);
```

In this example, the Price is created in USD, with additional currency options in EUR and JPY.

## Create a Checkout Session [Server-side]

Create a Checkout Session using the multi-currency price:

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Testing [Server-side] [Client-side]

To test local currency presentment, pass in a location-formatted customer email that includes a suffix in a `+location_XX` format in the local part of the email. `XX` must be a valid [two-letter ISO country code](https://www.nationsonline.org/oneworld/country_code_list.htm).

For example, to test currency presentment for a customer in France, pass in an email like `test+location_FR@example.com`. When you visit the URL for a Checkout Session created with a location-formatted email, you see the same currency as a customer does in the specified country.

When you create a Checkout Session, pass the location-formatted email as [customer_email](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-customer_email) to simulate a particular country.

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return" \
  --customer-email="test+location_FR@example.com"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  customer_email: 'test+location_FR@example.com',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
  "customer_email": "test+location_FR@example.com",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
  'customer_email' => 'test+location_FR@example.com',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .setCustomerEmail("test+location_FR@example.com")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  customer_email: 'test+location_FR@example.com',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
  CustomerEmail: stripe.String("test+location_FR@example.com"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
    CustomerEmail = "test+location_FR@example.com",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u <<YOUR_SECRET_KEY>>: \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  -d ui_mode=embedded \
  -d return_url="https://example.com/return" \
  --data-urlencode customer_email="test+location_FR@example.com"
```

You can also create a [Customer](https://docs.stripe.com/api/customers/create.md) and specify their email that contains the `+location_XX` suffix. Stripe test cards work as usual.

When it’s possible to present the customer’s local currency, the [Checkout Session](https://docs.stripe.com/api/checkout/sessions/object.md) object changes. Fields like `currency`, `payment_method_types`, and `amount_total` reflect the local currency and price.

## Optional: Specify a currency [Server-side]

When you use multi-currency Prices, the Checkout Session automatically handles currency localization for your customers. However, you can override this behavior by specifying a currency when you create the Checkout Session.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d currency=eur \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  --currency=eur \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  currency: 'eur',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "currency": "eur",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'currency' => 'eur',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setCurrency("eur")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  currency: 'eur',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Currency: stripe.String(stripe.CurrencyEUR),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Currency = "eur",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

In this example, the Checkout Session’s currency is always EUR (`eur`) regardless of the customer’s location.

## Local payment methods 

The Checkout Session presents customers with popular payment methods compatible with their local currencies. For example, for customers located in the Netherlands, the Checkout Session converts prices to EUR and also present popular Dutch payment methods like iDEAL.

You can configure which payment methods you accept in your [payment methods settings](https://dashboard.stripe.com/settings/payment_methods).

## Supported integrations 

Checkout automatically presents the local currency to customers if all of the following are true:

- The Checkout Session’s prices, shipping rates, and discounts have the relevant currency in their `currency_options`.
- If a price on the Checkout Session has an upsell, the upsell’s price has the relevant currency in its `currency_options`.
- For a Checkout Session using Stripe Tax, the `tax_behavior` on the Checkout Session is specified for the relevant currency for all of the Checkout Session’s prices, shipping rates, and discounts.
- You didn’t specify a currency during Checkout Session creation.

If Checkout can’t localize the currency because the relevant currency option or `tax_behavior` is missing, the Session presents to the customer in the default currency. The default currency must be the same across all prices, shipping rates, and discounts.

### Restrictions 

Price localization isn’t available for Checkout Sessions that:

- Use manual tax rates.
- Use `payment_intent_data.application_fee_amount` or `payment_intent_data.transfer_data.amount`.

## Fees 

Stripe’s standard transaction fees apply to automatically converted transactions:

- Cards or payment methods fee
- International cards or payment methods fee (if applicable)
- Currency conversion fee

See the [pricing page](https://stripe.com/pricing) for more details about these fees.

