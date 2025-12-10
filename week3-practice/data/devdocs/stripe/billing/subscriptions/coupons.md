# Coupons and promotion codes

Add discounts to subscriptions and subscription items using coupons and promotion codes.

Redeem coupons to apply discounts to the subscriptions you offer. You can also use coupons to create promotion codes to share with your customers. Customers can redeem these promotion codes to apply discounts to their subscriptions.

- [Coupons](https://docs.stripe.com/billing/subscriptions/coupons.md#coupons): You create and manage coupons to define discounts, such as a percentage or amount off from the subscription price.
- [Promotion codes](https://docs.stripe.com/billing/subscriptions/coupons.md#promotion-codes): You create customer-facing codes that map to your coupons. For example, FALLPROMO and SPRINGPROMO can both map to a single 25% off coupon. You can share promotion codes directly with your customers, who can enter and redeem the codes at checkout.

You can use coupons and promotion codes to:

- Apply one or more discounts to an invoice, subscription, or subscription item
- Apply one or more discounts for a certain duration of time
- Reduce invoice amounts by a percentage or a flat amount

You can also define a coupon that a customer must redeem by a certain date, or that?™s limited to a set number of redemptions across all of your customers.

To use discounts for one-time payments, see [Add discounts for one-time payments](https://docs.stripe.com/payments/checkout/discounts.md).

## Coupons 

To apply discounts to a customer or a customer?™s charges, redeem coupons into discounts. Learn how to create and manage coupons in the following sections.

### Create a coupon

Create coupons in the Dashboard or with the [API](https://docs.stripe.com/api/coupons/create.md):

#### Dashboard

1. In the Dashboard, open the [Products](https://dashboard.stripe.com/test/products?active=true) page.
1. Click **Coupons**.
1. Click **+New**.
1. In the **Create a coupon** dialog, enter the coupon?™s parameters.
1. Click **Create coupon**.

The following are all the settings for coupons. The name is the only setting you can edit after you create the coupon.

| Setting                                   | Description                                                                                                                                                                                                                                                                                                                                    |
| ----------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Name**                                  | The name of the coupon that appears on receipts and invoices.                                                                                                                                                                                                                                                                                  |
| **ID** (optional)                         | A unique identifier for the coupon in the API. If you leave this field blank, Stripe generates an ID for you.                                                                                                                                                                                                                                  |
| **Type**                                  | Determines whether a coupon discounts a subscription by a fixed amount or by a percentage.                                                                                                                                                                                                                                                     |
| **Percentage off** or **Discount amount** | Indicates how much the coupon actually discounts.

  If you sell in multiple currencies, a single coupon can define different discount amounts for different currencies. Multi-currency coupons follow the same rules as [multi-currency prices](https://docs.stripe.com/products-prices/how-products-and-prices-work.md#multiple-currencies). |
| **Apply to specific products** (optional) | Limits the type of items that the coupon can apply to.                                                                                                                                                                                                                                                                                         |
| **Duration**                              | Indicates how long the coupon is valid for.                                                                                                                                                                                                                                                                                                    |
| **Redemption limits** (optional)          | Allows you to limit when a customer can redeem the coupon and the number of times a coupon can be redeemed.                                                                                                                                                                                                                                    |
| **Codes** (optional)                      | Allows you to create [promotion codes](https://docs.stripe.com/billing/subscriptions/coupons.md#promotion-codes--promotion-codes) for the coupon.                                                                                                                                                                                              |

#### API

```curl
curl https://api.stripe.com/v1/coupons \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d duration=once \
  -d id=free-period \
  -d percent_off=100
```

```cli
stripe coupons create  \
  --duration=once \
  --id=free-period \
  --percent-off=100
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

coupon = client.v1.coupons.create({
  duration: 'once',
  id: 'free-period',
  percent_off: 100,
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
coupon = client.v1.coupons.create({
  "duration": "once",
  "id": "free-period",
  "percent_off": 100,
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$coupon = $stripe->coupons->create([
  'duration' => 'once',
  'id' => 'free-period',
  'percent_off' => 100,
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CouponCreateParams params =
  CouponCreateParams.builder()
    .setDuration(CouponCreateParams.Duration.ONCE)
    .setId("free-period")
    .setPercentOff(new BigDecimal(100))
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Coupon coupon = client.v1().coupons().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const coupon = await stripe.coupons.create({
  duration: 'once',
  id: 'free-period',
  percent_off: 100,
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CouponCreateParams{
  Duration: stripe.String(stripe.CouponDurationOnce),
  ID: stripe.String("free-period"),
  PercentOff: stripe.Float64(100),
}
result, err := sc.V1Coupons.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CouponCreateOptions
{
    Duration = "once",
    Id = "free-period",
    PercentOff = 100M,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Coupons;
Coupon coupon = service.Create(options);
```

The following table contains coupon parameters.

| Setting                                                | Description                                                                                                                                                                                                                                                          |
| ------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `id`                                                   | A unique identifier for the coupon.                                                                                                                                                                                                                                  |
| `percent_off` or `amount_off`                          | The amount that is taken off the subtotal for the duration of the coupon.                                                                                                                                                                                            |
| `currency` (if `amount_off` is set)                    | The three-letter ISO code for the currency of the amount to take off.                                                                                                                                                                                                |
| `currency_options` (if `amount_off` is set) (optional) | If you sell in multiple currencies, amounts to take off the subtotal for different currencies. Multi-currency coupons follow the same rules as [multi-currency prices](https://docs.stripe.com/products-prices/how-products-and-prices-work.md#multiple-currencies). |
| `duration`                                             | Indicates how long the coupon is valid for. Values include **once**, **forever**, or **repeating**.                                                                                                                                                                  |
| `max_redemptions` (optional)                           | Maximum number of times a coupon can be redeemed across all customers.                                                                                                                                                                                               |
| `redeem_by` (optional)                                 | The latest date at which you can apply this coupon to customers.                                                                                                                                                                                                     |
| `applies_to` (optional)                                | Limits the items in an invoice that the coupon can apply to.                                                                                                                                                                                                         |

### Set eligible products

#### Dashboard

To set the products that are eligible for discounts, add the relevant product in the **Apply to specific product** field. Any promotion codes that are associated with the coupon are also restricted to this list of eligible products.

If you configure a coupon to apply to specific products and a subscription doesn?™t have any applicable products, no discount is applied when you add the coupon to the subscription.

#### API

To set the products eligible for discounts, add the relevant product IDs to the `applies_to` hash in the coupon. This list of eligible products also applies to promotion codes associated with the coupon.

If you configure a coupon to apply to specific products and a subscription doesn?™t have any applicable products, no discount is applied when you add the coupon to the subscription.

When you [make changes](https://docs.stripe.com/billing/subscriptions/change.md) to a subscription, Stripe calculates the proration and applies any existing discounts. You can?™t discount proration line items further on the invoice that?™s generated.

### Apply coupons to subscriptions 

After you?™ve created coupons, create a discount by applying them to a subscription. You can apply the coupon when you create the subscription or by [updating a customer?™s existing subscription](https://docs.stripe.com/api.md#update_subscription).

#### Dashboard

1. In the Dashboard, open the [Subscriptions](https://dashboard.stripe.com/test/subscriptions?status=active) page.
1. Click the relevant subscription.
1. Click **Actions**.
1. Click **Update subscription**.
1. Click **Add coupon**.
1. Select one or more coupons from the dropdown menus and click **Submit**.

#### API

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "discounts[0][coupon]"=free-period
```

```cli
stripe subscriptions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "discounts[0][coupon]"=free-period
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  items: [{price: '{{PRICE_ID}}'}],
  discounts: [{coupon: 'free-period'}],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "customer": "{{CUSTOMER_ID}}",
  "items": [{"price": "{{PRICE_ID}}"}],
  "discounts": [{"coupon": "free-period"}],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'items' => [['price' => '{{PRICE_ID}}']],
  'discounts' => [['coupon' => 'free-period']],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionCreateParams params =
  SubscriptionCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addItem(
      SubscriptionCreateParams.Item.builder().setPrice("{{PRICE_ID}}").build()
    )
    .addDiscount(
      SubscriptionCreateParams.Discount.builder().setCoupon("free-period").build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Subscription subscription = client.v1().subscriptions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  items: [
    {
      price: '{{PRICE_ID}}',
    },
  ],
  discounts: [
    {
      coupon: 'free-period',
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  Items: []*stripe.SubscriptionCreateItemParams{
    &stripe.SubscriptionCreateItemParams{Price: stripe.String("{{PRICE_ID}}")},
  },
  Discounts: []*stripe.SubscriptionCreateDiscountParams{
    &stripe.SubscriptionCreateDiscountParams{Coupon: stripe.String("free-period")},
  },
}
result, err := sc.V1Subscriptions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    Items = new List<SubscriptionItemOptions>
    {
        new SubscriptionItemOptions { Price = "{{PRICE_ID}}" },
    },
    Discounts = new List<SubscriptionDiscountOptions>
    {
        new SubscriptionDiscountOptions { Coupon = "free-period" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

You can still create a subscription when a customer doesn?™t have a stored payment method if [no immediate payment](https://docs.stripe.com/billing/subscriptions/deferred-payment.md) is required after you apply coupons to it.

### Apply coupons to Checkout 

Apply coupons to subscriptions in a Checkout Session by setting the `discounts` parameter in the [API](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-discounts). To create a session with an applied discount, pass the coupon ID in the `coupon` parameter of the `discounts` array.

#### curl

```bash
curl https://api.stripe.com/v1/checkout/sessions \
  -u <<YOUR_SECRET_KEY>>: \
  -d "payment_method_types[]"=card \
  -d "line_items[][price]"="{{PRICE_ID}}" \
  -d "line_items[][quantity]"=1 \
  -d mode=subscription \-d "discounts[][coupon]"="{{COUPON_ID}}" \
  -d success_url="https://example.com/success" \
```

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

session = Stripe::Checkout::Session.create({
  payment_method_types: ['card'],
  line_items: [{
    price: '{{PRICE_ID}}',
    quantity: 1
  }],
  mode: 'subscription',
  discounts: [{coupon: '{{COUPON_ID}}',
  }],
  success_url: 'https://example.com/success'
})
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

session = stripe.checkout.Session.create(
  payment_method_types=['card'],
  line_items=[{
    'price': '{{PRICE_ID}}',
    'quantity': 1
  }],
  mode='subscription',
  discounts=[{'coupon': '{{COUPON_ID}}',
  }],
  success_url='https://example.com/success',
)
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

$session = \Stripe\Checkout\Session::create([
  'payment_method_types' => ['card'],
  'line_items' => [[
    'price' => '{{PRICE_ID}}',
    'quantity' => 1
  ]],
  'mode' => 'subscription',
  'discounts' => [['coupon' => '{{COUPON_ID}}',
  ]],
  'success_url' => 'https://example.com/success'
]);
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

SessionCreateParams params =
  SessionCreateParams.builder()
    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice(""{{PRICE_ID}}"")
        .setQuantity(1L)
        .build())
    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
    .addDiscount(
      SessionCreateParams.Discount.builder().setCoupon("{{COUPON_ID}}")
        .build())
    .setSuccessUrl("https://example.com/success")
    .build();

Session session = Session.create(params);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  payment_method_types: ['card'],
  line_items: [{
    price: '{{PRICE_ID}}',
    quantity: 1
  }],
  mode: 'subscription',
  discounts: [{coupon: '{{COUPON_ID}}',
  }],
  success_url: 'https://example.com/success'
});
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

params := &stripe.CheckoutSessionParams{
  PaymentMethodTypes: stripe.StringSlice([]string{
    "card"
  }),
  LineItems: []*stripe.CheckoutSessionLineItemParams{
      &stripe.CheckoutSessionLineItemParams{
          Price: stripe.String(""{{PRICE_ID}}""),
          Quantity: stripe.Int64(1)
      }
  },
  Mode: stripe.String("subscription"),
  Discounts: []*stripe.CheckoutSessionDiscountParams{
      &stripe.CheckoutSessionDiscountParams{Coupon: stripe.String("{{COUPON_ID}}"),
      }
  },
  SuccessURL: stripe.String("https://example.com/success"),
}

session, _ := session.New(params)
```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var options = new SessionCreateOptions
{
  PaymentMethodTypes = new List<string>
  {
    "card"
  },
  LineItems = new List<SessionLineItemOptions>
  {
      new SessionLineItemOptions
      {
          Price = ""{{PRICE_ID}}"",
          Quantity = 1
      }
  },
  Mode = "subscription",
  Discounts = new List<SessionDiscountOptions>
  {
      new SessionDiscountOptions
      {Coupon = "{{COUPON_ID}}",
      }
  },
  SuccessUrl = "https://example.com/success",
};

var service = new SessionService();
var session = service.Create(options);
```

### Delete coupons 

You can delete coupons with the Dashboard or the [API](https://docs.stripe.com/api/coupons/delete.md).

Deleting a coupon prevents it from being applied to future subscriptions or invoices, but it doesn?™t remove the discount from any subscription or invoice that already has it.

#### Dashboard

1. In the Dashboard, open the [Products](https://dashboard.stripe.com/test/products?active=true) page.
1. Click **Coupons**
1. Click the relevant coupon.
1. Click the overflow menu (??.
1. Click **Delete coupon**.

#### API

```curl
curl -X DELETE https://api.stripe.com/v1/coupons/free-period \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe coupons delete free-period
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

deleted = client.v1.coupons.delete('free-period')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
deleted = client.v1.coupons.delete("free-period")
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$deleted = $stripe->coupons->delete('free-period', []);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Coupon coupon = client.v1().coupons().delete("free-period");
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const deleted = await stripe.coupons.del('free-period');
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CouponDeleteParams{}
result, err := sc.V1Coupons.Delete(context.TODO(), "free-period", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Coupons;
Coupon deleted = service.Delete("free-period");
```

### Coupon duration

A coupon?™s duration indicates how long the redeemed [discount](https://docs.stripe.com/api/.md#discounts) is valid for. For example, a coupon for 50% off with a duration of 4 months applies to all invoices in the 4 month period starting when the coupon is first applied. If a customer applies this coupon to a yearly subscription during the coupon?™s 4 month period, the 50% discount applies to the entire yearly subscription. In a monthly subscription, the coupon applies to the first 4 months. For a weekly subscription, a 4 month coupon applies to every invoice in the first 4 months.

If you?™re configuring a coupon?™s duration in the API, when you use the value `repeating` you must specify `duration_in_months` as the number of months that the coupon repeatedly applies to. If you set the duration to `once`, the coupon applies only to the first invoice. If you set the duration to `forever`, the coupon applies to all invoices indefinitely.

### Redemption limits

Redemption limits apply to the coupon across every customer. For example, if you limit the number of times a coupon can be redeemed to 50, you can apply it to your customers only 50 times. This can be one time each for 50 different customers, one customer 50 times, or multiple customers multiple times until the max of 50 times.

If you set a coupon to last forever when a customer uses it but the coupon has an expiration date, any customer given that coupon will have that coupon?™s discount forever. No new customers can apply the coupon after the expiration date.

## Promotion codes 

Promotion codes are customer-facing codes that you create for coupons. For example, FALLPROMO and SPRINGPROMO can both point to a single 25% off coupon. You can share promotion codes directly with your customers to use at checkout.

If you?™ve implemented the *customer portal* (The customer portal is a secure, Stripe-hosted page that lets your customers manage their subscriptions and billing details) and turned on promotion codes, customers can apply a discount when upgrading or downgrading their existing subscriptions in the portal.

> Subscriptions apply promotion code and price updates separately, which might cause unexpected updates. For example, a payment failure can cause a price upgrade to fail, but the promotion code included with the price upgrade succeeds.

> The customer portal displays promotion codes that have been applied to a subscription. If you don?™t want to allow customers to apply the promotion code themselves or potentially share it with others, you should either [set limits on the promotion code](https://docs.stripe.com/billing/subscriptions/coupons.md#promo-code-config) or [apply a coupon](https://docs.stripe.com/billing/subscriptions/coupons.md#discount-subscriptions) directly.

Customize controls and limits on promotion codes by specifying eligible customers, first time orders, minimum order values, expiration dates, and redemption limits.

### Restrictions

There are some restrictions to promotion codes.

- You can?™t apply a promotion code with amount restrictions on:
  - [Subscription Item objects](https://docs.stripe.com/api/subscription_items/object.md)
  - [Invoice Item objects](https://docs.stripe.com/api/invoiceitems/object.md)
  - [Subscriptions objects](https://docs.stripe.com/api/subscriptions/object.md) when you make an update
  - Future phases on [Subscription Schedule objects](https://docs.stripe.com/api/subscription_schedules/object.md)

### Create promotion codes

#### Dashboard

You can create a promotion code in the Dashboard when you [create a coupon](https://docs.stripe.com/billing/subscriptions/coupons.md#create-coupons--create-coupons).

The **Code** is case-insensitive and unique across active promotion codes for any customer. For example:

- You can create multiple customer-restricted promotion codes with the same **Code**, but you can?™t reuse that **Code** for a promotion code that any customer can redeem.
- If you create a promotion code that is redeemable by any customer, you can?™t create another active promotion code with the same **code**.
- You can create a promotion code with one **Code**, [inactivate](https://docs.stripe.com/billing/subscriptions/coupons.md#inactive-promotions--inactivate) it, and then create a new promotion code with the same **Code**.

1. In the Dashboard on the [Create a coupon](https://dashboard.stripe.com/test/coupons/create) page, click the **Use customer-facing coupon codes** button.
1. Enter a code. This is the code that a customer enters at checkout to redeem the discount. If you don?™t set a code, Stripe generates one for you.
1. Select requirements for the promotion code. For example, you can restrict the coupon to only being valid on first-time orders.

#### API

The `code` is case-insensitive and unique across active promotion codes for any customer. For example:

- You can create multiple customer-restricted promotion codes with the same `code`, but you can?™t reuse that `code` for a promotion code that any customer can redeem.
- If you create a promotion code that is redeemable by any customer, you can?™t create another active promotion code with the same `code`.
- You can create a promotion code with `code: NEWUSER`, inactivate it by passing `active: false`, and then create a new promotion code with `code: NEWUSER`.

To create a [promotion code](https://docs.stripe.com/api/promotion_codes.md), specify an existing `coupon` and any restrictions (for example, limiting to a specific `customer`). If you have a specific code that you want to give to your customer (for example, `FALL25OFF`), set the `code`. If you leave this field blank, Stripe generates a random `code` for you.

```curl
curl https://api.stripe.com/v1/promotion_codes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d coupon=ZQO00CcH \
  -d code=ALICE20 \
  -d customer="{{CUSTOMER_ID}}"
```

When you create a promotion code, it inherits the configuration of the associated coupon.

### Promotion code configurations 

By configuring the promotion code settings, you can customize the following:

- Which customers are eligible to use a promotion code
- How many times a customer can redeem a promotion code
- When a promotion code expires
- Set a minimum amount a promotion code can apply to

### Limit by customer

#### Dashboard

To limit a promotion code to a particular customer, complete these steps:

1. On the [Create a coupon](https://dashboard.stripe.com/test/coupons/create) page, select **Limit to a specific customer**.
1. Select the relevant customer. If you don?™t specify a customer, any customer can redeem the promotion code.

#### API

To limit a promotion code to a particular customer, specify a `customer` when you create the promotion code. If you don?™t specify a customer, any customer can redeem the promotion code.

### Limit by first time order

Restricts the coupon to customers who have no prior transaction history on your platform. This setting prevents customers from using the coupon if they:

- Initiated a PaymentIntent, even if the payment never completed.
- Subscribed to a trial period, even if it subsequently canceled.

#### Dashboard

To limit a promotion code to first-time customers, on the [Create a coupon](https://dashboard.stripe.com/test/coupons/create) page, select **Eligible for first-time order only**.

#### API

Limit the promotion code to first time customers by setting the `first_time_transaction` parameter of the `restrictions` attribute.

### Set a minimum amount

#### Dashboard

To set an minimum amount that is eligible for a promotion code, on the [Create a coupon](https://dashboard.stripe.com/test/coupons/create) page, select **Require minimum order value** and enter the minimum value.

Because promotion code restrictions are checked at redemption time, the minimum transaction amount only applies to the initial payment for a subscription.

If the coupon supports multiple currencies, the minimum amount can be different per-currency.

#### API

With promotion codes, you can set a minimum transaction amount for eligible discount by configuring the `minimum_amount` and the `minimum_amount_currency` properties. Since promotion code restrictions are checked at redemption time, the minimum transaction amount only applies to the initial payment for a subscription. If you sell in multiple currencies, set the minimum transaction amount for different currencies by configuring the `currency_options` property.

### Customize expirations

#### Dashboard

To set an expiration date for a promotion code, on the [Create a coupon](https://dashboard.stripe.com/test/coupons/create) page, select **Add an expiration date** and the date and time at which the promotion code expires.

If the underlying coupon already has an expiration date set, then the promotion code?™s expiration date can?™t be later than the coupon?™s.

For example, you might have plans to support a coupon for a year, but you only want it to be redeemable for one week after a customer receives it. To do this, set the coupon?™s expiration date to one year from now, and set each the promotion code?™s expiration date to one week after it is created.

#### API

Set an expiration date on the promotion code using `expires_at`. If the underlying coupon already has `redeem_by` set, then the promotion code?™s expiration date can?™t be later than the coupon?™s. If `promotion_code[expires_at]` isn?™t specified, the coupon?™s `redeem_by` automatically populates `expires_at`.

- For example, you might have plans to support a coupon for a year, but you only want it to be redeemable for one week after a customer receives it. You would set `coupon[redeem_by]` to one year from now, and set each `promotion_code[expires_at]` to one week after it is created.

### Limit redemptions

#### Dashboard

To set the total number of times the promotion code can be redeemed by your customers, select **Limit the number of times this code can be redeemed** on the [Create a coupon](https://dashboard.stripe.com/test/coupons/create) page and enter the number. See [Redemption limits](https://docs.stripe.com/billing/subscriptions/coupons.md#redemption-limits) for details.

If the underlying coupon already has a maximum number of times set, then the promotion code?™s maximum redemptions can?™t be greater than the coupon?™s.

#### API

Limit the total number of times a promotion code can be redeemed by your customers using `max_redemptions`, which works similarly to coupons. If the underlying coupon already has `max_redemptions` set, then the promotion code?™s `max_redemptions` can?™t be greater than the coupon?™s. See [Redemption limits](https://docs.stripe.com/billing/subscriptions/coupons.md#redemption-limits) for details.

### Deactivate promotion codes 

#### Dashboard

To deactivate a promotion code, doing the following steps:

1. In the Dashboard, open the [Products](https://dashboard.stripe.com/test/products?active=true) page.
1. Click **Coupons**.
1. Click the coupon whose promotion code you want to deactivate.
1. In the relevant promotion code row, click the overflow menu (??.
1. Click **Archive promotion code**.

However, if the underlying coupon for a promotion code becomes invalid, all of its promotion codes become permanently inactive. Similarly, if a promotion code reaches its maximum redemption limit or its expiration date, it becomes permanently inactive. These promotion codes can?™t be reactivated.

#### API

Set whether a promotion code is currently redeemable by using the `active` parameter. However, if the underlying coupon for a promotion code becomes invalid, all of its promotion codes become permanently inactive. Similarly, if a promotion code reaches its `max_redemptions` or `expires_at`, it becomes permanently inactive. These promotion codes can?™t be reactivated.

### Apply promotion codes to subscriptions 

After you create a promotion code, redeem a discount by applying the promotion code to a subscription. You can apply promotion codes two ways:

- When you [create a subscription](https://docs.stripe.com/api.md#create_subscription)
- When you [update a customer?™s existing subscription](https://docs.stripe.com/api.md#update_subscription)

#### Dashboard

1. In the Dashboard, go to **Billing** > **Subscriptions**.
1. Click the relevant subscription.
1. Click **Actions** > **Update subscription** > **Add coupon**.
1. Click a promotion code from the dropdown menu and click **Submit**.

#### API

1. [List](https://docs.stripe.com/api/promotion_codes/list.md) the promotion codes and use the [code](https://docs.stripe.com/api/promotion_codes/list.md#list_promotion_code-code) from your Customer as a filter to [retrieve](https://docs.stripe.com/api/promotion_codes/retrieve.md) the [promotion code ID](https://docs.stripe.com/api/promotion_codes/object.md#promotion_code_object-id).
1. To apply the promotion code, use the promotion code ID in the following API call:

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "discounts[0][promotion_code]"="{{PROMOTIONCODE_ID}}"
```

```cli
stripe subscriptions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "discounts[0][promotion_code]"="{{PROMOTIONCODE_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  items: [{price: '{{PRICE_ID}}'}],
  discounts: [{promotion_code: '{{PROMOTIONCODE_ID}}'}],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "customer": "{{CUSTOMER_ID}}",
  "items": [{"price": "{{PRICE_ID}}"}],
  "discounts": [{"promotion_code": "{{PROMOTIONCODE_ID}}"}],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'items' => [['price' => '{{PRICE_ID}}']],
  'discounts' => [['promotion_code' => '{{PROMOTIONCODE_ID}}']],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionCreateParams params =
  SubscriptionCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addItem(
      SubscriptionCreateParams.Item.builder().setPrice("{{PRICE_ID}}").build()
    )
    .addDiscount(
      SubscriptionCreateParams.Discount.builder()
        .setPromotionCode("{{PROMOTIONCODE_ID}}")
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Subscription subscription = client.v1().subscriptions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  items: [
    {
      price: '{{PRICE_ID}}',
    },
  ],
  discounts: [
    {
      promotion_code: '{{PROMOTIONCODE_ID}}',
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  Items: []*stripe.SubscriptionCreateItemParams{
    &stripe.SubscriptionCreateItemParams{Price: stripe.String("{{PRICE_ID}}")},
  },
  Discounts: []*stripe.SubscriptionCreateDiscountParams{
    &stripe.SubscriptionCreateDiscountParams{
      PromotionCode: stripe.String("{{PROMOTIONCODE_ID}}"),
    },
  },
}
result, err := sc.V1Subscriptions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    Items = new List<SubscriptionItemOptions>
    {
        new SubscriptionItemOptions { Price = "{{PRICE_ID}}" },
    },
    Discounts = new List<SubscriptionDiscountOptions>
    {
        new SubscriptionDiscountOptions { PromotionCode = "{{PROMOTIONCODE_ID}}" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

### Add promotion codes to Checkout

Enable promotion codes with the API by setting the [allow_promotion_codes](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-allow_promotion_codes) parameter in Checkout Sessions. When `allow_promotion_codes` is enabled on a Checkout Session, Checkout includes a promotion code redemption box for your customers to use.
![Promotion code field at checkout](https://b.stripecdn.com/docs-statics-srv/assets/promo_code_checkout.c07ef6d4f0b1b3f9a8a7e4bbba83d56f.png)

Promotion code field at checkout

## Stackable coupons and promotion codes 

You can add multiple coupons, promotion codes, or redeemed [discounts](https://docs.stripe.com/api/.md#discounts) to a customer?™s list of charges. You can do this when [creating a subscription](https://docs.stripe.com/api.md#create_subscription) or by [updating a customer?™s existing subscription](https://docs.stripe.com/api.md#update_subscription).

We support multiple discounts on both subscriptions and subscription items.

When you create a subscription with stackable discounts, each discount applies to all items on the subscription. The order of the discounts is important if you use both `amount_off` and `percent_off`. For example, the following stacked discounts apply differently:

- 20% off *then* $5 off
- $5 off *then* 20% off

#### Dashboard

1. In the Dashboard, go to **Billing** > **Subscriptions**.
1. Click the relevant subscription.
1. Click **Actions** > **Update subscription** > **Add coupon**.
1. Click coupons from the dropdown menus and click **Submit**.
1. Click the relevant product.
1. Click **Add coupons**.
1. Click coupons from the dropdown menus and click **Submit**.

#### API

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "items[0][discounts][0][coupon]"=item-coupon \
  -d "items[0][discounts][1][promotion_code]"=item-promo \
  -d "discounts[0][coupon]"=sub-coupon \
  -d "discounts[1][promotion_code]"=sub-promo
```

```cli
stripe subscriptions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "items[0][discounts][0][coupon]"=item-coupon \
  -d "items[0][discounts][1][promotion_code]"=item-promo \
  -d "discounts[0][coupon]"=sub-coupon \
  -d "discounts[1][promotion_code]"=sub-promo
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  items: [
    {
      price: '{{PRICE_ID}}',
      discounts: [{coupon: 'item-coupon'}, {promotion_code: 'item-promo'}],
    },
  ],
  discounts: [{coupon: 'sub-coupon'}, {promotion_code: 'sub-promo'}],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "customer": "{{CUSTOMER_ID}}",
  "items": [
    {
      "price": "{{PRICE_ID}}",
      "discounts": [{"coupon": "item-coupon"}, {"promotion_code": "item-promo"}],
    },
  ],
  "discounts": [{"coupon": "sub-coupon"}, {"promotion_code": "sub-promo"}],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'items' => [
    [
      'price' => '{{PRICE_ID}}',
      'discounts' => [['coupon' => 'item-coupon'], ['promotion_code' => 'item-promo']],
    ],
  ],
  'discounts' => [['coupon' => 'sub-coupon'], ['promotion_code' => 'sub-promo']],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionCreateParams params =
  SubscriptionCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addItem(
      SubscriptionCreateParams.Item.builder()
        .setPrice("{{PRICE_ID}}")
        .addDiscount(
          SubscriptionCreateParams.Item.Discount.builder()
            .setCoupon("item-coupon")
            .build()
        )
        .addDiscount(
          SubscriptionCreateParams.Item.Discount.builder()
            .setPromotionCode("item-promo")
            .build()
        )
        .build()
    )
    .addDiscount(
      SubscriptionCreateParams.Discount.builder().setCoupon("sub-coupon").build()
    )
    .addDiscount(
      SubscriptionCreateParams.Discount.builder().setPromotionCode("sub-promo").build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Subscription subscription = client.v1().subscriptions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  items: [
    {
      price: '{{PRICE_ID}}',
      discounts: [
        {
          coupon: 'item-coupon',
        },
        {
          promotion_code: 'item-promo',
        },
      ],
    },
  ],
  discounts: [
    {
      coupon: 'sub-coupon',
    },
    {
      promotion_code: 'sub-promo',
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  Items: []*stripe.SubscriptionCreateItemParams{
    &stripe.SubscriptionCreateItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Discounts: []*stripe.SubscriptionCreateItemDiscountParams{
        &stripe.SubscriptionCreateItemDiscountParams{
          Coupon: stripe.String("item-coupon"),
        },
        &stripe.SubscriptionCreateItemDiscountParams{
          PromotionCode: stripe.String("item-promo"),
        },
      },
    },
  },
  Discounts: []*stripe.SubscriptionCreateDiscountParams{
    &stripe.SubscriptionCreateDiscountParams{Coupon: stripe.String("sub-coupon")},
    &stripe.SubscriptionCreateDiscountParams{PromotionCode: stripe.String("sub-promo")},
  },
}
result, err := sc.V1Subscriptions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    Items = new List<SubscriptionItemOptions>
    {
        new SubscriptionItemOptions
        {
            Price = "{{PRICE_ID}}",
            Discounts = new List<SubscriptionItemDiscountOptions>
            {
                new SubscriptionItemDiscountOptions { Coupon = "item-coupon" },
                new SubscriptionItemDiscountOptions { PromotionCode = "item-promo" },
            },
        },
    },
    Discounts = new List<SubscriptionDiscountOptions>
    {
        new SubscriptionDiscountOptions { Coupon = "sub-coupon" },
        new SubscriptionDiscountOptions { PromotionCode = "sub-promo" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

### Restrictions 

There are some restrictions to using multiple discounts.

- You can set up to 20 entries in the `discounts` parameter.
- Each entry in `discounts` has to be unique.
- You can not pass in a coupon and a promotion code created from the same coupon.
- You can not pass in a coupon and a discount that is generated from the same coupon.
- Redeemed discounts must already be attached to the customer or subscription that you?™re updating.

### Update a subscription

You don?™t need to set `discounts` if you don?™t intend to make changes to existing discounts.

When updating `discounts`, you need to pass in any previously set `coupon`, `promotion_code` or `discount` you want to keep on the subscription.

Pass `discounts = ""` to clear all discounts from the subscription. When a subscription has no discounts, the customer-level discount, if any, applies to invoices.

If you have already set more than one discount on a subscription with the new `discounts` parameter, you can not update the subscription with the deprecated `coupon` or `promotion_code` parameter. Similarly, you can not update a schedule?™s phases with the deprecated `coupon` or `promotion_code` parameter if you have set more than one discount on a prior phase.

Updating `discounts` doesn?™t incur prorations or generate an invoice on its own. The new discounts are applied the next time the subscription creates an invoice.

## Alternative discount methods 

Although coupons are the most common way to discount a subscription, you can also do the following:

- Add a negative [customer balance](https://docs.stripe.com/api.md#customer_object-balance) to the customer.
- Add negative [invoice items](https://docs.stripe.com/billing/invoices/subscription.md#adding-draft-invoice-items).
- Add a [second price](https://docs.stripe.com/products-prices/manage-prices.md#create-price) that is a cheaper version of a product?™s usual price.

Of these methods, negative invoice items provide more detailed information as to what discount was created, when, and why.

## See also

- [Changing subscriptions](https://docs.stripe.com/billing/subscriptions/change.md)
- [Working with invoices](https://docs.stripe.com/billing/invoices/subscription.md)
- [Coupons API](https://docs.stripe.com/api.md#coupons)
- [Promotion codes API](https://docs.stripe.com/api.md#promotion_codes)
