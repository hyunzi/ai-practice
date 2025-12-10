# No-cost orders

Accept orders for no-cost line items or apply 100% off discounts for one-time payments.

# Stripe-hosted page

> This is a Stripe-hosted page for when payment-ui is stripe-hosted. View the full page at https://docs.stripe.com/payments/checkout/no-cost-orders?payment-ui=stripe-hosted.

You can process no-cost orders for one-time payments with [no-cost line items](https://docs.stripe.com/payments/checkout/no-cost-orders.md#no-cost-line-items) or discounts for 100% off with [coupons and customer-facing promotion codes](https://docs.stripe.com/payments/checkout/no-cost-orders.md#full-cost-discounts).

> You must be on API version [2023-08-16](https://docs.stripe.com/upgrades.md#2023-08-16) or later to process no-cost orders using the Checkout Sessions API.

## Create a Checkout Session with no-cost line items 

Create a [Price](https://docs.stripe.com/api/prices.md) with a [unit_amount](https://docs.stripe.com/api/prices/object.md#price_object-unit_amount) of 0, and pass it into the [line items](https://docs.stripe.com/api/checkout/sessions/line_items.md) array of the Checkout Session. See [Products and prices](https://docs.stripe.com/invoicing/products-prices.md) for more information on creating prices.

You can also use the [price_data](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-line_items-price_data) parameter of the `line_items` array to pass in a free price.

If the total amount is 0, Checkout doesn’t collect a payment method from the customer.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price_data][unit_amount]"=0 \
  -d "line_items[0][price_data][product_data][name]"="Free t-shirt" \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=0 \
  -d "line_items[0][price_data][product_data][name]"="Free t-shirt" \
  -d "line_items[0][price_data][currency]"=usd \
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
      price_data: {
        unit_amount: 0,
        product_data: {name: 'Free t-shirt'},
        currency: 'usd',
      },
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
  "line_items": [
    {
      "price_data": {
        "unit_amount": 0,
        "product_data": {"name": "Free t-shirt"},
        "currency": "usd",
      },
      "quantity": 1,
    },
  ],
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
        'unit_amount' => 0,
        'product_data' => ['name' => 'Free t-shirt'],
        'currency' => 'usd',
      ],
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
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(0L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("Free t-shirt")
                .build()
            )
            .setCurrency("usd")
            .build()
        )
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
      price_data: {
        unit_amount: 0,
        product_data: {
          name: 'Free t-shirt',
        },
        currency: 'usd',
      },
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
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(0),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("Free t-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyUSD),
      },
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
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 0,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "Free t-shirt",
                },
                Currency = "usd",
            },
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

If the `customer` property isn’t set, the Checkout Session automatically creates a new Customer object. This means [guest customers](https://docs.stripe.com/payments/checkout/guest-customers.md) aren’t supported.

## Create a discount 

Alternatively, create a coupon and a promotion code to allow customers to complete orders for free.

### Create a coupon

Create a [Coupon](https://docs.stripe.com/api/coupons.md) that makes your Checkout Session free. For example, you can create a 100% off coupon.

```curl
curl https://api.stripe.com/v1/coupons \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d percent_off=100 \
  -d duration=once
```

```cli
stripe coupons create  \
  --percent-off=100 \
  --duration=once
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

coupon = client.v1.coupons.create({
  percent_off: 100,
  duration: 'once',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
coupon = client.v1.coupons.create({"percent_off": 100, "duration": "once"})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$coupon = $stripe->coupons->create([
  'percent_off' => 100,
  'duration' => 'once',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CouponCreateParams params =
  CouponCreateParams.builder()
    .setPercentOff(new BigDecimal(100))
    .setDuration(CouponCreateParams.Duration.ONCE)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Coupon coupon = client.v1().coupons().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const coupon = await stripe.coupons.create({
  percent_off: 100,
  duration: 'once',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CouponCreateParams{
  PercentOff: stripe.Float64(100),
  Duration: stripe.String(stripe.CouponDurationOnce),
}
result, err := sc.V1Coupons.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CouponCreateOptions { PercentOff = 100M, Duration = "once" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Coupons;
Coupon coupon = service.Create(options);
```

To create a session with an applied discount, pass the [coupon ID](https://docs.stripe.com/api/coupons/object.md#coupon_object-id) in the `coupon` parameter of the [discounts](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-discounts) array.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][quantity]"=1 \
  -d "discounts[0][coupon]"="{{COUPON_ID}}" \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][quantity]"=1 \
  -d "discounts[0][coupon]"="{{COUPON_ID}}" \
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
        unit_amount: 2000,
        product_data: {name: 'T-shirt'},
        currency: 'usd',
      },
      quantity: 1,
    },
  ],
  discounts: [{coupon: '{{COUPON_ID}}'}],
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
        "unit_amount": 2000,
        "product_data": {"name": "T-shirt"},
        "currency": "usd",
      },
      "quantity": 1,
    },
  ],
  "discounts": [{"coupon": "{{COUPON_ID}}"}],
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
        'unit_amount' => 2000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'usd',
      ],
      'quantity' => 1,
    ],
  ],
  'discounts' => [['coupon' => '{{COUPON_ID}}']],
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
            .setUnitAmount(2000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("usd")
            .build()
        )
        .setQuantity(1L)
        .build()
    )
    .addDiscount(
      SessionCreateParams.Discount.builder().setCoupon("{{COUPON_ID}}").build()
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
        unit_amount: 2000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'usd',
      },
      quantity: 1,
    },
  ],
  discounts: [
    {
      coupon: '{{COUPON_ID}}',
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
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(2000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyUSD),
      },
      Quantity: stripe.Int64(1),
    },
  },
  Discounts: []*stripe.CheckoutSessionCreateDiscountParams{
    &stripe.CheckoutSessionCreateDiscountParams{
      Coupon: stripe.String("{{COUPON_ID}}"),
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
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 2000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "usd",
            },
            Quantity = 1,
        },
    },
    Discounts = new List<Stripe.Checkout.SessionDiscountOptions>
    {
        new Stripe.Checkout.SessionDiscountOptions { Coupon = "{{COUPON_ID}}" },
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

You can also create a free Checkout Session by applying a coupon for an amount equal to or exceeding the Checkout Session total.

### Create a promotion code

Promotion codes are customer-facing codes created on top of coupons. You can share these codes with customers who can enter them into Checkout to apply a discount. Create a promotion code from a 100% off coupon to allow customers to create orders for free.

```curl
curl https://api.stripe.com/v1/promotion_codes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d coupon="{{COUPON_ID}}" \
  -d code=FREECODE
```

Enable user-redeemable promotion codes using the [allow_promotion_codes](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-allow_promotion_codes) parameter in a Checkout Session. This enables a field in Checkout to allow users to enter promotion codes.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  -d allow_promotion_codes=true \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --allow-promotion-codes=true \
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
        unit_amount: 2000,
        product_data: {name: 'T-shirt'},
        currency: 'usd',
      },
      quantity: 1,
    },
  ],
  mode: 'payment',
  allow_promotion_codes: true,
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
        "unit_amount": 2000,
        "product_data": {"name": "T-shirt"},
        "currency": "usd",
      },
      "quantity": 1,
    },
  ],
  "mode": "payment",
  "allow_promotion_codes": True,
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
        'unit_amount' => 2000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'usd',
      ],
      'quantity' => 1,
    ],
  ],
  'mode' => 'payment',
  'allow_promotion_codes' => true,
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
            .setUnitAmount(2000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("usd")
            .build()
        )
        .setQuantity(1L)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setAllowPromotionCodes(true)
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
        unit_amount: 2000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'usd',
      },
      quantity: 1,
    },
  ],
  mode: 'payment',
  allow_promotion_codes: true,
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
        UnitAmount: stripe.Int64(2000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyUSD),
      },
      Quantity: stripe.Int64(1),
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  AllowPromotionCodes: stripe.Bool(true),
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
                UnitAmount = 2000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "usd",
            },
            Quantity = 1,
        },
    },
    Mode = "payment",
    AllowPromotionCodes = true,
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

Customers can also check out for free if they apply a promotion code for an amount equal to or exceeding the Checkout Session total. For more ways to apply discounts, see [Add discounts](https://docs.stripe.com/payments/checkout/discounts.md).

## Handle completed orders 

After the Checkout Session completes, you can make a request for the finalized [line items](https://docs.stripe.com/api/checkout/sessions/line_items.md) and their quantities. If your customer removes a line item, it also removes it from the line items response. See the [Fulfillment guide](https://docs.stripe.com/checkout/fulfillment.md) to learn how to create an event handler to handle completed Checkout Sessions.

> To fulfill no-cost orders, make sure to handle the `checkout.session.completed` event rather than [PaymentIntent](https://docs.stripe.com/payments/payment-intents.md) events. Completed Checkout Sessions that are free won’t have an associated [PaymentIntent](https://docs.stripe.com/payments/payment-intents.md).

You can see your completed no-cost orders in the [Dashboard](https://dashboard.stripe.com/no-cost-orders). The no-cost orders tab only appears if you have at least one completed no-cost order.

## Optional: Payment links and pricing tables

Payment links and pricing tables support no-cost orders by default when your account is created after August 17, 2023. For accounts created before August 17, 2023, you can enable no-cost orders for your Payment links and pricing tables by visiting your [Checkout settings](https://dashboard.stripe.com/settings/checkout) in the Dashboard.

> When you enable this feature, it has a 3-day grace period in which you can turn it off. After 3 days, you can’t disable it. Before you enable it for your live payment links and pricing tables, test it with your fulfillment flow in a sandbox.

To use no-cost orders with a payment link in a sandbox, specify a `prefilled_email` [URL parameter](https://docs.stripe.com/payment-links/customize.md#customize-checkout-with-url-parameters) with an email whose local part includes the suffix `+no_cost_orders`. For example, `j.appleseed+no_cost_orders@example.com`. The resulting checkout session lets you apply a discount that reduces the order total to zero.

To use no-cost orders with a pricing table in a sandbox, set the pricing table’s `customer-email` [property](https://docs.stripe.com/payments/checkout/pricing-table.md#customer-email) to an email whose local part includes the suffix `+no_cost_orders`. For example, `j.appleseed+no_cost_orders@example.com`. The resulting checkout session lets you apply a discount that reduces the order total to zero.


# Embedded form

> This is a Embedded form for when payment-ui is embedded-form. View the full page at https://docs.stripe.com/payments/checkout/no-cost-orders?payment-ui=embedded-form.

You can process no-cost orders for one-time payments with [no-cost line items](https://docs.stripe.com/payments/checkout/no-cost-orders.md#no-cost-line-items) or discounts for 100% off with [coupons and customer-facing promotion codes](https://docs.stripe.com/payments/checkout/no-cost-orders.md#full-cost-discounts).

> You must be on API version [2023-08-16](https://docs.stripe.com/upgrades.md#2023-08-16) or later to process no-cost orders using the Checkout Sessions API.

## Create a Checkout Session with no-cost line items 

Create a [Price](https://docs.stripe.com/api/prices.md) with a [unit_amount](https://docs.stripe.com/api/prices/object.md#price_object-unit_amount) of 0, and pass it into the [line items](https://docs.stripe.com/api/checkout/sessions/line_items.md) array of the Checkout Session. See [Products and prices](https://docs.stripe.com/invoicing/products-prices.md) for more information on creating prices.

You can also use the [price_data](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-line_items-price_data) parameter of the `line_items` array to pass in a free price.

If the total amount is 0, Checkout doesn’t collect a payment method from the customer.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price_data][unit_amount]"=0 \
  -d "line_items[0][price_data][product_data][name]"="Free t-shirt" \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=0 \
  -d "line_items[0][price_data][product_data][name]"="Free t-shirt" \
  -d "line_items[0][price_data][currency]"=usd \
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
      price_data: {
        unit_amount: 0,
        product_data: {name: 'Free t-shirt'},
        currency: 'usd',
      },
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
  "line_items": [
    {
      "price_data": {
        "unit_amount": 0,
        "product_data": {"name": "Free t-shirt"},
        "currency": "usd",
      },
      "quantity": 1,
    },
  ],
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
        'unit_amount' => 0,
        'product_data' => ['name' => 'Free t-shirt'],
        'currency' => 'usd',
      ],
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
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(0L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("Free t-shirt")
                .build()
            )
            .setCurrency("usd")
            .build()
        )
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
      price_data: {
        unit_amount: 0,
        product_data: {
          name: 'Free t-shirt',
        },
        currency: 'usd',
      },
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
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(0),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("Free t-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyUSD),
      },
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
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 0,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "Free t-shirt",
                },
                Currency = "usd",
            },
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

If the `customer` property isn’t set, the Checkout Session automatically creates a new Customer object. This means [guest customers](https://docs.stripe.com/payments/checkout/guest-customers.md) aren’t supported.

## Create a discount 

Alternatively, create a coupon and a promotion code to allow customers to complete orders for free.

### Create a coupon

Create a [Coupon](https://docs.stripe.com/api/coupons.md) that makes your Checkout Session free. For example, you can create a 100% off coupon.

```curl
curl https://api.stripe.com/v1/coupons \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d percent_off=100 \
  -d duration=once
```

```cli
stripe coupons create  \
  --percent-off=100 \
  --duration=once
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

coupon = client.v1.coupons.create({
  percent_off: 100,
  duration: 'once',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
coupon = client.v1.coupons.create({"percent_off": 100, "duration": "once"})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$coupon = $stripe->coupons->create([
  'percent_off' => 100,
  'duration' => 'once',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CouponCreateParams params =
  CouponCreateParams.builder()
    .setPercentOff(new BigDecimal(100))
    .setDuration(CouponCreateParams.Duration.ONCE)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Coupon coupon = client.v1().coupons().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const coupon = await stripe.coupons.create({
  percent_off: 100,
  duration: 'once',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CouponCreateParams{
  PercentOff: stripe.Float64(100),
  Duration: stripe.String(stripe.CouponDurationOnce),
}
result, err := sc.V1Coupons.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CouponCreateOptions { PercentOff = 100M, Duration = "once" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Coupons;
Coupon coupon = service.Create(options);
```

To create a session with an applied discount, pass the [coupon ID](https://docs.stripe.com/api/coupons/object.md#coupon_object-id) in the `coupon` parameter of the [discounts](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-discounts) array.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][quantity]"=1 \
  -d "discounts[0][coupon]"="{{COUPON_ID}}" \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][quantity]"=1 \
  -d "discounts[0][coupon]"="{{COUPON_ID}}" \
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
        unit_amount: 2000,
        product_data: {name: 'T-shirt'},
        currency: 'usd',
      },
      quantity: 1,
    },
  ],
  discounts: [{coupon: '{{COUPON_ID}}'}],
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
        "unit_amount": 2000,
        "product_data": {"name": "T-shirt"},
        "currency": "usd",
      },
      "quantity": 1,
    },
  ],
  "discounts": [{"coupon": "{{COUPON_ID}}"}],
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
        'unit_amount' => 2000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'usd',
      ],
      'quantity' => 1,
    ],
  ],
  'discounts' => [['coupon' => '{{COUPON_ID}}']],
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
            .setUnitAmount(2000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("usd")
            .build()
        )
        .setQuantity(1L)
        .build()
    )
    .addDiscount(
      SessionCreateParams.Discount.builder().setCoupon("{{COUPON_ID}}").build()
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
        unit_amount: 2000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'usd',
      },
      quantity: 1,
    },
  ],
  discounts: [
    {
      coupon: '{{COUPON_ID}}',
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
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(2000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyUSD),
      },
      Quantity: stripe.Int64(1),
    },
  },
  Discounts: []*stripe.CheckoutSessionCreateDiscountParams{
    &stripe.CheckoutSessionCreateDiscountParams{
      Coupon: stripe.String("{{COUPON_ID}}"),
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
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 2000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "usd",
            },
            Quantity = 1,
        },
    },
    Discounts = new List<Stripe.Checkout.SessionDiscountOptions>
    {
        new Stripe.Checkout.SessionDiscountOptions { Coupon = "{{COUPON_ID}}" },
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

You can also create a free Checkout Session by applying a coupon for an amount equal to or exceeding the Checkout Session total.

### Create a promotion code

Promotion codes are customer-facing codes created on top of coupons. You can share these codes with customers who can enter them into Checkout to apply a discount. Create a promotion code from a 100% off coupon to allow customers to create orders for free.

```curl
curl https://api.stripe.com/v1/promotion_codes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d coupon="{{COUPON_ID}}" \
  -d code=FREECODE
```

Enable user-redeemable promotion codes using the [allow_promotion_codes](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-allow_promotion_codes) parameter in a Checkout Session. This enables a field in Checkout to allow users to enter promotion codes.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  -d allow_promotion_codes=true \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --allow-promotion-codes=true \
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
        unit_amount: 2000,
        product_data: {name: 'T-shirt'},
        currency: 'usd',
      },
      quantity: 1,
    },
  ],
  mode: 'payment',
  allow_promotion_codes: true,
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
        "unit_amount": 2000,
        "product_data": {"name": "T-shirt"},
        "currency": "usd",
      },
      "quantity": 1,
    },
  ],
  "mode": "payment",
  "allow_promotion_codes": True,
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
        'unit_amount' => 2000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'usd',
      ],
      'quantity' => 1,
    ],
  ],
  'mode' => 'payment',
  'allow_promotion_codes' => true,
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
            .setUnitAmount(2000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("usd")
            .build()
        )
        .setQuantity(1L)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setAllowPromotionCodes(true)
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
        unit_amount: 2000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'usd',
      },
      quantity: 1,
    },
  ],
  mode: 'payment',
  allow_promotion_codes: true,
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
        UnitAmount: stripe.Int64(2000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyUSD),
      },
      Quantity: stripe.Int64(1),
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  AllowPromotionCodes: stripe.Bool(true),
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
                UnitAmount = 2000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "usd",
            },
            Quantity = 1,
        },
    },
    Mode = "payment",
    AllowPromotionCodes = true,
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

Customers can also check out for free if they apply a promotion code for an amount equal to or exceeding the Checkout Session total. For more ways to apply discounts, see [Add discounts](https://docs.stripe.com/payments/checkout/discounts.md).

## Handle completed orders 

After the Checkout Session completes, you can make a request for the finalized [line items](https://docs.stripe.com/api/checkout/sessions/line_items.md) and their quantities. If your customer removes a line item, it also removes it from the line items response. See the [Fulfillment guide](https://docs.stripe.com/checkout/fulfillment.md) to learn how to create an event handler to handle completed Checkout Sessions.

> To fulfill no-cost orders, make sure to handle the `checkout.session.completed` event rather than [PaymentIntent](https://docs.stripe.com/payments/payment-intents.md) events. Completed Checkout Sessions that are free won’t have an associated [PaymentIntent](https://docs.stripe.com/payments/payment-intents.md).

You can see your completed no-cost orders in the [Dashboard](https://dashboard.stripe.com/no-cost-orders). The no-cost orders tab only appears if you have at least one completed no-cost order.

