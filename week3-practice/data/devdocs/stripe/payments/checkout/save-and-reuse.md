# Set up future payments

Learn how to save payment details in a Checkout session and charge your customers later.

# Stripe-hosted page

> This is a Stripe-hosted page for when payment-ui is stripe-hosted. View the full page at https://docs.stripe.com/payments/checkout/save-and-reuse?payment-ui=stripe-hosted.

To collect customer payment details that you can reuse later, use Checkout’s setup mode. Setup mode uses the [Setup Intents API](https://docs.stripe.com/api/setup_intents.md) to create [Payment Methods](https://docs.stripe.com/api/payment_methods.md).

Check out our [full, working sample on GitHub](https://github.com/stripe-samples/checkout-remember-me-with-twilio-verify).

## Set up Stripe [Server-side]

First, you need a Stripe account. [Register now](https://dashboard.stripe.com/register).

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

## Create a Checkout Session [Client-side] [Server-side]

Add a checkout button to your website that calls a server-side endpoint to create a Checkout Session.

```html
<html>
  <head>
    <title>Checkout</title>
  </head>
  <body>
    <form action="/create-checkout-session" method="POST">
      <button type="submit">Checkout</button>
    </form>
  </body>
</html>
```

To create a setup mode Session, use the `mode` parameter with a value of `setup` when creating the Session. You can optionally specify the [customer](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer) parameter to automatically attach the created payment method to an existing customer. Checkout uses [Dynamic payment methods](https://docs.stripe.com/payments/payment-methods/dynamic-payment-methods.md) by default, which requires you to pass the [currency](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-currency) parameter when using `setup` mode.

Append the `{CHECKOUT_SESSION_ID}` template variable to the `success_url` to get access to the Session ID after your customer successfully completes a Checkout Session. After creating the Checkout Session, redirect your customer to the [URL](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-url) returned in the response.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=setup \
  -d currency=usd \
  -d customer="{{CUSTOMER_ID}}" \
  --data-urlencode success_url="https://example.com/success?session_id={CHECKOUT_SESSION_ID}"
```

```cli
stripe checkout sessions create  \
  --mode=setup \
  --currency=usd \
  --customer="{{CUSTOMER_ID}}" \
  --success-url="https://example.com/success?session_id={CHECKOUT_SESSION_ID}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'setup',
  currency: 'usd',
  customer: '{{CUSTOMER_ID}}',
  success_url: 'https://example.com/success?session_id={CHECKOUT_SESSION_ID}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "setup",
  "currency": "usd",
  "customer": "{{CUSTOMER_ID}}",
  "success_url": "https://example.com/success?session_id={CHECKOUT_SESSION_ID}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'setup',
  'currency' => 'usd',
  'customer' => '{{CUSTOMER_ID}}',
  'success_url' => 'https://example.com/success?session_id={CHECKOUT_SESSION_ID}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.SETUP)
    .setCurrency("usd")
    .setCustomer("{{CUSTOMER_ID}}")
    .setSuccessUrl("https://example.com/success?session_id={CHECKOUT_SESSION_ID}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'setup',
  currency: 'usd',
  customer: '{{CUSTOMER_ID}}',
  success_url: 'https://example.com/success?session_id={CHECKOUT_SESSION_ID}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModeSetup),
  Currency: stripe.String(stripe.CurrencyUSD),
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  SuccessURL: stripe.String("https://example.com/success?session_id={CHECKOUT_SESSION_ID}"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "setup",
    Currency = "usd",
    Customer = "{{CUSTOMER_ID}}",
    SuccessUrl = "https://example.com/success?session_id={CHECKOUT_SESSION_ID}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

### Payment methods

By default, Stripe enables cards and other common payment methods. You can turn individual payment methods on or off in the [Stripe Dashboard](https://dashboard.stripe.com/settings/payment_methods). In Checkout, Stripe evaluates the currency and any restrictions, then dynamically presents the supported payment methods to the customer.

To see how your payment methods appear to customers, enter a transaction ID or set an order amount and currency in the Dashboard.

You can enable Apple Pay and Google Pay in your [payment methods settings](https://dashboard.stripe.com/settings/payment_methods). By default, Apple Pay is enabled and Google Pay is disabled. However, in some cases Stripe filters them out even when they’re enabled. We filter Google Pay if you [enable automatic tax](https://docs.stripe.com/tax/checkout.md) without collecting a shipping address.

Checkout’s Stripe-hosted pages don’t need integration changes to enable Apple Pay or Google Pay. Stripe handles these payments the same way as other card payments.

## Retrieve the Checkout Session [Server-side]

After a customer successfully completes their Checkout Session, you need to retrieve the Session object. There are two ways to do this:

- **Asynchronously**: Handle `checkout.session.completed` *webhooks* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests), which contain a Session object. Learn more about [setting up webhooks](https://docs.stripe.com/webhooks.md).
- **Synchronously**: Obtain the Session ID from the `success_url` when a user redirects back to your site. Use the Session ID to [retrieve](https://docs.stripe.com/api/checkout/sessions/retrieve.md) the Session object.

```curl
curl https://api.stripe.com/v1/checkout/sessions/cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe checkout sessions retrieve cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.retrieve('cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.retrieve(
  "cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k",
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->retrieve(
  'cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k',
  []
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionRetrieveParams params = SessionRetrieveParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session =
  client.v1().checkout().sessions().retrieve(
    "cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k",
    params
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.retrieve(
  'cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k'
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionRetrieveParams{}
result, err := sc.V1CheckoutSessions.Retrieve(
  context.TODO(), "cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Get(
    "cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k");
```

The right choice depends on your tolerance for dropoff, as customers may not always reach the `success_url` after a successful payment. It’s possible for them close their browser tab before the redirect occurs. Handling webhooks prevents your integration from being susceptible to this form of dropoff.

After you have retrieved the Session object, get the value of the `setup_intent` key, which is the ID for the SetupIntent created during the Checkout Session. A [SetupIntent](https://docs.stripe.com/payments/setup-intents.md) is an object used to set up the customer’s bank account information for future payments.

Example `checkout.session.completed` payload:

```json
{
  "id": "evt_1Ep24XHssDVaQm2PpwS19Yt0",
  "object": "event",
  "api_version": "2019-03-14",
  "created": 1561420781,
  "data": {
    "object": {
      "id": "cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k",
      "object": "checkout.session",
      "billing_address_collection": null,
      "client_reference_id": null,
      "customer": "",
      "customer_email": null,
      "display_items": [],
      "mode": "setup","setup_intent": "seti_1EzVO3HssDVaQm2PJjXHmLlM",
      "submit_type": null,
      "subscription": null,
      "success_url": "https://example.com/success"
    }
  },
  "livemode": false,
  "pending_webhooks": 1,
  "request": {
    "id": null,
    "idempotency_key": null
  },
  "type": "checkout.session.completed"
}
```

Note the `setup_intent` ID for the next step.

## Retrieve the SetupIntent [Server-side]

Using the `setup_intent` ID, [retrieve](https://docs.stripe.com/api/setup_intents/retrieve.md) the SetupIntent object. The returned object contains a `payment_method` ID that you can attach to a customer in the next step.

```curl
curl https://api.stripe.com/v1/setup_intents/seti_1EzVO3HssDVaQm2PJjXHmLlM \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe setup_intents retrieve seti_1EzVO3HssDVaQm2PJjXHmLlM
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

setup_intent = client.v1.setup_intents.retrieve('seti_1EzVO3HssDVaQm2PJjXHmLlM')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
setup_intent = client.v1.setup_intents.retrieve("seti_1EzVO3HssDVaQm2PJjXHmLlM")
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$setupIntent = $stripe->setupIntents->retrieve('seti_1EzVO3HssDVaQm2PJjXHmLlM', []);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SetupIntentRetrieveParams params = SetupIntentRetrieveParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
SetupIntent setupIntent =
  client.v1().setupIntents().retrieve("seti_1EzVO3HssDVaQm2PJjXHmLlM", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const setupIntent = await stripe.setupIntents.retrieve('seti_1EzVO3HssDVaQm2PJjXHmLlM');
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SetupIntentRetrieveParams{}
result, err := sc.V1SetupIntents.Retrieve(
  context.TODO(), "seti_1EzVO3HssDVaQm2PJjXHmLlM", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.SetupIntents;
SetupIntent setupIntent = service.Get("seti_1EzVO3HssDVaQm2PJjXHmLlM");
```

> If you’re requesting this information synchronously from the Stripe API (as opposed to handling webhooks), you can combine the previous step with this step by [expanding](https://docs.stripe.com/api/expanding_objects.md) the SetupIntent object in the request to the /v1/checkout/session endpoint. Doing this prevents you from having to make two network requests to access the newly created PaymentMethod ID.

## Charge the payment method later [Server-side]

If you didn’t create the Checkout Session with an existing customer, use the ID of the PaymentMethod to [attach](https://docs.stripe.com/api/payment_methods/attach.md) the *PaymentMethod* (PaymentMethods represent your customer's payment instruments, used with the Payment Intents or Setup Intents APIs) to a *Customer* (Customer objects represent customers of your business. They let you reuse payment methods and give you the ability to track multiple payments). After you attach the PaymentMethod to a customer, you can make an *off-session* (A payment is described as off-session if it occurs without the direct involvement of the customer, using previously-collected payment information) payment using a [PaymentIntent](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-payment_method):

- Set [customer](https://docs.stripe.com/api.md#create_payment_intent-customer) to the ID of the Customer and [payment_method](https://docs.stripe.com/api.md#create_payment_intent-payment_method) to the ID of the PaymentMethod.
- Set [off_session](https://docs.stripe.com/api/payment_intents/confirm.md#confirm_payment_intent-off_session) to `true` to indicate that the customer isn’t in your checkout flow during a payment attempt and can’t fulfill an authentication request made by a partner, such as a card issuer, bank, or other payment institution. If, during your checkout flow, a partner requests authentication, Stripe requests exemptions using customer information from a previous *on-session* (A payment is described as on-session if it occurs while the customer is actively in your checkout flow and able to authenticate the payment method) transaction. If the conditions for exemption aren’t met, the PaymentIntent might throw an error.
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

When a payment attempt fails, the request also fails with a 402 HTTP status code and the status of the PaymentIntent is *requires\_payment\_method* (This status appears as "requires_source" in API versions before 2019-02-11). Notify your customer to return to your application (for example, by sending an email or in-app notification) and direct your customer to a new Checkout Session to select another payment method.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=1099 \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success?session_id={CHECKOUT_SESSION_ID}"
```

```cli
stripe checkout sessions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=1099 \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --success-url="https://example.com/success?session_id={CHECKOUT_SESSION_ID}"
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
  success_url: 'https://example.com/success?session_id={CHECKOUT_SESSION_ID}',
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
  "success_url": "https://example.com/success?session_id={CHECKOUT_SESSION_ID}",
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
  'success_url' => 'https://example.com/success?session_id={CHECKOUT_SESSION_ID}',
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
    .setSuccessUrl("https://example.com/success?session_id={CHECKOUT_SESSION_ID}")
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
  success_url: 'https://example.com/success?session_id={CHECKOUT_SESSION_ID}',
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
  SuccessURL: stripe.String("https://example.com/success?session_id={CHECKOUT_SESSION_ID}"),
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
    SuccessUrl = "https://example.com/success?session_id={CHECKOUT_SESSION_ID}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```


# Embedded form

> This is a Embedded form for when payment-ui is embedded-form. View the full page at https://docs.stripe.com/payments/checkout/save-and-reuse?payment-ui=embedded-form.

To collect customer payment details that you can reuse later, use Checkout’s setup mode. Setup mode uses the [Setup Intents API](https://docs.stripe.com/api/setup_intents.md) to create [Payment Methods](https://docs.stripe.com/api/payment_methods.md).

## Set up Stripe [Server-side]

First, you need a Stripe account. [Register now](https://dashboard.stripe.com/register).

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

## Create a Checkout Session [Server-side]

From your server, create a *Checkout Session* (A Checkout Session represents your customer's session as they pay for one-time purchases or subscriptions through Checkout. After a successful payment, the Checkout Session contains a reference to the Customer, and either the successful PaymentIntent or an active Subscription) and set the [ui_mode](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-ui_mode) to `embedded`. To create a setup mode Checkout Session, set the [mode](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-mode) to `setup`.

To return customers to a custom page that you host on your website, specify that page’s URL in the [return_url](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-return_url) parameter. Include the `{CHECKOUT_SESSION_ID}` template variable in the URL to retrieve the session’s status on the return page. Checkout automatically substitutes the variable with the Checkout Session ID before redirecting.

Read more about [configuring the return page](https://docs.stripe.com/payments/accept-a-payment.md?platform=web&ui=embedded-form#return-page) and other options for [customizing redirect behavior](https://docs.stripe.com/payments/checkout/custom-success-page.md?payment-ui=embedded-form).

You can optionally specify the [customer parameter](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer) to automatically attach the created payment method to an existing customer.

After you create the Checkout Session, use the `client_secret` returned in the response to [mount Checkout](https://docs.stripe.com/payments/checkout/save-and-reuse.md#mount-checkout).

#### Ruby

```ruby
# This example sets up an endpoint using the Sinatra framework.
# To learn more about Sinatra, watch this video: https://youtu.be/8aA9Enb8NVc.
require 'json'
require 'sinatra'
require 'stripe'

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

post '/create-checkout-session' do
  session = Stripe::Checkout::Session.create({
    currency: 'usd',
    mode: 'setup',
    ui_mode: 'embedded',
    return_url: 'https://example.com/return?session_id={CHECKOUT_SESSION_ID}'
  })

  {clientSecret: session.client_secret}.to_json
end
```

#### Python

```python
# This example sets up an endpoint using the Flask framework.
# To learn more about Flask, watch this video: https://youtu.be/7Ul1vfsmsDck.
import os
import stripe
from flask import Flask, redirect

app = Flask(__name__)

stripe.api_key = '<<YOUR_SECRET_KEY>>'

@app.route('/create-checkout-session', methods=['POST'])
def create_checkout_session():
  session = stripe.checkout.Session.create(
    currency = 'usd',
    mode = 'setup',
    ui_mode = 'embedded',
    return_url = 'https://example.com/return?session_id={CHECKOUT_SESSION_ID}',
  )

  return jsonify(clientSecret=session.client_secret)

if __name__ == '__main__':
  app.run(port=4242)
```

#### PHP

```php
<?php

require 'vendor/autoload.php';

$stripe = new \Stripe\StripeClient([
  "api_key" => '<<YOUR_SECRET_KEY>>'
]);

$checkout_session = $stripe->checkout->sessions->create([
  'currency' => 'usd',
  'mode' => 'setup',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return?session_id={CHECKOUT_SESSION_ID}',
]);

  echo json_encode(array('clientSecret' => $checkout_session->client_secret));
?>
```

#### Java

```java

import java.util.HashMap;
import java.util.Map;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.staticFiles;
import com.google.gson.Gson;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class Server {

  public static void main(String[] args) {
    port(4242);
    Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

    Gson gson = new Gson();

    post("/create-checkout-session", (request, response) -> {

      SessionCreateParams params =
        SessionCreateParams.builder()
          .setCurrency("usd")
          .setMode(SessionCreateParams.Mode.SETUP)
          .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
          .setReturnUrl("https://example.com/return?session_id={CHECKOUT_SESSION_ID}")
          .build();

      Session session = Session.create(params);

      Map<String, String> map = new HashMap();
      map.put("clientSecret", session.getRawJsonObject().getAsJsonPrimitive("client_secret").getAsString());

      return map;
    }, gson::toJson);
  }
}
```

#### Node.js

```javascript
// This example sets up an endpoint using the Express framework.
const express = require('express');
const app = express();

const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

app.post('/create-checkout-session', async (req, res) => {
  const session = await stripe.checkout.sessions.create({
    currency: 'usd',
    mode: 'setup',
    ui_mode: 'embedded',
    return_url: 'https://example.com/return?session_id={CHECKOUT_SESSION_ID}'
  });

  res.send({clientSecret: session.client_secret});
});

app.listen(4242, () => console.log(`Listening on port ${4242}!`));
```

#### Go

```go

package main

import (
  "net/http"

  "github.com/labstack/echo"
  "github.com/labstack/echo/middleware"
  "github.com/stripe/stripe-go/v76.0.0"
  "github.com/stripe/stripe-go/v76.0.0/checkout/session"
)

// This example sets up an endpoint using the Echo framework.
// To learn more about Echo, watch this video: https://youtu.be/ePmEVBu8w6Y.

func main() {
  stripe.Key = "<<YOUR_SECRET_KEY>>"

  e := echo.New()
  e.Use(middleware.Logger())
  e.Use(middleware.Recover())

  e.POST("/create-checkout-session", createCheckoutSession)

  e.Logger.Fatal(e.Start("localhost:4242"))
}

type CheckoutData struct {
  ClientSecret string `json:"clientSecret"`
}

func createCheckoutSession(c echo.Context) (err error) {
  params := &stripe.CheckoutSessionParams{
    Currency: stripe.String(string(stripe.CurrencyUSD)),
    Mode: stripe.String(string(stripe.CheckoutSessionModeSetup)),
    UIMode: stripe.String("embedded"),
    ReturnURL: stripe.String("https://example.com/return?session_id={CHECKOUT_SESSION_ID}"),
  }

  s, _ := session.New(params)

  if err != nil {
    return err
  }

  data := CheckoutData{
    ClientSecret: s.ClientSecret,
  }

  return c.JSON(http.StatusOK, data)
}
```

#### .NET

```dotnet
// This example sets up an endpoint using the ASP.NET MVC framework.
// To learn more about ASP.NET MVC, watch this video: https://youtu.be/2-mMOB8MhmE.

using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Stripe;
using Stripe.Checkout;

namespace server.Controllers
{
  public class PaymentsController : Controller
  {
    public PaymentsController()
    {
      StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";
    }

    [HttpPost("create-checkout-session")]
    public ActionResult CreateCheckoutSession()
    {
      var options = new SessionCreateOptions
      {
        Currency = "usd",
        Mode = "setup",
        UiMode = "embedded",
        ReturnUrl = "https://example.com/return?session_id={CHECKOUT_SESSION_ID}",
      };

      var service = new SessionService();
      Session session = service.Create(options);

      return Json(new {clientSecret = session.ClientSecret});
    }
  }
}
```

### Payment methods

By default, Stripe enables cards and other common payment methods. You can turn individual payment methods on or off in the [Stripe Dashboard](https://dashboard.stripe.com/settings/payment_methods). In Checkout, Stripe evaluates the currency and any restrictions, then dynamically presents the supported payment methods to the customer.

To see how your payment methods appear to customers, enter a transaction ID or set an order amount and currency in the Dashboard.

You can enable Apple Pay and Google Pay in your [payment methods settings](https://dashboard.stripe.com/settings/payment_methods). By default, Apple Pay is enabled and Google Pay is disabled. However, in some cases Stripe filters them out even when they’re enabled. We filter Google Pay if you [enable automatic tax](https://docs.stripe.com/tax/checkout.md) without collecting a shipping address.

Checkout’s Stripe-hosted pages don’t need integration changes to enable Apple Pay or Google Pay. Stripe handles these payments the same way as other card payments.

## Mount Checkout [Client-side]

#### HTML + JS

Checkout is available as part of [Stripe.js](https://docs.stripe.com/js.md). Include the Stripe.js script on your page by adding it to the head of your HTML file. Next, create an empty DOM node (container) to use for mounting.

```html
<head>
  <script src="https://js.stripe.com/clover/stripe.js"></script>
</head>
<body>
  <div id="checkout">
    <!-- Checkout will insert the payment form here -->
  </div>
</body>
```

Initialize Stripe.js with your publishable API key.

Create an asynchronous `fetchClientSecret` function that makes a request to your server to create the Checkout Session and retrieve the client secret. Pass this function into `options` when you create the Checkout instance:

```javascript
// Initialize Stripe.js
const stripe = Stripe('<<YOUR_PUBLISHABLE_KEY>>');

initialize();

// Fetch Checkout Session and retrieve the client secret
async function initialize() {
  const fetchClientSecret = async () => {
    const response = await fetch("/create-checkout-session", {
      method: "POST",
    });
    const { clientSecret } = await response.json();
    return clientSecret;
  };

  // Initialize Checkout
  const checkout = await stripe.initEmbeddedCheckout({
    fetchClientSecret,
  });

  // Mount Checkout
  checkout.mount('#checkout');
}
```

#### React

Install [react-stripe-js](https://docs.stripe.com/sdks/stripejs-react.md) and the Stripe.js loader from npm:

```bash
npm install --save @stripe/react-stripe-js @stripe/stripe-js
```

To use the Embedded Checkout component, create an `EmbeddedCheckoutProvider`. Call `loadStripe` with your publishable API key and pass the returned `Promise` to the provider.

Create an asynchronous `fetchClientSecret` function that makes a request to your server to create the Checkout Session and retrieve the client secret. Pass this function into the `options` prop accepted by the provider.

```jsx
import * as React from 'react';
import {loadStripe} from '@stripe/stripe-js';
import {
  EmbeddedCheckoutProvider,
  EmbeddedCheckout
} from '@stripe/react-stripe-js';

// Make sure to call `loadStripe` outside of a component’s render to avoid
// recreating the `Stripe` object on every render.
const stripePromise = loadStripe('pk_test_123');

const App = () => {
  const fetchClientSecret = useCallback(() => {
    // Create a Checkout Session
    return fetch("/create-checkout-session", {
      method: "POST",
    })
      .then((res) => res.json())
      .then((data) => data.clientSecret);
  }, []);

  const options = {fetchClientSecret};

  return (
    <div id="checkout">
      <EmbeddedCheckoutProvider
        stripe={stripePromise}
        options={options}
      >
        <EmbeddedCheckout />
      </EmbeddedCheckoutProvider>
    </div>
  )
}
```

Checkout renders in an iframe that securely sends payment information to Stripe over an HTTPS connection.

> Avoid placing Checkout within another iframe because some payment methods require redirecting to another page for payment confirmation.

### Customize appearance

Customize Checkout to match the design of your site by setting the background color, button color, border radius, and fonts in your account’s [branding settings](https://dashboard.stripe.com/settings/branding).

By default, Checkout renders with no external padding or margin. We recommend using a container element such as a div to apply your desired margin (for example, 16px on all sides).

## Retrieve the Checkout Session [Server-side]

After a customer successfully completes their Checkout Session, you need to retrieve the Session object. There are two ways to do this:

- **Asynchronously**: Handle `checkout.session.completed` *webhooks* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests), which contain a Session object. Learn more about [setting up webhooks](https://docs.stripe.com/webhooks.md).
- **Synchronously**: Obtain the Session ID from the `return_url` when a user redirects back to your site. Use the Session ID to [retrieve](https://docs.stripe.com/api/checkout/sessions/retrieve.md) the Session object.

```curl
curl https://api.stripe.com/v1/checkout/sessions/cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe checkout sessions retrieve cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.retrieve('cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.retrieve(
  "cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k",
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->retrieve(
  'cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k',
  []
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionRetrieveParams params = SessionRetrieveParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session =
  client.v1().checkout().sessions().retrieve(
    "cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k",
    params
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.retrieve(
  'cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k'
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionRetrieveParams{}
result, err := sc.V1CheckoutSessions.Retrieve(
  context.TODO(), "cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Get(
    "cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k");
```

The right choice depends on your tolerance for dropoff, as customers may not always reach the `return_url` after a successful payment. It’s possible for them close their browser tab before the redirect occurs. Handling webhooks prevents your integration from being susceptible to this form of dropoff.

After you have retrieved the Session object, get the value of the `setup_intent` key, which is the ID for the SetupIntent created during the Checkout Session. A [SetupIntent](https://docs.stripe.com/payments/setup-intents.md) is an object used to set up the customer’s bank account information for future payments.

Example `checkout.session.completed` payload:

```json
{
  "id": "evt_1Ep24XHssDVaQm2PpwS19Yt0",
  "object": "event",
  "api_version": "2019-03-14",
  "created": 1561420781,
  "data": {
    "object": {
      "id": "cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k",
      "object": "checkout.session",
      "billing_address_collection": null,
      "client_reference_id": null,
      "customer": "",
      "customer_email": null,
      "display_items": [],
      "mode": "setup","setup_intent": "seti_1EzVO3HssDVaQm2PJjXHmLlM",
      "submit_type": null,
      "subscription": null,
      "success_url": "https://example.com/success"
    }
  },
  "livemode": false,
  "pending_webhooks": 1,
  "request": {
    "id": null,
    "idempotency_key": null
  },
  "type": "checkout.session.completed"
}
```

Note the `setup_intent` ID for the next step.

## Retrieve the SetupIntent [Server-side]

Using the SetupIntent ID, [retrieve](https://docs.stripe.com/api/setup_intents/retrieve.md) the SetupIntent object. The returned object contains a `payment_method` ID that you can attach to a customer in the next step.

```curl
curl https://api.stripe.com/v1/setup_intents/seti_1EzVO3HssDVaQm2PJjXHmLlM \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe setup_intents retrieve seti_1EzVO3HssDVaQm2PJjXHmLlM
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

setup_intent = client.v1.setup_intents.retrieve('seti_1EzVO3HssDVaQm2PJjXHmLlM')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
setup_intent = client.v1.setup_intents.retrieve("seti_1EzVO3HssDVaQm2PJjXHmLlM")
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$setupIntent = $stripe->setupIntents->retrieve('seti_1EzVO3HssDVaQm2PJjXHmLlM', []);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SetupIntentRetrieveParams params = SetupIntentRetrieveParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
SetupIntent setupIntent =
  client.v1().setupIntents().retrieve("seti_1EzVO3HssDVaQm2PJjXHmLlM", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const setupIntent = await stripe.setupIntents.retrieve('seti_1EzVO3HssDVaQm2PJjXHmLlM');
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SetupIntentRetrieveParams{}
result, err := sc.V1SetupIntents.Retrieve(
  context.TODO(), "seti_1EzVO3HssDVaQm2PJjXHmLlM", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.SetupIntents;
SetupIntent setupIntent = service.Get("seti_1EzVO3HssDVaQm2PJjXHmLlM");
```

> If you’re requesting this information synchronously from the Stripe API (as opposed to handling webhooks), you can combine the previous step with this step by [expanding](https://docs.stripe.com/api/expanding_objects.md) the SetupIntent object in the request to the /v1/checkout/session endpoint. Doing this prevents you from having to make two network requests to access the newly created PaymentMethod ID.

## Charge the payment method later [Server-side]

If you didn’t create the Checkout Session with an existing customer, use the ID of the PaymentMethod to [attach](https://docs.stripe.com/api/payment_methods/attach.md) the *PaymentMethod* (PaymentMethods represent your customer's payment instruments, used with the Payment Intents or Setup Intents APIs) to a *Customer* (Customer objects represent customers of your business. They let you reuse payment methods and give you the ability to track multiple payments). After you attach the PaymentMethod to a customer, you can make an *off-session* (A payment is described as off-session if it occurs without the direct involvement of the customer, using previously-collected payment information) payment using a [PaymentIntent](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-payment_method):

- Set [customer](https://docs.stripe.com/api.md#create_payment_intent-customer) to the ID of the Customer and [payment_method](https://docs.stripe.com/api.md#create_payment_intent-payment_method) to the ID of the PaymentMethod.
- Set [off_session](https://docs.stripe.com/api/payment_intents/confirm.md#confirm_payment_intent-off_session) to `true` to indicate that the customer isn’t in your checkout flow during a payment attempt and can’t fulfill an authentication request made by a partner, such as a card issuer, bank, or other payment institution. If, during your checkout flow, a partner requests authentication, Stripe requests exemptions using customer information from a previous *on-session* (A payment is described as on-session if it occurs while the customer is actively in your checkout flow and able to authenticate the payment method) transaction. If the conditions for exemption aren’t met, the PaymentIntent might throw an error.
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

When a payment attempt fails, the request also fails with a 402 HTTP status code and the status of the PaymentIntent is *requires\_payment\_method* (This status appears as "requires_source" in API versions before 2019-02-11). Notify your customer to return to your application (for example, by sending an email or in-app notification) and direct your customer to a new Checkout Session to select another payment method.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=1099 \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return?session_id={CHECKOUT_SESSION_ID}"
```

```cli
stripe checkout sessions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=1099 \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return?session_id={CHECKOUT_SESSION_ID}"
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
  ui_mode: 'embedded',
  return_url: 'https://example.com/return?session_id={CHECKOUT_SESSION_ID}',
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
  "ui_mode": "embedded",
  "return_url": "https://example.com/return?session_id={CHECKOUT_SESSION_ID}",
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
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return?session_id={CHECKOUT_SESSION_ID}',
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
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return?session_id={CHECKOUT_SESSION_ID}")
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
  ui_mode: 'embedded',
  return_url: 'https://example.com/return?session_id={CHECKOUT_SESSION_ID}',
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
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return?session_id={CHECKOUT_SESSION_ID}"),
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
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return?session_id={CHECKOUT_SESSION_ID}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

