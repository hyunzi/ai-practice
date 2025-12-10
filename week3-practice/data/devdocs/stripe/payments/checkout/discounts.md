# Add discounts

Reduce the amount charged to a customer by discounting their subtotal with coupons and promotion codes.

# Stripe-hosted page

> This is a Stripe-hosted page for when payment-ui is stripe-hosted. View the full page at https://docs.stripe.com/payments/checkout/discounts?payment-ui=stripe-hosted.

You can use discounts to reduce the amount charged to a customer. Coupons and promotion codes allow you to:

- Apply a discount to an entire purchase subtotal
- Apply a discount to specific products
- Reduce the total charged by a percentage or a flat amount
- Create customer-facing promotion codes on top of coupons to share directly with customers

> To use coupons for discounting *subscriptions* (A Subscription represents the product details associated with the plan that your customer subscribes to. Allows you to charge the customer on a recurring basis) with Checkout and Billing, see [Discounts for subscriptions](https://docs.stripe.com/billing/subscriptions/coupons.md).

## Create a coupon

Coupons specify a fixed value discount. You can create customer-facing promotion codes that map to a single underlying coupon. This means that the codes `FALLPROMO` and `SPRINGPROMO` can both point to one 25% off coupon. You can create coupons in the [Dashboard](https://dashboard.stripe.com/coupons) or with the [API](https://docs.stripe.com/api.md#coupons):

```curl
curl https://api.stripe.com/v1/coupons \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d percent_off=20 \
  -d duration=once
```

```cli
stripe coupons create  \
  --percent-off=20 \
  --duration=once
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

coupon = client.v1.coupons.create({
  percent_off: 20,
  duration: 'once',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
coupon = client.v1.coupons.create({"percent_off": 20, "duration": "once"})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$coupon = $stripe->coupons->create([
  'percent_off' => 20,
  'duration' => 'once',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CouponCreateParams params =
  CouponCreateParams.builder()
    .setPercentOff(new BigDecimal(20))
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
  percent_off: 20,
  duration: 'once',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CouponCreateParams{
  PercentOff: stripe.Float64(20),
  Duration: stripe.String(stripe.CouponDurationOnce),
}
result, err := sc.V1Coupons.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CouponCreateOptions { PercentOff = 20M, Duration = "once" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Coupons;
Coupon coupon = service.Create(options);
```

## Use a coupon

To create a session with an applied discount, pass the [coupon ID](https://docs.stripe.com/api/coupons/object.md#coupon_object-id) in the `coupon` parameter of the [discounts](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-discounts) array. Checkout currently supports up to one coupon or promotion code.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "discounts[0][coupon]"="{{COUPON_ID}}" \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
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
      price: '{{PRICE_ID}}',
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
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
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
      'price' => '{{PRICE_ID}}',
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
        .setPrice("{{PRICE_ID}}")
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
      price: '{{PRICE_ID}}',
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
      Price: stripe.String("{{PRICE_ID}}"),
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
            Price = "{{PRICE_ID}}",
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

## Configure a coupon

Coupons have the following parameters that you can use:

- `currency`
- `percent_off` or `amount_off`
- `max_redemptions`
- `redeem_by`, the latest date customers can apply the coupon
- `applies_to`, limits the products that the coupon applies to

> The coupon object adds discounts to both one-time payments and subscriptions. Some coupon object parameters, like `duration`, only apply to [subscriptions](https://docs.stripe.com/billing/subscriptions/coupons.md).

### Limit redemption usage

The `max_redemptions` and `redeem_by` values apply to the coupon across every application. For example, you can restrict a coupon to the first 50 usages of it, or you can make a coupon expire by a certain date.

### Limit eligible products

You can limit the products that are eligible for discounts using a coupon by adding the product IDs to the `applies_to` hash in the Coupon object. Any promotion codes that map to this coupon only apply to the list of eligible products.

### Delete a coupon

You can delete coupons in the Dashboard or the API. Deleting a coupon prevents it from being applied to future transactions or customers.

## Create a promotion code

Promotion codes are customer-facing codes created on top of coupons. You can also specify additional restrictions that control when a customer can apply the promotion. You can share these codes with customers who can enter them during checkout to apply a discount.

To create a [promotion code](https://docs.stripe.com/api/promotion_codes.md), specify an existing `coupon` and any restrictions (for example, limiting it to a specific `customer`). If you have a specific code to give to your customer (for example, `FALL25OFF`), set the `code`. If you leave this field blank, we’ll generate a random `code` for you.

The `code` is case-insensitive and unique across active promotion codes for any customer. For example:

- You can create multiple customer-restricted promotion codes with the same `code`, but you can’t reuse that `code` for a promotion code redeemable by any customer.
- If you create a promotion code that is redeemable by any customer, you can’t create another active promotion code with the same `code`.
- You can create a promotion code with `code: NEWUSER`, inactivate it by passing `active: false`, and then create a new promotion code with `code: NEWUSER`.

Promotion codes can be created in the coupons section of the [Dashboard](https://dashboard.stripe.com/coupons/create) or with the [API](https://docs.stripe.com/api.md#promotion_codes):

```curl
curl https://api.stripe.com/v1/promotion_codes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d coupon={{COUPON_ID}} \
  -d code=VIPCODE
```

## Use a promotion code 

Enable customer-redeemable promotion codes using the [allow_promotion_codes](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-allow_promotion_codes) parameter in a Checkout Session. This enables a field in Checkout to allow customers to input promotion codes.

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

## Configure a promotion code

For each promotion code, you can customize eligible customers, redemptions, and other limits.

### Limit by customer

To limit a promotion to a particular customer, specify a [customer](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-customer) when creating the promotion code. If no customer is specified, any customer can redeem the code.

### Limit by first-time order

You can also limit the promotion code to first-time customers with [restrictions.first_time_transaction](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-restrictions-first_time_transaction). If the `customer` isn’t defined, or if a defined `customer` has no prior payments or non-void *invoices* (Invoices are statements of amounts owed by a customer. They track the status of payments from draft through paid or otherwise finalized. Subscriptions automatically generate invoices, or you can manually create a one-off invoice), it’s considered a first-time transaction.

> Sessions that don’t create [Customers](https://docs.stripe.com/api/customers.md) instead create [Guest Customers](https://support.stripe.com/questions/guest-customer-faq) in the Dashboard. Promotion codes limited to first-time customers are still accepted for these Sessions.

### Set a minimum amount

With promotion codes, you can set a minimum transaction amount for eligible discount by configuring [minimum_amount](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-restrictions-minimum_amount) and [minimum_amount_currency](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-restrictions-minimum_amount_currency). Since promotion code restrictions are checked at redemption time, the minimum transaction amount only applies to the initial payment for a subscription.

### Customize expirations

You can set an expiration date on the promotion code using [expires_at](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-expires_at). If the underlying coupon already has `redeem_by` set, then the expiration date for the promotion code can’t be later than that of the coupon. If `promotion_code[expires_at]` isn’t specified, the coupon’s `redeem_by` automatically populates `expires_at`.

For example, you might have plans to support a coupon for a year, but you only want it to be redeemable for one week after a customer receives it. You can set `coupon[redeem_by]` to one year from now, and set each `promotion_code[expires_at]` to one week after it’s created.

### Limit redemptions

You can limit the number of redemptions by using [max_redemptions](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-max_redemptions), which works similarly to the coupon parameter. If the underlying coupon already has `max_redemptions` set, then the `max_redemptions` for the promotion code can’t be greater than that of the coupon.

For example, you might want a seasonal sale coupon to be redeemable by the first 50 customers, but the winter promotion can only use 20 of those redemptions. In this scenario, you can set `coupon[max_redemptions]: 50` and `promotion_code[max_redemptions]: 20`.

### Inactive promotions

You can set whether a promotion code is currently redeemable by using the [active](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-active) parameter. However, if the underlying coupon for a promotion code becomes invalid, all of its promotion codes become permanently inactive. Similarly, if a promotion code reaches its `max_redemptions` or `expires_at`, it becomes permanently inactive. You can’t reactivate these promotion codes.

### Delete promotions

You can delete promotions in the Dashboard or the API. Deleting a promotion prevents it from being applied to future transactions or customers.


# Embedded form

> This is a Embedded form for when payment-ui is embedded-form. View the full page at https://docs.stripe.com/payments/checkout/discounts?payment-ui=embedded-form.

You can use discounts to reduce the amount charged to a customer. Coupons and promotion codes allow you to:

- Apply a discount to an entire purchase subtotal
- Apply a discount to specific products
- Reduce the total charged by a percentage or a flat amount
- Create customer-facing promotion codes on top of coupons to share directly with customers

> To use coupons for discounting *subscriptions* (A Subscription represents the product details associated with the plan that your customer subscribes to. Allows you to charge the customer on a recurring basis) with Checkout and Billing, see [Discounts for subscriptions](https://docs.stripe.com/billing/subscriptions/coupons.md).

## Create a coupon

Coupons specify a fixed value discount. You can create customer-facing promotion codes that map to a single underlying coupon. This means that the codes `FALLPROMO` and `SPRINGPROMO` can both point to one 25% off coupon. You can create coupons in the [Dashboard](https://dashboard.stripe.com/coupons) or with the [API](https://docs.stripe.com/api.md#coupons):

```curl
curl https://api.stripe.com/v1/coupons \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d percent_off=20 \
  -d duration=once
```

```cli
stripe coupons create  \
  --percent-off=20 \
  --duration=once
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

coupon = client.v1.coupons.create({
  percent_off: 20,
  duration: 'once',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
coupon = client.v1.coupons.create({"percent_off": 20, "duration": "once"})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$coupon = $stripe->coupons->create([
  'percent_off' => 20,
  'duration' => 'once',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CouponCreateParams params =
  CouponCreateParams.builder()
    .setPercentOff(new BigDecimal(20))
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
  percent_off: 20,
  duration: 'once',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CouponCreateParams{
  PercentOff: stripe.Float64(20),
  Duration: stripe.String(stripe.CouponDurationOnce),
}
result, err := sc.V1Coupons.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CouponCreateOptions { PercentOff = 20M, Duration = "once" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Coupons;
Coupon coupon = service.Create(options);
```

## Use a coupon

To create a session with an applied discount, pass the [coupon ID](https://docs.stripe.com/api/coupons/object.md#coupon_object-id) in the `coupon` parameter of the [discounts](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-discounts) array. Checkout currently supports up to one coupon or promotion code.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "discounts[0][coupon]"="{{COUPON_ID}}" \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "discounts[0][coupon]"="{{COUPON_ID}}" \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}"
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
  discounts: [{coupon: '{{COUPON_ID}}'}],
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "discounts": [{"coupon": "{{COUPON_ID}}"}],
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}",
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
  'discounts' => [['coupon' => '{{COUPON_ID}}']],
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}',
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
    .addDiscount(
      SessionCreateParams.Discount.builder().setCoupon("{{COUPON_ID}}").build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}")
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
  discounts: [
    {
      coupon: '{{COUPON_ID}}',
    },
  ],
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}',
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
  Discounts: []*stripe.CheckoutSessionCreateDiscountParams{
    &stripe.CheckoutSessionCreateDiscountParams{
      Coupon: stripe.String("{{COUPON_ID}}"),
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}"),
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
    Discounts = new List<Stripe.Checkout.SessionDiscountOptions>
    {
        new Stripe.Checkout.SessionDiscountOptions { Coupon = "{{COUPON_ID}}" },
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Configure a coupon

Coupons have the following parameters that you can use:

- `currency`
- `percent_off` or `amount_off`
- `max_redemptions`
- `redeem_by`, the latest date customers can apply the coupon
- `applies_to`, limits the products that the coupon applies to

> The coupon object adds discounts to both one-time payments and subscriptions. Some coupon object parameters, like `duration`, only apply to [subscriptions](https://docs.stripe.com/billing/subscriptions/coupons.md).

### Limit redemption usage

The `max_redemptions` and `redeem_by` values apply to the coupon across every application. For example, you can restrict a coupon to the first 50 usages of it, or you can make a coupon expire by a certain date.

### Limit eligible products

You can limit the products that are eligible for discounts using a coupon by adding the product IDs to the `applies_to` hash in the Coupon object. Any promotion codes that map to this coupon only apply to the list of eligible products.

### Delete a coupon

You can delete coupons in the Dashboard or the API. Deleting a coupon prevents it from being applied to future transactions or customers.

## Create a promotion code

Promotion codes are customer-facing codes created on top of coupons. You can also specify additional restrictions that control when a customer can apply the promotion. You can share these codes with customers who can enter them during checkout to apply a discount.

To create a [promotion code](https://docs.stripe.com/api/promotion_codes.md), specify an existing `coupon` and any restrictions (for example, limiting it to a specific `customer`). If you have a specific code to give to your customer (for example, `FALL25OFF`), set the `code`. If you leave this field blank, we’ll generate a random `code` for you.

The `code` is case-insensitive and unique across active promotion codes for any customer. For example:

- You can create multiple customer-restricted promotion codes with the same `code`, but you can’t reuse that `code` for a promotion code redeemable by any customer.
- If you create a promotion code that is redeemable by any customer, you can’t create another active promotion code with the same `code`.
- You can create a promotion code with `code: NEWUSER`, inactivate it by passing `active: false`, and then create a new promotion code with `code: NEWUSER`.

Promotion codes can be created in the coupons section of the [Dashboard](https://dashboard.stripe.com/coupons/create) or with the [API](https://docs.stripe.com/api.md#promotion_codes):

```curl
curl https://api.stripe.com/v1/promotion_codes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d coupon={{COUPON_ID}} \
  -d code=VIPCODE
```

## Use a promotion code 

Enable customer-redeemable promotion codes using the [allow_promotion_codes](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-allow_promotion_codes) parameter in a Checkout Session. This enables a field in Checkout to allow customers to input promotion codes.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  -d ui_mode=embedded \
  -d allow_promotion_codes=true \
  --data-urlencode return_url="https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --ui-mode=embedded \
  --allow-promotion-codes=true \
  --return-url="https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}"
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
  ui_mode: 'embedded',
  allow_promotion_codes: true,
  return_url: 'https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}',
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
  "ui_mode": "embedded",
  "allow_promotion_codes": True,
  "return_url": "https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}",
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
  'ui_mode' => 'embedded',
  'allow_promotion_codes' => true,
  'return_url' => 'https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}',
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
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setAllowPromotionCodes(true)
    .setReturnUrl("https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}")
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
  ui_mode: 'embedded',
  allow_promotion_codes: true,
  return_url: 'https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}',
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
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  AllowPromotionCodes: stripe.Bool(true),
  ReturnURL: stripe.String("https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}"),
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
    UiMode = "embedded",
    AllowPromotionCodes = true,
    ReturnUrl = "https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Configure a promotion code

For each promotion code, you can customize eligible customers, redemptions, and other limits.

### Limit by customer

To limit a promotion to a particular customer, specify a [customer](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-customer) when creating the promotion code. If no customer is specified, any customer can redeem the code.

### Limit by first-time order

You can also limit the promotion code to first-time customers with [restrictions.first_time_transaction](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-restrictions-first_time_transaction). If the `customer` isn’t defined, or if a defined `customer` has no prior payments or non-void *invoices* (Invoices are statements of amounts owed by a customer. They track the status of payments from draft through paid or otherwise finalized. Subscriptions automatically generate invoices, or you can manually create a one-off invoice), it’s considered a first-time transaction.

> Sessions that don’t create [Customers](https://docs.stripe.com/api/customers.md) instead create [Guest Customers](https://support.stripe.com/questions/guest-customer-faq) in the Dashboard. Promotion codes limited to first-time customers are still accepted for these Sessions.

### Set a minimum amount

With promotion codes, you can set a minimum transaction amount for eligible discount by configuring [minimum_amount](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-restrictions-minimum_amount) and [minimum_amount_currency](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-restrictions-minimum_amount_currency). Since promotion code restrictions are checked at redemption time, the minimum transaction amount only applies to the initial payment for a subscription.

### Customize expirations

You can set an expiration date on the promotion code using [expires_at](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-expires_at). If the underlying coupon already has `redeem_by` set, then the expiration date for the promotion code can’t be later than that of the coupon. If `promotion_code[expires_at]` isn’t specified, the coupon’s `redeem_by` automatically populates `expires_at`.

For example, you might have plans to support a coupon for a year, but you only want it to be redeemable for one week after a customer receives it. You can set `coupon[redeem_by]` to one year from now, and set each `promotion_code[expires_at]` to one week after it’s created.

### Limit redemptions

You can limit the number of redemptions by using [max_redemptions](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-max_redemptions), which works similarly to the coupon parameter. If the underlying coupon already has `max_redemptions` set, then the `max_redemptions` for the promotion code can’t be greater than that of the coupon.

For example, you might want a seasonal sale coupon to be redeemable by the first 50 customers, but the winter promotion can only use 20 of those redemptions. In this scenario, you can set `coupon[max_redemptions]: 50` and `promotion_code[max_redemptions]: 20`.

### Inactive promotions

You can set whether a promotion code is currently redeemable by using the [active](https://docs.stripe.com/api/promotion_codes/create.md#create_promotion_code-active) parameter. However, if the underlying coupon for a promotion code becomes invalid, all of its promotion codes become permanently inactive. Similarly, if a promotion code reaches its `max_redemptions` or `expires_at`, it becomes permanently inactive. You can’t reactivate these promotion codes.

### Delete promotions

You can delete promotions in the Dashboard or the API. Deleting a promotion prevents it from being applied to future transactions or customers.

