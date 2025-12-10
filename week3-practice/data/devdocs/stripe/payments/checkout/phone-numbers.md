# Collect customer phone numbers

Collect a phone number for shipping or invoicing when your customer makes a payment.

You can enable phone number collection on all `payment` and `subscription` [mode](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-mode) Sessions (phone number collection isn’t supported in `setup` mode). Only collect phone numbers if you need them for the transaction.

## Enable phone number collection

To enable phone number collection, set [phone_number_collection[enabled]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-phone_number_collection-enabled) to `true` when creating a Checkout Session.

#### Stripe-hosted page

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "phone_number_collection[enabled]"=true \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "phone_number_collection[enabled]"=true \
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
      price_data: {
        unit_amount: 1000,
        product_data: {name: 'T-shirt'},
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  phone_number_collection: {enabled: true},
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
  "line_items": [
    {
      "price_data": {
        "unit_amount": 1000,
        "product_data": {"name": "T-shirt"},
        "currency": "eur",
      },
      "quantity": 2,
    },
  ],
  "phone_number_collection": {"enabled": True},
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
      'price_data' => [
        'unit_amount' => 1000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'eur',
      ],
      'quantity' => 2,
    ],
  ],
  'phone_number_collection' => ['enabled' => true],
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
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(1000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("eur")
            .build()
        )
        .setQuantity(2L)
        .build()
    )
    .setPhoneNumberCollection(
      SessionCreateParams.PhoneNumberCollection.builder().setEnabled(true).build()
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
      price_data: {
        unit_amount: 1000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  phone_number_collection: {
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
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(1000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyEUR),
      },
      Quantity: stripe.Int64(2),
    },
  },
  PhoneNumberCollection: &stripe.CheckoutSessionCreatePhoneNumberCollectionParams{
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
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 1000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "eur",
            },
            Quantity = 2,
        },
    },
    PhoneNumberCollection = new Stripe.Checkout.SessionPhoneNumberCollectionOptions
    {
        Enabled = true,
    },
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
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "phone_number_collection[enabled]"=true \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "phone_number_collection[enabled]"=true \
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
      price_data: {
        unit_amount: 1000,
        product_data: {name: 'T-shirt'},
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  phone_number_collection: {enabled: true},
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
  "line_items": [
    {
      "price_data": {
        "unit_amount": 1000,
        "product_data": {"name": "T-shirt"},
        "currency": "eur",
      },
      "quantity": 2,
    },
  ],
  "phone_number_collection": {"enabled": True},
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
      'price_data' => [
        'unit_amount' => 1000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'eur',
      ],
      'quantity' => 2,
    ],
  ],
  'phone_number_collection' => ['enabled' => true],
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
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(1000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("eur")
            .build()
        )
        .setQuantity(2L)
        .build()
    )
    .setPhoneNumberCollection(
      SessionCreateParams.PhoneNumberCollection.builder().setEnabled(true).build()
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
      price_data: {
        unit_amount: 1000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  phone_number_collection: {
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
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(1000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyEUR),
      },
      Quantity: stripe.Int64(2),
    },
  },
  PhoneNumberCollection: &stripe.CheckoutSessionCreatePhoneNumberCollectionParams{
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
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 1000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "eur",
            },
            Quantity = 2,
        },
    },
    PhoneNumberCollection = new Stripe.Checkout.SessionPhoneNumberCollectionOptions
    {
        Enabled = true,
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

With phone number collection enabled, Checkout adds a *required* phone number field to the payment form. If you’re collecting a shipping address, the phone number field displays under the address fields. Otherwise, Checkout displays the phone number field below the email input. Customers can only enter one phone number per session.

## Retrieve the phone number

When your customer checks out with third-party wallets, such as [Apple Pay](https://docs.stripe.com/apple-pay.md) or [Google Pay](https://docs.stripe.com/google-pay.md), the phone number format isn’t guaranteed because of limitations on those platforms. We return the phone number value that’s provided by the third-party wallet.

We guarantee phone numbers in the [E.164](https://en.wikipedia.org/wiki/E.164) format when a customer doesn’t use [wallet payments](https://docs.stripe.com/payments/wallets.md).

After the session, you can retrieve customer phone numbers from the resulting *Customer* (Customer objects represent customers of your business. They let you reuse payment methods and give you the ability to track multiple payments), or *Checkout Session* (A Checkout Session represents your customer's session as they pay for one-time purchases or subscriptions through Checkout. After a successful payment, the Checkout Session contains a reference to the Customer, and either the successful PaymentIntent or an active Subscription) objects:

- [On the Customer](https://docs.stripe.com/api/customers.md): Checkout saves collected phone numbers onto the [phone](https://docs.stripe.com/api/customers/object.md#customer_object-phone) property of the Customer object, which you can access programmatically by either fetching the Customer object directly with the [API](https://docs.stripe.com/api/customers/retrieve.md), or by listening for the [customer.created](https://docs.stripe.com/api/events/types.md#event_types-customer.created) event in a *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests). You can also view the customer’s phone number in the [dashboard](https://dashboard.stripe.com/customers).
- [On the Checkout Session](https://docs.stripe.com/api/checkout/sessions.md): The customer’s phone number is also saved in the [customer_details](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-customer_details) hash of the Checkout Session object, under [customer_details.phone](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-customer_details-phone). After each successful Checkout Session, Stripe emits the [checkout.session.completed](https://docs.stripe.com/api/events/types.md#event_types-checkout.session.completed) event containing the Checkout Session object (and phone number), which you can listen for in a *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests).

## Collect phone numbers for existing customers

Passing in an existing [Customer](https://docs.stripe.com/api/customers.md) with a populated [phone](https://docs.stripe.com/api/customers/object.md#customer_object-phone) property to the [Checkout Session](https://docs.stripe.com/api/checkout/sessions.md) results in the phone number field being prefilled.

If the customer updates their phone number, this updated value persists on the [phone](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-phone) property on the [Customer object](https://docs.stripe.com/api/customers.md) , overwriting any previously saved phone number.

### Update phone numbers with the customer portal 

You can allow customers to manage their own accounts (which includes [updating their phone numbers](https://docs.stripe.com/api/customer_portal/configurations/create.md#create_portal_configuration-features-customer_update-allowed_updates)) in the customer portal.

## See also

- [Integrate the customer portal](https://docs.stripe.com/customer-management.md)
