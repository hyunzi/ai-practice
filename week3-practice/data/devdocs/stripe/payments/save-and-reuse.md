# Save a customer's payment method without making a payment

Learn how to save a payment method and charge it later.

# Checkout Sessions API

> This is a Checkout Sessions API for when payment-ui is embedded-components. View the full page at https://docs.stripe.com/payments/save-and-reuse?payment-ui=embedded-components.

The [Checkout Sessions API in `setup` mode](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-mode) lets you save a customer’s payment details without an initial payment. This is helpful if you want to onboard customers now, set them up for payments, and charge them using the Payment Intents API in the future—when they’re offline.

Use this integration to set up recurring payments or to create one-time payments with a final amount determined later, often after the customer receives your service.

> #### Card-present transactions
> 
> Card-present transactions, such as [collecting card details through Stripe Terminal](https://docs.stripe.com/terminal/features/saving-payment-details/save-directly.md), use a different process for saving the payment method.

## Compliance

You’re responsible for your compliance with all applicable laws, regulations, and network rules when saving a customer’s payment details. These requirements generally apply if you want to save your customer’s payment method for future use, such as displaying a customer’s payment method to them in the checkout flow for a future purchase or charging them when they’re not actively using your website or app. Add terms to your website or app that state how you plan to save payment method details and allow customers to opt in.

When you save a payment method, you can only use it for the specific usage you have included in your terms. To charge a payment method when a customer is offline and save it as an option for future purchases, make sure that you explicitly collect consent from the customer for this specific use. For example, include a “Save my payment method for future use” checkbox to collect consent.

To charge a customer when they’re offline, make sure your terms include the following:

- The customer’s agreement to your initiating a payment or a series of payments on their behalf for specified transactions.
- The anticipated timing and frequency of payments (for example, if the charges are for scheduled installments, subscription payments, or unscheduled top-ups).
- How you determine the payment amount.
- Your cancellation policy, if the payment method is for a subscription service.

Make sure you keep a record of your customer’s written agreement to these terms.

> If you need to use manual server-side confirmation or your integration requires presenting payment methods separately, see our [alternative guide](https://docs.stripe.com/payments/save-and-reuse-cards-only.md).

## Set up Stripe [Server-side]

First, [create a Stripe account](https://dashboard.stripe.com/register) or [sign in](https://dashboard.stripe.com/login).

Use our official libraries to access the Stripe API from your application:

#### Ruby

```bash
# Available as a gem
sudo gem install stripe
```

```ruby
# If you use bundler, you can add this line to your Gemfile
gem 'stripe'
```

#### Python

```bash
# Install through pip
pip3 install --upgrade stripe
```

```bash
# Or find the Stripe package on http://pypi.python.org/pypi/stripe/
```

```python
# Find the version you want to pin:
# https://github.com/stripe/stripe-python/blob/master/CHANGELOG.md
# Specify that version in your requirements.txt file
stripe>=5.0.0
```

#### PHP

```bash
# Install the PHP library with Composer
composer require stripe/stripe-php
```

```bash
# Or download the source directly: https://github.com/stripe/stripe-php/releases
```

#### Java

```java
/*
  For Gradle, add the following dependency to your build.gradle and replace with
  the version number you want to use from:
  - https://mvnrepository.com/artifact/com.stripe/stripe-java or
  - https://github.com/stripe/stripe-java/releases/latest
*/
implementation "com.stripe:stripe-java:30.0.0"
```

```xml
<!--
  For Maven, add the following dependency to your POM and replace with the
  version number you want to use from:
  - https://mvnrepository.com/artifact/com.stripe/stripe-java or
  - https://github.com/stripe/stripe-java/releases/latest
-->
<dependency>
  <groupId>com.stripe</groupId>
  <artifactId>stripe-java</artifactId>
  <version>30.0.0</version>
</dependency>
```

```bash
# For other environments, manually install the following JARs:
# - The Stripe JAR from https://github.com/stripe/stripe-java/releases/latest
# - Google Gson from https://github.com/google/gson
```

#### Node.js

```bash
# Install with npm
npm install stripe --save
```

#### Go

```bash
# Make sure your project is using Go Modules
go mod init
# Install stripe-go
go get -u github.com/stripe/stripe-go/v83
```

```go
// Then import the package
import (
  "github.com/stripe/stripe-go/v83"
)
```

#### .NET

```bash
# Install with dotnet
dotnet add package Stripe.net
dotnet restore
```

```bash
# Or install with NuGet
Install-Package Stripe.net
```

## Create a Customer [Server-side]

To set up a payment method for future payments, you must attach it to a *Customer* (Customer objects represent customers of your business. They let you reuse payment methods and give you the ability to track multiple payments). Create a `Customer` object when your customer creates an account with your business. `Customer` objects allow for reusing payment methods and tracking across multiple payments.

```curl
curl -X POST https://api.stripe.com/v1/customers \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe customers create
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.create()
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.create()
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->create([]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerCreateParams params = CustomerCreateParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Customer customer = client.v1().customers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.create();
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerCreateParams{}
result, err := sc.V1Customers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerCreateOptions();
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Create(options);
```

## Use setup mode [Server-side]

Create a Checkout Session with [mode=setup](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-mode).

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=setup \
  -d ui_mode=custom \
  -d currency=usd
```

```cli
stripe checkout sessions create  \
  --mode=setup \
  --ui-mode=custom \
  --currency=usd
```

```ruby
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'setup',
  ui_mode: 'custom',
  currency: 'usd',
})
```

```python
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "setup",
  "ui_mode": "custom",
  "currency": "usd",
})
```

```php
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'setup',
  'ui_mode' => 'custom',
  'currency' => 'usd',
]);
```

```java
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.SETUP)
    .setUiMode(SessionCreateParams.UiMode.CUSTOM)
    .setCurrency("usd")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'setup',
  ui_mode: 'custom',
  currency: 'usd',
});
```

```go
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModeSetup),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeCustom),
  Currency: stripe.String(stripe.CurrencyUSD),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "setup",
    UiMode = "custom",
    Currency = "usd",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Attach the payment method to a Customer [Server-side]

If you didn’t create the Checkout Session with an existing customer, use the ID of the *PaymentMethod* (PaymentMethods represent your customer's payment instruments, used with the Payment Intents or Setup Intents APIs) to [attach](https://docs.stripe.com/api/payment_methods/attach.md) the payment method to a customer.

Otherwise, the payment method automatically attaches to the customer you provided when creating the Checkout Session.

```curl
curl https://api.stripe.com/v1/payment_methods/{{PAYMENTMETHOD_ID}}/attach \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}"
```

```cli
stripe payment_methods attach {{PAYMENTMETHOD_ID}} \
  --customer="{{CUSTOMER_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_method = client.v1.payment_methods.attach(
  '{{PAYMENTMETHOD_ID}}',
  {customer: '{{CUSTOMER_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_method = client.v1.payment_methods.attach(
  "{{PAYMENTMETHOD_ID}}",
  {"customer": "{{CUSTOMER_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentMethod = $stripe->paymentMethods->attach(
  '{{PAYMENTMETHOD_ID}}',
  ['customer' => '{{CUSTOMER_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentMethodAttachParams params =
  PaymentMethodAttachParams.builder().setCustomer("{{CUSTOMER_ID}}").build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentMethod paymentMethod =
  client.v1().paymentMethods().attach("{{PAYMENTMETHOD_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentMethod = await stripe.paymentMethods.attach(
  '{{PAYMENTMETHOD_ID}}',
  {
    customer: '{{CUSTOMER_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentMethodAttachParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
}
result, err := sc.V1PaymentMethods.Attach(
  context.TODO(), "{{PAYMENTMETHOD_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentMethodAttachOptions { Customer = "{{CUSTOMER_ID}}" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentMethods;
PaymentMethod paymentMethod = service.Attach("{{PAYMENTMETHOD_ID}}", options);
```

## Retrieve the payment method [Server-side]

After a customer successfully completes their Checkout Session, handle the [checkout.session.completed](https://docs.stripe.com/api/events/types.md#event_types-checkout.session.completed) webhook. Retrieve the Session object in the webhook, and then do the following:

- Get the value of the [setup_intent](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-setup_intent) key, which is the SetupIntent ID created during the Checkout Session.
- Use the SetupIntent ID to [retrieve](https://docs.stripe.com/api/setup_intents/retrieve.md) the SetupIntent object. The returned object contains a [payment_method](https://docs.stripe.com/api/setup_intents/object.md#setup_intent_object-payment_method) ID that you can attach to a customer in the next step.

Learn more about [setting up webhooks](https://docs.stripe.com/webhooks.md).

## Charge the payment method later [Server-side]

After you attach the PaymentMethod to a customer, you can make an *off-session* (A payment is described as off-session if it occurs without the direct involvement of the customer, using previously-collected payment information) payment using a [PaymentIntent](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-payment_method):

- Set [customer](https://docs.stripe.com/api.md#create_payment_intent-customer) to the customer ID and [payment_method](https://docs.stripe.com/api.md#create_payment_intent-payment_method) to payment method ID.
- Set [off_session](https://docs.stripe.com/api/payment_intents/confirm.md#confirm_payment_intent-off_session) to `true` to indicate that the customer isn’t in your checkout flow during a payment attempt and can’t fulfill an authentication request made by a partner, such as a card issuer, bank, or other payment institution. If, during your checkout flow, a partner requests authentication, Stripe requests exemptions using customer information from a previous *on-session* (A payment is described as on-session if it occurs while the customer is actively in your checkout flow and able to authenticate the payment method) transaction. If the conditions for exemption aren’t met, the PaymentIntent might result in an error.
- Set the value of the PaymentIntent’s [confirm](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-confirm) property to `true`, which causes confirmation to occur immediately when you create the PaymentIntent.

```curl
curl https://api.stripe.com/v1/payment_intents \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1099 \
  -d currency=usd \
  -d customer="{{CUSTOMER_ID}}" \
  -d payment_method="{{PAYMENTMETHOD_ID}}" \
  -d off_session=true \
  -d confirm=true
```

```cli
stripe payment_intents create  \
  --amount=1099 \
  --currency=usd \
  --customer="{{CUSTOMER_ID}}" \
  --payment-method="{{PAYMENTMETHOD_ID}}" \
  --off-session=true \
  --confirm=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_intent = client.v1.payment_intents.create({
  amount: 1099,
  currency: 'usd',
  customer: '{{CUSTOMER_ID}}',
  payment_method: '{{PAYMENTMETHOD_ID}}',
  off_session: true,
  confirm: true,
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_intent = client.v1.payment_intents.create({
  "amount": 1099,
  "currency": "usd",
  "customer": "{{CUSTOMER_ID}}",
  "payment_method": "{{PAYMENTMETHOD_ID}}",
  "off_session": True,
  "confirm": True,
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentIntent = $stripe->paymentIntents->create([
  'amount' => 1099,
  'currency' => 'usd',
  'customer' => '{{CUSTOMER_ID}}',
  'payment_method' => '{{PAYMENTMETHOD_ID}}',
  'off_session' => true,
  'confirm' => true,
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentIntentCreateParams params =
  PaymentIntentCreateParams.builder()
    .setAmount(1099L)
    .setCurrency("usd")
    .setCustomer("{{CUSTOMER_ID}}")
    .setPaymentMethod("{{PAYMENTMETHOD_ID}}")
    .setOffSession(true)
    .setConfirm(true)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentIntent paymentIntent = client.v1().paymentIntents().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentIntent = await stripe.paymentIntents.create({
  amount: 1099,
  currency: 'usd',
  customer: '{{CUSTOMER_ID}}',
  payment_method: '{{PAYMENTMETHOD_ID}}',
  off_session: true,
  confirm: true,
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentIntentCreateParams{
  Amount: stripe.Int64(1099),
  Currency: stripe.String(stripe.CurrencyUSD),
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  PaymentMethod: stripe.String("{{PAYMENTMETHOD_ID}}"),
  OffSession: stripe.Bool(true),
  Confirm: stripe.Bool(true),
}
result, err := sc.V1PaymentIntents.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentIntentCreateOptions
{
    Amount = 1099,
    Currency = "usd",
    Customer = "{{CUSTOMER_ID}}",
    PaymentMethod = "{{PAYMENTMETHOD_ID}}",
    OffSession = true,
    Confirm = true,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentIntents;
PaymentIntent paymentIntent = service.Create(options);
```

If a payment attempt fails, the request also fails with a 402 HTTP status code, and the PaymentIntent status is *requires\_payment\_method* (This status appears as "requires_source" in API versions before 2019-02-11). Notify your customer to return to your application (for example, by sending an email or in-app notification) and direct your customer to a new Checkout Session to select another payment method.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=1099 \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  -d ui_mode=custom \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=1099 \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --ui-mode=custom \
  --return-url="https://example.com/return"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  customer: '{{CUSTOMER_ID}}',
  line_items: [
    {
      price_data: {
        currency: 'usd',
        product_data: {name: 'T-shirt'},
        unit_amount: 1099,
      },
      quantity: 1,
    },
  ],
  mode: 'payment',
  ui_mode: 'custom',
  return_url: 'https://example.com/return',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "customer": "{{CUSTOMER_ID}}",
  "line_items": [
    {
      "price_data": {
        "currency": "usd",
        "product_data": {"name": "T-shirt"},
        "unit_amount": 1099,
      },
      "quantity": 1,
    },
  ],
  "mode": "payment",
  "ui_mode": "custom",
  "return_url": "https://example.com/return",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'line_items' => [
    [
      'price_data' => [
        'currency' => 'usd',
        'product_data' => ['name' => 'T-shirt'],
        'unit_amount' => 1099,
      ],
      'quantity' => 1,
    ],
  ],
  'mode' => 'payment',
  'ui_mode' => 'custom',
  'return_url' => 'https://example.com/return',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setCurrency("usd")
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setUnitAmount(1099L)
            .build()
        )
        .setQuantity(1L)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.CUSTOM)
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
  customer: '{{CUSTOMER_ID}}',
  line_items: [
    {
      price_data: {
        currency: 'usd',
        product_data: {
          name: 'T-shirt',
        },
        unit_amount: 1099,
      },
      quantity: 1,
    },
  ],
  mode: 'payment',
  ui_mode: 'custom',
  return_url: 'https://example.com/return',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        Currency: stripe.String(stripe.CurrencyUSD),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        UnitAmount: stripe.Int64(1099),
      },
      Quantity: stripe.Int64(1),
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeCustom),
  ReturnURL: stripe.String("https://example.com/return"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                Currency = "usd",
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                UnitAmount = 1099,
            },
            Quantity = 1,
        },
    },
    Mode = "payment",
    UiMode = "custom",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```


# Payment Intents API

> This is a Payment Intents API for when payment-ui is elements. View the full page at https://docs.stripe.com/payments/save-and-reuse?payment-ui=elements.

The [Setup Intents API](https://docs.stripe.com/api/setup_intents.md) lets you save a customer’s payment details without an initial payment. This is helpful if you want to onboard customers now, set them up for payments, and charge them in the future—when they’re offline.

Use this integration to set up recurring payments or to create one-time payments with a final amount determined later, often after the customer receives your service.

> #### Card-present transactions
> 
> Card-present transactions, such as collecting card details through Stripe Terminal, use a different process for saving the payment method. For details, see [the Terminal documentation](https://docs.stripe.com/terminal/features/saving-payment-details/save-directly.md).

## Compliance

You’re responsible for your compliance with all applicable laws, regulations, and network rules when saving a customer’s payment details. These requirements generally apply if you want to save your customer’s payment method for future use, such as displaying a customer’s payment method to them in the checkout flow for a future purchase or charging them when they’re not actively using your website or app. Add terms to your website or app that state how you plan to save payment method details and allow customers to opt in.

When you save a payment method, you can only use it for the specific usage you have included in your terms. To charge a payment method when a customer is offline and save it as an option for future purchases, make sure that you explicitly collect consent from the customer for this specific use. For example, include a “Save my payment method for future use” checkbox to collect consent.

To charge them when they’re offline, make sure your terms include the following:

- The customer’s agreement to your initiating a payment or a series of payments on their behalf for specified transactions.
- The anticipated timing and frequency of payments (for example, if the charges are for scheduled installments, subscription payments, or unscheduled top-ups).
- How you determine the payment amount.
- Your cancellation policy, if the payment method is for a subscription service.

Make sure you keep a record of your customer’s written agreement to these terms.

> If you need to use manual server-side confirmation or your integration requires presenting payment methods separately, see our [alternative guide](https://docs.stripe.com/payments/save-and-reuse-cards-only.md).

## Set up Stripe [Server-side]

First, [create a Stripe account](https://dashboard.stripe.com/register) or [sign in](https://dashboard.stripe.com/login).

Use our official libraries to access the Stripe API from your application:

#### Ruby

```bash
# Available as a gem
sudo gem install stripe
```

```ruby
# If you use bundler, you can add this line to your Gemfile
gem 'stripe'
```

#### Python

```bash
# Install through pip
pip3 install --upgrade stripe
```

```bash
# Or find the Stripe package on http://pypi.python.org/pypi/stripe/
```

```python
# Find the version you want to pin:
# https://github.com/stripe/stripe-python/blob/master/CHANGELOG.md
# Specify that version in your requirements.txt file
stripe>=5.0.0
```

#### PHP

```bash
# Install the PHP library with Composer
composer require stripe/stripe-php
```

```bash
# Or download the source directly: https://github.com/stripe/stripe-php/releases
```

#### Java

```java
/*
  For Gradle, add the following dependency to your build.gradle and replace with
  the version number you want to use from:
  - https://mvnrepository.com/artifact/com.stripe/stripe-java or
  - https://github.com/stripe/stripe-java/releases/latest
*/
implementation "com.stripe:stripe-java:30.0.0"
```

```xml
<!--
  For Maven, add the following dependency to your POM and replace with the
  version number you want to use from:
  - https://mvnrepository.com/artifact/com.stripe/stripe-java or
  - https://github.com/stripe/stripe-java/releases/latest
-->
<dependency>
  <groupId>com.stripe</groupId>
  <artifactId>stripe-java</artifactId>
  <version>30.0.0</version>
</dependency>
```

```bash
# For other environments, manually install the following JARs:
# - The Stripe JAR from https://github.com/stripe/stripe-java/releases/latest
# - Google Gson from https://github.com/google/gson
```

#### Node.js

```bash
# Install with npm
npm install stripe --save
```

#### Go

```bash
# Make sure your project is using Go Modules
go mod init
# Install stripe-go
go get -u github.com/stripe/stripe-go/v83
```

```go
// Then import the package
import (
  "github.com/stripe/stripe-go/v83"
)
```

#### .NET

```bash
# Install with dotnet
dotnet add package Stripe.net
dotnet restore
```

```bash
# Or install with NuGet
Install-Package Stripe.net
```

## Enable payment methods

View your [payment methods settings](https://dashboard.stripe.com/settings/payment_methods) and enable the payment methods you want to support. You need at least one payment method enabled to create a *SetupIntent* (The Setup Intents API lets you build dynamic flows for collecting payment method details for future payments. It tracks the lifecycle of a payment setup flow and can trigger additional authentication steps if required by law or by the payment method).

By default, Stripe enables cards and other prevalent payment methods that can help you reach more customers, but we recommend turning on additional payment methods that are relevant for your business and customers. See [Payment method support](https://docs.stripe.com/payments/payment-methods/payment-method-support.md) for product and payment method support, and our [pricing page](https://stripe.com/pricing/local-payment-methods) for fees.

## Create a Customer [Server-side]

To set up a payment method for future payments, you must attach it to a *Customer* (Customer objects represent customers of your business. They let you reuse payment methods and give you the ability to track multiple payments). Create a `Customer` object when your customer creates an account with your business. `Customer` objects allow for reusing payment methods and tracking across multiple payments.

```curl
curl -X POST https://api.stripe.com/v1/customers \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe customers create
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.create()
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.create()
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->create([]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerCreateParams params = CustomerCreateParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Customer customer = client.v1().customers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.create();
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerCreateParams{}
result, err := sc.V1Customers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerCreateOptions();
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Create(options);
```

## Create a SetupIntent [Server-side]

> If you want to render the Payment Element without first creating a SetupIntent, see [Collect payment details before creating an Intent](https://docs.stripe.com/payments/accept-a-payment-deferred.md?type=setup).

A [SetupIntent](https://docs.stripe.com/api/setup_intents.md) is an object that represents your intent to set up a customer’s payment method for future payments. The payment methods shown to customers during the checkout process are also included on the `SetupIntent`. You can let Stripe automatically pull payment methods from your Dashboard settings or you can list them manually.

Unless your integration requires a code-based option for offering payment methods, Stripe recommends the automated option. This is because Stripe evaluates the currency, payment method restrictions, and other parameters to determine the list of supported payment methods. Payment methods that increase conversion and that are most relevant to the currency and customer’s location are prioritized. Lower priority payment methods are hidden beneath an overflow menu.

#### Manage payment methods from the Dashboard

Some payment methods can’t be saved for future payments, and customers don’t see them as options when setting up future payments. For more details about managing payment methods, see [Payment method integration options](https://docs.stripe.com/payments/payment-methods/integration-options.md).

You can optionally create a SetupIntent with `automatic_payment_methods` enabled, and the SetupIntent is created using the payment methods you configured in the Dashboard. Specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default in the latest version of the API.

You can manage payment methods from the [Dashboard](https://dashboard.stripe.com/settings/payment_methods). Stripe handles the return of eligible payment methods based on factors such as the transaction’s amount, currency, and payment flow.

```curl
curl https://api.stripe.com/v1/setup_intents \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "automatic_payment_methods[enabled]"=true
```

```cli
stripe setup_intents create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "automatic_payment_methods[enabled]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

setup_intent = client.v1.setup_intents.create({
  customer: '{{CUSTOMER_ID}}',
  automatic_payment_methods: {enabled: true},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
setup_intent = client.v1.setup_intents.create({
  "customer": "{{CUSTOMER_ID}}",
  "automatic_payment_methods": {"enabled": True},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$setupIntent = $stripe->setupIntents->create([
  'customer' => '{{CUSTOMER_ID}}',
  'automatic_payment_methods' => ['enabled' => true],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SetupIntentCreateParams params =
  SetupIntentCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setAutomaticPaymentMethods(
      SetupIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
SetupIntent setupIntent = client.v1().setupIntents().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const setupIntent = await stripe.setupIntents.create({
  customer: '{{CUSTOMER_ID}}',
  automatic_payment_methods: {
    enabled: true,
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SetupIntentCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  AutomaticPaymentMethods: &stripe.SetupIntentCreateAutomaticPaymentMethodsParams{
    Enabled: stripe.Bool(true),
  },
}
result, err := sc.V1SetupIntents.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SetupIntentCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    AutomaticPaymentMethods = new SetupIntentAutomaticPaymentMethodsOptions
    {
        Enabled = true,
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.SetupIntents;
SetupIntent setupIntent = service.Create(options);
```

#### Manually list payment methods

Create a SetupIntent on your server with a list of [payment methods](https://docs.stripe.com/payments/payment-methods/integration-options.md) you want to support.

```curl
curl https://api.stripe.com/v1/setup_intents \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer={{CUSTOMER_ID}} \
  -d "payment_method_types[]"=bancontact \
  -d "payment_method_types[]"=card \
  -d "payment_method_types[]"=ideal
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

setup_intent = client.v1.setup_intents.create({
  customer: '{{CUSTOMER_ID}}',
  payment_method_types: ['bancontact', 'card', 'ideal'],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
setup_intent = client.v1.setup_intents.create({
  "customer": "{{CUSTOMER_ID}}",
  "payment_method_types": ["bancontact", "card", "ideal"],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$setupIntent = $stripe->setupIntents->create([
  'customer' => '{{CUSTOMER_ID}}',
  'payment_method_types' => ['bancontact', 'card', 'ideal'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SetupIntentCreateParams params =
  SetupIntentCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addPaymentMethodType("bancontact")
    .addPaymentMethodType("card")
    .addPaymentMethodType("ideal")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
SetupIntent setupIntent = client.v1().setupIntents().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const setupIntent = await stripe.setupIntents.create({
  customer: '{{CUSTOMER_ID}}',
  payment_method_types: ['bancontact', 'card', 'ideal'],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SetupIntentCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  PaymentMethodTypes: []*string{
    stripe.String("bancontact"),
    stripe.String("card"),
    stripe.String("ideal"),
  },
}
result, err := sc.V1SetupIntents.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SetupIntentCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    PaymentMethodTypes = new List<string> { "bancontact", "card", "ideal" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.SetupIntents;
SetupIntent setupIntent = service.Create(options);
```

```cli
stripe setup_intents create \
  --customer={{CUSTOMER_ID}}
  -d "payment_method_types[]"="bancontact" \
  -d "payment_method_types[]"="card" \
  -d "payment_method_types[]"="ideal"
```

### Retrieve the client secret

The SetupIntent includes a *client secret* (The client secret is a unique key returned from Stripe as part of a PaymentIntent. This key lets the client access important fields from the PaymentIntent (status, amount, currency) while hiding sensitive ones (metadata, customer)) that the client side uses to securely complete the payment process. You can use different approaches to pass the client secret to the client side.

#### Single-page application

Retrieve the client secret from an endpoint on your server, using the browser’s `fetch` function. This approach is best if your client side is a single-page application, particularly one built with a modern frontend framework like React. Create the server endpoint that serves the client secret:

#### Ruby

```ruby
get '/secret' do
  intent = # ... Create or retrieve the SetupIntent
  {client_secret: intent.client_secret}.to_json
end
```

#### Python

```python
from flask import Flask, jsonify
app = Flask(__name__)

@app.route('/secret')
def secret():
  intent = # ... Create or retrieve the SetupIntent
  return jsonify(client_secret=intent.client_secret)
```

#### PHP

```php
<?php
    $intent = # ... Create or retrieve the SetupIntent
    echo json_encode(array('client_secret' => $intent->client_secret));
?>
```

#### Java

```java
import java.util.HashMap;
import java.util.Map;

import com.stripe.model.SetupIntent;

import com.google.gson.Gson;

import static spark.Spark.get;

public class StripeJavaQuickStart {
  public static void main(String[] args) {
    Gson gson = new Gson();

    get("/secret", (request, response) -> {
      SetupIntent intent = // ... Fetch or create the SetupIntent

      Map<String, String> map = new HashMap();
      map.put("client_secret", intent.getClientSecret());

      return map;
    }, gson::toJson);
  }
}
```

#### Node.js

```javascript
const express = require('express');
const app = express();

app.get('/secret', async (req, res) => {
  const intent = // ... Fetch or create the SetupIntent
  res.json({client_secret: intent.client_secret});
});

app.listen(3000, () => {
  console.log('Running on port 3000');
});
```

#### Go

```go
package main

import (
  "encoding/json"
  "net/http"

  stripe "github.com/stripe/stripe-go/v76.0.0"
)

type CheckoutData struct {
  ClientSecret string `json:"client_secret"`
}

func main() {
  http.HandleFunc("/secret", func(w http.ResponseWriter, r *http.Request) {
    intent := // ... Fetch or create the SetupIntent
    data := CheckoutData{
      ClientSecret: intent.ClientSecret,
    }
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(http.StatusOK)
    json.NewEncoder(w).Encode(data)
  })

  http.ListenAndServe(":3000", nil)
}
```

#### .NET

```csharp
using System;
using Microsoft.AspNetCore.Mvc;
using Stripe;

namespace StripeExampleApi.Controllers
{
  [Route("secret")]
  [ApiController]
  public class CheckoutApiController : Controller
  {
    [HttpGet]
    public ActionResult Get()
    {
      var intent = // ... Fetch or create the SetupIntent
      return Json(new {client_secret = intent.ClientSecret});
    }
  }
}
```

And then fetch the client secret with JavaScript on the client side:

```javascript
(async () => {
  const response = await fetch('/secret');
  const {client_secret: clientSecret} = await response.json();
  // Render the form using the clientSecret
})();
```

#### Server-side rendering

Pass the client secret to the client from your server. This approach works best if your application generates static content on the server before sending it to the browser.

Add the [client_secret](https://docs.stripe.com/api/setup_intents/object.md#setup_intent_object-client_secret) in your checkout form. In your server-side code, retrieve the client secret from the SetupIntent:

#### Ruby

```erb
<form id="payment-form" data-secret="<%= @intent.client_secret %>">
  <div id="payment-element">
    <!-- placeholder for Elements -->
  </div>
  <button id="submit">Submit</button>
</form>
```

```ruby
get '/checkout' do
  @intent = # ... Fetch or create the SetupIntent
  erb :checkout
end
```

#### Python

```html
<form id="payment-form" data-secret="{{ client_secret }}">
  <div id="payment-element">
    <!-- placeholder for Elements -->
  </div>
  <button id="submit">Submit</button>
</form>
```

```python
@app.route('/checkout')
def checkout():
  intent = # ... Fetch or create the SetupIntent
  return render_template('checkout.html', client_secret=intent.client_secret)
```

#### PHP

```php
<?php
  $intent = # ... Fetch or create the SetupIntent;
?>
...
<form id="payment-form" data-secret="<?= $intent->client_secret ?>">
  <div id="payment-element">
    <!-- placeholder for Elements -->
  </div>
  <button id="submit">Submit</button>
</form>
...
```

#### Java

```html
<form id="payment-form" data-secret="{{ client_secret }}">
  <div id="payment-element">
    <!-- placeholder for Elements -->
  </div>
  <button id="submit">Submit</button>
</form>
```

```java
import java.util.HashMap;
import java.util.Map;

import com.stripe.model.SetupIntent;

import spark.ModelAndView;

import static spark.Spark.get;

public class StripeJavaQuickStart {
  public static void main(String[] args) {
    get("/checkout", (request, response) -> {
      SetupIntent intent = // ... Fetch or create the SetupIntent

      Map map = new HashMap();
      map.put("client_secret", intent.getClientSecret());

      return new ModelAndView(map, "checkout.hbs");
    }, new HandlebarsTemplateEngine());
  }
}
```

#### Node.js

```html
<form id="payment-form" data-secret="{{ client_secret }}">
  <div id="payment-element">
    <!-- Elements will create form elements here -->
  </div>

  <button id="submit">Submit</button>
</form>
```

```javascript
const express = require('express');
const expressHandlebars = require('express-handlebars');
const app = express();

app.engine('.hbs', expressHandlebars({ extname: '.hbs' }));
app.set('view engine', '.hbs');
app.set('views', './views');

app.get('/checkout', async (req, res) => {
  const intent = // ... Fetch or create the SetupIntent
  res.render('checkout', { client_secret: intent.client_secret });
});

app.listen(3000, () => {
  console.log('Running on port 3000');
});
```

#### Go

```html
<form id="payment-form" data-secret="{{ .ClientSecret }}">
  <div id="payment-element">
    <!-- placeholder for Elements -->
  </div>
  <button id="submit">Submit</button>
</form>
```

```go
package main

import (
  "html/template"
  "net/http"

  stripe "github.com/stripe/stripe-go/v76.0.0"
)

type CheckoutData struct {
  ClientSecret string
}

func main() {
  checkoutTmpl := template.Must(template.ParseFiles("views/checkout.html"))

  http.HandleFunc("/checkout", func(w http.ResponseWriter, r *http.Request) {
    intent := // ... Fetch or create the SetupIntent
    data := CheckoutData{
      ClientSecret: intent.ClientSecret,
    }
    checkoutTmpl.Execute(w, data)
  })

  http.ListenAndServe(":3000", nil)
}
```

#### .NET

```html
<form id="payment-form" data-secret="@ViewData["ClientSecret"]">
  <div id="payment-element">
    <!-- placeholder for Elements -->
  </div>
  <button id="submit">Submit</button>
</form>
```

```csharp
using System;
using Microsoft.AspNetCore.Mvc;
using Stripe;

namespace StripeExampleApi.Controllers
{
  [Route("/[controller]")]
  public class CheckoutApiController : Controller
  {
    public IActionResult Index()
    {
      var intent = // ... Fetch or create the SetupIntent
      ViewData["ClientSecret"] = intent.ClientSecret;
      return View();
    }
  }
}
```

> #### Using Radar
> 
> When saving a customer’s payment method without an initial payment, [Radar](https://docs.stripe.com/radar.md) doesn’t act on the SetupIntent by default. If you want to activate this as the default, go to the [Radar settings](https://dashboard.stripe.com/settings/radar) and enable **Use Radar on payment methods saved for future use**.

## Collect payment details [Client-side]

You’re ready to collect payment details on the client with the [Payment Element](https://docs.stripe.com/payments/payment-element.md). The Payment Element is a prebuilt UI component that simplifies collecting payment details for a variety of payment methods.

The Payment Element contains an iframe that securely sends payment information to Stripe over an HTTPS connection. The checkout page address must start with `https://` rather than `http://` for your integration to work. You can test your integration without doing so, but remember to [enable HTTPS](https://docs.stripe.com/security/guide.md#tls) when you’re ready to accept live payments.

#### HTML + JS

### Set up Stripe.js

The Payment Element is automatically available as a feature of Stripe.js. Include the Stripe.js script on your checkout page by adding it to the `head` of your HTML file. Always load Stripe.js directly from js.stripe.com to remain PCI compliant. Don’t include the script in a bundle or host a copy of it yourself.

```html
<head>
  <title>Checkout</title>
  <script src="https://js.stripe.com/clover/stripe.js"></script>
</head>
```

Create an instance of Stripe with the following JavaScript on your checkout page:

```javascript
// Set your publishable key: remember to change this to your live publishable key in production
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = Stripe('<<YOUR_PUBLISHABLE_KEY>>');
```

### Add the Payment Element to your payment setup page

The Payment Element needs a place to live on your payment setup page. Create an empty DOM node (container) with a unique ID in your payment form:

```html
<form id="payment-form">
  <div id="payment-element">
    <!-- Elements will create form elements here -->
  </div>
  <button id="submit">Submit</button>
  <div id="error-message">
    <!-- Display error message to your customers here -->
  </div>
</form>
```

When the previous form loads, create an instance of the Payment Element and mount it to the container DOM node. Pass the [client secret](https://docs.stripe.com/api/setup_intents/object.md#setup_intent_object-client_secret) from the previous step into `options` when you create the [Elements](https://docs.stripe.com/js/elements_object/create) instance:

```javascript
const options = {
  clientSecret: '{{CLIENT_SECRET}}',
  // Fully customizable with appearance API.
  appearance: {/*...*/},
};

// Set up Stripe.js and Elements using the SetupIntent's client secretconst elements = stripe.elements(options);

// Create and mount the Payment Element
const paymentElementOptions = { layout: 'accordion'};
const paymentElement = elements.create('payment', paymentElementOptions);
paymentElement.mount('#payment-element');

```

Stripe Elements is a collection of drop-in UI components. To further customize your form or collect different customer information, browse the [Elements docs](https://docs.stripe.com/payments/elements.md).

The Payment Element renders a dynamic form that allows your customer to pick a payment method. For each payment method, the form automatically asks the customer to fill in all necessary payment details.

### Customize appearance

Customize the Payment Element to match the design of your site by passing the [appearance object](https://docs.stripe.com/js/elements_object/create#stripe_elements-options-appearance) into `options` when creating the `elements` instance.

### Request Apple Pay merchant token

If you accept Apple Pay payments, we recommend configuring the `applePay` [option](https://docs.stripe.com/js/elements_object/create_payment_element#payment_element_create-options-applePay) to modify what’s displayed in the Apple Pay interface. By doing this, Apple Pay’s [merchant tokens](https://docs.stripe.com/apple-pay/merchant-tokens.md?pay-element=web-pe) are used. The following example shows a configuration for a payment that begins on January 5, 2030. This information will be reflected in the Apple Pay interface.

```javascript
const paymentElement = elements.create('payment', {
  applePay: {
    deferredPaymentRequest: {
      paymentDescription: 'My deferred payment',
      managementURL: 'https://example.com/billing',
      deferredBilling: {
        amount: 2500,
        label: 'Deferred Fee',
        deferredPaymentDate: new Date('2030-01-05')
      },
    }
  },
  // Other options
});
```

#### React

### Set up Stripe.js

Install [React Stripe.js](https://www.npmjs.com/package/@stripe/react-stripe-js) and the [Stripe.js loader](https://www.npmjs.com/package/@stripe/stripe-js) from the npm public registry:

```bash
npm install --save @stripe/react-stripe-js @stripe/stripe-js
```

### Add and configure the Elements provider to your payment setup page

To use the Payment Element component, wrap your payment setup page component in an [Elements provider](https://docs.stripe.com/sdks/stripejs-react.md#elements-provider). Call `loadStripe` with your publishable key, and pass the returned `Promise` to the `Elements` provider. Also pass the [client secret](https://docs.stripe.com/api/setup_intents/object.md#setup_intent_object-client_secret) from the previous step as `options` to the `Elements` provider.

```jsx
import React from 'react';
import ReactDOM from 'react-dom';
import {Elements} from '@stripe/react-stripe-js';
import {loadStripe} from '@stripe/stripe-js';
import SetupForm from './SetupForm';

// Make sure to call `loadStripe` outside of a component's render to avoid
// recreating the `Stripe` object on every render.
const stripePromise = loadStripe('<<YOUR_PUBLISHABLE_KEY>>');

function App() {
  const options = {
    // passing the SetupIntent's client secret
    clientSecret: '{{CLIENT_SECRET}}',
    // Fully customizable with appearance API.
    appearance: {/*...*/},
  };

  return (
    <Elements stripe={stripePromise} options={options}>
      <SetupForm />
    </Elements>
  );
};

ReactDOM.render(<App />, document.getElementById('root'));
```

### Add the Payment Element component

Use the `PaymentElement` component to build your form:

```jsx
import React from 'react';
import {PaymentElement} from '@stripe/react-stripe-js';

const SetupForm = () => {
  return (
    <form><PaymentElement />
      <button>Submit</button>
    </form>
  );
};

export default SetupForm;
```

Stripe Elements is a collection of drop-in UI components. To further customize your form or collect different customer information, browse the [Elements docs](https://docs.stripe.com/payments/elements.md).

The Payment Element renders a dynamic form that allows your customer to pick a payment method. For each payment method, the form automatically asks the customer to fill in all necessary payment details.

### Customize appearance

Customize the Payment Element to match the design of your site by passing the [appearance object](https://docs.stripe.com/js/elements_object/create#stripe_elements-options-appearance) into `options` when creating the `Elements` provider.

### Request Apple Pay merchant token

If you accept Apple Pay payments, we recommend configuring the `applePay` [option](https://docs.stripe.com/js/elements_object/create_payment_element#payment_element_create-options-applePay) to modify what’s displayed in the Apple Pay interface. By doing this, Apple Pay’s [merchant tokens](https://docs.stripe.com/apple-pay/merchant-tokens.md?pay-element=web-pe) are used. The following example shows a configuration for a payment that begins on January 5, 2030. This information will be reflected in the Apple Pay interface.

```jsx
import React from 'react';
import {PaymentElement} from '@stripe/react-stripe-js';

const SetupForm = () => {
  const options = {
    applePay: {
      deferredPaymentRequest: {
        paymentDescription: 'My deferred payment',
        managementURL: 'https://example.com/billing',
        deferredBilling: {
          amount: 2500,
          label: 'Deferred Fee',
          deferredPaymentDate: new Date('2030-01-05')
        },
      }
    },
    // Other options
  };

  return (
    <form><PaymentElement options={options} />
      <button>Submit</button>
    </form>
  );
};

export default SetupForm;
```

### Configure currency

When using SetupIntents with [automatic_payment_methods](https://docs.stripe.com/api/setup_intents/create.md#create_setup_intent-automatic_payment_methods), you can specify the currency when you [create the Payment Element](https://docs.stripe.com/js/elements_object/create#stripe_elements-options-currency). The Payment Element renders the enabled payment methods that support the provided currency. For more details, see [the Payment Element documentation](https://docs.stripe.com/payments/payment-methods/integration-options.md).

### Collect addresses

By default, the Payment Element only collects the necessary billing address details. Some behavior, such as [calculating tax](https://docs.stripe.com/api/tax/calculations/create.md) or entering shipping details, requires your customer’s full address. You can:

- Use the [Address Element](https://docs.stripe.com/elements/address-element.md) to take advantage of autocomplete and localization features to collect your customer’s full address. This helps ensure the most accurate tax calculation.
- Collect address details using your own custom form.

## Optional: Link in your checkout page [Client-side]

Let your customer check out faster by using [Link](https://docs.stripe.com/payments/link.md) in the [Payment Element](https://docs.stripe.com/payments/payment-element.md). You can autofill information for any logged-in customer already using Link, regardless of whether they initially saved their information in Link with another business. The default Payment Element integration includes a Link prompt in the card form. To manage Link in the Payment Element, go to your [payment method settings](https://dashboard.stripe.com/settings/payment_methods).
![Authenticate or enroll with Link directly in the Payment Element during checkout](https://b.stripecdn.com/docs-statics-srv/assets/link-in-pe.2efb5138a4708b781b8a913ebddd9aba.png)

Collect a customer email address for Link authentication or enrollment

### Integration options 

There are two ways you can integrate Link with the Payment Element. Of these, Stripe recommends passing a customer email address to the Payment Element if available. Remember to consider how your checkout flow works when deciding between these options:

| Integration option                                                 | Checkout flow                                                                                                                                                                        | Description                                                                                                                                                                                                                                                |
| ------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Pass a customer email address to the Payment Element (Recommended) | - Your customer enters their email address before landing on the checkout page (in a previous account creation step, for example).
  - You prefer to use your own email input field. | Programmatically pass a customer email address to the Payment Element. In this scenario, a customer authenticates to Link directly in the payment form instead of a separate UI component.                                                                 |
| Collect a customer email address in the Payment Element            | - Your customers can choose to enter their email and authenticate or enroll with Link directly in the Payment Element during checkout.
  - No code change is required.               | If a customer hasn’t enrolled with Link and they choose a supported payment method in the Payment Element, they’re prompted to save their details using Link. For those who have already enrolled, Link automatically populates their payment information. |

Use [defaultValues](https://docs.stripe.com/js/elements_object/create_payment_element#payment_element_create-options-defaultValues) to pass a customer email address to the Payment Element.

```javascript
const paymentElement = elements.create('payment', {
  defaultValues: {
    billingDetails: {
      email: 'foo@bar.com',
    }
  },
  // Other options
});
```

For more information, read how to [build a custom checkout page that includes Link](https://docs.stripe.com/payments/link/add-link-elements-integration.md).

## Optional: Save and retrieve customer payment methods

You can configure the Payment Element to save your customer’s payment methods for future use. This section shows you how to integrate the [saved payment methods feature](https://docs.stripe.com/payments/save-customer-payment-methods.md), which enables the Payment Element to:

- Prompt buyers for consent to save a payment method
- Save payment methods when buyers provide consent
- Display saved payment methods to buyers for future purchases
- [Automatically update lost or expired cards](https://docs.stripe.com/payments/cards/overview.md#automatic-card-updates) when buyers replace them
![The Payment Element and a saved payment method checkbox](https://b.stripecdn.com/docs-statics-srv/assets/spm-save.fe0b24afd0f0a06e0cf4eecb0ce2403a.png)

Save payment methods.
![The Payment Element with a Saved payment method selected](https://b.stripecdn.com/docs-statics-srv/assets/spm-saved.5dba5a8a190a9a0e9f1a99271bed3f4b.png)

Reuse a previously saved payment method.

### Enable saving the payment method in the Payment Element

Create a [CustomerSession](https://docs.stripe.com/api/customer_sessions/.md) on your server by providing the [Customer ID](https://docs.stripe.com/api/customers/object.md#customer_object-id) and enabling the [payment_element](https://docs.stripe.com/api/customer_sessions/object.md#customer_session_object-components-payment_element) component for your session. Configure which saved payment method [features](https://docs.stripe.com/api/customer_sessions/create.md#create_customer_session-components-payment_element-features) you want to enable. For instance, enabling [payment_method_save](https://docs.stripe.com/api/customer_sessions/create.md#create_customer_session-components-payment_element-features-payment_method_save) displays a checkbox that allows customers to save their payment details for future use.

You can specify `setup_future_usage` on a PaymentIntent or Checkout Session to override the default behavior for saving payment methods. This ensures that you automatically save the payment method for future use, even if the customer doesn’t explicitly choose to save it.

> Allowing buyers to remove their saved payment methods by enabling [payment_method_remove](https://docs.stripe.com/api/customer_sessions/create.md#create_customer_session-components-payment_element-features-payment_method_remove) impacts subscriptions that depend on that payment method. Removing the payment method detaches the [PaymentMethod](https://docs.stripe.com/api/payment_methods.md) from that [Customer](https://docs.stripe.com/api/customers.md).

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

post '/create-intent-and-customer-session' do
  intent = Stripe::SetupIntent.create({
    # In the latest version of the API, specifying the `automatic_payment_methods` parameter
    # is optional because Stripe enables its functionality by default.
    automatic_payment_methods: {enabled: true},
    customer: {{CUSTOMER_ID}},
  })
  customer_session = Stripe::CustomerSession.create({
    customer: {{CUSTOMER_ID}},
    components: {
      payment_element: {
          enabled: true,
          features: {
            payment_method_redisplay: 'enabled',
            payment_method_save: 'enabled',
            payment_method_save_usage: 'off_session',
            payment_method_remove: 'enabled',
          },
        },
    },
  })
  {
    client_secret: intent.client_secret,
    customer_session_client_secret: customer_session.client_secret
  }.to_json
end
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

@app.route('/create-intent-and-customer-session', methods=['POST'])
def createIntentAndCustomerSession():
  intent = stripe.SetupIntent.create(
    # In the latest version of the API, specifying the `automatic_payment_methods` parameter
    # is optional because Stripe enables its functionality by default.
    automatic_payment_methods={
      'enabled': True,
    },
    customer={{CUSTOMER_ID}},
  )
  customer_session = stripe.CustomerSession.create(
    customer={{CUSTOMER_ID}},
    components={
      "payment_element": {
        "enabled": True,
        "features": {
          "payment_method_redisplay": "enabled",
          "payment_method_save": "enabled",
          "payment_method_save_usage": "off_session",
          "payment_method_remove": "enabled",
        },
      },
    },
  )
  return jsonify(
    client_secret=intent.client_secret,
    customer_session_client_secret=customer_session.client_secret
  )
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$intent = $stripe->setupIntents->create(
  [
    // In the latest version of the API, specifying the `automatic_payment_methods` parameter
    // is optional because Stripe enables its functionality by default.
    'automatic_payment_methods' => ['enabled' => true],
    'customer' => {{CUSTOMER_ID}},
  ]
);
$customer_session = $stripe->customerSessions->create([
  'customer' => {{CUSTOMER_ID}},
  'components' => [
    'payment_element' => [
      'enabled' => true,
      'features' => [
        'payment_method_redisplay' => 'enabled',
        'payment_method_save' => 'enabled',
        'payment_method_save_usage' => 'off_session',
        'payment_method_remove' => 'enabled',
      ],
    ],
  ],
]);
echo json_encode(array(
  'client_secret' => $intent->client_secret,
  'customer_session_client_secret' => $customer_session->client_secret
));
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

app.post('/create-intent-and-customer-session', async (req, res) => {
  const intent = await stripe.setupIntents.create({
    // In the latest version of the API, specifying the `automatic_payment_methods` parameter
    // is optional because Stripe enables its functionality by default.
    automatic_payment_methods: {enabled: true},
    customer: {{CUSTOMER_ID}},
  });
  const customerSession = await stripe.customerSessions.create({
    customer: {{CUSTOMER_ID}},
    components: {
      payment_element: {
        enabled: true,
        features: {
          payment_method_redisplay: 'enabled',
          payment_method_save: 'enabled',
          payment_method_save_usage: 'off_session',
          payment_method_remove: 'enabled',
        },
      },
    },
  });
  res.json({
    client_secret: intent.client_secret,
    customer_session_client_secret: customerSession.client_secret
  });
});
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

post(
  "/create-intent-and-customer-session",
  (request, response) -> {
    response.type("application/json");

    SetupIntentCreateParams intentParams = SetupIntentCreateParams.builder()
      // In the latest version of the API, specifying the `automatic_payment_methods` parameter
      // is optional because Stripe enables its functionality by default.
      .setAutomaticPaymentMethods(
        SetupIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
      )
      .setCustomer({{CUSTOMER_ID}})
      .build();

    SetupIntent setupIntent = SetupIntent.create(intentParams);

    CustomerSessionCreateParams csParams = CustomerSessionCreateParams.builder()
      .setCustomer({{CUSTOMER_ID}})
      .setComponents(CustomerSessionCreateParams.Components.builder().build())
      .putExtraParam("components[payment_element][enabled]", true)
      .putExtraParam(
        "components[payment_element][features][payment_method_redisplay]",
        "enabled"
      )
      .putExtraParam(
        "components[payment_element][features][payment_method_save]",
        "enabled"
      )
      .putExtraParam(
        "components[payment_element][features][payment_method_save_usage]",
        "off_session"
      )
      .putExtraParam(
        "components[payment_element][features][payment_method_remove]",
        "enabled"
      )
      .build();

    CustomerSession customerSession = CustomerSession.create(csParams);

    Map<String, Object> responseData = new HashMap<>();
    responseData.put("clientSecret", setupIntent.getClientSecret());
    responseData.put("customerSessionClientSecret", customerSession.getClientSecret());
    return StripeObject.PRETTY_PRINT_GSON.toJson(responseData);
  }
);
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

type CheckoutData struct {
  ClientSecret string `json:"client_secret"`
  CustomerSessionClientSecret string `json:"customer_session_client_secret"`
}

func main() {
  http.HandleFunc("/create-intent-and-customer-session", func(w http.ResponseWriter, r *http.Request) {
    intentParams := &stripe.SetupIntentParams{
      // In the latest version of the API, specifying the `automatic_payment_methods` parameter
      // is optional because Stripe enables its functionality by default.
      AutomaticPaymentMethods: &stripe.SetupIntentAutomaticPaymentMethodsParams{
        Enabled: stripe.Bool(true),
      },
      Customer: stripe.String({{CUSTOMER_ID}}),
    };
    intent, _ := .New(intentParams);

    csParams := &stripe.CustomerSessionParams{
      Customer: stripe.String({{CUSTOMER_ID}}),
      Components: &stripe.CustomerSessionComponentsParams{},
    }
    csParam.AddExtra("components[payment_element][enabled]", true)
    csParam.AddExtra(
      "components[payment_element][features][payment_method_redisplay]",
      "enabled",
    )
    csParam.AddExtra(
      "components[payment_element][features][payment_method_save]",
      "enabled",
    )
    csParam.AddExtra(
      "components[payment_element][features][payment_method_save_usage]",
      "off_session",
    )
    csParam.AddExtra(
      "components[payment_element][features][payment_method_remove]",
      "enabled",
    )

    customerSession, _ := customersession.New(csParams)
    data := CheckoutData{
      ClientSecret: intent.ClientSecret,
      CustomerSessionClientSecret: customerSession.ClientSecret
    }
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(http.StatusOK)
    json.NewEncoder(w).Encode(data)
  })
}
```

#### .NET

```csharp

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

namespace StripeExampleApi.Controllers
{
  [Route("create-intent-and-customer-session")]
  [ApiController]
  public class CheckoutApiController : Controller
  {
    [HttpPost]
    public ActionResult Post()
    {
      var intentOptions = new SetupIntentCreateOptions
      {
          // In the latest version of the API, specifying the `automatic_payment_methods` parameter
          // is optional because Stripe enables its functionality by default.
          AutomaticPaymentMethods = new SetupIntentAutomaticPaymentMethodsOptions
          {
              Enabled = true,
          },
          Customer = {{CUSTOMER_ID}},
      };
      var intentService = new SetupIntentService();
      var intent = intentService.Create(intentOptions);

      var customerSessionOptions = new CustomerSessionCreateOptions
      {
          Customer = {{CUSTOMER_ID}},
          Components = new CustomerSessionComponentsOptions(),
      }
      customerSessionOptions.AddExtraParam("components[payment_element][enabled]", true);
      customerSessionOptions.AddExtraParam(
          "components[payment_element][features][payment_method_redisplay]",
          "enabled");
      customerSessionOptions.AddExtraParam(
          "components[payment_element][features][payment_method_save]",
          "enabled");
      customerSessionOptions.AddExtraParam(
          "components[payment_element][features][payment_method_save_usage]",
          "off_session");
      customerSessionOptions.AddExtraParam(
          "components[payment_element][features][payment_method_remove]",
          "enabled");
      var customerSessionService = new CustomerSessionService();
      var customerSession = customerSessionService.Create(customerSessionOptions);

      return Json(new {
        client_secret = intent.ClientSecret,
        customerSessionClientSecret = customerSession.ClientSecret
      });
    }
  }
}
```

Your Elements instance uses the CustomerSession’s *client secret* (A client secret is used with your publishable key to authenticate a request for a single object. Each client secret is unique to the object it's associated with) to access that customer’s saved payment methods. [Handle errors](https://docs.stripe.com/error-handling.md) properly when you create the CustomerSession. If an error occurs, you don’t need to provide the CustomerSession client secret to the Elements instance, as it’s optional.

Create the Elements instance using the client secrets for both the SetupIntent and the CustomerSession. Then, use this Elements instance to create a Payment Element.

```javascript
// Create the CustomerSession and obtain its clientSecret
const res = await fetch("/create-intent-and-customer-session", {
  method: "POST"
});

const {
  customer_session_client_secret: customerSessionClientSecret
} = await res.json();

const elementsOptions = {
  clientSecret: '{{CLIENT_SECRET}}',customerSessionClientSecret,
  // Fully customizable with appearance API.
  appearance: {/*...*/},
};

// Set up Stripe.js and Elements to use in checkout form, passing the client secret
// and CustomerSession's client secret obtained in a previous step
const elements = stripe.elements(elementsOptions);

// Create and mount the Payment Element
const paymentElementOptions = { layout: 'accordion'};
const paymentElement = elements.create('payment', paymentElementOptions);
paymentElement.mount('#payment-element');
```

When confirming the SetupIntent, Stripe.js automatically controls setting [allow_redisplay](https://docs.stripe.com/api/payment_methods/object.md#payment_method_object-allow_redisplay) on the PaymentMethod, depending on whether the customer checked the box to save their payment details.

### Detect the selection of a saved payment method

To control dynamic content when a saved payment method is selected, listen to the Payment Element `change` event, which is populated with the selected payment method.

```javascript
paymentElement.on('change', function(event) {
  if (event.value.payment_method) {
    // Control dynamic content if a saved payment method is selected
  }
})
```

## Submit the payment details to Stripe [Client-side]

Use [stripe.confirmSetup](https://docs.stripe.com/js/setup_intents/confirm_setup) to complete the setup using details collected by the Payment Element. Provide a [return_url](https://docs.stripe.com/api/setup_intents/create.md#create_setup_intent-return_url) to this function so that Stripe can redirect the user after they complete setup. We may first redirect them to an intermediate site, like a bank authorization page, before redirecting them to the `return_url`.

If your customer saves their card details, we immediately redirect them to the `return_url` when setup is successful. If you don’t want to redirect for card payments, you can set [redirect](https://docs.stripe.com/js/setup_intents/confirm_setup#confirm_setup_intent-options-redirect) to `if_required`. This only redirects customers that check out with redirect-based payment methods.

#### HTML + JS

```javascript
const form = document.getElementById('payment-form');

form.addEventListener('submit', async (event) => {
  event.preventDefault();
const {error} = await stripe.confirmSetup({
    //`Elements` instance that was used to create the Payment Element
    elements,
    confirmParams: {
      return_url: 'https://example.com/account/payments/setup-complete',
    }
  });

  if (error) {
    // This point will only be reached if there is an immediate error when
    // confirming the payment. Show error to your customer (for example, payment
    // details incomplete)
    const messageContainer = document.querySelector('#error-message');
    messageContainer.textContent = error.message;
  } else {
    // Your customer will be redirected to your `return_url`. For some payment
    // methods like iDEAL, your customer will be redirected to an intermediate
    // site first to authorize the payment, then redirected to the `return_url`.
  }
});
```

#### React

To call [stripe.confirmSetup](https://docs.stripe.com/js/setup_intents/confirm_setup) from your payment form component, use the [useStripe](https://docs.stripe.com/sdks/stripejs-react.md#usestripe-hook) and [useElements](https://docs.stripe.com/sdks/stripejs-react.md#useelements-hook) hooks.

If you prefer traditional class components over hooks, you can instead use an [ElementsConsumer](https://docs.stripe.com/sdks/stripejs-react.md#elements-consumer).

```jsx
import React, {useState} from 'react';
import {useStripe, useElements, PaymentElement} from '@stripe/react-stripe-js';

const SetupForm = () => {
  const stripe = useStripe();
  const elements = useElements();

  const [errorMessage, setErrorMessage] = useState(null);

  const handleSubmit = async (event) => {
    // We don't want to let default form submission happen here,
    // which would refresh the page.
    event.preventDefault();

    if (!stripe || !elements) {
      // Stripe.js hasn't yet loaded.
      // Make sure to disable form submission until Stripe.js has loaded.
      return null;
    }
const {error} = await stripe.confirmSetup({
      //`Elements` instance that was used to create the Payment Element
      elements,
      confirmParams: {
        return_url: 'https://example.com/account/payments/setup-complete',
      },
    });

    if (error) {
      // This point will only be reached if there is an immediate error when
      // confirming the payment. Show error to your customer (for example, payment
      // details incomplete)
      setErrorMessage(error.message);
    } else {
      // Your customer will be redirected to your `return_url`. For some payment
      // methods like iDEAL, your customer will be redirected to an intermediate
      // site first to authorize the payment, then redirected to the `return_url`.
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <PaymentElement />
      <button disabled={!stripe}>Submit</button>
      {/* Show error message to your customers */}
      {errorMessage && <div>{errorMessage}</div>}
    </form>
  )
};

export default SetupForm;
```

Make sure the `return_url` corresponds to a page on your website that [provides the status](https://docs.stripe.com/payments/payment-intents/verifying-status.md) of the `SetupIntent`. Stripe provides the following URL query parameters to verify the status when we redirect the customer to the `return_url`. You can also append your own query parameters when providing the `return_url`, and they persist through the redirect process.

| Parameter                    | Description                                                                                                                             |
| ---------------------------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| `setup_intent`               | The unique identifier for the `SetupIntent`.                                                                                            |
| `setup_intent_client_secret` | The [client secret](https://docs.stripe.com/api/setup_intents/object.md#setup_intent_object-client_secret) of the `SetupIntent` object. |

You can use [stripe.retrieveSetupIntent](https://docs.stripe.com/js/setup_intents/retrieve_setup_intent) to retrieve the SetupIntent using the `setup_intent_client_secret` query parameter. Successful confirmation of the SetupIntent saves the resulting `PaymentMethod` ID (in `result.setupIntent.payment_method`) to the provided `Customer`.

#### HTML + JS

```javascript

// Initialize Stripe.js using your publishable key
const stripe = Stripe('{PUBLISHABLE_KEY}');

// Retrieve the "setup_intent_client_secret" query parameter appended to
// your return_url by Stripe.js
const clientSecret = new URLSearchParams(window.location.search).get(
  'setup_intent_client_secret'
);

// Retrieve the SetupIntent
stripe.retrieveSetupIntent(clientSecret).then(({setupIntent}) => {
  const message = document.querySelector('#message')

  // Inspect the SetupIntent `status` to indicate the status of the payment
  // to your customer.
  //
  // Some payment methods will [immediately succeed or fail][0] upon
  // confirmation, while others will first enter a `processing` state.
  //
  // [0]: https://stripe.com/docs/payments/payment-methods#payment-notification
  switch (setupIntent.status) {
    case 'succeeded': {
      message.innerText = 'Success! Your payment method has been saved.';
      break;
    }

    case 'processing': {
      message.innerText = "Processing payment details. We'll update you when processing is complete.";
      break;
    }

    case 'requires_payment_method': {
      message.innerText = 'Failed to process payment details. Please try another payment method.';

      // Redirect your user back to your payment page to attempt collecting
      // payment again

      break;
    }
  }
});
```

#### React

```jsx

// PaymentStatus.jsx

import React, {useState, useEffect} from 'react';
import {useStripe} from '@stripe/react-stripe-js';

const PaymentStatus = () => {
  const stripe = useStripe();
  const [message, setMessage] = useState(null);

  useEffect(() => {
    if (!stripe) {
      return;
    }

    // Retrieve the "setup_intent_client_secret" query parameter appended to
    // your return_url by Stripe.js
    const clientSecret = new URLSearchParams(window.location.search).get(
      'setup_intent_client_secret'
    );

    // Retrieve the SetupIntent
    stripe
      .retrieveSetupIntent(clientSecret)
      .then(({setupIntent}) => {
        // Inspect the SetupIntent `status` to indicate the status of the payment
        // to your customer.
        //
        // Some payment methods will [immediately succeed or fail][0] upon
        // confirmation, while others will first enter a `processing` state.
        //
        // [0]: https://stripe.com/docs/payments/payment-methods#payment-notification
        switch (setupIntent.status) {
          case 'succeeded':
            setMessage('Success! Your payment method has been saved.');
            break;

          case 'processing':
            setMessage("Processing payment details. We'll update you when processing is complete.");
            break;

          case 'requires_payment_method':
            // Redirect your user back to your payment page to attempt collecting
            // payment again
            setMessage('Failed to process payment details. Please try another payment method.');
            break;
        }
      });
  }, [stripe]);


  return message
};

export default PaymentStatus;
```

> If you have tooling that tracks the customer’s browser session, you might need to add the `stripe.com` domain to the referrer exclude list. Redirects cause some tools to create new sessions which prevents you from tracking the complete session.

## Charge the saved payment method later [Server-side]

> #### Compliance
> 
> You’re responsible for your compliance with all applicable laws, regulations, and network rules when saving a customer’s payment details. When rendering past payment methods to your end customer for future purchases, make sure you’re listing payment methods where you’ve collected consent from the customer to save the payment method details for this specific future use. To differentiate between payment methods attached to customers that can and can’t be presented to your end customer as a saved payment method for future purchases, use the [allow_redisplay](https://docs.stripe.com/api/payment_methods/object.md#payment_method_object-allow_redisplay) parameter.

When you’re ready to charge your customer *off-session* (A payment is described as off-session if it occurs without the direct involvement of the customer, using previously-collected payment information), use the Customer and PaymentMethod IDs to create a PaymentIntent. To find a payment method to charge, list the payment methods associated with your customer. This example lists cards but you can list any supported [type](https://docs.stripe.com/api/payment_methods/object.md#payment_method_object-type).

```curl
curl -G https://api.stripe.com/v1/payment_methods \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d type=card
```

```cli
stripe payment_methods list  \
  --customer="{{CUSTOMER_ID}}" \
  --type=card
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_methods = client.v1.payment_methods.list({
  customer: '{{CUSTOMER_ID}}',
  type: 'card',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_methods = client.v1.payment_methods.list({
  "customer": "{{CUSTOMER_ID}}",
  "type": "card",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentMethods = $stripe->paymentMethods->all([
  'customer' => '{{CUSTOMER_ID}}',
  'type' => 'card',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentMethodListParams params =
  PaymentMethodListParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setType(PaymentMethodListParams.Type.CARD)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
StripeCollection<PaymentMethod> stripeCollection =
  client.v1().paymentMethods().list(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentMethods = await stripe.paymentMethods.list({
  customer: '{{CUSTOMER_ID}}',
  type: 'card',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentMethodListParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  Type: stripe.String(stripe.PaymentMethodTypeCard),
}
result := sc.V1PaymentMethods.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentMethodListOptions
{
    Customer = "{{CUSTOMER_ID}}",
    Type = "card",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentMethods;
StripeList<PaymentMethod> paymentMethods = service.List(options);
```

When you have the Customer and PaymentMethod IDs, create a PaymentIntent with the amount and currency of the payment. Set a few other parameters to make the off-session payment:

- Set [off_session](https://docs.stripe.com/api/payment_intents/confirm.md#confirm_payment_intent-off_session) to `true` to indicate that the customer isn’t in your checkout flow during a payment attempt and can’t fulfill an authentication request made by a partner, such as a card issuer, bank, or other payment institution. If, during your checkout flow, a partner requests authentication, Stripe requests exemptions using customer information from a previous *on-session* (A payment is described as on-session if it occurs while the customer is actively in your checkout flow and able to authenticate the payment method) transaction. If the conditions for exemption aren’t met, the PaymentIntent might throw an error.
- Set the value of the PaymentIntent’s [confirm](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-confirm) property to `true`, which causes confirmation to occur immediately when the PaymentIntent is created.
- Set [payment_method](https://docs.stripe.com/api.md#create_payment_intent-payment_method) to the ID of the PaymentMethod and [customer](https://docs.stripe.com/api.md#create_payment_intent-customer) to the ID of the Customer.

#### curl

```bash
curl https://api.stripe.com/v1/payment_intents \
  -u <<YOUR_SECRET_KEY>>: \
  -d amount=1099 \
  -d currency=usd \
  # In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default.
  -d "automatic_payment_methods[enabled]"=true \-d customer="{{CUSTOMER_ID}}" \
  -d payment_method="{{PAYMENT_METHOD_ID}}" \
  -d return_url="https://example.com/order/123/complete" \
  -d off_session=true \
  -d confirm=true
```

#### Stripe CLI

```bash
stripe payment_intents create \
  --amount=1099 \
  --currency=usd \
  -d "automatic_payment_methods[enabled]"=true \
 # In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default.--customer={{CUSTOMER_ID}} \
  --payment-method={{PAYMENT_METHOD_ID}} \
  --return-url="https://example.com/order/123/complete" \
  --off-session=true \
  --confirm=true
```

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

begin
  intent = Stripe::PaymentIntent.create({
    amount: 1099,
    currency: 'usd',
    # In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default.
    automatic_payment_methods: {enabled: true},customer: '{{CUSTOMER_ID}}',
    payment_method: '{{PAYMENT_METHOD_ID}}',
    return_url: 'https://example.com/order/123/complete',
    off_session: true,
    confirm: true,
  })
rescue Stripe::CardError => e
  # Error code will be authentication_required if authentication is needed
  puts "Error is: \#{e.error.code}"
  payment_intent_id = e.error.payment_intent.id
  payment_intent = Stripe::PaymentIntent.retrieve(payment_intent_id)
  puts payment_intent.id
end
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

try:
  stripe.PaymentIntent.create(
    amount=1099,
    currency='usd',
    # In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default.
    automatic_payment_methods={"enabled": True},customer='{{CUSTOMER_ID}}',
    payment_method='{{PAYMENT_METHOD_ID}}',
    return_url='https://example.com/order/123/complete',
    off_session=True,
    confirm=True,
  )
except stripe.error.CardError as e:
  err = e.error
  # Error code will be authentication_required if authentication is needed
  print("Code is: %s" % err.code)
  payment_intent_id = err.payment_intent['id']
  payment_intent = stripe.PaymentIntent.retrieve(payment_intent_id)
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

try {
  \Stripe\PaymentIntent::create([
    'amount' => 1099,
    'currency' => 'usd',
    // In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default.
    'automatic_payment_methods' => ['enabled' => true],'customer' => '{{CUSTOMER_ID}}',
    'payment_method' => '{{PAYMENT_METHOD_ID}}',
    'return_url' => 'https://example.com/order/123/complete',
    'off_session' => true,
    'confirm' => true,
  ]);
} catch (\Stripe\Exception\CardException $e) {
  // Error code will be authentication_required if authentication is needed
  echo 'Error code is:' . $e->getError()->code;
  $payment_intent_id = $e->getError()->payment_intent->id;
  $payment_intent = \Stripe\PaymentIntent::retrieve($payment_intent_id);
}
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

PaymentIntentCreateParams params =
  PaymentIntentCreateParams.builder()
    .setCurrency("usd")
    .setAmount(1099)
    // In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default.
    .setAutomaticPaymentMethods(
      PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
    ).setCustomer("{{CUSTOMER_ID}}")
    .setPaymentMethod("{{PAYMENT_METHOD_ID}}")
    .setReturnUrl("https://example.com/order/123/complete")
    .setConfirm(true)
    .setOffSession(true)
    .build();
try {
  PaymentIntent.create(params);
} catch (CardException e) {
  // Error code will be authentication_required if authentication is needed
  System.out.println("Error code is : " + e.getCode());
  String paymentIntentId = e.getStripeError().getPaymentIntent().getId();
  PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
  System.out.println(paymentIntent.getId());
}
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

try {
  const paymentIntent = await stripe.paymentIntents.create({
    amount: 1099,
    currency: 'usd',
    // In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default.
    automatic_payment_methods: {enabled: true},customer: '{{CUSTOMER_ID}}',
    payment_method: '{{PAYMENT_METHOD_ID}}',
    return_url: 'https://example.com/order/123/complete',
    off_session: true,
    confirm: true,
  });
} catch (err) {
  // Error code will be authentication_required if authentication is needed
  console.log('Error code is: ', err.code);
  const paymentIntentRetrieved = await stripe.paymentIntents.retrieve(err.raw.payment_intent.id);
  console.log('PI retrieved: ', paymentIntentRetrieved.id);
}
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

params := &stripe.PaymentIntentParams{
  Amount: stripe.Int64(1099),
  Currency: stripe.String(string(stripe.CurrencyUSD)),
  // In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default.
  AutomaticPaymentMethods: &stripe.PaymentIntentAutomaticPaymentMethodsParams{
    Enabled: stripe.Bool(true),
  },Customer: stripe.String("{{CUSTOMER_ID}}"),
  PaymentMethod: stripe.String("{{PAYMENT_METHOD_ID}}"),
  ReturnUrl: stripe.String("https://example.com/order/123/complete"),
  Confirm: stripe.Bool(true),
  OffSession: stripe.Bool(true),
}

_, err := paymentintent.New(params)

if err != nil {
  if stripeErr, ok := err.(*stripe.Error); ok {
    // Error code will be authentication_required if authentication is needed
    fmt.Printf("Error code: %v", stripeErr.Code)

    paymentIntentID := stripeErr.PaymentIntent.ID
    paymentIntent, _ := paymentintent.Get(paymentIntentID, nil)

    fmt.Printf("PI: %v", paymentIntent.ID)
  }
}
```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

try
{
  var service = new PaymentIntentService();
  var options = new PaymentIntentCreateOptions
  {
    Amount = 1099,
    Currency = "usd",
    // In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default.
    AutomaticPaymentMethods = new PaymentIntentAutomaticPaymentMethodsOptions
    {
        Enabled = true,
    },Customer = "{{CUSTOMER_ID}}",
    PaymentMethod = "{{PAYMENT_METHOD_ID}}",
    ReturnUrl = "https://example.com/order/123/complete",
    Confirm = true,
    OffSession = true,
  };
  service.Create(options);
}
catch (StripeException e)
{
  switch (e.StripeError.Type)
  {
    case "card_error":
      // Error code will be authentication_required if authentication is needed
      Console.WriteLine("Error code: " + e.StripeError.Code);
      var paymentIntentId = e.StripeError.PaymentIntent.Id;
      var service = new PaymentIntentService();
      var paymentIntent = service.Get(paymentIntentId);

      Console.WriteLine(paymentIntent.Id);
      break;
    default:
      break;
  }
}
```

When a payment attempt fails, the request also fails with a 402 HTTP status code and the status of the PaymentIntent is *requires\_payment\_method* (This status appears as "requires_source" in API versions before 2019-02-11). You must notify your customer to return to your application to complete the payment (for example, by sending an email or in-app notification).

Check the code of the [error](https://docs.stripe.com/api/errors/handling.md) raised by the Stripe API library. If the payment failed due to an [authentication_required](https://docs.stripe.com/declines/codes.md) decline code, use the declined PaymentIntent’s client secret with confirmPayment to allow the customer to authenticate the payment.

```javascript
const form = document.getElementById('payment-form');

form.addEventListener('submit', async (event) => {
  event.preventDefault();

  const {error} = await stripe.confirmPayment({
    // The client secret of the PaymentIntent
    clientSecret,
    confirmParams: {
      return_url: 'https://example.com/order/123/complete',
    },
  });

  if (error) {
    // This point will only be reached if there is an immediate error when
    // confirming the payment. Show error to your customer (for example, payment
    // details incomplete)
    const messageContainer = document.querySelector('#error-message');
    messageContainer.textContent = error.message;
  } else {
    // Your customer will be redirected to your `return_url`. For some payment
    // methods like iDEAL, your customer will be redirected to an intermediate
    // site first to authorize the payment, then redirected to the `return_url`.
  }
});
```

> `stripe.confirmPayment` can take several seconds to complete. During that time, disable your form from being resubmitted and show a waiting indicator like a spinner. If you receive an error, show it to the customer, re-enable the form, and hide the waiting indicator. If the customer must perform additional steps to complete the payment, such as authentication, Stripe.js walks them through that process.

If the payment failed for other reasons, such as insufficient funds, send your customer to a payment page to enter a new payment method. You can reuse the existing PaymentIntent to attempt the payment again with the new payment details.

## Test the integration

Use test payment details and the test redirect page to verify your integration. Click the tabs below to view details for each payment method.

#### Cards

| Payment method | Scenario                                                                                                                                                                                                                                                                                                        | How to test                                                                                                                 |
| -------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------- |
| Credit card    | The card setup succeeds and doesn’t require *authentication* (Strong Customer Authentication (SCA) is a regulatory requirement in effect as of September 14, 2019, that impacts many European online payments. It requires customers to use two-factor authentication like 3D Secure to verify their purchase). | Fill out the credit card form using the credit card number `4242 4242 4242 4242` with any expiration, CVC, and postal code. |
| Credit card    | The card requires authentication for the initial setup, then succeeds for subsequent payments.                                                                                                                                                                                                                  | Fill out the credit card form using the credit card number `4000 0025 0000 3155` with any expiration, CVC, and postal code. |
| Credit card    | The card requires authentication for the initial setup and also requires authentication for subsequent payments.                                                                                                                                                                                                | Fill out the credit card form using the credit card number `4000 0027 6000 3184` with any expiration, CVC, and postal code. |
| Credit card    | The card is declined during setup.                                                                                                                                                                                                                                                                              | Fill out the credit card form using the credit card number `4000 0000 0000 9995` with any expiration, CVC, and postal code. |

#### Bank redirects

| Payment method    | Scenario                                                                                                                                                 | How to test                                                                                                                                                                            |
| ----------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Bancontact        | Your customer successfully sets up a SEPA Direct Debit payment method for future usage by using Bancontact.                                              | Use any name in the Bancontact form, then click **Authorize test setup** on the redirect page.                                                                                         |
| Bancontact        | Your customer fails to authenticate on the Bancontact redirect page.                                                                                     | Use any name in the Bancontact form, then click **Fail test setup** on the redirect page.                                                                                              |
| BECS Direct Debit | Your customer successfully pays with BECS Direct Debit.                                                                                                  | Fill out the form using the account number `900123456`. The confirmed PaymentIntent initially transitions to `processing`, then transitions to the `succeeded` status 3 minutes later. |
| BECS Direct Debit | Your customer’s payment fails with an `account_closed` error code.                                                                                       | Fill out the form using the account number `111111113`.                                                                                                                                |
| iDEAL             | Your customer successfully sets up a [SEPA Direct Debit](https://docs.stripe.com/payments/sepa-debit.md) payment method for future usage by using iDEAL. | Use any name and bank in the iDEAL form, then click **Authorize test setup** on the redirect page.                                                                                     |
| iDEAL             | Your customer fails to authenticate on the iDEAL redirect page.                                                                                          | Select any bank and use any name in the iDEAL form, then click **Fail test setup** on the redirect page.                                                                               |

#### Bank debits

| Payment method    | Scenario                                                                                         | How to test                                                                                                                                                                                       |
| ----------------- | ------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| SEPA Direct Debit | Your customer successfully pays with SEPA Direct Debit.                                          | Fill out the form using the account number `AT321904300235473204`. The confirmed PaymentIntent initially transitions to processing, then transitions to the succeeded status three minutes later. |
| SEPA Direct Debit | Your customer’s payment intent status transition from `processing` to `requires_payment_method`. | Fill out the form using the account number `AT861904300235473202`.                                                                                                                                |

### Test charging a saved SEPA Debit PaymentMethod

Confirming the  SetupIntent  using iDEAL, Bancontact, or Sofort, generates a [SEPA Direct Debit](https://docs.stripe.com/payments/sepa-debit.md) *PaymentMethod* (PaymentMethods represent your customer's payment instruments, used with the Payment Intents or Setup Intents APIs). SEPA Direct Debit is a [delayed notification](https://docs.stripe.com/payments/payment-methods.md#payment-notification) payment method that transitions to an intermediate `processing` state before transitioning several days later to a `succeeded` or `requires_payment_method` state.

#### Email

Set `payment_method.billing_details.email` to one of the following values to test the PaymentIntent status transitions. You can include your own custom text at the beginning of the email address followed by an underscore. For example, `test_1_generatedSepaDebitIntentsFail@example.com` results in a SEPA Direct Debit PaymentMethod that always fails when used with a PaymentIntent.

| Email Address                                          | Description                                                                                                       |
| ------------------------------------------------------ | ----------------------------------------------------------------------------------------------------------------- |
| `generatedSepaDebitIntentsSucceed@example.com`         | The PaymentIntent status transitions from `processing` to `succeeded`.                                            |
| `generatedSepaDebitIntentsSucceedDelayed@example.com`  | The PaymentIntent status transitions from `processing` to `succeeded` after at least three minutes.               |
| `generatedSepaDebitIntentsFail@example.com`            | The PaymentIntent status transitions from `processing` to `requires_payment_method`.                              |
| `generatedSepaDebitIntentsFailDelayed@example.com`     | The PaymentIntent status transitions from `processing` to `requires_payment_method` after at least three minutes. |
| `generatedSepaDebitIntentsSucceedDisputed@example.com` | The PaymentIntent status transitions from `processing` to `succeeded`, but a dispute is created immediately.      |

#### PaymentMethod

Use these PaymentMethods to test that the PaymentIntent status transitions. These tokens are useful for automated testing to immediately attach the PaymentMethod to the SetupIntent on the server.

| Payment Method                                           | Description                                                                                                       |
| -------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------- |
| `pm_bancontact_generatedSepaDebitIntentsSucceed`         | The PaymentIntent status transitions from `processing` to `succeeded`.                                            |
| `pm_bancontact_generatedSepaDebitIntentsSucceedDelayed`  | The PaymentIntent status transitions from `processing` to `succeeded` after at least three minutes.               |
| `pm_bancontact_generatedSepaDebitIntentsFail`            | The PaymentIntent status transitions from `processing` to `requires_payment_method`.                              |
| `pm_bancontact_generatedSepaDebitIntentsFailDelayed`     | The PaymentIntent status transitions from `processing` to `requires_payment_method` after at least three minutes. |
| `pm_bancontact_generatedSepaDebitIntentsSucceedDisputed` | The PaymentIntent status transitions from `processing` to `succeeded`, but a dispute is created immediately.      |

## Optional: Customize the layout [Client-side]

You can customize the Payment Element’s layout (accordion or tabs) to fit your checkout interface. For more information about each of the properties, see  [elements.create](https://docs.stripe.com/js/elements_object/create_payment_element#payment_element_create-options).

#### Accordion

You can start using the layout features by passing a layout `type` and other optional properties when creating the Payment Element:

```javascript
const paymentElement = elements.create('payment', {
  layout: {
    type: 'accordion',
    defaultCollapsed: false,
    radios: true,
    spacedAccordionItems: false
  }
});
```

#### Tabs

### Specify the layout

Set the value for layout to `tabs`. You also have the option to specify other properties, such as the ones in the following example:

```javascript
const paymentElement = elements.create('payment', {
  layout: {
    type: 'tabs',
    defaultCollapsed: false,
  }
});
```

The following image is the same Payment Element rendered using different layout configurations:
![Three checkout form experiences](https://b.stripecdn.com/docs-statics-srv/assets/pe_layout_example.525f78bcb99b95e49be92e5dd34df439.png)

Payment Element layouts

## Optional: Apple Pay and Google Pay [Client-side]

When you [enable card payments](https://docs.stripe.com/payments/save-and-reuse.md?platform=web&ui=elements#create-intent), we display Apple Pay and Google Pay for customers whose environment meets the [wallet display conditions](https://docs.stripe.com/testing/wallets.md). To accept payments from these wallets, you must also:

- Enable them in your [payment methods settings](https://dashboard.stripe.com/settings/payment_methods). Apple Pay is enabled by default.
- [Register your domain](https://docs.stripe.com/payments/payment-methods/pmd-registration.md).

> #### Regional Testing
> 
> Stripe Elements doesn’t support Google Pay or Apple Pay for Stripe accounts and customers in India. Therefore, you can’t test your Google Pay or Apple Pay integration if the tester’s IP address is in India, even if the Stripe account is based outside India.

## Disclose Stripe to your customers 

Stripe collects information on customer interactions with Elements to provide services to you, prevent fraud, and improve its services. This includes using cookies and IP addresses to identify which Elements a customer saw during a single checkout session. You’re responsible for disclosing and obtaining all rights and consents necessary for Stripe to use data in these ways. For more information, visit our [privacy center](https://stripe.com/legal/privacy-center#as-a-business-user-what-notice-do-i-provide-to-my-end-customers-about-stripe).

## See also

- [Accept a payment](https://docs.stripe.com/payments/accept-a-payment.md)
- [Save payment details during payment](https://docs.stripe.com/payments/save-during-payment.md)
- [The Elements Appearance API](https://docs.stripe.com/elements/appearance-api.md)
- [Send complete fraud signals](https://docs.stripe.com/radar/optimize-fraud-signals.md)

