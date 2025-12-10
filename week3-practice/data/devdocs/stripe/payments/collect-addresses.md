# Collect physical addresses

Learn how to collect billing and shipping addresses.

When you use a Stripe-hosted payment page or embedded payment form, you can collect billing and shipping addresses.

## Collect a billing address

By default, a Checkout Session only collects a customer’s billing address when necessary (for example, to calculate tax). To always collect a billing address, set `billing_address_collection` to `required` when you [create a Checkout Session](https://docs.stripe.com/api/checkout/sessions/create.md).

#### Stripe-hosted page

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d billing_address_collection=required \
  -d "automatic_tax[enabled]"=true \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  --billing-address-collection=required \
  -d "automatic_tax[enabled]"=true \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  billing_address_collection: 'required',
  automatic_tax: {enabled: true},
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
  "billing_address_collection": "required",
  "automatic_tax": {"enabled": True},
  "mode": "payment",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'billing_address_collection' => 'required',
  'automatic_tax' => ['enabled' => true],
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
    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
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
  billing_address_collection: 'required',
  automatic_tax: {
    enabled: true,
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  BillingAddressCollection: stripe.String(stripe.CheckoutSessionBillingAddressCollectionRequired),
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
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
    BillingAddressCollection = "required",
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

#### Embedded form

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d billing_address_collection=required \
  -d "automatic_tax[enabled]"=true \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  --billing-address-collection=required \
  -d "automatic_tax[enabled]"=true \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  billing_address_collection: 'required',
  automatic_tax: {enabled: true},
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
  "billing_address_collection": "required",
  "automatic_tax": {"enabled": True},
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
  'billing_address_collection' => 'required',
  'automatic_tax' => ['enabled' => true],
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
    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
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
  billing_address_collection: 'required',
  automatic_tax: {
    enabled: true,
  },
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
  BillingAddressCollection: stripe.String(stripe.CheckoutSessionBillingAddressCollectionRequired),
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
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
    BillingAddressCollection = "required",
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Collect a shipping address

To collect a customer’s shipping address in Checkout, pass the `shipping_address_collection` parameter when you [create a Checkout Session](https://docs.stripe.com/api/checkout/sessions/create.md). When you collect a shipping address, you must also specify which countries to allow shipping to. Configure the `allowed_countries` property with an array of [two-letter ISO country codes](https://www.nationsonline.org/oneworld/country_code_list.htm).

#### Stripe-hosted page

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d billing_address_collection=required \
  -d "shipping_address_collection[allowed_countries][]"=US \
  -d "shipping_address_collection[allowed_countries][]"=CA \
  -d "automatic_tax[enabled]"=true \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  --billing-address-collection=required \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d "shipping_address_collection[allowed_countries][1]"=CA \
  -d "automatic_tax[enabled]"=true \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  billing_address_collection: 'required',
  shipping_address_collection: {allowed_countries: ['US', 'CA']},
  automatic_tax: {enabled: true},
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
  "billing_address_collection": "required",
  "shipping_address_collection": {"allowed_countries": ["US", "CA"]},
  "automatic_tax": {"enabled": True},
  "mode": "payment",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'billing_address_collection' => 'required',
  'shipping_address_collection' => ['allowed_countries' => ['US', 'CA']],
  'automatic_tax' => ['enabled' => true],
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
    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
    .setShippingAddressCollection(
      SessionCreateParams.ShippingAddressCollection.builder()
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.US
        )
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.CA
        )
        .build()
    )
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
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
  billing_address_collection: 'required',
  shipping_address_collection: {
    allowed_countries: ['US', 'CA'],
  },
  automatic_tax: {
    enabled: true,
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  BillingAddressCollection: stripe.String(stripe.CheckoutSessionBillingAddressCollectionRequired),
  ShippingAddressCollection: &stripe.CheckoutSessionCreateShippingAddressCollectionParams{
    AllowedCountries: []*string{stripe.String("US"), stripe.String("CA")},
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
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
    BillingAddressCollection = "required",
    ShippingAddressCollection = new Stripe.Checkout.SessionShippingAddressCollectionOptions
    {
        AllowedCountries = new List<string> { "US", "CA" },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

#### Embedded form

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d billing_address_collection=required \
  -d "shipping_address_collection[allowed_countries][]"=US \
  -d "shipping_address_collection[allowed_countries][]"=CA \
  -d "automatic_tax[enabled]"=true \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  --billing-address-collection=required \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d "shipping_address_collection[allowed_countries][1]"=CA \
  -d "automatic_tax[enabled]"=true \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  billing_address_collection: 'required',
  shipping_address_collection: {allowed_countries: ['US', 'CA']},
  automatic_tax: {enabled: true},
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
  "billing_address_collection": "required",
  "shipping_address_collection": {"allowed_countries": ["US", "CA"]},
  "automatic_tax": {"enabled": True},
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
  'billing_address_collection' => 'required',
  'shipping_address_collection' => ['allowed_countries' => ['US', 'CA']],
  'automatic_tax' => ['enabled' => true],
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
    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
    .setShippingAddressCollection(
      SessionCreateParams.ShippingAddressCollection.builder()
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.US
        )
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.CA
        )
        .build()
    )
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
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
  billing_address_collection: 'required',
  shipping_address_collection: {
    allowed_countries: ['US', 'CA'],
  },
  automatic_tax: {
    enabled: true,
  },
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
  BillingAddressCollection: stripe.String(stripe.CheckoutSessionBillingAddressCollectionRequired),
  ShippingAddressCollection: &stripe.CheckoutSessionCreateShippingAddressCollectionParams{
    AllowedCountries: []*string{stripe.String("US"), stripe.String("CA")},
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
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
    BillingAddressCollection = "required",
    ShippingAddressCollection = new Stripe.Checkout.SessionShippingAddressCollectionOptions
    {
        AllowedCountries = new List<string> { "US", "CA" },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

When the customer completes the session, the [Checkout Session](https://docs.stripe.com/api/checkout/sessions/object.md) object saves the collected shipping address on the `shipping_details` property and includes it in the payload of the `checkout.session.completed` *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests). You can also see shipping information in the Dashboard on the payment details page.

## See also

- [Charge for shipping](https://docs.stripe.com/payments/during-payment/charge-shipping.md)
- [Collect phone numbers](https://docs.stripe.com/payments/checkout/phone-numbers.md)
