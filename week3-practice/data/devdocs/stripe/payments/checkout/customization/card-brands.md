# Customize card brands

Customize the card brands that Checkout displays.

When you use a Stripe-hosted payment page or embedded payment form, you can customize the card brands you want to display to your customers.

To block specific card brands, include the `brands_blocked` parameter when you create a Checkout Session. Pass an array with any of the following card brand values:

- `visa`
- `mastercard`
- `american_express`
- `discover_global_network`

The `discover_global_network` value encompasses all of the cards that are part of the Discover Global Network, including Discover, Diners, JCB, UnionPay, and Elo.

The following code example initializes the Checkout Session with the `brands_blocked` parameter set to `['american_express']`, which prevents customers from using American Express cards.

#### Stripe-hosted page

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success" \
  -d "payment_method_options[card][restrictions][brands_blocked][0]"=american_express
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --success-url="https://example.com/success" \
  -d "payment_method_options[card][restrictions][brands_blocked][0]"=american_express
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
  payment_method_options: {card: {restrictions: {brands_blocked: ['american_express']}}},
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
  "payment_method_options": {
    "card": {"restrictions": {"brands_blocked": ["american_express"]}},
  },
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
  'payment_method_options' => [
    'card' => ['restrictions' => ['brands_blocked' => ['american_express']]],
  ],
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
    .setPaymentMethodOptions(
      SessionCreateParams.PaymentMethodOptions.builder()
        .setCard(
          SessionCreateParams.PaymentMethodOptions.Card.builder()
            .setRestrictions(
              SessionCreateParams.PaymentMethodOptions.Card.Restrictions.builder()
                .addBrandsBlocked(
                  SessionCreateParams.PaymentMethodOptions.Card.Restrictions.BrandsBlocked.AMERICAN_EXPRESS
                )
                .build()
            )
            .build()
        )
        .build()
    )
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
  payment_method_options: {
    card: {
      restrictions: {
        brands_blocked: ['american_express'],
      },
    },
  },
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
  PaymentMethodOptions: &stripe.CheckoutSessionCreatePaymentMethodOptionsParams{
    Card: &stripe.CheckoutSessionCreatePaymentMethodOptionsCardParams{
      Restrictions: &stripe.CheckoutSessionCreatePaymentMethodOptionsCardRestrictionsParams{
        BrandsBlocked: []*string{
          stripe.String(stripe.CheckoutSessionPaymentMethodOptionsCardRestrictionsBrandsBlockedAmericanExpress),
        },
      },
    },
  },
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
    PaymentMethodOptions = new Stripe.Checkout.SessionPaymentMethodOptionsOptions
    {
        Card = new Stripe.Checkout.SessionPaymentMethodOptionsCardOptions
        {
            Restrictions = new Stripe.Checkout.SessionPaymentMethodOptionsCardRestrictionsOptions
            {
                BrandsBlocked = new List<string> { "american_express" },
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

#### Embedded form

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  --data-urlencode return_url="https://example.com/return" \
  -d ui_mode=embedded \
  -d "payment_method_options[card][restrictions][brands_blocked][0]"=american_express
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --return-url="https://example.com/return" \
  --ui-mode=embedded \
  -d "payment_method_options[card][restrictions][brands_blocked][0]"=american_express
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
  return_url: 'https://example.com/return',
  ui_mode: 'embedded',
  payment_method_options: {card: {restrictions: {brands_blocked: ['american_express']}}},
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
  "return_url": "https://example.com/return",
  "ui_mode": "embedded",
  "payment_method_options": {
    "card": {"restrictions": {"brands_blocked": ["american_express"]}},
  },
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
  'return_url' => 'https://example.com/return',
  'ui_mode' => 'embedded',
  'payment_method_options' => [
    'card' => ['restrictions' => ['brands_blocked' => ['american_express']]],
  ],
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
    .setReturnUrl("https://example.com/return")
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setPaymentMethodOptions(
      SessionCreateParams.PaymentMethodOptions.builder()
        .setCard(
          SessionCreateParams.PaymentMethodOptions.Card.builder()
            .setRestrictions(
              SessionCreateParams.PaymentMethodOptions.Card.Restrictions.builder()
                .addBrandsBlocked(
                  SessionCreateParams.PaymentMethodOptions.Card.Restrictions.BrandsBlocked.AMERICAN_EXPRESS
                )
                .build()
            )
            .build()
        )
        .build()
    )
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
  return_url: 'https://example.com/return',
  ui_mode: 'embedded',
  payment_method_options: {
    card: {
      restrictions: {
        brands_blocked: ['american_express'],
      },
    },
  },
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
  ReturnURL: stripe.String("https://example.com/return"),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  PaymentMethodOptions: &stripe.CheckoutSessionCreatePaymentMethodOptionsParams{
    Card: &stripe.CheckoutSessionCreatePaymentMethodOptionsCardParams{
      Restrictions: &stripe.CheckoutSessionCreatePaymentMethodOptionsCardRestrictionsParams{
        BrandsBlocked: []*string{
          stripe.String(stripe.CheckoutSessionPaymentMethodOptionsCardRestrictionsBrandsBlockedAmericanExpress),
        },
      },
    },
  },
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
    ReturnUrl = "https://example.com/return",
    UiMode = "embedded",
    PaymentMethodOptions = new Stripe.Checkout.SessionPaymentMethodOptionsOptions
    {
        Card = new Stripe.Checkout.SessionPaymentMethodOptionsCardOptions
        {
            Restrictions = new Stripe.Checkout.SessionPaymentMethodOptionsCardRestrictionsOptions
            {
                BrandsBlocked = new List<string> { "american_express" },
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

If a customer enters an unsupported card number in Checkout, an error message notifies them that their card brand isn’t accepted.
![Card brand filtering on Checkout](https://b.stripecdn.com/docs-statics-srv/assets/card-brand-filtering-on-form.e3a1bab1800020eefd977e093863d208.png)

An error surfaces informing the customer that you don’t accept Visa (or whatever card brand you have blocked).

Additionally, [Link](https://docs.stripe.com/payments/link/checkout-link.md) also disables saved cards for returning customers if the saved card is blocked.
![Card brand filtering on Checkout with Link](https://b.stripecdn.com/docs-statics-srv/assets/card-brand-filtering-link.eb5ed48829c0b18a59dadf2a77cd6a66.png)

If a Link user’s saved card is blocked, it is disabled.

Checkout also filters cards in Apple and Google Pay wallets, customer’s [saved payment methods](https://docs.stripe.com/payments/checkout/save-during-payment.md), and [networks from co-badged cards](https://docs.stripe.com/co-badged-cards-compliance.md).
